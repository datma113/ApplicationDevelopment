package panel.quanly;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import com.placeholder.PlaceHolder;

import custom.MyJButton;
import custom.MyJLabel;
import custom.MyJScrollPane;
import custom.MyJTextField;
import custom.MyTableCellRender;
import dao.DaoKhachhang;
import entity.Khachhang;
import frame.FrameTrangchu;
import model.ModelKhachhang;
import phantrang.DataProvider;
import phantrang.Decorator;

public class PanelQuanlyKhachhang extends JPanel implements ActionListener, MouseListener {

	private static final long serialVersionUID = 1L;
	private static final int vertical_always = JScrollPane.VERTICAL_SCROLLBAR_ALWAYS;
	private static final int horizoltal_always = JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS;
	private static final int error = JOptionPane.ERROR_MESSAGE;

	private JTable table;
	private DaoKhachhang dao;
	private ModelKhachhang model;

	private MyJTextField tfMakh;
	private MyJTextField tfHo;
	private MyJTextField tfDiaChi;
	private MyJTextField tfSDT;
	private MyJTextField tfTimMa;

	private MyJButton btnTim;
	private MyJButton btnThem;
	private MyJButton btnXoa;
	private MyJButton btnLuu;
	private MyJButton btnXoaTrang;
	private MyJButton btnLaphoadon;
	private Decorator<Khachhang> decorator;

	private JPanel panelMain;
	private JPanel panel_timkiem;
	private JPanel panel_phantrang;

