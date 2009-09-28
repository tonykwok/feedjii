/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nu.epsilon.rss.ui.components.timeline;

/**
 *
 * @author j
 */
public interface TimelineListener<T> {

    public void valueChanged(T min, T max, boolean changing);
}
