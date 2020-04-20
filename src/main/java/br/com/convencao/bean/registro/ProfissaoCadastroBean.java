package br.com.convencao.bean.registro;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.convencao.bo.ProfissaoBO;
import br.com.convencao.model.Profissao;
import br.com.convencao.util.jsf.FacesUtil;

@Named(value="profissaoCadastroBean")
@ViewScoped
public class ProfissaoCadastroBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private Profissao profissao;

	@Inject
	private ProfissaoBO profissaoBO;


	public void inicializar() {
		if(profissao == null)
			this.limpar();
	}

	private void limpar(){
		this.profissao = new Profissao();

	}

	public void salvar(){
		this.profissao = this.profissaoBO.salvar(this.profissao);

		FacesUtil.addInfoMessage("Profissao salva com sucesso!");
	}

	public Profissao getProfissao() {
		return profissao;
	}

	public void setProfissao(Profissao profissao) {
		this.profissao = profissao;
	}

	public boolean isEditando(){
		boolean retorno = false;
		if(this.profissao != null)
			retorno = this.profissao.getSqProfissao() != null;

		return retorno;
	}

}
