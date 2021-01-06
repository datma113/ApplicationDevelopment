package panel;

import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import custom.MyJButton;
import custom.MyJLabel;
import custom.MyJTextField;
import dao.DaoKhachhang;
import entity.Khachhang;

public class PanelThemkhachhang extends JPanel implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private MyJTextField tfTen;
	private MyJTextField tfMa;
	private MyJTextField tfSdt;
	private MyJTextField tfDiachi;

	private MyJButton btnTiep;
	private MyJButton btnBack;
	private Khachhang kh;
	private DaoKhachhang daoKh;

	public PanelThemkhachhang() {
		setSize(600, 407);
		setLayout(null);

		daoKh = new DaoKhachhang();

		JPanel panel_1 = new JPanel(null);
		panel_1.setBorder(BorderFactory.createEtchedBorder());
		panel_1.setLocation(10, 11);
		panel_1.setSize(562, 271);

		MyJLabel lblMKh = new MyJLabel("Mã KH");
		lblMKh.setBounds(25, 39, 138, 20);
		panel_1.add(lblMKh);

		MyJLabel lblHTn = new MyJLabel("Họ tên");
		lblHTn.setBounds(25, 86, 138, 20);
		panel_1.add(lblHTn);

		tfTen = new MyJTextField("");
		tfTen.setBounds(124, 86, 405, 23);
		panel_1.add(tfTen);

		tfMa = new MyJTextField("");
		tfMa.setBounds(124, 39, 405, 23);
		tfMa.setEnabled(false);
		tfMa.setText(daoKh.getNextMaKH());
		panel_1.add(tfMa);

		MyJLabel lblSt = new MyJLabel("SĐT");
		lblSt.setBounds(25, 135, 138, 20);
		panel_1.add(lblSt);

		tfSdt = new MyJTextField("");
		tfSdt.setBounds(124, 135, 405, 23);
		panel_1.add(tfSdt);

		tfDiachi = new MyJTextField("");
		tfDiachi.setBounds(124, 187, 405, 23);
		panel_1.add(tfDiachi);
		add(panel_1);

		MyJLabel lblTmNhanhTheo_1_1 = new MyJLabel("Địa chỉ");
		lblTmNhanhTheo_1_1.setBounds(25, 187, 138, 20);
		panel_1.add(lblTmNhanhTheo_1_1);

		JPanel panel_2 = new JPanel();
		panel_2.setBorder(BorderFactory.createEtchedBorder());
		panel_2.setBounds(10, 293, 562, 60);
		add(panel_2);
		panel_2.setLayout(null);

		btnTiep = new MyJButton("Tiếp tục");
		btnTiep.setIcon(new ImageIcon("img/save.png"));
		btnTiep.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnTiep.setBounds(105, 11, 115, 35);
		panel_2.add(btnTiep);

		btnBack = new MyJButton("Quay lại");
		btnBack.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnBack.setIcon(new ImageIcon("img/back.png"));
		btnBack.setBounds(345, 11, 115, 35);
		panel_2.add(btnBack);

		btnTiep.addActionListener(this);
		btnBack.addActionListener(this);
		tfTen.addActionListener(this);
		tfSdt.addActionListener(this);
		tfDiachi.addActionListener(this);

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource();
		if (o.equals(btnBack)) {
			JDialog frame = (JDialog) SwingUtilities.getWindowAncestor(this);
			frame.dispose();
		} else if (o.equals(btnTiep)) {
			kh = getDataFromTextField();
			if (kh != null) {
				JDialog frame = (JDialog) SwingUtilities.getWindowAncestor(this);
				frame.dispose();
			}
		} else if (o.equals(tfTen) && !tfTen.getText().trim().equals("")) {
			tfSdt.requestFocus();
		} else if (o.equals(tfSdt) && !tfSdt.getText().trim().equals("")) {
			tfDiachi.requestFocus();
		} else if (o.equals(tfDiachi) && !tfDiachi.getText().trim().equals("")) {
			btnTiep.doClick();
		}
	}

	private Khachhang getDataFromTextField() {
		if (validInputTextField()) {
			Khachhang kh = new Khachhang();
			kh.setMakhachhang(tfMa.getText().trim());
			kh.setHoten(tfTen.getText().trim());
			kh.setDiachi(tfDiachi.getText().trim());
			kh.setSodienthoai(tfSdt.getText().trim());
			return kh;
		}
		return null;
	}

	private boolean validInputTextField() {
		if (!tfTen.getText().trim().matches("[\\p{L}\\s]+")) {
			JOptionPane.showMessageDialog(this, "Họ tên khách chỉ chứa chữ, không chứa số hay kí tự đặc biệt",
					"Thông báo", JOptionPane.ERROR_MESSAGE);
			tfTen.selectAll();
			tfTen.requestFocus();
			return false;
		}
		if (!tfSdt.getText().trim().matches("[0-9()+-]+")) {
			JOptionPane.showMessageDialog(this, "SĐT phải là số, có thể bao gồm các kí tự ( ) + -", "Thông báo",
					JOptionPane.ERROR_MESSAGE);
			tfSdt.selectAll();
			tfSdt.requestFocus();
			return false;
		}
		if (!tfDiachi.getText().trim().matches("[\\p{L}\\s0-9()\\/_\\\\.,\\+-]+")) {
			JOptionPane.showMessageDialog(this, "Địa chỉ chỉ bao gồm chữ, số, và các kí tự ( ) \\ / _ , . + -",
					"Thông báo", JOptionPane.ERROR_MESSAGE);
			tfDiachi.selectAll();
			tfDiachi.requestFocus();
			return false;
		}
		return true;
	}

	public Khachhang getKh() {
		return kh;
	}

}
