package src;

public class Character {
    private int characterId;
    private String characterName;
    private String characterRole;
    private String imagePath;
    private int currentRelationship;
    
    public void displayCharacter() {
        // TODO: Implement character display logic
    }
    
    public void updateRelationship(int change) {
        // TODO: Implement relationship update logic
    }
    
    // Getters and Setters
    public int getCharacterId() { return characterId; }
    public void setCharacterId(int characterId) { this.characterId = characterId; }
    public String getCharacterName() { return characterName; }
    public void setCharacterName(String characterName) { this.characterName = characterName; }
    public String getCharacterRole() { return characterRole; }
    public void setCharacterRole(String characterRole) { this.characterRole = characterRole; }
    public String getImagePath() { return imagePath; }
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }
    public int getCurrentRelationship() { return currentRelationship; }
    public void setCurrentRelationship(int currentRelationship) { this.currentRelationship = currentRelationship; }
}