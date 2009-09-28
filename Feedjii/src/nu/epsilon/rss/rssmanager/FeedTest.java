/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package nu.epsilon.rss.rssmanager;

import nu.epsilon.rss.listeners.FeedListener;

/**
 *
 * @author Pär Sikö
 */
public class FeedTest implements FeedListener{

	public static void main(String[] args) {
		new FeedTest();
	}
	
	public FeedTest() {

		FeedReader reader = new FeedReaderImpl();
		reader.addFeed("http://www.aftonbladet.se/?service=rss");
		reader.addFeedListener(this);
		
	}

	public void newFeed(Feed feed) {
		for( FeedItem feedItem : feed.feedItems ) {
			System.out.println(feedItem.title);
			System.out.println(feedItem.publishedDate);
			
		}
		
	}

	public void feedUpdated(Feed feed) {
		System.out.println("New Updated: " + feed.feedItems.first());
	}

	public void connectionFailed(String url, String message) {
		System.out.println("Connection failed");
	}

}
