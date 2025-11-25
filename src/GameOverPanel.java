import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class GameOverPanel extends JPanel {
    private JLabel endingTitleLabel;
    private JTextArea endingDescriptionArea;
    private JLabel soekarnoLabel;
    private JLabel hattaLabel;
    private JLabel trustLabel;
    private JLabel pemudaLabel;
    private JButton mainMenuButton;
    private JButton replayButton;
    private Runnable onReturnToMenu;
    private Runnable onReplay;

    public GameOverPanel() {
        setLayout(new BorderLayout(16, 16));
        setBackground(new Color(0xF5, 0xED, 0xE0));
        buildHeader();
        buildCenter();
        buildFooter();
    }

    private void buildHeader() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);

        JLabel titleLabel = new JLabel("Perjalanan Usai", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 28));
        titleLabel.setForeground(new Color(0x8B, 0x45, 0x1E));

        endingTitleLabel = new JLabel("ENDING", SwingConstants.CENTER);
        endingTitleLabel.setFont(new Font("Serif", Font.BOLD, 22));
        endingTitleLabel.setForeground(new Color(0x5D, 0x30, 0x3C));

        headerPanel.add(titleLabel, BorderLayout.NORTH);
        headerPanel.add(endingTitleLabel, BorderLayout.SOUTH);
        add(headerPanel, BorderLayout.NORTH);
    }

    private void buildCenter() {
        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.setOpaque(false);

        endingDescriptionArea = new JTextArea();
        endingDescriptionArea.setEditable(false);
        endingDescriptionArea.setWrapStyleWord(true);
        endingDescriptionArea.setLineWrap(true);
        endingDescriptionArea.setFont(new Font("Serif", Font.PLAIN, 16));
        endingDescriptionArea.setBackground(new Color(0xFA, 0xF3, 0xE8));
        endingDescriptionArea.setForeground(new Color(0x3A, 0x1F, 0x24));
        endingDescriptionArea.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0x8B, 0x45, 0x1E)),
                BorderFactory.createEmptyBorder(12, 12, 12, 12)));

        centerPanel.add(new JScrollPane(endingDescriptionArea), BorderLayout.CENTER);

        JPanel statsPanel = new JPanel(new GridLayout(2, 2, 8, 8));
        statsPanel.setOpaque(false);

        soekarnoLabel = createStatLabel("Soekarno", 50);
        hattaLabel = createStatLabel("Hatta", 50);
        trustLabel = createStatLabel("Trust", 50);
        pemudaLabel = createStatLabel("Pemuda", 50);

        statsPanel.add(soekarnoLabel);
        statsPanel.add(hattaLabel);
        statsPanel.add(trustLabel);
        statsPanel.add(pemudaLabel);

        centerPanel.add(statsPanel, BorderLayout.SOUTH);
        add(centerPanel, BorderLayout.CENTER);
    }

    private void buildFooter() {
        JPanel footerPanel = new JPanel();
        footerPanel.setOpaque(false);

        mainMenuButton = new JButton("Kembali ke Menu");
        mainMenuButton.setFont(new Font("Serif", Font.BOLD, 16));
        mainMenuButton.setBackground(new Color(0xA0, 0x6C, 0x4F));
        mainMenuButton.setForeground(Color.WHITE);
        mainMenuButton.setFocusPainted(false);
        mainMenuButton.addActionListener(e -> {
            if (onReturnToMenu != null) {
                onReturnToMenu.run();
            }
        });

        replayButton = new JButton("Main Lagi");
        replayButton.setFont(new Font("Serif", Font.BOLD, 16));
        replayButton.setBackground(new Color(0x8B, 0x45, 0x1E));
        replayButton.setForeground(Color.WHITE);
        replayButton.setFocusPainted(false);
        replayButton.addActionListener(e -> {
            if (onReplay != null) {
                onReplay.run();
            }
        });

        footerPanel.add(mainMenuButton);
        footerPanel.add(replayButton);
        add(footerPanel, BorderLayout.SOUTH);
    }

    private JLabel createStatLabel(String name, int value) {
        JLabel label = new JLabel(formatStat(name, value));
        label.setFont(new Font("Serif", Font.BOLD, 14));
        label.setForeground(new Color(0x5D, 0x30, 0x3C));
        return label;
    }

    private String formatStat(String label, int value) {
        return String.format("%s: %d", label, value);
    }

    public void setEndingInfo(String endingKey, String description, Map<String, Integer> relationships) {
        endingTitleLabel.setText(formatEndingTitle(endingKey));
        endingDescriptionArea.setText(description != null ? description : "");
        endingDescriptionArea.setCaretPosition(0);
        if (relationships != null) {
            soekarnoLabel.setText(formatStat("Soekarno", relationships.getOrDefault("SOEKARNO", 50)));
            hattaLabel.setText(formatStat("Hatta", relationships.getOrDefault("HATTA", 50)));
            trustLabel.setText(formatStat("Trust", relationships.getOrDefault("TRUST", 50)));
            pemudaLabel.setText(formatStat("Pemuda", relationships.getOrDefault("PEMUDA", 50)));
        }
    }

    private String formatEndingTitle(String endingKey) {
        if (endingKey == null || endingKey.isEmpty()) {
            return "ENDING TIDAK DIKETAHUI";
        }
        return endingKey.replace('_', ' ');
    }

    public void setOnReturnToMenu(Runnable onReturnToMenu) {
        this.onReturnToMenu = onReturnToMenu;
    }

    public void setOnReplay(Runnable onReplay) {
        this.onReplay = onReplay;
    }
}
