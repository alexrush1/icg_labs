import filters.Emboss;
import filters.Floyd;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class PreferencesFrame extends JFrame {

    private int floydDots = 2;

    protected void floydPreferences(BufferedImage image, JPanel panel) {
        setSize(300, 150);
        setLocationRelativeTo(null);
        setLayout(null);
        setTitle("Preferences");
        setResizable(false);
        JSlider floydDotsSlider = new JSlider(2, 128);
        floydDotsSlider.setValue(floydDots);
        JLabel floydDotsLabel = new JLabel("Dots");
        floydDotsLabel.setBounds(15, 15, 100, 20);
        floydDotsSlider.setBounds(80, 17, 160, 20);

        JTextField currentFloydDots = new JTextField(String.valueOf(floydDots));
        currentFloydDots.setBounds(250, 15, 30, 20);

        floydDotsSlider.addChangeListener(e -> {
            floydDots = floydDotsSlider.getValue();
            currentFloydDots.setText(String.valueOf(floydDots));
        });

        currentFloydDots.addActionListener(e -> {
            if (Character.isLetter(currentFloydDots.getText().charAt(0))) {
                JOptionPane.showMessageDialog(this, "Letters are NOT NUMBERS!!!");
                currentFloydDots.setText(String.valueOf(floydDots));
                return;
            }
            var newValue = Integer.parseInt(currentFloydDots.getText());
            if (newValue >= 2 && newValue <= 128) {
                floydDots = newValue;
                floydDotsSlider.setValue(floydDots);
            } else {
                JOptionPane.showMessageDialog(this, "Dots only >=2 && <=128");
            }
        });

        JButton processButton = new JButton("Emboss");
        processButton.addActionListener(e -> {
            this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            Floyd.process(image, floydDots);
            panel.repaint();
            this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        });
        processButton.setBounds(120, 75, 60, 15);

        add(processButton);
        add(floydDotsLabel);
        add(floydDotsSlider);
        add(currentFloydDots);
        setVisible(true);
    }
}
