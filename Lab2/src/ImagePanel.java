import Utils.Avg;
import filters.*;
import filters.GrayFilter;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImagePanel extends JPanel {

    BufferedImage image;
    BufferedImage originalImage;

    BufferedImage tmp;

    JFrame board;

    Graphics2D imageGraphics2D;

    int xSize = 550;
    int ySize = 550;
    private AffineTransformOp AffineTransformOp;

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        requestFocus();

        imageGraphics2D = (Graphics2D) g;

        double resolution = image.getWidth() / image.getHeight();
        if (interpolation == 1) {
            AffineTransformOp = new AffineTransformOp(AffineTransform.getScaleInstance(
                    (board.getWidth() - 75) / (double) image.getWidth(),
                    (board.getWidth() - 75) / (double) image.getWidth() / resolution
            ), AffineTransformOp.TYPE_BILINEAR);
            imageGraphics2D.drawImage(image, AffineTransformOp, 0, 0);
        } else if (interpolation == 2) {
            AffineTransformOp = new AffineTransformOp(AffineTransform.getScaleInstance(
                    (board.getWidth() - 75) / (double) image.getWidth(),
                    (board.getWidth() - 75) / (double) image.getWidth() / resolution
            ), AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
            imageGraphics2D.drawImage(image, AffineTransformOp, 0, 0);
        } else if (interpolation == 3) {
            AffineTransformOp = new AffineTransformOp(AffineTransform.getScaleInstance(
                    (board.getWidth() - 75) / (double) image.getWidth(),
                    (board.getWidth() - 75) / (double) image.getWidth() / resolution
            ), AffineTransformOp.TYPE_BICUBIC);
            imageGraphics2D.drawImage(image, AffineTransformOp, 0, 0);
        } else {
            g.drawImage(image, 0, 0, null);
        }
    }

    public static BufferedImage copyImage(BufferedImage source){
        BufferedImage b = new BufferedImage(source.getWidth(), source.getHeight(), source.getType());
        Graphics g = b.getGraphics();
        g.drawImage(source, 0, 0, null);
        g.dispose();
        return b;
    }

    public ImagePanel(JFrame board) {
        this.board = board;
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
        board.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        image = GrayFilter.process(originalImage);
        repaint();
        board.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }

    protected void inverse() {
        board.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        image = Inverse.process(originalImage);
        repaint();
        board.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }

    protected void boundsDeline() {
        board.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        image = BoundDeline.process(originalImage);
        repaint();
        board.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }

    protected void emboss() {
        board.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        image = Emboss.process(originalImage);
        repaint();
        board.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }

    int floydDots = 2;
    protected void floyd() {
        JDialog preferencesFrame = new JDialog(board, true);
        preferencesFrame.setTitle("Preferences");
        preferencesFrame.setSize(300, 150);
        preferencesFrame.setLocationRelativeTo(null);
        preferencesFrame.setLayout(null);
        preferencesFrame.setTitle("Preferences");
        preferencesFrame.setResizable(false);
        JSlider floydDotsSlider = new JSlider(2, 128);
        floydDotsSlider.setValue(floydDots);
        JLabel floydDotsLabel = new JLabel("Dots");
        floydDotsLabel.setBounds(15, 15, 100, 20);
        floydDotsSlider.setBounds(80, 17, 160, 20);

        JTextField currentFloydDots = new JTextField(String.valueOf(floydDots));
        currentFloydDots.setBounds(250, 15, 30, 20);

        floydDotsSlider.addChangeListener(e -> {
            floydDots = floydDotsSlider.getValue();
            currentFloydDots.setText(String.valueOf(floydDots));
        });

        currentFloydDots.addActionListener(e -> {
            if (Character.isLetter(currentFloydDots.getText().charAt(0))) {
                JOptionPane.showMessageDialog(this, "Letters are NOT NUMBERS!!!");
                currentFloydDots.setText(String.valueOf(floydDots));
                return;
            }
            var newValue = Integer.parseInt(currentFloydDots.getText());
            if (newValue >= 2 && newValue <= 128) {
                floydDots = newValue;
                floydDotsSlider.setValue(floydDots);
            } else {
                JOptionPane.showMessageDialog(this, "Dots only >=2 && <=128");
            }
        });

        JButton processButton = new JButton("Emboss");
        processButton.addActionListener(e -> {preferencesFrame.setVisible(false); floydWorker(floydDots);});
        processButton.setBounds(120, 75, 60, 15);

        preferencesFrame.add(processButton);
        preferencesFrame.add(floydDotsLabel);
        preferencesFrame.add(floydDotsSlider);
        preferencesFrame.add(currentFloydDots);
        preferencesFrame.setVisible(true);
    }

    public void floydWorker(int dots) {
        board.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        image = copyImage(originalImage);
        Floyd.process(image, dots);
        repaint();
        board.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }

    protected void gauss3() {
        board.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        image = Gauss3.process(originalImage);
        repaint();
        board.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }

    protected void gauss5() {
        board.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        image = Gauss5.process(originalImage);
        repaint();
        board.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }

    protected void sharpness() {
        board.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        image = Sharpness.process(originalImage);
        repaint();
        board.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }

    protected void aquarel() {
        board.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        image = Sharpness.process(Median.process(originalImage));
        repaint();
        board.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }

    double gamma = 0.1;
    protected void gamma() {
        JDialog preferencesFrame = new JDialog(board, true);
        preferencesFrame.setTitle("Preferences");
        preferencesFrame.setSize(200, 150);
        preferencesFrame.setLocationRelativeTo(null);
        preferencesFrame.setLayout(null);
        preferencesFrame.setResizable(false);


        JLabel gammaValueLabel = new JLabel("Gamma value");
        gammaValueLabel.setBounds(15, 15, 100, 20);

        JTextField currentGammaValue = new JTextField(String.valueOf(gamma));
        currentGammaValue.setBounds(115, 15, 30, 20);

        currentGammaValue.addActionListener(e -> {
            if (Character.isLetter(currentGammaValue.getText().charAt(0))) {
                JOptionPane.showMessageDialog(this, "Letters are NOT NUMBERS!!!");
                currentGammaValue.setText(String.valueOf(gamma));
                return;
            }
            var newValue = Double.parseDouble(currentGammaValue.getText());
            if (newValue >= 0.1 && newValue <= 10) {
                gamma = newValue;
            } else {
                JOptionPane.showMessageDialog(this, "Gamma value only >=0.1 && <=10");
            }
        });

        JButton processButton = new JButton("Gamma");
        processButton.addActionListener(e -> {preferencesFrame.setVisible(false); gammaWorker(gamma);});
        processButton.setBounds(120, 75, 60, 15);

        preferencesFrame.add(processButton);
        preferencesFrame.add(gammaValueLabel);
        preferencesFrame.add(currentGammaValue);
        preferencesFrame.setVisible(true);
    }

    private void gammaWorker(double gamma) {
        board.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        image = Gamma.process(originalImage, gamma);
        repaint();
        board.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }

    int angle = 0;
    protected void rotate() {
        JDialog preferencesFrame = new JDialog(board, true);
        preferencesFrame.setSize(200, 150);
        preferencesFrame.setLocationRelativeTo(null);
        preferencesFrame.setLayout(null);
        preferencesFrame.setTitle("Preferences");
        preferencesFrame.setResizable(false);


        JLabel angleValueLabel = new JLabel("Angle");
        angleValueLabel.setBounds(15, 15, 100, 20);

        JTextField currentAngleValue = new JTextField(String.valueOf(angle));
        currentAngleValue.setBounds(115, 15, 30, 20);

        currentAngleValue.addActionListener(e -> {
            if (Character.isLetter(currentAngleValue.getText().charAt(0))) {
                JOptionPane.showMessageDialog(this, "Letters are NOT NUMBERS!!!");
                currentAngleValue.setText(String.valueOf(angle));
                return;
            }
            var newValue = Integer.parseInt(currentAngleValue.getText());
            if (newValue >= 0 && newValue <= 360) {
                angle = newValue;
            } else {
                JOptionPane.showMessageDialog(this, "Angle value only >=0 && <=360");
            }
        });

        JButton processButton = new JButton("Angle");
        processButton.addActionListener(e -> {preferencesFrame.setVisible(false); rotateWorker(angle);});
        processButton.setBounds(120, 75, 60, 15);

        preferencesFrame.add(processButton);
        preferencesFrame.add(angleValueLabel);
        preferencesFrame.add(currentAngleValue);
        preferencesFrame.setVisible(true);
    }

    private void rotateWorker(int angle) {
        board.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        image = Rotate.process(originalImage, angle);
        repaint();
        board.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }

    protected void orderedDithering() {
        JDialog preferencesFrame = new JDialog(board, true);
        preferencesFrame.setTitle("Preferences");
        preferencesFrame.setSize(300, 150);
        preferencesFrame.setLocationRelativeTo(null);
        preferencesFrame.setLayout(null);
        preferencesFrame.setTitle("Preferences");
        preferencesFrame.setResizable(false);
        JSlider floydDotsSlider = new JSlider(2, 128);
        floydDotsSlider.setValue(floydDots);
        JLabel floydDotsLabel = new JLabel("Dots");
        floydDotsLabel.setBounds(15, 15, 100, 20);
        floydDotsSlider.setBounds(80, 17, 160, 20);

        JTextField currentFloydDots = new JTextField(String.valueOf(floydDots));
        currentFloydDots.setBounds(250, 15, 30, 20);

        floydDotsSlider.addChangeListener(e -> {
            floydDots = floydDotsSlider.getValue();
            currentFloydDots.setText(String.valueOf(floydDots));
        });

        currentFloydDots.addActionListener(e -> {
            if (Character.isLetter(currentFloydDots.getText().charAt(0))) {
                JOptionPane.showMessageDialog(this, "Letters are NOT NUMBERS!!!");
                currentFloydDots.setText(String.valueOf(floydDots));
                return;
            }
            var newValue = Integer.parseInt(currentFloydDots.getText());
            if (newValue >= 2 && newValue <= 128) {
                floydDots = newValue;
                floydDotsSlider.setValue(floydDots);
            } else {
                JOptionPane.showMessageDialog(this, "Dots only >=2 && <=128");
            }
        });

        JButton processButton = new JButton("Emboss");
        processButton.addActionListener(e -> {preferencesFrame.setVisible(false); orderedDitheringWorker(floydDots);});
        processButton.setBounds(120, 75, 60, 15);

        preferencesFrame.add(processButton);
        preferencesFrame.add(floydDotsLabel);
        preferencesFrame.add(floydDotsSlider);
        preferencesFrame.add(currentFloydDots);
        preferencesFrame.setVisible(true);
    }

    private void orderedDitheringWorker(int dots) {
        board.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        image = OrderedDithering.process(GrayFilter.process(originalImage), dots);
        repaint();
        board.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }

    protected void roberts() {
        board.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        image = Roberts.process(originalImage);
        repaint();
        board.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }

    protected void sobel() {
        board.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        image = Sobel.process(originalImage);
        repaint();
        board.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }

    int k = 2;
    protected void kmeans() {
        JDialog preferencesFrame = new JDialog(board, true);
        preferencesFrame.setTitle("Preferences");
        preferencesFrame.setSize(200, 150);
        preferencesFrame.setLocationRelativeTo(null);
        preferencesFrame.setLayout(null);
        preferencesFrame.setResizable(false);


        JLabel kValueLabel = new JLabel("K value");
        kValueLabel.setBounds(15, 15, 100, 20);

        JTextField currentKValue = new JTextField(String.valueOf(k));
        currentKValue.setBounds(115, 15, 30, 20);

        currentKValue.addActionListener(e -> {
            if (Character.isLetter(currentKValue.getText().charAt(0))) {
                JOptionPane.showMessageDialog(this, "Letters are NOT NUMBERS!!!");
                currentKValue.setText(String.valueOf(k));
                return;
            }
            var newValue = Integer.parseInt(currentKValue.getText());
            if (newValue >= 2 && newValue <= 10) {
                k = newValue;
            } else {
                JOptionPane.showMessageDialog(this, "K value only >=2 && <=10");
            }
        });

        JButton processButton = new JButton("Process");
        processButton.addActionListener(e -> { preferencesFrame.setVisible(false); kmeansWorker(k);});
        processButton.setBounds(120, 75, 60, 15);

        preferencesFrame.add(processButton);
        preferencesFrame.add(kValueLabel);
        preferencesFrame.add(currentKValue);
        preferencesFrame.setVisible(true);

    }

    private void kmeansWorker(int k) {
        board.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        KMeans kmeans = new KMeans();
        image = kmeans.calculate(originalImage, k, 1);
        repaint();
        board.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }

    int interpolation = 0;
    protected void setInterpolationOn() {
        interpolation = 1;
    }

    protected void setInterpolationBicubicOn() {
        interpolation = 3;
    }

    protected void setInterpolationNearestOn() {
        interpolation = 2;
    }

    protected void setInterpolationOff() {
        interpolation = 0;
    }

    int matrix = 5;
    protected void matrix() {
        JDialog preferencesFrame = new JDialog(board, true);
        preferencesFrame.setTitle("Preferences");
        preferencesFrame.setSize(200, 150);
        preferencesFrame.setLocationRelativeTo(null);
        preferencesFrame.setLayout(null);
        preferencesFrame.setResizable(false);


        JLabel matrixValueLabel = new JLabel("Matrix size");
        matrixValueLabel.setBounds(15, 15, 100, 20);

        JTextField currentMatrixValue = new JTextField(String.valueOf(matrix));
        currentMatrixValue.setBounds(115, 15, 30, 20);

        currentMatrixValue.addActionListener(e -> {
            if (Character.isLetter(currentMatrixValue.getText().charAt(0))) {
                JOptionPane.showMessageDialog(this, "Letters are NOT NUMBERS!!!");
                currentMatrixValue.setText(String.valueOf(matrix));
                return;
            }
            var newValue = Integer.parseInt(currentMatrixValue.getText());
            if (newValue >= 2 && newValue <= 10) {
                matrix = newValue;
            } else {
                JOptionPane.showMessageDialog(this, "Matrix value only >=2 && <=10");
            }
        });

        JButton processButton = new JButton("Process");
        processButton.addActionListener(e -> { preferencesFrame.setVisible(false); matrixWorker(matrix);});
        processButton.setBounds(120, 75, 60, 15);

        preferencesFrame.add(processButton);
        preferencesFrame.add(matrixValueLabel);
        preferencesFrame.add(currentMatrixValue);
        preferencesFrame.setVisible(true);
    }

    private void matrixWorker(int size) {
        board.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        image = Avg.process(originalImage, size);
        repaint();
        board.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }



}
