/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nu.epsilon.rss.rssmanager;

import java.util.SortedSet;

/**
 * A feed represents an entire feed including its name, URL, description and its feed items
 * 
 * @author Johan Frick
 */
public class Feed {

	public String name;
	public String url;
	public String description;
	public SortedSet<FeedItem> feedItems;

	public String toString() {
		return name;
	}

	public boolean equals(Object o) {
		if (o instanceof Feed) {
			return url.equals(((Feed) o).url);
		} else {
			return false;
		}
	}

	public Feed clone() {
		Feed result = new Feed();
		result.name = name;
		result.url = url;
		result.description = description;
		result.feedItems = feedItems;
		return result;
	}
}
