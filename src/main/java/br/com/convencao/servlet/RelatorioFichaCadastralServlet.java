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


@WebServlet("/relatorioFichaCadastral")
public class RelatorioFichaCadastralServlet extends HttpServlet {
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
		
		// Obter variável de ambiente para localizar onde encontra as fotos
		String pathFoto = System.getenv("HOME") + "/.ministrofotos/fotos-ministro/";
	
		Map<String, Object> parametros = new HashMap<>();
		
		parametros.put("sqMinistro", Long.parseLong(request.getParameter("sqMinistro")));
		parametros.put("url-logo", urlLogo);
		parametros.put("path_foto", pathFoto);
		parametros.put("SUBREPORT_DIR", "/relatorios/");
		
		ExecutorRelatorio executor = new ExecutorRelatorio("/relatorios/fichaCadastral.jasper", response, request, parametros, "fichaCadastral", "pdf");
		
		Session session = manager.unwrap(Session.class);
		session.doWork(executor);
		
		if(!executor.isRelatorioGerado()) {
			response.getWriter().append("não há dados para exibir no relatório. FICHA CADASTRAL \t ").append(request.getContextPath());
		}
		
	}

}
