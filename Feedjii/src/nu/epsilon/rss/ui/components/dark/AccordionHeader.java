package nu.epsilon.rss.ui.components.dark;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LinearGradientPaint;
import java.awt.Paint;
import javax.swing.JLabel;
import nu.epsilon.rss.ui.utils.DrawUtil;

/**
 *
 * @author Martin Gunnarsson <martin.gunnarsson@xplora.info>
 */
public class AccordionHeader extends JLabel {

    private final static int RADIUS = 7;

    public AccordionHeader(String text) {
        super(text);
        setFont(getFont().deriveFont(Font.BOLD));
        setOpaque(false);
        setBackground(Color.RED);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(super.getPreferredSize().width, 24);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
//        g2.setPaint(Color.BLACK);
//        g2.fillRect(0, 0, getWidth(), getHeight());

        Paint paint = new LinearGradientPaint(0, 0, 0, getHeight(), new float[]{0f, 1f}, new Color[]{Color.DARK_GRAY, Color.BLACK});
        g2.setPaint(paint);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), RADIUS, RADIUS);
        DrawUtil.drawRoundEdge(g2, new Color(100, 100, 100), 0, 0, getWidth() - 1, RADIUS, true, true);
        g2.setColor(Color.WHITE);
        g.drawString(getText(), RADIUS, getHeight() - g.getFontMetrics().getHeight() / 2);
    }
}
