/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nu.epsilon.rss.ui.components.sweetshow;

import java.awt.Graphics;
import nu.epsilon.rss.ui.components.sweetshow.items.SweetShowItem;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import nu.epsilon.rss.ui.components.sweetshow.items.SweetShowFeedSubscription;
import nu.epsilon.rss.ui.components.sweetshow.items.SweetShowHeader;
import nu.epsilon.rss.ui.components.sweetshow.items.SweetShowProfile;

/**
 *
 * @author Pär Sikö
 */
public class SweetShowCellRenderer implements ListCellRenderer {

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
            boolean cellHasFocus) {
        JComponent label = new JLabel("Unrecognized class");
        if (value instanceof SweetShowFeedSubscription) {
            SweetShowItem item = (SweetShowItem) value;
            label = new SweetCellComponent(item.getListText());
            label.setOpaque(true);
            label.setBackground(Color.BLUE.darker().darker());
            label.setForeground(Color.WHITE);
            label.setPreferredSize(new Dimension(180, 40));
            if (isSelected) {
                label.setBackground(Color.BLACK);
            }
        } else if (value instanceof SweetShowProfile) {
            SweetShowProfile item = (SweetShowProfile) value;
            label = new SweetCellComponent(item.getListText());
            label.setOpaque(true);
            label.setBackground(Color.BLUE.darker().darker());
            label.setForeground(Color.WHITE);
            label.setPreferredSize(new Dimension(180, 40));
            if (isSelected) {
                label.setBackground(Color.BLACK);
            }
        } else if (value instanceof SweetShowHeader) {
            SweetShowHeader header = (SweetShowHeader) value;
            label = new JLabel(header.getListText());
            label.setOpaque(true);
            label.setBackground(Color.RED);
        }

        return label;
    }

    private class SweetCellComponent extends JComponent {

        private String title;

        public SweetCellComponent(String title) {
            setOpaque(true);
            this.title = title;
        }

        @Override
        protected void paintComponent(Graphics _g) {
            Graphics2D g = (Graphics2D) _g;
            System.out.println(getWidth() + " " + getHeight());
            g.setColor(getBackground());
            g.fillRect(0, 0, getWidth(), getHeight());
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setFont(new Font("Verdana", Font.BOLD, 11));
            g.setColor(getForeground());
            g.drawString(title, 10, 10);
        }
    }
}
