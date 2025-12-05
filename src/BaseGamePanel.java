import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

public abstract class BaseGamePanel extends JPanel {
    
    // Resource visual yang dibagi ke semua anak
    protected BufferedImage noiseTexture;
    
    // Konstanta ukuran standar
    protected static final int WINDOW_WIDTH = 1024;
    protected static final int WINDOW_HEIGHT = 768;

    public BaseGamePanel() {
        setLayout(null); // Absolute layout
        setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
        setBackground(ColorPalette.DARK_BG_1); 
        
        // Generate noise sekali saja di sini
        generateNoiseTexture();
    }

    // Logika pembuatan noise dipindah ke sini
    protected void generateNoiseTexture() {
        noiseTexture = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = noiseTexture.createGraphics();
        Random rand = new Random(12345);

        for (int y = 0; y < 100; y++) {
            for (int x = 0; x < 100; x++) {
                int noise = rand.nextInt(80) - 40;
                int gray = 128 + noise;
                gray = Math.max(0, Math.min(255, gray));
                int alpha = 20;
                noiseTexture.setRGB(x, y, new Color(gray, gray, gray, alpha).getRGB());
            }
        }
        g.dispose();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();

        // 1. Gambar Background Gradasi Standar
        drawBackgroundGradient(g2d, w, h);

        // 2. Gambar Noise Texture
        drawNoise(g2d, w, h);

        // 3. Gambar Efek Glow Merah (Vignette)
        drawRadialGlow(g2d, w, h);
    }

    // --- Helper Methods untuk modularitas ---

    protected void setRenderingHints(Graphics2D g2d) {
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    }

    protected void drawBackgroundGradient(Graphics2D g2d, int w, int h) {
        Color[] gradColors = {
            ColorPalette.DARK_BG_1,
            ColorPalette.DARK_BG_2,
            ColorPalette.DARK_BG_3
        };

        for (int y = 0; y < h; y++) {
            float ratio = (float) y / h;
            Color c1 = (ratio < 0.5f) ? gradColors[0] : gradColors[1];
            Color c2 = (ratio < 0.5f) ? gradColors[1] : gradColors[2];
            float localRatio = (ratio < 0.5f) ? ratio * 2 : (ratio - 0.5f) * 2;

            int r = (int) (c1.getRed() + (c2.getRed() - c1.getRed()) * localRatio);
            int gr = (int) (c1.getGreen() + (c2.getGreen() - c1.getGreen()) * localRatio);
            int b = (int) (c1.getBlue() + (c2.getBlue() - c1.getBlue()) * localRatio);

            g2d.setColor(new Color(r, gr, b));
            g2d.fillRect(0, y, w, 1);
        }
    }

    protected void drawNoise(Graphics2D g2d, int w, int h) {
        if (noiseTexture != null) {
            for (int y = 0; y < h; y += 100) {
                for (int x = 0; x < w; x += 100) {
                    g2d.drawImage(noiseTexture, x, y, null);
                }
            }
        }
    }

    protected void drawRadialGlow(Graphics2D g2d, int w, int h) {
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.1f));
        RadialGradientPaint glow = new RadialGradientPaint(
            w / 2, h / 2,
            w / 2, // Radius
            new float[]{0f, 1f},
            new Color[]{
                new Color(168, 106, 101, 255), // Merah tua di tengah
                new Color(168, 106, 101, 0)    // Transparan di pinggir
            }
        );
        g2d.setPaint(glow);
        g2d.fillOval(0, 0, w, h);
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
    }
}