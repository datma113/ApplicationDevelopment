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
import java.text.ParseException;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import com.placeholder.PlaceHolder;

import custom.MyJButton;
import custom.MyJLabel;
import custom.MyJScrollPane;
import custom.MyJTextField;
import custom.MyTableCellRenderSanpham;
import dao.DaoLoaisanpham;
import dao.DaoNhacungcap;
import dao.DaoSanpham;
import dao.ExportSanphamToExcel;
import entity.Loaisanpham;
import entity.Nhacungcap;
import entity.Sanpham;
import model.ModelSanpham;
import panel.PanelImportSanpham;
import phantrang.DataProvider;
import phantrang.Decorator;

public class PanelQuanlySanpham extends JPanel implements ActionListener, MouseListener {

	private static final long serialVersionUID = 1L;
	private static final int vertical_always = JScrollPane.VERTICAL_SCROLLBAR_ALWAYS;
	private static final int horizontal_always = JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS;

	private JTable table;
	private DaoSanpham dao;
	private DaoLoaisanpham dao_lsp;
	private DaoNhacungcap dao_ncc;
	private ModelSanpham model;

	private JTextArea tfMoTa;
	private MyJTextField tfMaSp;
	private MyJTextField tfDonvitinh;
	private MyJTextField tfTenSp;
	private MyJTextField tfThuonghieu;
	private MyJTextField tfMausac;
	private MyJTextField tfKichthuoc;
	private MyJTextField tfKhoiluong;
	private MyJTextField tfXuatxu;
	private MyJTextField tfTimMa;
	private JTextField tfPath;

	private JSpinner tfThue;
	private JSpinner tfTgbh;
	private JSpinner tfGiamua;
	private JSpinner tfGiaban;
	private JSpinner tfSoluongton;
	private JSpinner tfGiaban1;
	private JSpinner tfGiaban2;
	private JSpinner tfSoluongton1;
	private JSpinner tfSoluongton2;
	private JSpinner tfThue1;
	private JSpinner tfThue2;

	private MyJButton btnXoa;
	private MyJButton btnQLLSP;
	private MyJButton btnQLNCC;
	private MyJButton btnXoatrang;
	private MyJButton btnLuu;
	private MyJButton btnThem;
	private MyJButton btnImport;

	private JPanel pnlMain;
	private JPanel panel_timkiem;
	private JPanel panel_phantrang;

	private MyJLabel lblHinh;
	private MyJLabel lblNgayNhap;
	private MyJLabel lblTim;
	private MyJLabel lblThemLoaiSp;
	private MyJLabel lblThemNcc;
	private MyJLabel lblTimThue;
	private MyJLabel lblTimSoluongton;

	private JComboBox<String> cboLoai;
	private JComboBox<String> cboNhacc;
	private JComboBox<String> cboTimLoai;
	private JComboBox<String> cboTimNhacc;
	private JCheckBox chckbxNewCheckBox;
	private List<Loaisanpham> loaiSps;
	private List<Nhacungcap> nhaCungCaps;
	private Decorator<Sanpham> decorator;

