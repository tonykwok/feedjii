package nu.epsilon.rss.ui.components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import javax.swing.JComponent;
import nu.epsilon.rss.repository.ResourceManager;

/**
 *
 * @author Pär Sikö
 */
public class XButton extends JComponent {

    private Image right;
    private Image middle;
    private Image left;
    private BufferedImage image = null;
    private String text;
    private Font font = new Font("Arial", Font.BOLD, 16);

    public XButton(String text) {

        this.text = text;
        ResourceManager manager = ResourceManager.getInstance();
        right = manager.getImage("images", "button_right.png");
        middle = manager.getImage("images", "middle.png");
        left = manager.getImage("images", "button_left.png");

        setOpaque(true);

    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(200, middle.getHeight(null));
    }

    @Override
    protected void paintComponent(Graphics _g) {

        Graphics2D g = (Graphics2D) _g;

        if (image == null) {

            image = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
            Graphics2D gg = (Graphics2D) image.getGraphics();

            gg.drawImage(right, getWidth() - right.getWidth(null), 0, null);
            gg.drawImage(left, 0, 0, null);
            gg.drawImage(middle, left.getWidth(null), 0, getWidth() - right.getWidth(null) -
                    left.getWidth(null), getHeight(), null);

            gg.setColor(new Color(207, 207, 207));
            gg.setFont(font);
            gg.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int stringWidth = gg.getFontMetrics(font).stringWidth(text);
            gg.drawString(text, (getWidth() - stringWidth) / 2, 20);
            gg.dispose();

        }

//		g.setColor(new Color(0,0,0,200));
//		g.fillRect(0, 0, getWidth(), getHeight());

        g.drawImage(image, 0, 0, null);

    }
}
