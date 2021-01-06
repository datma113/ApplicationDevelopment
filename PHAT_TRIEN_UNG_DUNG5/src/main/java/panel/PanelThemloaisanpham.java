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
import dao.DaoLoaisanpham;
import entity.Loaisanpham;

public class PanelThemloaisanpham extends JPanel implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private MyJTextField tfTen;
	private MyJTextField tfMa;

	private MyJButton btnTiep;
	private MyJButton btnBack;
	private Loaisanpham lsp;
	private DaoLoaisanpham dao;

	public PanelThemloaisanpham() {
		setSize(600, 407);
		setLayout(null);

		dao = new DaoLoaisanpham();

		JPanel panel_1 = new JPanel(null);
		panel_1.setBorder(BorderFactory.createEtchedBorder());
		panel_1.setLocation(10, 11);
		panel_1.setSize(562, 271);

		MyJLabel lblMKh = new MyJLabel("Mã loại");
		lblMKh.setBounds(25, 39, 139, 20);
		panel_1.add(lblMKh);

		MyJLabel lblHTn = new MyJLabel("Tên loại");
		lblHTn.setBounds(25, 86, 139, 20);
		panel_1.add(lblHTn);

		tfTen = new MyJTextField("");
		tfTen.setBounds(124, 86, 405, 23);
		panel_1.add(tfTen);

		tfMa = new MyJTextField("");
		tfMa.setBounds(124, 39, 405, 23);
		tfMa.setEnabled(false);
		tfMa.setText(dao.getNextMaLSP());
		panel_1.add(tfMa);
		add(panel_1);

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

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource();
		if (o.equals(btnBack)) {
			JDialog frame = (JDialog) SwingUtilities.getWindowAncestor(this);
			frame.dispose();
		} else if (o.equals(btnTiep)) {
			lsp = getDataFromTextField();
			if (lsp != null) {
				JDialog frame = (JDialog) SwingUtilities.getWindowAncestor(this);
				frame.dispose();
			}
		} else if (o.equals(tfTen) && !tfTen.getText().trim().equals("")) {
			btnTiep.doClick();
		}
	}

	private Loaisanpham getDataFromTextField() {
		if (validInputTextField()) {
			Loaisanpham lsp = new Loaisanpham();
			lsp.setMaloai(tfMa.getText().trim());
			lsp.setTenloai(tfTen.getText().trim());
			return lsp;
		}
		return null;
	}

	private boolean validInputTextField() {
		if (!tfTen.getText().trim().matches("[\\p{L}\\s0-9]+")) {
			JOptionPane.showMessageDialog(this, "Tên loại sản phẩm chỉ chứa chữ, có thể có số", "Thông báo",
					JOptionPane.ERROR_MESSAGE);
			tfTen.selectAll();
			tfTen.requestFocus();
			return false;
		}
		return true;
	}

	public Loaisanpham getLsp() {
		return lsp;
	}

}
