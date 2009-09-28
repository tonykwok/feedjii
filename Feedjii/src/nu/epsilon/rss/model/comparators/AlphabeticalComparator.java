package nu.epsilon.rss.model.comparators;

import java.util.Comparator;
import nu.epsilon.rss.rssmanager.FeedItem;

/**
 *
 * @author Pär Sikö
 */
public class AlphabeticalComparator implements Comparator<FeedItem> {

    private final boolean naturalOrder;

    public AlphabeticalComparator(boolean naturalOrder) {
        this.naturalOrder = naturalOrder;
    }

    @Override
    public int compare(FeedItem item1, FeedItem item2) {
        if (item1 != null && item1.title != null && item2 != null &&
                item2.title != null) {
            if(naturalOrder) {
                return item1.title.compareTo(item2.title);
            } else {
                return item2.title.compareTo(item1.title);
            }
        }
        return 0;
    }

}
