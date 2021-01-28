package entity;

import java.sql.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Table(name = "Nhanvien")
@Entity
@Data
@Indexed
@NoArgsConstructor
@EqualsAndHashCode(of = "manhanvien")
public class Nhanvien {

	@Column(name = "manhanvien")
	private @Id String manhanvien;

	@Field
	@Column(name = "hodem", columnDefinition = "nvarchar(255) not null")
	private String hodem;

	@Field
	@Column(name = "ten", columnDefinition = "nvarchar(255) not null")
	private String ten;

	@Column(name = "diachi", columnDefinition = "nvarchar(255)")
	private String diachi;

	@Column(name = "chucvu", columnDefinition = "nvarchar(60)")
	private String chucvu;

	@Field
	@Column(name = "socmnd", columnDefinition = "nvarchar(60) not null")
	private String socmnd;

	@Field
	@Column(name = "sodienthoai")
	private String sodienthoai;

	@Column(name = "ngaysinh")
	private Date ngaysinh;

	@Column(name = "gioitinh")
	private boolean gioitinh;

	@ToString.Exclude
	@OneToOne(mappedBy = "nhanvien")
	private Taikhoan taikhoan;

	@ToString.Exclude
	@OneToMany(mappedBy = "nhanvien", fetch = FetchType.LAZY)
	private List<Hoadon> hoadons;

	public Nhanvien(String manhanvien) {
		super();
		this.manhanvien = manhanvien;
	}

	public Nhanvien(String manhanvien, String hodem, String ten, String socmnd) {
		super();
		this.manhanvien = manhanvien;
		this.hodem = hodem;
		this.ten = ten;
		this.socmnd = socmnd;
	}

}