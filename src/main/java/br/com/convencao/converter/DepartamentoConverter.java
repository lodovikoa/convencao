package br.com.convencao.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import org.apache.commons.lang3.StringUtils;

import br.com.convencao.bo.DepartamentoBO;
import br.com.convencao.model.Departamento;
import br.com.convencao.util.cdi.CDIServiceLocator;

@FacesConverter(forClass = Departamento.class)
public class DepartamentoConverter implements Converter<Departamento> {
	
	
	private DepartamentoBO bo;
	
	public DepartamentoConverter() {
		this.bo = CDIServiceLocator.getBean(DepartamentoBO.class);
	}

	@Override
	public Departamento getAsObject(FacesContext context, UIComponent component, String value) {
		Departamento retorno = null;
		if(StringUtils.isNotBlank(value)) {
			retorno = bo.find(Long.valueOf(value));
		}
		return retorno;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Departamento value) {
		if(value != null) {
			return value.getSqDepartamento() == null? "":  value.getSqDepartamento().toString();
		}
		return "";
	}

}
