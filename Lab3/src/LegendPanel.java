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

    public LegendPanel(JFrame board, Preferences preferences) {
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
        findMinMax();
        fillIntervals();
        repaint();
    }

    public void fillLegend() {
        var interval = legendImage.getWidth() / preferences.K;
        legendGraphics.setColor(Color.BLACK);
        for (int i = 1; i < preferences.K; i++) {
            legendGraphics.drawLine((int)(interval * i), 0, (int)(interval * i), 60);
        }
    }

    public void findMinMax() {
        min =  Double.MAX_VALUE;
        max = Double.MIN_VALUE;

        for (int x = 0; x < preferences.boardWidth; x++) {
            for (int y = 0; y < preferences.boardHeight; y++) {
                var value = Math.cos(x) * Math.sin(y);
                if (value < min) {
                    min = value;
                }
                if (value > max) {
                    max = value;
                }
            }
        }

        legendGraphics.drawString(String.format("%.3f", min), 0, 25);
        legendGraphics.drawString(String.format("%.3f", max), legendImage.getWidth() - 25, 25);
    }

    public void fillIntervals() {
        var interval = legendImage.getWidth() / preferences.K;
        var valueInterval = (max - min) / preferences.K;
        legendGraphics.setColor(Color.BLACK);
        for (int i = 1; i < preferences.K; i++) {
            legendGraphics.drawString(String.format("%.3f", min + valueInterval * i), interval * i, 25);
        }
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
                legendGraphics.setColor(Color.WHITE);
                legendGraphics.fillRect(0, 0, legendImage.getWidth(), legendImage.getHeight());
                repaint();
                spanIntervals();
                findMinMax();
                fillIntervals();
                repaint();
            }
        });

        colorChooserFrame.setVisible(true);

    }

}
