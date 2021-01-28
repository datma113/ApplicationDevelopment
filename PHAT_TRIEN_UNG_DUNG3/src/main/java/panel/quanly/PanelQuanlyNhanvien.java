package panel.quanly;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import com.placeholder.PlaceHolder;
import com.toedter.calendar.JDateChooser;

import custom.MyJButton;
import custom.MyJLabel;
import custom.MyJScrollPane;
import custom.MyJTextField;
import custom.MyTableCellRender;
import dao.DaoNhanvien;
import entity.Khachhang;
import entity.Nhanvien;
import model.ModelNhanvien;
import phantrang.DataProvider;
import phantrang.Decorator;

public class PanelQuanlyNhanvien extends JPanel implements ActionListener, MouseListener, ItemListener {

	private static final long serialVersionUID = 1L;
	private static final int vertical_always = JScrollPane.VERTICAL_SCROLLBAR_ALWAYS;
	private static final int horizontal_needed = JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED;

	private JTable table;
	private DaoNhanvien dao;
	private ModelNhanvien model;

	private MyJTextField tfManv;
	private MyJTextField tfHo;
	private MyJTextField tfDiaChi;
	private MyJTextField tfTen;
	private MyJTextField tfSDT;
	private MyJTextField tfTimMa;
	private MyJTextField tfSocmnd;

	private MyJButton btnTim;
	private MyJButton btnThem;
	private MyJButton btnXoa;
	private MyJButton btnLuu;
	private MyJButton btnQLTK;
	private MyJButton btnXoatrang;

	private JCheckBox chkNam;
	private JCheckBox chkQuanly;

	private JDateChooser dateNgaySinh;
	private MyJLabel lblPicture;
	private MyJLabel lblNewLabel;
	private Decorator<Nhanvien> decorator;
	private JPanel panel_timkiem;
	private JPanel panel_phantrang;
	private JPanel pnlMain;

