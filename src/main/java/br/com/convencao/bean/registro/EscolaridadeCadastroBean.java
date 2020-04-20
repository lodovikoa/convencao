package br.com.convencao.bean.registro;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.convencao.bo.EscolaridadeBO;
import br.com.convencao.model.Escolaridade;
import br.com.convencao.util.jsf.FacesUtil;

@Named(value="escolaridadeCadastroBean")
@ViewScoped
public class EscolaridadeCadastroBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private Escolaridade escolaridade;

	@Inject
	private EscolaridadeBO escolaridadeBO;


	public void inicializar() {
		if(this.escolaridade == null)
			this.limpar();
	}

	private void limpar(){
		this.escolaridade = new Escolaridade();

	}

	public void salvar(){
		this.escolaridade = this.escolaridadeBO.salvar(this.escolaridade);

		FacesUtil.addInfoMessage("Escolaridade salva com sucesso!");

	}

	public Escolaridade getEscolaridade() {
		return escolaridade;
	}

	public void setEscolaridade(Escolaridade escolaridade) {
		this.escolaridade = escolaridade;
	}

	public boolean isEditando(){
		boolean retorno = false;
		if(this.escolaridade != null)
			retorno = this.escolaridade.getSqEscolaridade() != null;

		return retorno;
	}

}
