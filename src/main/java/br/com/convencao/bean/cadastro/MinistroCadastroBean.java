package br.com.convencao.bean.cadastro;

import java.io.IOException;

import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.file.UploadedFile;

import br.com.convencao.bo.AnexosBO;
import br.com.convencao.bo.FotoBO;
import br.com.convencao.bo.MinistroBO;
import br.com.convencao.bo.MinistroParecerBO;
import br.com.convencao.model.Ministro;
import br.com.convencao.model.MinistroAnexo;
import br.com.convencao.util.Uteis;
import br.com.convencao.util.jsf.FacesUtil;
import br.com.convencao.util.permissao.Permissoes;

@Named(value="ministroCadastroBean")
@ViewScoped
public class MinistroCadastroBean extends MinistroCodbehind {

	private static final long serialVersionUID = 1L;

	private Ministro ministro;
	private MinistroAnexo ministroAnexo;

	private boolean flInserirEditarMinistro;
	private boolean flExibirComboDepartamento;
	private boolean flInserirEditarCodigoMinistro = true;
	private String dsCodigoSugerido;
	private Long sqCodigoDepartamentoOriginal;

	@Inject
	private AnexosBO anexosBO;

	@Inject
	private MinistroBO bo;

	@Inject
	private MinistroParecerBO ministroParecerBO;

	@Inject
	private FotoBO fotoBO;

	public void inicializar(String tipo){

		// Grupos de usuário que tem permissão para inserir/editar todos os dados do ministro.
		this.flInserirEditarMinistro = Permissoes.getPermissaoInserirEditarMinistro();

		// Caso estiver Cadatrando o Conjuge atraves do botão [...] ao lado do Combobox Conjuge, não faz as atualizações de tela
		if(!this.getFlCadastroConjugeSalvo()) {

			if(this.getParam_controle().equals("1")) {
				if(ministro.getDepartamento().getSqDepartamento() == 1 || ministro.getDepartamento().getSqDepartamento() == 5 )
					this.flExibirComboDepartamento = true;
			} else {
				this.flExibirComboDepartamento = false;
			}

			if(this.ministro == null){
				this.limpar();
			}

			this.inicializarDepartamentos(tipo);
			this.inicializarCargos();
			this.inicializarEstadoCiveis();
			this.inicializarEscolaridades();
			this.inicializarProfissoes();
			this.inicializarEstados();
			this.inicializarEsposasMinistro();
			// parametro: -1L buscar todas as igrejas independente de região
			// parametro: true colocar "ASSEMLBEIA DE DEUS" no final do nome da igreja Ex.: CAMPO GRANDE, ASSEMLBEIA DE DEUS
			this.inicializarIgrejas(-1L);

			this.inicializarMinistroConjuge();

		} else {
			// Caso estiver cadastrando o conjuge pelo botão [...] ao lado Combobox Conjuge, ao salvar entra aqui para atualizar a combobox Conjuge.
			this.setFlCadastroConjugeSalvo(false);

			if(null != this.getMinistroConjuge() && null != this.getMinistroConjuge().getSqMinistro()) {
				this.ministro.setConjuge(this.getMinistroConjuge());
			}
		}
	}

	private void limpar(){
		this.ministro = new Ministro();
	}

	public void salvar(){	

		this.ministro = this.bo.salvar(this.ministro);

		FacesUtil.addInfoMessage("Ministro salvo com sucesso!");
	}

	public void salvarParecerMinistro() {
		//Trocar <Enter> por <br /> para quebrar linha na exibição na tela em html
		this.getMinistroParecer().setDsParecer(Uteis.inserirQuebraLinhaHtml(this.getMinistroParecer().getDsParecerTemp()));

		// Adicionar o protocolo no parecer
		this.getMinistroParecer().setMinistro(ministro);

		// Salvar parecer
		ministroParecerBO.salvarParecer(this.getMinistroParecer());

		//Exibir mensagem
		FacesUtil.addInfoMessage("Parecer incluido para ministro (" + this.ministro.getNmNome() + ") com sucesso");

		// Fechar a tela de Dialog
		this.fecharDialogoPrimeFaces(true);

		// Atualizar ministro para exibir novo parecer na tela
		this.ministro = bo.find(this.ministro.getSqMinistro());

		// Ordenar exibição de pareceres
		this.ministro.getMinistroParecer().sort((a, b) -> b.getDtRegistro().compareTo(a.getDtRegistro()));
	}

	public void upload(FileUploadEvent event) {
		UploadedFile uploadFile = event.getFile();

		try {
			String foto = fotoBO.salvarFotoTemp(uploadFile.getFileName(), uploadFile.getContent(), ministro.getSqMinistro());
			ministro.setDsFoto(foto);

			// Se estiver editando salva a foto no local definitivo senão salvar a foto junto com o ministro
			if(this.isEditando()) {
				bo.salvarFotoNaAlteracao(ministro);
			}

		} catch (Exception e) {
			FacesUtil.addErrorMessage(e.getMessage());
		}
	}

