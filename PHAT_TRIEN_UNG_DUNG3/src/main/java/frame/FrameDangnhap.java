package frame;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import custom.MyJButton;
import custom.MyJLabel;
import custom.MyJPanel;
import custom.MyJPasswordField;
import custom.MyJTextField;
import dao.DaoTaikhoan;
import entity.Nhanvien;
import entity.Taikhoan;
import panel.PanelPhuchoi;

public class FrameDangnhap extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;

	private MyJTextField tfUser;
	private MyJPasswordField tfPass;
	private MyJButton btnDangNhap;
	private MyJButton btnThoat;
	private JProgressBar progressBar;

	private DaoTaikhoan dao;
	private Nhanvien nv;
	private boolean droped = false;

	@SuppressWarnings("unchecked")
	public FrameDangnhap() {
		try {
			setIconImage(ImageIO.read(new File("img\\code1.png")));
		} catch (IOException e) {
		}
		setAlwaysOnTop(true);
		setSize(500, 469);
		setResizable(false);
		setTitle("Đăng Nhập");
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		MyJLabel lblPhuchoi = new MyJLabel("Phục hồi dữ liệu");
		lblPhuchoi.setHorizontalAlignment(SwingConstants.CENTER);
		lblPhuchoi.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		lblPhuchoi.setForeground(Color.BLUE);
		lblPhuchoi.setBounds(66, 371, 350, 26);

		JPanel contentPane = new JPanel();
		contentPane.setLayout(null);

		try {
			dao = new DaoTaikhoan();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Không thể kết nối đến cơ sở dữ liệu", "Đăng nhập",
					JOptionPane.ERROR_MESSAGE);
			droped = true;
		}

		JFrame window = (JFrame) SwingUtilities.getWindowAncestor(this);
		lblPhuchoi.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				final JDialog frame = new JDialog(window, "Phục hồi dữ liệu", true);
				try {
					frame.setIconImage(ImageIO.read(new File("img/code1.png")));
				} catch (IOException exception) {
				}
				JPanel panel = new PanelPhuchoi(droped);
				frame.setAlwaysOnTop(true);
				frame.getContentPane().add(panel);
				frame.setSize(panel.getWidth(), panel.getHeight());
				frame.setLocationRelativeTo(window);
				frame.setVisible(true);
			}
		});

		tfUser = new MyJTextField("NV00000001");
		tfPass = new MyJPasswordField("123");
		btnDangNhap = new MyJButton("Đăng nhập");
		btnThoat = new MyJButton("Thoát");

		MyJPanel p = new MyJPanel();
		p.setLayout(null);
		p.setBounds(66, 87, 350, 273);
		p.setOpaque(false);

		btnThoat.setIcon(new ImageIcon("img/cancel.png"));
		btnDangNhap.setIcon(new ImageIcon("img/save.png"));

		MyJLabel lblDangNhap = new MyJLabel("Đăng Nhập");
		lblDangNhap.setHorizontalAlignment(SwingConstants.CENTER);
		lblDangNhap.setFont(new Font("serif", Font.BOLD, 30));
		lblDangNhap.setForeground(new Color(0, 153, 255));
		lblDangNhap.setBounds(10, 10, 330, 50);
		p.add(lblDangNhap);

		MyJLabel lblUser = new MyJLabel("Tên TK");
		lblUser.setBounds(40, 75, 117, 25);
		p.add(lblUser);

		MyJLabel lblpass = new MyJLabel("Mật Khẩu");
		lblpass.setBounds(40, 125, 117, 25);
		p.add(lblpass);

		MyJLabel lblTitle1 = new MyJLabel("HỆ THỐNG QUẢN LÝ CỬA HÀNG");
		lblTitle1.setForeground(Color.BLUE);
		lblTitle1.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitle1.setFont(new Font("Serif", Font.BOLD, 20));
		lblTitle1.setBounds(10, 11, 471, 41);
		contentPane.add(lblTitle1);

		MyJLabel lblTitle2 = new MyJLabel("LINH KIỆN MÁY TÍNH THÀNH ĐẠT");
		lblTitle2.setForeground(Color.BLUE);
		lblTitle2.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitle2.setFont(new Font("Serif", Font.BOLD, 20));
		lblTitle2.setBounds(10, 40, 471, 41);

		Font font = lblPhuchoi.getFont();
		@SuppressWarnings("rawtypes")
		Map attributes = font.getAttributes();
		attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
		lblPhuchoi.setFont(font.deriveFont(attributes));
		contentPane.add(lblPhuchoi);

		tfUser.setBounds(110, 75, 200, 25);
		tfUser.setOpaque(false);
		p.add(tfUser);

		tfPass.setBounds(110, 125, 200, 25);
		tfPass.setOpaque(false);
		p.add(tfPass);

		btnDangNhap.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnDangNhap.setBounds(27, 175, 130, 35);
		p.add(btnDangNhap);

		btnThoat.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnThoat.setBounds(194, 175, 130, 35);
		p.add(btnThoat);

		progressBar = new JProgressBar();
		progressBar.setBorderPainted(false);
		progressBar.setBounds(1, 419, 482, 10);
		progressBar.setForeground(new Color(6, 176, 37));
		contentPane.add(progressBar);
		contentPane.add(p);
		contentPane.add(lblTitle2);
		setContentPane(contentPane);

		tfUser.addActionListener(this);
		tfPass.addActionListener(this);
		btnDangNhap.addActionListener(this);
		btnThoat.addActionListener(this);

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource();
		if (o.equals(btnDangNhap)) {
			if (validInput()) {
				String user = tfUser.getText().trim().toUpperCase();
				String pass = new String(tfPass.getPassword());
				try {
					Taikhoan taiKhoan = dao.getTaikhoan(user, pass);
					if (taiKhoan != null) {
						nv = taiKhoan.getNhanvien();
						DangnhapTask task = new DangnhapTask(this, nv);
						task.addPropertyChangeListener(evt -> {
							if ("progress".equals(evt.getPropertyName()))
								progressBar.setValue((int) evt.getNewValue());
						});
						task.execute();
					} else {
						JOptionPane.showMessageDialog(this,
								"Đăng nhập không thành công. Mật khẩu hoặc tài khoản không đúng", "Đăng nhập",
								JOptionPane.ERROR_MESSAGE);
					}
				} catch (Exception e2) {
					JOptionPane.showMessageDialog(this, "Không thể kết nối đến cơ sở dữ liệu", "Đăng nhập",
							JOptionPane.ERROR_MESSAGE);
				}

			}
		} else if (o.equals(btnThoat)) {
			int option = JOptionPane.showConfirmDialog(getContentPane(), "Bạn có muốn thoát không?", "Thoát",
					JOptionPane.YES_NO_OPTION);
			if (option == JOptionPane.YES_OPTION)
				System.exit(1);
		} else if (o.equals(tfUser)) {
			tfPass.requestFocus();
		} else if (o.equals(tfPass)) {
			btnDangNhap.doClick();
		}
	}

	private boolean validInput() {
		if (tfUser.getText().equals("")) {
			JOptionPane.showMessageDialog(getContentPane(), "Chưa nhập tên tài khoản", "Đăng nhập",
					JOptionPane.ERROR_MESSAGE);
			tfUser.requestFocus();
			return false;
		}
		if (new String(tfPass.getPassword()).equals("")) {
			JOptionPane.showMessageDialog(getContentPane(), "Chưa nhập mật khẩu", "Đăng nhập",
					JOptionPane.ERROR_MESSAGE);
			tfPass.requestFocus();
			return false;
		}
		return true;
	}
}

/**
 * @author kienc
 *
 */
class DangnhapTask extends SwingWorker<Void, Integer> {

	private JFrame frameStart;
	private Nhanvien nhanvien;

	public DangnhapTask(JFrame frameStart, Nhanvien nhanvien) {
		super();
		this.frameStart = frameStart;
		this.nhanvien = nhanvien;
	}

	@Override
	protected Void doInBackground() throws Exception {
		new Thread(() -> {
			for (int i = 0; i <= 100; i++) {
				setProgress(i);
				try {
					Thread.sleep(15);
				} catch (InterruptedException e) {
				}
			}
		}).start();
		new FrameTrangchu(nhanvien).setVisible(true);
		return null;
	}

	@Override
	protected void done() {
		frameStart.dispose();
	}
}
