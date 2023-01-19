package br.com.convencao.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import org.apache.commons.lang3.StringUtils;

import br.com.convencao.bo.ProfissaoBO;
import br.com.convencao.model.Profissao;
import br.com.convencao.util.cdi.CDIServiceLocator;

@FacesConverter(forClass = Profissao.class)
public class ProfissaoConverter implements Converter<Profissao> {
	
	
	private ProfissaoBO bo;
	
	public ProfissaoConverter() {
		this.bo = CDIServiceLocator.getBean(ProfissaoBO.class);
	}

	@Override
	public Profissao getAsObject(FacesContext context, UIComponent component, String value) {
		Profissao retorno = null;
		if(StringUtils.isNotBlank(value)) {
			retorno = bo.find(Long.valueOf(value));
		}
		return retorno;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Profissao value) {
		if(value != null) {
			return value.getSqProfissao() == null? "":  value.getSqProfissao().toString();
		}
		return "";
	}
}
