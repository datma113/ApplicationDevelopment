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
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;

import custom.MyJButton;
import custom.MyJLabel;
import custom.MyJPasswordField;
import custom.MyJScrollPane;
import custom.MyJTextField;
import custom.MyTableCellRender;
import dao.DaoNhanvien;
import dao.DaoTaikhoan;
import entity.Nhanvien;
import entity.Taikhoan;
import model.ModelTaikhoan;
import phantrang.DataProvider;
import phantrang.Decorator;

public class PanelQuanlyTaikhoan extends JPanel implements ActionListener, MouseListener {

	private static final long serialVersionUID = 1L;

	private JTable table;
	private DaoTaikhoan dao;
	private DaoNhanvien daoNhanvien;
	private ModelTaikhoan model;
	private List<Nhanvien> nhanviens;
	private MyJPasswordField tfPassword;
	private MyJTextField tfUsername;

	private MyJButton btnThem;
	private MyJButton btnXoatrang;
	private MyJButton btnLuu;
	private MyJButton btnXoa;
	private Decorator<Taikhoan> decorator;
	private JComboBox<String> comboBox;

	public PanelQuanlyTaikhoan() {
		setSize(800, 650);
		setLayout(null);

		JPanel pnlButton = new JPanel(null);
		add(pnlButton);
		pnlButton.setBounds(10, 544, 764, 56);
		pnlButton.setBorder(BorderFactory.createEtchedBorder());

		MyJLabel lblTitle = new MyJLabel("QUẢN LÝ TÀI KHOẢN ĐĂNG NHẬP HỆ THỐNG");
		lblTitle.setForeground(Color.RED);
		lblTitle.setFont(new Font("Serif", Font.BOLD, 20));
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitle.setBounds(10, 0, 764, 30);
		add(lblTitle);

		MyJLabel lblPassword = new MyJLabel("Mật khẩu");
		lblPassword.setBounds(420, 82, 119, 25);
		add(lblPassword);

		MyJLabel lblUsername = new MyJLabel("Tên đăng nhập");
		lblUsername.setBounds(10, 82, 163, 25);
		add(lblUsername);

		MyJLabel lblMaNV = new MyJLabel("Mã NV");
		lblMaNV.setBounds(10, 41, 97, 25);
		add(lblMaNV);

		tfPassword = new MyJPasswordField("");
		tfPassword.setBounds(510, 82, 264, 25);
		add(tfPassword);

		tfUsername = new MyJTextField(20);
		tfUsername.setBounds(146, 82, 264, 25);
		tfUsername.setEditable(false);
		add(tfUsername);

		btnThem = new MyJButton("Thêm");
		btnThem.setBounds(584, 11, 170, 35);
		btnThem.setMnemonic('t');
		btnThem.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnThem.setIcon(new ImageIcon("img\\them.png"));
		pnlButton.add(btnThem);

		btnXoatrang = new MyJButton("Xóa trắng");
		btnXoatrang.setBounds(10, 11, 170, 35);
		btnXoatrang.setMnemonic('x');
		btnXoatrang.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnXoatrang.setIcon(new ImageIcon("img\\refresh.png"));
		pnlButton.add(btnXoatrang);

		btnXoa = new MyJButton("Xóa");
		btnXoa.setMnemonic('a');
		btnXoa.setBounds(201, 11, 170, 35);
		btnXoa.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnXoa.setIcon(new ImageIcon("img\\delete.png"));
		pnlButton.add(btnXoa);

		btnLuu = new MyJButton("Lưu");
		btnLuu.setBounds(396, 11, 170, 35);
		btnLuu.setMnemonic('l');
		btnLuu.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnLuu.setIcon(new ImageIcon("img\\save.png"));
		pnlButton.add(btnLuu);

		dao = new DaoTaikhoan();
		daoNhanvien = new DaoNhanvien();
		model = new ModelTaikhoan();
		table = new JTable(model);
		table.setRowHeight(35);
		table.setAutoCreateRowSorter(true);
		table.getTableHeader().setBackground(new Color(255, 208, 120));
		table.getTableHeader().setPreferredSize(new Dimension(table.getTableHeader().getWidth(), 30));
		Font f3 = table.getTableHeader().getFont();
		table.getTableHeader().setFont(new Font(f3.getName(), Font.BOLD, f3.getSize() + 2));

		MyTableCellRender renderTable = new MyTableCellRender();
		table.setDefaultRenderer(Object.class, renderTable);

		MyJScrollPane scrollPane = new MyJScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setBounds(10, 124, 764, 348);
		add(scrollPane);

		DataProvider<Taikhoan> dataProvider = new DataProvider<Taikhoan>() {

			@Override
			public int getTotalRowCount() {
				return dao.getSize();
			}

			@Override
			public void addDataToTable(int startIndex, int endIndex) {
				List<Taikhoan> list = dao.getDsTk(startIndex, endIndex);
				model.setDstk(list);
				model.fireTableDataChanged();
			}

		};
		decorator = Decorator.decorate(dataProvider, new int[] { 10, 25, 50, 75, 100 }, 25);
		JPanel panel = decorator.getContentPanel();
//		JPanel panel = new JPanel();
		panel.setBounds(10, 483, 764, 50);
		panel.setBorder(BorderFactory.createEtchedBorder());
		add(panel);

		comboBox = new JComboBox<String>();
		comboBox.setBounds(146, 41, 264, 25);
		add(comboBox);

		comboBox.addActionListener(this);
		btnThem.addActionListener(this);
		btnXoa.addActionListener(this);
		btnXoatrang.addActionListener(this);
		btnLuu.addActionListener(this);
		tfPassword.addActionListener(this);

		table.addMouseListener(this);

		reloadTable();
		loadComboBoaxNhanvien();
	}

