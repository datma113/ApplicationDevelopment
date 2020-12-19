package model;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import entity.Khachhang;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
public class ModelKhachhang extends AbstractTableModel {

	private static final long serialVersionUID = 1L;

	private @Setter List<Khachhang> dskh;
	private String[] headers = { "Mã KH", "Họ Tên", "SĐT", "Địa chỉ" };

	public ModelKhachhang(List<Khachhang> dskh) {
		this.dskh = dskh;
	}

	@Override
	public String getColumnName(int column) {
		return headers[column];
	}

	@Override
	public int getRowCount() {
		if (dskh != null && !dskh.isEmpty())
			return dskh.size();
		return 0;
	}

	@Override
	public int getColumnCount() {
		return headers.length;
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return String.class;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		if (dskh != null && !dskh.isEmpty()) {
			Khachhang kh = dskh.get(rowIndex);
			switch (columnIndex) {
			case 0:
				return kh.getMakhachhang();
			case 1:
				return kh.getHoten();
			case 2:
				return kh.getSodienthoai();
			default:
				return kh.getDiachi();
			}
		}
		return null;
	}
}
