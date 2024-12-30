package br.com.convencao.servlet;

import java.io.IOException;
import java.time.LocalDate;
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

import br.com.convencao.util.Uteis;
import br.com.convencao.util.report.ExecutorRelatorio;

@WebServlet("/relatorioFinanceiroCartaCobranca")
public class RelatorioFinanceiroCartaCobrancaServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		// Pegar a url usada para chegar aki
		String urlPrincipal = ((HttpServletRequest)request).getRequestURL().toString();
			
		// Pegar a url parte. Esse servlet
		String urlServlet = ((HttpServletRequest)request).getServletPath().toString();
			
		// URL para os parametros - url-logo
		String urlLogo = urlPrincipal.replace(urlServlet, "/javax.faces.resource/images/logo02.jpg.xhtml?ln=convencao");
			
		// URL para os parametros - url-assinatura
		String urlAssinatura = urlPrincipal.replace(urlServlet, "/javax.faces.resource/images/assinatura_tesoureiro.png.xhtml?ln=convencao");
			
		Map<String, Object> parametros = new HashMap<>();
			
		parametros.put("url-logo", urlLogo);
		parametros.put("url-assinatura", urlAssinatura);
		parametros.put("sqConvencao", 1L);
		parametros.put("sqRegiao", "-1".equals(request.getParameter("sqRegiao"))? 0L: Long.parseLong(request.getParameter("sqRegiao")));
		parametros.put("sqIgreja", "-1".equals(request.getParameter("sqIgreja"))? 0L: Long.parseLong(request.getParameter("sqIgreja")));
		parametros.put("nnConsiderarAnoAtual", "true".equals(request.getParameter("flConsiderarAnuidadeCorrente"))? 1: 0 );
		parametros.put("nome-tesoureiro", request.getParameter("nmTesoureiro"));
		parametros.put("cargo-tesoureiro", request.getParameter("cargoTesoureiro"));
		parametros.put("data-carta", "Cariacica ES, " + Uteis.dataExtenso(LocalDate.now(), false));
		parametros.put("SUBREPORT_DIR", "/relatorios/");
				
		ExecutorRelatorio executor = new ExecutorRelatorio("/relatorios/financeiroCartaCobranca.jasper", response, request, parametros, "relatorioFinanceiroCartaCobranca", "pdf");
				
		Session session = manager.unwrap(Session.class);
		session.doWork(executor);
		
			
		if(!executor.isRelatorioGerado()) {
			response.getWriter().append("Não há dados para exibir no relatório. EMISSÃO DE CARTAS DE COBRANÇA \t").append(request.getContextPath());
		}

	}
}
