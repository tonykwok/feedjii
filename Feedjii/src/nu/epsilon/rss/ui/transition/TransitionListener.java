/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package nu.epsilon.rss.ui.transition;

/**
 *
 * @author Pär Sikö
 */
public interface TransitionListener {

	void transitioningToNextView(View nextView);
	
	void transitioningToPreviousView(View nextView);
	
}
