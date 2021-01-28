package frame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.font.TextAttribute;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.border.MatteBorder;

import custom.MyJLabel;
import dao.DaoTaikhoan;
import entity.Khachhang;
import entity.Nhanvien;
import entity.Taikhoan;
import panel.PanelAbout;
import panel.PanelLaphoadon;
import panel.PanelSaoluu;
import panel.quanly.PanelQuanlyHoadon;
import panel.quanly.PanelQuanlyKhachhang;
import panel.quanly.PanelQuanlyNhanvien;
import panel.quanly.PanelQuanlySanpham;
import panel.thongke.PanelThongke;

public class FrameTrangchu extends JFrame implements MouseListener {

	/**
	* 
	**/
	private static final long serialVersionUID = 1L;

	private MyJLabel lblQLSP;
	private MyJLabel lblQLNV;
	private MyJLabel lblQLKH;
	private MyJLabel lblQLHD;
	private MyJLabel lblLapHD;
	private MyJLabel lblDoiMK;
	private MyJLabel lblSaoLuu;
	private MyJLabel lblThongKe;
	private MyJLabel lblDangXuat;
	private MyJLabel lblMenu;
	private MyJLabel lblTitle2;
	private MyJLabel lblTitle1;
	private MyJLabel lblHuongdan;
	private MyJLabel lblAbout;

	private JPanel panel_left;
	private JPanel panel_main;
	private PanelQuanlyHoadon hoadonPanel;
	private PanelQuanlySanpham sanphamPanel;
	private PanelThongke thongkePanel;
	private PanelQuanlyNhanvien nhanvienPanel;
	private PanelQuanlyKhachhang khachhangPanel;
	private PanelLaphoadon themHoadonPanel;
	private PanelAbout panelAbout;
	private ImageIcon bg_img;
	private Nhanvien nv;
	private boolean quanly = false;

	public Nhanvien getNv() {
		return nv;
	}

	public void setNv(Nhanvien nv) {
		this.nv = nv;
	}

