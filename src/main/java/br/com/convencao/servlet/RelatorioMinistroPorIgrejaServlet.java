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

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;

import br.com.convencao.util.report.ExecutorRelatorio;

@WebServlet("/relatorioMinistroPorIgreja")
public class RelatorioMinistroPorIgrejaServlet extends HttpServlet {

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
		String param_sqRegiao = request.getParameter("sqRegiao");
		String param_sqIgreja = request.getParameter("sqIgreja");
		String param_sqMinistro = request.getParameter("sqMinistro");
		String param_relatorio = ""; 
		
		Long sqConvencao = 1L;
		Long sqRegiao = StringUtils.isNotBlank(param_sqRegiao)? Long.parseLong(param_sqRegiao):  0L;
		Long sqMinistro = StringUtils.isNotBlank(param_sqMinistro)? Long.parseLong(param_sqMinistro):  0L;
		
		StringBuilder sqlComplemento1 = new StringBuilder();
		sqlComplemento1.append("");
		
		Map<String, Object> parametros = new HashMap<>();
		
		parametros.put("urlLogo", urlLogo);
		parametros.put("sqConvencao", sqConvencao);
		parametros.put("param_sqlComplemento1", sqlComplemento1.toString());
		parametros.put("param_relatorio", param_relatorio);
		parametros.put("SUBREPORT_DIR", "/relatorios/");
		
		ExecutorRelatorio executor = new ExecutorRelatorio("/relatorios/ministroPorIgreja.jasper", response, request, parametros, "ministroPorIgreja", request.getParameter("tpRelatorio"));
		
		Session session = manager.unwrap(Session.class);
		session.doWork(executor);
		
		if(!executor.isRelatorioGerado()) {
			response.getWriter().append("Não há dados para exibir no relatório. RELATÓRIO DE PASTORES MINISTROS POR IGREJA \t").append(request.getContextPath());
		}
		
		System.out.println("-----------------------------------");
		System.out.println("sqRegiao   : " + param_sqRegiao);
		System.out.println("sqIgreja   : " + param_sqIgreja);
		System.out.println("sqMinistro : " + param_sqMinistro);
		System.out.println("-----------------------------------");
		
	}
}
