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
public class InfoButton extends JComponent {

    private Image background;
    private BufferedImage image = null;
    private String text;
    private Font font = new Font("Arial", Font.BOLD, 16);

    public InfoButton(String text) {

        this.text = text;
        ResourceManager manager = ResourceManager.getInstance();
        background = manager.getImage("images", "info_bg.png");

        setOpaque(false);

    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(background.getWidth(null), background.getHeight(null));
    }

    @Override
    protected void paintComponent(Graphics _g) {

        Graphics2D g = (Graphics2D) _g;

        if (image == null) {
            image = new BufferedImage(background.getWidth(null), background.getHeight(null), BufferedImage.TYPE_INT_ARGB);
            Graphics2D gg = (Graphics2D) image.getGraphics();

            
            gg.drawImage(background, 0, 0, null);
            
            gg.setFont(font);
            gg.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int stringWidth = gg.getFontMetrics(font).stringWidth(text);
            
            gg.setColor(new Color(22,22,22));
            gg.drawString(text, (getWidth() - stringWidth) / 2, 27);
            
            gg.setColor(new Color(220,220,220));
            
            gg.drawString(text, (getWidth() - stringWidth +2) / 2, 28);
            
            gg.dispose();

        }

        g.drawImage(image, 0, 0, null);

    }
}
