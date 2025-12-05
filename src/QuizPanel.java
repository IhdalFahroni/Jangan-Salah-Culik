import javax.swing.*;
import java.awt.*;

public class QuizPanel extends JPanel {

    private RengasdengklokGame mainApp;
    private QuizManager quizManager;

    // Thread dan timer
    private Thread countdownThread;
    private Thread quizTimerThread;
    private final Object threadLock = new Object();
    private int timeLeft = 180; // 3 menit untuk quiz
    private int countdown = 3;
    private volatile boolean countDownThreadRunning = false;
    private volatile boolean quizTimerRunning = false;
    private long startTime;

    // Warna pakai ColorPalette + sedikit turunan biar match UI lain
    private final Color BG_COLOR          = ColorPalette.DARK_BG_1;
    private final Color CARD_BACKGROUND   = new Color(40, 25, 25, 235);   // panel isi
    private final Color CARD_BACKGROUND_2 = new Color(50, 32, 32, 235);   // panel opsi
    private final Color BORDER_COLOR      = new Color(168, 106, 101, 200);
    private final Color TEXT_MAIN         = ColorPalette.CHINA_DOLL;
    private final Color TEXT_SOFT         = new Color(220, 210, 200);
    private final Color TEXT_MUTED        = new Color(190, 170, 165);
    private final Color BUTTON_BG         = ColorPalette.PLUM_WINE;
    private final Color BUTTON_BG_HOVER   = ColorPalette.COPPER_ROSE;
    private final Color BUTTON_TEXT       = ColorPalette.CHINA_DOLL;
    private final Color SUCCESS_BG        = new Color(110, 145, 110);

    // Font
    private final Font TITLE_FONT             = new Font("Georgia", Font.BOLD, 26);
    private final Font INFO_FONT              = new Font("SansSerif", Font.BOLD, 14);
    private final Font QUESTION_FONT          = new Font("SansSerif", Font.PLAIN, 16);
    private final Font OPTION_FONT            = new Font("SansSerif", Font.PLAIN, 14);
    private final Font BUTTON_FONT            = new Font("SansSerif", Font.BOLD, 14);
    private final Font COUNTDOWN_TEXT_FONT    = new Font("Georgia", Font.BOLD, 28);
    private final Font COUNTDOWN_NUMBER_FONT  = new Font("Georgia", Font.BOLD, 120);
    private final Font RESULT_FONT            = new Font("Georgia", Font.BOLD, 42);

    // Komponen UI
    private JLabel countdownLabel, countdownNumber, timerLabel, questionNumberLabel, userLabel;
    private JPanel questionPanel, optionsPanel, resultPanel, navigationPanel;
    private JTextArea questionTextArea;
    private JRadioButton optionA, optionB, optionC, optionD;
    private ButtonGroup optionsGroup;
    private JButton backButton, nextButton, prevButton, submitButton;
    private JLabel resultScore, resultMessage;
    private JButton menuButton, retryButton;

    // Ukuran panel konsisten dengan layar lain
    private static final int PANEL_WIDTH  = 1024;
    private static final int PANEL_HEIGHT = 768;

    public QuizPanel(RengasdengklokGame mainApp) {
        this.mainApp = mainApp;
        this.quizManager = new QuizManager(mainApp.getDbManager());
        initializeUI();
    }

    // dipanggil waktu panel ini ditampilkan
    public void onPanelShown() {
        resetQuiz();
    }

