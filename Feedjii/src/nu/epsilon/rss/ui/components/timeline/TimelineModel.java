/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nu.epsilon.rss.ui.components.timeline;

import java.util.List;

/**
 *
 * @author j
 */
public interface TimelineModel<T> {

    public T getMin();

    public T getMax();

    public double getPercentage(T value);

    public T getValueAt(double percent);

    public List<String> getLables(int maxNumber);
}
