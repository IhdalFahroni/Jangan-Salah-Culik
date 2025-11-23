package src;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainMenuPanel extends JPanel {
    private RengasdengklokGame mainApp;
    private JButton startGameButton, quizButton, leaderboardButton, logoutButton;
    private JLabel welcomeLabel;
    private JButton profileButton;

    // Color palette
    private final Color BACKGROUND_COLOR = new Color(0xF5, 0xED, 0xE0); // Cream vintage
    private final Color ACCENT_COLOR = new Color(0x8B, 0x45, 0x1E);     // Dark brown vintage
    private final Color TEXT_COLOR = new Color(0x5D, 0x30, 0x3C);       // Deep brown
    private final Color BUTTON_BACKGROUND = new Color(0xA0, 0x6C, 0x4F); // Medium brown
    private final Color DIALOG_BACKGROUND = new Color(0xFA, 0xF3, 0xE8); // Light cream untuk dialog

    public MainMenuPanel(RengasdengklokGame mainApp) {
        this.mainApp = mainApp;
        setLayout(null);
        setBackground(BACKGROUND_COLOR);
        setPreferredSize(new Dimension(800, 600));
        initializeUI();
    }
    
    private void initializeUI() {
        createTitle();
        createWelcomeLabel();
        createProfileButton();
        createMenuButtons();
        addDecorativeElements();
    }
    
    private void createTitle() {
        // Main Title
        JLabel titleLabel = new JLabel("Rengasdengklok: Jangan Salah Culik!", JLabel.CENTER);
        titleLabel.setBounds(200, 60, 400, 50);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 32));
        titleLabel.setForeground(ACCENT_COLOR);
        add(titleLabel);
        
        // Subtitle
        JLabel subtitleLabel = new JLabel("Telltale Petualangan Sejarah 1945", JLabel.CENTER);
        subtitleLabel.setBounds(200, 100, 400, 30);
        subtitleLabel.setFont(new Font("Serif", Font.ITALIC, 16));
        subtitleLabel.setForeground(new Color(0x7D, 0x5A, 0x45)); // Brown medium
        add(subtitleLabel);
    }
    
    private void createWelcomeLabel() {
        String username = mainApp.getCurrentUsername();
        String displayName = (username == null || username.isEmpty()) ? "Guest" : username;
        
        welcomeLabel = new JLabel("Selamat datang, " + displayName, JLabel.LEFT);
        welcomeLabel.setBounds(50, 30, 400, 40);
        welcomeLabel.setFont(new Font("Serif", Font.BOLD, 20));
        welcomeLabel.setForeground(ACCENT_COLOR);
        add(welcomeLabel);
    }
    
    private void createProfileButton() {
        // Profile button di pojok kanan atas 
        profileButton = new JButton("👤"); 
        profileButton.setBounds(700, 20, 50, 50);
        profileButton.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 18));
        profileButton.setBackground(BUTTON_BACKGROUND);
        profileButton.setForeground(ACCENT_COLOR);
        profileButton.setFocusPainted(false);
        profileButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        profileButton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ACCENT_COLOR, 2),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        
        // Efek hover
        profileButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                profileButton.setBackground(ACCENT_COLOR);
                profileButton.setForeground(ACCENT_COLOR);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                profileButton.setBackground(BUTTON_BACKGROUND);
                profileButton.setForeground(ACCENT_COLOR);
            }
        });
        
        profileButton.addActionListener(new ProfileListener());
        add(profileButton);
    }
    
    private void createMenuButtons() {
        // Start Game Button
        startGameButton = createMenuButton("Mulai Game Cerita", 180);
        startGameButton.addActionListener(new StartGameListener());
        add(startGameButton);

        // Quiz Button  
        quizButton = createMenuButton("Kuis Sejarah", 250);
        quizButton.addActionListener(new QuizListener());
        add(quizButton);

        // Leaderboard Button
        leaderboardButton = createMenuButton("Leaderboard", 320);
        leaderboardButton.addActionListener(new LeaderboardListener());
        add(leaderboardButton);

        // Logout Button
        logoutButton = createMenuButton("Logout", 390);
        logoutButton.addActionListener(new LogoutListener());
        add(logoutButton);
    }
    
    private JButton createMenuButton(String text, int yPosition) {
        JButton button = new JButton(text);
        button.setBounds(200, yPosition, 400, 50);
        button.setFont(new Font("Serif", Font.BOLD, 18));
        button.setBackground(BUTTON_BACKGROUND);
        button.setForeground(ACCENT_COLOR);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ACCENT_COLOR, 2),
            BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        
        // Efek hover
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(ACCENT_COLOR);
                button.setForeground(ACCENT_COLOR);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(BUTTON_BACKGROUND);
                button.setForeground(ACCENT_COLOR);
            }
        });
        
        return button;
    }
    
    private void addDecorativeElements() {
        // Garis dekoratif atas
        JSeparator topSeparator = new JSeparator(SwingConstants.HORIZONTAL);
        topSeparator.setBounds(100, 150, 600, 2);
        topSeparator.setForeground(ACCENT_COLOR);
        topSeparator.setBackground(ACCENT_COLOR);
        add(topSeparator);
        
        // Garis dekoratif bawah
        JSeparator bottomSeparator = new JSeparator(SwingConstants.HORIZONTAL);
        bottomSeparator.setBounds(100, 460, 600, 2);
        bottomSeparator.setForeground(ACCENT_COLOR);
        bottomSeparator.setBackground(ACCENT_COLOR);
        add(bottomSeparator);
        
        // Corner decorations vintage
        JLabel cornerTL = createCornerDecoration("╔", 80, 135);
        JLabel cornerTR = createCornerDecoration("╗", 705, 135);
        JLabel cornerBL = createCornerDecoration("╚", 80, 450);
        JLabel cornerBR = createCornerDecoration("╝", 705, 450);
        
        add(cornerTL);
        add(cornerTR);
        add(cornerBL);
        add(cornerBR);
    }
    
    private JLabel createCornerDecoration(String symbol, int x, int y) {
        JLabel corner = new JLabel(symbol);
        corner.setBounds(x, y, 20, 20);
        corner.setFont(new Font("Monospaced", Font.BOLD, 18));
        corner.setForeground(ACCENT_COLOR);
        return corner;
    }

    public void refreshWelcomeMessage() {
        String username = mainApp.getCurrentUsername();
        String displayName = (username == null || username.isEmpty()) ? "Guest" : username;
        welcomeLabel.setText("Selamat datang, " + displayName);
    }
    
    public void onPanelShown() {
        refreshWelcomeMessage();
    }
    
    private class ProfileListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            showProfileDialog();
        }
    }
    
    private void showProfileDialog() {
        PlayerProfile currentProfile = mainApp.getCurrentProfile();
        String currentCharacterName = currentProfile != null ? currentProfile.getCharacterName() : "";
        String currentGender = currentProfile != null ? currentProfile.getGender() : "Laki-laki";
        
        // Dialog profile
        JDialog profileDialog = new JDialog();
        profileDialog.setTitle("Edit Profile - Rengasdengklok 1945");
        profileDialog.setModal(true);
        profileDialog.setSize(500, 500); // Lebih besar
        profileDialog.setLocationRelativeTo(this);
        profileDialog.setLayout(new BorderLayout());
        profileDialog.getContentPane().setBackground(DIALOG_BACKGROUND);
        
        // Title 
        JLabel titleLabel = new JLabel("Edit Profil Petualang", JLabel.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 20));
        titleLabel.setForeground(ACCENT_COLOR);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        
        // Main content panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(DIALOG_BACKGROUND);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(0, 30, 20, 30));
        
        // Nama Character
        JPanel namePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        namePanel.setBackground(DIALOG_BACKGROUND);
        JLabel nameLabel = new JLabel("Nama:");
        nameLabel.setFont(new Font("Serif", Font.BOLD, 14));
        nameLabel.setForeground(ACCENT_COLOR);
        
        JTextField nameField = new JTextField(currentCharacterName, 20);
        nameField.setFont(new Font("Serif", Font.PLAIN, 14));
        nameField.setBackground(BACKGROUND_COLOR);
        nameField.setForeground(ACCENT_COLOR);
        
        namePanel.add(nameLabel);
        namePanel.add(nameField);
        
        // Gender 
        JPanel genderPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        genderPanel.setBackground(DIALOG_BACKGROUND);
        JLabel genderLabel = new JLabel("Jenis Kelamin:");
        genderLabel.setFont(new Font("Serif", Font.BOLD, 14));
        genderLabel.setForeground(TEXT_COLOR);
        
        // Variabel untuk track selection
        final String[] selectedGender = new String[1];
        selectedGender[0] = currentGender; // Default
        
        // Panel radio button
        JPanel radioPanel = new JPanel(new GridLayout(2, 1));
        radioPanel.setBackground(DIALOG_BACKGROUND);
        
        JRadioButton maleRadio = new JRadioButton("Laki-laki");
        JRadioButton femaleRadio = new JRadioButton("Perempuan");
        
        maleRadio.setFont(new Font("Serif", Font.PLAIN, 14));
        femaleRadio.setFont(new Font("Serif", Font.PLAIN, 14));
        maleRadio.setBackground(DIALOG_BACKGROUND);
        femaleRadio.setBackground(DIALOG_BACKGROUND);
        maleRadio.setForeground(TEXT_COLOR);
        femaleRadio.setForeground(TEXT_COLOR);
        
        if ("Perempuan".equalsIgnoreCase(currentGender)) {
            femaleRadio.setSelected(true);
            maleRadio.setSelected(false);
            selectedGender[0] = "Perempuan";
        } else {
            maleRadio.setSelected(true);
            femaleRadio.setSelected(false);
            selectedGender[0] = "Laki-laki";
        }
        
        maleRadio.addActionListener(e -> {
            maleRadio.setSelected(true);
            femaleRadio.setSelected(false);
            selectedGender[0] = "Laki-laki";
            System.out.println("Selected: Laki-laki");
        });
        
        femaleRadio.addActionListener(e -> {
            femaleRadio.setSelected(true);
            maleRadio.setSelected(false);
            selectedGender[0] = "Perempuan";
            System.out.println("Selected: Perempuan");
        });
        
        radioPanel.add(maleRadio);
        radioPanel.add(femaleRadio);
        
        genderPanel.add(genderLabel);
        genderPanel.add(radioPanel);
        
        mainPanel.add(namePanel);
        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(genderPanel);
        mainPanel.add(Box.createVerticalStrut(10));
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.setBackground(DIALOG_BACKGROUND);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        
        JButton saveButton = createDialogButton("Simpan");
        JButton cancelButton = createDialogButton("Batal");
        
        saveButton.addActionListener(e -> {
            String newCharacterName = nameField.getText().trim();
            String newGender = selectedGender[0];
            
            if (newCharacterName.isEmpty()) {
                JOptionPane.showMessageDialog(profileDialog, 
                    "Nama petualang tidak boleh kosong!", 
                    "Peringatan", 
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            boolean success = updateProfile(newCharacterName, newGender);
            
            if (success) {
                JOptionPane.showMessageDialog(profileDialog, 
                    "Profil berhasil diperbarui!",
                    "Sukses", 
                    JOptionPane.INFORMATION_MESSAGE);
                profileDialog.dispose();
                refreshWelcomeMessage();
            } else {
                JOptionPane.showMessageDialog(profileDialog, 
                    "Gagal memperbarui profil!", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        });
        
        cancelButton.addActionListener(e -> profileDialog.dispose());
        
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        
        profileDialog.add(titleLabel, BorderLayout.NORTH);
        profileDialog.add(mainPanel, BorderLayout.CENTER);
        profileDialog.add(buttonPanel, BorderLayout.SOUTH);
        
        profileDialog.setVisible(true);
    }
    
    private JButton createDialogButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Serif", Font.BOLD, 14));
        button.setBackground(BUTTON_BACKGROUND);
        button.setForeground(ACCENT_COLOR);
        button.setPreferredSize(new Dimension(100, 35));
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ACCENT_COLOR, 2),
            BorderFactory.createEmptyBorder(5, 15, 5, 15)
        ));
        
        // Efek hover
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(ACCENT_COLOR);
                button.setForeground(ACCENT_COLOR);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(BUTTON_BACKGROUND);
                button.setForeground(ACCENT_COLOR);
            }
        });
        
        return button;
    }
    
    private boolean updateProfile(String characterName, String gender) {
        PlayerProfile currentProfile = mainApp.getCurrentProfile();
        
        if (currentProfile == null) {
            return mainApp.getDbManager().createProfile(
                mainApp.getCurrentUserId(), 
                characterName, 
                gender  
            );
        } else {
            return mainApp.getDbManager().updateProfile(
                mainApp.getCurrentUserId(), 
                characterName, 
                gender
            );
        }
    }
    
    private class StartGameListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            mainApp.startGame();
        }
    }
    
    private class QuizListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            mainApp.showQuizPanel();
        }
    }
    
    private class LeaderboardListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            mainApp.showLeaderboardPanel();
        }
    }
    
    private class LogoutListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            mainApp.logout();
        }
    }
}