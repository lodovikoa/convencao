package br.com.convencao.bean.financeiro.entrada;

import java.time.LocalDate;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang3.StringUtils;

import br.com.convencao.bean.cadastro.RegiaoItens;
import br.com.convencao.bean.financeiro.LancamentoCodbehind;
import br.com.convencao.bo.MinistroBO;
import br.com.convencao.bo.NegocioException;
import br.com.convencao.model.Cargo;
import br.com.convencao.model.Departamento;
import br.com.convencao.model.Estado;
import br.com.convencao.model.Igreja;
import br.com.convencao.model.to.MinistroRecebimentoCpl;
import br.com.convencao.util.Uteis;

@Named(value = "lancamentoEntradaPessoasList")
@ViewScoped
public class LancamentoEntradaPessoasListBean extends LancamentoCodbehind {

	private static final long serialVersionUID = 1L;
	
	
	@Inject
	private MinistroBO ministroBO;

	private List<MinistroRecebimentoCpl> lista;
	
	
	public void inicializar() {
		
		if( this.inicializarRegioes("FIN") == 1 ) {
			this.getFiltro().setRegiaoItensFiltro(this.getRegiaoItens().get(0));
		}

		this.inicializarIgrejas(this.getFiltro().getRegiaoItensFiltro() != null? this.getFiltro().getRegiaoItensFiltro().getSqRegiao(): null, true);
		this.inicializarDepartamentos("consultasInternas");
		this.inicializarCargos();
		this.inicializarEstados();
	}
	
	public void buscarIgrejasAjax(){		
		this.inicializarIgrejas(this.getFiltro().getRegiaoItensFiltro() != null? this.getFiltro().getRegiaoItensFiltro().getSqRegiao(): null  , true);
	}
	
	public void pesquisar() {
		// Validar preenchimento da Regiao
		if(!this.validarPreenchimentoComboRegiao()) {
			throw new NegocioException("Região: preenchimento obrigatório!");
		}
		
		this.listarPessoas();	
	}
	
	public void listarPessoas() {
		this.lista = ministroBO.findMinistrosForRecebimento(this.getFiltro());
	}

	public List<MinistroRecebimentoCpl> getLista() {
		return lista;
	}
	
	public void setLista(List<MinistroRecebimentoCpl> lista) {
		this.lista = lista;
	}

	public String getMensagemRodape(){
		return (this.lista != null? "Total registros encontrado: " + this.lista.size(): "");
	}
	
