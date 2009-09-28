package nu.epsilon.rss.ui;

import nu.epsilon.rss.ui.components.buttons.TimelineButton;
import java.awt.AWTException;
import java.awt.event.ComponentEvent;
import nu.epsilon.rss.ui.components.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Menu;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.Window;
import java.awt.event.ComponentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Area;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import nu.epsilon.rss.filter.FeedFilter;
import nu.epsilon.rss.filter.Filter;
import nu.epsilon.rss.model.FeedModel;
import nu.epsilon.rss.model.FeedModelImpl;
import nu.epsilon.rss.profiles.ProfileManager;
import nu.epsilon.rss.ui.components.buttons.ButtonFactory;
import nu.epsilon.rss.repository.ResourceManager;
import nu.epsilon.rss.rssmanager.FeedReader;
import nu.epsilon.rss.rssmanager.FeedReaderImpl;
import nu.epsilon.rss.rssmanager.FeedSubscription;
import nu.epsilon.rss.persistence.FeedStorageImpl;
import nu.epsilon.rss.persistence.FeedStorage;
import nu.epsilon.rss.ui.backend.BackendUI;
import nu.epsilon.rss.ui.backend.BackendUIImpl;
import nu.epsilon.rss.ui.components.glaspane.GlassPaneHandler;
import nu.epsilon.rss.ui.transition.ViewUI;
import nu.epsilon.rss.ui.transition.views.FeedView;
import nu.epsilon.rss.ui.transition.views.LoadingView;
import nu.epsilon.rss.ui.utils.FPSCounter;
import nu.epsilon.rss.ui.utils.FrameUtil;

/**
 *
 * @author Pär Sikö
 */
public class XFrame extends JFrame implements MouseMotionListener, MouseListener {

