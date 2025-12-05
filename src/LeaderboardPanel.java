import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.util.List;

public class LeaderboardPanel extends JPanel {
    private RengasdengklokGame mainApp;
    private JPanel dataPanel;
    private JButton backButton;
    
    // Warna dari MainMenuPanel
    private final Color DARK_BG_1 = new Color(0x30, 0x2A, 0x28);
    private final Color DARK_BG_3 = new Color(0x42, 0x38, 0x36);
    private final Color CHINA_DOLL = new Color(0xE0, 0xCB, 0xB9);
    private final Color ROSEWATER = new Color(0xF2, 0xD5, 0xC9);
    private final Color ACCENT_RED = new Color(0xA8, 0x6A, 0x65);
    
    // Warna tabel 
    private final Color TABLE_BG = new Color(0x3A, 0x34, 0x32);
    private final Color ROW_EVEN = new Color(0x40, 0x3A, 0x38);
    private final Color ROW_ODD = new Color(0x3A, 0x34, 0x32);
    private final Color ROW_HOVER = new Color(0x4A, 0x44, 0x42);
    private final Color TEXT_LIGHT = new Color(0xE0, 0xCB, 0xB9);
    private final Color TEXT_SOFT = new Color(0xD0, 0xBB, 0xA9);
    
    // Warna untuk top 3
    private final Color GOLD_COLOR = new Color(0xFF, 0xE8, 0x6E); // Kuning
    private final Color SILVER_COLOR = new Color(0xD1, 0xD5, 0xDB); // Silver 
    private final Color BRONZE_COLOR = new Color(0xCD, 0x95, 0x6E); // Bronze 
    
    // Warna latar top 3 yang sesuai tema
    private final Color GOLD_BG = new Color(0x4A, 0x3C, 0x20, 80);
    private final Color SILVER_BG = new Color(0x3C, 0x3C, 0x3C, 80);
    private final Color BRONZE_BG = new Color(0x3C, 0x28, 0x20, 80);
    
    // Fonts
    private final Font SUBTITLE_FONT = new Font("Georgia", Font.ITALIC, 18);
    private final Font HEADER_FONT = new Font("Georgia", Font.BOLD, 14);
    private final Font DATA_FONT = new Font("Georgia", Font.PLAIN, 13);
    private final Font SCORE_FONT = new Font("Georgia", Font.BOLD, 14);
    
    // Ukuran
    private static final int WINDOW_WIDTH = 1024;
    private static final int WINDOW_HEIGHT = 768;
    private static final int ROW_HEIGHT = 40; 
    
    private BufferedImage noiseTexture;
    
    public LeaderboardPanel(RengasdengklokGame mainApp) {
        this.mainApp = mainApp;
        setLayout(null);
        setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
        generateNoiseTexture();
        initializeUI();
    }
    
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
        
        int w = getWidth();
        int h = getHeight();
        
        // Gradient background seperti MainMenuPanel
        GradientPaint gradient = new GradientPaint(
            0, 0, DARK_BG_1,
            0, h, DARK_BG_3
        );
        g2d.setPaint(gradient);
        g2d.fillRect(0, 0, w, h);
        
        // Noise texture
        for (int y = 0; y < h; y += 100) {
            for (int x = 0; x < w; x += 100) {
                g2d.drawImage(noiseTexture, x, y, null);
            }
        }
        
