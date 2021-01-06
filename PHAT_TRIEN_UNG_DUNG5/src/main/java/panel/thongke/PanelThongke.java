package panel.thongke;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import custom.MyJLabel;
import entity.Nhanvien;

public class PanelThongke extends JPanel {

	private JPanel pnlMain;
	private JPanel thongkedoanhthuPanel;
	private JPanel thongkeSanphamPanel;
	private JPanel thongkeKhachangPanel;
	private JPanel thongkeNhanvienPanel;
	private JPanel thongkeSanpham_Nhacungcap;
	private JPanel thongkeSanpham_Loaisanpham;
	private JPanel thongkeSanphamSaphethang;
	private JComboBox<String> comboBox;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PanelThongke(Nhanvien nv) {
		setSize(1680, 1021);
		setLayout(null);

		setBackground(Color.WHITE);

		MyJLabel lblThongKe = new MyJLabel("THỐNG KÊ");
		lblThongKe.setBounds(10, 0, 1670, 30);
		lblThongKe.setHorizontalAlignment(SwingConstants.CENTER);
		lblThongKe.setForeground(Color.RED);
		lblThongKe.setFont(new Font("Serif", Font.BOLD, 20));
		add(lblThongKe);

		pnlMain = new JPanel(null);
		pnlMain.setBounds(10, 82, 1640, 916);
		add(pnlMain);

		thongkedoanhthuPanel = new PanelThongkeDoanhthu();
		thongkeSanphamPanel = new PanelThongkeSanpham();
		thongkeKhachangPanel = new PanelThongkeKhachhang();
		thongkeNhanvienPanel = new PanelThongkeNhanvien();
		thongkeSanpham_Nhacungcap = new PanelThongkeSanpham_Nhacungcap();
		thongkeSanpham_Loaisanpham = new PanelThongkeSanpham_Loaisanpham();
		thongkeSanphamSaphethang = new PanelThongkeSanphamSaphethang();

		comboBox = new JComboBox<String>();
		if (nv.getChucvu().equalsIgnoreCase("Quản Lý")) {
			comboBox.setBounds(204, 41, 330, 30);
			comboBox.addItem("Hôm nay");
			comboBox.addItem("Doanh thu theo từng tháng");
			comboBox.addItem("Sản phẩm bán chạy");
			comboBox.addItem("Khách hàng tiềm năng");
			comboBox.addItem("Nhân viên có doanh thu cao");
			comboBox.addItem("Số sản phẩm theo từng nhà cung cấp");
			comboBox.addItem("Số sản phẩm theo từng loại sản phẩm");
			comboBox.addItem("Sản phẩm sắp hết hàng");
		} else {
			comboBox.setBounds(204, 41, 330, 30);
			comboBox.addItem("Hôm nay");
			comboBox.addItem("Sản phẩm bán chạy");
			comboBox.addItem("Khách hàng tiềm năng");
			comboBox.addItem("Số sản phẩm theo từng nhà cung cấp");
			comboBox.addItem("Số sản phẩm theo từng loại sản phẩm");
			comboBox.addItem("Sản phẩm sắp hết hàng");
		}

		comboBox.addActionListener(e -> changePanel());
		add(comboBox);

		MyJLabel lblNewLabel = new MyJLabel("Chọn loại thống kê");
		lblNewLabel.setBounds(10, 41, 244, 30);
		add(lblNewLabel);

	}

	private void changePanel() {
		pnlMain.removeAll();
		if (comboBox.getSelectedItem().toString().equalsIgnoreCase("Doanh thu theo từng tháng"))
			pnlMain.add(thongkedoanhthuPanel);
		else if (comboBox.getSelectedItem().toString().equalsIgnoreCase("Sản phẩm bán chạy"))
			pnlMain.add(thongkeSanphamPanel);
		else if (comboBox.getSelectedItem().toString().equalsIgnoreCase("Khách hàng tiềm năng"))
			pnlMain.add(thongkeKhachangPanel);
		else if (comboBox.getSelectedItem().toString().equalsIgnoreCase("Nhân viên có doanh thu cao"))
			pnlMain.add(thongkeNhanvienPanel);
		else if (comboBox.getSelectedItem().toString().equalsIgnoreCase("Số sản phẩm theo từng nhà cung cấp"))
			pnlMain.add(thongkeSanpham_Nhacungcap);
		else if (comboBox.getSelectedItem().toString().equalsIgnoreCase("Số sản phẩm theo từng loại sản phẩm"))
			pnlMain.add(thongkeSanpham_Loaisanpham);
		else if (comboBox.getSelectedItem().toString().equalsIgnoreCase("Sản phẩm sắp hết hàng"))
			pnlMain.add(thongkeSanphamSaphethang);
		pnlMain.repaint();
		pnlMain.revalidate();
	}

}
