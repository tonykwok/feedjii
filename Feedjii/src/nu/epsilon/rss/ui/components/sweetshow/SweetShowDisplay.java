/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nu.epsilon.rss.ui.components.sweetshow;

import nu.epsilon.rss.ui.components.sweetshow.items.SweetShowItem;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 *
 * @author Pär Sikö
 */
public class SweetShowDisplay extends JComponent implements ListSelectionListener {

    private Rectangle rect;
    private SweetShowItem item;

    public SweetShowDisplay() {
        setOpaque(false);
        setLayout(new GridBagLayout());
    }

    private void displayItem(SweetShowItem item) {
        this.item = item;
        removeAll();
        if (item.getDisplayItem() != null) {
            add(item.getDisplayItem(), new GridBagConstraints());
        }
        validate();
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (e.getSource() instanceof JList) {
            JList list = (JList) e.getSource();
            displayItem((SweetShowItem) list.getSelectedValue());

            int index = list.getSelectedIndex();
            rect = list.getCellBounds(index, index);
            repaint();
        }
    }

    @Override
    protected void paintComponent(Graphics _g) {
        Graphics2D g = (Graphics2D) _g;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        if (rect != null) {
            int y1 = rect.y;
            int y2 = rect.y + rect.height;

            g.setColor(new Color(0x778ea1));
            g.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
            
            int height = 4;
            GradientPaint paint = new GradientPaint(0, 0, new Color(230, 230, 230), 0, getHeight(), new Color(180, 180,
                    180));
            g.setPaint(paint);
            g.fillRoundRect(0, height, getWidth(), getHeight() - (height*2),10,10);
            g.setColor(new Color(160,160,160));

        }
    }
    
}
