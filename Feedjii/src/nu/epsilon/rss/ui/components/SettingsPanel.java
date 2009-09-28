/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nu.epsilon.rss.ui.components;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URI;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import nu.epsilon.rss.common.PerformanceTuning;
import nu.epsilon.rss.persistence.FeedStorage;
import nu.epsilon.rss.persistence.FeedStorageImpl;
import nu.epsilon.rss.profiles.ProfileManager.Profile;
import nu.epsilon.rss.repository.ResourceManager;
import nu.epsilon.rss.rssmanager.FeedSubscription;
import nu.epsilon.rss.ui.backend.BackendUIImpl;
import nu.epsilon.rss.ui.components.dark.AccordionHeader;
import nu.epsilon.rss.ui.components.dark.DarkButton;
import nu.epsilon.rss.ui.components.dark.DarkCheckBox;
import nu.epsilon.rss.ui.components.dark.DarkLabel;
import nu.epsilon.rss.ui.components.dark.DarkPanel;
import nu.epsilon.rss.ui.components.dark.DarkTextArea;
import nu.epsilon.rss.ui.components.dark.DarkTextField;
import nu.epsilon.rss.ui.components.panel.accordion.AccordionPanel;
import nu.epsilon.rss.ui.components.sweetshow.items.DarkList;
import nu.epsilon.rss.ui.components.sweetshow.items.SweetShowFeedSubscription;
import nu.epsilon.rss.ui.components.sweetshow.items.SweetShowProfile;
import nu.epsilon.rss.ui.components.sweetshow.listener.SweetShowCreationAdapter;

/**
 *
 * @author Pär Sikö
 */
public class SettingsPanel extends JPanel {

    FeedStorage storage = (FeedStorage) BackendUIImpl.getInstance().
            getResource("storage");

    public SettingsPanel() {
        setOpaque(false);
        setLayout(new BorderLayout());

        AccordionPanel accordion = new AccordionPanel(400);
        accordion.setOpaque(false);

        accordion.add(new AccordionHeader("Subscriptions"),
                getSubscriptionSettingsPanel(), true);
        accordion.add(new AccordionHeader("Profiles"), getProfileSettingsPanel());
        accordion.add(new AccordionHeader("Settings"), getSettingsPanel());
//        accordion.add(new AccordionHeader("Enhancements/Bugs"), getBugPanel());
        accordion.add(new AccordionHeader("Performance"), getPerformancePanel());
        accordion.add(new AccordionHeader("About"), getAboutPanel());

        add(accordion, BorderLayout.CENTER);
    }

    private JComponent getSubscriptionSettingsPanel() {
        return new SubscriptionSettingsPanel();
    }

    private JComponent getProfileSettingsPanel() {
        return new ProfileSettingsPanel();
    }

