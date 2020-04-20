package br.com.convencao.model.to;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class RegLancamentoCpl implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Long sqRegLacamento;
	private LocalDate dtVencimento;
	private Long sqPlanoConta;
	private String dsConta;
	private String tpLancamento;
	private BigDecimal vlLancamento;
	private LocalDateTime dtRegistro;
	private LocalDate dtPagamento;
	private BigDecimal vlPagamento;
	private Long cdNsuRecibo;
	private String dsRegiao;
	private LocalDate dtPeriodoFechado;
	
	private BigDecimal sdDevedor;
	private String corLancamentoRepetido = "color:Black;";
	private String corSaldoDevedor = "color:red;";
	
	private boolean flExibirAlterarLancamento = false;
	private boolean flExibirExcluirLancamento;
	private boolean flExibirCancelarPagamento;
	private boolean flExibirConfirmarPagamento;
	private BigDecimal vlPagamentoRecebido;
	private boolean flRealizarPagamento;
	
	
	public RegLancamentoCpl(Long sqRegLacamento, LocalDate dtVencimento, Long sqPlanoConta, String dsConta, String tpLancamento, BigDecimal vlLancamento, LocalDateTime dtRegistro,
			LocalDate dtPagamento, BigDecimal vlPagamento, Long cdNsuRecibo, String dsRegiao, LocalDate dtPeriodoFechado) {
		
		this.sqRegLacamento = sqRegLacamento;
		this.dtVencimento = dtVencimento;
		this.sqPlanoConta = sqPlanoConta;
		this.dsConta = dsConta;
		this.tpLancamento = tpLancamento;
		this.vlLancamento = vlLancamento;
		this.dtRegistro = dtRegistro;
		this.dtPagamento = dtPagamento;
		this.vlPagamento = vlPagamento;
		this.cdNsuRecibo = cdNsuRecibo;
		this.dsRegiao = dsRegiao;
		this.dtPeriodoFechado = dtPeriodoFechado;
	}


	// Retornar somente a data.
	public LocalDate getDtVencimento() {
		return dtVencimento;
	}
	
	public void setDtVencimento(LocalDate dtVencimento) {
		this.dtVencimento = dtVencimento;
	}

	public String getTpLancamento() {
		return tpLancamento;
	}

	public void setTpLancamento(String tpLancamento) {
		this.tpLancamento = tpLancamento;
	}

	public BigDecimal getVlLancamento() {
		return vlLancamento;
	}
	
	public void setVlLancamento(BigDecimal vlLancamento) {
		this.vlLancamento = vlLancamento;
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

	public Long getCdNsuRecibo() {
		return cdNsuRecibo;
	}

	public void setCdNsuRecibo(Long cdNsuRecibo) {
		this.cdNsuRecibo = cdNsuRecibo;
	}
	
	public Long getSqPlanoConta() {
		return sqPlanoConta;
	}
	
	public void setSqPlanoConta(Long sqPlanoConta) {
		this.sqPlanoConta = sqPlanoConta;
	}
	
	public String getDsConta() {
		return dsConta;
	}
	
	public void setDsConta(String dsPlanoConta) {
		this.dsConta = dsPlanoConta;
	}

	public LocalDateTime getDtRegistro() {
		return dtRegistro;
	}


	public void setDtRegistro(LocalDateTime dtRegistro) {
		this.dtRegistro = dtRegistro;
	}


	public BigDecimal getSdDevedor() {
		return sdDevedor;
	}
	
	public void setSdDevedor(BigDecimal sdDevedor) {
		this.sdDevedor = sdDevedor;
	}
	
	public Long getSqRegLacamento() {
		return sqRegLacamento;
	}
	
	public void setSqRegLacamento(Long sqRegLacamento) {
		this.sqRegLacamento = sqRegLacamento;
	}
	
	public String getCorLancamentoRepetido() {
		return corLancamentoRepetido;
	}
	
	public void setCorLancamentoRepetido(String corLancamentoRepetido) {
		this.corLancamentoRepetido = corLancamentoRepetido;
	}

	public String getCorSaldoDevedor() {
		return corSaldoDevedor;
	}
	
	public void setCorSaldoDevedor(String corSaldoDevedor) {
		this.corSaldoDevedor = corSaldoDevedor;
	}


	public boolean isFlExibirAlterarLancamento() {
		return flExibirAlterarLancamento;
	}


	public void setFlExibirAlterarLancamento(boolean flExibirAlterarLancamento) {
		this.flExibirAlterarLancamento = flExibirAlterarLancamento;
	}


	public boolean isFlExibirExcluirLancamento() {
		return flExibirExcluirLancamento;
	}


	public void setFlExibirExcluirLancamento(boolean flExibirExcluirLancamento) {
		this.flExibirExcluirLancamento = flExibirExcluirLancamento;
	}


	public boolean isFlExibirCancelarPagamento() {
		return flExibirCancelarPagamento;
	}


	public void setFlExibirCancelarPagamento(boolean flExibirCancelarPagamento) {
		this.flExibirCancelarPagamento = flExibirCancelarPagamento;
	}


	public boolean isFlExibirConfirmarPagamento() {
		return flExibirConfirmarPagamento;
	}


	public void setFlExibirConfirmarPagamento(boolean flExibirConfirmarPagamento) {
		this.flExibirConfirmarPagamento = flExibirConfirmarPagamento;
	}

	public BigDecimal getVlPagamentoRecebido() {
		return vlPagamentoRecebido;
	}

	public void setVlPagamentoRecebido(BigDecimal vlPagamentoRecebido) {
		this.vlPagamentoRecebido = vlPagamentoRecebido;
	}
	
	public boolean isFlRealizarPagamento() {
		return flRealizarPagamento;
	}
	
	public void setFlRealizarPagamento(boolean flRealizarPagamento) {
		this.flRealizarPagamento = flRealizarPagamento;
	}
	
	public String getDsRegiao() {
		return dsRegiao;
	}
	
	public void setDsRegiao(String dsRegiao) {
		this.dsRegiao = dsRegiao;
	}
	
	public LocalDate getDtPeriodoFechado() {
		return dtPeriodoFechado;
	}
	
	public void setDtPeriodoFechado(LocalDate dtPeriodoFechado) {
		this.dtPeriodoFechado = dtPeriodoFechado;
	}
}
