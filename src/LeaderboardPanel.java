
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class LeaderboardPanel extends JPanel {
    private RengasdengklokGame mainApp;
    private JButton backButton;
    private JPanel dataPanel;
    
    // vintej
    private final Color BACKGROUND_COLOR = new Color(0xF5, 0xED, 0xE0); // Cream vintage
    private final Color ACCENT_COLOR = new Color(0x8B, 0x45, 0x1E);     // Dark brown vintage
    private final Color TEXT_COLOR = new Color(0x5D, 0x30, 0x3C);       // Deep brown
    private final Color BUTTON_BACKGROUND = new Color(0xA0, 0x6C, 0x4F); // Medium brown
    private final Color BUTTON_TEXT_COLOR = new Color(115, 102, 47); //Dark ijo yang dikirim alya
    private final Color ROW_EVEN_COLOR = new Color(0xFA, 0xF3, 0xE8);   // Light cream untuk baris genap
    private final Color ROW_ODD_COLOR = new Color(0xF5, 0xED, 0xE0);    // Cream vintage untuk baris ganjil
    private final Color HEADER_BACKGROUND = new Color(0x8B, 0x45, 0x1E); // Dark brown untuk header
    private final Color HEADER_TEXT_COLOR = new Color(0xFA, 0xF3, 0xE8); // Light cream untuk text header
    
    // Fonts 
    private final Font LEADERBOARD_TITLE_FONT = new Font("Serif", Font.BOLD, 32);
    private final Font LEADERHEADER_FONT = new Font("Serif", Font.BOLD, 14);
    private final Font DATA_FONT = new Font("Serif", Font.PLAIN, 13);
    private final Font BACK_BUTTON_FONT = new Font("Serif", Font.BOLD, 16);
    
    // ukuran
    private final Dimension WINDOW_SIZE = new Dimension(800, 600);
    private final Dimension HEADER_SIZE = new Dimension(650, 35);
    private final Dimension ROW_SIZE = new Dimension(650, 30);
    private final Dimension BACK_BUTTON_SIZE = new Dimension(200, 45);
    
    // posisi
    private final Point TITLE_POSITION = new Point(200, 30);
    private final Point HEADER_POSITION = new Point(75, 100);
    private final Point DATA_START_POSITION = new Point(75, 135);
    private final Point BACK_BUTTON_POSITION = new Point(300, 500);
    
    // Column widths yang disesuaikan
    private final int[] COLUMN_WIDTHS = {70, 180, 70, 100, 150}; // Lebar kolom disesuaikan
    private final int TOTAL_WIDTH = 650; // Total width disesuaikan
    
    // Medal colors
    private final Color GOLD_COLOR = new Color(0xD4, 0xAF, 0x37);    // Gold vintage dem
    private final Color SILVER_COLOR = new Color(0xC0, 0xC0, 0xC0);  // Silver
    private final Color BRONZE_COLOR = new Color(0xCD, 0x7F, 0x32);  // Bronze
    
    public LeaderboardPanel(RengasdengklokGame mainApp) {
        this.mainApp = mainApp;
        initializeUI();
        loadLeaderboard();
    }
    
    public void onPanelShown() {
        loadLeaderboard();
    }
    
    private void initializeUI() {
        setLayout(null);
        setBackground(BACKGROUND_COLOR);
        setPreferredSize(WINDOW_SIZE);
        
        createTitle();
        createHeader();
        createDataArea();
        createBackButton();
        addDecorativeElements();
    }
    
    private void createTitle() {
        JLabel titleLabel = new JLabel("Papan Peringkat", JLabel.CENTER);
        titleLabel.setBounds(TITLE_POSITION.x, TITLE_POSITION.y, 400, 50);
        titleLabel.setFont(LEADERBOARD_TITLE_FONT);
        titleLabel.setForeground(ACCENT_COLOR);
        
        // Subtitle italic biar ky keren gimana gitu
        JLabel subtitleLabel = new JLabel("Top 10 Pemain Terbaik", JLabel.CENTER);
        subtitleLabel.setBounds(TITLE_POSITION.x, TITLE_POSITION.y + 40, 400, 25);
        subtitleLabel.setFont(new Font("Serif", Font.ITALIC, 14));
        subtitleLabel.setForeground(new Color(0x7D, 0x5A, 0x45));
        
        add(titleLabel);
        add(subtitleLabel);
    }
    
    private void createHeader() {
        // Header background dengan border vintage
        JPanel headerPanel = new JPanel();
        headerPanel.setBounds(HEADER_POSITION.x, HEADER_POSITION.y, HEADER_SIZE.width, HEADER_SIZE.height);
        headerPanel.setBackground(HEADER_BACKGROUND);
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.X_AXIS));
        headerPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0x5D, 0x30, 0x3C), 2),
            BorderFactory.createEmptyBorder(8, 15, 8, 15)
        ));
        
        String[] headers = {"Rank", "Username", "Skor", "Waktu", "Tanggal"};
        
        for (int i = 0; i < headers.length; i++) {
            JLabel header = new JLabel(headers[i], JLabel.CENTER);
            header.setFont(LEADERHEADER_FONT);
            header.setForeground(HEADER_TEXT_COLOR);
            header.setPreferredSize(new Dimension(COLUMN_WIDTHS[i], 20));
            header.setMinimumSize(new Dimension(COLUMN_WIDTHS[i], 20));
            header.setMaximumSize(new Dimension(COLUMN_WIDTHS[i], 20));
            
            headerPanel.add(header);
            if (i < headers.length - 1) {
                headerPanel.add(Box.createRigidArea(new Dimension(5, 0)));
            }
        }
        
        add(headerPanel);
    }
    
    private void createDataArea() {
        dataPanel = new JPanel();
        dataPanel.setBounds(DATA_START_POSITION.x, DATA_START_POSITION.y, TOTAL_WIDTH, 320);
        dataPanel.setLayout(new BoxLayout(dataPanel, BoxLayout.Y_AXIS));
        dataPanel.setBackground(BACKGROUND_COLOR);
        dataPanel.setBorder(BorderFactory.createLineBorder(new Color(0xCD, 0xAA, 0x7D), 1));
        
        add(dataPanel);
    }
    
    private void createBackButton() {
        backButton = new JButton("← Kembali ke Menu");
        backButton.setBounds(BACK_BUTTON_POSITION.x, BACK_BUTTON_POSITION.y,
                           BACK_BUTTON_SIZE.width, BACK_BUTTON_SIZE.height);
        backButton.setBackground(BUTTON_BACKGROUND);
        backButton.setForeground(BUTTON_TEXT_COLOR);
        backButton.setFont(BACK_BUTTON_FONT);
        backButton.setFocusPainted(false);
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.setHorizontalAlignment(SwingConstants.CENTER);
        backButton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ACCENT_COLOR, 2),
            BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        
        // Efek hover
        backButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                backButton.setBackground(ACCENT_COLOR);
                backButton.setForeground(BUTTON_TEXT_COLOR);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                backButton.setBackground(BUTTON_BACKGROUND);
                backButton.setForeground(BUTTON_TEXT_COLOR);
            }
        });
        
        backButton.addActionListener(e -> mainApp.showPanel("MAIN_MENU"));
        
        add(backButton);
    }
    
    // ni method yang atur border atas bawah estetik vintej itu
    private void addDecorativeElements() {
        // Garis dekoratif atas
        JSeparator topSeparator = new JSeparator(SwingConstants.HORIZONTAL);
        topSeparator.setBounds(75, 90, 650, 2);
        topSeparator.setForeground(ACCENT_COLOR);
        topSeparator.setBackground(ACCENT_COLOR);
        add(topSeparator);
        
        // Garis dekoratif bawah
        JSeparator bottomSeparator = new JSeparator(SwingConstants.HORIZONTAL);
        bottomSeparator.setBounds(75, 465, 650, 2);
        bottomSeparator.setForeground(ACCENT_COLOR);
        bottomSeparator.setBackground(ACCENT_COLOR);
        add(bottomSeparator);
        
        // Corner decorations vintage
        JLabel cornerTL = createCornerDecoration("╔", 65, 75);
        JLabel cornerTR = createCornerDecoration("╗", 720, 75);
        JLabel cornerBL = createCornerDecoration("╚", 65, 455);
        JLabel cornerBR = createCornerDecoration("╝", 720, 455);
        
        add(cornerTL);
        add(cornerTR);
        add(cornerBL);
        add(cornerBR);
    }
    
    private JLabel createCornerDecoration(String symbol, int x, int y) {
        JLabel corner = new JLabel(symbol);
        corner.setBounds(x, y, 20, 20);
        corner.setFont(new Font("Monospaced", Font.BOLD, 16));
        corner.setForeground(ACCENT_COLOR);
        return corner;
    }
    
    private void loadLeaderboard() {
        System.out.println("Loading leaderboard data...");
        
        // Ambil 10 data teratas
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
            int playerScore = score.getScore();
            int timeTaken = score.getTimeTaken();
            String quizDuration = formatQuizDuration(timeTaken);
            String time = formatTimestamp(score.getAttemptedAt());
            
            JPanel rowPanel = createDataRow(rank, username, playerScore, quizDuration, time);
            dataPanel.add(rowPanel);
            
            // Add small space between rows
            if (i < rowsToShow - 1) {
                dataPanel.add(Box.createRigidArea(new Dimension(0, 2)));
            }
        }
        
        // Jika kurang dari 10 data, tambahkan baris kosong
        for (int i = rowsToShow; i < 10; i++) {
            JPanel emptyRow = createEmptyRow(i + 1);
            dataPanel.add(emptyRow);
            if (i < 9) { // Diperbaiki dari 19 ke 9
                dataPanel.add(Box.createRigidArea(new Dimension(0, 2)));
            }
        }
        
        dataPanel.revalidate();
        dataPanel.repaint();
    }
    
    private String formatQuizDuration(int seconds) {
        if (seconds <= 0) return "0:00";
        
        int minutes = seconds / 60;
        int remainingSeconds = seconds % 60;
        return String.format("%d:%02d", minutes, remainingSeconds);
    }
    
    private String formatTimestamp(java.util.Date date) {
        if (date == null) return "N/A";
        
        // Format sederhana: DD/MM/YYYY HH:MM
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yy HH:mm");
        return sdf.format(date);
    }
    
    private JPanel createDataRow(int rank, String username, int score, String quizDuration, String time) {
        JPanel rowPanel = new JPanel();
        rowPanel.setLayout(new BoxLayout(rowPanel, BoxLayout.X_AXIS));
        rowPanel.setPreferredSize(new Dimension(ROW_SIZE.width, ROW_SIZE.height));
        rowPanel.setMaximumSize(new Dimension(ROW_SIZE.width, ROW_SIZE.height));
        
        // Alternating row colors dengan border tipis
        if (rank % 2 == 0) {
            rowPanel.setBackground(ROW_EVEN_COLOR);
        } else {
            rowPanel.setBackground(ROW_ODD_COLOR);
        }
        
        rowPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(0xE8, 0xDC, 0xC8)),
            BorderFactory.createEmptyBorder(5, 15, 5, 15)
        ));
        
        // Rank - dengan icon medali untuk top 3
        String rankText;
        Color rankColor;
        if (rank == 1) {
            rankText = "🥇 " + rank;
            rankColor = GOLD_COLOR;
        } else if (rank == 2) {
            rankText = "🥈 " + rank;
            rankColor = SILVER_COLOR;
        } else if (rank == 3) {
            rankText = "🥉 " + rank;
            rankColor = BRONZE_COLOR;
        } else {
            rankText = String.valueOf(rank);
            rankColor = TEXT_COLOR;
        }
        
        JLabel rankLabel = createDataLabel(rankText, COLUMN_WIDTHS[0], JLabel.CENTER);
        rankLabel.setForeground(rankColor);
        rankLabel.setFont(DATA_FONT.deriveFont(Font.BOLD));

        // Username 
        JLabel usernameLabel = createDataLabel(username, COLUMN_WIDTHS[1], JLabel.CENTER);
        usernameLabel.setForeground(TEXT_COLOR);

        // Score - CENTER
        JLabel scoreLabel = createDataLabel(String.valueOf(score), COLUMN_WIDTHS[2], JLabel.CENTER);
        scoreLabel.setForeground(TEXT_COLOR);
        scoreLabel.setFont(DATA_FONT.deriveFont(Font.BOLD));

        // Lama Quiz - CENTER
        JLabel durationLabel = createDataLabel(quizDuration, COLUMN_WIDTHS[3], JLabel.CENTER);
        durationLabel.setForeground(TEXT_COLOR);

        // Time - CENTER
        JLabel timeLabel = createDataLabel(time, COLUMN_WIDTHS[4], JLabel.CENTER);
        timeLabel.setForeground(TEXT_COLOR);
        timeLabel.setFont(DATA_FONT.deriveFont(11f)); // Font sedikit lebih kecil
        
        // Tambah komponen dengan spacing yang sama dengan header
        rowPanel.add(rankLabel);
        rowPanel.add(Box.createRigidArea(new Dimension(5, 0)));
        rowPanel.add(usernameLabel);
        rowPanel.add(Box.createRigidArea(new Dimension(5, 0)));
        rowPanel.add(scoreLabel);
        rowPanel.add(Box.createRigidArea(new Dimension(5, 0)));
        rowPanel.add(durationLabel);
        rowPanel.add(Box.createRigidArea(new Dimension(5, 0)));
        rowPanel.add(timeLabel);
        
        return rowPanel;
    }
    
    private JLabel createDataLabel(String text, int width, int alignment) {
        JLabel label = new JLabel(text, alignment);
        label.setFont(DATA_FONT);
        label.setForeground(TEXT_COLOR);
        label.setPreferredSize(new Dimension(width, 20));
        label.setMinimumSize(new Dimension(width, 20));
        label.setMaximumSize(new Dimension(width, 20));
        return label;
    }
    
    private JPanel createEmptyRow(int rank) {
        JPanel rowPanel = new JPanel();
        rowPanel.setLayout(new BoxLayout(rowPanel, BoxLayout.X_AXIS));
        rowPanel.setPreferredSize(new Dimension(ROW_SIZE.width, ROW_SIZE.height));
        rowPanel.setMaximumSize(new Dimension(ROW_SIZE.width, ROW_SIZE.height));
        
        // Alternating row colors
        if (rank % 2 == 0) {
            rowPanel.setBackground(ROW_EVEN_COLOR);
        } else {
            rowPanel.setBackground(ROW_ODD_COLOR);
        }
        
        rowPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(0xE8, 0xDC, 0xC8)),
            BorderFactory.createEmptyBorder(5, 15, 5, 15)
        ));
        
        // Rank
        JLabel rankLabel = createDataLabel(String.valueOf(rank), COLUMN_WIDTHS[0], JLabel.CENTER);
        rankLabel.setForeground(new Color(0x99, 0x99, 0x99));
        
        // Empty data
        JLabel emptyLabel = createDataLabel("-", 
            COLUMN_WIDTHS[1] + COLUMN_WIDTHS[2] + COLUMN_WIDTHS[3] + COLUMN_WIDTHS[4] + 20, 
            JLabel.CENTER);
        emptyLabel.setForeground(new Color(0x99, 0x99, 0x99));
        emptyLabel.setFont(DATA_FONT.deriveFont(Font.ITALIC));
        
        rowPanel.add(rankLabel);
        rowPanel.add(Box.createRigidArea(new Dimension(5, 0)));
        rowPanel.add(emptyLabel);
        
        return rowPanel;
    }
}
