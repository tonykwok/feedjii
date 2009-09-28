package nu.epsilon.rss.ui.components.sweetshow.listener;

import nu.epsilon.rss.rssmanager.FeedSubscription;

public interface SweetShowSubscriptionListener {

    void subscriptionCreated(FeedSubscription subscription);

    void subscriptionUpdated(FeedSubscription newSubscription);

    void subscriptionDeleted(FeedSubscription subscription);
}
