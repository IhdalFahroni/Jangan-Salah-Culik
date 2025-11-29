import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import javax.swing.SwingWorker;

//ini panel utama buat halaman login dan daftar
public class LoginPanel extends JPanel {
    
    //referensi ke otak game punya temen biar bisa akses database
    private RengasdengklokGame mainApp; 
    
    //komponen-komponen visual buat input dan tombol
    private JTextField usernameField;
    private JPasswordField passwordField;
    private boolean isLoginMode = true; //penanda lagi di tab login atau daftar
    private JButton loginTab, registerTab;
    private JButton maleButton, femaleButton;
    private String selectedGender = "Male"; //default gender cowo sesuai database
    private JPanel genderPanel;
    private JButton submitButton;
    private BufferedImage noiseTexture; //buat nyimpen gambar efek bintik-bintik
    private FormContainer formContainer; //kotak transparan di tengah layar
    
    //ukuran layar disesuaikan sama frame utama
    private static final int WINDOW_WIDTH = 1024;
    private static final int WINDOW_HEIGHT = 768;
    
    public LoginPanel(RengasdengklokGame app) {
        this.mainApp = app; 
        
        //pake null layout biar bisa atur posisi x y sendiri
        setLayout(null);
        setBackground(ColorPalette.DARK_BG_1); 
        setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
        
        //siapin tekstur background dulu baru komponen
        generateNoiseTexture();
        initComponents();
    }
    
    //method ini dipanggil pas logout buat bersihin inputan
    public void reset() {
        if (usernameField != null) usernameField.setText("");
        if (passwordField != null) passwordField.setText("");
        if (formContainer != null) formContainer.switchToLogin();
    }
    
    //BAGIAN VISUAL (GAMBAR & EFEK)
    
    //bikin gambar bintik-bintik transparan (noise) biar estetik
    private void generateNoiseTexture() {
        noiseTexture = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = noiseTexture.createGraphics();
        java.util.Random rand = new java.util.Random(12345);
        
        for (int y = 0; y < 100; y++) {
            for (int x = 0; x < 100; x++) {
                int noise = rand.nextInt(80) - 40;
                int gray = 128 + noise;
                gray = Math.max(0, Math.min(255, gray));
                int alpha = 20; //transparansi tipis aja
                noiseTexture.setRGB(x, y, new Color(gray, gray, gray, alpha).getRGB());
            }
        }
        g.dispose();
    }
    
    //disini kita gambar backgroundnya secara manual
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        //biar gambarnya alus dan ga kotak-kotak
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        int w = getWidth();
        int h = getHeight();
        
        //1.gambar gradient background dari merah gelap ke hitam
        Color[] gradColors = { ColorPalette.DARK_BG_1, ColorPalette.DARK_BG_2, ColorPalette.DARK_BG_3 };
        
        for (int y = 0; y < h; y++) {
            float ratio = (float) y / h;
            //itung gradasi warna manual biar smooth
            Color c1 = (ratio < 0.5f) ? gradColors[0] : gradColors[1];
            Color c2 = (ratio < 0.5f) ? gradColors[1] : gradColors[2];
            float localRatio = (ratio < 0.5f) ? ratio * 2 : (ratio - 0.5f) * 2;
            
            int r = (int) (c1.getRed() + (c2.getRed() - c1.getRed()) * localRatio);
            int gr = (int) (c1.getGreen() + (c2.getGreen() - c1.getGreen()) * localRatio);
            int b = (int) (c1.getBlue() + (c2.getBlue() - c1.getBlue()) * localRatio);
            
            g2d.setColor(new Color(r, gr, b));
            g2d.fillRect(0, y, w, 1);
        }
        
        //2.tempel tekstur noise di atas gradient
        for (int y = 0; y < h; y += 100) {
            for (int x = 0; x < w; x += 100) {
                g2d.drawImage(noiseTexture, x, y, null);
            }
        }
        
