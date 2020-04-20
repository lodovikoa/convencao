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
import javax.validation.constraints.Size;

@Entity
@Table(name = "tb_lan_lancamento")
public class Lancamento implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "lan_sq_lancamento")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long  sqLancamento; 
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "rgl_sq_reglancamento", nullable = false)
	private RegLancamento regLancamento;
	
	@Column(name = "lan_cd_origem")
	private  Integer cdOrigem;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "lar_sq_resumo", nullable = false)
	private LancamentoResumo lancamentoResumo;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "rcb_sq_recibo", nullable = false)
	private Recibo recibo;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "igr_sq_igreja", nullable = false)
	private Igreja igreja;
	
	@Size(max = 50, message = "tamanho máximo de 50 caracteres")
	@Column(name = "lan_nm_outros", nullable = false, length = 50) 
	private String nmOutros;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "tpl_sq_tipolancamento", nullable = false)
	private TipoLancamento tipoLancamento;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "plc_sq_plano_contas", nullable = false)
	private PlanoConta planoConta;
	
	@Column(name = "lan_dt_registro")
	private LocalDateTime dtRegistro;
	
	@Column(name = "lan_dt_pagamento")
	private LocalDate dtPagamento;
	
	@Column(name="lan_vl_pagamento", nullable = false, precision = 10, scale = 2)
	private BigDecimal vlPagamento;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "bol_sq_boleto", nullable = false)
	private Boleto boleto;
	
	@Column(name="auditoria_data")
	private LocalDateTime auditoriaData;
	
	@Column(name="auditoria_usuario", length=50)
	private String auditoriaUsuario;

	public Long getSqLancamento() {
		return sqLancamento;
	}

	public void setSqLancamento(Long sqLancamento) {
		this.sqLancamento = sqLancamento;
	}

	public RegLancamento getRegLancamento() {
		return regLancamento;
	}

	public void setRegLancamento(RegLancamento regLancamento) {
		this.regLancamento = regLancamento;
	}

	public Integer getCdOrigem() {
		return cdOrigem;
	}

	public void setCdOrigem(Integer cdOrigem) {
		this.cdOrigem = cdOrigem;
	}

	public LancamentoResumo getLancamentoResumo() {
		return lancamentoResumo;
	}

	public void setLancamentoResumo(LancamentoResumo lancamentoResumo) {
		this.lancamentoResumo = lancamentoResumo;
	}

	public Recibo getRecibo() {
		return recibo;
	}

	public void setRecibo(Recibo recibo) {
		this.recibo = recibo;
	}

	public Igreja getIgreja() {
		return igreja;
	}

	public void setIgreja(Igreja igreja) {
		this.igreja = igreja;
	}

	public String getNmOutros() {
		return nmOutros;
	}
	
	public void setNmOutros(String nmOutros) {
		this.nmOutros = nmOutros;
	}

	public TipoLancamento getTipoLancamento() {
		return tipoLancamento;
	}

	public void setTipoLancamento(TipoLancamento tipoLancamento) {
		this.tipoLancamento = tipoLancamento;
	}

	public PlanoConta getPlanoConta() {
		return planoConta;
	}

	public void setPlanoConta(PlanoConta planoConta) {
		this.planoConta = planoConta;
	}

	public LocalDateTime getDtRegistro() {
		return dtRegistro;
	}

	public void setDtRegistro(LocalDateTime dtRegistro) {
		this.dtRegistro = dtRegistro;
	}

	public LocalDate getDtPagamento() {
		return dtPagamento;
	}

	public void setDtPagamento(LocalDate dtPagamento) {
		this.dtPagamento = dtPagamento;
	}

	public BigDecimal getVlPagamento() {
		return vlPagamento;
	}

	public void setVlPagamento(BigDecimal vlPagamento) {
		this.vlPagamento = vlPagamento;
	}

	public Boleto getBoleto() {
		return boleto;
	}

	public void setBoleto(Boleto boleto) {
		this.boleto = boleto;
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
	
	public String getDsOrigem() {
		if(this.cdOrigem == 1) return "Ministro";
		if(this.cdOrigem == 2) return "Igreja";
		if(this.cdOrigem == 3) return "Outros";
		return "Não identificado";
	}

	public String getDsContribuinte() {
		if(this.cdOrigem == 1) return "";
		if(this.cdOrigem == 2) return this.igreja.getDsIgreja();
		if(this.cdOrigem == 3) return this.nmOutros;
		return "Não identificado";
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((sqLancamento == null) ? 0 : sqLancamento.hashCode());
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
		Lancamento other = (Lancamento) obj;
		if (sqLancamento == null) {
			if (other.sqLancamento != null)
				return false;
		} else if (!sqLancamento.equals(other.sqLancamento))
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
		Lancamento other = (Lancamento) obj;
		if (nmOutros == null) {
			if (other.nmOutros != null)
				return false;
		} else if (!nmOutros.equals(other.nmOutros))
			return false;
		if (boleto == null) {
			if (other.boleto != null)
				return false;
		} else if (!boleto.equals(other.boleto))
			return false;
		if (cdOrigem == null) {
			if (other.cdOrigem != null)
				return false;
		} else if (!cdOrigem.equals(other.cdOrigem))
			return false;
		if (dtPagamento == null) {
			if (other.dtPagamento != null)
				return false;
		} else if (!dtPagamento.equals(other.dtPagamento))
			return false;
		if (dtRegistro == null) {
			if (other.dtRegistro != null)
				return false;
		} else if (!dtRegistro.equals(other.dtRegistro))
			return false;
		if (igreja == null) {
			if (other.igreja != null)
				return false;
		} else if (!igreja.equals(other.igreja))
			return false;
		if (planoConta == null) {
			if (other.planoConta != null)
				return false;
		} else if (!planoConta.equals(other.planoConta))
			return false;
		if (recibo == null) {
			if (other.recibo != null)
				return false;
		} else if (!recibo.equals(other.recibo))
			return false;
		if (regLancamento == null) {
			if (other.regLancamento != null)
				return false;
		} else if (!regLancamento.equals(other.regLancamento))
			return false;
		if (sqLancamento == null) {
			if (other.sqLancamento != null)
				return false;
		} else if (!sqLancamento.equals(other.sqLancamento))
			return false;
		if (tipoLancamento == null) {
			if (other.tipoLancamento != null)
				return false;
		} else if (!tipoLancamento.equals(other.tipoLancamento))
			return false;
		if (vlPagamento == null) {
			if (other.vlPagamento != null)
				return false;
		} else if (!vlPagamento.equals(other.vlPagamento))
			return false;
		return true;
	}
	
}
