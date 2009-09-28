package nu.epsilon.rss.model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import nu.epsilon.rss.filter.Filter;
import nu.epsilon.rss.listeners.FeedListener;
import nu.epsilon.rss.model.comparators.AlphabeticalComparator;
import nu.epsilon.rss.model.comparators.DateComparator;
import nu.epsilon.rss.rssmanager.Feed;
import nu.epsilon.rss.rssmanager.FeedItem;
import nu.epsilon.rss.ui.backend.BackendUI;
import nu.epsilon.rss.ui.backend.BackendUIImpl;

/**
 *
 * @author Pär Sikö
 */
public class FeedModelImpl implements FeedListener, FeedModel {

    private Logger logger = Logger.getLogger("nu.epsilon.rss.model");
    /** List containing all feed items. Value is true if it's a new feed item. */
    private TreeMap<FeedItem, Boolean> items;
    private BackendUI backend;
    private Sort sortOrder = Sort.NEWEST_FIRST;
    private boolean showOnlyNewFeeds;
    private Map<FeedItem, Boolean> visibleItems;
    private List<PropertyChangeListener> listeners =
            new ArrayList<PropertyChangeListener>();
    private final Filter filter;

    public enum Sort {

        ALPHABETICAL, ALPHABETICALLY_REVERSED, NEWEST_LAST, NEWEST_FIRST
    }

    public FeedModelImpl(Filter filter, TreeMap<FeedItem, Boolean> oldItems) {
        backend = BackendUIImpl.getInstance();
        backend.addResource("model", this);
        items = oldItems;
        this.filter = filter;
        visibleItems = new TreeMap<FeedItem, Boolean>();
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        listeners.add(listener);
    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        listeners.remove(listener);
    }

    @Override
    public synchronized void newFeed(Feed feed) {
        for (FeedItem item : feed.feedItems) {
            if(item.publishedDate == null ){
                item.publishedDate = Calendar.getInstance().getTime();
            }
            boolean isNew = !items.keySet().contains(item);
            items.put(item, isNew);
        }
        updateModel();
    }

    @Override
    public synchronized void feedUpdated(Feed feed) {
        for (FeedItem item : feed.feedItems) {
            if(item.publishedDate == null ){
                item.publishedDate = Calendar.getInstance().getTime();
            }
            items.put(item, true);
        }
        updateModel();
    }

    @Override
    public int getItemsMatchingFilter(Filter filter) {
        int matches = 0;
        for (FeedItem item : items.keySet()) {
            if (filter.matches(item) && items.get(item)) {
                matches++;
            }
        }
        return matches;
    }

    @Override
    public Map<FeedItem, Boolean> getVisibleItems() {
        return visibleItems;
    }

    @Override
    public TreeMap<FeedItem, Boolean> getAllItems() {
        return items;
    }

    @Override
    public synchronized void connectionFailed(String url, String message) {
        logger.logp(Level.WARNING, this.getClass().toString(),
                "connectionFailed",
                "error connecting to URL: " + url + ". Message returned: " +
                message);
    }

    @Override
    public Sort getSortOrder() {
        return sortOrder;
    }

    @Override
    public void setSortOrder(Sort sort) {
        sortOrder = sort;
        updateModel();
    }

    @Override
    public void showOnlyNewFeeds(boolean onlyNew) {
        showOnlyNewFeeds = onlyNew;
        updateModel();
    }

    @Override
    public boolean isOnlyShowingNewFeeds() {
        return showOnlyNewFeeds;
    }

    private void fireModelChangeEvent() {
        Map<FeedItem, Boolean> copyOfItems = Collections.unmodifiableMap(visibleItems);
        PropertyChangeEvent event = new PropertyChangeEvent(this.getClass(),
                "feedItems", null, copyOfItems);
        for (PropertyChangeListener listener : listeners) {
            listener.propertyChange(event);
        }
    }

    public void updateModel() {
        visibleItems = sortItems(filterItems(items));
        fireModelChangeEvent();
    }

    private Map<FeedItem, Boolean> filterItems(
            Map<FeedItem, Boolean> itemsToFilter) {
        Map<FeedItem, Boolean> newItems = new HashMap<FeedItem, Boolean>();
        for (FeedItem item : itemsToFilter.keySet()) {
            if ((filter.matches(item) && (!showOnlyNewFeeds ||
                    (showOnlyNewFeeds && itemsToFilter.get(item))))) {
                newItems.put(item, itemsToFilter.get(item));
            }
        }
        return newItems;
    }

    private Map<FeedItem, Boolean> sortItems(Map<FeedItem, Boolean> itemsToSort) {
        TreeMap<FeedItem, Boolean> sortedMap = new TreeMap<FeedItem, Boolean>(
                getComparator(sortOrder));
        sortedMap.putAll(itemsToSort);
        return sortedMap;
    }

    private Comparator<FeedItem> getComparator(Sort sortOrder) {
        Comparator<FeedItem> comp = null;
        switch (sortOrder) {
            case ALPHABETICAL:
                comp = new AlphabeticalComparator(true);
                break;
            case ALPHABETICALLY_REVERSED:
                comp = new AlphabeticalComparator(false);
                break;
            case NEWEST_FIRST:
                comp = new DateComparator(true);
                break;
            case NEWEST_LAST:
                comp = new DateComparator(false);
                break;
            default: throw new IllegalStateException("We are not supposed to be here.");
        }
        return comp;
    }
}
