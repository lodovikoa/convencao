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
import br.com.convencao.util.Uteis;
import br.com.convencao.util.jsf.FacesUtil;

@Named(value = "rptFinanceiroLancamentosEntradaMinistroBean")
@ViewScoped
public class RptFinanceiroLancamentosEntradaMinistroBean extends Codebehind {

	private static final long serialVersionUID = 1L;

	@Inject
	private MinistroBO ministroBO;
	
	@Inject
	private MinistroFiltro ministroFiltro;

	private List<Ministro> ministros;

	public void inicializar() {
		if(!getRelatorioFiltro().isFlBuscarAjax()) {
			this.inicializarDepartamentos("pesquisa");
			if(this.getRelatorioFiltro().getDepartamento() != null)
				this.getRelatorioFiltro().getDepartamento().setSqDepartamento(1L);
			if(this.inicializarRegioes("FIN") == 1) {
				this.getRelatorioFiltro().setRegiaoItensFiltro(this.getRegiaoItens().get(0));
				this.buscarIgrejasEministros();
			}
			
			if(this.inicializarRegioes("PAG", true) == 1) {
				this.getRelatorioFiltro().setRegiaoRecebimentoItensFiltro(this.getRegiaoRecebimentoItens().get(0));
			}
			
			this.inicializarCargos();
			this.inicializarPlanoContasPorTipo("C");
		}
	}

	public void imprimir() {
		if(getRelatorioFiltro().getRegiaoRecebimentoItensFiltro() == null) {
			throw new NegocioException("Região Financeiro é de preenchimento obrigatório.");
		}

		Long sqRegiaoMinistro = 0L;
		if(getRelatorioFiltro().getRegiaoItensFiltro() != null && getRelatorioFiltro().getRegiaoItensFiltro().getSqRegiao() != -1) {
			sqRegiaoMinistro = getRelatorioFiltro().getRegiaoItensFiltro().getSqRegiao();
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
				.append("relatorioFinanceiroEntradaMinistro(")
				.append("'../../relatorioFinanceiroEntradaMinistro'")
				.append(",'")
				.append(getRelatorioFiltro().getTpRelatorio())
				.append("',")
				.append(getRelatorioFiltro().getRegiaoRecebimentoItensFiltro().getSqRegiao())
				.append(",'")
				.append(getRelatorioFiltro().getRegiaoRecebimentoItensFiltro() != null? getRelatorioFiltro().getRegiaoRecebimentoItensFiltro().getDsRegiao() : null)
				.append("',")
				.append(sqRegiaoMinistro)
				.append(",'")
				.append(sqRegiaoMinistro != 0? getRelatorioFiltro().getRegiaoItensFiltro().getDsRegiao(): "Todas    ")
				.append("',")
				.append(getRelatorioFiltro().getDepartamento() != null? getRelatorioFiltro().getDepartamento().getSqDepartamento(): null)
				.append(",'")
				.append(getRelatorioFiltro().getDepartamento() != null? getRelatorioFiltro().getDepartamento().getDsReduzido(): null)
				.append("',")
				.append(getRelatorioFiltro().getIgreja() != null? getRelatorioFiltro().getIgreja().getSqIgreja(): null)
				.append(",'")
				.append(getRelatorioFiltro().getIgreja() != null? getRelatorioFiltro().getIgreja().getDsIgreja(): null)
				.append("',")
				.append(getRelatorioFiltro().getCargo() != null? getRelatorioFiltro().getCargo().getSqCargo(): null)
				.append(",'")
				.append(getRelatorioFiltro().getCargo() != null? getRelatorioFiltro().getCargo().getDsCargo(): null)
				.append("',")
				.append(getRelatorioFiltro().getMinistro() != null? getRelatorioFiltro().getMinistro().getSqMinistro(): null)
				.append(",'")
				.append(getRelatorioFiltro().getMinistro() != null? getRelatorioFiltro().getMinistro().getNmNome(): null)
				.append("',")
				.append(getRelatorioFiltro().getPlanoConta() != null? getRelatorioFiltro().getPlanoConta().getSqPlanoConta(): null)
				.append(",'")
				.append(getRelatorioFiltro().getPlanoConta() != null? getRelatorioFiltro().getPlanoConta().getDsConta(): null)
				.append("','")
				.append(dsSqTipoLancamentosSelecionados != null? dsSqTipoLancamentosSelecionados: null)
				.append("','")
				.append(Uteis.LocalDateParaString(getRelatorioFiltro().getDtInicio()))
				.append("','")
				.append(Uteis.LocalDateParaString(getRelatorioFiltro().getDtFim()))
				.append("'")
				.append(")");

		// Chama função javaScript para exibir relatório
		// FacesUtil.getRequestContext().execute(jsFunction.toString());
		FacesUtil.getRequestContext().executeScript(jsFunction.toString());
		
		this.getRelatorioFiltro().setFlBuscarAjax(true);
	}

	public void buscarIgrejasEministros() {
		getRelatorioFiltro().setFlBuscarAjax(true);
		this.buscarIgrejas();
		this.buscarMinistros();
	}

	public void buscarIgrejas() {
		if(getRelatorioFiltro().getRegiaoItensFiltro() != null)
			this.inicializarIgrejas(getRelatorioFiltro().getRegiaoItensFiltro().getSqRegiao(), true);
		else
			this.setIgrejas(null);
	}

	public void buscarMinistros() {
		if(getRelatorioFiltro().getRegiaoItensFiltro() != null) {
			// Se não foi selecionado a Região, atribuir todas (CONFRATERES)
			if(getRelatorioFiltro().getRegiaoItensFiltro().getSqRegiao() == null) getRelatorioFiltro().getRegiaoItensFiltro().setSqRegiao(-1L);;
			this.ministroFiltro.setRegiaoItensFiltro(getRelatorioFiltro().getRegiaoItensFiltro());
			this.ministroFiltro.setIgrejaFiltro(getRelatorioFiltro().getIgreja());
			this.ministroFiltro.setDepartamentoFiltro(getRelatorioFiltro().getDepartamento());
			this.ministroFiltro.setCargoFiltro(getRelatorioFiltro().getCargo());
			// CdSituacaoFiltro = 3 --> // Delimitar pela situação do Ministro: Ativo = 1, Inativo = 2, Todos = 3
			this.ministroFiltro.setCdSituacaoFiltro(3);

			this.ministros = ministroBO.findByParametros(ministroFiltro);
		} else {
			this.ministros = null;
		}
		this.getRelatorioFiltro().setFlBuscarAjax(true);
	}

	public List<Ministro> getMinistros() {
		return ministros;
	}

	public void setMinistros(List<Ministro> ministros) {
		this.ministros = ministros;
	}

	public MinistroFiltro getMinistroFiltro() {
		return ministroFiltro;
	}

	public void setMinistroFiltro(MinistroFiltro ministroFiltro) {
		this.ministroFiltro = ministroFiltro;
	}
	
}
