package custom;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Shape;
import java.awt.geom.RoundRectangle2D;

import javax.swing.JButton;

/**
 * ke thua lop jbutton dung de ve button
 * 
 * @author kienc
 *
 */
public class MyJButton extends JButton {

	private static final long serialVersionUID = 1L;

	private static final int FONT_SIZE = 12;
	private static final int RED = 255;
	private static final int GREEN = 160;
	private static final int BLUE = 60;
	private static final int ARC = 20;
	private static Shape shape;

	public MyJButton(String label) {
		super(label);
		setContentAreaFilled(false);
		setFont(new Font(Font.SANS_SERIF, Font.BOLD, FONT_SIZE));
		setFocusPainted(true);
	}

	@Override
	protected void paintComponent(Graphics g) {
		if (getModel().isArmed()) {
			g.setColor(new Color(249, 204, 118));
		} else {
			g.setColor(new Color(RED, GREEN, BLUE));
		}
		g.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, ARC, ARC);
		super.paintComponent(g);
	}

	@Override
	protected void paintBorder(Graphics g) {
		g.setColor(new Color(RED, GREEN, BLUE));
		g.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, ARC, ARC);
	}

	@Override
	public boolean contains(int x, int y) {
		if (shape == null || !shape.getBounds().equals(getBounds())) {
			shape = new RoundRectangle2D.Float(0, 0, getWidth() - 1f, getHeight() - 1f, ARC, ARC);
		}
		return shape.contains(x, y);
	}
}
