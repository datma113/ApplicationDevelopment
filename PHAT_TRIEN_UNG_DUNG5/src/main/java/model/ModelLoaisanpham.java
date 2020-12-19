package model;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import entity.Loaisanpham;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
public class ModelLoaisanpham extends AbstractTableModel {

	private static final long serialVersionUID = 1L;

	private @Setter List<Loaisanpham> dslsp;
	private String[] headers = { "Mã Loại", "Tên Loại" };

	public ModelLoaisanpham(List<Loaisanpham> dslsp) {
		this.dslsp = dslsp;
	}

	@Override
	public int getRowCount() {
		if (dslsp != null && !dslsp.isEmpty())
			return dslsp.size();
		return 0;
	}

	@Override
	public int getColumnCount() {
		return headers.length;
	}

	@Override
	public String getColumnName(int column) {
		return headers[column];
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return String.class;
	}

	@Override
	public Object getValueAt(int row, int column) {
		if (dslsp != null && !dslsp.isEmpty()) {
			Loaisanpham lsp = dslsp.get(row);
			if (column == 0)
				return lsp.getMaloai();
			return lsp.getTenloai();
		}
		return null;
	}
}
