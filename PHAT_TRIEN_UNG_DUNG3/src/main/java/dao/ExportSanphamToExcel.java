package dao;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.BuiltinFormats;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import entity.Sanpham;

public class ExportSanphamToExcel {

	private static Map<String, Integer> colMapByName;

	/**
	 * =================================================================================
	 * ghi dữ liệu xuống file excel
	 * 
	 * @param map
	 * @param excelFilePath
	 * @return
	 */
	public static boolean writeExcel(List<Sanpham> dssp, String excelFilePath) {
		colMapByName = new HashMap<String, Integer>();
		colMapByName.put("masanpham", 0);
		colMapByName.put("tensanpham", 1);
		colMapByName.put("giamua", 2);
		colMapByName.put("giaban", 3);
		colMapByName.put("thue", 4);
		colMapByName.put("soluongton", 5);
		colMapByName.put("thuonghieu", 6);
		colMapByName.put("xuatxu", 7);
		colMapByName.put("loaisanpham", 8);
		colMapByName.put("nhacungcap", 9);
		colMapByName.put("mausac", 10);
		colMapByName.put("kichthuoc", 11);
		colMapByName.put("khoiluong", 12);
		colMapByName.put("donvitinh", 13);
		colMapByName.put("mota", 14);
		colMapByName.put("ngaynhap", 15);
		colMapByName.put("thoigianbaohanh", 16);
		colMapByName.put("trangthai", 17);
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

				for (Sanpham sp : dssp) {
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
	private static Workbook getWorkbook(String excelFilePath) {
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
	 * ghi tên của các cột
	 * 
	 * @param sheet
	 * @param rowIndex
	 */
	private static void writeHeader(Sheet sheet, int rowIndex) {

		CellStyle cellStyle = createStyleForHeader(sheet);
		Row row = sheet.createRow(rowIndex);

		Cell cell = row.createCell(colMapByName.get("masanpham"));
		cell.setCellStyle(cellStyle);
		cell.setCellValue("masanpham");

		cell = row.createCell(colMapByName.get("tensanpham"));
		cell.setCellStyle(cellStyle);
		cell.setCellValue("tensanpham");

		cell = row.createCell(colMapByName.get("giamua"));
		cell.setCellStyle(cellStyle);
		cell.setCellValue("giamua");

		cell = row.createCell(colMapByName.get("giaban"));
		cell.setCellStyle(cellStyle);
		cell.setCellValue("giaban");

		cell = row.createCell(colMapByName.get("thue"));
		cell.setCellStyle(cellStyle);
		cell.setCellValue("thue");

		cell = row.createCell(colMapByName.get("soluongton"));
		cell.setCellStyle(cellStyle);
		cell.setCellValue("soluongton");

		cell = row.createCell(colMapByName.get("thuonghieu"));
		cell.setCellStyle(cellStyle);
		cell.setCellValue("thuonghieu");

		cell = row.createCell(colMapByName.get("xuatxu"));
		cell.setCellStyle(cellStyle);
		cell.setCellValue("xuatxu");

		cell = row.createCell(colMapByName.get("loaisanpham"));
		cell.setCellStyle(cellStyle);
		cell.setCellValue("loaisanpham");

		cell = row.createCell(colMapByName.get("nhacungcap"));
		cell.setCellStyle(cellStyle);
		cell.setCellValue("nhacungcap");

		cell = row.createCell(colMapByName.get("mausac"));
		cell.setCellStyle(cellStyle);
		cell.setCellValue("mausac");

		cell = row.createCell(colMapByName.get("kichthuoc"));
		cell.setCellStyle(cellStyle);
		cell.setCellValue("kichthuoc");

		cell = row.createCell(colMapByName.get("khoiluong"));
		cell.setCellStyle(cellStyle);
		cell.setCellValue("khoiluong");

		cell = row.createCell(colMapByName.get("donvitinh"));
		cell.setCellStyle(cellStyle);
		cell.setCellValue("donvitinh");

		cell = row.createCell(colMapByName.get("mota"));
		cell.setCellStyle(cellStyle);
		cell.setCellValue("mota");

		cell = row.createCell(colMapByName.get("ngaynhap"));
		cell.setCellStyle(cellStyle);
		cell.setCellValue("ngaynhap");

		cell = row.createCell(colMapByName.get("thoigianbaohanh"));
		cell.setCellStyle(cellStyle);
		cell.setCellValue("thoigianbaohanh");

		cell = row.createCell(colMapByName.get("trangthai"));
		cell.setCellStyle(cellStyle);
		cell.setCellValue("trangthai");

	}

	/**
	 * ghi tiêu đề vào dòng đầu tiên
	 * 
	 * @param sheet
	 * @param rowIndex
	 */
	private static void writeTitle(Sheet sheet, int rowIndex) {

		CellStyle cellStyle = createStyleForHeader(sheet);
		Row row = sheet.createRow(rowIndex);

		Cell cell = row.createCell(0);
		cell.setCellStyle(cellStyle);
		cell.setCellValue("Danh sách sản phẩm");

	}

	/**
	 * ghi từng dòng dữ liệu
	 * 
	 * @param entry
	 * @param row
	 */
	private static void writeSanpham(Sanpham sp, Row row) {

		Workbook workbook = row.getSheet().getWorkbook();

		CellStyle styleDouble = workbook.createCellStyle();
		styleDouble.setDataFormat((short) BuiltinFormats.getBuiltinFormat("#,##0.00"));

		CellStyle styleInteger = workbook.createCellStyle();
		styleInteger.setDataFormat((short) BuiltinFormats.getBuiltinFormat("#,##0"));

		CellStyle styleDate = workbook.createCellStyle();
		styleDate.setDataFormat((short) 14);

		Cell cell = row.createCell(colMapByName.get("masanpham"));
		cell.setCellValue(sp.getMasanpham());

		cell = row.createCell(colMapByName.get("tensanpham"));
		cell.setCellValue(sp.getTensanpham());

		cell = row.createCell(colMapByName.get("giamua"));
		cell.setCellStyle(styleDouble);
		cell.setCellValue(sp.getGiaban());

		cell = row.createCell(colMapByName.get("giaban"));
		cell.setCellStyle(styleDouble);
		cell.setCellValue(sp.getGiaban());

		cell = row.createCell(colMapByName.get("thue"));
		cell.setCellStyle(styleDouble);
		cell.setCellValue(sp.getThue());

		cell = row.createCell(colMapByName.get("soluongton"));
		cell.setCellStyle(styleInteger);
		cell.setCellValue(sp.getSoluongton());

		cell = row.createCell(colMapByName.get("thuonghieu"));
		cell.setCellValue(sp.getThuonghieu());

		cell = row.createCell(colMapByName.get("xuatxu"));
		cell.setCellValue(sp.getXuatxu());

		cell = row.createCell(colMapByName.get("loaisanpham"));
		cell.setCellValue(sp.getLoaisanpham().getTenloai());

		cell = row.createCell(colMapByName.get("nhacungcap"));
		cell.setCellValue(sp.getNhacungcap().getTennhacungcap());

		cell = row.createCell(colMapByName.get("mausac"));
		cell.setCellValue(sp.getMausac());

		cell = row.createCell(colMapByName.get("kichthuoc"));
		cell.setCellValue(sp.getKichthuoc());

		cell = row.createCell(colMapByName.get("khoiluong"));
		cell.setCellValue(sp.getKhoiluong());

		cell = row.createCell(colMapByName.get("donvitinh"));
		cell.setCellValue(sp.getDonvitinh());

		cell = row.createCell(colMapByName.get("mota"));
		cell.setCellValue(sp.getMota());

		cell = row.createCell(colMapByName.get("ngaynhap"));
		cell.setCellStyle(styleDate);
		cell.setCellValue(sp.getNgaynhap());

		cell = row.createCell(colMapByName.get("thoigianbaohanh"));
		cell.setCellStyle(styleInteger);
		cell.setCellValue(sp.getThoigianbaohanh());

		cell = row.createCell(colMapByName.get("trangthai"));
		cell.setCellValue(sp.isNgungkinhdoanh() ? "Ngừng kinh doanh" : "Đang bán");

	}

	/**
	 * tự động điều chỉnh lại độ rộng các cột
	 * 
	 * @param sheet
	 * @param lastColumn
	 */
	private static void autosizeColumn(Sheet sheet, int lastColumn) {
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
	private static void createOutputFile(Workbook workbook, String excelFilePath) throws IOException {
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
	private static CellStyle createStyleForHeader(Sheet sheet) {
		org.apache.poi.ss.usermodel.Font font = sheet.getWorkbook().createFont();
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