        // Efek glow radial merah
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.1f));
        RadialGradientPaint glow = new RadialGradientPaint(
            w / 2, h / 2,
            w / 2,
            new float[]{0f, 1f},
            new Color[]{new Color(168, 106, 101, 255), new Color(168, 106, 101, 0)}
        );
        g2d.setPaint(glow);
        g2d.fillOval(0, 0, w, h);
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
    }

    public void onPanelShown() {
        loadLeaderboard();
    }
    
    private void initializeUI() {
        backButton = new StoryPanel.MenuButton();
        backButton.setBounds(32, 32, 80, 45);
        backButton.addActionListener(e -> {
            mainApp.showPanel("MAIN_MENU");
        });
        add(backButton);
        
        // Title Panel
        JPanel titlePanel = new JPanel(null);
        titlePanel.setBounds((WINDOW_WIDTH - 700) / 2, 80, 700, 100);
        titlePanel.setOpaque(false);
        
        // Main Title
        JLabel titleLabel = new JLabel("# Leaderboard", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Georgia", Font.BOLD, 48));
        titleLabel.setForeground(CHINA_DOLL);
        titleLabel.setBounds(0, 0, 700, 60);
        titlePanel.add(titleLabel);
        
        // Subtitle
        JLabel subtitleLabel = new JLabel("Peringkat 10 Pemain Terbaik", SwingConstants.CENTER);
        subtitleLabel.setFont(SUBTITLE_FONT);
        subtitleLabel.setForeground(ROSEWATER);
        subtitleLabel.setBounds(0, 60, 700, 30);
        titlePanel.add(subtitleLabel);
        
        add(titlePanel);
        
        // Data Panel
        createDataArea();
    }
    
    private void createDataArea() {
        // Main container for the table
        JPanel tableContainer = new JPanel(null);
        tableContainer.setBounds((WINDOW_WIDTH - 900) / 2, 180, 900, 450);
        tableContainer.setOpaque(false);
        
        // Table background panel
        JPanel tablePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Background dengan round corners - warna gelap sesuai tema
                g2d.setColor(TABLE_BG);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                
                // Border dengan warna accent
                g2d.setColor(ACCENT_RED);
                g2d.setStroke(new BasicStroke(3f));
                g2d.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, 15, 15);
                
                super.paintComponent(g);
            }
        };
        tablePanel.setBounds(0, 0, 900, 450);
        tablePanel.setLayout(new BorderLayout());
        tablePanel.setOpaque(false);
        
        // Header dengan style yang sesuai - 5 kolom
        JPanel headerPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Gradient background untuk header yang sesuai tema
                GradientPaint gp = new GradientPaint(
                    0, 0, new Color(0x75, 0x4B, 0x4D),
                    0, getHeight(), new Color(0x5D, 0x30, 0x3C)
                );
                g2d.setPaint(gp);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                
                super.paintComponent(g);
            }
        };
        headerPanel.setLayout(new GridLayout(1, 5, 10, 0));
        headerPanel.setPreferredSize(new Dimension(900, 40));
        headerPanel.setOpaque(false);
        
        // Header labels
        String[] headers = {"RANK", "NAMA", "GELAR", "WAKTU", "SKOR"};
        
        for (String header : headers) {
            JLabel headerLabel = new JLabel(header, SwingConstants.CENTER);
            headerLabel.setFont(HEADER_FONT);
            headerLabel.setForeground(TEXT_LIGHT);
            headerLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
            headerPanel.add(headerLabel);
        }
        
        // Data panel untuk menampilkan scores
        dataPanel = new JPanel();
        dataPanel.setLayout(new BoxLayout(dataPanel, BoxLayout.Y_AXIS));
        dataPanel.setOpaque(false);
        dataPanel.setBackground(TABLE_BG);
        dataPanel.setBounds(20, 70, 860, 350);
        
        tablePanel.add(headerPanel, BorderLayout.NORTH);
        tablePanel.add(dataPanel, BorderLayout.CENTER);
        tableContainer.add(tablePanel);
        add(tableContainer);
        
        // Footer text
        JLabel footerLabel = new JLabel("Saksikan sejarah dan kuis untuk meningkatkan peringkat", SwingConstants.CENTER);
        footerLabel.setFont(SUBTITLE_FONT);
        footerLabel.setForeground(ROSEWATER);
        footerLabel.setBounds(0, 650, WINDOW_WIDTH, 30);
        add(footerLabel);
    }
    
    private void loadLeaderboard() {
        System.out.println("Loading leaderboard data...");
        
        // Ambil 10 data teratas dari database dengan calculated_score
        List<ScoreEntry> scores = mainApp.getDbManager().getTopScores(10);
        
        // Membersihkan data yang sudah ada
        dataPanel.removeAll();
        
        System.out.println("Found " + scores.size() + " scores in database");
        
        // Buat baris data - maksimal 10
        int rowsToShow = Math.min(scores.size(), 10);
        
        for (int i = 0; i < rowsToShow; i++) {
            ScoreEntry score = scores.get(i);
            int rank = i + 1;
            
            String username = score.getUsername();
            int calculatedScore = score.getCalculatedScore();
            int timeTaken = score.getTimeTaken();
            String quizDuration = formatQuizDuration(timeTaken);
            
            String gelar = getGelarKehormatan(rank, calculatedScore);
            String rankIcon = getRankIcon(rank);
            
            JPanel rowPanel = createDataRow(rank, rankIcon, username, gelar, quizDuration, calculatedScore);
            dataPanel.add(rowPanel);
            
            // Add small space between rows (lebih tipis)
            if (i < rowsToShow - 1) {
                dataPanel.add(Box.createRigidArea(new Dimension(0, 1)));
            }
        }
        
        // Jika kurang dari 10 data, tambahkan baris kosong
        for (int i = rowsToShow; i < 10; i++) {
            JPanel emptyRow = createEmptyRow(i + 1);
            dataPanel.add(emptyRow);
            if (i < 9) {
                dataPanel.add(Box.createRigidArea(new Dimension(0, 1)));
            }
        }
        
        // Add filler to push content up
        dataPanel.add(Box.createVerticalGlue());
        
        dataPanel.revalidate();
        dataPanel.repaint();
    }
    
    private String getRankIcon(int rank) {
        switch (rank) {
            case 1: return "👑";
            case 2: return "🥈";
            case 3: return "🥉";
            default: return String.valueOf("🎖️");
        }
    }
    
    private String getGelarKehormatan(int rank, int calculatedScore) {
        if (rank == 1) {
            return "Patriot Proklamasi";
        } else if (rank == 2) {
            return "Pejuang Kemerdekaan";
        } else if (rank == 3) {
            return "Pembela Republik";
        }
        
        if (calculatedScore >= 900) {
            return "Aktivis Pergerakan";
        } else if (calculatedScore >= 800) {
            return "Simpatisan Kemerdekaan";
        } else if (calculatedScore >= 700) {
            return "Pemuda Revolusioner";
        } else if (calculatedScore >= 600) {
            return "Pejuang Muda";
        } else if (calculatedScore >= 500) {
            return "Patriot Sejati";
        } else if (calculatedScore >= 400) {
            return "Pelajar Sejarah";
        } else if (calculatedScore >= 300) {
            return "Pencinta Tanah Air";
        } else {
            return "Pemula";
        }
    }
    
    private String formatQuizDuration(int seconds) {
        if (seconds <= 0) return "0:00";
        int minutes = seconds / 60;
        int remainingSeconds = seconds % 60;
        return String.format("%d:%02d", minutes, remainingSeconds);
    }
    
    private JPanel createDataRow(int rank, String rankIcon, String username, String gelar, String waktu, int skor) {
        JPanel rowPanel = new JPanel(new GridLayout(1, 5, 10, 0));
        rowPanel.setPreferredSize(new Dimension(890, ROW_HEIGHT));
        rowPanel.setMaximumSize(new Dimension(890, ROW_HEIGHT));
        rowPanel.setMinimumSize(new Dimension(890, ROW_HEIGHT));
        
        // Warna latar berdasarkan ranking
        Color backgroundColor;
        Color textColor = TEXT_SOFT;
        
        switch (rank) {
            case 1:
                backgroundColor = GOLD_BG;
                textColor = GOLD_COLOR;
                break;
            case 2:
                backgroundColor = SILVER_BG;
                textColor = SILVER_COLOR;
                break;
            case 3:
                backgroundColor = BRONZE_BG;
                textColor = BRONZE_COLOR;
                break;
            default:
                if (rank % 2 == 0) {
                    backgroundColor = ROW_EVEN;
                } else {
                    backgroundColor = ROW_ODD;
                }
        }
        
        rowPanel.setBackground(backgroundColor);
        
        // Border yang lebih tipis
        rowPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(0x5D, 0x30, 0x3C, 50)),
            BorderFactory.createEmptyBorder(5, 15, 5, 15)
        ));
        
        // Rank dengan HTML untuk emoji yang lebih konsisten
        JLabel rankLabel = createHtmlLabel("<html><div style='text-align:center;font-size:12px;'>" + rankIcon + " " + rank + "</div></html>");
        rankLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Atur font untuk rank label
        Font rankFont = getEmojiFont(14);
        if (rankFont != null) {
            rankLabel.setFont(rankFont);
        } else {
            rankLabel.setFont(DATA_FONT.deriveFont(Font.BOLD, 12));
        }
        
        // Warna teks berdasarkan rank
        switch (rank) {
            case 1:
                rankLabel.setForeground(GOLD_COLOR);
                break;
            case 2:
                rankLabel.setForeground(SILVER_COLOR);
                break;
            case 3:
                rankLabel.setForeground(BRONZE_COLOR);
                break;
            default:
                rankLabel.setForeground(textColor);
        }
        
        // Username 
        JLabel usernameLabel = new JLabel(username, SwingConstants.CENTER);
        usernameLabel.setFont(DATA_FONT.deriveFont(12f));
        usernameLabel.setForeground(textColor);
        
        // Gelar kehormatan
        JLabel gelarLabel = new JLabel(gelar, SwingConstants.CENTER);
        gelarLabel.setFont(DATA_FONT.deriveFont(Font.PLAIN, 12));
        gelarLabel.setForeground(textColor);
        
        // Waktu
        JLabel waktuLabel = new JLabel(waktu, SwingConstants.CENTER);
        waktuLabel.setFont(DATA_FONT.deriveFont(12f));
        waktuLabel.setForeground(textColor);
        
        // Skor
        JLabel skorLabel = new JLabel(String.valueOf(skor), SwingConstants.CENTER);
        skorLabel.setFont(SCORE_FONT.deriveFont(13f));
        
        // Warna skor
        if (skor >= 900) {
            skorLabel.setForeground(GOLD_COLOR);
        } else if (skor >= 700) {
            skorLabel.setForeground(SILVER_COLOR);
        } else if (skor >= 500) {
            skorLabel.setForeground(BRONZE_COLOR);
        } else if (skor >= 300) {
            skorLabel.setForeground(ACCENT_RED);
        } else {
            skorLabel.setForeground(textColor);
        }
        
        // Tambahkan mouse listener untuk efek hover (kecuali top 3)
        if (rank > 3) {
            rowPanel.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    rowPanel.setBackground(ROW_HOVER);
                }
                
                @Override
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    if (rank % 2 == 0) {
                        rowPanel.setBackground(ROW_EVEN);
                    } else {
                        rowPanel.setBackground(ROW_ODD);
                    }
                }
            });
        }
        
        // Tambahkan semua komponen
        rowPanel.add(rankLabel);
        rowPanel.add(usernameLabel);
        rowPanel.add(gelarLabel);
        rowPanel.add(waktuLabel);
        rowPanel.add(skorLabel);
        
        return rowPanel;
    }
    
    private JLabel createHtmlLabel(String html) {
        JLabel label = new JLabel(html);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setVerticalAlignment(SwingConstants.CENTER);
        return label;
    }
    
    private Font getEmojiFont(float size) {
        String[] fontNames = {
            "Segoe UI Emoji",
            "Apple Color Emoji", 
            "Noto Color Emoji",
            "Android Emoji",
            "EmojiOne",
            "Twemoji Mozilla",
            "DejaVu Sans"
        };
        
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        String[] availableFonts = ge.getAvailableFontFamilyNames();
        
        for (String fontName : fontNames) {
            for (String available : availableFonts) {
                if (available.equalsIgnoreCase(fontName)) {
                    try {
                        return new Font(fontName, Font.PLAIN, (int)size);
                    } catch (Exception e) {
                        continue;
                    }
                }
            }
        }
        return null;
    }
    
    private JPanel createEmptyRow(int rank) {
        JPanel rowPanel = new JPanel(new GridLayout(1, 5, 10, 0));
        rowPanel.setPreferredSize(new Dimension(890, ROW_HEIGHT));
        rowPanel.setMaximumSize(new Dimension(890, ROW_HEIGHT));
        rowPanel.setMinimumSize(new Dimension(890, ROW_HEIGHT));
        
        // Warna latar alternating
        if (rank % 2 == 0) {
            rowPanel.setBackground(ROW_EVEN);
        } else {
            rowPanel.setBackground(ROW_ODD);
        }
        
        rowPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(0x5D, 0x30, 0x3C, 50)),
            BorderFactory.createEmptyBorder(5, 15, 5, 15)
        ));

        
        // Rank dengan icon (gunakan HTML untuk konsistensi)
        String rankIcon = getRankIcon(rank);
        JLabel rankLabel = createHtmlLabel("<html><div style='text-align:center;color:#999999;font-size:12px;'>" + rankIcon + " " + rank + "</div></html>");
        rankLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        Font emojiFont = getEmojiFont(14);
        if (emojiFont != null) {
            rankLabel.setFont(emojiFont);
        } else {
            rankLabel.setFont(DATA_FONT.deriveFont(12f));
        }
        rankLabel.setForeground(new Color(0x99, 0x99, 0x99));
        
        // Empty data untuk kolom lain
        JLabel emptyLabel = new JLabel("-", SwingConstants.CENTER);
        emptyLabel.setFont(DATA_FONT.deriveFont(12f));
        emptyLabel.setForeground(new Color(0x99, 0x99, 0x99, 150));
        
        rowPanel.add(rankLabel);
        for (int i = 1; i < 5; i++) {
            JLabel empty = new JLabel("-", SwingConstants.CENTER);
            empty.setFont(DATA_FONT.deriveFont(12f));
            empty.setForeground(new Color(0x99, 0x99, 0x99, 150));
            rowPanel.add(empty);
        }
        
        return rowPanel;
    }
}