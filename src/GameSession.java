package src;

import java.util.HashMap;
import java.util.Map;
import java.util.Date; 
import java.sql.Timestamp;

public class GameSession {
    private int sessionId;
    private int profileId;
    private int currentScene;
    private int relationshipSoekarno;
    private int relationshipHatta;
    private int trustLevel;
    private String endingAchieved;
    private Date createdAt; 
    private Date lastUpdated; 
    private Map<String, Integer> relationships;
    private UIManager uiManager;
    
    public GameSession() {
        this.relationships = new HashMap<>(); 
        this.sessionId = 0;
        this.profileId = 0;
        this.currentScene = 1;
        this.relationshipSoekarno = 50;
        this.relationshipHatta = 50;
        this.trustLevel = 50;
        this.endingAchieved = null;
        this.createdAt = new Date();
        this.lastUpdated = new Date();
        this.uiManager = null;
    }
    

    public GameSession(UIManager uiManager) {
        this(); // Panggil constructor default dulu
        this.uiManager = uiManager;
    }
    
    public void setUiManager(UIManager uiManager) {
        this.uiManager = uiManager;
    }
    
    public void startNewGame() {
        // Reset semua values buat game baru
        this.currentScene = 1;
        this.relationshipSoekarno = 50;
        this.relationshipHatta = 50;
        this.trustLevel = 50;
        this.endingAchieved = null;
        this.lastUpdated = new Date();
        
        // Clear relationships map
        this.relationships.clear();
        
        System.out.println("New game started from scene 1");
    }
    
    public void saveProgress() {
        // TODO: Implement save game logic
        // Akan dipanggil oleh DatabaseManager.saveGameProgress()
        System.out.println("Game progress saved - Scene: " + currentScene);
    }
    
    public void loadProgress() {
        // TODO: Implement load game logic  
        // Akan dipanggil setelah data di-load dari database
        System.out.println("Game progress loaded - Scene: " + currentScene);
    }

    public void initializeForUser(int profileId) {
        this.profileId = profileId;
        // TODO: Load saved progress dari database jika ada
        System.out.println("Game session initialized for profile: " + profileId);
    }
    
    public void makeDecision(String decisionCode, String decisionText) {
        // Update relationships berdasarkan decision dan current scene
        updateRelationships(decisionCode);
        
        // pindah ke scene selanjutnya berdasarkan decision atau sequential (emg ud waktux pindah)
        moveToNextScene(decisionCode);
        
        // Save decision ke database
        // dbManager.savePlayerDecision(sessionId, currentScene, decisionCode, decisionText);
        
        // Update UI jika UIManager tersedia
        if (uiManager != null) {
            uiManager.updateUI();
        }
        
        lastUpdated = new Date();
        
        System.out.println("Decision made: " + decisionCode + " at scene " + currentScene);
    }
    
    private void updateRelationships(String decisionCode) {
        // Logic berdasarkan scene dan decision, ini cm contoh yeah
        switch (currentScene) {
            case 2: // The Confrontation
                if (decisionCode.equals("A")) {
                    relationshipSoekarno += 10; // Diplomatis
                    trustLevel += 5;
                } else {
                    relationshipSoekarno -= 30; // Keras
                    trustLevel -= 10;
                }
                break;
                
            case 5: // The Persuasion  
                if (decisionCode.equals("A")) {
                    relationshipSoekarno += 5; // Fakta
                    relationshipHatta += 10; // Hatta suka pendekatan logis
                } else {
                    relationshipSoekarno += 15; // Ancaman (efektif di sejarah)
                    trustLevel -= 5;
                }
                break;
                
            // Tambahin scene-scene lainnya
        }
        
        relationshipSoekarno = Math.max(0, Math.min(100, relationshipSoekarno));
        relationshipHatta = Math.max(0, Math.min(100, relationshipHatta));
        trustLevel = Math.max(0, Math.min(100, trustLevel));
    }
    
    private void moveToNextScene(String decisionCode) {
        // Logic untuk pindah scene berdasarkan decision
        // Bisa sequential atau berdasarkan choice tertentu
        if (currentScene < 9) { // Ada 9 scene total
            currentScene++;
        } else {
            // Game finished, calculate ending
            endingAchieved = calculateEnding();
        }
    }
    
    public String calculateEnding() {
        // Check relationship-based endings dulu
        if (relationshipSoekarno <= 20) {
            return "SOEKARNO_MENOLAK";
        }
        if (relationshipHatta <= 20) {
            return "HATTA_MENGUNDURKAN_DIRI";
        }
        if (trustLevel <= 30) {
            return "TIDAK_DIPERCAYA_PEMUDA";
        }
        
        // Check specific bad endings berdasarkan scene progression
        // (Ini akan diimplement lengkap ketika ada tracking decisions)
        
        // Jika semua baik, TRUE ENDING
        return "INDONESIA_MERDEKA";
    }
    
    public String getCurrentSceneTitle() {
        switch (currentScene) {
            case 1: return "The Spark (Markas Menteng 31)";
            case 2: return "The Confrontation (Rumah Soekarno)";
            case 3: return "The Roadblock (Perjalanan ke Rengasdengklok)";
            case 4: return "The Shelter (Rumah Djiaw Kie Siong)";
            case 5: return "The Persuasion (Debat Merdeka)";
            case 6: return "The Guarantee (Kedatangan Ahmad Soebardjo)";
            case 7: return "The General (Rumah Laksamana Maeda)";
            case 8: return "The Wording (Perumusan Naskah)";
            case 9: return "The Venue (Lokasi Proklamasi)";
            default: return "Unknown Scene";
        }
    }
    
    // cek selese
    public boolean isGameCompleted() {
        return endingAchieved != null;
    }
    
    // progress
    public double getGameProgress() {
        return (currentScene / 9.0) * 100.0; // 9 scene total
    }
    
    // Getters and Setters
    public int getSessionId() { return sessionId; }
    public void setSessionId(int sessionId) { this.sessionId = sessionId; }
    
    public int getProfileId() { return profileId; }
    public void setProfileId(int profileId) { this.profileId = profileId; }
    
    public int getCurrentScene() { return currentScene; }
    public void setCurrentScene(int currentScene) { this.currentScene = currentScene; }
    
    public int getRelationshipSoekarno() { return relationshipSoekarno; }
    public void setRelationshipSoekarno(int relationshipSoekarno) { 
        this.relationshipSoekarno = relationshipSoekarno; 
    }
    
    public int getRelationshipHatta() { return relationshipHatta; }
    public void setRelationshipHatta(int relationshipHatta) { 
        this.relationshipHatta = relationshipHatta; 
    }
    
    public int getTrustLevel() { return trustLevel; }
    public void setTrustLevel(int trustLevel) { this.trustLevel = trustLevel; }
    
    public String getEndingAchieved() { return endingAchieved; }
    public void setEndingAchieved(String endingAchieved) { this.endingAchieved = endingAchieved; }
    
    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
    
    public void setCreatedAt(Timestamp timestamp) {
        if (timestamp != null) {
            this.createdAt = new Date(timestamp.getTime());
        }
    }
    
    public Date getLastUpdated() { return lastUpdated; }
    public void setLastUpdated(Date lastUpdated) { this.lastUpdated = lastUpdated; }
    
    public void setLastUpdated(Timestamp timestamp) {
        if (timestamp != null) {
            this.lastUpdated = new Date(timestamp.getTime());
        }
    }
    
    public Map<String, Integer> getRelationships() { return relationships; }
    public void setRelationships(Map<String, Integer> relationships) { 
        this.relationships = relationships; 
    }
    
    public UIManager getUiManager() { return uiManager; }
}