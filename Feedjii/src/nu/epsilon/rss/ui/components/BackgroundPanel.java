package nu.epsilon.rss.ui.components;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.TexturePaint;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import nu.epsilon.rss.common.PerformanceTuning;

/**
 *
 * @author Pär Sikö
 */
public class BackgroundPanel extends JPanel {

    private Color startColor,  endColor;
    private boolean isResized = false;
    private BufferedImage background = null;

    public BackgroundPanel() {
        this(new Color(55, 55, 55), new Color(22, 22, 22));
    }

    public BackgroundPanel(Color color1, Color color2) {
        startColor = color1;
        endColor = color2;
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics _g) {

        boolean useCache = PerformanceTuning.isCacheImages();
        if (isResized || !useCache || background == null) {
            isResized = false;
            if (PerformanceTuning.isUseCompatibleImages()) {
                int transparency = PerformanceTuning.isUseOpaqueImages() ? Transparency.OPAQUE
                        : Transparency.TRANSLUCENT;
                background = getGraphicsConfiguration().createCompatibleImage(
                        getWidth(),
                        getHeight(), transparency);
            } else {
                background = new BufferedImage(1, getHeight(),
                        BufferedImage.TYPE_INT_RGB);
            }

            Graphics2D gg = (Graphics2D) background.getGraphics();
            GradientPaint paint = new GradientPaint(0, 0, startColor, 0,
                    background.getHeight(), endColor);

            gg.setPaint(paint);
            gg.fillRect(0, 0, background.getWidth(), background.getHeight());

            Image img = null;
            try {
                img =ImageIO.read(getClass().getResourceAsStream("/images/wallpaper2.jpg"));
                gg.drawImage(img, 0, 0, getWidth(), getHeight(), null);
            } catch (IOException ex) {
                Logger.getLogger(BackgroundPanel.class.getName()).
                        log(Level.SEVERE, null, ex);
            }

            // paints the "drop shadow" effect under the top gray field containing the search.
            int height = 8;
            gg.setPaint(new GradientPaint(0F, 0F, Color.BLACK, 0F,
                    (float) height,
                    new Color(0, 0, 0, 0)));
            gg.fillRect(0, 0, 159, height);
            gg.fillRect(getWidth() - 69, 0, getWidth(), height);

        }

        Graphics2D g = (Graphics2D) _g;
        g.drawImage(background, 0, 0, getWidth(), getHeight(), null);
    }
}
