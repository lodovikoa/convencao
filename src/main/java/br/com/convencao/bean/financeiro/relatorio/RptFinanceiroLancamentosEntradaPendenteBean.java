package br.com.convencao.bean.financeiro.relatorio;

import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.convencao.bean.cadastro.MinistroFiltro;
import br.com.convencao.bean.codebehind.Codebehind;
import br.com.convencao.bo.MinistroBO;
import br.com.convencao.bo.NegocioException;
import br.com.convencao.model.Ministro;
import br.com.convencao.util.jsf.FacesUtil;

@Named(value = "rptFinanceiroLancamentosEntradaPendenteBean")
@ViewScoped
public class RptFinanceiroLancamentosEntradaPendenteBean extends Codebehind {

	private static final long serialVersionUID = 1L;

	@Inject
	private MinistroBO ministroBO;

	@Inject
	private MinistroFiltro ministroFiltro;

	private List<Ministro> ministros;	

	public void inicializar() {
		if(!this.getRelatorioFiltro().isFlBuscarAjax()) {
			this.inicializarDepartamentos("pesquisa");
			this.getRelatorioFiltro().getDepartamento().setSqDepartamento(1L);
			if(this.inicializarRegioes("FIN") == 1) {
				this.getRelatorioFiltro().setRegiaoItensFiltro(this.getRegiaoItens().get(0));
				this.buscarIgrejasEministros();
			}
			this.inicializarCargos();
			this.getRelatorioFiltro().setCdSituacaoMinistro(1);
			this.inicializarPlanoContasPorTipo("C");
		}
	}

	public void imprimir() {
		if(this.getRelatorioFiltro().getRegiaoItensFiltro() == null) {
			throw new NegocioException("Região é de preenchimento obrigatório.");
		}
		
		// Pegando a primaryKey dos registros de Tipo de Lançamento
		String dsSqTipoLancamentosSelecionados = null;
		for (String st : this.getRelatorioFiltro().getTipoLancamentosSelecionadosString()) {
			if(dsSqTipoLancamentosSelecionados == null ) {
				 dsSqTipoLancamentosSelecionados = "(" + st.substring(st.indexOf(" {")+2, st.lastIndexOf("} "));
			} else {
				 dsSqTipoLancamentosSelecionados = dsSqTipoLancamentosSelecionados + "," + st.substring(st.indexOf(" {")+2, st.lastIndexOf("} "));
			}
		}
				
		if(dsSqTipoLancamentosSelecionados != null)
			dsSqTipoLancamentosSelecionados = dsSqTipoLancamentosSelecionados + ")";
		
		// Imprimir relatorio
		StringBuilder jsFunction = new StringBuilder()
				.append("relatorioFinanceiroEntradaPendente(")
				.append("'../../relatorioFinanceiroEntradaPendente'")
				.append(",'")
				.append(getRelatorioFiltro().getTpRelatorio())
				.append("',")
				.append(this.getRelatorioFiltro().getDepartamento() != null? this.getRelatorioFiltro().getDepartamento().getSqDepartamento(): null)
				.append(",'")
				.append(this.getRelatorioFiltro().getDepartamento() != null? this.getRelatorioFiltro().getDepartamento().getDsReduzido(): null)
				.append("',")
				.append(this.getRelatorioFiltro().getRegiaoItensFiltro().getSqRegiao())
				.append(",'")
				.append(this.getRelatorioFiltro().getRegiaoItensFiltro().getDsRegiao())
				.append("',")
				.append(this.getRelatorioFiltro().getIgreja() != null? this.getRelatorioFiltro().getIgreja().getSqIgreja(): null)
				.append(",'")
				.append(this.getRelatorioFiltro().getIgreja() != null? this.getRelatorioFiltro().getIgreja().getDsIgreja(): null)
				.append("',")
				.append(this.getRelatorioFiltro().getCargo() != null? this.getRelatorioFiltro().getCargo().getSqCargo(): null)
				.append(",'")
				.append(this.getRelatorioFiltro().getCargo() != null? this.getRelatorioFiltro().getCargo().getDsCargo(): null)
				.append("',")
				.append(this.getRelatorioFiltro().getMinistro() != null? this.getRelatorioFiltro().getMinistro().getSqMinistro(): null)
				.append(",'")
				.append(this.getRelatorioFiltro().getMinistro() != null? this.getRelatorioFiltro().getMinistro().getNmNome(): null)
				.append("',")
				.append(this.getRelatorioFiltro().getPlanoConta() != null? this.getRelatorioFiltro().getPlanoConta().getSqPlanoConta(): null)
				.append(",'")
				.append(this.getRelatorioFiltro().getPlanoConta() != null? this.getRelatorioFiltro().getPlanoConta().getDsConta(): null)
				.append("','")
				.append(dsSqTipoLancamentosSelecionados != null? dsSqTipoLancamentosSelecionados: null) 
				.append("',")
				.append(this.getRelatorioFiltro().getCdSituacaoMinistro())	
				.append(")");
		
		// Chama função javaScript para exibir relatório
		// FacesUtil.getRequestContext().execute(jsFunction.toString());
		FacesUtil.getRequestContext().executeScript(jsFunction.toString());
	}

	public void buscarIgrejasEministros() {
		this.getRelatorioFiltro().setFlBuscarAjax(true);
		this.buscarIgrejas();
		this.buscarMinistros();
	}

	public void buscarIgrejas() {
		if(this.getRelatorioFiltro().getRegiaoItensFiltro() != null)
			this.inicializarIgrejas(this.getRelatorioFiltro().getRegiaoItensFiltro().getSqRegiao(), true);
		else
			this.setIgrejas(null);
	}

	public void buscarMinistros() {
		if(this.getRelatorioFiltro().getRegiaoItensFiltro() != null) {
			this.ministroFiltro.setRegiaoItensFiltro(this.getRelatorioFiltro().getRegiaoItensFiltro());
			this.ministroFiltro.setIgrejaFiltro(this.getRelatorioFiltro().getIgreja());
			this.ministroFiltro.setDepartamentoFiltro(this.getRelatorioFiltro().getDepartamento());
			this.ministroFiltro.setCargoFiltro(this.getRelatorioFiltro().getCargo());

			this.ministros = ministroBO.findByParametros(ministroFiltro);
		} else {
			this.ministros = null;
		}
	}

	public MinistroFiltro getMinistroFiltro() {
		return ministroFiltro;
	}

	public void setMinistroFiltro(MinistroFiltro ministroFiltro) {
		this.ministroFiltro = ministroFiltro;
	}

	public List<Ministro> getMinistros() {
		return ministros;
	}

	public void setMinistros(List<Ministro> ministros) {
		this.ministros = ministros;
	}
}
