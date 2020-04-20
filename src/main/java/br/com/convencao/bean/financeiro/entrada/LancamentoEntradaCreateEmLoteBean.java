package br.com.convencao.bean.financeiro.entrada;

import java.math.BigDecimal;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.convencao.bean.financeiro.LancamentoCodbehind;
import br.com.convencao.bo.RegLancamentoBO;
import br.com.convencao.model.Ministro;
import br.com.convencao.model.RegLancamento;
import br.com.convencao.model.Regiao;
import br.com.convencao.util.Uteis;
import br.com.convencao.util.jsf.FacesUtil;

@Named(value = "lancamentoEntradaCreateEmLoteBean")
@ViewScoped
public class LancamentoEntradaCreateEmLoteBean extends LancamentoCodbehind {

	private static final long serialVersionUID = 1L;

	private List<Regiao> regiaoList;
	private List<Regiao> regiaoSelecionados;
	private RegLancamento regLancamentoTemp;

	private boolean flInicializar = true;
	
	@Inject
	private RegLancamentoBO regLancamentoBO;

	public void inicializar() {
		if(flInicializar) {
			this.flInicializar = false;
			this.inicializarRegioes("TUD");
			this.inicializarDepartamentoPrincipal(1L);
			this.inicializarTipoLancamentoPorTipo("C");

			this.regLancamentoTemp = new RegLancamento();
			this.regLancamentoTemp.setMinistro(new Ministro());
			this.regLancamentoTemp.getMinistro().setDepartamento(this.getDepartamento());
			this.regLancamentoTemp.setDtVencimento(Uteis.DataHoje().toLocalDate());
			this.regLancamentoTemp.setVlLancamento(BigDecimal.ZERO);
			
		}
	}

	public void atualizarValorLancamento() {	
		 this.flInicializar = false;
		this.regLancamentoTemp.setVlLancamento(this.regLancamentoTemp.getTipoLancamento() == null? BigDecimal.ZERO: this.regLancamentoTemp.getTipoLancamento().getVlTipoLancamento());
	}

	public void salvarRecebimentosEmLote() {
		
		// Aplicar regras de negócio antes de salvar os registros
		StringBuilder stRetorno = regLancamentoBO.salvarEmLoate(this.regiaoSelecionados, this.regLancamentoTemp);
		
		FacesUtil.addInfoMessage("Lançamentos em lote salvos com sucesso: " + stRetorno.toString());

	}



	public List<Regiao> getRegiaoList() {
		return regiaoList;
	}

	public void setRegiaoList(List<Regiao> regiaoList) {
		this.regiaoList = regiaoList;
	}

	public List<Regiao> getRegiaoSelecionados() {
		return regiaoSelecionados;
	}

	public void setRegiaoSelecionados(List<Regiao> regiaoSelecionados) {
		this.regiaoSelecionados = regiaoSelecionados;
	}

	public RegLancamento getRegLancamentoTemp() {
		return regLancamentoTemp;
	}

	public void setRegLancamentoTemp(RegLancamento regLancamentoTemp) {
		this.regLancamentoTemp = regLancamentoTemp;
	}

	public boolean isFlInicializar() {
		return flInicializar;
	}

	public void setFlInicializar(boolean flInicializar) {
		this.flInicializar = flInicializar;
	}
}