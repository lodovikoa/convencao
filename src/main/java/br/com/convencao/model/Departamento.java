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
@Table(name = "tb_dpt_departamento")
public class Departamento implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "dpt_sq_departamento")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long sqDepartamento;
	
	@NotBlank
	@Size(max = 20, message = "tamanho máximo de 20 caracteres")
	@Column(name = "dpt_ds_reduzido", length = 20)
	private String dsReduzido;
	
	@NotBlank
	@Size(max = 100, message = "tamanho máximo de 100 caracteres")
	@Column(name = "dpt_ds_departamento", length = 100) 
	private String dsDepartamento;
	
	@Column(name="auditoria_data")
	private LocalDateTime auditoriaData;
	
	@Column(name="auditoria_usuario", length=50)
	private String auditoriaUsuario;

	public Long getSqDepartamento() {
		return sqDepartamento;
	}
	
	public void setSqDepartamento(Long sqDepartamento) {
		this.sqDepartamento = sqDepartamento;
	}
	
	public String getDsReduzido() {
		return dsReduzido;
	}

	public void setDsReduzido(String dsReduzido) {
		this.dsReduzido = dsReduzido;
	}

	public String getDsDepartamento() {
		return dsDepartamento;
	}

	public void setDsDepartamento(String dsDepartamento) {
		this.dsDepartamento = dsDepartamento;
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
		return (int) ((sqDepartamento == null)? 0: sqDepartamento);
	}

	@Override
	public boolean equals(Object obj) {
		Long sqTemp = -1L;
		if(this == obj) return true;
		if (obj == null) return false;
		if(getClass() != obj.getClass()) {
			if(!((Departamento) obj).getSqDepartamento().equals(this.getSqDepartamento())) {
				return false;
			} else {
				sqTemp = ((Departamento) obj).getSqDepartamento();
			}
			
		}
		Departamento other = (Departamento) obj;
		if(sqDepartamento == null) {
			if(other.sqDepartamento != null)
				return false;
		} else if (!sqDepartamento.equals(other.sqDepartamento)) {
			if(!sqDepartamento.equals(sqTemp))
				return false;
		}
			
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
		Departamento other = (Departamento) obj;
		if (dsDepartamento == null) {
			if (other.dsDepartamento != null)
				return false;
		} else if (!dsDepartamento.equals(other.dsDepartamento))
			return false;
		if (dsReduzido == null) {
			if (other.dsReduzido != null)
				return false;
		} else if (!dsReduzido.equals(other.dsReduzido))
			return false;
		if (sqDepartamento == null) {
			if (other.sqDepartamento != null)
				return false;
		} else if (!sqDepartamento.equals(other.sqDepartamento))
			return false;
		return true;
	}
}
