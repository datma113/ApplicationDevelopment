package panel;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import custom.MyJButton;
import custom.MyJLabel;
import custom.MyJScrollPane;
import custom.MyTableCellRender;
import dao.DaoHoadon;
import entity.Chitiethoadon;
import entity.Hoadon;
import entity.Khachhang;
import entity.Nhanvien;
import generate_bill.GenerateBillPdf;
import model.ModelChitiethoadon;

public class PanelXemtruochoadon extends JPanel {

	private static final long serialVersionUID = 1L;
	private static final int vertical_always = JScrollPane.VERTICAL_SCROLLBAR_ALWAYS;
	private static final int horizontal_needed = JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS;

	private JPanel panel;
	private JPanel panel_1;
	private JTable tableChitiet;
	private MyJLabel lblTongtien_1;
	private MyJLabel lblKhTra_1;
	private MyJLabel lblTralai_1;
	private DaoHoadon daohd;
	private boolean susscess = false;

	public PanelXemtruochoadon(Hoadon hd, Khachhang kh, Nhanvien nv, double tiẹnkhachdua) {

		setSize(1090, 800);
		setLayout(null);

		panel = new JPanel(null);
		panel.setBounds(10, 11, 1053, 135);
		panel.setBorder(BorderFactory.createEtchedBorder());
		add(panel);

		panel_1 = new JPanel(null);
		panel_1.setBounds(10, 633, 1053, 115);
		panel_1.setBorder(BorderFactory.createEtchedBorder());
		add(panel_1);

		List<Chitiethoadon> dscthd = hd.getChitiethoadons();
		DecimalFormat df = new DecimalFormat("###,###,###.#");
		ModelChitiethoadon model = new ModelChitiethoadon(dscthd);

		tableChitiet = new JTable(model);
		tableChitiet.setRowHeight(35);
		tableChitiet.setAutoCreateRowSorter(true);
		tableChitiet.getTableHeader().setBackground(new Color(255, 208, 120));
		tableChitiet.getTableHeader().setPreferredSize(new Dimension(tableChitiet.getTableHeader().getWidth(), 30));
		Font f3 = tableChitiet.getTableHeader().getFont();
		tableChitiet.getTableHeader().setFont(new Font(f3.getName(), Font.BOLD, f3.getSize() + 2));

		MyTableCellRender renderTable = new MyTableCellRender();
		tableChitiet.setDefaultRenderer(String.class, renderTable);
		tableChitiet.setDefaultRenderer(Double.class, renderTable);
		tableChitiet.setDefaultRenderer(Integer.class, renderTable);

		MyJScrollPane scrollChiTiet = new MyJScrollPane(tableChitiet, vertical_always, horizontal_needed);
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

		MyJLabel lblNewLabel = new MyJLabel("Mã hóa đơn: " + hd.getMahoadon());
		lblNewLabel.setBounds(10, 11, 245, 25);
		panel.add(lblNewLabel);

		MyJLabel lblNhanvien = new MyJLabel("Nhân viên lập: " + nv.getHodem() + " " + nv.getTen());
		lblNhanvien.setBounds(626, 11, 417, 25);
		panel.add(lblNhanvien);

		MyJLabel lblMakh = new MyJLabel("Mã KH: " + kh.getMakhachhang());
		lblMakh.setBounds(10, 47, 245, 25);
		panel.add(lblMakh);

		MyJLabel lblNgay = new MyJLabel("Ngày lập: " + LocalDate.now());
		lblNgay.setBounds(265, 11, 351, 25);
		panel.add(lblNgay);

		MyJLabel lblHotenKhach = new MyJLabel("Họ tên KH: " + kh.getHoten());
		lblHotenKhach.setBounds(265, 47, 351, 25);
		panel.add(lblHotenKhach);

		MyJLabel lblSdt = new MyJLabel("SĐT: " + kh.getSodienthoai());
		lblSdt.setBounds(626, 47, 417, 25);
		panel.add(lblSdt);

		MyJLabel lblDiachi = new MyJLabel("Địa chỉ: " + kh.getDiachi());
		lblDiachi.setBounds(10, 83, 993, 25);
		panel.add(lblDiachi);

		MyJLabel lblTongtien = new MyJLabel("Tổng tiền: ");
		lblTongtien.setBounds(670, 11, 127, 25);
		panel_1.add(lblTongtien);

		MyJLabel lblKhTra = new MyJLabel("Khách đưa");
		lblKhTra.setBounds(670, 47, 127, 25);
		panel_1.add(lblKhTra);

		MyJLabel lblTralai = new MyJLabel("Trả lại");
		lblTralai.setBounds(670, 79, 127, 25);
		panel_1.add(lblTralai);

		MyJButton btnTaoHD = new MyJButton("Xác nhận lập hóa đơn");
		btnTaoHD.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnTaoHD.setIcon(new ImageIcon("img/save.png"));
		btnTaoHD.addActionListener(e -> {
			daohd = new DaoHoadon();
			if (daohd.themHD(hd)) {
				JOptionPane.showMessageDialog(this, "Thêm hóa đơn thành công.", "Thêm hóa đơn",
						JOptionPane.INFORMATION_MESSAGE);
				int in = JOptionPane.showConfirmDialog(this, "Bạn có muốn in hóa đơn không?", "In hóa đơn",
						JOptionPane.YES_NO_OPTION);
				if (in == JOptionPane.YES_OPTION) {
					String maKH = kh.getMakhachhang();
					String maHD = hd.getMahoadon();
					new GenerateBillPdf(maHD);
					if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
						try {
							Path currentRelativePath = Paths.get("");
							String path = currentRelativePath.toAbsolutePath().toString() + "\\hoadon\\";
							path = path.replaceAll("\\\\", "/");
							Desktop.getDesktop().browse(new URI(path + maHD + "_" + maKH + ".pdf"));
						} catch (IOException e1) {
						} catch (URISyntaxException e1) {
						}
					}
				}
				susscess = true;
				JDialog frame = (JDialog) SwingUtilities.getWindowAncestor(this);
				frame.dispose();
			} else {
				JOptionPane.showMessageDialog(null, "Thêm hóa đơn không thành công.");
			}
		});
		btnTaoHD.setMnemonic('t');
		btnTaoHD.setBounds(67, 41, 190, 35);
		panel_1.add(btnTaoHD);

