import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;

//panel utama untuk halaman login dan daftar akun
public class LoginPanel extends JPanel {

    //referensi ke main game supaya panel bisa akses db dan pindah screen
    private RengasdengklokGame mainApp;

    //komponen input dan tombol ui
    private JTextField usernameField;
    private JPasswordField passwordField;
    private boolean isLoginMode = true; //penanda lagi mode masuk atau daftar
    private JButton loginTab;
    private JButton registerTab;
    private JButton maleButton;
    private JButton femaleButton;
    private String selectedGender = "Male"; //default gender cowo
    private JPanel genderPanel;
    private JButton submitButton;

    //tekstur noise buat efek grain di background
    private BufferedImage noiseTexture;

    //kotak transparan yang berisi form login/daftar
    private FormContainer formContainer;

    //ukuran panel mengikuti ukuran jendela utama
    private static final int WINDOW_WIDTH = 1024;
    private static final int WINDOW_HEIGHT = 768;

    //konstruktor panel login
    public LoginPanel(RengasdengklokGame app) {
        this.mainApp = app;

        //pake null layout biar posisi bisa diatur manual
        setLayout(null);
        setBackground(ColorPalette.DARK_BG_1);
        setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));

        //buat tekstur background dulu lalu pasang komponen ui
        generateNoiseTexture();
        initComponents();
    }

    //fungsi reset dipanggil pas logout untuk bersihkan form
    public void reset() {
        if (usernameField != null) usernameField.setText("");
        if (passwordField != null) passwordField.setText("");
        if (formContainer != null) formContainer.switchToLogin();
    }

    //bikin noise texture buat efek bintik-bintik halus
    private void generateNoiseTexture() {
        noiseTexture = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = noiseTexture.createGraphics();
        java.util.Random rand = new java.util.Random(12345);

        for (int y = 0; y < 100; y++) {
            for (int x = 0; x < 100; x++) {

                int noise = rand.nextInt(80) - 40; //nilai noise random
                int gray = 128 + noise;            //warna abu-abu tengah
                gray = Math.max(0, Math.min(255, gray)); //clamp nilai rgb

                int alpha = 20; //transparansi rendah biar soft
                noiseTexture.setRGB(x, y, new Color(gray, gray, gray, alpha).getRGB());
            }
        }

        g.dispose();
    }

    //gambar background khusus panel login
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(
            RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON
        );

        int w = getWidth();
        int h = getHeight();

        //1.gambar gradasi warna dark bg secara manual dari atas ke bawah
        Color[] gradColors = {
            ColorPalette.DARK_BG_1,
            ColorPalette.DARK_BG_2,
            ColorPalette.DARK_BG_3
        };

        for (int y = 0; y < h; y++) {
            float ratio = (float) y / h;

            //warna transisi antar dark-bg
            Color c1 = (ratio < 0.5f) ? gradColors[0] : gradColors[1];
            Color c2 = (ratio < 0.5f) ? gradColors[1] : gradColors[2];
            float localRatio = (ratio < 0.5f) ? ratio * 2 : (ratio - 0.5f) * 2;

            int r  = (int)(c1.getRed()   + (c2.getRed()   - c1.getRed())   * localRatio);
            int gC = (int)(c1.getGreen() + (c2.getGreen() - c1.getGreen()) * localRatio);
            int b  = (int)(c1.getBlue()  + (c2.getBlue()  - c1.getBlue())  * localRatio);

            g2d.setColor(new Color(r, gC, b));
            g2d.fillRect(0, y, w, 1); //gambar per 1px row biar smooth
        }

        //2.tempelkan noise texture berulang-ulang di seluruh layar
        for (int y = 0; y < h; y += 100) {
            for (int x = 0; x < w; x += 100) {
                g2d.drawImage(noiseTexture, x, y, null);
            }
        }

        //3.efek glow lembut seperti lighting ambience
        g2d.setComposite(
            AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.1f)
        );

        int glowSize = 384;

        RadialGradientPaint glow = new RadialGradientPaint(
            w / 4,
            h / 4,
            glowSize / 2,
            new float[]{0f, 1f},
            new Color[]{
                new Color(168, 106, 101, 255),
                new Color(168, 106, 101,   0)
            }
        );

        g2d.setPaint(glow);
        g2d.fillOval(
            w / 4 - glowSize / 2,
            h / 4 - glowSize / 2,
            glowSize,
            glowSize
        );

        g2d.setComposite(
            AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f)
        );
    }

        //susun semua komponen tampilan login ke layar
    private void initComponents() {

        //judul besar nama game
        JLabel titleLabel = new JLabel("Rengasdengklok", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Georgia", Font.BOLD, 48));
        titleLabel.setForeground(ColorPalette.CHINA_DOLL);
        titleLabel.setBounds(0, 60, WINDOW_WIDTH, 60); //posisi tengah atas
        add(titleLabel);

        //divider kecil dekorasi di bawah judul
        OrnamentalDivider divider = new OrnamentalDivider();
        divider.setBounds((WINDOW_WIDTH - 200) / 2, 130, 200, 4);
        add(divider);

        //subjudul slogan game
        JLabel subtitleLabel = new JLabel("Jangan Salah Culik!", SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("Georgia", Font.ITALIC, 18));
        subtitleLabel.setForeground(ColorPalette.ROSEWATER);
        subtitleLabel.setBounds(0, 145, WINDOW_WIDTH, 25);
        add(subtitleLabel);

        //form login/register di tengah layar
        formContainer = new FormContainer();
        int formWidth = 448;
        int formX = (WINDOW_WIDTH - formWidth) / 2;

        formContainer.setBounds(formX, 185, formWidth, 380);
        add(formContainer);

        //footer dengan teks “Indonesia Merdeka • 1945”
        FooterPanel footer = new FooterPanel();
        footer.setBounds(0, WINDOW_HEIGHT - 70, WINDOW_WIDTH, 30);
        add(footer);
    }

    //logika yang berjalan saat tombol submit diklik
    private void handleSubmit() {

        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        //cek input kosong biar ga error database
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(
                this,
                "Username dan password tidak boleh kosong!",
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        //disable tombol supaya user ga spam klik
        submitButton.setEnabled(false);
        submitButton.setText(isLoginMode ? "Sedang Memproses..." : "Mendaftarkan...");

        //pakai SwingWorker biar proses database ga bikin UI freeze
        new SwingWorker<Boolean, String>() {

            String errorMessage = "Gagal"; //pesan error default
            User loggedUser;               //nyimpan user yang berhasil login

            //kode ini jalan di background thread
            @Override
            protected Boolean doInBackground() throws Exception {

                DatabaseManager db = mainApp.getDbManager();

                if (isLoginMode) {

                    //login normal
                    loggedUser = db.authenticateUser(username, password);
                    return loggedUser != null;

                } else {

                    //validasi register minimal 3 karakter
                    if (username.length() < 3 || password.length() < 3) {
                        errorMessage = "Username/Password minimal 3 karakter!";
                        return false;
                    }

                    //buat akun baru
                    if (db.createUser(username, password)) {

                        //login otomatis supaya dapet userId
                        loggedUser = db.authenticateUser(username, password);

                        if (loggedUser != null) {

                            //simpan data gender ke tabel profile
                            String genderDb = selectedGender.equals("Male")
                                    ? "Laki-laki"
                                    : "Perempuan";

                            db.createProfile(
                                loggedUser.getUserId(),
                                username,
                                genderDb
                            );

                            return true;
                        }

                    } else {
                        //username sudah dipakai
                        errorMessage = "Username sudah digunakan!";
                    }

                    return false;
                }
            }

            //kode ini jalan setelah background selesai dan aman update UI
            @Override
            protected void done() {
                try {
                    boolean success = get();

                    //kembalikan tombol ke kondisi normal
                    submitButton.setEnabled(true);
                    submitButton.setText(isLoginMode ? "Masuk ke Permainan" : "Daftar Akun");

                    if (success) {

                        if (isLoginMode) {

                            //login sukses → masuk ke menu utama
                            mainApp.onLoginSuccess(loggedUser);

                        } else {

                            //register sukses → pindah ke tab login
                            JOptionPane.showMessageDialog(
                                LoginPanel.this,
                                "Akun berhasil dibuat! Silakan Login.",
                                "Sukses",
                                JOptionPane.INFORMATION_MESSAGE
                            );

                            formContainer.switchToLogin();
                            usernameField.setText(username);
                            passwordField.setText("");
                        }

                    } else {

                        //login/register gagal → munculkan pesan error
                        JOptionPane.showMessageDialog(
                            LoginPanel.this,
                            isLoginMode
                                ? "Username atau password salah!"
                                : errorMessage,
                            "Gagal",
                            JOptionPane.ERROR_MESSAGE
                        );
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    submitButton.setEnabled(true); //fallback kalau gagal
                }
            }

        }.execute();
    }

    //garis hiasan kecil yang muncul di bawah judul
    class OrnamentalDivider extends JPanel {
        public OrnamentalDivider() {
            setOpaque(false); //panel transparan karena hanya gambar garis
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth();
            int centerX = w / 2;
            int centerY = 2;

            //gradasi garis kiri
            GradientPaint leftGrad = new GradientPaint(
                    0, centerY,
                    new Color(168, 106, 101, 0),
                    64, centerY,
                    new Color(168, 106, 101, 255)
            );
            g2d.setPaint(leftGrad);
            g2d.drawLine(0, centerY, centerX - 16, centerY);

            //diamond kecil di tengah
            g2d.setColor(ColorPalette.COPPER_ROSE);
            int[] xPoints = {centerX, centerX + 2, centerX, centerX - 2};
            int[] yPoints = {centerY - 2, centerY, centerY + 2, centerY};
            g2d.fillPolygon(xPoints, yPoints, 4);

            //gradasi garis kanan
            GradientPaint rightGrad = new GradientPaint(
                    w - 64, centerY,
                    new Color(168, 106, 101, 255),
                    w, centerY,
                    new Color(168, 106, 101, 0)
            );
            g2d.setPaint(rightGrad);
            g2d.drawLine(centerX + 16, centerY, w, centerY);
        }
    }

    //panel kecil di bagian paling bawah (footer)
    class FooterPanel extends JPanel {
        public FooterPanel() {
            setOpaque(false); //biar ga nutup background
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            int centerX = getWidth() / 2;
            int centerY = getHeight() / 2;

            //garis kiri footer
            GradientPaint leftGrad = new GradientPaint(
                    centerX - 180, centerY,
                    new Color(168, 106, 101, 0),
                    centerX - 80, centerY,
                    new Color(168, 106, 101, 255)
            );
            g2d.setPaint(leftGrad);
            g2d.drawLine(centerX - 180, centerY, centerX - 80, centerY);

            //teks tahun
            g2d.setFont(new Font("Georgia", Font.PLAIN, 12));
            g2d.setColor(ColorPalette.ROSEWATER);
            String text = "Indonesia Merdeka • 1945";
            FontMetrics fm = g2d.getFontMetrics();
            int tw = fm.stringWidth(text);
            g2d.drawString(text, centerX - tw / 2, centerY + 5);

            //garis kanan footer
            GradientPaint rightGrad = new GradientPaint(
                    centerX + 80, centerY,
                    new Color(168, 106, 101, 255),
                    centerX + 180, centerY,
                    new Color(168, 106, 101, 0)
            );
            g2d.setPaint(rightGrad);
            g2d.drawLine(centerX + 80, centerY, centerX + 180, centerY);
        }
    }

    //container utama tempat form login & register ditampilkan
    class FormContainer extends JPanel {
        public FormContainer() {
            setLayout(null);  //pakai posisi bebas
            setOpaque(false); //kotaknya semi-transparan, background digambar manual
            initFormComponents();
        }

        @Override
        protected void paintComponent(Graphics g) {

            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

            int w = getWidth();
            int h = getHeight();

            //background transparan warna rose
            g2d.setColor(new Color(216, 166, 148, 230));
            g2d.fillRoundRect(0, 0, w, h, 16, 16);

            //border halus
            g2d.setColor(new Color(168, 106, 101, 77));
            g2d.setStroke(new BasicStroke(1.5f));
            g2d.drawRoundRect(1, 1, w - 3, h - 3, 16, 16);
        }

                private void initFormComponents() {

            int px = 32;                 //padding kiri form
            int py = 32;                 //padding atas form
            int contentWidth = 384;      //lebar area isi form
            int halfWidth = (contentWidth - 8) / 2; //setengah lebar buat tab

            //tombol tab mode login
            loginTab = new TabButton("Masuk", true);
            loginTab.setBounds(px, py, halfWidth, 48);
            loginTab.addActionListener(e -> switchToLogin());
            add(loginTab);

            //tombol tab mode daftar
            registerTab = new TabButton("Daftar", false);
            registerTab.setBounds(px + halfWidth + 8, py, halfWidth, 48);
            registerTab.addActionListener(e -> switchToRegister());
            add(registerTab);

            int yPos = 104; //jarak awal setelah tab

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

            //panel gender (hanya muncul di mode daftar)
            genderPanel = new JPanel();
            genderPanel.setLayout(null);
            genderPanel.setOpaque(false);
            genderPanel.setBounds(px, yPos, contentWidth, 120);
            genderPanel.setVisible(false); //default disembunyikan

            JLabel genderLabel = new JLabel("Jenis Kelamin");
            genderLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
            genderLabel.setForeground(ColorPalette.PLUM_WINE);
            genderLabel.setBounds(0, 0, contentWidth, 18);
            genderPanel.add(genderLabel);

            int genderBtnWidth = (contentWidth - 16) / 2;

            //tombol gender laki-laki
            maleButton = new GenderButton("Laki-laki", true);
            maleButton.setBounds(0, 28, genderBtnWidth, 76);
            maleButton.addActionListener(e -> selectGender("Male"));
            genderPanel.add(maleButton);

            //tombol gender perempuan
            femaleButton = new GenderButton("Perempuan", false);
            femaleButton.setBounds(genderBtnWidth + 16, 28, genderBtnWidth, 76);
            femaleButton.addActionListener(e -> selectGender("Female"));
            genderPanel.add(femaleButton);

            add(genderPanel);

            //tombol submit utama
            submitButton = new GradientSubmitButton(isLoginMode ? "Masuk ke Permainan" : "Daftar Akun");

            //posisi default tombol login (tidak lompat-lompat)
            submitButton.setBounds(px, 290, contentWidth, 56);
            submitButton.addActionListener(e -> handleSubmit());
            add(submitButton);
        }

                //logika untuk pindah ke mode login
        public void switchToLogin() {
            if (isLoginMode) return; //kalau sudah login mode, ga perlu apa-apa

            isLoginMode = true;

            //update tampilan tab
            ((TabButton) loginTab).setActive(true);
            ((TabButton) registerTab).setActive(false);

            genderPanel.setVisible(false); //gender tidak muncul di mode login

            submitButton.setText("Masuk ke Permainan"); //ubah teks tombol
            submitButton.setBounds(32, 290, 384, 56);   //posisi tombol kembali ke atas

            //mengecilkan kotak form
            int formX = (WINDOW_WIDTH - 448) / 2;
            setBounds(formX, 185, 448, 378);

            repaint(); //refresh tampilan
        }

        //logika untuk pindah ke mode daftar
        private void switchToRegister() {
            if (!isLoginMode) return; //kalau sudah di register mode, skip

            isLoginMode = false;

            //update tampilan tab
            ((TabButton) loginTab).setActive(false);
            ((TabButton) registerTab).setActive(true);

            genderPanel.setVisible(true); //gender muncul di mode daftar

            submitButton.setText("Daftar Akun");  //ubah teks tombol
            submitButton.setBounds(32, 410, 384, 56); //tombol turun kebawah

            //membesarkan kotak form
            int formX = (WINDOW_WIDTH - 448) / 2;
            setBounds(formX, 185, 448, 498);

            repaint(); //refresh tampilan
        }

        //update pilihan gender (male / female)
        private void selectGender(String gender) {
            selectedGender = gender;

            //highlight tombol sesuai gender
            ((GenderButton) maleButton).setSelected(gender.equals("Male"));
            ((GenderButton) femaleButton).setSelected(gender.equals("Female"));
        }

    //tombol tab untuk ganti mode masuk / daftar
    class TabButton extends JButton {
        boolean active; //penanda tab lagi aktif atau tidak

        public TabButton(String text, boolean active) {
            super(text);
            this.active = active;

            setContentAreaFilled(false); //biar background custom
            setBorderPainted(false); //hilangin border default
            setFocusPainted(false); //hilangin outline fokus
            setFont(new Font("SansSerif", Font.BOLD, 14));
            setCursor(new Cursor(Cursor.HAND_CURSOR));

            //warna teks awal sesuai status aktif
            setForeground(active ? ColorPalette.CHINA_DOLL : ColorPalette.PLUM_WINE);
        }

        //ubah status aktif saat tab ditekan
        public void setActive(boolean a) {
            this.active = a;
            //update warna teksnya juga
            setForeground(active ? ColorPalette.CHINA_DOLL : ColorPalette.PLUM_WINE);
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            if (active) {
                //kalau tab aktif: pakai gradient merah plum → copper rose
                GradientPaint gp = new GradientPaint(
                        0, 0,
                        ColorPalette.PLUM_WINE,
                        getWidth(), 0,
                        ColorPalette.COPPER_ROSE
                );
                g2d.setPaint(gp);
            } else {
                //kalau tab tidak aktif: warna pastel transparan
                g2d.setColor(new Color(224, 203, 185, 102));
            }

            //gambar background membulat
            g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);

            //gambar teks
            super.paintComponent(g);
        }
    }

    //textfield custom buat input username yang ada icon user-nya
    class StyledTextField extends JTextField {
        private String placeholder; //teks placeholder
        private boolean isUsername; //penanda apakah field ini untuk username

        public StyledTextField(String placeholder, boolean isUsername) {
            this.placeholder = placeholder;
            this.isUsername = isUsername;

            setFont(new Font("SansSerif", Font.PLAIN, 14));
            setForeground(ColorPalette.PLUM_WINE);      //warna teks input
            setBackground(ColorPalette.CHINA_DOLL);     //warna background pastel
            setOpaque(false);                           //gambar background manual
            setBorder(BorderFactory.createEmptyBorder(12, 52, 12, 16)); //padding
            setCaretColor(ColorPalette.PLUM_WINE);      //warna garis caret
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            //background rounded
            g2d.setColor(getBackground());
            g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);

            //border halus
            g2d.setColor(new Color(139, 90, 94));
            g2d.setStroke(new BasicStroke(1.5f));
            g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 12, 12);

            //gambar icon user di kiri
            g2d.setColor(ColorPalette.COPPER_ROSE);
            g2d.setStroke(new BasicStroke(2f));

            int iconX = 16; 
            int iconY = getHeight() / 2;

            if (isUsername) {
                //gambar kepala
                g2d.drawOval(iconX + 6, iconY - 9, 8, 8);
                //gambar bahu
                g2d.drawArc(iconX + 2, iconY + 1, 16, 14, 0, -180);
            }

            //gambar placeholder kalau belum ada teks & belum fokus
            g2d.setColor(getForeground());
            g2d.setFont(getFont());

            if (getText().isEmpty() && !hasFocus()) {
                g2d.setColor(ColorPalette.DUSTY_ROSE); //warna placeholder
                g2d.drawString(placeholder, 52, getHeight() / 2 + 5);
            } else {
                //kalau sudah ada teks, biarkan swing yang gambar
                super.paintComponent(g);
            }
        }
    }

    //textfield khusus buat input password yang ada icon gembok
    class StyledPasswordField extends JPasswordField {
        private String placeholder; //teks placeholder

        public StyledPasswordField(String placeholder) {
            this.placeholder = placeholder;

            setFont(new Font("SansSerif", Font.PLAIN, 14));
            setForeground(ColorPalette.PLUM_WINE);      //warna teks
            setBackground(ColorPalette.CHINA_DOLL);     //warna pastel
            setOpaque(false);                           //gambar background manual
            setBorder(BorderFactory.createEmptyBorder(12, 52, 12, 16));
            setCaretColor(ColorPalette.PLUM_WINE);
        }

        @Override
        protected void paintComponent(Graphics g) {

            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            //background rounded
            g2d.setColor(getBackground());
            g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);

            //border halus
            g2d.setColor(new Color(139, 90, 94));
            g2d.setStroke(new BasicStroke(1.5f));
            g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 12, 12);

            //gambar icon gembok
            g2d.setColor(ColorPalette.COPPER_ROSE);
            g2d.setStroke(new BasicStroke(2f));

            int iconX = 16;
            int iconY = getHeight() / 2;

            //bagian kotak kunci
            g2d.drawRoundRect(iconX + 4, iconY - 1, 12, 10, 2, 2);
            //bagian lengkungan gembok
            g2d.drawArc(iconX + 7, iconY - 9, 6, 9, 0, 180);

            //placeholder teks
            g2d.setColor(getForeground());
            g2d.setFont(getFont());

            if (getPassword().length == 0 && !hasFocus()) {
                g2d.setColor(ColorPalette.DUSTY_ROSE); 
                g2d.drawString(placeholder, 52, getHeight() / 2 + 5);
            } else {
                super.paintComponent(g);
            }
        }
    }

    //tombol custom untuk memilih gender laki-laki / perempuan
    class GenderButton extends JButton {
        boolean selected;   //status tombol apakah lagi dipilih
        String text;        //teks yang ditampilkan

        public GenderButton(String text, boolean selected) {
            this.text = text;
            this.selected = selected;

            setContentAreaFilled(false); //biar background custom
            setBorderPainted(false);     //hilangin border default
            setCursor(new Cursor(Cursor.HAND_CURSOR));
        }

        //update status terpilih
        public void setSelected(boolean s) {
            this.selected = s;
            repaint(); //refresh tampilan
        }

        @Override
        protected void paintComponent(Graphics g) {

            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            if (selected) {
                //kalau tombol dipilih: gradient plum → copper rose
                GradientPaint gp = new GradientPaint(
                        0, 0,
                        ColorPalette.PLUM_WINE,
                        getWidth(), getHeight(),
                        ColorPalette.COPPER_ROSE
                );
                g2d.setPaint(gp);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
            } else {
                //kalau tidak dipilih: warna pastel transparan
                g2d.setColor(new Color(224, 203, 185, 102));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
            }

            //border luar tombol
            g2d.setColor(selected ? ColorPalette.PLUM_WINE : ColorPalette.COPPER_ROSE);
            g2d.setStroke(new BasicStroke(2));
            g2d.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, 12, 12);

            //gambar icon lingkaran gender di tengah atas
            int centerX = getWidth() / 2;
            int circleY = 24;
            int circleSize = 32;

            g2d.setColor(selected ? ColorPalette.CHINA_DOLL : ColorPalette.COPPER_ROSE);
            g2d.drawOval(centerX - circleSize / 2, circleY - circleSize / 2, circleSize, circleSize);

            //isi titik kecil kalau sedang dipilih
            if (selected) {
                g2d.fillOval(centerX - 6, circleY - 6, 12, 12);
            }

            //tulis teks label gender di bawah icon
            g2d.setFont(new Font("SansSerif", Font.PLAIN, 14));
            g2d.setColor(selected ? ColorPalette.CHINA_DOLL : ColorPalette.PLUM_WINE);

            int textWidth = g2d.getFontMetrics().stringWidth(text);
            g2d.drawString(text, centerX - textWidth / 2, 62);
        }
    }

        //tombol submit utama (dipakai untuk tombol Masuk / Daftar)
    class GradientSubmitButton extends JButton {

        public GradientSubmitButton(String text) {
            super(text);

            setContentAreaFilled(false);  //background digambar manual
            setBorderPainted(false);      //hapus border default
            setFont(new Font("SansSerif", Font.BOLD, 15));
            setCursor(new Cursor(Cursor.HAND_CURSOR));

            //warna teks cream biar kontras dengan gradient
            setForeground(ColorPalette.CHINA_DOLL);
        }

        @Override
        protected void paintComponent(Graphics g) {

            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            if (isEnabled()) {
                //gradient plum → copper rose (aktif)
                GradientPaint gp = new GradientPaint(
                        0, 0,
                        ColorPalette.PLUM_WINE,
                        getWidth(), 0,
                        ColorPalette.COPPER_ROSE
                );
                g2d.setPaint(gp);
            } else {
                //kalau tombol disabled: abu-abu
                g2d.setColor(Color.GRAY);
            }

            //background rounded
            g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);

            //gambar teks
            super.paintComponent(g);

            if (isEnabled()) {
                //gambar icon panah kecil di kanan tombol
                g2d.setColor(ColorPalette.CHINA_DOLL);
                g2d.setStroke(new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

                int ax = getWidth() - 40;
                int ay = getHeight() / 2;

                //garis horizontal
                g2d.drawLine(ax, ay, ax + 16, ay);
                //kepala panah
                g2d.drawLine(ax + 16, ay, ax + 10, ay - 6);
                g2d.drawLine(ax + 16, ay, ax + 10, ay + 6);
            }
        }
    }
    
}}
    
