package br.com.convencao.bean.financeiro.relatorio;


import javax.faces.view.ViewScoped;
import javax.inject.Named;

import br.com.convencao.bean.codebehind.Codebehind;
import br.com.convencao.bo.NegocioException;
import br.com.convencao.util.jsf.FacesUtil;

@Named(value = "rptFinanceiroCartaCobrancaBean")
@ViewScoped
public class RptFinanceiroCartaCobranca extends Codebehind {
	
	private static final long serialVersionUID = 1L;
	
	public void inicializar() {
		if(!getRelatorioFiltro().isFlBuscarAjax()) {
			if(this.inicializarRegioes("FIN") == 1) {
				this.getRelatorioFiltro().setRegiaoItensFiltro(this.getRegiaoItens().get(0));
			
			}
		}
	}

	public void imprimir() {
		if(getRelatorioFiltro().getRegiaoItensFiltro() == null) {
			throw new NegocioException("Região é de preenchimento obrigatório.");
		}
		
		// Imprimir relatorio
		StringBuilder jsFunction = new StringBuilder()
				.append("relatorioFinanceiroCartaCobranca(")
				.append("'../../relatorioFinanceiroCartaCobranca'")
				.append(",")
				.append(getRelatorioFiltro().getRegiaoItensFiltro().getSqRegiao())
				.append(",")
				.append(getRelatorioFiltro().getIgreja() != null? getRelatorioFiltro().getIgreja().getSqIgreja(): -1)
				.append(",")
				.append(getRelatorioFiltro().isFlConsiderarAnuidadeCorrente())
				.append(",'")
				.append("Pr. JOEL LOPES DE SOUZA")
				.append("','")
				.append("1º Tesoureiro")
				.append("')");
		
		// Chama função javaScript para exibir relatório
		FacesUtil.getRequestContext().executeScript(jsFunction.toString());
		this.getRelatorioFiltro().setFlBuscarAjax(true);
	}
	
	public void buscarIgrejas() {
		if(getRelatorioFiltro().getRegiaoItensFiltro() != null)
			this.inicializarIgrejas(getRelatorioFiltro().getRegiaoItensFiltro().getSqRegiao());
		else
			this.setIgrejas(null);
	}
}
