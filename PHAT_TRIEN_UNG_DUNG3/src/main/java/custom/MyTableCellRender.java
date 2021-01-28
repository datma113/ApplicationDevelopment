package custom;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

public class MyTableCellRender implements TableCellRenderer {

	private static final DefaultTableCellRenderer DEFAULT_RENDERER = new DefaultTableCellRenderer();
	private static final Color COLOR_MARK = new Color(210, 210, 210);
	private static final Color COLOR_SELECTED = new Color(0, 190, 43, 100);

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		Component c = DEFAULT_RENDERER.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

		Font f = c.getFont();
		c.setFont(new Font(f.getName(), Font.ROMAN_BASELINE, f.getSize() + 2));
		if (row % 5 == 0) {
			c.setBackground(COLOR_MARK);
		} else {
			c.setBackground(Color.WHITE);
		}

		if (isSelected) {
			c.setBackground(COLOR_SELECTED);
		}

		return c;
	}

}