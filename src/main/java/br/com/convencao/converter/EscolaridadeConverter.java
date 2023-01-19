package br.com.convencao.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import org.apache.commons.lang3.StringUtils;

import br.com.convencao.bo.EscolaridadeBO;
import br.com.convencao.model.Escolaridade;
import br.com.convencao.util.cdi.CDIServiceLocator;

@FacesConverter(forClass = Escolaridade.class)
public class EscolaridadeConverter implements Converter<Escolaridade> {
	
	
	private EscolaridadeBO bo;
	
	public EscolaridadeConverter() {
		this.bo = CDIServiceLocator.getBean(EscolaridadeBO.class);
	}

	@Override
	public Escolaridade getAsObject(FacesContext context, UIComponent component, String value) {
		Escolaridade retorno = null;
		if(StringUtils.isNotBlank(value)) {
			retorno = bo.find(Long.valueOf(value));
		}
		return retorno;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Escolaridade value) {
		if(value != null) {
			return value.getSqEscolaridade() == null? "":  value.getSqEscolaridade().toString();
		}
		return "";
	}
}
