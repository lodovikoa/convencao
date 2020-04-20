package br.com.convencao.model.to;

import java.io.Serializable;
import java.time.LocalDate;


public class RegLancamentoListLote implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long sqControle;
    private LocalDate dtVencimento;
	private Long sqRegiao;
	private String dsRegiao;
	private Long sqTipoLancamento;
	private String dsTipoLancamento;
	private Long nrQtdeLancamento;
	
	public Long getSqControle() {
		return sqControle;
	}
	
	public void setSqControle(Long sqControle) {
		this.sqControle = sqControle;
	}
	
	public LocalDate getDtVencimento() {
		return dtVencimento;
	}
	
	public void setDtVencimento(LocalDate dtVencimento) {
		this.dtVencimento = dtVencimento;
	}

	public Long getSqRegiao() {
		return sqRegiao;
	}

	public void setSqRegiao(Long sqRegiao) {
		this.sqRegiao = sqRegiao;
	}

	public String getDsRegiao() {
		return dsRegiao;
	}

	public void setDsRegiao(String dsRegiao) {
		this.dsRegiao = dsRegiao;
	}

	public Long getSqTipoLancamento() {
		return sqTipoLancamento;
	}

	public void setSqTipoLancamento(Long sqTipoLancamento) {
		this.sqTipoLancamento = sqTipoLancamento;
	}

	public String getDsTipoLancamento() {
		return dsTipoLancamento;
	}

	public void setDsTipoLancamento(String dsTipoLancamento) {
		this.dsTipoLancamento = dsTipoLancamento;
	}

	public Long getNrQtdeLancamento() {
		return nrQtdeLancamento;
	}

	public void setNrQtdeLancamento(Long nrQtdeLancamento) {
		this.nrQtdeLancamento = nrQtdeLancamento;
	}

}
