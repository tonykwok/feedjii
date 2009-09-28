package nu.epsilon.rss.listeners;

import nu.epsilon.rss.rssmanager.Feed;

/**
 *
 * @author Johan Frick
 */
public interface FeedListener {

    /**
     * Called every time a new feed is downloaded.
     *
     * @param feed new feed
     */
	public void newFeed(Feed feed);

    /**
     * Called every time a feed is updated.
     *
     * @param feed updated feed.
     */
	public void feedUpdated(Feed feed);

    /**
     * Called when a connection fails for some reason.
     *
     * @param url the URL that we couldn't connect to.
     * @param message error message.
     */
	public void connectionFailed(String url, String message);
	
}