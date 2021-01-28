package panel;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import custom.MyJButton;
import custom.MyJScrollPane;
import custom.MyTableCellRender;
import dao.DaoNhacungcap;
import entity.Nhacungcap;
import model.ModelNhacungcap;

public class PanelImportNhacungcap extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JTable table;
	private MyJButton btn;
	private DaoNhacungcap dao;
	private String excelFilePath;
	private JFileChooser fileChooser;
	private ModelNhacungcap model;

	public PanelImportNhacungcap() {
		setSize(940, 727);
		setLayout(null);

		fileChooser = new JFileChooser();

		btn = new MyJButton("Chọn file");
		btn.setLocation(785, 148);
		btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btn.setIcon(new ImageIcon("img\\excel.png"));
		btn.setMnemonic('f');
		btn.setSize(126, 35);

		dao = new DaoNhacungcap();
		model = new ModelNhacungcap();
		table = new JTable(model);
		table.setRowHeight(35);
		table.setAutoCreateRowSorter(true);
		table.getTableHeader().setBackground(new Color(255, 208, 120));
		table.getTableHeader().setPreferredSize(new Dimension(table.getTableHeader().getWidth(), 30));
		Font f3 = table.getTableHeader().getFont();
		table.getTableHeader().setFont(new Font(f3.getName(), Font.BOLD, f3.getSize() + 2));

		MyTableCellRender renderTable = new MyTableCellRender();
		table.setDefaultRenderer(Object.class, renderTable);

		table.getColumnModel().getColumn(0).setMinWidth(190);
		table.getColumnModel().getColumn(1).setMinWidth(400);
		table.getColumnModel().getColumn(2).setMinWidth(220);
		table.getColumnModel().getColumn(3).setMinWidth(432);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

		add(btn);

		MyJScrollPane scrollPane = new MyJScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setBounds(10, 194, 901, 480);
		add(scrollPane);

		JTextArea textArea = new JTextArea(
				"Nhập danh sách nhà cung cấp từ file excel. File phải có định dạng .xls hoặc xlsx. Dòng đầu tiên là tiêu đề của cột, file phải có 3 cột \"tennhacungcap\", \"email\", \"diachi\" lưu tên, email, địa chỉ của nhà cung cấp. Mã nhà cung cấp sẽ được cấp tự động.");
		textArea.append("\n=====================");
		textArea.setWrapStyleWord(true);
		textArea.setLineWrap(true);
		textArea.setEditable(false);
		textArea.setFont(new Font("Serif", Font.PLAIN, 18));
		textArea.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

		MyJScrollPane jscrollPane = new MyJScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		jscrollPane.setBounds(10, 11, 901, 126);
		jscrollPane.setBorder(BorderFactory.createLineBorder(Color.BLUE, 1));
		add(jscrollPane);

		btn.addActionListener(e -> {
			int returnVal = fileChooser.showOpenDialog(this);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				java.io.File file = fileChooser.getSelectedFile();
				excelFilePath = file.getPath();
				textArea.append("\nĐang đọc từ file " + excelFilePath);
				textArea.append("\nVui lòng đợi...");
				new Thread(() -> {
					List<Nhacungcap> list = readExcel(excelFilePath);
					model.setDsncc(list);
					model.fireTableDataChanged();
					if (!list.isEmpty()) {
						textArea.append("\nThêm thành công " + list.size() + " nhà cung cấp");
						textArea.append("\n=====================");
						JOptionPane.showMessageDialog(this, "Thêm thành công " + list.size() + " nhà cung cấp",
								"Nhập File Excel", JOptionPane.INFORMATION_MESSAGE);
					} else {
						textArea.append("\nThêm không thành công");
						textArea.append("\n=====================");
					}
				}).start();
			}
		});
	}

	/**
	 * đọc từ file excel
	 * 
	 * @param excelFilePath
	 * @return
	 */
	private List<Nhacungcap> readExcel(String excelFilePath) {
		List<Nhacungcap> list = new ArrayList<Nhacungcap>();

		InputStream inputStream = null;
		Workbook workbook = null;
		try {
			inputStream = new FileInputStream(new File(excelFilePath));
			workbook = getWorkbook(inputStream, excelFilePath);

			if (workbook != null) {
				Sheet sheet = workbook.getSheetAt(0);

				/*
				 * đọc dòng đầu tiên để lấy dữ liệu về tên cột
				 */
				Map<String, Integer> colMapByName = new HashMap<String, Integer>();
				Iterator<Row> iterator = sheet.iterator();
				if (iterator.hasNext()) {
					Row nextRow = iterator.next();
					if (nextRow.getRowNum() == 0) {
						Iterator<Cell> cellIterator = nextRow.cellIterator();
						int i = 0;
						while (cellIterator.hasNext()) {
							Cell cell = cellIterator.next();
							if (!cell.toString().trim().equals("")) {
								colMapByName.put(cell.toString(), i++);
							}
						}
					}
				}

				/*
				 * đọc dữ liệu
				 */
				Iterator<Row> iterator2 = sheet.iterator();
				while (iterator2.hasNext()) {
					Row nextRow = iterator2.next();
					if (nextRow.getRowNum() != 0) {
						Iterator<Cell> cellIterator = nextRow.cellIterator();
						Nhacungcap ncc = new Nhacungcap(dao.getNextMaNCC());
						while (cellIterator.hasNext()) {
							Cell cell = cellIterator.next();
							if (!cell.toString().trim().equals("")) {
								try {
									if (cell.getColumnIndex() == colMapByName.get("tennhacungcap")) {
										ncc.setTennhacungcap(cell.getStringCellValue());
									}
									if (cell.getColumnIndex() == colMapByName.get("email")) {
										ncc.setEmail(cell.getStringCellValue());
									}
									if (cell.getColumnIndex() == colMapByName.get("diachi")) {
										ncc.setDiachi(cell.getStringCellValue());
									}
								} catch (Exception e) {
									JOptionPane.showMessageDialog(this,
											"Lỗi. Vui lòng kiểm tra lại các cột của file excel", "Nhập File Excel",
											JOptionPane.ERROR_MESSAGE);
									return list;
								}
							}
						}
						if (ncc.getManhacungcap() != null && ncc.getTennhacungcap() != null) {
							if (dao.themNhaCungCap(ncc))
								list.add(ncc);
						}
					}
				}
			}
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(this, "Lỗi. Vui lòng kiểm tra lại file excel", "Nhập File Excel",
					JOptionPane.ERROR_MESSAGE);
		} finally {
			try {
				if (workbook != null)
					workbook.close();
				inputStream.close();
			} catch (IOException e) {
			}
		}
		return list;
	}

	// Get Workbook
	private Workbook getWorkbook(InputStream inputStream, String excelFilePath) {
		Workbook workbook = null;
		if (excelFilePath.endsWith("xlsx")) {
			try {
				workbook = new XSSFWorkbook(inputStream);
			} catch (IOException e) {
			}
		} else if (excelFilePath.endsWith("xls")) {
			try {
				workbook = new HSSFWorkbook(inputStream);
			} catch (IOException e) {
			}
		} else {
			JOptionPane.showMessageDialog(this,
					"File không đúng định dạng. Vui lòng kiểm tra lại. Tên file phải kết thúc bằng .xls hoặc .xlsx",
					"Nhập File Excel", JOptionPane.ERROR_MESSAGE);
		}

		return workbook;
	}
}