import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class Board {
    JFrame frame;
    ClassLoader cl;
    WorkingPanel workingPanel;
    LegendPanel legendPanel;
    Preferences preferences = new Preferences(this);


    private void preferences(ActionEvent e) {
        JFrame preferencesFrame = new JFrame("Preferences");
        preferencesFrame.setLayout(null);
        preferencesFrame.setLocationRelativeTo(null);
        preferencesFrame.setSize(300, 300);

        JLabel aLabel = new JLabel("a:");
        aLabel.setBounds(25, 10, 80, 40);
        JLabel bLabel = new JLabel("b:");
        bLabel.setBounds(25, 50, 80, 40);
        JLabel cLabel = new JLabel("c:");
        cLabel.setBounds(25, 90, 80, 40);
        JLabel dLabel = new JLabel("d:");
        dLabel.setBounds(25, 130, 80, 40);

        JTextField aField = new JTextField();
        aField.setBounds(50, 10, 80, 40);
        aField.setText(String.valueOf(preferences.a));
        JTextField bField = new JTextField();
        bField.setBounds(50, 50, 80, 40);
        bField.setText(String.valueOf(preferences.b));
        JTextField cField = new JTextField();
        cField.setBounds(50, 90, 80, 40);
        cField.setText(String.valueOf(preferences.c));
        JTextField dField = new JTextField();
        dField.setBounds(50, 130, 80, 40);
        dField.setText(String.valueOf(preferences.d));

        JLabel gridLabel = new JLabel("Grid");
        gridLabel.setBounds(25, 170, 80, 40);

        JTextField firstGrid = new JTextField();
        firstGrid.setBounds(60, 170, 80, 40);
        firstGrid.setText(String.valueOf(preferences.N));

        JLabel xLabel = new JLabel("x");
        xLabel.setBounds(143, 170, 10, 40);

        JTextField secondGrid = new JTextField();
        secondGrid.setBounds(150, 170, 80, 40);
        secondGrid.setText(String.valueOf(preferences.M));

        JLabel kLabel = new JLabel("Isolines");
        kLabel.setBounds(150, 10, 80, 40);

        JTextField kField = new JTextField();
        kField.setBounds(215, 10, 80, 40);
        kField.setText(String.valueOf(preferences.K));

        JButton saveButton = new JButton("Save");
        saveButton.setBounds(100, 220, 75, 50);

        saveButton.addActionListener(ee -> {
            preferences.a = (float) Double.parseDouble(aField.getText());
            preferences.b = (float) Double.parseDouble(bField.getText());
            preferences.c = (float) Double.parseDouble(cField.getText());
            preferences.d = (float) Double.parseDouble(dField.getText());

            preferences.N = Integer.parseInt(firstGrid.getText());
            preferences.M = Integer.parseInt(secondGrid.getText());

            int newColors = Integer.parseInt(kField.getText()) - preferences.K;
            System.out.println("NEW COLORS = " + newColors);
            if (newColors > 0) {
                for (int i = 0; i < newColors; i++) {
                    preferences.colors.add(Color.WHITE);
                }
            } else {
                for (int i = preferences.colors.size() - 1; i > Integer.parseInt(kField.getText()); i--) {
                    System.out.println("i = " + i);
                    preferences.colors.remove(i);
                }
            }

            preferences.K = Integer.parseInt(kField.getText());
            System.out.println("NEW K: " + preferences.K);


//            reCoef();
//            workingPanel.reCoef();
//            preferences.prepareValues();
//            preferences.loadIntervals();
//            workingPanel.reGrid();
//            workingPanel.paint();

            reCoef();
            preferences.prepareValues();
            preferences.loadIntervals();
            workingPanel.reCoef();
            workingPanel.reGrid();
            workingPanel.paint();
            legendPanel.paint();

        });

        preferencesFrame.add(aLabel);
        preferencesFrame.add(bLabel);
        preferencesFrame.add(cLabel);
        preferencesFrame.add(dLabel);

        preferencesFrame.add(aField);
        preferencesFrame.add(bField);
        preferencesFrame.add(cField);
        preferencesFrame.add(dField);

        preferencesFrame.add(gridLabel);
        preferencesFrame.add(firstGrid);
        preferencesFrame.add(xLabel);
        preferencesFrame.add(secondGrid);

        preferencesFrame.add(kLabel);
        preferencesFrame.add(kField);

        preferencesFrame.add(saveButton);

        preferencesFrame.setVisible(true);
    }

    double[] xKoef = {(preferences.b - preferences.a) / (double) preferences.boardWidth};
    double[] yKoef = {(preferences.d - preferences.c) / (double) preferences.boardHeight};

    public void reCoef() {
        xKoef = new double[]{(preferences.b - preferences.a) / (double) preferences.boardWidth};
        yKoef = new double[]{(preferences.d - preferences.c) / (double) preferences.boardHeight};
    }

    public Board() {
        frame = new JFrame("ICG Map");
//        try {
//            frame.setIconImage(new ImageIcon(Objects.requireNonNull(cl.getResource("author.jpg"))));
//        } catch (IOException e) {}
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(640, 600);

        cl = this.getClass().getClassLoader();

        frame.setMinimumSize(new Dimension(640,600));
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(createFileMenu());
        menuBar.add(createAboutMenu());

        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);

        JCheckBox gridCheckBox = new JCheckBox("Grid");
        gridCheckBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == 1) {
                    workingPanel.gridMode = true;
                    workingPanel.paint();
                } else {
                    workingPanel.gridMode = false;
                    workingPanel.paint();
                }
            }
        });
        toolBar.add(gridCheckBox);

        JCheckBox gradientCheckBox = new JCheckBox("Gradient");
        gradientCheckBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == 1) {
                    legendPanel.gradientMode = true;
                    legendPanel.paint();
                    workingPanel.gradientMode = true;
                    workingPanel.paint();
                } else {
                    legendPanel.gradientMode = false;
                    legendPanel.paint();
                    workingPanel.gradientMode = false;
                    workingPanel.paint();
                }
            }
        });
        toolBar.add(gradientCheckBox);

        toolBar.addSeparator();

        JButton preferencesButton = new JButton("Preferences");
        preferencesButton.addActionListener(this::preferences);
        toolBar.add(preferencesButton);

        frame.add(toolBar, BorderLayout.NORTH);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        //frame.setLayout(null);

        xKoef = new double[]{(preferences.b - preferences.a) / (double) preferences.boardWidth};
        yKoef = new double[]{(preferences.d - preferences.c) / (double) preferences.boardHeight};

        workingPanel = new WorkingPanel(frame, preferences, xKoef[0], yKoef[0], this);
        workingPanel.setPreferredSize(new Dimension((int)preferences.boardWidth, (int)preferences.boardWidth));
        //JScrollPane scrollPane = new JScrollPane(workingPanel);
        //scrollPane.setBorder(BorderFactory.createDashedBorder(null, 1.5f, 5, 4, false));
        //panel.add(scrollPane);
        workingPanel.setBounds(0, 0, panel.getWidth(), panel.getHeight());
        workingPanel.setBorder(BorderFactory.createDashedBorder(null, 1.5f, 5, 4, false));
        panel.add(workingPanel);