	public PanelQuanlyNhanvien() {

		setSize(1680, 1021);
		setLayout(null);

		setBackground(Color.WHITE);

		/**
		 * panel
		 */
		pnlMain = new JPanel(null);
		pnlMain.setLocation(10, 29);
		pnlMain.setSize(1640, 903);
		pnlMain.setBorder(BorderFactory.createEtchedBorder());

		panel_timkiem = new JPanel(null);

		JPanel pnlButton = new JPanel(null);
		pnlButton.setBounds(10, 943, 1640, 55);
		pnlButton.setBorder(BorderFactory.createEtchedBorder());
		add(pnlButton);

		/**
		 * label
		 */
		lblPicture = new MyJLabel("");
		lblPicture.setIcon(new ImageIcon("img\\nam.png"));
		lblPicture.setBounds(30, 15, 100, 100);
		pnlMain.add(lblPicture);

		MyJLabel lblTitle = new MyJLabel("QUẢN LÝ NHÂN VIÊN");
		lblTitle.setBounds(10, 0, 1638, 30);
		lblTitle.setForeground(Color.RED);
		lblTitle.setFont(new Font("Serif", Font.BOLD, 20));
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		add(lblTitle);

		MyJLabel lblMa = new MyJLabel("Mã NV (*)");
		lblMa.setBounds(168, 20, 142, 25);
		pnlMain.add(lblMa);

		MyJLabel lblHo = new MyJLabel("Họ (*)");
		lblHo.setBounds(168, 69, 142, 25);
		pnlMain.add(lblHo);

		MyJLabel lblTen = new MyJLabel("Tên (*)");
		lblTen.setBounds(648, 69, 145, 25);
		pnlMain.add(lblTen);

		MyJLabel lblNgaySinh = new MyJLabel("Ngày sinh");
		lblNgaySinh.setBounds(1137, 68, 125, 25);
		pnlMain.add(lblNgaySinh);

		MyJLabel lblDiaChi = new MyJLabel("Địa chỉ");
		lblDiaChi.setBounds(168, 118, 142, 25);
		pnlMain.add(lblDiaChi);

		MyJLabel lblSdt = new MyJLabel("SĐT");
		lblSdt.setBounds(648, 20, 145, 25);
		pnlMain.add(lblSdt);

		MyJLabel lblCmnd = new MyJLabel("Số CMND");
		lblCmnd.setBounds(1137, 19, 145, 25);
		pnlMain.add(lblCmnd);

		/**
		 * textfield
		 */
		tfManv = new MyJTextField(20);
		tfManv.setBounds(270, 20, 300, 25);
		tfManv.setEditable(false);
		pnlMain.add(tfManv);

		tfHo = new MyJTextField(20);
		tfHo.setBounds(270, 69, 300, 25);
		pnlMain.add(tfHo);

		tfDiaChi = new MyJTextField(20);
		tfDiaChi.setBounds(270, 118, 788, 25);
		pnlMain.add(tfDiaChi);

		tfTen = new MyJTextField(20);
		tfTen.setBounds(758, 69, 300, 25);
		pnlMain.add(tfTen);

		tfSDT = new MyJTextField(20);
		tfSDT.setBounds(758, 20, 300, 25);
		pnlMain.add(tfSDT);

		tfSocmnd = new MyJTextField(20);
		tfSocmnd.setBounds(1272, 19, 300, 25);
		pnlMain.add(tfSocmnd);

		dateNgaySinh = new JDateChooser(new Date());
		dateNgaySinh.setBounds(1272, 68, 200, 25);
		pnlMain.add(dateNgaySinh);

		chkQuanly = new JCheckBox("Quản Lý ?");
		chkQuanly.setFont(new Font("Tahoma", Font.BOLD, 15));
		chkQuanly.setSelected(false);
		chkQuanly.setForeground(Color.RED);
		chkQuanly.setBounds(1268, 118, 150, 25);
		pnlMain.add(chkQuanly);

		chkNam = new JCheckBox("Nam");
		chkNam.setFont(new Font("Tahoma", Font.BOLD, 15));
		chkNam.setForeground(Color.RED);
		chkNam.setBounds(1133, 117, 125, 25);
		pnlMain.add(chkNam);

		/**
		 * button
		 */

		btnXoa = new MyJButton("Xóa");
		btnXoa.setMnemonic('x');
		btnXoa.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnXoa.setBounds(918, 11, 170, 35);
		btnXoa.setIcon(new ImageIcon("img\\delete.png"));
		btnXoa.setEnabled(false);
//		pnlButton.add(btnXoa);

		btnLuu = new MyJButton("Lưu");
		btnLuu.setMnemonic('l');
		btnLuu.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnLuu.setBounds(1320, 9, 150, 35);
		btnLuu.setIcon(new ImageIcon("img\\save.png"));
		pnlButton.add(btnLuu);

		btnQLTK = new MyJButton("QL Tài Khoản");
		btnQLTK.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnQLTK.setIcon(new ImageIcon("img/secure.png"));
		btnQLTK.setBounds(980, 9, 170, 35);
		pnlButton.add(btnQLTK);
		btnQLTK.setMnemonic('a');
		btnQLTK.addActionListener(this);

		btnThem = new MyJButton("Thêm");
		btnThem.setMnemonic('t');
		btnThem.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnThem.setBounds(1480, 9, 150, 35);
		btnThem.setIcon(new ImageIcon("img\\them.png"));
		pnlButton.add(btnThem);

		btnXoatrang = new MyJButton("Xóa trắng");
		btnXoatrang.setMnemonic('x');
		btnXoatrang.setBounds(1160, 9, 150, 35);
		btnXoatrang.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnXoatrang.setIcon(new ImageIcon("img\\refresh.png"));
		pnlButton.add(btnXoatrang);

		lblNewLabel = new MyJLabel("Tìm theo từ khóa");
		lblNewLabel.setBounds(10, 9, 188, 37);
		pnlButton.add(lblNewLabel);

		tfTimMa = new MyJTextField("");
		tfTimMa.setBounds(170, 15, 580, 25);
		pnlButton.add(tfTimMa);
		btnTim = new MyJButton("Tìm");
		btnTim.setBounds(820, 9, 150, 35);
		pnlButton.add(btnTim);
		btnTim.setMnemonic('m');
		btnTim.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnTim.setIcon(new ImageIcon("img\\find.png"));
		btnTim.addActionListener(this);
		tfTimMa.addActionListener(this);

		dao = new DaoNhanvien();
		model = new ModelNhanvien();
		table = new JTable(model);
		table.setRowHeight(35);
		table.setAutoCreateRowSorter(true);
		table.getTableHeader().setBackground(new Color(255, 208, 120));
		table.getTableHeader().setPreferredSize(new Dimension(table.getTableHeader().getWidth(), 30));
		Font f3 = table.getTableHeader().getFont();
		table.getTableHeader().setFont(new Font(f3.getName(), Font.BOLD, f3.getSize() + 2));

		MyTableCellRender renderTable = new MyTableCellRender();
		table.setDefaultRenderer(Object.class, renderTable);

		MyJScrollPane scrollNV = new MyJScrollPane(table, vertical_always, horizontal_needed);
		scrollNV.setBounds(10, 168, 1620, 663);
		pnlMain.add(scrollNV);
		add(pnlMain);

		btnThem.addActionListener(this);
		btnXoatrang.addActionListener(this);
		btnXoa.addActionListener(this);
		btnLuu.addActionListener(this);

		tfHo.addActionListener(this);
		tfTen.addActionListener(this);
		tfSDT.addActionListener(this);
		tfDiaChi.addActionListener(this);

		chkNam.addItemListener(this);

		table.addMouseListener(this);

		DataProvider<Nhanvien> dataProvider = new DataProvider<Nhanvien>() {

			@Override
			public int getTotalRowCount() {
				return dao.getSize();
			}

			@Override
			public void addDataToTable(int startIndex, int endIndex) {
				List<Nhanvien> list = dao.getDsNv(startIndex, endIndex);
				model.setDsnv(list);
				model.fireTableDataChanged();
			}
		};
		decorator = Decorator.decorate(dataProvider, new int[] { 10, 25, 50, 75, 100 }, 25);
		panel_phantrang = decorator.getContentPanel();
//		panel_phantrang = new JPanel();
		panel_phantrang.setBounds(10, 842, 1620, 50);
		panel_phantrang.setBorder(BorderFactory.createEtchedBorder());
		pnlMain.add(panel_phantrang);

		xoaTrang();
		resizeColumnsNV();
	}

