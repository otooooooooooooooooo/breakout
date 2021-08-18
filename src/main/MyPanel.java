package main;

import java.awt.Color;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class MyPanel extends JPanel implements MouseMotionListener, MouseListener, Runnable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    JLabel bar;
    public int barPosRight; // mostright X of bar
    public final int barPosY = 365;
    public int barPosLeft; // mostleft X of bar
    public int barCenterX;
    public int barWidth = SMALL_BAR;
    public int extraBallLeft = 3;
    public boolean barBuff = true;
    public static int SMALL_BAR = 120;
    public static int BIG_BAR = 200;
    public int barBuffTime = 5;

    public MyPanel() {
        setLayout(null);
        setBackground(Color.black);
        ImageIcon barImg = null;
        try {

            barImg = new ImageIcon(ImageIO.read(new File("src/resources/images/excel-leggings-pastel-pink-3863200989301_grande.jpg")));
        } catch (Exception e) {
            System.out.println("Couldn't load bar image");
        }
        assert barImg != null;
        Image scaledBar = barImg.getImage().getScaledInstance(barWidth, 100, Image.SCALE_SMOOTH);
        barImg = new ImageIcon(scaledBar);
        bar = new JLabel(barImg);
        barPosLeft = 100;
        bar.setBounds(barPosLeft, barPosY, barWidth, 100);
        barPosRight = barPosLeft + barWidth;
        barCenterX = (barPosLeft + barPosRight) / 2;
        add(bar);
        this.addMouseMotionListener(this);
        this.addMouseListener(this);
    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        try {

            if (e.getX() - barWidth / 2 < 0 || e.getX() + barWidth / 2 > this.getWidth()) {
                return;
            }
            barPosRight = e.getX() + barWidth / 2;
            barPosLeft = e.getX() - barWidth / 2;
            barCenterX = (barPosLeft + barPosRight) / 2;
            bar.setBounds(barPosLeft, barPosY, barWidth, 100);
        } catch (Exception ignored) {

        }

    }

    @Override
    public void mouseClicked(MouseEvent e) {

        try {
            if (!Window.gameStarted) {
                return;
            }
            if (e.getButton() == 1) {
                if (Window.gameWindow.balls.isEmpty() || extraBallLeft <= 0)
                    return;
                Ball ball2 = new Ball(Window.gameWindow.balls.get(0));
                Window.gameWindow.balls.add(ball2);
                Window.gameWindow.panel.add(ball2);
                Audio.play(Audio.newBall);
                Thread s1 = new Thread(ball2);
                s1.start();
                extraBallLeft--;
            } else if (e.getButton() == 3 && barBuff) {
                barBuff = false;
                createBar(BIG_BAR);
                Audio.play(Audio.growBar);
                Thread s1 = new Thread(this);
                s1.start();

            }
        } catch (Exception ignored) {

        }

    }

    public void createBar(int size) {
        ImageIcon barImg = null;
        try {

            barImg = new ImageIcon(ImageIO.read(new File("src/resources/images/excel-leggings-pastel-pink-3863200989301_grande.jpg")));
        } catch (Exception k) {
            System.out.println("Couldn't load bar image");
        }
        barWidth = size;
        Image scaledBar;
        assert barImg != null;
        scaledBar = barImg.getImage().getScaledInstance(size, 100, Image.SCALE_SMOOTH);
        barImg = new ImageIcon(scaledBar);
        JLabel tempBar = new JLabel(barImg);
        if (size == BIG_BAR) {
            add(tempBar);
            remove(bar);
        }

        if (size == SMALL_BAR) {
            remove(bar);
            Window.reload();
            add(tempBar);
        }

        bar = tempBar;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseExited(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void run() {
        try {
            Thread.sleep(barBuffTime * 1000);
        } catch (Exception ignored) {

        }
        createBar(SMALL_BAR);
        Audio.play(Audio.decreaseBar);

    }

}
