package br.com.convencao.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tb_nsu_nsu")
public class Nsu implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "nsu_sq_nsu")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long sqNsu;

	public Long getSqNsu() {
		return sqNsu;
	}

	public void setSqNsu(Long sqNsu) {
		this.sqNsu = sqNsu;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((sqNsu == null) ? 0 : sqNsu.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Nsu other = (Nsu) obj;
		if (sqNsu == null) {
			if (other.sqNsu != null)
				return false;
		} else if (!sqNsu.equals(other.sqNsu))
			return false;
		return true;
	}
}
