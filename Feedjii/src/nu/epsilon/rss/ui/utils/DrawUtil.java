package nu.epsilon.rss.ui.utils;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;

/**
 * TODO: ADD DOCUMENTATION
 *
 * @author Martin Gunnarsson
 */
public class DrawUtil {

    private DrawUtil() {
    }

    public static void drawRoundEdge(Graphics g, Color color, int x, int y,
            int width, int height, boolean down, boolean fade) {
        Graphics2D g2 = (Graphics2D) g;
        int radius = height / 2;

        Paint oldPaint = g2.getPaint();
        Paint paint = color;

        if (fade) {
            Color color2 = new Color(color.getRed(), color.getGreen(), color.
                    getBlue(), 0);
            if (down) {
                paint = new GradientPaint(x, y, color, x, y + radius, color2,
                        false);
            } else {
                paint = new GradientPaint(x, y - radius, color2, x, y, color,
                        false);
            }

        }
        g2.setPaint(paint);

        //Line
        int lineWidth = width - height + 1;
        int lineX = x + radius;
        g.drawLine(lineX, y, lineX + lineWidth, y);

        //Left arc
        int arcY = down ? y : y - height + 1;
        int start = down ? 90 : 180;
        g.drawArc(x, arcY, height - 1, height - 1, start, 90);

        //Right arc
        start = down ? 0 : 270;
        g.drawArc(x + lineWidth, arcY, height - 1, height - 1, start, 90);

        g2.setPaint(oldPaint);
    }
}
