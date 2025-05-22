import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

public class FlappyBird extends JPanel implements ActionListener, KeyListener,GameEntity {

    private static final int boardWidth = 500;
    private static final int boardHeight = 650;

    Image backgroundImg, birdImg, topPipeImg, bottomPipeImg;

    Bird bird;

    int pipeX = boardWidth, pipeWidth = 64, pipeHeight = 512;

    ArrayList<Pipe> pipes = new ArrayList<>();
    ArrayList<GameObject> gameObjects = new ArrayList<>();
    Random random = new Random();

    int velocityX = -4,velocityY = 0, gravity = 1;

    Timer gameLoop,placePipeTimer;
    boolean gameOver = false;
    double score = 0;
    double highScore = 0;


    public FlappyBird() {
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        setFocusable(true);
        addKeyListener(this);

        // Load Images
        backgroundImg = new ImageIcon(getClass().getResource("./flappybirdbg.png")).getImage();
        birdImg = new ImageIcon(getClass().getResource("./flappybird.png")).getImage();
        topPipeImg = new ImageIcon(getClass().getResource("./toppipe.png")).getImage();
        bottomPipeImg = new ImageIcon(getClass().getResource("./bottompipe.png")).getImage();
        bird = new Bird(boardWidth / 8, boardWidth / 2, 34, 24, birdImg);

        gameObjects.add(bird);
        placePipeTimer = new Timer(1500, e -> placePipes());
        placePipeTimer.start();
        gameLoop = new Timer(1000 / 60, this);
        gameLoop.start();
    }

    void placePipes() {
        int randomPipeY = (int) ( - pipeHeight / 4 - Math.random() * (pipeHeight / 2));
        int openingSpace = boardHeight / 4;

        Pipe topPipe = new Pipe(pipeX, randomPipeY, pipeWidth, pipeHeight, topPipeImg);
        Pipe bottomPipe = new Pipe(pipeX, randomPipeY + pipeHeight + openingSpace, pipeWidth, pipeHeight, bottomPipeImg);

        pipes.add(topPipe);
        pipes.add(bottomPipe);

        gameObjects.add(topPipe);
        gameObjects.add(bottomPipe);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        g.drawImage(backgroundImg, 0, 0, boardWidth, boardHeight, null);
        for (GameObject obj : gameObjects) {
            obj.draw(g);
        }

        g.setColor(Color.black);
        g.setFont(new Font("Arial", Font.PLAIN, 32));
        g.drawString(gameOver ? "Game Over: " + (int) score : "Score: " + String.valueOf((int) score), 10, 35);
        g.drawString("HS: " + (int) highScore, 10, 70);
    }

    public void move() {
        velocityY += gravity;
        bird.setY(bird.getY() + velocityY);
        bird.setY(Math.max(bird.getY(), 0));

        for (Pipe pipe : pipes) {
            pipe.setX(pipe.getX() + velocityX);

            if (!pipe.isPassed() && bird.getX() > pipe.getX() + pipe.getWidth()) {
                score += 0.5;
                pipe.setPassed(true);
            }

            if (collision(bird, pipe)) {
                gameOver = true;
            }
        }

        if (bird.getY()> boardHeight) {
            gameOver = true;
        }
    }

    private boolean collision(Bird a, Pipe b) {
        return a.getX() < b.getX() + b.getWidth() &&
                a.getX() + a.getWidth() > b.getX() &&
                a.getY() < b.getY() + b.getHeight() &&
                a.getY() + a.getHeight() > b.getY();
    }

    public void actionPerformed(ActionEvent e) {
        move();
        repaint();

        if (gameOver) {

          if (score > highScore) {
              highScore = score;
            }

            placePipeTimer.stop();
            gameLoop.stop();
        }
    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            velocityY = -9;

            if (gameOver) {
                bird.setY(boardHeight / 2);
                velocityY = 0;
                pipes.clear();

                gameObjects.clear();
                gameObjects.add(bird);

                gameOver = false;
                score = 0;
                gameLoop.start();
                placePipeTimer.start();
            }
        }
    }

    public void keyTyped(KeyEvent e) {}
    public void keyReleased(KeyEvent e) {}


    public static void main(String[] args) {
        JFrame frame = new JFrame("Flappy Bird");
        FlappyBird game = new FlappyBird();
        frame.add(game);
        frame.setSize(360, 640);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        game.requestFocus();
        frame.setVisible(true);
    }
}

