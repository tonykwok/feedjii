/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nu.epsilon.rss.rssmanager;

import java.awt.Image;
import java.io.Serializable;
import java.util.Date;

/**
 * A feed item represents a single item in a feed
 */
public class FeedItem implements Comparable<FeedItem>, Serializable {

    public static final long serialVersionUID = 1L;
    
    public String title;
    public String link;
    public Date publishedDate;
    public String description;
    public transient Image image;

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 89 * hash + (this.title != null ? this.title.hashCode() : 0);
        hash = 89 * hash + (this.link != null ? this.link.hashCode() : 0);
        hash = 89 * hash + (this.publishedDate != null ? this.publishedDate.hashCode() : 0);
        hash = 89 * hash + (this.description != null ? this.description.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof FeedItem) {
            FeedItem item = (FeedItem) o;
            if (title != null && publishedDate != null && link != null && description != null) {
                return title.equals(item.title) && link.equals(item.link) &&
                        publishedDate.equals(item.publishedDate) && description.equals(item.description);
            } else if (title != null && description != null) {
                return title.equals(item.title) && description.equals(item.description);
            } else if (title != null) {
                return title.equals(item.title);
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    /**
     * FeedItems are sorted according the its date
     */
    @Override
    public int compareTo(FeedItem item) {
        if (item.title != null && title != null) {
            return item.title.toLowerCase().compareTo(title.toLowerCase());
        } else if (item.publishedDate != null && publishedDate != null) {
            return item.publishedDate.compareTo(publishedDate);
        } else {
            return 0;
        }
    }
}
