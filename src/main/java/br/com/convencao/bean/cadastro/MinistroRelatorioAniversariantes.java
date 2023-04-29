package br.com.convencao.bean.cadastro;

import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import br.com.convencao.bo.NegocioException;
import br.com.convencao.util.jsf.FacesUtil;
import lombok.Getter;
import lombok.Setter;

@Named(value = "ministroRelatorioAniversariantes")
@ViewScoped
public class MinistroRelatorioAniversariantes extends MinistroCodbehind {

	private static final long serialVersionUID = 1L;
	
	@Getter private List<String> mes = List.of("Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho", "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro");
	@Getter @Setter private String mesInicioSelecionado;
	@Getter @Setter private String mesFimSelecionado;
	
	public void imprimir() {
		int mesInicio = this.mes.indexOf(mesInicioSelecionado)+1;
		int mesFim = this.mes.indexOf(mesFimSelecionado)+1;
		
		// Validar preechimento da combo Mes inicio e Mes fim
		if(mesInicio == 0 || mesFim == 0) {
			throw new NegocioException("Mês Inicio e Mês Fim são de preechimento obrigatório.");
		}
		if(mesInicio > mesFim) {
			throw new NegocioException("Mês Fim deverá ser igual ou posterior ao Mês Inicio.");
		}

		// Monta parametros para exibir relatório
		StringBuilder jsFunction = new StringBuilder();
		jsFunction.append("relatorioMinistroAniversariantes(");
		jsFunction.append("'../../relatorioMinistroAniversariantes'");
		jsFunction.append(",");
		jsFunction.append("'pdf'");
		jsFunction.append(",");
		jsFunction.append(mesInicio);
		jsFunction.append(",'");
		jsFunction.append(this.mesInicioSelecionado);
		jsFunction.append("',");
		jsFunction.append(mesFim);
		jsFunction.append(",'");
		jsFunction.append(this.mesFimSelecionado);
		jsFunction.append("');");

		// Chama função javaScript para exibir relatório
		FacesUtil.getRequestContext().executeScript(jsFunction.toString());
		
		//System.out.println(jsFunction.toString());
		
		
	}
}
