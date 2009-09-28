package nu.epsilon.rss.ui.transition;

import java.awt.Color;
import java.awt.Component;
import java.util.LinkedList;
import java.util.List;
import nu.epsilon.rss.ui.UiConstants;
import nu.epsilon.rss.ui.components.BackgroundPanel;
import nu.epsilon.rss.ui.transition.navigation.NavigatorListener;

/**
 * Handles transitions between Views.
 * 
 * Usage:
 * <code>
 *		ViewUI viewUI = new ViewUI();
 *		viewUI.addView( new LoginView() );
 *		viewUI.addView( new FeedView() );
 *		JFrame frame = new JFrame();
 *		frame.add( viewUI );
 * </code>
 * 
 * @author Pär Sikö
 */
public class ViewUI extends BackgroundPanel implements TransitionListener {

    // A list with all views.
    private List<View> views = new LinkedList<View>();
    private View currentView;

    /**
     * Constructs an instance.
     */
    public ViewUI() {
        super(UiConstants.MAIN_GRADIENT_TOP, UiConstants.MAIN_GRADIENT_BOTTOM);
    }

    /**
     * Adds a View to the list of Views.
     * The View transitions are ordered in the same way they were added.
     * 
     * @param view View to add.
     */
    public void addView(View view) {
        view.addTransitionListener(this);
        view.setTransitionComponent(this);
        if (views.isEmpty()) {
            view.setupNextScreen();
            currentView = view;
        } else {
            views.get(views.size() - 1).setNextView(view);
        }
        views.add(view);
    }

    public void setNextComponent(Component next) {
        next.addMouseListener(new NavigatorListener(this));
    }

    public void setPreviousComponent(Component previous) {
    }

    public void previous() {
        currentView.previous();
    }

    public void next() {
        currentView.next();
    }

    @Override
    public void transitioningToNextView(View nextView) {
        currentView = nextView;
    }

    @Override
    public void transitioningToPreviousView(View nextView) {
        currentView = nextView;
    }
}
