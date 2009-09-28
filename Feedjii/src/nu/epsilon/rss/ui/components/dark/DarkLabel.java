package nu.epsilon.rss.ui.components.dark;

import java.awt.Color;
import javax.swing.JLabel;

public class DarkLabel extends JLabel {

    public DarkLabel() {
        setForeground(Color.WHITE);
    }

    public DarkLabel(String text) {
        this();
        setText(text);
    }
}
