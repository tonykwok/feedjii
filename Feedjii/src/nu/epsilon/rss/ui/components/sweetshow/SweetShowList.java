/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package nu.epsilon.rss.ui.components.sweetshow;

import nu.epsilon.rss.profiles.ProfileManager.Profile;
import nu.epsilon.rss.rssmanager.FeedSubscription;
import nu.epsilon.rss.ui.components.sweetshow.items.SweetShowItem;
import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JList;
import nu.epsilon.rss.ui.components.sweetshow.items.SweetShowFeedSubscription;
import nu.epsilon.rss.ui.components.sweetshow.items.SweetShowHeader;
import nu.epsilon.rss.ui.components.sweetshow.items.SweetShowProfile;
import nu.epsilon.rss.ui.components.sweetshow.listener.SweetShowCreationListener;

/**
 *
 * @author Pär Sikö
 */
public class SweetShowList extends JComponent implements SweetShowCreationListener{

    private final JList itemList;
    private final SweetShowListModel listModel;
    private final List<SweetShowListListener> listeners = new ArrayList<SweetShowListListener>();
    
    
    public SweetShowList() {
        setLayout(new BorderLayout());
        listModel = new SweetShowListModel(this);
        itemList = new JList(listModel);
        itemList.setCellRenderer(new SweetShowCellRenderer());
        add(itemList, BorderLayout.WEST);
        SweetShowDisplay display = new SweetShowDisplay();
        itemList.addListSelectionListener(display);
        add(display);
    }
    
    public void addItem(SweetShowItem item) {
        item.setCreationListener(this);
        listModel.addElement(item);
    }
    
    public void addHeader(SweetShowHeader header) {
        listModel.addElement(header);
    }
    
    @Override
    public void profileCreated(Profile profile) {
        listModel.addProfile(profile);
    }
    
    @Override
    public void profileUpdated(Profile newProfile) {
        listModel.updateProfile(newProfile);
    }
    
    @Override
    public void profileDeleted(Profile profile) {
        listModel.removeElement(profile.getId());
    }

    @Override
    public void subscriptionCreated(FeedSubscription subscription) {
        listModel.addSubscription(subscription);
    }

    @Override
    public void subscriptionUpdated(FeedSubscription subscription) {
        listModel.updateSubscription(subscription);
    }
    
    @Override
    public void subscriptionDeleted(FeedSubscription subscription) {
        listModel.removeElement(subscription.getId());
    }
    
}
