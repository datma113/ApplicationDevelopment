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

@Table(name = "Loaisanpham")
@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(of = "maloai")
public class Loaisanpham {

	@Column(name = "maloai")
	private @Id String maloai;

	@Field(analyze = Analyze.NO)
	@Column(name = "tenloai", columnDefinition = "nvarchar(255) not null")
	private String tenloai;

	@ContainedIn
	@ToString.Exclude
	@OneToMany(mappedBy = "loaisanpham", fetch = FetchType.LAZY)
	private List<Sanpham> sanphams;

	public Loaisanpham(String maloai) {
		super();
		this.maloai = maloai;
	}

	public Loaisanpham(String maloai, String tenloai) {
		super();
		this.maloai = maloai;
		this.tenloai = tenloai;
	}

}
