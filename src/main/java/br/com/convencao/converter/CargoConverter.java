package br.com.convencao.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import org.apache.commons.lang3.StringUtils;

import br.com.convencao.bo.CargoBO;
import br.com.convencao.model.Cargo;
import br.com.convencao.util.cdi.CDIServiceLocator;

@FacesConverter(forClass = Cargo.class)
public class CargoConverter implements Converter<Cargo> {
	
	
	private CargoBO bo;
	
	public CargoConverter() {
		this.bo = CDIServiceLocator.getBean(CargoBO.class);
	}

	@Override
	public Cargo getAsObject(FacesContext context, UIComponent component, String value) {
		Cargo retorno = null;
		if(StringUtils.isNotBlank(value)) {
			retorno = bo.find(new Long(value));
		}
		return retorno;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Cargo value) {
		if(value != null) {
			return value.getSqCargo() == null? "":  value.getSqCargo().toString();
		}
		return "";
	}
}
