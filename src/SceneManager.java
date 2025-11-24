
import java.util.ArrayList;
import java.util.List;

public class SceneManager {
    private Scene currentScene;
    private List<Scene> sceneHistory;
    private UIManager uiManager;
    
    public SceneManager(UIManager uiManager) {
        this.sceneHistory = new ArrayList<>();
        this.uiManager = uiManager;
    }
    
    public void loadScene(int sceneId) {
        // TODO: Implement scene loading logic
    }
    
    public Scene getCurrentScene() {
        // TODO: Implement get current scene
        return currentScene;
    }
    
    public void goToNextScene() {
        // TODO: Implement scene transition logic
    }
    
    public List<Choice> getAvailableChoices() {
        // TODO: Implement available choices logic
        return new ArrayList<>();
    }
    
    // Getters and Setters
    public void setCurrentScene(Scene currentScene) { this.currentScene = currentScene; }
    public List<Scene> getSceneHistory() { return sceneHistory; }
    public void setSceneHistory(List<Scene> sceneHistory) { this.sceneHistory = sceneHistory; }
}
