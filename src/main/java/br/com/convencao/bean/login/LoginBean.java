package br.com.convencao.bean.login;

import java.io.IOException;
import java.io.Serializable;

import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.convencao.bo.UsuarioBO;
import br.com.convencao.util.jsf.FacesUtil;


@Named(value = "loginBean")
@ViewScoped
public class LoginBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private final Integer QTDE_ERROS_SENHA_PERMITIDOS = 10;
	
	@Inject
	private UsuarioBO usuarioBO;

	private HttpServletRequest request;	
	private HttpServletResponse response;
	private FacesContext context;
	
	public void preRender() {

		this.request = FacesUtil.getHttpServletRequest();
		this.response = FacesUtil.getHttpServletResponse();
		this.context = FacesUtil.getFacesContext();

		if ("true".equals(request.getParameter("invalid"))) {
			if(FacesUtil.isUsuarioBloqueado()) {
				FacesUtil.setUsuarioBloqueado(false);
				FacesUtil.addErrorMessage("Usuário bloqueado!");
			}else {
				// Registrar quantidade de erros de senha cometidas simultaneamente pelo usuário. Permitido até QTDE_ERROS_SENHA_PERMITIDOS erros
				if(FacesUtil.getSqUsuario() != null) {
					Long sqUsuarioTemp = FacesUtil.getSqUsuario();
					FacesUtil.setSqUsuario(null);
					usuarioBO.errosSenhaUsuario(sqUsuarioTemp, QTDE_ERROS_SENHA_PERMITIDOS);
				}
				
				FacesUtil.addErrorMessage("Usuário ou senha inválido!");
			}
		} else {
			FacesUtil.setUsuarioBloqueado(false);
			FacesUtil.setSqUsuario(null);
		}

	}

	public void login() throws ServletException, IOException {
		RequestDispatcher dispatcher = request.getRequestDispatcher("/Login.xhtml");
		dispatcher.forward(request, response);
		context.responseComplete();

	}

}