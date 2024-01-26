
package galaxydefender;
//import java.awt.Container;
import java.awt.EventQueue;
//import javax.swing.JButton;
import javax.swing.JFrame;


public class GalaxyDefender extends JFrame {

  
    public GalaxyDefender() {
    /*
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setBounds(0, 0, 500, 500);
    Container container = frame.getContentPane();
    container.setLayout(null);
    frame.setVisible(true);
            
        strtbtn = new JButton(" Start Game");
        strtbtn.setBounds(150, 10, 200, 40);
        container.add(strtbtn);
        strtbtn.addActionListener(e -> frame.setVisible(false));
        
        
        hghscrbtn = new JButton("High Score");
        hghscrbtn.setBounds(150, 70, 200, 40);
        container.add(hghscrbtn);

        
        quitbtn = new JButton("Quit Game");
        quitbtn.setBounds(150, 130, 200, 40);
        container.add(quitbtn);
       
       */ 
         initUI();
    }

    public void initUI() {

        
        
        add(new Board());

        setTitle("Galaxy Defender Game");
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