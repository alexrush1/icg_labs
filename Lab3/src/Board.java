import javax.swing.*;
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


    public Board() {
        frame = new JFrame("ICG Map");
//        try {
//            frame.setIconImage(new ImageIcon(Objects.requireNonNull(cl.getResource("author.jpg"))));
//        } catch (IOException e) {}
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(640, 580);

        cl = this.getClass().getClassLoader();

        frame.setMinimumSize(new Dimension(640,580));
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(createFileMenu());
        menuBar.add(createAboutMenu());

        JPanel panel = new JPanel();
        panel.setLayout(null);
        //frame.setLayout(null);

        final double[] xKoef = {(preferences.b - preferences.a) / (double) preferences.boardWidth};
        final double[] yKoef = {(preferences.d - preferences.c) / (double) preferences.boardHeight};

        workingPanel = new WorkingPanel(frame, preferences, xKoef[0], yKoef[0]);
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
//                xKoef[0] = (preferences.b - preferences.a) / (double) preferences.boardWidth;
//                yKoef[0] = (preferences.d - preferences.c) / (double) preferences.boardHeight;
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
