package br.com.convencao.bean.registro;

import java.io.Serializable;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.convencao.bo.DepartamentoBO;
import br.com.convencao.model.Departamento;
import br.com.convencao.util.jsf.FacesUtil;

@Named(value = "departamentoPesquisaBean")
@ViewScoped
public class DepartamentoPesquisaBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private DepartamentoBO departamentoBO;
	
	private List<Departamento> lista;
	private Departamento selecionado;
	
	public void listar(){
		this.lista = this.departamentoBO.findAll();
	}

	public void buscar() {
		String param = FacesUtil.obterParametro("departamento");
		this.selecionado = departamentoBO.findByPrimaryKey(Long.parseLong(param));
	}

	
	public void excluir(){
		departamentoBO.delete(selecionado);

		// Refazer a pesquisa para listar as convenções
		this.listar();

		FacesUtil.addInfoMessage("Departamento " + selecionado.getDsReduzido() + " excluido com sucesso!");
	}
	
	public List<Departamento> getLista() {
		if(this.lista == null)
			this.listar();;
		
		return lista;
	}
	
	public void setDepartamentoLista(List<Departamento> lista) {
		this.lista = lista;
	}
	
	public Departamento getSelecionado() {
		return selecionado;
	}
	
	public void setSelecionado(Departamento selecionado) {
		this.selecionado = selecionado;
	}

	public String getMensagemRodape(){
		return "Total registros encontrados: " + this.lista.size();
	}
}
