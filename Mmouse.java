import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.LayoutManager;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextPane;
import javax.swing.text.JTextComponent;

public class Mmouse {

    private final int steps;
    private static final int X_DEFAULT = 50;
    private static final int Y_DEFAULT = 50;
    private int x = X_DEFAULT;
    private int y = Y_DEFAULT;

    private AtomicBoolean stopSign = new AtomicBoolean(false);
    private Thread currentThread;

    private final JFrame frame;

    public Mmouse(int steps) {
        this.steps = steps;
        frame = new JFrame();
        setDefaultFrame();
    }

    private void setDefaultFrame(){
        frame.setSize(220, 130);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Container contentPane = frame.getContentPane();
        contentPane.setLayout(new FlowLayout());

        JButton moveButton = new JButton("Move");
        moveButton.addActionListener(e -> {
            stopSign.set(false);
            move();
            contentPane.requestFocus();
        });

        contentPane.add(moveButton);

        JButton stopButton = new JButton("Stop");
        stopButton.addActionListener(e -> {
            stopSign.set(true);
            contentPane.requestFocus();
            System.out.println("STOP!");
        });

        contentPane.add(stopButton);

        contentPane.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                // TODO Auto-generated method stub
                
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if( e.getKeyChar() == 'C' || e.getKeyChar() == 'c' ) {
                    stopSign.set(true);
                    contentPane.requestFocus();
                    System.out.println("STOP!");
                }

                if( e.getKeyChar() == 'S' || e.getKeyChar() == 's' ) {
                    stopSign.set(false);
                    move();
                    contentPane.requestFocus();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                // TODO Auto-generated method stub
                
            }
        });

        JLabel labelStart = new JLabel("To start, press 's'");
        JLabel labelStop = new JLabel("To stop, press 'c'");

        contentPane.add(labelStart);
        contentPane.add(labelStop);


        frame.setFocusable(true);
        frame.setVisible(true);
        frame.setResizable(false);

        contentPane.setFocusable(true);
        contentPane.setVisible(true);
        contentPane.requestFocus();
        
    }

    private void move(){

        // if(!stopSign.get()) return ;
        if( currentThread != null ) {
            try{
                currentThread.interrupt();
            } catch(Exception e) {
                
            }
        }

        currentThread = new Thread( () -> { 
            try {
                Robot robot = new Robot();
                robot.mouseMove(x, y);
                System.out.println("START!, Press 'C' to stop ");
                while(!stopSign.get()) {
                    x = x+steps;
                    robot.mouseMove(x, y);
                    reset();
                    Thread.sleep(500);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        currentThread.start();
    }

    private void reset() {
        if( x > 1500 || y > 1500 ){
            x = X_DEFAULT;
            y = Y_DEFAULT;
        }
    }

    public static void main(String[] args) {
        Mmouse mmouse = new Mmouse(1);
    }

}