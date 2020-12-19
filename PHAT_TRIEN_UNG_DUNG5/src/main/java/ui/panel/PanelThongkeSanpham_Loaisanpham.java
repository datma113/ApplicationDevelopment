package ui.panel;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.BuiltinFormats;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import custom.CustomJButton;
import custom.CustomJScrollPane;
import custom.CustomTableRenderer;
import entity.Loaisanpham;
import session_factory.MySessionFactory;

/**
 * @author kienc
 *
 */
public class PanelThongkeSanpham_Loaisanpham extends JPanel {

	private JPanel pnlMain;
	private JFreeChart chart;
	private ChartPanel pnlChart;
	private DefaultTableModel model;
	private JFileChooser fileChooser;
	private SessionFactory sessionFactory;
	private DefaultCategoryDataset dataChart;
	private Map<Loaisanpham, Integer> dataLoaisanpham;
	private Map<String, Integer> colMapByName;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PanelThongkeSanpham_Loaisanpham() {
		sessionFactory = MySessionFactory.getInstance().getSessionFactory();
		setSize(1640, 916);
		setLayout(null);

		pnlMain = new JPanel(null);
		pnlMain.setBounds(0, 0, 1640, 916);
		pnlMain.setBorder(BorderFactory.createEtchedBorder());
		add(pnlMain);

		dataLoaisanpham = new HashMap<Loaisanpham, Integer>();
		fileChooser = new JFileChooser();

		colMapByName = new HashMap<String, Integer>();
		colMapByName.put("maloai", 0);
		colMapByName.put("tenloai", 1);
		colMapByName.put("tongsosanpham", 2);

		model = new DefaultTableModel(new String[] { "Mã loại", "Tên loại", "Số sản phẩm" }, 0) {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		JTable table = new JTable(model);
		table.setRowHeight(35);
		table.setAutoCreateRowSorter(true);
		table.getTableHeader().setBackground(new Color(255, 208, 120));
		table.getTableHeader().setPreferredSize(new Dimension(table.getTableHeader().getWidth(), 35));
		java.awt.Font f3 = table.getTableHeader().getFont();
		table.getTableHeader().setFont(new java.awt.Font(f3.getName(), java.awt.Font.BOLD, f3.getSize() + 2));
		CustomTableRenderer tableRenderer = new CustomTableRenderer();
		table.setDefaultRenderer(Object.class, tableRenderer);

		table.getColumnModel().getColumn(0).setMinWidth(200);
		table.getColumnModel().getColumn(1).setMinWidth(500);
		table.getColumnModel().getColumn(2).setMinWidth(200);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

		CustomJScrollPane scrollPane = new CustomJScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setBounds(10, 62, 910, 843);
		pnlMain.add(scrollPane);

		CustomJButton btnExport = new CustomJButton("Xuất ra file Excel");
		btnExport.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnExport.setBounds(10, 15, 170, 30);
		btnExport.setIcon(new ImageIcon("img/excel.png"));
		btnExport.setToolTipText("Xuất ra file báo cáo dạng excel");
		pnlMain.add(btnExport);
		btnExport.addActionListener(e -> {
			if (dataLoaisanpham.isEmpty()) {
				JOptionPane.showMessageDialog(this, "Chưa có dữ liệu.", "Xuất File Excel", JOptionPane.ERROR_MESSAGE);
			} else {
				int returnVal = fileChooser.showOpenDialog(this);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					java.io.File file = fileChooser.getSelectedFile();
					String excelFilePath = file.getPath();
					if (writeExcel(dataLoaisanpham, excelFilePath)) {
						JOptionPane.showMessageDialog(this, "Ghi xuống file thành công", "Xuất File Excel",
								JOptionPane.INFORMATION_MESSAGE);
					} else {
						JOptionPane.showMessageDialog(this, "Ghi xuống file không thành công", "Xuất File Excel",
								JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});

		reloadChar();

	}

	/**
	 * lấy dữ liệu từ database
	 */
	private void setValueChart() {
		model.setRowCount(0);
		try {
			if (!dataLoaisanpham.isEmpty())
				dataLoaisanpham.clear();
		} catch (Exception e) {
			dataLoaisanpham = new HashMap<Loaisanpham, Integer>();
		}

		String query = "SELECT TOP 100 l.maloai, l.tenloai, COUNT(s.maloai) AS tong from Loaisanpham l LEFT JOIN Sanpham s ON s.maloai = l.maloai GROUP BY l.maloai, l.tenloai ORDER BY tong DESC";
		Session session = sessionFactory.openSession();
		Transaction tran = session.getTransaction();
		try {
			tran.begin();
			List<?> result = session.createNativeQuery(query).getResultList();
			int i = 0;
			for (Object object : result) {
				Object[] o = (Object[]) object;
				Vector<Object> vector = new Vector<Object>();
				Loaisanpham loaisanpham = new Loaisanpham(o[0].toString(), o[1].toString());
				int tongsoluong = Integer.parseInt(o[2].toString());
				vector.add(loaisanpham.getMaloai());
				vector.add(loaisanpham.getTenloai());
				vector.add(tongsoluong);
				dataLoaisanpham.put(loaisanpham, tongsoluong);
				model.addRow(vector);
				if (i++ < 10) {
					dataChart.setValue(tongsoluong, loaisanpham.getTenloai(), "");
				}
			}
			model.fireTableDataChanged();
			tran.commit();
		} catch (Exception e) {
			tran.rollback();
		}
		session.close();
	}

	/**
	 * vẽ lại biểu đồ
	 */
	private void reloadChar() {
		dataChart = new DefaultCategoryDataset();

		setValueChart();

		chart = ChartFactory.createBarChart("Danh sách loại sản phẩm có nhiều mặt hàng nhất", "Loại sản phẩm",
				"Số sản phẩm", dataChart, PlotOrientation.VERTICAL, true, true, true);

		if (pnlChart != null)
			pnlMain.remove(pnlChart);
		pnlChart = new ChartPanel(chart);
		pnlChart.setBounds(930, 62, 700, 503);
		pnlMain.add(pnlChart);

		this.updateUI();
	}

	/**
	 * ghi dữ liệu xuống file excel
	 * 
	 * @param map
	 * @param excelFilePath
	 * @return
	 */
	public boolean writeExcel(Map<Loaisanpham, Integer> map, String excelFilePath) {
		Workbook workbook = null;
		try {
			workbook = getWorkbook(excelFilePath);

			if (workbook != null) {
				Sheet sheet = workbook.createSheet("LoaiSanPham");

				/*
				 * gộp các cột thành 1 để làm tiêu đề title
				 */
				sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, colMapByName.size() - 1));

				int rowIndex = 0;
				writeTitle(sheet, rowIndex);
				rowIndex++;
				writeHeader(sheet, rowIndex);
				rowIndex++;

				for (Map.Entry<Loaisanpham, Integer> entry : map.entrySet()) {
					Row row = sheet.createRow(rowIndex);
					writeDataToExcel(entry, row);
					rowIndex++;
				}

				int numberOfColumn = sheet.getRow(1).getPhysicalNumberOfCells();
				autosizeColumn(sheet, numberOfColumn);

				createOutputFile(workbook, excelFilePath);
				return true;
			}
		} catch (IOException e) {
		}
		return false;
	}

	/**
	 * tạo workbook
	 * 
	 * @param excelFilePath
	 * @return
	 */
	private Workbook getWorkbook(String excelFilePath) {
		Workbook workbook = null;
		if (excelFilePath.endsWith("xlsx")) {
			workbook = new XSSFWorkbook();
		} else if (excelFilePath.endsWith("xls")) {
			workbook = new HSSFWorkbook();
		} else {
			JOptionPane.showMessageDialog(null,
					"File không đúng định dạng. Vui lòng đặt lại tên file. Tên file phải kết thúc bằng .xls hoặc .xlsx",
					"Xuất File Excel", JOptionPane.ERROR_MESSAGE);
		}
		return workbook;
	}

	/**
	 * ghi dòng tiêu đề
	 * 
	 * @param sheet
	 * @param rowIndex
	 */
	private void writeHeader(Sheet sheet, int rowIndex) {

		CellStyle cellStyle = createStyleForHeader(sheet);
		Row row = sheet.createRow(rowIndex);

		Cell cell = row.createCell(colMapByName.get("maloai"));
		cell.setCellStyle(cellStyle);
		cell.setCellValue("maloai");

		cell = row.createCell(colMapByName.get("tenloai"));
		cell.setCellStyle(cellStyle);
		cell.setCellValue("tenloai");

		cell = row.createCell(colMapByName.get("tongsosanpham"));
		cell.setCellStyle(cellStyle);
		cell.setCellValue("tongsosanpham");

	}

	/**
	 * ghi tiêu đề vào dòng đầu tiên
	 * 
	 * @param sheet
	 * @param rowIndex
	 */
	private void writeTitle(Sheet sheet, int rowIndex) {

		CellStyle cellStyle = createStyleForHeader(sheet);
		Row row = sheet.createRow(rowIndex);

		Cell cell = row.createCell(0);
		cell.setCellStyle(cellStyle);
		cell.setCellValue("Danh sách loại sản phẩm có nhiều mặt hàng nhất");

	}

	/**
	 * ghi từng dòng dữ liệu
	 * 
	 * @param entry
	 * @param row
	 */
	private void writeDataToExcel(Entry<Loaisanpham, Integer> entry, Row row) {
		Loaisanpham loaisanpham = entry.getKey();

		Cell cell = row.createCell(colMapByName.get("maloai"));
		cell.setCellValue(loaisanpham.getMaloai());

		cell = row.createCell(colMapByName.get("tenloai"));
		cell.setCellValue(loaisanpham.getTenloai());

		Workbook workbook = row.getSheet().getWorkbook();
		CellStyle style = workbook.createCellStyle();
		style.setDataFormat((short) BuiltinFormats.getBuiltinFormat("#,##0"));

		cell = row.createCell(colMapByName.get("tongsosanpham"));
		cell.setCellStyle(style);
		cell.setCellValue(entry.getValue());

	}

	/**
	 * tự động điều chỉnh lại độ rộng các cột
	 * 
	 * @param sheet
	 * @param lastColumn
	 */
	private void autosizeColumn(Sheet sheet, int lastColumn) {
		for (int columnIndex = 0; columnIndex < lastColumn; columnIndex++) {
			sheet.autoSizeColumn(columnIndex);
		}
	}

	/**
	 * write workbook xuống file
	 * 
	 * @param workbook
	 * @param excelFilePath
	 * @throws IOException
	 */
	private void createOutputFile(Workbook workbook, String excelFilePath) throws IOException {
		try (OutputStream os = new FileOutputStream(excelFilePath)) {
			workbook.write(os);
		}
	}

	/**
	 * tạo style cho phần header
	 * 
	 * @param sheet
	 * @return
	 */
	private CellStyle createStyleForHeader(Sheet sheet) {
		Font font = sheet.getWorkbook().createFont();
		font.setFontName("Times New Roman");
//		font.setBold(true);
		font.setFontHeightInPoints((short) 12);
		font.setColor(IndexedColors.BLACK.getIndex());

		CellStyle cellStyle = sheet.getWorkbook().createCellStyle();
		cellStyle.setFont(font);
		cellStyle.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
		cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		cellStyle.setBorderLeft(BorderStyle.THIN);
		cellStyle.setBorderRight(BorderStyle.THIN);
		cellStyle.setBorderTop(BorderStyle.THIN);
		cellStyle.setBorderBottom(BorderStyle.THIN);
		cellStyle.setAlignment(HorizontalAlignment.CENTER);
		return cellStyle;
	}
}