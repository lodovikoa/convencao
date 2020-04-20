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

@WebServlet("/relatorioFinanceiroEntradaPeriodo")
public class RelatorioFinanceiroEntradaPeriodoServlet extends HttpServlet {
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
			case 4: dsOrdenacao = "Contribuinte"; break;
			case 5: dsOrdenacao = "Regiao"; break;
			default: dsOrdenacao = "???";
		}


		StringBuilder dsParametros = new StringBuilder();
		dsParametros.append("Período: ")
				.append(request.getParameter("dsPeriodo"))
				.append("  Fechado em: ")
				.append("".equalsIgnoreCase(request.getParameter("dsDataFechado").trim())? "__/__/____": request.getParameter("dsDataFechado"))
				.append(" Conta: ")
				.append("".equalsIgnoreCase(request.getParameter("dsConta"))? "Todas": request.getParameter("dsConta") )
				.append("   Ordenação: ")
				.append(dsOrdenacao );


		Map<String, Object> parametros = new HashMap<>();

		parametros.put("url-logo", urlLogo);
		parametros.put("sqResumo", Long.parseLong(request.getParameter("sqResumo")));
		parametros.put("ds-parametros", dsParametros.toString());
		parametros.put("nome-relatorio", "Entradas por período - Região: " + request.getParameter("dsRegiao"));
		parametros.put("sqPlanoConta", "null".equals(request.getParameter("sqPlanoConta"))? 0L: Long.parseLong(request.getParameter("sqPlanoConta")));
		parametros.put("cdOrdem", cdOrdem);
		parametros.put("SUBREPORT_DIR", "/relatorios/");


		
		ExecutorRelatorio executor = new ExecutorRelatorio("/relatorios/financeiroEntradaPeriodo.jasper", response, request, parametros, "financeiroEntradaPeriodo", request.getParameter("tpRelatorio"));
		
		Session session = manager.unwrap(Session.class);
		session.doWork(executor);

		if(!executor.isRelatorioGerado()) {
			response.getWriter().append("Não há dados para exibir no relatório. RELATÓRIO DE ENTRADAS POR PERÍODO \t").append(request.getContextPath());
		}
	}


}
