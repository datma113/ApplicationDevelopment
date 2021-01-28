package entity;

import java.io.Serializable;

import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Chitiethoadon_PK implements Serializable {

	private static final long serialVersionUID = 1L;

	private String mahoadon;
	private String masanpham;
}
