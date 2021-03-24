import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Board {
    JFrame frame;
    JScrollPane scrollPane;
    ImagePanel imagePanel;

    ClassLoader cl;

    boolean proMenu = false;

    public Board() {
        frame = new JFrame("ICG Paint");
        try {
            frame.setIconImage(ImageIO.read(new File("resources/appIcon.jpg")));
        } catch (IOException e) {}
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(640, 480);

        cl = this.getClass().getClassLoader();

        BufferedImage pane = new BufferedImage(500, 500, BufferedImage.TYPE_INT_ARGB);
        Graphics g = pane.getGraphics();
        imagePanel = new ImagePanel();
        imagePanel.setSize(640, 480);
        imagePanel.setPreferredSize(new Dimension(500, 500));
        frame.setMinimumSize(new Dimension(640,580));
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(createFileMenu());
        menuBar.add(createViewMenu());
        menuBar.add(createAboutMenu());
        frame.setJMenuBar(menuBar);

        JToolBar toolsBar = new JToolBar();

        JButton bwButton = new JButton("Gray");
        bwButton.addActionListener(this::grayFilter);
        toolsBar.add(bwButton);

        JButton inverseButton = new JButton("Inverse");
        inverseButton.addActionListener(this::inverse);
        toolsBar.add(inverseButton);

        JButton boundDelineButton = new JButton("Bounds");
        boundDelineButton.addActionListener(this::bounds);
        toolsBar.add(boundDelineButton);

        JButton embossButton = new JButton("Emboss");
        embossButton.addActionListener(this::emboss);
        toolsBar.add(embossButton);

        JButton floydButton = new JButton("Floyd");
        floydButton.addActionListener(this::floyd);
        toolsBar.add(floydButton);

        toolsBar.setOrientation(1);
        scrollPane = new JScrollPane(imagePanel);
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

    private JMenu createViewMenu() {
        JMenu viewMenu = new JMenu("Tools");

        ButtonGroup buttonGroup = new ButtonGroup();


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
                imagePanel.save(file);
                System.out.println("Save as file: " + file.getAbsolutePath());
            } catch (IOException ee) {
                JOptionPane.showMessageDialog(imagePanel, "Some problems with file");
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
                imagePanel.open(file);
            } catch (IOException ee) {
                JOptionPane.showMessageDialog(imagePanel, "Some problems with file");
            }
        }

    }

    private void grayFilter(ActionEvent e) {
        imagePanel.grayFilter();
    }

    private void inverse(ActionEvent e) {
        imagePanel.inverse();
    }

    private void bounds(ActionEvent e) { imagePanel.boundsDeline(); }

    private void emboss(ActionEvent e) { imagePanel.emboss(); }

    private void floyd(ActionEvent e) { imagePanel.floyd(); }

}
