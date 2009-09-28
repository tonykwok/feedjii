/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package nu.epsilon.rss.ui.components.gradient;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Pär Sikö
 */
public class GradientFactoryImpl implements GradientFactory{

    private final static GradientFactory INSTANCE = new GradientFactoryImpl();
    private Map<Integer, BufferedImage> gradients = new HashMap<Integer, BufferedImage>();

    public static GradientFactory getInstance() {
        return INSTANCE;
    }

    private GradientFactoryImpl() {
    }

    @Override
    public BufferedImage getGradient(int yPosition) {
        return gradients.get(yPosition);
    }

    @Override
    public void addGradient(BufferedImage paint, int yPosition) {
        System.out.println("Number of gradients: " + gradients.size());
        gradients.put(yPosition, paint);
    }

}
