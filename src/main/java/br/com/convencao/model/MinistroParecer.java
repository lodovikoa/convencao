package br.com.convencao.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "tb_mpa_ministro_parecer")
public class MinistroParecer implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "mpa_sq_ministro_parecer")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long sqMinistroParecer;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "min_sq_ministro", nullable = false)
	private Ministro ministro;
	
	@Column(name = "mpa_ds_parecer")
	private String dsParecer;
	
	@Column(name = "mpa_ds_responsavel")
	private String dsResponsavel;
	
	@Column(name = "mpa_dt_registro")
	private LocalDateTime dtRegistro;
	
	@Column(name="auditoria_data")
	private LocalDateTime auditoriaData;
	
	@Column(name="auditoria_usuario", length=50)
	private String auditoriaUsuario;
	
	@Transient
	private String dsParecerTemp;

	public Long getSqProtocoloParecer() {
		return sqMinistroParecer;
	}

	public void setSqProtocoloParecer(Long sqProtocoloParecer) {
		this.sqMinistroParecer = sqProtocoloParecer;
	}

	public Ministro getMinistro() {
		return ministro;
	}
	
	public void setMinistro(Ministro ministro) {
		this.ministro = ministro;
	}

	public String getDsParecer() {
		return dsParecer;
	}

	public void setDsParecer(String dsParecer) {
		this.dsParecer = dsParecer;
	}

	public String getDsResponsavel() {
		return dsResponsavel;
	}

	public void setDsResponsavel(String dsResponsavel) {
		this.dsResponsavel = dsResponsavel;
	}
	
	public LocalDateTime getDtRegistro() {
		return dtRegistro;
	}
	
	public void setDtRegistro(LocalDateTime dtRegistro) {
		this.dtRegistro = dtRegistro;
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
	
	public String getDsParecerTemp() {
		return dsParecerTemp;
	}
	
	public void setDsParecerTemp(String dsParecerTemp) {
		this.dsParecerTemp = dsParecerTemp;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((sqMinistroParecer == null) ? 0 : sqMinistroParecer.hashCode());
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
		MinistroParecer other = (MinistroParecer) obj;
		if (sqMinistroParecer == null) {
			if (other.sqMinistroParecer != null)
				return false;
		} else if (!sqMinistroParecer.equals(other.sqMinistroParecer))
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
		MinistroParecer other = (MinistroParecer) obj;
		if (dsParecer == null) {
			if (other.dsParecer != null)
				return false;
		} else if (!dsParecer.equals(other.dsParecer))
			return false;
		if (dsResponsavel == null) {
			if (other.dsResponsavel != null)
				return false;
		} else if (!dsResponsavel.equals(other.dsResponsavel))
			return false;
		if (dtRegistro == null) {
			if (other.dtRegistro != null)
				return false;
		} else if (!dtRegistro.equals(other.dtRegistro))
			return false;
		if (ministro == null) {
			if (other.ministro != null)
				return false;
		} else if (!ministro.equals(other.ministro))
			return false;
		return true;
	}

}