	public PanelQuanlyTaikhoan(Nhanvien nv) {
		setSize(800, 650);
		setLayout(null);

		JPanel pnlButton = new JPanel(null);
		add(pnlButton);
		pnlButton.setBounds(10, 544, 764, 56);
		pnlButton.setBorder(BorderFactory.createEtchedBorder());

		MyJLabel lblTitle = new MyJLabel("QUẢN LÝ TÀI KHOẢN ĐĂNG NHẬP HỆ THỐNG");
		lblTitle.setForeground(Color.RED);
		lblTitle.setFont(new Font("Serif", Font.BOLD, 20));
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitle.setBounds(10, 0, 764, 30);
		add(lblTitle);

		MyJLabel lblPassword = new MyJLabel("Mật khẩu");
		lblPassword.setBounds(420, 82, 119, 25);
		add(lblPassword);

		MyJLabel lblUsername = new MyJLabel("Tên đăng nhập");
		lblUsername.setBounds(10, 82, 163, 25);
		add(lblUsername);

		MyJLabel lblMaNV = new MyJLabel("Mã NV");
		lblMaNV.setBounds(10, 41, 97, 25);
		add(lblMaNV);

		tfPassword = new MyJPasswordField(nv.getTaikhoan().getMatkhau());
		tfPassword.setBounds(510, 82, 264, 25);
		add(tfPassword);

		tfUsername = new MyJTextField(nv.getTaikhoan().getTendangnhap());
		tfUsername.setBounds(146, 82, 264, 25);
		tfUsername.setEditable(false);
		add(tfUsername);

		btnThem = new MyJButton("Thêm");
		btnThem.setBounds(584, 11, 170, 35);
		btnThem.setMnemonic('t');
		btnThem.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnThem.setIcon(new ImageIcon("img\\them.png"));
		pnlButton.add(btnThem);

		btnXoatrang = new MyJButton("Xóa trắng");
		btnXoatrang.setBounds(10, 11, 170, 35);
		btnXoatrang.setMnemonic('x');
		btnXoatrang.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnXoatrang.setIcon(new ImageIcon("img\\refresh.png"));
		pnlButton.add(btnXoatrang);

		btnXoa = new MyJButton("Xóa");
		btnXoa.setMnemonic('a');
		btnXoa.setBounds(201, 11, 170, 35);
		btnXoa.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnXoa.setIcon(new ImageIcon("img\\delete.png"));
		pnlButton.add(btnXoa);

		btnLuu = new MyJButton("Lưu");
		btnLuu.setBounds(396, 11, 170, 35);
		btnLuu.setMnemonic('l');
		btnLuu.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnLuu.setIcon(new ImageIcon("img\\save.png"));
		pnlButton.add(btnLuu);

		dao = new DaoTaikhoan();
		daoNhanvien = new DaoNhanvien();
		model = new ModelTaikhoan();
		table = new JTable(model);
		table.setRowHeight(35);
		table.setAutoCreateRowSorter(true);
		table.getTableHeader().setBackground(new Color(255, 208, 120));
		table.getTableHeader().setPreferredSize(new Dimension(table.getTableHeader().getWidth(), 30));
		Font f3 = table.getTableHeader().getFont();
		table.getTableHeader().setFont(new Font(f3.getName(), Font.BOLD, f3.getSize() + 2));

		MyTableCellRender renderTable = new MyTableCellRender();
		table.setDefaultRenderer(Object.class, renderTable);

		MyJScrollPane scrollPane = new MyJScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setBounds(10, 124, 764, 348);
		add(scrollPane);

		DataProvider<Taikhoan> dataProvider = new DataProvider<Taikhoan>() {

			@Override
			public int getTotalRowCount() {
				return dao.getSize();
			}

			@Override
			public void addDataToTable(int startIndex, int endIndex) {
				List<Taikhoan> list = dao.getDsTk(startIndex, endIndex);
				model.setDstk(list);
				model.fireTableDataChanged();
			}

		};
		decorator = Decorator.decorate(dataProvider, new int[] { 10, 25, 50, 75, 100 }, 25);
		JPanel panel = decorator.getContentPanel();
//		JPanel panel = new JPanel();
		panel.setBounds(10, 483, 764, 50);
		panel.setBorder(BorderFactory.createEtchedBorder());
		add(panel);

		comboBox = new JComboBox<String>();
		comboBox.setBounds(146, 41, 264, 25);
		add(comboBox);

		comboBox.addActionListener(this);
		btnThem.addActionListener(this);
		btnXoa.addActionListener(this);
		btnXoatrang.addActionListener(this);
		btnLuu.addActionListener(this);
		tfPassword.addActionListener(this);

		table.addMouseListener(this);

//		reloadTable();
		loadComboBoaxNhanvien();
		comboBox.setSelectedItem(nv.getManhanvien());
	}

