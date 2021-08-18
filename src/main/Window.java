package main;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import static java.lang.Thread.sleep;

public class Window extends JFrame {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public static Window gameWindow;
    public static Point location = new Point(400, 150); // window location
    public static boolean gameModeSet = false;

    static boolean gameContinues = true;
    static boolean gameStarted = false;
    static boolean decision = false;
    static boolean lose = false;
    static boolean win = false;
    static String gameMode = null;

    public MyPanel panel;

    private int rows = 4;
    private int brickInRow = 7;
    public List<JLabel> bricks = new ArrayList<>();

    public ArrayList<Ball> balls = new ArrayList<>();

    public Window(String gameMode) {
        setLocation(location);
        setSize(420, 420);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ImageIcon icon = null;
        try {
            icon = new ImageIcon("src/resources/images/download.png");
        } catch (Exception e) {
            System.out.println("Couldn't load icon");
        }
        assert icon != null;
        setIconImage(icon.getImage());
        setTitle("Breakout");
        panel = new MyPanel();
        setGameMode(gameMode);
        add(panel);

        // adding bricks
        ImageIcon brickImg = null;
        try {
            brickImg = new ImageIcon(ImageIO.read(new File("src/resources/images/e1p0bbw06ul11.jpg")));
        } catch (Exception e) {
            System.out.println("Couldn't load brick image");
        }

        assert brickImg != null;
        int brickProperty = brickImg.getIconWidth() / brickImg.getIconHeight();
        int horizontalGap = 5;
        int brickWidth = 405 / brickInRow - horizontalGap;
        int brickHeight = brickWidth / brickProperty;
        int leftOver = 405 - (brickInRow * brickWidth + (brickInRow - 1) * horizontalGap);

        for (int i = 0; i < rows; i++) {
            int leftGap = i % 2 == 0 ? 0 : leftOver;
            for (int j = 0; j < brickInRow; j++) {
                JLabel brick = new JLabel(brickImg);
                int verticalGap = 6;
                brick.setBounds(j * (brickWidth + horizontalGap) + leftGap, i * (brickHeight + verticalGap), brickWidth,
                        brickHeight);
                panel.add(brick);
                bricks.add(brick);
            }
        }

        setVisible(true);

    }

    public Window() {
        setLocation(location);
        setSize(420, 420);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ImageIcon icon = null;
        try {
            icon = new ImageIcon("src/resources/images/download.png");
        } catch (Exception e) {
            System.out.println("Couldn't load icon");
        }
        assert icon != null;
        setIconImage(icon.getImage());
        setTitle("Breakout");

        JPanel tempPanel = new JPanel();
        add(tempPanel);
        tempPanel.setBackground(Color.black);
        tempPanel.setLayout(null);

        JButton easy = new JButton("EASY");
        easy.addActionListener(e -> {
            gameMode = "EASY";
            Audio.play(Audio.buttonClick);

        });

        JButton medium = new JButton("MEDIUM");
        medium.addActionListener(e -> {
            gameMode = "MEDIUM";
            Audio.play(Audio.buttonClick);

        });

        JButton hard = new JButton("HARD");
        hard.addActionListener(e -> {
            gameMode = "HARD";
            Audio.play(Audio.buttonClick);

        });

        JLabel text = new JLabel("SELECT DIFFICULTY");
        text.setForeground(Color.blue);
        text.setBounds(150, 50, 150, 50);
        tempPanel.add(text);

        easy.setBackground(Color.green);
        easy.setForeground(Color.black);
        easy.setBounds(60, 150, 100, 50);
        tempPanel.add(easy);

        medium.setBackground(Color.orange);
        medium.setForeground(Color.black);
        medium.setBounds(160, 150, 100, 50);
        tempPanel.add(medium);

        hard.setBackground(Color.red);
        hard.setForeground(Color.black);
        hard.setBounds(260, 150, 100, 50);
        tempPanel.add(hard);

        tempPanel.revalidate();
        tempPanel.repaint();
        setVisible(true);

    }

    public static void reload() {
        try {
            gameWindow.panel.revalidate();
            gameWindow.panel.repaint();
        } catch (Exception ignored) {

        }


    }

