package nu.epsilon.rss.ui.components;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.Area;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.util.Date;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.border.EmptyBorder;
import nu.epsilon.rss.filter.Filter;
import nu.epsilon.rss.model.FeedModel;
import nu.epsilon.rss.model.FeedModelImpl;
import nu.epsilon.rss.ui.backend.BackendUI;
import nu.epsilon.rss.ui.components.timeline.DateTimelineModel;
import nu.epsilon.rss.ui.components.timeline.FeedjiiTimelineRenderer;
import nu.epsilon.rss.ui.components.timeline.Timeline;
import nu.epsilon.rss.ui.components.timeline.TimelineListener;
import nu.epsilon.rss.ui.effects.fold.FoldableComponent;
import nu.epsilon.rss.ui.utils.DrawUtil;

public class TimelinePanel extends JComponent {

    private final int bottomOffset;
    private final int height;
    private Timeline<Date> timeline;
    private BufferedImage image;

    @SuppressWarnings("deprecation")
    public TimelinePanel(JFrame grandParent, int yOffset, int _height, Date startDate, Date endDate, final BackendUI backend) {
        this.bottomOffset = yOffset;
        this.height = _height;

        setLayout(new BorderLayout());

        timeline = new Timeline<Date>();
        if (startDate == null) {
            startDate = new Date(109, 0, 1, 0, 0, 0);
        }
        if (endDate == null) {
            endDate = new Date();
        }
        timeline.setModel(new DateTimelineModel(startDate, endDate));
        timeline.setRenderer(new FeedjiiTimelineRenderer());
        timeline.setBorder(new EmptyBorder(5, 5, 5, 5));
        timeline.addListener(new TimelineListener<Date>() {

            private Filter filter = (Filter) backend.getResource("filter");
            private FeedModel model = (FeedModel) backend.getResource("model");

            @Override
            public void valueChanged(Date min, Date max, boolean changing) {
                if (changing) {
                    return;
                }
                
                if (filter != null) {
                    filter.setStartDate(min);
                    filter.setEndDate(max);
                    model.updateModel();
                }
            }
        });

        grandParent.addComponentListener(new ComponentAdapter() {

            @Override
            public void componentResized(ComponentEvent e) {
                resize(e);
            }

            @Override
            public void componentShown(ComponentEvent e) {
                resize(e);
            }

            private void resize(ComponentEvent e) {
                setBounds(0, e.getComponent().getHeight() - bottomOffset - height, e.getComponent().getWidth(), height);
            }
        });

        FoldableComponent timelineWrapper = new FoldableComponent(timeline);
        this.add(timelineWrapper, BorderLayout.CENTER);
    }

    @Override
    public void paintComponent(Graphics g) {
        if (image == null) {
            image = render();
        }
        g.drawImage(image, 0, 0, null);
    }

    private BufferedImage render() {
        BufferedImage img = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D gg = (Graphics2D) img.getGraphics();

        int w = getWidth();
        int h = getHeight();
        int alpha = 240;

        Color color1 = new Color(141, 141, 141, alpha);
        Color color2 = new Color(97, 97, 97, 255);

        GradientPaint paint = new GradientPaint(0, 0, color1, 0, h, color2);
        gg.setPaint(paint);

        Area shape = new Area(new RoundRectangle2D.Float(0, 0, w - 1, h - 15, 15, 15));
        shape.add(new Area(new Rectangle2D.Float(0, 15, w, h)));
        gg.fill(shape);
        DrawUtil.drawRoundEdge(gg, new Color(188, 188, 188, alpha), 0, 0, shape.getBounds().width - 1, 15, true, true);
        gg.setColor(new Color(44, 44, 44, alpha));
        gg.draw(new Line2D.Double(0, h - 1, w, h - 1));

        gg.dispose();
        return img;
    }
}

