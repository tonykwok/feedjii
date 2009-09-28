package nu.epsilon.rss.model;

import java.beans.PropertyChangeListener;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import nu.epsilon.rss.filter.Filter;
import nu.epsilon.rss.listeners.FeedListener;
import nu.epsilon.rss.model.FeedModelImpl.Sort;
import nu.epsilon.rss.rssmanager.FeedItem;

/**
 *
 * @author Pär Sikö
 */
public interface FeedModel extends FeedListener{

    public void addPropertyChangeListener(PropertyChangeListener listener);

    public void removePropertyChangeListener(PropertyChangeListener listener);

    /**
     *
     * @param filter
     * @return
     */
    int getItemsMatchingFilter(Filter filter);

    /**
     * Gets the current sort order.
     *
     * @return sort order.
     */
    Sort getSortOrder();

    /**
     * Sets the sorting order of visible feeds.
     *
     * @param sort sort order.
     */
    void setSortOrder(Sort sort);

    /**
     * Sets whether to show only new feeds or old and new together.
     *
     * @param onlyNew true if we should only show new feeds.
     */
    void showOnlyNewFeeds(boolean onlyNew);

    /**
     * Checks if only new feeds are displayed.
     *
     * @return true if only new feeds are displayed.
     */
    boolean isOnlyShowingNewFeeds();

    /**
     * Gets all feed items matching the current filter, ie. all "visible".
     *
     * @return all visible feed items.
     */
    Map<FeedItem, Boolean> getVisibleItems();

    /**
     * Gets all feed items.
     *
     * @return all feed items.
     */
    TreeMap<FeedItem, Boolean> getAllItems();

    void updateModel();

}
