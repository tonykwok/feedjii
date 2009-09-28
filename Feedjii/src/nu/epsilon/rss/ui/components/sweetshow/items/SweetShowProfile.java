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
import nu.epsilon.rss.profiles.ProfileManager.Profile;
import nu.epsilon.rss.ui.components.dark.DarkButton;
import nu.epsilon.rss.ui.components.dark.DarkLabel;
import nu.epsilon.rss.ui.components.dark.DarkTextField;

/**
 *
 * @author Pär Sikö
 */
public class SweetShowProfile extends SweetShowItem implements ActionListener {

    private Profile profile;
    private JTextField name;
    private JTextField title;
    private JTextField description;
    private JTextField link;
    private JButton save;
    private JButton delete;

    /**
     * Constructs a new SweetShowProfile without Profile backup.
     */
    public SweetShowProfile() {
        id = 0;
        type = Type.PROFILE;
    }

    /**
     * Constructs a new instance backed up by profile. If profile is null then this is a new object.
     * Calling new SweetShowProfile(null) is the same as calling new SweetShowProfile()
     * 
     * @param profile profile that this object should display.
     */
    public SweetShowProfile(Profile profile) {
        id = profile.getId();
        this.profile = profile;
        type = Type.PROFILE;
    }

    @Override
    public JComponent getDisplayItem() {
        JPanel panel = new JPanel(new MigLayout(new LC().fillX()));
        panel.setOpaque(false);

        panel.add(new DarkLabel("Name:"));
        name = new DarkTextField(20);
        panel.add(name, "growx, wrap, span");

        panel.add(new DarkLabel("Title filter:"));
        title = new DarkTextField(20);
        panel.add(title, "growx, wrap, span");

        panel.add(new DarkLabel("Description filter:"));
        description = new DarkTextField(20);
        panel.add(description, "growx, wrap, span");

        panel.add(new DarkLabel("Link filter:"));
        link = new DarkTextField(20);
        panel.add(link, "growx, span, wrap 8");

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
        String titleStr = "";
        String descriptionStr = "";
        String linkStr = "";

        if (profile != null) {
            nameStr = profile.getProfileName();
            titleStr = profile.getTitleFilter();
            descriptionStr = profile.getDescriptionFilter();
            linkStr = profile.getLinkFilter();
        }

        name.setText(nameStr);
        title.setText(titleStr);
        description.setText(descriptionStr);
        link.setText(linkStr);

        delete.setVisible(profile != null);
        save.setText(profile == null ? "Save" : "Update");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() != save) {
            listener.profileDeleted(profile);
            return;
        }

        if (profile == null) {
            Profile newProfile = new Profile(null, name.getText());
            newProfile.setDescriptionFilter(description.getText());
            newProfile.setLinkFilter(link.getText());
            newProfile.setTitleFilter(title.getText());
            listener.profileCreated(newProfile);
            return;
        }

        profile.setProfileName(name.getText());
        profile.setDescriptionFilter(description.getText());
        profile.setLinkFilter(link.getText());
        profile.setTitleFilter(title.getText());

        listener.profileUpdated(profile);
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
        refresh();
    }
}
