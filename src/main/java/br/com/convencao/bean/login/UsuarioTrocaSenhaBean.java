package br.com.convencao.bean.login;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.convencao.bo.UsuarioBO;
import br.com.convencao.model.Usuario;
import br.com.convencao.util.jsf.FacesUtil;

@Named(value="usuarioTrocaSenhaBean")
@ViewScoped
public class UsuarioTrocaSenhaBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Usuario usuario;
	
	@Inject
	private UsuarioBO bo;
	
	
	public void inicializar() {
		// Buscar usuário pelo login do usuário logado
		this.usuario = bo.findByLogin();
	}
	

	public Usuario getUsuario() {
		return usuario;
	}
	
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	
	public void salvar() {
		
		String dsMensagem = "Troca de senha realizada com sucesso!";
		if(this.usuario.isTrocaSenha()) {
			dsMensagem = dsMensagem + " REINICIE O SISTEMA PARA GANHAR PERMISSÕES.";
		}
	
		bo.salvarTrocaSenha(this.usuario);
		
		FacesUtil.addInfoMessage(dsMensagem);
	}
	
}
