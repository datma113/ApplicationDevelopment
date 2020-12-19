package ui.panel;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;

import custom.CustomJButton;
import custom.CustomJLabel;
import custom.CustomJScrollPane;
import custom.CustomTableRenderer;
import dao.DaoHoadon;
import entity.Chitiethoadon;
import entity.Hoadon;
import entity.Khachhang;
import entity.Nhanvien;
import generate_bill.GenerateBillPdf;
import model.ModelChitiethoadon;

public class PanelChitiethoadon extends JPanel {

	private static final long serialVersionUID = 1L;
	private static final int vertical_always = JScrollPane.VERTICAL_SCROLLBAR_ALWAYS;
	private static final int horizontal_always = JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS;

	private JPanel panel;
	private JPanel panel_1;

	private JTable tableChitiet;
	private CustomJLabel lblTongtien_1;
	private DaoHoadon daohd;
	private Hoadon hd;

	public PanelChitiethoadon(String maHD) {
		daohd = new DaoHoadon();
		hd = daohd.getHd(maHD);
		Nhanvien nv = hd.getNhanvien();
		Khachhang kh = hd.getKhachhang();

		setSize(1090, 742);
		setLayout(null);

		panel = new JPanel(null);
		panel.setBounds(10, 11, 1053, 135);
		panel.setBorder(BorderFactory.createEtchedBorder());
		add(panel);

		panel_1 = new JPanel(null);
		panel_1.setBounds(10, 633, 1053, 59);
		panel_1.setBorder(BorderFactory.createEtchedBorder());
		add(panel_1);

		List<Chitiethoadon> dscthd = new ArrayList<Chitiethoadon>();
		dscthd = daohd.getDsCthd(hd.getMahoadon());
		hd.setChitiethoadons(dscthd);
		DecimalFormat df = new DecimalFormat("###,###,###.#");

		ModelChitiethoadon model = new ModelChitiethoadon(dscthd);
		tableChitiet = new JTable(model);
		tableChitiet.setRowHeight(35);
		tableChitiet.setAutoCreateRowSorter(true);
		tableChitiet.getTableHeader().setBackground(new Color(255, 208, 120));
		tableChitiet.getTableHeader().setPreferredSize(new Dimension(tableChitiet.getTableHeader().getWidth(), 30));
		Font f3 = tableChitiet.getTableHeader().getFont();
		tableChitiet.getTableHeader().setFont(new Font(f3.getName(), Font.BOLD, f3.getSize() + 2));

		CustomTableRenderer renderTable = new CustomTableRenderer();
		tableChitiet.setDefaultRenderer(String.class, renderTable);
		tableChitiet.setDefaultRenderer(Double.class, renderTable);
		tableChitiet.setDefaultRenderer(Integer.class, renderTable);

		CustomJScrollPane scrollChiTiet = new CustomJScrollPane(tableChitiet, vertical_always, horizontal_always);
		scrollChiTiet.setBounds(10, 169, 1053, 453);
		add(scrollChiTiet);

		tableChitiet.getColumnModel().getColumn(0).setMinWidth(110);
		tableChitiet.getColumnModel().getColumn(1).setMinWidth(332);
		tableChitiet.getColumnModel().getColumn(2).setMinWidth(100);
		tableChitiet.getColumnModel().getColumn(3).setMinWidth(100);
		tableChitiet.getColumnModel().getColumn(4).setMinWidth(100);
		tableChitiet.getColumnModel().getColumn(5).setMinWidth(100);
		tableChitiet.getColumnModel().getColumn(6).setMinWidth(200);
		tableChitiet.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

		CustomJLabel lblNewLabel = new CustomJLabel("Mã hóa đơn: " + hd.getMahoadon());
		lblNewLabel.setBounds(10, 11, 245, 25);
		panel.add(lblNewLabel);

		CustomJLabel lblNhanvien = new CustomJLabel("Nhân viên lập: " + nv.getHodem() + " " + nv.getTen());
		lblNhanvien.setBounds(589, 11, 414, 25);
		panel.add(lblNhanvien);

		CustomJLabel lblMakh = new CustomJLabel("Mã KH: " + kh.getMakhachhang());
		lblMakh.setBounds(10, 47, 245, 25);
		panel.add(lblMakh);

		CustomJLabel lblNgay = new CustomJLabel(
				"Ngày lập: " + new SimpleDateFormat("yyyy-MM-dd").format(hd.getNgaylap()));
		lblNgay.setBounds(850, 47, 193, 25);
		panel.add(lblNgay);

		CustomJLabel lblHotenKhach = new CustomJLabel("Họ tên KH: " + kh.getHoten());
		lblHotenKhach.setBounds(265, 47, 314, 25);
		panel.add(lblHotenKhach);

		CustomJLabel lblSdt = new CustomJLabel("SĐT: " + kh.getSodienthoai());
		lblSdt.setBounds(589, 47, 251, 25);
		panel.add(lblSdt);

		CustomJLabel lblDiachi = new CustomJLabel("Địa chỉ: " + kh.getDiachi());
		lblDiachi.setBounds(10, 83, 993, 25);
		panel.add(lblDiachi);

		CustomJLabel lblNhanvien_1 = new CustomJLabel("Mã nhân viên: " + nv.getManhanvien());
		lblNhanvien_1.setBounds(265, 11, 314, 25);
		panel.add(lblNhanvien_1);

		CustomJLabel lblTongtien = new CustomJLabel("Tổng tiền: ");
		lblTongtien.setBounds(672, 11, 135, 35);
		panel_1.add(lblTongtien);

		lblTongtien_1 = new CustomJLabel(df.format(hd.tinhTongtien()));
		lblTongtien_1.setHorizontalAlignment(SwingConstants.RIGHT);
		lblTongtien_1.setBounds(782, 11, 240, 35);
		panel_1.add(lblTongtien_1);

		CustomJButton btnIn = new CustomJButton("In hóa đơn");
		btnIn.setMnemonic('i');
		btnIn.setBounds(174, 11, 135, 35);
		btnIn.setIcon(new ImageIcon("img\\printer.png"));
		btnIn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		panel_1.add(btnIn);
		btnIn.addActionListener(e -> inHoaDon());
	}

	private void inHoaDon() {
		new GenerateBillPdf(hd.getMahoadon());
		String maKH = hd.getKhachhang().getMakhachhang();
		if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
			try {
				Desktop.getDesktop().browse(new URI("t:hoadon/" + hd.getMahoadon() + "_" + maKH + ".pdf"));
			} catch (IOException | URISyntaxException e) {
			}
		}
	}
}