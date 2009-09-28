package nu.epsilon.rss.rssmanager;

import java.util.SortedSet;
import java.util.TreeSet;

public class FeedUtils {
   
   /**
    * Generate HTML from a feed object
    * @param feed
    * @return
    */
   public static String generateHtml(Feed feed) {
      StringBuffer htmlContent = new StringBuffer();
      htmlContent.append("<h1>" + feed.name + "</h1>");
      htmlContent.append(feed.description);
      
      for (FeedItem feedItem : feed.feedItems) {
         htmlContent.append("<h2><a href='" + feedItem.link + "'>" + feedItem.title
               + "</a></h2>");
         htmlContent.append("<b>" + feedItem.publishedDate + "</b><br>");
         htmlContent.append(feedItem.description);
         htmlContent.append("<br><br>");
      }
      return htmlContent.toString();
   }
   
   /**
    * Returns all items in updatedFeed list that does not exist in oldFeed list
    * @param updatedFeed
    * @param oldFeed
    * @return all items in updatedFeed list that does not exist in oldFeed list
    */
   public static SortedSet<FeedItem> getUpdatedFeedItems(Feed updatedFeed, Feed oldFeed) {
      SortedSet<FeedItem> updatedFeedItems = new TreeSet<FeedItem>();
      boolean itemIsNew;
      for (FeedItem newFeedItem : updatedFeed.feedItems) {
         itemIsNew = true;
         for (FeedItem oldFeedItem : oldFeed.feedItems) {
            if (oldFeedItem.equals(newFeedItem)) {
               itemIsNew = false;
            }
         }
         if (itemIsNew) {
            updatedFeedItems.add(newFeedItem);
         }
      }
      return updatedFeedItems;
   }
}
