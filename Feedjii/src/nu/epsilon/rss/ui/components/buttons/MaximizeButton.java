package nu.epsilon.rss.ui.components.buttons;

import nu.epsilon.rss.ui.utils.FrameUtil;

/**
 *
 * @author Pär Sikö
 */
public class MaximizeButton extends ComponentButton{

	@Override
	public void loadImages() {
		super.button = manager.getImage("images/", "max.png");
		super.buttonMouseOver = manager.getImage("images/", "max_mouseover.png");
		super.buttonPressed = manager.getImage("images/", "max_mousedown.png");
	}

	@Override
	public void buttonPressed() {
		FrameUtil.getInstance().maximize();
	}
	
}
