package entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.search.annotations.ContainedIn;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Table(name = "Khachhang")
@Entity
@Data
@Indexed
@NoArgsConstructor
@EqualsAndHashCode(of = "makhachhang")
public class Khachhang {

	@Column(name = "makhachhang")
	private @Id String makhachhang;

	@Field
	@Column(name = "hoten", columnDefinition = "nvarchar(255) not null")
	private String hoten;

	@Column(name = "diachi", columnDefinition = "nvarchar(255)")
	private String diachi;

	@Field
	@Column(name = "sodienthoai")
	private String sodienthoai;

	@ContainedIn
	@ToString.Exclude
	@OneToMany(mappedBy = "khachhang", fetch = FetchType.LAZY)
	private List<Hoadon> hoadons;

	public Khachhang(String makhachhang) {
		super();
		this.makhachhang = makhachhang;
	}

	public Khachhang(String makhachhang, String hoten) {
		super();
		this.makhachhang = makhachhang;
		this.hoten = hoten;
	}

	public Khachhang(String makhachhang, String hoten, String diachi, String sodienthoai) {
		super();
		this.makhachhang = makhachhang;
		this.hoten = hoten;
		this.diachi = diachi;
		this.sodienthoai = sodienthoai;
	}

}