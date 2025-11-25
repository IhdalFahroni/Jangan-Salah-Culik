
import java.util.HashMap;
import java.util.Map;

public class RelationshipManager {
    private Map<String, Integer> relationships;

    public RelationshipManager() {
        this.relationships = new HashMap<>();
    }

    public void updateRelationship(String character, int value) {
        if (character == null) {
            return;
        }
        int currentValue = relationships.getOrDefault(character, 50);
        int newValue = Math.max(0, Math.min(100, currentValue + value));
        relationships.put(character, newValue);
        System.out.println("Relationship with " + character + " changed by " + value + " → " + newValue);
    }

    public int getRelationship(String character) {
        if (character == null) {
            return 0;
        }
        return relationships.getOrDefault(character, 50);
    }

    public boolean checkRelationshipRequirement(int requiredLevel) {
        for (int value : relationships.values()) {
            if (value >= requiredLevel) {
                return true;
            }
        }
        return relationships.isEmpty() && requiredLevel <= 50;
    }

    // Getters and Setters
    public Map<String, Integer> getRelationships() {
        return relationships;
    }

    public void setRelationships(Map<String, Integer> relationships) {
        this.relationships = relationships;
    }
}
