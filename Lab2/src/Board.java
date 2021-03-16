import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


public class Board {

    DrawPanel drawPanel;

    ClassLoader cl;

    JToggleButton cursorButton;
    JToggleButton linesButton;
    JToggleButton stampButton;
    JToggleButton starStampButton;
    JToggleButton fillButton;

    JScrollPane scrollPane;

    JFrame frame;

    public Board() {
        frame = new JFrame("ICG Paint");
        try {
            frame.setIconImage(ImageIO.read(new File("resources/appIcon.jpg")));
        } catch (IOException e) {}
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(640, 480);

        BufferedImage pane = new BufferedImage(500, 500, BufferedImage.TYPE_INT_ARGB);
        Graphics g = pane.getGraphics();
        drawPanel = new DrawPanel();
        drawPanel.setSize(640, 480);
        drawPanel.setPreferredSize(new Dimension(500, 500));
        frame.setMinimumSize(new Dimension(640,580));
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(createFileMenu());
        menuBar.add(createViewMenu());
        menuBar.add(createAboutMenu());
        frame.setJMenuBar(menuBar);

        JToolBar toolsBar = new JToolBar();

        cl = this.getClass().getClassLoader();

        cursorButton =new JToggleButton();
        ImageIcon cursorIco = new ImageIcon(cl.getResource("resources/cursor.jpg"));
        cursorButton.setSize(15, 15);
        cursorButton.setIcon(cursorIco);
        cursorButton.setToolTipText("Cursor mode");

        cursorButton.setPressedIcon(new ImageIcon(cl.getResource("resources/cursor_pressed.jpg")));

        cursorButton.addActionListener(this::cursor);


        linesButton = new JToggleButton();
        ImageIcon linesIco = new ImageIcon(cl.getResource("resources/lines.jpg"));
        linesButton.setSize(15, 15);
        linesButton.setIcon(linesIco);
        linesButton.addActionListener(this::lines);
        linesButton.setToolTipText("Draw lines");

        stampButton = new JToggleButton();
        ImageIcon stampIco = new ImageIcon(cl.getResource("resources/stamp.jpg"));
        stampButton.setSize(15, 15);
        stampButton.setIcon(stampIco);
        stampButton.addActionListener(this::stamp);
        stampButton.setToolTipText("Draw Stamp");

        starStampButton = new JToggleButton();
        ImageIcon starStampIco = new ImageIcon(cl.getResource("resources/starStamp.jpg"));
        starStampButton.setSize(15, 15);
        starStampButton.setIcon(starStampIco);
        starStampButton.addActionListener(this::starStamp);
        starStampButton.setToolTipText("Draw Stamp like a star");

        JRadioButton colorChooser = new JRadioButton();
        ImageIcon colorChooserIco = new ImageIcon(cl.getResource("resources/picker.jpg"));
        colorChooser.setSize(15,15);
        colorChooser.setIcon(colorChooserIco);
        colorChooser.addActionListener(this::colorChooser);
        colorChooser.setToolTipText("Too many colors");

        JRadioButton clearButton = new JRadioButton();
        ImageIcon clearButtonIcon = new ImageIcon(cl.getResource("resources/clear.jpg"));
        clearButton.setIcon(clearButtonIcon);
        clearButton.addActionListener(this::clear);
        clearButton.setToolTipText("Clear all!");

        fillButton = new JToggleButton();
        ImageIcon fillButtonIcon = new ImageIcon(cl.getResource("resources/fill.jpg"));
        fillButton.setIcon(fillButtonIcon);
        fillButton.addActionListener(this::fill);
        fillButton.setToolTipText("Fill mode");

        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(cursorButton);
        buttonGroup.add(linesButton);
        buttonGroup.add(stampButton);
        buttonGroup.add(starStampButton);
        buttonGroup.add(fillButton);
        buttonGroup.add(colorChooser);
        buttonGroup.add(clearButton);

        toolsBar.add(cursorButton);
        toolsBar.add(linesButton);
        toolsBar.add(stampButton);
        toolsBar.add(starStampButton);
        toolsBar.add(fillButton);
        toolsBar.add(colorChooser);
        toolsBar.add(clearButton);

        toolsBar.setOrientation(1);
        scrollPane = new JScrollPane(drawPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        frame.add(toolsBar, BorderLayout.EAST);

        frame.add(scrollPane, BorderLayout.CENTER);

        frame.pack();
        frame.setLocationRelativeTo(null);

        frame.setVisible(true);
    }

    private JMenu createFileMenu() {
        JMenu file = new JMenu("File");
        JMenuItem open = new JMenuItem("Open");
        JMenuItem save = new JMenuItem("Save");
        open.addActionListener(this::open);
        save.addActionListener(this::save);

        JMenuItem exit = new JMenuItem(new ExitAction());

        file.add(open);
        file.add(save);
        file.addSeparator();
        file.add(exit);
        return file;
    }

    JRadioButton cursorRadioButton;
    JRadioButton linesRadioButton;
    JRadioButton stampRadioButton;
    JRadioButton starStampRadioButton;
    JRadioButton fillRadioButton;

    private JMenu createViewMenu() {
        JMenu viewMenu = new JMenu("Tools");

        JMenuItem clearButton = new JMenuItem("Clear");
        clearButton.addActionListener(this::clear);

        JMenuItem colorChooser = new JMenuItem("Color Chooser");
        colorChooser.addActionListener(this::colorChooser);

        JMenuItem resize = new JMenuItem("Resize");
        resize.addActionListener(this::resize);

        cursorRadioButton = new JRadioButton("Cursor");
        cursorRadioButton.addActionListener(this::cursor);

        linesRadioButton = new JRadioButton("Lines");
        linesRadioButton.addActionListener(this::lines);

        stampRadioButton = new JRadioButton("Stamp");
        stampRadioButton.addActionListener(this::stamp);

        starStampRadioButton = new JRadioButton("Star Stamp");
        starStampRadioButton.addActionListener(this::starStamp);

        fillRadioButton = new JRadioButton("Fill");
        fillRadioButton.addActionListener(this::fill);

        ButtonGroup buttonGroup = new ButtonGroup();

        buttonGroup.add(cursorRadioButton);
        buttonGroup.add(linesRadioButton);
        buttonGroup.add(stampRadioButton);
        buttonGroup.add(starStampRadioButton);
        buttonGroup.add(fillRadioButton);

        viewMenu.add(clearButton);
        viewMenu.add(colorChooser);
        viewMenu.add(resize);
        viewMenu.addSeparator();
        viewMenu.add(cursorRadioButton);
        viewMenu.add(linesRadioButton);
        viewMenu.add(stampRadioButton);
        viewMenu.add(starStampRadioButton);
        viewMenu.add(fillRadioButton);

        return viewMenu;
    }

    private JMenu createAboutMenu() {
        JMenu viewMenu = new JMenu("About");
        JMenuItem about = new JMenuItem("About");
        about.addActionListener(e -> {
            JDialog aboutFrame = new JDialog(frame, true);
            aboutFrame.setLocation(frame.getLocation());
            aboutFrame.setTitle("About");
            aboutFrame.setSize(250, 120);
            aboutFrame.setLayout(null);
            JLabel icon = new JLabel();
            icon.setIcon(new ImageIcon(cl.getResource("resources/author.JPG")));
            icon.setBounds(7,7,70,70);
            JLabel infoName = new JLabel("Timofeev Ivan");
            infoName.setBounds(115, 15, 140, 20);
            JLabel groupName = new JLabel("18204");
            groupName.setBounds(135, 35, 50, 20);
            JLabel email = new JLabel("i.timofeev2@g.nsu.ru");
            email.setBounds(95, 55, 140, 20);
            aboutFrame.add(icon);
            aboutFrame.add(infoName);
            aboutFrame.add(groupName);
            aboutFrame.add(email);
            aboutFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            aboutFrame.setVisible(true);
        });
        viewMenu.add(about);
        return viewMenu;
    }

    class ExitAction extends AbstractAction {
        private static final long serialVersionUID = 1L;
        ExitAction() {
            putValue(NAME, "Exit");
        }
        public void actionPerformed(ActionEvent e) {
            System.exit(0);
        }
    }

    private void lines(ActionEvent e) {
        linesButton.setSelected(true);
        linesRadioButton.setSelected(true);
        drawPanel.lines();
    }

    private void cursor(ActionEvent e) { cursorButton.setSelected(true); cursorRadioButton.setSelected(true); drawPanel.cursor(); }

    private void stamp(ActionEvent e) { stampButton.setSelected(true); stampRadioButton.setSelected(true); drawPanel.stamp(); }

    private void starStamp(ActionEvent e) { starStampButton.setSelected(true); starStampRadioButton.setSelected(true); drawPanel.starStamp(); }

    private void colorChooser(ActionEvent e) { drawPanel.colorChooser(); }

    private void clear(ActionEvent e) { drawPanel.clearPanel(); }

    private void fill(ActionEvent e) { fillButton.setSelected(true); fillRadioButton.setSelected(true); drawPanel.fill(); }

    private void resize(ActionEvent e) { reSize(); }

    private void save(ActionEvent e) {
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter ("png file (*.png)", "png");
        fileChooser.setDialogTitle("Save picture");
        fileChooser.setFileFilter(filter);

        int userSelection = fileChooser.showSaveDialog(null);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            if (!file.getName().endsWith(".png")) {
                file = new File(file.getParentFile(), file.getName() + ".png");
            }

            try {
                drawPanel.save(file);
                System.out.println("Save as file: " + file.getAbsolutePath());
            } catch (IOException ee) {
                JOptionPane.showMessageDialog(drawPanel, "Some problems with file");
            }
        }
    }

