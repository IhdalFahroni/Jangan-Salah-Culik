import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.*;
import javax.imageio.ImageIO;
import javax.swing.Timer;

public class StoryPanel extends JPanel {

    private UIManager uiManager; //untuk berpindah layar

    //menyimpan id scene sebelumnya
    private int lastSceneId = -1;

    private BufferedImage bgImage;   //gambar background scene
    private BufferedImage noiseTexture; //tekstur noise untuk efek grain

    //komponen visual di panel cerita
    private StatsBarPanel statsBar;
    private SceneInfoBadge sceneInfo;
    private TimerBadge timerBadge;
    private CharacterDisplayPanel characterPanel;
    private DialogueBoxPanel dialogueBox;
    private MenuButton backBtn;

    //timer hitung mundur untuk scene pilihan
    private Timer visualTimer;
    private int currentTimeLeft = 30;

    private static final int WINDOW_WIDTH = 1024;
    private static final int WINDOW_HEIGHT = 768;

    public StoryPanel() {
        setLayout(null);
        setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setBackground(new Color(80, 55, 50)); //warna dasar fallback
        setOpaque(true);

        generateNoiseTexture();  //buat efek noise
        initComponents();        //siapkan semua komponen

        //timer berjalan tiap 1 detik untuk mengurangi waktu
        visualTimer = new Timer(1000, e -> {
            if (currentTimeLeft > 0) {
                currentTimeLeft--;
                if (timerBadge != null) timerBadge.updateTime(currentTimeLeft);
            }
        });

        //update layout saat ukuran panel berubah
        addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent evt) {
                updateLayout();
            }
        });
    }

    public void setUiManager(UIManager uiManager) {
        this.uiManager = uiManager;
    }

    //membuat tekstur noise untuk background
    private void generateNoiseTexture() {
        noiseTexture = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = noiseTexture.createGraphics();
        Random rand = new Random(12345);

        for (int y = 0; y < 100; y++) {
            for (int x = 0; x < 100; x++) {
                int noise = rand.nextInt(80) - 40;
                int gray = Math.max(0, Math.min(255, 128 + noise));
                int alpha = 15;
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

        //gambar background scene jika ada
        if (bgImage != null) {
            g2d.drawImage(bgImage, 0, 0, w, h, null);
        } else {
            //jika tidak ada gambar → fallback gradient + gelombang dekoratif
            GradientPaint bgGradient = new GradientPaint(
                0, 0, new Color(95, 70, 65),
                0, h, new Color(115, 85, 75)
            );
            g2d.setPaint(bgGradient);
            g2d.fillRect(0, 0, w, h);

            g2d.setColor(new Color(80, 55, 50));
            Path2D.Double backWave = new Path2D.Double();
            backWave.moveTo(0, h * 0.65);
            backWave.curveTo(w * 0.25, h * 0.60, w * 0.5, h * 0.68, w * 0.75, h * 0.62);
            backWave.curveTo(w * 0.85, h * 0.60, w * 0.95, h * 0.64, w, h * 0.62);
            backWave.lineTo(w, h);
            backWave.lineTo(0, h);
            backWave.closePath();
            g2d.fill(backWave);

            g2d.setColor(new Color(75, 50, 45));
            Path2D.Double frontWave = new Path2D.Double();
            frontWave.moveTo(0, h * 0.78);
            frontWave.curveTo(w * 0.2, h * 0.75, w * 0.5, h * 0.80, w * 0.8, h * 0.76);
            frontWave.curveTo(w * 0.9, h * 0.75, w * 0.98, h * 0.78, w, h * 0.77);
            frontWave.lineTo(w, h);
            frontWave.lineTo(0, h);
            frontWave.closePath();
            g2d.fill(frontWave);
        }

        //tambahkan noise ke background
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
        TexturePaint noisePaint = new TexturePaint(noiseTexture, new Rectangle(0, 0, 100, 100));
        g2d.setPaint(noisePaint);
        g2d.fillRect(0, 0, w, h);

        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
    }

        private void initComponents() {

        backBtn = new MenuButton();
        backBtn.setFocusable(false);
        backBtn.addActionListener(e -> {
            if (uiManager != null) {

                //stop timer visual
                visualTimer.stop();
                currentTimeLeft = 30;

                if (timerBadge != null) {
                    timerBadge.updateTime(currentTimeLeft);
                    timerBadge.setVisible(false);
                }

                //stop timer lain jika ada di uiManager
                Object timerObj = uiManager.getUiComponents().get("decisionTimer");
                if (timerObj instanceof Timer) {
                    ((Timer) timerObj).stop();
                } else if (timerObj != null) {
                    try {
                        timerObj.getClass().getMethod("stopTimer").invoke(timerObj);
                    } catch (Exception ex) {}
                }

                //reset state scene
                lastSceneId = -1;

                //reset sesi game jika ada
                Object sessionObj = uiManager.getUiComponents().get("gameSession");
                if (sessionObj instanceof GameSession) {
                    ((GameSession) sessionObj).startNewGame();
                }

                //kembali ke main menu
                uiManager.showScreen("MAIN_MENU");
            }
        });
        add(backBtn);

        sceneInfo = new SceneInfoBadge();
        add(sceneInfo);

        timerBadge = new TimerBadge();
        add(timerBadge);

        statsBar = new StatsBarPanel();
        add(statsBar);

        characterPanel = new CharacterDisplayPanel();
        add(characterPanel);

        dialogueBox = new DialogueBoxPanel();
        add(dialogueBox);

        setComponentZOrder(backBtn, 0);

        updateLayout();
    }

    private void updateLayout() {
        int w = getWidth();
        int h = getHeight();

        if (w == 0 || h == 0) return;

        backBtn.setBounds(20, 12, 85, 32);
        sceneInfo.setBounds(w - 110, 12, 90, 58);
        timerBadge.setBounds(w - 210, 12, 90, 58);

        statsBar.setBounds(20, 78, w - 40, 26);

        int charY = 100;
        int charH = 340;
        characterPanel.setBounds(0, charY, w, charH);

        int dialogH = 280;
        int dialogY = 440; 
        dialogueBox.setBounds(30, dialogY, w - 60, dialogH);

        revalidate();
        repaint();
    }

        public void displayScene(Scene scene) {
        if (scene == null) return;

        boolean isNewScene = (scene.getSceneId() != lastSceneId);
        lastSceneId = scene.getSceneId();

        try {
            String bgPath = "assets/Background/scene" + scene.getSceneId() + ".png";
            File bgFile = new File(bgPath);
            if (bgFile.exists()) {
                bgImage = ImageIO.read(bgFile);
            } else {
                bgImage = null;
            }
        } catch (Exception e) {
            bgImage = null;
        }

        repaint();

        sceneInfo.updateInfo(1, scene.getSceneId(), 9);
        characterPanel.setCharacters(scene.getCharacters());
        dialogueBox.setScene(scene);

        boolean hasChoices = (scene.getChoices() != null && !scene.getChoices().isEmpty());

        if (hasChoices) {
            if (timerBadge != null) {
                timerBadge.setVisible(true);
                currentTimeLeft = 30;
                timerBadge.updateTime(currentTimeLeft);
            }
            if (visualTimer != null) {
                visualTimer.restart();
            }
        } else {
            if (timerBadge != null) {
                timerBadge.setVisible(false);
                currentTimeLeft = 30;
                timerBadge.updateTime(currentTimeLeft);
            }
            if (visualTimer != null) {
                visualTimer.stop();
            }
        }
    }

    public void updateRelationships(Map<String, Integer> relationships) {
        if (relationships != null) {
            statsBar.updateStats(relationships);
        }
    }

    public void updateTimer(int secondsRemaining) {
        //optional sinkronisasi kalau kamu mau pakai
    }
    //kelas tombol kecil buat balik ke menu
    static class MenuButton extends JButton {
        private boolean isHovered = false; //penanda mouse lagi di atas tombol atau tidak

        public MenuButton() {
            setText("Menu"); //tulisan pada tombol
            setContentAreaFilled(false); //hapus background default swing
            setFocusPainted(false); //biar ga ada outline fokus
            setBorderPainted(false); //hapus border standar
            setForeground(new Color(225, 205, 195)); //warna teks cream
            setFont(new Font("SansSerif", Font.PLAIN, 15)); //font standar tombol kecil
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); //ubah cursor jadi tangan saat hover

            //event hover buat efek warna berubah pas mouse masuk dan keluar
            addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent e) {
                    isHovered = true; //aktifkan efek hover
                    repaint();
                }
                public void mouseExited(java.awt.event.MouseEvent e) {
                    isHovered = false; //matikan efek hover
                    repaint();
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
            ); //biar bentuk dan border halus

            int w = getWidth();
            int h = getHeight();

            //warna tombol berubah kalau dihover
            g2d.setColor(
                isHovered
                    ? new Color(100, 65, 68, 240) //warna lebih terang kalau dihover
                    : new Color(90, 59, 61, 230)  //warna normal
            );

            //bentuk tombol oval / rounded penuh
            g2d.fillRoundRect(0, 0, w, h, h, h);

            //border tipis warna coklat muda
            g2d.setColor(new Color(130, 95, 95, 200));
            g2d.setStroke(new BasicStroke(1.5f));
            g2d.drawRoundRect(1, 1, w - 2, h - 2, h - 2, h - 2);

            super.paintComponent(g); //gambar teks
        }
    }

    //kotak kecil di pojok kanan atas buat nunjukin info bab & scene
    class SceneInfoBadge extends JPanel {
        private int current = 1;
        private int total = 9;

        public SceneInfoBadge() {
            setOpaque(false);
        }

        public void updateInfo(int b, int c, int t) {
            this.current = c;
            this.total = t;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth();
            int h = getHeight();

            // background sama seperti TimerBadge
            g2d.setColor(new Color(95, 65, 60, 235));
            g2d.fillRoundRect(0, 0, w, h, 16, 16);

            // border sama
            g2d.setColor(new Color(185, 120, 115, 220));
            g2d.setStroke(new BasicStroke(2f));
            g2d.drawRoundRect(1, 1, w - 2, h - 2, 16, 16);

            // label kecil "Scene"
            g2d.setColor(new Color(240, 230, 220));
            g2d.setFont(new Font("SansSerif", Font.PLAIN, 10));
            String label = "Scene";
            g2d.drawString(label, (w - g2d.getFontMetrics().stringWidth(label)) / 2, 20);

            // angka besar "1/9" → warna sama
            g2d.setColor(new Color(240, 230, 220));
            g2d.setFont(new Font("SansSerif", Font.BOLD, 18));
            String val = current + "/" + total;
            g2d.drawString(val, (w - g2d.getFontMetrics().stringWidth(val)) / 2, 44);
        }
    }

        //badge kecil di pojok kanan atas yang nunjukin sisa waktu (timer)
    class TimerBadge extends JPanel {
        private int time = 30; //default 30 detik

        public TimerBadge() {
            setOpaque(false); //biar background tidak nutup area lain
        }

        //dipanggil dari luar pas waktu berkurang
        public void updateTime(int t) {
            this.time = t;
            repaint(); //update tampilan angka
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth();
            int h = getHeight();

            //background coklat gelap transparan (sama gaya dengan info badge)
            g2d.setColor(new Color(95, 65, 60, 235));
            g2d.fillRoundRect(0, 0, w, h, 16, 16);

            //border coklat muda
            g2d.setColor(new Color(185, 120, 115, 220));
            g2d.setStroke(new BasicStroke(2f));
            g2d.drawRoundRect(1, 1, w - 2, h - 2, 16, 16);

            //label "Waktu" kecil di atas
            g2d.setColor(new Color(240, 230, 220)); //cream
            g2d.setFont(new Font("SansSerif", Font.PLAIN, 10));
            String label = "Waktu";
            g2d.drawString(
                label,
                (w - g2d.getFontMetrics().stringWidth(label)) / 2,
                20
            );

            //kalau waktu tinggal 10 detik → warnanya merah biar tegang
            if (time <= 10 && time > 0) {
                g2d.setColor(new Color(255, 100, 100));
            } else {
                g2d.setColor(new Color(240, 230, 220));
            }

            //angka besar “30s” / “15s”, dst
            g2d.setFont(new Font("SansSerif", Font.BOLD, 18));
            String timeText = time + "s";

            g2d.drawString(
                timeText,
                (w - g2d.getFontMetrics().stringWidth(timeText)) / 2,
                45
            );
        }
    }

    //panel baris kecil yang menampilkan hubungan pemain dengan tokoh lain
    class StatsBarPanel extends JPanel {

        //nyimpen nilai hubungan ke setiap tokoh
        private Map<String, Integer> currentStats = new LinkedHashMap<>();

        public StatsBarPanel() {
            setOpaque(false); //biar background transparan
        }

        //dipanggil dari luar setiap ada perubahan statistik
        public void updateStats(Map<String, Integer> s) {
            currentStats.clear();
            if (s != null) {
                currentStats.put("SOEKARNO", s.getOrDefault("SOEKARNO", 50));
                currentStats.put("HATTA",    s.getOrDefault("HATTA", 50));
                currentStats.put("PEMUDA",   s.getOrDefault("PEMUDA", 50));
                currentStats.put("TRUST",    s.getOrDefault("TRUST", 50));
            }
            repaint(); //update tampilan
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth();
            int h = getHeight();

            //background bar transparan cream muda
            g2d.setColor(new Color(220, 205, 190, 240));
            g2d.fillRoundRect(0, 0, w, h, 20, 20);

            //border tipis coklat muda
            g2d.setColor(new Color(180, 160, 145, 200));
            g2d.setStroke(new BasicStroke(1.5f));
            g2d.drawRoundRect(1, 1, w - 3, h - 3, 20, 20);

            if (currentStats.isEmpty()) return;

            //urutan stat selalu fix: Soekarno, Hatta, Pemuda, Trust
            String[] keys   = {"SOEKARNO", "HATTA", "PEMUDA", "TRUST"};
            String[] labels = {"Soekarno", "Hatta", "Pemuda", "Trust"};

            int count = keys.length;          // SELALU 4 kolom
            int sw    = (w - 40) / count;     //space per kolom
            int x     = 20;

            g2d.setFont(new Font("SansSerif", Font.PLAIN, 11));

            for (int i = 0; i < keys.length; i++) {
                String key = keys[i];
                int val = currentStats.getOrDefault(key, 50);
                String label = labels[i];

                //tulis nama tokoh
                g2d.setColor(new Color(80, 50, 45));
                int nw = g2d.getFontMetrics().stringWidth(label);
                g2d.drawString(label, x, (h + 5) / 2);

                //bar kosong transparan
                int bx = x + nw + 8;
                int bh = 8;
                int by = (h - bh) / 2;
                int bw = sw - nw - 15;

                g2d.setColor(new Color(180, 160, 145, 150));
                g2d.fillRoundRect(bx, by, bw, bh, 4, 4);

                //bar berisi → panjang sesuai nilai relasi (0–100)
                int fw = (int) (bw * (val / 100.0));
                GradientPaint barFill = new GradientPaint(
                    bx,
                    by,
                    new Color(145, 75, 75),
                    bx + fw,
                    by,
                    new Color(168, 95, 85)
                );

                g2d.setPaint(barFill);
                g2d.fillRoundRect(bx, by, fw, bh, 4, 4);

                x += sw; //geser ke kolom selanjutnya
            }
        }
    }

    //panel untuk menampilkan potret tokoh yang sedang muncul di scenee
    class CharacterDisplayPanel extends JPanel {

        //list karakter dari scene sekarang
        private java.util.List<Character> characters = new ArrayList<>();

        public CharacterDisplayPanel() {
            setOpaque(false); //biar background transparan
            setLayout(null);  //posisi manual
        }

        //dipanggil dari StoryPanel.displayScene()
        public void setCharacters(java.util.List<Character> c) {
            this.characters = (c != null) ? c : new ArrayList<>();

            removeAll(); //hapus potret lama

            //maksimal tampil 2 karakter untuk format dialog
            int limit = Math.min(characters.size(), 2);

            //ambil width panel
            int w = (getWidth() > 0) ? getWidth() : 1024;

            for (int i = 0; i < limit; i++) {
                Character chara = characters.get(i);
                CharacterPortrait p = new CharacterPortrait(chara);

                int x = (i == 0) ? 100 : w - 260 - 100;

                p.setBounds(x, 20, 260, 300);

                add(p);
            }
            revalidate();
            repaint();
        }

        //override setBounds supaya karakter ikut berubah posisi saat resize
        @Override
        public void setBounds(int x, int y, int width, int height) {
            super.setBounds(x, y, width, height);
        }
    }

        //panel kecil yang menampilkan gambar 1 tokoh + nama tokohnya
    class CharacterPortrait extends JPanel {
        private Character character;
        private BufferedImage charImage;

        public CharacterPortrait(Character c) {
            this.character = c;
            setOpaque(false); 
            loadCharacterImage(); //coba muat gambar tokoh dari folder
        }

        //cari file gambar berdasarkan nama karakter
        private void loadCharacterImage() {
            Map<String, String> fixTypoMap = new HashMap<>();
            //perbaikan typo yang pernah terjadi
            fixTypoMap.put("jenderalnishimura", "jendralnismura.png");

            try {
                //nama file gambar = nama karakter lowercase tanpa spasi
                String rawName = character.getCharacterName().replaceAll("\\s+", "").toLowerCase();

                String fileNameToSearch = rawName;

                if (fixTypoMap.containsKey(rawName)) {
                    fileNameToSearch = fixTypoMap.get(rawName);
                }

                //cari di folder assets/Tokoh
                File folder = new File("assets/Tokoh");
                File[] files = folder.listFiles();

                if (files != null) {
                    for (File f : files) {
                        String fName = f.getName().toLowerCase();

                        if (fName.startsWith(rawName) || fName.equals(fileNameToSearch)) {
                            charImage = ImageIO.read(f);
                            break;
                        }
                    }
                }
            } catch (Exception e) {
                charImage = null; //fallback jika error
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth();
            int h = getHeight();

            //kalau gambar ada → tampilkan penuh
            if (charImage != null) {
                g2d.setClip(new RoundRectangle2D.Float(0, 0, w, h, 16, 16));
                g2d.drawImage(charImage, 0, 0, w, h, null);
                g2d.setClip(null);
            } 
            //kalau tidak ada → tampilkan card placeholder
            else {
                GradientPaint photo = new GradientPaint(
                    0, 0,
                    new Color(180, 190, 200, 100),
                    w, h,
                    new Color(140, 150, 160, 100)
                );
                g2d.setPaint(photo);
                g2d.fillRoundRect(0, 0, w, h, 16, 16);
            }

            //border luar agar lebih rapi
            g2d.setColor(new Color(120, 100, 90, 150));
            g2d.setStroke(new BasicStroke(2f));
            g2d.drawRoundRect(1, 1, w - 3, h - 3, 16, 16);

            //kotak nama di bawah
            g2d.setColor(new Color(60, 40, 35, 200));
            g2d.fillRoundRect(5, h - 36, w - 10, 30, 12, 12);

            //tulisan nama karakter
            g2d.setColor(new Color(240, 230, 220));
            g2d.setFont(new Font("SansSerif", Font.BOLD, 14));

            String name = character.getCharacterName();
            int tw = g2d.getFontMetrics().stringWidth(name);

            g2d.drawString(name, (w - tw) / 2, h - 16);
        }
    }

        //panel besar tempat dialog & pilihan muncul (bagian bawah layar)
    class DialogueBoxPanel extends JPanel {

        public DialogueBoxPanel() {
            setOpaque(false);  //biar background transparan
            setLayout(null);   //posisi elemen manual
        }

        //dipanggil setiap scene baru muncul
        public void setScene(Scene scene) {
            removeAll(); //hapus dialog lama dulu

            int w = getWidth();
            int h = getHeight();

            //kalau belum di-render, pakai ukuran default supaya tidak error
            if (w == 0) w = 964;
            if (h == 0) h = 280;

            //nama speaker (tokoh yang bicara)
            String speaker = "Narator";
            if (scene.getCharacters() != null && !scene.getCharacters().isEmpty()) {
                speaker = scene.getCharacters().get(0).getCharacterName();
            }

            //tab nama tokoh (card kecil di atas kotak dialog)
            SpeakerTab tab = new SpeakerTab(speaker);
            tab.setBounds(20, 0, 200, 40);
            add(tab);

            //teks dialog
            JTextArea text = new JTextArea(scene.getSceneDescription());
            text.setFont(new Font("SansSerif", Font.PLAIN, 18));
            text.setForeground(new Color(250, 245, 240));
            text.setOpaque(false);
            text.setEditable(false);
            text.setLineWrap(true);
            text.setWrapStyleWord(true);
            text.setBounds(40, 60, w - 80, 90);
            add(text);

            //pilihan (kalau ada)
            java.util.List<Choice> choices = scene.getChoices();
            if (choices != null && !choices.isEmpty()) {

                int btnY = 160; //mulai di bawah teks dialog
                int idx = 0;

                for (Choice c : choices) {
                    ChoiceButton btn = new ChoiceButton(c.getChoiceText());
                    btn.setBounds(40, btnY, w - 80, 45);

                    final int btnIndex = idx;

                    //saat tombol pilihan diklik
                    btn.addActionListener(e -> {
                        visualTimer.stop(); //stop timer visual
                        if (uiManager != null) {
                            uiManager.handleChoiceSelection(c, btnIndex);
                        }
                    });

                    add(btn);
                    btnY += 55; //jarak antar tombol
                    idx++;
                }
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth();
            int h = getHeight();

            //background kotak dialog (gradient merah tua → coklat)
            GradientPaint bg = new GradientPaint(
                0, 30,
                new Color(107, 78, 79, 250),
                0, h,
                new Color(95, 70, 70, 250)
            );

            g2d.setPaint(bg);
            g2d.fillRoundRect(0, 30, w, h - 30, 20, 20);

            //border luar
            g2d.setColor(new Color(180, 140, 130, 180));
            g2d.setStroke(new BasicStroke(1.5f));
            g2d.drawRoundRect(1, 31, w - 3, h - 32, 20, 20);
        }
    }

        //panel kecil berisi nama tokoh yang sedang bicara
    class SpeakerTab extends JPanel {

        String name;

        public SpeakerTab(String n) {
            this.name = n;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {

            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth();
            int h = getHeight();

            //background merah muda tua
            GradientPaint bg = new GradientPaint(
                0, 0,
                new Color(168, 120, 115, 245),
                0, h,
                new Color(150, 105, 100, 245)
            );

            g2d.setPaint(bg);
            g2d.fillRoundRect(0, 0, w, h, 12, 12);

            //nama tokoh warna cream
            g2d.setColor(new Color(240, 230, 220));
            g2d.setFont(new Font("SansSerif", Font.BOLD, 15));

            g2d.drawString(name, 25, 24);
        }
    }

        //tombol pilihan dalam dialog (misal: "Ajak Soekarno", "Diam saja", dll)
    class ChoiceButton extends JButton {

        boolean hover = false; //penanda apakah mouse berada di atas tombol

        public ChoiceButton(String t) {

            setText(t);

            //hapus tampilan default tombol supaya bisa digambar manual
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);

            //warna teks cream
            setForeground(new Color(250, 245, 240));

            setFont(new Font("SansSerif", Font.BOLD, 16));
            setCursor(new Cursor(Cursor.HAND_CURSOR));

            //event mouse masuk/keluar → ubah tampilan hover
            addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseEntered(java.awt.event.MouseEvent e) {
                    hover = true;
                    repaint(); //update tampilan
                }
                @Override
                public void mouseExited(java.awt.event.MouseEvent e) {
                    hover = false;
                    repaint();
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {

            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth();
            int h = getHeight();

            //gradient background (warna berubah sedikit saat hover)
            GradientPaint gp;
            if (hover) {
                //lebih terang sedikit untuk efek "dipilih"
                gp = new GradientPaint(
                    0, 0, new Color(137, 95, 97, 220),
                    w, h, new Color(188, 126, 121, 220)
                );
            } else {
                //warna normal
                gp = new GradientPaint(
                    0, 0, new Color(127, 85, 87, 200),
                    w, h, new Color(178, 116, 111, 200)
                );
            }

            g2d.setPaint(gp);
            g2d.fillRoundRect(0, 0, w, h, 14, 14);

            //border tipis warna cream muda
            g2d.setColor(new Color(224, 203, 185, 150));
            g2d.setStroke(new BasicStroke(1.5f));
            g2d.drawRoundRect(1, 1, w - 2, h - 2, 14, 14);

            //gambar teks
            super.paintComponent(g);
        }
    }
}



