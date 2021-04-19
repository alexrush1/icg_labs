import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

public class LegendPanel extends JPanel {

    JFrame board;
    AffineTransformOp AffineTransformOp;
    Preferences preferences;
    BufferedImage legendImage;
    Graphics2D legendGraphics;
    double min;
    double max;
    WorkingPanel workingPanel;

    boolean gradientMode;

    @Override
    protected void paintComponent(Graphics g) {
        legendGraphics = (Graphics2D) g;
        requestFocus();

//        AffineTransformOp = new AffineTransformOp(AffineTransform.getScaleInstance(
//                (board.getWidth() - 20) / (double) preferences.boardWidth,
//                (board.getHeight() - 180) / (double) preferences.boardHeight
//        ), AffineTransformOp.TYPE_BILINEAR);

        legendGraphics.drawImage(legendImage, AffineTransformOp, 0, 0);
    }

    public LegendPanel(JFrame board, Preferences preferences, WorkingPanel workingPanel) {
        this.workingPanel = workingPanel;
        min = preferences.intervals.get(0);
        max = preferences.intervals.get(preferences.intervals.size() - 1);
        this.board = board;
        this.preferences = preferences;
        legendImage = new BufferedImage(600, 60, BufferedImage.TYPE_INT_RGB);
        legendGraphics = (Graphics2D) legendImage.getGraphics();
        legendGraphics.setColor(Color.WHITE);
        legendGraphics.fillRect(0, 0, legendImage.getWidth(), legendImage.getHeight());
        repaint();
        setLayout(new BorderLayout());
        fillLegend();
        spanIntervals();
        drawIntervals();
        findMinMax();
        repaint();
        if (gradientMode) {
            turnGradient();
        }
        workingPanel.paint();
    }

    public void fillLegend() {
        var interval = legendImage.getWidth() / preferences.K;
        legendGraphics.setColor(Color.BLACK);
        for (int i = 1; i < preferences.K; i++) {
            legendGraphics.drawLine((int)(interval * i), 0, (int)(interval * i), 60);
        }
    }

    public void drawIntervals() {
        var interval = legendImage.getWidth() / preferences.K;
        legendGraphics.setColor(Color.BLACK);
        for (int i = 1; i < preferences.K; i++) {
            legendGraphics.drawString(String.format("%.3f", preferences.intervals.get(i)), (int) (interval * i), 25);
        }
    }

    public void findMinMax() {
        legendGraphics.drawString(String.format("%.3f", min), 0, 25);
        legendGraphics.drawString(String.format("%.3f", max), legendImage.getWidth() - 25, 25);
    }


    public void spanIntervals() {
        var interval = legendImage.getWidth() / preferences.K;
        var count = 0;
        for (var color: preferences.colors) {
            Span span = new Span(legendImage, color, legendImage.getWidth(), legendImage.getHeight());
            span.spanFill(interval * count + 5, 5);
            count++;
        }

    }

    Color newColor;
    public void changeColor(int mouseX, int mouseY) {
        JFrame colorChooserFrame = new JFrame("Pick color!");
        colorChooserFrame.setLocationRelativeTo(null);
        colorChooserFrame.setSize(600, 400);
        JButton chooseButton = new JButton("Choose color");
        System.out.println("click!");
        var interval = legendImage.getWidth() / preferences.K;
        int colorNumber = mouseX / interval;

        newColor = Color.WHITE;

        JColorChooser colorChooser = new JColorChooser();
        colorChooserFrame.add(colorChooser, BorderLayout.CENTER);
        colorChooserFrame.add(chooseButton, BorderLayout.SOUTH);

        chooseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                newColor = colorChooser.getColor();
                preferences.colors.set(colorNumber, newColor);
                colorChooserFrame.setVisible(false);
                paint();
                workingPanel.paint();
            }
        });

        colorChooserFrame.setVisible(true);

    }

    private static int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }

    public void paint() {
        legendImage = new BufferedImage(600, 60, BufferedImage.TYPE_INT_RGB);
        legendGraphics = (Graphics2D) legendImage.getGraphics();
        legendGraphics.setColor(Color.WHITE);
        legendGraphics.fillRect(0, 0, legendImage.getWidth(), legendImage.getHeight());
        repaint();
        fillLegend();
        spanIntervals();
        drawIntervals();
        if (gradientMode) {
            turnGradient();
        }
        findMinMax();
        repaint();
    }

    public void turnGradient() {
        var interval = legendImage.getWidth() / (preferences.K - 1);
        BufferedImage newImage = new BufferedImage(600, 60, BufferedImage.TYPE_INT_RGB);
        System.out.println(interval);
        int r1, r2, g1, g2, b1, b2;
        r1 = legendImage.getRGB(1, 5)>>16&0xFF;
        g1 = legendImage.getRGB(1, 5)>>8&0xFF;
        b1 = legendImage.getRGB(1, 5)&0xFF;
        for (int x = 0; x < preferences.K - 1; x++) {
            r2 = legendImage.getRGB((interval * (x + 1)) - 1, 5)>>16&0xFF;
            g2 = legendImage.getRGB((interval * (x + 1)) - 1, 5)>>8&0xFF;
            b2 = legendImage.getRGB((interval * (x + 1)) - 1, 5)&0xFF;


            System.out.println((interval * x + 1) + "  " + ((interval * (x + 1)) - 1));

            for (int i = interval * x; i < interval * (x + 1); i++) {
                double perc = (i % interval) / (double)interval;
                var rs = r1 + ((r2 - r1) * perc);
                var gs = g1 + ((g2 - g1) * perc);
                var bs = b1 + ((b2 - b1) * perc);


                for (int j = 0; j < legendImage.getHeight(); j++) {
                    newImage.setRGB(i, j, (new Color((int)rs, (int)gs, (int)bs)).getRGB());
                }
            }
            r1 = r2;
            g1 = g2;
            b1 = b2;
        }
        legendImage = newImage;
        repaint();
    }

}
