import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
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

    int xSize = 500;
    int ySize = 500;

    boolean pressed = false;

    int thickness = 1;
    int corners = 3;
    int stampRadius = 50;
    Color color = Color.BLACK;

    private DrawTypes mode = DrawTypes.CURSOR;

    public DrawPanel() {
        setLayout(new BorderLayout());
        image = new BufferedImage(xSize, ySize, BufferedImage.TYPE_3BYTE_BGR);
        imageGraphics2D = (Graphics2D) image.getGraphics();
        imageGraphics2D.setBackground(Color.white);
        imageGraphics2D.clearRect(0, 0, xSize, ySize);

        imageGraphics2D.setColor(color);

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
            canvasGraphics2D.setColor(color);
            if (thickness > 1) {
                canvasGraphics2D.drawLine(xPressed, yPressed, cursorX, cursorY);
            } else if (thickness == 1) {
                BrezLine.line(canvasGraphics2D, color, xPressed, yPressed, cursorX, cursorY);
            }
        } else if (mode == DrawTypes.STAMP) {
            int x[] = new int [corners];
            int y[] = new int [corners];
            double a, b, z = 0;
            int i = 0;
            double angle = 360.0 / corners;
            double R = stampRadius;
            while(i < corners){
                b = Math.sin( z/180*Math.PI);
                a = Math.cos( z/180*Math.PI);
                x[i] = cursorX + (int)(a * R);
                y[i] = cursorY - (int)(b * R);
                z = z + angle;
                i++;
            }
            int x1, x2, y1, y2;
            int j = corners-1;
            canvasGraphics2D.setColor(color);
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
        } else if (mode == DrawTypes.STAR_STAMP) {
            int x1[] = new int [corners];
            int y1[] = new int [corners];
            int x2[] = new int [corners];
            int y2[] = new int [corners];
            double a1, b1, a2, b2, z = 0;
            int i = 0;
            double angle = 360.0 / corners;
            double R = stampRadius;
            double r = stampRadius / 3;
            while(i < corners){
                a1 = Math.cos( z/180 * Math.PI);
                b1 = Math.sin( z/180 * Math.PI);
                a2 = Math.cos( (z + (360 / (2 * corners)))/180 * Math.PI);
                b2 = Math.sin( (z + (360 / (2 * corners)))/180 * Math.PI);
                x1[i] = cursorX + (int)(a1 * R);
                y1[i] = cursorY - (int)(b1 * R);
                x2[i] = cursorX + (int)(a2 * r);
                y2[i] = cursorY - (int)(b2 * r);
                z = z + angle;
                i++;
            }
            int x11, x22, y11, y22, x33, y33;
            int j = corners-1;
            canvasGraphics2D.setColor(color);
            canvasGraphics2D.setStroke(new BasicStroke(thickness));
            int k = 0;
            while(j >= 0){
                if((j >= 1) && (k < corners - 1)){
                    x11 = x1[j]; x22 = x2[j]; x33 = x2[j-1];
                    y11 = y1[j]; y22 = y2[j]; y33 = y2[j-1];
                    canvasGraphics2D.drawLine(x11, y11, x22, y22);
                    canvasGraphics2D.drawLine(x11, y11, x33, y33);
                    j--;
                    k++;
                }
                else{
                    x11 = x1[j]; x22 = x2[corners-1]; x33 = x2[0];
                    y11 = y1[j]; y22 = y2[corners-1]; y33 = y2[0];
                    canvasGraphics2D.drawLine(x11, y11, x22, y22);
                    canvasGraphics2D.drawLine(x11, y11, x33, y33);
                    j--;
                    k++;
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
                    imageGraphics2D.setColor(color);
                    if (thickness > 1) {
                        imageGraphics2D.drawLine(xPressed, yPressed, e.getX(), e.getY());
                    } else if (thickness == 1) {
                        BrezLine.line(imageGraphics2D, color, xPressed, yPressed, e.getX(), e.getY());
                    }
                    repaint();
                } else if (mode == DrawTypes.STAMP) {
                    int x[] = new int [corners];
                    int y[] = new int [corners];
                    double a, b, z = 0;
                    int i = 0;
                    double angle = 360.0 / corners;
                    double R = stampRadius;
                    while(i < corners){
                        a = Math.cos(z / 180 * Math.PI);
                        b = Math.sin(z / 180 * Math.PI);
                        x[i] = xPressed + (int)(a * R);
                        y[i] = yPressed - (int)(b * R);
                        z = z + angle;
                        i++;
                    }
                    int x1, x2, y1, y2;
                    int j = corners-1;
                    imageGraphics2D.setColor(color);
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
                } else if (mode == DrawTypes.STAR_STAMP) {
                    int x1[] = new int [corners];
                    int y1[] = new int [corners];
                    int x2[] = new int [corners];
                    int y2[] = new int [corners];
                    double a1, b1, a2, b2, z = 0;
                    int i = 0;
                    double angle = 360.0 / corners;
                    double R = stampRadius;
                    double r = stampRadius / 3;
                    while(i < corners){
                        a1 = Math.cos( z/180 * Math.PI);
                        b1 = Math.sin( z/180 * Math.PI);
                        a2 = Math.cos( (z + (360 / (2 * corners)))/180 * Math.PI);
                        b2 = Math.sin( (z + (360 / (2 * corners)))/180 * Math.PI);
                        x1[i] = xPressed + (int)(a1 * R);
                        y1[i] = yPressed - (int)(b1 * R);
                        x2[i] = xPressed + (int)(a2 * r);
                        y2[i] = yPressed - (int)(b2 * r);
                        z = z + angle;
                        i++;
                    }
                    int x11, x22, y11, y22, x33, y33;
                    int j = corners-1;
                    imageGraphics2D.setColor(color);
                    imageGraphics2D.setStroke(new BasicStroke(thickness));
                    int k = 0;
                    while(j >= 0){
                        if((j >= 1) && (k < corners - 1)){
                            x11 = x1[j]; x22 = x2[j]; x33 = x2[j-1];
                            y11 = y1[j]; y22 = y2[j]; y33 = y2[j-1];
                            imageGraphics2D.drawLine(x11, y11, x22, y22);
                            imageGraphics2D.drawLine(x11, y11, x33, y33);
                            j--;
                            k++;
                        }
                        else{
                            x11 = x1[j]; x22 = x2[corners-1]; x33 = x2[0];
                            y11 = y1[j]; y22 = y2[corners-1]; y33 = y2[0];
                            imageGraphics2D.drawLine(x11, y11, x22, y22);
                            imageGraphics2D.drawLine(x11, y11, x33, y33);
                            j--;
                            k++;
                        }
                    }
                    repaint();
                } else if (mode == DrawTypes.SPAN) {
                    Span span = new Span(image, color, xSize, ySize);
                    span.spanFill(xPressed, yPressed);
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
                if ((mode == DrawTypes.STAMP) || (mode == DrawTypes.STAR_STAMP)) {
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

        preferencesFrame.setVisible(true);
        mode = DrawTypes.STAMP;
    }

    public void cursor() {
        mode = DrawTypes.CURSOR;
    }

    public void colorChooser() {
        JFrame colorChooserFrame = new JFrame("Color chooser");
        colorChooserFrame.setSize(600, 400);
        JColorChooser colorChooser = new JColorChooser();
        JButton chooseButton = new JButton("Choose color");
        colorChooserFrame.add(colorChooser, BorderLayout.CENTER);
        colorChooserFrame.add(chooseButton, BorderLayout.SOUTH);
        colorChooserFrame.setVisible(true);

        chooseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                color = colorChooser.getColor();
            }
        });

    }

    public void starStamp() {
        stamp();
        mode = DrawTypes.STAR_STAMP;
    }

    public void clearPanel() {
        if (preferencesFrame != null) {preferencesFrame.setVisible(false);}
        mode = DrawTypes.CURSOR;
        imageGraphics2D.clearRect(0, 0, xSize, ySize);
        repaint();
    }

    public void fill() {
        if (preferencesFrame != null) {preferencesFrame.setVisible(false);}
        mode = DrawTypes.SPAN;
    }

    public void reSize() {
        if (preferencesFrame != null) {preferencesFrame.setVisible(false);}
        mode = DrawTypes.CURSOR;
        preferencesFrame = new JFrame("Preferences");
        preferencesFrame.setLayout(null);
        preferencesFrame.setSize(200, 140);
        //preferencesFrame.setResizable(false);

        JLabel xFieldLabel = new JLabel("New size:");
        xFieldLabel.setBounds(70, 10, 100, 20);
        JTextField xField = new JTextField(4);
        xField.setBounds(35, 35, 50, 20);

        JLabel yFieldLabel = new JLabel("x");
        yFieldLabel.setBounds(95, 35, 10, 10);
        JTextField yField = new JTextField(4);
        yField.setBounds(110, 35, 50, 20);

        JButton change = new JButton("Resize");
        change.setBounds(55, 65, 80, 30);

        change.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                xSize = Integer.parseInt(xField.getText());
                ySize = Integer.parseInt(yField.getText());

                var oldImage = image;
                image = new BufferedImage(xSize, ySize, BufferedImage.TYPE_INT_ARGB);

                imageGraphics2D = image.createGraphics();
                imageGraphics2D.setBackground(Color.white);
                imageGraphics2D.clearRect(0, 0, xSize, ySize);
                imageGraphics2D.drawImage(oldImage, 0, 0, null);
                imageGraphics2D.dispose();
                repaint();
            }
        });

        preferencesFrame.add(xFieldLabel);
        preferencesFrame.add(xField);
        preferencesFrame.add(yFieldLabel);
        preferencesFrame.add(yField);
        preferencesFrame.add(change);

        preferencesFrame.setVisible(true);


    }
}
