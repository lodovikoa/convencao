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

@WebServlet("/relatorioFinanceiroResumo")
public class RelatorioFinanceiroResumoServlet extends HttpServlet {
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
			
		StringBuilder dsCabecalho = new StringBuilder();
		String dsPeriodotemp = request.getParameter("dsPeriodo").trim().length() > 20? request.getParameter("dsPeriodo"): request.getParameter("dsPeriodo") + " __/__/____";
		dsCabecalho.append("Período: ")
				.append(dsPeriodotemp)
				.append(" - Fechado em: ")
				.append(request.getParameter("dtFechado") == ""? "Em aberto": request.getParameter("dtFechado"))
				.append(" - Regiao: ")
				.append(request.getParameter("dsRegiao"));

		Map<String, Object> parametros = new HashMap<>();

		parametros.put("url-logo", urlLogo);
		parametros.put("sqResumo", Long.parseLong(request.getParameter("sqResumo")));
		parametros.put("ds-cabecalho", dsCabecalho.toString());
		parametros.put("sqConvencao", 1L);
		parametros.put("SUBREPORT_DIR", "/relatorios/");
		parametros.put("linha-assinatura", "Tesoureiro CONFRATERES - Regiao: " + request.getParameter("dsRegiao"));
	

		ExecutorRelatorio executor = new ExecutorRelatorio("/relatorios/financeiroResumo.jasper", response, request, parametros, "financeiroResumo", request.getParameter("tpRelatorio"));

		Session session = manager.unwrap(Session.class);
		session.doWork(executor);

		if(!executor.isRelatorioGerado()) {
			response.getWriter().append("não há dados para exibir no relatório. RELATÓRIO DE RESUMO \t").append(request.getContextPath());
		}
	}


}
