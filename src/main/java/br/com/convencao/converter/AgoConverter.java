package br.com.convencao.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import org.apache.commons.lang3.StringUtils;

import br.com.convencao.bo.AgoBO;
import br.com.convencao.model.Ago;
import br.com.convencao.util.cdi.CDIServiceLocator;

@FacesConverter(forClass = Ago.class)
public class AgoConverter implements Converter<Ago> {
	
	
	private AgoBO bo;
	
	public AgoConverter() {
		this.bo = CDIServiceLocator.getBean(AgoBO.class);
	}

	@Override
	public Ago getAsObject(FacesContext context, UIComponent component, String value) {
		Ago retorno = null;
		if(StringUtils.isNotBlank(value)) {
			retorno = bo.find(Long.parseLong(value));
		}
		return retorno;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Ago value) {
		if(value != null) {
			return value.getSqAgo() == null? "":  value.getSqAgo().toString();
		}
		return "";
	}
}
