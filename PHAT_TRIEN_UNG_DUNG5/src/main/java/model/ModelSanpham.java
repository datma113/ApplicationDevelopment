package model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import entity.Sanpham;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
public class ModelSanpham extends AbstractTableModel {

	private static final long serialVersionUID = 1L;

	private @Setter @Getter List<Sanpham> dssp;
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	private String[] headers = { "Mã SP", "Tên sản phẩm", "Giá Mua", "Giá Bán", "Đơn giá", "Thuế", "SL Tồn",
			"Thương hiệu", "Xuất xứ", "Loại", "Nhà Cung Cấp", "Màu sắc", "Kích thước", "Khối lượng", "ĐV Tính", "Mô Tả",
			"Ngày Nhập", "Thời Gian BH", "Trạng thái" };

	public ModelSanpham(List<Sanpham> dssp) {
		this.dssp = dssp;
	}

	@Override
	public int getRowCount() {
		if (dssp != null && !dssp.isEmpty())
			return dssp.size();
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
		case 4:
		case 5:
			return Double.class;
		case 6:
		case 17:
			return Integer.class;
		default:
			return String.class;
		}
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		if (dssp != null && !dssp.isEmpty()) {
			Sanpham s = dssp.get(rowIndex);
			switch (columnIndex) {
			case 0:
				return s.getMasanpham();
			case 1:
				return s.getTensanpham();
			case 2:
				return BigDecimal.valueOf(s.getGiamua()).setScale(1, RoundingMode.FLOOR);
			case 3:
				return BigDecimal.valueOf(s.getGiaban()).setScale(1, RoundingMode.FLOOR);
			case 4:
				return BigDecimal.valueOf(s.getGiaban() * (1 + s.getThue())).setScale(1, RoundingMode.FLOOR);
			case 5:
				return s.getThue();
			case 6:
				return s.getSoluongton();
			case 7:
				return s.getThuonghieu();
			case 8:
				return s.getXuatxu();
			case 9:
				return s.getLoaisanpham().getTenloai();
			case 10:
				return s.getNhacungcap().getTennhacungcap();
			case 11:
				return s.getMausac();
			case 12:
				return s.getKichthuoc();
			case 13:
				return s.getKhoiluong();
			case 14:
				return s.getDonvitinh();
			case 15:
				return s.getMota();
			case 16:
				return sdf.format(s.getNgaynhap());
			case 17:
				return s.getThoigianbaohanh();
			default:
				return s.isNgungkinhdoanh() ? "Ngừng kinh doanh" : "Đang bán";
			}
		}
		return null;
	}

}