    private JComponent getPerformancePanel() {
        JPanel panel = new SettingsSubPanel();
        panel.setLayout(new GridLayout(4, 2));
        final JCheckBox nano = new DarkCheckBox("Use Nano Source");
        nano.setSelected(PerformanceTuning.isUseNanoSource());
        panel.add(nano);
        nano.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                PerformanceTuning.setUseNanoSource(nano.isSelected());
            }
        });
        final JCheckBox cache = new DarkCheckBox("Cache Images");
        cache.setSelected(PerformanceTuning.isCacheImages());
        panel.add(cache);
        cache.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                PerformanceTuning.setUseCacheImages(cache.isSelected());
            }
        });
        final JCheckBox ci = new DarkCheckBox("Use Compatible Images");
        ci.setSelected(PerformanceTuning.isUseCompatibleImages());
        panel.add(ci);
        ci.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                PerformanceTuning.setUseCompatibleImages(ci.isSelected());
            }
        });
        final JCheckBox opaque = new DarkCheckBox("Use Opaque Images");
        opaque.setSelected(PerformanceTuning.isUseOpaqueImages());
        panel.add(opaque);
        opaque.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                PerformanceTuning.setUseOpaqueImages(opaque.isSelected());
            }
        });
        final JCheckBox gradients = new DarkCheckBox("Use Gradients");
        gradients.setSelected(PerformanceTuning.isUseGradients());
        panel.add(gradients);
        gradients.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                PerformanceTuning.setUseGradients(gradients.isSelected());
            }
        });
        final JCheckBox softClip = new DarkCheckBox("Use Soft Clipping");
        softClip.setSelected(PerformanceTuning.isUseSoftClip());
        panel.add(softClip);
        softClip.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                PerformanceTuning.setUseSoftClip(softClip.isSelected());
            }
        });


        panel.add(new JLabel());
        JPanel resolutionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        resolutionPanel.setOpaque(false);
        DarkLabel resolutionLabel = new DarkLabel("Resolution:");
        resolutionPanel.add(resolutionLabel);
        final DarkTextField resolution = new DarkTextField(String.valueOf(PerformanceTuning.getResolution()), 3);
        resolution.addKeyListener(new KeyAdapter() {

            @Override
            public void keyTyped(KeyEvent e) {
                try {
                    int res;
                    if (e.getKeyCode() != KeyEvent.VK_BACK_SPACE) {
                        res = Integer.valueOf(resolution.getText() + e.getKeyChar());
                    } else {
                        res = Integer.valueOf(resolution.getText());
                    }
                    resolution.setBackground(Color.DARK_GRAY);
                    PerformanceTuning.setResolution(res);
                } catch (NumberFormatException nfe) {
                    resolution.setBackground(Color.RED);
                }
            }
        });
        resolutionPanel.add(resolution);
        panel.add(resolutionPanel);

        return panel;
    }

    private JComponent getBugPanel() {
        JPanel panel = new SettingsSubPanel();

        panel.setLayout(new BorderLayout(0, 10));
        JLabel label =
                new DarkLabel(
                "<html>Submit an enhancement request or bug by filling out the form below.<br>Thank you</html>");
        panel.add(label, BorderLayout.NORTH);
        JTextArea vTextField = new DarkTextArea(4, 30, "");
        panel.add(vTextField, BorderLayout.CENTER);
        JButton send = new DarkButton("Submit");
        panel.add(send, BorderLayout.SOUTH);
        return panel;
    }

    private JComponent getSettingsPanel() {
        JPanel proxyPanel = new SettingsSubPanel();
        proxyPanel.setLayout(new BorderLayout());
        proxyPanel.add(new DarkLabel("Proxy Settings"), BorderLayout.NORTH);
        JPanel settings = new SettingsSubPanel();
        settings.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 0));
        settings.add(new DarkLabel("Host: "));
        final JTextField host = new DarkTextField(storage.getProxyHost(), 30);
        settings.add(host);
        settings.add(new JLabel("      "));
        settings.add(new DarkLabel("Port: "));
        final JTextField port = new DarkTextField(storage.getProxyPort(), 6);
        settings.add(port);
        settings.add(new JLabel("      "));
        JButton apply = new DarkButton("Apply");
        apply.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                System.setProperty("http.proxyHost", host.getText());
                System.setProperty("http.proxyPort", port.getText());
                storage.setProxyHost(host.getText());
                storage.setProxyPort(port.getText());
            }
        });
        settings.add(apply);

        proxyPanel.add(settings);
        return proxyPanel;
    }

    private JComponent getAboutPanel() {
        JPanel panel = new SettingsSubPanel();
        panel.setLayout(new BorderLayout());
        JLabel info =
                new DarkLabel(
                "<html>Feedjii was written and designed by Pär Sikö and Martin Gunnarsson, to be used as an example in our \"Swing Rocks\" presentations.<br><br>External resources used:<br><li>Icons from the Tango Desktop Project and FAMFAMFAM</li><li>Timing Framework, written by Chet Haase</li><li>MiG Layout, written by Mikael Grev</li><li>...</li></html>");
        info.setVerticalAlignment(JLabel.TOP);
        panel.add(info, BorderLayout.CENTER);

        JPanel photos = new JPanel();
        photos.setOpaque(false);
        photos.add(
                getPhoto("polaroidpär.png", "Pär Sikö", "par.siko@epsilon.nu"));
        photos.add(getPhoto("polaroidmartin.png", "Martin Gunnarsson",
                "martin.gunnarsson@epsilon.nu"));
        panel.add(photos, BorderLayout.EAST);

        JLabel link =
                new JLabel(
                "<html><a href=\"http://www.swing-rocks.com\">www.swing-rocks.com</a></html>");
        link.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseReleased(MouseEvent e) {
                try {
                    Desktop.getDesktop().browse(new URI(
                            "http://www.swing-rocks.com"));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        link.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        panel.add(link, BorderLayout.SOUTH);
        return panel;
    }

    private JLabel getPhoto(String filename, String name, final String email) {
        final JLabel photo = new JLabel(new ImageIcon(ResourceManager.loadImage(
                "images/", filename)));
        photo.setToolTipText(name + " (" + email + ")");
        photo.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseReleased(MouseEvent e) {
                try {
                    Desktop.getDesktop().mail(new URI("mailto:" + email));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        photo.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return photo;
    }
}

class SettingsSubPanel extends JPanel {

    public SettingsSubPanel() {
        setOpaque(false);
        setForeground(Color.WHITE);
        setBorder(new EmptyBorder(10, 10, 10, 10));
    }
}

class ProfileSettingsPanel extends SettingsSubPanel {

    private SweetShowProfile profileForm = new SweetShowProfile();
    private DarkList profileList = new DarkList();

    public ProfileSettingsPanel() {
        setLayout(new BorderLayout());

        initProfileList();

        JPanel listPanel = new DarkPanel();
        listPanel.setLayout(new BorderLayout());

        profileList.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                Object value = profileList.getSelectedValue();
                Profile profile =
                        value instanceof Profile ? (Profile) value : null;
                profileForm.setProfile(profile);
            }
        });

        profileForm.setCreationListener(new SweetShowCreationAdapter() {

            @Override
            public void profileCreated(Profile profile) {
                List<Profile> profiles = loadProfiles();
                profiles.add(profile);
                saveProfiles(profiles);
                refreshProfiles(profiles);
            }

            @Override
            public void profileUpdated(Profile newProfile) {
                refreshProfiles(loadProfiles());
            }

            @Override
            public void profileDeleted(Profile profile) {
                List<Profile> profiles = loadProfiles();
                profiles.remove(profile);
                saveProfiles(profiles);
                refreshProfiles(profiles);
                profileForm.setProfile(null);
            }
        });

        listPanel.add(profileList, BorderLayout.CENTER);
        add(listPanel, BorderLayout.WEST);
        add(profileForm.getDisplayItem(), BorderLayout.CENTER);
    }

    private void refreshProfiles(List<Profile> profiles) {
        DefaultListModel model = new DefaultListModel();
        model.addElement("+ New profile");
        for (Profile profile : profiles) {
            model.addElement(profile);
        }

        profileList.setModel(model);

        //Refresh profile panel
        final ProfilePanel profilePanel = (ProfilePanel) BackendUIImpl.getInstance().getResource("profilepanel");
        if (profilePanel != null) {
            profilePanel.refresh();
        }
    }

    private List<Profile> loadProfiles() {
//        final List<Profile> profiles = ((ProfileManager) BackendUIImpl.getInstance().getResource("profilemanager")).getProfiles();
        FeedStorageImpl feedStorage = FeedStorageImpl.getInstance();
        return feedStorage.getProfiles();
    }

    private void saveProfiles(List<Profile> profiles) {
        FeedStorageImpl feedStorage = FeedStorageImpl.getInstance();
        feedStorage.saveProfiles(profiles);
    }

    private void initProfileList() {
        List<Profile> profiles = loadProfiles();
        refreshProfiles(profiles);
    }
}

