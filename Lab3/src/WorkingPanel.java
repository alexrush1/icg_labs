import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

public class WorkingPanel extends JPanel {

    BufferedImage image;
    BufferedImage showImage;
    JFrame board;
    Graphics2D graphics2D;
    Graphics2D showGraphics2D;
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
                (board.getHeight() - 160) / (double) image.getHeight()
        ), AffineTransformOp.TYPE_BILINEAR);

        graphics.drawImage(showImage, AffineTransformOp, 0, 0);

        if (dynamic) {
            System.out.println("mouse value: " + mouseValue);
            core.lines(mouseValue, Color.BLACK, graphics);
        }


    }

    public void changeMouseValue(int mouseX, int mouseY, double x, double y) {
        var intervalX = 629 / (double) preferences.N;
        var intervalY = 425 / (double) preferences.M;

        int xNormalCoord = (int) (mouseX / intervalX);
        int yNormalCoord = (int) (mouseY / intervalY);

        var xCoord = xNormalCoord * intervalX * xKoef + preferences.a;
        var yCoord = yNormalCoord * intervalY * yKoef + preferences.c;

        System.out.println("mouseX " + x + " mouseY " + y);

        var upLeftValue = grid[yNormalCoord * preferences.N + xNormalCoord];
        var downLeftValue = grid[(yNormalCoord + 1) * preferences.N + xNormalCoord];
        var upRightValue = grid[yNormalCoord * preferences.N + xNormalCoord + 1];
        var downRightValue = grid[(yNormalCoord + 1) * preferences.N + 1 + xNormalCoord];

        var R2value = (xCoord + 1 - x) * downLeftValue + (x - xCoord) * downRightValue;
        var R1value = (xCoord + 1 - x) * upLeftValue + (x - xCoord) * upRightValue;

        mouseValue = (yCoord - y) * R1value + (y - yCoord + 1) * R2value;

    }

    public WorkingPanel(JFrame board, Preferences preferences, double xKoef, double yKoef) {
        this.board = board;
        this.preferences = preferences;
        this.xKoef = xKoef;
        this.yKoef = yKoef;
        image = new BufferedImage(629, 425, BufferedImage.TYPE_3BYTE_BGR);
        showImage = new BufferedImage(629, 425, BufferedImage.TYPE_3BYTE_BGR);
        graphics2D = (Graphics2D) image.getGraphics();
        core = new Core(preferences, 629, 425, xKoef, yKoef, graphics2D);
        setLayout(new BorderLayout());


        graphics2D.setBackground(Color.white);
        graphics2D.clearRect(0, 0, 629, 425);
        grid = core.calcGridPoints();

        core.lines(-1, Color.BLACK);
        core.lines(-0.75, Color.BLACK);
        core.lines(-0.5, Color.BLACK);
        core.lines(-0.25, Color.BLACK);
        core.lines(0.25, Color.BLACK);
        core.lines(0.5, Color.BLACK);
        core.lines(0.75, Color.BLACK);
        core.span(-1, -0.75, Color.GRAY, image);
        core.span(-0.75, -0.5, Color.red, image);
        core.span(-0.5, -0.25, Color.orange, image);
        core.span(-0.25, 0, Color.yellow, image);
        core.span(0, 0.25, Color.cyan, image);
        core.span(0.25, 0.5, Color.green, image);
        core.span(0.5, 0.75, Color.blue, image);
        core.span(0.75, 1, Color.PINK, image);

        showImage = copyImage(image);
        showGraphics2D = (Graphics2D) showImage.getGraphics();
        drawGrid();
        repaint();
        setFocusable(true);
        //setVisible(true);
    }

    public static BufferedImage copyImage(BufferedImage source){
        BufferedImage b = new BufferedImage(source.getWidth(), source.getHeight(), source.getType());
        Graphics g = b.getGraphics();
        g.drawImage(source, 0, 0, null);
        g.dispose();
        return b;
    }

    protected void drawGrid() {
        var interval = image.getWidth() / (double) preferences.N;
        showGraphics2D.setColor(Color.GREEN);
        for (int i = 1; i <= preferences.N; i++) {
            showGraphics2D.drawLine((int)(interval * i), 0, (int)(interval * i), image.getHeight());
        }

        interval = image.getHeight() / (double) preferences.M;
        graphics2D.setColor(Color.GREEN);
        for (int i = 1; i <= preferences.M; i++) {
            showGraphics2D.drawLine(0, (int)(interval * i), image.getWidth(), (int)(interval * i));
        }

    }
}
