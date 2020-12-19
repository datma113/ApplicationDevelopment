package custom;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Shape;
import java.awt.geom.RoundRectangle2D;

import javax.swing.JPasswordField;
import javax.swing.SwingConstants;

public class CustomJPassField extends JPasswordField {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final int ARC = 20;
	private static Shape shape;

	public CustomJPassField() {
		setOpaque(false);
		setHorizontalAlignment(SwingConstants.CENTER);
	}

	public CustomJPassField(String text) {
		this();
		setText(text);
	}

	public CustomJPassField(int cols) {
		this();
		setColumns(cols);
	}

	@Override
	protected void paintComponent(Graphics g) {
		g.setColor(new Color(255, 255, 255));
		g.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, ARC, ARC);
		super.paintComponent(g);
	}

	@Override
	protected void paintBorder(Graphics g) {
		g.setColor(Color.BLUE);
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