	public void recuperarParametros() {
		if(StringUtils.isNotBlank(this.getParam_controle()) && this.getParam_controle().equals("1")) {
			this.setParam_controle(null);
			
			RegiaoItens regiaoItem = new RegiaoItens();
			Departamento depto =  new Departamento();
			Igreja igr = new Igreja();
			Cargo cgr = new Cargo();
			Estado est = new Estado();
			Long cdRg = null;
			Long cdNsu = null;	
			LocalDate dtInicio1 = null;
			LocalDate dtFim1 = null;
			LocalDate dtInicio2 = null;
			LocalDate dtFim2 = null;
			
			if(StringUtils.isNotBlank(this.getParam_regiaoItens_sqRegiao()) && !this.getParam_regiaoItens_sqRegiao().equalsIgnoreCase("null") ){
				regiaoItem.setSqRegiao(Long.parseLong(this.getParam_regiaoItens_sqRegiao()));
			}
			
			if(StringUtils.isNotBlank(this.getParam_departamentoItensFiltro_sqDepartamento()) && !this.getParam_departamentoItensFiltro_sqDepartamento().equalsIgnoreCase("null")){
				//depto = new Departamento();
				depto.setSqDepartamento(Long.parseLong(this.getParam_departamentoItensFiltro_sqDepartamento()));
			}
			
			if(StringUtils.isNotBlank(this.getParam_igrejaItensFiltro_sqIgreja()) && !this.getParam_igrejaItensFiltro_sqIgreja().equalsIgnoreCase("null") ){
				//igr = new Igreja();
				igr.setSqIgreja(Long.parseLong(this.getParam_igrejaItensFiltro_sqIgreja()));
			}
			
			if(StringUtils.isNotBlank(this.getParam_cargo_sqCargo()) && !this.getParam_cargo_sqCargo().equalsIgnoreCase("null") ){
				//cgr = new Cargo();
				cgr.setSqCargo(Long.parseLong(this.getParam_cargo_sqCargo()));
			}
			
			if(StringUtils.isNotBlank(this.getParam_estado_sqEstado()) && !this.getParam_estado_sqEstado().equalsIgnoreCase("null") ){
				//cgr = new Cargo();
				est.setSqEstado(Long.parseLong(this.getParam_estado_sqEstado()));
			}
			
			if(StringUtils.isNotBlank(this.getParam_cdRg()) && !this.getParam_cdRg().equalsIgnoreCase("null")){
				cdRg = Long.parseLong(this.getParam_cdRg());
			}
			
			
			if(StringUtils.isNotBlank(this.getParam_cdNsu()) && !this.getParam_cdNsu().equalsIgnoreCase("null")){
				cdNsu = Long.parseLong(this.getParam_cdNsu());
			}
			
			if(StringUtils.isNotBlank(this.getParam_dtInicio1()) && !this.getParam_dtInicio1().equalsIgnoreCase("null")){
				dtInicio1 = Uteis.StringParaLocalDate(this.getParam_dtInicio1());
			}
			
			if(StringUtils.isNotBlank(this.getParam_dtFim1()) && !this.getParam_dtFim1().equalsIgnoreCase("null")){
				dtFim1 = Uteis.StringParaLocalDate(this.getParam_dtFim1());
			}
			
			if(StringUtils.isNotBlank(this.getParam_dtInicio2()) && !this.getParam_dtInicio2().equalsIgnoreCase("null")){
				dtInicio2 = Uteis.StringParaLocalDate(this.getParam_dtInicio2());
			}
			
			if(StringUtils.isNotBlank(this.getParam_dtFim2()) && !this.getParam_dtFim2().equalsIgnoreCase("null")){
				dtFim2 = Uteis.StringParaLocalDate(this.getParam_dtFim2());
			}
		
			
			this.getFiltro().setRegiaoItensFiltro(regiaoItem);
			this.getFiltro().setDepartamentoFiltro(depto);
			this.getFiltro().setIgrejaFiltro(igr);
			this.getFiltro().setCargoFiltro(cgr);
			this.getFiltro().setEstadoFiltro(est);
			this.getFiltro().setCdCodigoFiltro(cdRg);
			this.getFiltro().setCdReciboFiltro(cdNsu);
			this.getFiltro().setNmMinistro("null".equalsIgnoreCase(this.getParam_nmMinistro())? "":this.getParam_nmMinistro());
			this.getFiltro().setCdSituacaoFiltro(StringUtils.isNotBlank(this.getParam_cdSituacao())? Integer.parseInt(this.getParam_cdSituacao()): 0);
			this.getFiltro().setFlJubiladoFiltro(Boolean.parseBoolean(this.getParam_flJubilado()));
			this.getFiltro().setDsCidadeFiltro("null".equalsIgnoreCase(this.getParam_dsCidade())? "": this.getParam_dsCidade() );
			this.getFiltro().setDtInicio1Filtro(dtInicio1);
			this.getFiltro().setDtFim1Filtro(dtFim1);
			this.getFiltro().setDtInicio2Filtro(dtInicio2);
			this.getFiltro().setDtFim2Filtro(dtFim2);
			
			this.inicializarIgrejas(this.getFiltro().getRegiaoItensFiltro().getSqRegiao(), true);
		
			this.listarPessoas();
		}
	}
}