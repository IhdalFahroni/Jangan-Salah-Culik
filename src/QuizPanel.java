import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

public class QuizPanel extends JPanel {

    private RengasdengklokGame mainApp;
    private QuizManager quizManager;
    private AudioManager audioManager;

    // Thread dan timer
    private Thread countdownThread;
    private Thread quizTimerThread;
    private final Object threadLock = new Object();
    private int timeLeft = 180;
    private int countdown = 3;
    private volatile boolean countDownThreadRunning = false;
    private volatile boolean quizTimerRunning = false;
    private long startTime;
    
    // Color palette nya tak samain sm yang di main menu panel
    private final Color DARK_BG_1 = new Color(0x30, 0x2A, 0x28);
    private final Color DARK_BG_3 = new Color(0x42, 0x38, 0x36);
    private final Color CHINA_DOLL = new Color(0xE0, 0xCB, 0xB9);
    private final Color ROSEWATER = new Color(0xF2, 0xD5, 0xC9);
    private final Color ACCENT_RED = new Color(0xA8, 0x6A, 0x65);
    
    private static final int WINDOW_WIDTH = 1024;
    private static final int WINDOW_HEIGHT = 768;
    private static final int TOTAL_TIME = 180;
    
    private BufferedImage noiseTexture;
    
    // Komponen UI
    private JPanel quizContentPanel, resultPanel, countdownPanel;
    private JLabel countdownLabel, countdownNumber, questionNumberLabel, questionTextLabel;
    private JRadioButton optionA, optionB, optionC, optionD;
    private ButtonGroup optionsGroup;
    private JButton prevButton, nextButton, submitButton, backButton;
    private JLabel resultScore, resultMessage, calculatedScoreLabel;
    private JButton menuButton, retryButton;
    private JPanel timerBar;
    
    public QuizPanel(RengasdengklokGame mainApp) {
        this.mainApp = mainApp;
        this.quizManager = new QuizManager(mainApp.getDbManager());
        setLayout(null);
        setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
        generateNoiseTexture();
        initializeUI();
    }

    // dipanggil waktu panel ini ditampilkan
    public void onPanelShown() {
        resetQuiz();
    }
    