	// Fonte: https://blog.algaworks.com/primefaces-fileupload/
	public void uploadArquivo(FileUploadEvent event) {
		UploadedFile uploadFile = event.getFile();
		this.ministroAnexo = new MinistroAnexo();


		try {
			String anexo = anexosBO.salvarAnexosTemp(uploadFile.getFileName(), uploadFile.getContent(), this.ministro.getSqMinistro());



			this.ministroAnexo.setDsAnexoOriginal(uploadFile.getFileName());
			this.ministroAnexo.setDsAnexoContentType(uploadFile.getContentType());
			this.ministroAnexo.setDsAnexoRenomeado(anexo);
			this.ministroAnexo.setMinistro(this.ministro);

			// Se estiver editando salva o anexo no local definitivo senão salvar anexo junto com o candidato
			if(this.isEditando()) {
				bo.salvarAnexosNaAlteracao(this.ministro, ministroAnexo);

				// Atualizar ministro para exibir novo anexo na tela
				this.ministro = bo.find(this.ministro.getSqMinistro());

			}else {
				this.ministro.getMinistroAnexo().add(ministroAnexo);
			}


		} catch (Exception e) {
			FacesUtil.addErrorMessage(e.getMessage());
		}

	}

	public void removerFoto() {
		try {
			fotoBO.deletarTemp(ministro.getDsFoto());
		} catch (IOException e) {
			FacesUtil.addErrorMessage(e.getMessage());
		}

		ministro.setDsFoto(null);
	}

	public void removerAnexo(ActionEvent event) {

		if(this.isEditando()) {
			Long attribute_sqMinistroAnexo = (Long)event.getComponent().getAttributes().get("attribute_sqMinistroAnexo");
			if(attribute_sqMinistroAnexo != null && attribute_sqMinistroAnexo > 0) {
				String anexoRemovido = bo.salvarExcluirAnexoNaAlteracao(attribute_sqMinistroAnexo);

				// Atualizar Tela de Ministro
				this.ministro = bo.find(this.ministro.getSqMinistro());

				FacesUtil.addInfoMessage("Anexo (" + anexoRemovido + ") removido com sucesso.");
			} else {
				FacesUtil.addErrorMessage("Não foi possível excluir o Anexo, pois o código (" + attribute_sqMinistroAnexo + ") está em desacordo com o padrão.");
			}
		} else {
			FacesUtil.addErrorMessage("Inclusão de anexos para Ministro deve ser incluído na Alteração do Ministro.");
		}


	}


	public Ministro getMinistro() {
		return ministro;
	}

	public void setMinistro(Ministro ministro) {
		this.ministro = ministro;
	}

	public boolean isFlInserirEditarMinistro() {
		return flInserirEditarMinistro;
	}

	public void setFlInserirEditarMinistro(boolean flInserirEditarMinistro) {
		this.flInserirEditarMinistro = flInserirEditarMinistro;
	}

	public boolean isFlExibirComboDepartamento() {
		return flExibirComboDepartamento;
	}

	public void setFlExibirComboDepartamento(boolean flExibirComboDepartamento) {
		this.flExibirComboDepartamento = flExibirComboDepartamento;
	}

	public boolean isEditando(){
		boolean retorno = false;
		if(this.ministro != null){
			retorno = this.ministro.getSqMinistro() != null;
		}

		return retorno;
	}


	public boolean isFlInserirEditarCodigoMinistro() {
		return flInserirEditarCodigoMinistro;
	}

	public void setFlInserirEditarCodigoMinistro(boolean flInserirEditarCodigoMinistro) {
		this.flInserirEditarCodigoMinistro = flInserirEditarCodigoMinistro;
	}

	public String getDsCodigoSugerido() {
		return dsCodigoSugerido;
	}

	public void setDsCodigoSugerido(String dsCodigoSugerido) {
		this.dsCodigoSugerido = dsCodigoSugerido;
	}

	public Long getSqCodigoDepartamentoOriginal() {
		return sqCodigoDepartamentoOriginal;
	}

	// Codigo de ministro para os departamentos diferente de CONFRATERES (1) serão calculados pelo próprio sistema, 
	// Exceto na alteração de código diferente de 1 para 1 (CONFRATERES)
	public void sugerirCodigoAjax() {
		// Se estiver editando e o código não era de CONFRATERES e passou a ser CONFRATERES
		if(ministro.getDepartamento() != null) { 
			if(isEditando()) {
				if(this.sqCodigoDepartamentoOriginal == null ) this.sqCodigoDepartamentoOriginal = bo.findCodigoDepartamento(ministro.getSqMinistro());
				if(this.sqCodigoDepartamentoOriginal != null && this.sqCodigoDepartamentoOriginal != 1 && ministro.getDepartamento().getSqDepartamento() == 1) {
					this.flInserirEditarCodigoMinistro = false;
					this.dsCodigoSugerido = "(" + bo.ultimoCodigoRegisgtrado(ministro) + ") Rg sugerido";
				} else {
					this.dsCodigoSugerido = null;
					this.flInserirEditarCodigoMinistro = true;
				}
			} else {
				if(ministro.getDepartamento().getSqDepartamento() == 1) {
					this.flInserirEditarCodigoMinistro = false;
					this.dsCodigoSugerido = "(" + bo.ultimoCodigoRegisgtrado(ministro) + ") Rg sugerido";
				} else {
					this.flInserirEditarCodigoMinistro = true;
					this.dsCodigoSugerido = "Rg será calculado pelo sistema";
				}
			}

		}else {
			this.flInserirEditarCodigoMinistro = true;
			this.dsCodigoSugerido = null;
		}
	}

}
