/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nu.epsilon.rss.rssmanager;

import java.io.Serializable;

/**
 *
 * @author Pär Sikö
 */
public class FeedSubscription implements Serializable {

    private long id;
    private String name;
    private String url;
    private String description;

    public FeedSubscription(String name, String url, String description) {
        id = (long) (Math.random() * Long.MAX_VALUE);
        this.name = name;
        this.url = url;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public long getId() {
        return id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + (int) (this.id ^ (this.id >>> 32));
        System.out.println(this + " hash: " + hash);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        System.out.println(this + " equals " + obj + "?");

        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            System.out.println("Wrong class");
            return false;
        }
        final FeedSubscription other = (FeedSubscription) obj;

        System.out.println(this.getId() + "/" + other.getId());
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return getName();
    }
}
