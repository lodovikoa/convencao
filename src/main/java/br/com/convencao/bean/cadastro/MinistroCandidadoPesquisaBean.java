package br.com.convencao.bean.cadastro;

import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang3.StringUtils;

import br.com.convencao.bo.MinistroCandidatoBO;
import br.com.convencao.bo.NegocioException;
import br.com.convencao.bo.ProtocoloStatusBO;
import br.com.convencao.model.Igreja;
import br.com.convencao.model.ProtocoloStatus;
import br.com.convencao.model.to.MinistroCandidatoListTO;
import br.com.convencao.util.jsf.FacesUtil;
import br.com.convencao.util.permissao.Permissoes;

@ViewScoped
@Named(value = "ministroCandidatoPesquisaBeam")
public class MinistroCandidadoPesquisaBean extends MinistroCodbehind {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private MinistroCandidatoBO candidatoBO;
	
	@Inject
	private ProtocoloStatusBO protocoloStatusBO;
	
	private MinistroCandidatoListTO selecionado;
	
	private List<ProtocoloStatus> protocolosStatus;
	
	private List<MinistroCandidatoListTO> ministroCandidatoListTO;
	
	private boolean flExibirBotaoEditarPrincipal;
	private boolean flExibirBotaoExcluirPrincipal;
	
	private boolean flExibirBotaoNovo;
	
	private boolean flPrimeiraExibicaoProtocoloStatus = true;
	
	public void inicializar(String tipo) {
		if(this.inicializarRegioes("SEC") == 1) {
			this.getFiltro().setRegiaoItensFiltro(this.getRegiaoItens().get(0));
		}
		
		this.inicializarIgrejas(this.getFiltro().getRegiaoItensFiltro() != null? this.getFiltro().getRegiaoItensFiltro().getSqRegiao(): null);
		
		// Busca os status do protocolo e para a primeira exibição atribui o valor de Cadastrando
		this.protocolosStatus = this.protocoloStatusBO.findAllAtivos();
		if(this.flPrimeiraExibicaoProtocoloStatus && StringUtils.isEmpty(this.getParam_controle())) {
			 this.getFiltro().setProtocoloStatusFiltro(this.protocolosStatus.get(0));
			 this.flPrimeiraExibicaoProtocoloStatus = false;
		}
		
		// Verificar se usuário logado tem permissão para alterar e excluir.
		this.flExibirBotaoEditarPrincipal = Permissoes.getPermissaoEditarProtocolo();
		this.flExibirBotaoExcluirPrincipal = Permissoes.getPermissaoExcluirProtocolo();
		this.flExibirBotaoNovo = Permissoes.getPermissaoInserirProtocolo();
	}
	
	// Buscar ministros Candidatos conforme filtros informado
	private void listarMinistrosCandidato() {
		//Validar preenchimento de Regiao
		if(!this.validarPreenchimentoComboRegiao()) {
			throw new NegocioException("Região: preenchimento obrigatório");
		}
		
		this.ministroCandidatoListTO = candidatoBO.findByParametros(this.getFiltro());
		
		for (int i = 0; i < this.ministroCandidatoListTO.size(); i++) {
			if(this.ministroCandidatoListTO.get(i).getSqProtocoloStatus() == 7 || this.ministroCandidatoListTO.get(i).getSqProtocoloStatus() == 8) {
				this.ministroCandidatoListTO.get(i).setFlExibirBotaoEditar(false);
				if(this.ministroCandidatoListTO.get(i).getSqProtocoloStatus() == 7) {
					this.ministroCandidatoListTO.get(i).setFlExibirBotaoExcluir(false);
				} else {
					if(this.flExibirBotaoExcluirPrincipal) this.ministroCandidatoListTO.get(i).setFlExibirBotaoExcluir(true);
				}
			} else {
				if(this.flExibirBotaoEditarPrincipal) this.ministroCandidatoListTO.get(i).setFlExibirBotaoEditar(true);
				if(this.flExibirBotaoExcluirPrincipal) this.ministroCandidatoListTO.get(i).setFlExibirBotaoExcluir(true);
			}
			
		}
		
		if(this.ministroCandidatoListTO.size() == 0) {
			FacesUtil.addInfoMessage("Nenhum Candidato encontrado com os filtros informados.");
		}
	}
	

	public void excluir() {
		
		// Confirmar que usuário logado tem permissão para excluir
		if(!flExibirBotaoExcluirPrincipal) 
			throw new NegocioException("Você não tem permissão para excluir candidatos!");
		
		candidatoBO.remover(selecionado);
		
		// Atualizar tela
		this.listarMinistrosCandidato();
		
		// Exibir mensagem
		FacesUtil.addInfoMessage("Protocolo " + selecionado.getCdProtocolo() + " e candidato " + selecionado.getNmCandidato() + " excluídos com sucesso!");
	}
	
