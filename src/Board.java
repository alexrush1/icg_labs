import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Board {

    DrawPanel drawPanel;
    JRadioButton cursorButton;

    public Board() {
        JFrame frame = new JFrame("ICG Paint");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(640, 480);
        //JPanel panel = new JPanel();
        //panel.setForeground(Color.WHITE);

        BufferedImage pane = new BufferedImage(620, 460, BufferedImage.TYPE_INT_ARGB);
        Graphics g = pane.getGraphics();
        drawPanel = new DrawPanel();
        drawPanel.setSize(640, 480);
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(createFileMenu());
        menuBar.add(createViewMenu());
        menuBar.add(createAboutMenu());
        frame.setJMenuBar(menuBar);

        JToolBar toolsBar = new JToolBar();

        cursorButton = new JRadioButton();
        ImageIcon cursorIco = new ImageIcon("./src/resources/cursor.jpg");
        cursorButton.setSize(15, 15);
        cursorButton.setIcon(cursorIco);

        cursorButton.setPressedIcon(new ImageIcon("./src/resources/cursor_pressed.jpg"));
        //cursorButton.setRolloverIcon(new ImageIcon("C:\\Users\\Иван\\IdeaProjects\\untitled\\src\\resources\\cursor_pressed.jpg"));
        //cursorButton.setDefaultCapable();
        cursorButton.addActionListener(this::cursor);


        JRadioButton linesButton = new JRadioButton();
        ImageIcon linesIco = new ImageIcon("./src/resources/lines.jpg");
        linesButton.setSize(15, 15);
        linesButton.setIcon(linesIco);
        linesButton.addActionListener(this::lines);


        JRadioButton stampButton = new JRadioButton();
        ImageIcon stampIco = new ImageIcon("./src/resources/stamp.jpg");
        stampButton.setSize(15, 15);
        stampButton.setIcon(stampIco);
        stampButton.addActionListener(this::stamp);

        JRadioButton starStampButton = new JRadioButton();
        ImageIcon starStampIco = new ImageIcon("./src/resources/starStamp.jpg");
        starStampButton.setSize(15, 15);
        starStampButton.setIcon(starStampIco);
        starStampButton.addActionListener(this::starStamp);

        JRadioButton colorChooser = new JRadioButton();
        ImageIcon colorChooserIco = new ImageIcon("./src/resources/picker.jpg");
        colorChooser.setSize(15,15);
        colorChooser.setIcon(colorChooserIco);
        colorChooser.addActionListener(this::colorChooser);

        JRadioButton clearButton = new JRadioButton();
        ImageIcon clearButtonIcon = new ImageIcon("./src/resources/clear.jpg");
        clearButton.setIcon(clearButtonIcon);
        clearButton.addActionListener(this::clear);

        JRadioButton fillButton = new JRadioButton();
        ImageIcon fillButtonIcon = new ImageIcon("./src/resources/fill.jpg");
        fillButton.setIcon(fillButtonIcon);
        fillButton.addActionListener(this::fill);


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

//        toolsBar.setBounds(0,20,640, 33);
        toolsBar.setOrientation(1);
        drawPanel.add(toolsBar, BorderLayout.EAST);
        //drawPanel.setBackground(Color.white);

        //drawPanel.add(menuBar, BorderLayout.NORTH);
        //drawPanel.add(toolsBar, BorderLayout.NORTH);

        frame.setContentPane(drawPanel);
        frame.setLocationRelativeTo(null);
       //frame.getContentPane().add(drawPanel, BorderLayout.SOUTH);
        frame.setVisible(true);
    }

    private JMenu createFileMenu() {
        JMenu file = new JMenu("File");
        //JMenuItem open = new JMenuItem("Открыть", new ImageIcon("images/open.png"));
        JMenuItem exit = new JMenuItem(new ExitAction());

        //file.add(open);
        // Добавление разделителя
        file.addSeparator();
        file.add(exit);

//        open.addActionListener(new ActionListener()
//        {
//            @Override
//            public void actionPerformed(ActionEvent arg0) {
//                System.out.println ("ActionListener.actionPerformed : open");
//            }
//        });
        return file;
    }

    private JMenu createViewMenu()
    {
        // создадим выпадающее меню
        JMenu viewMenu = new JMenu("Tools");

        JMenuItem clearButton = new JMenuItem("Clear");
        clearButton.addActionListener(this::clear);

        JMenuItem colorChooser = new JMenuItem("Color Chooser");
        colorChooser.addActionListener(this::colorChooser);

        JMenuItem resize = new JMenuItem("Resize");
        resize.addActionListener(this::resize);

        JRadioButton cursorButton = new JRadioButton("Cursor");
        cursorButton.addActionListener(this::cursor);

        JRadioButton linesButton = new JRadioButton("Lines");
        linesButton.addActionListener(this::lines);

        JRadioButton stampButton = new JRadioButton("Stamp");
        stampButton.addActionListener(this::stamp);

        JRadioButton starStampButton = new JRadioButton("Star Stamp");
        starStampButton.addActionListener(this::starStamp);

        JRadioButton fillButton = new JRadioButton("Fill");
        fillButton.addActionListener(this::fill);

        ButtonGroup buttonGroup = new ButtonGroup();

        buttonGroup.add(cursorButton);
        buttonGroup.add(linesButton);
        buttonGroup.add(stampButton);
        buttonGroup.add(starStampButton);
        buttonGroup.add(fillButton);

        viewMenu.add(clearButton);
        viewMenu.add(colorChooser);
        viewMenu.add(resize);
        viewMenu.addSeparator();
        viewMenu.add(cursorButton);
        viewMenu.add(linesButton);
        viewMenu.add(stampButton);
        viewMenu.add(starStampButton);
        viewMenu.add(fillButton);
        // меню-флажки
//        JCheckBoxMenuItem line  = new JCheckBoxMenuItem("Линейка");
//        JCheckBoxMenuItem grid  = new JCheckBoxMenuItem("Сетка");
//        JCheckBoxMenuItem navig = new JCheckBoxMenuItem("Навигация");
//        // меню-переключатели
//        JRadioButtonMenuItem one = new JRadioButtonMenuItem("Одна страница");
//        JRadioButtonMenuItem two = new JRadioButtonMenuItem("Две страницы");
//        // организуем переключатели в логическую группу
//        ButtonGroup bg = new ButtonGroup();
//        bg.add(one);
//        bg.add(two);
//        // добавим все в меню
//        viewMenu.add(line);
//        viewMenu.add(grid);
//        viewMenu.add(navig);
//        // разделитель можно создать и явно
//        viewMenu.add( new JSeparator());
//        viewMenu.add(one);
//        viewMenu.add(two);
        return viewMenu;
    }

    private JMenu createAboutMenu() {
        JMenu viewMenu = new JMenu("About");
        JMenuItem about = new JMenuItem("About");
        about.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame aboutFrame = new JFrame("About");
                aboutFrame.setSize(250, 120);
                aboutFrame.setLayout(null);
                JLabel icon = new JLabel();
                icon.setIcon(new ImageIcon("./src/resources/author.JPG"));
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
                aboutFrame.setVisible(true);
            }
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
        drawPanel.lines();
    }

    private void cursor(ActionEvent e) { drawPanel.cursor(); }

    private void stamp(ActionEvent e) { drawPanel.stamp(); }

    private void starStamp(ActionEvent e) { drawPanel.starStamp(); }

    private void colorChooser(ActionEvent e) { drawPanel.colorChooser(); }

    private void clear(ActionEvent e) { drawPanel.clearPanel(); }

    private void fill(ActionEvent e) { drawPanel.fill(); }

    private void resize(ActionEvent e) { drawPanel.reSize(); }

}
