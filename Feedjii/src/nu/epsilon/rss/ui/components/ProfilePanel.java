/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nu.epsilon.rss.ui.components;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import nu.epsilon.rss.filter.Filter;
import nu.epsilon.rss.model.FeedModelImpl;
import nu.epsilon.rss.persistence.FeedStorageImpl;
import nu.epsilon.rss.profiles.ProfileManager;
import nu.epsilon.rss.profiles.ProfileManager.Profile;
import nu.epsilon.rss.repository.ResourceManager;
import nu.epsilon.rss.ui.backend.BackendUIImpl;

/**
 *
 * @author Pär Sikö
 */
public class ProfilePanel extends JPanel implements MouseListener {

    private Map<JPanel, Profile> mapping = new HashMap<JPanel, Profile>();
    private Filter filter;
    private FeedModelImpl model;

    public ProfilePanel() {
        setOpaque(false);
        filter = (Filter) BackendUIImpl.getInstance().getResource("filter");
        model = (FeedModelImpl) BackendUIImpl.getInstance().getResource("model");
        refresh();
    }

    public void refresh() {
        removeAll();

        List<Profile> profiles = FeedStorageImpl.getInstance().getProfiles();
        setLayout(new GridLayout(1, profiles.size()));

        for (Profile profile : profiles) {
            JPanel tmp = new JPanel(new BorderLayout());
            tmp.setOpaque(false);
            JLabel label = new JLabel(new ImageIcon(ResourceManager.loadImage("images/", "icon_profiles.png")));
            label.setOpaque(false);
            label.setMinimumSize(new Dimension(80, 80));
            tmp.add(label);
            JLabel descLabel = new JLabel(profile.getProfileName(), SwingConstants.CENTER);
            descLabel.setOpaque(false);
            descLabel.setForeground(Color.WHITE.darker());
            tmp.add(descLabel, BorderLayout.SOUTH);
            tmp.addMouseListener(this);
            tmp.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
            mapping.put(tmp, profile);
            add(tmp);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {

        JPanel panel = (JPanel) e.getSource();
        Profile profile = mapping.get(panel);
        ((ProfileManager) BackendUIImpl.getInstance().getResource("profilemanager")).setActiveProfile(profile);
        filter.setProfileFilter(profile);
        model.updateModel();
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
}
