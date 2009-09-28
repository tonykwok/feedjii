package nu.epsilon.rss.ui.components.buttons;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import javax.swing.JComponent;
import nu.epsilon.rss.repository.ResourceManager;

/**
 *
 * @author Pär Sikö
 */
public abstract class ComponentButton extends JComponent implements MouseListener {

    protected BufferedImage button;
    protected BufferedImage buttonPressed;
    protected BufferedImage buttonMouseOver;
    protected ResourceManager manager;
    private Image currentImage;

    public ComponentButton() {
        manager = ResourceManager.getInstance();
        loadImages();
        currentImage = button;
        addMouseListener(this);
    }

    public abstract void loadImages();

    public abstract void buttonPressed();

    @Override
    protected void paintComponent(Graphics _g) {
        super.paintComponent(_g);
        Graphics2D g = (Graphics2D) _g;
        g.drawImage(currentImage, 0, 0, null);

    }

    @Override
    public Dimension getPreferredSize() {
        int xMax = Math.max(button.getWidth(), buttonMouseOver.getWidth());
        xMax = Math.max(xMax, buttonPressed.getWidth());

        int yMax = Math.max(button.getHeight(), buttonMouseOver.getHeight());
        yMax = Math.max(yMax, buttonPressed.getHeight());

        return new Dimension(xMax, yMax);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        currentImage = buttonPressed;
        buttonPressed();
        repaint();
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        currentImage = buttonMouseOver;
        repaint();
    }

    @Override
    public void mouseExited(MouseEvent e) {
        currentImage = button;
        repaint();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        currentImage = buttonPressed;
        repaint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        currentImage = buttonMouseOver;
        repaint();
    }
}
