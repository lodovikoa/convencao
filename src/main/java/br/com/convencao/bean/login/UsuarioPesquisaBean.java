package br.com.convencao.bean.login;

import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.convencao.bean.codebehind.Codebehind;
import br.com.convencao.bo.UGrupoBO;
import br.com.convencao.bo.UsuarioBO;
import br.com.convencao.bo.UsuarioGrupoBO;
import br.com.convencao.model.UGrupo;
import br.com.convencao.model.UPermissao;
import br.com.convencao.model.Usuario;
import br.com.convencao.model.UsuarioGrupo;
import br.com.convencao.util.jsf.FacesUtil;

@Named(value="usuarioPesquisaBean")
@ViewScoped
public class UsuarioPesquisaBean extends Codebehind {

	private static final long serialVersionUID = 1L;

	@Inject
	private UsuarioBO usuarioBO;

	@Inject
	private UGrupoBO uGrupoBO;

	@Inject
	private UsuarioGrupoBO usuarioGrupoBO;

	private List<Usuario> lista;
	private Usuario selecionado;

	private List<UGrupo> listaGrupos;

	private List<UGrupo> gruposDisponivelSelecionados;
	private List<UGrupo> gruposAssociadosSelecionados;

	private List<UsuarioGrupo> usuGrupo;
	private List<UPermissao> listaPermissoes;

	private boolean recuperarLista = false;

	private void listar() {
		this.lista = usuarioBO.findAll();	
	}

	// Buscar usuário com seus grupos pela prymaryKey
	public void buscarUsuario(Usuario selecionado){
		this.selecionado = usuarioBO.findByPrimaryKeyComGrupos(selecionado.getSqUsuario());
	}

	public void excluir(){
		usuarioBO.remover(selecionado);

		// Refazer a pesquisa para listar as usuarios
		this.listar();

		FacesUtil.addInfoMessage("Usuario " + selecionado.getDsLogin() + " excluido com sucesso!");

	}

	public void iniciarGrupos() {
		this.listaGrupos = uGrupoBO.findAllGrupos();
	}

	public void salvarGruposAssociados() {

		usuarioGrupoBO.salvarGruposAssociados(gruposDisponivelSelecionados, gruposAssociadosSelecionados, this.selecionado);

		//Exibir mensagem
		FacesUtil.addInfoMessage("Grupos associados ao usuário " + this.selecionado.getDsLogin() + " com sucesso");

		// Fechar a tela de Dialog
		this.fecharDialogoPrimeFaces(true);
	}

	public void salvarReiniciarSenha() {
		this.selecionado = usuarioBO.salvarReiniciarSenha(this.selecionado);

		// Exibir mensagem
		FacesUtil.addInfoMessage("Senha do usuário " + selecionado.getDsLogin() + " reiniciada.");

		// Fechar a tela de Dialog
		this.fecharDialogoPrimeFaces(true);

		this.recuperarLista = true;

	}

	public List<Usuario> getLista() {
		if(this.lista == null || this.recuperarLista) {
			this.recuperarLista = false;
			this.listar();
		}

		return lista;
	}

	public void setLista(List<Usuario> lista) {
		this.lista = lista;
	}

	public Usuario getSelecionado() {
		return selecionado;
	}

	public void setSelecionado(Usuario selecionado) {
		this.buscarUsuario(selecionado);
	}


	public List<UGrupo> getListaGrupos() {

		// Remover grupos já associados da lista de grupos
		if(this.selecionado != null)
			this.removerGruposAssociados();

		return listaGrupos;
	}

	public List<UGrupo> getGruposDisponivelSelecionados() {
		return gruposDisponivelSelecionados;
	}

	public void setGruposDisponivelSelecionados(List<UGrupo> gruposDisponivelSelecionados) {
		this.gruposDisponivelSelecionados = gruposDisponivelSelecionados;
	}

	public List<UGrupo> getGruposAssociadosSelecionados() {
		return gruposAssociadosSelecionados;
	}

	public void setGruposAssociadosSelecionados(List<UGrupo> gruposAssociadosSelecionados) {
		this.gruposAssociadosSelecionados = gruposAssociadosSelecionados;
	}

	public UsuarioGrupoBO getUsuarioGrupoBO() {
		return usuarioGrupoBO;
	}

	public void setUsuarioGrupoBO(UsuarioGrupoBO usuarioGrupoBO) {
		this.usuarioGrupoBO = usuarioGrupoBO;
	}

	public String getMensagemRodape(){
		return "Total registros encontrados: " + this.lista	.size();
	}

	public List<UsuarioGrupo> getUsuGrupo() {
		return usuGrupo;
	}

	public void setUsuGrupo(List<UsuarioGrupo> usuGrupo) {
		this.usuGrupo = usuGrupo;
	}

	public List<UPermissao> getListaPermissoes() {
		return listaPermissoes;
	}

	// Remover grupos já associados da lista de grupos
	private void removerGruposAssociados() {
		for(int i = 0; i < this.selecionado.getGrupos().size(); i++) {
			UGrupo ugSelecioinado = this.selecionado.getGrupos().get(i);

			for(int j = 0; j < this.listaGrupos.size(); j++) {
				UGrupo ugDisponivel = this.listaGrupos.get(j);			
				if (ugDisponivel.getDsNome().equals(ugSelecioinado.getDsNome()) ) {
					this.listaGrupos.remove(ugDisponivel);
					break;
				}
			}
		}
	}
}
