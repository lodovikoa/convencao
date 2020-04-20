package br.com.convencao.bean.registro;

import java.io.Serializable;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.convencao.bo.ConvencaoBO;
import br.com.convencao.bo.RegiaoBO;
import br.com.convencao.model.Convencao;
import br.com.convencao.model.Regiao;
import br.com.convencao.util.jsf.FacesUtil;

@Named(value="regiaoCadastroBean")
@ViewScoped
public class RegiaoCadastroBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Regiao regiao;

	private List<Convencao> convencoes;
	
	@Inject
	private ConvencaoBO covencaoBO;
	
	@Inject
	private RegiaoBO bo;
	
	
	public void inicializar() {
		if (this.regiao == null) {
			this.limpar();
		}
		
		this.inicializarConvencao();
	}
	
	public void inicializarConvencao(){
		this.convencoes =  covencaoBO.listarConvencoes();
	}
	
	private void limpar(){
		this.regiao = new Regiao();
		this.regiao.setConvencao(new Convencao());
//		this.convencoes = new ArrayList<>();
	}
	
	public void salvar(){
		this.regiao = this.bo.salvar(this.regiao);

		FacesUtil.addInfoMessage("Região salva com sucesso!");

	}
	
	public Regiao getRegiao() {
		return regiao;
	}
	
	public void setRegiao(Regiao regiao) {
		this.regiao = regiao;
	}
	
	public List<Convencao> getConvencoes() {
		return convencoes;
	}
	
	public boolean isEditando(){
		boolean retorno = false;
		if(this.regiao != null)
			retorno = this.regiao.getSqRegiao() != null;
		
		return retorno;
	}
	
}
