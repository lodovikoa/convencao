package br.com.convencao.bean.cadastro;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang3.StringUtils;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import br.com.convencao.bo.AnexosBO;
import br.com.convencao.bo.FotoBO;
import br.com.convencao.bo.MinistroCandidatoBO;
import br.com.convencao.bo.MinistroParecerBO;
import br.com.convencao.bo.NegocioException;
import br.com.convencao.bo.ProtocoloBO;
import br.com.convencao.bo.ProtocoloStatusBO;
import br.com.convencao.model.Ministro;
import br.com.convencao.model.MinistroAnexo;
import br.com.convencao.model.Protocolo;
import br.com.convencao.model.ProtocoloStatus;
import br.com.convencao.util.Uteis;
import br.com.convencao.util.jsf.FacesUtil;
import br.com.convencao.util.permissao.Permissoes;

@Named(value = "ministroCandidatoCadastroBean")
@ViewScoped
public class MinistroCandidatoCadastroBean extends MinistroCodbehind{

	private static final long serialVersionUID = 1L;

	private boolean flCadastrando;
	private boolean flExibirCampo;
	private boolean flExibirBotaoConcluir;
	private boolean flExibirBotaoSalvar;
	private Protocolo protocolo;
	
	private MinistroAnexo ministroAnexo;

	private Long sqCodigoDepartamentoOriginal;
	private boolean flInserirEditarCodigoMinistro = true;

	private int cdProtocoloConclusao;

	@Inject
	private MinistroCandidatoBO candidatoBO;

	@Inject
	private ProtocoloStatusBO protocoloStatusBO;

	@Inject
	private MinistroParecerBO ministroParecerBO;

	@Inject
	private FotoBO fotoBO;

	@Inject
	private AnexosBO anexosBO;

	@Inject
	private ProtocoloBO protocoloBO;

	public void inicializar() {
		String tipo = this.getParam_cadastro();
		
		// Grupo de usuários que tem permsisão e acesso ao botão Concluir PROTOCOLO
		this.flExibirBotaoConcluir = Permissoes.getPermissaoConcluirProtocolo( this.protocolo == null? 0L: this.protocolo.getProtocoloStatus().getSqProtocoloStatus());

		if(this.protocolo == null) {
			this.limpar();
			this.flExibirBotaoSalvar = true;
		} else if(this.protocolo.getProtocoloStatus().getSqProtocoloStatus() == 7 || this.protocolo.getProtocoloStatus().getSqProtocoloStatus() == 8) {
			this.flExibirBotaoSalvar = false;
		} else {
			this.flExibirBotaoSalvar = true;
		}

		if(tipo.equalsIgnoreCase("cadastrarCandidato")) {
			this.flExibirCampo = false;
			if(this.protocolo.getMinistro().getSqMinistro() == null) {
				this.flCadastrando = true;
			}else {
				this.flCadastrando = false;
			}

			this.protocolo.setDtProtocolo(LocalDate.now());

		} else if(tipo.equalsIgnoreCase("alterarCandidato")) {
			this.flCadastrando = false;	
		}
		
		if(this.isEditando()) {
			// Ordenar exibição de pareceres
			protocolo.getMinistro().getMinistroParecer().sort((a, b) -> b.getDtRegistro().compareTo(a.getDtRegistro()));
		}


		// Buscar status
		List<ProtocoloStatus> protocolosStatus = protocoloStatusBO.findAllAtivos();

		// Iniciazliar departamentos
		this.inicializarDepartamentos(tipo);

		if(this.flCadastrando) {
			this.protocolo.setProtocoloStatus(protocolosStatus.get(0));
			this.protocolo.getMinistro().setDepartamento(this.getDepartamentos().get(0));
		}

		this.inicializarCargos();
		this.inicializarEstadoCiveis();
		this.inicializarEsposasMinistro();
		this.inicializarEscolaridades();
		this.inicializarProfissoes();
		this.inicializarEstados();

		// parametro: -1L buscar todas as igrejas independente de região
		// parametro: true colocar "ASSEMLBEIA DE DEUS" no final do nome da igreja Ex.: CAMPO GRANDE, ASSEMLBEIA DE DEUS
		this.inicializarIgrejas(-1L, true);


	}

	public void inicializarProtocoloConclusao() {
		this.limparMinistroParecer();
		this.cdProtocoloConclusao = 0;
	}

	public void salvar() {
		// Verificar se foi preenchido parecer na inclusão de novo protocolo
		if(this.protocolo.getSqProtocolo() == null) {
			if(StringUtils.isNotEmpty(this.getMinistroParecer().getDsParecerTemp().trim())) {
				this.getMinistroParecer().setDsParecer(Uteis.inserirQuebraLinhaHtml(this.getMinistroParecer().getDsParecerTemp().trim()));
				this.getMinistroParecer().setMinistro(protocolo.getMinistro());

				this.protocolo.getMinistro().getMinistroParecer().clear();
				this.protocolo.getMinistro().getMinistroParecer().add(this.getMinistroParecer());
			}
		}

		// se não foi selecionado nenhuma foto, será colocado a foto padrão.
		if(null == protocolo.getMinistro().getDsFoto())
			protocolo.getMinistro().setDsFoto(Uteis._FOTO_DEFAULT);

		this.protocolo = candidatoBO.salvar(protocolo);

		this.flCadastrando = false;

		FacesUtil.addInfoMessage("Candidato a Ministro salvo com sucesso!");
	}

