package br.com.convencao.bean.cadastro;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import br.com.convencao.bo.NegocioException;
import br.com.convencao.util.jsf.FacesUtil;

@Named(value = "ministroRelatorioPrPresidente")
@ViewScoped
public class MinistroRelatorioPrPresidente extends MinistroCodbehind {

	private static final long serialVersionUID = 1L;

	public void inicializar() {
		if(!this.getFlBuscarAjax()){
			if(this.inicializarRegioes("SEC") == 1) {
				this.getFiltro().setRegiaoItensFiltro(this.getRegiaoItens().get(0));
			}
		}
	}

	public void imprimir() {

		// Validar preechimento da combo região
		if(!this.validarPreenchimentoComboRegiao()) {
			throw new NegocioException("Região: preenchimento obrigatório!");
		}

		// Monta parametros para exibir relatório
		StringBuilder jsFunction = new StringBuilder();
		jsFunction.append("relatorioMinistroPrPresidente(");
		jsFunction.append("'../../relatorioMinistroPrPresidente'");
		jsFunction.append(",'");
		jsFunction.append(getRelatorioFiltro().getTpRelatorio());
		jsFunction.append("',");
		jsFunction.append(this.getFiltro().getRegiaoItensFiltro() != null? this.getFiltro().getRegiaoItensFiltro().getSqRegiao(): 0);
		jsFunction.append(",");
		jsFunction.append(this.getFiltro().getCdSituacaoFiltro());
		jsFunction.append(");");

		// Chama função javaScript para exibir relatório
		// FacesUtil.getRequestContext().execute(jsFunction.toString());
		FacesUtil.getRequestContext().executeScript(jsFunction.toString());

	}

}
