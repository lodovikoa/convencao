package br.com.convencao.bean.auditoria;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.convencao.bo.AuditoriaBO;
import br.com.convencao.model.Auditoria;
import br.com.convencao.model.to.AuditoriaCpl;
import br.com.convencao.util.jsf.FacesUtil;

@Named(value="auditoriaPesquisaBean")
@ViewScoped
public class AuditoriaPesquisaBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private AuditoriaBO bo;
	
	@Inject
	AuditoriaFiltro filtro;

	private List<Auditoria> lista;
	private AuditoriaCpl selecionado;
	private List<String> tabelas;
	private List<String> usuarios;


	private void listar() {
		
		lista = bo.findAByParametros(filtro);
			
		for (int i = 0; i < lista.size(); i++) {
			lista.get(i).setDsTipo(lista.get(i).getDsTipo());
		}
	}
	
	public void inicializar(){
		this.usuarios = bo.findAllUsuarios();
		// Caso tenha objeto null, remove da lista
		this.usuarios.remove(null);
		
		this.tabelas = bo.findAllTabelas();
	}

	public void buscar(){
		String param = FacesUtil.obterParametro("auditoria");
		this.selecionado = bo.findByPrimaryKey(Long.parseLong(param));

		if(selecionado != null && selecionado.getAuditoria() != null){
			selecionado.getAuditoria().setDsTipo(this.tipoAlteracao(selecionado.getAuditoria().getDsTipo()));;
		}
	}

	public void pesquisar(){
		// Adicionar 23:59h na data fim 
		if(this.filtro.getDtFimFiltro() != null){
			LocalDateTime dtFim = this.filtro.getDtFimFiltro().plusHours(23);
			dtFim = dtFim.plusMinutes(59);
			this.filtro.setDtFimFiltro(dtFim);
		}
		this.listar();
	}

	public List<Auditoria> getLista() {
		if(this.lista == null)
			this.lista = new ArrayList<Auditoria>();

		return lista;
	}

	public AuditoriaCpl getSelecionado() {
		return selecionado;
	}

	public void setSelecionado(AuditoriaCpl selecionado) {
		this.selecionado = selecionado;
	}

	public AuditoriaFiltro getFiltro() {
		return filtro;
	}
	
	public void setFiltro(AuditoriaFiltro filtro) {
		this.filtro = filtro;
	}
	
	public List<String> getTabelas() {
		return tabelas;
	}
	
	public List<String> getUsuarios() {
		return usuarios;
	}

	public String getMensagemRodape(){
		return "Total registros encontrados: " + this.lista	.size();
	}
	
	private String tipoAlteracao(String tp) {
		String tpRetorno = "";
		switch(tp){
		case "A":
			tpRetorno = "Alteração";
			break;
			
		case "I":
			tpRetorno = "Inclusão";
			break;
			
		case "E":
			tpRetorno = "Exclusão";
			break;
			
			default:
				tpRetorno = "Não identificado";
		}
		return tpRetorno;
	}
}
