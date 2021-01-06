package custom;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;

import javax.swing.JPanel;

public class MyJProgress extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int p = 0;
	private double g22 = 270;
	private Thread t = new Thread(() -> {
		int i = 0;
		int j = 0;
		while (true) {
			update(i++, j += 2);
			repaint();
			try {
				Thread.sleep(5);
			} catch (InterruptedException e1) {
			}
			if (i == 360)
				i = 10;
		}
	});

	public void update(int p, double g22) {
		this.p = p;
		this.g22 = g22;
	}

	@SuppressWarnings("deprecation")
	public void stop() {
		t.suspend();
		update(0, 0);
		repaint();
	}

	public void start() {
		if (!t.isAlive())
			t.start();
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.translate(this.getWidth() / 2, this.getHeight() / 2);
		g2.rotate(Math.toRadians(g22));
		Arc2D.Float arc = new Arc2D.Float(Arc2D.PIE);
		Ellipse2D c = new Ellipse2D.Float(0, 0, 15, 15);
		arc.setFrameFromCenter(new Point(0, 0), new Point(17, 17));
		c.setFrameFromCenter(new Point(0, 0), new Point(15, 15));
		arc.setAngleStart(0);
		arc.setAngleExtent(-p);
		g2.setColor(Color.RED);
		g2.draw(arc);
		g2.fill(arc);
		g2.setColor(super.getBackground());
		g2.draw(c);
		g2.fill(c);
	}
}
