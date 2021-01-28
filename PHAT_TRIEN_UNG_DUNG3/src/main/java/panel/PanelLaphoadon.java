package panel;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.table.TableColumn;

import com.placeholder.PlaceHolder;

import custom.MyJButton;
import custom.MyJLabel;
import custom.MyJScrollPane;
import custom.MyJTextField;
import custom.MyTableCellRender;
import custom.MyTableCellRenderSanphamHoadon;
import dao.DaoHoadon;
import dao.DaoKhachhang;
import dao.DaoLoaisanpham;
import dao.DaoNhacungcap;
import dao.DaoSanpham;
import entity.Chitiethoadon;
import entity.Chitiethoadon_PK;
import entity.Hoadon;
import entity.Khachhang;
import entity.Loaisanpham;
import entity.Nhacungcap;
import entity.Nhanvien;
import entity.Sanpham;
import frame.FrameTrangchu;
import model.ModelChitiethoadon;
import model.ModelKhachhang;
import model.ModelSanpham;

/**
 * @author kienc
 *
 */
public class PanelLaphoadon extends JPanel implements ActionListener, MouseListener {

	private static final long serialVersionUID = 1L;
	private static final int vertical_always = JScrollPane.VERTICAL_SCROLLBAR_ALWAYS;
	private static final int horizontal_needed = JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED;
	private static final int horizontal_never = JScrollPane.HORIZONTAL_SCROLLBAR_NEVER;
	private static final DecimalFormat df = new DecimalFormat("###,###,###.#");

	private JTable tableChitiet;
	private JTable tableSanpham;
	private JTable tableKh;

	private ModelSanpham modelSanpham;
	private ModelChitiethoadon modelChiTiet;
	private ModelKhachhang modelKh;

	private DaoHoadon daohd;
	private DaoSanpham daosp;
	private DaoKhachhang daokh;
	private DaoLoaisanpham daolsp;
	private DaoNhacungcap daoncc;

	private MyJTextField tfTimSp;
	private MyJTextField tfSoLuong;
	private MyJTextField tfTien;
	private MyJTextField tfDonvitinh;
	private MyJTextField tfTenSp;
	private MyJTextField tfThuonghieu;
	private MyJTextField tfMausac;
	private MyJTextField tfKichthuoc;
	private MyJTextField tfKhoiluong;
	private MyJTextField tfXuatxu;
	private MyJTextField tfLoaiSp;
	private MyJTextField tfNhacc;
	private MyJTextField tfTimKh;
	private MyJTextField tfMaSp;
	private MyJTextField tfSoluongton;
	private MyJTextField tfThue;
	private MyJTextField tfTgbh;
	private MyJTextField tfGiaban;
	private JTextArea tfMota;

	private MyJButton btnXoa;
	private MyJButton btnThem;
	private MyJButton btnTaoHD;
	private MyJButton btnXoaTrang;

	private MyJLabel lblSumTongTien;
	private MyJLabel lblSumSoLuong;
	private MyJLabel lblMaHD;
	private MyJLabel lblTimSanpham;
	private MyJLabel lblThemKh;
	private MyJLabel lblTimKhach;
	private MyJLabel lblNgayNhap;
	private MyJLabel lblMakh;
	private MyJLabel lblHoten;
	private MyJLabel lblDiachi;
	private MyJLabel lblSdt;

	private List<Chitiethoadon> dscthd;
	private List<Sanpham> dssp;
	private List<Khachhang> dskh;
	private List<Loaisanpham> dslsp;
	private List<Nhacungcap> dsncc;

	private JComboBox<String> cboLoaisp;
	private JComboBox<String> cboNhacc;

	private String maHDHienTai;
	private Nhanvien nv;
	private Khachhang kh = null;

	public PanelLaphoadon(Nhanvien nv2, Khachhang khachhang) {
		this(nv2);
		kh = khachhang;
		dskh = new ArrayList<Khachhang>();
		dskh.add(kh);
		modelKh.setDskh(dskh);
		modelKh.fireTableDataChanged();
		lblMakh.setText("Mã KH: " + kh.getMakhachhang());
		lblHoten.setText("Họ tên: " + kh.getHoten());
		lblDiachi.setText("Địa chỉ: " + kh.getDiachi());
		lblSdt.setText("Số điện thoại: " + kh.getSodienthoai());
		try {
			tableKh.setRowSelectionInterval(0, 0);
		} catch (Exception e) {
		}
	}

