package br.com.convencao.bean.financeiro.relatorio;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import br.com.convencao.bean.codebehind.Codebehind;
import br.com.convencao.bo.NegocioException;
import br.com.convencao.util.Uteis;
import br.com.convencao.util.jsf.FacesUtil;

@Named(value = "rptFinanceiroLancamentosEntradaReciboBean")
@ViewScoped
public class RptFinanceiroLancamentosEntradaReciboBean extends Codebehind {

	private static final long serialVersionUID = 1L;

	public void inicializar() {
		if(!getRelatorioFiltro().isFlBuscarAjax()) {
			if(this.inicializarRegioes("FIN") == 1) {
				this.getRelatorioFiltro().setRegiaoItensFiltro(this.getRegiaoItens().get(0));
			}
			this.inicializarDepartamentos("pesquisa");
			this.getRelatorioFiltro().getDepartamento().setSqDepartamento(1L);
		}
	}

	public void imprimir() {
		if(getRelatorioFiltro().getRegiaoItensFiltro() == null || getRelatorioFiltro().getRegiaoItensFiltro().getSqRegiao() == null) {
			throw new NegocioException("Região: preenchimento obrigatório!");
		}

		// Imprimir relatorio
		StringBuilder jsFunction = new StringBuilder()
				.append("relatorioFinanceiroEntradaRecibo(")
				.append("'../../relatorioFinanceiroEntradaRecibo'")
				.append(",'")
				.append(getRelatorioFiltro().getTpRelatorio())
				.append("',")
				.append(getRelatorioFiltro().getDepartamento() != null?  getRelatorioFiltro().getDepartamento().getSqDepartamento(): 0L)
				.append(",'")
				.append(getRelatorioFiltro().getDepartamento() != null? getRelatorioFiltro().getDepartamento().getDsReduzido(): null)
				.append("',")
				.append(getRelatorioFiltro().getRegiaoItensFiltro().getSqRegiao())
				.append(",'")
				.append(getRelatorioFiltro().getRegiaoItensFiltro().getDsRegiao())
				.append("','")
				.append(Uteis.LocalDateParaString(getRelatorioFiltro().getDtInicio()))
				.append("','")
				.append(Uteis.LocalDateParaString(getRelatorioFiltro().getDtFim()))
				.append("',")
				.append(getRelatorioFiltro().getCdOrdem())
				.append(")");

		// Chama função javaScript para exibir relatório
		// FacesUtil.getRequestContext().execute(jsFunction.toString());
		FacesUtil.getRequestContext().executeScript(jsFunction.toString());
		
		this.getRelatorioFiltro().setFlBuscarAjax(true);
	}

}