class SubscriptionSettingsPanel extends SettingsSubPanel {

    private SweetShowFeedSubscription subscriptionForm =
            new SweetShowFeedSubscription();
    private DarkList subscriptionList = new DarkList();

    public SubscriptionSettingsPanel() {
        setLayout(new BorderLayout());

        initSubscriptionList();

        JPanel listPanel = new DarkPanel();
        listPanel.setLayout(new BorderLayout());

        subscriptionList.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                Object value = subscriptionList.getSelectedValue();
                FeedSubscription subscription = value instanceof FeedSubscription
                        ? (FeedSubscription) value : null;
                subscriptionForm.setSubscription(subscription);
            }
        });

        subscriptionForm.setCreationListener(new SweetShowCreationAdapter() {

            @Override
            public void subscriptionCreated(FeedSubscription subscription) {
                List<FeedSubscription> subscriptions = loadSubscriptions();
                subscriptions.add(subscription);
                saveSubscriptions(subscriptions);
                refreshSubscriptions(subscriptions);
            }

            @Override
            public void subscriptionUpdated(FeedSubscription subscription) {
                refreshSubscriptions(loadSubscriptions());
            }

            @Override
            public void subscriptionDeleted(FeedSubscription subscription) {
                List<FeedSubscription> subscriptions = loadSubscriptions();
                subscriptions.remove(subscription);
                saveSubscriptions(subscriptions);
                refreshSubscriptions(subscriptions);
                subscriptionForm.setSubscription(null);
            }
        });

        listPanel.add(subscriptionList, BorderLayout.CENTER);
        add(listPanel, BorderLayout.WEST);
        add(subscriptionForm.getDisplayItem(), BorderLayout.CENTER);
    }

    private List<FeedSubscription> loadSubscriptions() {
        return FeedStorageImpl.getInstance().getSubscriptions();
    }

    private void saveSubscriptions(List<FeedSubscription> subscriptions) {
        FeedStorageImpl.getInstance().saveSubscriptions(subscriptions);
    }

    private void refreshSubscriptions(List<FeedSubscription> subscriptions) {
        DefaultListModel model = new DefaultListModel();
        model.addElement("+ New subscription");
        for (FeedSubscription subscription : subscriptions) {
            model.addElement(subscription);
        }

        subscriptionList.setModel(model);
    }

    private void initSubscriptionList() {
        refreshSubscriptions(FeedStorageImpl.getInstance().getSubscriptions());
    }
}