package custom;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.plaf.basic.BasicScrollBarUI;

/**
 * custom jscrollbar
 * 
 * @author kienc
 *
 */
public class MyJScrollPane extends JScrollPane {

	private static final long serialVersionUID = 8607734981506765935L;

	private static final int SCROLL_BAR_ALPHA_ROLLOVER = 100;
	private static final int SCROLL_BAR_ALPHA = 50;
	private static final int THUMB_SIZE = 8;
	private static final Color THUMB_COLOR = Color.BLACK;

	public MyJScrollPane(Component view) {
		this(view, VERTICAL_SCROLLBAR_AS_NEEDED, HORIZONTAL_SCROLLBAR_AS_NEEDED);
	}

	public MyJScrollPane(int vsbPolicy, int hsbPolicy) {
		this(null, vsbPolicy, hsbPolicy);
	}

	public MyJScrollPane(Component view, int vsbPolicy, int hsbPolicy) {

		setBorder(null);

		// Set ScrollBar UI
		JScrollBar verticalScrollBar = getVerticalScrollBar();
		verticalScrollBar.setOpaque(false);
		verticalScrollBar.setUI(new ModernScrollBarUI(this));
		verticalScrollBar.setPreferredSize(new Dimension(8, this.getHeight()));

		JScrollBar horizontalScrollBar = getHorizontalScrollBar();
		horizontalScrollBar.setOpaque(false);
		horizontalScrollBar.setUI(new ModernScrollBarUI(this));
		horizontalScrollBar.setPreferredSize(new Dimension(this.getWidth(), 8));

		// Layering
		setComponentZOrder(getVerticalScrollBar(), 0);
		setComponentZOrder(getHorizontalScrollBar(), 1);
		setComponentZOrder(getViewport(), 2);

		viewport.setView(view);
	}

	/**
	 * Class extending the BasicScrollBarUI and overrides all necessary methods
	 */
	private static class ModernScrollBarUI extends BasicScrollBarUI {

		private JScrollPane sp;

		public ModernScrollBarUI(MyJScrollPane sp) {
			this.sp = sp;
		}

		@Override
		protected JButton createDecreaseButton(int orientation) {
			return new InvisibleScrollBarButton();
		}

		@Override
		protected JButton createIncreaseButton(int orientation) {
			return new InvisibleScrollBarButton();
		}

		@Override
		protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
		}

		@Override
		protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
			int alpha = isThumbRollover() ? SCROLL_BAR_ALPHA_ROLLOVER : SCROLL_BAR_ALPHA;
			int orientation = scrollbar.getOrientation();
			int x = thumbBounds.x;
			int y = thumbBounds.y;

			int width = orientation == JScrollBar.VERTICAL ? THUMB_SIZE : thumbBounds.width;
			width = Math.max(width, THUMB_SIZE);

			int height = orientation == JScrollBar.VERTICAL ? thumbBounds.height : THUMB_SIZE;
			height = Math.max(height, THUMB_SIZE);

			Graphics2D graphics2D = (Graphics2D) g.create();
			graphics2D.setColor(new Color(THUMB_COLOR.getRed(), THUMB_COLOR.getGreen(), THUMB_COLOR.getBlue(), alpha));
			graphics2D.fillRect(x, y, width, height);
			graphics2D.dispose();
		}

		@Override
		protected void setThumbBounds(int x, int y, int width, int height) {
			super.setThumbBounds(x, y, width, height);
			sp.repaint();
		}

		/**
		 * Invisible Buttons, to hide scroll bar buttons
		 */
		private static class InvisibleScrollBarButton extends JButton {

			private static final long serialVersionUID = 1552427919226628689L;

			private InvisibleScrollBarButton() {
				setOpaque(false);
				setFocusable(false);
				setFocusPainted(false);
				setBorderPainted(false);
				setBorder(BorderFactory.createEmptyBorder());
			}
		}
	}
}