import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

public class FlappyBird extends JPanel implements ActionListener, KeyListener {

    int boardWidth = 600;
    int boardHeight = 640;

    // Images
    Image backgroundImg, birdImg, topPipeImg, bottomPipeImg;

    Bird bird;

    // Pipe properties
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

        gameObjects.add(bird); //For polymorphism

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

        gameObjects.add(topPipe); //Polymorphism
        gameObjects.add(bottomPipe);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        g.drawImage(backgroundImg, 0, 0, boardWidth, boardHeight, null);

//        g.drawImage(bird.img, bird.x, bird.y, bird.width, bird.height, null);
//        for (Pipe pipe : pipes) {
//            g.drawImage(pipe.img, pipe.x, pipe.y, pipe.width, pipe.height, null);
//        }

        for (GameObject obj : gameObjects) {
            obj.draw(g);  // Polymorphic call
        }

        g.setColor(Color.black);
        g.setFont(new Font("Arial", Font.PLAIN, 32));
        g.drawString(gameOver ? "Game Over: " + (int) score : "Score: " + String.valueOf((int) score), 10, 35);

        //High score
        g.drawString("HS: " + (int) highScore, 10, 70);
    }

    public void move() {
        velocityY += gravity;
        bird.y += velocityY;
        bird.y = Math.max(bird.y, 0);

        for (Pipe pipe : pipes) {
            pipe.x += velocityX;

            if (!pipe.passed && bird.x > pipe.x + pipe.width) {
                score += 0.5;
                pipe.passed = true;
            }

            if (collision(bird, pipe)) {
                gameOver = true;
            }
        }

        if (bird.y > boardHeight) {
            gameOver = true;
        }
    }

    boolean collision(Bird a, Pipe b) {
        return a.x < b.x + b.width && a.x + a.width > b.x && a.y < b.y + b.height && a.y + a.height > b.y;
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
                bird.y = boardWidth / 2;
                velocityY = 0;
                pipes.clear();

                gameObjects.clear();
                gameObjects.add(bird);  // Polymorphism

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

