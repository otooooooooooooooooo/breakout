package main;

import java.awt.Image;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class Ball extends JLabel implements Runnable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    public int directionX = 1;
    public int directionY = 2;
    public int size = 20;
    public int delay = Delay.SMALL_DELAY;
    public ImageIcon ballImage;
    public int ballPosX = 100;
    public int ballPosY = 300;
    public int ballCenterX;
    public int ballCenterY;
    public boolean ballOut = false;

    public Ball(Ball ball) {
        try {
            ballImage = new ImageIcon(ImageIO.read(new File("src/resources/images/white-circle-icon-14.jpg")));
        } catch (Exception e) {
            System.out.println("Couldn't load ball image");
        }

        Image scaledImage = ballImage.getImage().getScaledInstance(size, size, Image.SCALE_SMOOTH);
        ballImage = new ImageIcon(scaledImage);
        setIcon(ballImage);

        if (ball != null) {
            delay = ball.delay;
            directionX = -ball.directionX;
            directionY = ball.directionY;
            ballPosX = ball.ballPosX;
            ballPosY = ball.ballPosY;
        }
        ballCenterX = ballPosX + ballImage.getIconWidth() / 2;
        ballCenterY = ballPosY + ballImage.getIconHeight() / 2;
        setBounds(ballPosX, ballPosY, ballImage.getIconWidth(), ballImage.getIconHeight());

    }

    @Override
    public void run() {
        while (!ballOut) {
            ballPosX += directionX;
            ballPosY += directionY;
            ballCenterX += directionX;
            ballCenterY += directionY;
            setBounds(ballPosX, ballPosY, ballImage.getIconWidth(), ballImage.getIconHeight());
            Window.gameWindow.hitBar(this);
            Window.gameWindow.hitBorder(this);
            Window.gameWindow.hitBrick(this);
            try {
                Thread.sleep(delay);
            } catch (Exception ignored) {

            }
        }
    }

}
