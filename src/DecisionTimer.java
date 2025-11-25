
import javax.swing.SwingUtilities;
import java.util.List;
import java.util.Map;

public class DecisionTimer extends Timer {
    private Choice currentChoice;
    private UIManager uiManager;

    public DecisionTimer(int initialTime) {
        super(initialTime);
    }

    public void onTimeOut() {
        if (uiManager != null) {
            SwingUtilities.invokeLater(() -> uiManager.updateTimer(0));
        }
        autoSelectDefaultChoice();
    }

    @Override
    protected void onTick(int remainingSeconds) {
        if (uiManager != null) {
            SwingUtilities.invokeLater(() -> uiManager.updateTimer(Math.max(remainingSeconds, 0)));
        }
    }

    @Override
    protected void onTimerFinished() {
        onTimeOut();
    }

    public void setUiManager(UIManager uiManager) {
        this.uiManager = uiManager;
    }

    // Getters and Setters
    public Choice getCurrentChoice() {
        return currentChoice;
    }

    public void setCurrentChoice(Choice currentChoice) {
        this.currentChoice = currentChoice;
    }

    private void autoSelectDefaultChoice() {
        if (uiManager == null) {
            return;
        }
        Map<String, Object> components = uiManager.getUiComponents();
        if (components == null) {
            return;
        }
        GameSession session = null;
        Object sessionObj = components.get("gameSession");
        if (sessionObj instanceof GameSession) {
            session = (GameSession) sessionObj;
        }
        SceneManager sceneManager = null;
        Object sceneObj = components.get("sceneManager");
        if (sceneObj instanceof SceneManager) {
            sceneManager = (SceneManager) sceneObj;
        }
        if (session == null || sceneManager == null) {
            return;
        }
        List<Choice> availableChoices = sceneManager.getAvailableChoices();
        if (availableChoices == null || availableChoices.isEmpty()) {
            return;
        }
        Choice fallbackChoice = availableChoices.get(0);
        String decisionCode = "A";
        String decisionText = fallbackChoice.getChoiceText();
        session.makeDecision(decisionCode, decisionText);
    }
}
