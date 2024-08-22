import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;

import javax.swing.JPanel;

public class GamePanel extends JPanel implements ActionListener{
    //instance variables
    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;
    static final int UNIT_SIZE = 25;
    static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT)/UNIT_SIZE;
    static final int DELAY = 75;
    int[] x = new int[GAME_UNITS];
    int[] y = new int[GAME_UNITS];
    int bodyParts = 6;
    int applesEaten;
    int appleX;
    int appleY;
    char direction = 'R';
    boolean running = false;
    Timer timer;
    Random random;
    //Constructor
    GamePanel(){
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.green);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();
    }

    //Starts the game and runs the timer
    public void startGame() {
        newApple();
        running = true;
        timer = new Timer(DELAY, this);
        timer.start();
    }
    //Calls the super method and draw
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }
    //If the progrma is running, draws the grid lines, the apples, the snake, and the score.
    public void draw(Graphics g) {
        if(running) {
            for (int i = 0; i < SCREEN_HEIGHT / UNIT_SIZE; i++) {
                g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT);
                g.drawLine(0, i * UNIT_SIZE, SCREEN_WIDTH, i * UNIT_SIZE);
            }
            g.setColor(Color.red);
            g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);

            for (int i = 0; i < bodyParts; i++) {
                if (i == 0) {
                    g.setColor(new Color(random.nextInt(255),random.nextInt(255),random.nextInt(255)));
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                } else {
                    g.setColor(new Color(random.nextInt(255),random.nextInt(255),random.nextInt(255)));
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
            }
            g.setColor(Color.red);
            g.setFont(new Font("Comic Sans MS", Font.PLAIN, 30));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Apples Eaten: " + applesEaten, (SCREEN_WIDTH - metrics.stringWidth("Apples Eaten: " + applesEaten))/2, g.getFont().getSize());
        }
        else {
            gameOver(g);
        }
    }
    public void newApple() {
        appleX = random.nextInt(SCREEN_WIDTH/UNIT_SIZE)*UNIT_SIZE;
        appleY = random.nextInt(SCREEN_HEIGHT/UNIT_SIZE)*UNIT_SIZE;
    }
    public void move() {
        for(int i = bodyParts; i > 0; i--) {
            x[i] = x[i-1];
            y[i] = y[i-1];
        }

        switch(direction) {
            case 'U':
                y[0] = y[0] - UNIT_SIZE;
                break;
            case 'D':
                y[0] = y[0] + UNIT_SIZE;
                break;
            case 'L':
                x[0] = x[0] - UNIT_SIZE;
                break;
            case 'R':
                x[0] = x[0] + UNIT_SIZE;
                break;
        }
    }
    public void checkApple() {
        if(x[0] == appleX && y[0] == appleY) {
            bodyParts++;
            applesEaten++;
            newApple();
        }
    }
    public void checkCollision() {
        //Check if head collides with left border
        if(x[0] < 0) {
            running = false;
        }
        //Check if head collides with right border
        else if(x[0] > SCREEN_WIDTH) {
            running = false;
        }
        //Check if head collides with top border
        else if(y[0] < 0) {
            running = false;
        }
        //Check if head collides with bottom border
        else if(y[0] > SCREEN_HEIGHT) {
            running = false;
        }
        //Checks if head collides with body
        for(int i = bodyParts; i > 0; i--){
            if(x[0] == x[i] && y[0] == y[i]) {
                running = false;
            }
        }
        if(!running) {
            timer.stop();
        }
    }
    public void gameOver(Graphics g) {
        //Game Over Message
        g.setColor(Color.red);
        g.setFont(new Font("Comic Sans MS", Font.BOLD, 65));
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        g.drawString("Game Over", (SCREEN_WIDTH - metrics1.stringWidth("Game Over"))/2, SCREEN_HEIGHT/2);
        g.setColor(Color.red);
        g.setFont(new Font("Comic Sans MS", Font.PLAIN, 30));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString("Apples Eaten: " + applesEaten, (SCREEN_WIDTH - metrics2.stringWidth("Apples Eaten: " + applesEaten))/2, g.getFont().getSize());
        g.setColor(Color.white);
        g.setFont(new Font("Comic Sans MS", Font.PLAIN, 30));
        FontMetrics metrics3 = getFontMetrics(g.getFont());
        g.drawString("Play Again? (Y)", (SCREEN_WIDTH - metrics3.stringWidth("Apples Eaten: " + applesEaten))/2, 3*SCREEN_HEIGHT/4);
    }
    public void resetData() {
        direction = 'R';
        applesEaten = 0;
        bodyParts = 6;
        x = new int[GAME_UNITS];
        y = new int[GAME_UNITS];
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if(running){
            move();
            checkApple();
            checkCollision();
        }
        repaint();
    }
    public class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            switch(e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    if(direction != 'R') {
                        direction = 'L';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if(direction != 'L') {
                        direction = 'R';
                    }
                    break;
                case KeyEvent.VK_UP:
                    if(direction != 'D') {
                        direction = 'U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if(direction != 'U') {
                        direction = 'D';
                    }
                    break;
                case KeyEvent.VK_Y:
                    if(!running) {
                        repaint();
                        resetData();
                        startGame();
                    }
                    break;
            }
        }
    }
}
