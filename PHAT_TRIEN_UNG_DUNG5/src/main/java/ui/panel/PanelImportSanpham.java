package ui.panel;

import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
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

import custom.CustomJButton;
import custom.CustomJScrollPane;
import dao.DaoSanpham;
import entity.Loaisanpham;
import entity.Nhacungcap;
import entity.Sanpham;
import model.ModelSanpham;

public class PanelImportSanpham extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JTable table;
	private CustomJButton btn;
	private DaoSanpham dao;
	private String excelFilePath;
	private ModelSanpham model;
	private JFileChooser fileChooser;

	public PanelImportSanpham() {
		setSize(1800, 1000);
		setLayout(null);

		fileChooser = new JFileChooser();

		btn = new CustomJButton("Chọn file");
		btn.setLocation(1641, 11);
		btn.setSize(126, 35);
		btn.setMnemonic('x');

		dao = new DaoSanpham();
		model = new ModelSanpham();
		table = new JTable(model);
		table.setRowHeight(35);
		table.setAutoCreateRowSorter(true);
		table.getTableHeader().setBackground(new Color(255, 208, 120));

		table.getColumnModel().getColumn(0).setMinWidth(100);
		table.getColumnModel().getColumn(1).setMinWidth(271);
		table.getColumnModel().getColumn(2).setMinWidth(100);
		table.getColumnModel().getColumn(3).setMinWidth(100);
		table.getColumnModel().getColumn(4).setMinWidth(100);
		table.getColumnModel().getColumn(5).setMinWidth(80);
		table.getColumnModel().getColumn(6).setMinWidth(80);
		table.getColumnModel().getColumn(7).setMinWidth(100);
		table.getColumnModel().getColumn(8).setMinWidth(100);
		table.getColumnModel().getColumn(9).setMinWidth(250);
		table.getColumnModel().getColumn(10).setMinWidth(300);
		table.getColumnModel().getColumn(11).setMinWidth(100);
		table.getColumnModel().getColumn(12).setMinWidth(200);
		table.getColumnModel().getColumn(13).setMinWidth(100);
		table.getColumnModel().getColumn(14).setMinWidth(100);
		table.getColumnModel().getColumn(15).setMinWidth(300);
		table.getColumnModel().getColumn(16).setMinWidth(100);
		table.getColumnModel().getColumn(17).setMinWidth(100);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

		add(btn);

		CustomJScrollPane scrollPane = new CustomJScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setBounds(10, 194, 1757, 751);
		add(scrollPane);

		JTextArea textArea = new JTextArea(
				"Nhập danh sách sản phẩm từ file excel. File phải có định dạng .xls hoặc xlsx. Dòng đầu tiên là tiêu đề của cột. Danh sách các cột: \"tensanpham\", \"maloai\", \"manhacungcap\", \"xuatxu\", \"thuonghieu\", \"mausac\", \"khoiluong\", \"kichthuoc\", \"giamua\", \"giaban\", \"thue\", \"donvitinh\", \"mota\", \"soluongton\", \"ngaynhap\", \"thoigianbaohanh\". Mã sản phẩm sẽ được cấp tự động.");
		textArea.append("\n=====================");
		textArea.setWrapStyleWord(true);
		textArea.setLineWrap(true);
		textArea.setEditable(false);
		textArea.setFont(new Font("Serif", Font.PLAIN, 18));

		CustomJScrollPane jscrollPane = new CustomJScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		jscrollPane.setBounds(10, 57, 1757, 90);
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
					List<Sanpham> list = readExcel(excelFilePath);
					model.setDssp(list);
					model.fireTableDataChanged();
					if (!list.isEmpty()) {
						textArea.append("\nThêm thành công " + list.size() + " sản phẩm");
						textArea.append("\n=====================");
						JOptionPane.showMessageDialog(this, "Thêm thành công " + list.size() + " sản phẩm",
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
	 * @throws IOException
	 */
	public List<Sanpham> readExcel(String excelFilePath) {
		List<Sanpham> list = new ArrayList<Sanpham>();

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
						Sanpham sp = new Sanpham(dao.getNextMaSP());
						while (cellIterator.hasNext()) {
							Cell cell = cellIterator.next();
							if (!cell.toString().trim().equals("")) {
								try {
									if (cell.getColumnIndex() == colMapByName.get("tensanpham")) {
										sp.setTensanpham(cell.getStringCellValue());
									}
									if (cell.getColumnIndex() == colMapByName.get("maloai")) {
										sp.setLoaisanpham(new Loaisanpham(cell.getStringCellValue()));
									}
									if (cell.getColumnIndex() == colMapByName.get("manhacungcap")) {
										sp.setNhacungcap(new Nhacungcap(cell.getStringCellValue()));
									}
									if (cell.getColumnIndex() == colMapByName.get("thuonghieu")) {
										sp.setThuonghieu(cell.getStringCellValue());
									}
									if (cell.getColumnIndex() == colMapByName.get("mausac")) {
										sp.setMausac(cell.getStringCellValue());
									}
									if (cell.getColumnIndex() == colMapByName.get("khoiluong")) {
										sp.setKhoiluong(cell.getStringCellValue());
									}
									if (cell.getColumnIndex() == colMapByName.get("kichthuoc")) {
										sp.setKichthuoc(cell.getStringCellValue());
									}
									if (cell.getColumnIndex() == colMapByName.get("giamua")) {
										sp.setGiamua(cell.getNumericCellValue());
									}
									if (cell.getColumnIndex() == colMapByName.get("giaban")) {
										sp.setGiaban(cell.getNumericCellValue());
									}
									if (cell.getColumnIndex() == colMapByName.get("thue")) {
										sp.setThue(cell.getNumericCellValue());
									}
									if (cell.getColumnIndex() == colMapByName.get("donvitinh")) {
										sp.setDonvitinh(cell.getStringCellValue());
									}
									if (cell.getColumnIndex() == colMapByName.get("mota")) {
										sp.setMota(cell.getStringCellValue());
									}
									if (cell.getColumnIndex() == colMapByName.get("soluongton")) {
										sp.setSoluongton((int) cell.getNumericCellValue());
									}
									if (cell.getColumnIndex() == colMapByName.get("ngaynhap")) {
										sp.setNgaynhap(new Date(cell.getDateCellValue().getTime()));
									}
									if (cell.getColumnIndex() == colMapByName.get("thoigianbaohanh")) {
										sp.setThoigianbaohanh((int) cell.getNumericCellValue());
									}
									if (cell.getColumnIndex() == colMapByName.get("xuatxu")) {
										sp.setXuatxu(cell.getStringCellValue());
									}
								} catch (Exception e) {
									JOptionPane.showMessageDialog(this,
											"Lỗi. Vui lòng kiểm tra lại các cột của file excel", "Nhập File Excel",
											JOptionPane.ERROR_MESSAGE);
									return list;
								}
							}
						}
						if (sp.getMasanpham() != null && sp.getTensanpham() != null) {
//							if (dao.themSanPham(sp))
							list.add(sp);
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