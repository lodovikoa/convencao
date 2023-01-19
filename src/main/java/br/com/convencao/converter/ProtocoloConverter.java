package br.com.convencao.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import org.apache.commons.lang3.StringUtils;

import br.com.convencao.bo.ProtocoloBO;
import br.com.convencao.model.Protocolo;
import br.com.convencao.util.cdi.CDIServiceLocator;

@FacesConverter(forClass = Protocolo.class)
public class ProtocoloConverter implements Converter<Protocolo> {
	
	
	private ProtocoloBO bo;
	
	public ProtocoloConverter() {
		this.bo = CDIServiceLocator.getBean(ProtocoloBO.class);
	}

	@Override
	public Protocolo getAsObject(FacesContext context, UIComponent component, String value) {
		Protocolo retorno = null;
		if(StringUtils.isNotBlank(value)) {
			retorno = bo.find(Long.valueOf(value));
		}
		return retorno;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Protocolo value) {
		if(value != null) {
			return value.getSqProtocolo() == null? "":  value.getSqProtocolo().toString();
		}
		return "";
	}
}
