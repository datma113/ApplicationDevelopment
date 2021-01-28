package panel.thongke;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

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

import custom.MyJButton;
import custom.MyJLabel;
import custom.MyJScrollPane;
import session_factory.MySessionFactory;

/**
 * @author kienc
 *
 */
public class PanelThongkeDoanhthu extends JPanel {

	private JPanel pnlMain;
	private JFreeChart chart;
	private DecimalFormat df;
	private ChartPanel pnlChart;
	private DefaultTableModel model;
	private JComboBox<String> cboNam;
	private JFileChooser fileChooser;
	private SessionFactory sessionFactory;
	private DefaultCategoryDataset dataChart;
	private Map<Integer, Double> dataDoanhthu;
	private Map<String, Integer> colMapByName;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PanelThongkeDoanhthu() {
		sessionFactory = MySessionFactory.getInstance().getSessionFactory();
		setSize(1640, 916);
		setLayout(null);

		df = new DecimalFormat("###,###,###");

		pnlMain = new JPanel(null);
		pnlMain.setBounds(0, 0, 1640, 916);
		pnlMain.setBorder(BorderFactory.createEtchedBorder());
		add(pnlMain);

		dataDoanhthu = new HashMap<Integer, Double>();
		fileChooser = new JFileChooser();

		colMapByName = new HashMap<String, Integer>();
		colMapByName.put("thang", 0);
		colMapByName.put("doanhthu", 1);

		model = new DefaultTableModel(new String[] { "Tháng", "Doanh thu" }, 0) {
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
		table.getTableHeader().setPreferredSize(new Dimension(table.getTableHeader().getWidth(), 30));
		java.awt.Font f3 = table.getTableHeader().getFont();
		table.getTableHeader().setFont(new java.awt.Font(f3.getName(), java.awt.Font.BOLD, f3.getSize() + 2));
		table.setDefaultRenderer(Object.class, new TableCellRenderer() {

			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
					boolean hasFocus, int row, int column) {
				Component c = new DefaultTableCellRenderer().getTableCellRendererComponent(table, value, isSelected,
						hasFocus, row, column);

				java.awt.Font f = c.getFont();
				c.setFont(new java.awt.Font(f.getName(), java.awt.Font.ROMAN_BASELINE, f.getSize() + 2));
				if (row % 3 == 0) {
					c.setBackground(new Color(210, 210, 210));
				} else {
					c.setBackground(Color.WHITE);
				}

				if (isSelected) {
					c.setBackground(new Color(0, 190, 43, 100));
				}
				return c;
			}
		});

		MyJLabel lblNam = new MyJLabel("Chọn năm");
		lblNam.setBounds(10, 11, 146, 30);
		pnlMain.add(lblNam);

		cboNam = new JComboBox<String>();
		cboNam.setBounds(194, 12, 330, 30);
		pnlMain.add(cboNam);

		MyJScrollPane scrollPane = new MyJScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setBounds(10, 62, 910, 503);
		pnlMain.add(scrollPane);

		MyJButton btnXem = new MyJButton("Xem");
		btnXem.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnXem.setBounds(534, 11, 170, 30);
		btnXem.setIcon(new ImageIcon("img/refresh.png"));
		btnXem.addActionListener(e -> {
			reloadChar();
			if (dataDoanhthu.isEmpty()) {
				JOptionPane.showMessageDialog(this, "Dữ liệu trống", "Thống kê", JOptionPane.INFORMATION_MESSAGE);
			}
		});
		pnlMain.add(btnXem);

		MyJButton btnExport = new MyJButton("Xuất ra file Excel");
		btnExport.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnExport.setBounds(714, 12, 170, 30);
		btnExport.setIcon(new ImageIcon("img/excel.png"));
		btnExport.setToolTipText("Xuất ra file báo cáo dạng excel");
		pnlMain.add(btnExport);
		btnExport.addActionListener(e -> {
			if (dataDoanhthu.isEmpty()) {
				JOptionPane.showMessageDialog(this, "Chưa có dữ liệu.", "Xuất File Excel", JOptionPane.ERROR_MESSAGE);
			} else {
				int returnVal = fileChooser.showOpenDialog(this);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					java.io.File file = fileChooser.getSelectedFile();
					String excelFilePath = file.getPath();
					if (writeExcel(dataDoanhthu, excelFilePath)) {
						JOptionPane.showMessageDialog(this, "Ghi xuống file thành công", "Xuất File Excel",
								JOptionPane.INFORMATION_MESSAGE);
					} else {
						JOptionPane.showMessageDialog(this, "Ghi xuống file không thành công", "Xuất File Excel",
								JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});

		loadComboBoxNam();
		cboNam.addActionListener(e -> reloadChar());
	}

	/**
	 * đưa danh sách các năm lên combobox
	 */
	private void loadComboBoxNam() {
		String query = "SELECT DISTINCT YEAR(ngaylap) as nam FROM Hoadon order by YEAR(ngaylap) desc";
		Session session = sessionFactory.openSession();
		Transaction tran = session.getTransaction();
		try {
			tran.begin();
			List<?> result = session.createNativeQuery(query).getResultList();
			for (Object obj : result) {
				try {
					cboNam.addItem(obj.toString());
				} catch (Exception e) {
				}
			}
			tran.commit();
		} catch (Exception e) {
			tran.rollback();
		}
		session.close();
	}

	/**
	 * lấy dữ liệu từ database
	 */
	private void setValueChart() {
		model.setRowCount(0);
		if (!dataDoanhthu.isEmpty())
			dataDoanhthu.clear();
		String query = "SELECT MONTH(ngaylap) as thang ,SUM(tongtien) as doanhthu FROM Hoadon WHERE YEAR(ngaylap)= :x GROUP BY MONTH(ngaylap) order by doanhthu desc";
		Session session = sessionFactory.openSession();
		Transaction tran = session.getTransaction();
		try {
			tran.begin();
			List<?> result = session.createNativeQuery(query)
					.setParameter("x", Integer.parseInt(cboNam.getSelectedItem().toString())).getResultList();
			int i = 0;
			for (Object object : result) {
				Object[] o = (Object[]) object;
				Vector<Object> vector = new Vector<Object>();
				int thang = Integer.parseInt(o[0].toString());
				double doanhthu = Double.parseDouble(o[1].toString());
				vector.add(thang);
				vector.add(df.format(doanhthu));
				dataDoanhthu.put(thang, doanhthu);
				model.addRow(vector);
				if (i++ < 10) {
					dataChart.setValue(doanhthu, thang + "", "");
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

		chart = ChartFactory.createBarChart("Tổng doanh thu từng tháng năm " + cboNam.getSelectedItem().toString(),
				"Tháng", "Doanh thu", dataChart, PlotOrientation.VERTICAL, true, true, true);

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
	private boolean writeExcel(Map<Integer, Double> map, String excelFilePath) {
		Workbook workbook = null;
		try {
			workbook = getWorkbook(excelFilePath);

			if (workbook != null) {
				Sheet sheet = workbook.createSheet("DoanhThuTungThang");

				/*
				 * gộp các cột thành 1 để làm tiêu đề title
				 */
				sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, colMapByName.size() - 1));

				int rowIndex = 0;
				writeTitle(sheet, rowIndex);
				rowIndex++;
				writeHeader(sheet, rowIndex);
				rowIndex++;

				for (Map.Entry<Integer, Double> entry : map.entrySet()) {
					Row row = sheet.createRow(rowIndex);
					writeBook(entry, row);
					rowIndex++;
				}

				sheet.setColumnWidth(0, 20 * 256);
				sheet.setColumnWidth(1, 20 * 256);

				/*
				 * int numberOfColumn = sheet.getRow(1).getPhysicalNumberOfCells();
				 * autosizeColumn(sheet, numberOfColumn);
				 */

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

		Cell cell = row.createCell(colMapByName.get("thang"));
		cell.setCellStyle(cellStyle);
		cell.setCellValue("thang");

		cell = row.createCell(colMapByName.get("doanhthu"));
		cell.setCellStyle(cellStyle);
		cell.setCellValue("doanhthu");

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
		cell.setCellValue("Tổng doanh thu từng tháng năm " + cboNam.getSelectedItem().toString());

	}

	/**
	 * ghi từng dòng dữ liệu
	 * 
	 * @param entry
	 * @param row
	 */
	private void writeBook(Entry<Integer, Double> entry, Row row) {

		Workbook workbook = row.getSheet().getWorkbook();
		CellStyle styleDouble = workbook.createCellStyle();
		styleDouble.setDataFormat((short) BuiltinFormats.getBuiltinFormat("#,##0.00"));

		CellStyle styleInteger = workbook.createCellStyle();
		styleInteger.setDataFormat((short) BuiltinFormats.getBuiltinFormat("#,##0"));

		Cell cell = row.createCell(colMapByName.get("thang"));
		cell.setCellStyle(styleInteger);
		cell.setCellValue(entry.getKey());

		cell = row.createCell(colMapByName.get("doanhthu"));
		cell.setCellStyle(styleDouble);
		cell.setCellValue(entry.getValue());

	}

	/**
	 * tự động điều chỉnh lại độ rộng các cột
	 * 
	 * @param sheet
	 * @param lastColumn
	 */
	@SuppressWarnings("unused")
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
