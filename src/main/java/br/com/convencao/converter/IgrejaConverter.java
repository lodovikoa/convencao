package br.com.convencao.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import org.apache.commons.lang3.StringUtils;

import br.com.convencao.bo.IgrejaBO;
import br.com.convencao.model.Igreja;
import br.com.convencao.util.cdi.CDIServiceLocator;

@FacesConverter(forClass = Igreja.class)
public class IgrejaConverter implements Converter<Igreja> {
	
	private IgrejaBO bo;
	
	public IgrejaConverter() {
		this.bo = CDIServiceLocator.getBean(IgrejaBO.class);
	}

	@Override
	public Igreja getAsObject(FacesContext context, UIComponent component, String value) {
		Igreja retorno = null;
		if(StringUtils.isNotBlank(value)) {
			retorno = bo.findByPrimaryKey(new Long(value));
		}
		return retorno;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Igreja value) {		
		if(value != null) {
			return value.getSqIgreja() == null? "":  value.getSqIgreja().toString();
		}
		return "";
	}

}
