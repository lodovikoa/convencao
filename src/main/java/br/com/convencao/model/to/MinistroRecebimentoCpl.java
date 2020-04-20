package br.com.convencao.model.to;

import java.io.Serializable;
import java.time.LocalDate;

public class MinistroRecebimentoCpl implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Long sqMinistro;
	private Long cdCodigo;
	private String nmMinistro;
	private String dsRegiao;
	private String dsDepartamento;
	private LocalDate dtIngresso;
	private String dsCargo;
	private String dsIgreja;
	private String dsCidade;
	private String dsEstado;
	private LocalDate dtExcluido;
	private boolean flJubilado;
	private String dsFoto;
	
	private String dsCpf;
	private String prPresidente;
	
	private String dsCidEst;
	private String dsJubilado;
	private String dsCodigoNomeMinistro;
	@SuppressWarnings("unused")
	private boolean flInativo;
	private boolean flMinistro;
	
	
	public MinistroRecebimentoCpl(Long sqMinistro, Long cdCodigo, String nmMinistro, String dsRegiao, String dsDepartamento, 
			LocalDate dtIngresso, String dsCargo, String dsIgreja, String dsCidade, String dsEstado, LocalDate dtExcluido, boolean flJubilado ) {
		this.sqMinistro = sqMinistro;
		this.cdCodigo = cdCodigo;
		this.nmMinistro = nmMinistro;
		this.dsRegiao = dsRegiao;
		this.dsDepartamento = dsDepartamento;
		this.dtIngresso = dtIngresso;
		this.dsCargo = dsCargo;
		this.dsIgreja = dsIgreja;
		this.dsCidade = dsCidade;
		this.dsEstado = dsEstado;
		this.dtExcluido = dtExcluido;
		this.flJubilado = flJubilado;
	}
	

	public MinistroRecebimentoCpl(Long sqMinistro, Long cdCodigo, String nmMinistro, String dsRegiao,
			String dsDepartamento, LocalDate dtIngresso, String dsCargo, String dsIgreja, String dsCidade,
			String dsEstado, LocalDate dtExcluido, boolean flJubilado, String dsCpf, String prPresidente, String dsFoto) {
		this.sqMinistro = sqMinistro;
		this.cdCodigo = cdCodigo;
		this.nmMinistro = nmMinistro;
		this.dsRegiao = dsRegiao;
		this.dsDepartamento = dsDepartamento;
		this.dtIngresso = dtIngresso;
		this.dsCargo = dsCargo;
		this.dsIgreja = dsIgreja;
		this.dsCidade = dsCidade;
		this.dsEstado = dsEstado;
		this.dtExcluido = dtExcluido;
		this.flJubilado = flJubilado;
		this.dsCpf = dsCpf;
		this.prPresidente = prPresidente;
		this.dsFoto = dsFoto;
	}


	public Long getSqMinistro() {
		return sqMinistro;
	}

	public void setSqMinistro(Long sqMinistro) {
		this.sqMinistro = sqMinistro;
	}

	public Long getCdCodigo() {
		return cdCodigo;
	}

	public void setCdCodigo(Long cdCodigo) {
		this.cdCodigo = cdCodigo;
	}

	public String getNmMinistro() {
		return nmMinistro;
	}

	public void setNmMinistro(String nmMinistro) {
		this.nmMinistro = nmMinistro;
	}

	public String getDsRegiao() {
		return dsRegiao;
	}

	public void setDsRegiao(String dsRegiao) {
		this.dsRegiao = dsRegiao;
	}

	public String getDsDepartamento() {
		return dsDepartamento;
	}

	public void setDsDepartamento(String dsDepartamento) {
		this.dsDepartamento = dsDepartamento;
	}

	public LocalDate getDtIngresso() {
		return dtIngresso;
	}

	public void setDtIngresso(LocalDate dtIngresso) {
		this.dtIngresso = dtIngresso;
	}

	public String getDsCargo() {
		return dsCargo;
	}

	public void setDsCargo(String dsCargo) {
		this.dsCargo = dsCargo;
	}

	public String getDsIgreja() {
		return dsIgreja;
	}

	public void setDsIgreja(String dsIgreja) {
		this.dsIgreja = dsIgreja;
	}

	public String getDsCidade() {
		return dsCidade;
	}

	public void setDsCidade(String dsCidade) {
		this.dsCidade = dsCidade;
	}

	public String getDsEstado() {
		return dsEstado;
	}

	public void setDsEstado(String dsEstado) {
		this.dsEstado = dsEstado;
	}
	
	public LocalDate getDtExcluido() {
		return dtExcluido;
	}
	
	public void setDtExcluido(LocalDate dtExcluido) {
		this.dtExcluido = dtExcluido;
	}
	
	public boolean isFlJubilado() {
		return flJubilado;
	}
	
	public void setFlJubilado(boolean flJubilado) {
		this.flJubilado = flJubilado;
	}
	
	
	
	public String getDsCpf() {
		return dsCpf;
	}


	public void setDsCpf(String dsCpf) {
		this.dsCpf = dsCpf;
	}


	public String getPrPresidente() {
		return prPresidente;
	}


	public void setPrPresidente(String prPresidente) {
		this.prPresidente = prPresidente;
	}

	public String getDsFoto() {
		return dsFoto;
	}
	
	public void setDsFoto(String dsFoto) {
		this.dsFoto = dsFoto;
	}

	public String getDsCidEst() {	
		StringBuilder result = new StringBuilder();
		
		if(this.dsCidade != null ) 
			result.append(this.dsCidade);
		
		if(!result.toString().isEmpty() && this.dsEstado != null) {
			result.append("/");
			result.append(this.dsEstado);
		} else if(this.dsEstado != null) {
			result.append(this.dsEstado);
		} else
			result.append("");
		
		this.dsCidEst = result.toString();
					
		return this.dsCidEst;
	}
	
	public String getDsCodigoNomeMinistro() {
		this.dsCodigoNomeMinistro = this.cdCodigo + " - " + this.nmMinistro;
		
		return this.dsCodigoNomeMinistro;
	}
	
	public String getDsJubilado() {
		this.dsJubilado = (this.flJubilado? "Jubilado": "");
		return dsJubilado;
	}
	
	public boolean isFlInativo() {
		return (this.dtExcluido != null? true: false);
	}
	
	public boolean isFlMinistro() {
		this.flMinistro = this.dsDepartamento.equals("CONFRATERES");
		return flMinistro;
	}

}