    private static void actionPerformed(ActionEvent e) {
        gameContinues = false;
        decision = true;
        Audio.play(Audio.buttonClick);

    }

    public void hitBorder(Ball ball) {
        try {
            if (ball.getY() <= 0) {
                ball.directionY = -ball.directionY;
                Audio.play(Audio.smash);
            } else if (ball.getX() <= 0 || ball.getX() >= panel.getWidth() - ball.ballImage.getIconWidth()) {
                ball.directionX = -ball.directionX;
                Audio.play(Audio.smash);

            } else if (ball.getY() >= panel.getHeight() - ball.ballImage.getIconHeight()) {
                balls.remove(ball);
                panel.remove(ball);
                ball.ballOut = true;
                Audio.play(Audio.ballOut);
                reload();
                if (balls.isEmpty()) {
                    lose();
                }

            }
        } catch (Exception ignored) {

        }

    }

    public void hitBar(Ball ball) {
        if (panel.barPosY - (ball.ballPosY + ball.ballImage.getIconHeight()) <= 1
                && ball.ballCenterX >= panel.barPosLeft && ball.ballCenterX <= panel.barPosRight) {
            if (ball.ballCenterX <= panel.barCenterX) {
                if (ball.ballCenterX <= panel.barPosLeft + panel.barWidth / 6) {
                    ball.directionX = -2;
                    ball.directionY = -1;
                    ball.delay = Delay.SMALL_DELAY;

                } else if (ball.ballCenterX > panel.barCenterX - panel.barWidth / 6) {
                    ball.directionX = -1;
                    ball.directionY = -2;
                    ball.delay = Delay.SMALL_DELAY;

                } else {
                    ball.directionX = -2;
                    ball.directionY = -2;
                    ball.delay = Delay.BIG_DELAY;

                }
            } else if (ball.ballCenterX <= panel.barCenterX + panel.barWidth / 6) {
                ball.directionX = 1;
                ball.directionY = -2;
                ball.delay = Delay.SMALL_DELAY;

            } else if (ball.ballCenterX > panel.barPosRight - panel.barWidth / 6) {
                ball.directionX = 2;
                ball.directionY = -1;
                ball.delay = Delay.SMALL_DELAY;

            } else {
                ball.directionX = 2;
                ball.directionY = -2;
                ball.delay = Delay.BIG_DELAY;

            }
            Audio.play(Audio.smash);
        }

    }

    public synchronized void hitBrick(Ball ball) {
        Point top = new Point(ball.ballCenterX, ball.ballPosY);
        Point bottom = new Point(ball.ballCenterX, ball.ballPosY + ball.ballImage.getIconHeight());
        Point left = new Point(ball.ballPosX, ball.ballCenterY);
        Point right = new Point(ball.ballPosX + ball.ballImage.getIconWidth(), ball.ballCenterY);

        for (JLabel brick : bricks) {

            try {
                if (brick == null) {
                    throw new AssertionError();
                }
                Rectangle brickRectangle = brick.getBounds();

                if (brickRectangle.contains(top) || brickRectangle.contains(bottom)) {
                    ball.directionY *= -1;
                    bricks.remove(brick);
                    panel.remove(brick);
                    Audio.play(Audio.smashBrick);
                    reload();
                    if (bricks.isEmpty()) {
                        win();
                        return;
                    }
                    break;
                } else if (brickRectangle.contains(left) || brickRectangle.contains(right)) {
                    ball.directionX *= -1;
                    bricks.remove(brick);
                    panel.remove(brick);
                    Audio.play(Audio.smashBrick);
                    reload();
                    if (bricks.isEmpty()) {
                        win();
                        return;
                    }
                    break;
                }
            } catch (Exception ignored) {

            }

        }

    }

