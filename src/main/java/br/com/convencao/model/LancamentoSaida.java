package br.com.convencao.model;

import java.io.Serializable;
import java.math.BigDecimal;
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
import javax.persistence.Transient;

@Entity
@Table(name = "tb_las_lancamento_saida")
public class LancamentoSaida implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "las_sq_saida")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long  sqSaida; 
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "lar_sq_resumo", nullable = false)
	private LancamentoResumo lancamentoResumo;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "rgn_sq_regiao", nullable = false)
	private Regiao regiao;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "plc_sq_plano_contas", nullable = false)
	private PlanoConta planoConta;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "dpt_sq_departamento", nullable = false)
	private Departamento departamento;
	
	@Column(name = "las_cd_forma_pagamento")
	private  Integer cdFormaPagamento;
	
	@Column(name = "las_dt_registro")
	private LocalDate dtRegistro;
	
	@Column(name = "las_dt_saida")
	private LocalDate dtSaida;
	
	@Column(name = "las_cd_nsu")
	private Long cdNsu;
	
	@Column(name="las_cd_documento", length=50)
	private String cdDocumento;
	
	@Column(name="las_nm_favorecido", length=100)
	private String nmFaforecido;
	
	@Column(name="las_vl_saida", nullable = false, precision = 10, scale = 2)
	private BigDecimal vlSaida;
	
	@Column(name="las_ds_historico")
	private String dsHistorico;
	
	@Column(name="auditoria_data")
	private LocalDateTime auditoriaData;
	
	@Column(name="auditoria_usuario", length=50)
	private String auditoriaUsuario;
	
	@Transient
	private String dsFormaPagamento;

	public Long getSqSaida() {
		return sqSaida;
	}

	public void setSqSaida(Long sqSaida) {
		this.sqSaida = sqSaida;
	}

	public LancamentoResumo getLancamentoResumo() {
		return lancamentoResumo;
	}

	public void setLancamentoResumo(LancamentoResumo lancamentoResumo) {
		this.lancamentoResumo = lancamentoResumo;
	}

	public Regiao getRegiao() {
		return regiao;
	}

	public void setRegiao(Regiao regiao) {
		this.regiao = regiao;
	}

	public PlanoConta getPlanoConta() {
		return planoConta;
	}

	public void setPlanoConta(PlanoConta planoConta) {
		this.planoConta = planoConta;
	}

	public Departamento getDepartamento() {
		return departamento;
	}

	public void setDepartamento(Departamento departamento) {
		this.departamento = departamento;
	}

	public Integer getCdFormaPagamento() {
		return cdFormaPagamento;
	}

	public void setCdFormaPagamento(Integer cdFormaPagamento) {
		this.cdFormaPagamento = cdFormaPagamento;
	}

	public LocalDate getDtRegistro() {
		return dtRegistro;
	}

	public void setDtRegistro(LocalDate dtRegistro) {
		this.dtRegistro = dtRegistro;
	}

	public LocalDate getDtSaida() {
		return dtSaida;
	}

	public void setDtSaida(LocalDate dtSaida) {
		this.dtSaida = dtSaida;
	}

	public Long getCdNsu() {
		return cdNsu;
	}

	public void setCdNsu(Long cdNsu) {
		this.cdNsu = cdNsu;
	}

	public String getCdDocumento() {
		return cdDocumento;
	}

	public void setCdDocumento(String cdDocumento) {
		this.cdDocumento = cdDocumento;
	}

	public String getNmFaforecido() {
		return nmFaforecido;
	}

	public void setNmFaforecido(String nmFaforecido) {
		this.nmFaforecido = nmFaforecido;
	}

	public BigDecimal getVlSaida() {
		return vlSaida;
	}

	public void setVlSaida(BigDecimal vlSaida) {
		this.vlSaida = vlSaida;
	}

	public String getDsHistorico() {
		return dsHistorico;
	}

	public void setDsHistorico(String dsHistorico) {
		this.dsHistorico = dsHistorico;
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
	
	public String getDsFormaPagamento() {	
		String result = this.cdFormaPagamento +  " - Não reconhecido";
		for (TipoPagamento tp : TipoPagamento.values()) {
			if(tp.getLabel() == this.cdFormaPagamento) {
				result = tp.toString();
				break;
			}
		}
		
		return result;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((sqSaida == null) ? 0 : sqSaida.hashCode());
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
		LancamentoSaida other = (LancamentoSaida) obj;
		if (sqSaida == null) {
			if (other.sqSaida != null)
				return false;
		} else if (!sqSaida.equals(other.sqSaida))
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
		LancamentoSaida other = (LancamentoSaida) obj;
		if (cdDocumento == null) {
			if (other.cdDocumento != null)
				return false;
		} else if (!cdDocumento.equals(other.cdDocumento))
			return false;
		if (cdFormaPagamento == null) {
			if (other.cdFormaPagamento != null)
				return false;
		} else if (!cdFormaPagamento.equals(other.cdFormaPagamento))
			return false;
		if (cdNsu == null) {
			if (other.cdNsu != null)
				return false;
		} else if (!cdNsu.equals(other.cdNsu))
			return false;
		if (departamento == null) {
			if (other.departamento != null)
				return false;
		} else if (!departamento.equals(other.departamento))
			return false;
		if (dsHistorico == null) {
			if (other.dsHistorico != null)
				return false;
		} else if (!dsHistorico.equals(other.dsHistorico))
			return false;
		if (dtRegistro == null) {
			if (other.dtRegistro != null)
				return false;
		} else if (!dtRegistro.equals(other.dtRegistro))
			return false;
		if (dtSaida == null) {
			if (other.dtSaida != null)
				return false;
		} else if (!dtSaida.equals(other.dtSaida))
			return false;
		if (lancamentoResumo == null) {
			if (other.lancamentoResumo != null)
				return false;
		} else if (!lancamentoResumo.equals(other.lancamentoResumo))
			return false;
		if (nmFaforecido == null) {
			if (other.nmFaforecido != null)
				return false;
		} else if (!nmFaforecido.equals(other.nmFaforecido))
			return false;
		if (planoConta == null) {
			if (other.planoConta != null)
				return false;
		} else if (!planoConta.equals(other.planoConta))
			return false;
		if (regiao == null) {
			if (other.regiao != null)
				return false;
		} else if (!regiao.equals(other.regiao))
			return false;
		if (vlSaida == null) {
			if (other.vlSaida != null)
				return false;
		} else if (!vlSaida.equals(other.vlSaida))
			return false;
		return true;
	}

	
}
