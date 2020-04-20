package br.com.convencao.bean.registro;

import java.io.Serializable;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.convencao.bo.PlanoContaBO;
import br.com.convencao.bo.TipoLancamentoBO;
import br.com.convencao.model.PlanoConta;
import br.com.convencao.model.TipoLancamento;
import br.com.convencao.util.jsf.FacesUtil;

@Named(value="tipoLancamentoCadastroBean")
@ViewScoped
public class TipoLancamentoCadastroBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private TipoLancamento tipoLancamento;

	private List<PlanoConta> planoContas;
	
	@Inject
	private PlanoContaBO planoContaBO;
	
	@Inject
	private TipoLancamentoBO bo;
	
	
	public void inicializar(){
		if(this.tipoLancamento == null) {
			this.limpar();
		}
		
		this.inicializarPlanoContas();
	}
	
	private void inicializarPlanoContas() {
		this.planoContas =  planoContaBO.findAll();
	}
	
	private void limpar(){
		this.tipoLancamento = new TipoLancamento();
		this.tipoLancamento.setPlanoConta(new PlanoConta());
	}
	
	public void salvar(){
		this.tipoLancamento = this.bo.salvar(this.tipoLancamento);

		System.out.println("================================PlanoContas = " + this.tipoLancamento.getPlanoConta().getDsConta());
		
		for (PlanoConta p : planoContas) {
			System.out.println("============================PlanoContas = " + p.getDsConta());
		}

		
		FacesUtil.addInfoMessage("Tipo de Lançamento salvo com sucesso!");

	}
	
	public TipoLancamento getTipoLancamento() {
		return tipoLancamento;
	}
	
	public void setTipoLancamento(TipoLancamento tipoLancamento) {
		this.tipoLancamento = tipoLancamento;
	}
	
	public List<PlanoConta> getPlanoContas() {
		return planoContas;
	}
	
	public boolean isEditando(){
		boolean retorno = false;
		if(this.tipoLancamento != null)
			retorno = this.tipoLancamento.getSqTipoLancamento() != null;
		
		return retorno;
	}
	
}
