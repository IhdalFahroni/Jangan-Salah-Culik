package src;

import java.util.Date;

public class ScoreEntry {
    private String username;
    private String characterName;
    private int score;
    private int timeTaken;
    private Date attemptedAt;
    
    // Getters and Setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getCharacterName() { return characterName; }
    public void setCharacterName(String characterName) { this.characterName = characterName; }
    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }
    public int getTimeTaken() { return timeTaken; }
    public void setTimeTaken(int timeTaken) { this.timeTaken = timeTaken; }
    public Date getAttemptedAt() { return attemptedAt; }
    public void setAttemptedAt(Date attemptedAt) { this.attemptedAt = attemptedAt; }
    
    @Override
    public String toString() {
        return String.format("%s (%s) - %d points - %d seconds", 
            username, characterName, score, timeTaken);
    }
}