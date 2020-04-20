package br.com.convencao.bean.registro;

import java.io.Serializable;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.convencao.bo.CargoBO;
import br.com.convencao.model.Cargo;
import br.com.convencao.util.jsf.FacesUtil;

@Named(value="cargoPesquisaBean")
@ViewScoped
public class CargoPesquisaBean implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Inject
	private CargoBO cargoBO;
	
	private List<Cargo> lista;
	private Cargo selecionado;
	
	
	private void listar() {
		this.lista = cargoBO.findAll();
	}
	
	public void buscarCargo(){
		String param = FacesUtil.obterParametro("cargo");
		this.selecionado = cargoBO.findByPrimaryKey(Long.parseLong(param));
	}
	
	public void excluir(){
		cargoBO.remover(selecionado);

		// Refazer a pesquisa para listar as convenções
		this.listar();

		FacesUtil.addInfoMessage("Cargo " + selecionado.getDsCargo() + " excluido com sucesso!");

	}
	
	
	public List<Cargo> getLista() {
		if(this.lista == null)
			this.listar();
		
		return lista;
	}
	
	public void setLista(List<Cargo> lista) {
		this.lista = lista;
	}
	public Cargo getCargoSelecionado() {
		return selecionado;
	}
	public void setCargoSelecionado(Cargo selecionado) {
		this.selecionado = selecionado;
	}
	
	public String getMensagemRodape(){
		return "Total registros encontrados: " + this.lista	.size();
	}

}
