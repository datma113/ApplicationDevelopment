package panel.thongke;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
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

import com.toedter.calendar.JDateChooser;

import custom.MyJButton;
import custom.MyJLabel;
import custom.MyJScrollPane;
import custom.MyTableCellRender;
import entity.Nhanvien;
import session_factory.MySessionFactory;

/**
 * @author kienc
 *
 */
public class PanelThongkeNhanvien extends JPanel {

	private JPanel pnlMain;
	private JFreeChart chart;
	private DecimalFormat df;
	private JDateChooser date1;
	private JDateChooser date2;
	private SimpleDateFormat sdf;
	private ChartPanel pnlChart;
	private DefaultTableModel model;
	private JFileChooser fileChooser;
	private SessionFactory sessionFactory;
	private Map<Nhanvien, Double> dataNhanVien;
	private DefaultCategoryDataset dataChart;
	private Map<String, Integer> colMapByName;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PanelThongkeNhanvien() {
		sessionFactory = MySessionFactory.getInstance().getSessionFactory();
		setSize(1640, 916);
		setLayout(null);

		sdf = new SimpleDateFormat("yyyy-MM-dd");
		df = new DecimalFormat("###,###,###");

		pnlMain = new JPanel(null);
		pnlMain.setBounds(0, 0, 1640, 916);
		pnlMain.setBorder(BorderFactory.createEtchedBorder());
		add(pnlMain);

		dataNhanVien = new HashMap<Nhanvien, Double>();
		fileChooser = new JFileChooser();

		colMapByName = new HashMap<String, Integer>();
		colMapByName.put("manhanvien", 0);
		colMapByName.put("hodem", 1);
		colMapByName.put("ten", 2);
		colMapByName.put("socmnd", 3);
		colMapByName.put("sodienthoai", 4);
		colMapByName.put("tongdoanhthu", 5);

		model = new DefaultTableModel(
				new String[] { "Mã NV", "Họ", "Tên", "Số CMND", "Số điện thoại", "Tổng doanh thu" }, 0) {
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
		MyTableCellRender tableRenderer = new MyTableCellRender();
		table.setDefaultRenderer(Object.class, tableRenderer);

		table.getColumnModel().getColumn(0).setMinWidth(120);
		table.getColumnModel().getColumn(1).setMinWidth(190);
		table.getColumnModel().getColumn(2).setMinWidth(120);
		table.getColumnModel().getColumn(3).setMinWidth(135);
		table.getColumnModel().getColumn(4).setMinWidth(134);
		table.getColumnModel().getColumn(5).setMinWidth(200);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

		MyJLabel lblNam = new MyJLabel("Chọn thời gian");
		lblNam.setBounds(10, 11, 194, 30);
		pnlMain.add(lblNam);

		MyJScrollPane scrollPane = new MyJScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setBounds(10, 62, 910, 843);
		pnlMain.add(scrollPane);

		date1 = new JDateChooser(new Date());
		date1.setBounds(194, 12, 160, 30);
		pnlMain.add(date1);

		date2 = new JDateChooser(new Date());
		date2.setBounds(364, 12, 160, 30);
		pnlMain.add(date2);

		MyJButton btnXem = new MyJButton("Xem");
		btnXem.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnXem.setBounds(534, 11, 170, 30);
		btnXem.setIcon(new ImageIcon("img/refresh.png"));
		btnXem.addActionListener(e -> {
			reloadChart();
			if (dataNhanVien.isEmpty()) {
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
			if (dataNhanVien.isEmpty()) {
				JOptionPane.showMessageDialog(this, "Chưa có dữ liệu.", "Xuất File Excel", JOptionPane.ERROR_MESSAGE);
			} else {
				int returnVal = fileChooser.showOpenDialog(this);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					java.io.File file = fileChooser.getSelectedFile();
					String excelFilePath = file.getPath();
					if (writeExcel(dataNhanVien, excelFilePath)) {
						JOptionPane.showMessageDialog(this, "Ghi xuống file thành công", "Xuất File Excel",
								JOptionPane.INFORMATION_MESSAGE);
					} else {
						JOptionPane.showMessageDialog(this, "Ghi xuống file không thành công", "Xuất File Excel",
								JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});

		reloadChart();
	}

	/**
	 * lấy dữ liệu từ database
	 */
	private void setValueChart() {
		model.setRowCount(0);
		if (!dataNhanVien.isEmpty())
			dataNhanVien.clear();
		String query = "SELECT TOP 100 n.manhanvien, n.hodem, n.ten, n.socmnd, n.sodienthoai, SUM(h.tongtien) as tong FROM Nhanvien n JOIN Hoadon h ON h.manhanvien = n.manhanvien "
				+ " WHERE h.ngaylap >= :d1 AND h.ngaylap <= :d2 GROUP BY n.manhanvien, n.hodem, n.ten, n.socmnd, n.sodienthoai ORDER BY tong DESC";
		Session session = sessionFactory.openSession();
		Transaction tran = session.getTransaction();
		try {
			tran.begin();
			List<?> result = session.createNativeQuery(query).setParameter("d1", date1.getDate())
					.setParameter("d2", date2.getDate()).getResultList();
			int i = 0;
			for (Object object : result) {
				Object[] o = (Object[]) object;
				Vector<Object> vector = new Vector<Object>();
				Nhanvien nv = new Nhanvien(o[0].toString());
				nv.setHodem(o[1].toString());
				nv.setTen(o[2].toString());
				nv.setSocmnd(o[3].toString());
				nv.setSodienthoai(o[4].toString());
				double tongtien = Double.parseDouble(o[5].toString());
				vector.add(nv.getManhanvien());
				vector.add(nv.getHodem());
				vector.add(nv.getTen());
				vector.add(nv.getSocmnd());
				vector.add(nv.getSodienthoai());
				vector.add(df.format(tongtien));
				dataNhanVien.put(nv, tongtien);
				model.addRow(vector);
				if (i++ < 10) {
					dataChart.setValue(tongtien, nv.getHodem() + " " + nv.getTen(), "");
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
	private void reloadChart() {
		dataChart = new DefaultCategoryDataset();

		setValueChart();

		chart = ChartFactory.createBarChart(
				"Nhân viên có doanh thu cao từ ngày " + sdf.format(date1.getDate()) + " đến "
						+ sdf.format(date2.getDate()),
				"Nhân viên", "Tổng doanh thu đã bán", dataChart, PlotOrientation.VERTICAL, true, true, true);

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
	private boolean writeExcel(Map<Nhanvien, Double> map, String excelFilePath) {
		Workbook workbook = null;
		try {
			workbook = getWorkbook(excelFilePath);

			if (workbook != null) {
				Sheet sheet = workbook.createSheet("NhanVienCoDoanhThuCao");

				/*
				 * gộp các cột thành 1 để làm tiêu đề title
				 */
				sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, colMapByName.size() - 1));

				int rowIndex = 0;
				writeTitle(sheet, rowIndex);
				rowIndex++;
				writeHeader(sheet, rowIndex);
				rowIndex++;

				for (Map.Entry<Nhanvien, Double> entry : map.entrySet()) {
					Row row = sheet.createRow(rowIndex);
					writeEntry(entry, row);
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

		Cell cell = row.createCell(colMapByName.get("manhanvien"));
		cell.setCellStyle(cellStyle);
		cell.setCellValue("manhanvien");

		cell = row.createCell(colMapByName.get("hodem"));
		cell.setCellStyle(cellStyle);
		cell.setCellValue("hodem");

		cell = row.createCell(colMapByName.get("ten"));
		cell.setCellStyle(cellStyle);
		cell.setCellValue("ten");

		cell = row.createCell(colMapByName.get("socmnd"));
		cell.setCellStyle(cellStyle);
		cell.setCellValue("socmnd");

		cell = row.createCell(colMapByName.get("sodienthoai"));
		cell.setCellStyle(cellStyle);
		cell.setCellValue("sodienthoai");

		cell = row.createCell(colMapByName.get("tongdoanhthu"));
		cell.setCellStyle(cellStyle);
		cell.setCellValue("tongdoanhthu");

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
		cell.setCellValue("Danh sách nhân viên có doanh thu cao từ ngày " + sdf.format(date1.getDate()) + " đến ngày "
				+ sdf.format(date2.getDate()));

	}

	/**
	 * ghi từng dòng dữ liệu
	 * 
	 * @param entry
	 * @param row
	 */
	private void writeEntry(Entry<Nhanvien, Double> entry, Row row) {
		Nhanvien nv = entry.getKey();

		Cell cell = row.createCell(colMapByName.get("manhanvien"));
		cell.setCellValue(nv.getManhanvien());

		cell = row.createCell(colMapByName.get("hodem"));
		cell.setCellValue(nv.getHodem());

		cell = row.createCell(colMapByName.get("ten"));
		cell.setCellValue(nv.getTen());

		cell = row.createCell(colMapByName.get("socmnd"));
		cell.setCellValue(nv.getSocmnd());

		cell = row.createCell(colMapByName.get("sodienthoai"));
		cell.setCellValue(nv.getSodienthoai());

		Workbook workbook = row.getSheet().getWorkbook();
		CellStyle style = workbook.createCellStyle();
		style.setDataFormat((short) BuiltinFormats.getBuiltinFormat("#,##0.00"));

		cell = row.createCell(colMapByName.get("tongdoanhthu"));
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
