package br.com.convencao.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import org.apache.commons.lang3.StringUtils;

import br.com.convencao.bo.ProtocoloStatusBO;
import br.com.convencao.model.ProtocoloStatus;
import br.com.convencao.util.cdi.CDIServiceLocator;

@FacesConverter(forClass = ProtocoloStatus.class)
public class ProtocoloStatusConverter implements Converter<ProtocoloStatus> {
	
	
	private ProtocoloStatusBO bo;
	
	public ProtocoloStatusConverter() {
		this.bo = CDIServiceLocator.getBean(ProtocoloStatusBO.class);
	}

	@Override
	public ProtocoloStatus getAsObject(FacesContext context, UIComponent component, String value) {
		ProtocoloStatus retorno = null;
		if(StringUtils.isNotBlank(value)) {
			retorno = bo.find(Long.valueOf(value));
		}
		return retorno;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, ProtocoloStatus value) {
		if(value != null) {
			return value.getSqProtocoloStatus() == null? "":  value.getSqProtocoloStatus().toString();
		}
		return "";
	}
}
