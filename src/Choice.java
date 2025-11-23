package src;

import java.util.HashMap;
import java.util.Map;

public class Choice {
    private int choiceId;
    private String choiceText;
    private int nextSceneId;
    private Map<String, Integer> relationshipImpact;
    private int requiredTrustLevel;
    
    public Choice() {
        this.relationshipImpact = new HashMap<>();
    }
    
    public void executeChoice() {
        // TODO: Implement choice execution logic
    }
    
    public void calculateImpact() {
        // TODO: Implement impact calculation logic
    }
    
    // Getters and Setters
    public int getChoiceId() { return choiceId; }
    public void setChoiceId(int choiceId) { this.choiceId = choiceId; }
    public String getChoiceText() { return choiceText; }
    public void setChoiceText(String choiceText) { this.choiceText = choiceText; }
    public int getNextSceneId() { return nextSceneId; }
    public void setNextSceneId(int nextSceneId) { this.nextSceneId = nextSceneId; }
    public Map<String, Integer> getRelationshipImpact() { return relationshipImpact; }
    public void setRelationshipImpact(Map<String, Integer> relationshipImpact) { this.relationshipImpact = relationshipImpact; }
    public int getRequiredTrustLevel() { return requiredTrustLevel; }
    public void setRequiredTrustLevel(int requiredTrustLevel) { this.requiredTrustLevel = requiredTrustLevel; }
}