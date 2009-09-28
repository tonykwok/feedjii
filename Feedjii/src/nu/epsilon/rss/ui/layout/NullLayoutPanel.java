package nu.epsilon.rss.ui.layout;

import java.awt.Color;
import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JComponent;
import javax.swing.JPanel;

/**
 *
 * @author Pär Sikö
 */
public class NullLayoutPanel extends JPanel implements PropertyChangeListener {

    public NullLayoutPanel(JComponent comp) {
        comp.addPropertyChangeListener("size", this);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        setSize(new Dimension((int) ((Dimension) evt.getNewValue()).getWidth(), getHeight()));
    }    
    
}
