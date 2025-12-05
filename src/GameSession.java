
import java.util.HashMap;
import java.util.Map;
import java.util.List;
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
    private DatabaseManager dbManager; 
    private SceneManager sceneManager;
    private RelationshipManager relationshipManager; 

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
        
        // Inisialisasi helper logic sekali saja
        this.relationshipManager = new RelationshipManager();
    }

    public GameSession(UIManager uiManager) {
        this();
        setUiManager(uiManager);
    }

    public void setUiManager(UIManager uiManager) {
        this.uiManager = uiManager;
        // Cache SceneManager dan DB Manager saat UI Manager diset
        if (uiManager != null) {
            this.sceneManager = new SceneManager(uiManager);
            
            // Ambil DB Manager sekali saja dari UI Components
            Map<String, Object> comps = uiManager.getUiComponents();
            if (comps != null && comps.containsKey("databaseManager")) {
                this.dbManager = (DatabaseManager) comps.get("databaseManager");
            }
        }
    }
    public void setDbManager(DatabaseManager dbManager) {
        this.dbManager = dbManager;
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
        this.relationships.put("SOEKARNO", relationshipSoekarno);
        this.relationships.put("HATTA", relationshipHatta);
        this.relationships.put("TRUST", trustLevel);
        this.relationships.put("PEMUDA", 50);

        System.out.println("New game started from scene 1");
        syncUiState(30);
        saveProgress();
    }

    public void saveProgressAsync() {
        if (dbManager == null || sessionId <= 0) return;
        
        // Copy state saat ini agar thread aman
        final GameSession snapshot = new GameSession();
        snapshot.setSessionId(this.sessionId);
        snapshot.setCurrentScene(this.currentScene);
        snapshot.setRelationshipSoekarno(this.relationshipSoekarno);
        snapshot.setRelationshipHatta(this.relationshipHatta);
        snapshot.setTrustLevel(this.trustLevel);
        snapshot.setEndingAchieved(this.endingAchieved);

        new Thread(() -> {
            dbManager.saveGameProgress(snapshot);
            System.out.println("Game progress saved (Async) - Scene: " + snapshot.getCurrentScene());
        }).start();
    }

    public void saveProgress() {
        this.lastUpdated = new Date();
        saveProgressAsync();
    }

    public void loadProgress() {
        if (relationships == null) relationships = new HashMap<>();
        
        // Pastikan nilai valid
        if (!relationships.containsKey("PEMUDA")) relationships.put("PEMUDA", 50);
        relationshipSoekarno = Math.max(0, relationshipSoekarno);
        relationshipHatta = Math.max(0, relationshipHatta);
        trustLevel = Math.max(0, trustLevel);

        relationships.put("SOEKARNO", relationshipSoekarno);
        relationships.put("HATTA", relationshipHatta);
        relationships.put("TRUST", trustLevel);
        
        syncUiState(30);
        System.out.println("Game progress loaded - Scene: " + currentScene);
    }

    public void initializeForUser(int profileId) {
        this.profileId = profileId;
        loadProgress();
        System.out.println("Game session initialized for profile: " + profileId);
    }

    public void makeDecision(String decisionCode, String decisionText) {
        int sceneBeforeDecision = currentScene;

        // 1. Update Internal Relationship Manager
        relationshipManager.setRelationships(new HashMap<>(this.relationships));

        // 2. Resolve Choice (Tanpa lookup map berulang)
        Choice selectedChoice = null;
        if (sceneManager != null) {
            List<Choice> availableChoices = sceneManager.getAvailableChoices();
            if (availableChoices != null && !availableChoices.isEmpty()) {
                int choiceIndex = 0;
                // Parsing logic yang lebih simpel
                if (decisionCode != null && !decisionCode.isEmpty()) {
                    char c = decisionCode.toUpperCase().charAt(0);
                    if (c >= 'A' && c <= 'Z') choiceIndex = c - 'A';
                    else {
                        try { choiceIndex = Integer.parseInt(decisionCode) - 1; } 
                        catch (NumberFormatException e) { choiceIndex = 0; }
                    }
                }
                if (choiceIndex >= 0 && choiceIndex < availableChoices.size()) {
                    selectedChoice = availableChoices.get(choiceIndex);
                } else {
                    selectedChoice = availableChoices.get(0);
                }
            }
        }

        // 3. Execute Decision Logic
        if (selectedChoice != null) {
            // Record decision flags
            if (currentScene == 6 || currentScene == 7 || currentScene == 9) {
                relationships.put("SCENE" + currentScene + "_DECISION", selectedChoice.getChoiceId());
            }

            // Trust Check
            if (selectedChoice.getRequiredTrustLevel() > 0 && trustLevel < selectedChoice.getRequiredTrustLevel()) {
                System.out.println("Trust level too low. Fallback logic needed here."); 
                // Note: Logic fallback harusnya mengubah selectedChoice, tapi di sini kita lanjut saja sesuai kode asli
            } else {
                selectedChoice.executeChoice();
            }

            // Update UI Component Last Choice
            if (uiManager != null && uiManager.getUiComponents() != null) {
                uiManager.getUiComponents().put("lastChoice", selectedChoice);
            }

            // Apply Impact
            Map<String, Integer> impact = selectedChoice.getRelationshipImpact();
            if (impact != null && !impact.isEmpty()) {
                for (Map.Entry<String, Integer> entry : impact.entrySet()) {
                    relationshipManager.updateRelationship(entry.getKey(), entry.getValue());
                    // Sync local vars
                    updateLocalStatsFromManager(); 
                }
            }
        }

        // Hardcoded logic tambahan
        updateRelationshipsHardcoded(decisionCode);

        // 4. Async Database Operations (PENTING AGAR TIDAK LEMOT)
        final Choice finalChoice = selectedChoice;
        if (dbManager != null) {
            new Thread(() -> {
                persistDecision(sceneBeforeDecision, finalChoice, decisionCode, decisionText);
            }).start();
        }

        // 5. Cek Ending
        if (currentScene == 9) {
            endingAchieved = calculateEnding();
            if (endingAchieved != null && !endingAchieved.isEmpty()) {
                handleGameCompletion();
                return;
            }
        }

        // 6. Pindah Scene
        moveToNextScene(decisionCode, selectedChoice);
        
        // 7. Simpan Progress (Async)
        saveProgressAsync();

        // 8. Update UI
        if (uiManager != null) {
            uiManager.updateUI();
        }
        lastUpdated = new Date();
    }

    private void updateLocalStatsFromManager() {
        relationships.putAll(relationshipManager.getRelationships());
        relationshipSoekarno = relationships.getOrDefault("SOEKARNO", 50);
        relationshipHatta = relationships.getOrDefault("HATTA", 50);
        trustLevel = relationships.getOrDefault("TRUST", 50);
    }

    private void updateRelationshipsHardcoded(String decisionCode) {
        // Logic Hardcoded Scene 2 & 5
        switch (currentScene) {
            case 2:
                if ("A".equals(decisionCode)) { relationshipSoekarno += 10; trustLevel += 5; }
                else { relationshipSoekarno -= 30; trustLevel -= 10; }
                break;
            case 5:
                if ("A".equals(decisionCode)) { relationshipSoekarno += 5; relationshipHatta += 10; }
                else { relationshipSoekarno += 15; trustLevel -= 5; }
                break;
        }
        // Clamp values
        relationshipSoekarno = clamp(relationshipSoekarno);
        relationshipHatta = clamp(relationshipHatta);
        trustLevel = clamp(trustLevel);
        
        // Sync map
        relationships.put("SOEKARNO", relationshipSoekarno);
        relationships.put("HATTA", relationshipHatta);
        relationships.put("TRUST", trustLevel);
    }

    private int clamp(int val) { return Math.max(0, Math.min(100, val)); }

    private void moveToNextScene(String decisionCode, Choice lastChoice) {
        int targetScene = currentScene < 9 ? currentScene + 1 : currentScene;
        
        if (lastChoice != null && lastChoice.getNextSceneId() > 0) {
            targetScene = lastChoice.getNextSceneId();
        }
        if (targetScene > 9) targetScene = 9;
        
        currentScene = targetScene;
        
        // Reuse SceneManager, jangan buat baru
        if (sceneManager == null && uiManager != null) {
            sceneManager = new SceneManager(uiManager);
        }
        
        if (sceneManager != null) {
            sceneManager.loadScene(currentScene);
        }
        
        if (uiManager != null) {
            uiManager.showScreen("SCENE_" + currentScene);
            uiManager.showDecisionTimer(30);
            uiManager.updateUI();
        }
        
        // Cek Ending lagi jika scene > 9 (just in case)
        if (currentScene > 9) {
            endingAchieved = calculateEnding();
            if (endingAchieved != null) handleGameCompletion();
        }
    }

    public String calculateEnding() {
        int pemudaTrust = relationships.getOrDefault("PEMUDA", 50);
        int scene6 = relationships.getOrDefault("SCENE6_DECISION", 0);
        int scene7 = relationships.getOrDefault("SCENE7_DECISION", 0);
        int scene9 = relationships.getOrDefault("SCENE9_DECISION", 0);

        if (scene7 == 1 || scene9 == 1 || trustLevel <= 30) return "BAD_ENDING_DIHANCURKAN_JEPANG";
        if (trustLevel < 40) return "BAD_ENDING_PENJAJAH_KEMBALI";
        if (relationshipSoekarno <= 30 || scene6 == 2) return "BAD_ENDING_PROKLAMASI_GAGAL";
        if (pemudaTrust <= 20 && relationshipHatta <= 30) return "BAD_ENDING_DIKHIANATI";
        
        if (relationshipSoekarno >= 70 && relationshipHatta >= 60 && pemudaTrust >= 50 && trustLevel >= 80 && scene9 == 2) 
            return "TRUE_ENDING_INDONESIA_MERDEKA";
            
        if (relationshipSoekarno >= 50 && relationshipHatta >= 40 && trustLevel >= 60 && pemudaTrust >= 30) 
            return "GOOD_ENDING_KEMERDEKAAN_TEGANG";
            
        return "ENDING_TIDAK_PASTI";
    }

    public boolean hasExistingProgress() {
        return endingAchieved == null && currentScene > 1;
    }

    public String getCurrentSceneTitle() {
        switch (currentScene) {
            case 1:
                return "The Spark (Markas Menteng 31)";
            case 2:
                return "The Confrontation (Rumah Soekarno)";
            case 3:
                return "The Roadblock (Perjalanan ke Rengasdengklok)";
            case 4:
                return "The Shelter (Rumah Djiaw Kie Siong)";
            case 5:
                return "The Persuasion (Debat Merdeka)";
            case 6:
                return "The Guarantee (Kedatangan Ahmad Soebardjo)";
            case 7:
                return "The General (Rumah Laksamana Maeda)";
            case 8:
                return "The Wording (Perumusan Naskah)";
            case 9:
                return "The Venue (Lokasi Proklamasi)";
            default:
                return "Unknown Scene";
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
    public int getSessionId() {
        return sessionId;
    }

    public void setSessionId(int sessionId) {
        this.sessionId = sessionId;
    }

    public int getProfileId() {
        return profileId;
    }

    public void setProfileId(int profileId) {
        this.profileId = profileId;
    }

    public int getCurrentScene() {
        return currentScene;
    }

    public void setCurrentScene(int currentScene) {
        this.currentScene = currentScene;
    }

    public int getRelationshipSoekarno() {
        return relationshipSoekarno;
    }

    public void setRelationshipSoekarno(int relationshipSoekarno) {
        this.relationshipSoekarno = relationshipSoekarno;
    }

    public int getRelationshipHatta() {
        return relationshipHatta;
    }

    public void setRelationshipHatta(int relationshipHatta) {
        this.relationshipHatta = relationshipHatta;
    }

    public int getTrustLevel() {
        return trustLevel;
    }

    public void setTrustLevel(int trustLevel) {
        this.trustLevel = trustLevel;
    }

    public String getEndingAchieved() {
        return endingAchieved;
    }

    public void setEndingAchieved(String endingAchieved) {
        this.endingAchieved = endingAchieved;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public void setCreatedAt(Timestamp timestamp) {
        if (timestamp != null) {
            this.createdAt = new Date(timestamp.getTime());
        }
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public void setLastUpdated(Timestamp timestamp) {
        if (timestamp != null) {
            this.lastUpdated = new Date(timestamp.getTime());
        }
    }

    public Map<String, Integer> getRelationships() {
        return relationships;
    }

    public void setRelationships(Map<String, Integer> relationships) {
        this.relationships = relationships;
    }

    public UIManager getUiManager() {
        return uiManager;
    }

    private void handleGameCompletion() {
        if (uiManager != null) {
            Map<String, Object> comps = uiManager.getUiComponents();
            if (comps != null && comps.containsKey("decisionTimer")) {
                 Object timer = comps.get("decisionTimer");
                 // Reflection atau casting aman
                 if (timer instanceof Timer) ((Timer) timer).stopTimer();
            }
        }
        
        // Simpan ending (Async)
        new Thread(this::persistEndingResult).start();
        
        if (uiManager != null) {
            uiManager.displayEnding(endingAchieved, describeEnding(endingAchieved), new HashMap<>(relationships));
        }
    }

    private String describeEnding(String endingKey) {
        if (endingKey == null) {
            return "Perjalananmu belum mencapai akhir yang jelas.";
        }
        switch (endingKey) {
            case "BAD_ENDING_DIHANCURKAN_JEPANG":
                return "Pasukan Jepang berhasil memadamkan gerakanmu. Proklamasi gagal dibacakan.";
            case "BAD_ENDING_PENJAJAH_KEMBALI":
                return "Keraguan membuat penjajah memanfaatkan situasi. Indonesia tetap terbelenggu.";
            case "BAD_ENDING_PROKLAMASI_GAGAL":
                return "Soekarno dan Hatta kehilangan keyakinan. Naskah proklamasi tak pernah rampung.";
            case "BAD_ENDING_DIKHIANATI":
                return "Ketidakpercayaan antar faksi membuat perjuangan pecah dan gagal.";
            case "TRUE_ENDING_INDONESIA_MERDEKA":
                return "Kerja sama pemuda dan dwitunggal berhasil. Proklamasi berkumandang dan bangsa merdeka!";
            case "GOOD_ENDING_KEMERDEKAAN_TEGANG":
                return "Indonesia merdeka, namun ketegangan politik mengintai hari-hari pertama.";
            case "ENDING_TIDAK_PASTI":
            default:
                return "Takdir Indonesia masih kabur. Mungkin waktunya mencoba strategi lain.";
        }
    }

    private void persistEndingResult() {
        if (dbManager == null) return;

        // Ambil ID langsung dari field jika ada, atau fallback ke UI map
        int uId = resolveComponentId("currentUserId", 0);
        
        if (uId <= 0 || sessionId <= 0) return;

        GameSession session = new GameSession();
        session.setSessionId(sessionId);
        session.setCurrentScene(currentScene);
        session.setRelationshipSoekarno(relationshipSoekarno);
        session.setRelationshipHatta(relationshipHatta);
        session.setTrustLevel(trustLevel);
        session.setEndingAchieved(endingAchieved);

        dbManager.saveGameProgress(session);
        System.out.println("Story result saved successfully.");
    }

    private void syncUiState(int countdownSeconds) {
        if (uiManager == null) return;
        Map<String, Object> uiComponents = uiManager.getUiComponents();
        if (uiComponents == null) return;

        if (relationships == null) relationships = new HashMap<>();
        
        uiComponents.put("gameSession", this);
        updateLocalStatsFromManager();
        relationships.putIfAbsent("PEMUDA", 50);

        // Jangan buat baru, reuse
        relationshipManager.setRelationships(new HashMap<>(relationships));
        uiComponents.put("relationshipManager", relationshipManager);

        // Load scene
        if (sceneManager == null) sceneManager = new SceneManager(uiManager);
        uiComponents.put("sceneManager", sceneManager);
        
        int sceneToShow = currentScene > 0 ? currentScene : 1;
        sceneManager.loadScene(sceneToShow);

        if (countdownSeconds > 0) {
            uiManager.showScreen("SCENE_" + sceneToShow);
            uiManager.showDecisionTimer(countdownSeconds);
            uiManager.updateUI();
        }
    }

    private void persistDecision(int sceneNumber, Choice selectedChoice, String decisionCode, String decisionText) {
        if (dbManager == null || sessionId <= 0) return;

        String label = decisionText;
        if ((label == null || label.isEmpty()) && selectedChoice != null) {
            label = selectedChoice.getChoiceText();
        }
        String code = decisionCode;
        if ((code == null || code.isEmpty()) && selectedChoice != null) {
            code = String.valueOf(selectedChoice.getChoiceId());
        }
        if (code != null) code = code.trim().toUpperCase();

        dbManager.savePlayerDecision(sessionId, sceneNumber, code, label);
    }

    private int resolveComponentId(String key, int fallback) {
        if (uiManager == null || uiManager.getUiComponents() == null) return fallback;
        Object val = uiManager.getUiComponents().get(key);
        return (val instanceof Integer) ? (Integer) val : fallback;
    }
}
