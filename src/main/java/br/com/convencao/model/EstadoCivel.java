package br.com.convencao.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

@Entity
@Table(name = "tb_civ_estadocivel")
public class EstadoCivel implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "civ_sq_estadocivel")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long sqEstadoCivel;
	
	@NotBlank
	@Size(max = 30, message = "tamanho máximo de 30 caracteres")
	@Column(name = "civ_ds_estadocivel", nullable = false, length = 30) 
	private String dsEstadoCivel;
	
	@Column(name="auditoria_data")
	private LocalDateTime auditoriaData;
	
	@Column(name="auditoria_usuario", length=50)
	private String auditoriaUsuario;

	public Long getSqEstadoCivel() {
		return sqEstadoCivel;
	}

	public void setSqEstadoCivel(Long sqEstadoCivel) {
		this.sqEstadoCivel = sqEstadoCivel;
	}

	public String getDsEstadoCivel() {
		return dsEstadoCivel;
	}

	public void setDsEstadoCivel(String dsEstadoCivel) {
		this.dsEstadoCivel = dsEstadoCivel;
	}

	public LocalDateTime getAuditoriaData() {
		return auditoriaData;
	}

	public void setAuditoriaData(LocalDateTime auditoriaData) {
		this.auditoriaData = auditoriaData;
	}

	public String getAuditoriaUsuario() {
		return auditoriaUsuario;
	}

	public void setAuditoriaUsuario(String auditoriaUsuario) {
		this.auditoriaUsuario = auditoriaUsuario;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((sqEstadoCivel == null) ? 0 : sqEstadoCivel.hashCode());
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
		EstadoCivel other = (EstadoCivel) obj;
		if (sqEstadoCivel == null) {
			if (other.sqEstadoCivel != null)
				return false;
		} else if (!sqEstadoCivel.equals(other.sqEstadoCivel))
			return false;
		return true;
	}

	// Método equals personalizado
	public boolean equalsTO(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EstadoCivel other = (EstadoCivel) obj;
		if (dsEstadoCivel == null) {
			if (other.dsEstadoCivel != null)
				return false;
		} else if (!dsEstadoCivel.equals(other.dsEstadoCivel))
			return false;
		return true;
	}



}
