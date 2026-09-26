import javax.swing.SwingUtilities;
import ui.HomeFrame;

public class Main {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(
                () -> {

                    HomeFrame home =
                            new HomeFrame();

                    home.setVisible(true);
                }
        );
    }
}