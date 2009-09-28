/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package nu.epsilon.rss.ui.components.gradient;

import java.awt.image.BufferedImage;

/**
 *
 * @author Pär Sikö
 */
public interface GradientFactory {

    /**
     * Gets a gradient with a specific start and end color that depends on the
     * xPos value.
     *
     * @param yPos y-position on screen for the calling component.
     * @return a gradient with a color setup matching the y-position.
     */
    BufferedImage getGradient(int yPos);

    /**
     * Adds
     *
     * @param paint
     * @param yPos
     */
    void addGradient(BufferedImage paint, int yPos);

}