    private void open(ActionEvent e) {
    JFileChooser fileChooser = new JFileChooser();
    FileNameExtensionFilter filter = new FileNameExtensionFilter ("picture file (*.png, *.jpg, *.jpeg, ...)", "png", "jpg", "jpeg", "bmp");
    fileChooser.setFileFilter(filter);
    int ret = fileChooser.showDialog(null, "Open file");
        if (ret == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try {
                drawPanel.open(file);
            } catch (IOException ee) {
                JOptionPane.showMessageDialog(drawPanel, "Some problems with file");
            }
        }

    }

    public void reSize() {
        JDialog preferencesFrame = new JDialog(frame, true);
        preferencesFrame.setLocation(frame.getLocation());
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
                int xSize = 0;
                int ySize = 0;
                if (!yField.getText().isEmpty() && !xField.getText().isEmpty() &&
                        !Character.isLetter(xField.getText().charAt(0)) && !Character.isLetter(yField.getText().charAt(0))) {
                    xSize = Integer.parseInt(xField.getText());
                    ySize = Integer.parseInt(yField.getText());
                } else {
                    JOptionPane.showMessageDialog(preferencesFrame, "Letters are NOT NUMBERS!!!");
                    preferencesFrame.setVisible(false);
                }
                if (xSize < 10000 && ySize < 10000) {
                    drawPanel.resizeWorker(xSize, ySize);
                } else {
                    JOptionPane.showMessageDialog(preferencesFrame, "so big! (must be < 10000)");
                    preferencesFrame.setVisible(false);
                }

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
