package nu.epsilon.rss.ui.components;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import javax.swing.JComponent;
import javax.swing.JTextField;
import nu.epsilon.rss.rssmanager.FeedItem;
import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.TimingTarget;

/**
 *
 * @author Pär Sikö
 */
public class DoubleSidedComponent extends JComponent implements TimingTarget {

    private FeedComponent front;
    private JComponent back;
    private JComponent currentlyVisibleComponent;
    private Animator animator;
    private double degrees = 1.0;
    private double percentOfTotalAnimation = 0;
    private boolean componentChanged = false;
    private int maxWidth;
    private int maxHeight;
    private MouseListener parentMouseListener;
    private MouseMotionListener parentMouseMotionListener;
    // Component width. This attribute affects both sides of the component.
    private int componentWidth = 500;
    private Component parent;

    public DoubleSidedComponent(MouseListener mouseListener,
            MouseMotionListener motionListener, FeedItem item, Component parent, boolean isNew) {

        this.parent = parent;
        parentMouseListener = mouseListener;
        parentMouseMotionListener = motionListener;
        front = new FeedComponent(this,isNew);
        front.addFeedInfo(item);

        back = new JTextField();

        setBackground(Color.BLACK);
        int w1 = (int) front.getPreferredSize().getWidth();
        int h1 = (int) front.getPreferredSize().getHeight();
        int w2 = (int) back.getPreferredSize().getWidth();
        int h2 = (int) back.getPreferredSize().getHeight();
        maxWidth = Math.max(w1, w2);
        maxHeight = Math.max(h1, h2);

        front.setSize(new Dimension(maxWidth, maxHeight));
        front.setPreferredSize(new Dimension(maxWidth, maxHeight));

        currentlyVisibleComponent = this.front;
        setLayout(new FlowLayout(FlowLayout.CENTER));
        add(currentlyVisibleComponent);

    }

    /**
     * Change side of this double sided component. The flip is animated.
     */
    public void flip() {
        if (animator == null) {
            back.setBounds(front.getBounds());
            back.setSize(front.getSize());
            back.setPreferredSize(front.getSize());
            animator = new Animator(500, this);
            animator.setResolution(10);
            animator.setAcceleration(0.499f);
            animator.setDeceleration(0.501f);
            animator.setRepeatBehavior(Animator.RepeatBehavior.REVERSE);

        }
        animator.start();

    }

    /**
     * Gets the preferred size.
     * 
     * @return preferred size.
     */
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(componentWidth, (int) front.getPreferredSize().getHeight());
    }

    /**
     * Gets the size.
     * 
     * @return the size.
     */
    @Override
    public Dimension getSize() {
        return new Dimension(componentWidth, (int) front.getPreferredSize().getHeight());
    }

    /**
     * Paints this component.
     * 
     * @param _g
     */
    @Override
    public void paintComponent(Graphics _g) {

        if (animator != null && animator.isRunning()) {
            BufferedImage image = new BufferedImage(currentlyVisibleComponent.getWidth(),
                    currentlyVisibleComponent.getHeight(), BufferedImage.TYPE_INT_ARGB);

            Graphics2D gg = image.createGraphics();

            AffineTransform transform = new AffineTransform();
            transform.translate(currentlyVisibleComponent.getWidth() * percentOfTotalAnimation, 0);
            transform.scale(degrees, 1);

            gg.setTransform(transform);

            currentlyVisibleComponent.paint(gg);

            Graphics2D g = (Graphics2D) _g;
            g.drawImage(image, (int) currentlyVisibleComponent.getBounds().getX(), (int) currentlyVisibleComponent.getBounds().
                    getY(), null);
        } else {
            super.paintComponent(_g);
        }

    }

    public void timingEvent(float percentage) {

        degrees = Math.abs(1 - (2 * percentage));
        percentOfTotalAnimation = (-degrees / 2) + 0.5;

        if (percentage > 0.5 && !componentChanged) {
            componentChanged = true;
            if (currentlyVisibleComponent == front) {
                currentlyVisibleComponent = back;
            } else {
                currentlyVisibleComponent = front;
            }
        }
        repaint();
    }

    public void begin() {
        componentChanged = false;
        removeAll();
        validate();
        degrees = 1;
    }

    public void end() {
        add(currentlyVisibleComponent);
        validate();
    }

    public void repeat() {
    }

    public void mousePressed(MouseEvent e) {
        parentMouseListener.mousePressed(e);
    }

    public void mouseReleased(MouseEvent e) {
        parentMouseListener.mouseReleased(e);
    }

    public void mouseDragged(MouseEvent e) {
        parentMouseMotionListener.mouseDragged(e);
    }

    public void updateGraphics() {
        repaint();
        parent.repaint();
    }
}
