package ui.panel;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import custom.CustomJLabel;

public class PanelThongke extends JPanel {

	private JPanel pnlMain;
	private JPanel thongkedoanhthuPanel;
	private JPanel thongkeSanphamPanel;
	private JPanel thongkeKhachangPanel;
	private JPanel thongkeNhanvienPanel;
	private JPanel thongkeSanpham_Nhacungcap;
	private JPanel thongkeSanpham_Loaisanpham;
	private JComboBox<String> comboBox;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PanelThongke() {
		setSize(1680, 1021);
		setLayout(null);

		setBackground(Color.WHITE);

		CustomJLabel lblThongKe = new CustomJLabel("THỐNG KÊ");
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

		comboBox = new JComboBox<String>();
		comboBox.setBounds(204, 41, 330, 30);
		comboBox.addItem("Doanh thu theo từng tháng");
		comboBox.addItem("Sản phẩm bán chạy");
		comboBox.addItem("Khách hàng tiềm năng");
		comboBox.addItem("Nhân viên có doanh thu cao");
		comboBox.addItem("Số sản phẩm theo từng nhà cung cấp");
		comboBox.addItem("Số sản phẩm theo từng loại sản phẩm");
		comboBox.addActionListener(e -> changePanel());
		add(comboBox);

		pnlMain.add(thongkedoanhthuPanel);

		CustomJLabel lblNewLabel = new CustomJLabel("Chọn loại thống kê");
		lblNewLabel.setBounds(10, 41, 244, 30);
		add(lblNewLabel);

	}

	private void changePanel() {
		pnlMain.removeAll();
		if (comboBox.getSelectedIndex() == 0)
			pnlMain.add(thongkedoanhthuPanel);
		else if (comboBox.getSelectedIndex() == 1)
			pnlMain.add(thongkeSanphamPanel);
		else if (comboBox.getSelectedIndex() == 2)
			pnlMain.add(thongkeKhachangPanel);
		else if (comboBox.getSelectedIndex() == 3)
			pnlMain.add(thongkeNhanvienPanel);
		else if (comboBox.getSelectedIndex() == 4)
			pnlMain.add(thongkeSanpham_Nhacungcap);
		else if (comboBox.getSelectedIndex() == 5)
			pnlMain.add(thongkeSanpham_Loaisanpham);
		pnlMain.repaint();
		pnlMain.revalidate();
	}

}
