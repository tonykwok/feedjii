package nu.epsilon.rss.ui.transition.views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JPanel;
import nu.epsilon.rss.rssmanager.FeedReader;
import nu.epsilon.rss.rssmanager.FeedReaderImpl.FeedCheckListener;
import nu.epsilon.rss.ui.UiConstants;
import nu.epsilon.rss.ui.components.BackgroundPanel;
import nu.epsilon.rss.ui.transition.View;

/**
 *
 * @author Pär Sikö
 */
public class LoadingView extends View implements FeedCheckListener {

    private FeedReader reader;
    private Logger logger = Logger.getLogger(
            "nu.epsilon.rss.ui.transition.views");

    public LoadingView(FeedReader reader) {
        this.reader = reader;
        reader.addCheckListener(this);
    }

    @Override
    public void setupNextScreen() {

        transitionComponent.removeAll();
        transitionComponent.setLayout(new BorderLayout());
        JPanel centerPanel = new BackgroundPanel(UiConstants.MAIN_GRADIENT_TOP,
                UiConstants.MAIN_GRADIENT_BOTTOM);
        centerPanel.setOpaque(false);
        centerPanel.setLayout(new GridBagLayout());

        JLabel loading = new JLabel("Loading...");
        loading.setFont(new Font("Verdana", Font.BOLD, 35));
        loading.setForeground(Color.WHITE.darker());
        centerPanel.add(loading, new GridBagConstraints());
        transitionComponent.add(centerPanel);

    }

    @Override
    public void checkPerformed() {
        reader.removeCheckListener(this);
        next();
    }
}
