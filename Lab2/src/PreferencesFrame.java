import filters.Emboss;

import javax.swing.*;
import java.awt.image.BufferedImage;

public class PreferencesFrame extends JFrame {

    private int embossBrightness = 0;

    protected void embossPreferences(BufferedImage image, JPanel panel) {
        setSize(300, 150);
        setLocationRelativeTo(null);
        setLayout(null);
        setTitle("Preferences");
        setResizable(false);
        JSlider embossBrightnessSlider = new JSlider(0, 150);
        embossBrightnessSlider.setValue(embossBrightness);
        JLabel embossBrightnessLabel = new JLabel("Brightness");
        embossBrightnessLabel.setBounds(15, 15, 100, 20);
        embossBrightnessSlider.setBounds(80, 17, 160, 20);

        JTextField currentEmbossBrightness = new JTextField(String.valueOf(embossBrightness));
        currentEmbossBrightness.setBounds(250, 15, 30, 20);

        embossBrightnessSlider.addChangeListener(e -> {
            embossBrightness = embossBrightnessSlider.getValue();
            currentEmbossBrightness.setText(String.valueOf(embossBrightness));
        });

        currentEmbossBrightness.addActionListener(e -> {
            if (Character.isLetter(currentEmbossBrightness.getText().charAt(0))) {
                JOptionPane.showMessageDialog(this, "Letters are NOT NUMBERS!!!");
                currentEmbossBrightness.setText(String.valueOf(embossBrightness));
                return;
            }
            var newValue = Integer.parseInt(currentEmbossBrightness.getText());
            if (newValue >= 0 && newValue <= 150) {
                embossBrightness = newValue;
                embossBrightnessSlider.setValue(embossBrightness);
            } else {
                JOptionPane.showMessageDialog(this, "Brightness only >=0 && <=150");
            }
        });

        JButton processButton = new JButton("Emboss");
        processButton.addActionListener(e -> {
            Emboss.process(image);
            panel.repaint();
        });
        processButton.setBounds(120, 75, 60, 15);

        add(processButton);
        add(embossBrightnessLabel);
        add(embossBrightnessSlider);
        add(currentEmbossBrightness);
        setVisible(true);
    }
}
