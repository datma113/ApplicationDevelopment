package ui.frame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.font.TextAttribute;
import java.io.File;
import java.io.IOException;
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

import custom.CustomJLabel;
import dao.DaoTaikhoan;
import entity.Nhanvien;
import entity.Taikhoan;
import ui.panel.PanelAbout;
import ui.panel.PanelLaphoadon;
import ui.panel.PanelQuanlyHoadon;
import ui.panel.PanelQuanlyKhachhang;
import ui.panel.PanelQuanlyNhanvien;
import ui.panel.PanelQuanlySanpham;
import ui.panel.PanelSaoluu;
import ui.panel.PanelThongke;

public class FrameTrangchu extends JFrame implements MouseListener {

	/**
	* 
	**/
	private static final long serialVersionUID = 1L;

	private CustomJLabel lblQLSP;
	private CustomJLabel lblQLNV;
	private CustomJLabel lblQLKH;
	private CustomJLabel lblQLHD;
	private CustomJLabel lblLapHD;
	private CustomJLabel lblDoiMK;
	private CustomJLabel lblSaoLuu;
	private CustomJLabel lblThongKe;
	private CustomJLabel lblDangXuat;
	private CustomJLabel lblMenu;
	private CustomJLabel lblTitle2;
	private CustomJLabel lblTitle1;
	private CustomJLabel lblAbout;

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
		setSize(1920, 1050);
		setResizable(false);
		setLocationRelativeTo(null);
		getContentPane().setLayout(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		setTitle("HỆ THỐNG QUẢN LÝ CỬA HÀNG BÁN LINH KIỆN MÁY VI TÍNH THÀNH ĐẠT");
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

		lblTitle2 = new CustomJLabel("LINH KIỆN MÁY VI TÍNH THÀNH ĐẠT");
		lblTitle2.setBounds(10, 469, 1654, 50);
		lblTitle2.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitle2.setForeground(Color.BLUE);
		lblTitle2.setFont(new Font("Serif", Font.BOLD, 30));

		lblTitle1 = new CustomJLabel("HỆ THỐNG QUẢN LÝ CỬA HÀNG BÁN");
		lblTitle1.setBounds(10, 408, 1654, 50);
		lblTitle1.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitle1.setForeground(Color.BLUE);
		lblTitle1.setFont(new Font("Serif", Font.BOLD, 30));

		showHomePage();

		lblMenu = new CustomJLabel("");
		lblMenu.addMouseListener(this);
		lblMenu.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		lblMenu.setIcon(new ImageIcon("img\\home.png"));
		lblMenu.setBounds(10, 11, 37, 32);
		panel_left.add(lblMenu);

		lblQLSP = new CustomJLabel("Quản Lý Sản Phẩm");
		lblQLSP.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		lblQLSP.setIcon(new ImageIcon("img\\loaisp.png"));
		lblQLSP.setBounds(10, 99, 218, 35);
		panel_left.add(lblQLSP);

		lblQLNV = new CustomJLabel("Quản Lý Nhân Viên");
		lblQLNV.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		lblQLNV.setIcon(new ImageIcon("img\\nhanvien.png"));
		lblQLNV.setBounds(10, 191, 218, 35);
		panel_left.add(lblQLNV);

		lblQLHD = new CustomJLabel("Quản Lý Hóa Đơn");
		lblQLHD.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		lblQLHD.setIcon(new ImageIcon("img\\bill.png"));
		lblQLHD.setBounds(10, 237, 218, 35);
		panel_left.add(lblQLHD);

		CustomJLabel lblThongTinNV = new CustomJLabel(nv.getManhanvien() + " - " + nv.getHodem() + " " + nv.getTen());
		lblThongTinNV.setHorizontalAlignment(SwingConstants.CENTER);
		lblThongTinNV.setBounds(10, 867, 220, 30);
		panel_left.add(lblThongTinNV);

		CustomJLabel lblChucVu = new CustomJLabel(nv.getChucvu());
		lblChucVu.setHorizontalAlignment(SwingConstants.CENTER);
		lblChucVu.setBounds(10, 898, 220, 30);
		panel_left.add(lblChucVu);

		lblAbout = new CustomJLabel("About");
		lblAbout.setHorizontalAlignment(SwingConstants.CENTER);
		lblAbout.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		lblAbout.setBounds(10, 971, 220, 30);
		panel_left.add(lblAbout);

		lblSaoLuu = new CustomJLabel("Sao lưu dữ liệu");
		lblSaoLuu.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		lblSaoLuu.setIcon(new ImageIcon("img\\backup.png"));
		lblSaoLuu.setBounds(10, 329, 218, 35);
		panel_left.add(lblSaoLuu);

		lblQLKH = new CustomJLabel("Quản Lý Khách Hàng");
		lblQLKH.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		lblQLKH.setIcon(new ImageIcon("img\\khachhang.png"));
		lblQLKH.setBounds(10, 145, 218, 35);
		panel_left.add(lblQLKH);

		lblThongKe = new CustomJLabel("Thống Kê");
		lblThongKe.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		lblThongKe.setIcon(new ImageIcon("img\\thongke.png"));
		lblThongKe.setBounds(10, 283, 218, 35);
		panel_left.add(lblThongKe);

		lblLapHD = new CustomJLabel("Lập Hóa Đơn Mới");
		lblLapHD.setIcon(new ImageIcon("img\\laphoadon.png"));
		lblLapHD.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		lblLapHD.setBounds(10, 54, 218, 35);
		panel_left.add(lblLapHD);

		lblDoiMK = new CustomJLabel("Đổi MK");
		lblDoiMK.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		lblDoiMK.setForeground(Color.BLUE);
		lblDoiMK.setBounds(48, 930, 60, 30);
		panel_left.add(lblDoiMK);

		lblDangXuat = new CustomJLabel("Đăng Xuất");
		lblDangXuat.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		lblDangXuat.setForeground(Color.RED);
		lblDangXuat.setBounds(129, 930, 71, 30);
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
		lblAbout.setFont(font.deriveFont(attributes));

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

		themHoadonPanel = new PanelLaphoadon(this.nv);
		hoadonPanel = new PanelQuanlyHoadon(this.nv);
		khachhangPanel = new PanelQuanlyKhachhang();
		nhanvienPanel = new PanelQuanlyNhanvien();
		sanphamPanel = new PanelQuanlySanpham();
		thongkePanel = new PanelThongke();
		panelAbout = new PanelAbout();

	}

	private void showHomePage() {
		panel_main.removeAll();
		panel_main.add(lblTitle2);
		panel_main.add(lblTitle1);

		CustomJLabel lblNewLabel = new CustomJLabel("");
		lblNewLabel.setBounds(0, 0, 1674, 1021);
		lblNewLabel.setIcon(bg_img);
		panel_main.add(lblNewLabel);
		panel_main.repaint();
		panel_main.revalidate();
	}

	protected void changePanel(JPanel p) {
		panel_main.removeAll();
		panel_main.add(p, BorderLayout.CENTER);
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
			changePanel(nhanvienPanel);
			reloadColor();
			lblQLNV.setForeground(Color.BLUE);
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
			reloadColor();
			lblSaoLuu.setForeground(Color.BLUE);
		} else if (o.equals(lblThongKe)) {
			changePanel(thongkePanel);
			reloadColor();
			lblThongKe.setForeground(Color.BLUE);
		} else if (o.equals(lblMenu)) {
			showHomePage();
		} else if (o.equals(lblDangXuat)) {
			this.dispose();
			new FrameDangnhap().setVisible(true);
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
