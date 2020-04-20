package br.com.convencao.model;

import java.io.Serializable;
import java.time.LocalDate;
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

@Entity
@Table(name = "tb_man_ministro_anexo")
public class MinistroAnexo implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "man_sq_ministro_anexo")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long	sqMinistroAnexo;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "min_sq_ministro", nullable = false)
	private Ministro ministro;
	
	@Column(name = "man_ds_anexo_original")
	private String dsAnexoOriginal;
	
	@Column(name = "man_ds_anexo_content_type")
	private String dsAnexoContentType;

	
	@Column(name = "man_ds_anexo_renomeado")
	private String dsAnexoRenomeado;
	
	@Column(name = "man_ds_anexo_responsavel")
	private String dsAnexoResponsavel;
	
	@Column(name = "man_dt_anexo_registro")
	private LocalDate dtAnexoRegistro;
	
	@Column(name="auditoria_data")
	private LocalDateTime auditoriaData;
	
	@Column(name="auditoria_usuario", length=50)
	private String auditoriaUsuario;

	public Long getSqMinistroAnexo() {
		return sqMinistroAnexo;
	}

	public void setSqMinistroAnexo(Long sqMinistroAnexo) {
		this.sqMinistroAnexo = sqMinistroAnexo;
	}

	public Ministro getMinistro() {
		return ministro;
	}

	public void setMinistro(Ministro ministro) {
		this.ministro = ministro;
	}

	public String getDsAnexoOriginal() {
		return dsAnexoOriginal;
	}

	public void setDsAnexoOriginal(String dsAnexoOriginal) {
		this.dsAnexoOriginal = dsAnexoOriginal;
	}
	
	public String getDsAnexoContentType() {
		return dsAnexoContentType;
	}
	
	public void setDsAnexoContentType(String dsAnexoContentType) {
		this.dsAnexoContentType = dsAnexoContentType;
	}

	public String getDsAnexoRenomeado() {
		return dsAnexoRenomeado;
	}

	public void setDsAnexoRenomeado(String dsAnexoRenomeado) {
		this.dsAnexoRenomeado = dsAnexoRenomeado;
	}
	
	public String getDsAnexoResponsavel() {
		return dsAnexoResponsavel;
	}

	public void setDsAnexoResponsavel(String dsAnexoResponsavel) {
		this.dsAnexoResponsavel = dsAnexoResponsavel;
	}

	public LocalDate getDtAnexoRegistro() {
		return dtAnexoRegistro;
	}

	public void setDtAnexoRegistro(LocalDate dtAnexoRegistro) {
		this.dtAnexoRegistro = dtAnexoRegistro;
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
		result = prime * result + ((sqMinistroAnexo == null) ? 0 : sqMinistroAnexo.hashCode());
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
		MinistroAnexo other = (MinistroAnexo) obj;
		if (sqMinistroAnexo == null) {
			if (other.sqMinistroAnexo != null)
				return false;
		} else if (!sqMinistroAnexo.equals(other.sqMinistroAnexo))
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
		MinistroAnexo other = (MinistroAnexo) obj;
		if (auditoriaData == null) {
			if (other.auditoriaData != null)
				return false;
		} else if (!auditoriaData.equals(other.auditoriaData))
			return false;
		if (auditoriaUsuario == null) {
			if (other.auditoriaUsuario != null)
				return false;
		} else if (!auditoriaUsuario.equals(other.auditoriaUsuario))
			return false;
		if (dsAnexoOriginal == null) {
			if (other.dsAnexoOriginal != null)
				return false;
		} else if (!dsAnexoOriginal.equals(other.dsAnexoOriginal))
			return false;
		if (dsAnexoContentType == null) {
			if (other.dsAnexoContentType != null)
				return false;
		} else if (!dsAnexoContentType.equals(other.dsAnexoContentType))
			return false;
		if (dsAnexoRenomeado == null) {
			if (other.dsAnexoRenomeado != null)
				return false;
		} else if (!dsAnexoRenomeado.equals(other.dsAnexoRenomeado))
			return false;
		if (dsAnexoResponsavel == null) {
			if (other.dsAnexoResponsavel != null)
				return false;
		} else if (!dsAnexoResponsavel.equals(other.dsAnexoResponsavel))
			return false;
		if (dtAnexoRegistro == null) {
			if (other.dtAnexoRegistro != null)
				return false;
		} else if (!dtAnexoRegistro.equals(other.dtAnexoRegistro))
			return false;
		if (ministro == null) {
			if (other.ministro != null)
				return false;
		} else if (!ministro.equals(other.ministro))
			return false;
		if (sqMinistroAnexo == null) {
			if (other.sqMinistroAnexo != null)
				return false;
		} else if (!sqMinistroAnexo.equals(other.sqMinistroAnexo))
			return false;
		return true;
	}

	
}
