package ui.panel;

import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import custom.CustomJButton;
import custom.CustomJLabel;
import custom.CustomJTextField;
import dao.DaoNhacungcap;
import entity.Nhacungcap;

public class PanelThemnhacungcap extends JPanel implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private CustomJTextField tfTen;
	private CustomJTextField tfMa;
	private CustomJTextField tfEmail;
	private CustomJTextField tfDiachi;

	private CustomJButton btnTiep;
	private CustomJButton btnBack;
	private Nhacungcap ncc;
	private DaoNhacungcap dao;

	public PanelThemnhacungcap() {
		setSize(600, 407);
		setLayout(null);
		dao = new DaoNhacungcap();

		JPanel panel_1 = new JPanel(null);
		panel_1.setBorder(BorderFactory.createEtchedBorder());
		panel_1.setLocation(10, 11);
		panel_1.setSize(562, 271);

		CustomJLabel lblMKh = new CustomJLabel("Mã");
		lblMKh.setBounds(25, 39, 150, 20);
		panel_1.add(lblMKh);

		CustomJLabel lblHTn = new CustomJLabel("Tên");
		lblHTn.setBounds(25, 86, 150, 20);
		panel_1.add(lblHTn);

		tfTen = new CustomJTextField("");
		tfTen.setBounds(124, 86, 405, 23);
		panel_1.add(tfTen);

		tfMa = new CustomJTextField("");
		tfMa.setBounds(124, 39, 405, 23);
		tfMa.setEnabled(false);
		tfMa.setText(dao.getNextMaNCC());
		panel_1.add(tfMa);

		CustomJLabel lblSt = new CustomJLabel("Email");
		lblSt.setBounds(25, 135, 150, 20);
		panel_1.add(lblSt);

		tfEmail = new CustomJTextField("");
		tfEmail.setBounds(124, 135, 405, 23);
		panel_1.add(tfEmail);

		tfDiachi = new CustomJTextField("");
		tfDiachi.setBounds(124, 187, 405, 23);
		panel_1.add(tfDiachi);
		add(panel_1);

		CustomJLabel lblTmNhanhTheo_1_1 = new CustomJLabel("Địa chỉ");
		lblTmNhanhTheo_1_1.setBounds(25, 187, 150, 20);
		panel_1.add(lblTmNhanhTheo_1_1);

		JPanel panel_2 = new JPanel();
		panel_2.setBorder(BorderFactory.createEtchedBorder());
		panel_2.setBounds(10, 293, 562, 60);
		add(panel_2);
		panel_2.setLayout(null);

		btnTiep = new CustomJButton("Tiếp tục");
		btnTiep.setIcon(new ImageIcon("img/save.png"));
		btnTiep.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnTiep.setBounds(105, 11, 115, 35);
		panel_2.add(btnTiep);

		btnBack = new CustomJButton("Quay lại");
		btnBack.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnBack.setIcon(new ImageIcon("img/back.png"));
		btnBack.setBounds(345, 11, 115, 35);
		panel_2.add(btnBack);

		btnTiep.addActionListener(this);
		btnBack.addActionListener(this);
		tfTen.addActionListener(this);
		tfEmail.addActionListener(this);
		tfDiachi.addActionListener(this);

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource();
		if (o.equals(btnBack)) {
			JDialog frame = (JDialog) SwingUtilities.getWindowAncestor(this);
			frame.dispose();
		} else if (o.equals(btnTiep)) {
			ncc = getDataFromTextField();
			if (ncc != null) {
				JDialog frame = (JDialog) SwingUtilities.getWindowAncestor(this);
				frame.dispose();
			}
		} else if (o.equals(tfTen) && !tfTen.getText().trim().equals("")) {
			tfEmail.requestFocus();
		} else if (o.equals(tfEmail) && !tfEmail.getText().trim().equals("")) {
			tfDiachi.requestFocus();
		} else if (o.equals(tfDiachi) && !tfDiachi.getText().trim().equals("")) {
			btnTiep.doClick();
		}
	}

	private Nhacungcap getDataFromTextField() {
		if (validInputTextField()) {
			Nhacungcap ncc = new Nhacungcap();
			ncc.setManhacungcap(tfMa.getText().trim());
			ncc.setTennhacungcap(tfTen.getText().trim());
			ncc.setEmail(tfEmail.getText().trim());
			ncc.setDiachi(tfDiachi.getText().trim());
			return ncc;
		}
		return null;
	}

	private boolean validInputTextField() {
		if (!tfTen.getText().trim().matches("[\\p{L}\\s0-9]+")) {
			JOptionPane.showMessageDialog(this, "Tên nhà cung cấp chỉ chứa chữ, có thể có số", "Thông báo",
					JOptionPane.ERROR_MESSAGE);
			tfTen.selectAll();
			tfTen.requestFocus();
			return false;
		}
		if (!tfEmail.getText().trim().matches("[a-zA-Z][a-zA-Z0-9]+")) {
			JOptionPane.showMessageDialog(this, "email phải bắt đầu bằng chữ, sau đó là số hoặc chữ", "Thông báo",
					JOptionPane.ERROR_MESSAGE);
			tfEmail.selectAll();
			tfEmail.requestFocus();
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

	public Nhacungcap getNcc() {
		return ncc;
	}

}
