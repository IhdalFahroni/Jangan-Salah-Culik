import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.util.List;

public class LeaderboardPanel extends JPanel {
    private RengasdengklokGame mainApp;
    private JPanel dataPanel;
    private BufferedImage noiseTexture;

    // Ukuran Window
    private static final int WINDOW_WIDTH = 1024;
    private static final int WINDOW_HEIGHT = 768;

    // Font Setup
    private final Font TITLE_FONT = new Font("Georgia", Font.BOLD, 46);
    private final Font SUBTITLE_FONT = new Font("Georgia", Font.ITALIC, 16);
    private final Font HEADER_FONT = new Font("SansSerif", Font.BOLD, 11);
    private final Font ROW_NAME_FONT = new Font("Georgia", Font.BOLD, 14);
    private final Font ROW_DETAIL_FONT = new Font("SansSerif", Font.PLAIN, 12);

    private final int CONTAINER_WIDTH = 960; 
    private final int[] COL_WIDTHS = { 70, 230, 300, 120, 100, 140 };
    
    public LeaderboardPanel(RengasdengklokGame mainApp) {
        this.mainApp = mainApp;
        
        setLayout(null);
        setBackground(ColorPalette.DARK_BG_1);
        setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));

        generateNoiseTexture(); 
        initializeUI();
    }

    public void onPanelShown() {
        loadLeaderboard();
    }

    //GRAPHICS & BACKGROUND 
    private void generateNoiseTexture() {
        noiseTexture = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = noiseTexture.createGraphics();
        java.util.Random rand = new java.util.Random(12345);
        for (int y = 0; y < 100; y++) {
            for (int x = 0; x < 100; x++) {
                int noise = rand.nextInt(80) - 40;
                int gray = 128 + noise;
                gray = Math.max(0, Math.min(255, gray));
                noiseTexture.setRGB(x, y, new Color(gray, gray, gray, 15).getRGB());
            }
        }
        g.dispose();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Gradient Background
        Color[] gradColors = {ColorPalette.DARK_BG_1, ColorPalette.DARK_BG_2, ColorPalette.DARK_BG_3};
        GradientPaint gp = new GradientPaint(0, 0, gradColors[0], 0, getHeight(), gradColors[2]);
        g2d.setPaint(gp);
        g2d.fillRect(0, 0, getWidth(), getHeight());

        // Noise Texture
        for (int y = 0; y < getHeight(); y += 100) {
            for (int x = 0; x < getWidth(); x += 100) {
                g2d.drawImage(noiseTexture, x, y, null);
            }
        }
        
        // Vignette
        RadialGradientPaint vignette = new RadialGradientPaint(
            getWidth()/2, getHeight()/2, getWidth(),
            new float[]{0.0f, 1.0f},
            new Color[]{new Color(0,0,0,0), new Color(0,0,0,80)}
        );
        g2d.setPaint(vignette);
        g2d.fillRect(0,0,getWidth(), getHeight());
    }

    //UI COMPONENTS 
    private void initializeUI() {
        // 1. Tombol Kembali
        BackButton backBtn = new BackButton("← KEMBALI");
        backBtn.setBounds(32, 32, 120, 40);
        backBtn.addActionListener(e -> mainApp.showPanel("MAIN_MENU"));
        add(backBtn);

        // 2. Judul
        JLabel titleLabel = new JLabel("Leaderboard", SwingConstants.CENTER);
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(ColorPalette.CHINA_DOLL);
        titleLabel.setBounds(0, 35, WINDOW_WIDTH, 60);
        add(titleLabel);

        // Divider
        OrnamentalDivider divider = new OrnamentalDivider();
        divider.setBounds((WINDOW_WIDTH - 300) / 2, 95, 300, 10);
        add(divider);

        JLabel subtitleLabel = new JLabel("Peringkat 10 Pemain Terbaik", SwingConstants.CENTER);
        subtitleLabel.setFont(SUBTITLE_FONT);
        subtitleLabel.setForeground(ColorPalette.ROSEWATER);
        subtitleLabel.setBounds(0, 110, WINDOW_WIDTH, 25);
        add(subtitleLabel);

        // 3. Table Container (Glass Panel)
        // Tinggi 560px cukup untuk 10 baris @50px + Header 40px + Margin
        int containerH = 560; 
        int containerX = (WINDOW_WIDTH - CONTAINER_WIDTH) / 2; // Posisi X agar pas tengah
        int containerY = 150; 

        JPanel tableContainer = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Background Transparan
                g2d.setColor(new Color(40, 30, 30, 160)); 
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 24, 24);
                
                // Border
                g2d.setColor(new Color(168, 106, 101, 80));
                g2d.setStroke(new BasicStroke(1f));
                g2d.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 24, 24);
            }
        };
        tableContainer.setLayout(null);
        tableContainer.setOpaque(false);
        tableContainer.setBounds(containerX, containerY, CONTAINER_WIDTH, containerH);
        add(tableContainer);

        // 4. Table Header (Fixed at Top)
        JPanel headerRow = new JPanel();
        headerRow.setLayout(new BoxLayout(headerRow, BoxLayout.X_AXIS));
        headerRow.setOpaque(false);
        // Set ukuran header SAMA PERSIS dengan lebar container
        headerRow.setBounds(0, 10, CONTAINER_WIDTH, 40); 
        
        // Padding kiri sedikit agar teks tidak mepet border container
        headerRow.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(216, 166, 148, 50)),
            BorderFactory.createEmptyBorder(0, 20, 0, 20) // Kiri kanan 20px
        ));
        
        headerRow.add(createHeaderLabel("RANK", COL_WIDTHS[0]));
        headerRow.add(createHeaderLabel("NAMA PEMAIN", COL_WIDTHS[1]));
        headerRow.add(createHeaderLabel("GELAR KEHORMATAN", COL_WIDTHS[2]));
        headerRow.add(createHeaderLabel("SKOR", COL_WIDTHS[3]));
        headerRow.add(createHeaderLabel("WAKTU", COL_WIDTHS[4]));
        headerRow.add(createHeaderLabel("TANGGAL", COL_WIDTHS[5]));
        
        tableContainer.add(headerRow);

        // 5. Data Rows
        dataPanel = new JPanel();
        dataPanel.setLayout(new GridLayout(10, 1, 0, 4)); // 10 Baris fix
        dataPanel.setOpaque(false);
        // Bounds disesuaikan: x=0, lebar=CONTAINER_WIDTH agar full
        dataPanel.setBounds(0, 55, CONTAINER_WIDTH, 490); 
        
        // Beri padding internal ke panel data agar kontennya lurus sama header
        dataPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));

        tableContainer.add(dataPanel);
        
        // 6. Footer Text
        FooterPanel footer = new FooterPanel();
        footer.setBounds(0, WINDOW_HEIGHT - 35, WINDOW_WIDTH, 30);
        add(footer);
    }

    private JLabel createHeaderLabel(String text, int width) {
        JLabel lbl = new JLabel(text.toUpperCase());
        lbl.setFont(HEADER_FONT);
        lbl.setForeground(new Color(216, 166, 148, 120));
        // Pakai PreferredSize Fix agar kolom tidak melar/menciut sembarangan
        lbl.setPreferredSize(new Dimension(width, 30));
        lbl.setMinimumSize(new Dimension(width, 30));
        lbl.setMaximumSize(new Dimension(width, 30));
        return lbl;
    }

    //DATA LOADING 
    private void loadLeaderboard() {
        dataPanel.removeAll();
        List<ScoreEntry> scores = mainApp.getDbManager().getTopScores(10);
        
        for (int i = 0; i < 10; i++) {
            JPanel row;
            if (i < scores.size()) {
                row = createDataRow(i + 1, scores.get(i));
            } else {
                row = createEmptyRow(i + 1);
            }
            dataPanel.add(row);
        }

        dataPanel.revalidate();
        dataPanel.repaint();
    }

    private JPanel createDataRow(int rank, ScoreEntry s) {
        JPanel row = new JPanel();
        row.setLayout(new BoxLayout(row, BoxLayout.X_AXIS));
        row.setOpaque(false); 
        
        // Custom painting background per baris
        JPanel content = new JPanel() {
             @Override
            protected void paintComponent(Graphics g) {
                if (isOpaque()) {
                    g.setColor(getBackground());
                    g.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                }
            }
        };
        content.setLayout(new BoxLayout(content, BoxLayout.X_AXIS));
        
        // Logic warna background belang
        if (rank % 2 == 0) {
            content.setBackground(new Color(255, 255, 255, 10)); // Putih transparan
            content.setOpaque(true);
        } else {
            content.setBackground(new Color(0,0,0,0));
            content.setOpaque(false);
        }

        // 1. Rank Icon
        JPanel rankWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 8));
        rankWrapper.setOpaque(false);
        setFixedSize(rankWrapper, COL_WIDTHS[0]);
        
        RankIconPanel icon = new RankIconPanel(rank);
        icon.setPreferredSize(new Dimension(32, 32));
        rankWrapper.add(icon);
        content.add(rankWrapper);

        // 2. Nama
        content.add(createDataLabel(s.getUsername(), COL_WIDTHS[1], ROW_NAME_FONT, ColorPalette.CHINA_DOLL));

        // 3. Gelar
        content.add(createDataLabel("🏆 " + getGelarForRank(rank), COL_WIDTHS[2], ROW_DETAIL_FONT, ColorPalette.DUSTY_ROSE));

        // 4. Skor
        JLabel scoreLbl = createDataLabel(String.valueOf(s.getScore()), COL_WIDTHS[3], ROW_NAME_FONT, ColorPalette.CHINA_DOLL);
        content.add(scoreLbl);

        // 5. Waktu
        String timeStr = String.format("%d:%02d", s.getTimeTaken() / 60, s.getTimeTaken() % 60);
        content.add(createDataLabel(timeStr, COL_WIDTHS[4], ROW_DETAIL_FONT, ColorPalette.ROSEWATER));

        // 6. Tanggal
        String dateStr = (s.getAttemptedAt() != null) ? new java.text.SimpleDateFormat("dd/MM/yy").format(s.getAttemptedAt()) : "-";
        content.add(createDataLabel(dateStr, COL_WIDTHS[5], ROW_DETAIL_FONT, new Color(216, 166, 148, 100)));

        row.add(content);
        return row;
    }

    private JPanel createEmptyRow(int rank) {
        JPanel row = new JPanel();
        row.setLayout(new BoxLayout(row, BoxLayout.X_AXIS));
        row.setOpaque(false);

        JPanel rankWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 8));
        rankWrapper.setOpaque(false);
        setFixedSize(rankWrapper, COL_WIDTHS[0]);
        
        RankIconPanel icon = new RankIconPanel(rank);
        icon.setPreferredSize(new Dimension(32, 32));
        rankWrapper.add(icon);
        row.add(rankWrapper);
        
        JLabel dash = new JLabel("-");
        dash.setFont(ROW_DETAIL_FONT);
        dash.setForeground(new Color(255, 255, 255, 30));
        row.add(dash);

        return row;
    }

    // Helper untuk memaksa ukuran komponen
    private void setFixedSize(JComponent c, int width) {
        Dimension d = new Dimension(width, 50);
        c.setPreferredSize(d);
        c.setMinimumSize(d);
        c.setMaximumSize(d);
    }

    private JLabel createDataLabel(String text, int width, Font font, Color color) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(font);
        lbl.setForeground(color);
        // Pakai setFixedSize logic manual disini
        Dimension d = new Dimension(width, 40);
        lbl.setPreferredSize(d);
        lbl.setMinimumSize(d);
        lbl.setMaximumSize(d);
        return lbl;
    }

    private String getGelarForRank(int r) {
        return switch (r) {
            case 1 -> "Sang Proklamator";
            case 2 -> "Pahlawan Rengasdengklok";
            case 3 -> "Pembela Republik";
            case 4 -> "Aktivis Pergerakan";
            case 5 -> "Simpatisan Kemerdekaan";
            case 6 -> "Pemuda Revolusioner";
            case 7 -> "Pejuang Muda";
            case 8 -> "Patriot Sejati";
            case 9 -> "Pemerhati Sejarah";
            case 10 -> "Pewaris Semangat";
            default -> "-";
        };
    }

    //HELPER CLASSES

    class BackButton extends JButton {
        public BackButton(String text) {
            super(text);
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);
            setForeground(ColorPalette.PLUM_WINE);
            setFont(new Font("SansSerif", Font.BOLD, 12));
            setCursor(new Cursor(Cursor.HAND_CURSOR));
        }
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setColor(new Color(216, 166, 148, 200)); 
            if (getModel().isRollover()) g2d.setColor(ColorPalette.CHINA_DOLL);
            g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 40, 40);
            super.paintComponent(g);
        }
    }

    class OrnamentalDivider extends JPanel {
        public OrnamentalDivider() { setOpaque(false); }
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int w = getWidth(); int cy = getHeight()/2;
            Color lineColor = new Color(168, 106, 101);

            GradientPaint leftGrad = new GradientPaint(0, cy, new Color(168, 106, 101, 0), w/2 - 15, cy, lineColor);
            g2d.setPaint(leftGrad); g2d.setStroke(new BasicStroke(1.5f)); g2d.drawLine(0, cy, w/2 - 15, cy);

            g2d.setColor(new Color(186, 84, 80));
            int s = 6;
            int[] x = { w/2, w/2 + s, w/2, w/2 - s };
            int[] y = { cy - s, cy, cy + s, cy };
            g2d.fillPolygon(x, y, 4);

            GradientPaint rightGrad = new GradientPaint(w/2 + 15, cy, lineColor, w, cy, new Color(168, 106, 101, 0));
            g2d.setPaint(rightGrad); g2d.drawLine(w/2 + 15, cy, w, cy);
        }
    }

    class RankIconPanel extends JPanel {
        int rank;
        public RankIconPanel(int rank) { this.rank = rank; setOpaque(false); }
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int d = 32; 
            Color circleColor; String symbol = String.valueOf(rank); Color textColor = Color.WHITE;

            if (rank == 1) { circleColor = new Color(255, 193, 7); symbol = "♔"; textColor = new Color(120, 80, 0); }
            else if (rank == 2) { circleColor = new Color(192, 192, 192); symbol = "♕"; textColor = Color.DARK_GRAY; }
            else if (rank == 3) { circleColor = new Color(205, 127, 50); symbol = "V"; }
            else { 
                circleColor = new Color(255, 255, 255, 20); textColor = new Color(255, 255, 255, 150);
                g2d.setColor(new Color(255, 255, 255, 40)); g2d.setStroke(new BasicStroke(1f)); g2d.drawOval(0, 0, d-1, d-1);
            }
            if (rank <= 3) { g2d.setColor(circleColor); g2d.fillOval(0, 0, d, d); }
            else { g2d.setColor(circleColor); g2d.fillOval(0, 0, d, d); }

            g2d.setColor(textColor); g2d.setFont(new Font("SansSerif", Font.BOLD, rank <= 3 ? 18 : 12));
            FontMetrics fm = g2d.getFontMetrics();
            g2d.drawString(symbol, (d - fm.stringWidth(symbol)) / 2, ((d - fm.getHeight()) / 2) + fm.getAscent() - (rank==1?2:0));
        }
    }

    class FooterPanel extends JPanel {
        public FooterPanel() { setOpaque(false); }
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int cx = getWidth()/2; int cy = 15;
            GradientPaint l = new GradientPaint(cx-120, cy, new Color(168,106,101,0), cx-60, cy, ColorPalette.COPPER_ROSE);
            g2d.setPaint(l); g2d.drawLine(cx-120, cy, cx-60, cy);
            g2d.setColor(ColorPalette.ROSEWATER); g2d.setFont(new Font("Georgia", Font.PLAIN, 12));
            String txt = "Indonesia Merdeka • 1945";
            g2d.drawString(txt, cx - g2d.getFontMetrics().stringWidth(txt)/2, cy+4);
            GradientPaint r = new GradientPaint(cx+60, cy, ColorPalette.COPPER_ROSE, cx+120, cy, new Color(168,106,101,0));
            g2d.setPaint(r); g2d.drawLine(cx+60, cy, cx+120, cy);
        }
    }
}