    private Top top;
    private Bottom bottom;
//    private FoldableComponent timelineWrapper;
    private MouseEvent e;
//    private final XFrame frame;
    private BackendUI backend;
    private Filter feedFilter;
    private final String[][] defaultFeeds = {
        {"http://developers.sun.com/rss/sdn.xml", "Sun Developer Network",
            "Everything related to Java"},
        {"http://today.java.net/rss/19.rss", "java.net",
            "Everything related to Java"},
        {"http://developers.sun.com/rss/java.xml", "Java Technology", "Java news from Sun"},
        {"http://feeds.dzone.com/javalobby/frontpage", "Java Lobby", "News from Java Lobby"},
        {"http://www.feedjii.com/?feed=rss2", "Feedjii", "Feedjii news"},};
    private final Logger logger = Logger.getLogger("nu.epsilon.rss.ui.XFrame");
    private static final int LOG_LIMIT = 5 * 1024 * 1024;
    private static final int LOG_COUNT = 10;
    private static final boolean LOG_APPEND = true;

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Throwable t) {
        }
        XFrame frame = new XFrame(args);
        FrameUtil.getInstance().setFrame(frame);
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.setBounds(100, 50, 730, 700);
        frame.setUndecorated(true);

        setFrameShape(frame, new RoundRectangle2D.Float(0, 0, 730, 700, 21, 21));

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    @SuppressWarnings("unchecked")
    private static void setFrameShape(Frame frame, Shape shape) {
        try {
            Class AWTUtils = Class.forName("com.sun.awt.AWTUtilities");
            AWTUtils.getMethod("setWindowShape", Window.class, Shape.class).invoke(null, frame, shape);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public XFrame(String[] args) {

        initLogger();
        Image iconImage = ResourceManager.loadImage("images/", "trayicon.png");

        if (SystemTray.isSupported()) {
            SystemTray tray = SystemTray.getSystemTray();
            PopupMenu menu = new PopupMenu("Feedjii");
            menu.add(new MenuItem("Open"));
            menu.add(new MenuItem("Minimize"));
            Menu profileMenu = new Menu("Profiles");
            profileMenu.add(new MenuItem("Java"));
            profileMenu.add(new MenuItem("Triathlon"));
            menu.add(profileMenu);
            menu.addSeparator();
            menu.add(new MenuItem("Exit"));
            TrayIcon icon = new TrayIcon(iconImage, "Feedjii", menu);
            try {
                tray.add(icon);
            } catch (AWTException ex) {
                logger.logp(Level.WARNING, this.getClass().toString(),
                        "constructor",
                        "Error occured when initializing tray icon", ex);
            }

        }

        setTitle("Feedjii");
        setIconImage(iconImage);

        GlassPaneHandler.getInstance().setFrame(this);
        backend =
                BackendUIImpl.getInstance();
        FeedStorage storage = FeedStorageImpl.getInstance();
        backend.addResource("storage", storage);
        backend.addResource("profilemanager", new ProfileManager());
        feedFilter =
                new FeedFilter();
        backend.addResource("filter", feedFilter);

        initProxy();

        KineticScrollingComponent feedContainer = new KineticScrollingComponent(
                this);
        FeedModel feedModel = new FeedModelImpl(feedFilter, storage.getFeeds());
        feedModel.addPropertyChangeListener(feedContainer);

        FeedReader reader = new FeedReaderImpl();
        backend.addResource("feedReader", reader);

        List<FeedSubscription> rssSubscriptions = ((FeedStorage) backend.getResource("storage")).getSubscriptions();
        ViewUI setup = new ViewUI();
        if (rssSubscriptions.isEmpty()) {
            List<FeedSubscription> subscriptions =
                    new ArrayList<FeedSubscription>();
            for (String[] defaultFeed : defaultFeeds) {
                FeedSubscription subscription = new FeedSubscription(
                        defaultFeed[1], defaultFeed[0], defaultFeed[2]);
                subscriptions.add(subscription);
                rssSubscriptions.add(subscription);
            }

            storage.saveSubscriptions(subscriptions);
        }

        for (FeedSubscription subscription : rssSubscriptions) {
            reader.addFeed(subscription.getUrl());

        }

        reader.addFeedListener(feedModel);
        setup.addView(new LoadingView(reader));
//        setup.addView(new ShortInfoView());
        setup.addView(new FeedView(feedContainer, this));
        add(setup);

        top =
                new Top(89);
        int bottomHeight = 37;
        TimelinePanel timelinePanel = new TimelinePanel(this, 37, 70, storage.getTimelineStartDate(), storage.getTimelineEndDate(), backend);
        bottom =
                new Bottom(bottomHeight, timelinePanel);
        addComponentListener(bottom);
        addComponentListener(top);
        top.addMouseMotionListener(this);
        top.addMouseListener(this);
        super.add(top, BorderLayout.NORTH);
        super.add(bottom, BorderLayout.SOUTH);
    }

    private void initLogger() {
        try {
            Logger baselogger = Logger.getLogger("nu.epsilon");
            baselogger.setLevel(Level.FINER);
            SimpleFormatter formatter = new SimpleFormatter();
            FileHandler fileHandler = new FileHandler(
                    "feedjii.log", LOG_LIMIT, LOG_COUNT, LOG_APPEND);
            fileHandler.setFormatter(formatter);
            baselogger.addHandler(fileHandler);
            Logger.getLogger("nu.epsilon").fine("lgging.....");
        } catch (IOException ioe) {
            System.out.println("Unable to initialize logger");
            ioe.printStackTrace();
        }

    }

    private void initProxy() {
        FeedStorage storage = (FeedStorage) backend.getResource("storage");
        String host = storage.getProxyHost();
        String port = storage.getProxyPort();
        if (host != null) {
            System.setProperty("http.proxyHost", host);
        }

        if (port != null) {
            System.setProperty("http.proxyPort", port);
        }

    }

    @Override
    public void add(Component comp, Object constraints) {
        if (constraints != null && constraints.equals(BorderLayout.NORTH)) {
            top.add(comp);
        } else if (constraints != null && constraints.equals(BorderLayout.SOUTH)) {
            bottom.add(comp);
        } else {
            super.add(comp, constraints);
        }



    }

    private class Bottom extends JPanel implements ComponentListener {

        private Color background = new Color(193, 191, 193);
        private int height;
        private BufferedImage image = null;

        public Bottom(int height, final TimelinePanel timeline) {
            this.height = height;
            setOpaque(false);

            //Add timeline button
            this.setLayout(new FlowLayout(FlowLayout.LEFT, 9, 5));
            final TimelineButton button = new TimelineButton();

            button.addMouseListener(new MouseAdapter() {

                @Override
                public void mouseReleased(MouseEvent e) {
                    if (!button.isPressed()) {
                        timeline.setVisible(false);
                        timeline.repaint();
                    } else {
                        timeline.setVisible(true);
                        GlassPaneHandler.getInstance().addComponentToGlassPane(
                                timeline);
                        getTopLevelAncestor().repaint();
                    }
                }
            });

            this.add(button);
            this.add(FPSCounter.getInstance());

        }

        @Override
        protected void paintComponent(Graphics _g) {
            Graphics2D g = (Graphics2D) _g;

            if (image == null) {
                image = new BufferedImage(getWidth(), getHeight(),
                        BufferedImage.TYPE_INT_ARGB);

                Graphics2D gg = (Graphics2D) image.getGraphics();
                gg.setColor(background);
                gg.fillRect(10, 10, getWidth() - 20, height - 20);


                int w = getWidth();
                int h = height;

                Color color1 = new Color(100, 100, 100);
                Color color2 = new Color(69, 69, 69);

                GradientPaint paint = new GradientPaint(0, 0, color1, 0, h,
                        color2);
                gg.setPaint(paint);

                Area shape = new Area();
                shape.add(new Area(new Rectangle2D.Double(0, 0, w - 1, h - 10)));
                shape.add(new Area(new RoundRectangle2D.Double(0, 0, w - 1, h -
                        1, 10, 10)));

                gg.fill(shape);
                gg.setColor(new Color(39, 39, 39));
                gg.draw(shape);
                gg.setColor(new Color(176, 176, 176));
                gg.draw(new Line2D.Double(0, 0, w, 0));
                gg.dispose();
            }

            g.drawImage(image, null, 0, 0);
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(getWidth(), height);
        }

        @Override
        public void componentResized(ComponentEvent e) {
            image = null;
        }

        @Override
        public void componentMoved(ComponentEvent e) {
        }

        @Override
        public void componentShown(ComponentEvent e) {
        }

        @Override
        public void componentHidden(ComponentEvent e) {

            image = null;
        }
    }

    private class Top extends JPanel implements ComponentListener {

        private Color background = new Color(193, 191, 193);
        private int height;
        private BufferedImage image = null;

        public Top(int height) {
            this.height = height;

            setOpaque(false);
            setLayout(new BorderLayout());

            // Search component
            JPanel tmp = new JPanel(new FlowLayout(FlowLayout.LEFT, 14, 8));
            tmp.setOpaque(false);
            SearchComponent search = new SearchComponent();
            search.setVisible(false);
            BackendUIImpl.getInstance().addResource("searchComponent", search);
            search.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 0));
            tmp.add(search);
            add(tmp, BorderLayout.SOUTH);

            //Exit component


            tmp = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            tmp.setOpaque(false);
            tmp.add(ButtonFactory.getButton(ButtonFactory.Type.CLOSE));
            add(tmp, BorderLayout.NORTH);
        }

        @Override
        public void paintComponent(Graphics _g) {
            Graphics2D g = (Graphics2D) _g;
            if (image == null) {
                image = new BufferedImage(getWidth(), getHeight(),
                        BufferedImage.TYPE_INT_ARGB);

                Graphics2D gg = (Graphics2D) image.getGraphics();
                gg.setPaint(
                        new GradientPaint(0, 0, new Color(185, 185, 185), 0,
                        height, new Color(100, 100, 100)));

                Area topShape = new Area(new RoundRectangle2D.Float(0, 0,
                        getWidth(), height,
                        10, 10));

                topShape.add(
                        new Area(
                        new Rectangle2D.Float(0, 10, getWidth(), height)));

                gg.fill(topShape);
                gg.setColor(new Color(221, 221, 221));
                gg.draw(topShape);

                gg.setColor(new Color(150, 150, 150));

                gg.drawLine(0, 10, 0, getHeight());
                gg.drawLine(getWidth() - 1, 10, getWidth() - 1, getHeight());

                gg.setColor(new Color(43, 43, 43));

                gg.drawLine(
                        0, height - 1, getWidth(), height - 1);

                // Draw the "Feedjii" logotype centered on the top panel.
                String name = "Feedjii";
                gg.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                gg.setFont(new Font("Verdana", Font.BOLD, 14));

                int x =
                        (getWidth() - gg.getFontMetrics().stringWidth("Feedjii")) /
                        2;
                gg.setColor(new Color(185, 185, 185));
                gg.drawString(name, x, 20);
                gg.setColor(
                        new Color(165, 165, 165));
                gg.drawString(name, x + 1, 19);



                gg.setColor(new Color(57, 57, 57));
                gg.drawString(name, x, 19);


                gg.dispose();
            }

            g.drawImage(image, null, 0, 0);
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(getWidth(), height);
        }

        @Override
        public void componentResized(ComponentEvent e) {
            image = null;
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
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        setLocation(e.getXOnScreen() - this.e.getX(), e.getYOnScreen() - this.e.getY());
    }

    public void mouseMoved(MouseEvent e) {
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
        this.e = e;
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }
}
