package model;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import entity.Taikhoan;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
public class ModelTaikhoan extends AbstractTableModel {

	private static final long serialVersionUID = 1L;

	private @Setter List<Taikhoan> dstk;
	private String[] headers = { "Mã Nhân Viên", "Tên Đăng Nhập", "Mật Khẩu" };

	public ModelTaikhoan(List<Taikhoan> dstk) {
		this.dstk = dstk;
	}

	@Override
	public int getRowCount() {
		if (dstk != null && !dstk.isEmpty())
			return dstk.size();
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
		if (dstk != null && !dstk.isEmpty()) {
			Taikhoan tk = dstk.get(row);
			switch (column) {
			case 0:
				return tk.getTendangnhap();
			case 1:
				return tk.getTendangnhap();
			default:
				return "*********";
			}
		}
		return null;
	}
}
