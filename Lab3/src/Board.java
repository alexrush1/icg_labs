import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class Board {
    JFrame frame;
    ClassLoader cl;
    Preferences preferences = new Preferences();

    public Board() {
        frame = new JFrame("ICG Filter");
//        try {
//            frame.setIconImage(new ImageIcon(Objects.requireNonNull(cl.getResource("author.jpg"))));
//        } catch (IOException e) {}
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(640, 480);

        cl = this.getClass().getClassLoader();

        //BufferedImage pane = new BufferedImage(500, 500, BufferedImage.TYPE_INT_ARGB);
        //Graphics g = pane.getGraphics();
//        imagePanel = new ImagePanel(frame);
//        imagePanel.setSize(640, 480);
//        imagePanel.setPreferredSize(new Dimension(500, 500));
        frame.setMinimumSize(new Dimension(640,580));
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(createFileMenu());
        menuBar.add(createAboutMenu());

        JPanel panel = new JPanel();
        panel.setLayout(null);
        //frame.setLayout(null);

        final double[] xKoef = {(preferences.b - preferences.a) / (double) 640};
        final double[] yKoef = {(preferences.d - preferences.c) / (double) 400};

        WorkingPanel workingPanel = new WorkingPanel(frame, preferences, xKoef[0], yKoef[0]);
        workingPanel.setPreferredSize(new Dimension(400, 400));
        JScrollPane scrollPane = new JScrollPane(workingPanel);
        scrollPane.setBorder(BorderFactory.createDashedBorder(null, 1.5f, 5, 4, false));
        panel.add(scrollPane);
        scrollPane.setBounds(4, 4, panel.getWidth() - 10, panel.getHeight() - 10);
        //panel.setBounds(4, 4, frame.getWidth() - 79, frame.getHeight() - 75);
        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                xKoef[0] = (preferences.b - preferences.a) / (double) panel.getWidth();
                yKoef[0] = (preferences.d - preferences.c) / (double) panel.getHeight();
                scrollPane.setBounds(4, 4, panel.getWidth() - 10, panel.getHeight() - 10);
                scrollPane.repaint();
                scrollPane.revalidate();
            }
        });
        frame.add(panel, BorderLayout.CENTER);

        JPanel legendPanel = new JPanel();
        legendPanel.setLayout(null);
        legendPanel.setPreferredSize(new Dimension(400, 100));
        JLabel label = new JLabel("X: Y: F(X, Y):");
        label.setBounds(10, 80, 400, 20);
        legendPanel.add(label);


        scrollPane.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                label.setText("X: " + (e.getX() * xKoef[0] + (preferences.a))  + "  Y: " + (e.getY() * yKoef[0] + preferences.c) + " F(X, Y): ");
                //System.out.println(e.getX() + " " + e.getY());
            }
        });

        frame.add(legendPanel, BorderLayout.SOUTH);

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