	public PanelQuanlySanpham() {
		setSize(1680, 1021);
		setLayout(null);

		setBackground(Color.WHITE);

		/**
		 * panel
		 */
		panel_timkiem = new JPanel(null);

		pnlMain = new JPanel(null);
		pnlMain.setLocation(10, 29);
		pnlMain.setSize(1062, 724);
		pnlMain.setBorder(BorderFactory.createEtchedBorder());

		JPanel panelRight = new JPanel(null);
		panelRight.setBounds(1082, 29, 568, 969);
		panelRight.setBorder(BorderFactory.createEtchedBorder());
		add(panelRight);

		JPanel pnlTimKiem = new JPanel(null);
		pnlTimKiem.setBounds(10, 764, 1062, 168);
		pnlTimKiem.setBorder(BorderFactory.createEtchedBorder());
		add(pnlTimKiem);

		JPanel pnlButton = new JPanel(null);
		add(pnlButton);
		pnlButton.setBounds(10, 943, 1062, 55);
		pnlButton.setBorder(BorderFactory.createEtchedBorder());

		/**
		 * button
		 */

		btnXoa = new MyJButton("Xóa");
		btnXoa.setBounds(305, 11, 170, 35);
		btnXoa.setMnemonic('x');
		btnXoa.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnXoa.setIcon(new ImageIcon("img\\delete.png"));
		btnXoa.setEnabled(false);
//		pnlButton.add(btnXoa);

		btnQLLSP = new MyJButton("QL loại SP");
		btnQLLSP.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnQLLSP.setIcon(new ImageIcon("img/loaisp.png"));
		btnQLLSP.setBounds(540, 9, 140, 35);
		pnlButton.add(btnQLLSP);

		btnQLNCC = new MyJButton("QL nhà cung cấp");
		btnQLNCC.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnQLNCC.setIcon(new ImageIcon("img/ncc.png"));
		btnQLNCC.setBounds(370, 9, 160, 35);
		pnlButton.add(btnQLNCC);

		btnXoatrang = new MyJButton("Xóa Trắng");
		btnXoatrang.setMnemonic('x');
		btnXoatrang.setBounds(692, 9, 140, 35);
		btnXoatrang.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnXoatrang.setIcon(new ImageIcon("img/refresh.png"));
		pnlButton.add(btnXoatrang);

		btnLuu = new MyJButton("Lưu");
		btnLuu.setBounds(842, 9, 100, 35);
		pnlButton.add(btnLuu);
		btnLuu.setMnemonic('l');
		btnLuu.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnLuu.setIcon(new ImageIcon("img\\save.png"));

		btnThem = new MyJButton("Thêm");
		btnThem.setBounds(952, 9, 100, 35);
		pnlButton.add(btnThem);
		btnThem.setMnemonic('t');
		btnThem.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnThem.setIcon(new ImageIcon("img\\them.png"));

		btnImport = new MyJButton("Nhập từ file Excel");
		btnImport.setMnemonic('i');
		btnImport.setBounds(190, 9, 170, 35);
		btnImport.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnImport.setToolTipText("Thêm danh sách sản phẩm từ file Excel");
		btnImport.setIcon(new ImageIcon("img\\excel.png"));
		pnlButton.add(btnImport);

		MyJButton btnExport = new MyJButton("Xuất ra file Excel");
		btnExport.setBounds(10, 9, 170, 35);
		pnlButton.add(btnExport);
		btnExport.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnExport.setMnemonic('e');
		btnExport.setIcon(new ImageIcon("img/excel.png"));
		btnExport.setToolTipText("Xuất danh sách sản phẩm trên bảng ra file excel");
		btnExport.addActionListener(e -> {
			List<Sanpham> dssp = model.getDssp();
			if (dssp.isEmpty()) {
				JOptionPane.showMessageDialog(this, "Bảng dữ liệu trống.", "Xuất File Excel",
						JOptionPane.ERROR_MESSAGE);
			} else {
				JFileChooser fileChooser = new JFileChooser("");
				fileChooser.setDialogTitle("Chọn nơi lưu file và đặt tên");
				panelRight.add(fileChooser);
				int returnVal = fileChooser.showOpenDialog(this);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					java.io.File file = fileChooser.getSelectedFile();
					String excelFilePath = file.getPath();
					if (ExportSanphamToExcel.writeExcel(dssp, excelFilePath)) {
						JOptionPane.showMessageDialog(this, "Ghi xuống file thành công", "Xuất File Excel",
								JOptionPane.INFORMATION_MESSAGE);
					} else {
						JOptionPane.showMessageDialog(this, "Ghi xuống file không thành công", "Xuất File Excel",
								JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});

		btnThem.addActionListener(this);
		btnLuu.addActionListener(this);
		btnImport.addActionListener(e -> {
			final JDialog frame = new JDialog((JFrame) SwingUtilities.getWindowAncestor(this),
					"Nhập dữ liệu sản phẩm từ file Excel", true);
			try {
				frame.setIconImage(ImageIO.read(new File("img/code1.png")));
			} catch (IOException exception) {
			}
			PanelImportSanpham importSanpham = new PanelImportSanpham();
			frame.getContentPane().add(importSanpham);
			frame.setSize(importSanpham.getWidth(), importSanpham.getHeight());
			frame.setLocationRelativeTo((JFrame) SwingUtilities.getWindowAncestor(this));
			frame.setVisible(true);
		});

		/**
		 * label
		 */

		MyJLabel lblTitle = new MyJLabel("QUẢN LÝ SẢN PHẨM");
		lblTitle.setForeground(Color.RED);
		lblTitle.setFont(new Font("Serif", Font.BOLD, 20));
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitle.setBounds(10, 0, 1640, 30);
		add(lblTitle);

		MyJLabel lblMa = new MyJLabel("Mã SP (*)");
		lblMa.setBounds(10, 23, 149, 25);
		panelRight.add(lblMa);

		MyJLabel lblDonGia = new MyJLabel("Giá mua (*)");
		lblDonGia.setBounds(10, 127, 138, 25);
		panelRight.add(lblDonGia);

		MyJLabel lblDvTinh = new MyJLabel("Đơn vị tính (*)");
		lblDvTinh.setBounds(323, 338, 105, 25);
		panelRight.add(lblDvTinh);

		MyJLabel lblMoTa = new MyJLabel("Mô tả");
		lblMoTa.setBounds(10, 521, 111, 25);
		panelRight.add(lblMoTa);

		MyJLabel lblTen = new MyJLabel("Tên SP (*)");
		lblTen.setBounds(10, 74, 160, 25);
		panelRight.add(lblTen);

		MyJLabel lblLoaiSp = new MyJLabel("Loại SP (*)");
		lblLoaiSp.setBounds(10, 179, 138, 25);
		panelRight.add(lblLoaiSp);

		MyJLabel lblNhacc = new MyJLabel("Nhà cung cấp");
		lblNhacc.setBounds(10, 229, 138, 25);
		panelRight.add(lblNhacc);

		MyJLabel lblGiaBan = new MyJLabel("Thuế (%) (*)");
		lblGiaBan.setBounds(405, 485, 125, 25);
		panelRight.add(lblGiaBan);

		MyJLabel lblSLTon = new MyJLabel("Số lượng tồn (*)");
		lblSLTon.setBounds(209, 485, 125, 25);
		panelRight.add(lblSLTon);

		MyJLabel lblTGBH = new MyJLabel("Thời gian BH");
		lblTGBH.setBounds(10, 485, 111, 25);
		panelRight.add(lblTGBH);

		MyJLabel lblVnd = new MyJLabel("vnđ");
		lblVnd.setHorizontalAlignment(SwingConstants.RIGHT);
		lblVnd.setBounds(513, 128, 45, 23);
		panelRight.add(lblVnd);

		MyJLabel lblThang = new MyJLabel("t");
		lblThang.setBounds(183, 485, 45, 25);
		panelRight.add(lblThang);

		MyJLabel lblMausac = new MyJLabel("Màu sắc");
		lblMausac.setBounds(323, 287, 105, 25);
		panelRight.add(lblMausac);

		MyJLabel lblThuongHieu = new MyJLabel("Thương hiệu");
		lblThuongHieu.setBounds(10, 285, 138, 25);
		panelRight.add(lblThuongHieu);

		MyJLabel lblKichThuoc = new MyJLabel("Kích thước");
		lblKichThuoc.setBounds(10, 338, 138, 25);
		panelRight.add(lblKichThuoc);

		MyJLabel lblKhoiLuong = new MyJLabel("Khối lượng");
		lblKhoiLuong.setBounds(10, 388, 138, 25);
		panelRight.add(lblKhoiLuong);

		MyJLabel lblXuatXu = new MyJLabel("Xuất xứ");
		lblXuatXu.setBounds(10, 438, 138, 25);
		panelRight.add(lblXuatXu);

		MyJLabel lblGiaban = new MyJLabel("Giá bán (*)");
		lblGiaban.setBounds(288, 127, 90, 25);
		panelRight.add(lblGiaban);

		MyJLabel lblTimNhacc = new MyJLabel("Tìm theo nhà cung cấp");
		lblTimNhacc.setBounds(10, 112, 191, 25);
		pnlTimKiem.add(lblTimNhacc);

		MyJLabel lblTimLoaisp = new MyJLabel("Tìm theo loại sản phẩm");
		lblTimLoaisp.setBounds(10, 71, 191, 25);
		pnlTimKiem.add(lblTimLoaisp);

		MyJLabel lblThuT = new MyJLabel("Thuế từ");
		lblThuT.setBounds(671, 71, 148, 23);
		pnlTimKiem.add(lblThuT);

		MyJLabel lblSLngTn = new MyJLabel("Số lượng tồn từ");
		lblSLngTn.setBounds(671, 114, 148, 23);
		pnlTimKiem.add(lblSLngTn);

		MyJLabel lblTimMa = new MyJLabel("Tìm theo từ khóa");
		lblTimMa.setBounds(10, 25, 191, 23);
		pnlTimKiem.add(lblTimMa);

		MyJLabel lblTimGiaban = new MyJLabel("Giá bán từ");
		lblTimGiaban.setBounds(671, 27, 122, 23);
		pnlTimKiem.add(lblTimGiaban);

		MyJLabel lblNewLabel = new MyJLabel("Ngày nhập");
		lblNewLabel.setBounds(323, 388, 105, 25);
		panelRight.add(lblNewLabel);

		lblTim = new MyJLabel("");
		lblTim.setIcon(new ImageIcon("img\\find.png"));
		lblTim.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		lblTim.setBounds(1019, 25, 24, 24);
		lblTim.setToolTipText("Tìm sản phẩm theo giá bán");
		pnlTimKiem.add(lblTim);

		lblThemLoaiSp = new MyJLabel("");
		lblThemLoaiSp.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		lblThemLoaiSp.setIcon(new ImageIcon("img\\them.png"));
		lblThemLoaiSp.setBounds(533, 179, 24, 24);
		lblThemLoaiSp.setToolTipText("Thêm một loại sản phẩm mới");
		panelRight.add(lblThemLoaiSp);

		lblThemNcc = new MyJLabel("");
		lblThemNcc.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		lblThemNcc.setIcon(new ImageIcon("img\\them.png"));
		lblThemNcc.setBounds(533, 229, 24, 24);
		lblThemNcc.setToolTipText("Thêm mới một nhà cung cấp");
		panelRight.add(lblThemNcc);

		lblHinh = new MyJLabel("");
		lblHinh.setBounds(10, 698, 548, 260);
		lblHinh.setBorder(BorderFactory.createLineBorder(Color.RED, 1));
		lblHinh.setHorizontalAlignment(SwingConstants.CENTER);
		panelRight.add(lblHinh);

		lblNgayNhap = new MyJLabel("");
		lblNgayNhap.setHorizontalAlignment(SwingConstants.CENTER);
		lblNgayNhap.setBounds(398, 385, 160, 25);
		panelRight.add(lblNgayNhap);

		lblTimThue = new MyJLabel("");
		lblTimThue.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		lblTimThue.setIcon(new ImageIcon("img\\find.png"));
		lblTimThue.setBounds(1019, 70, 24, 24);
		lblTimThue.setToolTipText("Tìm sản phẩm theo thuế");
		pnlTimKiem.add(lblTimThue);

		lblTimSoluongton = new MyJLabel("");
		lblTimSoluongton.setIcon(new ImageIcon("img\\find.png"));
		lblTimSoluongton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		lblTimSoluongton.setBounds(1019, 112, 24, 24);
		lblTimSoluongton.setToolTipText("Tìm sản phẩm theo số lượng tồn");
		pnlTimKiem.add(lblTimSoluongton);

		MyJButton btnChonhinh = new MyJButton("Chọn hình");
		btnChonhinh.setBounds(418, 652, 140, 35);
		btnChonhinh.setIcon(new ImageIcon("img\\image.png"));
		btnChonhinh.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnChonhinh.setMnemonic('h');
		btnChonhinh.setToolTipText("Chọn hình ảnh cho sản phẩm");
		panelRight.add(btnChonhinh);
		btnChonhinh.addActionListener(e -> {
			JFileChooser fileChooser = new JFileChooser("");
			fileChooser.setDialogTitle("Chọn hình ảnh cho sản phẩm");
			int returnVal = fileChooser.showOpenDialog(this);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				java.io.File file = fileChooser.getSelectedFile();
				tfPath.setText(file.getPath());
				try {
					ImageIcon bg_img = new ImageIcon(file.getPath());
//					Image img = bg_img.getImage();
//					Image temp_img = img.getScaledInstance(lblHinh.getWidth(), lblHinh.getHeight(), Image.SCALE_SMOOTH);
//					bg_img = new ImageIcon(temp_img);
					lblHinh.setIcon(bg_img);
				} catch (Exception e2) {
				}
				lblHinh.repaint();
				lblHinh.revalidate();
			}
		});

		/**
		 * text field
		 */

		tfMaSp = new MyJTextField(20);
		tfMaSp.setBounds(123, 21, 186, 25);
		tfMaSp.setEditable(false);
		panelRight.add(tfMaSp);

		tfMoTa = new JTextArea();
		tfMoTa.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
		Font fontMota = tfMoTa.getFont();
		tfMoTa.setFont(new Font(fontMota.getName(), fontMota.getStyle(), fontMota.getSize() + 5));

		tfDonvitinh = new MyJTextField(20);
		tfDonvitinh.setBounds(433, 338, 125, 25);
		panelRight.add(tfDonvitinh);

		tfTenSp = new MyJTextField(20);
		tfTenSp.setBounds(123, 74, 435, 25);
		panelRight.add(tfTenSp);

		tfTimMa = new MyJTextField("");
		tfTimMa.setBounds(201, 25, 460, 25);
		pnlTimKiem.add(tfTimMa);

		tfThuonghieu = new MyJTextField(20);
		tfThuonghieu.setBounds(123, 285, 190, 25);
		panelRight.add(tfThuonghieu);

		tfMausac = new MyJTextField(20);
		tfMausac.setBounds(433, 285, 125, 25);
		panelRight.add(tfMausac);

		tfKichthuoc = new MyJTextField(20);
		tfKichthuoc.setBounds(123, 336, 190, 25);
		panelRight.add(tfKichthuoc);

		tfKhoiluong = new MyJTextField(20);
		tfKhoiluong.setBounds(123, 386, 190, 25);
		panelRight.add(tfKhoiluong);

		tfXuatxu = new MyJTextField(20);
		tfXuatxu.setBounds(123, 436, 435, 25);
		panelRight.add(tfXuatxu);

		tfSoluongton = new JSpinner();
		tfSoluongton.setModel(new SpinnerNumberModel(Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(10000),
				Integer.valueOf(5)));
		tfSoluongton.setBounds(335, 485, 55, 25);
		panelRight.add(tfSoluongton);

		tfThue = new JSpinner();
		tfThue.setModel(new SpinnerNumberModel(Double.valueOf(0.d), Double.valueOf(0.d), Double.valueOf(100.0d),
				Double.valueOf(0.01d)));
		tfThue.setBounds(498, 485, 55, 25);
		panelRight.add(tfThue);

		tfTgbh = new JSpinner();
		tfTgbh.setModel(new SpinnerNumberModel(Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(100),
				Integer.valueOf(3)));
		tfTgbh.setBounds(123, 485, 50, 25);
		panelRight.add(tfTgbh);

		tfGiamua = new JSpinner();
		tfGiamua.setModel(new SpinnerNumberModel(Double.valueOf(0.d), Double.valueOf(0.d), Double.valueOf(1000000000),
				Double.valueOf(10000)));
		tfGiamua.setBounds(123, 127, 155, 25);
		panelRight.add(tfGiamua);

		tfGiaban = new JSpinner();
		tfGiaban.setModel(new SpinnerNumberModel(Double.valueOf(0.d), Double.valueOf(0.d), Double.valueOf(1000000000),
				Double.valueOf(10000)));
		tfGiaban.setBounds(368, 128, 155, 25);
		panelRight.add(tfGiaban);

		tfPath = new JTextField();
		tfPath.setEditable(false);
		tfPath.setBounds(10, 654, 398, 33);
		panelRight.add(tfPath);

		tfGiaban1 = new JSpinner();
		tfGiaban1.setModel(new SpinnerNumberModel(Double.valueOf(0), Double.valueOf(0), Double.valueOf(1000000000),
				Double.valueOf(10000)));
		tfGiaban1.setBounds(799, 25, 100, 25);
		pnlTimKiem.add(tfGiaban1);

		tfGiaban2 = new JSpinner(new SpinnerNumberModel(Double.valueOf(0), Double.valueOf(0),
				Double.valueOf(1000000000), Double.valueOf(10000)));
		tfGiaban2.setBounds(909, 25, 100, 25);
		pnlTimKiem.add(tfGiaban2);

		tfThue1 = new JSpinner();
		tfThue1.setModel(new SpinnerNumberModel(Float.valueOf(0f), Float.valueOf(0f), Float.valueOf(100f),
				Float.valueOf(0.01f)));
		tfThue1.setBounds(799, 69, 100, 25);
		pnlTimKiem.add(tfThue1);

		tfThue2 = new JSpinner(new SpinnerNumberModel(Float.valueOf(0f), Float.valueOf(0f), Float.valueOf(100f),
				Float.valueOf(0.01f)));
		tfThue2.setBounds(909, 69, 100, 25);
		pnlTimKiem.add(tfThue2);

		tfSoluongton1 = new JSpinner(new SpinnerNumberModel(Integer.valueOf(0), Integer.valueOf(0),
				Integer.valueOf(10000), Integer.valueOf(5)));
		tfSoluongton1.setBounds(799, 112, 100, 25);
		pnlTimKiem.add(tfSoluongton1);

		tfSoluongton2 = new JSpinner(new SpinnerNumberModel(Integer.valueOf(0), Integer.valueOf(0),
				Integer.valueOf(10000), Integer.valueOf(5)));
		tfSoluongton2.setBounds(909, 112, 100, 25);
		pnlTimKiem.add(tfSoluongton2);

		/**
		 * model table
		 */

		model = new ModelSanpham();
		table = new JTable(model);
		table.setRowHeight(35);
		table.setAutoCreateRowSorter(true);
		table.getTableHeader().setBackground(new Color(255, 208, 120));
		table.getTableHeader().setPreferredSize(new Dimension(table.getTableHeader().getWidth(), 30));
		Font f3 = table.getTableHeader().getFont();
		table.getTableHeader().setFont(new Font(f3.getName(), Font.BOLD, f3.getSize() + 2));

		MyTableCellRenderSanpham renderTableSanpham = new MyTableCellRenderSanpham();
		table.setDefaultRenderer(Double.class, renderTableSanpham);
		table.setDefaultRenderer(String.class, renderTableSanpham);
		table.setDefaultRenderer(Integer.class, renderTableSanpham);

		dao = new DaoSanpham();
		dao_lsp = new DaoLoaisanpham();
		dao_ncc = new DaoNhacungcap();

		/**
		 * scroll
		 */

		MyJScrollPane scrollSP = new MyJScrollPane(table, vertical_always, horizontal_always);
		scrollSP.setBounds(10, 15, 1042, 636);
		pnlMain.add(scrollSP);
		add(pnlMain);

		MyJScrollPane scrollPane = new MyJScrollPane(tfMoTa);
		scrollPane.setBounds(10, 557, 548, 86);
		scrollPane.setBorder(BorderFactory.createLineBorder(Color.BLUE, 1));
		panelRight.add(scrollPane);

		/**
		 * combobox
		 */

		cboLoai = new JComboBox<String>();
		cboLoai.setBounds(123, 179, 400, 25);
		panelRight.add(cboLoai);

		cboNhacc = new JComboBox<String>();
		cboNhacc.setBounds(123, 230, 400, 25);
		panelRight.add(cboNhacc);

		cboTimLoai = new JComboBox<String>();
		cboTimLoai.setBounds(201, 69, 460, 25);
		pnlTimKiem.add(cboTimLoai);

		cboTimNhacc = new JComboBox<String>();
		cboTimNhacc.setBounds(201, 112, 460, 25);
		pnlTimKiem.add(cboTimNhacc);

		chckbxNewCheckBox = new JCheckBox("Ngừng kinh doanh");
		chckbxNewCheckBox.setForeground(Color.RED);
		chckbxNewCheckBox.setFont(new Font("Tahoma", Font.BOLD, 16));
		chckbxNewCheckBox.setBounds(341, 21, 199, 27);
		chckbxNewCheckBox.setSelected(false);
		panelRight.add(chckbxNewCheckBox);

		reloadComBoBoxLoaisp();
		reloadComBoBoxNhacungcap();

		/**
		 * panel phan trang
		 */
		DataProvider<Sanpham> dataProvider = new DataProvider<Sanpham>() {

			@Override
			public int getTotalRowCount() {
				return dao.getSize();
			}

			@Override
			public void addDataToTable(int startIndex, int endIndex) {
				List<Sanpham> list = dao.getDsSp(startIndex, endIndex);
				model.setDssp(list);
				model.fireTableDataChanged();
			}
		};
		decorator = Decorator.decorate(dataProvider, new int[] { 10, 25, 50, 75, 100, 200, 500 }, 25);
		panel_phantrang = decorator.getContentPanel();
//		panel_phantrang = new JPanel();
		panel_phantrang.setBounds(10, 663, 1042, 50);
		panel_phantrang.setBorder(BorderFactory.createEtchedBorder());
		pnlMain.add(panel_phantrang);

		/**
		 * su kien
		 */
		cboTimLoai.addActionListener(this);
		cboTimNhacc.addActionListener(this);
		cboLoai.addActionListener(this);
		cboNhacc.addActionListener(this);

		tfTenSp.addActionListener(this);
		tfThuonghieu.addActionListener(this);
		tfMausac.addActionListener(this);
		tfKichthuoc.addActionListener(this);
		tfDonvitinh.addActionListener(this);
		tfKhoiluong.addActionListener(this);
		tfXuatxu.addActionListener(this);
		tfTimMa.addActionListener(this);

		table.addMouseListener(this);
		lblTim.addMouseListener(this);
		lblTimThue.addMouseListener(this);
		lblTimSoluongton.addMouseListener(this);
		lblThemLoaiSp.addMouseListener(this);
		lblThemNcc.addMouseListener(this);
		btnXoa.addActionListener(this);
		btnQLLSP.addActionListener(this);
		btnQLNCC.addActionListener(this);
		btnXoatrang.addActionListener(this);

		reloadTable();
		resizeColumnsSanPham();
		xoaTrang();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource();
		if (o.equals(btnThem)) {
			them();
		} else if (o.equals(btnLuu)) {
			luu();
		} else if (o.equals(btnXoatrang)) {
			pnlMain.remove(panel_timkiem);
			pnlMain.add(panel_phantrang);
			pnlMain.repaint();
			pnlMain.revalidate();
			reloadTable();
			xoaTrang();
		} else if (o.equals(btnXoa)) {
			xoa();
		} else if (o.equals(btnQLLSP)) {
			final JDialog frame = new JDialog((JFrame) SwingUtilities.getWindowAncestor(this), "Quản lý loại sản phẩm",
					true);
			try {
				frame.setIconImage(ImageIO.read(new File("img/code1.png")));
			} catch (IOException exception) {
			}
			JPanel panel = new PanelQuanlyLoaisanpham();
			frame.getContentPane().add(panel);
			frame.setSize(panel.getWidth(), panel.getHeight());
			frame.setLocationRelativeTo((JFrame) SwingUtilities.getWindowAncestor(this));
			frame.setVisible(true);
		} else if (o.equals(btnQLNCC)) {
			final JDialog frame = new JDialog((JFrame) SwingUtilities.getWindowAncestor(this), "Quản lý nhà cung cấp",
					true);
			try {
				frame.setIconImage(ImageIO.read(new File("img/code1.png")));
			} catch (IOException exception) {
			}
			JPanel panel = new PanelQuanlyNhacungcap();
			frame.getContentPane().add(panel);
			frame.setSize(panel.getWidth(), panel.getHeight());
			frame.setLocationRelativeTo((JFrame) SwingUtilities.getWindowAncestor(this));
			frame.setVisible(true);
		} else if (o.equals(tfTimMa)) {
			tim();
		} else if (o.equals(cboTimLoai) && cboTimLoai.getItemCount() > 0) {
			timTheoLoaiSp();
		} else if (o.equals(cboTimNhacc) && cboTimNhacc.getItemCount() > 0) {
			timTheoNhacungcap();
		} else if (o.equals(tfTenSp)) {
			cboLoai.setPopupVisible(true);
		} else if (o.equals(cboLoai)) {
			cboNhacc.setPopupVisible(true);
		} else if (o.equals(cboNhacc)) {
			tfThuonghieu.requestFocus();
		} else if (o.equals(tfThuonghieu)) {
			tfMausac.requestFocus();
		} else if (o.equals(tfMausac)) {
			tfKichthuoc.requestFocus();
		} else if (o.equals(tfKichthuoc)) {
			tfDonvitinh.requestFocus();
		} else if (o.equals(tfDonvitinh)) {
			tfKhoiluong.requestFocus();
		} else if (o.equals(tfKhoiluong)) {
			tfXuatxu.requestFocus();
		} else if (o.equals(tfXuatxu)) {
			tfMoTa.requestFocus();
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		Object o = e.getSource();
		if (o.equals(table)) {
			fillTextField();
		} else if (o.equals(lblTim)) {
			timTheoGiaban();
		} else if (o.equals(lblTimThue)) {
			timTheoThue();
		} else if (o.equals(lblTimSoluongton)) {
			timTheoSoluongton();
		} else if (o.equals(lblThemLoaiSp)) {
			themLoaisanpham();
		} else if (o.equals(lblThemNcc)) {
			themNhacungcap();
		}
	}

	private void themNhacungcap() {
		String maNhacungcap1 = dao_ncc.getMaxMaNCC();
		final JDialog frame = new JDialog((JFrame) SwingUtilities.getWindowAncestor(this), "Quản lý nhà cung cấp",
				true);
		try {
			frame.setIconImage(ImageIO.read(new File("img/code1.png")));
		} catch (IOException exception) {
		}
		JPanel panel = new PanelQuanlyNhacungcap();
		frame.getContentPane().add(panel);
		frame.setSize(panel.getWidth(), panel.getHeight());
		frame.setLocationRelativeTo((JFrame) SwingUtilities.getWindowAncestor(this));
		frame.setVisible(true);
		String maNhacungcap2 = dao_ncc.getMaxMaNCC();
		if (!maNhacungcap1.equalsIgnoreCase(maNhacungcap2)) {
			Nhacungcap ncc = dao_ncc.getByID(maNhacungcap2);
			reloadComBoBoxNhacungcap();
			cboNhacc.setSelectedItem(ncc.getTennhacungcap());
		}
	}

	private void themLoaisanpham() {
		String maLoai = dao_lsp.getMaxMaLoaiSp();
		final JDialog frame = new JDialog((JFrame) SwingUtilities.getWindowAncestor(this), "Quản lý loại sản phẩm",
				true);
		try {
			frame.setIconImage(ImageIO.read(new File("img/code1.png")));
		} catch (IOException exception) {
		}
		JPanel panel = new PanelQuanlyLoaisanpham();
		frame.getContentPane().add(panel);
		frame.setSize(panel.getWidth(), panel.getHeight());
		frame.setLocationRelativeTo((JFrame) SwingUtilities.getWindowAncestor(this));
		frame.setVisible(true);
		String maLoai2 = dao_lsp.getMaxMaLoaiSp();
		if (!maLoai.equalsIgnoreCase(maLoai2)) {
			Loaisanpham lsp = dao_lsp.getByID(maLoai2);
			reloadComBoBoxLoaisp();
			cboLoai.setSelectedItem(lsp.getTenloai());
		}

	}

	/**
	 * nạp lại dữ liệu cho combobox nhà cung cấp
	 */
	private void reloadComBoBoxNhacungcap() {
		cboNhacc.removeAllItems();
		cboTimNhacc.removeAllItems();
		nhaCungCaps = dao_ncc.getAll();
		for (Nhacungcap ncc : nhaCungCaps) {
			cboNhacc.addItem(ncc.getTennhacungcap());
			cboTimNhacc.addItem(ncc.getTennhacungcap());
		}
	}

	/**
	 * nạp lại dữ liệu cho combobox loại sản phẩm
	 */
	private void reloadComBoBoxLoaisp() {
		cboLoai.removeAllItems();
		cboTimLoai.removeAllItems();
		loaiSps = dao_lsp.getAll();
		for (Loaisanpham loaiSp : loaiSps) {
			cboLoai.addItem(loaiSp.getTenloai());
			cboTimLoai.addItem(loaiSp.getTenloai());
		}
	}

	/**
	 * tìm sản phẩm theo giá bán
	 */
	private void timTheoGiaban() {
		try {
			tfGiaban1.commitEdit();
			tfGiaban2.commitEdit();
		} catch (ParseException e) {
			JOptionPane.showMessageDialog(this, "Giá bán phải là một con số", "Tìm sản phẩm",
					JOptionPane.ERROR_MESSAGE);

		}
		double from = Double.parseDouble(tfGiaban1.getValue().toString());
		double to = Double.parseDouble(tfGiaban2.getValue().toString());
		DataProvider<Sanpham> dataTimkiem = new DataProvider<Sanpham>() {

			@Override
			public int getTotalRowCount() {
				return dao.getSizeTimkiemTheoGiaban(from, to);
			}

			@Override
			public void addDataToTable(int startIndex, int endIndex) {
				List<Sanpham> list = dao.timkiemTheoGiaban(startIndex, endIndex, from, to);
				model.setDssp(list);
				model.fireTableDataChanged();
			}
		};
		pnlMain.remove(panel_timkiem);
		Decorator<Sanpham> decorator2 = Decorator.decorate(dataTimkiem, new int[] { 10, 25, 50, 75, 100, 200, 500 },
				25);
		panel_timkiem = decorator2.getContentPanel();
		panel_timkiem.setBounds(10, 663, 1042, 50);
		panel_timkiem.setBorder(BorderFactory.createEtchedBorder());
		pnlMain.remove(panel_phantrang);
		pnlMain.add(panel_timkiem);
		pnlMain.repaint();
		pnlMain.revalidate();
		if (model.getRowCount() == 0)
			JOptionPane.showMessageDialog(this, "Không tìm thấy sản phẩm nào");
	}

	/**
	 * tìm sản phẩm theo tên nhà cung cấp
	 */
	private void timTheoNhacungcap() {
		cboTimNhacc.setPopupVisible(false);
		String keyword = cboTimNhacc.getSelectedItem().toString();
		DataProvider<Sanpham> dataTimkiem = new DataProvider<Sanpham>() {

			@Override
			public int getTotalRowCount() {
				return dao.getSizeTimkiemTheoNhacc(keyword);
			}

			@Override
			public void addDataToTable(int startIndex, int endIndex) {
				List<Sanpham> list = dao.timkiemTheoNhacc(startIndex, endIndex, keyword);
				model.setDssp(list);
				model.fireTableDataChanged();
			}
		};
		pnlMain.remove(panel_timkiem);
		Decorator<Sanpham> decorator2 = Decorator.decorate(dataTimkiem, new int[] { 10, 25, 50, 75, 100, 200, 500 },
				25);
		panel_timkiem = decorator2.getContentPanel();
		panel_timkiem.setBounds(10, 663, 1042, 50);
		panel_timkiem.setBorder(BorderFactory.createEtchedBorder());
		pnlMain.remove(panel_phantrang);
		pnlMain.add(panel_timkiem);
		pnlMain.repaint();
		pnlMain.revalidate();
		if (model.getRowCount() == 0)
			JOptionPane.showMessageDialog(this, "Không tìm thấy sản phẩm nào", "Tìm sản phẩm",
					JOptionPane.INFORMATION_MESSAGE);
	}

	/**
	 * tìm sản phẩm theo tên của loại sản phẩm
	 */
	private void timTheoLoaiSp() {
		cboTimLoai.setPopupVisible(false);
		String keyword = cboTimLoai.getSelectedItem().toString();
		DataProvider<Sanpham> dataTimkiem = new DataProvider<Sanpham>() {

			@Override
			public int getTotalRowCount() {
				return dao.getSizeTimkiemTheoLoaiSp(keyword);
			}

			@Override
			public void addDataToTable(int startIndex, int endIndex) {
				List<Sanpham> list = dao.timkiemTheoLoaiSp(startIndex, endIndex, keyword);
				model.setDssp(list);
				model.fireTableDataChanged();
			}
		};
		pnlMain.remove(panel_timkiem);
		Decorator<Sanpham> decorator2 = Decorator.decorate(dataTimkiem, new int[] { 10, 25, 50, 75, 100, 200, 500 },
				25);
		panel_timkiem = decorator2.getContentPanel();
		panel_timkiem.setBounds(10, 663, 1042, 50);
		panel_timkiem.setBorder(BorderFactory.createEtchedBorder());
		pnlMain.remove(panel_phantrang);
		pnlMain.add(panel_timkiem);
		pnlMain.repaint();
		pnlMain.revalidate();
		if (model.getRowCount() == 0)
			JOptionPane.showMessageDialog(this, "Không tìm thấy sản phẩm nào", "Tìm sản phẩm",
					JOptionPane.INFORMATION_MESSAGE);
	}

	/**
	 * đưa dữ liệu từ bảng lên textfield khi click 1 dòng trên bảng
	 */
	private void fillTextField() {
		int index = table.getSelectedRow();
		if (index >= 0 && index < table.getRowCount()) {
			cboLoai.removeActionListener(this);
			cboNhacc.removeActionListener(this);
			Sanpham sp = dao.getById(table.getValueAt(index, 0).toString());
			tfMaSp.setText(sp.getMasanpham());
			tfDonvitinh.setText(sp.getDonvitinh());
			tfMoTa.setText(sp.getMota());
			tfTenSp.setText(sp.getTensanpham());
			cboLoai.setSelectedItem(sp.getLoaisanpham().getTenloai());
			cboNhacc.setSelectedItem(sp.getNhacungcap().getTennhacungcap());
			tfGiaban.setValue(sp.getGiaban());
			tfGiamua.setValue(sp.getGiamua());
			tfKhoiluong.setText(sp.getKhoiluong());
			tfKichthuoc.setText(sp.getKichthuoc());
			tfMausac.setText(sp.getMausac());
			tfSoluongton.setValue(sp.getSoluongton());
			tfTgbh.setValue(sp.getThoigianbaohanh());
			tfThue.setValue(sp.getThue());
			tfXuatxu.setText(sp.getXuatxu());
			tfThuonghieu.setText(sp.getThuonghieu());
			lblNgayNhap.setText("" + sp.getNgaynhap());
			try {
				if (sp.getHinhanh().trim().equals("")) {
					lblHinh.setIcon(new ImageIcon("img\\image_not_found.png"));
				} else {
					lblHinh.setIcon(new ImageIcon(sp.getHinhanh()));
				}
			} catch (Exception e) {
			}
			chckbxNewCheckBox.setSelected(sp.isNgungkinhdoanh());
			tfPath.setText(sp.getHinhanh());
			cboLoai.addActionListener(this);
			cboNhacc.addActionListener(this);
		}
	}

	private void reloadTable() {
		tfTimMa.setText("");
		tfGiaban1.setValue(0d);
		tfGiaban2.setValue(0d);
		tfThue1.setValue(0f);
		tfThue2.setValue(0f);
		tfSoluongton1.setValue(0);
		tfSoluongton2.setValue(0);
		new PlaceHolder(tfTimMa, new Color(192, 192, 192), Color.BLACK, "Nhập mã, tên, mô tả, màu sắc hoặc thương hiệu",
				false, this.getFont().toString(), 19);
		decorator.reload();
	}

	/**
	 * đặt lại kích thước cho các cột của bảng sản phẩm
	 */
	private void resizeColumnsSanPham() {
		table.getColumnModel().getColumn(0).setMinWidth(100);
		table.getColumnModel().getColumn(1).setMinWidth(350);
		table.getColumnModel().getColumn(2).setMinWidth(130);
		table.getColumnModel().getColumn(3).setMinWidth(130);
		table.getColumnModel().getColumn(4).setMinWidth(130);
		table.getColumnModel().getColumn(5).setMinWidth(100);
		table.getColumnModel().getColumn(6).setMinWidth(100);
		table.getColumnModel().getColumn(7).setMinWidth(100);
		table.getColumnModel().getColumn(8).setMinWidth(100);
		table.getColumnModel().getColumn(9).setMinWidth(250);
		table.getColumnModel().getColumn(10).setMinWidth(400);
		table.getColumnModel().getColumn(11).setMinWidth(100);
		table.getColumnModel().getColumn(12).setMinWidth(200);
		table.getColumnModel().getColumn(13).setMinWidth(100);
		table.getColumnModel().getColumn(14).setMinWidth(100);
		table.getColumnModel().getColumn(15).setMinWidth(300);
		table.getColumnModel().getColumn(16).setMinWidth(100);
		table.getColumnModel().getColumn(17).setMinWidth(100);
		table.getColumnModel().getColumn(18).setMinWidth(150);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
	}

	/*
	 * private void count() { int soluong = 0; for (SanPham sp : dssp) { soluong +=
	 * sp.getSoLuongTon(); } lblSumSLTon.setText(soluong + ""); }
	 */

	private void xoaTrang() {
		tfMaSp.setText(dao.getNextMaSP());
		tfDonvitinh.setText("");
		tfMoTa.setText("");
		tfTenSp.setText("");
		tfTenSp.requestFocus();
		cboLoai.setSelectedItem("");
		cboNhacc.setSelectedItem("");
		tfGiaban.setValue(0);
		tfGiamua.setValue(0);
		tfKhoiluong.setText("");
		tfKichthuoc.setText("");
		tfMausac.setText("");
		tfSoluongton.setValue(0);
		tfTgbh.setValue(0);
		tfThue.setValue(0);
		tfXuatxu.setText("");
		tfThuonghieu.setText("");
		lblNgayNhap.setText("" + LocalDate.now());
		lblHinh.setIcon(new ImageIcon());
		tfPath.setText("");
		chckbxNewCheckBox.setSelected(false);
		table.clearSelection();
		try {
			cboLoai.removeActionListener(this);
			cboNhacc.removeActionListener(this);
			cboLoai.setSelectedIndex(0);
			cboNhacc.setSelectedIndex(0);
			cboLoai.addActionListener(this);
			cboNhacc.addActionListener(this);
		} catch (Exception e) {
		}
	}

	/**
	 * thêm 1 sản phẩm mới
	 */
	private void them() {
		if (table.getSelectedRow() != -1 || tfMaSp.getText().trim().equals("")) {
			xoaTrang();
		} else if (validInputTextField()) {
			Sanpham sp = getDataFromTextField();
			if (sp != null) {
				sp.setNgaynhap(new java.sql.Date(new Date().getTime()));
				if (dao.themSanPham(sp)) {
					reloadTable();
					xoaTrang();
					JOptionPane.showMessageDialog(this, "Thêm thành công", "Thêm sản phẩm",
							JOptionPane.INFORMATION_MESSAGE);
				} else
					JOptionPane.showMessageDialog(this, "Thêm không thành công.", "Thêm sản phẩm",
							JOptionPane.ERROR_MESSAGE);
			} else {
				JOptionPane.showMessageDialog(this, "Thêm không thành công.", "Thêm sản phẩm",
						JOptionPane.ERROR_MESSAGE);
			}

		}
	}

	/**
	 * lấy thông tin 1 sản phẩm từ textfield
	 * 
	 * @return
	 */
	private Sanpham getDataFromTextField() {
		if (validInputTextField()) {
			Sanpham sp = new Sanpham();
			sp.setMasanpham(tfMaSp.getText().trim());
			sp.setDonvitinh(tfDonvitinh.getText().trim());
			sp.setMota(tfMoTa.getText().trim());
			sp.setTensanpham(tfTenSp.getText().trim());

			sp.setLoaisanpham(loaiSps.get(cboLoai.getSelectedIndex()));
			sp.setNhacungcap(nhaCungCaps.get(cboNhacc.getSelectedIndex()));

			sp.setGiaban(Double.parseDouble(tfGiaban.getValue().toString()));
			sp.setGiamua(Double.parseDouble(tfGiamua.getValue().toString()));
			sp.setKhoiluong(tfKhoiluong.getText().trim());
			sp.setKichthuoc(tfKichthuoc.getText().trim());
			sp.setMausac(tfMausac.getText().trim());
			sp.setSoluongton(Integer.parseInt(tfSoluongton.getValue().toString()));
			sp.setThoigianbaohanh(Integer.parseInt(tfTgbh.getValue().toString()));
			sp.setThue(Double.parseDouble(tfThue.getValue().toString()));
			sp.setXuatxu(tfXuatxu.getText().trim());
			sp.setThuonghieu(tfThuonghieu.getText().trim());
			sp.setNgaynhap(new java.sql.Date(new java.util.Date().getTime()));
			sp.setHinhanh(tfPath.getText());
			sp.setNgungkinhdoanh(chckbxNewCheckBox.isSelected());
			lblNgayNhap.setText("" + LocalDate.now());
			return sp;
		}
		return null;
	}

	private void xoa() {
		/*
		 * int index = table.getSelectedRow(); if (index >= 0 && index <
		 * table.getRowCount()) { int xoa = JOptionPane.showConfirmDialog(this,
		 * "Bạn có muốn xóa sản phẩm này không?", "Xóa", JOptionPane.WARNING_MESSAGE);
		 * if (xoa == JOptionPane.YES_OPTION) { Sanpham sanPham = new
		 * Sanpham(tfMaSp.getText().trim()); if (dao.xoaSanPham(sanPham)) {
		 * reloadTable(); xoaTrang(); JOptionPane.showMessageDialog(this,
		 * "Xóa thành công"); } else JOptionPane.showMessageDialog(this,
		 * "Xóa không thành công"); } } else { JOptionPane.showMessageDialog(this,
		 * "Chọn một sản phẩm để xóa!", "Xóa", JOptionPane.INFORMATION_MESSAGE); }
		 */
	}

	private void luu() {
		int index = table.getSelectedRow();
		if (index >= 0 && index < table.getRowCount() && validInputTextField()) {
			Sanpham s = getDataFromTextField();
			if (s != null) {
				Sanpham sp = dao.getById(table.getValueAt(index, 0).toString());
				s.setNgaynhap(sp.getNgaynhap());
				if (dao.capNhatSanPham(s)) {
					reloadTable();
					JOptionPane.showMessageDialog(this, "Cập nhật thành công", "Cập nhật sản phẩm",
							JOptionPane.INFORMATION_MESSAGE);
				} else
					JOptionPane.showMessageDialog(this, "Cập nhật không thành công", "Cập nhật sản phẩm",
							JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	/**
	 * tìm kiếm theo textsearch
	 */
	private void tim() {
		String s = "";
		if (!tfTimMa.getText().trim().equals(""))
			s = tfTimMa.getText().trim().toUpperCase();
		else {
			JOptionPane.showMessageDialog(this, "Nhập mã, tên, mô tả, màu sắc hoặc thương hiệu sản phẩm để tìm kiếm",
					"Tìm sản phẩm", JOptionPane.INFORMATION_MESSAGE);
			tfTimMa.requestFocus();
			return;
		}

		String keyword = s;
		DataProvider<Sanpham> dataTimkiem = new DataProvider<Sanpham>() {

			@Override
			public int getTotalRowCount() {
				return dao.getSizeTimkiem(keyword);
			}

			@Override
			public void addDataToTable(int startIndex, int endIndex) {
				List<Sanpham> list = dao.timkiem(startIndex, endIndex, keyword);
				model.setDssp(list);
				model.fireTableDataChanged();
			}
		};
		pnlMain.remove(panel_timkiem);
		Decorator<Sanpham> decorator2 = Decorator.decorate(dataTimkiem, new int[] { 10, 25, 50, 75, 100, 200, 500 },
				25);
		panel_timkiem = decorator2.getContentPanel();
		panel_timkiem.setBounds(10, 663, 1042, 50);
		panel_timkiem.setBorder(BorderFactory.createEtchedBorder());
		pnlMain.remove(panel_phantrang);
		pnlMain.add(panel_timkiem);
		pnlMain.repaint();
		pnlMain.revalidate();
		if (model.getRowCount() == 0)
			JOptionPane.showMessageDialog(this, "Không tìm thấy sản phẩm nào", "Tìm sản phẩm",
					JOptionPane.INFORMATION_MESSAGE);
	}

	private boolean validInputTextField() {
		if (!tfTenSp.getText().trim().matches("[\\p{L}\\s0-9()\\/\\_\\\\*\"\'\\.,\\+-]+")) {
			JOptionPane.showMessageDialog(this, "Tên SP chỉ bao gồm chữ, số, và các kí tự ( ) \\ / _ , . + - ' \"",
					"Thông báo", JOptionPane.ERROR_MESSAGE);
			tfTenSp.requestFocus();
			return false;
		}
		if (tfDonvitinh.getText().trim().equals("")) {
			JOptionPane.showMessageDialog(this, "Nhập vào đơn vị tính cho sản phẩm", "Thông báo",
					JOptionPane.ERROR_MESSAGE);
			tfDonvitinh.requestFocus();
			return false;
		}
		if (Double.parseDouble(tfGiaban.getValue().toString()) <= 0) {
			JOptionPane.showMessageDialog(this, "Giá bán phải > 0", "Thông báo", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		if (Double.parseDouble(tfGiamua.getValue().toString()) <= 0) {
			JOptionPane.showMessageDialog(this, "Giá mua phải > 0", "Thông báo", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		if (Integer.parseInt(tfSoluongton.getValue().toString()) <= 0) {
			JOptionPane.showMessageDialog(this, "Vui lòng nhập số lượng tồn (>0) của sản phẩm", "Thông báo",
					JOptionPane.ERROR_MESSAGE);
			return false;
		}
		if (Float.parseFloat(tfThue.getValue().toString()) <= 0) {
			JOptionPane.showMessageDialog(this, "Vui lòng nhập thuế (>0) của sản phẩm", "Thông báo",
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

	/**
	 * tìm kiếm theo khoảng của số lượng tồn
	 */
	private void timTheoSoluongton() {
		try {
			tfSoluongton1.commitEdit();
			tfSoluongton2.commitEdit();
		} catch (ParseException e) {
			JOptionPane.showMessageDialog(this, "Số lượng tồn phải là một con số", "Tìm sản phẩm",
					JOptionPane.ERROR_MESSAGE);
		}
		int from = Integer.parseInt(tfSoluongton1.getValue().toString());
		int to = Integer.parseInt(tfSoluongton2.getValue().toString());
		DataProvider<Sanpham> dataTimkiem = new DataProvider<Sanpham>() {

			@Override
			public int getTotalRowCount() {
				return dao.getSizeTimkiemTheoSoluongton(from, to);
			}

			@Override
			public void addDataToTable(int startIndex, int endIndex) {
				List<Sanpham> list = dao.timkiemTheoSoluongton(startIndex, endIndex, from, to);
				model.setDssp(list);
				model.fireTableDataChanged();
			}
		};
		pnlMain.remove(panel_timkiem);
		Decorator<Sanpham> decorator2 = Decorator.decorate(dataTimkiem, new int[] { 10, 25, 50, 75, 100, 200, 500 },
				25);
		panel_timkiem = decorator2.getContentPanel();
		panel_timkiem.setBounds(10, 663, 1042, 50);
		panel_timkiem.setBorder(BorderFactory.createEtchedBorder());
		pnlMain.remove(panel_phantrang);
		pnlMain.add(panel_timkiem);
		pnlMain.repaint();
		pnlMain.revalidate();
		if (model.getRowCount() == 0)
			JOptionPane.showMessageDialog(this, "Không tìm thấy sản phẩm nào");
	}

	/**
	 * tìm kiếm theo khoảng thuế
	 */
	private void timTheoThue() {
		try {
			tfThue1.commitEdit();
			tfThue2.commitEdit();
		} catch (ParseException e) {
			JOptionPane.showMessageDialog(this, "Thuế phải là một con số", "Tìm sản phẩm", JOptionPane.ERROR_MESSAGE);
		}
		double from = Double.parseDouble(tfThue1.getValue().toString());
		double to = Double.parseDouble(tfThue2.getValue().toString());
		DataProvider<Sanpham> dataTimkiem = new DataProvider<Sanpham>() {

			@Override
			public int getTotalRowCount() {
				return dao.getSizeTimkiemTheoThue(from, to);
			}

			@Override
			public void addDataToTable(int startIndex, int endIndex) {
				List<Sanpham> list = dao.timkiemTheoThue(startIndex, endIndex, from, to);
				model.setDssp(list);
				model.fireTableDataChanged();
			}
		};
		pnlMain.remove(panel_timkiem);
		Decorator<Sanpham> decorator2 = Decorator.decorate(dataTimkiem, new int[] { 10, 25, 50, 75, 100, 200, 500 },
				25);
		panel_timkiem = decorator2.getContentPanel();
		panel_timkiem.setBounds(10, 663, 1042, 50);
		panel_timkiem.setBorder(BorderFactory.createEtchedBorder());
		pnlMain.remove(panel_phantrang);
		pnlMain.add(panel_timkiem);
		pnlMain.repaint();
		pnlMain.revalidate();
		if (model.getRowCount() == 0)
			JOptionPane.showMessageDialog(this, "Không tìm thấy sản phẩm nào");
	}

}