import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.AffineTransformOp;
import java.io.File;
import java.io.IOException;

public class Board {
    JFrame frame;
    JScrollPane scrollPane;
    ImagePanel imagePanel;
    JToggleButton interpolationButton;
    JRadioButton interpolation;
    JRadioButton interpolationBicubic;
    JRadioButton interpolationNearest;

    ClassLoader cl;
    private AffineTransformOp AffineTransformOp;

    public Board() {
        frame = new JFrame("ICG Paint");
        try {
            frame.setIconImage(ImageIO.read(new File("resources/appIcon.jpg")));
        } catch (IOException e) {}
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(640, 480);

        cl = this.getClass().getClassLoader();

        //BufferedImage pane = new BufferedImage(500, 500, BufferedImage.TYPE_INT_ARGB);
        //Graphics g = pane.getGraphics();
        imagePanel = new ImagePanel(frame);
        imagePanel.setSize(640, 480);
        imagePanel.setPreferredSize(new Dimension(500, 500));
        frame.setMinimumSize(new Dimension(640,580));
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(createFileMenu());
        menuBar.add(createViewMenu());
        menuBar.add(createModeMenu());
        menuBar.add(createAboutMenu());
        frame.setJMenuBar(menuBar);

        JToolBar toolsBar = new JToolBar();

        JButton rotateButton = new JButton();
        rotateButton.setIcon(new ImageIcon(cl.getResource("resources/rotateIco.JPG")));
        rotateButton.addActionListener(this::rotate);
        toolsBar.add(rotateButton);

        JButton sharpnessButton = new JButton();
        sharpnessButton.setIcon(new ImageIcon(cl.getResource("resources/sharpnessIco.JPG")));
        sharpnessButton.addActionListener(this::sharpness);
        toolsBar.add(sharpnessButton);

        JButton gammaButton = new JButton();
        gammaButton.setIcon(new ImageIcon(cl.getResource("resources/gammaIco.JPG")));
        gammaButton.addActionListener(this::gamma);
        toolsBar.add(gammaButton);

        JButton bwButton = new JButton();
        bwButton.setIcon(new ImageIcon(cl.getResource("resources/grayIco.JPG")));
        bwButton.addActionListener(this::grayFilter);
        toolsBar.add(bwButton);

        JButton inverseButton = new JButton();
        inverseButton.setIcon(new ImageIcon(cl.getResource("resources/inverseIco.JPG")));
        inverseButton.addActionListener(this::inverse);
        toolsBar.add(inverseButton);

//        JButton boundDelineButton = new JButton("Bounds");
//        boundDelineButton.addActionListener(this::bounds);
//        toolsBar.add(boundDelineButton);

        JButton embossButton = new JButton();
        embossButton.setIcon(new ImageIcon(cl.getResource("resources/embossIco.JPG")));
        embossButton.addActionListener(this::emboss);
        toolsBar.add(embossButton);

        JButton sobelButton = new JButton();
        sobelButton.setIcon(new ImageIcon(cl.getResource("resources/sobelIco.JPG")));
        sobelButton.addActionListener(this::sobel);
        toolsBar.add(sobelButton);

        JButton robertsButton = new JButton();
        robertsButton.setIcon(new ImageIcon(cl.getResource("resources/robertsIco.JPG")));
        robertsButton.addActionListener(this::roberts);
        toolsBar.add(robertsButton);

        JButton aquarelButton = new JButton();
        aquarelButton.setIcon(new ImageIcon(cl.getResource("resources/aquarelIco.JPG")));
        aquarelButton.addActionListener(this::aquarel);
        toolsBar.add(aquarelButton);

        JButton gauss3Button = new JButton();
        gauss3Button.setIcon(new ImageIcon(cl.getResource("resources/gauss3Ico.JPG")));
        gauss3Button.addActionListener(this::gauss3);
        toolsBar.add(gauss3Button);

        JButton gauss5Button = new JButton();
        gauss5Button.setIcon(new ImageIcon(cl.getResource("resources/gauss5Ico.JPG")));
        gauss5Button.addActionListener(this::gauss5);
        toolsBar.add(gauss5Button);

        JButton orderedDitheringButton = new JButton();
        orderedDitheringButton.setIcon(new ImageIcon(cl.getResource("resources/od.PNG")));
        orderedDitheringButton.addActionListener(this::orderedDithering);
        toolsBar.add(orderedDitheringButton);

        JButton floydButton = new JButton();
        floydButton.setIcon(new ImageIcon(cl.getResource("resources/fsd.PNG")));
        floydButton.addActionListener(this::floyd);
        toolsBar.add(floydButton);

        JButton kmeansButton = new JButton();
        kmeansButton.setIcon(new ImageIcon(cl.getResource("resources/clusterIco.JPG")));
        kmeansButton.addActionListener(this::kmeans);
        toolsBar.add(kmeansButton);

        JButton matrixButton = new JButton();
        matrixButton.setIcon(new ImageIcon(cl.getResource("resources/3x3Ico.JPG")));
        matrixButton.addActionListener(this::matrix);
        toolsBar.add(matrixButton);


        interpolationButton = new JToggleButton();
        interpolationButton.setIcon(new ImageIcon(cl.getResource("resources/resizeIco.jpg")));

        interpolationButton.addItemListener(this::select);
        toolsBar.add(interpolationButton);

        toolsBar.setOrientation(1);
        scrollPane = new JScrollPane(imagePanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(BorderFactory.createDashedBorder(null, 1.5f, 5, 4, false));

        JPanel panel = new JPanel();
        frame.add(toolsBar, BorderLayout.EAST);
        panel.add(scrollPane);
        scrollPane.setBounds(4, 4, frame.getWidth(), frame.getHeight());
        frame.add(panel);

        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                scrollPane.setBounds(4, 4, frame.getWidth() - 75, frame.getHeight() - 60);
                scrollPane.repaint();
                scrollPane.revalidate();
                //frame.revalidate();
                //System.out.println(frame.getWidth() + " " + frame.getHeight());
            }
        });

        //frame.add(toolsBar, BorderLayout.EAST);
        //frame.add(scrollPane, BorderLayout.CENTER);

        MouseAdapter ma = new MouseAdapter() {

            private Point origin;

            @Override
            public void mousePressed(MouseEvent e) {
                origin = new Point(e.getPoint());
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                if (origin != null) {
                    JViewport viewPort = (JViewport) SwingUtilities.getAncestorOfClass(JViewport.class, imagePanel);
                    if (viewPort != null) {
                        int deltaX = origin.x - e.getX();
                        int deltaY = origin.y - e.getY();

                        Rectangle view = viewPort.getViewRect();
                        view.x += deltaX;
                        view.y += deltaY;

                        imagePanel.scrollRectToVisible(view);
                    }
                }
            }

        };

        imagePanel.addMouseListener(ma);
        imagePanel.addMouseMotionListener(ma);

        frame.pack();
        frame.setLocationRelativeTo(null);

        frame.setVisible(true);
    }

    private ItemListener select(ItemEvent e) {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                setInterpolationOn();
                //interpolationButton.setSelected(true);
                interpolation.setSelected(true);
                scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
                scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
                imagePanel.repaint();
            } else if (e.getStateChange() == ItemEvent.DESELECTED) {
                setInterpolationOff();
                //interpolationButton.setSelected(false);
                interpolation.setSelected(false);
                scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
                scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
                imagePanel.repaint();
            }
        return null;
    }

    private ItemListener selectBicubic(ItemEvent e) {
        if (e.getStateChange() == ItemEvent.SELECTED) {
            setInterpolationBicubicOn();
            //interpolationButton.setSelected(true);
            interpolationBicubic.setSelected(true);
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
            scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            imagePanel.repaint();
        } else if (e.getStateChange() == ItemEvent.DESELECTED) {
            setInterpolationOff();
            //interpolationButton.setSelected(false);
            interpolationBicubic.setSelected(false);
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            imagePanel.repaint();
        }
        return null;
    }

    private ItemListener selectNearest(ItemEvent e) {
        if (e.getStateChange() == ItemEvent.SELECTED) {
            setInterpolationNearestOn();
            //interpolationButton.setSelected(true);
            interpolationNearest.setSelected(true);
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
            scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            imagePanel.repaint();
        } else if (e.getStateChange() == ItemEvent.DESELECTED) {
            setInterpolationOff();
            //interpolationButton.setSelected(false);
            interpolationNearest.setSelected(false);
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            imagePanel.repaint();
        }
        return null;
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

    private JMenu createModeMenu() {
        JMenu modeMenu = new JMenu("View mode");

        ButtonGroup buttonGroup = new ButtonGroup();
        interpolation = new JRadioButton("Interpolation");
        interpolation.addItemListener(this::select);
        buttonGroup.add(interpolation);

        interpolationBicubic = new JRadioButton("Bicubic");
        interpolationBicubic.addItemListener(this::selectBicubic);
        buttonGroup.add(interpolationBicubic);

        interpolationNearest = new JRadioButton("Nearest");
        interpolationNearest.addItemListener(this::selectNearest);
        buttonGroup.add(interpolationNearest);

        modeMenu.add(interpolation);
        modeMenu.add(interpolationBicubic);
        modeMenu.add(interpolationNearest);
        return modeMenu;
    }

    private JMenu createViewMenu() {
        JMenu viewMenu = new JMenu("Tools");

        JMenuItem rotate = new JMenuItem("Rotate");
        rotate.addActionListener(this::rotate);
        viewMenu.add(rotate);
        viewMenu.addSeparator();

        JMenuItem sharpness = new JMenuItem("Sharpness");
        sharpness.addActionListener(this::sharpness);
        viewMenu.add(sharpness);

        JMenuItem gamma = new JMenuItem("Gamma");
        gamma.addActionListener(this::gamma);
        viewMenu.add(gamma);
        viewMenu.addSeparator();

        JMenuItem gray = new JMenuItem("Gray filter");
        gray.addActionListener(this::grayFilter);
        viewMenu.add(gray);

        JMenuItem inverse = new JMenuItem("Inverse");
        inverse.addActionListener(this::inverse);
        viewMenu.add(inverse);

        JMenuItem emboss = new JMenuItem("Emboss");
        emboss.addActionListener(this::emboss);
        viewMenu.add(emboss);
        viewMenu.addSeparator();

        JMenuItem sobel = new JMenuItem("Sobel");
        sobel.addActionListener(this::sobel);
        viewMenu.add(sobel);

        JMenuItem roberts = new JMenuItem("Roberts");
        roberts.addActionListener(this::roberts);
        viewMenu.add(roberts);

        JMenuItem aquarel = new JMenuItem("Aquarel");
        aquarel.addActionListener(this::aquarel);
        viewMenu.add(aquarel);
        viewMenu.addSeparator();

        JMenuItem gauss3 = new JMenuItem("Gauss 3x3");
        gauss3.addActionListener(this::gauss3);
        viewMenu.add(gauss3);

        JMenuItem gauss5 = new JMenuItem("Gauss 5x5");
        gauss5.addActionListener(this::gauss5);
        viewMenu.add(gauss5);
        viewMenu.addSeparator();

        JMenuItem oredD = new JMenuItem("Ordered Dithering");
        oredD.addActionListener(this::orderedDithering);
        viewMenu.add(oredD);

        JMenuItem floyd = new JMenuItem("Floyd - Stainberg");
        floyd.addActionListener(this::floyd);
        viewMenu.add(floyd);

        viewMenu.addSeparator();

        JMenuItem kmeans = new JMenuItem("Cluster");
        kmeans.addActionListener(this::kmeans);
        viewMenu.add(kmeans);

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

    private void gauss3(ActionEvent e) { imagePanel.gauss3(); }

    private void gauss5(ActionEvent e) { imagePanel.gauss5(); }

    private void sharpness(ActionEvent e) { imagePanel.sharpness(); }

    private void aquarel(ActionEvent e) { imagePanel.aquarel(); }

    private void gamma(ActionEvent e) { imagePanel.gamma(); }

    private void rotate(ActionEvent e) { imagePanel.rotate(); }

    private void orderedDithering(ActionEvent e) { imagePanel.orderedDithering(); }

    private void roberts(ActionEvent e) { imagePanel.roberts(); }

    private void sobel(ActionEvent e) { imagePanel.sobel(); }

    private void kmeans(ActionEvent e) { imagePanel.kmeans(); }

    private void setInterpolationOn() {
        imagePanel.setInterpolationOn();
    }

    private void setInterpolationBicubicOn() { imagePanel.setInterpolationBicubicOn(); }

    private void setInterpolationNearestOn() { imagePanel.setInterpolationNearestOn(); }

    private void setInterpolationOff() {
        imagePanel.setInterpolationOff();
    }

    private void matrix(ActionEvent e) { imagePanel.matrix(); }

}
