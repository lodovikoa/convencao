package br.com.convencao.bean.registro;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.convencao.bo.PlanoContaBO;
import br.com.convencao.model.PlanoConta;
import br.com.convencao.util.jsf.FacesUtil;

@Named(value="planoContaCadastroBean")
@ViewScoped
public class PlanoContaCadastroBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private PlanoConta planoConta;

	@Inject
	private PlanoContaBO bo;

	public void inicializarPlanoConta(){
		if(this.isNovo()){
			Long proximoCodigo = bo.proximoCodigoConta();
			this.planoConta = new PlanoConta();
			this.planoConta.setCdConta(proximoCodigo);
		}
	}

	public PlanoContaCadastroBean() {
		this.limpar();
	}

	private void limpar(){
		this.planoConta = new PlanoConta();

	}

	public void salvar(){	
		this.planoConta = this.bo.salvar(this.planoConta);

		FacesUtil.addInfoMessage("Plano de conta salvo com sucesso!");
	}

	public PlanoConta getPlanoConta() {
		return planoConta;
	}

	public void setPlanoConta(PlanoConta planoConta) {
		this.planoConta = planoConta;
	}

	// Retorna true se estiver editando
	public boolean isEditando(){
		return !this.isNovo();
	}

	// Retorna true se for novo registro
	private boolean isNovo(){
		return this.planoConta == null || this.planoConta.getSqPlanoConta() == null;

	}

}