	/**
	 * --------------------------------------------------------
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource();
		if (o.equals(btnThem)) {
			them();
		} else if (o.equals(btnLuu)) {
			luu();
		} else if (o.equals(btnXoa)) {
			xoa();
		} else if (o.equals(btnTim)) {
			tim();
		} else if (o.equals(btnXoatrang)) {
			xoaTrang();
		} else if (o.equals(btnQLTK)) {
			if (table.getSelectedRow() > 0) {
				String manv = table.getValueAt(table.getSelectedRow(), 0).toString();
				Nhanvien nhanvien = dao.getNhanVien(manv);
				final JDialog frame = new JDialog((JFrame) SwingUtilities.getWindowAncestor(this), "Quản lý tài khoản",
						true);
				try {
					frame.setIconImage(ImageIO.read(new File("img/code1.png")));
				} catch (IOException exception) {
				}
				JPanel panel = new PanelQuanlyTaikhoan(nhanvien);
				frame.getContentPane().add(panel);
				frame.setSize(panel.getWidth(), panel.getHeight());
				frame.setLocationRelativeTo((JFrame) SwingUtilities.getWindowAncestor(this));
				frame.setVisible(true);
			} else {
				final JDialog frame = new JDialog((JFrame) SwingUtilities.getWindowAncestor(this), "Quản lý tài khoản",
						true);
				try {
					frame.setIconImage(ImageIO.read(new File("img/code1.png")));
				} catch (IOException exception) {
				}
				JPanel panel = new PanelQuanlyTaikhoan();
				frame.getContentPane().add(panel);
				frame.setSize(panel.getWidth(), panel.getHeight());
				frame.setLocationRelativeTo((JFrame) SwingUtilities.getWindowAncestor(this));
				frame.setVisible(true);
			}
		} else if (o.equals(tfHo)) {
			tfTen.requestFocus();
		} else if (o.equals(tfTen)) {
			tfSDT.requestFocus();
		} else if (o.equals(tfSDT)) {
			tfDiaChi.requestFocus();
		} else if (o.equals(tfDiaChi)) {
			btnThem.doClick();
		} else if (o.equals(tfTimMa)) {
			btnTim.doClick();
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		Object o = e.getSource();
		if (o.equals(table)) {
			fillTextField();
		}
	}

	/**
	 * lay du lieu tu table dua len textfield
	 */
	private void fillTextField() {
		int index = table.getSelectedRow();
		if (index >= 0 && index < table.getRowCount()) {
			Nhanvien nv = dao.getNhanVien(table.getValueAt(index, 0).toString());
			if (nv != null) {
				tfManv.setText(nv.getManhanvien());
				tfHo.setText(nv.getHodem());
				tfTen.setText(nv.getTen());
				if (nv.getChucvu().equalsIgnoreCase("Quản Lý"))
					chkQuanly.setSelected(true);
				else
					chkQuanly.setSelected(false);
				tfDiaChi.setText(nv.getDiachi());
				tfSDT.setText(nv.getSodienthoai());
				dateNgaySinh.setDate(nv.getNgaysinh());
				chkNam.setSelected(nv.isGioitinh());
				tfSocmnd.setText(nv.getSocmnd());
			}
		}
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		Object o = e.getSource();
		if (o.equals(chkNam)) {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				lblPicture.setIcon(new ImageIcon("img\\nam.png"));
			} else {
				lblPicture.setIcon(new ImageIcon("img\\nu.png"));
			}
		}
	}

	private void resizeColumnsNV() {
		table.getColumnModel().getColumn(0).setMinWidth(150);
		table.getColumnModel().getColumn(1).setMinWidth(159);
		table.getColumnModel().getColumn(2).setMinWidth(150);
		table.getColumnModel().getColumn(3).setMinWidth(150);
		table.getColumnModel().getColumn(4).setMinWidth(150);
		table.getColumnModel().getColumn(5).setMinWidth(150);
		table.getColumnModel().getColumn(6).setMinWidth(400);
		table.getColumnModel().getColumn(7).setMinWidth(150);
		table.getColumnModel().getColumn(8).setMinWidth(150);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
	}

	/*
	 * private void count() { DecimalFormat df = new
	 * DecimalFormat("###,###,###.###"); double sum = 0; for (int i = 0; i <
	 * table.getRowCount(); i++) sum += (Double) table.getValueAt(i, 7);
	 * lblSumTongTien.setText(df.format(sum)); }
	 */

	/**
	 * xoa trang
	 */
	private void xoaTrang() {
		tfManv.setText(dao.getNextMaNV());
		tfHo.requestFocus();
		tfHo.setText("");
		tfTen.setText("");
		tfSDT.setText("");
		tfDiaChi.setText("");
		chkNam.setSelected(true);
		chkQuanly.setSelected(false);
		dateNgaySinh.setDate(new Date());
		decorator.reload();
		pnlMain.remove(panel_timkiem);
		pnlMain.add(panel_phantrang);
		pnlMain.repaint();
		pnlMain.revalidate();
		table.clearSelection();
		tfTimMa.setText("");
		tfSocmnd.setText("");
		new PlaceHolder(tfTimMa, new Color(192, 192, 192), Color.BLACK,
				"Nhập mã, họ tên, số điện thoại hoặc số CMND của nhân viên", false, this.getFont().toString(), 19);
	}

	/**
	 * them 1 nhan vien
	 */
	private void them() {
		if (table.getSelectedRow() != -1 || tfManv.getText().trim().equals("")) {
			xoaTrang();
		} else if (validInputTextField()) {
			Nhanvien nv = getDataFromTextField();
			if (nv != null) {
				if (dao.themNhanVien(nv)) {
					xoaTrang();
					JOptionPane.showMessageDialog(this, "Thêm thành công", "Thêm nhân viên",
							JOptionPane.INFORMATION_MESSAGE);
				} else
					JOptionPane.showMessageDialog(this, "Thêm không thành công", "Thêm nhân viên",
							JOptionPane.ERROR_MESSAGE);
			} else {
				JOptionPane.showMessageDialog(this, "Thêm không thành công", "Thêm nhân viên",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	/**
	 * lay thong tin nhan vien tu jtextfield
	 * 
	 * @return
	 */
	private Nhanvien getDataFromTextField() {
		Nhanvien nv = new Nhanvien();
		if (validInputTextField()) {
			nv.setManhanvien(tfManv.getText().trim());
			nv.setHodem(tfHo.getText().trim());
			nv.setTen(tfTen.getText().trim());
			nv.setNgaysinh(new java.sql.Date(dateNgaySinh.getDate().getTime()));
			nv.setGioitinh(chkNam.isSelected());
			nv.setDiachi(tfDiaChi.getText().trim());
			nv.setSodienthoai(tfSDT.getText().trim());
			nv.setChucvu(chkQuanly.isSelected() ? "Quản Lý" : "Nhân Viên");
			nv.setSocmnd(tfSocmnd.getText().trim());
			return nv;
		}
		return null;
	}

	/**
	 * xoa 1 nhan vien
	 */
	private void xoa() {
		/*
		 * int index = table.getSelectedRow(); if (index >= 0 && index <
		 * table.getRowCount()) { int xoa = JOptionPane.showConfirmDialog(this,
		 * "Bạn có muốn xóa nhân viên này không?", "Xóa", JOptionPane.WARNING_MESSAGE);
		 * if (xoa == JOptionPane.YES_OPTION) { Nhanvien nhanVien = new
		 * Nhanvien(table.getValueAt(index, 0).toString()); if
		 * (dao.xoaNhanVien(nhanVien)) { xoaTrang(); JOptionPane.showMessageDialog(this,
		 * "Xóa thành công"); } else JOptionPane.showMessageDialog(this,
		 * "Xóa không thành công\nLỗi: "); } } else {
		 * JOptionPane.showMessageDialog(this, "Chọn một nhân viên để xóa!", "Xóa",
		 * JOptionPane.INFORMATION_MESSAGE); }
		 */
	}

	/**
	 * luu thong tin nhan vien sau khi sua
	 */
	private void luu() {
		int index = table.getSelectedRow();
		if (index >= 0 && index < table.getRowCount()) {
			Nhanvien nvTemp = getDataFromTextField();
			if (nvTemp != null) {
				if (dao.capNhatNhanVien(nvTemp)) {
					xoaTrang();
					JOptionPane.showMessageDialog(this, "Cập nhật thành công", "Cập nhật nhân viên",
							JOptionPane.INFORMATION_MESSAGE);
				} else
					JOptionPane.showMessageDialog(this, "Cập nhật không thành công", "Cập nhật nhân viên",
							JOptionPane.ERROR_MESSAGE);
			} else {
				JOptionPane.showMessageDialog(this, "Cập nhật không thành công", "Cập nhật nhân viên",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	/**
	 * tim kiem
	 */
	private void tim() {
		String keyword = tfTimMa.getText().trim().toUpperCase();
		DataProvider<Khachhang> dataTimkiem = new DataProvider<Khachhang>() {

			@Override
			public int getTotalRowCount() {
				return dao.getSizeTimkiem(keyword);
			}

			@Override
			public void addDataToTable(int startIndex, int endIndex) {
				List<Nhanvien> list = dao.timkiem(startIndex, endIndex, keyword);
				model.setDsnv(list);
				model.fireTableDataChanged();
			}
		};
		pnlMain.remove(panel_timkiem);
		Decorator<Khachhang> decorator2 = Decorator.decorate(dataTimkiem, new int[] { 10, 25, 50, 75, 100 }, 25);
		panel_timkiem = decorator2.getContentPanel();
		panel_timkiem.setBounds(10, 842, 1620, 50);
		panel_timkiem.setBorder(BorderFactory.createEtchedBorder());
		pnlMain.remove(panel_phantrang);
		pnlMain.add(panel_timkiem);
		pnlMain.repaint();
		pnlMain.revalidate();
		if (model.getRowCount() == 0)
			JOptionPane.showMessageDialog(this, "Không tìm thấy nhân viên nào", "Tìm nhân viên",
					JOptionPane.INFORMATION_MESSAGE);
	}

	/**
	 * KIEM TRA TINH HOP LE CUA DU LIEU DAU VAO
	 * 
	 * @return
	 */
	private boolean validInputTextField() {
		if (!tfHo.getText().trim().matches("[\\p{L}\\s]+")) {
			JOptionPane.showMessageDialog(this, "Họ chỉ gồm chữ, không chứa số hay kí tự đặc biệt", "Thông báo",
					JOptionPane.ERROR_MESSAGE);
			tfHo.requestFocus();
			return false;
		}
		if (!tfTen.getText().trim().matches("[\\p{L}\\s]+")) {
			JOptionPane.showMessageDialog(this, "Tên chỉ gồm chữ, không chứa số hay kí tự đặc biệt", "Thông báo",
					JOptionPane.ERROR_MESSAGE);
			tfTen.requestFocus();
			return false;
		}
		if (!tfSDT.getText().trim().matches("[0-9()+-]+")) {
			JOptionPane.showMessageDialog(this, "SĐT phải là số, có thể bao gồm các kí tự ( ) + -", "Thông báo",
					JOptionPane.ERROR_MESSAGE);
			tfSDT.requestFocus();
			return false;
		}
		if (!tfDiaChi.getText().trim().matches("[\\p{L}\\s0-9()\\/_\\\\.,\\+-]+")) {
			JOptionPane.showMessageDialog(this, "Địa chỉ chỉ bao gồm chữ, số, và các kí tự ( ) \\ / _ , . + -",
					"Thông báo", JOptionPane.ERROR_MESSAGE);
			tfDiaChi.requestFocus();
			return false;
		}
		if (!(tfSocmnd.getText().trim().matches("[0-9]{9}") || tfSocmnd.getText().trim().matches("[0-9]{12}"))) {
			JOptionPane.showMessageDialog(this, "Số CMND phải là 9 hoặc 12 chữ số", "Thông báo",
					JOptionPane.ERROR_MESSAGE);
			tfSocmnd.requestFocus();
			return false;
		}
		if (dateNgaySinh.getDate().getTime() > System.currentTimeMillis()) {
			JOptionPane.showMessageDialog(this, "Ngày sinh không hợp lệ", "Thông báo", JOptionPane.ERROR_MESSAGE);
			return false;
		}

		if ((LocalDateTime.now().getYear() - (1900 + dateNgaySinh.getDate().getYear())) < 16) {
			JOptionPane.showMessageDialog(this, "Không đủ tuổi để đăng ký(>=16)", "Thông báo",
					JOptionPane.ERROR_MESSAGE);
			return false;
		}

		if (((LocalDateTime.now().getYear() - (1900 + dateNgaySinh.getDate().getYear())) == 16)
				&& ((1 + dateNgaySinh.getDate().getMonth()) > LocalDateTime.now().getMonthValue())) {

			JOptionPane.showMessageDialog(this, "Không đủ tuổi để đăng ký(>=16)", "Thông báo",
					JOptionPane.ERROR_MESSAGE);
			return false;
		}
		if (((LocalDateTime.now().getYear() - (1900 + dateNgaySinh.getDate().getYear())) == 16)
				&& ((1 + dateNgaySinh.getDate().getMonth()) == LocalDateTime.now().getMonthValue())
				&& (dateNgaySinh.getDate().getDate()) > LocalDateTime.now().getDayOfMonth()) {

			JOptionPane.showMessageDialog(this, "Không đủ tuổi để đăng ký(>=16)", "Thông báo",
					JOptionPane.ERROR_MESSAGE);
			return false;
		}
		return true;
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}
}