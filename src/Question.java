
import java.util.HashMap;
import java.util.Map;

public class Question {
    private int questionId;
    private String questionText;
    private Map<String, String> options;
    private String correctAnswer;
    private String difficulty;
    private int sceneReference;
    
    public Question() {
        this.options = new HashMap<>();
    }
    
    public void setOptionA(String option) { 
        options.put("A", option); 
    }
    
    public void setOptionB(String option) { 
        options.put("B", option); 
    }
    
    public void setOptionC(String option) { 
        if (option != null) {
            options.put("C", option); 
        }
    }
    
    public void setOptionD(String option) { 
        if (option != null) {
            options.put("D", option); 
        }
    }
    
    public String getOptionA() { 
        return options.get("A"); 
    }
    
    public String getOptionB() { 
        return options.get("B"); 
    }
    
    public String getOptionC() { 
        return options.get("C"); 
    }
    
    public String getOptionD() { 
        return options.get("D"); 
    }
    
    // Existing methods
    public void displayQuestion() {
        // TODO: Implement question display logic
    }
    
    public boolean checkAnswer(String userAnswer) {
        return correctAnswer != null && correctAnswer.equalsIgnoreCase(userAnswer);
    }
    
    // Getters and Setters
    public int getQuestionId() { return questionId; }
    public void setQuestionId(int questionId) { this.questionId = questionId; }
    
    public String getQuestionText() { return questionText; }
    public void setQuestionText(String questionText) { this.questionText = questionText; }
    
    public Map<String, String> getOptions() { return options; }
    public void setOptions(Map<String, String> options) { this.options = options; }
    
    public String getCorrectAnswer() { return correctAnswer; }
    public void setCorrectAnswer(String correctAnswer) { this.correctAnswer = correctAnswer; }
    
    public String getDifficulty() { return difficulty; }
    public void setDifficulty(String difficulty) { this.difficulty = difficulty; }
    
    public int getSceneReference() { return sceneReference; }
    public void setSceneReference(int sceneReference) { this.sceneReference = sceneReference; }
}
