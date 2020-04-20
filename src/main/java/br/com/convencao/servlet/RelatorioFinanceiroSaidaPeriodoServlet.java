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

@WebServlet("/relatorioFinanceiroSaidaPeriodo")
public class RelatorioFinanceiroSaidaPeriodoServlet extends HttpServlet {
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

		Integer cdOrdem = Integer.parseInt(request.getParameter("cdOrdem"));
		String dsOrdenacao = "";

		switch(cdOrdem) {
			case 1: dsOrdenacao = "Cadastro"; break;
			case 2: dsOrdenacao = "Data"; break;
			case 3: dsOrdenacao = "Recibo"; break;
			case 4: dsOrdenacao = "Favorecido"; break;
			default: dsOrdenacao = "???";
		}

		StringBuilder nomeRelatorio = new StringBuilder();
		StringBuilder dsParametros1 = new StringBuilder();

		nomeRelatorio.append("Saídas por Período - Região: ")
				.append("null".equals(request.getParameter("dsRegiao"))? "Todas": request.getParameter("dsRegiao"));

		dsParametros1.append("Período: ")
				.append(request.getParameter("dsPeriodo"))
				.append("\tFechado em: ")
				.append("".equals(request.getParameter("dsDtFechado").trim())? "__/___/____" : request.getParameter("dsDtFechado"))
				.append("\tConta: ")
				.append("null".equals(request.getParameter("dsConta"))? "Todas": request.getParameter("dsConta") )
				.append("\tOrdem: ")
				.append(dsOrdenacao);
		

		Map<String, Object> parametros = new HashMap<>();

		parametros.put("url-logo", urlLogo);
		parametros.put("sqRegiao", "null".equals(request.getParameter("sqRegiao"))? 0L: Long.parseLong(request.getParameter("sqRegiao")));
		parametros.put("sqResumo", "null".equals(request.getParameter("sqResumo"))? 0L: Long.parseLong(request.getParameter("sqResumo")));		
		parametros.put("sqPlanoConta", "null".equals(request.getParameter("sqPlanoConta"))? 0L: Long.parseLong(request.getParameter("sqPlanoConta")));
		parametros.put("SUBREPORT_DIR", "/relatorios/");
		parametros.put("sqConvencao", 1L);
		parametros.put("nome-relatorio", nomeRelatorio.toString());
		parametros.put("ds-parametros1", dsParametros1.toString());
		parametros.put("cdOrdem", cdOrdem);



		ExecutorRelatorio executor = new ExecutorRelatorio("/relatorios/financeiroSaidaPeriodo.jasper", response, request, parametros, "financeiroSaidaPeriodo", request.getParameter("tpRelatorio"));

		Session session = manager.unwrap(Session.class);
		session.doWork(executor);

		if(!executor.isRelatorioGerado()) {
			response.getWriter().append("não há dados para exibir no relatório. RELATÓRIO Saídas por Período \t").append(request.getContextPath());
		}
	}


}
