import Utilites.BoxLayoutUtilites;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

public class Board {

    DrawPanel drawPanel;
    JRadioButton cursorButton;

    public Board() {
        JFrame frame = new JFrame("ICG Paint");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(640, 480);
        //JPanel panel = new JPanel();
        //panel.setForeground(Color.WHITE);

        BufferedImage pane = new BufferedImage(620, 460, BufferedImage.TYPE_INT_RGB);
        Graphics g = pane.getGraphics();
        drawPanel = new DrawPanel();
        drawPanel.setSize(640, 480);
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(createFileMenu());
        menuBar.add(createViewMenu());
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

        JRadioButton colorChooser = new JRadioButton();
        ImageIcon colorChooserIco = new ImageIcon("./src/resources/picker.jpg");
        colorChooser.setSize(15,15);
        colorChooser.setIcon(colorChooserIco);
        colorChooser.addActionListener(this::colorChooser);


        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(cursorButton);
        buttonGroup.add(linesButton);
        buttonGroup.add(stampButton);
        buttonGroup.add(colorChooser);

        toolsBar.add(cursorButton);
        toolsBar.add(linesButton);
        toolsBar.add(stampButton);
        toolsBar.add(colorChooser);

//        toolsBar.setBounds(0,20,640, 33);
        drawPanel.add(toolsBar, BorderLayout.NORTH);
        //drawPanel.setBackground(Color.white);

        //drawPanel.add(menuBar, BorderLayout.NORTH);
        //drawPanel.add(toolsBar, BorderLayout.NORTH);

        frame.setContentPane(drawPanel);
        frame.setLocationRelativeTo(null);
       //frame.getContentPane().add(drawPanel, BorderLayout.SOUTH);
        frame.setVisible(true);
    }

    private JMenu createFileMenu() {
        JMenu file = new JMenu("Файл");
        JMenuItem open = new JMenuItem("Открыть",
                new ImageIcon("images/open.png"));
        JMenuItem exit = new JMenuItem(new ExitAction());
        // Добавление к пункту меню изображения
        exit.setIcon(new ImageIcon("images/exit.png"));
        // Добавим в меню пункта open
        file.add(open);
        // Добавление разделителя
        file.addSeparator();
        file.add(exit);

        open.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                System.out.println ("ActionListener.actionPerformed : open");
            }
        });
        return file;
    }

    private JMenu createViewMenu()
    {
        // создадим выпадающее меню
        JMenu viewMenu = new JMenu("Вид");
        // меню-флажки
        JCheckBoxMenuItem line  = new JCheckBoxMenuItem("Линейка");
        JCheckBoxMenuItem grid  = new JCheckBoxMenuItem("Сетка");
        JCheckBoxMenuItem navig = new JCheckBoxMenuItem("Навигация");
        // меню-переключатели
        JRadioButtonMenuItem one = new JRadioButtonMenuItem("Одна страница");
        JRadioButtonMenuItem two = new JRadioButtonMenuItem("Две страницы");
        // организуем переключатели в логическую группу
        ButtonGroup bg = new ButtonGroup();
        bg.add(one);
        bg.add(two);
        // добавим все в меню
        viewMenu.add(line);
        viewMenu.add(grid);
        viewMenu.add(navig);
        // разделитель можно создать и явно
        viewMenu.add( new JSeparator());
        viewMenu.add(one);
        viewMenu.add(two);
        return viewMenu;
    }

    class ExitAction extends AbstractAction {
        private static final long serialVersionUID = 1L;
        ExitAction() {
            putValue(NAME, "Выход");
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

    private void colorChooser(ActionEvent e) { drawPanel.colorChooser(); }

}
