import javax.swing.*;
import java.awt.*;

public class RengasdengklokGame {
    private User currentUser;
    private PlayerProfile currentProfile;
    private GameSession currentSession;
    
    // Managers
    private SceneManager sceneManager;
    private QuizManager quizManager;
    private AudioManager audioManager;
    private DatabaseManager dbManager;
    private LeaderboardPanel leaderboardPanel;
    private UIManager uiManager; 
    
    // Komponen Swing
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private JFrame frame;
    
    // Panel references
    private LoginPanel loginPanel;
    private MainMenuPanel mainMenuPanel;
    private QuizPanel quizPanel;
    
    public RengasdengklokGame() {
        this.dbManager = new DatabaseManager();
        this.dbManager.connect(); // Connect ke database
        
        this.uiManager = new UIManager(); 
        this.sceneManager = new SceneManager(uiManager);
        this.quizManager = new QuizManager(dbManager);
        this.audioManager = new AudioManager();
        this.leaderboardPanel = new LeaderboardPanel(this);
        this.currentSession = new GameSession(uiManager);
        
        initializeFrame();
        initializePanels();
        showPanel("LOGIN"); 
    }
    
    private void initializeFrame() {
        frame = new JFrame("Rengasdengklok: Jangan Salah Culik!");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        frame.add(mainPanel);
        frame.setVisible(true);
    }
    
    private void initializePanels() {
        // Login Panel
        loginPanel = new LoginPanel(this);
        mainPanel.add(loginPanel, "LOGIN");
        
        // Main Menu Panel
        mainMenuPanel = new MainMenuPanel(this);
        mainPanel.add(mainMenuPanel, "MAIN_MENU");
        
        // Leaderboard Panel
        leaderboardPanel = new LeaderboardPanel(this);
        mainPanel.add(leaderboardPanel, "LEADERBOARD");
    }
    
    public void showPanel(String panelName) {
        System.out.println("Switching to panel: " + panelName);
        cardLayout.show(mainPanel, panelName);
        
        switch (panelName) {
            case "LOGIN":
                if (loginPanel != null) {
                    loginPanel.reset();
                }
                break;
            case "MAIN_MENU":
                if (mainMenuPanel != null) {
                    mainMenuPanel.onPanelShown();
                }
                break;
            case "QUIZ":
                if (quizPanel != null) {
                    quizPanel.onPanelShown();
                }
                break;
        }
    }
    
    // Method untuk handle login success dari LoginPanel
    public void onLoginSuccess(User user) {
        this.currentUser = user;
        
        // Load user profiles
        java.util.List<PlayerProfile> profiles = dbManager.getUserProfiles(user.getUserId());
        if (!profiles.isEmpty()) {
            this.currentProfile = profiles.get(0);
        } else {
            // Create default profile if no profile exists
            createDefaultProfile(user);
        }
        
        showPanel("MAIN_MENU");
    }
    
    private void createDefaultProfile(User user) {
        boolean success = dbManager.createProfile(user.getUserId(), user.getUsername(), "Male");
        if (success) {
            java.util.List<PlayerProfile> profiles = dbManager.getUserProfiles(user.getUserId());
            if (!profiles.isEmpty()) {
                this.currentProfile = profiles.get(0);
            }
        }
    }
    
    // Method untuk handle logout
    public void logout() {
        // Save game progress
        if (currentSession != null && currentUser != null) {
            dbManager.saveGameProgress(currentSession);
        }
        
        // Reset user data
        currentUser = null;
        currentProfile = null;
        currentSession = new GameSession(uiManager); // Create new session
        
        // Stop audio
        if (audioManager != null) {
            audioManager.stopBackgroundMusic();
        }
        
        // Balik k login
        showPanel("LOGIN");
    }
    
    public void showQuizPanel() {
        if (quizPanel == null) {
            quizPanel = new QuizPanel(this);
            mainPanel.add(quizPanel, "QUIZ");
        }
        showPanel("QUIZ");
    }
    
    public void showLeaderboardPanel() {
        if (leaderboardPanel != null) {
            leaderboardPanel.onPanelShown();
        }
        showPanel("LEADERBOARD");
    }
    
    public void startGame() {
        System.out.println("Starting main story game...");
        
        // Create or load game session
        if (currentProfile != null) {
            int sessionId = dbManager.createGameSession(currentProfile.getProfileId());
            if (sessionId != -1) {
                currentSession = dbManager.loadGameSession(sessionId);
                currentSession.setUiManager(uiManager);
            }
        }
        
        // Reset atau initialize session untuk game baru
        if (currentSession != null) {
            currentSession.startNewGame();
        }
        
        // Mulai dari scene pertama menggunakan UIManager
        uiManager.showScreen("SCENE_1");
        sceneManager.loadScene(1);
        
        // Show decision timer untuk scene pertama
        uiManager.showDecisionTimer(45);
        
        // TODO: Show game scene panel
        System.out.println("Game started!");
    }
    
    public void showQuizResultPanel(int score, int totalQuestions, double percentage, int timeTaken) {
        System.out.printf("Quiz Result: %d/%d (%.1f%%) in %d seconds%n", 
            score, totalQuestions, percentage, timeTaken);
        showPanel("MAIN_MENU");
    }

    public static void main(String[] args) {
        try {
            javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            try {
                javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getCrossPlatformLookAndFeelClassName());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        
        SwingUtilities.invokeLater(() -> {
            new RengasdengklokGame();
        });
    }
    
    // Getters and Setters
    public User getCurrentUser() { return currentUser; }
    public void setCurrentUser(User currentUser) { 
        this.currentUser = currentUser; 
    }
    
    public PlayerProfile getCurrentProfile() { return currentProfile; }
    public void setCurrentProfile(PlayerProfile currentProfile) { 
        this.currentProfile = currentProfile; 
    }
    
    public String getCurrentUsername() { 
        if (currentUser != null) {
            return currentUser.getUsername();
        }
        return "Guest"; 
    }
    
    public int getCurrentUserId() {
        return currentUser != null ? currentUser.getUserId() : 0;
    }
    
    public int getCurrentProfileId() {
        return currentProfile != null ? currentProfile.getProfileId() : 0;
    }
    
    public GameSession getCurrentSession() { return currentSession; }
    public void setCurrentSession(GameSession currentSession) { this.currentSession = currentSession; }
    
    public SceneManager getSceneManager() { return sceneManager; }
    public QuizManager getQuizManager() { return quizManager; }
    public AudioManager getAudioManager() { return audioManager; }
    public DatabaseManager getDbManager() { return dbManager; }
    public LeaderboardPanel getLeaderboardManager() { return leaderboardPanel; }
    public UIManager getUiManager() { return uiManager; }
}
