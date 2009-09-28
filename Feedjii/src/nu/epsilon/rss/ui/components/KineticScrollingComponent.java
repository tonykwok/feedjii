package nu.epsilon.rss.ui.components;

import java.beans.PropertyChangeEvent;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;
import nu.epsilon.rss.ui.layout.NullLayoutPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Point;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import nu.epsilon.rss.common.PerformanceTuning;
import nu.epsilon.rss.rssmanager.FeedItem;
import nu.epsilon.rss.ui.utils.FPSCounter;
import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.TimingTarget;

/**
 * A scrolling component that adds a nice deceleration effect to the animation.
 *
 * TODO: horizontal scrolling
 *		
 * 
 * @author Pär Sikö
 */
public class KineticScrollingComponent extends JComponent implements
        MouseListener,
        MouseMotionListener,
        PropertyChangeListener {

    private JPanel feedPanel;
    private double currentYPosition = 0;
    private double startYPositionWhenDraging = 0;
    private Animator slideAnimator;
    private int startPoint;
    private long startTime;
    // Every event generated due to the fact that we are dragging the components generates
    // a Point object and we need to keep track of the previous Point to know how many pixels
    // the user has moved the mouse.
    private int startingPoint;
    private JFrame parent;
    private int frames = 0;
    private Map<FeedItem, DoubleSidedComponent> items =
            new HashMap<FeedItem, DoubleSidedComponent>();

    /**
     * 
     */
    public KineticScrollingComponent(JFrame parent) {
        this.parent = parent;
        setLayout(null);

        // TODO: DO WE NEED NullLayoutPanel?
        feedPanel = new NullLayoutPanel(this);
        feedPanel.setOpaque(false);
        addComponentListener(new ComponentListener() {

            @Override
            public void componentResized(ComponentEvent e) {
                firePropertyChange("size", null, getSize());
            }

            @Override
            public void componentMoved(ComponentEvent e) {
            }

            @Override
            public void componentShown(ComponentEvent e) {
            }

            @Override
            public void componentHidden(ComponentEvent e) {
            }
        });

        addMouseListener(this);
        addMouseMotionListener(this);
        feedPanel.setSize(new Dimension(600, 0));
        feedPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        add(feedPanel);
    }

    /**
     * Adds a feed to the feed list.
     * 
     * @param item feed item to add to list.
     * @param isNew true if this feed is new and should be displayed
     *        differently from old ones.
     */
    public void addFeed(FeedItem item, Boolean isNew) {
        if (!items.keySet().contains(item)) {
            DoubleSidedComponent comp = new DoubleSidedComponent(this, this,
                    item, this, isNew);
            items.put(item, comp);
            feedPanel.add(comp);
            feedPanel.setSize(new Dimension(feedPanel.getWidth(), feedPanel.
                    getHeight() + (int) comp.getSize().
                    getHeight()));
            feedPanel.validate();

        }
    }

    /**
     * Removes all visible feeds from the feed list.
     */
    public void removeAllFeeds() {
        items.clear();
        feedPanel.removeAll();
        feedPanel.setSize(new Dimension(feedPanel.getWidth(), 0));
        validate();
        repaint();
    }

    /**
     * Removes a specific feed from the feed list. If the list doesn't
     * contain the specific feed nothing happens.
     * 
     * @param item feed to remove.
     */
    public void removeFeed(FeedItem item) {
        if (items.keySet().contains(item)) {

            feedPanel.remove(items.get(item));
            feedPanel.setSize(new Dimension(feedPanel.getWidth(), feedPanel.
                    getHeight() - (int) items.get(item).getSize().
                    getHeight()));
            items.remove(item);
            feedPanel.validate();
        }
    }

    /**
     * Ensures that the top of the feedPanel is visible. This is
     * important when the feed model changes filter and the resulting
     * list of feeds is smaller then the previous. This situation will
     * lead to new visible list that is outside of the screen bounds.
     */
    public void ensureFeedPanelTopIsVisible() {
        feedPanel.setLocation(0, 0);
        currentYPosition = 0;
    }

    @Override
    public void paint(Graphics _g) {
        frames++;

        Graphics2D g = (Graphics2D) _g;
        super.paint(_g);

        // paints the "drop shadow" effect under the top gray field containing the search.
        Paint oldPaint = g.getPaint();
        int height = 8;
        g.setPaint(new GradientPaint(0F, 0F, Color.BLACK, 0F, (float) height,
                new Color(0, 0, 0, 0)));
        g.fillRect(24, 0, getWidth() - 48, height);
        g.setPaint(oldPaint);
    }

    @Override
    public void setBackground(Color color) {
        feedPanel.setBackground(color);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (slideAnimator != null && slideAnimator.isRunning()) {
            slideAnimator.stop();
        }
        startPoint = e.getYOnScreen();
        startTime = System.currentTimeMillis();
    }

    @Override
    public void mouseReleased(MouseEvent e) {

        double time = (System.currentTimeMillis() - startTime) / 1000d;
        int yDist = e.getYOnScreen() - startPoint;
        final double pixelsPerSecond = yDist / time;

        // added
        final double dist = pixelsPerSecond * 0.6;
        final double originalStart = feedPanel.getY();
        startingPoint = 0;

        slideAnimator = new Animator(1000, new TimingTarget() {

            @Override
            public void timingEvent(float arg0) {

                if (Math.abs(currentYPosition - (originalStart + dist * arg0)) <
                        0.5f) {
                    return;
                }

                currentYPosition = originalStart + dist * arg0;
                feedPanel.setLocation(0, (int) currentYPosition);
                System.out.println("KineticScrolling: " + SwingUtilities.isEventDispatchThread());
                repaint();
            }

            @Override
            public void begin() {
            }

            @Override
            public void end() {

                System.out.println("FPS:" + frames);
                FPSCounter counter = FPSCounter.getInstance();
                counter.setFPS(frames);
                frames = 0;
            }

            @Override
            public void repeat() {
            }
        });
        if (PerformanceTuning.isUseNanoSource()) {
            slideAnimator.setTimer(new NanoSource());
        }
        System.out.println("res: " + PerformanceTuning.getResolution());
        slideAnimator.setResolution(PerformanceTuning.getResolution());
        slideAnimator.setAcceleration(0f);
        slideAnimator.setDeceleration(1f);
        slideAnimator.start();
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (startingPoint == 0) {
            startYPositionWhenDraging = currentYPosition;
            startingPoint = e.getYOnScreen();
        } else {
            int yDiff = e.getYOnScreen() - startingPoint;

            currentYPosition = startYPositionWhenDraging + yDiff;
            if (currentYPosition <= 0 && currentYPosition +
                    feedPanel.getHeight() >= getHeight() - 10) {
                feedPanel.setLocation(0, (int) currentYPosition);
            }

        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        @SuppressWarnings("unchecked")
        Map<FeedItem, Boolean> newItems = (Map<FeedItem, Boolean>) evt.
                getNewValue();
        updateFeedList(newItems);
    }

    private void updateFeedList(Map<FeedItem, Boolean> newItems) {
        Point location = getLocation();
        removeAllFeeds();

        for (FeedItem item : newItems.keySet()) {
            Boolean b = newItems.get(item);
            addFeed(item, b);
        }
        ensureFeedPanelTopIsVisible();
        setLocation(location);
        validate();
        repaint();
    }
}
