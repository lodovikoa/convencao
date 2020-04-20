package br.com.convencao.bean.registro;

import java.io.Serializable;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.convencao.bo.TipoLancamentoBO;
import br.com.convencao.model.TipoLancamento;
import br.com.convencao.util.jsf.FacesUtil;

@Named(value="tipoLancamentoPesquisaBean")
@ViewScoped
public class TipoLancamentoPesquisaBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private List<TipoLancamento> lista;
	
	@Inject
	private TipoLancamentoBO bo;
	
	private TipoLancamento selecionada;
	
	private void listar(){
		this.lista = bo.findAll();
	}
	
	public void excluir(){

		bo.remover(selecionada);

		// Refazer a pesquisa para listar as convenções
		this.listar();;

		FacesUtil.addInfoMessage("Tipo de Lançamento " + selecionada.getDsTipoLancamento() + " excluido com sucesso!");

	}
	
	public TipoLancamento getSelecionada() {
		return selecionada;
	}
	
	public void setSelecionada(TipoLancamento selecionada) {
		this.selecionada = selecionada;
	}
	
	
	
	
	
	public List<TipoLancamento> getLista() {
		if(this.lista == null)
			this.listar();
		
		return lista;
	}
	
	public void buscar(){
		String param = FacesUtil.obterParametro("tipoLancamento");
		this.selecionada = bo.findByPrimaryKey(Long.parseLong(param));
	}
	
	public String getMensagemRodape(){
		return "Total registros encontrados: " + this.lista.size();
	}
}
