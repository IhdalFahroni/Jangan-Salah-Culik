import java.util.ArrayList;
import java.util.List;
import javax.swing.SwingWorker; // Wajib import ini
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;

public class SceneManager {
    private Scene currentScene;
    private List<Scene> sceneHistory;
    private UIManager uiManager;
    private DatabaseManager dbManager;

    public SceneManager(UIManager uiManager) {
        this.sceneHistory = new ArrayList<>();
        this.uiManager = uiManager;
        
        // Resolve DatabaseManager
        if (uiManager != null && uiManager.getUiComponents() != null) {
            Object dbObj = uiManager.getUiComponents().get("databaseManager");
            if (dbObj instanceof DatabaseManager) {
                this.dbManager = (DatabaseManager) dbObj;
            }
        }
    }

    public void loadScene(int sceneId) {
        if (dbManager == null) {
            System.err.println("[SceneManager] Error: DatabaseManager is null!");
            return;
        }

        // Tampilkan loading state (opsional, biar user tau sedang proses)
        // uiManager.showLoading(); 

        // Gunakan SwingWorker untuk proses berat
        new SwingWorker<Scene, Void>() {
            @Override
            protected Scene doInBackground() throws Exception {
                // 1. Ambil Data dari DB (Berat)
                Scene scene = dbManager.loadSceneData(sceneId);
                
                if (scene != null) {
                    // 2. Load Gambar dari Disk (Berat)
                    String bgPath = "assets/Background/" + scene.getBackgroundImage();
                    try {
                        File imgFile = new File(bgPath);
                        if (imgFile.exists()) {
                            BufferedImage img = ImageIO.read(imgFile);
                            scene.setLoadedImage(img); // Simpan di objek scene
                        }
                    } catch (Exception e) {
                        System.err.println("Gagal load gambar background: " + bgPath);
                    }
                }
                return scene;
            }

            @Override
            protected void done() {
                try {
                    Scene scene = get(); 

                    if (scene == null) return;

                    setCurrentScene(scene);
                    sceneHistory.add(scene);

                    if (uiManager != null) {
                        uiManager.showScreen("SCENE_" + sceneId);
                        
                        // Update UI (Ini akan memanggil displayScene & menyalakan tombol)
                        uiManager.updateUI(); 
                        
                        // --- PERBAIKAN: Reset Timer dengan Jelas ---
                        // Pastikan timer lama mati total sebelum timer baru mulai
                        uiManager.showDecisionTimer(30); 
                    }
                    
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.execute();
    }

    public Scene getCurrentScene() {
        return currentScene;
    }

    public void goToNextScene() {
        if (currentScene == null) {
            loadScene(1);
            return;
        }
        int nextSceneId = currentScene.getSceneId() + 1;
        loadScene(nextSceneId);
    }

    public List<Choice> getAvailableChoices() {
        if (currentScene != null && currentScene.getChoices() != null) {
            return currentScene.getChoices();
        }
        return new ArrayList<>();
    }

    // Getters and Setters
    public void setCurrentScene(Scene currentScene) {
        this.currentScene = currentScene;
    }

    public List<Scene> getSceneHistory() {
        return sceneHistory;
    }

    public void setSceneHistory(List<Scene> sceneHistory) {
        this.sceneHistory = sceneHistory;
    }
}
