package nu.epsilon.rss.rssmanager;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeSet;
import com.sun.syndication.feed.synd.SyndEntryImpl;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.fetcher.FetcherEvent;
import com.sun.syndication.fetcher.FetcherListener;
import com.sun.syndication.fetcher.impl.FeedFetcherCache;
import com.sun.syndication.fetcher.impl.HashMapFeedInfoCache;
import com.sun.syndication.fetcher.impl.HttpURLFeedFetcher;
import com.sun.syndication.io.FeedException;
import java.awt.Image;
import java.net.URI;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import nu.epsilon.rss.common.InvalidCharacterCleaner;
import nu.epsilon.rss.listeners.FeedListener;

/**
 * The FeedReader handles retrieval of feeds. As soon as a feed address is added to it, it will 
 * start to periodically check that feed for updates. It will notify its listeners if an update 
 * is found.
 * 
 * @author Johan Frick
 */
public class FeedReaderImpl implements FeedReader {
    // Maps from feed address to Feed object

    private final Map<String, Feed> feeds = new HashMap<String, Feed>();
    private List<FeedListener> feedListeners = new ArrayList<FeedListener>();
    private List<FeedCheckListener> checkListeners =
            new ArrayList<FeedCheckListener>();
    private Timer timer = new Timer();
    private FeedChecker feedChecker;
    private long checkIntervalMillis = 1000 * 60 * 10;
    private HttpURLFeedFetcher fetcher;
    private FeedFetcherCache feedInfoCache;
    private Logger logger = Logger.getLogger("nu.epsilon.rss.rssmanager");

    /**
     * Constructor which instantiates the feed fetcher
     */
    public FeedReaderImpl() {
        feedInfoCache = HashMapFeedInfoCache.getInstance();
        fetcher = new HttpURLFeedFetcher(feedInfoCache);
        FetcherEventListenerImpl listener = new FetcherEventListenerImpl();
        fetcher.addFetcherEventListener(listener);
    }

    public void addCheckListener(FeedCheckListener listener) {
        checkListeners.add(listener);
    }

    public void removeCheckListener(FeedCheckListener listener) {
        checkListeners.remove(listener);
    }

    //########################################################
    // Implemented interfaces
    //########################################################
    @Override
    public void addFeedListener(FeedListener listener) {
        feedListeners.add(listener);
    }

    @Override
    public void removeFeedListener(FeedListener listener) {
        feedListeners.remove(listener);
    }

    @Override
    public void addFeed(String feedAddress) {
        feeds.put(feedAddress, null);
        if (feeds.size() == 1) {
            // First feed added, start the checker thread
            feedChecker = new FeedChecker();
            timer.schedule(feedChecker, 0, checkIntervalMillis);
        }
    }

    @Override
    public void removeFeed(String feedAddress) {
        feeds.remove(feedAddress);
        try {
            // Must perform the following to clear the fetcher's cache
            feedInfoCache.setFeedInfo(new URL(feedAddress), null);
        } catch (MalformedURLException e) {
            System.err.println("Unable to remove feed from reader. Problems might occur later, " +
                    "like not being able to add the feed again.");
        }
        if (feeds.size() < 1) {
            // last feed removed, stop the checker thread
            feedChecker.cancel();
        }
    }

    @Override
    public void setInterval(long parseLong) {
        // Restart the checker with a new interval
        feedChecker.cancel();
        feedChecker = new FeedChecker();
        checkIntervalMillis = parseLong;
        timer.schedule(feedChecker, 0, checkIntervalMillis);
    }

    @Override
    public long getInterval() {
        return checkIntervalMillis;
    }

    //########################################################
    // Private classes
    //########################################################
    /**
     * Callback for the feed fetcher
     */
    private class FetcherEventListenerImpl implements FetcherListener {

