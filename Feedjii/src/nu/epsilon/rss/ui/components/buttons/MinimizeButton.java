package nu.epsilon.rss.ui.components.buttons;

import nu.epsilon.rss.ui.utils.FrameUtil;

/**
 *
 * @author Pär Sikö
 */
public class MinimizeButton extends ComponentButton {

    @Override
    public void loadImages() {
        super.button = manager.getImage("images/", "min.png");
        super.buttonMouseOver = manager.getImage("images/", "min_mouseover.png");
        super.buttonPressed = manager.getImage("images/", "min_mousedown.png");
    }

    @Override
    public void buttonPressed() {
        FrameUtil.getInstance().iconify();
    }
}
