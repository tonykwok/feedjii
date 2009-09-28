/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nu.epsilon.rss.ui.sweetfadepanel;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.RenderingHints;
import javax.swing.JPanel;
import nu.epsilon.rss.repository.ResourceManager;
import nu.epsilon.rss.ui.utils.DrawUtil;

/**
 *
 * @author Pär Sikö
 */
public class TransparentPanel extends JPanel {

    private final static Image CONTRACT_ICON = ResourceManager.loadImage("images/", "close_small.png");

    public TransparentPanel() {
        setLayout(new GridBagLayout());
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics _g) {
        final Color highlightColor = new Color(200, 200, 200, 200);
        int borderWidth = 5;
        Graphics2D g = (Graphics2D) _g;

        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g.setColor(new Color(60, 60, 60, 100));
        g.fillRoundRect(0, 0, getWidth(), getHeight(), 13, 13);

        g.setColor(new Color(10, 10, 10, 170)); //230 was the original color before JavaOne
        g.fillRoundRect(borderWidth, borderWidth, getWidth() - 2 * borderWidth, getHeight() - 2 * borderWidth, 8, 8);

        DrawUtil.drawRoundEdge(g, highlightColor, 0, 0, getWidth(), 13, true, true);
        DrawUtil.drawRoundEdge(g, highlightColor, borderWidth, getHeight() - borderWidth - 1, getWidth() - borderWidth * 2, 8, false, true);

        g.drawImage(CONTRACT_ICON, getWidth() - CONTRACT_ICON.getWidth(null) - 9, 9, null);

    }
}
