package nu.epsilon.rss.ui.components.buttons;

import javax.swing.JFrame;

/**
 *
 * @author Pär Sikö
 */
public class ButtonFactory {

    public enum Type {

        CLOSE, MINIMIZE, MAXIMIZE, RESIZE
    };

    public static ComponentButton getButton(Type type) {

        ComponentButton button = null;

        switch (type) {
            case CLOSE:
                button = new CloseButton();
                break;
            case MINIMIZE:
                button = new MinimizeButton();
                break;
            case MAXIMIZE:
                button = new MaximizeButton();
                break;
        }

        return button;

    }

    public static void main(String[] args) {

        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(getButton(Type.CLOSE));
        frame.setBounds(100, 100, 200, 200);
        frame.setVisible(true);

    }
}