	@SuppressWarnings("unchecked")
	public FrameTrangchu(Nhanvien nv) {
		try {
			setIconImage(ImageIO.read(new File("img/code1.png")));
		} catch (IOException e) {
		}
		setNv(nv);
		if (nv.getChucvu().equalsIgnoreCase("Quản Lý"))
			quanly = true;
		setSize(1920, 1050);
		setResizable(false);
		setLocationRelativeTo(null);
		getContentPane().setLayout(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		setTitle("HỆ THỐNG QUẢN LÝ CỬA HÀNG LINH KIỆN MÁY TÍNH THÀNH ĐẠT");
		bg_img = new ImageIcon("img\\home2.png");
		Image img = bg_img.getImage();
		Image temp_img = img.getScaledInstance(getWidth(), getHeight(), Image.SCALE_SMOOTH);
		bg_img = new ImageIcon(temp_img);

		panel_left = new JPanel(null);
		panel_left.setBounds(0, 0, 240, 1021);
		panel_left.setBackground(new Color(255, 216, 184, 50));
		panel_left.setBorder(new MatteBorder(0, 0, 0, 2, Color.GRAY));
		getContentPane().add(panel_left);

		panel_main = new JPanel(null);
		panel_main.setBounds(240, 0, 1674, 1021);
		getContentPane().add(panel_main);

		lblTitle2 = new MyJLabel("LINH KIỆN MÁY TÍNH THÀNH ĐẠT");
		lblTitle2.setBounds(10, 469, 1654, 50);
		lblTitle2.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitle2.setForeground(Color.BLUE);
		lblTitle2.setFont(new Font("Serif", Font.BOLD, 30));

		lblTitle1 = new MyJLabel("HỆ THỐNG QUẢN LÝ CỬA HÀNG");
		lblTitle1.setBounds(10, 408, 1654, 50);
		lblTitle1.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitle1.setForeground(Color.BLUE);
		lblTitle1.setFont(new Font("Serif", Font.BOLD, 30));

		showHomePage();

		lblMenu = new MyJLabel("");
		lblMenu.addMouseListener(this);
		lblMenu.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		lblMenu.setIcon(new ImageIcon("img\\home.png"));
		lblMenu.setBounds(10, 11, 37, 32);
		panel_left.add(lblMenu);

		lblQLSP = new MyJLabel("Quản Lý Sản Phẩm");
		lblQLSP.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		lblQLSP.setIcon(new ImageIcon("img\\loaisp.png"));
		lblQLSP.setBounds(10, 99, 218, 35);
		panel_left.add(lblQLSP);

		lblQLNV = new MyJLabel("Quản Lý Nhân Viên");
		lblQLNV.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		lblQLNV.setIcon(new ImageIcon("img\\nhanvien.png"));
		lblQLNV.setBounds(10, 191, 218, 35);
		panel_left.add(lblQLNV);

		lblQLHD = new MyJLabel("Quản Lý Hóa Đơn");
		lblQLHD.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		lblQLHD.setIcon(new ImageIcon("img\\bill.png"));
		lblQLHD.setBounds(10, 237, 218, 35);
		panel_left.add(lblQLHD);

		MyJLabel lblThongTinNV = new MyJLabel(nv.getManhanvien() + " - " + nv.getHodem() + " " + nv.getTen());
		lblThongTinNV.setHorizontalAlignment(SwingConstants.CENTER);
		lblThongTinNV.setBounds(10, 907, 220, 30);
		panel_left.add(lblThongTinNV);

		MyJLabel lblChucVu = new MyJLabel(nv.getChucvu());
		lblChucVu.setHorizontalAlignment(SwingConstants.CENTER);
		lblChucVu.setBounds(10, 937, 220, 30);
		panel_left.add(lblChucVu);

		lblSaoLuu = new MyJLabel("Sao lưu dữ liệu");
		lblSaoLuu.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		lblSaoLuu.setIcon(new ImageIcon("img\\backup.png"));
		lblSaoLuu.setBounds(10, 329, 218, 35);
		panel_left.add(lblSaoLuu);

		lblQLKH = new MyJLabel("Quản Lý Khách Hàng");
		lblQLKH.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		lblQLKH.setIcon(new ImageIcon("img\\khachhang.png"));
		lblQLKH.setBounds(10, 145, 218, 35);
		panel_left.add(lblQLKH);

		lblThongKe = new MyJLabel("Thống Kê");
		lblThongKe.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		lblThongKe.setIcon(new ImageIcon("img\\thongke.png"));
		lblThongKe.setBounds(10, 283, 218, 35);
		panel_left.add(lblThongKe);

		lblLapHD = new MyJLabel("Lập Hóa Đơn Mới");
		lblLapHD.setIcon(new ImageIcon("img\\laphoadon.png"));
		lblLapHD.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		lblLapHD.setBounds(10, 54, 218, 35);
		panel_left.add(lblLapHD);

		lblHuongdan = new MyJLabel("Hướng dẫn sử dụng");
		lblHuongdan.setBounds(10, 375, 218, 35);
		lblHuongdan.setIcon(new ImageIcon("img\\question.png"));
		lblHuongdan.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		panel_left.add(lblHuongdan);

		lblAbout = new MyJLabel("About");
		lblAbout.setBounds(10, 421, 218, 35);
		lblAbout.setIcon(new ImageIcon("img\\information.png"));
		lblAbout.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		panel_left.add(lblAbout);

		lblDoiMK = new MyJLabel("Đổi MK");
		lblDoiMK.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		lblDoiMK.setForeground(Color.BLUE);
		lblDoiMK.setBounds(48, 969, 60, 30);
		panel_left.add(lblDoiMK);

		lblDangXuat = new MyJLabel("Đăng Xuất");
		lblDangXuat.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		lblDangXuat.setForeground(Color.RED);
		lblDangXuat.setBounds(129, 969, 71, 30);
		panel_left.add(lblDangXuat);

		/**
		 * 
		 */
		Font font = lblDoiMK.getFont();
		@SuppressWarnings("rawtypes")
		Map attributes = font.getAttributes();
		attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
		lblDoiMK.setFont(font.deriveFont(attributes));
		lblDangXuat.setFont(font.deriveFont(attributes));

		lblHuongdan.addMouseListener(this);
		lblAbout.addMouseListener(this);
		lblDangXuat.addMouseListener(this);
		lblDoiMK.addMouseListener(this);
		lblLapHD.addMouseListener(this);
		lblQLHD.addMouseListener(this);
		lblQLKH.addMouseListener(this);
		lblQLNV.addMouseListener(this);
		lblQLSP.addMouseListener(this);
		lblSaoLuu.addMouseListener(this);
		lblThongKe.addMouseListener(this);
		lblMenu.addMouseListener(this);

		/*
		 * 
		 */

		JSeparator separator = new JSeparator();
		separator.setBounds(13, 134, 205, 10);
		panel_left.add(separator);

		JSeparator separator_2 = new JSeparator();
		separator_2.setBounds(13, 226, 205, 10);
		panel_left.add(separator_2);

		JSeparator separator_3 = new JSeparator();
		separator_3.setBounds(12, 180, 205, 10);
		panel_left.add(separator_3);

		JSeparator separator_6 = new JSeparator();
		separator_6.setBounds(12, 272, 205, 10);
		panel_left.add(separator_6);

		JSeparator separator_7 = new JSeparator();
		separator_7.setBounds(12, 318, 205, 10);
		panel_left.add(separator_7);

		JSeparator separator_8 = new JSeparator();
		separator_8.setBounds(12, 364, 205, 10);
		panel_left.add(separator_8);

		JSeparator separator_9 = new JSeparator();
		separator_9.setBounds(12, 89, 205, 10);
		panel_left.add(separator_9);

		JSeparator separator_8_1 = new JSeparator();
		separator_8_1.setBounds(12, 410, 205, 10);
		panel_left.add(separator_8_1);

		JSeparator separator_8_1_1 = new JSeparator();
		separator_8_1_1.setBounds(12, 456, 205, 10);
		panel_left.add(separator_8_1_1);

		themHoadonPanel = new PanelLaphoadon(this.nv);
		hoadonPanel = new PanelQuanlyHoadon(this.nv);
		khachhangPanel = new PanelQuanlyKhachhang();
		nhanvienPanel = new PanelQuanlyNhanvien();
		sanphamPanel = new PanelQuanlySanpham();
		thongkePanel = new PanelThongke(this.nv);
		panelAbout = new PanelAbout();

	}

	private void showHomePage() {
		panel_main.removeAll();
		panel_main.add(lblTitle2);
		panel_main.add(lblTitle1);

		MyJLabel lblNewLabel = new MyJLabel("");
		lblNewLabel.setBounds(0, 0, 1674, 1021);
		lblNewLabel.setIcon(bg_img);
		panel_main.add(lblNewLabel);
		panel_main.repaint();
		panel_main.revalidate();
	}

	private void changePanel(JPanel p) {
		panel_main.removeAll();
		panel_main.add(p, BorderLayout.CENTER);
		panel_main.repaint();
		panel_main.revalidate();
	}

	public void changePanelKhachhang() {
		khachhangPanel.xoaTrang();
		panel_main.removeAll();
		panel_main.add(khachhangPanel, BorderLayout.CENTER);
		reloadColor();
		lblQLKH.setForeground(Color.BLUE);
		panel_main.repaint();
		panel_main.revalidate();
	}

	public void changePanelLaphoadon(Khachhang khachhang) {
		themHoadonPanel = new PanelLaphoadon(nv, khachhang);
		panel_main.removeAll();
		panel_main.add(themHoadonPanel, BorderLayout.CENTER);
		reloadColor();
		lblLapHD.setForeground(Color.BLUE);
		panel_main.repaint();
		panel_main.revalidate();
	}

	private void doiMK() {
		FrameDoimatkhau dialogDoiMK = new FrameDoimatkhau(nv);
		if (dialogDoiMK.getOption() == JOptionPane.OK_OPTION) {
			DaoTaikhoan taikhoanDAO = new DaoTaikhoan();
			Taikhoan taiKhoan = new Taikhoan(nv.getManhanvien(), dialogDoiMK.getMKmoi());
			if (taikhoanDAO.capNhatTaiKhoan(taiKhoan)) {
				JOptionPane.showMessageDialog(this, "Đổi mật khẩu thành công", "Đổi mật khẩu",
						JOptionPane.INFORMATION_MESSAGE);
			} else
				JOptionPane.showMessageDialog(this, "Đổi mật khẩu không thành công", "Đổi mật khẩu",
						JOptionPane.ERROR_MESSAGE);
		}
	}

	private void reloadColor() {
		lblLapHD.setForeground(Color.BLACK);
		lblAbout.setForeground(Color.BLACK);
		lblQLHD.setForeground(Color.BLACK);
		lblQLKH.setForeground(Color.BLACK);
		lblQLNV.setForeground(Color.BLACK);
		lblQLSP.setForeground(Color.BLACK);
		lblSaoLuu.setForeground(Color.BLACK);
		lblThongKe.setForeground(Color.BLACK);
		lblMenu.setForeground(Color.BLACK);
		this.repaint();
		this.revalidate();
	}

	/*
	 * 
	 */
	@Override
	public void mouseClicked(MouseEvent e) {
		Object o = e.getSource();
		if (o.equals(lblDoiMK))
			doiMK();
		else if (o.equals(lblLapHD)) {
			changePanel(themHoadonPanel);
			reloadColor();
			lblLapHD.setForeground(Color.BLUE);
		} else if (o.equals(lblAbout)) {
			changePanel(panelAbout);
			reloadColor();
			lblAbout.setForeground(Color.BLUE);
		} else if (o.equals(lblQLHD)) {
			changePanel(hoadonPanel);
			reloadColor();
			lblQLHD.setForeground(Color.BLUE);
		} else if (o.equals(lblQLKH)) {
			changePanel(khachhangPanel);
			reloadColor();
			lblQLKH.setForeground(Color.BLUE);
		} else if (o.equals(lblQLNV)) {
			if (quanly) {
				changePanel(nhanvienPanel);
				reloadColor();
				lblQLNV.setForeground(Color.BLUE);
			} else {
				JOptionPane.showMessageDialog(this, "Chỉ quản lý mới có thể thực hiện chức năng này", "Thông báo",
						JOptionPane.INFORMATION_MESSAGE);
			}

		} else if (o.equals(lblQLSP)) {
			changePanel(sanphamPanel);
			reloadColor();
			lblQLSP.setForeground(Color.BLUE);
		} else if (o.equals(lblSaoLuu)) {
			final JDialog frame = new JDialog(this, "Sao lưu dữ liệu", true);
			try {
				frame.setIconImage(ImageIO.read(new File("img/code1.png")));
			} catch (IOException exception) {
			}
			JPanel panel = new PanelSaoluu();
			frame.getContentPane().add(panel);
			frame.setSize(panel.getWidth(), panel.getHeight());
			frame.setLocationRelativeTo(this);
			frame.setVisible(true);
		} else if (o.equals(lblThongKe)) {
			changePanel(thongkePanel);
			reloadColor();
			lblThongKe.setForeground(Color.BLUE);
		} else if (o.equals(lblMenu)) {
			showHomePage();
			reloadColor();
		} else if (o.equals(lblDangXuat)) {
			this.dispose();
			new FrameDangnhap().setVisible(true);
		} else if (o.equals(lblHuongdan)) {
			if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
				try {
					Path currentRelativePath = Paths.get("");
					String path = currentRelativePath.toAbsolutePath().toString() + "\\huongdansudung\\html\\main.html";
					path = path.replaceAll("\\\\", "/");
					Desktop.getDesktop().browse(new URI(path));
				} catch (IOException | URISyntaxException e1) {
				}
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {

	}
}
