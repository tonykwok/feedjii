/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nu.epsilon.rss.ui.transition.views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import nu.epsilon.rss.ui.sweetfadepanel.TransparentPanel;
import nu.epsilon.rss.ui.sweetfadepanel.IconComponent;
import nu.epsilon.rss.ui.sweetfadepanel.SweetTransformPanel;
import nu.epsilon.rss.repository.ResourceManager;
import nu.epsilon.rss.ui.backend.BackendUIImpl;
import nu.epsilon.rss.ui.components.BackgroundPanel;
import nu.epsilon.rss.ui.components.KineticScrollingComponent;
import nu.epsilon.rss.ui.components.ProfilePanel;
import nu.epsilon.rss.ui.components.SearchComponent;
import nu.epsilon.rss.ui.components.SettingsPanel;
import nu.epsilon.rss.ui.components.SortPanel;
import nu.epsilon.rss.ui.transition.View;

/**
 *
 * @author Pär Sikö
 */
public class FeedView extends View implements MouseListener {

    private KineticScrollingComponent feedContainer;
    private JFrame parent;

    public FeedView(KineticScrollingComponent container, JFrame parent) {
        feedContainer = container;
        this.parent = parent;
    }

    @Override
    public void setupNextScreen() {

        ((SearchComponent) BackendUIImpl.getInstance().getResource("searchComponent")).setVisible(true);

        transitionComponent.removeAll();
        transitionComponent.setLayout(new BorderLayout(0, 0));

        feedContainer.setOpaque(false);
        feedContainer.setPreferredSize(new Dimension(550, transitionComponent.getHeight()));
        feedContainer.setSize(new Dimension(550, transitionComponent.getHeight()));
        feedContainer.setLocation(135, 0);

        JPanel menuPanel = new JPanel(null);
        menuPanel.setOpaque(true);
        menuPanel.setBackground(Color.BLACK);

        JPanel bufferPanel = new JPanel(null);
        bufferPanel.setOpaque(false);
        bufferPanel.setPreferredSize(new Dimension(parent.getWidth(), transitionComponent.getHeight()));
        bufferPanel.add(feedContainer);

//        BackgroundPanel topDropShadow = new BackgroundPanel(Color.BLACK, new Color(0, 0, 0, 0));
//        bufferPanel.add(topDropShadow);
//        topDropShadow.setBounds(0, 0, 900, 10);

        SweetTransformPanel fade = new SweetTransformPanel(parent);
        fade.setLocation(20, 20);
        fade.setMaximumSize(500, 100);
        fade.setSize(48, 48);
        fade.setIconComponent(new IconComponent(ResourceManager.loadImage("images/", "icon_profiles.png")));
        TransparentPanel panel = new TransparentPanel();
        final ProfilePanel profilePanel = new ProfilePanel();
        BackendUIImpl.getInstance().addResource("profilepanel", profilePanel);
        panel.add(profilePanel);
        fade.setFullComponent(panel);
        menuPanel.add(fade);

        fade = new SweetTransformPanel(parent);
        fade.setLocation(20, 80);
        fade.setMaximumSize(430, 100);
        fade.setSize(48, 48);
        fade.setIconComponent(new IconComponent(ResourceManager.loadImage("images/", "icon_view.png")));
        panel = new TransparentPanel();
        panel.setLocation(0, 100);
        fade.setFullComponent(panel);
        panel.add(new SortPanel());
        menuPanel.add(fade);

        fade = new SweetTransformPanel(parent);
        fade.setLocation(20, 140);
        fade.setMaximumSize(690, 400);
        fade.setSize(48, 48);
        fade.setIconComponent(new IconComponent(ResourceManager.loadImage("images/", "icon_settings.png")));
        panel = new TransparentPanel();
        panel.setLocation(0, 20);
        panel.setLayout(new BorderLayout());
        panel.setBorder(new EmptyBorder(20, 10, 10, 10));
        fade.setFullComponent(panel);
        panel.add(new SettingsPanel(), BorderLayout.CENTER);
        menuPanel.add(fade);

        bufferPanel.add(menuPanel, BorderLayout.WEST);
        menuPanel.setSize(90, 600);
        menuPanel.setLocation(0, 0);

        transitionComponent.add(bufferPanel);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        next();
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }
}
