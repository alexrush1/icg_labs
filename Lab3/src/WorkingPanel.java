import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

public class WorkingPanel extends JPanel {

    BufferedImage image;
    JFrame board;
    Graphics2D graphics2D;
    Preferences preferences;
    Core core;

    AffineTransformOp AffineTransformOp;


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        graphics2D = (Graphics2D) g;

        requestFocus();

        AffineTransformOp = new AffineTransformOp(AffineTransform.getScaleInstance(
                (board.getWidth() - 10) / (double) image.getWidth(),
                (board.getHeight() - 150) / (double) image.getHeight()
        ), AffineTransformOp.TYPE_BILINEAR);
        graphics2D.drawImage(image, AffineTransformOp, 0, 0);

    }

    public WorkingPanel(JFrame board, Preferences preferences, double xKoef, double yKoef) {
        this.board = board;
        this.preferences = preferences;
        core = new Core(preferences);
        setLayout(new BorderLayout());
        image = new BufferedImage(640, 400, BufferedImage.TYPE_3BYTE_BGR);

        graphics2D = (Graphics2D) image.getGraphics();
        graphics2D.setBackground(Color.white);
        graphics2D.clearRect(0, 0, 640, 400);
        drawGrid();
        core.calcGridPoints(640, 400, xKoef, yKoef);

        setFocusable(true);
        //setVisible(true);
    }

    protected void drawGrid() {
        var interval = image.getWidth() / (double) preferences.N;
        graphics2D.setColor(Color.GREEN);
        for (int i = 1; i <= preferences.N; i++) {
            graphics2D.drawLine((int)(interval * i), 0, (int)(interval * i), image.getHeight());
        }

        interval = image.getHeight() / (double) preferences.M;
        graphics2D.setColor(Color.GREEN);
        for (int i = 1; i <= preferences.M; i++) {
            graphics2D.drawLine(0, (int)(interval * i), image.getWidth(), (int)(interval * i));
        }

    }
}