	public PanelLaphoadon(Nhanvien nv) {
		setNv(nv);
		setSize(1680, 1021);
		setLayout(null);

		setBackground(Color.WHITE);

		/**
		 * panel
		 */
		JPanel pnlButton = new JPanel(null);
		pnlButton.setBounds(10, 943, 1055, 55);
		pnlButton.setBorder(BorderFactory.createEtchedBorder());
		add(pnlButton);

		JPanel panelTimSp = new JPanel(null);
		panelTimSp.setBounds(10, 56, 1055, 125);
		panelTimSp.setBorder(BorderFactory.createEtchedBorder());
		add(panelTimSp);

		JPanel panelLaphd = new JPanel(null);
		panelLaphd.setBounds(1075, 943, 575, 55);
		panelLaphd.setBorder(BorderFactory.createEtchedBorder());
		add(panelLaphd);

		JPanel pnlThemsp = new JPanel(null);
		pnlThemsp.setBounds(10, 554, 1055, 55);
		pnlThemsp.setBorder(BorderFactory.createEtchedBorder());
		add(pnlThemsp);

		JPanel panelRight = new JPanel(null);
		panelRight.setBounds(1075, 55, 575, 488);
		panelRight.setBorder(BorderFactory.createEtchedBorder());
		add(panelRight);

		JPanel panelKH = new JPanel(null);
		panelKH.setBounds(1075, 554, 575, 378);
		panelKH.setBorder(BorderFactory.createEtchedBorder());
		add(panelKH);

		/**
		 * label
		 */
		MyJLabel lblTitle = new MyJLabel("TẠO HÓA ĐƠN MỚI");
		lblTitle.setForeground(Color.RED);
		lblTitle.setFont(new Font("Serif", Font.BOLD, 20));
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitle.setBounds(10, 0, 1633, 30);
		add(lblTitle);

		MyJLabel lblTimSp = new MyJLabel("Tìm sản phẩm theo từ khóa");
		lblTimSp.setBounds(10, 11, 219, 25);
		panelTimSp.add(lblTimSp);

		MyJLabel lblCount = new MyJLabel("Tổng:");
		lblCount.setBounds(610, 9, 70, 33);
		pnlButton.add(lblCount);

		MyJLabel lblNgay = new MyJLabel();
		lblNgay.setText("Ngày lập: " + LocalDate.now());
		lblNgay.setBounds(249, 29, 200, 25);
		add(lblNgay);

		MyJLabel lblManv = new MyJLabel();
		lblManv.setText("Mã nhân viên: " + nv.getManhanvien());
		lblManv.setBounds(459, 31, 250, 25);
		add(lblManv);

		MyJLabel lblTennv = new MyJLabel();
		lblTennv.setText("Tên nhân viên: " + nv.getHodem() + " " + nv.getTen());
		lblTennv.setBounds(719, 29, 250, 25);
		add(lblTennv);

		MyJLabel lblTien = new MyJLabel("Tiền khách đưa");
		lblTien.setBounds(10, 9, 139, 35);
		panelLaphd.add(lblTien);

		MyJLabel lblSoLuong = new MyJLabel("Nhập số lượng mua");
		lblSoLuong.setBounds(10, 9, 167, 35);
		pnlThemsp.add(lblSoLuong);

		MyJLabel lblTimLoaisp = new MyJLabel("Tìm theo loại sản phẩm");
		lblTimLoaisp.setBounds(10, 49, 162, 25);
		panelTimSp.add(lblTimLoaisp);

		MyJLabel lblTimNhacc = new MyJLabel("Tìm theo nhà cung cấp");
		lblTimNhacc.setBounds(10, 85, 162, 25);
		panelTimSp.add(lblTimNhacc);

		MyJLabel tooltipTimTheoLoai = new MyJLabel("");
		tooltipTimTheoLoai.setIcon(new ImageIcon("img\\information.png"));
		tooltipTimTheoLoai.setBounds(979, 49, 24, 24);
		tooltipTimTheoLoai.setToolTipText("Tìm theo tên của loại sản phẩm");
		panelTimSp.add(tooltipTimTheoLoai);

		MyJLabel tooltipTimNhacc = new MyJLabel("");
		tooltipTimNhacc.setIcon(new ImageIcon("img\\information.png"));
		tooltipTimNhacc.setBounds(979, 93, 24, 24);
		tooltipTimNhacc.setToolTipText("Tìm sản phẩm theo tên của nhà cung cấp");
		panelTimSp.add(tooltipTimNhacc);

		MyJLabel lblMa = new MyJLabel("Mã sản phẩm");
		lblMa.setBounds(12, 13, 103, 25);
		panelRight.add(lblMa);

		MyJLabel lblDvTinh = new MyJLabel("Đơn vị tính");
		lblDvTinh.setBounds(326, 211, 90, 25);
		panelRight.add(lblDvTinh);

		MyJLabel lblMoTa = new MyJLabel("Mô tả");
		lblMoTa.setBounds(12, 364, 103, 25);
		panelRight.add(lblMoTa);

		MyJLabel lblTen = new MyJLabel("Tên sản phẩm");
		lblTen.setBounds(13, 50, 102, 25);
		panelRight.add(lblTen);

		MyJLabel lblLoaiSp = new MyJLabel("Loại sản phẩm");
		lblLoaiSp.setBounds(13, 88, 102, 25);
		panelRight.add(lblLoaiSp);

		MyJLabel lblNhacc = new MyJLabel("Nhà cung cấp");
		lblNhacc.setBounds(13, 128, 102, 25);
		panelRight.add(lblNhacc);

		MyJLabel lblGiaBan = new MyJLabel("Thuế (%)");
		lblGiaBan.setBounds(401, 328, 103, 25);
		panelRight.add(lblGiaBan);

		MyJLabel lblSLTon = new MyJLabel("Số lượng tồn");
		lblSLTon.setBounds(226, 328, 110, 25);
		panelRight.add(lblSLTon);

		MyJLabel lblTGBH = new MyJLabel("Thời gian BH");
		lblTGBH.setBounds(13, 328, 102, 25);
		panelRight.add(lblTGBH);

		MyJLabel lblVnd = new MyJLabel("vnđ");
		lblVnd.setHorizontalAlignment(SwingConstants.RIGHT);
		lblVnd.setBounds(520, 11, 45, 26);
		panelRight.add(lblVnd);

		MyJLabel lblThang = new MyJLabel("th");
		lblThang.setBounds(185, 328, 45, 25);
		panelRight.add(lblThang);

		MyJLabel lblMausac = new MyJLabel("Màu sắc");
		lblMausac.setBounds(326, 170, 65, 25);
		panelRight.add(lblMausac);

		MyJLabel lblThuongHieu = new MyJLabel("Thương hiệu");
		lblThuongHieu.setBounds(13, 168, 102, 25);
		panelRight.add(lblThuongHieu);

		MyJLabel lblKichThuoc = new MyJLabel("Kích thước");
		lblKichThuoc.setBounds(13, 209, 102, 25);
		panelRight.add(lblKichThuoc);

		MyJLabel lblKhoiLuong = new MyJLabel("Khối lượng");
		lblKhoiLuong.setBounds(13, 250, 102, 25);
		panelRight.add(lblKhoiLuong);

		MyJLabel lblXuatXu = new MyJLabel("Xuất xứ");
		lblXuatXu.setBounds(13, 288, 102, 25);
		panelRight.add(lblXuatXu);

		MyJLabel lblGiaban = new MyJLabel("Đơn giá");
		lblGiaban.setBounds(304, 13, 65, 25);
		panelRight.add(lblGiaban);

		MyJLabel lblTimKh = new MyJLabel("Tìm kiếm khách hàng");
		lblTimKh.setBounds(10, 11, 180, 27);
		panelKH.add(lblTimKh);

		MyJLabel lblNewLabel = new MyJLabel("Ngày nhập: ");
		lblNewLabel.setBounds(326, 247, 90, 25);
		panelRight.add(lblNewLabel);

		lblMaHD = new MyJLabel();
		lblMaHD.setBounds(10, 29, 222, 25);
		add(lblMaHD);

		lblNgayNhap = new MyJLabel("");
		lblNgayNhap.setHorizontalAlignment(SwingConstants.CENTER);
		lblNgayNhap.setBounds(401, 247, 164, 25);
		panelRight.add(lblNgayNhap);

		lblSumTongTien = new MyJLabel("0");
		lblSumTongTien.setBounds(838, 9, 207, 33);
		lblSumTongTien.setHorizontalAlignment(SwingConstants.RIGHT);
		pnlButton.add(lblSumTongTien);

		lblSumSoLuong = new MyJLabel("0");
		lblSumSoLuong.setBounds(728, 9, 100, 33);
		lblSumSoLuong.setHorizontalAlignment(SwingConstants.RIGHT);
		pnlButton.add(lblSumSoLuong);

		lblTimSanpham = new MyJLabel("");
		lblTimSanpham.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		lblTimSanpham.setIcon(new ImageIcon("img\\find.png"));
		lblTimSanpham.setBounds(979, 11, 25, 25);
		lblTimSanpham.setToolTipText("Tìm sản phẩm theo mã, tên, mô tả, màu sắc hoặc thương hiệu");
		panelTimSp.add(lblTimSanpham);

		lblMakh = new MyJLabel("Mã KH: ");
		lblMakh.setBounds(10, 270, 252, 25);
		panelKH.add(lblMakh);

		lblHoten = new MyJLabel("Họ tên: ");
		lblHoten.setBounds(272, 270, 286, 25);
		panelKH.add(lblHoten);

		lblDiachi = new MyJLabel("Địa chỉ: ");
		lblDiachi.setBounds(10, 342, 548, 25);
		panelKH.add(lblDiachi);

		lblSdt = new MyJLabel("Số điện thoại: ");
		lblSdt.setBounds(10, 306, 252, 25);
		panelKH.add(lblSdt);

		lblThemKh = new MyJLabel("");
		lblThemKh.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		lblThemKh.setIcon(new ImageIcon("img\\them.png"));
		lblThemKh.setBounds(525, 11, 25, 25);
		lblThemKh.setToolTipText("Nhập thông tin cho một khách hàng mới");
		panelKH.add(lblThemKh);

		lblTimKhach = new MyJLabel("");
		lblTimKhach.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		lblTimKhach.setIcon(new ImageIcon("img\\find.png"));
		lblTimKhach.setBounds(480, 11, 25, 25);
		lblTimKhach.setToolTipText("Tìm khách hàng theo mã, họ tên hoặc số điện thoại");
		panelKH.add(lblTimKhach);

		/**
		 * button
		 */
		btnThem = new MyJButton("Thêm sản phẩm vào hóa đơn");
		btnThem.setBounds(815, 9, 230, 35);
		btnThem.setMnemonic('t');
		btnThem.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnThem.setToolTipText("Thêm sản phẩm đang chọn trên bảng sản phẩm vào bảng hóa đơn");
		btnThem.setIcon(new ImageIcon("img\\them.png"));
		pnlThemsp.add(btnThem);

		btnTaoHD = new MyJButton("Lập hóa đơn");
		btnTaoHD.setBounds(395, 9, 170, 35);
		btnTaoHD.setMnemonic('l');
		btnTaoHD.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnTaoHD.setToolTipText("Xem trước và lập hóa đơn");
		btnTaoHD.setIcon(new ImageIcon("img\\bill.png"));
		panelLaphd.add(btnTaoHD);

		btnXoa = new MyJButton("Xóa sản phẩm khỏi hóa đơn");
		btnXoa.setBounds(311, 9, 220, 35);
		btnXoa.setMnemonic('a');
		btnXoa.setIcon(new ImageIcon("img\\delete.png"));
		btnXoa.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnXoa.setToolTipText("Xóa một hoặc nhiều sản phẩm đang chọn trên bảng chi tiết khỏi hóa đơn");
		pnlButton.add(btnXoa);

		btnXoaTrang = new MyJButton("Xóa trắng");
		btnXoaTrang.setMnemonic('x');
		btnXoaTrang.setBounds(10, 9, 220, 35);
		btnXoaTrang.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnXoaTrang.setIcon(new ImageIcon("img\\refresh.png"));
		pnlButton.add(btnXoaTrang);

		/**
		 * textfield
		 */

		tfTimKh = new MyJTextField(20);
		tfTimKh.setBounds(170, 12, 300, 25);
		panelKH.add(tfTimKh);

		tfTimSp = new MyJTextField(20);
		tfTimSp.setBounds(239, 11, 730, 25);
		panelTimSp.add(tfTimSp);

		tfSoLuong = new MyJTextField(20);
		tfSoLuong.setBounds(187, 14, 300, 25);
		pnlThemsp.add(tfSoLuong);

		tfTien = new MyJTextField("");
		tfTien.setBounds(148, 15, 237, 25);
		panelLaphd.add(tfTien);

		tfMaSp = new MyJTextField(20);
		tfMaSp.setBounds(125, 11, 162, 25);
		panelRight.add(tfMaSp);

		tfDonvitinh = new MyJTextField(20);
		tfDonvitinh.setBounds(411, 209, 154, 25);
		panelRight.add(tfDonvitinh);

		tfTenSp = new MyJTextField(20);
		tfTenSp.setBounds(125, 50, 440, 25);
		panelRight.add(tfTenSp);

		tfThuonghieu = new MyJTextField(20);
		tfThuonghieu.setBounds(125, 168, 191, 25);
		panelRight.add(tfThuonghieu);

		tfMausac = new MyJTextField(20);
		tfMausac.setBounds(411, 168, 154, 25);
		panelRight.add(tfMausac);

		tfKichthuoc = new MyJTextField(20);
		tfKichthuoc.setBounds(125, 207, 191, 25);
		panelRight.add(tfKichthuoc);

		tfKhoiluong = new MyJTextField(20);
		tfKhoiluong.setBounds(125, 248, 191, 25);
		panelRight.add(tfKhoiluong);

		tfXuatxu = new MyJTextField(20);
		tfXuatxu.setBounds(125, 286, 440, 25);
		panelRight.add(tfXuatxu);

		tfLoaiSp = new MyJTextField(20);
		tfLoaiSp.setBounds(125, 88, 440, 25);
		panelRight.add(tfLoaiSp);

		tfNhacc = new MyJTextField(20);
		tfNhacc.setBounds(125, 128, 440, 25);
		panelRight.add(tfNhacc);

		tfSoluongton = new MyJTextField(20);
		tfSoluongton.setBounds(325, 328, 65, 25);
		panelRight.add(tfSoluongton);

		tfThue = new MyJTextField(20);
		tfThue.setBounds(500, 328, 65, 25);
		panelRight.add(tfThue);

		tfTgbh = new MyJTextField(20);
		tfTgbh.setBounds(125, 328, 50, 25);
		panelRight.add(tfTgbh);

		tfGiaban = new MyJTextField(20);
		tfGiaban.setBounds(362, 11, 170, 25);
		panelRight.add(tfGiaban);

		tfMota = new JTextArea();
		tfMota.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
		Font fontMota = tfMota.getFont();
		tfMota.setFont(new Font(fontMota.getName(), fontMota.getStyle(), fontMota.getSize() + 5));

		/**
		 * dao
		 */
		daolsp = new DaoLoaisanpham();
		daoncc = new DaoNhacungcap();
		daokh = new DaoKhachhang();
		daosp = new DaoSanpham();
		daohd = new DaoHoadon();
		dscthd = new ArrayList<Chitiethoadon>();
		dssp = new ArrayList<Sanpham>();
		dskh = new ArrayList<Khachhang>();

		/**
		 * combobox
		 */
		cboLoaisp = new JComboBox<String>();
		cboLoaisp.setBounds(239, 49, 730, 25);
		panelTimSp.add(cboLoaisp);

		cboNhacc = new JComboBox<String>();
		cboNhacc.setBounds(239, 87, 730, 25);
		panelTimSp.add(cboNhacc);

		dslsp = daolsp.getAll();
		dsncc = daoncc.getAll();
		for (Loaisanpham lsp : dslsp) {
			cboLoaisp.addItem(lsp.getTenloai());
		}
		for (Nhacungcap ncc : dsncc) {
			cboNhacc.addItem(ncc.getTennhacungcap());
		}

		/**
		 * table, model
		 */
		modelChiTiet = new ModelChitiethoadon(dscthd) {
			private static final long serialVersionUID = 1L;

			/**
			 * model bảng chi tiết cho phép chỉnh sửa số lượng mua trực tiếp trên bảng
			 */
			@Override
			public boolean isCellEditable(int rowIndex, int columnIndex) {
				return columnIndex == 5 ? true : false;
			}
		};
		tableChitiet = new JTable(modelChiTiet);
		tableChitiet.setRowHeight(35);
		tableChitiet.setAutoCreateRowSorter(true);
		tableChitiet.getTableHeader().setBackground(new Color(255, 208, 120));
		tableChitiet.getTableHeader().setPreferredSize(new Dimension(tableChitiet.getTableHeader().getWidth(), 29));
		Font f1 = tableChitiet.getTableHeader().getFont();
		tableChitiet.getTableHeader().setFont(new Font(f1.getName(), Font.BOLD, f1.getSize() + 2));

		MyTableCellRender renderTable = new MyTableCellRender();
		tableChitiet.setDefaultRenderer(String.class, renderTable);
		tableChitiet.setDefaultRenderer(Double.class, renderTable);
		tableChitiet.setDefaultRenderer(Integer.class, renderTable);

		modelSanpham = new ModelSanpham();
		tableSanpham = new JTable(modelSanpham);
		tableSanpham.setRowHeight(35);
		tableSanpham.setAutoCreateRowSorter(true);
		tableSanpham.getTableHeader().setBackground(new Color(255, 208, 120));
		tableSanpham.getTableHeader().setPreferredSize(new Dimension(tableSanpham.getTableHeader().getWidth(), 25));
		Font f2 = tableSanpham.getTableHeader().getFont();
		tableSanpham.getTableHeader().setFont(new Font(f2.getName(), Font.BOLD, f2.getSize() + 2));
		/*
		 * xóa cột giá mua, giá bán trên bảng sản phẩm
		 */
		TableColumn column2 = tableSanpham.getColumnModel().getColumn(2);
		tableSanpham.removeColumn(column2);
		TableColumn column3 = tableSanpham.getColumnModel().getColumn(2);
		tableSanpham.removeColumn(column3);

		MyTableCellRenderSanphamHoadon renderTableSanpham = new MyTableCellRenderSanphamHoadon();
		tableSanpham.setDefaultRenderer(String.class, renderTableSanpham);
		tableSanpham.setDefaultRenderer(Double.class, renderTableSanpham);
		tableSanpham.setDefaultRenderer(Integer.class, renderTableSanpham);

		modelKh = new ModelKhachhang();
		tableKh = new JTable(modelKh);
		tableKh.setRowHeight(35);
		tableKh.setAutoCreateRowSorter(true);
		tableKh.getTableHeader().setBackground(new Color(255, 208, 120));
		tableKh.setDefaultRenderer(Object.class, renderTable);
		tableKh.getTableHeader().setPreferredSize(new Dimension(tableKh.getTableHeader().getWidth(), 29));
		Font f3 = tableKh.getTableHeader().getFont();
		tableKh.getTableHeader().setFont(new Font(f3.getName(), Font.BOLD, f3.getSize() + 2));

		/**
		 * scrollpane
		 */
		MyJScrollPane scrollChiTiet = new MyJScrollPane(tableChitiet, vertical_always, horizontal_never);
		scrollChiTiet.setBounds(10, 620, 1055, 312);
		add(scrollChiTiet);

		MyJScrollPane scrollSanpham = new MyJScrollPane(tableSanpham, vertical_always, horizontal_needed);
		scrollSanpham.setBounds(10, 192, 1055, 351);
		add(scrollSanpham);

		MyJScrollPane scrollPane = new MyJScrollPane(tfMota, vertical_always, horizontal_never);
		scrollPane.setBounds(10, 400, 555, 77);
		scrollPane.setBorder(BorderFactory.createLineBorder(Color.BLUE, 1));
		panelRight.add(scrollPane);

		MyJScrollPane scrollKh = new MyJScrollPane(tableKh, vertical_always, horizontal_needed);
		scrollKh.setBounds(10, 49, 555, 215);
		panelKH.add(scrollKh);

		/**
		 * su kien
		 */
		tfTien.addActionListener(this);
		tfSoLuong.addActionListener(this);
		tfTimKh.addActionListener(this);
		tfTimSp.addActionListener(this);

		btnXoa.addActionListener(this);
		btnThem.addActionListener(this);
		btnTaoHD.addActionListener(this);
		btnXoaTrang.addActionListener(this);

		cboLoaisp.addActionListener(this);
		cboNhacc.addActionListener(this);

		tableSanpham.addMouseListener(this);
		tableChitiet.addMouseListener(this);
		tableKh.addMouseListener(this);

		lblTimSanpham.addMouseListener(this);
		lblTimKhach.addMouseListener(this);
		lblThemKh.addMouseListener(this);

		/*
		 * khi người dùng sửa số lượng mua trên bảng chi tiết hóa đơn và nhấn enter thì
		 * cập nhật lại số lượng mua và tổng tiền của hóa đơn
		 */
		tableChitiet.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				Object o = e.getSource();
				if (o.equals(tableChitiet) && e.getKeyCode() == KeyEvent.VK_ENTER)
					count();
			}
		});

		tfGiaban.setEditable(false);
		tfTgbh.setEditable(false);
		tfThue.setEditable(false);
		tfSoluongton.setEditable(false);
		tfMota.setEditable(false);
		tfMaSp.setEditable(false);
		tfDonvitinh.setEditable(false);
		tfMota.setEditable(false);
		tfTenSp.setEditable(false);
		tfLoaiSp.setEditable(false);
		tfNhacc.setEditable(false);
		tfKhoiluong.setEditable(false);
		tfKichthuoc.setEditable(false);
		tfMausac.setEditable(false);
		tfXuatxu.setEditable(false);
		tfThuonghieu.setEditable(false);

		reloadMahoadon();
		resizeColumnChitietHoadon();
		resizeColumnsSanPham();
		resizeColumnKhachhang();
		xoaTrang();

	}

	/**
	 * thay đổi độ rộng các cột của bảng khách hàng
	 */
	private void resizeColumnKhachhang() {
		tableKh.getColumnModel().getColumn(0).setMinWidth(100);
		tableKh.getColumnModel().getColumn(1).setMinWidth(180);
		tableKh.getColumnModel().getColumn(2).setMinWidth(100);
		tableKh.getColumnModel().getColumn(3).setMinWidth(300);
		tableKh.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
	}

	/**
	 * thay đổi độ rộng các cột của bảng sản phẩm
	 */
	private void resizeColumnsSanPham() {
		tableSanpham.getColumnModel().getColumn(0).setMinWidth(100);
		tableSanpham.getColumnModel().getColumn(1).setMinWidth(330);
		tableSanpham.getColumnModel().getColumn(2).setMinWidth(150);
		tableSanpham.getColumnModel().getColumn(3).setMinWidth(100);
		tableSanpham.getColumnModel().getColumn(4).setMinWidth(100);
		tableSanpham.getColumnModel().getColumn(5).setMinWidth(100);
		tableSanpham.getColumnModel().getColumn(6).setMinWidth(100);
		tableSanpham.getColumnModel().getColumn(7).setMinWidth(250);
		tableSanpham.getColumnModel().getColumn(8).setMinWidth(300);
		tableSanpham.getColumnModel().getColumn(9).setMinWidth(100);
		tableSanpham.getColumnModel().getColumn(10).setMinWidth(200);
		tableSanpham.getColumnModel().getColumn(11).setMinWidth(100);
		tableSanpham.getColumnModel().getColumn(12).setMinWidth(100);
		tableSanpham.getColumnModel().getColumn(13).setMinWidth(300);
		tableSanpham.getColumnModel().getColumn(14).setMinWidth(100);
		tableSanpham.getColumnModel().getColumn(15).setMinWidth(100);
		tableSanpham.getColumnModel().getColumn(16).setMinWidth(150);
		tableSanpham.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

	}

	/**
	 * thay đổi độ rộng các cột của bảng chi tiết hóa đơn
	 */
	private void resizeColumnChitietHoadon() {
		tableChitiet.getColumnModel().getColumn(0).setMinWidth(110);
		tableChitiet.getColumnModel().getColumn(1).setMinWidth(334);
		tableChitiet.getColumnModel().getColumn(2).setMinWidth(100);
		tableChitiet.getColumnModel().getColumn(3).setMinWidth(100);
		tableChitiet.getColumnModel().getColumn(4).setMinWidth(100);
		tableChitiet.getColumnModel().getColumn(5).setMinWidth(100);
		tableChitiet.getColumnModel().getColumn(6).setMinWidth(200);
		tableChitiet.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource();
		if (o.equals(btnThem) || o.equals(tfSoLuong))
			them();
		else if (o.equals(btnXoa))
			xoa();
		else if (o.equals(btnTaoHD) || o.equals(tfTien))
			taoHoadon();
		else if (o.equals(tfTimSp)) {
			timSanPham();
		} else if (o.equals(btnXoaTrang)) {
			xoaTrang();
		} else if (o.equals(tfTimKh)) {
			timKhachhang();
		} else if (o.equals(cboLoaisp) && cboLoaisp.getItemCount() > 0) {
			timTheoLoaiSp();
		} else if (o.equals(cboNhacc) && cboNhacc.getItemCount() > 0) {
			timTheoNhacungcap();
		}
	}

	/**
	 * tìm sản phẩm theo tên nhà cung cấp
	 */
	private void timTheoNhacungcap() {
		String keyword = cboNhacc.getSelectedItem().toString();
		dssp = daosp.timkiemTheoNhacc(0, daosp.getSizeTimkiemTheoNhacc(keyword) + 1, keyword);
		modelSanpham.setDssp(dssp);
		modelSanpham.fireTableDataChanged();
		cboNhacc.setPopupVisible(false);
		if (dssp.isEmpty())
			JOptionPane.showMessageDialog(this, "Không tìm thấy sản phẩm nào");
	}

	/**
	 * tìm sản phẩm theo tên của loại sản phẩm
	 */
	private void timTheoLoaiSp() {
		String keyword = cboLoaisp.getSelectedItem().toString();
		dssp = daosp.timkiemTheoLoaiSp(0, daosp.getSizeTimkiemTheoLoaiSp(keyword) + 1, keyword);
		modelSanpham.setDssp(dssp);
		modelSanpham.fireTableDataChanged();
		cboLoaisp.setPopupVisible(false);
		if (dssp.isEmpty())
			JOptionPane.showMessageDialog(this, "Không tìm thấy sản phẩm nào");
	}

	/**
	 * xóa trắng textfield và bảng
	 */
	private void xoaTrang() {
		tfMaSp.setText("");
		tfDonvitinh.setText("");
		tfMota.setText("");
		tfTenSp.setText("");
		tfLoaiSp.setText("");
		tfNhacc.setText("");
		tfGiaban.setText("");
		tfKhoiluong.setText("");
		tfKichthuoc.setText("");
		tfMausac.setText("");
		tfSoluongton.setText("");
		tfTgbh.setText("");
		tfThue.setText("");
		tfXuatxu.setText("");
		tfThuonghieu.setText("");
		lblNgayNhap.setText("");
		tfTimSp.setText("");
		tfTimKh.setText("");
		tfSoLuong.setText("");
		tfSoLuong.setEditable(true);
		btnThem.setEnabled(true);
		try {
			if (!dssp.isEmpty())
				dssp.clear();
		} catch (Exception e) {
			dssp = new ArrayList<Sanpham>();
			modelSanpham.setDssp(dssp);
		}
		modelSanpham.fireTableDataChanged();
		new PlaceHolder(tfTimSp, new Color(192, 192, 192), Color.BLACK,
				"Nhập mã, tên, mô tả, màu sắc hoặc thương hiệu của sản phẩm", false, this.getFont().toString(), 19);
		new PlaceHolder(tfSoLuong, new Color(192, 192, 192), Color.BLACK, "Nhập số lượng mua", false,
				this.getFont().toString(), 19);
		new PlaceHolder(tfTimKh, new Color(192, 192, 192), Color.BLACK, "Nhập mã, họ tên hoặc SĐT", false,
				this.getFont().toString(), 19);
	}

	/**
	 * kiểm tra thông tin khách hàng và tiền khách hàng đưa
	 * 
	 * @return
	 */
	private boolean kiemTraTien() {
		if (kh == null) {
			JOptionPane.showMessageDialog(this, "Chưa có thông tin khách hàng", "Thông báo", JOptionPane.ERROR_MESSAGE);
			tfTimKh.requestFocus();
			return false;
		}
		if (!tfTien.getText().trim().equals("")) {
			try {
				Hoadon hd = new Hoadon(this.maHDHienTai);
				hd.setChitiethoadons(dscthd);
				double tiẹnkhachdua = Double.parseDouble(tfTien.getText().trim());
				if (tiẹnkhachdua > 10000000000d) {
					JOptionPane.showMessageDialog(this, "Số tiền vượt quá giới hạn. (< 10,000,000,000)", "Thông báo",
							JOptionPane.ERROR_MESSAGE);
					return false;
				} else if (tiẹnkhachdua < hd.tinhTongtien()) {
					String s = "Tiền khách đưa phải lớn hơn hoặc bằng tổng tiền của hóa đơn (>="
							+ df.format(hd.tinhTongtien()) + ")";
					JOptionPane.showMessageDialog(this, s, "Thông báo", JOptionPane.ERROR_MESSAGE);
					tfTien.requestFocus();
					return false;
				}
			} catch (Exception e2) {
				JOptionPane.showMessageDialog(this, "Số tiền phải là một con số", "Thông báo",
						JOptionPane.ERROR_MESSAGE);
				tfTien.requestFocus();
				return false;
			}
		} else {
			JOptionPane.showMessageDialog(this, "Nhập vào số tiền khách trả");
			tfTien.requestFocus();
			return false;
		}
		return true;
	}

	/**
	 * tìm sản phẩm theo từ khóa dựa trên các field đã được đánh index mã, tên, mô
	 * tả, màu sắc hoặc thương hiệu
	 */
	private void timSanPham() {
		String keyword = tfTimSp.getText().trim().toUpperCase();
		if (keyword.equals("")) {
			JOptionPane.showMessageDialog(this,
					"Vui lòng nhập mã, tên, mô tả, màu sắc hoặc thương hiệu sản phẩm để tìm kiếm", "Tìm sản phẩm",
					JOptionPane.INFORMATION_MESSAGE);
			tfTimSp.requestFocus();
		} else {
			dssp = daosp.timkiem(0, daosp.getSizeTimkiem(keyword) + 1, keyword);
			modelSanpham.setDssp(dssp);
			modelSanpham.fireTableDataChanged();
			if (dssp.isEmpty()) {
				JOptionPane.showMessageDialog(this, "Không tìm thấy sản phẩm nào", "Tìm sản phẩm",
						JOptionPane.INFORMATION_MESSAGE);
				tfTimSp.requestFocus();
			}
		}
	}

	/**
	 * tự động lấy ra mã hóa đơn tiếp theo
	 */
	private void reloadMahoadon() {
		maHDHienTai = daohd.getNextMaHD();
		lblMaHD.setText("Mã Hóa Đơn: " + maHDHienTai);
	}

	/**
	 * đếm số lượng sản phẩm và tổng tiền của hóa đơn
	 */
	private void count() {
		int soluong = 0;
		double tongtien = 0;
		for (Chitiethoadon ct : dscthd) {
			soluong += ct.getSoluong();
			tongtien += ct.tinhThanhTien();
		}
		lblSumSoLuong.setText(soluong + "");
		lblSumTongTien.setText(df.format(tongtien));
	}

	/**
	 * thêm 1 sản phẩm vào hóa đơn
	 */
	private void them() {
		boolean b = true;
		if (validInputTextField()) {
			Chitiethoadon ct = getDataFromTextField();
			if (ct != null) {
				if (!dscthd.contains(ct)) {
					dscthd.add(ct);
				} else if (dscthd.contains(ct)) {
					Chitiethoadon cthd = dscthd.get(dscthd.indexOf(ct));
					int slt = cthd.getSanpham().getSoluongton();
					if (ct.getSoluong() + cthd.getSoluong() <= slt) {
						cthd.setSoluong(ct.getSoluong() + cthd.getSoluong());
					} else {
						JOptionPane.showMessageDialog(this,
								"Số lượng tồn không đủ, sản phẩm này chỉ còn tối đa "
										+ (ct.getSanpham().getSoluongton() - cthd.getSoluong()) + " sản phẩm",
								"Lập hóa đơn", JOptionPane.ERROR_MESSAGE);
						b = false;
					}
				}
				if (b) {
					modelChiTiet.fireTableDataChanged();
					tfTimSp.setText("");
					tfSoLuong.setText("");
					tfTimSp.requestFocus();
					count();
//					xoaTrang();
				}
			} else {
				JOptionPane.showMessageDialog(this, "Thêm không thành công.", "Lập hóa đơn", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	/**
	 * xem trước và tạo hóa đơn
	 */
	private void taoHoadon() {
		if (!dscthd.isEmpty()) {
			if (kiemTraTien()) {
				Hoadon hd = new Hoadon(this.maHDHienTai);
				hd.setChitiethoadons(dscthd);
				hd.setNhanvien(nv);
				hd.setNgaylap(new Date(new java.util.Date().getTime()));
				double tien = Double.parseDouble(tfTien.getText().trim());
				hd.setKhachhang(kh);
				final JDialog frame = new JDialog((JFrame) SwingUtilities.getWindowAncestor(this),
						"Xác nhận lập hóa đơn", true);
				try {
					frame.setIconImage(ImageIO.read(new File("img/code1.png")));
				} catch (IOException exception) {
				}
				PanelXemtruochoadon xemtruoc = new PanelXemtruochoadon(hd, kh, nv, tien);
				frame.getContentPane().add(xemtruoc);
				frame.setSize(xemtruoc.getWidth(), xemtruoc.getHeight());
				frame.setLocationRelativeTo((JFrame) SwingUtilities.getWindowAncestor(this));
				frame.setVisible(true);
				/**
				 * khi nào đóng dialog trên thì mới chạy đoạn code bên dưới
				 */
				frame.dispose();
				if (xemtruoc.isSusscess()) {
					dscthd = new ArrayList<Chitiethoadon>();
					modelChiTiet.setDsCTHD(dscthd);
					modelChiTiet.fireTableDataChanged();
					xoaTrang();
					dskh = new ArrayList<Khachhang>();
					modelKh.setDskh(dskh);
					modelKh.fireTableDataChanged();
					tfTimKh.setText("");
					tfTien.setText("");
					kh = null;
					lblSdt.setText("Số điện thoại: ");
					lblDiachi.setText("Địa chỉ: ");
					lblHoten.setText("Họ tên: ");
					lblMakh.setText("Mã KH: ");
					tfTimSp.requestFocus();
					count();
					reloadMahoadon();
				}
			}
		} else {
			JOptionPane.showMessageDialog(this, "Hóa đơn trống", "Lập hóa đơn", JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * tạo chi tiết hóa đơn từ các trường trên textfield
	 * 
	 * @return
	 */
	private Chitiethoadon getDataFromTextField() {
		if (validInputTextField()) {
			Chitiethoadon ct = new Chitiethoadon();
			try {
				ct.setSoluong(Integer.parseInt(tfSoLuong.getText().trim()));
			} catch (Exception e) {
				ct.setSoluong(1);
			}
			String masp = tableSanpham.getValueAt(tableSanpham.getSelectedRow(), 0).toString().toUpperCase();
			Sanpham sp = daosp.getById(masp);
			ct.setSanpham(sp);
			ct.setPk(new Chitiethoadon_PK(maHDHienTai, masp));
			return ct;
		}
		return null;
	}

	/**
	 * xóa 1 hoặc nhiều sản phẩm đã chọn khỏi hóa đơn
	 */
	private void xoa() {
		int[] selectedRows = tableChitiet.getSelectedRows();
		if (selectedRows.length > 0) {
			int xoa = JOptionPane.showConfirmDialog(this, "Bạn có muốn xóa sản phẩm đã chọn khỏi hóa đơn không?", "Xóa",
					JOptionPane.WARNING_MESSAGE);
			if (xoa == JOptionPane.YES_OPTION) {
				/**
				 * đánh dấu những sản phẩm đã chọn với số lượng mua thành -1
				 */
				for (int i = 0; i < selectedRows.length; i++) {
					int y = selectedRows[i];
					if (y < dscthd.size() && dscthd.contains(dscthd.get(y))) {
						dscthd.get(y).setSoluong(-1);
					}
				}
				/**
				 * sau đó mới xóa những sản phẩm có số lượng mua bằng -1 khỏi hóa đơn
				 */
				for (int i = 0; i < dscthd.size(); i++) {
					if (dscthd.get(i).getSoluong() == -1) {
						dscthd.remove(i);
						i--;
					}
				}
				modelChiTiet.fireTableDataChanged();
				count();
				xoaTrang();
			}
		} else {
			JOptionPane.showMessageDialog(this, "Chọn một hoặc nhiều sản phẩm cần xóa", "Xóa sản phẩm khỏi hóa đơn",
					JOptionPane.INFORMATION_MESSAGE);
			if (tableChitiet.getRowCount() > 0) {
				tableChitiet.setRowSelectionInterval(0, 0);
			}
		}
	}

	/**
	 * kiểm tra tính hợp lệ của hóa đơn (số lượng tồn)
	 * 
	 * @return
	 */
	private boolean validInputTextField() {
		if (tableSanpham.getSelectedRow() == -1) {
			JOptionPane.showMessageDialog(this, "Chưa chọn sản phẩm", "Thông báo", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		if (tfSoLuong.getText().trim().equals("")) {
			JOptionPane.showMessageDialog(this, "Vui lòng nhập số lượng mua", "Thông báo", JOptionPane.ERROR_MESSAGE);
			tfSoLuong.requestFocus();
			return false;
		}
		String masp = tableSanpham.getValueAt(tableSanpham.getSelectedRow(), 0).toString().toUpperCase();
		Sanpham sp = daosp.getById(masp);
		try {
			if (Integer.parseInt(tfSoLuong.getText()) > sp.getSoluongton()) {
				JOptionPane.showMessageDialog(this, "Số lượng tồn không đủ. Sản phẩm " + sp.getMasanpham() + " chỉ còn "
						+ sp.getSoluongton() + " sản phẩm", "Thông báo", JOptionPane.ERROR_MESSAGE);
				return false;
			}
			if (Integer.parseInt(tfSoLuong.getText()) <= 0) {
				JOptionPane.showMessageDialog(this, "Số lượng phải là một số nguyên và lớn hơn 0", "Thông báo",
						JOptionPane.ERROR_MESSAGE);
				return false;
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Số lượng không đúng định dạng, phải là một số nguyên", "Thông báo",
					JOptionPane.ERROR_MESSAGE);
			tfSoLuong.requestFocus();
			return false;
		}
		return true;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		Object o = e.getSource();
		if (o.equals(tableSanpham)) {
			int index = tableSanpham.getSelectedRow();
			if (index >= 0 && index < tableSanpham.getRowCount()) {
				String masp = tableSanpham.getValueAt(tableSanpham.getSelectedRow(), 0).toString();
				Sanpham sp = daosp.getById(masp);
				if (sp.isNgungkinhdoanh()) {
					tfSoLuong.setText("");
					tfSoLuong.setEditable(false);
					btnThem.setEnabled(false);
					JOptionPane.showMessageDialog(this, "Sản phẩm này đã ngừng kinh doanh. Vui lòng chọn sản phẩm khác",
							"Lập hóa đơn", JOptionPane.ERROR_MESSAGE);
				} else {
					tfSoLuong.setEditable(true);
					btnThem.setEnabled(true);
					fillTextField(sp);
					tfSoLuong.setText("1");
					tfSoLuong.selectAll();
					tfSoLuong.requestFocus();
				}
			}
		} else if (o.equals(tableChitiet)) {
			int index = tableChitiet.getSelectedRow();
			if (index >= 0 && index < tableChitiet.getRowCount()) {
				String masp = tableChitiet.getValueAt(tableChitiet.getSelectedRow(), 0).toString();
				Sanpham sp = daosp.getById(masp);
				fillTextField(sp);
			}
		} else if (o.equals(lblTimSanpham)) {
			timSanPham();
		} else if (o.equals(lblTimKhach)) {
			timKhachhang();
		} else if (o.equals(tableKh)) {
			String makh = tableKh.getValueAt(tableKh.getSelectedRow(), 0).toString();
			try {
				if (daokh.getByID(makh) != null) {
					kh = daokh.getByID(makh);
					lblMakh.setText("Mã KH: " + kh.getMakhachhang());
					lblHoten.setText("Họ tên: " + kh.getHoten());
					lblDiachi.setText("Địa chỉ: " + kh.getDiachi());
					lblSdt.setText("Số điện thoại: " + kh.getSodienthoai());
					tfTien.requestFocus();
				}
			} catch (Exception e2) {
			}
		} else if (o.equals(lblThemKh)) {
			FrameTrangchu fr = (FrameTrangchu) SwingUtilities.getWindowAncestor(this);
			fr.changePanelKhachhang();
		}
	}

	/**
	 * tìm khách hàng dựa trên các field đã được đánh index: mã, họ tên hoặc số điện
	 * thoại
	 */
	private void timKhachhang() {
		String keyword = tfTimKh.getText().trim().toUpperCase();
		if (keyword.equals("")) {
			JOptionPane.showMessageDialog(this, "Vui lòng nhập mã, họ tên hoặc số điện thoại khách hàng để tìm kiếm",
					"Tìm khách hàng", JOptionPane.INFORMATION_MESSAGE);
			tfTimKh.requestFocus();
		} else {
			dskh = daokh.timkiem(0, daokh.getSizeTimkiem(keyword) + 1, keyword);
			modelKh.setDskh(dskh);
			modelKh.fireTableDataChanged();
			if (dskh.isEmpty()) {
				kh = null;
				lblSdt.setText("Số điện thoại: ");
				lblDiachi.setText("Địa chỉ: ");
				lblHoten.setText("Họ tên: ");
				lblMakh.setText("Mã KH: ");
				tfTimSp.requestFocus();
				JOptionPane.showMessageDialog(this, "Không tìm thấy khách hàng nào", "Tìm khách hàng",
						JOptionPane.INFORMATION_MESSAGE);
				tfTimKh.requestFocus();
			}
		}
	}

	/**
	 * đưa thông tin sản phẩm lên textfield khi người dùng chọn một sản phẩm trên
	 * bảng sản phẩm
	 */
	private void fillTextField(Sanpham sp) {
		tfMaSp.setText(sp.getMasanpham());
		tfDonvitinh.setText(sp.getDonvitinh());
		tfMota.setText(sp.getMota());
		tfTenSp.setText(sp.getTensanpham());
		tfLoaiSp.setText(sp.getLoaisanpham().getTenloai());
		tfNhacc.setText(sp.getNhacungcap().getTennhacungcap());
		tfKhoiluong.setText(sp.getKhoiluong());
		tfKichthuoc.setText(sp.getKichthuoc());
		tfMausac.setText(sp.getMausac());
		tfXuatxu.setText(sp.getXuatxu());
		tfThuonghieu.setText(sp.getThuonghieu());
		lblNgayNhap.setText("" + sp.getNgaynhap());
		tfGiaban.setText(Math.floor(sp.getGiaban() * (1 + sp.getThue())) + "");
		tfSoluongton.setText(sp.getSoluongton() + "");
		tfTgbh.setText(sp.getThoigianbaohanh() + "");
		tfThue.setText(sp.getThue() + "");
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