		lblTongtien_1 = new MyJLabel(df.format(hd.tinhTongtien()));
		lblTongtien_1.setHorizontalAlignment(SwingConstants.RIGHT);
		lblTongtien_1.setBounds(780, 11, 252, 25);
		panel_1.add(lblTongtien_1);

		lblKhTra_1 = new MyJLabel(df.format(tiẹnkhachdua));
		lblKhTra_1.setHorizontalAlignment(SwingConstants.RIGHT);
		lblKhTra_1.setBounds(780, 47, 252, 25);
		panel_1.add(lblKhTra_1);

		lblTralai_1 = new MyJLabel(df.format(tiẹnkhachdua - hd.tinhTongtien()));
		lblTralai_1.setHorizontalAlignment(SwingConstants.RIGHT);
		lblTralai_1.setBounds(780, 79, 252, 25);
		panel_1.add(lblTralai_1);

		MyJButton btnQuaylai = new MyJButton("Quay lại");
		btnQuaylai.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnQuaylai.setIcon(new ImageIcon("img/back.png"));
		btnQuaylai.addActionListener(e -> {
			JDialog frame = (JDialog) SwingUtilities.getWindowAncestor(this);
			frame.dispose();
		});
		btnQuaylai.setMnemonic('q');
		btnQuaylai.setBounds(359, 41, 190, 35);
		panel_1.add(btnQuaylai);

	}

	public boolean isSusscess() {
		return susscess;
	}

}