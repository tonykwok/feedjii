package nu.epsilon.rss.ui.components.dark;

import java.awt.Color;
import javax.swing.JCheckBox;

/**
 *
 * @author Pär Sikö
 */
public class DarkCheckBox extends JCheckBox {

    public DarkCheckBox() {
        setOpaque(false);
        setBackground(Color.DARK_GRAY);
        setForeground(Color.WHITE);
    }

    public DarkCheckBox(String text) {
        this();
        setText(text);
    }
}
