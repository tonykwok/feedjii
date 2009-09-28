/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nu.epsilon.rss.ui.components.timeline;

import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JComponent;

/**
 *
 * @author j
 */
public class Timeline<T> extends JComponent {

    private TimelineModel<T> model;
    private TimelineRenderer renderer;
    private Knob<T> knob1;
    private Knob<T> knob2;
    private Knob<T> dragKnob = null;
    private List<TimelineListener<T>> listeners = new LinkedList<TimelineListener<T>>();

    public Timeline() {
        this(null);
    }

    public Timeline(TimelineModel<T> model) {
        knob1 = new Knob<T>();
        knob2 = new Knob<T>();

        this.setModel(model);
        this.setRenderer(new DefaultTimelineRenderer());
        this.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                dragKnob = getKnobAt(e.getX());
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (dragKnob != null) {
                    System.out.println(getRangeMin() + " - " + getRangeMax());
                    notifyChanged(false);
                }

                dragKnob = null;
            }
        });

        this.addMouseMotionListener(new MouseMotionAdapter() {

            @Override
            public void mouseDragged(MouseEvent e) {
                if (dragKnob == null) {
                    return;
                }

                setPosition(dragKnob, e.getX());
                notifyChanged(true);
            }
        });
    }

    private Knob<T> getKnobAt(int x) {
        if (getRenderer() == null) {
            return null;
        }
        int width = getRenderer().getKnobSize().width;
        int from = x - width / 2;
        int to = x + width / 2;

        for (Knob<T> knob : getKnobs()) {
            int position = getPosition(knob);
            if (position >= from && position <= to) {
                return knob;
            }
        }

        return null;
    }

    public T getMin() {
        return getModel().getMin();
    }

    public T getMax() {
        return getModel().getMax();
    }

    public T getRangeMin() {
        return getKnobs().get(0).getValue();
    }

    public T getRangeMax() {
        return getKnobs().get(1).getValue();
    }

    private int getPosition(Knob<T> knob) {
        double percent = getModel().getPercentage(knob.getValue());
        return getRenderer().getPositionFor(percent, getWidth());
    }

    private void setPosition(Knob<T> knob, int pos) {
        double percent = getRenderer().getPercentAt(pos, getWidth());
        knob.setValue(getModel().getValueAt(percent));
    }

    public TimelineModel<T> getModel() {
        return model;
    }

    public void setModel(TimelineModel<T> model) {
        this.model = model;
        if (model != null) {
            knob1.setValue(model.getMin());
            knob2.setValue(model.getMax());
        }
    }

    public TimelineRenderer getRenderer() {
        return renderer;
    }

    public void setRenderer(TimelineRenderer renderer) {
        this.renderer = renderer;
    }

    private Rectangle getInnerBounds() {
        Insets insets = getInsets();
        return new Rectangle(insets.left, insets.top, getWidth() - insets.left - insets.right, getHeight() - insets.top - insets.bottom);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (getRenderer() == null) {
            return;
        }

        getRenderer().paintLine(getInnerBounds(), getModel().getLables(getRenderer().getMaxTicks(getInnerBounds())), g);
        for (Knob<T> knob : getKnobs()) {
            getRenderer().paintKnob(getModel().getPercentage(knob.getValue()), getInnerBounds(), g);
        }
        repaint();
    }

    private List<Knob<T>> getKnobs() {
        List<Knob<T>> knobs = new ArrayList<Knob<T>>();
        knobs.add(knob1);
        knobs.add(knob2);

        if (getPosition(knob2) < getPosition(knob1)) {
            Collections.reverse(knobs);
        }

        return knobs;
    }

    public void addListener(TimelineListener<T> listener) {
        listeners.add(listener);
    }

    public void removeListener(TimelineListener<T> listener) {
        listeners.remove(listener);
    }

    private void notifyChanged(boolean changing) {
        for (TimelineListener<T> listener : listeners) {
            listener.valueChanged(getRangeMin(), getRangeMax(), changing);
        }
    }
}

class Knob<T> {

    private T value;

    void setValue(T value) {
        this.value = value;
    }

    T getValue() {
        return this.value;
    }

    @Override
    public String toString() {
        return "Knob " + value;
    }
}