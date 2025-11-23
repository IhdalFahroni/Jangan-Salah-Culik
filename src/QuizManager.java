package src;

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
    
    public QuizManager(DatabaseManager dbManager) {
        this.questions = new ArrayList<>();
        this.userAnswers = new HashMap<>();
        this.currentQuestionIndex = 0;
        this.score = 0;
        this.quizCompleted = false;
        this.dbManager = dbManager;
    }
    
    public void startQuiz(int totalQuestions) {
        this.totalQuestions = totalQuestions;
        this.currentQuestionIndex = 0;
        this.score = 0;
        this.quizCompleted = false;
        this.userAnswers.clear();
        this.startTime = System.currentTimeMillis();
        
        // Load questions from database
        questions = dbManager.loadQuestionsFromDatabase(totalQuestions);
        
        if (!questions.isEmpty() && questions.size() >= totalQuestions) {
            currentQuestion = questions.get(0);
        } else {
            System.err.println("Not enough questions in database!");
        }
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
    
    public boolean saveQuizResults(int userId, int profileId) {
        int timeTaken = getTimeTaken();
        boolean attemptSaved = dbManager.saveQuizAttempt(userId, score, totalQuestions, timeTaken);
        boolean leaderboardUpdated = dbManager.updateLeaderboard(userId, profileId, score, timeTaken);
        
        return attemptSaved || leaderboardUpdated; 
    }
    
    public List<ScoreEntry> getTopScores(int limit) {
        return dbManager.getTopScores(limit);
    }
    
    public int calculateScore() {
        return score;
    }
    
    public int getTimeTaken() {
        return (int)((System.currentTimeMillis() - startTime) / 1000);
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