
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
        System.out.println("Executing choice: " + choiceText);
        calculateImpact();
        if (nextSceneId > 0) {
            System.out.println("Next scene will be: " + nextSceneId);
        }
    }

    public void calculateImpact() {
        if (relationshipImpact == null || relationshipImpact.isEmpty()) {
            System.out.println("No relationship impact for this choice.");
            return;
        }
        for (Map.Entry<String, Integer> entry : relationshipImpact.entrySet()) {
            System.out.println("Impact on " + entry.getKey() + ": " + entry.getValue());
        }
    }

    // Getters and Setters
    public int getChoiceId() {
        return choiceId;
    }

    public void setChoiceId(int choiceId) {
        this.choiceId = choiceId;
    }

    public String getChoiceText() {
        return choiceText;
    }

    public void setChoiceText(String choiceText) {
        this.choiceText = choiceText;
    }

    public int getNextSceneId() {
        return nextSceneId;
    }

    public void setNextSceneId(int nextSceneId) {
        this.nextSceneId = nextSceneId;
    }

    public Map<String, Integer> getRelationshipImpact() {
        return relationshipImpact;
    }

    public void setRelationshipImpact(Map<String, Integer> relationshipImpact) {
        this.relationshipImpact = relationshipImpact;
    }

    public int getRequiredTrustLevel() {
        return requiredTrustLevel;
    }

    public void setRequiredTrustLevel(int requiredTrustLevel) {
        this.requiredTrustLevel = requiredTrustLevel;
    }
}
