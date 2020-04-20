package br.com.convencao.bean.cadastro;

import java.io.Serializable;

import javax.inject.Inject;

import br.com.convencao.model.Estado;
import br.com.convencao.model.Ministro;

public class IgrejaFiltro implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private RegiaoItens  regiaoItens;
	
	@Inject
	private Ministro ministro ;
	
	@Inject
	private Estado estado;
	
	private String dsIgrejaFiltro;
	private String dsCnpjFiltro;
	private String dsBairroFiltro;
	private String dsCidadeFiltro;
	
	public RegiaoItens getRegiaoItens() {
		return regiaoItens;
	}
	
	public void setRegiaoItens(RegiaoItens regiaoItens) {
		this.regiaoItens = regiaoItens;
	}
	
	public String getDsIgrejaFiltro() {
		return dsIgrejaFiltro;
	}
	
	public void setDsIgrejaFiltro(String dsIgrejaFiltro) {
		this.dsIgrejaFiltro = dsIgrejaFiltro;
	}
	
	public String getDsCnpjFiltro() {
		return dsCnpjFiltro;
	}
	
	public void setDsCnpjFiltro(String dsCnpjFiltro) {
		this.dsCnpjFiltro = dsCnpjFiltro;
	}
	
	public Ministro getMinistro() {
		return ministro;
	}
	
	public void setMinistro(Ministro ministro) {
		this.ministro = ministro;
	}
	
	public String getDsBairroFiltro() {
		return dsBairroFiltro;
	}
	
	public void setDsBairroFiltro(String dsBairroFiltro) {
		this.dsBairroFiltro = dsBairroFiltro;
	}
	
	public String getDsCidadeFiltro() {
		return dsCidadeFiltro;
	}
	
	public void setDsCidadeFiltro(String dsCidadeFiltro) {
		this.dsCidadeFiltro = dsCidadeFiltro;
	}
	
	public Estado getEstado() {
		return estado;
	}
	
	public void setEstado(Estado estado) {
		this.estado = estado;
	}
}
