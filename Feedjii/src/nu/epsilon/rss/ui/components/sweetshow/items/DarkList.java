package nu.epsilon.rss.ui.components.sweetshow.items;

import java.awt.Color;
import javax.swing.JList;

public class DarkList extends JList {

    public DarkList() {
        setBackground(Color.BLACK);
        setSelectionBackground(Color.DARK_GRAY);
        setSelectionForeground(Color.WHITE);
        setForeground(Color.WHITE);
    }
}
