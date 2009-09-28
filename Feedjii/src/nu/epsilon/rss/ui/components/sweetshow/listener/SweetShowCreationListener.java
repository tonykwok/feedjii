/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package nu.epsilon.rss.ui.components.sweetshow.listener;

import nu.epsilon.rss.profiles.ProfileManager.Profile;
import nu.epsilon.rss.rssmanager.FeedSubscription;

/**
 *
 * @author Pär Sikö
 */
public interface SweetShowCreationListener extends SweetShowSubscriptionListener {

    void profileCreated(Profile profile);
    
    void profileUpdated(Profile newProfile);
    
    void profileDeleted(Profile profile);
    
}
