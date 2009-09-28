/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nu.epsilon.rss.ui.components.timeline;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.util.List;
import nu.epsilon.rss.repository.ResourceManager;

/**
 *
 * @author j
 */
public class FeedjiiTimelineRenderer implements TimelineRenderer {

    static {
        knob = ResourceManager.loadImage("images/", "knob.png");
    }
    private static Image knob;
    private Color shadowTextColor = new Color(0, 0, 0, 230);
    private Color shadowColor = new Color(0, 0, 0, 200);
    private Color highlightColor = new Color(255, 255, 255, 80);
    private final static int TEXT_LIFT = 2;
    private final static int TICK_HEIGHT = 7;
    private final static int LINE_LIFT = 3;
    private final static int KNOB_LIFT = 1;
    private final static int knobWidth = 14;
    private final static int knobHeight = 18;

    public void paintLine(Rectangle bounds, List<String> labels, Graphics g) {
        int bottom = bounds.height - LINE_LIFT - knobHeight;
        int top = bottom - TICK_HEIGHT;
        int left = getKnobWidth() / 2 + bounds.x;
        int right = bounds.width + bounds.x - getKnobWidth() / 2;

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        drawNiceText(g2, labels.get(0), left, top - TEXT_LIFT, Align.LEFT);
        drawNiceLine(g2, left, bottom, left, top);
        drawNiceText(g2, labels.get(labels.size() - 1), right, top - TEXT_LIFT, Align.RIGHT);
        drawNiceLine(g2, right, bottom, right, top);

        int ticks = labels.size() - 1;

        for (int i = 1; i < ticks; i++) {
            //int x = (int) Math.round(tickWidth * i) + getKnobWidth() / 2;
            int x = getPositionFor(((double) i) / ticks, bounds.width) + bounds.x;
            drawNiceLine(g2, x, bottom, x, top + 3);
            drawNiceText(g2, labels.get(i), x, top - TEXT_LIFT, Align.CENTER);
        }

        drawNiceLine(g2, left, bottom, right, bottom);
    }

    public void paintKnob(double percent, Rectangle bounds, Graphics g) {
        int position = getPositionFor(percent, bounds.width);
        g.drawImage(knob, position - getKnobWidth() / 2 + bounds.x, bounds.height - knobHeight - KNOB_LIFT, null);
        // g.drawString(String.valueOf(getPercentAt(position, bounds.width)), position, 20);
    }

    private int getKnobWidth() {
        return getKnobSize().width;
    }

    public Dimension getKnobSize() {
        return new Dimension(knobWidth, knobHeight);
    }

    public int getMaxTicks(Rectangle bounds) {
        return 10;
    }

    private void drawNiceLine(Graphics2D g, int x1, int y1, int x2, int y2) {
        int offset = 1;
        g.setColor(highlightColor);
        g.drawLine(x1, y1 + offset, x2, y2 + offset);
        g.setColor(shadowColor);
        g.drawLine(x1, y1, x2, y2);
    }

    private void drawNiceText(Graphics2D g, String text, int x, int bottom, Align align) {
        int width = (int) Math.round(g.getFontMetrics().getStringBounds(text, g).getWidth());

        if (align == Align.LEFT) {
            x--;
        }

        if (align == Align.CENTER) {
            x -= width / 2;
        }

        if (align == Align.RIGHT) {
            x -= width - 2;
        }

        int offset = 1;
        g.setColor(highlightColor);
        g.drawString(text, x, bottom + offset);
        g.setColor(shadowTextColor);
        g.drawString(text, x, bottom);
    }

    public double getPercentAt(int x, int width) {
        double percent = ((double) (x)) / ((double) (width - getKnobWidth()));
        return Math.max(0F, Math.min(percent, 1F));
    }

    public int getPositionFor(double percent, int width) {
        return (int) Math.round((width - getKnobWidth()) * percent) + getKnobWidth() / 2;
    }
}

enum Align {

    LEFT, CENTER, RIGHT
};
