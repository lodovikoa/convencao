package br.com.convencao.bean.financeiro.entrada;

import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.convencao.bean.financeiro.LancamentoCodbehind;
import br.com.convencao.bo.RegLancamentoBO;
import br.com.convencao.model.to.RegLancamentoListLote;
import br.com.convencao.util.jsf.FacesUtil;

@Named(value = "lancamentoEntradaDeleteEmLoteBean")
@ViewScoped
public class LancamentoEntradaDeleteEmLoteBean extends LancamentoCodbehind {

	private static final long serialVersionUID = 1L;

	private List<RegLancamentoListLote> listaEmLote;
	private List<RegLancamentoListLote> listaEmLoteSelecionada;
	
	@Inject
	RegLancamentoBO regLancamentoBO;

	public void inicializar() {
		// Inicializar departamento
		this.inicializarDepartamentoPrincipal(1L);
		
		// Inicializar lancamentos em lote em aberto
		this.listaEmLote = regLancamentoBO.findLancamentosEmLoteEmAberto(1, this.getDepartamento().getSqDepartamento());
		
	}

	public void salvarExclusaoEmLote() {
		
		// Validar e excluir Lançamentos em Lote
		StringBuilder resultado = regLancamentoBO.salvarExcluirEmLote(this.getDepartamento(), this.listaEmLoteSelecionada);
		
		// Limpar lista selecionada
		this.listaEmLoteSelecionada = null;
		
		FacesUtil.addInfoMessage("Operação realizada - " + resultado.toString());
	}

	public List<RegLancamentoListLote> getListaEmLote() {
		return listaEmLote;
	}
	
	public List<RegLancamentoListLote> getListaEmLoteSelecionada() {
		return listaEmLoteSelecionada;
	}
	
	public void setListaEmLoteSelecionada(List<RegLancamentoListLote> listaEmLoteSelecionada) {
		this.listaEmLoteSelecionada = listaEmLoteSelecionada;
	}
}