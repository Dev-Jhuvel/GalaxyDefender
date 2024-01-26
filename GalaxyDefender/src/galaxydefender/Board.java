
package galaxydefender;


import galaxydefender.sprite.Alien;
import galaxydefender.sprite.Player;
import galaxydefender.sprite.Shot;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;


public class Board extends JPanel  {

    private Dimension d;
    private List<Alien> aliens;
    private Player player;
    private Shot shot;
    private int score = 0;
    private int initialscore;
    private JButton playAgainButton;

    private int direction = -1;
    private int deaths = 0;
    private int lvl = 1;
    public int needToDestroy = Commons.NUMBER_OF_ALIENS_TO_DESTROY;
   
    

    private boolean inGame = true;
    private String explImg = "src/images/explosion.png";
    private String message = "Game Over";
    private Image backgroundImg;

    private Timer timer;


    public Board() {

       initBoard();
        gameInit();
        initUI();
    }
    
 
private void initUI() {
        playAgainButton = new JButton("Continue");
        playAgainButton.addActionListener(new PlayAgainListener());
        playAgainButton.setFocusable(false);
        add(playAgainButton);
        playAgainButton.setVisible(false);
    }
private void resetGame() {
        deaths = 0;
        score = 0;
        inGame = true;
        message = "Game Over";
        player = new Player();
        aliens.clear();
        gameInit();
        

     
        if (!timer.isRunning()) {
            timer.start();
        }

       
        playAgainButton.setVisible(false);

       
        repaint();
        
    }
    private void initBoard() {
        addKeyListener(new TAdapter());
        setFocusable(true);
        d = new Dimension(Commons.BOARD_WIDTH, Commons.BOARD_HEIGHT);
        setBackground(Color.black);

        timer = new Timer(Commons.DELAY, new GameCycle());
        timer.start();

        gameInit();
    }


    private void gameInit() {

        aliens = new ArrayList<>();

        for (int i = 0; i < 1*lvl; i++) {
            for (int j = 0; j < 1*lvl; j++) {

                var alien = new Alien(Commons.ALIEN_INIT_X + 90 * j,
                        Commons.ALIEN_INIT_Y + 70 * i);
                aliens.add(alien);
            }
        }
        backgroundImg = new ImageIcon("src/images/background.jpg").getImage();

        player = new Player();
        shot = new Shot();
    }

    private void drawAliens(Graphics g) {

        for (Alien alien : aliens) {

            if (alien.isVisible()) {

                g.drawImage(alien.getImage(), alien.getX(), alien.getY(), this);
            }

            if (alien.isDying()) {

                alien.die();
            }
        }
    }

    private void drawPlayer(Graphics g) {

        if (player.isVisible()) {

            g.drawImage(player.getImage(), player.getX(), player.getY(), this);
        }

        if (player.isDying()) {

            player.die();
            inGame = false;
        }
    }

    private void drawShot(Graphics g) {

        if (shot.isVisible()) {

            g.drawImage(shot.getImage(), shot.getX(), shot.getY(), this);
        }
    }

     private void drawBombing(Graphics g) {

        for (Alien a : aliens) {

            Alien.Bomb b = a.getBomb();

            if (!b.isDestroyed()) {

                g.drawImage(b.getImage(), b.getX(), b.getY(), this);
            }
        }
    }


    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        doDrawing(g);
    }

    private void doDrawing(Graphics g) {

        g.drawImage(backgroundImg, 0, 0, this);
        g.setColor(Color.green);

        if (inGame) {
        var small = new Font("Comic Sans MS", Font.BOLD, 35);
        var fontMetrics = this.getFontMetrics(small);
        g.setFont(small);
        
        g.drawString("Level: "+lvl, (Commons.BOARD_WIDTH - fontMetrics.stringWidth(message)) / 2 -600,
                Commons.BOARD_WIDTH / 2 - 688);
        g.drawString("Score: " + score, (Commons.BOARD_WIDTH - fontMetrics.stringWidth(message)) / 2 -400,
                Commons.BOARD_WIDTH / 2 - 688);
        
            g.drawLine(0, Commons.GROUND,
                    Commons.BOARD_WIDTH, Commons.GROUND);

            drawAliens(g);
            drawPlayer(g);
            drawShot(g);
            drawBombing(g);

        } else {

            if (timer.isRunning()) {
                timer.stop();
            }

            gameOver(g);
        }

        Toolkit.getDefaultToolkit().sync();
    }

    private void gameOver(Graphics g) {

        g.setColor(Color.black);
        g.fillRect(0, 0, Commons.BOARD_WIDTH, Commons.BOARD_HEIGHT);

        g.setColor(new Color(0, 32, 48));
        g.fillRect(50, Commons.BOARD_WIDTH / 2 - 600, Commons.BOARD_WIDTH - 100, 500);
        g.setColor(Color.white);
        g.drawRect(50, Commons.BOARD_WIDTH / 2 - 600, Commons.BOARD_WIDTH - 100, 500);

        var small = new Font("Comic Sans MS", Font.BOLD, 65);
        var fontMetrics = this.getFontMetrics(small);

        g.setColor(Color.white);
        g.setFont(small);
        g.drawString(message, (Commons.BOARD_WIDTH - fontMetrics.stringWidth(message)) / 2,
                Commons.BOARD_WIDTH / 2 - 400);
        g.drawString("Score: " + score, (Commons.BOARD_WIDTH - fontMetrics.stringWidth("Score: " + score)) / 2,
        Commons.BOARD_WIDTH / 2 - 320);
        playAgainButton.setVisible(true);
    }
