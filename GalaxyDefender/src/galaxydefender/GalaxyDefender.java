
package galaxydefender;
import java.awt.EventQueue;
import javax.swing.JFrame;


public class GalaxyDefender extends JFrame {

  
    public GalaxyDefender() {
    
        initUI();
    }

    private void initUI() {

        add(new Board());

        setTitle("Galaxy Defender");
        setSize(Commons.BOARD_WIDTH, Commons.BOARD_HEIGHT);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {

        EventQueue.invokeLater(() -> {

            var ex = new GalaxyDefender();
            ex.setVisible(true);
        });
    }
}