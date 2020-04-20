package br.com.convencao.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import org.apache.commons.lang3.StringUtils;

import br.com.convencao.bo.RegiaoBO;
import br.com.convencao.model.Regiao;
import br.com.convencao.util.cdi.CDIServiceLocator;

@FacesConverter(forClass = Regiao.class)
public class RegiaoConverter implements Converter<Regiao> {
	
	private RegiaoBO bo;
	
	public RegiaoConverter() {
		this.bo = CDIServiceLocator.getBean(RegiaoBO.class);
	}

	@Override
	public Regiao getAsObject(FacesContext context, UIComponent component, String value) {
		Regiao retorno = null;
		if(StringUtils.isNotBlank(value)) {
			retorno = bo.findByPrimaryKey(new Long(value));
		}
		return retorno;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Regiao value) {
		if(value != null) {
			return value.getSqRegiao() == null? "":  value.getSqRegiao().toString();
		}
		return "";
	}
}