private class PlayAgainListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            resetGame();
        }
    }
    private int update() {

        if (deaths == Commons.NUMBER_OF_ALIENS_TO_DESTROY*lvl*lvl) {
            inGame = false;
            timer.stop();
            message = "LEVEL COMPLETE!";
            lvl++;
            
          
            
            
        }

   
        player.act();

    
        if (shot.isVisible()) {

            int shotX = shot.getX();
            int shotY = shot.getY();

            for (Alien alien : aliens) {

                int alienX = alien.getX();
                int alienY = alien.getY();

                if (alien.isVisible() && shot.isVisible()) {
                    if (shotX >= (alienX)
                            && shotX <= (alienX + Commons.ALIEN_WIDTH)
                            && shotY >= (alienY)
                            && shotY <= (alienY + Commons.ALIEN_HEIGHT)) {

                        var ii = new ImageIcon(explImg);
                        alien.setImage(ii.getImage());
                        alien.setDying(true);
                        deaths++;
                        shot.die();                        
                        score += 10;
                    }
                }
            }
            
            int y = shot.getY();
            y -= 4;
            if(score>=10){
            y -= 2;
            }
             if(score>=40){
            y -= 2;
            }
             
              if(score>=60){
            y -= 2;
            }
              
               if(score>=70){
            y -= 2;
            }
               
                if(score>=90){
            y -= 2;
            }
                
                if(score>=120){
            y -= 5;
            }
            if(score>=200){
            y -= 10;
            }
            /*
            else if(score>20){
            y -= 12;
            }else if(score>30){
            y -= 14;
            }else if(score>40){
            y -= 16;
            }else if(score>50){
            y -= 18;
            }else if(score>60){
            y -= 20;
            }else if(score>70){
            y -= 22;
            }else if(score>20){
            y -= 24;
            }else if(score>80){
            y -= 28;
            }else if(score>90){
            y -= 30;
            }else if(score>100){
            y -= 50;
            }else if(score>120){
            y -= 70;
            }*/
            
            if (y < 0) {
                shot.die();
            } else {
                shot.setY(y);
            }
        }

  

        for (Alien alien : aliens) {

            int x = alien.getX();

            if (x >= Commons.BOARD_WIDTH - Commons.BORDER_RIGHT && direction != -1) {

                direction = -1;

                Iterator<Alien> i1 = aliens.iterator();

                while (i1.hasNext()) {

                    Alien a2 = i1.next();
                    a2.setY(a2.getY() + Commons.GO_DOWN*lvl);
                }
            }

            if (x <= Commons.BORDER_LEFT && direction != 1) {

                direction = 1;

                Iterator<Alien> i2 = aliens.iterator();

                while (i2.hasNext()) {

                    Alien a = i2.next();
                    a.setY(a.getY() + Commons.GO_DOWN*lvl);
                }
            }
        }

        Iterator<Alien> it = aliens.iterator();

        while (it.hasNext()) {

            Alien alien = it.next();

            if (alien.isVisible()) {

                int y = alien.getY();

                if (y > Commons.GROUND - Commons.ALIEN_HEIGHT) {
                    inGame = false;
                    message = "Invasion!";
                }

                alien.act(direction);
            }
        }

       
        var generator = new Random();

        for (Alien alien : aliens) {

            int shot = generator.nextInt(15);
            Alien.Bomb bomb = alien.getBomb();

            if (shot == Commons.CHANCE && alien.isVisible() && bomb.isDestroyed()) {

                bomb.setDestroyed(false);
                bomb.setX(alien.getX());
                bomb.setY(alien.getY());
            }

            int bombX = bomb.getX();
            int bombY = bomb.getY();
            int playerX = player.getX();
            int playerY = player.getY();

            if (player.isVisible() && !bomb.isDestroyed()) {

                if (bombX >= (playerX)
                        && bombX <= (playerX + Commons.PLAYER_WIDTH)
                        && bombY >= (playerY)
                        && bombY <= (playerY + Commons.PLAYER_HEIGHT)) {

                    var ii = new ImageIcon(explImg);
                    player.setImage(ii.getImage());
                    player.setDying(true);
                    bomb.setDestroyed(true);
                    
                }
            }

            if (!bomb.isDestroyed()) {

                bomb.setY(bomb.getY() + 5);

                if (bomb.getY() >= Commons.GROUND - Commons.BOMB_HEIGHT) {

                    bomb.setDestroyed(true);
                }
            }
        }
      
        return lvl;
    }

    private void doGameCycle() {

        update();
        repaint();
    }

    private class GameCycle implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            doGameCycle();
        }
    }

    private class TAdapter extends KeyAdapter {

        @Override
        public void keyReleased(KeyEvent e) {

            player.keyReleased(e);
        }

        @Override
        public void keyPressed(KeyEvent e) {

            player.keyPressed(e);

            int x = player.getX();
            int y = player.getY();

            int key = e.getKeyCode();

            if (key == KeyEvent.VK_SPACE) {

                if (inGame) {

                    if (!shot.isVisible()) {

                        shot = new Shot(x, y);
                    }
                }
            }
        }
    }
    
}