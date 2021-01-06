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
import javax.swing.JFrame;
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
import dao.DaoHoadon;
import entity.Hoadon;
import entity.Khachhang;
import entity.Nhanvien;
import model.ModelHoadon;
import panel.PanelChitiethoadon;
import phantrang.DataProvider;
import phantrang.Decorator;

public class PanelQuanlyHoadon extends JPanel implements ActionListener, MouseListener {

	private static final long serialVersionUID = 1L;
	private static final int vertical_always = JScrollPane.VERTICAL_SCROLLBAR_ALWAYS;
	private static final int horizontal_needed = JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED;

	private JTable table;
	private DaoHoadon dao;
	private ModelHoadon model;

	private MyJTextField tfTimMaHD;

	private MyJButton btnTim;

	private JPanel pnlTimKiem;
//	private CustomJLabel lblSumTongTien;
	private Nhanvien nv;
	private MyJLabel lblNewLabel;
	private Decorator<Hoadon> decorator;
	private JPanel panel_phantrang;
	private JPanel pnlMain;
	private JPanel panel_timkiem;
	private MyJButton btnXoatrang;

	public PanelQuanlyHoadon(Nhanvien nv) {
		setNv(nv);
		setSize(1680, 1021);
		setLayout(null);

		setBackground(Color.WHITE);

		/**
		 * panel
		 */

		panel_timkiem = new JPanel(null);

		pnlMain = new JPanel(null);
		pnlMain.setLocation(10, 29);
		pnlMain.setSize(1640, 903);
		pnlMain.setBorder(BorderFactory.createEtchedBorder());

		pnlTimKiem = new JPanel(null);
		pnlTimKiem.setBounds(10, 943, 1640, 55);
		pnlTimKiem.setBorder(BorderFactory.createEtchedBorder());
		add(pnlTimKiem);

		/**
		 * label
		 */
		MyJLabel lblTitle = new MyJLabel("QUẢN LÝ HÓA ĐƠN");
		lblTitle.setForeground(Color.RED);
		lblTitle.setFont(new Font("Serif", Font.BOLD, 20));
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitle.setBounds(10, 0, 1638, 30);
		add(lblTitle);

//		pnlMain.add(lblSumTongTien = new CustomJLabel(""));
//		lblSumTongTien.setHorizontalAlignment(SwingConstants.RIGHT);
//		lblSumTongTien.setBounds(696, 498, 121, 30);

		/**
		 * textfield
		 */
		tfTimMaHD = new MyJTextField("");
		tfTimMaHD.setBounds(170, 15, 580, 25);
		pnlTimKiem.add(tfTimMaHD);

		/**
		 * button
		 */
		btnTim = new MyJButton("Tìm");
		btnTim.setBounds(1270, 9, 175, 35);
		btnTim.setMnemonic('m');
		btnTim.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnTim.setIcon(new ImageIcon("img\\find.png"));
		pnlTimKiem.add(btnTim);

		btnXoatrang = new MyJButton("Xóa trắng");
		btnXoatrang.setMnemonic('x');
		btnXoatrang.setBounds(1455, 9, 175, 35);
		btnXoatrang.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnXoatrang.setIcon(new ImageIcon("img\\refresh.png"));
		pnlTimKiem.add(btnXoatrang);

		lblNewLabel = new MyJLabel("Tìm theo từ khóa");
		lblNewLabel.setBounds(10, 9, 181, 37);
		pnlTimKiem.add(lblNewLabel);

		dao = new DaoHoadon();
		model = new ModelHoadon();
		table = new JTable(model);
		table.setRowHeight(35);
		table.setAutoCreateRowSorter(true);
		table.getTableHeader().setBackground(new Color(255, 208, 120));
		table.getTableHeader().setPreferredSize(new Dimension(table.getTableHeader().getWidth(), 30));
		Font f3 = table.getTableHeader().getFont();
		table.getTableHeader().setFont(new Font(f3.getName(), Font.BOLD, f3.getSize() + 2));

		MyTableCellRender renderTable = new MyTableCellRender();
		table.setDefaultRenderer(String.class, renderTable);
		table.setDefaultRenderer(Double.class, renderTable);

		MyJScrollPane scrollPane = new MyJScrollPane(table, vertical_always, horizontal_needed);
		scrollPane.setBounds(10, 28, 1620, 803);
		pnlMain.add(scrollPane);

		add(pnlMain);
		DataProvider<Hoadon> dataProvider = new DataProvider<Hoadon>() {

			@Override
			public int getTotalRowCount() {
				return dao.getSize();
			}

			@Override
			public void addDataToTable(int startIndex, int endIndex) {
				List<Hoadon> list = dao.getDsHd(startIndex, endIndex);
				model.setDshd(list);
				model.fireTableDataChanged();
			}

		};
		decorator = Decorator.decorate(dataProvider, new int[] { 10, 25, 50, 75, 100 }, 25);
		panel_phantrang = decorator.getContentPanel();
//		JPanel panel_phantrang = new JPanel();
		panel_phantrang.setBounds(10, 842, 1620, 50);
		panel_phantrang.setBorder(BorderFactory.createEtchedBorder());
		pnlMain.add(panel_phantrang);

		btnTim.addActionListener(this);
		btnXoatrang.addActionListener(this);
		tfTimMaHD.addActionListener(this);

		table.addMouseListener(this);

		reloadTable();
	}

