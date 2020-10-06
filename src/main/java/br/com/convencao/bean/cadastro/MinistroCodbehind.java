package br.com.convencao.bean.cadastro;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.primefaces.model.StreamedContent;

import br.com.convencao.bean.financeiro.LancamentoCodbehind;
import br.com.convencao.bo.AnexosBO;
import br.com.convencao.bo.DepartamentoBO;
import br.com.convencao.bo.MinistroBO;
import br.com.convencao.bo.NegocioException;
import br.com.convencao.model.Igreja;
import br.com.convencao.model.Ministro;
import br.com.convencao.model.MinistroAnexo;
import br.com.convencao.model.MinistroParecer;
import br.com.convencao.util.jsf.FacesUtil;


public class MinistroCodbehind extends LancamentoCodbehind {

	private static final long serialVersionUID = 1L;

	private String param_regiaoItensFiltro_sqRegiao;
	private String param_igrejaFiltro_sqIgreja;
	private String param_protocoloStatus_sqProtocoloStatus;
	private String param_cdCodigoFiltro;
	private String param_departamentoFiltro_sqDepartamento;
	private String param_cargoFiltro_sqCargo;
	private String param_dsCidadeFiltro;	
	private String param_dtIngressoInicioFiltro;
	private String param_dtIngressoFimFiltro;
	private String param_cdSituacaoFiltro;
	private String param_flJubiladoFiltro;
	private String param_cdProtocolo;
	private String param_cadastro;

	private MinistroParecer ministroParecer;
	private Ministro ministroConjuge;
	

	private Boolean flBuscarAjax = false;
	private Boolean flCadastroConjugeSalvo = false;

	private StreamedContent streamedContent;

	//	@Inject
	//	private S3Config s3Config;
	
	@Inject
	private AnexosBO anexosBO;
	
	@Inject
	private DepartamentoBO departamentoBO;
	
	@Inject
	private MinistroBO ministroBO;

	public String getParam_regiaoItensFiltro_sqRegiao() {
		return param_regiaoItensFiltro_sqRegiao;
	}

	public void setParam_regiaoItensFiltro_sqRegiao(String param_regiaoItensFiltro_sqRegiao) {
		this.param_regiaoItensFiltro_sqRegiao = param_regiaoItensFiltro_sqRegiao;
	}

	public String getParam_igrejaFiltro_sqIgreja() {
		return param_igrejaFiltro_sqIgreja;
	}

	public void setParam_igrejaFiltro_sqIgreja(String param_igrejaFiltro_sqIgreja) {
		this.param_igrejaFiltro_sqIgreja = param_igrejaFiltro_sqIgreja;
	}

	public String getParam_protocoloStatus_sqProtocoloStatus() {
		return param_protocoloStatus_sqProtocoloStatus;
	}

	public void setParam_protocoloStatus_sqProtocoloStatus(String param_protocoloStatus_sqProtocoloStatus) {
		this.param_protocoloStatus_sqProtocoloStatus = param_protocoloStatus_sqProtocoloStatus;
	}

	public String getParam_cdCodigoFiltro() {
		return param_cdCodigoFiltro;
	}

	public void setParam_cdCodigoFiltro(String param_cdCodigoFiltro) {
		this.param_cdCodigoFiltro = param_cdCodigoFiltro;
	}


	public String getParam_departamentoFiltro_sqDepartamento() {
		return param_departamentoFiltro_sqDepartamento;
	}

	public void setParam_departamentoFiltro_sqDepartamento(String param_departamentoFiltro_sqDepartamento) {
		this.param_departamentoFiltro_sqDepartamento = param_departamentoFiltro_sqDepartamento;
	}

	public String getParam_cargoFiltro_sqCargo() {
		return param_cargoFiltro_sqCargo;
	}

	public void setParam_cargoFiltro_sqCargo(String param_cargoFiltro_sqCargo) {
		this.param_cargoFiltro_sqCargo = param_cargoFiltro_sqCargo;
	}

	public String getParam_dsCidadeFiltro() {
		return param_dsCidadeFiltro;
	}

	public void setParam_dsCidadeFiltro(String param_dsCidadeFiltro) {
		this.param_dsCidadeFiltro = param_dsCidadeFiltro;
	}

