package entity;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import lombok.ToString;

@Table(name = "Hoadon")
@Entity
@Data
@Indexed
@EqualsAndHashCode(of = "mahoadon")
public class Hoadon {

	@Column(name = "mahoadon")
	private @Id String mahoadon;

	@ManyToOne
	@JoinColumn(name = "makhachhang")
	@ToString.Exclude
	@IndexedEmbedded(prefix = "khachhang.", includeEmbeddedObjectId = true)
	private Khachhang khachhang;

	@ManyToOne
	@JoinColumn(name = "manhanvien")
	@ToString.Exclude
	@IndexedEmbedded(prefix = "nhanvien.", includeEmbeddedObjectId = true)
	private Nhanvien nhanvien;

	@ToString.Exclude
	@Setter(AccessLevel.PROTECTED)
	@Column(name = "tongtien")
	private double tongtien;

	@Column(name = "ngaylap", nullable = false)
	private Date ngaylap;

	@ToString.Exclude
	@OneToMany(mappedBy = "hoadon", fetch = FetchType.LAZY)
	private List<Chitiethoadon> chitiethoadons;

	public double tinhTongtien() {
		tongtien = 0;
		for (Chitiethoadon cthd : chitiethoadons) {
			tongtien += cthd.tinhThanhTien();
		}
		return tongtien;
	}

	public Hoadon() {
		chitiethoadons = new ArrayList<Chitiethoadon>();
		ngaylap = new Date(new java.util.Date().getTime());
	}

	public Hoadon(String mahoadon) {
		this();
		this.mahoadon = mahoadon;
	}

	public Hoadon(String mahoadon, Khachhang khachhang, Nhanvien nhanvien) {
		this();
		this.mahoadon = mahoadon;
		this.khachhang = khachhang;
		this.nhanvien = nhanvien;
	}

}
