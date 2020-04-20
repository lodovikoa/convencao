package br.com.convencao.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import org.apache.commons.lang3.StringUtils;

import br.com.convencao.bo.UsuarioBO;
import br.com.convencao.model.Usuario;
import br.com.convencao.util.cdi.CDIServiceLocator;

@FacesConverter(forClass = Usuario.class)
public class UsuarioConverter implements Converter<Usuario> {
	
	
	private UsuarioBO bo;
	
	public UsuarioConverter() {
		this.bo = CDIServiceLocator.getBean(UsuarioBO.class);
	}

	@Override
	public Usuario getAsObject(FacesContext context, UIComponent component, String value) {
		Usuario retorno = null;
		if(StringUtils.isNotBlank(value)) {
			retorno = bo.find(new Long(value));
		}
		return retorno;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Usuario value) {
		if(value != null) {
			return value.getSqUsuario() == null? "":  value.getSqUsuario().toString();
		}
		return "";
	}
}
