/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nu.epsilon.rss.ui.transition.views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JPanel;
import nu.epsilon.rss.filter.FeedFilter;
import nu.epsilon.rss.filter.Filter;
import nu.epsilon.rss.model.FeedModelImpl;
import nu.epsilon.rss.profiles.ProfileManager;
import nu.epsilon.rss.profiles.ProfileManager.Profile;
import nu.epsilon.rss.ui.UiConstants;
import nu.epsilon.rss.ui.backend.BackendUI;
import nu.epsilon.rss.ui.backend.BackendUIImpl;
import nu.epsilon.rss.ui.components.BackgroundPanel;
import nu.epsilon.rss.ui.components.InfoButton;
import nu.epsilon.rss.ui.transition.View;

/**
 *
 * @author Pär Sikö
 */
public class ShortInfoView extends View implements MouseListener {

    private Map<InfoButton, Profile> infoButtons =
            new HashMap<InfoButton, Profile>();
    private FeedModelImpl model;
    private FeedFilter filter;

    public ShortInfoView() {
        model = (FeedModelImpl) BackendUIImpl.getInstance().getResource("model");
    }

    @Override
    public void setupNextScreen() {

        BackendUI backend = BackendUIImpl.getInstance();
        ProfileManager manager = (ProfileManager) backend.getResource(
                "profilemanager");
        List<Profile> profiles = manager.getProfiles();
        filter = (FeedFilter) backend.getResource("filter");

        Collections.sort(profiles);

        for (Profile profile : profiles) {
            Filter profileFilter = new FeedFilter();
            profileFilter.setProfileFilter(profile);
            int matches = model.getItemsMatchingFilter(profileFilter);
            InfoButton button = new InfoButton(matches +
                    " new feeds related to " + profile.getProfileName());
            button.addMouseListener(this);
            infoButtons.put(button, profile);
        }

        transitionComponent.removeAll();
        transitionComponent.setLayout(new BorderLayout());
        JPanel centerPanel = new BackgroundPanel(UiConstants.MAIN_GRADIENT_TOP,
                UiConstants.MAIN_GRADIENT_BOTTOM);
        centerPanel.setOpaque(false);
        centerPanel.setLayout(new GridBagLayout());
        JPanel tmp = new JPanel(new GridLayout(profiles.size(), 1, 0, 30));
        tmp.setOpaque(false);
        for (InfoButton button : infoButtons.keySet()) {
            tmp.add(button);
        }
        centerPanel.add(tmp, new GridBagConstraints());
        transitionComponent.add(centerPanel);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        Profile profile = infoButtons.get(e.getSource());
        filter.setDescription(profile.getDescriptionFilter());
        filter.setEndDate(profile.getEndDateFilter());
        filter.setLink(profile.getLinkFilter());
        filter.setStartDate(profile.getStartDateFilter());
        filter.setTitle(profile.getTitleFilter());
        filter.setQuickSearch(null);
        model.updateModel();
        next();
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }
}