	public PanelQuanlyKhachhang() {
		setSize(1680, 1021);
		setLayout(null);

		setBackground(Color.WHITE);

		/**
		 * dao model table
		 */
		dao = new DaoKhachhang();
		model = new ModelKhachhang();
		table = new JTable(model);
		table.setRowHeight(35);
		table.setAutoCreateRowSorter(true);
		table.getTableHeader().setBackground(new Color(255, 208, 120));
		table.getTableHeader().setPreferredSize(new Dimension(table.getTableHeader().getWidth(), 30));
		Font f3 = table.getTableHeader().getFont();
		table.getTableHeader().setFont(new Font(f3.getName(), Font.BOLD, f3.getSize() + 2));

		MyTableCellRender renderTable = new MyTableCellRender();
		table.setDefaultRenderer(Object.class, renderTable);

		/**
		 * panel
		 */
		panel_timkiem = new JPanel(null);

		panelMain = new JPanel(null);
		panelMain.setLocation(10, 29);
		panelMain.setSize(1640, 903);
		panelMain.setBorder(BorderFactory.createEtchedBorder());

		JPanel pnlButton = new JPanel(null);
		add(pnlButton);
		pnlButton.setBounds(10, 943, 1640, 55);
		pnlButton.setBorder(BorderFactory.createEtchedBorder());

		/**
		 * label
		 */
		MyJLabel lblTitle = new MyJLabel("QUẢN LÝ KHÁCH HÀNG");
		lblTitle.setForeground(Color.RED);
		lblTitle.setFont(new Font("Serif", Font.BOLD, 20));
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitle.setBounds(10, 0, 1638, 30);
		add(lblTitle);

		MyJLabel lblMa = new MyJLabel("Mã KH (*)");
		lblMa.setBounds(10, 15, 142, 25);
		panelMain.add(lblMa);

		MyJLabel lblHo = new MyJLabel("Họ Tên (*)");
		lblHo.setBounds(460, 15, 142, 25);
		panelMain.add(lblHo);

		MyJLabel lblDiaChi = new MyJLabel("Địa chỉ");
		lblDiaChi.setBounds(10, 56, 142, 25);
		panelMain.add(lblDiaChi);

		MyJLabel lblSdt = new MyJLabel("SĐT");
		lblSdt.setBounds(926, 56, 73, 25);
		panelMain.add(lblSdt);

		/**
		 * textfield
		 */
		tfMakh = new MyJTextField(20);
		tfMakh.setBounds(105, 15, 300, 25);
		tfMakh.setEditable(false);
		panelMain.add(tfMakh);

		tfHo = new MyJTextField(20);
		tfHo.setBounds(572, 17, 300, 25);
		panelMain.add(tfHo);

		tfDiaChi = new MyJTextField(20);
		tfDiaChi.setBounds(105, 56, 767, 25);
		panelMain.add(tfDiaChi);

		tfSDT = new MyJTextField(20);
		tfSDT.setBounds(1009, 54, 300, 25);
		panelMain.add(tfSDT);

		/**
		 * button
		 */

		btnLaphoadon = new MyJButton("Lập hóa đơn");
		btnLaphoadon.setMnemonic('h');
		btnLaphoadon.setBounds(980, 9, 170, 35);
		btnLaphoadon.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnLaphoadon.setIcon(new ImageIcon("img\\bill.png"));
		btnLaphoadon.setToolTipText("Lập hóa đơn cho khách hàng này");
		pnlButton.add(btnLaphoadon);

		btnThem = new MyJButton("Thêm");
		btnThem.setMnemonic('t');
		btnThem.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnThem.setBounds(1480, 9, 150, 35);
		btnThem.setIcon(new ImageIcon("img\\them.png"));
		pnlButton.add(btnThem);

		btnXoa = new MyJButton("Xóa");
		btnXoa.setMnemonic('x');
		btnXoa.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnXoa.setBounds(557, 11, 150, 35);
		btnXoa.setIcon(new ImageIcon("img\\delete.png"));
		btnXoa.setEnabled(false);
//		pnlButton.add(btnXoa);

		btnLuu = new MyJButton("Lưu");
		btnLuu.setMnemonic('l');
		btnLuu.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnLuu.setBounds(1320, 9, 150, 35);
		btnLuu.setIcon(new ImageIcon("img\\save.png"));
		pnlButton.add(btnLuu);

		btnXoaTrang = new MyJButton("Xóa trắng");
		btnXoaTrang.setMnemonic('x');
		btnXoaTrang.setBounds(1160, 9, 150, 35);
		btnXoaTrang.setIcon(new ImageIcon("img\\refresh.png"));
		btnXoaTrang.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		pnlButton.add(btnXoaTrang);

		MyJLabel lblTimMa = new MyJLabel("Tìm theo từ khóa");
		lblTimMa.setBounds(10, 11, 195, 33);
		pnlButton.add(lblTimMa);

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

		/**
		 * jscrollpane
		 */
		MyJScrollPane scrollKH = new MyJScrollPane(table, vertical_always, horizoltal_always);
		scrollKH.setBounds(10, 98, 1620, 733);
		panelMain.add(scrollKH);
		add(panelMain);

		/**
		 * su kien
		 */
		btnXoaTrang.addActionListener(this);
		btnThem.addActionListener(this);
		btnXoa.addActionListener(this);
		btnLuu.addActionListener(this);
		btnLaphoadon.addActionListener(this);

		tfHo.addActionListener(this);
		tfSDT.addActionListener(this);
		tfDiaChi.addActionListener(this);

		table.addMouseListener(this);

		DataProvider<Khachhang> dataProvider = new DataProvider<Khachhang>() {
			@Override
			public int getTotalRowCount() {
				return dao.getSize();
			}

			@Override
			public void addDataToTable(int startIndex, int endIndex) {
				List<Khachhang> list = dao.getDsKh(startIndex, endIndex);
				model.setDskh(list);
				model.fireTableDataChanged();
			}
		};
		decorator = Decorator.decorate(dataProvider, new int[] { 10, 25, 50, 75, 100 }, 25);
		panel_phantrang = decorator.getContentPanel();
//		panel_phantrang = new JPanel();
		panel_phantrang.setBounds(10, 842, 1620, 50);
		panel_phantrang.setBorder(BorderFactory.createEtchedBorder());
		panelMain.add(panel_phantrang);

//		resizeColumnsKH();
		xoaTrang();
	}