        //3.kasih efek glow (cahaya) di tengah biar dramatis
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.1f));
        int glowSize = 384; 
        RadialGradientPaint glow1 = new RadialGradientPaint(w / 4, h / 4, glowSize / 2, new float[]{0f, 1f}, new Color[]{new Color(168, 106, 101, 255), new Color(168, 106, 101, 0)});
        g2d.setPaint(glow1);
        g2d.fillOval(w / 4 - glowSize / 2, h / 4 - glowSize / 2, glowSize, glowSize);
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
    }
    
    //susun posisi komponen-komponen ke layar
    private void initComponents() {
        //judul besar rengasdengklok
        JLabel titleLabel = new JLabel("Rengasdengklok", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Georgia", Font.BOLD, 48));
        titleLabel.setForeground(ColorPalette.CHINA_DOLL);
        titleLabel.setBounds(0, 60, WINDOW_WIDTH, 60);
        add(titleLabel);
        
        //garis hiasan di bawah judul
        OrnamentalDivider divider = new OrnamentalDivider();
        divider.setBounds((WINDOW_WIDTH - 200) / 2, 130, 200, 4);
        add(divider);
        
        //subjudul miring
        JLabel subtitleLabel = new JLabel("Jangan Salah Culik!", SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("Georgia", Font.ITALIC, 18));
        subtitleLabel.setForeground(ColorPalette.ROSEWATER);
        subtitleLabel.setBounds(0, 145, WINDOW_WIDTH, 25);
        add(subtitleLabel);
        
        //masukin form container ke tengah layar
        formContainer = new FormContainer();
        int formWidth = 448;
        int formX = (WINDOW_WIDTH - formWidth) / 2;
        //posisi y disamakan 185 biar ga lompat
        formContainer.setBounds(formX, 185, formWidth, 380);
        add(formContainer);
        
        //footer tulisan di bawah
        FooterPanel footer = new FooterPanel();
        footer.setBounds(0, WINDOW_HEIGHT - 70, WINDOW_WIDTH, 30);
        add(footer);
    }
    
    //--- LOGIKA PAS TOMBOL MASUK/DAFTAR DIKLIK ---
    private void handleSubmit() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();
        
        //validasi input ga boleh kosong
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Username dan password tidak boleh kosong!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        //matiin tombol biar user ga spam klik pas loading
        submitButton.setEnabled(false);
        submitButton.setText(isLoginMode ? "Sedang Memproses..." : "Mendaftarkan...");
        
        //pake swingworker biar aplikasi ga macet (not responding) pas connect database
        new SwingWorker<Boolean, String>() {
            String errorMessage = "Gagal";
            User loggedUser; 

            //ini jalan di background thread (aman buat database)
            @Override
            protected Boolean doInBackground() throws Exception {
                DatabaseManager db = mainApp.getDbManager();
                
                if (isLoginMode) {
                    //coba login pake method punya temen
                    loggedUser = db.authenticateUser(username, password);
                    return loggedUser != null;
                } else {
                    //validasi register
                    if (username.length() < 3 || password.length() < 3) {
                        errorMessage = "Username/Password minimal 3 karakter!";
                        return false;
                    }
                    //coba create user baru
                    if (db.createUser(username, password)) {
                        //kalo sukses langsung login otomatis buat dapet id
                        loggedUser = db.authenticateUser(username, password);
                        if (loggedUser != null) {
                            //simpan profile gender juga
                            String genderDb = selectedGender.equals("Male") ? "Laki-laki" : "Perempuan";
                            db.createProfile(loggedUser.getUserId(), username, genderDb);
                            return true;
                        }
                    } else {
                        errorMessage = "Username sudah digunakan!";
                    }
                    return false;
                }
            }

            //ini jalan setelah proses background selesai (balik ke ui)
            @Override
            protected void done() {
                try {
                    boolean success = get();
                    submitButton.setEnabled(true);
                    submitButton.setText(isLoginMode ? "Masuk ke Permainan" : "Daftar Akun");
                    
                    if (success) {
                        if (isLoginMode) {
                            //login sukses pindah ke menu utama
                            mainApp.onLoginSuccess(loggedUser); 
                        } else {
                            //register sukses pindah ke tab login
                            JOptionPane.showMessageDialog(LoginPanel.this, 
                                "Akun berhasil dibuat! Silakan Login.", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                            formContainer.switchToLogin();
                            usernameField.setText(username);
                            passwordField.setText("");
                        }
                    } else {
                        //gagal munculin pesan error
                        JOptionPane.showMessageDialog(LoginPanel.this, 
                            isLoginMode ? "Username atau password salah!" : errorMessage, 
                            "Gagal", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    submitButton.setEnabled(true);
                }
            }
        }.execute();
    }
    
    //INNER CLASSES (KOMPONEN VISUAL CUSTOM) 
    
    //garis hiasan di bawah judul
    class OrnamentalDivider extends JPanel {
        public OrnamentalDivider() { setOpaque(false); }
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            int w = getWidth(); int centerX = w / 2; int centerY = 2;
            
            //gambar garis gradasi kiri kanan
            GradientPaint leftGrad = new GradientPaint(0, centerY, new Color(168, 106, 101, 0), 64, centerY, new Color(168, 106, 101, 255));
            g2d.setPaint(leftGrad); g2d.drawLine(0, centerY, centerX - 16, centerY);
            
            //gambar diamond di tengah
            g2d.setColor(ColorPalette.COPPER_ROSE);
            int[] xPoints = {centerX, centerX + 2, centerX, centerX - 2};
            int[] yPoints = {centerY - 2, centerY, centerY + 2, centerY};
            g2d.fillPolygon(xPoints, yPoints, 4);
            
            GradientPaint rightGrad = new GradientPaint(w - 64, centerY, new Color(168, 106, 101, 255), w, centerY, new Color(168, 106, 101, 0));
            g2d.setPaint(rightGrad); g2d.drawLine(centerX + 16, centerY, w, centerY);
        }
    }
    
    //panel footer di bawah
    class FooterPanel extends JPanel {
        public FooterPanel() { setOpaque(false); }
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            
            int centerX = getWidth() / 2; int centerY = getHeight() / 2;
            
            //garis kiri
            GradientPaint leftGrad = new GradientPaint(centerX - 180, centerY, new Color(168, 106, 101, 0), centerX - 80, centerY, new Color(168, 106, 101, 255));
            g2d.setPaint(leftGrad); g2d.drawLine(centerX - 180, centerY, centerX - 80, centerY);
            
            //teks footer
            g2d.setFont(new Font("Georgia", Font.PLAIN, 12));
            g2d.setColor(ColorPalette.ROSEWATER);
            String text = "Indonesia Merdeka • 1945";
            FontMetrics fm = g2d.getFontMetrics();
            int textWidth = fm.stringWidth(text);
            g2d.drawString(text, centerX - textWidth / 2, centerY + 5);
            
            //garis kanan
            GradientPaint rightGrad = new GradientPaint(centerX + 80, centerY, new Color(168, 106, 101, 255), centerX + 180, centerY, new Color(168, 106, 101, 0));
            g2d.setPaint(rightGrad); g2d.drawLine(centerX + 80, centerY, centerX + 180, centerY);
        }
    }
    
    //kotak transparan tempat form berada
    class FormContainer extends JPanel {
        public FormContainer() { setLayout(null); setOpaque(false); initFormComponents(); }
        
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
            int w = getWidth(); int h = getHeight();
            
            //background kotak semi transparan
            g2d.setColor(new Color(216, 166, 148, 230));
            g2d.fillRoundRect(0, 0, w, h, 16, 16);
            
            //garis border kotak
            g2d.setColor(new Color(168, 106, 101, 77));
            g2d.setStroke(new BasicStroke(1.5f));
            g2d.drawRoundRect(1, 1, w - 3, h - 3, 16, 16);
        }
        
        private void initFormComponents() {
            int px = 32; int py = 32; int contentWidth = 384; int halfWidth = (contentWidth - 8) / 2;
            
            //tombol tab masuk
            loginTab = new TabButton("Masuk", true);
            loginTab.setBounds(px, py, halfWidth, 48);
            loginTab.addActionListener(e -> switchToLogin());
            add(loginTab);
            
            //tombol tab daftar
            registerTab = new TabButton("Daftar", false);
            registerTab.setBounds(px + halfWidth + 8, py, halfWidth, 48);
            registerTab.addActionListener(e -> switchToRegister());
            add(registerTab);
            
            int yPos = 104;
            
            //label username
            JLabel userLabel = new JLabel("Nama Pengguna");
            userLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
            userLabel.setForeground(ColorPalette.PLUM_WINE);
            userLabel.setBounds(px, yPos, contentWidth, 18);
            add(userLabel);
            
            yPos += 26;
            //input username
            usernameField = new StyledTextField("Masukkan nama pengguna", true);
            usernameField.setBounds(px, yPos, contentWidth, 48);
            add(usernameField);
            
            yPos += 68;
            //label password
            JLabel passLabel = new JLabel("Kata Sandi");
            passLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
            passLabel.setForeground(ColorPalette.PLUM_WINE);
            passLabel.setBounds(px, yPos, contentWidth, 18);
            add(passLabel);
            
            yPos += 26;
            //input password
            passwordField = new StyledPasswordField("Masukkan kata sandi");
            passwordField.setBounds(px, yPos, contentWidth, 48);
            add(passwordField);
            
            yPos += 68;
            
            //panel gender cuma muncul pas daftar
            genderPanel = new JPanel();
            genderPanel.setLayout(null);
            genderPanel.setOpaque(false);
            genderPanel.setBounds(px, yPos, contentWidth, 120);
            genderPanel.setVisible(false); //default sembunyi
            
            JLabel genderLabel = new JLabel("Jenis Kelamin");
            genderLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
            genderLabel.setForeground(ColorPalette.PLUM_WINE);
            genderLabel.setBounds(0, 0, contentWidth, 18);
            genderPanel.add(genderLabel);
            
            int genderBtnWidth = (contentWidth - 16) / 2;
            maleButton = new GenderButton("Laki-laki", true);
            maleButton.setBounds(0, 28, genderBtnWidth, 76);
            maleButton.addActionListener(e -> selectGender("Male"));
            genderPanel.add(maleButton);
            
            femaleButton = new GenderButton("Perempuan", false);
            femaleButton.setBounds(genderBtnWidth + 16, 28, genderBtnWidth, 76);
            femaleButton.addActionListener(e -> selectGender("Female"));
            genderPanel.add(femaleButton);
            add(genderPanel);
            
            //tombol submit utama
            submitButton = new GradientSubmitButton("Masuk ke Permainan");
            //posisi y diset 290 biar sama kaya mode login awal (ga lompat)
            submitButton.setBounds(px, 290, contentWidth, 56);
            submitButton.addActionListener(e -> handleSubmit());
            add(submitButton);
        }
        
        //logika ganti ke mode login
        public void switchToLogin() {
            if (isLoginMode) return;
            isLoginMode = true;
            ((TabButton)loginTab).setActive(true);
            ((TabButton)registerTab).setActive(false);
            genderPanel.setVisible(false);
            submitButton.setText("Masuk ke Permainan");
            submitButton.setBounds(32, 290, 384, 56);
            int formX = (WINDOW_WIDTH - 448) / 2;
            setBounds(formX, 185, 448, 378); //kecilin kotak
            repaint();
        }
        
        //logika ganti ke mode register
        private void switchToRegister() {
            if (!isLoginMode) return;
            isLoginMode = false;
            ((TabButton)loginTab).setActive(false);
            ((TabButton)registerTab).setActive(true);
            genderPanel.setVisible(true);
            submitButton.setText("Daftar Akun");
            submitButton.setBounds(32, 410, 384, 56);
            int formX = (WINDOW_WIDTH - 448) / 2;
            setBounds(formX, 185, 448, 498); //gedein kotak
            repaint();
        }
        
        private void selectGender(String gender) {
            selectedGender = gender;
            ((GenderButton)maleButton).setSelected(gender.equals("Male"));
            ((GenderButton)femaleButton).setSelected(gender.equals("Female"));
        }
    }
    
    //tombol tab di atas masuk daftar
    class TabButton extends JButton {
        boolean active;
        public TabButton(String text, boolean active) { 
            super(text); this.active = active; 
            setContentAreaFilled(false); setBorderPainted(false); setFocusPainted(false); 
            setFont(new Font("SansSerif", Font.BOLD, 14)); setCursor(new Cursor(Cursor.HAND_CURSOR));
            //set warna awal biar ga hitam pas pertama run
            setForeground(active ? ColorPalette.CHINA_DOLL : ColorPalette.PLUM_WINE);
        }
        public void setActive(boolean a) { 
            this.active = a; 
            //update warna teks pas berubah status
            setForeground(active ? ColorPalette.CHINA_DOLL : ColorPalette.PLUM_WINE);
            repaint(); 
        }
        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            if(active) {
                //kalo aktif warna gradient merah
                GradientPaint gp = new GradientPaint(0,0, ColorPalette.PLUM_WINE, getWidth(), 0, ColorPalette.COPPER_ROSE);
                g2d.setPaint(gp);
            } else {
                //kalo mati warna transparan
                g2d.setColor(new Color(224,203,185,102));
            }
            g2d.fillRoundRect(0,0,getWidth(),getHeight(),12,12);
            super.paintComponent(g);
        }
    }
    
    //textfield username ada icon
    class StyledTextField extends JTextField {
        private String placeholder;
        private boolean isUsername;
        public StyledTextField(String placeholder, boolean isUsername) {
            this.placeholder = placeholder; this.isUsername = isUsername;
            setFont(new Font("SansSerif", Font.PLAIN, 14));
            setForeground(ColorPalette.PLUM_WINE);
            setBackground(ColorPalette.CHINA_DOLL);
            setOpaque(false);
            setBorder(BorderFactory.createEmptyBorder(12, 52, 12, 16));
            setCaretColor(ColorPalette.PLUM_WINE);
        }
        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            //background border
            g2d.setColor(getBackground()); g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
            g2d.setColor(new Color(139, 90, 94)); g2d.setStroke(new BasicStroke(1.5f));
            g2d.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 12, 12);
            //gambar icon user manual
            g2d.setColor(ColorPalette.COPPER_ROSE); g2d.setStroke(new BasicStroke(2f));
            int iconX = 16; int iconY = getHeight() / 2;
            if (isUsername) { g2d.drawOval(iconX + 6, iconY - 9, 8, 8); g2d.drawArc(iconX + 2, iconY + 1, 16, 14, 0, -180); }
            //teks placeholder
            g2d.setColor(getForeground()); g2d.setFont(getFont());
            if (getText().isEmpty() && !hasFocus()) {
                g2d.setColor(ColorPalette.DUSTY_ROSE); g2d.drawString(placeholder, 52, getHeight()/2 + 5);
            } else { super.paintComponent(g); }
        }
    }
    
    //password field ada icon
    class StyledPasswordField extends JPasswordField {
        private String placeholder;
        public StyledPasswordField(String placeholder) {
            this.placeholder = placeholder;
            setFont(new Font("SansSerif", Font.PLAIN, 14));
            setForeground(ColorPalette.PLUM_WINE);
            setBackground(ColorPalette.CHINA_DOLL);
            setOpaque(false);
            setBorder(BorderFactory.createEmptyBorder(12, 52, 12, 16));
            setCaretColor(ColorPalette.PLUM_WINE);
        }
        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            //background border
            g2d.setColor(getBackground()); g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
            g2d.setColor(new Color(139, 90, 94)); g2d.setStroke(new BasicStroke(1.5f));
            g2d.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 12, 12);
            //gambar icon gembok manual
            g2d.setColor(ColorPalette.COPPER_ROSE); g2d.setStroke(new BasicStroke(2f));
            int iconX = 16; int iconY = getHeight() / 2;
            g2d.drawRoundRect(iconX + 4, iconY - 1, 12, 10, 2, 2); g2d.drawArc(iconX + 7, iconY - 9, 6, 9, 0, 180);
            //teks placeholder
            g2d.setColor(getForeground()); g2d.setFont(getFont());
            if (getPassword().length == 0 && !hasFocus()) {
                g2d.setColor(ColorPalette.DUSTY_ROSE); g2d.drawString(placeholder, 52, getHeight()/2 + 5);
            } else { super.paintComponent(g); }
        }
    }
    
    //tombol gender custom
    class GenderButton extends JButton {
        boolean selected; String text;
        public GenderButton(String text, boolean selected) { 
            this.text=text; this.selected=selected; 
            setContentAreaFilled(false); setBorderPainted(false); setCursor(new Cursor(Cursor.HAND_CURSOR));
        }
        public void setSelected(boolean s) { this.selected = s; repaint(); }
        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            if(selected) {
                GradientPaint gp = new GradientPaint(0,0, ColorPalette.PLUM_WINE, getWidth(), getHeight(), ColorPalette.COPPER_ROSE);
                g2d.setPaint(gp);
                g2d.fillRoundRect(0,0,getWidth(),getHeight(),12,12);
            } else {
                g2d.setColor(new Color(224,203,185,102));
                g2d.fillRoundRect(0,0,getWidth(),getHeight(),12,12);
            }
            g2d.setColor(selected ? ColorPalette.PLUM_WINE : ColorPalette.COPPER_ROSE);
            g2d.setStroke(new BasicStroke(2));
            g2d.drawRoundRect(1,1,getWidth()-3,getHeight()-3,12,12);
            int centerX = getWidth() / 2; int circleY = 24; int circleSize = 32;
            g2d.setColor(selected ? ColorPalette.CHINA_DOLL : ColorPalette.COPPER_ROSE);
            g2d.drawOval(centerX - circleSize / 2, circleY - circleSize / 2, circleSize, circleSize);
            if(selected) g2d.fillOval(centerX - 6, circleY - 6, 12, 12);
            g2d.setFont(new Font("SansSerif", Font.PLAIN, 14));
            g2d.setColor(selected ? ColorPalette.CHINA_DOLL : ColorPalette.PLUM_WINE);
            g2d.drawString(text, centerX - g2d.getFontMetrics().stringWidth(text)/2, 62);
        }
    }
    
    //tombol submit utama dengan gradient
    class GradientSubmitButton extends JButton {
        public GradientSubmitButton(String text) { 
            super(text); setContentAreaFilled(false); setBorderPainted(false); 
            setFont(new Font("SansSerif", Font.BOLD, 15)); setCursor(new Cursor(Cursor.HAND_CURSOR)); 
            //set warna teks awal biar terang
            setForeground(ColorPalette.CHINA_DOLL);
        }
        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            if(isEnabled()) {
                GradientPaint gp = new GradientPaint(0,0, ColorPalette.PLUM_WINE, getWidth(), 0, ColorPalette.COPPER_ROSE);
                g2d.setPaint(gp);
            } else g2d.setColor(Color.GRAY);
            g2d.fillRoundRect(0,0,getWidth(),getHeight(),12,12);
            super.paintComponent(g);
            if(isEnabled()) {
                //gambar panah & set warna stroke jadi cream
                g2d.setColor(ColorPalette.CHINA_DOLL); 
                g2d.setStroke(new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                int ax = getWidth()-40, ay=getHeight()/2;
                g2d.drawLine(ax,ay,ax+16,ay); g2d.drawLine(ax+16,ay,ax+10,ay-6); g2d.drawLine(ax+16,ay,ax+10,ay+6);
            }
        }
    }
}