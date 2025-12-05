import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Map;
import java.util.Random;
import javax.imageio.ImageIO;

//kelas untuk panel tampilan game over
public class GameOverPanel extends BaseGamePanel {

    //label judul, subjudul, dan deskripsi ending
    private JLabel endingTitleLabel;
    private JLabel endingSubtitleLabel;
    private JLabel endingDescriptionLabel;

    //komponen statistik akhir
    private StatsBox statsBox;

    //aksi saat player klik replay atau kembali ke menu
    private Runnable onReturnToMenu;
    private Runnable onReplay;

    //texture noise dan background gambar
    private BufferedImage bgImage;

    //data ending yang akan ditampilkan
    private String endingKey = "UNKNOWN";
    private Map<String, Integer> finalStats;

    //konstruktor panel game over
    public GameOverPanel() {
        super(); // Generate noise
        setBackground(ColorPalette.DARK_BG_1);
        loadBackground();
        initComponents();
    }

    //fungsi untuk memuat gambar background scene terakhir
    private void loadBackground() {
        try {
            File imgFile = new File("assets/Background/scene9.png");
            if (imgFile.exists()) {
                bgImage = ImageIO.read(imgFile); //set gambar background
            }
        } catch (Exception e) {
            System.err.println("Gagal memuat background game over: " + e.getMessage());
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();

        //gambar background utama
        if (bgImage != null) {
            g2d.drawImage(bgImage, 0, 0, w, h, null);

            //overlay gelap biar efek kayak modal popup
            GradientPaint darkOverlay = new GradientPaint(
                0, 0, new Color(50, 30, 30, 220),
                0, h, new Color(20, 10, 10, 250)
            );
            g2d.setPaint(darkOverlay);
            g2d.fillRect(0, 0, w, h);

        } else {
            //fallback gradient kalau gambar background ga ketemu
            GradientPaint bgGradient = new GradientPaint(
                0, 0, new Color(60, 40, 40),
                0, h, new Color(30, 20, 20)
            );
            g2d.setPaint(bgGradient);
            g2d.fillRect(0, 0, w, h);
        }

        //gambar noise halus di atas background
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
        drawNoise(g2d, w, h); // <--- Panggil fungsi induk
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));

        //==== gambar kotak modal utama di tengah layar ====

        int mw = 700;   //lebar modal
        int mh = 600;   //tinggi modal
        int mx = (w - mw) / 2; //posisi horizontal center
        int my = (h - mh) / 2; //posisi vertikal center

        //background modal warna plum wine gelap
        g2d.setColor(new Color(40, 25, 25, 240));
        g2d.fillRoundRect(mx, my, mw, mh, 30, 30);

        //border modal warna copper rose sedikit transparan
        g2d.setColor(new Color(168, 106, 101, 80));
        g2d.setStroke(new BasicStroke(1f));
        g2d.drawRoundRect(mx, my, mw, mh, 30, 30);

        //==== dekorasi sudut modal (hiasan garis bentuk L) ====

        int cl = 40; //panjang dekorasi
        int co = 25; //offset jarak dari tepi modal

        g2d.setColor(new Color(140, 90, 85)); //warna copper rose gelap
        g2d.setStroke(new BasicStroke(3f));

        //kiri atas
        g2d.drawPolyline(
            new int[]{mx + co, mx + co, mx + co + cl},
            new int[]{my + co + cl, my + co, my + co},
            3
        );

        //kanan atas
        g2d.drawPolyline(
            new int[]{mx + mw - co - cl, mx + mw - co, mx + mw - co},
            new int[]{my + co, my + co, my + co + cl},
            3
        );

        //kiri bawah
        g2d.drawPolyline(
            new int[]{mx + co, mx + co, mx + co + cl},
            new int[]{my + mh - co - cl, my + mh - co, my + mh - co},
            3
        );

