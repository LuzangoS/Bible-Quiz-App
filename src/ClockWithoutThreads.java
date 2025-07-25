import java.util.Date;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ClockWithoutThreads extends JFrame {

    JLabel label = new JLabel("Time will show here");

    public ClockWithoutThreads() {
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panel = new JPanel();
        panel.add(label);
        setContentPane(panel);
        setVisible(true);

        // This runs on the main thread
        while (true) {
            Date date = new Date();
            label.setText(date.toString());
            try {
                Thread.sleep(1000); // Pauses the main thread
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        new ClockWithoutThreads(); // GUI + Clock all on one thread
    }
}
