package nu.epsilon.rss.repository;

import java.awt.MediaTracker;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JLabel;

/**
 *
 * @author Pär Sikö
 */
public class ResourceManager {

    private static Map<String, BufferedImage> preLoadedImages = new HashMap<String, BufferedImage>();
    private static String[] imagesToPreLoad = new String[]{"close.png", "close_mouseover.png", "close_mousedown.png", "info_bg.png"};
    private static ResourceManager INSTANCE = new ResourceManager();
    private static Logger logger = Logger.getLogger("nu.epsilon.rss.repository");

    static {
        MediaTracker tracker = new MediaTracker(new JLabel(""));
        String path = "images/";
        int index = 0;
        for (String image : imagesToPreLoad) {
            BufferedImage bImage = loadImage(path, image);
            tracker.addImage(bImage, index++);
            preLoadedImages.put(path + image, bImage);
        }
        try {
            tracker.waitForAll();
        } catch (InterruptedException ie) {
            logger.logp(Level.WARNING, ResourceManager.class.toString(), "static context",
                    "description", ie);
        }
    }

    public static ResourceManager getInstance() {
        return INSTANCE;
    }

    private ResourceManager() {
    }

    public static BufferedImage loadImage(String path, String imageName) {

        if (preLoadedImages.containsKey(path + imageName)) {
            return preLoadedImages.get(path + imageName);
        }

        if (path != null && !path.endsWith("/")) {
            path += "/";
        }

        InputStream is = INSTANCE.getClass().getResourceAsStream("/" + path + imageName);
        BufferedImage bImage = null;
        try {
            bImage = ImageIO.read(is);
            return bImage;
        } catch (IOException ex) {
            Logger.getLogger(ResourceManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return bImage;
    }

    public BufferedImage getImage(String imageName) {
        return loadImage("images/", imageName);
    }

    public BufferedImage getImage(String path, String imageName) {
        return loadImage(path, imageName);
    }
}
