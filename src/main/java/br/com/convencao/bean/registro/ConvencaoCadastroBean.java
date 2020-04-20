package br.com.convencao.bean.registro;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.convencao.bean.codebehind.Codebehind;
import br.com.convencao.bo.ConvencaoBO;
import br.com.convencao.model.Convencao;
import br.com.convencao.model.Estado;
import br.com.convencao.util.jsf.FacesUtil;

@Named(value="convencaoCadastroBean")
@ViewScoped
public class ConvencaoCadastroBean extends Codebehind {

	private static final long serialVersionUID = 1L;
	
	private Convencao convencao;

	@Inject
	private ConvencaoBO convencaoBO;
	
	
	private void limpar(){
		this.convencao = new Convencao();
		this.convencao.setEstado(new Estado());
	}
	
	public void inicializar() {
		if(this.convencao == null) {
			this.limpar();
		}
		this.inicializarEstados();
	}
	
	public void salvar(){
		this.convencao = this.convencaoBO.salvar(this.convencao);

		FacesUtil.addInfoMessage("Convenção salva com sucesso!");

	}
	
	public Convencao getConvencao() {
		return convencao;
	}
	
	public void setConvencao(Convencao convencao) {
		this.convencao = convencao;
	}
	
	public boolean isEditando(){
		boolean retorno = false;
		if(this.convencao != null)
			retorno = this.convencao.getSqConvencao() != null;
		
		return retorno;
	}
	
}
