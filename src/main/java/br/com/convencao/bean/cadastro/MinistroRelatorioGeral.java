package br.com.convencao.bean.cadastro;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import br.com.convencao.bo.NegocioException;
import br.com.convencao.util.jsf.FacesUtil;

@Named(value = "ministroRelatorioGeral")
@ViewScoped
public class MinistroRelatorioGeral extends MinistroCodbehind {

	private static final long serialVersionUID = 1L;
	
	public void inicializar() {
		if(!this.getFlBuscarAjax()){
			if(this.inicializarRegioes("SEC") == 1 ) {
				this.getFiltro().setRegiaoItensFiltro(this.getRegiaoItens().get(0));
				this.buscarIgrejasAjax();
			}
			this.inicializarDepartamentos("pesquisa");
			this.inicializarCargos();
		}
	}
	
	public void imprimir() {
		
		// Validar preechimento da combo região
		if(!this.validarPreenchimentoComboRegiao()) {
			throw new NegocioException("Região: preenchimento obrigatório!");
		}
		
		// Monta parametros para exibir relatório
		StringBuilder jsFunction = new StringBuilder();
		jsFunction.append("relatorioMinistroGeral(");
		jsFunction.append("'../../relatorioMinistroGeral'");
		jsFunction.append(",'");
		jsFunction.append(getFiltro().getTpRelatorio());
		jsFunction.append("',");
		jsFunction.append(this.getFiltro().getRegiaoItensFiltro() != null? this.getFiltro().getRegiaoItensFiltro().getSqRegiao(): 0);
		jsFunction.append(",");
		jsFunction.append(this.getFiltro().getIgrejaFiltro() != null? this.getFiltro().getIgrejaFiltro().getSqIgreja(): 0);
		jsFunction.append(",");
		jsFunction.append(this.getFiltro().getDepartamentoFiltro() != null? this.getFiltro().getDepartamentoFiltro().getSqDepartamento(): 0);
		jsFunction.append(",");
		jsFunction.append(this.getFiltro().getCargoFiltro() != null?  this.getFiltro().getCargoFiltro().getSqCargo(): 0);
		jsFunction.append(",");
		jsFunction.append(this.getFiltro().getCdSituacaoFiltro());
		jsFunction.append(",");
		jsFunction.append(this.getFiltro().getNnOrdemExibicao());
		jsFunction.append(");");
		
		// Chama função javaScript para exibir relatório
		// FacesUtil.getRequestContext().execute(jsFunction.toString());
		FacesUtil.getRequestContext().executeScript(jsFunction.toString());
			
	}

}
