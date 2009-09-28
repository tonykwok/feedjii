package nu.epsilon.rss.ui.sweetfadepanel;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JComponent;

/**
 *
 * @author Pär Sikö
 */
public class IconComponent extends JComponent{

    private Dimension size;
    private BufferedImage image;
    private Logger logger = Logger.getLogger("nu.epsilon.rss.ui.sweetfadepanel");
    
    public IconComponent(String imagePath, int width, int height) {
        
        try {
            File url = new File(imagePath);
            image = ImageIO.read(url);
            BufferedImage tmp = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            tmp.getGraphics().drawImage(image, 0, 0, width, height, null);
            image = tmp;
            size = new Dimension(width, height);
        } catch (IOException ex) {
            logger.logp(Level.WARNING, this.getClass().toString(), "constructor",
                    "Error reading file", ex);
        }
        
    }
    
    public IconComponent(BufferedImage image) {
        this.image = image;
        size = new Dimension(image.getWidth(), image.getHeight());
    }

    @Override
    public Dimension getSize() {
        return size;
    }

    @Override
    protected void paintComponent(Graphics g) {
        g.drawImage(image, 0, 0, null);
    }
    
}
