package nu.epsilon.rss.ui.components;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Stroke;
import javax.swing.border.AbstractBorder;

/**
 *
 * @author Pär Sikö
 */
public class XBorder extends AbstractBorder {

	@Override
	public Insets getBorderInsets(Component c) {
		return new Insets(10, 10, 10, 10);
	}

	@Override
	public Insets getBorderInsets(Component c, Insets i) {
		return new Insets(10, 10, 10, 10);
	}

	@Override
	public void paintBorder(Component c, Graphics _g, int x, int y, int width, int height) {
		Graphics2D g = (Graphics2D) _g;
		Stroke s = new BasicStroke(3);
		g.setStroke(s);
		g.setColor(Color.WHITE);
		g.drawRoundRect(3, 3, width - 6, height - 6, 10, 10);

	}
}