	public void salvarParecerCandidato() {
		//Trocar <Enter> por <br /> para quebrar linha na exibição na tela em html
		this.getMinistroParecer().setDsParecer(Uteis.inserirQuebraLinhaHtml(this.getMinistroParecer().getDsParecerTemp()));

		// Adicionar o protocolo no parecer
		this.getMinistroParecer().setMinistro(protocolo.getMinistro());

		// Salvar parecer
		ministroParecerBO.salvarParecer(this.getMinistroParecer());

		//Exibir mensagem
		FacesUtil.addInfoMessage("Parecer incluido para protocolo (" + this.protocolo.getCdProtocolo() + ") associado ao candidato (" + this.protocolo.getMinistro().getNmNome() + ") com sucesso");

		// Fechar a tela de Dialog
		this.fecharDialogoPrimeFaces(true);

		// Atualizar protocolo para exibir novo parecer na tela
		this.protocolo = protocoloBO.find(this.protocolo.getSqProtocolo());
	}

	public void salvarProtocoloConclusao() {
		// Validações primárias

		boolean flErro = false;
		if(this.cdProtocoloConclusao != 8 && (protocolo.getMinistro().getCargo() == null || protocolo.getMinistro().getCargo().getSqCargo() < 1 )) {
			flErro = true;
			FacesUtil.addErrorMessage("Faltou selecionar o Cargo do candidato.");
		}

		if(this.cdProtocoloConclusao != 8 && protocolo.getMinistro().getDtOrdenado() == null) {
			flErro = true;
			FacesUtil.addErrorMessage("Faltou informar a data de ordenação.");
		}

		if(this.cdProtocoloConclusao != 7 && this.cdProtocoloConclusao != 8) {
			flErro = true;
			FacesUtil.addErrorMessage("Faltou selecionar se o protocolo foi concluído ou cancelado.");
		}

		if(this.getMinistroParecer() == null || StringUtils.isBlank(this.getMinistroParecer().getDsParecerTemp())) {
			flErro = true;
			FacesUtil.addErrorMessage("Faltou informar o parecer.");
		}

		if(flErro) {
			throw new NegocioException("Corrija os eventos e tente novamente.");
		}

		// Salvar as alteraçoes do protocolo
		candidatoBO.salvarProtocoloConclusao(this.protocolo, Uteis.inserirQuebraLinhaHtml(this.getMinistroParecer().getDsParecerTemp()), this.cdProtocoloConclusao );

		//Exibir mensagem
		FacesUtil.addInfoMessage("Protocolo (" + this.protocolo.getCdProtocolo() + ") finalizado.");
		if(cdProtocoloConclusao == 8)
			FacesUtil.addInfoMessage("Candidato (" + this.protocolo.getMinistro().getCdCodigo() + " - " + this.protocolo.getMinistro().getNmNome() + ") CANCELADO.");

		if(cdProtocoloConclusao == 7)
			FacesUtil.addInfoMessage("Candidato (" + this.protocolo.getMinistro().getCdCodigo() + " - " + this.protocolo.getMinistro().getNmNome() + ") transferido para o rol de Ministros da CONFRATERES.");
		// Fechar a tela de Dialog
		this.fecharDialogoPrimeFaces(true);

		// Atualizar o protocolo para exibir as alterações na tela
		this.protocolo = protocoloBO.find(this.protocolo.getSqProtocolo());
	}


	private void limpar() {
		this.protocolo = new Protocolo();
		this.protocolo.setMinistro(new Ministro());
		this.protocolo.setProtocoloStatus(new ProtocoloStatus());

		this.protocolo.getMinistro().setMinistroAnexo(new ArrayList<MinistroAnexo>());

		this.limparMinistroParecer();

	}

	public boolean isEditando(){
		boolean retorno = false;
		if(this.protocolo!= null){
			retorno = this.protocolo.getSqProtocolo() != null;
		}

		return retorno;
	}

	public Protocolo getProtocolo() {
		return protocolo;
	}

	public void setProtocolo(Protocolo protocolo) {
		this.protocolo = protocolo;
	}

	public Long getSqCodigoDepartamentoOriginal() {
		return sqCodigoDepartamentoOriginal;
	}

	public boolean isFlInserirEditarCodigoMinistro() {
		return flInserirEditarCodigoMinistro;
	}

	public void setFlInserirEditarCodigoMinistro(boolean flInserirEditarCodigoMinistro) {
		this.flInserirEditarCodigoMinistro = flInserirEditarCodigoMinistro;
	}

