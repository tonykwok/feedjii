package nu.epsilon.rss.ui.components.buttons;

import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import nu.epsilon.rss.model.FeedModelImpl;
import nu.epsilon.rss.persistence.FeedStorage;
import nu.epsilon.rss.rssmanager.FeedItem;
import nu.epsilon.rss.ui.backend.BackendUIImpl;
import nu.epsilon.rss.ui.components.buttons.listener.CloseListener;

/**
 *
 * @author Pär Sikö
 */
class CloseButton extends ComponentButton {

    private final List<CloseListener> listeners = new ArrayList<CloseListener>();

    @Override
    public void loadImages() {
        super.button = manager.getImage("close.png");
        super.buttonMouseOver = manager.getImage("close.png");
        super.buttonPressed = manager.getImage("close.png");
    }

    @Override
    public void buttonPressed() {

        //TODO: this is not a good solution, must be refactorered.
        FeedModelImpl model = (FeedModelImpl)BackendUIImpl.getInstance().getResource("model");
        FeedStorage storage = (FeedStorage)BackendUIImpl.getInstance().getResource("storage");
        TreeMap<FeedItem, Boolean> items = model.getAllItems();

        for (FeedItem item : items.keySet()) {
            items.put(item, false);
        }
        storage.saveFeeds(items);

        for (CloseListener listener : listeners) {
            listener.applicationIsClosing();
        }
        TrayIcon[] trayIcons = SystemTray.getSystemTray().getTrayIcons();
        for (TrayIcon icon : trayIcons) {
            SystemTray.getSystemTray().remove(icon);
        }
        System.exit(0);
    }

    public void addCloseListener(CloseListener listener) {
        listeners.add(listener);
    }

    public void removeCloseListener(CloseListener listener) {
        listeners.add(listener);
    }
}
