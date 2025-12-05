    import java.util.HashMap;
    import java.util.Map;
    import java.util.function.Consumer;
    import javax.swing.Timer; // Pakai Timer bawaan Swing (Aman)

    public class UIManager {

        private Object currentScreen;
        private Map<String, Object> uiComponents;
        private StoryPanel storyPanel;
        private GameOverPanel gameOverPanel;
        private Consumer<String> screenSwitcher;

        // Timer Swing (Pengganti DecisionTimer custom)
        private Timer decisionTimer; 
        private int secondsLeft;

        public UIManager() {
            this.uiComponents = new HashMap<>();
        }

        // --- LOGIKA TIMER BARU (AMAN) ---
        
        public void showDecisionTimer(int durationSeconds) {
            // 1. Matikan timer lama jika ada (Wajib!)
            stopDecisionTimer();

            // 2. Reset waktu
            this.secondsLeft = durationSeconds;
            updateTimerUI(secondsLeft);

            // 3. Buat Timer baru (Tick setiap 1 detik / 1000ms)
            decisionTimer = new Timer(1000, e -> {
                secondsLeft--;
                updateTimerUI(secondsLeft);

                if (secondsLeft <= 0) {
                    ((Timer)e.getSource()).stop(); // Stop diri sendiri
                    autoSelectChoice(); // Waktu habis, pilih otomatis
                }
            });
            
            decisionTimer.setRepeats(true);
            decisionTimer.start();
            System.out.println("[UIManager] Timer started: " + durationSeconds + "s");
        }

        public void stopDecisionTimer() {
            if (decisionTimer != null && decisionTimer.isRunning()) {
                decisionTimer.stop();
                decisionTimer = null; // Hapus referensi
                System.out.println("[UIManager] Timer stopped.");
            }
        }

        private void autoSelectChoice() {
            System.out.println("[UIManager] Waktu habis! Memilih otomatis...");
            
            // Ambil SceneManager untuk tahu pilihan yang tersedia
            SceneManager sm = resolveSceneManager();
            if (sm == null) return;
            
            java.util.List<Choice> choices = sm.getAvailableChoices();
            if (choices == null || choices.isEmpty()) return;

            // Pilih opsi pertama (A) sebagai default
            handleChoiceSelection(choices.get(0), 0);
        }

        private void updateTimerUI(int seconds) {
            if (storyPanel != null) {
                storyPanel.updateTimer(seconds);
            }
        }

        // --- SISA KODE LAMA (TETAP SAMA) ---

        public void handleChoiceSelection(Choice choice, int choiceIndex) {
            // PENTING: Matikan timer segera saat user klik
            stopDecisionTimer();
            
            // Matikan tombol agar tidak bisa klik ganda (Fix previous issue)
            if (storyPanel != null) {
                storyPanel.setButtonsEnabled(false);
            }

            GameSession session = resolveGameSession();
            if (session == null) return;

            String decisionCode = String.valueOf((char) ('A' + Math.max(0, choiceIndex)));
            String decisionText = (choice != null) ? choice.getChoiceText() : null;

            session.makeDecision(decisionCode, decisionText);
        }

        public void showScreen(String screenName) {
            this.currentScreen = screenName;

            // Matikan timer jika keluar dari story
            if (!screenName.startsWith("SCENE_") && !"STORY".equals(screenName)) {
                stopDecisionTimer();
            }

            if (screenSwitcher != null) {
                if (screenName.startsWith("SCENE_")) {
                    screenSwitcher.accept("STORY");
                } else {
                    screenSwitcher.accept(screenName);
                }
            }

            if (storyPanel != null && screenName.startsWith("SCENE_")) {
                SceneManager sceneManager = resolveSceneManager();
                if (sceneManager != null) {
                    // Pastikan SceneManager sudah memuat scene terbaru sebelum ditampilkan
                    storyPanel.displayScene(sceneManager.getCurrentScene());
                }
            }
        }

        public void updateUI() {
            if (storyPanel == null) return;
            RelationshipManager relationshipManager = resolveRelationshipManager();
            if (relationshipManager != null) {
                storyPanel.updateRelationships(relationshipManager.getRelationships());
            }
        }

        public void displayEnding(String endingKey, String description, Map<String, Integer> relationships) {
            stopDecisionTimer(); // Pastikan timer mati saat ending
            if (gameOverPanel != null) {
                gameOverPanel.setEndingInfo(endingKey, description, relationships);
            }
            if (screenSwitcher != null) {
                screenSwitcher.accept("ENDING");
            }
        }

        // --- RESOLVERS & SETTERS ---

        public void setStoryPanel(StoryPanel storyPanel) {
            this.storyPanel = storyPanel;
            if (storyPanel != null) storyPanel.setUiManager(this);
        }

        public void setGameOverPanel(GameOverPanel gameOverPanel) {
            this.gameOverPanel = gameOverPanel;
        }

        public void setScreenSwitcher(Consumer<String> screenSwitcher) {
            this.screenSwitcher = screenSwitcher;
        }

        private SceneManager resolveSceneManager() {
            Object obj = uiComponents.get("sceneManager");
            return (obj instanceof SceneManager) ? (SceneManager) obj : null;
        }

        private RelationshipManager resolveRelationshipManager() {
            Object obj = uiComponents.get("relationshipManager");
            return (obj instanceof RelationshipManager) ? (RelationshipManager) obj : null;
        }

        private GameSession resolveGameSession() {
            Object obj = uiComponents.get("gameSession");
            return (obj instanceof GameSession) ? (GameSession) obj : null;
        }

        public Map<String, Object> getUiComponents() { return uiComponents; }
        public void setUiComponents(Map<String, Object> uiComponents) { this.uiComponents = uiComponents; }
    }