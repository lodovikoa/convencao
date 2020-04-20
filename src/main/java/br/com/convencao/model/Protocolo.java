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
@Table(name = "tb_prt_protocolo")
public class Protocolo implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "prt_sq_protocolo")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long sqProtocolo;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "min_sq_ministro")
	private Ministro ministro;
	
	@Column(name = "prt_cd_protocolo")
	private Long cdProtocolo; 
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "prs_sq_protocolo_status", nullable = false)
	private ProtocoloStatus protocoloStatus;
	
	@Column(name = "prt_dt_protocolo")
	private LocalDate dtProtocolo;
	
	@Column(name = "prt_dt_status")
	private LocalDate dtStatus;
	
	@Column(name = "prt_ds_responsavel")
	private String dsResponsavel;
	
	@Column(name="auditoria_data")
	private LocalDateTime auditoriaData;
	
	@Column(name="auditoria_usuario", length=50)
	private String auditoriaUsuario;

	public Long getSqProtocolo() {
		return sqProtocolo;
	}

	public void setSqProtocolo(Long sqProtocolo) {
		this.sqProtocolo = sqProtocolo;
	}

	public Ministro getMinistro() {
		return ministro;
	}

	public void setMinistro(Ministro ministro) {
		this.ministro = ministro;
	}

	public Long getCdProtocolo() {
		return cdProtocolo;
	}

	public void setCdProtocolo(Long cdProtocolo) {
		this.cdProtocolo = cdProtocolo;
	}

	public ProtocoloStatus getProtocoloStatus() {
		return protocoloStatus;
	}

	public void setProtocoloStatus(ProtocoloStatus protocoloStatus) {
		this.protocoloStatus = protocoloStatus;
	}
	
	public LocalDate getDtProtocolo() {
		return dtProtocolo;
	}

	public void setDtProtocolo(LocalDate dtProtocolo) {
		this.dtProtocolo = dtProtocolo;
	}

	public LocalDate getDtStatus() {
		return dtStatus;
	}

	public void setDtStatus(LocalDate dtStatus) {
		this.dtStatus = dtStatus;
	}

	public String getDsResponsavel() {
		return dsResponsavel;
	}

	public void setDsResponsavel(String dsResponsavel) {
		this.dsResponsavel = dsResponsavel;
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
		result = prime * result + ((sqProtocolo == null) ? 0 : sqProtocolo.hashCode());
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
		Protocolo other = (Protocolo) obj;
		if (sqProtocolo == null) {
			if (other.sqProtocolo != null)
				return false;
		} else if (!sqProtocolo.equals(other.sqProtocolo))
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
		Protocolo other = (Protocolo) obj;
		if (sqProtocolo == null) {
			if (other.sqProtocolo != null)
				return false;
		} else if (!sqProtocolo.equals(other.sqProtocolo))
			return false;
		return true;
	}
	
	
	
}