        public void fetcherEvent(FetcherEvent event) {
            String eventType = event.getEventType();
            if (FetcherEvent.EVENT_TYPE_FEED_RETRIEVED.equals(eventType)) {
                // Notify listeners that a feed has been updated
                try {
                    String url = event.getUrlString();
                    Feed feed = generateFeed(url, event.getFeed());
                    removeInvalidCharacterSequencesFromFeed(feed);
                    if (feeds.get(url) == null) {
                        // A new feed has been retrieved
                        for (FeedListener listener : feedListeners) {
                            listener.newFeed(feed);
                        }
                        feeds.put(url, feed);

                    } else {
                        // An updated feed has been retrieved, get the new feed items
                        SortedSet<FeedItem> newFeedItems = FeedUtils.
                                getUpdatedFeedItems(feed, feeds.get(url));
                        feeds.put(url, feed);
                        // Create feed that should be sent to listeners
                        Feed updatedFeed = feed.clone();
                        updatedFeed.feedItems = newFeedItems;
                        for (FeedListener listener : feedListeners) {
                            listener.feedUpdated(updatedFeed);
                        }
                    }

                } catch (Exception e) {
                    logger.logp(Level.WARNING, this.getClass().toString(), "fetcherEvent",
                            "Error occured when handling FetcherEvent", e);
                }
            }
        }

        /**
         * Removes invalid character sequences from all FeedItems contained in feed.
         * Illegal characters are \n, <html> etc.
         *
         * @param feed Container for all FeedItems that will be processed.
         */
        private void removeInvalidCharacterSequencesFromFeed(Feed feed) {
            InvalidCharacterCleaner cleaner = new InvalidCharacterCleaner();
            for(FeedItem item : feed.feedItems) {
                item.description = cleaner.clean(item.description);
                item.title = cleaner.clean(item.title);
            }
        }

    }

    private class FeedChecker extends TimerTask {

        /**
         * This method will be run periodically if the timer has been started.
         */
        @Override
        public void run() {
            // Only perform the check if there are any feeds available
            if (feeds.size() > 0) {
                Iterator<String> feedIterator = null;
//                try {
                    for( String currentUrl : feeds.keySet().toArray(new String[0])) {

//                    for (feedIterator = feeds.keySet().iterator(); feedIterator.hasNext();) {
//                        currentUrl = feedIterator.next();
                    try {
                        fetcher.retrieveFeed(new URL(currentUrl));

                    } catch (FeedException e) {
                        logger.logp(Level.WARNING, this.getClass().toString(),
                                "run", "Error retrieving feed", e);
                    } catch (UnknownHostException e) {
                        logger.logp(Level.WARNING, this.getClass().toString(),
                                "run", "Error retrieving feed", e);
                    } catch (Exception e) {
                        logger.logp(Level.WARNING, this.getClass().toString(),
                                "run", "Error retrieving feed", e);
                    }

                }
                // If there is a listener that would like to know every time a check
                // has been performed, notify that listener.
                FeedCheckListener[] checkListenersArray = checkListeners.toArray(new FeedCheckListener[0]);
                for (FeedCheckListener listener : checkListenersArray) {
                    listener.checkPerformed();
                }
            }
        }
    }
    // ########################################################
    // Listener interfaces
    // ########################################################

    public interface FeedCheckListener {

        public void checkPerformed();
    }
    //########################################################
    // Private methods
    //########################################################

    /**
     * generate FeedItems from a URL
     */
    private Feed generateFeed(String urlString, SyndFeed syndFeed) throws
            IOException,
            IllegalArgumentException, FeedException {
        Feed feed = new Feed();
        feed.url = urlString;
        feed.name = syndFeed.getTitle();
        feed.description = syndFeed.getDescription();
        feed.feedItems = buildFeedItems(syndFeed);
        return feed;
    }

    /**
     * Generates the HTML for the specified feed. Starts by retrieving the data
     * from the URL and then generate the HTML code from that data.
     * 
     * @param feedItem
     */
    @SuppressWarnings("unchecked")
    private SortedSet<FeedItem> buildFeedItems(SyndFeed feed) {
        Image image = null;
        try {
            if (feed != null && feed.getImage() != null) {
                image = ImageIO.read(new URI(feed.getImage().getUrl()).toURL());
            }
        } catch (Exception ex) {
            logger.logp(Level.WARNING, this.getClass().toString(), "buildFeedItems",
                    "Error building feed", ex);
        }

        List<SyndEntryImpl> entries = (List<SyndEntryImpl>) feed.getEntries();
        SortedSet<FeedItem> result = new TreeSet<FeedItem>();
        for (SyndEntryImpl entry : entries) {
            FeedItem item = new FeedItem();
            item.image = image;
            item.link = entry.getLink();
            item.title = entry.getTitle();
            item.publishedDate = entry.getPublishedDate();
            item.description = entry.getDescription().getValue();
            result.add(item);
        }
        return result;
    }
}
