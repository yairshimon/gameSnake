import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;
import java.awt.Color;
public class Snake extends JPanel implements ActionListener {
    final int[] x = new int[Definitions.UNITS_GAME];
    final int[] y = new int[Definitions.UNITS_GAME];
    Timer timer;
    Random random;
    int countApple;
    int eatenApples;
    int appleX;
    int appleY;
    char direction = 'R';
    boolean running = false;
    int level1 = 1;
    boolean pause = false;
    private ImageIcon keyArrows;

    Snake() {
        this.keyArrows = new ImageIcon("images/211px-Arrow_keys.jpg");
        random = new Random();
        this.setPreferredSize(new Dimension(Definitions.WINDOWS_WIDTH, Definitions.WINDOWS_HEIGHT));
        this.setBackground(Color.cyan);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        button();
    }

    public void button() { //A function that defines the buttons in the game
        Button buttonToStart1 = new Button("Press here to start game Level 1");
        buttonToStart1.setBounds(Definitions.BUTTON_TO_START_WIDTH * 2, 50, Definitions.BUTTON_TO_START_WIDTH, Definitions.BUTTON_TO_START_HEIGHT);
        buttonToStart1.setVisible(true);
        buttonToStart1.isVisible();
        this.add(buttonToStart1);
        Button buttonToStart2 = new Button("Press here to start game Level 2");
        buttonToStart2.setBounds(Definitions.BUTTON_TO_START_WIDTH * 2, Definitions.ZERO, Definitions.BUTTON_TO_START_WIDTH, Definitions.BUTTON_TO_START_HEIGHT);
        buttonToStart2.setVisible(true);
        buttonToStart2.isVisible();
        this.add(buttonToStart2);
        Button buttonForPause = new Button();
        buttonForPause.setLabel("PAUSE");
        buttonForPause.setBounds(Definitions.BUTTON_TO_START_WIDTH * 9, Definitions.ZERO, Definitions.BUTTON_TO_START_WIDTH, Definitions.BUTTON_TO_START_HEIGHT);
        buttonForPause.setVisible(true);
        buttonForPause.isVisible();
        this.add(buttonForPause);
        buttonToStart1.addActionListener((event) -> {
            buttonToStart2.setVisible(false);
            buttonToStart1.setVisible(false);
            buttonForPause.setVisible(false);
            startGame();
            buttonForPause.setVisible(true);
        });
        buttonToStart2.addActionListener((event) -> {
            buttonToStart2.setVisible(false);
            buttonToStart1.setVisible(false);
            buttonForPause.setVisible(false);
            level1++;
            startGame();
            buttonForPause.setVisible(true);
        });
        for (int i = Definitions.ZERO; i < Definitions.FONT_SIZE; i++) { // Loop Avoid a loop with the "pause button"
            if (!running) buttonForPause.setVisible(false);
            buttonForPause.addActionListener((event) -> {
                buttonForPause.setVisible(false);
                if (pause) {
                    timer.start();
                    pause = false;
                    buttonForPause.setLabel("PAUSE");
                } else {
                    timer.stop();
                    pause = true;
                    buttonForPause.setLabel("PLAY");
                }
                buttonForPause.setVisible(true);
            });
        }
    }

