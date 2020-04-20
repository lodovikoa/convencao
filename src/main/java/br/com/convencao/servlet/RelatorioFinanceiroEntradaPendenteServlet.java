package br.com.convencao.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;

import br.com.convencao.util.report.ExecutorRelatorio;

@WebServlet("/relatorioFinanceiroEntradaPendente")
public class RelatorioFinanceiroEntradaPendenteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;


	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Pegar a url usada para chegar aki
		String urlPrincipal = ((HttpServletRequest)request).getRequestURL().toString();

		// Pegar a url parte. Esse servlet
		String urlServlet = ((HttpServletRequest)request).getServletPath().toString();

		// URL para os parametros de url-logo
		String urlLogo = urlPrincipal.replace(urlServlet, "/javax.faces.resource/images/logo02.jpg.xhtml?ln=convencao");
		
		Integer cdSituacao = Integer.parseInt(request.getParameter("cdSituacao"));
		String dsSituacao = "";

		switch(cdSituacao) {
			case 1: dsSituacao = "Ativos"; break;
			case 2: dsSituacao = "Inativos"; break;
			default: dsSituacao = "Todos";
		}
		
		StringBuilder nomeRelatorio = new StringBuilder();
		StringBuilder dsParametros1 = new StringBuilder();
		StringBuilder dsParametros2 = new StringBuilder();
		
		nomeRelatorio.append("Lançamentos pendentes - Regiao Ministro: ")
				.append("null".equals(request.getParameter("dsRegiao"))? "Todas": request.getParameter("dsRegiao"));

		dsParametros1.append("Depto.: ")
				.append("null".equals(request.getParameter("dsDepartamento"))? "Todos": request.getParameter("dsDepartamento"))
				.append("\tIgreja: ")
				.append("null".equals(request.getParameter("dsIgreja"))? "Todas": request.getParameter("dsIgreja"))
				.append("\tCargo: ")
				.append("null".equals(request.getParameter("dsCargo"))? "Todos": request.getParameter("dsCargo")) ;
		
		dsParametros2.append("Conta: ")
				.append("null".equals(request.getParameter("dsPlanoConta"))? "Todas": request.getParameter("dsPlanoConta"))
				.append("\tContribuinte: ")
				.append("null".equals(request.getParameter("nmMinistro"))? "Todos": request.getParameter("nmMinistro"))
				.append("\tSituação: ")
				.append(dsSituacao);
		
		StringBuilder sqlComplemento = new StringBuilder();
		if(!"null".equals(request.getParameter("dsTpLanSelecionados"))) {
			sqlComplemento.append(" and tpl.tpl_sq_tipolancamento in");
			sqlComplemento.append(request.getParameter("dsTpLanSelecionados"));
			sqlComplemento.append(" ");
		} else {
			sqlComplemento.append("");
		}
		
		Map<String, Object> parametros = new HashMap<>();

		parametros.put("url-logo", urlLogo);
		parametros.put("sqDepartamento", "null".equals(request.getParameter("sqDepartamento"))? 0L : Long.parseLong(request.getParameter("sqDepartamento")));
		parametros.put("sqRegiao", "null".equals(request.getParameter("sqRegiao"))? 0L : Long.parseLong(request.getParameter("sqRegiao")));
		parametros.put("sqIgreja","null".equals(request.getParameter("sqIgreja"))? 0L: Long.parseLong(request.getParameter("sqIgreja")));
		parametros.put("sqCargo", "null".equals(request.getParameter("sqCargo"))? 0L: Long.parseLong(request.getParameter("sqCargo")));
		parametros.put("sqMinistro", "null".equals(request.getParameter("sqMinistro"))? 0L: Long.parseLong(request.getParameter("sqMinistro")));
		parametros.put("sqPlanoConta", "null".equals(request.getParameter("sqPlanoConta"))? 0L: Long.parseLong(request.getParameter("sqPlanoConta")));
		parametros.put("dsTpLanSelecionados", sqlComplemento.toString());
		parametros.put("cdSituacaoMinistro","null".equals(request.getParameter("cdSituacao"))? 9: Integer.parseInt(request.getParameter("cdSituacao")));
		parametros.put("sqConvencao", 1L);
		parametros.put("SUBREPORT_DIR", "/relatorios/");
		parametros.put("nome-relatorio", nomeRelatorio.toString());
		parametros.put("ds-parametros1", dsParametros1.toString());
		parametros.put("ds-parametros2", dsParametros2.toString());
	
		ExecutorRelatorio executor = new ExecutorRelatorio("/relatorios/financeiroEntradaPendente.jasper", response, request, parametros, "financeiroEntradaPendente", request.getParameter("tpRelatorio"));

		Session session = manager.unwrap(Session.class);
		session.doWork(executor);

		if(!executor.isRelatorioGerado()) {
			response.getWriter().append("não há dados para exibir no relatório. RELATÓRIO DE LANÇAMENTOS PENDENTES \t").append(request.getContextPath());
		}
	}


}
