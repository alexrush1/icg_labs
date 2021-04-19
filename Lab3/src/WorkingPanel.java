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

    public void setXKoef(double value) { xKoef = value; }
    public void setYKoef(double value) { yKoef = value; }

    @Override
    protected void paintComponent(Graphics g) {

        var graphics = (Graphics2D) g;

        //requestFocus();

//        AffineTransformOp = new AffineTransformOp(AffineTransform.getScaleInstance(
//                (board.getWidth() - 57) / (double) preferences.boardWidth,
//                (board.getHeight() - 180) / (double) preferences.boardHeight
//        ), AffineTransformOp.TYPE_BILINEAR);

        graphics.drawImage(showImage, AffineTransformOp, 0, 0);

        if (dynamic) {
            System.out.println("mouse value: " + mouseValue);
            core.lines(mouseValue, Color.BLACK, graphics);
        }


    }

    public void changeMouseValue(int mouseX, int mouseY, double x, double y) {
        var intervalX = preferences.boardWidth / (double) preferences.N;
        var intervalY = preferences.boardHeight / (double) preferences.M;

        double xNormalCoord = (mouseX / intervalX);
        double yNormalCoord = (mouseY / intervalY);

        double dotCoordX = xNormalCoord % 1;
        double dotCoordY = yNormalCoord % 1;

        var upLeftValue = grid[(int)yNormalCoord * preferences.N + (int)xNormalCoord];
        var downLeftValue = grid[((int)yNormalCoord + 1) * preferences.N + (int)xNormalCoord];
        var upRightValue = grid[(int)yNormalCoord * preferences.N + (int)xNormalCoord + 1];
        var downRightValue = grid[((int)yNormalCoord + 1) * preferences.N + 1 + (int)xNormalCoord];

        var R1 = (upRightValue - upLeftValue) * dotCoordX + upLeftValue;
        var R2 = (downRightValue - downLeftValue) * dotCoordX + downLeftValue;
        mouseValue = (R2 - R1) * dotCoordY + R1;

    }

    public WorkingPanel(JFrame board, Preferences preferences, double xKoef, double yKoef) {
        this.board = board;
        this.preferences = preferences;
        this.xKoef = xKoef;
        this.yKoef = yKoef;
        image = new BufferedImage(preferences.boardWidth, preferences.boardHeight, BufferedImage.TYPE_3BYTE_BGR);
        showImage = new BufferedImage(preferences.boardWidth, preferences.boardHeight, BufferedImage.TYPE_3BYTE_BGR);
        graphics2D = (Graphics2D) image.getGraphics();
        core = new Core(preferences, preferences.boardWidth, preferences.boardHeight, xKoef, yKoef, graphics2D);
        setLayout(new BorderLayout());


        graphics2D.setBackground(Color.white);
        graphics2D.clearRect(0, 0, 600, 400);
        grid = core.calcGridPoints();


//        core.lines(-1, Color.BLACK);
//        core.lines(-0.75, Color.BLACK);
//        core.lines(-0.5, Color.BLACK);
//        core.lines(-0.25, Color.BLACK);
//        core.lines(0.25, Color.BLACK);
//        core.lines(0.5, Color.BLACK);
//        core.lines(0.75, Color.BLACK);
//        core.span(-1, -0.75, Color.GRAY, image);
//        core.span(-0.75, -0.5, Color.red, image);
//        core.span(-0.5, -0.25, Color.orange, image);
//        core.span(-0.25, 0, Color.yellow, image);
//        core.span(0, 0.25, Color.cyan, image);
//        core.span(0.25, 0.5, Color.green, image);
//        core.span(0.5, 0.75, Color.blue, image);
//        core.span(0.75, 1, Color.PINK, image);

        repaint();
        setFocusable(true);
        //setVisible(true);
    }

    public void paint() {
        for (int i = 1; i < preferences.K; i++) {
            core.lines(preferences.intervals.get(i), preferences.isolinesColor);
            System.out.println("Line value - " + preferences.intervals.get(i));
        }
        for (int i = 0; i < preferences.K; i++) {
            core.span(preferences.intervals.get(i), preferences.intervals.get(i + 1), preferences.colors.get(i), image);
        }
        showImage = copyImage(image);
        showGraphics2D = (Graphics2D) showImage.getGraphics();
        drawGrid();
        repaint();
    }

    public static BufferedImage copyImage(BufferedImage source){
        BufferedImage b = new BufferedImage(source.getWidth(), source.getHeight(), source.getType());
        Graphics g = b.getGraphics();
        g.drawImage(source, 0, 0, null);
        g.dispose();
        return b;
    }

    protected void drawGrid() {
        var interval = preferences.boardWidth / (double) preferences.N;
        showGraphics2D.setColor(Color.GREEN);
        for (int i = 1; i <= preferences.N; i++) {
            showGraphics2D.drawLine((int)(interval * i), 0, (int)(interval * i), preferences.boardHeight);
        }

        interval = preferences.boardHeight / (double) preferences.M;
        graphics2D.setColor(Color.GREEN);
        for (int i = 1; i <= preferences.M; i++) {
            showGraphics2D.drawLine(0, (int)(interval * i), preferences.boardWidth, (int)(interval * i));
        }

    }
}
