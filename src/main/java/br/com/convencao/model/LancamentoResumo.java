package br.com.convencao.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

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

import br.com.convencao.util.Uteis;

@Entity
@Table(name = "tb_lar_lancamento_resumo")
public class LancamentoResumo implements Serializable {

	private static final long serialVersionUID = 1L;


	@Id
	@Column(name = "lar_sq_resumo")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long sqResumo;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "rgn_sq_regiao", nullable = false)
	private Regiao regiao;

	@Column(name = "lar_dt_registro")
	private LocalDate dtRegistro;

	@Column(name = "lar_dt_periodo_inicio")
	private LocalDate dtPeriodoInicio;

	@Column(name = "lar_dt_periodo_fim")
	private LocalDate dtPeriodoFim;

	@Column(name = "lar_dt_fechado")
	private LocalDate dtFechado;

	@Column(name="lar_vl_saldo_anterior", nullable = false, precision = 10, scale = 2)
	private BigDecimal vlSaldoAnterior;

	@Column(name="lar_vl_entradas", nullable = false, precision = 10, scale = 2)
	private BigDecimal vlEntradas;

	@Column(name="lar_vl_saidas", nullable = false, precision = 10, scale = 2)
	private BigDecimal vlSaidas;

	@Transient
	private BigDecimal vlSaldoAtual;

	@Transient
	private boolean flExibirDataFim;

	@Transient
	private boolean flExibirExcluir;

	@Transient
	private boolean flExibirEntradas;

	@Transient
	private boolean flExibirSaidas;

	@Transient
	private boolean flExibirFecharPeriodo;

	@Transient
	private boolean flExibirReabrirPeriodo;

	@Transient
	private String dsPeriodo;

	public Long getSqResumo() {
		return sqResumo;
	}

	public void setSqResumo(Long sqResumo) {
		this.sqResumo = sqResumo;
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

	public LocalDate getDtPeriodoInicio() {
		return dtPeriodoInicio;
	}

	public void setDtPeriodoInicio(LocalDate dtPeriodoInicio) {
		this.dtPeriodoInicio = dtPeriodoInicio;
	}

	public LocalDate getDtPeriodoFim() {
		return dtPeriodoFim;
	}

	public void setDtPeriodoFim(LocalDate dtPeriodoFim) {
		this.dtPeriodoFim = dtPeriodoFim;
	}

	public LocalDate getDtFechado() {
		return dtFechado;
	}

	public void setDtFechado(LocalDate dtFechado) {
		this.dtFechado = dtFechado;
	}

	public BigDecimal getVlSaldoAnterior() {
		return vlSaldoAnterior;
	}

	public void setVlSaldoAnterior(BigDecimal vlSaldoAnterior) {
		this.vlSaldoAnterior = vlSaldoAnterior;
	}

	public BigDecimal getVlEntradas() {
		return vlEntradas;
	}

	public void setVlEntradas(BigDecimal vlEntradas) {
		this.vlEntradas = vlEntradas;
	}

	public BigDecimal getVlSaidas() {
		return vlSaidas;
	}

	public void setVlSaidas(BigDecimal vlSaidas) {
		this.vlSaidas = vlSaidas;
	}

	public BigDecimal getVlSaldoAtual() {
		return this.vlSaldoAtual = this.vlSaldoAnterior.add(this.vlEntradas).subtract(this.vlSaidas);
	}

	public void setVlSaldoAtual(BigDecimal vlSaldoAtual) {
		this.vlSaldoAtual = vlSaldoAtual;
	}

	public boolean isFlExibirDataFim() {
		return flExibirDataFim;
	}

	public void setFlExibirDataFim(boolean flExibirDataFim) {
		this.flExibirDataFim = flExibirDataFim;
	}

	public boolean isFlExibirExcluir() {
		return flExibirExcluir;
	}

	public void setFlExibirExcluir(boolean flExibirExcluir) {
		this.flExibirExcluir = flExibirExcluir;
	}

	public boolean isFlExibirEntradas() {
		return flExibirEntradas = true;
	}

	public void setFlExibirEntradas(boolean flExibirEntradas) {
		this.flExibirEntradas = flExibirEntradas;
	}

	public boolean isFlExibirSaidas() {
		return flExibirSaidas = true;
	}

	public void setFlExibirSaidas(boolean flExibirSaidas) {
		this.flExibirSaidas = flExibirSaidas;
	}

	public boolean isFlExibirFecharPeriodo() {
		return flExibirFecharPeriodo;
	}

	public void setFlExibirFecharPeriodo(boolean flExibirFecharPeriodo) {
		this.flExibirFecharPeriodo = flExibirFecharPeriodo;
	}

	public boolean isFlExibirReabrirPeriodo() {
		return flExibirReabrirPeriodo;
	}

	public void setFlExibirReabrirPeriodo(boolean flExibirReabrirPeriodo) {
		this.flExibirReabrirPeriodo = flExibirReabrirPeriodo;
	}

	public String getDsPeriodo() {
		StringBuilder result = new StringBuilder();

		if(this.dtPeriodoInicio == null)
			result.append("00/00/0000");
		else 
			result.append(Uteis.LocalDateParaString(this.dtPeriodoInicio));

		result.append(" a ");

		if(this.dtPeriodoFim != null)
			result.append(Uteis.LocalDateParaString(this.dtPeriodoFim));

		return result.toString();
	}

	public void setDsPeriodo(String dsPeriodo) {
		this.dsPeriodo = dsPeriodo;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((sqResumo == null) ? 0 : sqResumo.hashCode());
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
		LancamentoResumo other = (LancamentoResumo) obj;
		if (sqResumo == null) {
			if (other.sqResumo != null)
				return false;
		} else if (!sqResumo.equals(other.sqResumo))
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
		LancamentoResumo other = (LancamentoResumo) obj;
		if (dtFechado == null) {
			if (other.dtFechado != null)
				return false;
		} else if (!dtFechado.equals(other.dtFechado))
			return false;
		if (dtPeriodoFim == null) {
			if (other.dtPeriodoFim != null)
				return false;
		} else if (!dtPeriodoFim.equals(other.dtPeriodoFim))
			return false;
		if (dtPeriodoInicio == null) {
			if (other.dtPeriodoInicio != null)
				return false;
		} else if (!dtPeriodoInicio.equals(other.dtPeriodoInicio))
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
		if (vlEntradas == null) {
			if (other.vlEntradas != null)
				return false;
		} else if (!vlEntradas.equals(other.vlEntradas))
			return false;
		if (vlSaidas == null) {
			if (other.vlSaidas != null)
				return false;
		} else if (!vlSaidas.equals(other.vlSaidas))
			return false;
		if (vlSaldoAnterior == null) {
			if (other.vlSaldoAnterior != null)
				return false;
		} else if (!vlSaldoAnterior.equals(other.vlSaldoAnterior))
			return false;
		return true;
	}


}
