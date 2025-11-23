package src;

import java.util.HashMap;
import java.util.Map;

public class AudioManager {
    private AudioClip backgroundMusic;
    private Map<String, AudioClip> soundEffects;
    private boolean isMuted;
    private float volume;
    
    public AudioManager() {
        this.soundEffects = new HashMap<>();
        this.volume = 1.0f;
    }
    
    public void playBackgroundMusic(String musicName) {
        // TODO: Implement background music playback
        System.out.println("Playing background music: " + musicName);
        if (backgroundMusic != null) {
            backgroundMusic.stop();
        }
        // Logic untuk load dan play music
    }
    
    public void stopBackgroundMusic() {
        // TODO: Implement background music stop
        System.out.println("Stopping background music");
        if (backgroundMusic != null) {
            backgroundMusic.stop();
        }
    }

    public void cleanup() {
        if (backgroundMusic != null) {
            backgroundMusic.stop();
        }
        // Cleanup sound effects jika perlu
        soundEffects.clear();
        System.out.println("AudioManager cleaned up");
    }
    
    public void playSoundEffect(String effectName) {
        // TODO: Implement sound effect playback
    }
    
    public void setVolume(float volume) {
        // TODO: Implement volume control
    }
    
    public void toggleMute() {
        // TODO: Implement mute toggle
    }
    
    // Getters and Setters
    public AudioClip getBackgroundMusic() { return backgroundMusic; }
    public void setBackgroundMusic(AudioClip backgroundMusic) { this.backgroundMusic = backgroundMusic; }
    public Map<String, AudioClip> getSoundEffects() { return soundEffects; }
    public void setSoundEffects(Map<String, AudioClip> soundEffects) { this.soundEffects = soundEffects; }
    public boolean isMuted() { return isMuted; }
    public void setMuted(boolean muted) { isMuted = muted; }
    public float getVolume() { return volume; }
}