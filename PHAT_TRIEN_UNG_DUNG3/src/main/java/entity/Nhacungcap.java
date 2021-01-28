package entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.ContainedIn;
import org.hibernate.search.annotations.Field;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Table(name = "Nhacungcap")
@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(of = "manhacungcap")
public class Nhacungcap {

	@Column(name = "manhacungcap")
	private @Id String manhacungcap;

	@Field(analyze = Analyze.NO)
	@Column(name = "tennhacungcap", columnDefinition = "nvarchar(255) not null")
	private String tennhacungcap;

	@Column(name = "email", columnDefinition = "nvarchar(255)")
	private String email;

	@Column(name = "diachi", columnDefinition = "nvarchar(255)")
	private String diachi;

	@ToString.Exclude
	@ContainedIn
	@OneToMany(mappedBy = "nhacungcap", fetch = FetchType.LAZY)
	private List<Sanpham> sanphams;

	public Nhacungcap(String manhacungcap) {
		super();
		this.manhacungcap = manhacungcap;
	}

	public Nhacungcap(String manhacungcap, String tennhacungcap) {
		super();
		this.manhacungcap = manhacungcap;
		this.tennhacungcap = tennhacungcap;
	}

	public Nhacungcap(String manhacungcap, String tennhacungcap, String email, String diachi) {
		super();
		this.manhacungcap = manhacungcap;
		this.tennhacungcap = tennhacungcap;
		this.email = email;
		this.diachi = diachi;
	}

}
