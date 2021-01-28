package entity;

import java.sql.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Table(name = "Sanpham")
@Entity
@Data
@Indexed
@NoArgsConstructor
@EqualsAndHashCode(of = "masanpham")
public class Sanpham {

	@Column(name = "masanpham")
	private @Id String masanpham;

	@Field
	@Column(name = "tensanpham", columnDefinition = "nvarchar(255) not null")
	private String tensanpham;

	@Field
	@Column(name = "mota", columnDefinition = "nvarchar(1000)")
	private String mota;

	@Field
	@Column(name = "mausac", columnDefinition = "nvarchar(60)")
	private String mausac;

	@Field
	@Column(name = "thuonghieu", columnDefinition = "nvarchar(60)")
	private String thuonghieu;

	@Column(name = "donvitinh", columnDefinition = "nvarchar(60)")
	private String donvitinh;

	@Column(name = "kichthuoc", columnDefinition = "nvarchar(60)")
	private String kichthuoc;

	@Column(name = "khoiluong", columnDefinition = "nvarchar(60)")
	private String khoiluong;

	@Column(name = "xuatxu", columnDefinition = "nvarchar(255)")
	private String xuatxu;

	@Column(name = "thue")
	@Field(analyze = Analyze.NO)
	private double thue;

	@Column(name = "giaban", nullable = false)
	@Field(analyze = Analyze.NO)
	private double giaban;

	@Column(name = "soluongton", nullable = false)
	@Field(analyze = Analyze.NO)
	private int soluongton;

	@Column(name = "hinhanh")
	private String hinhanh;

	@Column(name = "ngaynhap")
	private Date ngaynhap;

	@Column(name = "giamua", nullable = false)
	private double giamua;

	@Column(name = "thoigianbaohanh")
	private int thoigianbaohanh;

	@Column(name = "ngungkinhdoanh")
	private boolean ngungkinhdoanh = false;

	@ManyToOne
	@JoinColumn(name = "maloai")
	@ToString.Exclude
	@IndexedEmbedded(prefix = "loaisanpham.", includeEmbeddedObjectId = true)
	private Loaisanpham loaisanpham;

	@ManyToOne
	@JoinColumn(name = "manhacungcap")
	@ToString.Exclude
	@IndexedEmbedded(prefix = "nhacungcap.", includeEmbeddedObjectId = true)
	private Nhacungcap nhacungcap;

	@ToString.Exclude
	@OneToMany(mappedBy = "sanpham", fetch = FetchType.LAZY)
	private List<Chitiethoadon> chitiethoadons;

	public Sanpham(String masanpham) {
		super();
		this.masanpham = masanpham;
	}

	public Sanpham(String masanpham, String tensanpham, double giaban, int soluongton, double giamua) {
		super();
		this.masanpham = masanpham;
		this.tensanpham = tensanpham;
		this.giaban = giaban;
		this.soluongton = soluongton;
		this.giamua = giamua;
	}

}
