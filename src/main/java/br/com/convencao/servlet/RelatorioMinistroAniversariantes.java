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


@WebServlet("/relatorioMinistroAniversariantes")
public class RelatorioMinistroAniversariantes extends HttpServlet {
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

		// Parametros
		String param_nnMesInicio = request.getParameter("nnMesInicio");
		String param_dsMesInicio = request.getParameter("dsMesInicio");
		String param_nnMesFim = request.getParameter("nnMesFim");
		String param_dsMesFim = request.getParameter("dsMesFim");
		String param_dsParametros = param_dsMesInicio.equalsIgnoreCase(param_dsMesFim)? "Aniversariantes de " + param_dsMesInicio: "Aniversariantes de "  + param_dsMesInicio + " a " + param_dsMesFim;
		
		Long sqConvencao = 		1L;

		Map<String, Object> parametros = new HashMap<>();
		
		parametros.put("mesAnivInicio", Integer.parseInt(param_nnMesInicio));
		parametros.put("mesAnivFim", Integer.parseInt(param_nnMesFim));
		parametros.put("url-logo", urlLogo);
		parametros.put("sqConvencao", sqConvencao);
		parametros.put("nome-relatorio", "Aniversariantes");
		parametros.put("ds-parametros", param_dsParametros);
		parametros.put("SUBREPORT_DIR", "/relatorios/");
				
		ExecutorRelatorio executor = new ExecutorRelatorio("/relatorios/ministroAniversariantes.jasper", response, request, parametros, "ministrosAniversariantes", request.getParameter("tpRelatorio"));
		
		Session session = manager.unwrap(Session.class);
		session.doWork(executor);
		
		if(!executor.isRelatorioGerado()) {
			response.getWriter().append("Não há dados para exibir no relatório. RELATÓRIO DE MINISTROS ANIVERSARIANTES \t").append(request.getContextPath());
		}
		
	}

}
