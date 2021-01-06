package custom;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Shape;
import java.awt.geom.RoundRectangle2D;

import javax.swing.JTextField;
import javax.swing.SwingConstants;

/**
 * class roundtextfield dung de ve textfile cong
 * 
 * @author kienc
 *
 */
public class MyJTextField extends JTextField {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final int FONT_SIZE = 18;
	private static final int ARC = 20;
	private Shape shape;

	public MyJTextField() {
		setOpaque(false);
		setHorizontalAlignment(SwingConstants.CENTER);
		Font f = this.getFont();
		setFont(new Font(f.getName(), f.getStyle(), FONT_SIZE));
	}

	public MyJTextField(String text) {
		this();
		setText(text);
	}

	public MyJTextField(int cols) {
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