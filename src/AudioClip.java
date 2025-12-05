import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class AudioClip {
    private String audioName;
    private String filePath;
    private float duration;
    private boolean isLooping;

    private Clip clip;
    private FloatControl gainControl;
    private boolean isPlaying = false;
    private int pausePosition = 0; // frame position when paused

    public AudioClip() {
    }

    public void play() {
        try {
            if (clip == null) {
                File f = new File(filePath);
                if (!f.exists()) {
                    System.err.println("AudioClip.play: file not found -> " + filePath);
                    return;
                }
                AudioInputStream ais = AudioSystem.getAudioInputStream(f);
                AudioFormat baseFormat = ais.getFormat();
                AudioFormat decodedFormat = new AudioFormat(
                        AudioFormat.Encoding.PCM_SIGNED,
                        baseFormat.getSampleRate(),
                        16,
                        baseFormat.getChannels(),
                        baseFormat.getChannels() * 2,
                        baseFormat.getSampleRate(),
                        false
                );

                AudioInputStream dais = AudioSystem.getAudioInputStream(decodedFormat, ais);
                clip = AudioSystem.getClip();
                clip.open(dais);
                try {
                    gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                } catch (Exception e) {
                    gainControl = null;
                }
            }

            if (gainControl != null) {
                setVolume(0.5f); // default volume
            }

            if (isLooping) {
                clip.loop(Clip.LOOP_CONTINUOUSLY);
            } else {
                clip.setFramePosition(pausePosition);
                clip.start();
            }
            isPlaying = true;
            System.out.println("[AudioClip] play(): " + filePath + " (loop=" + isLooping + ")");
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.err.println("AudioClip.play error: " + e.getMessage());
        }
    }

    public void stop() {
        if (clip != null) {
            clip.stop();
            clip.flush();
            clip.close();
            clip = null;
        }
        isPlaying = false;
        pausePosition = 0;
        System.out.println("[AudioClip] stop(): " + filePath);
    }

    public void pause() {
        if (clip != null && clip.isRunning()) {
            pausePosition = clip.getFramePosition();
            clip.stop();
            isPlaying = false;
            System.out.println("[AudioClip] pause(): " + filePath + " pos=" + pausePosition);
        }
    }

    public boolean isPlaying() {
        return isPlaying && clip != null && clip.isRunning();
    }

    // volume control (0.0 - 1.0)
    public float getVolume() {
        if (gainControl == null) return 1.0f;
        float min = gainControl.getMinimum();
        float max = gainControl.getMaximum();
        float linear = (gainControl.getValue() - min) / (max - min);
        return Math.max(0f, Math.min(1f, linear));
    }

    public void setVolume(float volume) {
        if (gainControl == null) return;
        float v = Math.max(0f, Math.min(1f, volume));
        float min = gainControl.getMinimum();
        float max = gainControl.getMaximum();
        float gain = min + (max - min) * v;
        gainControl.setValue(gain);
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
