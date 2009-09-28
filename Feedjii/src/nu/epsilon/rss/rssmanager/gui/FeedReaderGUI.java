package nu.epsilon.rss.rssmanager.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.ListSelectionModel;
import nu.epsilon.rss.listeners.FeedListener;
import nu.epsilon.rss.rssmanager.Feed;
import nu.epsilon.rss.rssmanager.FeedReaderImpl;
import nu.epsilon.rss.rssmanager.FeedUtils;
import nu.epsilon.rss.rssmanager.FeedReaderImpl.FeedCheckListener;

/**
 * Feed Reader
 * 
 * @author Johan Frick
 * 
 */
public class FeedReaderGUI {

	/**
	 * Constants
	 */
	private static final int FRAME_WIDTH = 1000;
	private static final int FRAME_HEIGHT = 600;
	private static final String LAST_CHECK_TEXT = "Last check for feed updates: ";
	private static final String FEED_UPDATED = " (updated)";
	/**
	 * Swing stuff
	 */
	private JEditorPane htmlPane = new JEditorPane("text/html", "<center><h2>To show something " +
			"here, select a feed in the list (either by clicking on it or pressing the return key).");
	private JLabel lastCheckIndicator = new JLabel(LAST_CHECK_TEXT + "never");
	private JList feedList;
	private DefaultListModel feedListModel = new DefaultListModel();
	private JScrollPane htmlScrollPane = new JScrollPane(htmlPane);
	private JFrame frame;
	private JButton newFeedButton;
	private JButton removeFeedButton;
	private JButton intervalButton;
	private boolean resetScrollbarPosition;
	/**
	 * Timer related
	 */
	private Timer timer = new Timer();
	private long lastCheckMillis = System.currentTimeMillis();
	private LastCheckCounter lastCheckCounterTask = new LastCheckCounter();
	private FeedReaderImpl feedReader;

	/**
	 * Main method with some example feeds added
	 */
	public static void main(String[] args) {
		new FeedReaderGUI(new String[]{"http://www.kde.org/dot/kde-apps-content.rdf",
					"http://feeds.feedburner.com/tapestrydilbert",
					"http://newsrss.bbc.co.uk/rss/newsonline_uk_edition/front_page/rss.xml"
				});
	}

	/**
	 * Constructor which initializes the feed reader, adds listeners and creates the GUI
	 * 
	 * @param feedUrls
	 */
	public FeedReaderGUI(final String[] feedUrls) {
		htmlPane.setEditable(false);
		feedReader = new FeedReaderImpl();
		for (String string : feedUrls) {
			feedReader.addFeed(string);
		}
		feedReader.addCheckListener(new CheckListener());
		feedReader.addFeedListener(new FeedListenerImpl());
		timer.schedule(lastCheckCounterTask, 0, 1000);
		// Initialize the GUI
		javax.swing.SwingUtilities.invokeLater(new Runnable() {

			public void run() {
				createAndShowGUI(feedUrls);
			}
		});
	}

	// ########################################################
	// Private methods
	// ########################################################
	/**
	 * Initializes the GUI
	 */
	private void createAndShowGUI(String[] feeds) {
		// Create and set up the window.
		frame = new JFrame("Feed Reader");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Splitpane for list and right content
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		frame.getContentPane().add(splitPane);
		splitPane.setDividerLocation(FRAME_WIDTH / 3);

		// Left panel
		JPanel leftPanel = new JPanel(new BorderLayout());
		splitPane.setLeftComponent(leftPanel);

		// Add top controls
		JPanel topPanel = new JPanel();
		// Add new feed button
		newFeedButton = new JButton("Add feed");
		ButtonListener buttonListener = new ButtonListener();
		newFeedButton.addActionListener(buttonListener);
		topPanel.add(newFeedButton);
		// Add remove feed button
		removeFeedButton = new JButton("Remove feed");
		removeFeedButton.addActionListener(buttonListener);
		topPanel.add(removeFeedButton);
		// Add set interval button
		intervalButton = new JButton("Set interval");
		intervalButton.addActionListener(buttonListener);
		topPanel.add(intervalButton);
		leftPanel.add(topPanel, BorderLayout.NORTH);


		// Add list
		feedList = new JList(feedListModel);
		feedList.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		FeedListListener feedListListener = new FeedListListener();
		feedList.addMouseListener(feedListListener);
		feedList.addKeyListener(feedListListener);
		leftPanel.add(feedList, BorderLayout.CENTER);
		// Add last check indicator
		leftPanel.add(lastCheckIndicator, BorderLayout.SOUTH);
		lastCheckIndicator.setHorizontalAlignment(JLabel.CENTER);

		// Add right Content, i.e. the feed html
		splitPane.setRightComponent(htmlScrollPane);

		// Show the GUI
		frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
		frame.setVisible(true);

		// Move focus to the list
		feedList.requestFocus();
	}

