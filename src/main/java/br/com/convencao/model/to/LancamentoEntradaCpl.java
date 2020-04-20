package br.com.convencao.model.to;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class LancamentoEntradaCpl implements Serializable {
	private static final long serialVersionUID = 1L;

	
	private Long sqLancamento; 
	private Long sqRecibo; 
	private Integer cdOrigem;
	private String dsConta;
	private LocalDateTime dtRegistro;
	private LocalDate dtPagamento;
	private Long cdNsu;
	private BigDecimal vlPagamento;
	private String nmMinistro;
	private String dsIgreja;
	private String nmOutro;
	private Long sqRegiao;
	private String auditoriaUsuario;
		
	public LancamentoEntradaCpl(Long sqLancamento, Long sqRecibo, Integer cdOrigem, String dsConta, LocalDateTime dtRegistro,
			LocalDate dtPagamento, Long cdNsu, BigDecimal vlPagamento, String nmMinistro, String dsIgreja,
			String nmOutro, Long sqRegiao, String auditoriaUsuario) {
		this.sqLancamento = sqLancamento;
		this.sqRecibo = sqRecibo;
		this.cdOrigem = cdOrigem;
		this.dsConta = dsConta;
		this.dtRegistro = dtRegistro;
		this.dtPagamento = dtPagamento;
		this.cdNsu = cdNsu;
		this.vlPagamento = vlPagamento;
		this.nmMinistro = nmMinistro;
		this.dsIgreja = dsIgreja;
		this.nmOutro = nmOutro;
		this.sqRegiao = sqRegiao;
		this.auditoriaUsuario = auditoriaUsuario;
	}

	public Long getSqLancamento() {
		return sqLancamento;
	}

	public void setSqLancamento(Long sqLancamento) {
		this.sqLancamento = sqLancamento;
	}

	public Long getSqRecibo() {
		return sqRecibo;
	}

	public void setSqRecibo(Long sqRecibo) {
		this.sqRecibo = sqRecibo;
	}

	public Integer getCdOrigem() {
		return cdOrigem;
	}

	public void setCdOrigem(Integer cdOrigem) {
		this.cdOrigem = cdOrigem;
	}

	public String getDsConta() {
		return dsConta;
	}

	public void setDsConta(String dsConta) {
		this.dsConta = dsConta;
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

	public Long getCdNsu() {
		return cdNsu;
	}

	public void setCdNsu(Long cdNsu) {
		this.cdNsu = cdNsu;
	}

	public BigDecimal getVlPagamento() {
		return vlPagamento;
	}

	public void setVlPagamento(BigDecimal vlPagamento) {
		this.vlPagamento = vlPagamento;
	}

	public String getNmMinistro() {
		return nmMinistro;
	}

	public void setNmMinistro(String nmMinistro) {
		this.nmMinistro = nmMinistro;
	}

	public String getDsIgreja() {
		return dsIgreja;
	}

	public void setDsIgreja(String dsIgreja) {
		this.dsIgreja = dsIgreja;
	}

	public String getNmOutro() {
		return nmOutro;
	}

	public void setNmOutro(String nmOutro) {
		this.nmOutro = nmOutro;
	}
	
	public Long getSqRegiao() {
		return sqRegiao;
	}
	
	public void setSqRegiao(Long sqRegiao) {
		this.sqRegiao = sqRegiao;
	}

	public String getAuditoriaUsuario() {
		return auditoriaUsuario;
	}

	public void setAuditoriaUsuario(String auditoriaUsuario) {
		this.auditoriaUsuario = auditoriaUsuario;
	}
	
	public boolean isFlConsultar() {
		return this.cdOrigem == 2 || this.cdOrigem == 3;
	}
	
	public boolean isFlAlterar() {
		return this.cdOrigem == 2 || this.cdOrigem == 3;
	}
	
	public boolean isFlExcluir() {
		return this.cdOrigem == 2 || this.cdOrigem == 3;
	}

	public String getDsOrigem() {
		if(this.cdOrigem == 1) return "Ministro";
		if(this.cdOrigem == 2) return "Igreja";
		if(this.cdOrigem == 3) return "Outros";
		return "Não identificado";
	}

	public String getDsContribuinte() {
		if(this.cdOrigem == 1) return this.nmMinistro;
		if(this.cdOrigem == 2) return this.dsIgreja;
		if(this.cdOrigem == 3) return this.nmOutro;
		return "Não identificado";
	}
	
}
