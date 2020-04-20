package br.com.convencao.util.jsf;

import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.primefaces.PrimeFaces;

// import org.primefaces.context.RequestContext;

public class FacesUtil {
	
	private static boolean usuarioBloqueado = false;
	private static boolean usuarioTrocarSenha = false;
	private static Long sqUsuario;
	private static Integer qtdeErrosSenha = 0;
	
	public static Integer getQtdeErrosSenha() {
		return qtdeErrosSenha;
	}
	
	public static void setQtdeErrosSenha(Integer qtdeErrosSenha) {
		FacesUtil.qtdeErrosSenha = qtdeErrosSenha;
	}
	
	public static Long getSqUsuario() {
		return sqUsuario;
	}
	
	public static void setSqUsuario(Long sqUsuario) {
		FacesUtil.sqUsuario = sqUsuario;
	}
	
	
	public static boolean isUsuarioBloqueado() {
		return usuarioBloqueado;
	}
	
	public static void setUsuarioBloqueado(boolean usuarioBloqueado) {
		FacesUtil.usuarioBloqueado = usuarioBloqueado;
	}
	
	public static boolean isUsuarioTrocarSenha() {
		return usuarioTrocarSenha;
	}
	
	public static void setUsuarioTrocarSenha(boolean usuarioTrocarSenha) {
		FacesUtil.usuarioTrocarSenha = usuarioTrocarSenha;
	}

	public static void addErrorMessage(String message) {
		FacesContext.getCurrentInstance().addMessage(null, 
				new FacesMessage(FacesMessage.SEVERITY_ERROR, message, message));
	}
	
	public static void addInfoMessage(String message) {
		FacesContext.getCurrentInstance().addMessage(null, 
				new FacesMessage(FacesMessage.SEVERITY_INFO, message, message));
	}
	
	public static String obterParametro(String param){
		return FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get(param);
	}
	
	public static boolean isPostback(){
		return FacesContext.getCurrentInstance().isPostback();
	}
	
	public static boolean isNotPostback(){
		return !isPostback();
	}
	
	
	public static ExternalContext getExternalContext() {
		return getFacesContext().getExternalContext();
	}
	
	public static FacesContext getFacesContext() {
		return FacesContext.getCurrentInstance();
	}
	
	public static HttpServletRequest getHttpServletRequest() {
		return ((HttpServletRequest) getExternalContext().getRequest());	
	}
	
	public static HttpServletResponse getHttpServletResponse() {
		return ((HttpServletResponse) getExternalContext().getResponse());	
	}	
	
//	public static RequestContext getRequestContext() {
//		return RequestContext.getCurrentInstance();
//	}
	
	public static PrimeFaces getRequestContext() {
		return PrimeFaces.current();
	}
	
}
