package nu.epsilon.rss.ui.components.dark;

import java.awt.Color;
import javax.swing.JTextField;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class DarkTextField extends JTextField {

    public DarkTextField() {
        setBackground(Color.DARK_GRAY);
        setForeground(Color.WHITE);
        setCaretColor(Color.WHITE);
        setBorder(new CompoundBorder(new LineBorder(Color.LIGHT_GRAY, 1), new EmptyBorder(2, 2, 2, 2)));
    }

    public DarkTextField(int i) {
        this();
        setColumns(i);
    }

    public DarkTextField(String text, int columns) {
        this();
        setText(text);
        setColumns(columns);
    }
}
