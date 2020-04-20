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
@Table(name = "tb_rcb_recibo")
public class Recibo implements Serializable {

	private static final long serialVersionUID = 1L;

	
	@Id
	@Column(name = "rcb_sq_recibo")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long sqRecibo;
	
	@Column(name = "rcb_cd_nsu")
	private  Long cdNsu;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "rgn_sq_regiao", nullable = false)
	private Regiao regiao;
	
	@Column(name = "rcb_dt_registro")
	private LocalDate dtRegistro;
	
	@Column(name = "rcb_dt_recibo")
	private LocalDate dtRecibo;
	
	@Column(name = "rcb_ds_historico")
	private String dsHistorico;
	
	@Column(name = "rcb_dt_cancelado")
	private LocalDateTime dtCancelado;
	
	@Column(name = "rcb_ds_cancelado")
	private String dsCancelado;
	
	
	@Column(name="auditoria_data")
	private LocalDateTime auditoriaData;
	
	@Column(name="auditoria_usuario", length=50)
	private String auditoriaUsuario;

	public Long getSqRecibo() {
		return sqRecibo;
	}

	public void setSqRecibo(Long sqRecibo) {
		this.sqRecibo = sqRecibo;
	}

	public Long getCdNsu() {
		return cdNsu;
	}

	public void setCdNsu(Long cdNsu) {
		this.cdNsu = cdNsu;
	}

	public Regiao getRegiao() {
		return regiao;
	}

	public void setRegiao(Regiao regiao) {
		this.regiao = regiao;
	}

	public LocalDate getDtRegistro() {
		return dtRegistro;
	}

	public void setDtRegistro(LocalDate dtRegistro) {
		this.dtRegistro = dtRegistro;
	}

	public LocalDate getDtRecibo() {
		return dtRecibo;
	}

	public void setDtRecibo(LocalDate dtRecibo) {
		this.dtRecibo = dtRecibo;
	}

	public String getDsHistorico() {
		return dsHistorico;
	}

	public void setDsHistorico(String dsHistorico) {
		this.dsHistorico = dsHistorico;
	}

	public LocalDateTime getDtCancelado() {
		return dtCancelado;
	}

	public void setDtCancelado(LocalDateTime dtCancelado) {
		this.dtCancelado = dtCancelado;
	}

	public String getDsCancelado() {
		return dsCancelado;
	}

	public void setDsCancelado(String dsCancelado) {
		this.dsCancelado = dsCancelado;
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
		result = prime * result + ((sqRecibo == null) ? 0 : sqRecibo.hashCode());
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
		Recibo other = (Recibo) obj;
		if (sqRecibo == null) {
			if (other.sqRecibo != null)
				return false;
		} else if (!sqRecibo.equals(other.sqRecibo))
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
		Recibo other = (Recibo) obj;
		if (cdNsu == null) {
			if (other.cdNsu != null)
				return false;
		} else if (!cdNsu.equals(other.cdNsu))
			return false;
		if (dsCancelado == null) {
			if (other.dsCancelado != null)
				return false;
		} else if (!dsCancelado.equals(other.dsCancelado))
			return false;
		if (dsHistorico == null) {
			if (other.dsHistorico != null)
				return false;
		} else if (!dsHistorico.equals(other.dsHistorico))
			return false;
		if (dtCancelado == null) {
			if (other.dtCancelado != null)
				return false;
		} else if (!dtCancelado.equals(other.dtCancelado))
			return false;
		if (dtRecibo == null) {
			if (other.dtRecibo != null)
				return false;
		} else if (!dtRecibo.equals(other.dtRecibo))
			return false;
		if (dtRegistro == null) {
			if (other.dtRegistro != null)
				return false;
		} else if (!dtRegistro.equals(other.dtRegistro))
			return false;
		if (regiao == null) {
			if (other.regiao != null)
				return false;
		} else if (!regiao.equals(other.regiao))
			return false;
		if (sqRecibo == null) {
			if (other.sqRecibo != null)
				return false;
		} else if (!sqRecibo.equals(other.sqRecibo))
			return false;
		return true;
	}
	
}
