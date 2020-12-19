package model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import entity.Hoadon;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
public class ModelHoadon extends AbstractTableModel {

	private static final long serialVersionUID = 1L;
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	private @Setter List<Hoadon> dshd;
	private String[] headers = { "Mã hóa đơn", "Tên Khách Hàng", "Ngày Lập", "Người Lập hóa đơn", " Tổng Tiền" };

	public ModelHoadon(List<Hoadon> dshd) {
		super();
		this.dshd = dshd;
	}

	@Override
	public int getRowCount() {
		if (dshd != null && !dshd.isEmpty())
			return dshd.size();
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
		if (columnIndex == 4)
			return Double.class;
		return String.class;
	}

	@Override
	public Object getValueAt(int row, int column) {
		if (dshd != null && !dshd.isEmpty()) {
			Hoadon hd = dshd.get(row);
			switch (column) {
			case 0:
				return hd.getMahoadon();
			case 1:
				return hd.getKhachhang().getHoten();
			case 2:
				return sdf.format(hd.getNgaylap());
			case 3:
				return hd.getNhanvien().getHodem() + " " + hd.getNhanvien().getTen();
			default:
				return BigDecimal.valueOf(hd.getTongtien()).setScale(1, RoundingMode.FLOOR);
			}
		}
		return null;
	}
}