	/**
	 * This method changes the name of a list item so that the user can see that
	 * the feed has been updated.
	 * 
	 * @param feedItem
	 * @param updated
	 */
	private void setFeedItemUpdated(Feed feedItem, boolean updated) {
		if (updated && !feedItem.name.endsWith(FEED_UPDATED)) {
			feedItem.name = feedItem.name + FEED_UPDATED;
		} else if (feedItem.name.endsWith(FEED_UPDATED)) {
			feedItem.name = feedItem.name.substring(0, feedItem.name.lastIndexOf(FEED_UPDATED));
		}
	}

	// ########################################################
	// Private classes
	// ########################################################
	/**
	 * Makes sure that the feed is shown in the HTML pane when an item is
	 * clicked or pressed.
	 */
	private class FeedListListener extends MouseAdapter implements KeyListener {

		@Override
		public void mouseClicked(MouseEvent event) {
			if (event.getButton() == MouseEvent.BUTTON1) {
				refreshSelectedFeed();
			}
		}

		@Override
		public void keyTyped(KeyEvent keyevent) {
			if (keyevent.getKeyChar() == '\n') {
				refreshSelectedFeed();
			}
		}

		@Override
		public void keyPressed(KeyEvent event) {
		}

		@Override
		public void keyReleased(KeyEvent keyevent) {
		}

		/**
		 * Update list item and HTML content
		 */
		private void refreshSelectedFeed() {
			Feed feedItem = ((Feed) feedList.getSelectedValue());
			if (feedItem != null) {
				setFeedItemUpdated(feedItem, false);
				feedList.repaint();
				htmlPane.setText(FeedUtils.generateHtml(feedItem));
				htmlPane.repaint();
				// Make sure the scroll bars moves up to the top of the page
				resetScrollbarPosition = true;
			}
		}
	}

	/**
	 * Listens for when a check for feed updates has been performed
	 */
	private class CheckListener implements FeedCheckListener {

		@Override
		public void checkPerformed() {
			lastCheckMillis = System.currentTimeMillis();
		}
	}

	/**
	 * Listens for feed notifications
	 */
	private class FeedListenerImpl implements FeedListener {

		@Override
		public void newFeed(Feed updatedFeedItem) {
			feedListModel.addElement(updatedFeedItem);
			// If this is the first feed to be added, select it
			if (feedListModel.getSize() == 1) {
				feedList.setSelectedIndex(0);
			}
			feedList.repaint();
		}

		@Override
		public void feedUpdated(Feed feedWithUpdatedItems) {
			for (int i = 0; i < feedListModel.getSize(); i++) {
				Feed oldFeedItem = (Feed) feedListModel.getElementAt(i);
				if (oldFeedItem.equals(feedWithUpdatedItems)) {
					oldFeedItem.feedItems.addAll(feedWithUpdatedItems.feedItems);
					setFeedItemUpdated(oldFeedItem, true);
				}
			}
			feedList.repaint();
		}

		@Override
		public void connectionFailed(String url, String message) {
			JOptionPane.showMessageDialog(frame, message, "Connection failed",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * Handles adding and removing of feeds as well as changing the feed reader interval
	 */
	private class ButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent event) {
			if (event.getSource() == newFeedButton) {
				// Add a new feed to the list
				JDialog dialog = new AddFeedDialog(frame, feedReader);
				dialog.setVisible(true);
			} else if (event.getSource() == removeFeedButton) {
				// Remove a feed from the list
				int index = feedList.getSelectedIndex();
				// Only remove if an item has been selected
				if (index >= 0) {
					Feed feedItem = (Feed) feedList.getSelectedValue();
					feedListModel.removeElementAt(index);
					// Also remove from the feed reader
					feedReader.removeFeed(feedItem.url);
					if (feedListModel.getSize() >= 1) {
						// Select another list item
						feedList.setSelectedIndex(0);
					}
				}
			} else if (event.getSource() == intervalButton) {
				// Change interval for the feed checker
				JDialog dialog = new IntervalDialog(frame, feedReader);
				dialog.setVisible(true);
			}
		}
	}

	/**
	 * A small class that shows the duration since the last time the feeds were
	 * checked for updates
	 */
	private class LastCheckCounter extends TimerTask {

		@Override
		public void run() {
			if (resetScrollbarPosition && htmlScrollPane.getVerticalScrollBar().getValue() > 0) {
				/**
				 * TODO: After content is added, the scroll bar moves down near the
				 * end. This is a quick fix to move back up. This should probably be
				 * solved in a more pretty way.
				 */
				htmlScrollPane.getVerticalScrollBar().setValue(0);
				htmlPane.repaint();
				resetScrollbarPosition = false;
			}

			long timeSinceLastCheck = System.currentTimeMillis() - lastCheckMillis;
			lastCheckIndicator.setText(LAST_CHECK_TEXT + timeSinceLastCheck + " ms ago");
		}
	}
}