    public void setGameMode(String gameMode) {
        switch (gameMode) {
            case "EASY" -> {
                rows = 2;
                brickInRow = 3;
                if (!gameModeSet) {
                    Delay.BIG_DELAY *= 2;
                    Delay.SMALL_DELAY *= 2;
                    gameModeSet = true;
                }
            }
            case "MEDIUM" -> {
                rows = 3;
                brickInRow = 5;
                panel.extraBallLeft = 2;
                panel.barBuffTime = 3;
            }
            case "HARD" -> {
                rows = 4;
                brickInRow = 7;
                panel.extraBallLeft = 1;
                panel.barBuffTime = 2;
            }
        }
    }

    public static void setBar(String gameMode) {
        if ("HARD".equals(gameMode)) {
            MyPanel.SMALL_BAR = 90;
            MyPanel.BIG_BAR = 150;
        }

    }

    public void lose() throws Exception {
        Audio.play(Audio.loseMusic);
        lose = true;
        JLabel label = new JLabel("YOU LOSE!");
        label.setForeground(Color.white);
        JButton restart = new JButton("Restart");
        restart.addActionListener(e -> {
            gameContinues = true;
            decision = true;
            Audio.play(Audio.buttonClick);

        });
        JButton exit = new JButton("Exit");
        exit.addActionListener(Window::actionPerformed);
        label.setBounds(175, 100, 200, 50);
        restart.setBounds(110, 151, 100, 50);
        exit.setBounds(210, 151, 100, 50);
        panel.add(label);
        panel.add(restart);
        panel.add(exit);
        reload();

        while (!decision) sleep(500);


    }

    public void win() throws Exception {
        Audio.play(Audio.winMusic);
        win = true;
        JLabel label = new JLabel("YOU WON!");
        label.setForeground(Color.white);
        JButton restart = new JButton("Restart");
        restart.addActionListener(e -> {
            gameContinues = true;
            decision = true;
            win = true;
            Audio.play(Audio.buttonClick);

        });
        JButton exit = new JButton("Exit");
        exit.addActionListener(e -> {
            gameContinues = false;
            decision = true;
            win = true;
            Audio.play(Audio.buttonClick);

        });
        label.setBounds(175, 100, 200, 50);
        restart.setBounds(110, 151, 100, 50);
        exit.setBounds(210, 151, 100, 50);
        panel.add(label);
        panel.add(restart);
        panel.add(exit);
        reload();
        while (!decision) {
            sleep(500);
        }

        for (Ball ball : balls) {
            ball.ballOut = true;
        }


    }

    public static void main(String[] args) throws Exception {

        Audio.loadFiles();

        gameWindow = new Window();

        while (gameMode == null) {
            sleep(500);

        }

        gameWindow.dispose();
        setBar(gameMode);

        while (gameContinues) {
            gameWindow = new Window(gameMode);
            Ball ball = new Ball(null);
            gameWindow.balls.add(ball);
            gameWindow.panel.add(ball);

            reload();
            Thread ballThread = new Thread(ball);
            if (!gameStarted) {
                JButton start = new JButton("Start");
                start.addActionListener(e -> {
                    gameStarted = true;
                    Audio.play(Audio.buttonClick);

                });

                start.setBounds(160, 160, 100, 50);
                gameWindow.panel.add(start);
                JLabel controls = new JLabel("MOUSE1 for extra ball (x" + gameWindow.panel.extraBallLeft
                        + "), MOUSE2 for large bar for " + gameWindow.panel.barBuffTime + " seconds (x1)");
                controls.setForeground(Color.white);
                controls.setBounds(7, 210, 500, 100);
                gameWindow.panel.add(controls);
                reload();

                while (!gameStarted) {
                    try {
                        sleep(50);
                    } catch (Exception ignored) {

                    }
                }

                gameWindow.panel.remove(start);
                gameWindow.panel.remove(controls);
                reload();

            }
            try {
                Audio.play(Audio.gameStart);
                sleep(1000);
            } catch (Exception ignored) {

            }
            ballThread.start();

            while (true) {
                try {
                    sleep(500);
                } catch (Exception ignored) {

                }

                if (win || lose) {
                    win = false;
                    lose = false;
                    while (!decision) {
                        try {
                            sleep(500);
                        } catch (Exception ignored) {

                        }
                    }
                    gameWindow.dispose();
                    decision = false;
                    break;
                }

            }
        }

        System.exit(0);

    }

}
