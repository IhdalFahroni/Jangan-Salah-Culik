import javax.swing.*;
import java.awt.*;
import java.util.Map;

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
    private StoryPanel storyPanel;
    private GameOverPanel gameOverPanel;

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
        this.storyPanel = new StoryPanel();
        this.gameOverPanel = new GameOverPanel();
        this.uiManager.setStoryPanel(this.storyPanel);
        this.uiManager.setGameOverPanel(this.gameOverPanel);
        this.uiManager.setScreenSwitcher(this::showPanel);
        this.uiManager.getUiComponents().put("databaseManager", this.dbManager);
        this.sceneManager = new SceneManager(uiManager);
        this.quizManager = new QuizManager(dbManager);
        this.audioManager = new AudioManager();
        this.leaderboardPanel = new LeaderboardPanel(this);
        this.currentSession = new GameSession(uiManager);
        registerSession(this.currentSession);

        initializeFrame();
        initializePanels();
        showPanel("LOGIN");
    }

    private void initializeFrame() {
        frame = new JFrame("Rengasdengklok: Jangan Salah Culik!");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1024, 768);
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

        if (storyPanel == null) {
            storyPanel = new StoryPanel();
            uiManager.setStoryPanel(storyPanel);
        }
        mainPanel.add(storyPanel, "STORY");

        if (gameOverPanel == null) {
            gameOverPanel = new GameOverPanel();
            uiManager.setGameOverPanel(gameOverPanel);
        }
        gameOverPanel.setOnReturnToMenu(() -> showPanel("MAIN_MENU"));
        gameOverPanel.setOnReplay(this::replayStory);
        mainPanel.add(gameOverPanel, "ENDING");
    }

    public void showPanel(String panelName) {
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
                if (audioManager != null) {
                    audioManager.stopBackgroundMusic(1200);
                }
                break;
            case "QUIZ":
                if (quizPanel != null) {
                    quizPanel.onPanelShown();
                }
                if (audioManager != null) {
                    audioManager.playBackgroundMusic("assets/Audio/RUSH E.wav", 1200);
                }
                break;
            case "STORY":
                // Start background music for story with fade-in
                if (audioManager != null) {
                    audioManager.playBackgroundMusic("assets/Audio/tense music.wav", 1200);
                }
                break;
            case "ENDING":
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
        if (currentProfile == null) {
            JOptionPane.showMessageDialog(frame,
                    "Silakan masuk atau buat profil terlebih dahulu sebelum memulai cerita.",
                    "Profil belum dipilih",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (currentSession == null) {
            currentSession = new GameSession(uiManager);
        }
        currentSession.setUiManager(uiManager);
        currentSession.setProfileId(currentProfile.getProfileId());

        if (dbManager != null) {
            if (currentSession.getSessionId() <= 0) {
                int sessionId = dbManager.createGameSession(currentProfile.getProfileId());
                if (sessionId != -1) {
                    GameSession loadedSession = dbManager.loadGameSession(sessionId);
                    if (loadedSession != null) {
                        loadedSession.setUiManager(uiManager);
                        loadedSession.setProfileId(currentProfile.getProfileId());
                        currentSession = loadedSession;
                    } else {
                        currentSession.setSessionId(sessionId);
                    }
                }
            } else {
                GameSession loadedSession = dbManager.loadGameSession(currentSession.getSessionId());
                if (loadedSession != null) {
                    loadedSession.setUiManager(uiManager);
                    loadedSession.setProfileId(currentProfile.getProfileId());
                    currentSession = loadedSession;
                }
            }
        }

        registerSession(currentSession);
        showPanel("STORY");

        if (currentSession.hasExistingProgress()) {
            currentSession.loadProgress();
        } else {
            currentSession.startNewGame();
        }

        Map<String, Object> components = uiManager.getUiComponents();
        if (components != null) {
            Object sceneObj = components.get("sceneManager");
            if (sceneObj instanceof SceneManager) {
                this.sceneManager = (SceneManager) sceneObj;
            }
        }
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
    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public PlayerProfile getCurrentProfile() {
        return currentProfile;
    }

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

    public GameSession getCurrentSession() {
        return currentSession;
    }

    public void setCurrentSession(GameSession currentSession) {
        this.currentSession = currentSession;
    }

    public SceneManager getSceneManager() {
        return sceneManager;
    }

    public QuizManager getQuizManager() {
        return quizManager;
    }

    public AudioManager getAudioManager() {
        return audioManager;
    }

    public DatabaseManager getDbManager() {
        return dbManager;
    }

    public LeaderboardPanel getLeaderboardManager() {
        return leaderboardPanel;
    }

    public UIManager getUiManager() {
        return uiManager;
    }

    private void registerSession(GameSession session) {
        if (session == null || uiManager == null) {
            return;
        }
        session.setUiManager(uiManager);
        uiManager.getUiComponents().put("gameSession", session);
        uiManager.getUiComponents().put("currentUserId", getCurrentUserId());
        uiManager.getUiComponents().put("currentProfileId", getCurrentProfileId());
        if (storyPanel != null) {
            storyPanel.setUiManager(uiManager);
        }
        if (gameOverPanel != null) {
            gameOverPanel.setOnReplay(this::replayStory);
        }
    }

    private void replayStory() {
        if (currentProfile == null) {
            showPanel("MAIN_MENU");
            return;
        }
        if (currentSession == null) {
            currentSession = new GameSession(uiManager);
        }
        currentSession.setProfileId(currentProfile.getProfileId());
        registerSession(currentSession);
        showPanel("STORY");
        currentSession.startNewGame();
    }
}