	public String getParam_dtIngressoInicioFiltro() {
		return param_dtIngressoInicioFiltro;
	}

	public void setParam_dtIngressoInicioFiltro(String param_dtIngressoInicioFiltro) {
		this.param_dtIngressoInicioFiltro = param_dtIngressoInicioFiltro;
	}

	public String getParam_dtIngressoFimFiltro() {
		return param_dtIngressoFimFiltro;
	}

	public void setParam_dtIngressoFimFiltro(String param_dtIngressoFimFiltro) {
		this.param_dtIngressoFimFiltro = param_dtIngressoFimFiltro;
	}

	public String getParam_cdSituacaoFiltro() {
		return param_cdSituacaoFiltro;
	}

	public void setParam_cdSituacaoFiltro(String param_cdSituacaoFiltro) {
		this.param_cdSituacaoFiltro = param_cdSituacaoFiltro;
	}

	public String getParam_flJubiladoFiltro() {
		return param_flJubiladoFiltro;
	}

	public void setParam_flJubiladoFiltro(String param_flJubiladoFiltro) {
		this.param_flJubiladoFiltro = param_flJubiladoFiltro;
	}

	public Boolean getFlBuscarAjax() {
		return flBuscarAjax;
	}

	public void setFlBuscarAjax(Boolean flBuscarAjax) {
		this.flBuscarAjax = flBuscarAjax;
	}

	public String getParam_cdProtocolo() {
		return param_cdProtocolo;
	}

	public void setParam_cdProtocolo(String param_cdProtocolo) {
		this.param_cdProtocolo = param_cdProtocolo;
	}

	public String getParam_cadastro() {
		return param_cadastro;
	}

	public void setParam_cadastro(String param_cadastro) {
		this.param_cadastro = param_cadastro;
	}

	public void buscarIgrejasAjax(){
		this.flBuscarAjax = true;			
		this.inicializarIgrejas(this.getFiltro().getRegiaoItensFiltro() != null? this.getFiltro().getRegiaoItensFiltro().getSqRegiao(): null  , true);
	}

	// Fazer download de arquivos
	public void downloadArquivo(MinistroAnexo anexo) {
		streamedContent =  anexosBO.download(anexo);		
	} 

	public StreamedContent getStreamedContent() {
		return streamedContent;
	}

	public MinistroParecer getMinistroParecer() {
		return ministroParecer;
	}

	public void setMinistroParecer(MinistroParecer ministroParecer) {
		this.ministroParecer = ministroParecer;
	}

	public void limparMinistroParecer() {
		this.ministroParecer = new MinistroParecer();
	}
	
	public Boolean getFlCadastroConjugeSalvo() {
		return flCadastroConjugeSalvo;
	}

	public void setFlCadastroConjugeSalvo(Boolean flCadastroConjugeSalvo) {
		this.flCadastroConjugeSalvo = flCadastroConjugeSalvo;
	}

	public Ministro getMinistroConjuge() {
		return ministroConjuge;
	}
	
	public void setMinistroConjuge(Ministro ministroConjuge) {
		this.ministroConjuge = ministroConjuge;
	}
	
	public void inicializarMinistroConjuge() {
		this.ministroConjuge = new Ministro();
		this.ministroConjuge.setIgreja(new Igreja());
	}
	
	public void salvarCadastroConjuge() {
		
		// Atribuir Departamento ao conjuge
		this.ministroConjuge.setDepartamento(departamentoBO.find(2L));
		
		// Verificar campos obrigatórios
		if(StringUtils.isBlank(this.ministroConjuge.getNmNome()) && this.ministroConjuge.getNmNome().trim().length() < 5)
			throw new NegocioException("Faltou informar o nome do conjuge.");
			
		this.ministroConjuge = ministroBO.salvar(this.ministroConjuge);
		
		FacesUtil.addInfoMessage("Conjuge salvo com sucesso!");
		
		this.flCadastroConjugeSalvo = true;
		
		this.inicializarEsposasMinistro();
		
		this.fecharDialogoPrimeFaces(true);
	}
	
	public void inicializarMinistroConjugeAvulso() {
		this.flCadastroConjugeSalvo = true;
	}
}
