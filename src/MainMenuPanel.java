import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;

public class MainMenuPanel extends JPanel {
    
    private RengasdengklokGame mainApp;
    
    // Komponen visual
    private MenuButton storyButton, quizButton, leaderboardButton;
    private IconButton logoutButton;
    private GreetingPill greetingPill;
    private BufferedImage noiseTexture;
    
    private static final int WINDOW_WIDTH = 1024;
    private static final int WINDOW_HEIGHT = 768;
    
    public MainMenuPanel(RengasdengklokGame mainApp) {
        this.mainApp = mainApp;
        setLayout(null);
        setBackground(ColorPalette.DARK_BG_1);
        setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
        
        generateNoiseTexture();
        initComponents();
    }
    
    public void onPanelShown() {
        updateGreeting();
    }
    
    //--- VISUAL BACKGROUND ---
    private void generateNoiseTexture() {
        noiseTexture = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = noiseTexture.createGraphics();
        java.util.Random rand = new java.util.Random(12345);
        for (int y = 0; y < 100; y++) {
            for (int x = 0; x < 100; x++) {
                int noise = rand.nextInt(80) - 40;
                int gray = 128 + noise;
                gray = Math.max(0, Math.min(255, gray));
                int alpha = 20;
                noiseTexture.setRGB(x, y, new Color(gray, gray, gray, alpha).getRGB());
            }
        }
        g.dispose();
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        int w = getWidth(); int h = getHeight();
        
        Color[] gradColors = { ColorPalette.DARK_BG_1, ColorPalette.DARK_BG_2, ColorPalette.DARK_BG_3 };
        for (int y = 0; y < h; y++) {
            float ratio = (float) y / h;
            Color c1 = (ratio < 0.5f) ? gradColors[0] : gradColors[1];
            Color c2 = (ratio < 0.5f) ? gradColors[1] : gradColors[2];
            float localRatio = (ratio < 0.5f) ? ratio * 2 : (ratio - 0.5f) * 2;
            int r = (int) (c1.getRed() + (c2.getRed() - c1.getRed()) * localRatio);
            int gr = (int) (c1.getGreen() + (c2.getGreen() - c1.getGreen()) * localRatio);
            int b = (int) (c1.getBlue() + (c2.getBlue() - c1.getBlue()) * localRatio);
            g2d.setColor(new Color(r, gr, b));
            g2d.fillRect(0, y, w, 1);
        }
        
        for (int y = 0; y < h; y += 100) {
            for (int x = 0; x < w; x += 100) {
                g2d.drawImage(noiseTexture, x, y, null);
            }
        }
        
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.1f));
        RadialGradientPaint glow = new RadialGradientPaint(w/2, h/2, w/2, new float[]{0f, 1f}, 
            new Color[]{new Color(168, 106, 101, 255), new Color(168, 106, 101, 0)});
        g2d.setPaint(glow);
        g2d.fillOval(0, 0, w, h);
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
    }

    // --- SUSUN KOMPONEN ---
    private void initComponents() {
        
        // 1. Sapaan (Kiri Atas)
        greetingPill = new GreetingPill("Selamat datang, Pejuang");
        greetingPill.setBounds(32, 32, 280, 48);
        add(greetingPill);
        
        // 2. Tombol Bulat Kanan Atas
        int iconBtnSize = 56;
        int iconBtnY = 32;
        int iconBtnRightMargin = 32;
        
        logoutButton = new IconButton(IconButtonType.LOGOUT);
        logoutButton.setBounds(WINDOW_WIDTH - iconBtnRightMargin - iconBtnSize, iconBtnY, iconBtnSize, iconBtnSize);
        logoutButton.addActionListener(e -> mainApp.logout());
        add(logoutButton);
        
        // 3. Judul & Divider
        OrnamentalDivider dividerTop = new OrnamentalDivider();
        dividerTop.setBounds((WINDOW_WIDTH - 280) / 2, 130, 280, 8);
        add(dividerTop);
        
        JLabel titleLabel = new JLabel("Rengasdengklok", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Georgia", Font.BOLD, 48)); // Font size disamakan dengan Login
        titleLabel.setForeground(ColorPalette.CHINA_DOLL);
        titleLabel.setBounds(0, 140, WINDOW_WIDTH, 60);
        add(titleLabel);
        
        OrnamentalDivider dividerBottom = new OrnamentalDivider();
        dividerBottom.setBounds((WINDOW_WIDTH - 50)/2, 210, 50, 8); // Posisi disesuaikan naik
        add(dividerBottom);
        
        JLabel subtitleLabel = new JLabel("Jangan Salah Culik!", SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("Georgia", Font.ITALIC, 18)); // Font size disamakan dengan Login
        subtitleLabel.setForeground(ColorPalette.ROSEWATER);
        subtitleLabel.setBounds(0, 225, WINDOW_WIDTH, 25);
        add(subtitleLabel);
        
        // 4. Tombol Menu Utama
        int btnWidth = 588;
        int btnHeight = 78;
        int btnX = (WINDOW_WIDTH - btnWidth) / 2;
        int btnY = 340;
        int btnGap = 20;
        
        storyButton = new MenuButton("Mode Cerita", "Ikuti perjalanan proklamasi kemerdekaan", MenuIconType.BOOK);
        storyButton.setBounds(btnX, btnY, btnWidth, btnHeight);
        storyButton.addActionListener(e -> mainApp.startGame());
        add(storyButton);
        
        btnY += btnHeight + btnGap;
        
        quizButton = new MenuButton("Kuis Uji Pemahaman", "Uji pengetahuan sejarah Anda", MenuIconType.QUIZ);
        quizButton.setBounds(btnX, btnY, btnWidth, btnHeight);
        quizButton.addActionListener(e -> mainApp.showQuizPanel());
        add(quizButton);
        
        btnY += btnHeight + btnGap;
        
        leaderboardButton = new MenuButton("Leaderboard", "Lihat peringkat pemain terbaik", MenuIconType.TROPHY);
        leaderboardButton.setBounds(btnX, btnY, btnWidth, btnHeight);
        leaderboardButton.addActionListener(e -> mainApp.showLeaderboardPanel());
        add(leaderboardButton);
        
        // 5. Footer (POSISI DISAMAKAN DENGAN LOGIN)
        FooterPanel footer = new FooterPanel();
        footer.setBounds(0, WINDOW_HEIGHT - 70, WINDOW_WIDTH, 30);
        add(footer);
    }
    
    private void updateGreeting() {
        if (mainApp.getCurrentUser() != null && mainApp.getCurrentProfile() != null) {
            String name = mainApp.getCurrentProfile().getCharacterName();
            String gender = mainApp.getCurrentProfile().getGender(); 
            String honorific = "Bung";
            if (gender != null && (gender.equalsIgnoreCase("Female") || gender.equalsIgnoreCase("Perempuan"))) {
                honorific = "Sus";
            }
            greetingPill.setText("Selamat datang, " + honorific + " " + name);
        } else {
            greetingPill.setText("Selamat datang, Pejuang");
        }
        greetingPill.repaint();
    }

    //--- ENUM ---
    enum IconButtonType { LOGOUT } 
    enum MenuIconType { BOOK, QUIZ, TROPHY }
    
    //--- KOMPONEN KUSTOM ---

    // 1. Greeting Pill
    class GreetingPill extends JLabel {
        public GreetingPill(String text) {
            super(text, SwingConstants.CENTER);
            setFont(new Font("SansSerif", Font.PLAIN, 14));
            setForeground(ColorPalette.CHINA_DOLL);
        }
        public void setText(String text) { super.setText(text); }
        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            // Background Cream Transparan (Warna Muda)
            g2d.setColor(new Color(224, 203, 185, 40)); 
            g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 44, 44);
            
            // Border Cream
            g2d.setColor(new Color(224, 203, 185, 100));
            g2d.setStroke(new BasicStroke(1.5f));
            g2d.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 44, 44);
            
            // Menggambar teks secara manual
            g2d.setFont(getFont());
            g2d.setColor(getForeground());
            FontMetrics fm = g2d.getFontMetrics();
            int textX = (getWidth() - fm.stringWidth(getText())) / 2;
            int textY = (getHeight() + fm.getAscent()) / 2 - 2;
            g2d.drawString(getText(), textX, textY);
        }
    }
    
    // 2. Icon Button (Logout)
    class IconButton extends JButton {
        private IconButtonType type;
        private boolean isHovered = false;
        
        public IconButton(IconButtonType type) {
            this.type = type;
            setContentAreaFilled(false); setFocusPainted(false); setBorderPainted(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) { isHovered = true; repaint(); }
                public void mouseExited(java.awt.event.MouseEvent evt) { isHovered = false; repaint(); }
            });
        }
        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            int w = getWidth(); int h = getHeight();
            
            // Background Cream Transparan
            g2d.setColor(new Color(224, 203, 185, isHovered ? 60 : 40));
            g2d.fillOval(0, 0, w, h);
            
            // Border Cream
            g2d.setColor(new Color(224, 203, 185, 100));
            g2d.setStroke(new BasicStroke(1.5f));
            g2d.drawOval(1, 1, w - 3, h - 3);
            
            // Icon Cream
            g2d.setColor(ColorPalette.CHINA_DOLL);
            g2d.setStroke(new BasicStroke(2.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            
            int cx = w/2; int cy = h/2;
            
            if (type == IconButtonType.LOGOUT) {
                g2d.setStroke(new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2d.drawRoundRect(cx-6, cy-8, 12, 16, 3, 3);
                int arrowX = cx + 4;
                g2d.drawLine(arrowX - 8, cy, arrowX + 6, cy);
                g2d.drawLine(arrowX + 2, cy - 4, arrowX + 6, cy);
                g2d.drawLine(arrowX + 2, cy + 4, arrowX + 6, cy);
            }
        }
    }
    
    // 3. Menu Button (Tombol Kaca Besar)
    class MenuButton extends JButton {
        private String title, subtitle;
        private MenuIconType iconType;
        private boolean isHovered = false;
        
        public MenuButton(String title, String subtitle, MenuIconType iconType) {
            this.title = title; this.subtitle = subtitle; this.iconType = iconType;
            setContentAreaFilled(false); setFocusPainted(false); setBorderPainted(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) { isHovered = true; repaint(); }
                public void mouseExited(MouseEvent e) { isHovered = false; repaint(); }
            });
        }
        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            int w = getWidth(); int h = getHeight();
            
            // Background Gradasi Merah/Coklat
            if (isHovered) {
                GradientPaint gp = new GradientPaint(0, 0, new Color(188, 126, 121, 200), w, h, new Color(137, 95, 97, 200));
                g2d.setPaint(gp);
            } else {
                GradientPaint gp = new GradientPaint(0, 0, new Color(168, 106, 101, 150), w, h, new Color(117, 75, 77, 150));
                g2d.setPaint(gp);
            }
            g2d.fillRoundRect(0, 0, w, h, 16, 16);
            
            // Efek Kilau Kaca (Top Shine)
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.2f));
            GradientPaint innerGlow = new GradientPaint(0, 0, Color.WHITE, 0, h/3, new Color(255,255,255,0));
            g2d.setPaint(innerGlow);
            g2d.fillRoundRect(1, 1, w - 2, h/2, 16, 16);
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
            
            // Border Luar
            g2d.setColor(new Color(224, 203, 185, 120));
            g2d.setStroke(new BasicStroke(2f));
            g2d.drawRoundRect(2, 2, w - 5, h - 5, 16, 16);
            
            // Kotak Icon Kiri
            int iconBoxSize = 48; int iconBoxX = 24; int iconBoxY = (h - iconBoxSize) / 2;
            g2d.setColor(new Color(224, 203, 185, 60));
            g2d.fillRoundRect(iconBoxX, iconBoxY, iconBoxSize, iconBoxSize, 12, 12);
            g2d.setColor(new Color(224, 203, 185, 180));
            g2d.setStroke(new BasicStroke(1.5f));
            g2d.drawRoundRect(iconBoxX, iconBoxY, iconBoxSize, iconBoxSize, 12, 12);
            
            //gambar icon 
            g2d.setColor(ColorPalette.CHINA_DOLL);
            g2d.setStroke(new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            int icx = iconBoxX + iconBoxSize/2; int icy = iconBoxY + iconBoxSize/2;
            
            if (iconType == MenuIconType.BOOK) {
                //buku Melengkung
                g2d.drawRect(icx-10, icy-7, 20, 14); g2d.drawLine(icx, icy-7, icx, icy+7); 
                g2d.drawLine(icx-6, icy, icx-2, icy); g2d.drawLine(icx+2, icy, icx+6, icy); 
            } else if (iconType == MenuIconType.QUIZ) {
                //tanda Tanya Serif
                g2d.setFont(new Font("Georgia", Font.BOLD, 32)); g2d.drawString("?", icx-8, icy+10);
            } else if (iconType == MenuIconType.TROPHY) {
                //piala Solid
                g2d.drawArc(icx-8, icy-8, 16, 16, 180, 180); g2d.drawLine(icx, icy, icx, icy+8); g2d.drawLine(icx-6, icy+8, icx+6, icy+8); 
            }
            
            //teks Judul
            g2d.setFont(new Font("Georgia", Font.PLAIN, 26));
            g2d.setColor(ColorPalette.CHINA_DOLL);
            g2d.drawString(title, 90, 40);
            
            //teks Deskripsi
            g2d.setFont(new Font("SansSerif", Font.PLAIN, 13));
            g2d.setColor(new Color(224, 203, 185, 220)); 
            g2d.drawString(subtitle, 90, 65);
            
            //panah Kanan
            g2d.setFont(new Font("SansSerif", Font.PLAIN, 24));
            g2d.setColor(ColorPalette.CHINA_DOLL);
            g2d.drawString("→", w - 50, h/2 + 8);
        }
    }
    
    //divider
    class OrnamentalDivider extends JPanel {
        public OrnamentalDivider() { setOpaque(false); }
        @Override protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int w = getWidth(); int centerX = w / 2; int centerY = 4;
            Color lineColor = new Color(168, 106, 101);
            GradientPaint leftGrad = new GradientPaint(0, centerY, new Color(168, 106, 101, 0), centerX - 24, centerY, lineColor);
            g2d.setPaint(leftGrad); g2d.setStroke(new BasicStroke(1.5f)); g2d.drawLine(0, centerY, centerX - 24, centerY);
            g2d.setColor(new Color(186, 84, 80));
            int s = 6;
            int[] x = {centerX, centerX + s/2, centerX, centerX - s/2};
            int[] y = {centerY - s/2, centerY, centerY + s/2, centerY};
            g2d.fillPolygon(x, y, 4);
            GradientPaint rightGrad = new GradientPaint(centerX + 24, centerY, lineColor, w, centerY, new Color(168, 106, 101, 0));
            g2d.setPaint(rightGrad); g2d.drawLine(centerX + 24, centerY, w, centerY);
        }
    }
    
    //footer 
    class FooterPanel extends JPanel {
        public FooterPanel() { setOpaque(false); }
        @Override protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            int centerX = getWidth() / 2; int centerY = getHeight() / 2;
            Color lineColor = new Color(168, 106, 101);
            GradientPaint leftGrad = new GradientPaint(centerX - 180, centerY, new Color(168, 106, 101, 0), centerX - 80, centerY, lineColor);
            g2d.setPaint(leftGrad); g2d.setStroke(new BasicStroke(1.5f)); g2d.drawLine(centerX - 180, centerY, centerX - 80, centerY);
            g2d.setFont(new Font("Georgia", Font.PLAIN, 12));
            g2d.setColor(ColorPalette.ROSEWATER);
            String text = "Indonesia Merdeka • 1945";
            FontMetrics fm = g2d.getFontMetrics();
            int textWidth = fm.stringWidth(text);
            g2d.drawString(text, centerX - textWidth / 2, centerY + 5);
            GradientPaint rightGrad = new GradientPaint(centerX + 80, centerY, lineColor, centerX + 180, centerY, new Color(168, 106, 101, 0));
            g2d.setPaint(rightGrad); g2d.drawLine(centerX + 80, centerY, centerX + 180, centerY);
        }
    }
}