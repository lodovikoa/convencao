package br.com.convencao.bean.registro;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.convencao.bo.EstadoCivelBO;
import br.com.convencao.model.EstadoCivel;
import br.com.convencao.util.jsf.FacesUtil;

@Named(value="estadoCivelCadastroBean")
@ViewScoped
public class EstadoCivelCadastroBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private EstadoCivel estadoCivel;

	@Inject
	private EstadoCivelBO estadoCivelBO;


	public void inicializar() {
		if(estadoCivel == null)
			this.limpar();
	}

	private void limpar(){
		this.estadoCivel = new EstadoCivel();

	}

	public void salvar(){
		this.estadoCivel = this.estadoCivelBO.salvar(this.estadoCivel);

		FacesUtil.addInfoMessage("Estado civil salvo com sucesso!");
	}

	public EstadoCivel getEstadoCivel() {
		return estadoCivel;
	}

	public void setEstadoCivel(EstadoCivel estadoCivel) {
		this.estadoCivel = estadoCivel;
	}

	public boolean isEditando(){
		boolean retorno = false;
		if(this.estadoCivel != null)
			retorno = this.estadoCivel.getSqEstadoCivel() != null;

		return retorno;
	}

}
