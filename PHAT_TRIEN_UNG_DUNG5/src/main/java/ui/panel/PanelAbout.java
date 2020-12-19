package ui.panel;

import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

import custom.CustomJLabel;

public class PanelAbout extends JPanel {

	private JPanel pnlMain;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PanelAbout() {
		setSize(1680, 1021);
		setLayout(null);

		setBackground(Color.WHITE);

		CustomJLabel lblThongKe = new CustomJLabel("About");
		lblThongKe.setBounds(10, 0, 1670, 30);
		lblThongKe.setHorizontalAlignment(SwingConstants.CENTER);
		lblThongKe.setForeground(Color.RED);
		lblThongKe.setFont(new Font("Serif", Font.BOLD, 20));
		add(lblThongKe);

		pnlMain = new JPanel(null);
		pnlMain.setBounds(10, 30, 1640, 968);
		add(pnlMain);

		JTextArea textArea = new JTextArea();
		textArea.setWrapStyleWord(true);
		textArea.setLineWrap(true);
		textArea.setEditable(false);
		textArea.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
		Font f = textArea.getFont();
		textArea.setFont(new Font(f.getName(), Font.BOLD, f.getSize() + 5));
		textArea.append("Hệ thống quản lý cửa hàng mua bán linh kiện máy vi tính Thành Đạt");
		textArea.append("\n");
		textArea.append("\nThông tin về nhóm phát triển:");
		textArea.append("\nMai Kiên Cường - 18089811");
		textArea.append("\nTrương Công Cường - 18084791:");
		textArea.append("\nNguyễn Công Thành Đạt - 0000000:");
		textArea.append("\nLê Ngọc Tồn - 00000000:");

		JScrollPane scrollPane = new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setBounds(10, 11, 1620, 946);
		scrollPane.setBorder(BorderFactory.createLineBorder(Color.BLUE, 1));
		pnlMain.add(scrollPane);

	}
}
