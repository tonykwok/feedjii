package nu.epsilon.rss.model.comparators;

import java.util.Comparator;
import nu.epsilon.rss.rssmanager.FeedItem;

/**
 *
 * @author Pär Sikö
 */
public class DateComparator implements Comparator<FeedItem> {

    boolean newestFirst = true;

    public DateComparator(boolean newestFirst) {
        this.newestFirst = newestFirst;
    }

    @Override
    public int compare(FeedItem item1, FeedItem item2) {
        if (item1 != null && item1.publishedDate != null && item2 != null &&
                item2.publishedDate != null) {
            if (!newestFirst) {
                return item1.publishedDate.compareTo(item2.publishedDate);
            } else {
                return item2.publishedDate.compareTo(item1.publishedDate);
            }
        } else if(item1 != null && item1.publishedDate != null) {
            return -1;
        } else if(item2 != null && item2.publishedDate != null) {
            return 1;
        } else {
            return item1.description.compareTo(item2.description);
        }
    }
}
