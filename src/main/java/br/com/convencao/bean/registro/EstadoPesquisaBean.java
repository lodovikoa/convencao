package br.com.convencao.bean.registro;

import java.io.Serializable;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.convencao.bo.EstadoBO;
import br.com.convencao.model.Estado;
import br.com.convencao.util.jsf.FacesUtil;

@Named(value="estadoPesquisaBean")
@ViewScoped
public class EstadoPesquisaBean implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Inject
	private EstadoBO estadoBO;
	
	private List<Estado> lista;
	private Estado selecionado;
	
	
	private void listarEstados() {
		this.lista = estadoBO.findAllEstados();
	}
	
	public void buscar(){
		String param = FacesUtil.obterParametro("estado");
		this.selecionado = estadoBO.findByPrimaryKey(Long.parseLong(param));
	}
	
	public void excluir(){
		estadoBO.remover(selecionado);

		// Refazer a pesquisa para listar as convenções
		this.listarEstados();

		FacesUtil.addInfoMessage("Estado " + selecionado.getDsUf() + " excluido com sucesso!");

	}
	
	
	public List<Estado> getLista() {
		if(this.lista == null)
			this.listarEstados();
		
		return lista;
	}
	
	public void setLista(List<Estado> lista) {
		this.lista = lista;
	}
	public Estado getSelecionado() {
		return selecionado;
	}
	public void setSelecionado(Estado selecionado) {
		this.selecionado = selecionado;
	}
	
	public String getMensagemRodape(){
		return "Total registros encontrados: " + this.lista.size();
	}

}