	/**
	 * ================================================
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource();
		if (o.equals(btnTim)) {
			tim();
		} else if (o.equals(tfTimMaHD)) {
			btnTim.doClick();
		} else if (o.equals(btnXoatrang)) {
			reloadTable();
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		Object o = e.getSource();
		if (o.equals(table)) {
			if (e.getClickCount() % 2 == 0 && !e.isConsumed()) {
				e.consume();
				int index = table.getSelectedRow();
				final JDialog frame = new JDialog((JFrame) SwingUtilities.getWindowAncestor(this), "Chi tiết hóa đơn",
						true);
				try {
					frame.setIconImage(ImageIO.read(new File("img/code1.png")));
				} catch (IOException exception) {
				}
				JPanel panel = new PanelChitiethoadon(table.getValueAt(index, 0).toString());
				frame.getContentPane().add(panel);
				frame.setSize(panel.getWidth(), panel.getHeight());
				frame.setLocationRelativeTo((JFrame) SwingUtilities.getWindowAncestor(this));
				frame.setVisible(true);
			}
		}
	}

	private void reloadTable() {
		tfTimMaHD.setText("");
		new PlaceHolder(tfTimMaHD, new Color(192, 192, 192), Color.BLACK,
				"Nhập mã hóa đơn; mã, họ tên của nhân viên hoặc khách hàng", false, this.getFont().toString(), 19);
		decorator.reload();
		pnlMain.remove(panel_timkiem);
		pnlMain.add(panel_phantrang);
		pnlMain.repaint();
		pnlMain.revalidate();
		table.clearSelection();
	}

	/*
	 * private void count() { double tongtien = 0; DecimalFormat df = new
	 * DecimalFormat("###,###,###.#"); for (HoaDon hd : dshd) { tongtien +=
	 * hd.tinhTongTien(); } lblSumTongTien.setText(df.format(tongtien)); }
	 */

	private void tim() {
		String keyword = tfTimMaHD.getText().toUpperCase();
		DataProvider<Khachhang> dataTimkiem = new DataProvider<Khachhang>() {

			@Override
			public int getTotalRowCount() {
				return dao.getSizeTimkiem(keyword);
			}

			@Override
			public void addDataToTable(int startIndex, int endIndex) {
				List<Hoadon> list = dao.timkiem(startIndex, endIndex, keyword);
				model.setDshd(list);
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
			JOptionPane.showMessageDialog(this, "Không tìm thấy hóa đơn nào", "Tìm hóa đơn",
					JOptionPane.INFORMATION_MESSAGE);
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

	public Nhanvien getNv() {
		return nv;
	}

	public void setNv(Nhanvien nv) {
		this.nv = nv;
	}
}
