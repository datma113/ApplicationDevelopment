package panel.quanly;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;
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
import custom.MyJTextField;
import custom.MyTableCellRender;
import dao.DaoNhacungcap;
import entity.Nhacungcap;
import model.ModelNhacungcap;
import panel.PanelImportNhacungcap;
import phantrang.DataProvider;
import phantrang.Decorator;

public class PanelQuanlyNhacungcap extends JPanel implements ActionListener, MouseListener {

	private static final long serialVersionUID = 1L;
	private static final int vertical_always = JScrollPane.VERTICAL_SCROLLBAR_ALWAYS;
	private static final int horizontal_needed = JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED;

	private JTable table;
	private DaoNhacungcap dao;
	private ModelNhacungcap model;

	private MyJTextField tfMaNcc;
	private MyJTextField tfTenNcc;
	private MyJTextField tfEmail;
	private MyJTextField tfDiaChi;

	private MyJButton btnThem;
	private MyJButton btnXoa;
	private MyJButton btnLuu;
	private MyJButton btnXoaTrang;
	private MyJButton btnImport;
	private Decorator<Nhacungcap> decorator;

	public PanelQuanlyNhacungcap() {

		setSize(1000, 755);
		setLayout(null);

		JPanel pnlButton = new JPanel(null);
		add(pnlButton);
		pnlButton.setBounds(10, 649, 964, 56);
		pnlButton.setBorder(BorderFactory.createEtchedBorder());

		MyJLabel lblTitle = new MyJLabel("QUẢN LÝ NHÀ CUNG CẤP SẢN PHẨM");
		lblTitle.setForeground(Color.RED);
		lblTitle.setFont(new Font("Serif", Font.BOLD, 20));
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitle.setBounds(10, 0, 964, 30);
		add(lblTitle);

		MyJLabel lblEmail = new MyJLabel("Email");
		lblEmail.setBounds(508, 43, 86, 25);
		add(lblEmail);

		MyJLabel lblDiaChi = new MyJLabel("Địa Chỉ");
		lblDiaChi.setBounds(10, 132, 142, 25);
		add(lblDiaChi);

		MyJLabel lblMaNcc = new MyJLabel("Mã NCC");
		lblMaNcc.setBounds(10, 43, 142, 25);
		add(lblMaNcc);

		MyJLabel lblTenNcc = new MyJLabel("Tên NCC");
		lblTenNcc.setBounds(10, 87, 142, 25);
		add(lblTenNcc);

		tfEmail = new MyJTextField(20);
		tfEmail.setBounds(560, 43, 414, 25);
		add(tfEmail);

		tfDiaChi = new MyJTextField(20);
		tfDiaChi.setBounds(108, 130, 866, 25);
		add(tfDiaChi);

		tfMaNcc = new MyJTextField("");
		tfMaNcc.setBounds(108, 43, 390, 25);
		tfMaNcc.setEditable(false);
		add(tfMaNcc);

		tfTenNcc = new MyJTextField(20);
		tfTenNcc.setBounds(108, 85, 866, 25);
		add(tfTenNcc);

		btnThem = new MyJButton("Thêm");
		btnThem.setBounds(530, 11, 170, 35);
		btnThem.setMnemonic('t');
		btnThem.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnThem.setIcon(new ImageIcon("img\\them.png"));
		pnlButton.add(btnThem);

		btnXoa = new MyJButton("Xóa");
		btnXoa.setBounds(203, 11, 135, 35);
		btnXoa.setMnemonic('x');
		btnXoa.setEnabled(false);
		btnXoa.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnXoa.setIcon(new ImageIcon("img\\delete.png"));
//		pnlButton.add(btnXoa);

		btnLuu = new MyJButton("Lưu");
		btnLuu.setBounds(263, 11, 170, 35);
		btnLuu.setMnemonic('l');
		btnLuu.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnLuu.setIcon(new ImageIcon("img\\save.png"));
		pnlButton.add(btnLuu);

		btnXoaTrang = new MyJButton("Xóa Trắng");
		btnXoaTrang.setMnemonic('x');
		btnXoaTrang.setBounds(10, 11, 170, 35);
		btnXoaTrang.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnXoaTrang.setIcon(new ImageIcon("img\\refresh.png"));
		pnlButton.add(btnXoaTrang);

		btnImport = new MyJButton("Nhập từ file Excel");
		btnImport.setMnemonic('N');
		btnImport.setBounds(784, 11, 170, 35);
		btnImport.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnImport.setIcon(new ImageIcon("img\\excel.png"));
		pnlButton.add(btnImport);

		dao = new DaoNhacungcap();
		model = new ModelNhacungcap();
		table = new JTable(model);
		table.setRowHeight(35);
		table.setAutoCreateRowSorter(true);
		table.getTableHeader().setBackground(new Color(255, 208, 120));
		table.getTableHeader().setPreferredSize(new Dimension(table.getTableHeader().getWidth(), 30));
		Font f3 = table.getTableHeader().getFont();
		table.getTableHeader().setFont(new Font(f3.getName(), Font.BOLD, f3.getSize() + 2));

		MyTableCellRender renderTable = new MyTableCellRender();
		table.setDefaultRenderer(Object.class, renderTable);

		MyJScrollPane scrollPane = new MyJScrollPane(table, vertical_always, horizontal_needed);
		scrollPane.setBounds(10, 185, 964, 391);
		add(scrollPane);

		DataProvider<Nhacungcap> dataProvider = new DataProvider<Nhacungcap>() {

			@Override
			public int getTotalRowCount() {
				return dao.getSize();
			}

			@Override
			public void addDataToTable(int startIndex, int endIndex) {
				List<Nhacungcap> list = dao.getDsNcc(startIndex, endIndex);
				model.setDsncc(list);
				model.fireTableDataChanged();
			}

		};
		decorator = Decorator.decorate(dataProvider, new int[] { 10, 25, 50, 75, 100 }, 25);
		JPanel panel = decorator.getContentPanel();
//		JPanel panel = new JPanel();
		panel.setBounds(10, 588, 964, 50);
		panel.setBorder(BorderFactory.createEtchedBorder());
		add(panel);

		btnThem.addActionListener(this);
		btnXoa.addActionListener(this);
		btnLuu.addActionListener(this);
		btnImport.addActionListener(this);
		btnXoaTrang.addActionListener(this);
		tfTenNcc.addActionListener(this);
		tfEmail.addActionListener(this);
		tfDiaChi.addActionListener(this);

		table.addMouseListener(this);

		reloadTable();
		resizeColumn();
		xoaTrang();
	}

//------------------------------------------------------------------------------------
	@Override
	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource();
		if (o.equals(btnThem) || o.equals(tfDiaChi)) {
			them();
		} else if (o.equals(btnLuu)) {
			luu();
		} else if (o.equals(btnXoa)) {
			xoa();
		} else if (o.equals(tfTenNcc)) {
			tfEmail.requestFocus();
		} else if (o.equals(tfEmail)) {
			tfDiaChi.requestFocus();
		} else if (o.equals(btnXoaTrang)) {
			xoaTrang();
		} else if (o.equals(btnImport)) {
			final JDialog frame = new JDialog((JDialog) SwingUtilities.getWindowAncestor(this),
					"Nhập dữ liệu nhà cung cấp từ file Excel", true);
			try {
				frame.setIconImage(ImageIO.read(new File("img/code1.png")));
			} catch (IOException exception) {
			}
			PanelImportNhacungcap importNhacungcap = new PanelImportNhacungcap();
			frame.getContentPane().add(importNhacungcap);
			frame.setSize(importNhacungcap.getWidth(), importNhacungcap.getHeight());
			frame.setLocationRelativeTo((JDialog) SwingUtilities.getWindowAncestor(this));
			frame.setVisible(true);
		}
	}

	/**
	 * reload table
	 */
	private void reloadTable() {
		decorator.reload();
	}

	private void resizeColumn() {
		table.getColumnModel().getColumn(0).setMinWidth(100);
		table.getColumnModel().getColumn(1).setMinWidth(350);
		table.getColumnModel().getColumn(2).setMinWidth(200);
		table.getColumnModel().getColumn(3).setMinWidth(350);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
	}

	/**
	 * xoa trang
	 */
	private void xoaTrang() {
		tfMaNcc.setText(dao.getNextMaNCC());
		tfTenNcc.setText("");
		tfTenNcc.requestFocus();
		tfEmail.setText("");
		tfDiaChi.setText("");
		table.clearSelection();
	}

	/**
	 * them
	 */
	private void them() {
		if (table.getSelectedRow() != -1) {
			xoaTrang();
		} else if (validInputTextField()) {
			Nhacungcap ncc = getDataFromTextField();
			if (ncc != null) {
				if (dao.themNhaCungCap(ncc)) {
					reloadTable();
					xoaTrang();
					JOptionPane.showMessageDialog(this, "Thêm thành công", "Thêm nhà cung cấp",
							JOptionPane.INFORMATION_MESSAGE);
				} else
					JOptionPane.showMessageDialog(this, "Thêm không thành công", "Thêm nhà cung cấp",
							JOptionPane.ERROR_MESSAGE);
			} else {
				JOptionPane.showMessageDialog(this, "Thêm không thành công", "Thêm nhà cung cấp",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	/**
	 * tao 1 kh tu textfield
	 * 
	 * @return
	 */
	private Nhacungcap getDataFromTextField() {
		Nhacungcap ncc = new Nhacungcap();
		if (validInputTextField()) {
			ncc.setManhacungcap(tfMaNcc.getText().trim());
			ncc.setTennhacungcap(tfTenNcc.getText().trim());
			ncc.setEmail(tfEmail.getText().trim());
			ncc.setDiachi(tfDiaChi.getText().trim());
			return ncc;
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
		 * "Bạn có muốn xóa nhà cung cấp này không?", "Xóa",
		 * JOptionPane.WARNING_MESSAGE); if (xoa == JOptionPane.YES_OPTION) { Nhacungcap
		 * nhaCungCap = new Nhacungcap(tfMaNcc.getText()); if
		 * (dao.xoaNhaCungCap(nhaCungCap)) { reloadTable(); xoaTrang();
		 * JOptionPane.showMessageDialog(this, "Xóa thành công"); } else
		 * JOptionPane.showMessageDialog(this, "Xóa không thành công"); } } else {
		 * JOptionPane.showMessageDialog(this, "Chọn 1 dòng để xóa!", "Xóa",
		 * JOptionPane.INFORMATION_MESSAGE); }
		 */
	}

	/**
	 * luu kh sau khi sua
	 */
	private void luu() {
		int index = table.getSelectedRow();
		if (index >= 0 && index < table.getRowCount()) {
			Nhacungcap nccnew = getDataFromTextField();
			if (nccnew != null) {
				if (dao.capNhatNhaCc(nccnew)) {
					reloadTable();
					JOptionPane.showMessageDialog(this, "Cập nhật thành công", "Cập nhật nhà cung cấp",
							JOptionPane.INFORMATION_MESSAGE);
				} else
					JOptionPane.showMessageDialog(this, "Cập nhật không thành công", "Cập nhật nhà cung cấp",
							JOptionPane.ERROR_MESSAGE);
			} else {
				JOptionPane.showMessageDialog(this, "Cập nhật không thành công", "Cập nhật nhà cung cấp",
						JOptionPane.ERROR_MESSAGE);
			}
		} else {
			JOptionPane.showMessageDialog(this, "Click vào một dòng cần sửa trên bảng", "Cập nhật nhà cung cấp",
					JOptionPane.INFORMATION_MESSAGE);
		}
	}

	/**
	 * kiem tra tinh hop le cua du lieu dau vao
	 * 
	 * @return
	 */
	private boolean validInputTextField() {
		if (!tfTenNcc.getText().trim().matches("[\\p{L}\\s0-9()\\/_\\\\.,\\+-]+")) {
			JOptionPane.showMessageDialog(this, "Tên nhà cung cấp chỉ bao gồm chữ, số, và các kí tự ( ) \\ / _ , . + -",
					"Thông báo", JOptionPane.ERROR_MESSAGE);
			tfTenNcc.requestFocus();
			return false;
		}
		if (tfEmail.getText().trim().equals("")) {
			JOptionPane.showMessageDialog(this, "Email không được để trống", "Thông báo", JOptionPane.ERROR_MESSAGE);
			tfEmail.requestFocus();
			return false;
		}
		if (!tfDiaChi.getText().trim().matches("[\\p{L}\\s0-9()\\/_\\\\.,\\+-]+")) {
			JOptionPane.showMessageDialog(this, "Địa chỉ chỉ bao gồm chữ, số, và các kí tự ( ) \\ / _ , . + -",
					"Thông báo", JOptionPane.ERROR_MESSAGE);
			tfDiaChi.requestFocus();
			return false;
		}
		return true;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		Object o = e.getSource();
		if (o.equals(table)) {
			fillTextField();
		}
	}

	private void fillTextField() {
		int index = table.getSelectedRow();
		if (index >= 0 && index < table.getRowCount()) {
			Nhacungcap ncc = dao.getByID(table.getValueAt(index, 0).toString());
			tfMaNcc.setText(ncc.getManhacungcap());
			tfTenNcc.setText(ncc.getTennhacungcap());
			tfEmail.setText(ncc.getEmail());
			tfDiaChi.setText(ncc.getDiachi());
		}
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
