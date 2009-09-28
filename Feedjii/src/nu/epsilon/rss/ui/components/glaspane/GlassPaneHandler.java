/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package nu.epsilon.rss.ui.components.glaspane;

import java.awt.Component;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author Pär Sikö
 */
public class GlassPaneHandler {
    
    private JFrame parent;
    
    private final JPanel glassPane = new JPanel(null);
    
    private final static GlassPaneHandler INSTANCE = new GlassPaneHandler();
    
    public static GlassPaneHandler getInstance() {
        return INSTANCE;
    }
    
    private GlassPaneHandler() {
        glassPane.setOpaque(false);
    }

    public void addComponentToGlassPane(JComponent component) {
        glassPane.add(component);
        glassPane.validate();
        parent.setGlassPane(glassPane);
        glassPane.setVisible(true);
    }

    public void setOpaque(boolean opaque) {
        glassPane.setOpaque(opaque);
    }

    public void hideGlassPane() {
        glassPane.setVisible(false);
    }
    
    public void removeComponentFromGlassPane(JComponent component) {
        glassPane.remove(component);
        glassPane.validate();
    }
    
    public void setFrame(JFrame frame) {
        parent = frame;
    }
    
}