        //kanan bawah
        g2d.drawPolyline(
            new int[]{mx + mw - co - cl, mx + mw - co, mx + mw - co},
            new int[]{my + mh - co, my + mh - co, my + mh - co - cl},
            3
        );
    }

        //fungsi untuk membuat semua komponen dalam modal game over
    private void initComponents() {
        int w = 1024;        // lebar panel
        int h = 768;         // tinggi panel

        int mw = 700;        // lebar modal (sama seperti di paintComponent)
        int mh = 600;        // tinggi modal

        int mx = (w - mw) / 2;   // posisi modal di tengah horizontal
        int modalY = (h - mh) / 2; 

        //ikon jam dekoratif di atas judul
        ClockIcon clock = new ClockIcon();
        int clockSize = 100;
        int clockX = mx + (mw - clockSize) / 2;  // titik tengah modal
        int clockY = modalY + 40;                // naikkan sedikit biar simetris

        clock.setBounds(clockX, clockY, clockSize, clockSize);

        add(clock);

        //label judul ending (besar)
        endingTitleLabel = new JLabel("JUDUL ENDING", SwingConstants.CENTER);
        endingTitleLabel.setFont(new Font("Georgia", Font.BOLD, 42));
        endingTitleLabel.setForeground(ColorPalette.CHINA_DOLL);
        endingTitleLabel.setBounds(0, modalY + 130, w, 50);
        add(endingTitleLabel);

        //label jenis ending (bad ending / good ending / true ending)
        endingSubtitleLabel = new JLabel("BAD ENDING 1", SwingConstants.CENTER);
        endingSubtitleLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        endingSubtitleLabel.setForeground(ColorPalette.COPPER_ROSE);
        endingSubtitleLabel.setBounds(0, modalY + 180, w, 20);
        add(endingSubtitleLabel);

        //pembatas bentuk diamond di tengah
        DiamondDivider divider = new DiamondDivider();
        divider.setBounds((w - 20) / 2, modalY + 210, 20, 20);
        add(divider);

        //deskripsi ending dalam teks html
        endingDescriptionLabel = new JLabel("", SwingConstants.CENTER);
        endingDescriptionLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        endingDescriptionLabel.setForeground(new Color(220, 210, 200));
        endingDescriptionLabel.setBounds((w - 600) / 2, modalY + 240, 600, 80);
        add(endingDescriptionLabel);

        //kotak statistik akhir hubungan player
        statsBox = new StatsBox();
        statsBox.setBounds((w - 500) / 2, modalY + 340, 500, 40);
        add(statsBox);

        //lebar dan tinggi tombol
        int btnW = 320;
        int btnH = 55;
        int btnX = (w - btnW) / 2;

        //tombol untuk replay / mulai ulang cerita
        DarkRoseButton replayBtn = new DarkRoseButton("↻ Mulai Ulang Cerita");
        replayBtn.setBounds(btnX, modalY + 410, btnW, btnH);
        replayBtn.addActionListener(e -> {
            if (onReplay != null) onReplay.run(); //jalankan aksi yang dikirim dari luar
        });
        add(replayBtn);

        //tombol untuk kembali ke menu utama
        DarkRoseButton menuBtn = new DarkRoseButton("⌂ Kembali ke Menu Utama");
        menuBtn.setBounds(btnX, modalY + 480, btnW, btnH);
        menuBtn.addActionListener(e -> {
            if (onReturnToMenu != null) onReturnToMenu.run();
        });
        add(menuBtn);

        //tanggal dekoratif di bagian bawah modal
        JLabel dateLabel = new JLabel("17 Agustus 1945", SwingConstants.CENTER);
        dateLabel.setFont(new Font("Georgia", Font.ITALIC, 12));
        dateLabel.setForeground(new Color(160, 130, 120));
        dateLabel.setBounds(0, modalY + 550, w, 20);
        add(dateLabel);
    }

    //fungsi untuk update data ending berdasarkan key dan relasi player
    public void setEndingInfo(String endingKey, String description, Map<String, Integer> relationships) {
        this.endingKey = endingKey;
        this.finalStats = relationships;

        //default value sebelum dicek jenis ending
        String titleText = "Perjuangan Berakhir";
        String typeText = "ENDING";
        Color titleColor = ColorPalette.CHINA_DOLL;

        //cek apakah ending ini true ending
        if (endingKey.contains("TRUE")) {
            titleText = "Indonesia Merdeka";
            typeText = "TRUE ENDING";
            titleColor = new Color(220, 200, 150);
        }
        //cek apakah good ending
        else if (endingKey.contains("GOOD")) {
            titleText = "Kemerdekaan Tegang";
            typeText = "GOOD ENDING";
            titleColor = new Color(180, 200, 220);
        }
        //kalau bukan keduanya berarti bad ending
        else {
            String[] parts = endingKey.split("_");
            if (parts.length >= 3) {
                //ambil kata dari key untuk dijadikan judul bad ending
                StringBuilder sb = new StringBuilder();
                for (int i = 2; i < parts.length; i++) {
                    sb.append(parts[i]).append(" ");
                }
                titleText = sb.toString().trim();
                titleText = titleText.substring(0, 1).toUpperCase()
                           + titleText.substring(1).toLowerCase();
            }

            typeText = "BAD ENDING";
            titleColor = new Color(220, 150, 150);
        }

        //update label setelah ditentukan jenis ending
        endingTitleLabel.setText(titleText);
        endingTitleLabel.setForeground(titleColor);

        endingSubtitleLabel.setText(typeText);

        //pakai html biar teks bisa center dan wrap
        endingDescriptionLabel.setText(
            "<html><div style='text-align: center;'>" + description + "</div></html>"
        );

        repaint(); //refresh ui
    }

    //setter untuk aksi kembali ke menu
    public void setOnReturnToMenu(Runnable onReturnToMenu) {
        this.onReturnToMenu = onReturnToMenu;
    }

    //setter untuk aksi replay
    public void setOnReplay(Runnable onReplay) {
        this.onReplay = onReplay;
    }

        //kelas untuk menampilkan statistik akhir hubungan (soekarno, hatta, pemuda, trust)
    class StatsBox extends JPanel {

        public StatsBox() {
            setOpaque(false); //panel transparan agar hanya kotaknya yang terlihat
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            //kalau finalStats belum diisi, jangan gambar apa-apa
            if (finalStats == null) return;

            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
            );

            int w = getWidth();
            int h = getHeight();

            //background kotak statistik warna gelap
            g2d.setColor(new Color(50, 35, 35, 200));
            g2d.fillRoundRect(0, 0, w, h, h, h);

            //border kotak warna copper rose
            g2d.setColor(new Color(140, 90, 85));
            g2d.setStroke(new BasicStroke(1f));
            g2d.drawRoundRect(0, 0, w - 1, h - 1, h, h);

            //font untuk teks statistik
            g2d.setFont(new Font("SansSerif", Font.PLAIN, 13));
            g2d.setColor(ColorPalette.CHINA_DOLL);

            //format teks statistik
            String s =
                "Soekarno: " + finalStats.getOrDefault("SOEKARNO", 50) +
                " | Hatta: " + finalStats.getOrDefault("HATTA", 50) +
                " | Pemuda: " + finalStats.getOrDefault("PEMUDA", 50) +
                " | Trust: "  + finalStats.getOrDefault("TRUST", 50);

            //biar teks di tengah kotak
            FontMetrics fm = g2d.getFontMetrics();
            int textX = (w - fm.stringWidth(s)) / 2;
            int textY = (h + fm.getAscent()) / 2 - 2;

            g2d.drawString(s, textX, textY);
        }
    }

        //kelas untuk ikon jam kecil sebagai dekorasi di atas judul ending
    class ClockIcon extends JPanel {
        private Image clockIcon = new ImageIcon("assets/Icon/clock_icon.png").getImage();
        public ClockIcon() {
            setOpaque(false); //biar background panel transparan
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
            );

            int size = Math.min(getWidth(), getHeight());
            g2d.drawImage(clockIcon, 0, 0, size, size, null);
        }
    }

        //kelas untuk divider kecil bentuk diamond yang jadi pemisah antara judul dan deskripsi ending
    class DiamondDivider extends JPanel {

        public DiamondDivider() {
            setOpaque(false); //panel transparan biar cuma bentuk diamondnya aja yang terlihat
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
            );

            int cx = getWidth() / 2;  //titik tengah x panel
            int cy = getHeight() / 2; //titik tengah y panel

            //warna tembaga gelap biar nyatu sama tema UI
            g2d.setColor(new Color(160, 110, 100));

            //empat titik untuk bentuk diamond (ketupat)
            int[] x = {cx, cx + 6, cx, cx - 6};
            int[] y = {cy - 6, cy, cy + 6, cy};

            //gambar diamond
            g2d.fillPolygon(x, y, 4);
        }
    }

        //kelas untuk tombol gaya rose gelap yang dipakai di bagian game over
    class DarkRoseButton extends JButton {

        private boolean hover = false; //status hover buat ganti warna saat cursor di atas tombol

        public DarkRoseButton(String text) {
            super(text);

            setContentAreaFilled(false); //matikan background default tombol swing
            setFocusPainted(false);       //hapus garis fokus biru default
            setBorderPainted(false);      //border digambar manual di paintComponent
            setFont(new Font("SansSerif", Font.BOLD, 16)); //font tebal biar lebih jelas
            setForeground(ColorPalette.CHINA_DOLL);        //warna teks rose muda
            setCursor(new Cursor(Cursor.HAND_CURSOR));     //biar cursor berubah tangan

            //event hover untuk efek glow ringan
            addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent e) {
                    hover = true; //ubah status hover
                    repaint();    //redraw biar warna berubah
                }
                public void mouseExited(java.awt.event.MouseEvent e) {
                    hover = false;
                    repaint();
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
            );

            int w = getWidth();
            int h = getHeight();

            //gradient warnanya berubah sedikit saat hover
            GradientPaint gp;

            if (hover) {
                //warna saat hover: lebih terang sedikit biar keliatan aktif
                gp = new GradientPaint(
                    0, 0, new Color(180, 120, 115, 240),
                    w, h, new Color(160, 100, 95, 240)
                );
            } else {
                //warna normal: rose gelap ke tembaga tua
                gp = new GradientPaint(
                    0, 0, new Color(160, 100, 95, 230),
                    w, h, new Color(140, 80, 75, 230)
                );
            }

            //gambar background tombol
            g2d.setPaint(gp);
            g2d.fillRoundRect(0, 0, w, h, 20, 20);

            //highlight lembut di bagian atas tomboll
            GradientPaint shine = new GradientPaint(
                0, 0, new Color(255, 255, 255, 40),
                0, h / 2, new Color(255, 255, 255, 0)
            );
            g2d.setPaint(shine);
            g2d.fillRoundRect(0, 0, w, h / 2, 20, 20);

            //border luar tombol (plum wine gelap)
            g2d.setColor(new Color(100, 60, 60, 200));
            g2d.setStroke(new BasicStroke(1.5f));
            g2d.drawRoundRect(1, 1, w - 2, h - 2, 20, 20);

            super.paintComponent(g);
        }
    }
}