package br.com.convencao.converter.outros;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

import org.apache.commons.lang3.StringUtils;

@FacesConverter("converter_cep")
public class CepConverter implements Converter<Object> {

	// Referencia: https://pablonobrega.wordpress.com/2009/08/10/implementando-converter-e-validator-de-cpf/
	
	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) throws ConverterException {
		
		String dsCep = value;
		if(StringUtils.isNotBlank(value)) {
			dsCep = value.replaceAll("\\.", "").replaceAll("\\-", "");
		}
		
		return dsCep;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) throws ConverterException {
		

		String dsCep = (String)value;
		if(StringUtils.isNotBlank(dsCep) && dsCep.length() == 8) {
			dsCep = dsCep.substring(0, 2) + "." + dsCep.substring(2, 5) + "-" + dsCep.substring(5, 8);
		}
		
		return dsCep;
	}

}
