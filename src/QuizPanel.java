package src;

import javax.swing.*;
import java.awt.*;

public class QuizPanel extends JPanel {
    private RengasdengklokGame mainApp;
    private QuizManager quizManager;
    
    private Thread countdownThread;
    private Thread quizTimerThread;
    private final Object threadLock = new Object();
    private int timeLeft = 180; // 3 minutes for quiz
    private int countdown = 3;
    private volatile boolean countDownThreadRunning = false;
    private volatile boolean quizTimerRunning = false;
    private long startTime;
    
    // ni color palette
    private final Color BACKGROUND_COLOR = new Color(0xF5, 0xED, 0xE0); // Cream vintage
    private final Color ACCENT_COLOR = new Color(0x8B, 0x45, 0x1E);     // Dark brown vintage
    private final Color TEXT_COLOR = new Color(0x5D, 0x30, 0x3C);       // Deep brown
    private final Color INPUT_BACKGROUND = new Color(0xFA, 0xF3, 0xE8); // Light cream
    private final Color BUTTON_BACKGROUND = new Color(0xA0, 0x6C, 0x4F); // Medium brown
    private final Color BUTTON_TEXT_COLOR = new Color(115, 102, 47); // Light cream untuk text button
    private final Color COUNTDOWN_TEXT_COLOR = new Color(0x8B, 0x45, 0x1E); // Dark brown
    private final Color SUCCESS_COLOR = new Color(0x72, 0x9D, 0x39);     // Green vintage
    private final Color BORDER_COLOR = new Color(0x8B, 0x45, 0x1E);      // Dark brown untuk border
    
    // Fonts 
    private final Font TITLE_FONT = new Font("Serif", Font.BOLD, 24);
    private final Font INFO_FONT = new Font("Serif", Font.BOLD, 14);
    private final Font QUESTION_FONT = new Font("Serif", Font.PLAIN, 16);
    private final Font OPTION_FONT = new Font("Serif", Font.PLAIN, 14);
    private final Font BUTTON_FONT = new Font("Serif", Font.BOLD, 14);
    private final Font COUNTDOWN_TEXT_FONT = new Font("Serif", Font.BOLD, 28);
    private final Font COUNTDOWN_NUMBER_FONT = new Font("Serif", Font.BOLD, 120);
    private final Font RESULT_FONT = new Font("Serif", Font.BOLD, 48);
    
    private JLabel countdownLabel, countdownNumber, timerLabel, questionNumberLabel, userLabel;
    private JPanel questionPanel, optionsPanel, resultPanel, navigationPanel;
    private JTextArea questionTextArea;
    private JRadioButton optionA, optionB, optionC, optionD;
    private ButtonGroup optionsGroup;
    private JButton backButton, nextButton, prevButton, submitButton;
    private JLabel resultScore, resultMessage;
    private JButton menuButton, retryButton;
    
    public QuizPanel(RengasdengklokGame mainApp) {
        this.mainApp = mainApp;
        this.quizManager = new QuizManager(mainApp.getDbManager());
        initializeUI();
    }
    
    public void onPanelShown() {
        resetQuiz();
    }
    
    private void initializeUI() {
        setLayout(null);
        setBackground(BACKGROUND_COLOR);
        setPreferredSize(new Dimension(800, 600));
        
        createInfoPanel();
        createQuestionPanel();
        createOptionsPanel();
        createNavigationPanel();
        createCountdownOverlay();
        createResultPanel();
    }
    
