package nu.epsilon.rss.ui.sweetfadepanel;

import java.awt.AlphaComposite;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import javax.swing.JComponent;
import javax.swing.JFrame;
import nu.epsilon.rss.ui.components.glaspane.GlassPaneHandler;
import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.TimingTarget;

/**
 * SweetTransformPanel transforms it's size between a small icon like component
 * and a full blown component. We use it instead of boring menus and buttons.
 * 
 * Usage:
 * <code>
 *      fade = new SweetTransformPanel(this);
 *      fade.setLocation(10, 100);
 *      fade.setMaximumSize(0, 0, 300, 300);
 *      JPanel panel = new JPanel(null);        // Yes null layout but we don't care.
 *      panel.add(fade);
 * </code>
 * 
 * @author Pär Sikö
 */
public class SweetTransformPanel extends JComponent {

    private Dimension preferredSizeWhenHidden;
    private JFrame parent;
    private Dimension originalDimension;
    private int maxWidth,  maxHeight;
    private boolean isExpanded = false;
    private Component iconComponent;
    private Component fullComponent;
    private BufferedImage iconImage;
    private GlassPaneAnimator gpAnimator;

    /**
     * Constructs a new transform component. Use this component to
     * fade/resize between two component. 
     * 
     * @param parent parent JFrame. We use the JFrames glass pane to
     * animate.
     */
    public SweetTransformPanel(JFrame parent) {
        this.parent = parent;
        preferredSizeWhenHidden = new Dimension(80, 80);
        setSize(preferredSizeWhenHidden);
        setPreferredSize(preferredSizeWhenHidden);
        setOpaque(true);
        this.originalDimension = getSize();
    }

    /**
     * Sets the maximum size of the end component.
     * 
     * @param width maximum width.
     * @param height maximum height.
     */
    public void setMaximumSize(int width, int height) {
        maxWidth = width;
        maxHeight = height;
    }

    /**
     * Sets the icon component. This is a small component that should
     * represent the full component that is exposed when expanding.
     * 
     * @param c icon component.
     */
    public void setIconComponent(Component c) {
        removeAll();
        iconComponent = c;
        add(iconComponent);
        iconComponent.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                transform();
            }
        });
        iconComponent.setSize(80, 80);
        iconImage = new BufferedImage(iconComponent.getWidth(), iconComponent.getWidth(), BufferedImage.TYPE_INT_ARGB);
        iconComponent.paint(iconImage.getGraphics());
    }

    /**
     * Sets the full component. This is a complete component that should
     * contain JTextFields, JLabel and other graphics component.
     * 
     * @param c full component.
     */
    public void setFullComponent(Component c) {
        fullComponent = c;
        fullComponent.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                Rectangle contractArea = new Rectangle(fullComponent.getWidth() - 30, 0, 30, 30);
                if (contractArea.contains(e.getPoint())) {
                    transform();
                }
            }
        });
        fullComponent.setSize(maxWidth, maxHeight);
    }

    /**
     * Starts the transformation between icon and full component.
     */
    public void transform() {
        if (gpAnimator == null || !gpAnimator.isAnimating()) {
            if (isExpanded) {
                contract();
            } else {
                expandTo();
            }
            isExpanded = !isExpanded;
        }
    }

    private void expandTo() {
        Point location = iconComponent.getLocationOnScreen();
        Point parentLocation = parent.getLocationOnScreen();

        gpAnimator = new GlassPaneAnimator(iconComponent, fullComponent, parent);
        gpAnimator.setStartSize(80, 80);
        gpAnimator.setEndSize(fullComponent.getWidth(), fullComponent.getHeight());

        gpAnimator.setStartLocation(20, location.y - parentLocation.y);
        gpAnimator.setEndLocation(20, location.y - parentLocation.y);
        gpAnimator.setSize(900, 900);
        GlassPaneHandler.getInstance().addComponentToGlassPane(gpAnimator);
        gpAnimator.startAnimation();

    }

    private void contract() {
        gpAnimator.reverseAnimation();
    }

    private class GlassPaneAnimator extends JComponent implements TimingTarget {

        private Component from;
        private Component to;
        private int startX;
        private int startY;
        private int endX;
        private int endY;
        private int startWidth;
        private int startHeight;
        private int endWidth;
        private int endHeight;
        private BufferedImage startImage;
        private BufferedImage endImage;
        private Animator animator;
        private float fraction = 0;
        private Component parent;
        private boolean isExpanded = false;

        public GlassPaneAnimator(Component f, Component t, Component parent) {
            setLayout(null);
            this.parent = parent;
            this.from = f;
            this.to = t;
            startImage = new BufferedImage(from.getWidth(), from.getHeight(), BufferedImage.TYPE_INT_ARGB);
            endImage = new BufferedImage(to.getWidth(), to.getHeight(), BufferedImage.TYPE_INT_ARGB);
            from.print(startImage.getGraphics());
            to.print(endImage.getGraphics());
            animator = new Animator(350, this);
            animator.setResolution(0);
            animator.setRepeatBehavior(Animator.RepeatBehavior.REVERSE);
        }

        public void setStartLocation(int x, int y) {
            this.startX = x;
            this.startY = y;
        }

        public void setEndLocation(int x, int y) {
            this.endX = x;
            this.endY = y;
        }

        public void setStartSize(int width, int height) {
            this.startWidth = width;
            this.startHeight = height;
        }

        public void setEndSize(int width, int height) {
            this.endWidth = width;
            this.endHeight = height;
        }

        public void startAnimation() {
            isExpanded = true;
            animator.start();
        }

        public void reverseAnimation() {
            isExpanded = false;
            BufferedImage tmp = startImage;
            startImage = endImage;
            endImage = tmp;

            int tmp1 = startWidth;
            startWidth = endWidth;
            endWidth = tmp1;

            tmp1 = startHeight;
            startHeight = endHeight;
            endHeight = tmp1;

            tmp1 = startX;
            startX = endX;
            endX = tmp1;

            tmp1 = startY;
            startY = endY;
            endY = tmp1;

            animator.start();
        }

        public boolean isAnimating() {
            return animator != null ? animator.isRunning() : false;
        }

        public void cancel() {
            animator.cancel();
        }

        @Override
        protected void paintComponent(Graphics _g) {
            if (animator.isRunning()) {
                Graphics2D g = (Graphics2D) _g;
                int width = (int) (startWidth + (endWidth - startWidth) * fraction);
                int height = (int) (startHeight + (endHeight - startHeight) * fraction);
                int x = (int) (startX + (endX - startX) * fraction);
                int y = (int) (startY + (endY - startY) * fraction);

                g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1 - fraction));
                g.drawImage(startImage, x, y, width, height, null);

                g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, fraction));
                g.drawImage(endImage, x, y, width, height, null);
            }
        }

        @Override
        public void timingEvent(float arg0) {
            fraction = arg0;
            repaint();
        }

        @Override
        public void begin() {
            if (isExpanded) {
                from.setVisible(false);
            } else {
                to.setVisible(false);
            }
            validate();
            parent.repaint();
        }

        @Override
        public void end() {
            if (!isExpanded) {
                from.setVisible(true);
                setVisible(false);
            } else {
                to.setLocation(endX, endY);
                add(to);
                to.setVisible(true);
            }
            validate();
            parent.repaint();
        }

        @Override
        public void repeat() {
        }
    }
}
