/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nu.epsilon.rss.ui.components.sweetshow.items;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import net.miginfocom.layout.LC;
import net.miginfocom.swing.MigLayout;
import nu.epsilon.rss.rssmanager.FeedSubscription;
import nu.epsilon.rss.ui.components.dark.DarkButton;
import nu.epsilon.rss.ui.components.dark.DarkLabel;
import nu.epsilon.rss.ui.components.dark.DarkTextField;

/**
 *
 * @author Pär Sikö
 */
public class SweetShowFeedSubscription extends SweetShowItem implements ActionListener {

    private FeedSubscription subscription;
    private JTextField name;
    private JTextField description;
    private JTextField url;
    private JButton save;
    private JButton delete;

    public SweetShowFeedSubscription() {
        id = 0;
        type = Type.SUBSCRIPTION;
    }

    public SweetShowFeedSubscription(FeedSubscription feed) {
        id = feed.getId();
        this.subscription = feed;
        type = Type.SUBSCRIPTION;
    }

    @Override
    public JComponent getDisplayItem() {
        JPanel panel = new JPanel(new MigLayout(new LC().fillX()));
        panel.setOpaque(false);

        panel.add(new DarkLabel("Name:"));
        name = new DarkTextField(20);
        panel.add(name, "growx, wrap, span");

        panel.add(new DarkLabel("Description:"));

        description = new DarkTextField(20);
        panel.add(description, "growx, wrap, span");

        panel.add(new DarkLabel("URL:"));

        url = new DarkTextField(20);
        panel.add(url, "growx, span, wrap 8");

        panel.add(new JLabel());

        delete = new DarkButton("Delete");
        delete.addActionListener(this);
        panel.add(delete, "align right");

        save = new DarkButton("Save");
        save.addActionListener(this);
        panel.add(save, "align right");

        refresh();
        return panel;
    }

    public void refresh() {
        String nameStr = "";
        String descriptionStr = "";
        String urlStr = "";

        if (subscription != null) {
            nameStr = subscription.getName();
            descriptionStr = subscription.getDescription();
            urlStr = subscription.getUrl();
        }

        name.setText(nameStr);
        description.setText(descriptionStr);
        url.setText(urlStr);

        delete.setVisible(subscription != null);
        save.setText(subscription == null ? "Save" : "Update");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == save) {
            if (subscription == null) {
                FeedSubscription newSubscription = new FeedSubscription(name.getText(), url.getText(), description.getText());
                listener.subscriptionCreated(newSubscription);
            } else {
                subscription.setDescription(description.getText());
                subscription.setName(name.getText());
                subscription.setUrl(url.getText());
                listener.subscriptionUpdated(subscription);
            }

        } else {
            listener.subscriptionDeleted(subscription);
        }
    }

    public void newSubscription() {
        setSubscription(null);
    }

    public void setSubscription(FeedSubscription subscription) {
        this.subscription = subscription;
        refresh();
    }
}
