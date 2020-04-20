package br.com.convencao.bean.registro;

import java.io.Serializable;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.convencao.bo.RegiaoBO;
import br.com.convencao.model.Regiao;
import br.com.convencao.util.jsf.FacesUtil;

@Named(value="regiaoPesquisaBean")
@ViewScoped
public class RegiaoPesquisaBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private List<Regiao> lista;
	
	@Inject
	private RegiaoBO bo;
	
	private Regiao selecionada;
	
	private void listar(){
		this.lista = bo.findAll();
	}
	
	public void excluir(){

		bo.remover(selecionada);

		// Refazer a pesquisa para listar as convenções
		this.listar();

		FacesUtil.addInfoMessage("Região " + selecionada.getDsRegiao() + " excluida com sucesso!");

	}
	
	
	public Regiao getSelecionada() {
		return selecionada;
	}
	
	public void setSelecionada(Regiao selecionada) {
		this.selecionada = selecionada;
	}
	
	public List<Regiao> getLista() {
		if(this.lista == null)
			this.listar();
		
		return lista;
	}
	
	public void buscar(){
		String param = FacesUtil.obterParametro("regiao");
		this.selecionada = bo.findByPrimaryKey(Long.parseLong(param));		
	}
	
	public String getMensagemRodape(){
		return "Total registros encontrados: " + this.lista.size();
	}
}
