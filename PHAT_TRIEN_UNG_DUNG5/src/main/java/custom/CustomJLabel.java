package custom;

import java.awt.Font;

import javax.swing.JLabel;

public class CustomJLabel extends JLabel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Font f;

	public CustomJLabel() {
		f = getFont();
		setFont(new Font(f.getName(), f.getStyle(), f.getSize() + 2));
		setOpaque(false);
	}

	public CustomJLabel(String text) {
		this();
		setText(text);
	}
}
