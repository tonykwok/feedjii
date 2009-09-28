/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nu.epsilon.rss.ui.components;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.RoundRectangle2D;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JTextField;
import nu.epsilon.rss.filter.Filter;
import nu.epsilon.rss.model.FeedModelImpl;
import nu.epsilon.rss.rssmanager.FeedItem;
import nu.epsilon.rss.ui.backend.BackendUIImpl;

/**
 *
 * @author Pär Sikö
 */
public class SearchComponent extends JPanel implements KeyListener {

    private JTextField input;
    private Filter filter;
    private FeedModelImpl model;
    private Color background;
    private int roundedCornerValue = 22;

    public SearchComponent() {
        super(new FlowLayout(FlowLayout.RIGHT));
        background = Color.WHITE;
        filter = (Filter) BackendUIImpl.getInstance().getResource("filter");
        model = (FeedModelImpl) BackendUIImpl.getInstance().getResource("model");
        setLayout(new FlowLayout());
        input = new JTextField();
        input.setOpaque(false);
        input.addKeyListener(this);
        input.setBorder(BorderFactory.createEmptyBorder(0, 0, 2, 0));
        input.setPreferredSize(new Dimension(145, 14));
        add(input);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(200, 20);
    }

    @Override
    protected void paintComponent(Graphics _g) {
        Graphics2D g = (Graphics2D) _g;
        g.setColor(background);
        g.fill(new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, roundedCornerValue, roundedCornerValue));

        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        g.setColor(new Color(55,55,55));
        g.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, roundedCornerValue,roundedCornerValue);
        
        g.setColor(new Color(0xEEEEEE));
        g.drawRoundRect(1, 2, getWidth() - 2, getHeight() - 3, roundedCornerValue,roundedCornerValue);

        g.setColor(new Color(0xDDDDDD));
        g.drawRoundRect(1, 1, getWidth() - 2, getHeight() - 2, roundedCornerValue,roundedCornerValue);
        
        g.setColor(new Color(75,75,75));
        g.setStroke(new BasicStroke(2));
        g.drawOval(6, 4, 8, 8);
        g.drawLine(14, 11, 18, 15);
        
        GeneralPath triangle = new GeneralPath();
        triangle.append(new Line2D.Float(20, 9, 26, 9), true);
        triangle.append(new Line2D.Float(26, 9, 23, 12), true);
        triangle.append(new Line2D.Float(23, 12, 20, 9), true);
        g.fill(triangle);
        

    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
        filter.setQuickSearch(input.getText());
        FeedComponent.quickSearch = input.getText();
        model.updateModel();
    }
    
}
