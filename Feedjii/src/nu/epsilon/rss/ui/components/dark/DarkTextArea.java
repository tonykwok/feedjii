package nu.epsilon.rss.ui.components.dark;

import java.awt.Color;
import javax.swing.JTextArea;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

/**
 *
 * @author Martin Gunnarsson <martin.gunnarsson@xplora.info>
 */
public class DarkTextArea extends JTextArea {

    public DarkTextArea() {
        this("");
    }

    public DarkTextArea(int rows, int cols, String text) {
        super(text, rows, cols);
        initComponent();
    }

    public DarkTextArea(String text) {
        super(text);
        initComponent();
    }

    private void initComponent() {
        setBackground(Color.DARK_GRAY);
        setForeground(Color.WHITE);
        setCaretColor(Color.WHITE);
        setBorder(new CompoundBorder(new LineBorder(Color.LIGHT_GRAY, 1), new EmptyBorder(2, 2, 2, 2)));
    }

}
