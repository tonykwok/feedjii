/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nu.epsilon.rss.persistence;

import java.util.Date;
import java.util.List;
import java.util.TreeMap;
import nu.epsilon.rss.profiles.ProfileManager.Profile;
import nu.epsilon.rss.rssmanager.FeedItem;
import nu.epsilon.rss.rssmanager.FeedSubscription;

/**
 *
 * @author Pär Sikö
 */
public interface FeedStorage {

    /**
     * Save all feeds.
     * 
     * @param feeds a map containing Feed as key and a boolean, indicating if the Feed is new, as value.
     */
    void saveFeeds(TreeMap<FeedItem, Boolean> feeds);

    /**
     * Gets all feeds
     * 
     * @return a map containing Feed as key and a boolean, indicating if the Feed is new, as value.
     */
    TreeMap<FeedItem, Boolean> getFeeds();

    /**
     * Clears all Feeds, Profiles, Subscriptions and other data.
     */
    void clear();

    /**
     * Removes all Feeds older than a specific date.
     * 
     * @param olderThan remove all feeds older than this date.
     */
    void removeOldFeeds(Date olderThan);

    /**
     * Saves Profiles.
     * 
     * @param profiles a list with all Profiles to save.
     */
    void saveProfiles(List<Profile> profiles);

    /**
     * Gets all Profiles.
     * 
     * @return a list with all Profiles.
     */
    List<Profile> getProfiles();

    /**
     * Gets all Subscriptions.
     * 
     * @return a list with all subscriptions.
     */
    List<FeedSubscription> getSubscriptions();

    /**
     * Saves subscriptions.
     * 
     * @param subscriptions a list with all subscriptions to save.
     */
    void saveSubscriptions(List<FeedSubscription> subscriptions);
    
    /**
     * Saves start and end dates used by the time line component.
     * 
     * @param startDate start date on time line.
     * @param endDate end date on time line.
     */
    void saveTimeLineDates(Date startDate, Date endDate);
    
    /**
     * Gets the start date used by the time line.
     * 
     * @return start date.
     */
    Date getTimelineStartDate();
    
    /**
     * Gets the end date used by the time line.
     * 
     * @return end date.
     */
    Date getTimelineEndDate();

    /**
     * Saves the proxy host.
     *
     * @param host proxy host.
     */
    void setProxyHost(String host);

    /**
     * Gets the proxy host.
     *
     * @return proxy host.
     */
    String getProxyHost();

    /**
     * Saves the proxy port.
     *
     * @param port proxy port.
     */
    void setProxyPort(String port);

    /**
     * Gets the proxy port.
     *
     * @return proxy port.
     */
    String getProxyPort();

}
