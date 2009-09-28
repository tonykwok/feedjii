package nu.epsilon.rss.rssmanager;

import nu.epsilon.rss.listeners.FeedListener;

/**
 * The FeedReader handles retrieval of feeds. As soon as a feed address is added to it, it will 
 * start to periodically check that feed for updates. It will notify its listeners if an update 
 * is found.
 * 
 * @author Johan Frick
 */
public interface FeedReader {

   /**
    * Add a listener that will be notified as soon as any feed added has been updated. 
    * @param listener
    */
   public void addFeedListener(FeedListener listener);

   /**
    * Remove a listener
    * @param listener
    */
   public void removeFeedListener(FeedListener listener);

   /**
    * Add a feed address, which should be checked periodically for updates.
    * @param feedAddress
    */
   public void addFeed(String feedAddress);

   /**
    * Remove a feed address
    * @param feedAddress
    */
   public void removeFeed(String feedAddress);
   
   /**
    * Set the interval for how often to check for updated feeds
    * @param parseLong
    */
   public void setInterval(long parseLong);
   
   /**
    * Get the interval for how often to check for updated feeds
    */
   public long getInterval();
   
   /**
    * Adds a FeedCheckListener.
    * 
    * @param listener FeedCheckListener to add.
    */
   public void addCheckListener(FeedReaderImpl.FeedCheckListener listener);

    /**
    * Removes a FeedCheckListener.
    * 
    * @param listener FeedCheckListener to remove.
    */
   public void removeCheckListener(FeedReaderImpl.FeedCheckListener listener);
   
}
