package br.com.convencao.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import org.apache.commons.lang3.StringUtils;

import br.com.convencao.bean.cadastro.RegiaoItens;
import br.com.convencao.bo.RegiaoBO;
import br.com.convencao.model.Regiao;
import br.com.convencao.util.cdi.CDIServiceLocator;

@FacesConverter(forClass = RegiaoItens.class)
public class RegiaoItensConverter implements Converter<RegiaoItens> {
	
	private RegiaoBO bo;
	
	public RegiaoItensConverter() {
		this.bo = CDIServiceLocator.getBean(RegiaoBO.class);
	}

	@Override
	public RegiaoItens getAsObject(FacesContext context, UIComponent component, String value) {
		RegiaoItens retorno = new RegiaoItens();
		if(StringUtils.isNotBlank(value)) {
			Regiao regiao = bo.findByPrimaryKey(Long.valueOf(value));
			
			retorno.setSqRegiao(regiao.getSqRegiao());
			retorno.setDsRegiao(regiao.getDsRegiao());
		}
		return retorno;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, RegiaoItens value) {
		if(value != null) {
			return value.getSqRegiao() == null? "":  value.getSqRegiao().toString();
		}
		return "";
	}
}
