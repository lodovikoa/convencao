package br.com.convencao.converter.outros;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

import org.apache.commons.lang3.StringUtils;

@FacesConverter("converter_cnpj")
public class CnpjConverter implements Converter<Object>{
	
	// Fonte: https://pablonobrega.wordpress.com/2009/08/25/implementando-converter-e-validator-de-cnpj/

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) throws ConverterException {
		
		String dsCnpj = value;
		if(StringUtils.isNotBlank(dsCnpj)) {
			dsCnpj = value.replaceAll("[^0-9]", "");
		}
		
		
		return dsCnpj;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) throws ConverterException {
		
		String dsCnpj = ((String) value).replaceAll("[^0-9]", "");;
		if(StringUtils.isNotBlank(dsCnpj) && dsCnpj.length() == 14) {
			dsCnpj = dsCnpj.substring(0, 2) + "." + dsCnpj.substring(2,5) + "." + dsCnpj.substring(5,8) + "/" + dsCnpj.substring(8,12) + "-" + dsCnpj.substring(12,14);
		}
		
		return dsCnpj;
	}

}