    private void generateNoiseTexture() {
        noiseTexture = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = noiseTexture.createGraphics();
        java.util.Random rand = new java.util.Random(12345);
        
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
        
        // Gradient background ky yang MainMenuPanel
        GradientPaint gradient = new GradientPaint(
            0, 0, DARK_BG_1,
            0, h, DARK_BG_3
        );
        g2d.setPaint(gradient);
        g2d.fillRect(0, 0, w, h);
        
        // buat noise texture
        for (int y = 0; y < h; y += 100) {
            for (int x = 0; x < w; x += 100) {
                g2d.drawImage(noiseTexture, x, y, null);
            }
        }
        
        // buat efek glow radial merah
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.1f));
        RadialGradientPaint glow = new RadialGradientPaint(
            w / 2, h / 2,
            w / 2,
            new float[]{0f, 1f},
            new Color[]{new Color(168, 106, 101, 255), new Color(168, 106, 101, 0)}
        );
        g2d.setPaint(glow);
        g2d.fillOval(0, 0, w, h);
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
    }

    private void initializeUI() {
        // Tombol Kembali pake yang dibuat alyeah
        backButton = new StoryPanel.MenuButton();
        backButton.setBounds(32, 32, 80, 45);
        backButton.addActionListener(e -> {
            stopAllThreads();
            mainApp.showPanel("MAIN_MENU");
        });
        add(backButton);
        
        // Panel utama buat konten quiz
        quizContentPanel = new JPanel(null);
        quizContentPanel.setBounds((WINDOW_WIDTH - 900) / 2, 100, 900, 600);
        quizContentPanel.setOpaque(false);
        quizContentPanel.setVisible(false);
        
        // Judul Kuis 
        JLabel titleLabel = new JLabel("Kuis Uji Pemahaman", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Georgia", Font.BOLD, 36));
        titleLabel.setForeground(CHINA_DOLL);
        titleLabel.setBounds(0, 0, 900, 40);
        quizContentPanel.add(titleLabel);
        
        // Subtitle judul
        JLabel subtitleLabel = new JLabel("Uji pengetahuan sejarah Rengasdengklok Anda", SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("Georgia", Font.ITALIC, 14));
        subtitleLabel.setForeground(ROSEWATER);
        subtitleLabel.setBounds(0, 42, 900, 22);
        quizContentPanel.add(subtitleLabel);

        
        // Panel untuk nomor soal
        JPanel questionNumberPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Background pill transparan
                g2d.setColor(new Color(224, 203, 185, 40));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 44, 44);
                
                // Border pill
                g2d.setColor(new Color(224, 203, 185, 100));
                g2d.setStroke(new BasicStroke(1.5f));
                g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 44, 44);
                
                super.paintComponent(g);
            }
        };
        questionNumberPanel.setBounds((900 - 200) / 2, 75, 200, 44);
        questionNumberPanel.setLayout(new BorderLayout());
        questionNumberPanel.setOpaque(false);
        
        questionNumberLabel = new JLabel("Pertanyaan 1 dari 15", SwingConstants.CENTER);
        questionNumberLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        questionNumberLabel.setForeground(CHINA_DOLL);
        questionNumberPanel.add(questionNumberLabel, BorderLayout.CENTER);
        quizContentPanel.add(questionNumberPanel);
        
        // Horizontal timer bar di bawah nomor soal, dari ujung k ujung panel soal 
        timerBar = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int w = getWidth();
                int h = getHeight();

                // Track
                Color track = new Color(0x3A, 0x34, 0x32);
                g2d.setColor(track);
                g2d.fillRoundRect(0, 0, w, h, h, h);

                // ngisi sesuai waktu
                double frac = Math.max(0.0, Math.min(1.0, (double) timeLeft / TOTAL_TIME));
                int fillW = (int) Math.round(frac * w);
                GradientPaint gp = new GradientPaint(0, 0, ACCENT_RED, Math.max(1, w), 0, new Color(0x75, 0x4B, 0x4D));
                g2d.setPaint(gp);
                if (fillW > 0) {
                    g2d.fillRoundRect(0, 0, fillW, h, h, h);
                }
            }
        };
        timerBar.setOpaque(false);
        timerBar.setBounds(0, 125, 900, 10);
        quizContentPanel.add(timerBar);
        
        // panel buat si soal dan opsi
        JPanel contentPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // background translucent cream 
                g2d.setColor(new Color(224, 203, 185, 40));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);

                // border cream jg
                g2d.setColor(new Color(224, 203, 185, 100));
                g2d.setStroke(new BasicStroke(2.5f));
                g2d.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, 16, 16);

                super.paintComponent(g);
            }
        };
        contentPanel.setBounds(0, 140, 900, 380);
        contentPanel.setLayout(null);
        contentPanel.setOpaque(false);
        
        // teks pertanyaan 
        questionTextLabel = new JLabel("", JLabel.LEFT);
        questionTextLabel.setBounds(40, 15, 820, 60);
        questionTextLabel.setFont(new Font("Georgia", Font.PLAIN, 20));
        //warna soal krim
        questionTextLabel.setForeground(CHINA_DOLL);
        questionTextLabel.setVerticalAlignment(SwingConstants.TOP);
        contentPanel.add(questionTextLabel);
        
        // Panel untuk opsi jawaban 
        JPanel optionsContainer = new JPanel(new GridLayout(4, 1, 12, 18));
        optionsContainer.setBounds(40, 85, 820, 280);
        optionsContainer.setOpaque(false);
        
        optionA = createOptionButton("A.");
        optionB = createOptionButton("B.");
        optionC = createOptionButton("C.");
        optionD = createOptionButton("D.");
        
        optionsGroup = new ButtonGroup();
        optionsGroup.add(optionA);
        optionsGroup.add(optionB);
        optionsGroup.add(optionC);
        optionsGroup.add(optionD);
        
        optionsContainer.add(optionA);
        optionsContainer.add(optionB);
        optionsContainer.add(optionC);
        optionsContainer.add(optionD);
        
        contentPanel.add(optionsContainer);
        quizContentPanel.add(contentPanel);
        
        // Panel buat navigasi lanjut, balik, selesai
        JPanel navPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        navPanel.setBounds(0, 540, 900, 50);
        navPanel.setOpaque(false);
        
        prevButton = createStyledButton("Sebelumnya");
        prevButton.setEnabled(false);
        prevButton.addActionListener(e -> moveToPreviousQuestion());
        
        nextButton = createStyledButton("Selanjutnya");
        nextButton.addActionListener(e -> moveToNextQuestion());
        
        submitButton = createStyledButton("Selesai Quiz");
        submitButton.addActionListener(e -> confirmFinishQuiz());
        
        navPanel.add(prevButton);
        navPanel.add(submitButton);
        navPanel.add(nextButton);
        
        quizContentPanel.add(navPanel);
        add(quizContentPanel);
        
        // Panel untuk hasil
        createResultPanel();
        
        // Countdown overlay
        createCountdownOverlay();
    }
    
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                int w = getWidth();
                int h = getHeight();
                
                // Background gradient
                GradientPaint gp;
                if (getModel().isPressed()) {
                    gp = new GradientPaint(0, 0, new Color(0x7B, 0x35, 0x0E), 0, h, new Color(0x98, 0x5A, 0x55));
                } else if (getModel().isRollover()) {
                    gp = new GradientPaint(0, 0, new Color(0x9B, 0x55, 0x2E), 0, h, new Color(0xB8, 0x7A, 0x75));
                } else {
                    gp = new GradientPaint(0, 0, new Color(0x8B, 0x45, 0x1E), 0, h, new Color(0xA8, 0x6A, 0x65));
                }
                g2d.setPaint(gp);
                g2d.fillRoundRect(0, 0, w, h, 25, 25);
                
                // Border
                g2d.setColor(new Color(0xE0, 0xCB, 0xB9, 150));
                g2d.setStroke(new BasicStroke(2f));
                g2d.drawRoundRect(1, 1, w - 3, h - 3, 25, 25);
                
                super.paintComponent(g);
            }
        };
        
        button.setFont(new Font("Georgia", Font.BOLD, 14));
        button.setForeground(CHINA_DOLL);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(150, 45));
        
        return button;
    }
    
    private JRadioButton createOptionButton(String prefix) {
        JRadioButton button = new JRadioButton(prefix) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int w = getWidth();
                int h = getHeight();

                // Shadow
                g2d.setColor(new Color(0x00, 0x00, 0x00, 30));
                g2d.fillRoundRect(3, 3, Math.max(0, w-6), Math.max(0, h-6), 12, 12);

                // Background
                if (isSelected()) {
                    GradientPaint sel = new GradientPaint(0, 0, ACCENT_RED, 0, h, new Color(0x75, 0x4B, 0x4D));
                    g2d.setPaint(sel);
                } else {
                    g2d.setColor(new Color(0x3A, 0x34, 0x32));
                }
                g2d.fillRoundRect(2, 2, Math.max(0, w-5), Math.max(0, h-5), 12, 12);

                // Border
                g2d.setColor(new Color(0x5D, 0x30, 0x3C, 120));
                g2d.setStroke(new BasicStroke(1.5f));
                g2d.drawRoundRect(2, 2, Math.max(0, w - 5), Math.max(0, h - 5), 12, 12);

                // label teks
                g2d.setFont(getFont());
                g2d.setColor(CHINA_DOLL);
                FontMetrics fm = g2d.getFontMetrics();
                String text = getText();
                if (text != null) {
                    int textX = 28;
                    int textY = (h - fm.getHeight()) / 2 + fm.getAscent();
                    g2d.drawString(text, textX, textY);
                }
            }
        };

        button.setFont(new Font("Georgia", Font.BOLD, 20)); 
        button.setForeground(CHINA_DOLL);
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setBorder(BorderFactory.createEmptyBorder(18, 24, 18, 24)); 
        // ini buat nyembunyiin button radio yg bulet putih jelek itu
        button.setIcon(null);
        button.setSelectedIcon(null);

        // Hover 
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (!button.isSelected()) {
                    button.setCursor(new Cursor(Cursor.HAND_CURSOR));
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (!button.isSelected()) {
                    button.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                }
            }
        });

        button.addActionListener(e -> {
            if (quizManager.getCurrentQuestion() != null) {
                String answer = prefix.substring(0, 1);
                quizManager.submitAnswer(answer);
            }
        });

        return button;
    }

    private void confirmFinishQuiz() {
        int result = JOptionPane.showConfirmDialog(
            this,
            "Apakah Anda yakin ingin menyelesaikan quiz?",
            "Konfirmasi Selesai Quiz",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );
        
        if (result == JOptionPane.YES_OPTION) {
            finishQuiz();
        }
    }
    
    private void createResultPanel() {
        resultPanel = new JPanel(null);
        resultPanel.setBounds((WINDOW_WIDTH - 800) / 2, 150, 800, 450);
        resultPanel.setOpaque(false);
        resultPanel.setVisible(false);
        
        // Panel konten hasil
        JPanel resultContent = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Background cream dengan corner rounded
                g2d.setColor(new Color(0xF5, 0xED, 0xE0));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                
                // Border coklat tebal
                g2d.setColor(new Color(0xA8, 0x6A, 0x65));
                g2d.setStroke(new BasicStroke(4f));
                g2d.drawRoundRect(2, 2, getWidth() - 5, getHeight() - 5, 20, 20);
                
                super.paintComponent(g);
            }
        };
        resultContent.setBounds(0, 0, 800, 450);
        resultContent.setLayout(null);
        resultContent.setOpaque(false);
        
        // Judul hasil
        JLabel resultTitle = new JLabel("Kuis Selesai!", SwingConstants.CENTER);
        resultTitle.setFont(new Font("Georgia", Font.BOLD, 40));
        resultTitle.setForeground(new Color(0x8B, 0x45, 0x1E));
        resultTitle.setBounds(0, 30, 800, 50);
        resultContent.add(resultTitle);
        
        // Label "SKOR ANDA" di atas panel skor
        JLabel scoreTitleLabel = new JLabel("SKOR ANDA", SwingConstants.CENTER);
        scoreTitleLabel.setFont(new Font("Georgia", Font.BOLD, 18));
        scoreTitleLabel.setForeground(new Color(0xA8, 0x6A, 0x65));
        scoreTitleLabel.setBounds(100, 90, 600, 30);
        resultContent.add(scoreTitleLabel);
        
        // Panel skor (ukuran 600x140, diposisikan tengah)
        JPanel scorePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Gradient merah untuk skor
                GradientPaint gp = new GradientPaint(
                    0, 0, ACCENT_RED,
                    0, getHeight(), new Color(0x75, 0x4B, 0x4D)
                );
                g2d.setPaint(gp);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                
                // Border
                g2d.setColor(new Color(0xE0, 0xCB, 0xB9, 150));
                g2d.setStroke(new BasicStroke(3f));
                g2d.drawRoundRect(2, 2, getWidth() - 5, getHeight() - 5, 20, 20);
                
                super.paintComponent(g);
            }
        };
        scorePanel.setBounds(100, 120, 600, 140); // Diposisikan tengah (800-600)/2 = 100
        scorePanel.setLayout(new BorderLayout());
        scorePanel.setOpaque(false);
        
        // Label untuk skor dengan padding atas
        JPanel scoreInnerPanel = new JPanel(new BorderLayout());
        scoreInnerPanel.setOpaque(false);
        scoreInnerPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0)); // Padding atas 20px
        
        resultScore = new JLabel("", SwingConstants.CENTER);
        resultScore.setFont(new Font("Georgia", Font.BOLD, 56));
        resultScore.setForeground(CHINA_DOLL);
        scoreInnerPanel.add(resultScore, BorderLayout.CENTER);
        
        // Label tambahan untuk format skor
        JLabel scoreDetailLabel = new JLabel("", SwingConstants.CENTER);
        scoreDetailLabel.setFont(new Font("Georgia", Font.PLAIN, 18));
        scoreDetailLabel.setForeground(new Color(0xE0, 0xCB, 0xB9, 200));
        scoreDetailLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 10, 0)); // Padding atas 5px, bawah 10px
        scoreInnerPanel.add(scoreDetailLabel, BorderLayout.SOUTH);
        
        scorePanel.add(scoreInnerPanel, BorderLayout.CENTER);
        resultContent.add(scorePanel);
        
        // Pesan hasil
        resultMessage = new JLabel("", SwingConstants.CENTER);
        resultMessage.setFont(new Font("Georgia", Font.ITALIC, 24));
        resultMessage.setForeground(new Color(0x5D, 0x30, 0x3C));
        resultMessage.setBounds(0, 280, 800, 40);
        resultContent.add(resultMessage);
        
        // Panel tombol 
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setBounds(100, 340, 600, 90); // Sama-sama 600px lebar, diposisikan tengah
        buttonPanel.setOpaque(false);
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        retryButton = createStyledButton("Coba Lagi");
        retryButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        retryButton.setMaximumSize(new Dimension(600, 50));
        retryButton.addActionListener(e -> resetQuiz());
        
        menuButton = createStyledButton("Menu Utama");
        menuButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        menuButton.setMaximumSize(new Dimension(600, 50));
        menuButton.addActionListener(e -> {
            stopAllThreads();
            mainApp.showPanel("MAIN_MENU");
        });
        
        // Tambahkan spacing antara tombol
        buttonPanel.add(retryButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 15))); // Spacer
        buttonPanel.add(menuButton);
        
        resultContent.add(buttonPanel);
        resultPanel.add(resultContent);
        add(resultPanel);
    }

    //COUNTDOWN OVERLAY

    private void createCountdownOverlay() {
        countdownPanel = new JPanel(new GridBagLayout());
        countdownPanel.setBounds(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
        countdownPanel.setOpaque(false);
        countdownPanel.setVisible(false);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        
        // Label "Bersiap..." 
        countdownLabel = new JLabel("Bersiap...", JLabel.CENTER);
        countdownLabel.setFont(new Font("Georgia", Font.BOLD, 48));
        countdownLabel.setForeground(CHINA_DOLL);
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 0, 0); 
        countdownPanel.add(countdownLabel, gbc);

        // Label angka countdown 
        countdownNumber = new JLabel("3", JLabel.CENTER);
        countdownNumber.setFont(new Font("Georgia", Font.BOLD, 120));
        countdownNumber.setForeground(ACCENT_RED);
        gbc.gridy = 1;
        gbc.insets = new Insets(-12, 0, 0, 0); 
        countdownPanel.add(countdownNumber, gbc);
        
        add(countdownPanel);
    }

    //LOGIKA QUIZ

    private void resetQuiz() {
        stopAllThreads();

        timeLeft = 180;
        countdown = 3;
        
        quizContentPanel.setVisible(false);
        resultPanel.setVisible(false);
        
        // Tampilkan countdown
        countdownPanel.setVisible(true);
        countdownLabel.setText("Bersiap...");
        countdownNumber.setText("3");
        
        startCountdown();
    }

    private void startCountdown() {
        countDownThreadRunning = true;
        countdownThread = new Thread(() -> {
            try {
                while (countdown > 0 && countDownThreadRunning) {
                    final int currentCount = countdown;
                    SwingUtilities.invokeLater(() -> {
                        countdownNumber.setText(String.valueOf(currentCount));
                        if (currentCount == 1) {
                            countdownLabel.setText("Mulai!");
                        }
                    });
                    Thread.sleep(1000);
                    countdown--;
                }
                if (countDownThreadRunning) {
                    SwingUtilities.invokeLater(() -> {
                        countdownPanel.setVisible(false);
                        startQuiz();
                    });
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        countdownThread.start();
    }

    private void startQuiz() {
        quizContentPanel.setVisible(true);
        quizManager.startQuiz(15);
        startTime = System.currentTimeMillis();

        startQuizTimer();
        updateQuestionDisplay();
    }

    private void startQuizTimer() {
        quizTimerRunning = true;
        quizTimerThread = new Thread(() -> {
            try {
                while (quizTimerRunning && timeLeft > 0) {
                    Thread.sleep(1000);
                    timeLeft--;
                    SwingUtilities.invokeLater(() -> {
                        if (timerBar != null) timerBar.repaint();
                    });
                }

                if (quizTimerRunning && timeLeft <= 0) {
                    SwingUtilities.invokeLater(this::finishQuiz);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        quizTimerThread.start();
    }

    private void updateQuestionDisplay() {
        if (quizManager.getCurrentQuestion() != null) {
            Question currentQuestion = quizManager.getCurrentQuestion();
            questionTextLabel.setText("<html><div style='text-align: left; width: 820px;'>" + 
                                     currentQuestion.getQuestionText() + "</div></html>");
            
            // opsi jawaban
            optionA.setText("A. " + currentQuestion.getOptionA());
            optionB.setText("B. " + currentQuestion.getOptionB());
            optionC.setText("C. " + (currentQuestion.getOptionC() != null ? currentQuestion.getOptionC() : ""));
            optionD.setText("D. " + (currentQuestion.getOptionD() != null ? currentQuestion.getOptionD() : ""));

            optionC.setVisible(currentQuestion.getOptionC() != null);
            optionD.setVisible(currentQuestion.getOptionD() != null);
            
            // label nomor soal
            questionNumberLabel.setText("Pertanyaan " + quizManager.getCurrentQuestionNumber() + " dari " + quizManager.getTotalQuestions());
            
            // navigation buttons
            prevButton.setEnabled(quizManager.getCurrentQuestionIndex() > 0);
            nextButton.setEnabled(quizManager.getCurrentQuestionIndex() < quizManager.getTotalQuestions() - 1);

            optionsGroup.clearSelection();
            String userAnswer = quizManager.getUserAnswerForCurrentQuestion();
            if (!userAnswer.isEmpty()) {
                switch (userAnswer) {
                    case "A": optionA.setSelected(true); break;
                    case "B": optionB.setSelected(true); break;
                    case "C": optionC.setSelected(true); break;
                    case "D": optionD.setSelected(true); break;
                }
            }
        }
    }

    private void moveToNextQuestion() {
        if (quizManager.getCurrentQuestionIndex() < quizManager.getTotalQuestions() - 1) {
            quizManager.moveToNextQuestion();
            updateQuestionDisplay();
        }
    }

    private void moveToPreviousQuestion() {
        if (quizManager.getCurrentQuestionIndex() > 0) {
            quizManager.moveToPreviousQuestion();
            updateQuestionDisplay();
        }
    }

    private void finishQuiz() {
        stopAllThreads();
        
        quizContentPanel.setVisible(false);
        resultPanel.setVisible(true);

        int score = quizManager.calculateScore();
        int totalQuestions = quizManager.getTotalQuestions();
        
        resultScore.setText(String.valueOf(quizManager.calculateFinalScore()));
        
        // Pesan sesuai skor
        double percentage = (score * 100.0) / totalQuestions;
        if (percentage == 100) {
            resultMessage.setText("Selamat! Anda pahaman sejarah sejati!");
        } else if (percentage >= 70) {
            resultMessage.setText("Bagus! Pengetahuan Anda cukup baik!");
        } else if (percentage >= 50) {
            resultMessage.setText("Cukup baik! Tingkatkan lagi pemahaman sejarah Anda!");
        } else {
            resultMessage.setText("Yuk belajar lagi sejarah Rengasdengklok!");
        }
        
        // Save d database
        boolean saved = quizManager.saveQuizResults(
            mainApp.getCurrentUserId()
        );

        if (!saved) {
            System.err.println("Failed to save quiz results to database.");
        }
    }

    //THREAD CONTROL 

    public void stopAllThreads() {
        synchronized (threadLock) {
            countDownThreadRunning = false;
            quizTimerRunning = false;

            if (countdownThread != null && countdownThread.isAlive()) {
                countdownThread.interrupt();
            }
            if (quizTimerThread != null && quizTimerThread.isAlive()) {
                quizTimerThread.interrupt();
            }
        }
    }

    @Override
    public void removeNotify() {
        stopAllThreads();
        super.removeNotify();
    }
}