	/***
	 * ---------------------------------------------------------------------------------
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource();
		if (o.equals(btnThem))
			them();
		else if (o.equals(btnLuu))
			luu();
		else if (o.equals(btnXoa))
			xoa();
		else if (o.equals(btnTim))
			timTheoMa();
		else if (o.equals(tfHo))
			tfSDT.requestFocus();
		else if (o.equals(tfSDT))
			tfDiaChi.requestFocus();
		else if (o.equals(tfDiaChi))
			them();
		else if (o.equals(tfTimMa))
			timTheoMa();
		else if (o.equals(btnXoaTrang))
			xoaTrang();
		else if (o.equals(btnLaphoadon)) {
			FrameTrangchu fr = (FrameTrangchu) SwingUtilities.getWindowAncestor(this);
			int index = table.getSelectedRow();
			if (index >= 0 && index < table.getRowCount()) {
				Khachhang kh = dao.getByID(table.getValueAt(index, 0).toString());
				fr.changePanelLaphoadon(kh);
			} else {
				JOptionPane.showMessageDialog(this, "Chọn khách hàng cần lập hóa đơn", "Thông báo",
						JOptionPane.INFORMATION_MESSAGE);
			}
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
	 * filltextfield
	 */
	private void fillTextField() {
		int index = table.getSelectedRow();
		if (index >= 0 && index < table.getRowCount()) {
			Khachhang kh = dao.getByID(table.getValueAt(index, 0).toString());
			tfMakh.setText(kh.getMakhachhang());
			tfHo.setText(kh.getHoten());
			tfSDT.setText(kh.getSodienthoai());
			tfDiaChi.setText(kh.getDiachi());
		}
	}

	/*
	 * private void resizeColumnsKH() {
	 * table.getColumnModel().getColumn(0).setMinWidth(150);
	 * table.getColumnModel().getColumn(1).setMinWidth(300);
	 * table.getColumnModel().getColumn(2).setMinWidth(210);
	 * table.getColumnModel().getColumn(3).setMinWidth(500);
	 * table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF); }
	 */

	/*
	 * private void count() { DecimalFormat df = new
	 * DecimalFormat("###,###,###.###"); double tongtien = 0; for (KhachHang kh :
	 * dskh) { try { tongtien += dao.getTongTien(kh.getMaKH()); } catch
	 * (SQLException e) { tongtien += 0; } }
	 * lblSumTongTien.setText(df.format(tongtien)); }
	 */

	/**
	 * xoa trang
	 */
	public void xoaTrang() {
		tfMakh.setText(dao.getNextMaKH());
		tfHo.requestFocus();
		tfHo.setText("");
		tfDiaChi.setText("");
		tfSDT.setText("");
		tfTimMa.setText("");
		new PlaceHolder(tfTimMa, new Color(192, 192, 192), Color.BLACK,
				"Nhập mã, họ tên hoặc số điện thoại của khách hàng", false, this.getFont().toString(), 19);
		decorator.reload();
		panelMain.remove(panel_timkiem);
		panelMain.add(panel_phantrang);
		panelMain.repaint();
		panelMain.revalidate();
		table.clearSelection();
	}

