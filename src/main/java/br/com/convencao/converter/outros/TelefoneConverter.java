package br.com.convencao.converter.outros;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

import org.apache.commons.lang3.StringUtils;

@FacesConverter("converter_telefone")
public class TelefoneConverter implements Converter<Object> {

	// Referencia: https://pablonobrega.wordpress.com/2009/08/10/implementando-converter-e-validator-de-cpf/
	
	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) throws ConverterException {
		
		String dsTel = value;
		if(StringUtils.isNotBlank(value)) {
			dsTel = value.replaceAll("[^0-9]", "");
		}
		
		return dsTel; // 99999999999
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) throws ConverterException {
		
		String dsTel = ((String)value).replaceAll("[^0-9]", "");		
		if(StringUtils.isNotBlank(dsTel) && dsTel.length() >= 7) {
			dsTel = "(" + dsTel.substring(0, 2) + ") " + dsTel.substring(2, 6) + "-" + dsTel.substring(6);
		}
		
		return dsTel;  //(99) 99999-9999
	}

}
