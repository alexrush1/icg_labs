import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

public class DrawPanel extends JPanel {

    Graphics2D imageGraphics2D;
    Graphics2D canvasGraphics2D;

    BufferedImage image;
    Frame preferencesFrame;

    int xPressed = -1;
    int yPressed = -1;

    int cursorX = -1;
    int cursorY = -1;

    boolean pressed = false;

    int thickness = 1;
    int corners = 3;
    int stampRadius = 50;

    private DrawTypes mode = DrawTypes.CURSOR;

    public DrawPanel() {
        setLayout(new BorderLayout());
        image = new BufferedImage(500, 500, BufferedImage.TYPE_3BYTE_BGR);
        imageGraphics2D = (Graphics2D) image.getGraphics();
        imageGraphics2D.setBackground(Color.white);
        imageGraphics2D.clearRect(0, 0, 500, 500);

        imageGraphics2D.setColor(Color.BLACK);

        setFocusable(true);
        createMouseListener();

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        requestFocus();

        canvasGraphics2D = (Graphics2D) g;

        g.drawImage(image, 0, 0, null);

        if(mode == DrawTypes.LINES) {
            canvasGraphics2D.setStroke(new BasicStroke(thickness));
            canvasGraphics2D.setColor(Color.BLACK);
            canvasGraphics2D.drawLine(xPressed, yPressed, cursorX, cursorY);
        } else if (mode == DrawTypes.STAMP) {
            int x[] = new int [corners];
            int y[] = new int [corners];
            double a, b, z = 0;
            int i = 0;
            double angle = 360.0 / corners;
            double R = stampRadius;
            while(i < corners){
                a = Math.cos( z/180*Math.PI);
                b = Math.sin( z/180*Math.PI);
                x[i] = cursorX + (int)(Math.round(a) * R);
                y[i] = cursorY - (int)(Math.round(b) * R);
                z = z + angle;
                i++;
            }
            int x1, x2, y1, y2;
            int j = corners-1;
            canvasGraphics2D.setColor(Color.BLACK);
            canvasGraphics2D.setStroke(new BasicStroke(thickness));
            while(j >= 0){
                if(j > 0){
                    x1 = x[j]; x2 = x[j-1];
                    y1 = y[j]; y2 = y[j-1];
                    canvasGraphics2D.drawLine(x1, y1, x2, y2);
                    j--;
                }
                else{
                    x1 = x[j]; x2 = x[corners-1];
                    y1 = y[j]; y2 = y[corners-1];
                    canvasGraphics2D.drawLine(x1, y1, x2, y2);
                    j--;
                }
            }
        }
    }

