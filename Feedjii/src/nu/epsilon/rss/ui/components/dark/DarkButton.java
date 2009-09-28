package nu.epsilon.rss.ui.components.dark;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JButton;
import nu.epsilon.rss.ui.utils.DrawUtil;

public class DarkButton extends JButton {

    private final static Color FOREGROUND = Color.WHITE;
    private final static Color BACKGROUND_TOP = new Color(130, 130, 130);
    private final static Color BACKGROUND_BOTTOM = new Color(30, 30, 30);
    private final static Color HIGHLIGHT = Color.LIGHT_GRAY;
    private boolean pressed;

    public DarkButton() {
        this("");
    }

    public DarkButton(String text) {
        super(text);
        setOpaque(false);

        addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                pressed = true;
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                pressed = false;
            }
        });
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(100, 25);
    }

    @Override
    protected void paintComponent(Graphics g) {
        int radius = getHeight() - 2;

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setPaint(new GradientPaint(new Point(0, 0), getTopColor(), new Point(0, getHeight()), getBottomColor()));

        g2.fillRoundRect(0, 0, getWidth(), getHeight() - 1, radius, radius);

        DrawUtil.drawRoundEdge(g, HIGHLIGHT, 0, 0, getWidth() - 1, radius, true, true);
        g2.setColor(FOREGROUND);
        g2.setFont(g2.getFont().deriveFont(Font.BOLD));
        g2.drawString(getText(), (int) (getWidth() / 2 - g.getFontMetrics().getStringBounds(getText(), g).getWidth() / 2), getHeight() / 2 + 3);
    }

    private Color getTopColor() {
        return pressed ? BACKGROUND_BOTTOM : BACKGROUND_TOP;
    }

    private Color getBottomColor() {
        return pressed ? BACKGROUND_TOP : BACKGROUND_BOTTOM;
    }
}
