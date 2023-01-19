package br.com.convencao.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import org.apache.commons.lang3.StringUtils;

import br.com.convencao.bo.LancamentoResumoBO;
import br.com.convencao.model.LancamentoResumo;
import br.com.convencao.util.cdi.CDIServiceLocator;

@FacesConverter(forClass = LancamentoResumo.class)
public class LancamentoResumoConverter implements Converter<LancamentoResumo> {
	
	private LancamentoResumoBO bo;
	
	public LancamentoResumoConverter() {
		this.bo = CDIServiceLocator.getBean(LancamentoResumoBO.class);
	}

	@Override
	public LancamentoResumo getAsObject(FacesContext context, UIComponent component, String value) {
		LancamentoResumo retorno = null;
		if(StringUtils.isNotBlank(value)) {
			retorno = bo.findByPrimaryKeyCompleto(Long.valueOf(value));
		}
		return retorno;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, LancamentoResumo value) {		
		if(value != null) {
			return value.getSqResumo() == null? "":  value.getSqResumo().toString();
		}
		return "";
	}

}