    private void initializeUI() {
        setLayout(null);
        setBackground(BG_COLOR);
        setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));

        createInfoPanel();
        createQuestionPanel();
        createOptionsPanel();
        createNavigationPanel();
        createCountdownOverlay();
        createResultPanel();
    }

    //PANEL ATAS (INFO BAR)

    private void createInfoPanel() {
        int margin = 60;
        int width = PANEL_WIDTH - margin * 2;

        JPanel infoPanel = new JPanel();
        infoPanel.setBounds(margin, 40, width, 56);
        infoPanel.setLayout(new GridLayout(1, 4, 12, 0));
        infoPanel.setBackground(new Color(30, 20, 20, 220));
        infoPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));

        backButton = createBackButton();
        infoPanel.add(backButton);

        timerLabel = new JLabel("⏱ Waktu: 180s", JLabel.CENTER);
        timerLabel.setFont(INFO_FONT);
        timerLabel.setForeground(TEXT_MAIN);
        infoPanel.add(timerLabel);

        questionNumberLabel = new JLabel("📝 Soal: 0/15", JLabel.CENTER);
        questionNumberLabel.setFont(INFO_FONT);
        questionNumberLabel.setForeground(TEXT_MAIN);
        infoPanel.add(questionNumberLabel);

        userLabel = new JLabel("👤 " + mainApp.getCurrentUsername(), JLabel.CENTER);
        userLabel.setFont(INFO_FONT);
        userLabel.setForeground(TEXT_MUTED);
        infoPanel.add(userLabel);

        add(infoPanel);
    }

    //PANEL SOAL

    private void createQuestionPanel() {
        int margin = 60;
        int width = PANEL_WIDTH - margin * 2;

        questionPanel = new JPanel();
        questionPanel.setBounds(margin, 120, width, 150);
        questionPanel.setBackground(CARD_BACKGROUND);
        questionPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 2),
                BorderFactory.createEmptyBorder(6, 6, 6, 6)
        ));
        questionPanel.setLayout(new BorderLayout());

        questionTextArea = new JTextArea();
        questionTextArea.setFont(QUESTION_FONT);
        questionTextArea.setForeground(TEXT_SOFT);
        questionTextArea.setBackground(CARD_BACKGROUND);
        questionTextArea.setLineWrap(true);
        questionTextArea.setWrapStyleWord(true);
        questionTextArea.setEditable(false);
        questionTextArea.setBorder(BorderFactory.createEmptyBorder(15, 18, 15, 18));

        JScrollPane scrollPane = new JScrollPane(questionTextArea);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(CARD_BACKGROUND);

        questionPanel.add(scrollPane, BorderLayout.CENTER);
        add(questionPanel);
    }

    //PANEL OPSI JAWABAN

    private void createOptionsPanel() {
        int margin = 60;
        int width = PANEL_WIDTH - margin * 2;

        optionsPanel = new JPanel();
        optionsPanel.setBounds(margin, 290, width, 220);
        optionsPanel.setBackground(CARD_BACKGROUND_2);
        optionsPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 2),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        optionsPanel.setLayout(new GridLayout(4, 1, 10, 10));

        optionA = createOptionRadioButton("A.");
        optionB = createOptionRadioButton("B.");
        optionC = createOptionRadioButton("C.");
        optionD = createOptionRadioButton("D.");

        optionsGroup = new ButtonGroup();
        optionsGroup.add(optionA);
        optionsGroup.add(optionB);
        optionsGroup.add(optionC);
        optionsGroup.add(optionD);

        optionsPanel.add(optionA);
        optionsPanel.add(optionB);
        optionsPanel.add(optionC);
        optionsPanel.add(optionD);

        add(optionsPanel);
    }

    private JRadioButton createOptionRadioButton(String label) {
        JRadioButton radioButton = new JRadioButton(label);
        radioButton.setFont(OPTION_FONT);
        radioButton.setForeground(TEXT_SOFT);
        radioButton.setBackground(CARD_BACKGROUND_2);
        radioButton.setFocusPainted(false);
        radioButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        radioButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(120, 90, 85), 1),
                BorderFactory.createEmptyBorder(8, 15, 8, 15)
        ));

        radioButton.addActionListener(e -> {
            if (quizManager.getCurrentQuestion() != null) {
                String answer = label.substring(0, 1);
                quizManager.submitAnswer(answer);
            }
        });

        radioButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                radioButton.setBackground(new Color(60, 40, 40, 235));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                radioButton.setBackground(CARD_BACKGROUND_2);
            }
        });

        return radioButton;
    }

    // NAVIGASI BAWAH

    private void createNavigationPanel() {
        int margin = 60;
        int width = PANEL_WIDTH - margin * 2;

        navigationPanel = new JPanel();
        navigationPanel.setBounds(margin, 530, width, 60);
        navigationPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 5));
        navigationPanel.setOpaque(false);

        prevButton = createNavButton("← Sebelumnya");
        prevButton.setEnabled(false);
        prevButton.addActionListener(e -> moveToPreviousQuestion());

        nextButton = createNavButton("Selanjutnya →");
        nextButton.setEnabled(false);
        nextButton.addActionListener(e -> moveToNextQuestion());

        submitButton = createNavButton("Selesai Quiz");
        submitButton.setBackground(SUCCESS_BG);
        submitButton.addActionListener(e -> confirmFinishQuiz());

        navigationPanel.add(prevButton);
        navigationPanel.add(nextButton);
        navigationPanel.add(submitButton);

        add(navigationPanel);
    }

    private void confirmFinishQuiz() {
        JOptionPane optionPane = new JOptionPane(
                "Apakah Anda yakin ingin menyelesaikan quiz?",
                JOptionPane.QUESTION_MESSAGE,
                JOptionPane.YES_NO_OPTION
        );

        JDialog dialog = optionPane.createDialog("Konfirmasi Selesai Quiz");
        dialog.getContentPane().setBackground(BG_COLOR);
        dialog.setVisible(true);

        Object result = optionPane.getValue();
        if (result != null && (int) result == JOptionPane.YES_OPTION) {
            finishQuiz();
        }
    }

    private JButton createNavButton(String text) {
        JButton button = new JButton(text);
        button.setFont(BUTTON_FONT);
        button.setBackground(BUTTON_BG);
        button.setForeground(BUTTON_TEXT);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(180, 42));
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 2),
                BorderFactory.createEmptyBorder(8, 18, 8, 18)
        ));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(BUTTON_BG_HOVER);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(BUTTON_BG);
            }
        });

        return button;
    }

    private JButton createBackButton() {
        JButton button = new JButton("← Kembali");
        button.setFont(BUTTON_FONT);
        button.setBackground(new Color(30, 20, 20, 220));
        button.setForeground(TEXT_SOFT);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 2),
                BorderFactory.createEmptyBorder(5, 12, 5, 12)
        ));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(60, 40, 40, 230));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(30, 20, 20, 220));
            }
        });

        button.addActionListener(e -> {
            stopAllThreads();
            mainApp.showPanel("MAIN_MENU");
        });
        return button;
    }

    //COUNTDOWN OVERLAY

    private void createCountdownOverlay() {
        countdownLabel = new JLabel("Bersiap...", JLabel.CENTER);
        countdownLabel.setBounds(0, 260, PANEL_WIDTH, 50);
        countdownLabel.setFont(COUNTDOWN_TEXT_FONT);
        countdownLabel.setForeground(TEXT_MAIN);
        countdownLabel.setVisible(false);

        countdownNumber = new JLabel("3", JLabel.CENTER);
        countdownNumber.setBounds(0, 180, PANEL_WIDTH, 150);
        countdownNumber.setFont(COUNTDOWN_NUMBER_FONT);
        countdownNumber.setForeground(ColorPalette.COPPER_ROSE);
        countdownNumber.setVisible(false);

        add(countdownNumber);
        add(countdownLabel);
    }

    //PANEL HASIL QUIZ

    private void createResultPanel() {
        int w = 520;
        int h = 320;
        int x = (PANEL_WIDTH - w) / 2;
        int y = (PANEL_HEIGHT - h) / 2;

        resultPanel = new JPanel();
        resultPanel.setBounds(x, y, w, h);
        resultPanel.setBackground(CARD_BACKGROUND);
        resultPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 3),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        resultPanel.setLayout(new BorderLayout(0, 20));
        resultPanel.setVisible(false);

        JLabel resultTitle = new JLabel("Hasil Quiz Sejarah", JLabel.CENTER);
        resultTitle.setFont(TITLE_FONT);
        resultTitle.setForeground(ColorPalette.COPPER_ROSE);
        resultTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        resultScore = new JLabel("", JLabel.CENTER);
        resultScore.setFont(RESULT_FONT);
        resultScore.setForeground(TEXT_MAIN);

        resultMessage = new JLabel("", JLabel.CENTER);
        resultMessage.setFont(QUESTION_FONT);
        resultMessage.setForeground(TEXT_SOFT);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(CARD_BACKGROUND);
        centerPanel.add(resultScore, BorderLayout.CENTER);
        centerPanel.add(resultMessage, BorderLayout.SOUTH);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(CARD_BACKGROUND);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        menuButton = createNavButton("🏠 Menu Utama");
        menuButton.addActionListener(e -> mainApp.showPanel("MAIN_MENU"));

        retryButton = createNavButton("🔄 Main Lagi");
        retryButton.addActionListener(e -> resetQuiz());

        buttonPanel.add(menuButton);
        buttonPanel.add(retryButton);

        resultPanel.add(resultTitle, BorderLayout.NORTH);
        resultPanel.add(centerPanel, BorderLayout.CENTER);
        resultPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(resultPanel);
    }

    //LOGIKA QUIZ

    private void resetQuiz() {
        stopAllThreads();

        timeLeft = 180;
        countdown = 3;

        questionPanel.setVisible(false);
        optionsPanel.setVisible(false);
        resultPanel.setVisible(false);
        navigationPanel.setVisible(true);

        timerLabel.setText("⏱ Waktu: 180s");
        questionNumberLabel.setText("📝 Soal: 0/15");

        countdownLabel.setVisible(true);
        countdownNumber.setVisible(true);
        countdownLabel.setText("Bersiap...");

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
                    SwingUtilities.invokeLater(this::startQuiz);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        countdownThread.start();
    }

    private void startQuiz() {
        countdownLabel.setVisible(false);
        countdownNumber.setVisible(false);

        questionPanel.setVisible(true);
        optionsPanel.setVisible(true);
        navigationPanel.setVisible(true);

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
                    final int currentTime = timeLeft;
                    SwingUtilities.invokeLater(() ->
                            timerLabel.setText("⏱ Waktu: " + currentTime + "s")
                    );
                    Thread.sleep(1000);
                    timeLeft--;
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

            questionTextArea.setText(currentQuestion.getQuestionText());

            optionA.setText("A. " + currentQuestion.getOptionA());
            optionB.setText("B. " + currentQuestion.getOptionB());
            optionC.setText("C. " + (currentQuestion.getOptionC() != null ? currentQuestion.getOptionC() : ""));
            optionD.setText("D. " + (currentQuestion.getOptionD() != null ? currentQuestion.getOptionD() : ""));

            optionC.setVisible(currentQuestion.getOptionC() != null);
            optionD.setVisible(currentQuestion.getOptionD() != null);

            questionNumberLabel.setText("📝 Soal: " +
                    quizManager.getCurrentQuestionNumber() + "/" + quizManager.getTotalQuestions());

            prevButton.setEnabled(quizManager.getCurrentQuestionIndex() > 0);
            nextButton.setEnabled(quizManager.getCurrentQuestionIndex() < quizManager.getTotalQuestions() - 1);

            optionsGroup.clearSelection();
            optionA.setEnabled(true);
            optionB.setEnabled(true);
            optionC.setEnabled(true);
            optionD.setEnabled(true);
            optionA.setForeground(TEXT_SOFT);
            optionB.setForeground(TEXT_SOFT);
            optionC.setForeground(TEXT_SOFT);
            optionD.setForeground(TEXT_SOFT);

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
        } else {
            submitButton.setEnabled(true);
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

        questionPanel.setVisible(false);
        optionsPanel.setVisible(false);
        navigationPanel.setVisible(false);
        resultPanel.setVisible(true);

        int score = quizManager.calculateScore();
        int totalQuestions = quizManager.getTotalQuestions();
        int timeTaken = (int) ((System.currentTimeMillis() - startTime) / 1000);
        double percentage = (score * 100.0) / totalQuestions;

        resultScore.setText(score + "/" + totalQuestions);
        resultMessage.setText(String.format("Akurasi: %.1f%% | Waktu: %d detik", percentage, timeTaken));

        boolean saved = quizManager.saveQuizResults(
                mainApp.getCurrentUserId(),
                mainApp.getCurrentProfileId()
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