	public void upload(FileUploadEvent event) {
		UploadedFile uploadFile = event.getFile();

		try {
			String foto = fotoBO.salvarFotoTemp(uploadFile.getFileName(), uploadFile.getContents());
			protocolo.getMinistro().setDsFoto(foto);

			// Se estiver editando salva a foto no local definitivo senão salvar a foto junto com o candidato
			if(this.isEditando()) {
				candidatoBO.salvarFotoNaAlteracaoCandidato(protocolo);
			}

		} catch (Exception e) {
			FacesUtil.addErrorMessage(e.getMessage());
		}
	}

	public void removerFoto() {
		try {
			if(this.isEditando()) {
				candidatoBO.salvarExcluirFotoNaAlteracao(protocolo);
			} else {
				fotoBO.deletarTemp(protocolo.getMinistro().getDsFoto());
			}

			protocolo.getMinistro().setDsFoto(Uteis._FOTO_DEFAULT);

		} catch (IOException e) {
			FacesUtil.addErrorMessage(e.getMessage());
		}
	}


	public void removerAnexo(ActionEvent event) {
		try {
			if(this.isEditando()) {
				Long attribute_sqMinistroAnexo = (Long)event.getComponent().getAttributes().get("attribute_sqMinistroAnexo");
				if(attribute_sqMinistroAnexo != null && attribute_sqMinistroAnexo > 0) {
					String anexoRemovido = candidatoBO.salvarExcluirAnexoNaAlteracao(attribute_sqMinistroAnexo);

					// Atualizar protocolo
					this.protocolo = protocoloBO.find(this.protocolo.getSqProtocolo());

					FacesUtil.addInfoMessage("Anexo (" + anexoRemovido + ") removido com sucesso.");

				} else {
					FacesUtil.addErrorMessage("Não foi possível excluir o Anexo, pois o código (" + attribute_sqMinistroAnexo + ") está em desacordo com o padrão.");
				}

			} else {

				String attribute_dsAnexoRenomeado = (String)event.getComponent().getAttributes().get("attribute_dsAnexoRenomeado");

				// Excluindo o anexo
				anexosBO.deletarTemp(attribute_dsAnexoRenomeado);

				// Excluindo da lista
				for (int i = 0; i < protocolo.getMinistro().getMinistroAnexo().size(); i++) {
					if(protocolo.getMinistro().getMinistroAnexo().get(i).getDsAnexoRenomeado().equalsIgnoreCase(attribute_dsAnexoRenomeado)) {
						String dsAnexoOriginal = protocolo.getMinistro().getMinistroAnexo().get(i).getDsAnexoOriginal();
						protocolo.getMinistro().getMinistroAnexo().remove(i);
						FacesUtil.addInfoMessage("Anexo (" + dsAnexoOriginal + ") removido com sucesso.");
						break;
					}
				}
			}	

		} catch (IOException e ) {
			FacesUtil.addErrorMessage(e.getMessage());
		}

	}

	// Fonte: https://blog.algaworks.com/primefaces-fileupload/
	public void uploadArquivo(FileUploadEvent event) {
		UploadedFile uploadFile = event.getFile();
		this.ministroAnexo = new MinistroAnexo();


		try {
			String anexo = anexosBO.salvarAnexosTemp(uploadFile.getFileName(), uploadFile.getContents(), protocolo.getMinistro().getSqMinistro());



			this.ministroAnexo.setDsAnexoOriginal(uploadFile.getFileName());
			this.ministroAnexo.setDsAnexoContentType(uploadFile.getContentType());
			this.ministroAnexo.setDsAnexoRenomeado(anexo);
			this.ministroAnexo.setMinistro(protocolo.getMinistro());

			// Se estiver editando salva o anexo no local definitivo senão salvar anexo junto com o candidato
			if(this.isEditando()) {
				candidatoBO.salvarAnexosNaAlteracaoCandidato(protocolo, ministroAnexo);

				// Atualizar protocolo para exibir novo anexo na tela
				this.protocolo = protocoloBO.find(this.protocolo.getSqProtocolo());

			}else {
				this.protocolo.getMinistro().getMinistroAnexo().add(ministroAnexo);
			}


		} catch (Exception e) {
			FacesUtil.addErrorMessage(e.getMessage());
		}

	}

	public boolean isflCadastrando() {
		return flCadastrando;
	}

	public boolean isFlExibirCampo() {
		return flExibirCampo;
	}

	public boolean isFlExibirBotaoConcluir() {
		return flExibirBotaoConcluir;
	}

	public boolean isFlExibirBotaoSalvar() {
		return flExibirBotaoSalvar;
	}

	public int getCdProtocoloConclusao() {
		return cdProtocoloConclusao;
	}

	public void setCdProtocoloConclusao(int cdProtocoloConclusao) {
		this.cdProtocoloConclusao = cdProtocoloConclusao;
	}

}