    private void createInfoPanel() {
        JPanel infoPanel = new JPanel();
        infoPanel.setBounds(50, 20, 700, 40);
        infoPanel.setLayout(new GridLayout(1, 4, 10, 0));
        infoPanel.setBackground(BACKGROUND_COLOR);
        infoPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, ACCENT_COLOR));
        
        backButton = createBackButton();
        infoPanel.add(backButton);

        // Timer label
        timerLabel = new JLabel("⏱ Waktu: 180s", JLabel.CENTER);
        timerLabel.setFont(INFO_FONT);
        timerLabel.setForeground(TEXT_COLOR);
        infoPanel.add(timerLabel);
        
        // Question number label
        questionNumberLabel = new JLabel("📝 Soal: 0/15", JLabel.CENTER);
        questionNumberLabel.setFont(INFO_FONT);
        questionNumberLabel.setForeground(TEXT_COLOR);
        infoPanel.add(questionNumberLabel);
        
        // User label
        userLabel = new JLabel("👤 " + mainApp.getCurrentUsername(), JLabel.CENTER);
        userLabel.setFont(INFO_FONT);
        userLabel.setForeground(TEXT_COLOR);
        infoPanel.add(userLabel);
        
        add(infoPanel);
    }
    
    private void createQuestionPanel() {
        questionPanel = new JPanel();
        questionPanel.setBounds(50, 80, 700, 120);
        questionPanel.setBackground(INPUT_BACKGROUND);
        questionPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 2),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        questionPanel.setLayout(new BorderLayout());
        
        questionTextArea = new JTextArea();
        questionTextArea.setFont(QUESTION_FONT);
        questionTextArea.setForeground(TEXT_COLOR);
        questionTextArea.setBackground(INPUT_BACKGROUND);
        questionTextArea.setLineWrap(true);
        questionTextArea.setWrapStyleWord(true);
        questionTextArea.setEditable(false);
        questionTextArea.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        JScrollPane scrollPane = new JScrollPane(questionTextArea);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(INPUT_BACKGROUND);
        
        questionPanel.add(scrollPane, BorderLayout.CENTER);
        add(questionPanel);
    }
    
    private void createOptionsPanel() {
        optionsPanel = new JPanel();
        optionsPanel.setBounds(50, 220, 700, 200);
        optionsPanel.setBackground(INPUT_BACKGROUND);
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
        radioButton.setForeground(TEXT_COLOR);
        radioButton.setBackground(INPUT_BACKGROUND);
        radioButton.setFocusPainted(false);
        radioButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Style untuk radio button
        radioButton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0xCD, 0xAA, 0x7D), 1),
            BorderFactory.createEmptyBorder(8, 15, 8, 15)
        ));
        
        radioButton.addActionListener(e -> {
            if (quizManager.getCurrentQuestion() != null) {
                String answer = label.substring(0, 1); // Get "A", "B", etc.
                quizManager.submitAnswer(answer);
            }
        });
        
        // Efek hover
        radioButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                radioButton.setBackground(new Color(0xE8, 0xDC, 0xC8));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                radioButton.setBackground(INPUT_BACKGROUND);
            }
        });
        
        return radioButton;
    }
    
    private void createNavigationPanel() {
        navigationPanel = new JPanel();
        navigationPanel.setBounds(50, 440, 700, 50);
        navigationPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 0));
        navigationPanel.setOpaque(false);
        
        prevButton = createNavButton("← Sebelumnya");
        prevButton.setEnabled(false);
        prevButton.addActionListener(e -> {
            moveToPreviousQuestion();
        });
        
        nextButton = createNavButton("Selanjutnya →");
        nextButton.setEnabled(false);
        nextButton.addActionListener(e -> {
            moveToNextQuestion();
        });
        
        submitButton = createNavButton("Selesai Quiz");
        submitButton.setBackground(SUCCESS_COLOR);
        submitButton.setForeground(BUTTON_TEXT_COLOR);
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
        dialog.getContentPane().setBackground(BACKGROUND_COLOR);
        dialog.setVisible(true);
        
        Object result = optionPane.getValue();
        if (result != null && (int)result == JOptionPane.YES_OPTION) {
            finishQuiz();
        }
    }
    
    private JButton createNavButton(String text) {
        JButton button = new JButton(text);
        button.setFont(BUTTON_FONT);
        button.setBackground(BUTTON_BACKGROUND);
        button.setForeground(BUTTON_TEXT_COLOR);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(150, 40));
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ACCENT_COLOR, 2),
            BorderFactory.createEmptyBorder(8, 15, 8, 15)
        ));
        
        // Efek hover
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(ACCENT_COLOR);
                button.setForeground(BUTTON_TEXT_COLOR);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(BUTTON_BACKGROUND);
                button.setForeground(BUTTON_TEXT_COLOR);
            }
        });
        
        return button;
    }
    
    private JButton createBackButton() {
        JButton button = new JButton("← Kembali");
        button.setFont(BUTTON_FONT);
        button.setBackground(BUTTON_BACKGROUND);
        button.setForeground(BUTTON_TEXT_COLOR);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(100, 35));
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ACCENT_COLOR, 2),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        
        // Efek hover
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(ACCENT_COLOR);
                button.setForeground(BUTTON_TEXT_COLOR);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(BUTTON_BACKGROUND);
                button.setForeground(BUTTON_TEXT_COLOR);
            }
        });
        
        button.addActionListener(e -> {
            stopAllThreads();
            mainApp.showPanel("MAIN_MENU");
        });
        return button;
    }
    
    private void createCountdownOverlay() {
        countdownLabel = new JLabel("Bersiap...", JLabel.CENTER);
        countdownLabel.setBounds(0, 200, 800, 50);
        countdownLabel.setFont(COUNTDOWN_TEXT_FONT);
        countdownLabel.setForeground(COUNTDOWN_TEXT_COLOR);
        countdownLabel.setVisible(false);
        
        countdownNumber = new JLabel("3", JLabel.CENTER);
        countdownNumber.setBounds(0, 150, 800, 150);
        countdownNumber.setFont(COUNTDOWN_NUMBER_FONT);
        countdownNumber.setForeground(ACCENT_COLOR);
        countdownNumber.setVisible(false);
        
        add(countdownNumber);
        add(countdownLabel);
    }
    
    private void createResultPanel() {
        resultPanel = new JPanel();
        resultPanel.setBounds(150, 150, 500, 300);
        resultPanel.setBackground(INPUT_BACKGROUND);
        resultPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ACCENT_COLOR, 3),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        resultPanel.setLayout(new BorderLayout(0, 20));
        resultPanel.setVisible(false);
        
        JLabel resultTitle = new JLabel("Hasil Quiz Sejarah", JLabel.CENTER);
        resultTitle.setFont(TITLE_FONT);
        resultTitle.setForeground(ACCENT_COLOR);
        resultTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        
        resultScore = new JLabel("", JLabel.CENTER);
        resultScore.setFont(RESULT_FONT);
        resultScore.setForeground(ACCENT_COLOR);
        
        resultMessage = new JLabel("", JLabel.CENTER);
        resultMessage.setFont(QUESTION_FONT);
        resultMessage.setForeground(TEXT_COLOR);
        
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(INPUT_BACKGROUND);
        centerPanel.add(resultScore, BorderLayout.CENTER);
        centerPanel.add(resultMessage, BorderLayout.SOUTH);
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(INPUT_BACKGROUND);
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
                    SwingUtilities.invokeLater(() -> {
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
                    SwingUtilities.invokeLater(() -> {
                        timerLabel.setText("⏱ Waktu: " + currentTime + "s");
                    });
                    Thread.sleep(1000);
                    timeLeft--;
                }
                
                if (quizTimerRunning && timeLeft <= 0) {
                    SwingUtilities.invokeLater(() -> {
                        finishQuiz();
                    });
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
            
            questionNumberLabel.setText("📝 Soal: " + quizManager.getCurrentQuestionNumber() + "/" + quizManager.getTotalQuestions());
            
            // Update navigation buttons
            prevButton.setEnabled(quizManager.getCurrentQuestionIndex() > 0);
            nextButton.setEnabled(quizManager.getCurrentQuestionIndex() < quizManager.getTotalQuestions() - 1);
            
            // Reset options dan set jawaban yang sudah dipilih sebelumnya
            optionsGroup.clearSelection();
            optionA.setEnabled(true);
            optionB.setEnabled(true);
            optionC.setEnabled(true);
            optionD.setEnabled(true);
            optionA.setForeground(TEXT_COLOR);
            optionB.setForeground(TEXT_COLOR);
            optionC.setForeground(TEXT_COLOR);
            optionD.setForeground(TEXT_COLOR);
            
            // Set jawaban yang sudah dipilih sebelumnya (kalo ada)
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
            // Jika sudah di soal terakhir, enable submit button
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
        int timeTaken = (int)((System.currentTimeMillis() - startTime) / 1000);
        double percentage = (score * 100.0) / totalQuestions;
        
        resultScore.setText(score + "/" + totalQuestions);
        resultMessage.setText(String.format("Akurasi: %.1f%% | Waktu: %d detik", percentage, timeTaken));
        
        // Save to database
        boolean saved = quizManager.saveQuizResults(
            mainApp.getCurrentUserId(), 
            mainApp.getCurrentProfileId()
        );
        
        if (!saved) {
            System.err.println("Failed to save quiz results to database.");
        }
    }
    
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