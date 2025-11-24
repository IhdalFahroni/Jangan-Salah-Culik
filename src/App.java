import javax.swing.SwingUtilities;

public class App {

    public static void main(String[] args) {
        configureLookAndFeel();
        SwingUtilities.invokeLater(App::launchGame);
    }

    private static void configureLookAndFeel() {
        try {
            javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
        } catch (Exception systemLfError) {
            try {
                javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getCrossPlatformLookAndFeelClassName());
            } catch (Exception fallbackError) {
                fallbackError.printStackTrace();
            }
        }
    }

    private static void launchGame() {
        new RengasdengklokGame();
    }
}
