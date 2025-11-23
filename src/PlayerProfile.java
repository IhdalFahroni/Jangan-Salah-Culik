package src;

import java.sql.Date;

public class PlayerProfile {
    private int profileId;
    private int userId;
    private String characterName;
    private String gender;
    private Date createdAt;
    
    public void createProfile() {
        // TODO: Implement profile creation logic
    }
    
    public void updateProfile() {
        // TODO: Implement profile update logic
    }
    
    public void deleteProfile() {
        // TODO: Implement profile deletion logic
    }
    
    // Getters and Setters
    public int getProfileId() { return profileId; }
    public void setProfileId(int profileId) { this.profileId = profileId; }
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public String getCharacterName() { return characterName; }
    public void setCharacterName(String characterName) { this.characterName = characterName; }
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
}