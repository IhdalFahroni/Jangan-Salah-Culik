package src;

import java.util.HashMap;
import java.util.Map;

public class UIManager {
    private Object currentScreen; 
    private Map<String, Object> uiComponents;
    
    public UIManager() {
        this.uiComponents = new HashMap<>();
    }
    
    public void showScreen(String screenName) {
        // TODO: Implement screen display logic
    }
    
    public void updateUI() {
        // TODO: Implement UI update logic
    }
    
    public void showDecisionTimer(int countdown) {
        // TODO: Implement decision timer display
    }
    
    // Getters and Setters
    public Object getCurrentScreen() { return currentScreen; }
    public void setCurrentScreen(Object currentScreen) { this.currentScreen = currentScreen; }
    public Map<String, Object> getUiComponents() { return uiComponents; }
    public void setUiComponents(Map<String, Object> uiComponents) { this.uiComponents = uiComponents; }
}