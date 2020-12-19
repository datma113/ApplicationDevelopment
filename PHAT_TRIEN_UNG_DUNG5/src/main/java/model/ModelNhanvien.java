package model;

import java.text.SimpleDateFormat;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import entity.Nhanvien;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
public class ModelNhanvien extends AbstractTableModel {

	private static final long serialVersionUID = 1L;

	private @Setter List<Nhanvien> dsnv;
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	private String[] headers = { "Mã NV", "Họ", "Tên", "Ngày sinh", "Giới Tính", "Chức Vụ", "Địa chỉ", "SĐT",
			"Số CMND" };

	public ModelNhanvien(List<Nhanvien> dsnv) {
		this.dsnv = dsnv;
	}

	@Override
	public String getColumnName(int column) {
		return headers[column];
	}

	@Override
	public int getRowCount() {
		if (dsnv != null && !dsnv.isEmpty())
			return dsnv.size();
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
		if (dsnv != null && !dsnv.isEmpty()) {
			Nhanvien nv = dsnv.get(rowIndex);
			switch (columnIndex) {
			case 0:
				return nv.getManhanvien();
			case 1:
				return nv.getHodem();
			case 2:
				return nv.getTen();
			case 3:
				return sdf.format(nv.getNgaysinh());
			case 4:
				return nv.isGioitinh() == true ? String.valueOf("Nam") : String.valueOf("Nữ");
			case 5:
				return nv.getChucvu();
			case 6:
				return nv.getDiachi();
			case 7:
				return nv.getSodienthoai();
			default:
				return nv.getSocmnd();
			}
		}
		return null;
	}
}
