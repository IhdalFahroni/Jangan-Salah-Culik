package src;

public class AudioClip {
    private String audioName;
    private String filePath;
    private float duration;
    private boolean isLooping;
    
    public void play() {
        // TODO: Implement audio playback
    }
    
    public void stop() {
        // TODO: Implement audio stop
    }
    
    public void pause() {
        // TODO: Implement audio pause
    }
    
    // Getters and Setters
    public String getAudioName() { return audioName; }
    public void setAudioName(String audioName) { this.audioName = audioName; }
    public String getFilePath() { return filePath; }
    public void setFilePath(String filePath) { this.filePath = filePath; }
    public float getDuration() { return duration; }
    public void setDuration(float duration) { this.duration = duration; }
    public boolean isLooping() { return isLooping; }
    public void setLooping(boolean looping) { isLooping = looping; }
}