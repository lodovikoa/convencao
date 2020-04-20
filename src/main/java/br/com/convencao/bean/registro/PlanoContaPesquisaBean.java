package br.com.convencao.bean.registro;

import java.io.Serializable;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.convencao.bo.PlanoContaBO;
import br.com.convencao.model.PlanoConta;
import br.com.convencao.util.jsf.FacesUtil;

@Named(value="planoContaPesquisaBean")
@ViewScoped
public class PlanoContaPesquisaBean implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Inject
	private PlanoContaBO bo;
	
	private List<PlanoConta> lista;
	private PlanoConta selecionado;
	
	
	private void listar() {
		this.lista = bo.findAll();
	}

	public void buscar(){
		String param = FacesUtil.obterParametro("planoConta");
		this.selecionado = bo.findByPrimaryKey(Long.parseLong(param));
	}

	public void excluir(){
		bo.remover(selecionado);

		// Refazer a pesquisa para listar as convenções
		this.listar();

		FacesUtil.addInfoMessage("Plano de conta " + selecionado.getDsConta() + " excluido com sucesso!");
	}
	
	
	public List<PlanoConta> getlista() {
		if(this.lista == null)
			this.listar();
		
		return lista;
	}
	
	public void setLista(List<PlanoConta> lista) {
		this.lista = lista;
	}
	public PlanoConta getSelecionado() {
		return selecionado;
	}
	public void setSelecionado(PlanoConta selecionado) {
		this.selecionado = selecionado;
	}
	
	public String getMensagemRodape(){
		return "Total registros encontrados: " + this.lista.size();
	}

}
