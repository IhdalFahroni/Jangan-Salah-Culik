import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class UIManager {
    private Object currentScreen;
    private Map<String, Object> uiComponents;
    private StoryPanel storyPanel;
    private GameOverPanel gameOverPanel;
    private Consumer<String> screenSwitcher;

    public UIManager() {
        this.uiComponents = new HashMap<>();
    }

    public void setStoryPanel(StoryPanel storyPanel) {
        this.storyPanel = storyPanel;
        if (storyPanel != null) {
            storyPanel.setUiManager(this);
        }
    }

    public void setGameOverPanel(GameOverPanel gameOverPanel) {
        this.gameOverPanel = gameOverPanel;
    }

    public void setScreenSwitcher(Consumer<String> screenSwitcher) {
        this.screenSwitcher = screenSwitcher;
    }

    public void showScreen(String screenName) {
        this.currentScreen = screenName;
        if (storyPanel != null && screenName != null && screenName.startsWith("SCENE_")) {
            SceneManager sceneManager = resolveSceneManager();
            if (sceneManager != null) {
                storyPanel.displayScene(sceneManager.getCurrentScene());
            }
        }
    }

    public void updateUI() {
        if (storyPanel == null) {
            return;
        }
        RelationshipManager relationshipManager = resolveRelationshipManager();
        if (relationshipManager != null) {
            storyPanel.updateRelationships(relationshipManager.getRelationships());
        }
    }

    public void showDecisionTimer(int countdown) {
        updateTimer(countdown);
        Timer decisionTimer = resolveDecisionTimer();
        if (decisionTimer != null) {
            decisionTimer.stopTimer();
            decisionTimer.setTimeRemaining(countdown);
            if (decisionTimer instanceof DecisionTimer) {
                ((DecisionTimer) decisionTimer).setUiManager(this);
            }
            decisionTimer.startTimer();
        }
    }

    public void handleChoiceSelection(Choice choice, int choiceIndex) {
        stopDecisionTimer();
        GameSession session = resolveGameSession();
        if (session == null) {
            return;
        }
        String decisionCode = String.valueOf((char) ('A' + Math.max(0, choiceIndex)));
        String decisionText = choice != null ? choice.getChoiceText() : null;
        session.makeDecision(decisionCode, decisionText);
    }

    public void updateTimer(int secondsRemaining) {
        if (storyPanel != null) {
            storyPanel.updateTimer(secondsRemaining);
        }
    }

    public void displayEnding(String endingKey, String description, Map<String, Integer> relationships) {
        if (gameOverPanel != null) {
            gameOverPanel.setEndingInfo(endingKey, description, relationships);
        }
        if (screenSwitcher != null) {
            screenSwitcher.accept("ENDING");
        }
    }

    private SceneManager resolveSceneManager() {
        if (uiComponents == null) {
            return null;
        }
        Object managerObj = uiComponents.get("sceneManager");
        if (managerObj instanceof SceneManager) {
            return (SceneManager) managerObj;
        }
        return null;
    }

    private RelationshipManager resolveRelationshipManager() {
        if (uiComponents == null) {
            return null;
        }
        Object managerObj = uiComponents.get("relationshipManager");
        if (managerObj instanceof RelationshipManager) {
            return (RelationshipManager) managerObj;
        }
        return null;
    }

    private Timer resolveDecisionTimer() {
        if (uiComponents == null) {
            return null;
        }
        Object timerObj = uiComponents.get("decisionTimer");
        if (timerObj instanceof Timer) {
            return (Timer) timerObj;
        }
        return null;
    }

    private void stopDecisionTimer() {
        Timer timer = resolveDecisionTimer();
        if (timer != null) {
            timer.stopTimer();
        }
    }

    private GameSession resolveGameSession() {
        if (uiComponents == null) {
            return null;
        }
        Object sessionObj = uiComponents.get("gameSession");
        if (sessionObj instanceof GameSession) {
            return (GameSession) sessionObj;
        }
        return null;
    }

    // Getters and Setters
    public Object getCurrentScreen() {
        return currentScreen;
    }

    public void setCurrentScreen(Object currentScreen) {
        this.currentScreen = currentScreen;
    }

    public Map<String, Object> getUiComponents() {
        return uiComponents;
    }

    public void setUiComponents(Map<String, Object> uiComponents) {
        this.uiComponents = uiComponents;
    }
}
