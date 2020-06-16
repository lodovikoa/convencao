package br.com.convencao.bean.cadastro;

import java.time.LocalDate;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang3.StringUtils;

import br.com.convencao.bo.MinistroBO;
import br.com.convencao.bo.MinistroParecerBO;
import br.com.convencao.bo.NegocioException;
import br.com.convencao.model.Cargo;
import br.com.convencao.model.Departamento;
import br.com.convencao.model.Igreja;
import br.com.convencao.model.Ministro;
import br.com.convencao.model.MinistroParecer;
import br.com.convencao.util.Uteis;
import br.com.convencao.util.jsf.FacesUtil;
import br.com.convencao.util.permissao.Permissoes;

@ViewScoped
@Named(value = "ministroPesquisaBean")
public class MinistroPesquisaBean extends MinistroCodbehind {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private MinistroBO bo;
		

	@Inject
	private Ministro selecionado;
	
	@Inject
	private MinistroParecerBO ministroParecerBO;
	
	private List<MinistroListarTO> ministroListarTO;
	
	private boolean exibirBotaoCadastrarMinistro;
	

	public void inicializar(String tipo){
		this.exibirBotaoCadastrarMinistro = Permissoes.getPermissaoInserirEditarMinistro();
		
		if(!this.getFlBuscarAjax()){
			if( this.inicializarRegioes("SEC") == 1 ) {
				this.getFiltro().setRegiaoItensFiltro(this.getRegiaoItens().get(0));
			}
			this.inicializarDepartamentos(tipo);
			this.inicializarCargos();
			
			this.inicializarIgrejas(this.getFiltro().getRegiaoItensFiltro() != null? this.getFiltro().getRegiaoItensFiltro().getSqRegiao(): null, true);
		}
	}
	

	// Buscar ministros conforme filtro informado
	private void listarMinistros(){
		
		// Validar preechimento da combo região
		if(!this.validarPreenchimentoComboRegiao()) {
			throw new NegocioException("Região: preenchimento obrigatório!");
		}
		
		List<Ministro> lista = bo.findByParametros(this.getFiltro());
		this.ministroListarTO = this.getMinistroListarTOList(lista);
	}
	
	public void pesquisar(){
		this.listarMinistros();

	}
	
	public void inicializarMinistro(Long sqMinistro) {
		this.selecionado = bo.find(sqMinistro);
	}
	
	public void inicializarParecerMinistro(Long sqMinistro) {
		
		this.inicializarMinistro(sqMinistro);
		
		// Ordenar exibição de pareceres
		this.selecionado.getMinistroParecer().sort((a, b) -> b.getDtRegistro().compareTo(a.getDtRegistro()));
		
		// Inicializar Parecer
		this.setMinistroParecer(new MinistroParecer());
		
	}
	
	public void excluir(){
		bo.remover(selecionado);
		
		// Exibir mensagem
		FacesUtil.addInfoMessage("Ministro " + selecionado.getNmNome() + " excluido com sucesso!");
	}
	
	public void salvarDesativacaoMinistro() {
		
		// Para desativar um ministro basta colocar data de excluido.
		this.selecionado = bo.desativarMinistro(this.selecionado);
		
		//Exibir mensagem
		FacesUtil.addInfoMessage("Desativação do ministro " + this.selecionado.getNmNome() + " efetuada com sucesso");
		
		// Fechar a tela de Dialog
		this.fecharDialogoPrimeFaces(true);

	}
	
	public void salvarReativacaoMinistro() {
		
		// Para reativar ministro basta setar null na data de excluido.
		this.selecionado = bo.reativarMinistro(this.selecionado);
		
		//Exibir mensagem
		FacesUtil.addInfoMessage("Reativação do ministro " + this.selecionado.getNmNome() + " efetuada com sucesso");
		
		// Fechar a tela de Dialog
		this.fecharDialogoPrimeFaces(true);
		
	}
	
	
	public void salvarHistoricoMinistro() {
		
		// Gravar historico do ministro
		this.selecionado = bo.registrarHistoricoMinistro(this.selecionado);
		
		//Exibir mensagem
		FacesUtil.addInfoMessage("Histórico do ministro " + this.selecionado.getNmNome() + " gravado com sucesso");
		
		// Fechar a tela de Dialog
		this.fecharDialogoPrimeFaces(true);
		
	}

	public List<MinistroListarTO> getMinistroListarTO() {
		return ministroListarTO;
	}
	
	public String getMensagemRodape(){
		return "Total regisgro encontrado: " + this.ministroListarTO;
	}
	
	
	public Ministro getSelecionado() {
		return selecionado;
	}
	
	public void setSelecionado(Ministro selecionado) {
		this.selecionado = selecionado;
	}
	
