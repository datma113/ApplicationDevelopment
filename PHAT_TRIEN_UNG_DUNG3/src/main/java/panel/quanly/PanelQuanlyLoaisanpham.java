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
import dao.DaoLoaisanpham;
import entity.Loaisanpham;
import model.ModelLoaisanpham;
import panel.PanelImportLoaisanpham;
import phantrang.DataProvider;
import phantrang.Decorator;

public class PanelQuanlyLoaisanpham extends JPanel implements ActionListener, MouseListener {

	private static final long serialVersionUID = 1L;

	private JTable table;
	private DaoLoaisanpham dao;
	private ModelLoaisanpham model;

	private MyJTextField tfTenLsp;
	private MyJTextField tfMaLsp;

	private MyJButton btnThem;
	private MyJButton btnXoa;
	private MyJButton btnLuu;
	private MyJButton btnXoatrang;
	private MyJButton btnImport;
	private Decorator<Loaisanpham> decorator;

	public PanelQuanlyLoaisanpham() {

		setSize(800, 650);
		setLayout(null);

		JPanel pnlButton = new JPanel(null);
		add(pnlButton);
		pnlButton.setBounds(10, 544, 764, 56);
		pnlButton.setBorder(BorderFactory.createEtchedBorder());

		MyJLabel lblTitle = new MyJLabel("QUẢN LÝ LOẠI SẢN PHẨM");
		lblTitle.setForeground(Color.RED);
		lblTitle.setFont(new Font("Serif", Font.BOLD, 20));
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitle.setBounds(10, 0, 764, 30);
		add(lblTitle);

		MyJLabel lblTenSp = new MyJLabel("Tên Loại SP");
		lblTenSp.setBounds(10, 84, 163, 25);
		add(lblTenSp);

		MyJLabel lblMH = new MyJLabel("Mã Loại SP");
		lblMH.setBounds(10, 46, 163, 25);
		add(lblMH);

		tfTenLsp = new MyJTextField("");
		tfTenLsp.setBounds(144, 82, 630, 25);
		add(tfTenLsp);

		tfMaLsp = new MyJTextField(20);
		tfMaLsp.setBounds(144, 44, 630, 25);
		tfMaLsp.setEditable(false);
		add(tfMaLsp);

		btnThem = new MyJButton("Thêm");
		btnThem.setBounds(392, 11, 170, 35);
		btnThem.setMnemonic('t');
		btnThem.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnThem.setIcon(new ImageIcon("img\\them.png"));
		pnlButton.add(btnThem);

		btnXoa = new MyJButton("Xóa");
		btnXoa.setBounds(153, 11, 130, 35);
		btnXoa.setMnemonic('x');
		btnXoa.setEnabled(false);
		btnXoa.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnXoa.setIcon(new ImageIcon("img\\delete.png"));
//		pnlButton.add(btnXoa);

		btnLuu = new MyJButton("Lưu");
		btnLuu.setBounds(201, 11, 170, 35);
		btnLuu.setMnemonic('L');
		btnLuu.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnLuu.setIcon(new ImageIcon("img\\save.png"));
		pnlButton.add(btnLuu);

		btnXoatrang = new MyJButton("Xóa trắng");
		btnXoatrang.setMnemonic('x');
		btnXoatrang.setBounds(10, 11, 170, 35);
		btnXoatrang.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnXoatrang.setIcon(new ImageIcon("img\\refresh.png"));
		pnlButton.add(btnXoatrang);

		btnImport = new MyJButton("Nhập từ file Excel");
		btnImport.setMnemonic('N');
		btnImport.setBounds(584, 11, 170, 35);
		btnImport.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnImport.setIcon(new ImageIcon("img\\excel.png"));
		pnlButton.add(btnImport);

		dao = new DaoLoaisanpham();
		model = new ModelLoaisanpham();
		table = new JTable(model);
		table.setRowHeight(35);
		table.setAutoCreateRowSorter(true);
		table.getTableHeader().setBackground(new Color(255, 208, 120));
		table.getTableHeader().setPreferredSize(new Dimension(table.getTableHeader().getWidth(), 30));
		Font f3 = table.getTableHeader().getFont();
		table.getTableHeader().setFont(new Font(f3.getName(), Font.BOLD, f3.getSize() + 2));

		MyTableCellRender renderTable = new MyTableCellRender();
		table.setDefaultRenderer(Object.class, renderTable);

		MyJScrollPane scrollPane = new MyJScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setBounds(10, 124, 764, 348);
		add(scrollPane);

		DataProvider<Loaisanpham> dataProvider = new DataProvider<Loaisanpham>() {
			@Override
			public int getTotalRowCount() {
				return dao.getSize();
			}

			@Override
			public void addDataToTable(int startIndex, int endIndex) {
				List<Loaisanpham> list = dao.getDsLsp(startIndex, endIndex);
				model.setDslsp(list);
				model.fireTableDataChanged();
			}

		};
		decorator = Decorator.decorate(dataProvider, new int[] { 10, 25, 50, 75, 100 }, 10);
		JPanel panel = decorator.getContentPanel();
//		JPanel panel = new JPanel();
		panel.setBounds(10, 483, 764, 50);
		panel.setBorder(BorderFactory.createEtchedBorder());
		add(panel);

		btnThem.addActionListener(this);
		btnXoa.addActionListener(this);
		btnLuu.addActionListener(this);
		btnXoatrang.addActionListener(this);
		btnImport.addActionListener(this);
		tfTenLsp.addActionListener(this);

		table.addMouseListener(this);

		reloadTable();
//		resizeColumns();
		xoaTrang();
	}

//------------------------------------------------------------------------------------
	@Override
	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource();
		if (o.equals(btnThem) || o.equals(tfTenLsp)) {
			them();
		} else if (o.equals(btnLuu)) {
			luu();
		} else if (o.equals(btnXoa)) {
			xoa();
		} else if (o.equals(btnXoatrang)) {
			xoaTrang();
		} else if (o.equals(btnImport)) {
			final JDialog frame = new JDialog((JDialog) SwingUtilities.getWindowAncestor(this),
					"Nhập dữ liệu loại sản phẩm từ file Excel", true);
			try {
				frame.setIconImage(ImageIO.read(new File("img/code1.png")));
			} catch (IOException exception) {
			}
			PanelImportLoaisanpham importLoaisanpham = new PanelImportLoaisanpham();
			frame.getContentPane().add(importLoaisanpham);
			frame.setSize(importLoaisanpham.getWidth(), importLoaisanpham.getHeight());
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

	/*
	 * private void resizeColumns() {
	 * table.getColumnModel().getColumn(0).setMinWidth(200);
	 * table.getColumnModel().getColumn(1).setMinWidth(353);
	 * table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF); }
	 */

	/**
	 * xoa trang
	 */
	private void xoaTrang() {
		tfMaLsp.setText(dao.getNextMaLSP());
		tfTenLsp.setText("");
		table.clearSelection();
	}

	/**
	 * them
	 */
	private void them() {
		if (table.getSelectedRow() != -1) {
			xoaTrang();
		} else if (validInputTextField()) {
			Loaisanpham lsp = getDataFromTextField();
			if (lsp != null) {
				if (dao.themLoaiSanPham(lsp)) {
					reloadTable();
					xoaTrang();
					JOptionPane.showMessageDialog(this, "Thêm thành công", "Thêm loại sản phẩm",
							JOptionPane.INFORMATION_MESSAGE);
				} else
					JOptionPane.showMessageDialog(this, "Thêm không thành công", "Thêm loại sản phẩm",
							JOptionPane.ERROR_MESSAGE);
			} else {
				JOptionPane.showMessageDialog(this, "Thêm không thành công", "Thêm loại sản phẩm",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	/**
	 * tao 1 kh tu textfield
	 * 
	 * @return
	 */
	private Loaisanpham getDataFromTextField() {
		Loaisanpham lsp = new Loaisanpham();
		if (validInputTextField()) {
			lsp.setMaloai(tfMaLsp.getText().trim());
			lsp.setTenloai(tfTenLsp.getText().trim());
			return lsp;
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
		 * "Bạn có muốn xóa loại sản phẩm này không?", "Xóa",
		 * JOptionPane.WARNING_MESSAGE); if (xoa == JOptionPane.YES_OPTION) {
		 * Loaisanpham loaiSp = new Loaisanpham(tfMaLsp.getText().trim()); if
		 * (dao.xoaLoaiSanPham(loaiSp)) { reloadTable(); xoaTrang();
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
			Loaisanpham lspnew = getDataFromTextField();
			if (lspnew != null) {
				if (dao.capNhatLoaiSp(lspnew)) {
					reloadTable();
					JOptionPane.showMessageDialog(this, "Cập nhật thành công", "Cập nhật loại sản phẩm",
							JOptionPane.INFORMATION_MESSAGE);
				} else
					JOptionPane.showMessageDialog(this, "Cập nhật không thành công", "Cập nhật loại sản phẩm",
							JOptionPane.ERROR_MESSAGE);
			} else {
				JOptionPane.showMessageDialog(this, "Cập nhật không thành công", "Cập nhật loại sản phẩm",
						JOptionPane.ERROR_MESSAGE);
			}
		} else {
			JOptionPane.showMessageDialog(this, "Click vào loại sản phẩm cần sửa trên bảng", "Cập nhật",
					JOptionPane.INFORMATION_MESSAGE);
		}
	}

	/**
	 * kiem tra tinh hop le cua du lieu dau vao
	 * 
	 * @return
	 */
	private boolean validInputTextField() {
		if (!tfTenLsp.getText().trim().matches("[\\p{L}\\s0-9()\\/_\\\\.,\\+-]+")) {
			JOptionPane.showMessageDialog(this, "Tên LSP chỉ bao gồm chữ, số, và các kí tự ( ) \\ / _ , . + -",
					"Thông báo", JOptionPane.ERROR_MESSAGE);
			tfTenLsp.requestFocus();
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
			Loaisanpham lsp = dao.getByID(table.getValueAt(index, 0).toString());
			tfMaLsp.setText(lsp.getMaloai());
			tfTenLsp.setText(lsp.getTenloai());
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