	private void loadComboBoaxNhanvien() {
		nhanviens = daoNhanvien.getAll();
		for (Nhanvien nhanvien : nhanviens) {
			comboBox.addItem(nhanvien.getManhanvien());
		}
	}

	// ------------------------------------------------------------------------------------
	@Override
	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource();
		if (o.equals(btnThem) || o.equals(tfPassword)) {
			them();
		} else if (o.equals(btnLuu)) {
			luu();
		} else if (o.equals(btnXoatrang)) {
			xoaTrang();
		} else if (o.equals(btnXoa)) {
			xoa();
		} else if (o.equals(comboBox)) {
			tfUsername.setText(comboBox.getSelectedItem().toString());
			tfPassword.requestFocus();
		}
	}

	/**
	 * reload table
	 */
	private void reloadTable() {
		decorator.reload();
		xoaTrang();
	}

	/**
	 * xoa trang
	 */
	private void xoaTrang() {
		try {
			comboBox.setSelectedIndex(0);
		} catch (Exception e) {
		}
		tfUsername.setText("");
		tfPassword.setText("");
		table.clearSelection();
	}

	/**
	 * them
	 */
	private void them() {
		if (table.getSelectedRow() != -1) {
			xoaTrang();
		} else if (validInputTextField()) {
			Taikhoan tknew = getDataFromTextField();
			if (tknew != null) {
				if (dao.themTaiKhoan(tknew)) {
					reloadTable();
					JOptionPane.showMessageDialog(this, "Thêm thành công", "Thêm tài khoản",
							JOptionPane.INFORMATION_MESSAGE);
				} else
					JOptionPane.showMessageDialog(this,
							"Thêm không thành công. Mỗi nhân viên hoặc người quản lý chỉ có thể có một tài khoản",
							"Thêm tài khoản", JOptionPane.ERROR_MESSAGE);
			} else {
				JOptionPane.showMessageDialog(this, "Thêm không thành công", "Thêm tài khoản",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	/**
	 * tao 1 kh tu textfield
	 * 
	 * @return
	 */
	private Taikhoan getDataFromTextField() {
		Taikhoan tk = new Taikhoan();
		if (validInputTextField()) {
			tk.setTendangnhap(tfUsername.getText().trim());
			tk.setMatkhau(new String(tfPassword.getPassword()).trim());
			tk.setNhanvien(daoNhanvien.getNhanVien(comboBox.getSelectedItem().toString()));
			return tk;
		}
		return null;
	}

	/**
	 * xoa
	 */
	private void xoa() {
		int index = table.getSelectedRow();
		if (index >= 0 && index < table.getRowCount()) {
			int xoa = JOptionPane.showConfirmDialog(this, "Bạn có muốn xóa tài khoản này không?", "Xóa",
					JOptionPane.WARNING_MESSAGE);
			if (xoa == JOptionPane.YES_OPTION) {
				if (dao.xoaTaiKhoan(getDataFromTextField())) {
					reloadTable();
					JOptionPane.showMessageDialog(this, "Xóa thành công", "Xóa tài khoản",
							JOptionPane.INFORMATION_MESSAGE);
				} else
					JOptionPane.showMessageDialog(this, "Xóa không thành công", "Xóa tài khoản",
							JOptionPane.ERROR_MESSAGE);
			}
		} else {
			JOptionPane.showMessageDialog(this, "Chọn 1 dòng để xóa", "Xóa tài khoản", JOptionPane.INFORMATION_MESSAGE);
		}
	}

	/**
	 * luu kh sau khi sua
	 */
	private void luu() {
		int index = table.getSelectedRow();
		if (index >= 0 && index < table.getRowCount()) {
			Taikhoan tknew = getDataFromTextField();
			if (tknew != null) {
				if (dao.capNhatTaiKhoan(tknew)) {
					reloadTable();
					JOptionPane.showMessageDialog(this, "Cập nhật thành công", "Cập nhật tài khoản",
							JOptionPane.INFORMATION_MESSAGE);
				} else
					JOptionPane.showMessageDialog(this, "Cập nhật không thành công", "Cập nhật tài khoản",
							JOptionPane.ERROR_MESSAGE);
			} else {
				JOptionPane.showMessageDialog(this, "Cập nhật không thành công", "Cập nhật tài khoản",
						JOptionPane.ERROR_MESSAGE);
			}
		} else {
			JOptionPane.showMessageDialog(this, "Click vào tài khoản cần sửa trên table", "Cập nhật tài khoản",
					JOptionPane.INFORMATION_MESSAGE);
		}
	}

	/**
	 * kiem tra tinh hop le cua du lieu dau vao
	 * 
	 * @return
	 */
	private boolean validInputTextField() {
		if (tfUsername.getText().trim().equals("")) {
			JOptionPane.showMessageDialog(this, "Chưa chọn mã nhân viên", "Thông báo", JOptionPane.ERROR_MESSAGE);
			comboBox.setPopupVisible(true);
			return false;
		}
		if (new String(tfPassword.getPassword()).trim().equals("")) {
			JOptionPane.showMessageDialog(this, "Mật khẩu không được để trống", "Thông báo", JOptionPane.ERROR_MESSAGE);
			tfPassword.requestFocus();
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
			tfUsername.setText(table.getValueAt(index, 1).toString());
			tfPassword.setText(table.getValueAt(index, 2).toString());
			comboBox.setSelectedItem(tfUsername.getText());
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
