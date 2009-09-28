/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nu.epsilon.rss.ui.components.timeline;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.List;

/**
 *
 * @author j
 */
public class DefaultTimelineRenderer implements TimelineRenderer {

    private final static int MIN_STEP_WIDTH = 50;
    private final static int KNOB_WIDTH = 10;
    private final static int KNOB_HEIGHT = 20;

    public Dimension getKnobSize() {
        return new Dimension(KNOB_WIDTH, KNOB_HEIGHT);
    }

    private int getKnobWidth() {
        return getKnobSize().width;
    }

    public void paintLine(Rectangle bounds, List<String> labels, Graphics g) {
        Color oldColor = g.getColor();
        g.setColor(Color.RED);
        int y = bounds.height - 2;
        g.drawLine(0, y, bounds.width, y);
        g.setColor(oldColor);
    }

    public void paintKnob(int position, Rectangle bounds, Graphics g) {
        Color oldColor = g.getColor();
        g.setColor(Color.GREEN);
        g.drawRect(position - getKnobWidth() / 2, 10, getKnobWidth(), bounds.height - 10);
        g.setColor(Color.ORANGE);
        g.fillRect(position - getKnobWidth() / 2, 10, getKnobWidth(), bounds.height - 10);

        g.setColor(oldColor);
        g.drawLine(position, 0, position, bounds.height);
    }

    public int getMaxTicks(Rectangle bounds) {
        return bounds.width / MIN_STEP_WIDTH;
    }

    public double getPercentAt(int x, int width) {
        return ((double) x) / ((double) width);
    }

    public void paintKnob(double percent, Rectangle bounds, Graphics g) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int getPositionFor(double percent, int width) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}


