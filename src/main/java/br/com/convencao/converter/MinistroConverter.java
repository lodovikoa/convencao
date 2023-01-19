package br.com.convencao.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import org.apache.commons.lang3.StringUtils;

import br.com.convencao.bo.MinistroBO;
import br.com.convencao.model.Ministro;
import br.com.convencao.util.cdi.CDIServiceLocator;

@FacesConverter(forClass = Ministro.class)
public class MinistroConverter implements Converter<Ministro> {
	
	
	private MinistroBO bo;
	
	public MinistroConverter() {
		this.bo = CDIServiceLocator.getBean(MinistroBO.class);
	}

	@Override
	public Ministro getAsObject(FacesContext context, UIComponent component, String value) {
		Ministro retorno = null;
		if(StringUtils.isNotBlank(value)) {
			retorno = bo.findByPrimaryKey(Long.valueOf(value));
	
			// Ordenar registros de pareceres por ordem inversa de data de registro
			retorno.getMinistroParecer().sort((a,b) -> b.getDtRegistro().compareTo(a.getDtRegistro()));
		}
		return retorno;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Ministro value) {
		if(value != null) {
			return value.getSqMinistro() == null? "":  value.getSqMinistro().toString();
		}
		return "";
	}
}
