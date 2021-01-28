package generate_bill;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import dao.DaoHoadon;
import entity.Chitiethoadon;
import entity.Hoadon;
import entity.Khachhang;
import entity.Nhanvien;
import entity.Sanpham;

public class GenerateBillPdf {

	private DaoHoadon daohd;

	public GenerateBillPdf(String mahd) {
		Hoadon hd = new Hoadon();
		Khachhang kh = new Khachhang();
		Nhanvien nv = new Nhanvien();
		List<Chitiethoadon> dscthd = new ArrayList<Chitiethoadon>();
		DecimalFormat df = new DecimalFormat("###,###,###.#");

		daohd = new DaoHoadon();
		hd = daohd.getHd(mahd);
		kh = hd.getKhachhang();
		nv = hd.getNhanvien();
		dscthd = daohd.getDsCthd(mahd);
		hd.setChitiethoadons(dscthd);

		Document doc = new Document();
		try {

			Path currentRelativePath = Paths.get("");
			String path = currentRelativePath.toAbsolutePath().toString() + "\\hoadon\\";
			path = path.replaceAll("\\\\", "/");

			if (!new File(path).exists()) {
				new File(path).mkdirs();
			}

			PdfWriter write = PdfWriter.getInstance(doc,
					new FileOutputStream(path + mahd + "_" + kh.getMakhachhang() + ".pdf"));

			BaseFont bf1 = BaseFont.createFont("font/cour.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
			Font f1 = new Font(bf1, 10);

			doc.open();
			Paragraph p1 = new Paragraph("CỬA HÀNG LINH KIỆN MÁY TÍNH THÀNH ĐẠT", f1);
			p1.setAlignment(Paragraph.ALIGN_CENTER);
			Paragraph p2 = new Paragraph("HÓA ĐƠN BÁN HÀNG", f1);
			p2.setAlignment(Paragraph.ALIGN_CENTER);
			p2.setSpacingAfter(20f);

			doc.add(p1);
			doc.add(p2);
			doc.add(new Paragraph(String.format("Tên người bán:%10sCỬA HÀNG LINH KIỆN MÁY TÍNH THÀNH ĐẠT", ""), f1));
			doc.add(new Paragraph(String.format("Địa chỉ:%10sDương Quảng Hàm, P5, Quận Gò Vấp, TP Hồ Chí Minh", ""),
					f1));

			doc.add(new Paragraph("\n"));
			LocalTime time = LocalTime.now();
			String s1 = String.format(
					"Số HĐ:%3s" + mahd + "%5s" + "Nhân viên lập:%3s" + nv.getHodem() + " " + nv.getTen(), "", "", "");
			String s2 = String.format(
					"Ngày lập:%3s" + hd.getNgaylap() + "%5s" + "Ngày in:%3s" + LocalDate.now() + "%3s%02d:%02d:%02d",
					"", "", "", "", time.getHour(), +time.getMinute(), +time.getSecond());
			String s3 = String.format("Mã KH:%3s" + kh.getMakhachhang() + "%5s" + "Khách hàng:%3s" + kh.getHoten()
					+ "%5s" + "SĐT:%3s" + kh.getSodienthoai(), "", "", "", "", "");
			doc.add(new Paragraph(s1, f1));
			doc.add(new Paragraph(s2, f1));
			doc.add(new Paragraph("\n"));
			doc.add(new Paragraph(s3, f1));
			doc.add(new Paragraph(String.format("Địa chỉ:%3s" + kh.getDiachi(), ""), f1));

			PdfPTable table = new PdfPTable(7);
			table.setWidthPercentage(100);
			table.setSpacingBefore(20f);
			table.setSpacingAfter(20f);

			float[] colwidth = { 0.75f, 1.5f, 3f, 0.75f, 1.5f, 0.75f, 1.5f };
			table.setWidths(colwidth);

			PdfPCell c0 = new PdfPCell(new Paragraph("STT", f1));
			PdfPCell c1 = new PdfPCell(new Paragraph("Mã SP", f1));
			PdfPCell c2 = new PdfPCell(new Paragraph("Tên SP", f1));
			PdfPCell c3 = new PdfPCell(new Paragraph("ĐVT", f1));
			PdfPCell c4 = new PdfPCell(new Paragraph("Đơn Giá (vnd)", f1));
			PdfPCell c5 = new PdfPCell(new Paragraph("SL", f1));
			PdfPCell c6 = new PdfPCell(new Paragraph("Thành Tiền (vnd)", f1));

			c0.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
			c1.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
			c2.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
			c3.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
			c4.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
			c5.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
			c6.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);

			c0.setFixedHeight(30);
			c0.setBorderWidthRight(0f);
			c1.setBorderWidthRight(0f);
			c2.setBorderWidthRight(0f);
			c3.setBorderWidthRight(0f);
			c4.setBorderWidthRight(0f);
			c5.setBorderWidthRight(0f);
			c6.setBorderWidthRight(0.5f);

			table.addCell(c0);
			table.addCell(c1);
			table.addCell(c2);
			table.addCell(c3);
			table.addCell(c4);
			table.addCell(c5);
			table.addCell(c6);

			int i = 1;
			for (Chitiethoadon ct : dscthd) {
				Sanpham s = ct.getSanpham();
				PdfPCell c7 = new PdfPCell(new Paragraph(i++ + "", f1));
				PdfPCell c8 = new PdfPCell(new Paragraph(s.getMasanpham(), f1));
				PdfPCell c9 = new PdfPCell(new Paragraph(s.getTensanpham(), f1));
				PdfPCell c10 = new PdfPCell(new Paragraph(s.getDonvitinh(), f1));
				PdfPCell c11 = new PdfPCell(new Paragraph(df.format(ct.getDongia()), f1));
				PdfPCell c12 = new PdfPCell(new Paragraph(ct.getSoluong() + "", f1));
				PdfPCell c13 = new PdfPCell(new Paragraph(df.format(ct.tinhThanhTien()), f1));

				c7.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
				c8.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
				c9.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
				c10.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
				c11.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
				c12.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
				c13.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);

				c7.setFixedHeight(30f);

				c7.setBorderWidthTop(0f);
				c7.setBorderWidthRight(0f);
				c8.setBorderWidthTop(0f);
				c8.setBorderWidthRight(0f);
				c9.setBorderWidthTop(0f);
				c9.setBorderWidthRight(0f);
				c10.setBorderWidthTop(0f);
				c10.setBorderWidthRight(0f);
				c11.setBorderWidthTop(0f);
				c11.setBorderWidthRight(0f);
				c12.setBorderWidthTop(0f);
				c12.setBorderWidthRight(0f);
				c13.setBorderWidthTop(0f);

				table.addCell(c7);
				table.addCell(c8);
				table.addCell(c9);
				table.addCell(c10);
				table.addCell(c11);
				table.addCell(c12);
				table.addCell(c13);

			}
			doc.add(new Paragraph("\n"));
			doc.add(table);

			double tax = 0.d;
			doc.add(new Paragraph(
					String.format("%-30s %-35s" + df.format(hd.tinhTongtien()) + " vnd", "Tiền hàng hóa:", ""), f1));
			doc.add(new Paragraph(String.format("%-30s %-35s" + df.format(hd.tinhTongtien() + tax) + " vnd",
					"Tổng cộng thanh toán:", ""), f1));
			doc.add(new Paragraph(String.format("%s", "Giá trên đã bao gồm thuế VAT."), f1));

			doc.add(new Paragraph("\n\n", f1));
			doc.add(new Paragraph(String.format("%8sNgười mua hàng %38s   Người bán hàng", "", ""), f1));
			doc.add(new Paragraph(String.format("%5s(Ký, ghi rõ họ tên) %35s (Ký, ghi rõ họ tên)", "", ""), f1));
			doc.add(new Paragraph("\n\n\n", f1));
			doc.add(new Paragraph(
					String.format("%8s %-30s %23s %-30s", "", kh.getHoten(), "", nv.getHodem() + " " + nv.getTen()),
					f1));
			Paragraph p3 = new Paragraph(
					"Quý khách vui lòng kiểm tra kỹ hàng hóa và hóa đơn trước khi rời khỏi cửa hàng.", f1);
			p3.setAlignment(Paragraph.ALIGN_CENTER);
			doc.add(p3);

			doc.close();
			write.close();

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Không thể tạo hóa đơn");
		}
	}
}
