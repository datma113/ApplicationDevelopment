package frame;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import custom.MyJLabel;
import entity.Nhanvien;

public class FrameDoimatkhau {

	private JTextField tfTenDN;
	private JPasswordField tfMKmoi;
	private JPasswordField tfConfirmMKmoi;
	private MyJLabel lblMess;
	private int option;
	private String MKmoi = "";
	private Nhanvien nv;

	public Nhanvien getNv() {
		return nv;
	}

	public void setNv(Nhanvien nv) {
		this.nv = nv;
	}

	public String getMKmoi() {
		return MKmoi;
	}

	public int getOption() {
		return option;
	}

	public FrameDoimatkhau(Nhanvien nv) {
		setNv(nv);
		tfTenDN = new JTextField();
		Font f = tfTenDN.getFont();
		tfTenDN.setFont(new Font(f.getName(), f.getStyle(), f.getSize() + 4));
		tfMKmoi = new JPasswordField();
		tfMKmoi.setFont(new Font(f.getName(), f.getStyle(), f.getSize() + 4));
		tfConfirmMKmoi = new JPasswordField();
		tfConfirmMKmoi.setFont(new Font(f.getName(), f.getStyle(), f.getSize() + 4));
		lblMess = new MyJLabel("\n");
		tfTenDN.setText(nv.getManhanvien());
		tfTenDN.setEditable(false);
		Object[] obj = { new MyJLabel("Tên Đăng Nhập :"), tfTenDN, new MyJLabel("Mật Khẩu mới :"), tfMKmoi,
				new MyJLabel("Xác Nhận Mật Khẩu Mới :"), tfConfirmMKmoi, lblMess };
		lblMess.setForeground(Color.RED);

		do {
			option = JOptionPane.showConfirmDialog(null, obj, "Đổi mật khẩu", JOptionPane.OK_CANCEL_OPTION);
			if (option != JOptionPane.OK_OPTION) {
				break;
			}
		} while (!validInput());
		MKmoi = String.valueOf(tfConfirmMKmoi.getPassword());
	}

	private boolean validInput() {
		if (String.valueOf(tfMKmoi.getPassword()).trim().equals("")) {
			lblMess.setText("MK mới không được để trống");
			return false;
		}
		if (!String.valueOf(tfConfirmMKmoi.getPassword()).trim().equals(String.valueOf(tfMKmoi.getPassword()).trim())) {
			lblMess.setText("MK mới không khớp");
			return false;
		}
		return true;
	}
}
