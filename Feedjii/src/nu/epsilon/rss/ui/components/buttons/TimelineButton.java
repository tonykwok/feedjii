/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nu.epsilon.rss.ui.components.buttons;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JComponent;
import nu.epsilon.rss.repository.ResourceManager;

/**
 *
 * @author j
 */
public class TimelineButton extends JComponent {

    private boolean pressed;
    private Image up;
    private Image down;

    public TimelineButton() {

        this.up = ResourceManager.loadImage("images/", "timelinebutton.png");
        this.down = ResourceManager.loadImage("images/", "timelinebutton_pressed.png");
        this.setPreferredSize(new Dimension(up.getWidth(null), up.getHeight(null)));

        addMouseListener(new MouseAdapter() {

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                pressed = !pressed;
                repaint();
            }
        });
    }

    public boolean isPressed() {
        return pressed;
    }

    @Override
    public void paintComponent(Graphics g) {
        g.drawImage(this.pressed ? down : up, 0, 0, null);
    }
}
