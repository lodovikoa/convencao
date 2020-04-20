package br.com.convencao.model.to;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

import br.com.convencao.util.Uteis;

public class ReciboCpl implements Serializable {
	private static final long serialVersionUID = 1L;

	private BigDecimal vlToltalPagamento;
	private Long cdNsu;
	private String dsIgreja;
	private String dsCnpj;
	private String dsTituloCargo;
	private String dsNomeMin;
	private LocalDate dtRecibo;
	private String dsHistorico;
	private String dsRegiao;
	private String nmOutros;
	
	public ReciboCpl(BigDecimal vlToltalPagamento, Long cdNsu, String dsIgreja, String dsCnpj, String dsTituloCargo,
			String dsNomeMin, LocalDate dtRecibo, String dsHistorico, String dsRegiao, String nmOutros) {
		this.vlToltalPagamento = vlToltalPagamento;
		this.cdNsu = cdNsu;
		this.dsIgreja = dsIgreja;
		this.dsCnpj = dsCnpj;
		this.dsTituloCargo = dsTituloCargo;
		this.dsNomeMin = dsNomeMin;
		this.dtRecibo = dtRecibo;
		this.dsHistorico = dsHistorico;
		this.dsRegiao = dsRegiao;
		this.nmOutros = nmOutros;
	}

	public BigDecimal getVlToltalPagamento() {
		return vlToltalPagamento;
	}
	
	public String getVlTotalPagamentoFormatado() {
		return Uteis.valorFormatoBrasileiro(this.getVlToltalPagamento());
	}

	public Long getCdNsu() {
		return cdNsu;
	}

	public String getDsIgreja() {
		return dsIgreja;
	}

	public String getDsCnpj() {
		return dsCnpj;
	}
	
	public String getDsCnpjFormatado() {
		return Uteis.cnpjMask(this.getDsCnpj());
	}

	public String getDsTituloCargo() {
		return dsTituloCargo;
	}

	public String getDsNomeMin() {
		return dsNomeMin;
	}
	
	public LocalDate getDtRecibo() {
		return dtRecibo;
	}

	public String getDsHistorico() {
		return dsHistorico;
	}
	
	public String getDsRegiao() {
		return dsRegiao;
	}
	
	public String getNmOutros() {
		return nmOutros;
	}
}
