import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Map;

public class StoryPanel extends JPanel {
    private JLabel sceneTitleLabel;
    private JTextArea descriptionArea;
    private DefaultListModel<String> characterListModel;
    private JList<String> characterList;
    private JPanel choicesPanel;
    private JLabel timerLabel;
    private JLabel soekarnoLabel;
    private JLabel hattaLabel;
    private JLabel trustLabel;
    private JLabel pemudaLabel;
    private UIManager uiManager;

    public StoryPanel() {
        setLayout(new BorderLayout(12, 12));
        setBackground(new Color(0xF5, 0xED, 0xE0));
        buildHeader();
        buildCenterArea();
        buildFooter();
    }

    public void setUiManager(UIManager uiManager) {
        this.uiManager = uiManager;
    }

    private void buildHeader() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);

        sceneTitleLabel = new JLabel("Scene Title", SwingConstants.LEFT);
        sceneTitleLabel.setFont(new Font("Serif", Font.BOLD, 26));
        sceneTitleLabel.setForeground(new Color(0x8B, 0x45, 0x1E));

        timerLabel = new JLabel("Timer: --", SwingConstants.RIGHT);
        timerLabel.setFont(new Font("Serif", Font.BOLD, 18));
        timerLabel.setForeground(new Color(0x5D, 0x30, 0x3C));

        headerPanel.add(sceneTitleLabel, BorderLayout.WEST);
        headerPanel.add(timerLabel, BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);
    }

    private void buildCenterArea() {
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1;

        descriptionArea = new JTextArea();
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setEditable(false);
        descriptionArea.setFont(new Font("Serif", Font.PLAIN, 16));
        descriptionArea.setBackground(new Color(0xFA, 0xF3, 0xE8));
        descriptionArea.setForeground(new Color(0x5D, 0x30, 0x3C));
        descriptionArea.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0x8B, 0x45, 0x1E)),
                BorderFactory.createEmptyBorder(12, 12, 12, 12)));

        JScrollPane descriptionScroll = new JScrollPane(descriptionArea);
        descriptionScroll.setBorder(BorderFactory.createEmptyBorder());

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weighty = 0.6;
        centerPanel.add(descriptionScroll, gbc);

        characterListModel = new DefaultListModel<>();
        characterList = new JList<>(characterListModel);
        characterList.setBackground(new Color(0xFA, 0xF3, 0xE8));
        characterList.setForeground(new Color(0x5D, 0x30, 0x3C));
        characterList.setFont(new Font("Serif", Font.PLAIN, 14));
        characterList.setBorder(BorderFactory.createTitledBorder("Tokoh di Scene"));

        gbc.gridy = 1;
        gbc.weighty = 0.4;
        centerPanel.add(new JScrollPane(characterList), gbc);

        add(centerPanel, BorderLayout.CENTER);
    }

    private void buildFooter() {
        JPanel footerPanel = new JPanel(new BorderLayout(10, 10));
        footerPanel.setOpaque(false);

        JPanel relationshipPanel = new JPanel(new GridLayout(2, 2, 8, 8));
        relationshipPanel.setOpaque(false);

        soekarnoLabel = createStatLabel("Soekarno", 50);
        hattaLabel = createStatLabel("Hatta", 50);
        trustLabel = createStatLabel("Trust", 50);
        pemudaLabel = createStatLabel("Pemuda", 50);

        relationshipPanel.add(soekarnoLabel);
        relationshipPanel.add(hattaLabel);
        relationshipPanel.add(trustLabel);
        relationshipPanel.add(pemudaLabel);

        choicesPanel = new JPanel();
        choicesPanel.setOpaque(false);
        choicesPanel.setLayout(new BoxLayout(choicesPanel, BoxLayout.Y_AXIS));
        choicesPanel.setBorder(BorderFactory.createTitledBorder("Pilihan"));

        footerPanel.add(relationshipPanel, BorderLayout.NORTH);
        footerPanel.add(new JScrollPane(choicesPanel), BorderLayout.CENTER);
        add(footerPanel, BorderLayout.SOUTH);
    }

    private JLabel createStatLabel(String label, int value) {
        JLabel statLabel = new JLabel(formatStat(label, value));
        statLabel.setFont(new Font("Serif", Font.BOLD, 14));
        statLabel.setForeground(new Color(0x5D, 0x30, 0x3C));
        return statLabel;
    }

    private String formatStat(String label, int value) {
        return String.format("%s: %d", label, value);
    }

    public void displayScene(Scene scene) {
        if (scene == null) {
            sceneTitleLabel.setText("Scene tidak tersedia");
            descriptionArea.setText("");
            characterListModel.clear();
            choicesPanel.removeAll();
            revalidate();
            repaint();
            return;
        }
        sceneTitleLabel.setText(scene.getSceneTitle());
        descriptionArea.setText(scene.getSceneDescription());
        descriptionArea.setCaretPosition(0);

        characterListModel.clear();
        if (scene.getCharacters() != null) {
            for (Character character : scene.getCharacters()) {
                characterListModel.addElement(character.getCharacterName() +
                        " (" + character.getCharacterRole() + ")");
            }
        }

        updateChoices(scene.getChoices());
    }

    public void updateRelationships(Map<String, Integer> relationships) {
        if (relationships == null) {
            return;
        }
        soekarnoLabel.setText(formatStat("Soekarno", relationships.getOrDefault("SOEKARNO", 50)));
        hattaLabel.setText(formatStat("Hatta", relationships.getOrDefault("HATTA", 50)));
        trustLabel.setText(formatStat("Trust", relationships.getOrDefault("TRUST", 50)));
        pemudaLabel.setText(formatStat("Pemuda", relationships.getOrDefault("PEMUDA", 50)));
    }

    public void updateTimer(int secondsRemaining) {
        timerLabel.setText("Timer: " + secondsRemaining + "s");
    }

    private void updateChoices(List<Choice> choices) {
        choicesPanel.removeAll();
        if (choices == null || choices.isEmpty()) {
            JLabel noChoiceLabel = new JLabel("Tidak ada pilihan tersedia.");
            noChoiceLabel.setFont(new Font("Serif", Font.ITALIC, 14));
            choicesPanel.add(noChoiceLabel);
        } else {
            char optionCode = 'A';
            int index = 0;
            for (Choice choice : choices) {
                final int buttonIndex = index;
                String rawText = choice.getChoiceText() != null ? choice.getChoiceText().trim() : "";
                String buttonLabel = rawText.isEmpty() ? (optionCode + ". Pilihan") : rawText;
                JButton choiceButton = new JButton(buttonLabel);
                choiceButton.setAlignmentX(Component.LEFT_ALIGNMENT);
                choiceButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
                choiceButton.setFocusPainted(false);
                choiceButton.addActionListener(e -> {
                    if (uiManager != null) {
                        uiManager.handleChoiceSelection(choice, buttonIndex);
                    }
                });
                choicesPanel.add(choiceButton);
                choicesPanel.add(Box.createVerticalStrut(8));
                optionCode++;
                index++;
            }
        }
        choicesPanel.revalidate();
        choicesPanel.repaint();
    }
}
