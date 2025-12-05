
import java.util.HashMap;
import java.util.Map;

public class AudioManager {
    private AudioClip backgroundMusic;
    private Map<String, AudioClip> soundEffects;
    private boolean isMuted;
    private float volume;
    private volatile Thread fadeThread;
    private final Object fadeLock = new Object();
    
    public AudioManager() {
        this.soundEffects = new HashMap<>();
        this.volume = 1.0f;
    }
    
    public void playBackgroundMusic(String musicName, int fadeInMs) {
        System.out.println("AudioManager.playBackgroundMusic(): requested -> " + musicName + " fadeIn=" + fadeInMs);

        // kalo ud ke play, nd ngapa ngapain
        if (backgroundMusic != null && backgroundMusic.getFilePath() != null
                && backgroundMusic.getFilePath().equals(musicName)
                && backgroundMusic.isPlaying()) {
            System.out.println("AudioManager: same background already playing, skipping.");
            return;
        }

        // berhenti playback bg music lama dulu
        if (backgroundMusic != null) {
            stopBackgroundMusic(500); // short crossfade
        }

        // buat n mulai bg music baru
        AudioClip clip = new AudioClip();
        clip.setFilePath(musicName);
        clip.setAudioName(musicName);
        clip.setLooping(true);
        clip.setVolume(0f);
        clip.play();
        this.backgroundMusic = clip;

        // mulai fade-in
        startFade(0f, this.volume, fadeInMs, clip);
    }

    public void playBackgroundMusic(String musicName) {
        playBackgroundMusic(musicName, 1200);
    }
    
    public void stopBackgroundMusic() {
        stopBackgroundMusic(800);
    }

    //stop bg music pake fade-out
    public void stopBackgroundMusic(int fadeOutMs) {
        if (backgroundMusic == null) return;
        System.out.println("AudioManager.stopBackgroundMusic(): fadeOut=" + fadeOutMs);
        // Fade to 0 then stop
        startFade(backgroundMusic.getVolume(), 0f, fadeOutMs, backgroundMusic);
    }

    private void startFade(float from, float to, int durationMs, AudioClip targetClip) {
        synchronized (fadeLock) {
            if (fadeThread != null && fadeThread.isAlive()) {
                fadeThread.interrupt();
            }

            fadeThread = new Thread(() -> {
                try {
                    int steps = Math.max(1, durationMs / 50);
                    float delta = (to - from) / steps;
                    float current = from;
                    for (int i = 0; i < steps; i++) {
                        if (Thread.currentThread().isInterrupted()) break;
                        current += delta;
                        targetClip.setVolume(current);
                        try { Thread.sleep(50); } catch (InterruptedException ie) { break; }
                    }
                    targetClip.setVolume(to);

                    if (to == 0f) {
                        // stop n clear kalo masi keplay d bg
                        if (backgroundMusic == targetClip) {
                            targetClip.stop();
                            backgroundMusic = null;
                        }
                    }
                } catch (Exception e) {
                    System.err.println("Audio fade thread error: " + e.getMessage());
                }
            }, "audio-fade-thread");
            fadeThread.setDaemon(true);
            fadeThread.start();
        }
    }

    public void cleanup() {
        if (backgroundMusic != null) {
            backgroundMusic.stop();
        }
        // Cleanup sound effects klo perlu
        soundEffects.clear();
        System.out.println("AudioManager cleaned up");
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