//        frame.setLayout(null);
//        panel.setBounds(4, 4, frame.getWidth() - 20, frame.getHeight() - 75);
        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                //resizeX = frame
                //resizeY = (preferences.d - preferences.c) / (double) preferences.boardHeight;
//                workingPanel.setXKoef(xKoef[0]);
//                workingPanel.setYKoef(yKoef[0]);
                //scrollPane.setBounds(4, 4, panel.getWidth() - 10, panel.getHeight() - 10);
                //scrollPane.repaint();
                //scrollPane.revalidate();
                workingPanel.setBounds(18, 4, frame.getWidth() - 45, frame.getHeight() - 179);
                workingPanel.repaint();
                workingPanel.revalidate();
            }
        });
        frame.add(panel, BorderLayout.CENTER);

        JPanel panelLegend = new JPanel();
        panelLegend.setLayout(new BoxLayout(panelLegend, BoxLayout.Y_AXIS));
        //panelLegend.setLayout(null);

        JPanel legendPanelMain = new JPanel();
        legendPanelMain.setLayout(null);
        legendPanelMain.setPreferredSize(new Dimension(640, 100));
        legendPanel = new LegendPanel(frame, preferences, workingPanel);
        legendPanel.setBounds(15, 20, 600, 60);
        System.out.println(legendPanel.getX() + " " + legendPanel.getY());
        legendPanelMain.add(legendPanel);


        JPanel labelPanel = new JPanel();
        labelPanel.setLayout(null);
        labelPanel.setPreferredSize(new Dimension(640, 20));

        JLabel label = new JLabel("X: Y: F(X, Y):");
        label.setBounds(10, 0, 400, 20);
        labelPanel.add(label);


        legendPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                legendPanel.changeColor(e.getX(), e.getY());
            }
        });

        panelLegend.add(legendPanelMain);
        panelLegend.add(labelPanel);

        workingPanel.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
                workingPanel.changeMouseValue(e.getX(), e.getY(), e.getX() * xKoef[0] + (preferences.a), (e.getY() * yKoef[0] + preferences.c));
                label.setText(String.format("X: %.4f", (e.getX() * xKoef[0] + (preferences.a)))  + String.format("  Y: %.4f", (e.getY() * yKoef[0] + preferences.c)) + String.format(" F(X, Y): %.4f", Math.sin((e.getY() * yKoef[0] + preferences.c)) * Math.cos((e.getX() * xKoef[0] + (preferences.a)))));
                workingPanel.dynamic = true;
                workingPanel.repaint();
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                label.setText(String.format("X: %.4f", (e.getX() * xKoef[0] + (preferences.a)))  + String.format("  Y: %.4f", (e.getY() * yKoef[0] + preferences.c)) + String.format(" F(X, Y): %.4f", Math.sin((e.getY() * yKoef[0] + preferences.c)) * Math.cos((e.getX() * xKoef[0] + (preferences.a)))));
                //System.out.println(e.getX() + " " + e.getY());
                workingPanel.dynamic = false;
                workingPanel.repaint();
            }

        });

        frame.add(panelLegend, BorderLayout.SOUTH);

        frame.pack();
        frame.setLocationRelativeTo(null);

        frame.setJMenuBar(menuBar);
        frame.setVisible(true);
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
            icon.setIcon(new ImageIcon(Objects.requireNonNull(cl.getResource("author.JPG"))));
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

    private JMenu createFileMenu() {
        JMenu file = new JMenu("Function");
        JMenuItem open = new JMenuItem("Open config file");
        open.addActionListener(this::open);

        JMenuItem exit = new JMenuItem(new ExitAction());

        file.add(open);
        file.addSeparator();
        file.add(exit);
        return file;
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

    private void open(ActionEvent e) {
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter ("txt file (.txt)", "txt");
        fileChooser.setFileFilter(filter);
        int ret = fileChooser.showDialog(null, "Open file");
        if (ret == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try {
                preferences.loadPreferences(file);
            } catch (IOException ee) {
                JOptionPane.showMessageDialog(frame, "Some problems with file");
            }
        }

    }
}
