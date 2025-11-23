package src;

public class DecisionTimer extends Timer {
    private Choice currentChoice;
    
    public DecisionTimer(int initialTime) {
        super(initialTime);
    }
    
    public void onTimeOut() {
        // TODO: Implement timeout logic for decisions
    }
    
    // Getters and Setters
    public Choice getCurrentChoice() { return currentChoice; }
    public void setCurrentChoice(Choice currentChoice) { this.currentChoice = currentChoice; }
}