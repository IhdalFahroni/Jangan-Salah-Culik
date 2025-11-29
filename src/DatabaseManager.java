
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
    private static final String URL = "jdbc:mysql://127.0.0.1:3307/rengasdengklok_db";
    private static final String USERNAME = "root"; 
    private static final String PASSWORD = ""; 
    
    private Connection connection;
    
    public void connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            System.out.println("Database connected successfully!");
        } catch (Exception e) {
            System.err.println("Database connection failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public void disconnect() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Database disconnected.");
            }
        } catch (SQLException e) {
            System.err.println("Error disconnecting database: " + e.getMessage());
        }
    }
    
    public ResultSet executeQuery(String query) {
        try {
            Statement stmt = connection.createStatement();
            return stmt.executeQuery(query);
        } catch (SQLException e) {
            System.err.println("Query execution failed: " + e.getMessage());
            return null;
        }
    }
    
    public int executeUpdate(String query) {
        try {
            Statement stmt = connection.createStatement();
            return stmt.executeUpdate(query);
        } catch (SQLException e) {
            System.err.println("Update execution failed: " + e.getMessage());
            return 0;
        }
    }
    
    public User getUserData(int userId) {
        String query = "SELECT * FROM users WHERE user_id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                User user = new User();
                user.setUserId(rs.getInt("user_id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                java.sql.Timestamp timestamp = rs.getTimestamp("created_at");
                if (timestamp != null) {
                    user.setCreatedAt(new Date(timestamp.getTime()));
                } 
                return user;
            }
        } catch (SQLException e) {
            System.err.println("Error getting user data: " + e.getMessage());
        }
        
        return null;
    }
    
    public void saveGameProgress(GameSession session) {
        String query = "UPDATE sessions SET current_scene = ?, relationship_soekarno = ?, " +
                      "relationship_hatta = ?, trust_level = ?, ending_achieved = ?, " +
                      "last_updated = NOW() WHERE session_id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, session.getCurrentScene());
            stmt.setInt(2, session.getRelationshipSoekarno());
            stmt.setInt(3, session.getRelationshipHatta());
            stmt.setInt(4, session.getTrustLevel());
            stmt.setString(5, session.getEndingAchieved());
            stmt.setInt(6, session.getSessionId());
            
            stmt.executeUpdate();
            System.out.println("Game progress saved for session: " + session.getSessionId());
            
        } catch (SQLException e) {
            System.err.println("Error saving game progress: " + e.getMessage());
        }
    }
    
    // ambil soal dari database acak
    public List<Question> loadQuestionsFromDatabase(int count) {
        List<Question> questions = new ArrayList<>();
        
        String query = "SELECT * FROM questions ORDER BY RAND() LIMIT ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, count);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Question question = new Question();
                question.setQuestionId(rs.getInt("question_id"));
                question.setQuestionText(rs.getString("question_text"));
                
                question.setOptionA(rs.getString("option_a"));
                question.setOptionB(rs.getString("option_b"));
                question.setOptionC(rs.getString("option_c")); 
                question.setOptionD(rs.getString("option_d")); 
                
                question.setCorrectAnswer(rs.getString("correct_answer"));
                question.setDifficulty(rs.getString("difficulty"));
                question.setSceneReference(rs.getInt("scene_reference"));
                
                questions.add(question);
            }
            
            System.out.println("Loaded " + questions.size() + " questions from database.");
            
        } catch (SQLException e) {
            System.err.println("Error loading questions: " + e.getMessage());
        }
        
        return questions;
    }
    
    public boolean saveQuizAttempt(int userId, int score, int totalQuestions, int timeTaken) {
        String query = "INSERT INTO quiz_attempts (user_id, score, total_questions, time_taken_seconds) VALUES (?, ?, ?, ?)";
        
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, score);
            stmt.setInt(3, totalQuestions);
            stmt.setInt(4, timeTaken);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error saving quiz attempt: " + e.getMessage());
            return false;
        }
    }
    
    public boolean updateLeaderboard(int userId, int profileId, int score, int timeTaken) {
        String query = "INSERT INTO leaderboard (user_id, profile_id, score, time_taken_seconds) VALUES (?, ?, ?, ?)";
        
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, profileId);
            stmt.setInt(3, score);
            stmt.setInt(4, timeTaken);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating leaderboard: " + e.getMessage());
            return false;
        }
    }
    
    public List<ScoreEntry> getTopScores(int limit) {
        List<ScoreEntry> topScores = new ArrayList<>();
        
        String query = "SELECT u.username, p.character_name, l.score, l.time_taken_seconds, l.attempted_at " +
                      "FROM leaderboard l " +
                      "JOIN users u ON l.user_id = u.user_id " +
                      "JOIN profiles p ON l.profile_id = p.profile_id " +
                      "ORDER BY l.score DESC, l.time_taken_seconds ASC " +
                      "LIMIT ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, limit);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                ScoreEntry entry = new ScoreEntry();
                entry.setUsername(rs.getString("username"));
                entry.setCharacterName(rs.getString("character_name"));
                entry.setScore(rs.getInt("score"));
                entry.setTimeTaken(rs.getInt("time_taken_seconds"));
                entry.setAttemptedAt(rs.getTimestamp("attempted_at"));
                
                topScores.add(entry);
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting top scores: " + e.getMessage());
        }
        
        return topScores;
    }
    
    public boolean createUser(String username, String password) {
        String query = "INSERT INTO users (username, password) VALUES (?, ?)";
        
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, password); 
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error creating user: " + e.getMessage());
            return false;
        }
    }
    
    public User authenticateUser(String username, String password) {
        if (connection == null) {
            System.err.println("Database connection is null!");
            return null;
        }
        String query = "SELECT * FROM users WHERE username = ? AND password = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, password); 
            
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                User user = new User();
                user.setUserId(rs.getInt("user_id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                java.sql.Timestamp timestamp = rs.getTimestamp("created_at");
                if (timestamp != null) {
                    user.setCreatedAt(new Date(timestamp.getTime()));
                }
                return user;
            }
            
        } catch (SQLException e) {
            System.err.println("Error authenticating user: " + e.getMessage());
        }
        
        return null;
    }
    
    public boolean createProfile(int userId, String characterName, String gender) {
        String query = "INSERT INTO profiles (user_id, character_name, gender) VALUES (?, ?, ?)";
        
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, userId);
            stmt.setString(2, characterName);
            
            // soalx d database pake enum
            String dbGender = convertToDbGender(gender);
            stmt.setString(3, dbGender);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error creating profile: " + e.getMessage());
            return false;
        }
    }

    public boolean updateProfile(int userId, String characterName, String gender) {
        String query = "UPDATE profiles SET character_name = ?, gender = ? WHERE user_id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, characterName);
            
            // konv gender ke format enum database
            String dbGender = convertToDbGender(gender);
            stmt.setString(2, dbGender);
            
            stmt.setInt(3, userId);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating profile: " + e.getMessage());
            return false;
        }
    }

    private String convertToDbGender(String uiGender) {
        if (uiGender == null) return "Male"; // defaultnya cwo biar nda dinikahin soekarno
        
        String normalized = uiGender.trim().toLowerCase();
        if (normalized.equals("perempuan") || normalized.equals("female") || normalized.equals("wanita")) {
            return "Female";
        } else if (normalized.equals("laki-laki") || normalized.equals("male") || normalized.equals("pria")) {
            return "Male";
        } else {
            return "Other";
        }
    }
    
    public List<PlayerProfile> getUserProfiles(int userId) {
        List<PlayerProfile> profiles = new ArrayList<>();
        String query = "SELECT * FROM profiles WHERE user_id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                PlayerProfile profile = new PlayerProfile();
                profile.setProfileId(rs.getInt("profile_id"));
                profile.setUserId(rs.getInt("user_id"));
                profile.setCharacterName(rs.getString("character_name"));
                
                // konversi gender dari DB ke UI
                String dbGender = rs.getString("gender");
                String uiGender = convertToUiGender(dbGender);
                profile.setGender(uiGender);
                
                java.sql.Timestamp timestamp = rs.getTimestamp("created_at");
                if (timestamp != null) {
                    profile.setCreatedAt(new Date(timestamp.getTime()));
                }
                profiles.add(profile);
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting user profiles: " + e.getMessage());
        }
        
        return profiles;
    }

    private String convertToUiGender(String dbGender) {
        if (dbGender == null) return "Laki-laki"; 
        
        String normalized = dbGender.trim().toLowerCase();
        if (normalized.equals("female")) {
            return "Perempuan";
        } else if (normalized.equals("male")) {
            return "Laki-laki";
        } else {
            return "Lainnya";
        }
    }
    
    public int createGameSession(int profileId) {
        String query = "INSERT INTO sessions (profile_id) VALUES (?)";
        
        try (PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, profileId);
            stmt.executeUpdate();
            
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1); 
            }
            
        } catch (SQLException e) {
            System.err.println("Error creating game session: " + e.getMessage());
        }
        
        return -1;
    }
    
    public GameSession loadGameSession(int sessionId) {
        String query = "SELECT * FROM sessions WHERE session_id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, sessionId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                GameSession session = new GameSession();
                session.setSessionId(rs.getInt("session_id"));
                session.setProfileId(rs.getInt("profile_id"));
                session.setCurrentScene(rs.getInt("current_scene"));
                session.setRelationshipSoekarno(rs.getInt("relationship_soekarno"));
                session.setRelationshipHatta(rs.getInt("relationship_hatta"));
                session.setTrustLevel(rs.getInt("trust_level"));
                session.setEndingAchieved(rs.getString("ending_achieved"));
                session.setCreatedAt(rs.getTimestamp("created_at"));
                session.setLastUpdated(rs.getTimestamp("last_updated"));
                return session;
            }
            
        } catch (SQLException e) {
            System.err.println("Error loading game session: " + e.getMessage());
        }
        
        return null;
    }
    
    public void savePlayerDecision(int sessionId, int sceneNumber, String decisionCode, String decisionText) {
        String query = "INSERT INTO decisions (session_id, scene_number, decision_code, decision_text) VALUES (?, ?, ?, ?)";
        
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, sessionId);
            stmt.setInt(2, sceneNumber);
            stmt.setString(3, decisionCode);
            stmt.setString(4, decisionText);
            
            stmt.executeUpdate();
            
        } catch (SQLException e) {
            System.err.println("Error saving player decision: " + e.getMessage());
        }
    }
    
    // Getters and Setters
    public Connection getConnection() { return connection; }
    public void setConnection(Connection connection) { this.connection = connection; }
}
