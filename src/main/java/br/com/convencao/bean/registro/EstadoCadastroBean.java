package br.com.convencao.bean.registro;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.convencao.bo.EstadoBO;
import br.com.convencao.model.Estado;
import br.com.convencao.util.jsf.FacesUtil;

@Named(value="estadoCadastroBean")
@ViewScoped
public class EstadoCadastroBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private Estado estado;

	@Inject
	private EstadoBO estadoBO;


	public void inicializar() {
		if(this.estado == null)
			this.limpar();
	}

	private void limpar(){
		this.estado = new Estado();

	}

	public void salvar(){
		this.estado = this.estadoBO.salvar(this.estado);

		FacesUtil.addInfoMessage("Estado salvo com sucesso!");

	}

	public Estado getEstado() {
		return estado;
	}

	public void setEstado(Estado estado) {
		this.estado = estado;
	}


	public boolean isEditando(){
		boolean retorno = false;
		if(this.estado != null)
			retorno = this.estado.getSqEstado() != null;

		return retorno;
	}

}
