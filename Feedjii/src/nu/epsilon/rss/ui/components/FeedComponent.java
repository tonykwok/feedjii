package nu.epsilon.rss.ui.components;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.Image;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Transparency;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.net.URI;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComponent;
import nu.epsilon.rss.common.CharConverter;
import nu.epsilon.rss.common.PerformanceTuning;
import nu.epsilon.rss.filter.Filter;
import nu.epsilon.rss.profiles.ProfileManager;
import nu.epsilon.rss.repository.ResourceManager;
import nu.epsilon.rss.rssmanager.FeedItem;
import nu.epsilon.rss.ui.backend.BackendUIImpl;
import nu.epsilon.rss.ui.utils.DrawUtil;
import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.TimingTarget;

/**
 *
 * @author Pär Sikö
 */
public class FeedComponent extends JComponent implements MouseListener,
        MouseMotionListener, TimingTarget {

    // Feed info contains information that display.
    private FeedItem feedItem;
    // Ordinary component size when not maximized.
    private int componentHeight = 100;
    // Parent component contains a flip() method that starts the flip animation.
    private DoubleSidedComponent feedContainer;
    // Controls the direction we are scrolling.
    private boolean directionUp = false;
    // Animator takes care of timing when animating movement.
    private Animator animator;
    // Component will increate/decrease by this amount when single clicking.
    private int additionalHeightWhenFocsed = 200;
    // Components height when it's not been clicked.
    private final int componentHeightNotFocused = 100;
    // Indicates whether this component is visible on the glass pane.
    // If true a special paint method is invoked.
    private boolean isVisibleOnGlassPane = false;
    // Components size on glass pane is not necessarily the same as when visible
    // on the content pane.
    private Dimension preferredSizeOnGlassPane;
    // Rectangle in which we paint the mail sympol. If a mouse click is inside
    // the rectangle then the component will turn around.
    private Rectangle mailRect;
    private Rectangle favouriteRect;
    private Rectangle openRect;
    // Indicates whether this is a new feed or not.
    private boolean isNew;
    // Improve performance by caching the component graphics.
    private BufferedImage bufferedFeedImage;
    // We need this to be able to format the dates properly.
    private static DateFormat dateFormatter = new SimpleDateFormat(
            "yyyy-MM-dd hh:mm");
    private ProfileManager profileManager = (ProfileManager) BackendUIImpl.
            getInstance().getResource("profilemanager");
    private boolean isMouseOver = false;
    private boolean isFavourite = false;
    private final static Image WORLD_ICON = ResourceManager.loadImage("images/",
            "world.png");
    private final static Image EMAIL_ICON = ResourceManager.loadImage("images/",
            "email_go.png");
    private final static Image HEART_ICON = ResourceManager.loadImage("images/",
            "heart.png");
    private final static Image WORLD_ICON_GRAY = ResourceManager.loadImage(
            "images/", "world_gray.png");
    private final static Image EMAIL_ICON_GRAY = ResourceManager.loadImage(
            "images/", "email_go_gray.png");
    private final static Image HEART_ICON_GRAY = ResourceManager.loadImage(
            "images/", "heart_gray.png");
    private static final Map<Integer, BufferedImage> gradientCache =
            new HashMap<Integer, BufferedImage>();
    private Logger logger = Logger.getLogger("nu.epsilon.rss.ui.components");
    private int additionalRows = 0;
    public static String quickSearch = "";

    /**
     * Constructs a graphical feed item component. This component is
     * basically a rectangle with feed information displayed in the
     * visible area.
     * 
     * @param parentContainer is used to determine the width and height. We also
     *		propagate mouse events to the parent container and call flip when
     *		it's time to turn the component around.
     * @param handler handles the glass pane that is used to displayed 
     *		"full screen feed items" on top of the frame.
     * @param paint gradient paint shared by all instances of FeedComponent.
     */
    public FeedComponent(DoubleSidedComponent parentContainer, boolean isNew) {
        this.isNew = isNew;
        addMouseListener(this);
        addMouseMotionListener(this);
        feedContainer = parentContainer;
        preferredSizeOnGlassPane = new Dimension(400, 200);
    }

    /**
     * Sets the feed to display. When painting this component we use attributes
     * from feedItem.
     * 
     * @param feedItem item to use when painting.
     */
    public void addFeedInfo(FeedItem feedItem) {
        feedItem.description = CharConverter.convertChars(feedItem.description);
        feedItem.title = CharConverter.convertChars(feedItem.title);
        this.feedItem = feedItem;
    }

    /**
     * Gets the preferred size. The height is determined by componentHeight and
     * the width is determined by the parent component. This is due to the fact
     * that we are part of a double sided component and we need to keep the width
     * consistent.
     * 
     * @return this component's preferred size.
     */
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(feedContainer.getWidth() - 0, componentHeight);
    }

    /**
     * Gets the size. The height is determined by componentHeight and
     * the width is determined by the parent component. This is due to the fact
     * that we are part of a double sided component and we need to keep the width
     * consistent.
     * 
     * @return this component's size.
     */
    @Override
    public Dimension getSize() {
        return new Dimension(feedContainer.getWidth() - 0, componentHeight);
    }

    private BufferedImage getGradientImage() {
        int yLocation = getParent().getY() + getParent().getParent().getY();
        if (!gradientCache.keySet().contains(yLocation) ||
                (animator != null && animator.isRunning()) || componentHeight !=
                100) {

            BufferedImage img;
            if (PerformanceTuning.isUseCompatibleImages()) {
                int transparency = PerformanceTuning.isUseOpaqueImages() ? Transparency.OPAQUE
                        : Transparency.TRANSLUCENT;
                img = getGraphicsConfiguration().createCompatibleImage(
                        1, componentHeight - 6, transparency);
            } else {
                img = new BufferedImage(1, componentHeight - 6,
                        BufferedImage.TYPE_INT_RGB);
            }

            Graphics2D g = (Graphics2D) img.getGraphics();
            g.setPaint(getGradientPaint());
            g.fillRect(0, 0, img.getWidth(), img.getHeight());
            gradientCache.put(yLocation, img);
        }
        return gradientCache.get(yLocation);
    }

    private Paint getGradientPaint() {
        Component parent = getParent();
        Component grandParent = parent.getParent();
        int globalY = grandParent.getY() + parent.getY();
        int windowHeight = getTopLevelAncestor().getHeight();
        final int darkestColorConstant = 140;

        double percentTop = ((double) globalY) / windowHeight;
        double percentBottom = ((double) globalY + getHeight()) / windowHeight;

        percentTop = Math.max(0D, percentTop);

        int colTop = (int) (255 - percentTop * darkestColorConstant);
        int colBottom = (int) (255 - percentBottom * darkestColorConstant);

        Color top;
        Color bottom;
        if (isFavourite) {
            colTop = Math.max(0, (int) (colTop * 0.8));
            colBottom = Math.max(0, (int) (colBottom * 0.8));

            top = Color.YELLOW;
            bottom = Color.GREEN;

        } else {
            top = new Color(colTop, colTop, colTop);
            bottom = new Color(colBottom, colBottom, colBottom);
        }
        return new GradientPaint(0, 0, top, 0, getHeight(), bottom);
    }

    /**
     * Paints component. What to paint depends on the state that we're in.
     * 
     * @param _g graphics object to paint on.
     */
    @Override
    protected void paintComponent(Graphics _g) {
        // We cannot set the dimension of the rectangle until the parent component and this
        // component is displayed since it depends on the parent size. 
        if (mailRect == null) {
            int xPos = feedContainer.getWidth();
            int yPos = getHeight() - 27;
            openRect = new Rectangle(xPos - 70, yPos, 20, 20);
            favouriteRect = new Rectangle(xPos - 50, yPos, 20, 20);
            mailRect = new Rectangle(xPos - 30, yPos, 20, 20);
        }

        if (isVisibleOnGlassPane) {
            // Special paint method used when this component is visible on the glass pane.
            // The component looks very different on the glass pane.
            paintOnGlassPane(_g);
        } else {
            if (bufferedFeedImage == null || !PerformanceTuning.isCacheImages()) {
                // Convert _g, which is the old kind of graphics object, to the new and 
                // enhanced type. A Graphics2D object is much more powerfull than the predecessor.

                // We need a little bit margin so we set the width to the same as our parent minus 50.
                int width = getWidth();
                // Set the size. The need for -6 is still under investigation.
                int height = componentHeight - 6;

                if (PerformanceTuning.isUseCompatibleImages()) {
                    bufferedFeedImage = getGraphicsConfiguration().
                            createCompatibleImage(width, height,
                            Transparency.TRANSLUCENT);
                } else {
                    bufferedFeedImage = new BufferedImage(width, height,
                            BufferedImage.TYPE_INT_ARGB);
                }

                Graphics2D g = (Graphics2D) bufferedFeedImage.getGraphics();
                // Set rendering hints to make edges look less jagged.
                g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);

                Font currentFont = new Font("Verdana", Font.BOLD, 13);
                g.setFont(currentFont);
                g.setColor(new Color(20, 20, 20));

                int row = 1;

                // Add title
                String title = feedItem.title;

                double w;
                int pos = 0;

                if (title != null && !title.isEmpty()) {
                    do {
                        w = currentFont.getStringBounds(
                                title.substring(0, ++pos), g.
                                getFontRenderContext()).getWidth();
                    } while (w < getWidth() - 30 && pos < title.length());
                    while (pos < title.length() && pos > -1 &&
                            title.charAt(pos) != ' ') {
                        pos--;
                    }

                    g.drawString(feedItem.title.substring(0, pos), 10, 20 *
                            row++);
                    if (pos < title.length()) {
                        g.drawString(feedItem.title.substring(pos + 1), 10, 20 *
                                row++);
                    }
                }


                g.setFont(new Font("Verdana", Font.PLAIN, 11));
                // Add description
                String description =
                        feedItem.description.replaceAll("<.*?>", "");
                if (description != null && !description.isEmpty()) {
                    AttributedString aString = new AttributedString(description);
                    aString.addAttribute(TextAttribute.FONT, new Font("Verdana",
                            Font.PLAIN, 11));
                    markProfileOccurences(aString, description);
                    markQuickSearchOccurences(aString, description, quickSearch);
                    FontRenderContext frc = new FontRenderContext(null, true,
                            true);
                    AttributedCharacterIterator iter = aString.getIterator();
                    LineBreakMeasurer measurer =
                            new LineBreakMeasurer(iter, frc);
                    int y = 20 * row;
                    additionalRows = 0;
                    while (measurer.getPosition() < iter.getEndIndex()) {
                        TextLayout layout = measurer.nextLayout(getWidth() - 20);
                        if (measurer.getPosition() < iter.getEndIndex()) {
                            layout = layout.getJustifiedLayout(getWidth() - 20);
                        }
                        if (y < getHeight() - 20) {
                            layout.draw(g, 10, y);
                        } else {
                            additionalRows++;
                        }
                        y += 16;

                    }
                    additionalHeightWhenFocsed = Math.max(y -
                            componentHeightNotFocused + 20, 0);
                }

                // Add published date
                if (feedItem.publishedDate != null) {
                    g.drawString(dateFormatter.format(feedItem.publishedDate),
                            10, getHeight() - 10);
                }
                if (additionalRows > 1) {
                    g.drawString(additionalRows + " more rows",
                            170, getHeight() - 10);
                } else if (additionalRows == 1) {
                    g.drawString(additionalRows + " more row",
                            170, getHeight() - 10);
                }
                g.drawImage((BufferedImage) WORLD_ICON_GRAY, null, getWidth() -
                        70, getHeight() - 27);
                g.drawImage((BufferedImage) HEART_ICON_GRAY, null, getWidth() -
                        50, getHeight() - 27);
                g.drawImage((BufferedImage) EMAIL_ICON_GRAY, null, getWidth() -
                        30, getHeight() - 27);
                g.dispose();
            }

            Graphics2D g = (Graphics2D) _g;
            if (PerformanceTuning.isUseGradients()) {
                drawBackgroundGradient(g);
            } else {
                g.setColor(Color.LIGHT_GRAY);
                g.fillRoundRect(0, 0, getWidth(), componentHeight - 6, 20, 20);
            }
            g.drawImage(bufferedFeedImage, 0, 0, null);

            DrawUtil.drawRoundEdge(g, Color.WHITE, 0, 0, getWidth() - 1, 10,
                    true, true);

            if (isMouseOver) {
                g.drawImage((BufferedImage) WORLD_ICON, null, getWidth() - 70,
                        getHeight() - 27);
                g.drawImage((BufferedImage) HEART_ICON, null, getWidth() - 50,
                        getHeight() - 27);
                g.drawImage((BufferedImage) EMAIL_ICON, null, getWidth() - 30,
                        getHeight() - 27);
            }

            g.dispose();
        }
    }

    private void drawBackgroundGradient(Graphics2D g) {
        BufferedImage onePixelWideGradient = getGradientImage();

        if (!PerformanceTuning.isUseSoftClip()) {
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            Shape oldClip = g.getClip();
            g.clip(new RoundRectangle2D.Float(0, 0, getWidth(),
                    componentHeight - 6, 10, 10));
            g.drawImage(onePixelWideGradient, 0, 0, getWidth(),
                    onePixelWideGradient.getHeight(), 0, 0, 1,
                    onePixelWideGradient.getHeight(), null);
            g.setClip(oldClip);
            return;
        }

        GraphicsConfiguration gc = g.getDeviceConfiguration();
        int maskWidth = getWidth();
        int maskHeight = componentHeight - 6;

        // This part could be cached, but it's uncertain how much we would gain from this - from here ...
        BufferedImage img = gc.createCompatibleImage(maskWidth, maskHeight,
                Transparency.TRANSLUCENT);
        Graphics2D g2 = img.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2.fillRoundRect(0, 0, maskWidth, maskHeight, 10, 10);
        // ... to here typically takes around 1 ms

        g2.setComposite(AlphaComposite.SrcAtop);
        g2.drawImage(onePixelWideGradient, 0, 0, maskWidth,
                onePixelWideGradient.getHeight(), null);
        g2.dispose();

        g.drawImage(img, 0, 0, null);
    }

    private void markQuickSearchOccurences(AttributedString attributedText,
            String text, String quickSearch) {
        if (text != null && quickSearch != null && !text.isEmpty() &&
                !quickSearch.isEmpty()) {
            int index =
                    text.toLowerCase().indexOf(quickSearch.toLowerCase());
            while (index > -1) {
                attributedText.addAttribute(TextAttribute.FONT, new Font(
                        "Verdana", Font.BOLD, 11), index, index +
                        quickSearch.length());
                attributedText.addAttribute(TextAttribute.FOREGROUND,
                        new Color(36, 35, 3), index, index +
                        quickSearch.length());
                attributedText.addAttribute(TextAttribute.BACKGROUND,
                        new Color(236, 233, 163), index, index +
                        quickSearch.length());
                index =
                        text.toLowerCase().indexOf(quickSearch.toLowerCase(),
                        index + quickSearch.length());
            }
        }
    }

    private void markProfileOccurences(AttributedString attributedText,
            String text) {
        ProfileManager.Profile activeProfile = profileManager.getActiveProfile();
        if (activeProfile != null) {
            String description = activeProfile.getDescriptionFilter();
            if (text != null && description != null && !text.isEmpty() &&
                    !description.isEmpty()) {
                int index =
                        text.toLowerCase().indexOf(description.toLowerCase());
                while (index > -1) {
                    attributedText.addAttribute(TextAttribute.FONT, new Font(
                            "Verdana", Font.BOLD, 11), index, index +
                            description.length());
                    attributedText.addAttribute(TextAttribute.FOREGROUND,
                            new Color(36, 35, 3), index, index +
                            description.length());
                    attributedText.addAttribute(TextAttribute.BACKGROUND,
                            new Color(236, 233, 163), index, index +
                            description.length());
                    index =
                            text.toLowerCase().indexOf(description.toLowerCase(),
                            index + description.length());
                }
            }
        }
    }

    /**
     * Paints a special version of this component when displayed on a glass pane.
     * 
     * @param _g a Graphics object to paint on.
     */
    private void paintOnGlassPane(Graphics _g) {
        super.paintComponent(_g);
        Graphics2D g = (Graphics2D) _g;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        int width = (int) preferredSizeOnGlassPane.getWidth();
        int height = (int) preferredSizeOnGlassPane.getHeight();

        // Fill rect
        g.setColor(new Color(100, 100, 100, 168));
        g.fillRoundRect(50, 50, width, height, 20, 20);

        // Draw border
        g.setColor(new Color(200, 200, 200));
        g.setStroke(new BasicStroke(2));
        g.drawRoundRect(50, 50, width, height, 20, 20);

    }

    /**
     * When a mouse click is registered on the component there are a few different
     * action that can occur:
     *   - Single click (no dragging) on the mail symbol = Flip the component and display
     *		the text field.
     *   - Single click (no dragging) outside the mail symbol = Increase the feed height
     *		and display more of the feed info.
     *	 - Double click (no dragging) outside the mail symbol = Make the feed visible on
     *		the glass pane. This is "maximized" mode and all of the feed is visible to the
     *		user.
     *	 - Press button and drag = all feeds will follow the mouse.
     *	 - Press button, drag and then release = feeds will follow the mouse and when user
     *		releases the button the feeds will continue to move and at the same time
     *		decelerate.
     * 
     * @param e generated mouse event.
     */
    @Override
    public void mouseClicked(MouseEvent e) {

        // Is mail icon clicked
        Desktop desktop = Desktop.getDesktop();
        if (openRect.contains(e.getPoint())) {
            try {
                desktop.browse(new URI(feedItem.link));
            } catch (Exception exc) {
                logger.logp(Level.WARNING, this.getClass().toString(),
                        "mouseClicked",
                        "Error occured when trying to open native browser", exc);
            }
        } else if (favouriteRect.contains(e.getPoint())) {
            isFavourite = !isFavourite;
            feedContainer.repaint();
        } else if (mailRect.contains(e.getPoint())) {
            try {
                desktop.mail(new URI("mailto:?subject=Message%20from%20Feedjii&body=" +
                        getFormattedMailBody(feedItem)));
            } catch (Exception exc) {
                logger.logp(Level.WARNING, this.getClass().toString(),
                        "mouseClicked",
                        "Error occured when opening native mail client", exc);
            }
        } else if (e.getClickCount() > 1 && animator != null && (animator.
                getTotalElapsedTime() < 0)) {
            animator.cancel();
        // is animation already running then do nothing
        } else if (animator != null && animator.isRunning()) {
            return;
        // Start animation where the component height is incresing.
        } else {
            animator = new Animator(200, this);
            // We need to have a timeout before animation is started, otherwise we wont
            // be able to check if it's a double click.
            animator.setStartDelay(300);
            animator.setResolution(10);
            animator.start();
        }
        revalidate();
    }

    /**
     * Forwards mouse presses to parentComponent
     * 
     * @param e generated mouse event.
     */
    @Override
    public void mousePressed(MouseEvent e) {
        feedContainer.mousePressed(e);
    }

    /**
     * Forwards mouse releases to parentComponent
     * 
     * @param e generated mouse event.
     */
    @Override
    public void mouseReleased(MouseEvent e) {
        feedContainer.mouseReleased(e);
    }

    /**
     * Empty implementation.
     * 
     * @param e mouse event
     */
    @Override
    public void mouseEntered(MouseEvent e) {
        isMouseOver = true;
        repaint(100, 50, getWidth(), getHeight());
    }

    /**
     * Empty implementation.
     * 
     * @param e mouse event
     */
    @Override
    public void mouseExited(MouseEvent e) {
        isMouseOver = false;
        repaint(100, 50, getWidth(), getHeight());
    }

    /**
     * Called when a user pressed a button and moves the mouse.
     * Forwards events to parent.
     * 
     * @param e mouse event.
     */
    @Override
    public void mouseDragged(MouseEvent e) {
        feedContainer.mouseDragged(e);
    }

    /**
     * Empty implementation.
     * 
     * @param e mouse event.
     */
    @Override
    public void mouseMoved(MouseEvent e) {
    }

    /**
     * Called by Animator to change the component height.
     * At what rate depends on the 'setResolution" method.
     * 
     * @param percentage amount of the time that has elapsed. From 0 to 1.
     */
    @Override
    public void timingEvent(float percentage) {
        if (!directionUp) {
            componentHeight = componentHeightNotFocused +
                    (int) (additionalHeightWhenFocsed * percentage);
        } else {
            componentHeight = componentHeightNotFocused + (int) (additionalHeightWhenFocsed * (1 -
                    percentage));
        }
        // We need to layout components again to make the change visible.
        bufferedFeedImage = null;
        repaint();
        revalidate();
//        feedContainer.updateGraphics();

//        repaint();
    }

    /**
     * Called when the animation begins.
     */
    @Override
    public void begin() {
        isMouseOver = false;
    }

    /**
     * Called when the animation ends.
     * Change the direction of animation until next time.
     */
    @Override
    public void end() {
        directionUp = !directionUp;
        revalidate();
        feedContainer.updateGraphics();
    }

    /**
     * Empty implementation.
     */
    @Override
    public void repeat() {
    }

    private String getFormattedMailBody(FeedItem item) {
        System.out.println("'" + item.link + "'");
        String title = item.title;
        if (title != null) {
            title = title.trim().replace("\n", "%20");
        }
        String description = item.description;
        if (description != null) {
            description = description.trim().replace("\n", "%20");
        }

        String link = item.link;
        if (link != null) {
            link = link.trim().replace("\n", "%20");
        }

        String body = title + "%0D%0A" + description + "%0D%0A" + link;
        body = body.replace(" ", "%20");
        body = body.replace("\n", "%0D%0A");
        return body;
    }
}
