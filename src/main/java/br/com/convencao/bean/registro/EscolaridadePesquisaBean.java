package br.com.convencao.bean.registro;

import java.io.Serializable;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.convencao.bo.EscolaridadeBO;
import br.com.convencao.model.Escolaridade;
import br.com.convencao.util.jsf.FacesUtil;

@Named(value="escolaridadePesquisaBean")
@ViewScoped
public class EscolaridadePesquisaBean implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Inject
	private EscolaridadeBO escolaridadeBO;
	
	private List<Escolaridade> lista;
	private Escolaridade selecionado;
	
	
	private void listar() {
		this.lista = escolaridadeBO.findAll();
	}
	
	public void buscar(){
		String param = FacesUtil.obterParametro("escolaridade");
		this.selecionado = escolaridadeBO.findByPrimaryKey(Long.parseLong(param));
	}
	
	public void excluir(){
		escolaridadeBO.remover(selecionado);

		// Refazer a pesquisa para listar as convenções
		this.listar();

		FacesUtil.addInfoMessage("Escolaridade " + selecionado.getDsDescricao() + " excluido com sucesso!");
	}

	
	public List<Escolaridade> getlista() {
		if(this.lista == null)
			this.listar();
		
		return lista;
	}
	
	public void setLista(List<Escolaridade> lista) {
		this.lista = lista;
	}
	public Escolaridade getSelecionado() {
		return selecionado;
	}
	public void setSelecionado(Escolaridade selecionado) {
		this.selecionado = selecionado;
	}
	
	public String getMensagemRodape(){
		return "Total registros encontrados: " + this.lista.size();
	}

}
