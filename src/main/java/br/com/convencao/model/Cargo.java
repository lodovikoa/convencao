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
@Table(name = "tb_cgo_cargo")
public class Cargo implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "cgo_sq_cargo")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long sqCargo;
	
	@NotBlank
	@Size(max = 50, message = "tamanho máximo de 50 caracteres")
	@Column(name = "cgo_ds_cargo", nullable=false, length = 50)
	private String dsCargo;
	
	@Size(max = 5, message = "Tamanho máximo de 5 caracteres")
	@Column(name = "cgo_ds_titulo", nullable = false, length = 5)
	private String dsTitulo;
	
	@Column(name="auditoria_data")
	private LocalDateTime auditoriaData;
	
	@Column(name="auditoria_usuario", length=50)
	private String auditoriaUsuario;

	public Long getSqCargo() {
		return sqCargo;
	}

	public void setSqCargo(Long sqCargo) {
		this.sqCargo = sqCargo;
	}

	public String getDsCargo() {
		return dsCargo;
	}

	public void setDsCargo(String dsCargo) {
		this.dsCargo = dsCargo;
	}

	public String getDsTitulo() {
		return dsTitulo;
	}

	public void setDsTitulo(String dsTitulo) {
		this.dsTitulo = dsTitulo;
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
		return (int) ((sqCargo == null)? 0: sqCargo);
	}

	@Override
	public boolean equals(Object obj) {
		if(this == obj) return true;
		if (obj == null) return false;
		if(getClass() != obj.getClass()) return false;
		Cargo other = (Cargo) obj;
		if(sqCargo == null) {
			if(other.sqCargo != null)
				return false;
		} else if (!sqCargo.equals(other.sqCargo))
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
		Cargo other = (Cargo) obj;
		if (dsCargo == null) {
			if (other.dsCargo != null)
				return false;
		} else if (!dsCargo.equals(other.dsCargo))
			return false;
		if (dsTitulo == null) {
			if (other.dsTitulo != null)
				return false;
		} else if (!dsTitulo.equals(other.dsTitulo))
			return false;
		return true;
	}

}
