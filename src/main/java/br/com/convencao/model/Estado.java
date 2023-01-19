package br.com.convencao.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table(name="tb_est_estado")
public class Estado implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="est_sq_estado")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long sqEstado;

	@NotBlank
	@Size(min = 2, max = 2, message = "deve ter 2 caracteres")
	@Column(name="est_ds_uf", nullable=false, length=2)
	private String dsUf;

	@NotBlank
	@Size(max = 50, message = "tamanho máximo de 50 caracteres")
	@Column(name="est_ds_nome", nullable=false, length=50)
	private String dsNome;

	@Column(name="auditoria_data")
	private LocalDateTime auditoriaData;

	@Column(name="auditoria_usuario", length=50)
	private String auditoriaUsuario;

	public Long getSqEstado() {
		return sqEstado;
	}

	public void setSqEstado(Long sqEstado) {
		this.sqEstado = sqEstado;
	}

	public String getDsUf() {
		return dsUf;
	}

	public void setDsUf(String dsUf) {
		this.dsUf = dsUf;
	}

	public String getDsNome() {
		return dsNome;
	}

	public void setDsNome(String dsNome) {
		this.dsNome = dsNome;
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
		result = prime * result + ((sqEstado == null) ? 0 : sqEstado.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		Long sqTemp = -1L;
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass()) {
			if(!((Estado) obj).getSqEstado().equals(this.getSqEstado())) {
				return false;
			} else {
				sqTemp = ((Estado) obj).getSqEstado();
			}
		}
		Estado other = (Estado) obj;
		if (sqEstado == null) {
			if (other.sqEstado != null)
				return false;
		} else if (!sqEstado.equals(other.sqEstado)) {
			if(!sqEstado.equals(sqTemp))
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
		Estado other = (Estado) obj;
		if (dsNome == null) {
			if (other.dsNome != null)
				return false;
		} else if (!dsNome.equals(other.dsNome))
			return false;
		if (dsUf == null) {
			if (other.dsUf != null)
				return false;
		} else if (!dsUf.equals(other.dsUf))
			return false;
		return true;
	}


}
