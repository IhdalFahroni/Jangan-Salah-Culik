import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;

public class MainMenuPanel extends JPanel {
    
    private RengasdengklokGame mainApp;
    
    // Komponen
    private MenuButton storyButton, quizButton, leaderboardButton;
    private CircleButton muteButton, logoutButton;
    private GreetingPill greetingPill;
    private BufferedImage noiseTexture;
    
    // Ukuran
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
    
    // --- VISUAL BACKGROUND (Sama dengan Login) ---
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
        
        // Gradient Background
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
        
        // Noise
        for (int y = 0; y < h; y += 100) {
            for (int x = 0; x < w; x += 100) {
                g2d.drawImage(noiseTexture, x, y, null);
            }
        }
        
        // Glow Effect
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.1f));
        RadialGradientPaint glow = new RadialGradientPaint(w/2, h/2, w/2, new float[]{0f, 1f}, 
            new Color[]{new Color(168, 106, 101, 255), new Color(168, 106, 101, 0)});
        g2d.setPaint(glow);
        g2d.fillOval(0, 0, w, h);
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
    }

    // --- SUSUN KOMPONEN ---
    private void initComponents() {
        
        // 1. Sapaan (Kapsul di Kiri Atas)
        greetingPill = new GreetingPill("Selamat datang, Pejuang");
        greetingPill.setBounds(40, 40, 250, 44);
        add(greetingPill);
        
        // 2. Tombol Bulat (Kanan Atas)
        int topBtnY = 40;
        
        // Tombol Logout (Paling Kanan)
        logoutButton = new CircleButton("↪"); // Icon panah keluar
        logoutButton.setBounds(WINDOW_WIDTH - 40 - 44, topBtnY, 44, 44);
        logoutButton.addActionListener(e -> mainApp.logout());
        add(logoutButton);
        
        // Tombol Mute (Sebelah kirinya)
        muteButton = new CircleButton("🔊");
        muteButton.setBounds(WINDOW_WIDTH - 40 - 44 - 16 - 44, topBtnY, 44, 44);
        muteButton.addActionListener(e -> toggleMute());
        add(muteButton);
        
        // 3. Judul & Ornamen (Tengah)
        // Garis atas + Diamond
        OrnamentalDivider dividerTop = new OrnamentalDivider();
        dividerTop.setBounds((WINDOW_WIDTH - 300)/2, 130, 300, 10);
        add(dividerTop);
        
        JLabel titleLabel = new JLabel("Rengasdengklok", JLabel.CENTER);
        titleLabel.setFont(new Font("Georgia", Font.BOLD, 64));
        titleLabel.setForeground(ColorPalette.CHINA_DOLL);
        titleLabel.setBounds(0, 150, WINDOW_WIDTH, 80);
        add(titleLabel);
        
        // Garis bawah kecil
        OrnamentalDivider dividerBottom = new OrnamentalDivider();
        dividerBottom.setBounds((WINDOW_WIDTH - 50)/2, 235, 50, 10); // Diamond kecil di bawah judul
        add(dividerBottom);
        
        JLabel subtitleLabel = new JLabel("Jangan Salah Culik!", JLabel.CENTER);
        subtitleLabel.setFont(new Font("Georgia", Font.PLAIN, 20));
        subtitleLabel.setForeground(ColorPalette.ROSEWATER);
        subtitleLabel.setBounds(0, 260, WINDOW_WIDTH, 30);
        add(subtitleLabel);
        
        // 4. Tombol Menu Besar (Tengah Bawah)
        int startY = 340;
        int btnHeight = 85;
        int btnWidth = 640;
        int gap = 20;
        int btnX = (WINDOW_WIDTH - btnWidth) / 2;
        
        storyButton = new MenuButton("Mode Cerita", "Ikuti perjalanan proklamasi kemerdekaan", "📖");
        storyButton.setBounds(btnX, startY, btnWidth, btnHeight);
        storyButton.addActionListener(e -> mainApp.startGame());
        add(storyButton);
        
        quizButton = new MenuButton("Kuis Uji Pemahaman", "Uji pengetahuan sejarah Anda", "❓");
        quizButton.setBounds(btnX, startY + btnHeight + gap, btnWidth, btnHeight);
        quizButton.addActionListener(e -> mainApp.showQuizPanel());
        add(quizButton);
        
        leaderboardButton = new MenuButton("Leaderboard", "Lihat peringkat pemain terbaik", "🏆");
        leaderboardButton.setBounds(btnX, startY + (btnHeight + gap) * 2, btnWidth, btnHeight);
        leaderboardButton.addActionListener(e -> mainApp.showLeaderboardPanel());
        add(leaderboardButton);
        
        // 5. Footer
        FooterPanel footer = new FooterPanel();
        footer.setBounds(0, WINDOW_HEIGHT - 60, WINDOW_WIDTH, 40);
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
    
    private void toggleMute() {
        if (muteButton.getText().equals("🔊")) muteButton.setText("🔇");
        else muteButton.setText("🔊");
    }

    // --- KOMPONEN KUSTOM SESUAI DESAIN ---

    // 1. Kapsul Sapaan (Pojok Kiri Atas)
    class GreetingPill extends JLabel {
        public GreetingPill(String text) {
            super(text, SwingConstants.CENTER);
            setFont(new Font("SansSerif", Font.PLAIN, 13));
            setForeground(ColorPalette.ROSEWATER);
        }
        public void setText(String text) { super.setText(text); }
        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            // Background Kapsul Transparan
            g2d.setColor(new Color(180, 140, 130, 60)); 
            g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 44, 44);
            super.paintComponent(g);
        }
    }

    // 2. Tombol Bulat (Pojok Kanan Atas)
    class CircleButton extends JButton {
        public CircleButton(String text) {
            super(text);
            setContentAreaFilled(false); setBorderPainted(false); setFocusPainted(false);
            setFont(new Font("SansSerif", Font.BOLD, 18));
            setForeground(ColorPalette.CHINA_DOLL);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
        }
        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            // Lingkaran Background
            g2d.setColor(new Color(180, 140, 130, 60));
            g2d.fillOval(0, 0, getWidth(), getHeight());
            super.paintComponent(g);
        }
    }
    
    // 3. Tombol Menu Besar (Kartu Kaca)
    class MenuButton extends JButton {
        private String title, subtitle, icon;
        private boolean isHovered = false;
        
        public MenuButton(String title, String subtitle, String icon) {
            this.title = title; this.subtitle = subtitle; this.icon = icon;
            setContentAreaFilled(false); setBorderPainted(false); setFocusPainted(false);
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
            
            // Background (Semi Transparan Merah/Coklat)
            if (isHovered) g2d.setColor(new Color(168, 106, 101, 150)); // Lebih terang pas hover
            else g2d.setColor(new Color(168, 106, 101, 100)); // Normal
            
            g2d.fillRoundRect(0, 0, w, h, 20, 20);
            
            // Border Tipis (Efek Kaca)
            g2d.setColor(new Color(224, 203, 185, 100)); // China Doll pudar
            g2d.setStroke(new BasicStroke(2f));
            g2d.drawRoundRect(1, 1, w-3, h-3, 20, 20);
            
            // Ikon Kotak di Kiri
            int iconSize = 50;
            int iconX = 24;
            int iconY = (h - iconSize) / 2;
            
            g2d.setColor(new Color(0,0,0,30)); // Bayangan kotak ikon
            g2d.fillRoundRect(iconX, iconY, iconSize, iconSize, 12, 12);
            g2d.setColor(new Color(224, 203, 185, 150)); // Border kotak ikon
            g2d.setStroke(new BasicStroke(1.5f));
            g2d.drawRoundRect(iconX, iconY, iconSize, iconSize, 12, 12);
            
            // Gambar Simbol Ikon
            g2d.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));
            g2d.setColor(ColorPalette.CHINA_DOLL);
            g2d.drawString(icon, iconX + 12, iconY + 34);
            
            // Teks Judul
            g2d.setFont(new Font("Georgia", Font.PLAIN, 26));
            g2d.setColor(ColorPalette.CHINA_DOLL);
            g2d.drawString(title, 90, 40);
            
            // Teks Deskripsi
            g2d.setFont(new Font("SansSerif", Font.PLAIN, 13));
            g2d.setColor(new Color(224, 203, 185, 180)); // Agak pudar
            g2d.drawString(subtitle, 90, 65);
            
            // Panah di Kanan ->
            g2d.setFont(new Font("SansSerif", Font.PLAIN, 24));
            g2d.setColor(ColorPalette.CHINA_DOLL);
            g2d.drawString("→", w - 50, h/2 + 8);
        }
    }
    
    // Garis Hiasan
    class OrnamentalDivider extends JPanel {
        public OrnamentalDivider() { setOpaque(false); }
        @Override protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int w = getWidth(); int cx = w/2; int cy = getHeight()/2;
            
            g2d.setColor(new Color(168, 106, 101, 100)); // Garis pudar
            g2d.drawLine(0, cy, w, cy);
            
            g2d.setColor(ColorPalette.COPPER_ROSE); // Wajik tengah
            int s = 5;
            int[] x = {cx, cx+s, cx, cx-s};
            int[] y = {cy-s, cy, cy+s, cy};
            g2d.fillPolygon(x, y, 4);
        }
    }
    
    // Footer
    class FooterPanel extends JPanel {
        public FooterPanel() { setOpaque(false); }
        @Override protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int w = getWidth(); int h = getHeight();
            
            g2d.setColor(new Color(168, 106, 101, 80));
            g2d.drawLine(w/2 - 150, h/2, w/2 - 250, h/2);
            g2d.drawLine(w/2 + 150, h/2, w/2 + 250, h/2);
            
            g2d.setFont(new Font("Georgia", Font.PLAIN, 12));
            g2d.setColor(new Color(168, 106, 101)); // Warna agak gelap dikit
            String text = "Indonesia Merdeka • 1945";
            int tw = g2d.getFontMetrics().stringWidth(text);
            g2d.drawString(text, (w - tw)/2, h/2 + 5);
        }
    }
}