package br.com.convencao.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import org.apache.commons.lang3.StringUtils;

import br.com.convencao.bo.PlanoContaBO;
import br.com.convencao.model.PlanoConta;
import br.com.convencao.util.cdi.CDIServiceLocator;

@FacesConverter(forClass = PlanoConta.class)
public class PlanoContaConverter implements Converter<PlanoConta> {
	
	
	private PlanoContaBO bo;
	
	public PlanoContaConverter() {
		this.bo = CDIServiceLocator.getBean(PlanoContaBO.class);
	}

	@Override
	public PlanoConta getAsObject(FacesContext context, UIComponent component, String value) {
		PlanoConta retorno = null;
		if(StringUtils.isNotBlank(value)) {
			retorno = bo.findByPrimaryKey(Long.valueOf(value));
		}
		return retorno;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, PlanoConta value) {
		if(value != null) {
			return value.getSqPlanoConta() == null? "":  value.getSqPlanoConta().toString();
		}
		return "";
	}
}
