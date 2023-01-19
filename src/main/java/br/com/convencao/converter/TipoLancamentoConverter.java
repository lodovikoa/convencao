package br.com.convencao.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import org.apache.commons.lang3.StringUtils;

import br.com.convencao.bo.TipoLancamentoBO;
import br.com.convencao.model.TipoLancamento;
import br.com.convencao.util.cdi.CDIServiceLocator;

@FacesConverter(forClass = TipoLancamento.class)
public class TipoLancamentoConverter implements Converter<TipoLancamento> {
	
	
	private TipoLancamentoBO bo;
	
	public TipoLancamentoConverter() {
		this.bo = CDIServiceLocator.getBean(TipoLancamentoBO.class);
	}

	@Override
	public TipoLancamento getAsObject(FacesContext context, UIComponent component, String value) {
		TipoLancamento retorno = null;
		if(StringUtils.isNotBlank(value)) {
			retorno = bo.findByPrimaryKey(Long.valueOf(value));
		}
		return retorno;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, TipoLancamento value) {
		if(value != null) {
			return value.getSqTipoLancamento() == null? "":  value.getSqTipoLancamento().toString();
		}
		return "";
	}
}
