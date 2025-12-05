import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

//panel utama untuk halaman menu setelah user login
public class MainMenuPanel extends BaseGamePanel {
    
    private RengasdengklokGame mainApp; //referensi ke aplikasi utama

    //komponen visual yang tampil di menu
    private MenuButton storyButton, quizButton, leaderboardButton;
    private IconButton logoutButton;
    private GreetingPill greetingPill;

    //constructor dipanggil pas panel menu dibuat
    public MainMenuPanel(RengasdengklokGame mainApp) {
        super(); // Panggil constructor BaseGamePanel (otomatis bikin noise & background)
        this.mainApp = mainApp;
        initComponents();
    }

    //dipanggil setiap kali panel dibuka untuk update sapaan user
    public void onPanelShown() {
        updateGreeting();
    }

    //atur posisi semua komponen menu ke layar
    private void initComponents() {

        //sapaan user di kiri atas
        greetingPill = new GreetingPill("Selamat datang, Pejuang");
        greetingPill.setBounds(32, 32, 280, 48);
        add(greetingPill);

        //tombol logout di kanan atas
        int iconBtnSize = 56;
        int iconBtnY = 32;
        int iconBtnRightMargin = 32;

        logoutButton = new IconButton(IconButtonType.LOGOUT);
        logoutButton.setBounds(
            WINDOW_WIDTH - iconBtnRightMargin - iconBtnSize,
            iconBtnY,
            iconBtnSize,
            iconBtnSize
        );
        logoutButton.addActionListener(e -> mainApp.logout());
        add(logoutButton);

        //divider ornamen atas judul
        OrnamentalDivider dividerTop = new OrnamentalDivider();
        dividerTop.setBounds((WINDOW_WIDTH - 280) / 2, 130, 280, 8);
        add(dividerTop);

        //judul besar game
        JLabel titleLabel = new JLabel("Rengasdengklok", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Georgia", Font.BOLD, 72));
        titleLabel.setForeground(ColorPalette.CHINA_DOLL);
        titleLabel.setBounds(0, 140, WINDOW_WIDTH, 90);
        add(titleLabel);

        //divider ornamen bawah judul
        OrnamentalDivider dividerBottom = new OrnamentalDivider();
        dividerBottom.setBounds((WINDOW_WIDTH - 50) / 2, 238, 50, 8);
        add(dividerBottom);

        //subjudul kecil di bawah judul utama
        JLabel subtitleLabel = new JLabel("Jangan Salah Culik!", SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("Georgia", Font.ITALIC, 22));
        subtitleLabel.setForeground(ColorPalette.ROSEWATER);
        subtitleLabel.setBounds(0, 255, WINDOW_WIDTH, 35);
        add(subtitleLabel);

        //ukuran tombol menu utama
        int btnWidth = 588;
        int btnHeight = 78;

        //posisi tombol di tengah layar
        int btnX = (WINDOW_WIDTH - btnWidth) / 2;
        int btnY = 340;
        int btnGap = 20; //jarak antar tombol

        //tombol mode cerita
        storyButton = new MenuButton(
            "Mode Cerita",
            "Ikuti perjalanan proklamasi kemerdekaan",
            MenuIconType.BOOK
        );
        storyButton.setBounds(btnX, btnY, btnWidth, btnHeight);
        storyButton.addActionListener(e -> mainApp.startGame());
        add(storyButton);

        //geser posisi Y ke tombol selanjutnya
        btnY += btnHeight + btnGap;

        //tombol kuis
        quizButton = new MenuButton(
            "Kuis Uji Pemahaman",
            "Uji pengetahuan sejarah Anda",
            MenuIconType.QUIZ
        );
        quizButton.setBounds(btnX, btnY, btnWidth, btnHeight);
        quizButton.addActionListener(e -> mainApp.showQuizPanel());
        add(quizButton);

        //geser lagi ke bawah
        btnY += btnHeight + btnGap;

        //tombol leaderboard pemain
        leaderboardButton = new MenuButton(
            "Leaderboard",
            "Lihat peringkat pemain terbaik",
            MenuIconType.TROPHY
        );
        leaderboardButton.setBounds(btnX, btnY, btnWidth, btnHeight);
        leaderboardButton.addActionListener(e -> mainApp.showLeaderboardPanel());
        add(leaderboardButton);

        //footer tulisan di bawah (sama seperti login panel)
        FooterPanel footer = new FooterPanel();
        footer.setBounds(0, WINDOW_HEIGHT - 70, WINDOW_WIDTH, 30);
        add(footer);
    }

        //update sapaan user sesuai data profile dari database
    private void updateGreeting() {
        if (mainApp.getCurrentUser() != null && mainApp.getCurrentProfile() != null) {

            //ambil nama dan gender karakter dari database
            String name = mainApp.getCurrentProfile().getCharacterName();
            String gender = mainApp.getCurrentProfile().getGender();

            //ubah kata panggil berdasarkan gender
            String honorific = "Bung";
            if (gender != null && (gender.equalsIgnoreCase("Female") || gender.equalsIgnoreCase("Perempuan"))) {
                honorific = "Sus";
            }

            //tampilkan sapaan lengkap
            greetingPill.setText("Selamat datang, " + honorific + " " + name);
        } else {
            //fallback kalau profile tidak ditemukan
            greetingPill.setText("Selamat datang, Pejuang");
        }

        greetingPill.repaint(); //refresh tampilannya
    }

    //enum tipe icon tombol atas
    enum IconButtonType { LOGOUT }

    //enum tipe icon untuk tombol menu utama
    enum MenuIconType { BOOK, QUIZ, TROPHY }

    //komponen sapaan bentuk "pill" transparan di kiri atas
    class GreetingPill extends JLabel {

        public GreetingPill(String text) {
            super(text, SwingConstants.CENTER);
            setFont(new Font("SansSerif", Font.PLAIN, 14));
            setForeground(ColorPalette.CHINA_DOLL);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            //background warna cream tipis transparan
            g2d.setColor(new Color(224, 203, 185, 40));
            g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 44, 44);

            //border warna cream sedikit lebih tebal
            g2d.setColor(new Color(224, 203, 185, 100));
            g2d.setStroke(new BasicStroke(1.5f));
            g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 44, 44);

            super.paintComponent(g);
        }
    }

    //tombol icon bundar (dipakai untuk logout)
    class IconButton extends JButton {

        private IconButtonType type;
        private boolean isHovered = false;

        public IconButton(IconButtonType type) {
            this.type = type;

            //hilangkan efek default tombol
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));

            //kasih event hover
            addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    isHovered = true; repaint();
                }
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    isHovered = false; repaint();
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth();
            int h = getHeight();

            //background bulat transparan
            g2d.setColor(new Color(224, 203, 185, isHovered ? 60 : 40));
            g2d.fillOval(0, 0, w, h);

            //border bulat
            g2d.setColor(new Color(224, 203, 185, 100));
            g2d.setStroke(new BasicStroke(1.5f));
            g2d.drawOval(1, 1, w - 3, h - 3);

            //gambar icon logout
            g2d.setColor(ColorPalette.CHINA_DOLL);
            g2d.setStroke(new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

            int cx = w / 2;
            int cy = h / 2;

            if (type == IconButtonType.LOGOUT) {
                //kotak pintu
                g2d.drawRoundRect(cx - 6, cy - 8, 12, 16, 3, 3);

                //panah keluar
                int arrowX = cx + 4;
                g2d.drawLine(arrowX - 8, cy, arrowX + 6, cy);
                g2d.drawLine(arrowX + 2, cy - 4, arrowX + 6, cy);
                g2d.drawLine(arrowX + 2, cy + 4, arrowX + 6, cy);
            }
        }
    }

        //tombol menu utama (mode cerita, kuis, leaderboard)
    class MenuButton extends JButton {

        private String title;        //judul tombol
        private String subtitle;     //deskripsi kecil
        private MenuIconType iconType; 
        private boolean isHovered = false;

        public MenuButton(String title, String subtitle, MenuIconType iconType) {
            this.title = title;
            this.subtitle = subtitle;
            this.iconType = iconType;

            //hapus efek default tombol
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));

            //kasih event hover
            addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) { 
                    isHovered = true; repaint(); 
                }
                public void mouseExited(MouseEvent e) { 
                    isHovered = false; repaint(); 
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth();
            int h = getHeight();

            //background gradient tombol
            if (isHovered) {
                //warna lebih terang saat hover
                GradientPaint gp = new GradientPaint(
                    0, 0, new Color(137, 95, 97),
                    w, h, new Color(188, 126, 121)
                );
                g2d.setPaint(gp);
            } else {
                //warna default lebih gelap
                GradientPaint gp = new GradientPaint(
                    0, 0, new Color(127, 85, 87),
                    w, h, new Color(178, 116, 111)
                );
                g2d.setPaint(gp);
            }
            g2d.fillRoundRect(0, 0, w, h, 16, 16);

            //efek kilau di bagian atas tombol
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.15f));
            GradientPaint shine = new GradientPaint(0, 0, Color.WHITE, 0, h / 3, new Color(255, 255, 255, 0));
            g2d.setPaint(shine);
            g2d.fillRoundRect(1, 1, w - 2, h / 2, 16, 16);
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));

            //border luar
            g2d.setColor(new Color(224, 203, 185, 200));
            g2d.setStroke(new BasicStroke(2.5f));
            g2d.drawRoundRect(2, 2, w - 5, h - 5, 16, 16);

            //kotak icon kiri
            int iconBoxSize = 48;
            int iconBoxX = 24;
            int iconBoxY = (h - iconBoxSize) / 2;

            g2d.setColor(new Color(224, 203, 185, 100));
            g2d.fillRoundRect(iconBoxX, iconBoxY, iconBoxSize, iconBoxSize, 12, 12);

            g2d.setColor(new Color(224, 203, 185, 140));
            g2d.setStroke(new BasicStroke(1.5f));
            g2d.drawRoundRect(iconBoxX, iconBoxY, iconBoxSize, iconBoxSize, 12, 12);

            //gambar icon berdasarkan tipe menu
            g2d.setColor(ColorPalette.CHINA_DOLL);
            g2d.setStroke(new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            int icx = iconBoxX + iconBoxSize / 2;
            int icy = iconBoxY + iconBoxSize / 2;

            if (iconType == MenuIconType.BOOK) {
                //icon buku
                g2d.drawRect(icx - 10, icy - 7, 20, 14);
                g2d.drawLine(icx, icy - 7, icx, icy + 7);
                g2d.drawLine(icx - 6, icy, icx - 2, icy);
                g2d.drawLine(icx + 2, icy, icx + 6, icy);

            } else if (iconType == MenuIconType.QUIZ) {
                //icon tanda tanya
                g2d.setFont(new Font("Georgia", Font.BOLD, 28));
                g2d.drawString("?", icx - 7, icy + 10);

            } else if (iconType == MenuIconType.TROPHY) {
                //icon piala
                g2d.drawArc(icx - 8, icy - 8, 16, 16, 180, 180);
                g2d.drawLine(icx, icy, icx, icy + 8);
                g2d.drawLine(icx - 6, icy + 8, icx + 6, icy + 8);
            }

            //tulis judul tombol
            g2d.setFont(new Font("Georgia", Font.PLAIN, 26));
            g2d.setColor(ColorPalette.CHINA_DOLL);
            g2d.drawString(title, 90, 40);

            //tulis deskripsi tombol
            g2d.setFont(new Font("SansSerif", Font.PLAIN, 13));
            g2d.setColor(new Color(224, 203, 185, 220));
            g2d.drawString(subtitle, 90, 65);

            //panah kanan
            g2d.setColor(ColorPalette.CHINA_DOLL);
            g2d.setFont(new Font("SansSerif", Font.PLAIN, 24));
            g2d.drawString("→", w - 50, h / 2 + 8);
        }
    }

        //garis ornamen dekoratif (dipakai atas dan bawah judul)
    class OrnamentalDivider extends JPanel {

        public OrnamentalDivider() {
            setOpaque(false); //biar background panel transparan
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth();
            int centerX = w / 2;
            int centerY = 4;

            Color lineColor = new Color(168, 106, 101);

            //gradasi garis kiri
            GradientPaint leftGrad = new GradientPaint(
                0, centerY,
                new Color(168, 106, 101, 0),
                centerX - 24, centerY,
                lineColor
            );
            g2d.setPaint(leftGrad);
            g2d.setStroke(new BasicStroke(1.5f));
            g2d.drawLine(0, centerY, centerX - 24, centerY);

            //diamond kecil di tengah
            g2d.setColor(new Color(186, 84, 80));
            int s = 6;
            int[] x = { centerX, centerX + s/2, centerX, centerX - s/2 };
            int[] y = { centerY - s/2, centerY, centerY + s/2, centerY };
            g2d.fillPolygon(x, y, 4);

            //gradasi garis kanan
            GradientPaint rightGrad = new GradientPaint(
                centerX + 24, centerY,
                lineColor,
                w, centerY,
                new Color(168, 106, 101, 0)
            );
            g2d.setPaint(rightGrad);
            g2d.drawLine(centerX + 24, centerY, w, centerY);
        }
    }

    //footer dekoratif di bawah (sama seperti login panel)
    class FooterPanel extends JPanel {

        public FooterPanel() {
            setOpaque(false); //tanpa background
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            int centerX = getWidth() / 2;
            int centerY = getHeight() / 2;

            Color lineColor = new Color(168, 106, 101);

            //gradasi garis kiri
            GradientPaint leftGrad = new GradientPaint(
                centerX - 180, centerY,
                new Color(168, 106, 101, 0),
                centerX - 80, centerY,
                lineColor
            );
            g2d.setPaint(leftGrad);
            g2d.setStroke(new BasicStroke(1.5f));
            g2d.drawLine(centerX - 180, centerY, centerX - 80, centerY);

            //teks footer
            g2d.setFont(new Font("Georgia", Font.PLAIN, 12));
            g2d.setColor(ColorPalette.ROSEWATER);

            String text = "Indonesia Merdeka • 1945";
            FontMetrics fm = g2d.getFontMetrics();
            int textWidth = fm.stringWidth(text);

            g2d.drawString(text, centerX - textWidth / 2, centerY + 5);

            //gradasi garis kanan
            GradientPaint rightGrad = new GradientPaint(
                centerX + 80, centerY,
                lineColor,
                centerX + 180, centerY,
                new Color(168, 106, 101, 0)
            );
            g2d.setPaint(rightGrad);
            g2d.drawLine(centerX + 80, centerY, centerX + 180, centerY);
        }
    }
}