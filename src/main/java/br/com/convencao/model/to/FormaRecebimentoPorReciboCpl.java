package br.com.convencao.model.to;

import java.io.Serializable;
import java.math.BigDecimal;

public class FormaRecebimentoPorReciboCpl implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Long sqFormaPagamento;
	private String dsFormaPagamento;
	private boolean flExibirComplemento;
	private Long sqFormaRecebimento;
	private Long sqRecibo;
	private BigDecimal vlRecebido;
	private BigDecimal vlContabil;
	private String dsComplemento;
	
	public FormaRecebimentoPorReciboCpl(Long sqFormaPagamento, String dsFormaPagamento, boolean flExibirComplemento,
			Long sqFormaRecebimento, Long sqRecibo, BigDecimal vlRecebido, BigDecimal vlContabil, String dsComplemento) {
		this.sqFormaPagamento = sqFormaPagamento;
		this.dsFormaPagamento = dsFormaPagamento;
		this.flExibirComplemento = flExibirComplemento;
		this.sqFormaRecebimento = sqFormaRecebimento;
		this.sqRecibo = sqRecibo;
		this.vlRecebido = vlRecebido;
		this.vlContabil = vlContabil;
		this.dsComplemento = dsComplemento;
	}

	public Long getSqFormaPagamento() {
		return sqFormaPagamento;
	}

	public void setSqFormaPagamento(Long sqFormaPagamento) {
		this.sqFormaPagamento = sqFormaPagamento;
	}

	public String getDsFormaPagamento() {
		return dsFormaPagamento;
	}

	public void setDsFormaPagamento(String dsFormaPagamento) {
		this.dsFormaPagamento = dsFormaPagamento;
	}

	public boolean isFlExibirComplemento() {
		return flExibirComplemento;
	}

	public void setFlExibirComplemento(boolean flExibirComplemento) {
		this.flExibirComplemento = flExibirComplemento;
	}

	public Long getSqFormaRecebimento() {
		return sqFormaRecebimento;
	}

	public void setSqFormaRecebimento(Long sqFormaRecebimento) {
		this.sqFormaRecebimento = sqFormaRecebimento;
	}

	public Long getSqRecibo() {
		return sqRecibo;
	}

	public void setSqRecibo(Long sqRecibo) {
		this.sqRecibo = sqRecibo;
	}
	
	public BigDecimal getVlRecebido() {
		return vlRecebido == null? BigDecimal.ZERO: vlRecebido;
	}
	
	public void setVlRecebido(BigDecimal vlRecebido) {
		this.vlRecebido = vlRecebido;
	}

	public BigDecimal getVlContabil() {
		return vlContabil == null? BigDecimal.ZERO: vlContabil;
	}

	public void setVlContabil(BigDecimal vlContabil) {
		this.vlContabil = vlContabil;
	}

	public String getDsComplemento() {
		return dsComplemento;
	}

	public void setDsComplemento(String dsComplemento) {
		this.dsComplemento = dsComplemento;
	}
	
}
