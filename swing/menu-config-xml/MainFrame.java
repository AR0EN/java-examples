import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

public class MainFrame extends JFrame {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(
                // UIManager.getCrossPlatformLookAndFeelClassName()
                // "com.sun.java.swing.plaf.motif.MotifLookAndFeel"
                // "javax.swing.plaf.nimbus.NimbusLookAndFeel"
                // "com.sun.java.swing.plaf.gtk.GTKLookAndFeel"
                // "com.jgoodies.looks.windows.WindowsLookAndFeel"
                UIManager.getSystemLookAndFeelClassName()
                // "com.bulenkov.darcula.DarculaLaf"
            );
        } catch (Exception e) {
            e.printStackTrace();
        }

        MainFrame mainFrame = new MainFrame();
        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mainFrame.setSize(800, 600);
        mainFrame.setJMenuBar(new ZMenuBar(args[0]));

        JLabel text = new JLabel("Hello");
        mainFrame.add(text);

        mainFrame.setVisible(true);
    }
}