	public void pesquisar() {
		this.listarMinistrosCandidato();
	}
	
	
	public void buscarIgrejasAjax(){
		//this.flBuscarAjax = true;			
		this.inicializarIgrejas(this.getFiltro().getRegiaoItensFiltro() != null? this.getFiltro().getRegiaoItensFiltro().getSqRegiao(): null);
	}
	
	public List<ProtocoloStatus> getProtocolosStatus() {
		return protocolosStatus;
	}
	
	public void setProtocolosStatus(List<ProtocoloStatus> protocolosStatus) {
		this.protocolosStatus = protocolosStatus;
	}
	
	public List<MinistroCandidatoListTO> getMinistroCandidatoListTO() {
		return ministroCandidatoListTO;
	}
	
	public void setMinistroCandidatoListTO(List<MinistroCandidatoListTO> ministroCandidatoListTO) {
		this.ministroCandidatoListTO = ministroCandidatoListTO;
	}
	
	public MinistroCandidatoListTO getSelecionado() {
		return selecionado;
	}
	
	public void setSelecionado(MinistroCandidatoListTO selecionado) {
		this.selecionado = selecionado;
	}
	
	
	public boolean isFlExibirBotaoEditarPrincipal() {
		return flExibirBotaoEditarPrincipal;
	}

	public boolean isFlExibirBotaoExcluirPrincipal() {
		return flExibirBotaoExcluirPrincipal;
	}
	
	
	public boolean isFlExibirBotaoNovo() {
		return flExibirBotaoNovo;
	}

	public void setFlExibirBotaoNovo(boolean flExibirBotaoNovo) {
		this.flExibirBotaoNovo = flExibirBotaoNovo;
	}

	public void recuperarParametros(){
		if(StringUtils.isNotBlank(this.getParam_controle()) && this.getParam_controle().equals("1")){
			this.flPrimeiraExibicaoProtocoloStatus = false;
			
			this.setParam_controle(null);
			RegiaoItens regiaoItem = new RegiaoItens();
			Igreja igr = new Igreja();
			Long cdCodigo = null;
			Long cdProtocolo = null;
			ProtocoloStatus prtSt = new ProtocoloStatus();
			
			
			if(StringUtils.isNotBlank(this.getParam_regiaoItensFiltro_sqRegiao()) && !this.getParam_regiaoItensFiltro_sqRegiao().equalsIgnoreCase("null") ){
				regiaoItem.setSqRegiao(Long.parseLong(this.getParam_regiaoItensFiltro_sqRegiao()));
			}
			
			if(StringUtils.isNotBlank(this.getParam_igrejaFiltro_sqIgreja()) && !this.getParam_igrejaFiltro_sqIgreja().equalsIgnoreCase("null") ){
				igr.setSqIgreja(Long.parseLong(this.getParam_igrejaFiltro_sqIgreja()));
			}
			
			if(StringUtils.isNotBlank(this.getParam_protocoloStatus_sqProtocoloStatus()) && !this.getParam_protocoloStatus_sqProtocoloStatus().equalsIgnoreCase("null")) {
				prtSt.setSqProtocoloStatus(Long.parseLong(this.getParam_protocoloStatus_sqProtocoloStatus()));
			}
			
			if(StringUtils.isNotBlank(this.getParam_cdCodigoFiltro()) && !this.getParam_cdCodigoFiltro().equalsIgnoreCase("null")){
				cdCodigo = Long.parseLong(this.getParam_cdCodigoFiltro());
			}
			
			if(StringUtils.isNotBlank(this.getParam_cdProtocolo()) && !this.getParam_cdProtocolo().equalsIgnoreCase("null")) {
				cdProtocolo = Long.parseLong(this.getParam_cdProtocolo());
			}
			
			this.getFiltro().setRegiaoItensFiltro(regiaoItem);
			this.getFiltro().setIgrejaFiltro(igr);
			this.getFiltro().setProtocoloStatusFiltro(prtSt);
			
			this.getFiltro().setCdCodigoFiltro(cdCodigo);
			this.getFiltro().setNmMinistro(this.getParam_nmMinistro());
			this.getFiltro().setDsCpf(this.getParam_dsCpf());
			this.getFiltro().setCdProtocoloFiltro(cdProtocolo);
			
			this.inicializarIgrejas(this.getFiltro().getRegiaoItensFiltro().getSqRegiao());

			this.listarMinistrosCandidato();;

		}
	}
}
