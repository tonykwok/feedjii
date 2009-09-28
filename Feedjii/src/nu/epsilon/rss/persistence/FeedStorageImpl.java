package nu.epsilon.rss.persistence;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import nu.epsilon.rss.profiles.ProfileManager.Profile;
import nu.epsilon.rss.rssmanager.FeedItem;
import nu.epsilon.rss.rssmanager.FeedSubscription;
import nu.epsilon.rss.ui.backend.BackendUIImpl;

/**
 * Handles feed and profile storage.
 * Today we store data as serialized objects on disc but this might change.
 * File format:
 * <pre>
 *      #Profiles (stored as a serialized object.):
 *      List<Profiles>
 *      #Feed Items (stored as a serialized object.):
 *      List<FeedItem>
 *      #Subscriptions
 *      List<String>
 *      # End of file.
 * <pre>
 * 
 * @author Pär Sikö
 */
public class FeedStorageImpl implements FeedStorage {

    private List<Profile> profiles;
    private TreeMap<FeedItem, Boolean> feedItems;
    private List<FeedSubscription> subscriptions;
    private final String fileName = "feedjii.data";
    private static FeedStorageImpl INSTANCE = new FeedStorageImpl();
    private Date timelineStartDate;
    private Date timelineEndDate;
    private String proxyHost;
    private String proxyPort;
    private Logger logger = Logger.getLogger("nu.epsilon.rss.persistence");

    public static FeedStorageImpl getInstance() {
        return INSTANCE;
    }

    private FeedStorageImpl() {
        readData();
        BackendUIImpl.getInstance().addResource("storage", this);
    }

    private void readData() {
        try {
            File storageFile = new File(fileName);
            if (storageFile.exists()) {
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(storageFile));
                ois.readUTF();
                profiles = (List<Profile>) ois.readObject();
                ois.readUTF();
                feedItems = (TreeMap<FeedItem, Boolean>) ois.readObject();
                ois.readUTF();
                subscriptions = (List<FeedSubscription>) ois.readObject();
                ois.readUTF();
                timelineStartDate = (Date) ois.readObject();
                ois.readUTF();
                timelineEndDate = (Date) ois.readObject();
                ois.readUTF();
                proxyHost = (String) ois.readObject();
                ois.readUTF();
                proxyPort = (String) ois.readObject();
                ois.close();
            } else {
                feedItems = new TreeMap<FeedItem, Boolean>();
                profiles = new ArrayList<Profile>();
                subscriptions = new ArrayList<FeedSubscription>();
                proxyHost = "";
                proxyPort = "";
            }
        } catch (Exception e) {
            logger.logp(Level.WARNING, this.getClass().toString(), "readData",
                    "Error when reading feedjii data file (feedjii.data)", e);
        }
    }

    private void saveData() {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName, false));
            oos.writeUTF("#Profiles");
            oos.writeObject(profiles);
            oos.writeUTF("#Feed Items");
            oos.writeObject(feedItems);
            oos.writeUTF("#Subscriptions");
            oos.writeObject(subscriptions);
            oos.writeUTF("#Timeline start date");
            oos.writeObject(timelineStartDate);
            oos.writeUTF("#Timeline end date");
            oos.writeObject(timelineEndDate);
            oos.writeUTF("#Proxy hots");
            oos.writeObject(proxyHost);
            oos.writeUTF("#Proxy port");
            oos.writeObject(proxyPort);
            oos.close();
        } catch (Exception e) {
            logger.logp(Level.WARNING, this.getClass().toString(), "saveData",
                    "Error when saving feedjii data file (feedjii.data)", e);
        }
    }

    @Override
    public void saveFeeds(TreeMap<FeedItem, Boolean> feeds) {
        feedItems = feeds;
        saveData();
    }

    @Override
    public void saveProfiles(List<Profile> profiles) {
        this.profiles = profiles;
        saveData();
    }

    @Override
    public TreeMap<FeedItem, Boolean> getFeeds() {
        return feedItems;
    }

    @Override
    public List<Profile> getProfiles() {
        return profiles;
    }

    @Override
    public List<FeedSubscription> getSubscriptions() {
        return subscriptions;
    }

    @Override
    public void saveSubscriptions(List<FeedSubscription> subscriptions) {
        this.subscriptions = subscriptions;
        saveData();
    }

    @Override
    public Date getTimelineEndDate() {
        return timelineEndDate;
    }

    @Override
    public Date getTimelineStartDate() {
        return timelineStartDate;
    }

    @Override
    public void saveTimeLineDates(Date startDate, Date endDate) {
        timelineStartDate = startDate;
        timelineEndDate = endDate;
        saveData();
    }

    @Override
    public void clear() {
        profiles.clear();
        feedItems.clear();
        subscriptions.clear();
        timelineStartDate = null;
        timelineEndDate = null;
        proxyHost = "";
        proxyPort = "";
        saveData();
    }

    @Override
    public void removeOldFeeds(Date olderThan) {
        for (FeedItem item : feedItems.keySet()) {
            if (item.publishedDate == null || item.publishedDate.before(olderThan)) {
                feedItems.remove(item);
                continue;
            }
        }
    }

    @Override
    public void setProxyHost(String host) {
        proxyHost = host;
        saveData();
    }

    @Override
    public String getProxyHost() {
        return proxyHost;
    }

    @Override
    public void setProxyPort(String port) {
        proxyPort = port;
        saveData();
    }

    @Override
    public String getProxyPort() {
        return proxyPort;
    }
}