    public void startGame() {
        newApple();
        running = true;
        if (level1 == Definitions.ZERO) // Conditions for setting speed at each level
            timer = new Timer(Definitions.ZERO, this);
        else
            timer = new Timer(Definitions.INHIBIT / level1, this);
        timer.start();

    }

    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        if (countApple == Definitions.ZERO) {  //  If you ate 0 apples still did not start the game showed game rules.
            rulesGame(graphics);
            this.keyArrows.paintIcon(this, graphics, 540, 210);
        }else if (countApple == Definitions.winner) {// If you ate 25 apples you won
            graphics.setColor(Color.red);
            graphics.setFont(new Font("Ink Free", Font.BOLD, 75));
            // FontMetrics metrics2 = getFontMetrics(graphics.getFont());
            graphics.drawString("Congratulations you won", Definitions.ZERO, Definitions.WINDOWS_HEIGHT / 2);
        } else { // Otherwise continue playing
            draw(graphics);
        }
    }

    public void draw(Graphics graphics) {
        if (running) {
            graphics.setColor(Color.red);
            graphics.fillOval(appleX, appleY, Definitions.SIZE_UNIT, Definitions.SIZE_UNIT);
            if (level1 == 1) {
                for (int i = Definitions.ZERO; i < Definitions.BODY_PARTS; i++) {
                    graphics.setColor(Color.green);
                    graphics.fillRect(x[i], y[i], Definitions.SIZE_UNIT, Definitions.SIZE_UNIT);
                }
            } else  {
                for (int i = Definitions.ZERO; i < Definitions.BODY_PARTS; i++) {
                    if (i == Definitions.ZERO) {
                        graphics.setColor(Color.green);
                    } else {
                        graphics.setColor(new Color(random.nextInt(Definitions.BODY_COLOR_RANDOM), random.nextInt(Definitions.BODY_COLOR_RANDOM), random.nextInt(Definitions.BODY_COLOR_RANDOM)));
                    }
                    graphics.fillRect(x[i], y[i], Definitions.SIZE_UNIT, Definitions.SIZE_UNIT);
                }
            }
            graphics.setColor(Color.red);
            graphics.setFont(new Font("Ink Free", Font.BOLD, Definitions.FONT_SIZE));
            FontMetrics metrics = getFontMetrics(graphics.getFont());
            graphics.drawString("Apples: " + eatenApples, (Definitions.WINDOWS_WIDTH - metrics.stringWidth("Apples: " + eatenApples)) / 2, graphics.getFont().getSize());
        } else {
            gameOver(graphics);
        }

    }

    public void newApple() {  // The function prints a new apple on the screen
        appleX = random.nextInt(Definitions.WINDOWS_WIDTH / Definitions.SIZE_UNIT) * Definitions.SIZE_UNIT;
        appleY = random.nextInt(Definitions.WINDOWS_HEIGHT / Definitions.SIZE_UNIT) * Definitions.SIZE_UNIT;
        countApple++;
    }

    public void move() {
        for (int i = Definitions.BODY_PARTS; i > Definitions.ZERO; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }
        switch (direction) {
            case 'U':
                y[Definitions.ZERO] = y[Definitions.ZERO] - Definitions.SIZE_UNIT;
                break;
            case 'D':
                y[Definitions.ZERO] = y[Definitions.ZERO] + Definitions.SIZE_UNIT;
                break;
            case 'L':
                x[Definitions.ZERO] = x[Definitions.ZERO] - Definitions.SIZE_UNIT;
                break;
            case 'R':
                x[Definitions.ZERO] = x[Definitions.ZERO] + Definitions.SIZE_UNIT;
                break;
        }

    }

    public void checkApple() {  //The function checks if you have eaten an apple and if so adds a score to your score
        if ((x[0] == appleX) && (y[0] == appleY)) {
            Definitions.BODY_PARTS++;
            eatenApples++;
            newApple();
        }

    }

    public void checkCollisions() {
        //checks if head collides with body
        if (level1 == 2) { // // Checks if the head collides with the body only at level 2
            for (int i = Definitions.BODY_PARTS; i > 0; i--) {
                if ((x[Definitions.ZERO] == x[i]) && (y[Definitions.ZERO] == y[i])) {
                    running = false;
                    break;
                }
            }
        }
        //check if head touches left border
        if (x[Definitions.ZERO] < Definitions.ZERO) {
            running = false;
        }
        //check if head touches right border
        if (x[Definitions.ZERO] > Definitions.WINDOWS_WIDTH) {
            running = false;
        }
        //check if head touches top border
        if (y[Definitions.ZERO] < Definitions.ZERO) {
            running = false;
        }
        //check if head touches bottom border
        if (y[Definitions.ZERO] > Definitions.WINDOWS_HEIGHT) {
            running = false;
        }

        if (!running) {
            timer.stop();
        }

    }

    public void rulesGame(Graphics graphics) { //The function prints the rules of the game on the screen
        graphics.setColor(Color.black);
        graphics.setFont(new Font("BOLD", Font.BOLD, Definitions.FONT_SIZE / 2));
        graphics.drawString("Game Instructions:", Definitions.ZERO, Definitions.WINDOWS_HEIGHT / 4);
        graphics.drawString("You should eat the apples and not collide with the walls or the body of the snake", Definitions.ZERO, Definitions.WINDOWS_HEIGHT / 2 - 50);
        graphics.drawString("To win the game you have to eat 25 apples.", Definitions.ZERO, Definitions.WINDOWS_HEIGHT / 2);
        graphics.drawString("The buttons in the game are the 4 arrows on the keyboard.", Definitions.ZERO, Definitions.WINDOWS_HEIGHT / 2 + 50);
        graphics.drawString("In level 1 the snake is slow and can pass through the snake's body.", Definitions.ZERO, Definitions.WINDOWS_HEIGHT / 2 + 100);
        graphics.drawString("In level 2 the snake is fast and contact with the body of the snake will result in disqualification.", Definitions.ZERO, Definitions.WINDOWS_HEIGHT / 2 + 150);
        graphics.drawString("To stop the game in the middle click on the pause button in the top right screen, or on the space button on the keyboard ", Definitions.ZERO, Definitions.WINDOWS_HEIGHT / 2 + 200);
        graphics.drawString("To start a new game in the middle of a game, press esc on the keyboard ", Definitions.ZERO, Definitions.WINDOWS_HEIGHT / 2 + 240);
    }

    public void gameOver(Graphics graphics) { // The function prints your final score on the screen, "game over" and an option for another game
        graphics.setColor(Color.red);
        graphics.setFont(new Font("Ink Free", Font.BOLD, Definitions.FONT_SIZE));
        FontMetrics metrics1 = getFontMetrics(graphics.getFont());
        graphics.drawString("Your score: " + eatenApples, (Definitions.WINDOWS_WIDTH - metrics1.stringWidth("Your score: " + eatenApples)) / 2, graphics.getFont().getSize());
        graphics.setColor(Color.red);
        graphics.setFont(new Font("Ink Free", Font.BOLD, Definitions.FONT_SIZE * 3));
        FontMetrics metrics2 = getFontMetrics(graphics.getFont());
        graphics.drawString("Game Over", (Definitions.WINDOWS_WIDTH - metrics2.stringWidth("Game Over")) / 2, Definitions.WINDOWS_HEIGHT / 2);
        Button buttonToStart4 = new Button("New game");
        buttonToStart4.setBounds(Definitions.ZERO, Definitions.ZERO, Definitions.BUTTON_TO_START_WIDTH, Definitions.BUTTON_TO_START_HEIGHT);
        buttonToStart4.isVisible();
        buttonToStart4.setVisible(true);
        this.add(buttonToStart4);
        buttonToStart4.addActionListener((event) -> {
            buttonToStart4.setVisible(false);
            new DisplaysGameScreen();
        });

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            move();
            checkApple();
            checkCollisions();
        }
        repaint();
    }


    public class MyKeyAdapter extends KeyAdapter  {
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_ESCAPE:
                    timer.stop();
                    new DisplaysGameScreen();
                    break;
                case KeyEvent.VK_SPACE:
                    if (pause) {
                        timer.start();
                        pause = false;
                    } else {
                        timer.stop();
                        pause = true;
                    }
                    break;
                case KeyEvent.VK_LEFT:
                    if (direction != 'R') {
                        direction = 'L';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if (direction != 'L') {
                        direction = 'R';
                    }
                    break;
                case KeyEvent.VK_UP:
                    if (direction != 'Y') {
                        direction = 'U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if (direction != 'U') {
                        direction = 'D';
                    }
                    break;
            }
        }
    }
}