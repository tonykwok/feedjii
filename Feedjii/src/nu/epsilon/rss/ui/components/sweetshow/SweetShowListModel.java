/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nu.epsilon.rss.ui.components.sweetshow;

import nu.epsilon.rss.ui.components.sweetshow.items.SweetShowItem;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import nu.epsilon.rss.profiles.ProfileManager.Profile;
import nu.epsilon.rss.rssmanager.FeedSubscription;
import nu.epsilon.rss.ui.components.sweetshow.items.SweetShowFeedSubscription;
import nu.epsilon.rss.ui.components.sweetshow.items.SweetShowProfile;

/**
 *
 * @author Pär Sikö
 */
public class SweetShowListModel implements ListModel {

    private final List<SweetShowItem> items = new ArrayList<SweetShowItem>();
    private final Set<ListDataListener> listeners = new HashSet<ListDataListener>();
    private int subscriptionEndIndex = 2;
    private final SweetShowList showList;
    
    public SweetShowListModel(SweetShowList list) {
        showList = list;
    }

    @Override
    public int getSize() {
        return items.size();
    }

    @Override
    public Object getElementAt(int index) {
        if (index < 0 || index >= items.size()) {
            throw new IllegalArgumentException("Index " + index + " out of bounds");
        }
        return items.get(index);
    }

    public void removeElement(long id) {
        for (SweetShowItem item : items) {
            if (item.getId() == id) {
                items.remove(item);
                break;
            }
        }
        for (ListDataListener listener : listeners) {
            listener.contentsChanged(new ListDataEvent(this, ListDataEvent.CONTENTS_CHANGED, 0, items.size()));
        }
    }

    public void updateProfile(Profile itemToUpdate) {
        for (SweetShowItem item : items) {
            if (item instanceof SweetShowProfile) {
                SweetShowProfile itemProfile = (SweetShowProfile) item;
                if (itemToUpdate.getId() == itemProfile.getId()) {
                    itemProfile.setProfile(itemToUpdate);
                    itemProfile.setListText(itemToUpdate.getProfileName());
                }
            }
        }
        for (ListDataListener listener : listeners) {
            listener.contentsChanged(new ListDataEvent(this, ListDataEvent.CONTENTS_CHANGED, 0, items.size()));
        }
    }
    
    public void addProfile(Profile profile) {
        SweetShowProfile showProfile = new SweetShowProfile(profile);
        showProfile.setCreationListener(showList);
        showProfile.setListText(profile.getProfileName());
        items.add(items.size() -1, showProfile);
        for (ListDataListener listener : listeners) {
            listener.contentsChanged(new ListDataEvent(this, ListDataEvent.CONTENTS_CHANGED, 0, items.size()));
        }
    }
    
    public void updateSubscription(FeedSubscription itemToUpdate) {
        for (SweetShowItem item : items) {
            if (item instanceof SweetShowFeedSubscription) {
                SweetShowFeedSubscription itemSubscription = (SweetShowFeedSubscription) item;
                if (itemToUpdate.getId() == itemSubscription.getId()) {
                    itemSubscription.setSubscription(itemToUpdate);
                    itemSubscription.setListText(itemToUpdate.getName());
                }
            }
        }
        for (ListDataListener listener : listeners) {
            listener.contentsChanged(new ListDataEvent(this, ListDataEvent.CONTENTS_CHANGED, 0, items.size()));
        }
    }
    
    public void addSubscription(FeedSubscription subscription) {
        SweetShowFeedSubscription showSubscription = new SweetShowFeedSubscription(subscription);
        showSubscription.setCreationListener(showList);
        showSubscription.setListText(subscription.getName());
        items.add(items.size() -1, showSubscription);
        for (ListDataListener listener : listeners) {
            listener.contentsChanged(new ListDataEvent(this, ListDataEvent.CONTENTS_CHANGED, 0, items.size()));
        }
    }

    /**
     * Adds an item to this model.
     * @param item
     */
    public void addElement(SweetShowItem item) {

        int index;
        if (item instanceof SweetShowFeedSubscription) {
            items.add(subscriptionEndIndex - 1, item);
            index = subscriptionEndIndex - 1;
            subscriptionEndIndex++;
        } else if (item instanceof SweetShowProfile) {
            items.add(items.size(), item);
            index = items.size();
        } else {
            items.add(items.size(), item);
            index = items.size();
        }

        for (ListDataListener listener : listeners) {
            listener.intervalAdded(new ListDataEvent(this, ListDataEvent.INTERVAL_ADDED, index, index));
        }
    }

    @Override
    public void addListDataListener(ListDataListener listener) {
        listeners.add(listener);
    }

    @Override
    public void removeListDataListener(ListDataListener listener) {
        listeners.remove(listener);
    }
}
