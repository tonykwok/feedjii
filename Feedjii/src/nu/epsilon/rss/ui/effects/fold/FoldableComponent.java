package nu.epsilon.rss.ui.effects.fold;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.swing.JComponent;


public class FoldableComponent extends JComponent implements Foldable {

    private JComponent intern;
    private BufferedImage buffer;
    private int preferredHeight;
    private boolean animating = false;

    public FoldableComponent(JComponent component) {
        this.intern = component;
        setLayout(new BorderLayout());
        add(component, BorderLayout.CENTER);
        setHeight(0);
    }

    @Override
    public void setHeight(int height) {
        setPreferredSize(new Dimension(intern.getWidth(), height));
        setSize(getWidth(), height);
    }

    @Override
    public int getOriginalHeight() {
        return preferredHeight;
    }

    @Override
    public void prepareUnfolding() {
        setAnimating(true);
        prepareBuffer();
    }

    @Override
    public void prepareFolding() {
        setAnimating(true);
        prepareBuffer();
    }

    @Override
    public void folded() {
    }

    @Override
    public void unfolded() {
        buffer = null;
        setAnimating(false);
        repaint();
    }

    @Override
    public void paint(Graphics g) {
        if (buffer == null) {
            super.paint(g);
            return;
        }

        g.drawImage(buffer, 0, 0, null);
    }

    public void refreshSize() {
        preferredHeight = intern.getPreferredSize().height;
        if (isAnimating() && buffer != null) {
            return;
        }
        prepareBuffer();
    }

    private void setAnimating(boolean animating) {
        this.animating = animating;
    }

    private boolean isAnimating() {
        return animating;
    }

    private void prepareBuffer() {
        if (getWidth() == 0 || getOriginalHeight() == 0) {
            buffer = null;
            return;
        }

        buffer = new BufferedImage(getWidth(), getOriginalHeight(), BufferedImage.TYPE_INT_ARGB);
        super.paint(buffer.getGraphics());
    }
}
