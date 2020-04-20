package br.com.convencao.bean.registro;

import java.io.Serializable;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.convencao.bo.EstadoCivelBO;
import br.com.convencao.model.EstadoCivel;
import br.com.convencao.util.jsf.FacesUtil;

@Named(value="estadoCivelPesquisaBean")
@ViewScoped
public class EstadoCivelPesquisaBean implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Inject
	private EstadoCivelBO estadoCivelBO;
	
	private List<EstadoCivel> lista;
	private EstadoCivel selecionado;
	
	
	private void listar() {
		this.lista = estadoCivelBO.findAll();
	}
	
	public void buscarEstadoCivel(){
		String param = FacesUtil.obterParametro("estadoCivel");
		this.selecionado = estadoCivelBO.findByPrimaryKey(Long.parseLong(param));
	}
	
	public void excluir(){
		estadoCivelBO.remover(selecionado);

		// Refazer a pesquisa para listar
		this.listar();

		FacesUtil.addInfoMessage("Estado civil " + selecionado.getDsEstadoCivel() + " excluido com sucesso!");
	}

	
	public List<EstadoCivel> getLista() {
		if(this.lista == null)
			this.listar();
		
		return lista;
	}
	
	public void setLista(List<EstadoCivel> lista) {
		this.lista = lista;
	}
	public EstadoCivel getSelecionado() {
		return selecionado;
	}
	public void setSelecionado(EstadoCivel selecionado) {
		this.selecionado = selecionado;
	}
	
	public String getMensagemRodape(){
		return "Total registros encontrados: " + this.lista	.size();
	}

}
