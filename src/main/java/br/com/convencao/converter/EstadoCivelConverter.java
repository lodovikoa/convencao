package br.com.convencao.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import org.apache.commons.lang3.StringUtils;

import br.com.convencao.bo.EstadoCivelBO;
import br.com.convencao.model.EstadoCivel;
import br.com.convencao.util.cdi.CDIServiceLocator;

@FacesConverter(forClass = EstadoCivel.class)
public class EstadoCivelConverter implements Converter<EstadoCivel> {
	
	
	private EstadoCivelBO bo;
	
	public EstadoCivelConverter() {
		this.bo = CDIServiceLocator.getBean(EstadoCivelBO.class);
	}

	@Override
	public EstadoCivel getAsObject(FacesContext context, UIComponent component, String value) {
		EstadoCivel retorno = null;
		if(StringUtils.isNotBlank(value)) {
			retorno = bo.find(new Long(value));
		}
		return retorno;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, EstadoCivel value) {
		if(value != null) {
			return value.getSqEstadoCivel() == null? "":  value.getSqEstadoCivel().toString();
		}
		return "";
	}
}
