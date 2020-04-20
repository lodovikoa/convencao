package br.com.convencao.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import org.apache.commons.lang3.StringUtils;

import br.com.convencao.bo.EstadoBO;
import br.com.convencao.model.Estado;
import br.com.convencao.util.cdi.CDIServiceLocator;

@FacesConverter(forClass = Estado.class)
public class EstadoConverter implements Converter<Estado> {
	
	
	private EstadoBO bo;
	
	public EstadoConverter() {
		this.bo = CDIServiceLocator.getBean(EstadoBO.class);
	}

	@Override
	public Estado getAsObject(FacesContext context, UIComponent component, String value) {
		Estado retorno = null;
		if(StringUtils.isNotBlank(value)) {
			retorno = bo.find(new Long(value));
		}
		return retorno;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Estado value) {
		if(value != null) {
			return value.getSqEstado() == null? "":  value.getSqEstado().toString();
		}
		return "";
	}
}