	public void recuperarParametros(){
		if(StringUtils.isNotBlank(this.getParam_controle()) && this.getParam_controle().equals("1")){
			this.setParam_controle(null);
			RegiaoItens regiaoItem = new RegiaoItens();
			Departamento depto =  new Departamento();
			Igreja igr = new Igreja();
			Cargo cgr = new Cargo();
			Long cdCodigo = null;
			LocalDate dtInicio = null;
			LocalDate dtFim = null;
			
			
			if(StringUtils.isNotBlank(this.getParam_regiaoItensFiltro_sqRegiao()) && !this.getParam_regiaoItensFiltro_sqRegiao().equalsIgnoreCase("null") ){
				regiaoItem.setSqRegiao(Long.parseLong(this.getParam_regiaoItensFiltro_sqRegiao()));
			}
			
			if(StringUtils.isNotBlank(this.getParam_departamentoFiltro_sqDepartamento()) && !this.getParam_departamentoFiltro_sqDepartamento().equalsIgnoreCase("null")){
				//depto = new Departamento();
				depto.setSqDepartamento(Long.parseLong(this.getParam_departamentoFiltro_sqDepartamento()));
			}
			
			if(StringUtils.isNotBlank(this.getParam_igrejaFiltro_sqIgreja()) && !this.getParam_igrejaFiltro_sqIgreja().equalsIgnoreCase("null") ){
				//igr = new Igreja();
				igr.setSqIgreja(Long.parseLong(this.getParam_igrejaFiltro_sqIgreja()));
			}
			
			if(StringUtils.isNotBlank(this.getParam_cargoFiltro_sqCargo()) && !this.getParam_cargoFiltro_sqCargo().equalsIgnoreCase("null") ){
				//cgr = new Cargo();
				cgr.setSqCargo(Long.parseLong(this.getParam_cargoFiltro_sqCargo()));
			}
			
			if(StringUtils.isNotBlank(this.getParam_cdCodigoFiltro()) && !this.getParam_cdCodigoFiltro().equalsIgnoreCase("null")){
				cdCodigo = Long.parseLong(this.getParam_cdCodigoFiltro());
			}
			
			if(StringUtils.isNotBlank(this.getParam_dtIngressoInicioFiltro()) && !this.getParam_dtIngressoInicioFiltro().equalsIgnoreCase("null")){
				dtInicio = Uteis.StringParaLocalDate(this.getParam_dtIngressoInicioFiltro());
			}
			
			if(StringUtils.isNotBlank(this.getParam_dtIngressoFimFiltro()) && !this.getParam_dtIngressoFimFiltro().equalsIgnoreCase("null")){
				dtFim = Uteis.StringParaLocalDate(this.getParam_dtIngressoFimFiltro());
			}
			
			
			this.getFiltro().setRegiaoItensFiltro(regiaoItem);
			this.getFiltro().setDepartamentoFiltro(depto);
			this.getFiltro().setIgrejaFiltro(igr);
			this.getFiltro().setCargoFiltro(cgr);
			this.getFiltro().setCdCodigoFiltro(cdCodigo);
			this.getFiltro().setNmMinistro(this.getParam_nmMinistro());
			this.getFiltro().setDsCpf(this.getParam_dsCpf());
			this.getFiltro().setCdSituacaoFiltro(StringUtils.isNotBlank(this.getParam_cdSituacaoFiltro())? Integer.parseInt(this.getParam_cdSituacaoFiltro()): 0);
			this.getFiltro().setFlJubiladoFiltro(Boolean.parseBoolean(this.getParam_flJubiladoFiltro()));
			this.getFiltro().setDsCidadeFiltro(this.getParam_dsCidadeFiltro());
			this.getFiltro().setDtInicio1Filtro(dtInicio);
			this.getFiltro().setDtFim1Filtro(dtFim);
			
			this.inicializarIgrejas(this.getFiltro().getRegiaoItensFiltro().getSqRegiao(), true);

			this.listarMinistros();

		}
	}


	public boolean isExibirBotaoCadastrarMinistro() {
		return exibirBotaoCadastrarMinistro;
	}


	public void setExibirBotaoCadastrarMinistro(boolean exibirBotaoCadastrarMinistro) {
		this.exibirBotaoCadastrarMinistro = exibirBotaoCadastrarMinistro;
	}
	
	public void salvarParecerMinistro() {
		//Trocar <Enter> por <br /> para quebrar linha na exibição na tela em html
		this.getMinistroParecer().setDsParecer(Uteis.inserirQuebraLinhaHtml(this.getMinistroParecer().getDsParecerTemp()));

		// Adicionar o protocolo no parecer
		this.getMinistroParecer().setMinistro(this.selecionado);

		// Salvar parecer
		ministroParecerBO.salvarParecer(this.getMinistroParecer());

		//Exibir mensagem
		FacesUtil.addInfoMessage("Parecer incluido para ministro (" + this.selecionado.getNmNome() + ") com sucesso");

		// Fechar a tela de Dialog
		this.fecharDialogoPrimeFaces(true);
		
		// Atualizar ministro para exibir novo parecer na tela
		this.selecionado = bo.find(this.selecionado.getSqMinistro());
		
		// Ordenar exibição de pareceres
		this.selecionado.getMinistroParecer().sort((a, b) -> b.getDtRegistro().compareTo(a.getDtRegistro()));
	}
}
