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

import br.com.convencao.util.Uteis;
import br.com.convencao.util.report.ExecutorRelatorio;

@WebServlet("/relatorioFinanceiroEntradaFormaRecebimento")
public class RelatorioFinanceiroEntradaFormaRecebimentoServlet extends HttpServlet {
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
		String sqlOrdenacao = "";
		
		switch(cdOrdem) {
			case 1: dsOrdenacao = "Recibo"; 
					sqlOrdenacao = " order by cdNsu";
					break;
			case 2: dsOrdenacao = "Rg";
					sqlOrdenacao = " order by cdMinistro";
					break;
			case 3: dsOrdenacao = "Contribuingte"; 
					sqlOrdenacao = " order by nmContribuinte";
					break;						
			case 4: dsOrdenacao = "Data"; 
					sqlOrdenacao = " order by dtPagamento";
					break;
			case 5: dsOrdenacao = "Forma Pgto"; 
					sqlOrdenacao = " order by dsFormaPagamento";
					break;
			default: dsOrdenacao = "Não identificado";
		}

		StringBuilder dsParametros1 = new StringBuilder();
		dsParametros1.append("Depto.: ")
				.append(request.getParameter("dsDepartamento"))
				.append("   Região: ")
				.append(request.getParameter("dsRegiaoMinistro"))
				.append("   Período: ")
				.append("null".equals(request.getParameter("dtInicio").trim())? "__/__/____": Uteis.LocalDateParaString(Uteis.StringParaLocalDate(request.getParameter("dtInicio"))))
				.append(" à ")
				.append("null".equals(request.getParameter("dtFim").trim())? "__/__/____": Uteis.LocalDateParaString(Uteis.StringParaLocalDate(request.getParameter("dtFim"))))
				.append("   Ordenação: ")
				.append(dsOrdenacao );
		
		StringBuilder dsParametros2 = new StringBuilder();
		dsParametros2.append("Igreja: ")
				.append(request.getParameter("dsIgreja"))
				.append("    Cargo: ")
				.append(request.getParameter("dsCargo"))
				.append("    Contrib.: ")
				.append(request.getParameter("nmMinistro"));
				
		StringBuilder sqlComplemento = new StringBuilder();
		sqlComplemento.append("0".equals(request.getParameter("sqDepartamento").trim())? "": " and ministro.sqDepartamento = " + request.getParameter("sqDepartamento"))
					.append("0".equals(request.getParameter("sqRegiaoRecebimento").trim())? "": " and ministro.sqRegiaoReceb = " + request.getParameter("sqRegiaoRecebimento"))
					.append("0".equals(request.getParameter("sqRegiaoMinistro").trim())? "": " and ministro.sqRegiaoMin = " + request.getParameter("sqRegiaoMinistro"))
					.append("0".equals(request.getParameter("sqIgreja").trim())? "": " and ministro.sqIgreja_ministro = " + request.getParameter("sqIgreja"))
					.append("0".equals(request.getParameter("sqCargo").trim())? "": " and ministro.sqCargo = " + request.getParameter("sqCargo"))
					.append("0".equals(request.getParameter("sqMinistro").trim())? "": " and ministro.sqMinistro = " + request.getParameter("sqMinistro"))
					.append("null".equals(request.getParameter("dtInicio"))? "": " and ministro.dtPagamento >= '" + request.getParameter("dtInicio") + "'")
					.append("null".equals(request.getParameter("dtFim"))? "": " and ministro.dtPagamento <= '" + request.getParameter("dtFim") + "'");
		

		Map<String, Object> parametros = new HashMap<>();

		parametros.put("url-logo", urlLogo);
		parametros.put("sqDepartamento", Long.parseLong(request.getParameter("sqDepartamento")));
		parametros.put("ds-parametros1", dsParametros1.toString());
		parametros.put("ds-parametros2", dsParametros2.toString());
		parametros.put("nome-relatorio", "Entradas forma recebimento - Região Receb.: " + request.getParameter("dsRegiaoRecebimento"));
		parametros.put("sql-complemento", sqlComplemento.toString() + sqlOrdenacao);
		parametros.put("SUBREPORT_DIR", "/relatorios/");

		ExecutorRelatorio executor = new ExecutorRelatorio("/relatorios/financeiroEntradaFormaRecebimento.jasper", response, request, parametros, "financeiroEntradaFormaRecebimento", request.getParameter("tpRelatorio"));
		
		Session session = manager.unwrap(Session.class);
		session.doWork(executor);

		if(!executor.isRelatorioGerado()) {
			response.getWriter().append("Não há dados para exibir no relatório. RELATÓRIO DE ENTRADAS POR TIPO DE RECEBIMENTO \t").append(request.getContextPath());
		}
	}


}