	/**
	 * them
	 */
	private void them() {
//		final JDialog frame = new JDialog((JFrame) SwingUtilities.getWindowAncestor(this), "Thêm khách hàng mới", true);
//		try {
//			frame.setIconImage(ImageIO.read(new File("img/code1.png")));
//		} catch (IOException exception) {
//		}
//		PanelThemkhachhang panelThemkhachhang = new PanelThemkhachhang();
//		frame.getContentPane().add(panelThemkhachhang);
//		frame.setSize(panelThemkhachhang.getWidth(), panelThemkhachhang.getHeight());
//		frame.setLocationRelativeTo((JFrame) SwingUtilities.getWindowAncestor(this));
//		frame.setVisible(true);
//		/**
//		 * khi nào đóng dialog trên thì mới chạy đoạn code bên dưới
//		 */
//		frame.dispose();
		Khachhang kh = getDataFromTextField();
		if (kh != null) {
			if (dao.themKhachHang((kh))) {
				xoaTrang();
				JOptionPane.showMessageDialog(this, "Thêm thành công", "Thêm khách hàng",
						JOptionPane.INFORMATION_MESSAGE);
			} else {
				JOptionPane.showMessageDialog(this, "Thêm không thành công", "Thêm khách hàng",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	/**
	 * tao 1 kh tu textfield
	 * 
	 * @return
	 */
	private Khachhang getDataFromTextField() {
		Khachhang kh = new Khachhang();
		if (validInputTextField()) {
			kh.setMakhachhang(tfMakh.getText().trim());
			kh.setHoten(tfHo.getText().trim());
			kh.setDiachi(tfDiaChi.getText().trim());
			kh.setSodienthoai(tfSDT.getText().trim());
			return kh;
		}
		return null;
	}

	/**
	 * xoa
	 */
	private void xoa() {
		/*
		 * int index = table.getSelectedRow(); if (index >= 0 && index <
		 * table.getRowCount()) { int xoa = JOptionPane.showConfirmDialog(this,
		 * "Bạn có muốn xóa khách hàng này không?", "Xóa", warning); if (xoa ==
		 * JOptionPane.YES_OPTION) { try { Khachhang khachHang = new
		 * Khachhang(tfMakh.getText().trim()); if (dao.xoaKhachHang(khachHang)) {
		 * xoaTrang(); JOptionPane.showMessageDialog(this, "Xóa thành công"); } } catch
		 * (HeadlessException e) { JOptionPane.showMessageDialog(this,
		 * "Xóa không thành công\nLỗi: " + e.getMessage()); } } } else {
		 * JOptionPane.showMessageDialog(this, "Chọn một khách hàng để xóa!", "Xóa",
		 * infomation); }
		 */
	}

	/**
	 * luu kh sau khi sua
	 */
	private void luu() {
		int index = table.getSelectedRow();
		if (index >= 0 && index < table.getRowCount()) {
			Khachhang kh = getDataFromTextField();
			if (kh != null) {
				if (dao.capNhatKhachHang((kh))) {
					xoaTrang();
					JOptionPane.showMessageDialog(this, "Cập nhật thành công", "Cập nhật khách hàng",
							JOptionPane.INFORMATION_MESSAGE);
				} else
					JOptionPane.showMessageDialog(this, "Cập nhật không thành công", "Cập nhật khách hàng",
							JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	/**
	 * tim kh
	 */
	private void timTheoMa() {
		String keyword = tfTimMa.getText().toUpperCase();
		DataProvider<Khachhang> dataTimkiem = new DataProvider<Khachhang>() {

			@Override
			public int getTotalRowCount() {
				return dao.getSizeTimkiem(keyword);
			}

			@Override
			public void addDataToTable(int startIndex, int endIndex) {
				List<Khachhang> list = dao.timkiem(startIndex, endIndex, keyword);
				model.setDskh(list);
				model.fireTableDataChanged();
			}
		};
		panelMain.remove(panel_timkiem);
		Decorator<Khachhang> decorator2 = Decorator.decorate(dataTimkiem, new int[] { 10, 25, 50, 75, 100 }, 25);
		panel_timkiem = decorator2.getContentPanel();
		panel_timkiem.setBounds(10, 842, 1620, 50);
		panel_timkiem.setBorder(BorderFactory.createEtchedBorder());
		panelMain.remove(panel_phantrang);
		panelMain.add(panel_timkiem);
		panelMain.repaint();
		panelMain.revalidate();
		if (model.getRowCount() == 0)
			JOptionPane.showMessageDialog(this, "Không tìm thấy khách hàng nào", "Tìm khách hàng",
					JOptionPane.INFORMATION_MESSAGE);
	}

	/**
	 * kiem tra tinh hop le cua du lieu dau vao
	 * 
	 * @return
	 */
	private boolean validInputTextField() {
		if (!tfHo.getText().trim().matches("[\\p{L}\\s]+")) {
			JOptionPane.showMessageDialog(this, "Họ tên khách chỉ chứa chữ, không chứa số hay kí tự đặc biệt",
					"Thông báo", error);
			tfHo.requestFocus();
			return false;
		}
		if (!tfSDT.getText().trim().matches("[0-9()+-]+")) {
			JOptionPane.showMessageDialog(this, "SĐT phải là số, có thể bao gồm các kí tự ( ) + -", "Thông báo", error);
			tfSDT.requestFocus();
			return false;
		}
		if (!tfDiaChi.getText().trim().matches("[\\p{L}\\s0-9()\\/_\\\\.,\\+-]+")) {
			JOptionPane.showMessageDialog(this, "Địa chỉ chỉ bao gồm chữ, số, và các kí tự ( ) \\ / _ , . + -",
					"Thông báo", error);
			tfDiaChi.requestFocus();
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