    public void createMouseListener() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                xPressed = e.getX();
                yPressed = e.getY();
                pressed = true;
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                if (mode == DrawTypes.LINES) {
                    imageGraphics2D.drawLine(xPressed, yPressed, e.getX(), e.getY());
                    repaint();
                } else if (mode == DrawTypes.STAMP) {
                    int x[] = new int [corners];
                    int y[] = new int [corners];
                    double a, b, z = 0;
                    int i = 0;
                    double angle = 360.0 / corners;
                    double R = stampRadius;
                    while(i < corners){
                        a = Math.cos( z/180*Math.PI);
                        b = Math.sin( z/180*Math.PI);
                        x[i] = xPressed + (int)(Math.round(a) * R);
                        y[i] = yPressed - (int)(Math.round(b) * R);
                        z = z + angle;
                        i++;
                    }
                    int x1, x2, y1, y2;
                    int j = corners-1;
                    imageGraphics2D.setStroke(new BasicStroke(thickness));
                    while(j >= 0){
                        if(j > 0){
                            x1 = x[j]; x2 = x[j-1];
                            y1 = y[j]; y2 = y[j-1];
                            imageGraphics2D.drawLine(x1, y1, x2, y2);
                            j--;
                        }
                        else{
                            x1 = x[j]; x2 = x[corners-1];
                            y1 = y[j]; y2 = y[corners-1];
                            imageGraphics2D.drawLine(x1, y1, x2, y2);
                            j--;
                        }
                    }
                    repaint();
                }
                pressed = false;
            }
        });

        addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (mode == DrawTypes.LINES) {
                    cursorX = e.getX();
                    cursorY = e.getY();
                    repaint();
                }
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                cursorX = e.getX();
                cursorY = e.getY();
                if (mode == DrawTypes.STAMP) {
                    repaint();
                }
            }
        });
    }

    public void lines() {
        mode = DrawTypes.LINES;
        if (preferencesFrame != null) {preferencesFrame.setVisible(false);}
        preferencesFrame = new JFrame("Preferences");
        preferencesFrame.setLayout(null);
        preferencesFrame.setSize(300, 120);
        preferencesFrame.setResizable(false);
        JSlider lineThickness = new JSlider(1, 15);
        lineThickness.setValue(thickness);
        JLabel lineThicknessLabel = new JLabel("Thickness");
        lineThicknessLabel.setBounds(15, 15, 100, 20);
        lineThickness.setBounds(80, 17, 160, 20);

        JLabel currentThickness = new JLabel(String.valueOf(thickness));

        lineThickness.addChangeListener(e -> {
            thickness = ((JSlider)e.getSource()).getValue();
            currentThickness.setText(String.valueOf(thickness));
            imageGraphics2D.setStroke(new BasicStroke(thickness));
        });
        currentThickness.setBounds(250, 15, 30, 20);
        preferencesFrame.add(currentThickness);
        preferencesFrame.add(lineThicknessLabel);
        preferencesFrame.add(lineThickness, BorderLayout.AFTER_LAST_LINE);
        preferencesFrame.setLocationRelativeTo(null);
        preferencesFrame.setVisible(true);
    }

    public void stamp() {
        if (preferencesFrame != null) {preferencesFrame.setVisible(false);}
        preferencesFrame = new JFrame("Preferences");
        preferencesFrame.setLayout(null);
        preferencesFrame.setSize(300, 200);
        preferencesFrame.setResizable(false);
        JSlider lineThickness = new JSlider(1, 15);
        lineThickness.setValue(thickness);
        JLabel lineThicknessLabel = new JLabel("Thickness");
        lineThicknessLabel.setBounds(15, 15, 100, 20);
        lineThickness.setBounds(80, 17, 160, 20);

        JSlider cornersCount = new JSlider(3, 10);
        cornersCount.setValue(corners);
        JLabel cornersCountLabel = new JLabel("Corners");
        cornersCountLabel.setBounds(15, 65, 100, 20);
        cornersCount.setBounds(80, 67, 160, 20);

        JLabel currentThickness = new JLabel(String.valueOf(thickness));
        JLabel currentCorners = new JLabel(String.valueOf(corners));
        JLabel currentRadius = new JLabel(String.valueOf(stampRadius));

        JSlider stampRadiusSlider = new JSlider(50, 500);
        stampRadiusSlider.setValue(stampRadius);
        JLabel stampRadiusLabel = new JLabel("Radius");
        stampRadiusLabel.setBounds(15, 115, 100, 20);
        stampRadiusSlider.setBounds(80, 117, 160, 20);

        lineThickness.addChangeListener(e -> {
            thickness = ((JSlider)e.getSource()).getValue();
            currentThickness.setText(String.valueOf(thickness));
            imageGraphics2D.setStroke(new BasicStroke(thickness));
        });

        cornersCount.addChangeListener(e -> {
            corners = ((JSlider)e.getSource()).getValue();
            currentCorners.setText(String.valueOf(corners));
            imageGraphics2D.setStroke(new BasicStroke(corners));
        });

        stampRadiusSlider.addChangeListener(e -> {
            stampRadius = ((JSlider)e.getSource()).getValue();
            currentRadius.setText(String.valueOf(stampRadius));
            imageGraphics2D.setStroke(new BasicStroke(stampRadius));
        });

        currentThickness.setBounds(250, 15, 30, 20);
        currentCorners.setBounds(250, 65, 30, 20);
        currentRadius.setBounds(250, 115, 30, 20);

        preferencesFrame.add(currentThickness);
        preferencesFrame.add(lineThicknessLabel);
        preferencesFrame.add(lineThickness);

        preferencesFrame.add(currentCorners);
        preferencesFrame.add(cornersCountLabel);
        preferencesFrame.add(cornersCount);

        preferencesFrame.add(currentRadius);
        preferencesFrame.add(stampRadiusLabel);
        preferencesFrame.add(stampRadiusSlider);

        preferencesFrame.setLocationRelativeTo(null);


        preferencesFrame.setVisible(true);
        mode = DrawTypes.STAMP;
    }

    public void cursor() {
        mode = DrawTypes.CURSOR;
    }
}
