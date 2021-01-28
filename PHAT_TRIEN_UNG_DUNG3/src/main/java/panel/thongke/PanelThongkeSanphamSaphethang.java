package panel.thongke;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

import custom.MyJButton;
import custom.MyJScrollPane;
import custom.MyTableCellRender;
import entity.Loaisanpham;
import entity.Nhacungcap;
import entity.Sanpham;
import session_factory.MySessionFactory;

/**
 * @author kienc
 *
 */
public class PanelThongkeSanphamSaphethang extends JPanel {

	private JPanel pnlMain;
	private JFreeChart chart;
	private ChartPanel pnlChart;
	private DefaultTableModel model;
	private JFileChooser fileChooser;
	private SessionFactory sessionFactory;
	private List<Sanpham> dataSanPham;
	private DefaultCategoryDataset dataChart;
	private Map<String, Integer> colMapByName;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PanelThongkeSanphamSaphethang() {
		sessionFactory = MySessionFactory.getInstance().getSessionFactory();
		setSize(1640, 916);
		setLayout(null);

		pnlMain = new JPanel(null);
		pnlMain.setBounds(0, 0, 1640, 916);
		pnlMain.setBorder(BorderFactory.createEtchedBorder());
		add(pnlMain);

		dataSanPham = new ArrayList<Sanpham>();
		fileChooser = new JFileChooser();

		colMapByName = new HashMap<String, Integer>();
		colMapByName.put("masanpham", 0);
		colMapByName.put("tensanpham", 1);
		colMapByName.put("loaisanpham", 2);
		colMapByName.put("nhacungcap", 3);
		colMapByName.put("thuonghieu", 4);
		colMapByName.put("xuatxu", 5);
		colMapByName.put("soluongton", 6);

		model = new DefaultTableModel(new String[] { "Mã SP", "Tên sản phẩm", "Loại sản phẩm", "Nhà cung cấp",
				"Thương hiệu", "Xuất xứ", "Số lượng tồn" }, 0) {
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
		table.getTableHeader().setPreferredSize(new Dimension(table.getTableHeader().getWidth(), 27));
		java.awt.Font f3 = table.getTableHeader().getFont();
		table.getTableHeader().setFont(new java.awt.Font(f3.getName(), java.awt.Font.BOLD, f3.getSize() + 2));
		MyTableCellRender tableRenderer = new MyTableCellRender();
		table.setDefaultRenderer(Object.class, tableRenderer);

		table.getColumnModel().getColumn(0).setMinWidth(120);
		table.getColumnModel().getColumn(1).setMinWidth(300);
		table.getColumnModel().getColumn(2).setMinWidth(200);
		table.getColumnModel().getColumn(3).setMinWidth(200);
		table.getColumnModel().getColumn(4).setMinWidth(135);
		table.getColumnModel().getColumn(5).setMinWidth(135);
		table.getColumnModel().getColumn(6).setMinWidth(135);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

		MyJScrollPane scrollPane = new MyJScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setBounds(10, 62, 910, 843);
		pnlMain.add(scrollPane);

		MyJButton btnXem = new MyJButton("Xem");
		btnXem.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnXem.setBounds(10, 15, 170, 30);
		btnXem.setIcon(new ImageIcon("img/refresh.png"));
		btnXem.addActionListener(e -> {
			reloadChart();
			if (dataSanPham != null && dataSanPham.isEmpty()) {
				JOptionPane.showMessageDialog(this, "Không có sản phẩm nào sắp hết hàng", "Thống kê",
						JOptionPane.INFORMATION_MESSAGE);
				return;
			}
		});
		pnlMain.add(btnXem);

		MyJButton btnExport = new MyJButton("Xuất ra file Excel");
		btnExport.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnExport.setBounds(190, 15, 170, 30);
		btnExport.setIcon(new ImageIcon("img/excel.png"));
		btnExport.setToolTipText("Xuất ra file báo cáo dạng excel");
		pnlMain.add(btnExport);
		btnExport.addActionListener(e -> {
			if (dataSanPham.isEmpty()) {
				JOptionPane.showMessageDialog(this, "Chưa có dữ liệu.", "Xuất File Excel", JOptionPane.ERROR_MESSAGE);
			} else {
				int returnVal = fileChooser.showOpenDialog(this);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					java.io.File file = fileChooser.getSelectedFile();
					String excelFilePath = file.getPath();
					if (writeExcel(dataSanPham, excelFilePath)) {
						JOptionPane.showMessageDialog(this, "Ghi xuống file thành công", "Xuất File Excel",
								JOptionPane.INFORMATION_MESSAGE);
					} else {
						JOptionPane.showMessageDialog(this, "Ghi xuống file không thành công", "Xuất File Excel",
								JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});

	}

	/**
	 * lấy dữ liệu từ database
	 */
	private void setValueChart() {
		model.setRowCount(0);
		if (!dataSanPham.isEmpty())
			dataSanPham.clear();
		String query = "SELECT TOP 100 * FROM dbo.Sanpham WHERE soluongton<=10 ORDER BY soluongton";
		Session session = sessionFactory.openSession();
		Transaction tran = session.getTransaction();
		try {
			tran.begin();
			List<Sanpham> result = session.createNativeQuery(query, Sanpham.class).getResultList();
			int i = 0;
			for (Sanpham sp : result) {
				Vector<Object> vector = new Vector<Object>();
				Loaisanpham loaiSp = sp.getLoaisanpham();
				Nhacungcap nhaCungCap = sp.getNhacungcap();
				vector.add(sp.getMasanpham());
				vector.add(sp.getTensanpham());
				try {
					vector.add(loaiSp.getTenloai());
				} catch (Exception e) {
					vector.add("null");
				}
				try {
					vector.add(nhaCungCap.getTennhacungcap());
				} catch (Exception e) {
					vector.add("null");
				}
				vector.add(sp.getThuonghieu());
				vector.add(sp.getXuatxu());
				vector.add(sp.getSoluongton());
				dataSanPham.add(sp);
				model.addRow(vector);
				if (i++ < 10) {
					dataChart.setValue(sp.getSoluongton(), sp.getTensanpham(), "");
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

		chart = ChartFactory.createBarChart("Sản phẩm sắp hết hàng ", "Sản phẩm", "Số lượng tồn", dataChart,
				PlotOrientation.VERTICAL, true, true, true);

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
	private boolean writeExcel(List<Sanpham> list, String excelFilePath) {
		Workbook workbook = null;
		try {
			workbook = getWorkbook(excelFilePath);

			if (workbook != null) {
				Sheet sheet = workbook.createSheet("SanPham");

				/*
				 * gộp các cột thành 1 để làm tiêu đề title
				 */
				sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, colMapByName.size() - 1));

				int rowIndex = 0;
				writeTitle(sheet, rowIndex);
				rowIndex++;
				writeHeader(sheet, rowIndex);
				rowIndex++;

				for (Sanpham sp : list) {
					Row row = sheet.createRow(rowIndex);
					writeSanpham(sp, row);
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

		Cell cell = row.createCell(colMapByName.get("masanpham"));
		cell.setCellStyle(cellStyle);
		cell.setCellValue("masanpham");

		cell = row.createCell(colMapByName.get("tensanpham"));
		cell.setCellStyle(cellStyle);
		cell.setCellValue("tensanpham");

		cell = row.createCell(colMapByName.get("loaisanpham"));
		cell.setCellStyle(cellStyle);
		cell.setCellValue("loaisanpham");

		cell = row.createCell(colMapByName.get("nhacungcap"));
		cell.setCellStyle(cellStyle);
		cell.setCellValue("nhacungcap");

		cell = row.createCell(colMapByName.get("thuonghieu"));
		cell.setCellStyle(cellStyle);
		cell.setCellValue("thuonghieu");

		cell = row.createCell(colMapByName.get("xuatxu"));
		cell.setCellStyle(cellStyle);
		cell.setCellValue("xuatxu");

		cell = row.createCell(colMapByName.get("soluongton"));
		cell.setCellStyle(cellStyle);
		cell.setCellValue("soluongton");

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
		cell.setCellValue("Danh sách sản phẩm sắp hết hàng");

	}

	/**
	 * ghi từng dòng dữ liệu
	 * 
	 * @param entry
	 * @param row
	 */
	private void writeSanpham(Sanpham sp, Row row) {

		Cell cell = row.createCell(colMapByName.get("masanpham"));
		cell.setCellValue(sp.getMasanpham());

		cell = row.createCell(colMapByName.get("tensanpham"));
		cell.setCellValue(sp.getTensanpham());

		cell = row.createCell(colMapByName.get("loaisanpham"));
		try {
			cell.setCellValue(sp.getLoaisanpham().getTenloai());
		} catch (Exception e) {
			cell.setCellValue("null");
		}

		cell = row.createCell(colMapByName.get("nhacungcap"));
		try {
			cell.setCellValue(sp.getNhacungcap().getTennhacungcap());
		} catch (Exception e) {
			cell.setCellValue("null");
		}

		cell = row.createCell(colMapByName.get("thuonghieu"));
		cell.setCellValue(sp.getThuonghieu());

		cell = row.createCell(colMapByName.get("xuatxu"));
		cell.setCellValue(sp.getXuatxu());

		Workbook workbook = row.getSheet().getWorkbook();
		CellStyle styleInteger = workbook.createCellStyle();
		styleInteger.setDataFormat((short) BuiltinFormats.getBuiltinFormat("#,##0"));

		cell = row.createCell(colMapByName.get("soluongton"));
		cell.setCellStyle(styleInteger);
		cell.setCellValue(sp.getSoluongton());

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
