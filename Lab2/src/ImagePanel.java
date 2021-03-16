import filters.BoundDeline;
import filters.Emboss;
import filters.GrayFilter;
import filters.Inverse;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImagePanel extends JPanel {

    BufferedImage image;
    BufferedImage originalImage;

    BufferedImage tmp;


    Graphics2D imageGraphics2D;

    int xSize = 500;
    int ySize = 500;

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        requestFocus();

        imageGraphics2D = (Graphics2D) g;

        g.drawImage(image, 0, 0, null);
    }

    public static BufferedImage copyImage(BufferedImage source){
        BufferedImage b = new BufferedImage(source.getWidth(), source.getHeight(), source.getType());
        Graphics g = b.getGraphics();
        g.drawImage(source, 0, 0, null);
        g.dispose();
        return b;
    }

    public ImagePanel() {
        setLayout(new BorderLayout());
        image = new BufferedImage(xSize, ySize, BufferedImage.TYPE_3BYTE_BGR);

        imageGraphics2D = (Graphics2D) image.getGraphics();
        imageGraphics2D.setBackground(Color.white);
        imageGraphics2D.clearRect(0, 0, xSize, ySize);


        setFocusable(true);
    }

    public void save(File file) throws IOException {
        ImageIO.write(image, "png", file);
    }

    public void open(File file) throws IOException {
        image = ImageIO.read(file);
        originalImage = copyImage(image);
        xSize = image.getWidth();
        ySize = image.getHeight();
        this.setPreferredSize(new Dimension(image.getWidth(), image.getHeight()));
        imageGraphics2D = (Graphics2D) image.getGraphics();
        revalidate();
        repaint();
    }

    protected void grayFilter() {
        image = GrayFilter.process(originalImage);
        repaint();
    }

    protected void inverse() {
        image = Inverse.process(originalImage);
        repaint();
    }

    protected void boundsDeline() {
        image = BoundDeline.process(originalImage);
        repaint();
    }

    protected void emboss() {
        image = Emboss.process(originalImage);
        repaint();
    }

    protected void showOriginal() {
        if (image == originalImage) {
            System.out.println("clone!");
        }
        tmp = image;
        image = originalImage;
        repaint();
    }

    protected void showCurrent() {
        image = tmp;
        repaint();
    }
}
