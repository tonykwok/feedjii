/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package nu.epsilon.rss.ui.transition.navigation;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import nu.epsilon.rss.ui.transition.ViewUI;

/**
 *
 * @author Pär Sikö
 */
public class NavigatorListener  extends MouseAdapter implements ActionListener{

	private ViewUI viewUI;
	
	public NavigatorListener(ViewUI viewUI) {
		this.viewUI = viewUI;
	}

    @Override
	public void actionPerformed(ActionEvent e) {
		viewUI.next();
	}

    @Override
	public void mouseClicked(MouseEvent e) {
		viewUI.next();
	}
    
}
