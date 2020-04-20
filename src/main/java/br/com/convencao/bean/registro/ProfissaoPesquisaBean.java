package br.com.convencao.bean.registro;

import java.io.Serializable;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.convencao.bo.ProfissaoBO;
import br.com.convencao.model.Profissao;
import br.com.convencao.util.jsf.FacesUtil;

@Named(value="profissaoPesquisaBean")
@ViewScoped
public class ProfissaoPesquisaBean implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Inject
	private ProfissaoBO profissaoBO;
	
	private List<Profissao> lista;
	private Profissao selecionado;
	
	
	private void listar() {
		this.lista = profissaoBO.findAll();
	}
	
	public void buscar(){
		String param = FacesUtil.obterParametro("profissao");
		this.selecionado = profissaoBO.findByPrimaryKey(Long.parseLong(param));
	}
	
	public void excluir(){
		profissaoBO.remover(selecionado);

		// Refazer a pesquisa para listar as convenções
		this.listar();

		FacesUtil.addInfoMessage("Profissão " + selecionado.getDsDescricao() + " excluido com sucesso!");

	}
	
	
	public List<Profissao> getlista() {
		if(this.lista == null)
			this.listar();
		
		return lista;
	}
	
	public void setLista(List<Profissao> lista) {
		this.lista = lista;
	}
	public Profissao getSelecionado() {
		return selecionado;
	}
	public void setSelecionado(Profissao selecionado) {
		this.selecionado = selecionado;
	}
	
	public String getMensagemRodape(){
		return "Total registros encontrados: " + this.lista.size();
	}

}
