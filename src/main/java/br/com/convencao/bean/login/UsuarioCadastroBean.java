package br.com.convencao.bean.login;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.convencao.bo.UsuarioBO;
import br.com.convencao.model.Usuario;
import br.com.convencao.util.jsf.FacesUtil;

@Named(value="usuarioCadastroBean")
@ViewScoped
public class UsuarioCadastroBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Usuario usuario;
	
	@Inject
	private UsuarioBO usuarioBO;
	

	public void inicializar() {
		if (this.usuario == null)
			this.limpar();
				
	}
	
	
	private void limpar(){
		this.usuario = new Usuario();
		
	}
	
	public void salvar(){
	
			this.usuario = this.usuarioBO.salvar(this.usuario);
			
			FacesUtil.addInfoMessage("Usuário salvo com sucesso!");

	}
	
	public Usuario getUsuario() {
		return usuario;
	}
	
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	
	
	public boolean isEditando(){
		boolean retorno = false;
		if(this.usuario != null)
			retorno = this.usuario.getSqUsuario() != null;
		
		return retorno;
	}
	
}
