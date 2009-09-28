package nu.epsilon.rss.ui.utils;

import javax.swing.JFrame;

/**
 *
 * @author Pär Sikö
 */
public class FrameUtil {

    private static final FrameUtil INSTANCE = new FrameUtil();
    private JFrame frame;

    private FrameUtil() {
    }

    public static FrameUtil getInstance() {
        return INSTANCE;
    }

    public void setFrame(JFrame frame) {
        this.frame = frame;
    }

    public void iconify() {
        if (frame == null) {
            throw new IllegalStateException("setFrame(JFrame frame) must be called before usage");
        }
        frame.setExtendedState(JFrame.ICONIFIED);

    }

    public void maximize() {
        if (frame == null) {
            throw new IllegalStateException("setFrame(JFrame frame) must be called before usage");
        }
        if (frame.getExtendedState() == JFrame.MAXIMIZED_BOTH) {
            frame.setExtendedState(JFrame.NORMAL);
            frame.setResizable(true);
        } else {
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            frame.setResizable(false);
        }

    }
}
