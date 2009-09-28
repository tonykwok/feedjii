/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nu.epsilon.rss.ui.components.sweetshow;

import nu.epsilon.rss.ui.components.sweetshow.items.SweetShowItem;
import java.awt.BorderLayout;
import java.util.List;
import javax.swing.JFrame;
import nu.epsilon.rss.rssmanager.FeedSubscription;
import nu.epsilon.rss.persistence.FeedStorageImpl;
import nu.epsilon.rss.profiles.ProfileManager.Profile;
import nu.epsilon.rss.ui.components.sweetshow.items.SweetShowFeedSubscription;
import nu.epsilon.rss.ui.components.sweetshow.items.SweetShowHeader;
import nu.epsilon.rss.ui.components.sweetshow.items.SweetShowProfile;

/**
 *
 * @author Pär Sikö
 */
public class SweetShowExample {

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        FeedStorageImpl feedStorage = FeedStorageImpl.getInstance();
        List<FeedSubscription> subscriptions = feedStorage.getSubscriptions();
        SweetShowList list = new SweetShowList();
        list.addHeader(new SweetShowHeader("Subscriptions"));
        for (FeedSubscription subscription : subscriptions) {
            FeedSubscription feed = new FeedSubscription(subscription.getName(), subscription.getUrl(), subscription.
                    getDescription());
            SweetShowItem item = new SweetShowFeedSubscription(feed);
            item.setListText(subscription.getName());
            list.addItem(item);
        }
        FeedSubscription feed = new FeedSubscription("", "", "");
        SweetShowItem item = new SweetShowFeedSubscription(feed);
        item.setListText("New Subscription");
        list.addItem(item);

        List<Profile> profiles = feedStorage.getProfiles();
        list.addHeader(new SweetShowHeader("Profiles"));
        for (Profile profile : profiles) {
            item = new SweetShowProfile(profile);
            item.setListText(profile.getProfileName());
            list.addItem(item);
        }
        item = new SweetShowProfile();
        item.setListText("New Profile");
        list.addItem(item);

        frame.add(list, BorderLayout.CENTER);
        frame.setBounds(100, 100, 500, 400);
        frame.setVisible(true);
    }
}
