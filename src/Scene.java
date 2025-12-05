import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class Scene {
    private int sceneId;
    private String sceneTitle;
    private String sceneDescription;
    private String backgroundImage;
    private String backgroundMusic;
    private BufferedImage loadedImage;
    private List<Character> characters;
    private List<Choice> choices;

    public Scene() {
        this.characters = new ArrayList<>();
        this.choices = new ArrayList<>();
    }

    public void displayScene() {
        // Display handled through UIManager and StoryPanel.
    }

    public void playBackgroundMusic() {
        // Placeholder for future audio routing.
    }

    // Getters and Setters
    public int getSceneId() {
        return sceneId;
    }

    public void setSceneId(int sceneId) {
        this.sceneId = sceneId;
    }

    public String getSceneTitle() {
        return sceneTitle;
    }

    public void setSceneTitle(String sceneTitle) {
        this.sceneTitle = sceneTitle;
    }

    public String getSceneDescription() {
        return sceneDescription;
    }

    public void setSceneDescription(String sceneDescription) {
        this.sceneDescription = sceneDescription;
    }

    public String getBackgroundImage() {
        return backgroundImage;
    }

    public void setBackgroundImage(String backgroundImage) {
        this.backgroundImage = backgroundImage;
    }

    public String getBackgroundMusic() {
        return backgroundMusic;
    }

    public void setBackgroundMusic(String backgroundMusic) {
        this.backgroundMusic = backgroundMusic;
    }

    public List<Character> getCharacters() {
        return characters;
    }

    public void setCharacters(List<Character> characters) {
        this.characters = characters;
    }

    public List<Choice> getChoices() {
        return choices;
    }

    public void setChoices(List<Choice> choices) {
        this.choices = choices;
    }

    public BufferedImage getLoadedImage() { return loadedImage; }
    public void setLoadedImage(BufferedImage loadedImage) { this.loadedImage = loadedImage; }
}
