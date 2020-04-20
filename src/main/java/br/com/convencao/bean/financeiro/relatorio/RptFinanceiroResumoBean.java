package br.com.convencao.bean.financeiro.relatorio;

import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.convencao.bean.codebehind.Codebehind;
import br.com.convencao.bo.LancamentoResumoBO;
import br.com.convencao.bo.NegocioException;
import br.com.convencao.model.LancamentoResumo;
import br.com.convencao.util.Uteis;
import br.com.convencao.util.jsf.FacesUtil;

@Named(value = "rptFinanceiroResumoBean")
@ViewScoped
public class RptFinanceiroResumoBean extends Codebehind {

	private static final long serialVersionUID = 1L;


	@Inject
	private LancamentoResumoBO resumoBO;

	private List<LancamentoResumo> lancamentoResumo;

	public void inicializar() {
		if(!getRelatorioFiltro().isFlBuscarAjax()) {
			if (this.inicializarRegioes("FIN") == 1 ) {			
				this.getRelatorioFiltro().setRegiaoItensFiltro(this.getRegiaoItens().get(0));
				this.buscarLancamentosResumo();
			}
		}

	}

	public void imprimir() {
		if(getRelatorioFiltro().getRegiaoItensFiltro() == null || getRelatorioFiltro().getRegiaoItensFiltro().getSqRegiao() == null) {
			throw new NegocioException("Região: preenchimento obrigatório!");
		}

		if(getRelatorioFiltro().getLancamentoResumoFiltro() == null || getRelatorioFiltro().getLancamentoResumoFiltro().getSqResumo() == null) {
			throw new NegocioException("Período: preenchimento obrigatório!");
		}

		// Imprimir relatorio
		StringBuilder jsFunction = new StringBuilder()
				.append("relatorioFinanceiroResumo(")
				.append("'../../relatorioFinanceiroResumo'")
				.append(",'")
				.append(getRelatorioFiltro().getTpRelatorio())
				.append("',")
				.append(getRelatorioFiltro().getLancamentoResumoFiltro().getSqResumo())
				.append(",'")
				.append(getRelatorioFiltro().getLancamentoResumoFiltro().getDsPeriodo())
				.append("','")
				.append(getRelatorioFiltro().getRegiaoItensFiltro().getDsRegiao())
				.append("','")
				.append(Uteis.LocalDateParaString(getRelatorioFiltro().getLancamentoResumoFiltro().getDtFechado()))				
				.append("')");

		// Chama função javaScript para exibir relatório
		// FacesUtil.getRequestContext().execute(jsFunction.toString());
		FacesUtil.getRequestContext().executeScript(jsFunction.toString());
	}

	public void buscarLancamentosResumo() {
		if(getRelatorioFiltro().getRegiaoItensFiltro() != null)
			this.lancamentoResumo = this.resumoBO.findAllPorRegiao(getRelatorioFiltro().getRegiaoItensFiltro().getSqRegiao());
		else
			this.lancamentoResumo = null;

		getRelatorioFiltro().setFlBuscarAjax(true);
	}

	public List<LancamentoResumo> getLancamentoResumo() {
		return lancamentoResumo;
	}

}
