import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

public class WorkingPanel extends JPanel {

    BufferedImage image;
    JFrame board;
    Graphics2D graphics2D;
    Preferences preferences;
    Core core;
    boolean dynamic;
    double[] grid;

    AffineTransformOp AffineTransformOp;

    double mouseValue = 0.0;

    double xKoef;
    double yKoef;

    @Override
    protected void paintComponent(Graphics g) {

        var graphics = (Graphics2D) g;

        requestFocus();

        AffineTransformOp = new AffineTransformOp(AffineTransform.getScaleInstance(
                (board.getWidth() - 30) / (double) image.getWidth(),
                (board.getHeight() - 170) / (double) image.getHeight()
        ), AffineTransformOp.TYPE_BILINEAR);

        graphics.drawImage(image, AffineTransformOp, 0, 0);

        if (dynamic) {
            System.out.println("mouse value: " + mouseValue);
            core.lines(mouseValue, Color.BLACK, graphics);
        }


    }

    public void changeMouseValue(int mouseX, int mouseY) {
        var intervalX = 640 / (double) preferences.N;
        var intervalY = 400 / (double) preferences.M;

        int xNormalCoord = (int) (mouseX / intervalX);
        int yNormalCoord = (int) (mouseY / intervalY);

        var xCoord = xNormalCoord * intervalX * xKoef + preferences.a;
        var yCoord = yNormalCoord * intervalY * yKoef + preferences.c;

        var x = mouseX * xKoef + preferences.a;
        var y = mouseY * yKoef + preferences.c;

        System.out.println("mouseX " + xCoord + " mouseY " + yCoord);

        var upLeftValue = grid[yNormalCoord * preferences.N + xNormalCoord];
        var downLeftValue = grid[(yNormalCoord + 1) * preferences.N + xNormalCoord];
        var upRightValue = grid[yNormalCoord * preferences.N + xNormalCoord + 1];
        var downRightValue = grid[(yNormalCoord + 1) * preferences.N + 1 + xNormalCoord];

        var R1value = (xCoord + 1 - x) * downLeftValue + (x - xCoord) * downRightValue;
        var R2value = (xCoord + 1 - x) * upLeftValue + (x - xCoord) * upRightValue;

        mouseValue = (yCoord - y) * R1value + (y - yCoord + 1) * R2value;

    }

    public WorkingPanel(JFrame board, Preferences preferences, double xKoef, double yKoef) {
        this.board = board;
        this.preferences = preferences;
        this.xKoef = xKoef;
        this.yKoef = yKoef;
        image = new BufferedImage(640, 400, BufferedImage.TYPE_3BYTE_BGR);
        graphics2D = (Graphics2D) image.getGraphics();
        core = new Core(preferences, 640, 400, xKoef, yKoef, graphics2D);
        setLayout(new BorderLayout());


        graphics2D.setBackground(Color.white);
        graphics2D.clearRect(0, 0, 640, 400);
        drawGrid();
        grid = core.calcGridPoints();

        core.lines(-0.5, Color.BLACK);
        core.lines(-0.25, Color.BLUE);
        core.lines(0.25, Color.orange);
        core.lines(0.5, Color.pink);
        core.lines(0.75, Color.red);
        repaint();

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
