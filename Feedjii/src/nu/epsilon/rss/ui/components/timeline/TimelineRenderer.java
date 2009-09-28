/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nu.epsilon.rss.ui.components.timeline;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.List;

/**
 *
 * @author j
 */
public interface TimelineRenderer {

    public void paintLine(Rectangle bounds, List<String> labels, Graphics g);

    public void paintKnob(double percent, Rectangle bounds, Graphics g);

    public Dimension getKnobSize();

    public int getMaxTicks(Rectangle bounds);

    public double getPercentAt(int x, int width);

    public int getPositionFor(double percent, int width);
}
