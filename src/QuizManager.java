
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuizManager {
    private List<Question> questions;
    private Question currentQuestion;
    private Map<Question, String> userAnswers;
    private int currentQuestionIndex;
    private int score;
    private int totalQuestions;
    private long startTime;
    private boolean quizCompleted;
    private DatabaseManager dbManager;

    private static final int EASY_SCORE = 50;
    private static final int MEDIUM_SCORE = 75;
    private static final int HARD_SCORE = 100;
    private static final int TIME_BONUS_PER_SECOND = 1;
    private static final int MAX_TIME = 180;
    
    public QuizManager(DatabaseManager dbManager) {
        this.questions = new ArrayList<>();
        this.userAnswers = new HashMap<>();
        this.currentQuestionIndex = 0;
        this.score = 0;
        this.quizCompleted = false;
        this.dbManager = dbManager;
        this.currentQuestionIndex = 0;
        this.totalQuestions = 15; 
    }
    
    public void startQuiz(int totalQuestions) {
        this.totalQuestions = totalQuestions;
        this.currentQuestionIndex = 0;
        this.score = 0;
        this.quizCompleted = false;
        this.userAnswers.clear();
        this.startTime = System.currentTimeMillis();
        
        // Load questions sesuai diffcituly
        questions = dbManager.loadQuestionsWithDistribution();
        
        if (!questions.isEmpty() && questions.size() >= totalQuestions) {
            currentQuestion = questions.get(0);
        } else {
            System.err.println("Not enough questions in database!");
        }
        
        userAnswers.clear();
        for (int i = 0; i < questions.size(); i++) {
            userAnswers.put(questions.get(i), ""); // Inisialisasi jawaban kosong untuk setiap pertanyaan
        }
        
        currentQuestionIndex = 0;
        startTime = System.currentTimeMillis();
    }
    
    public void submitAnswer(String answer) {
        if (currentQuestion != null && !quizCompleted) {
            userAnswers.put(currentQuestion, answer);
            
            if (currentQuestion.checkAnswer(answer)) {
                score++;
            }
        }
    }
    
    public void moveToNextQuestion() {
        if (currentQuestionIndex < questions.size() - 1) {
            currentQuestionIndex++;
            currentQuestion = questions.get(currentQuestionIndex);
        } else {
            endQuiz();
        }
    }
    
    public void moveToPreviousQuestion() {
        if (currentQuestionIndex > 0) {
            currentQuestionIndex--;
            currentQuestion = questions.get(currentQuestionIndex);
        }
    }
    
    public void endQuiz() {
        this.quizCompleted = true;
    }
    
    public boolean saveQuizResults(int userId) {
        int rawScore = calculateScore(); // Untuk display di UI
        int timeTaken = (int) ((System.currentTimeMillis() - startTime) / 1000);
        int finalScore = calculateFinalScore(); // Untuk leaderboard
        
        // Save quiz attempt (score mentah)
        boolean attemptSaved = dbManager.saveQuizAttempt(userId, rawScore, totalQuestions, timeTaken);
        
        // Update leaderboard dengan score akhir
        boolean leaderboardSaved = dbManager.updateLeaderboard(userId, rawScore, timeTaken, finalScore);
        
        return attemptSaved && leaderboardSaved;
    }
    
    public List<ScoreEntry> getTopScores(int limit) {
        return dbManager.getTopScores(limit);
    }
    
    public int calculateScore() {
        int rawScore = 0;
        for (int i = 0; i < questions.size(); i++) {
            Question question = questions.get(i);
            String userAnswer = userAnswers.get(question);
            String correctAnswer = question.getCorrectAnswer();

            if (userAnswer != null && userAnswer.equals(correctAnswer)) {
                rawScore++;
            }
        }
        
        return rawScore;
    }
    
    public int getTimeTaken() {
        return (int)((System.currentTimeMillis() - startTime) / 1000);
    }
    
    public int calculateFinalScore() {
        int baseScore = 0;
        
        // Hitung base score berdasarkan difficulty
        for (int i = 0; i < questions.size(); i++) {
            Question question = questions.get(i);
            String userAnswer = userAnswers.get(question);
            String correctAnswer = question.getCorrectAnswer();

            if (userAnswer != null && userAnswer.equals(correctAnswer)) {
                String difficulty = question.getDifficulty() != null ? question.getDifficulty().toLowerCase() : "medium";

                if (difficulty.equals("hard")) {
                    baseScore += HARD_SCORE;
                } else if (difficulty.equals("medium")) {
                    baseScore += MEDIUM_SCORE;
                } else if (difficulty.equals("easy")) {
                    baseScore += EASY_SCORE;
                } else {
                    baseScore += MEDIUM_SCORE;
                }
            }
        }
        
        // Hitung waktu
        long endTime = System.currentTimeMillis();
        int timeTaken = (int) ((endTime - startTime) / 1000);

        // Normalisasi baseScore berdasarkan maksimal possible base untuk semua pertanyaan
        int maxBase = 0;
        for (Question q : questions) {
            String diff = q.getDifficulty() != null ? q.getDifficulty().toLowerCase() : "medium";
            if (diff.equals("hard")) maxBase += HARD_SCORE;
            else if (diff.equals("medium")) maxBase += MEDIUM_SCORE;
            else if (diff.equals("easy")) maxBase += EASY_SCORE;
            else maxBase += MEDIUM_SCORE;
        }

        double normalizedBase = 0.0;
        if (maxBase > 0) {
            // max 800, sisanya berdasarkan cepet ap ndknya jawab soal
            normalizedBase = ((double) baseScore / (double) maxBase) * 800.0;
        }

        // Hitung bonus waktu tetapi batasi agar tidak mendominasi (maks 200)
        int timeBonus = 0;
        if (timeTaken < MAX_TIME) {
            timeBonus = Math.min((MAX_TIME - timeTaken) * TIME_BONUS_PER_SECOND, 200);
        }

        int finalScore = (int) Math.round(normalizedBase) + timeBonus;
        return Math.min(finalScore, 1000);
    }

        
    public boolean isQuizCompleted() {
        return quizCompleted;
    }
    
    public int getCurrentQuestionNumber() {
        return currentQuestionIndex + 1;
    }
    
    public int getTotalQuestions() {
        return totalQuestions;
    }
    
    public String getUserAnswerForCurrentQuestion() {
        return userAnswers.getOrDefault(currentQuestion, "");
    }
    
    // Getters
    public List<Question> getQuestions() { return questions; }
    public Question getCurrentQuestion() { return currentQuestion; }
    public int getCurrentQuestionIndex() { return currentQuestionIndex; }
    public int getScore() { return score; }
}
