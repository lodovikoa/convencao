package br.com.convencao.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import org.apache.commons.lang3.StringUtils;

import br.com.convencao.bo.ConvencaoBO;
import br.com.convencao.model.Convencao;
import br.com.convencao.util.cdi.CDIServiceLocator;


@FacesConverter(forClass = Convencao.class)
public class ConvencaoConverter implements Converter<Convencao> {
	
	
	private ConvencaoBO bo;
	
	public ConvencaoConverter() {
		this.bo = CDIServiceLocator.getBean(ConvencaoBO.class);
	}

	@Override
	public Convencao getAsObject(FacesContext context, UIComponent component, String value) {
		Convencao retorno = null;
		if(StringUtils.isNotBlank(value)) {
			retorno = bo.findByPrimaryKey(new Long(value));
		}
		return retorno;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Convencao value) {
		if(value != null) {
			return value.getSqConvencao() == null? "":  value.getSqConvencao().toString();
		}
		return "";
	}
}
