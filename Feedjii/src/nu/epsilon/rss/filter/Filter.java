package nu.epsilon.rss.filter;

import java.util.Date;
import nu.epsilon.rss.profiles.ProfileManager.Profile;
import nu.epsilon.rss.rssmanager.FeedItem;

/**
 * Class provides methods to filter the received posts
 * 
 * @author Pär Sikö
 */
public interface Filter {

    /**
     * Sets the title filter.
     * Only FeedItems matching the given title will be displayed.
     *
     * @param title the title
     */
    void setTitle(String title);

    /**
     * Sets the description filter.
     * Only FeedItems matching the description will be displayed.
     *
     * @param subject the subject to use as filter.
     */
    void setDescription(String subject);

    /**
     * Sets the link filter.
     * Only FeedItems received from the specified source will be displayed.
     *
     * @param from the link source to use as filter.
     */
    void setLink(String from);

    /**
     * Sets the start date.
     * Only FeedItems received after the startDate will be displayed.
     *
     * @param startDate the start date.
     */
    void setStartDate(Date startDate);

    /**
     * Sets the end date.
     * Only FeedItems received before the endDate will be displayed.
     *
     * @param endDate the end date.
     */
    void setEndDate(Date endDate);

    /**
     * Sets the quick search filter.
     * Only FeedItems matching the quick search criteria will be displayed.
     *
     * @param search quick search criteria.
     */
    void setQuickSearch(String search);

    /**
     * Gets the quick search filter.
     *
     * @return quick search filter as regular expression.
     */
    String getQuickSearch();

    /**
     * Sets a profile as filter. The filter will use properties from filter
     * when filtering.
     *
     * @param profile profile to use for filtering.
     */
    void setProfileFilter(Profile profile);

    /**
     * Checks if this filter matches the item.
     * A matching item implies that all filter attributes matches (with AND operator)
     * all FeedItem attributes. Regular expressions are allowed.
     *
     * @param item the item to match.
     * @return a boolean indicating whether the item matches the filter or not.
     */
    boolean matches(FeedItem item);
}
