package model;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import entity.Nhacungcap;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
public class ModelNhacungcap extends AbstractTableModel {

	private static final long serialVersionUID = 1L;

	private @Setter List<Nhacungcap> dsncc;
	private String[] headers = { "Mã NCC", "Tên Nhà CC", "Email", "Địa Chỉ" };

	public ModelNhacungcap(List<Nhacungcap> dsncc) {
		this.dsncc = dsncc;
	}

	@Override
	public int getRowCount() {
		if (dsncc != null && !dsncc.isEmpty())
			return dsncc.size();
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
		if (dsncc != null && !dsncc.isEmpty()) {
			Nhacungcap ncc = dsncc.get(row);
			switch (column) {
			case 0:
				return ncc.getManhacungcap();
			case 1:
				return ncc.getTennhacungcap();
			case 2:
				return ncc.getEmail();
			default:
				return ncc.getDiachi();
			}
		}
		return null;
	}
}
