package model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.table.AbstractTableModel;

import entity.Chitiethoadon;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
public class ModelChitiethoadon extends AbstractTableModel {

	private static final long serialVersionUID = 1L;

	private @Setter List<Chitiethoadon> dsCTHD;
	private String[] headers = { "Mã Sản Phẩm", "Tên Sản Phẩm", "Đơn Giá", "Thuế", "Đơn vị tính", "SL Mua",
			"Thành Tiền" };

	public ModelChitiethoadon(List<Chitiethoadon> dsCTHD) {
		this.dsCTHD = dsCTHD;
	}

	@Override
	public int getRowCount() {
		if (dsCTHD != null && !dsCTHD.isEmpty())
			return dsCTHD.size();
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
		switch (columnIndex) {
		case 2:
		case 3:
		case 6:
			return Double.class;
		case 5:
			return Integer.class;
		default:
			return String.class;
		}
	}

	@Override
	public Object getValueAt(int row, int column) {
		if (dsCTHD != null && !dsCTHD.isEmpty()) {
			Chitiethoadon cthd = dsCTHD.get(row);
			switch (column) {
			case 0:
				return cthd.getSanpham().getMasanpham();
			case 1:
				return cthd.getSanpham().getTensanpham();
			case 2:
				return BigDecimal.valueOf(cthd.getDongia()).setScale(1, RoundingMode.FLOOR);
			case 3:
				return cthd.getSanpham().getThue();
			case 4:
				return cthd.getSanpham().getDonvitinh();
			case 5:
				return cthd.getSoluong();
			default:
				return BigDecimal.valueOf(cthd.tinhThanhTien()).setScale(1, RoundingMode.FLOOR);
			}
		}
		return null;
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		Chitiethoadon cthd = dsCTHD.get(rowIndex);
		int k = cthd.getSoluong();
		if ((Integer) aValue > 0 && (Integer) aValue <= cthd.getSanpham().getSoluongton()) {
			cthd.setSoluong((Integer) aValue);
			fireTableCellUpdated(rowIndex, columnIndex);
			fireTableCellUpdated(rowIndex, columnIndex + 1);
		} else if ((Integer) aValue <= 0) {
			JOptionPane.showMessageDialog(null, "Sửa số lượng không thành công. Số lượng phải lớn hơn 0",
					"Sửa số lượng", JOptionPane.ERROR_MESSAGE);
		} else {
			cthd.setSoluong(k);
			fireTableCellUpdated(rowIndex, columnIndex);
			fireTableCellUpdated(rowIndex, columnIndex + 1);
			JOptionPane.showMessageDialog(
					null, "Sửa số lượng không thành công. Sản phẩm này chỉ còn tối đa"
							+ cthd.getSanpham().getSoluongton() + " sản phẩm",
					"Sửa số lượng", JOptionPane.ERROR_MESSAGE);
		}
	}
}
