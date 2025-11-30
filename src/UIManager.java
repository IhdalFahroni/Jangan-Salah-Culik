import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

//kelas yang ngatur perpindahan layar dan hubungin panel-panel
public class UIManager {

    private Object currentScreen;                //nama layar yang sedang tampil
    private Map<String, Object> uiComponents;    //tempat nyimpen komponen penting
    private StoryPanel storyPanel;               //panel cerita
    private GameOverPanel gameOverPanel;         //panel ending
    private Consumer<String> screenSwitcher;     //fungsi ganti layar pakai cardlayout

    public UIManager() {
        this.uiComponents = new HashMap<>();
    }

    //hubungkan storyPanel dengan uiManager
    public void setStoryPanel(StoryPanel storyPanel) {
        this.storyPanel = storyPanel;
        if (storyPanel != null) {
            storyPanel.setUiManager(this);
        }
    }

    //set panel ending
    public void setGameOverPanel(GameOverPanel gameOverPanel) {
        this.gameOverPanel = gameOverPanel;
    }

    //set fungsi pengganti layar
    public void setScreenSwitcher(Consumer<String> screenSwitcher) {
        this.screenSwitcher = screenSwitcher;
    }

    //fungsi utama buat ganti layar
    public void showScreen(String screenName) {
        this.currentScreen = screenName;

        //kalau pindah ke menu utama atau halaman non-cerita, timer dihentikan
        if ("MAIN_MENU".equals(screenName) || 
            "LOGIN".equals(screenName) || 
            "LEADERBOARD".equals(screenName)) 
        {
            stopDecisionTimer();
            System.out.println("[UIManager] Timer dihentikan karena pindah layar.");
        }

        //panggil cardlayout buat ganti tampilan
        if (screenSwitcher != null) {
            if (screenName.startsWith("SCENE_")) {
                screenSwitcher.accept("STORY");      //semua scene masuk lewat panel STORY
            } else {
                screenSwitcher.accept(screenName);
            }
        }

        //update isi scene kalau lagi buka panel cerita
        if (storyPanel != null && screenName.startsWith("SCENE_")) {
            SceneManager sceneManager = resolveSceneManager();
            if (sceneManager != null) {
                storyPanel.displayScene(sceneManager.getCurrentScene());
            }
        }
    }

    //update hubungan antar tokoh
    public void updateUI() {
        if (storyPanel == null) return;

        RelationshipManager relationshipManager = resolveRelationshipManager();
        if (relationshipManager != null) {
            storyPanel.updateRelationships(relationshipManager.getRelationships());
        }
    }

    //menampilkan timer untuk pilihan
    public void showDecisionTimer(int countdown) {
        updateTimer(countdown);

        Timer decisionTimer = resolveDecisionTimer();
        if (decisionTimer != null) {
            decisionTimer.stopTimer();               //stop timer lama
            decisionTimer.setTimeRemaining(countdown);

            if (decisionTimer instanceof DecisionTimer) {
                ((DecisionTimer) decisionTimer).setUiManager(this);
            }

            decisionTimer.startTimer();
        }
    }

    //dipanggil saat user memilih pilihan
    public void handleChoiceSelection(Choice choice, int choiceIndex) {
        stopDecisionTimer();                        //matikan timer dulu

        GameSession session = resolveGameSession();
        if (session == null) return;

        //mengubah index pilihan menjadi kode huruf A/B/C
        String decisionCode = String.valueOf((char) ('A' + Math.max(0, choiceIndex)));
        String decisionText = (choice != null) ? choice.getChoiceText() : null;

        session.makeDecision(decisionCode, decisionText);
    }

    //sinkronisasi tampilan timer di panel cerita
    public void updateTimer(int secondsRemaining) {
        if (storyPanel != null) {
            storyPanel.updateTimer(secondsRemaining);
        }
    }

    //menampilkan halaman ending
    public void displayEnding(String endingKey, String description, Map<String, Integer> relationships) {
        if (gameOverPanel != null) {
            gameOverPanel.setEndingInfo(endingKey, description, relationships);
        }

        if (screenSwitcher != null) {
            screenSwitcher.accept("ENDING");
        }

        stopDecisionTimer();
    }

    //ambil sceneManager dari map
    private SceneManager resolveSceneManager() {
        Object managerObj = uiComponents.get("sceneManager");
        return (managerObj instanceof SceneManager) ? (SceneManager) managerObj : null;
    }

    //ambil relationshipManager dari map
    private RelationshipManager resolveRelationshipManager() {
        Object managerObj = uiComponents.get("relationshipManager");
        return (managerObj instanceof RelationshipManager) ? (RelationshipManager) managerObj : null;
    }

    //ambil timer pilihan dari map
    private Timer resolveDecisionTimer() {
        Object timerObj = uiComponents.get("decisionTimer");
        return (timerObj instanceof Timer) ? (Timer) timerObj : null;
    }

    //matikan timer pilihan
    private void stopDecisionTimer() {
        Timer timer = resolveDecisionTimer();
        if (timer != null) {
            timer.stopTimer();
        }
    }

    //ambil session dari map
    private GameSession resolveGameSession() {
        Object sessionObj = uiComponents.get("gameSession");
        return (sessionObj instanceof GameSession) ? (GameSession) sessionObj : null;
    }

    //getter setter standar
    public Object getCurrentScreen() { 
        return currentScreen; 
    }

    public void setCurrentScreen(Object currentScreen) { 
        this.currentScreen = currentScreen; 
    }

    public Map<String, Object> getUiComponents() { 
        return uiComponents; 
    }

    public void setUiComponents(Map<String, Object> uiComponents) 
    { this.uiComponents = uiComponents; 
        
    }
}
