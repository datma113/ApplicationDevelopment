package custom;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

public class MyTableCellRenderSanphamHoadon implements TableCellRenderer {

	private static final DefaultTableCellRenderer DEFAULT_RENDERER = new DefaultTableCellRenderer();
	private static final Color COLOR_DISCONTINUTED = new Color(255, 0, 0, 30);
	private static final Color COLOR_SELECTED = new Color(0, 190, 43, 100);
	private static final Color COLOR_SELECTED2 = new Color(255, 0, 0, 100);
	private static final Color COLOR_GOOD = Color.decode("#00BB00");
	private static final Color COLOR_BAD = Color.decode("#CC3333");
	private static final int INDEX_DONGIA = 2;
	private static final int INDEX_SOLUONGTON = 4;
	private static final int INDEX_NGUNGKINHDOANH = 16;
	private Color colorForeground;
	private Color colorBackground;

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		Component c = DEFAULT_RENDERER.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

		colorForeground = Color.BLACK;
		colorBackground = Color.WHITE;
		Object object = table.getValueAt(row, INDEX_NGUNGKINHDOANH);
		String ngungkinhdoanh = object.toString();

		if (column == INDEX_DONGIA) {
			colorForeground = Color.BLACK;
			Font font = c.getFont();
			c.setFont(new Font(font.getFontName(), Font.BOLD, font.getSize() + 2));
		} else if (column == INDEX_SOLUONGTON) {
			Object objectSoluongton = table.getValueAt(row, INDEX_SOLUONGTON);
			try {
				int soluongton = Integer.parseInt(objectSoluongton.toString());
				if (soluongton < 10) {
					colorForeground = COLOR_BAD;
				} else {
					colorForeground = COLOR_GOOD;
				}
			} catch (Exception e) {
			}
			Font font = c.getFont();
			c.setFont(new Font(font.getFontName(), Font.BOLD, font.getSize() + 2));
		} else if (column == INDEX_NGUNGKINHDOANH) {
			if (ngungkinhdoanh.equalsIgnoreCase("Ngừng kinh doanh")) {
				colorForeground = COLOR_BAD;
				colorBackground = COLOR_DISCONTINUTED;
			} else {
				colorForeground = COLOR_GOOD;
			}
			Font font = c.getFont();
			c.setFont(new Font(font.getFontName(), Font.BOLD, font.getSize()));
		}
		if (ngungkinhdoanh.equalsIgnoreCase("Ngừng kinh doanh")) {
			colorBackground = COLOR_DISCONTINUTED;
		}
		c.setForeground(colorForeground);
		c.setBackground(colorBackground);
		Font font = c.getFont();
		c.setFont(new Font(font.getFontName(), Font.ROMAN_BASELINE, font.getSize() + 2));

		if (isSelected) {
			if (ngungkinhdoanh.equalsIgnoreCase("Ngừng kinh doanh"))
				c.setBackground(COLOR_SELECTED2);
			else
				c.setBackground(COLOR_SELECTED);
		}

		return c;
	}

}