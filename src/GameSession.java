
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
        this.relationships.put("SOEKARNO", relationshipSoekarno);
        this.relationships.put("HATTA", relationshipHatta);
        this.relationships.put("TRUST", trustLevel);
        this.relationships.put("PEMUDA", 50);

        System.out.println("New game started from scene 1");
        syncUiState(30);
        saveProgress();
    }

    public void saveProgress() {
        this.lastUpdated = new Date();
        DatabaseManager dbManager = resolveDatabaseManager();
        if (dbManager != null && sessionId > 0) {
            dbManager.saveGameProgress(this);
        }
        System.out.println("Game progress saved - Scene: " + currentScene);
    }

    public void loadProgress() {
        if (relationships == null) {
            relationships = new HashMap<>();
        }
        if (!relationships.containsKey("PEMUDA")) {
            relationships.put("PEMUDA", 50);
        }
        if (relationshipSoekarno <= 0) {
            relationshipSoekarno = 50;
        }
        if (relationshipHatta <= 0) {
            relationshipHatta = 50;
        }
        if (trustLevel <= 0) {
            trustLevel = 50;
        }
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
        // Update relationships berdasarkan decision dan current scene
        Map<String, Object> uiComponents = null;
        if (uiManager != null) {
            uiComponents = uiManager.getUiComponents();
        }
        RelationshipManager relationshipManager = null;
        if (uiComponents != null) {
            Object relationObj = uiComponents.get("relationshipManager");
            if (relationObj instanceof RelationshipManager) {
                relationshipManager = (RelationshipManager) relationObj;
            }
        }
        if (relationshipManager != null) {
            relationshipManager.setRelationships(new HashMap<>(this.relationships));
        }
        SceneManager sceneManager = null;
        List<Choice> availableChoices = null;
        if (uiComponents != null) {
            Object managerObj = uiComponents.get("sceneManager");
            if (managerObj instanceof SceneManager) {
                sceneManager = (SceneManager) managerObj;
                availableChoices = sceneManager.getAvailableChoices();
            }
        }
        Choice selectedChoice = null;
        if (availableChoices != null && !availableChoices.isEmpty()) {
            int choiceIndex = -1;
            if (decisionCode != null && !decisionCode.isEmpty()) {
                char codeChar = java.lang.Character.toUpperCase(decisionCode.charAt(0));
                if (codeChar >= 'A' && codeChar <= 'Z') {
                    choiceIndex = codeChar - 'A';
                } else {
                    try {
                        choiceIndex = Integer.parseInt(decisionCode) - 1;
                    } catch (NumberFormatException ignored) {
                    }
                }
            }
            if (choiceIndex < 0 || choiceIndex >= availableChoices.size()) {
                choiceIndex = 0;
            }
            selectedChoice = availableChoices.get(choiceIndex);
        }
        if (selectedChoice != null) {
            if (currentScene == 6) {
                relationships.put("SCENE6_DECISION", selectedChoice.getChoiceId());
            }
            if (currentScene == 7) {
                relationships.put("SCENE7_DECISION", selectedChoice.getChoiceId());
            }
            if (currentScene == 9) {
                relationships.put("SCENE9_DECISION", selectedChoice.getChoiceId());
            }
            if (selectedChoice.getRequiredTrustLevel() > 0 && trustLevel < selectedChoice.getRequiredTrustLevel()) {
                System.out.println("Trust level too low for this choice. Falling back to default option.");
            } else {
                selectedChoice.executeChoice();
            }
            if (uiComponents != null) {
                uiComponents.put("lastChoice", selectedChoice);
            }
            Map<String, Integer> impact = selectedChoice.getRelationshipImpact();
            if (impact != null && !impact.isEmpty()) {
                if (relationshipManager == null) {
                    relationshipManager = new RelationshipManager();
                    if (uiComponents != null) {
                        uiComponents.put("relationshipManager", relationshipManager);
                    }
                    relationshipManager.setRelationships(new HashMap<>(this.relationships));
                }
                for (Map.Entry<String, Integer> entry : impact.entrySet()) {
                    relationshipManager.updateRelationship(entry.getKey(), entry.getValue());
                    relationships.put(entry.getKey(), relationshipManager.getRelationship(entry.getKey()));
                    if ("SOEKARNO".equalsIgnoreCase(entry.getKey())) {
                        relationshipSoekarno = relationshipManager.getRelationship(entry.getKey());
                    } else if ("HATTA".equalsIgnoreCase(entry.getKey())) {
                        relationshipHatta = relationshipManager.getRelationship(entry.getKey());
                    } else if ("TRUST".equalsIgnoreCase(entry.getKey())) {
                        trustLevel = relationshipManager.getRelationship(entry.getKey());
                    }
                }
            }
        }
        updateRelationships(decisionCode);

        // pindah ke scene selanjutnya berdasarkan decision atau sequential (emg ud
        // waktux pindah)
        persistDecision(sceneBeforeDecision, selectedChoice, decisionCode, decisionText);
        moveToNextScene(decisionCode);

        saveProgress();

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
        relationships.put("SOEKARNO", relationshipSoekarno);
        relationships.put("HATTA", relationshipHatta);
        relationships.put("TRUST", trustLevel);
    }

    private void moveToNextScene(String decisionCode) {
        int targetScene = currentScene < 9 ? currentScene + 1 : currentScene;
        Choice lastChoice = null;
        SceneManager sceneManager = null;
        Map<String, Object> uiComponents = null;
        if (uiManager != null) {
            uiComponents = uiManager.getUiComponents();
        }
        if (uiComponents != null) {
            Object choiceObj = uiComponents.get("lastChoice");
            if (choiceObj instanceof Choice) {
                lastChoice = (Choice) choiceObj;
            }
            Object managerObj = uiComponents.get("sceneManager");
            if (managerObj instanceof SceneManager) {
                sceneManager = (SceneManager) managerObj;
            }
        }
        if (lastChoice != null && lastChoice.getNextSceneId() > 0) {
            targetScene = lastChoice.getNextSceneId();
        }
        if (targetScene > 9) {
            targetScene = 9;
        }
        currentScene = targetScene;
        if (sceneManager == null && uiManager != null) {
            sceneManager = new SceneManager(uiManager);
            if (uiComponents != null) {
                uiComponents.put("sceneManager", sceneManager);
            }
        }
        if (sceneManager != null) {
            sceneManager.loadScene(currentScene);
        }
        if (uiManager != null) {
            uiManager.showScreen("SCENE_" + currentScene);
            uiManager.showDecisionTimer(30);
            uiManager.updateUI();
        }
        if (currentScene >= 9) {
            endingAchieved = calculateEnding();
            if (endingAchieved != null && !endingAchieved.isEmpty()) {
                handleGameCompletion();
                return;
            }
        }
    }

    public String calculateEnding() {
        int pemudaTrust = relationships.getOrDefault("PEMUDA", 50);
        int scene6Decision = relationships.getOrDefault("SCENE6_DECISION", 0);
        int scene7Decision = relationships.getOrDefault("SCENE7_DECISION", 0);
        int scene9Decision = relationships.getOrDefault("SCENE9_DECISION", 0);
        if (scene7Decision == 1 || scene9Decision == 1 || trustLevel <= 30) {
            return "BAD_ENDING_DIHANCURKAN_JEPANG";
        }
        if (trustLevel < 40) {
            return "BAD_ENDING_PENJAJAH_KEMBALI";
        }
        if (relationshipSoekarno <= 30 || scene6Decision == 2) {
            return "BAD_ENDING_PROKLAMASI_GAGAL";
        }
        if (pemudaTrust <= 20 && relationshipHatta <= 30) {
            return "BAD_ENDING_DIKHIANATI";
        }
        if (relationshipSoekarno >= 70 && relationshipHatta >= 60 && pemudaTrust >= 50 && trustLevel >= 80
                && scene9Decision == 2) {
            return "TRUE_ENDING_INDONESIA_MERDEKA";
        }
        if (relationshipSoekarno >= 50 && relationshipHatta >= 40 && trustLevel >= 60 && pemudaTrust >= 30) {
            return "GOOD_ENDING_KEMERDEKAAN_TEGANG";
        }
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
        Map<String, Object> uiComponents = uiManager != null ? uiManager.getUiComponents() : null;
        if (uiComponents != null) {
            Object timerObj = uiComponents.get("decisionTimer");
            if (timerObj instanceof Timer) {
                ((Timer) timerObj).stopTimer();
            }
        }
        persistEndingResult();
        if (uiManager != null) {
            Map<String, Integer> snapshot = new HashMap<>(relationships != null ? relationships : new HashMap<>());
            uiManager.displayEnding(endingAchieved, describeEnding(endingAchieved), snapshot);
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
        DatabaseManager dbManager = resolveDatabaseManager();
        if (dbManager == null) {
            return;
        }
        int userId = resolveComponentId("currentUserId", 0);
        int profileIdValue = profileId > 0 ? profileId : resolveComponentId("currentProfileId", 0);
        if (userId <= 0 || profileIdValue <= 0) {
            return;
        }
        int pemuda = relationships != null ? relationships.getOrDefault("PEMUDA", 50) : 50;
        int finalScore = Math.max(0, relationshipSoekarno + relationshipHatta + trustLevel + pemuda);
        int estimatedTime = Math.max(0, currentScene * 30); // approx seconds spent per scene
        dbManager.updateLeaderboard(userId, profileIdValue, finalScore, estimatedTime);
    }

    private void syncUiState(int countdownSeconds) {
        Map<String, Object> uiComponents = uiManager != null ? uiManager.getUiComponents() : null;
        if (uiComponents == null) {
            return;
        }
        if (relationships == null) {
            relationships = new HashMap<>();
        }
        uiComponents.put("gameSession", this);
        relationships.put("SOEKARNO", relationshipSoekarno);
        relationships.put("HATTA", relationshipHatta);
        relationships.put("TRUST", trustLevel);
        relationships.putIfAbsent("PEMUDA", 50);
        RelationshipManager relationshipManager = new RelationshipManager();
        relationshipManager.setRelationships(new HashMap<>(relationships));
        uiComponents.put("relationshipManager", relationshipManager);
        SceneManager sceneManager = new SceneManager(uiManager);
        int sceneToShow = currentScene > 0 ? currentScene : 1;
        sceneManager.loadScene(sceneToShow);
        uiComponents.put("sceneManager", sceneManager);
        if (countdownSeconds > 0) {
            uiManager.showScreen("SCENE_" + sceneToShow);
            uiManager.showDecisionTimer(countdownSeconds);
            uiManager.updateUI();
        }
    }

    private void persistDecision(int sceneNumber, Choice selectedChoice, String decisionCode, String decisionText) {
        DatabaseManager dbManager = resolveDatabaseManager();
        if (dbManager == null || sessionId <= 0) {
            return;
        }
        String decisionLabel = decisionText;
        if ((decisionLabel == null || decisionLabel.isEmpty()) && selectedChoice != null) {
            decisionLabel = selectedChoice.getChoiceText();
        }
        String codeToSave = decisionCode;
        if ((codeToSave == null || codeToSave.isEmpty()) && selectedChoice != null) {
            codeToSave = String.valueOf(selectedChoice.getChoiceId());
        }
        if (codeToSave != null) {
            codeToSave = codeToSave.trim().toUpperCase();
        }
        dbManager.savePlayerDecision(sessionId, sceneNumber, codeToSave, decisionLabel);
    }

    private DatabaseManager resolveDatabaseManager() {
        if (uiManager == null) {
            return null;
        }
        Map<String, Object> uiComponents = uiManager.getUiComponents();
        if (uiComponents == null) {
            return null;
        }
        Object dbObj = uiComponents.get("databaseManager");
        if (dbObj instanceof DatabaseManager) {
            return (DatabaseManager) dbObj;
        }
        return null;
    }

    private int resolveComponentId(String key, int fallback) {
        if (uiManager == null) {
            return fallback;
        }
        Map<String, Object> uiComponents = uiManager.getUiComponents();
        if (uiComponents == null) {
            return fallback;
        }
        Object value = uiComponents.get(key);
        if (value instanceof Integer) {
            return (Integer) value;
        }
        return fallback;
    }
}
