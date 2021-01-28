package custom;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

/**
 * class roundpanel dung de ve panel cong
 * 
 * @author kienc
 *
 */
public class MyJPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final int ARC = 25;

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(new Color(255, 180, 0, 80));
		g.fillRoundRect(5, 5, getWidth() - 10, getHeight() - 10, ARC, ARC);
	}
}