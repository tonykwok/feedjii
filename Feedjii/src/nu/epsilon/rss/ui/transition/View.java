package nu.epsilon.rss.ui.transition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JComponent;
import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.transitions.ScreenTransition;
import org.jdesktop.animation.transitions.TransitionTarget;

/**
 * Class responsible for transitions between screens and component reuse.
 * 
 * @author Pär Sikö
 */
public abstract class View implements TransitionTarget {

    // Map caontaining all components added by subclasses. This gives other Views
    // the possibility to reuse a previously created component.
    private static Map<String, JComponent> components = new HashMap<String, JComponent>();
    // Next view in the sequence.
    private View nextView;
    // Component that subclasses use to setup their individual screens.
    protected JComponent transitionComponent;
    //
    private int transitionTime = 2000;
    private List<TransitionListener> listeners = new ArrayList<TransitionListener>();

    /**
     * Sets the next view.
     * 
     * @param view
     */
    final void setNextView(View view) {
        nextView = view;
    }

    /**
     * Sets the component to use when adding and removing components used in the transition.
     * 
     * @param component component to use.
     */
    final void setTransitionComponent(JComponent component) {
        transitionComponent = component;
    }

    /**
     * Sets the transition time.
     * 
     * @param time
     */
    protected final void setTransitionTime(int time) {
        transitionTime = time;
    }

    /**
     * Gets a previously added component. Use add- and getComponent to reuse components between Views.
     * 
     * @param name
     * @return
     */
    protected final JComponent getComponent(String name) {
        return components.get(name);
    }

    /**
     * Sets a component. Lets a future View reuse a component created by this screen.
     * 
     * @param name name of the component.
     * 
     * @param comp the component to reuse.
     */
    protected final void addComponent(String name, JComponent comp) {
        components.put(name, comp);
    }

    public void addTransitionListener(TransitionListener listener) {
        listeners.add(listener);
    }

    /**
     * Starts the transition between Views.
     */
    protected final void next() {
        for (TransitionListener listener : listeners) {
            listener.transitioningToNextView(nextView);
        }
        transitionComponent.removeAll();
        if (nextView != null) {
            ScreenTransition transition = new ScreenTransition(transitionComponent, nextView, transitionTime);
            Animator animator = new Animator(1000);
            animator.setAcceleration(0.7f);
            transition.setAnimator(animator);
            transition.start();
        }
    }

    protected final void previous() {
    }
}
