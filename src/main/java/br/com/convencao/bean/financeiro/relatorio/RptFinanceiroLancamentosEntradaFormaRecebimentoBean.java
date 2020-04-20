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

@Named(value = "rptFinanceiroLancamentosEntradaFormaRecebimentoBean")
@ViewScoped
public class RptFinanceiroLancamentosEntradaFormaRecebimentoBean extends Codebehind {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private MinistroFiltro ministroFiltro;
	
	@Inject
	private MinistroBO ministroBO;
	
	private List<Ministro> ministros;

	public void inicializar() {
		if(!getRelatorioFiltro().isFlBuscarAjax()) {
			if(this.inicializarRegioes("FIN") == 1) {
				this.getRelatorioFiltro().setRegiaoItensFiltro(this.getRegiaoItens().get(0));
			}
			
			if(this.inicializarRegioes("PAG", true) == 1) {
				this.getRelatorioFiltro().setRegiaoRecebimentoItensFiltro(this.getRegiaoRecebimentoItens().get(0));
			}
			
			this.inicializarDepartamentos("pesquisa");
			this.getRelatorioFiltro().getDepartamento().setSqDepartamento(1L);
			this.inicializarCargos();
		}
	}
	
	public void buscarIgrejasEministros() {
		getRelatorioFiltro().setFlBuscarAjax(true);
		this.buscarIgrejas();
		this.buscarMinistros();
	}
	
	public void buscarIgrejas(){		
		this.inicializarIgrejas(this.getRelatorioFiltro().getRegiaoItensFiltro() != null? this.getRelatorioFiltro().getRegiaoItensFiltro().getSqRegiao(): null  , true);
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

	public void imprimir() {
		if(getRelatorioFiltro().getRegiaoRecebimentoItensFiltro() == null || getRelatorioFiltro().getRegiaoRecebimentoItensFiltro().getSqRegiao() == null) {
			throw new NegocioException("Região de Pagamento: preenchimento obrigatório!");
		}
		
		Long sqRegiaoMinistro = 0L;
		if(getRelatorioFiltro().getRegiaoItensFiltro() != null && getRelatorioFiltro().getRegiaoItensFiltro().getSqRegiao() != -1) {
			sqRegiaoMinistro = getRelatorioFiltro().getRegiaoItensFiltro().getSqRegiao();
		}
			
		// Imprimir relatorio
		StringBuilder jsFunction = new StringBuilder()
				.append("relatorioFinanceiroEntradaFormaRecebimento(")
				.append("'../../relatorioFinanceiroEntradaFormaRecebimento'")
				.append(",'")
				.append(getRelatorioFiltro().getTpRelatorio())
				.append("',")
				.append(getRelatorioFiltro().getDepartamento() != null?  getRelatorioFiltro().getDepartamento().getSqDepartamento(): 0L)
				.append(",'")
				.append(getRelatorioFiltro().getDepartamento() != null? getRelatorioFiltro().getDepartamento().getDsReduzido(): null)
				.append("',")
				.append(getRelatorioFiltro().getRegiaoRecebimentoItensFiltro().getSqRegiao())
				.append(",'")
				.append(getRelatorioFiltro().getRegiaoRecebimentoItensFiltro().getDsRegiao())
				.append("',")
				.append(sqRegiaoMinistro)
				.append(",'")
				.append(sqRegiaoMinistro != 0? getRelatorioFiltro().getRegiaoItensFiltro().getDsRegiao(): "Todas    ")
				.append("',")
				.append(getRelatorioFiltro().getIgreja() != null? getRelatorioFiltro().getIgreja().getSqIgreja(): 0)
				.append(",'")
				.append(getRelatorioFiltro().getIgreja() != null? getRelatorioFiltro().getIgreja().getDsIgreja(): "Todas     ")
				.append("',")
				.append(getRelatorioFiltro().getCargo()!= null? getRelatorioFiltro().getCargo().getSqCargo(): 0)
				.append(",'")
				.append(getRelatorioFiltro().getCargo()!= null? getRelatorioFiltro().getCargo().getDsCargo(): "Todos     ")
				.append("',")
				.append(getRelatorioFiltro().getMinistro() != null? getRelatorioFiltro().getMinistro().getSqMinistro(): 0)
				.append(",'")
				.append(getRelatorioFiltro().getMinistro() != null? getRelatorioFiltro().getMinistro().getNmNome(): "Todos")
				.append("','")
				.append(getRelatorioFiltro().getDtInicio())
				.append("','")
				.append(getRelatorioFiltro().getDtFim())
				.append("',")
				.append(getRelatorioFiltro().getCdOrdem())
				.append(")");

		// Chama função javaScript para exibir relatório
		//FacesUtil.getRequestContext().execute(jsFunction.toString());		
		FacesUtil.getRequestContext().executeScript(jsFunction.toString());
		
		this.getRelatorioFiltro().setFlBuscarAjax(true);
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
