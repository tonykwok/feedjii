/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nu.epsilon.rss.ui.components;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import nu.epsilon.rss.model.FeedModelImpl;
import nu.epsilon.rss.repository.ResourceManager;
import nu.epsilon.rss.ui.backend.BackendUIImpl;

/**
 *
 * @author Pär Sikö
 */
public class SortPanel extends JPanel {

    private final String[] items = {"Only new", "Sort A-Z", "Sort Z-A",
        "Newest first", "Newest last"};
    private final FilterComponent[] panels = new FilterComponent[items.length];
    private final FeedModelImpl model = (FeedModelImpl) BackendUIImpl.
            getInstance().getResource("model");

    public SortPanel() {
        setOpaque(false);
        setLayout(new GridLayout(1, items.length));
        int i = 0;
        for (String item : items) {
            FilterComponent panel = new FilterComponent(new ImageIcon(ResourceManager.loadImage(
                    "images/", "icon_view.png")), item);
            add(panel);
            panels[i++] = panel;
//            JPanel tmp = new JPanel(new BorderLayout());
//            tmp.setOpaque(false);
//            JLabel label = new JLabel(new ImageIcon(ResourceManager.loadImage(
//                    "images/", "icon_view.png")));
//            label.setOpaque(false);
//            label.setMinimumSize(new Dimension(80, 80));
//            tmp.add(label);
//            JLabel descLabel = new JLabel(item, SwingConstants.CENTER);
//            descLabel.setOpaque(false);
//            descLabel.setForeground(Color.WHITE.darker());
//            tmp.add(descLabel, BorderLayout.SOUTH);
//            tmp.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
//            panels[i++] = tmp;
//            add(tmp);
        }

        panels[0].addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                model.showOnlyNewFeeds(!model.isOnlyShowingNewFeeds());
                panels[0].setDescription(
                        model.isOnlyShowingNewFeeds() ? "All" : "Only new");
                repaint();
            }
        });

        panels[1].addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                model.setSortOrder(FeedModelImpl.Sort.ALPHABETICAL);
            }
        });

        panels[2].addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                model.setSortOrder(FeedModelImpl.Sort.ALPHABETICALLY_REVERSED);
            }
        });

        panels[3].addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                model.setSortOrder(FeedModelImpl.Sort.NEWEST_FIRST);
            }
        });

        panels[4].addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                model.setSortOrder(FeedModelImpl.Sort.NEWEST_LAST);
            }
        });

    }

    private class FilterComponent extends JPanel {

        private JLabel imageLabel;
        private JLabel descriptionLabel;

        public FilterComponent(ImageIcon image, String description) {
            super(new BorderLayout());
            setOpaque(false);
            imageLabel = new JLabel(new ImageIcon(ResourceManager.loadImage(
                    "images/", "icon_view.png")));
            imageLabel.setOpaque(false);
            imageLabel.setMinimumSize(new Dimension(80, 80));
            add(imageLabel);
            descriptionLabel = new JLabel(description, SwingConstants.CENTER);
            descriptionLabel.setOpaque(false);
            descriptionLabel.setForeground(Color.WHITE.darker());
            add(descriptionLabel, BorderLayout.SOUTH);
            setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        }

        void setDescription(String description) {
            descriptionLabel.setText(description);
        }
    }
}
