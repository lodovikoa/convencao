package br.com.convencao.bean.cadastro;

import java.util.ArrayList;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.convencao.bo.IgrejaBO;
import br.com.convencao.bo.MinistroBO;
import br.com.convencao.bo.NegocioException;
import br.com.convencao.model.Ministro;
import br.com.convencao.util.jsf.FacesUtil;
import lombok.Getter;
import lombok.Setter;

@Named(value = "ministroRelatorioPorIgreja")
@ViewScoped
public class MinistroRelatorioPorIgreja extends MinistroCodbehind {

	private static final long serialVersionUID = 1L;

	@Getter @Setter private List<Ministro> presidentes;
	@Getter @Setter private Ministro presidente;

	@Getter @Setter private Ministro ministro;

	@Getter @Setter private Long sqMinistroIgreja;

	@Inject
	private IgrejaBO igrejaBO;

	@Inject
	private MinistroBO ministroBO;


	public void inicializar() {
		if(!this.getFlBuscarAjax()) {
			if(this.inicializarRegioes("SEC") == 1) {
				this.getFiltro().setRegiaoItensFiltro(this.getRegiaoItens().get(0));
			}
		}
	}

	public void atualizarDados() {
		this.buscarIgrejasAjax();
		
		// Foi selecionado uma nova região
		// Limpar seleção da Igreja
		this.getFiltro().setIgrejaFiltro(null);
		
		if(this.getFiltro().getRegiaoItensFiltro() == null) {
			this.sqMinistroIgreja = -1L;
		} else 
			this.sqMinistroIgreja = null;
		
		this.inicializarPresidentes();
	}

	public void atualizarPresidentes() {

		if(this.getFiltro().getIgrejaFiltro() == null) {
			this.sqMinistroIgreja = null;
		} else if((this.getFiltro().getIgrejaFiltro().getMinistro() == null) || (this.getFiltro().getIgrejaFiltro().getMinistro().getSqMinistro() == null))  {
			this.sqMinistroIgreja = -10L;
		} else {
			this.sqMinistroIgreja = this.getFiltro().getIgrejaFiltro().getMinistro().getSqMinistro();
		}
		
		this.inicializarPresidentes();
	}

	// Buscar todos os pastores presidentes de uma regiao, caso a regiao seja = -1 buscar todos da Confrateres
	// Caso selecione a Igreja busca o presidente da igreja
	public void inicializarPresidentes() {

		if(this.sqMinistroIgreja != null) {
			this.presidentes = new ArrayList<>();
			if(this.sqMinistroIgreja != -10) {
				this.presidente = this.ministroBO.find(this.sqMinistroIgreja);
				if(this.presidente != null)
					this.presidentes.add(presidente);
			}
		} else {
			Long sqRegiao = -1L;
			if((this.getFiltro().getRegiaoItensFiltro() != null) && (this.getFiltro().getRegiaoItensFiltro().getSqRegiao() != null)) {
				sqRegiao = this.getFiltro().getRegiaoItensFiltro().getSqRegiao();
			}
			
			this.presidentes = igrejaBO.findPresidenteByRegiao(sqRegiao);
		}
	}

	public void imprimir() {
		
		if(this.getFiltro().getRegiaoItensFiltro() == null ) {
			throw new NegocioException("Selecione pelo menos uma Região!");
		}
		
		// Montar parametros para exibir o relatório
		StringBuilder jsFunction = new StringBuilder();
		jsFunction.append("relatorioMinistroPorIgreja(");
		jsFunction.append("'../../relatorioMinistroPorIgreja'");
		jsFunction.append(",'");
		jsFunction.append(getRelatorioFiltro().getTpRelatorio());
		jsFunction.append("',");
		jsFunction.append(this.getFiltro().getRegiaoItensFiltro() != null? this.getFiltro().getRegiaoItensFiltro().getSqRegiao(): 0);
		jsFunction.append(",");
		jsFunction.append(this.getFiltro().getIgrejaFiltro() != null? this.getFiltro().getIgrejaFiltro().getSqIgreja(): 0);
		jsFunction.append(",");
		jsFunction.append(this.getMinistro() != null? this.getMinistro().getSqMinistro(): 0);
		jsFunction.append(");");
		
		// Chama função javaScript para exibir o relatório
		FacesUtil.getRequestContext().executeScript(jsFunction.toString());
		
	}

}
