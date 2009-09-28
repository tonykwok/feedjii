package nu.epsilon.rss.ui.utils;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComponent;

/**
 *
 * @author Pär Sikö
 */
public class FPSCounter extends JComponent {

    private List<Integer> fpsList = new ArrayList<Integer>();
    private int average = 0;
    private int latest = 0;
    private final static FPSCounter INSTANCE = new FPSCounter();

    private FPSCounter() {
    }

    public static FPSCounter getInstance() {
        return INSTANCE;
    }

    public void setFPS(int fps) {
        fpsList.add(fps);
        average = 0;
        for (int fpsFromList : fpsList) {
            average += fpsFromList;
        }
        average = average / fpsList.size();
        latest = fps;
        repaint();
    }

    public int getAverage() {
        return average;
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(300, 30);
    }

    @Override
    protected void paintComponent(Graphics _g) {
        Graphics2D g = (Graphics2D) _g;
        g.setFont(new Font("Verdana", Font.BOLD, 12));
        g.setColor(Color.WHITE);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g.drawString("FPS: " + latest + " Average: " + average, 10, 18);
    }
}
