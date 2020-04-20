package br.com.convencao.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import org.apache.commons.lang3.StringUtils;

import br.com.convencao.bo.RegLancamentoBO;
import br.com.convencao.model.RegLancamento;
import br.com.convencao.util.cdi.CDIServiceLocator;

@FacesConverter(forClass = RegLancamento.class)
public class RegLancamentoConverter implements Converter<RegLancamento> {
	
	
	private RegLancamentoBO bo;
	
	public RegLancamentoConverter() {
		this.bo = CDIServiceLocator.getBean(RegLancamentoBO.class);
	}

	@Override
	public RegLancamento getAsObject(FacesContext context, UIComponent component, String value) {
		RegLancamento retorno = null;
		if(StringUtils.isNotBlank(value)) {
			retorno = bo.findByPrimaryKey(new Long(value));
		}
		return retorno;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, RegLancamento value) {
		if(value != null) {
			return value.getSqRegLancamento() == null? "":  value.getSqRegLancamento().toString();
		}
		return "";
	}
}
