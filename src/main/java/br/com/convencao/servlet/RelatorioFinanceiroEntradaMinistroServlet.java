package br.com.convencao.servlet;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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

@WebServlet("/relatorioFinanceiroEntradaMinistro")
public class RelatorioFinanceiroEntradaMinistroServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;


	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			// Pegar a url usada para chegar aki
			String urlPrincipal = ((HttpServletRequest)request).getRequestURL().toString();

			// Pegar a url parte. Esse servlet
			String urlServlet = ((HttpServletRequest)request).getServletPath().toString();

			// URL para os parametros de url-logo
			String urlLogo = urlPrincipal.replace(urlServlet, "/javax.faces.resource/images/logo02.jpg.xhtml?ln=convencao");

			StringBuilder nomeRelatorio = new StringBuilder();
			StringBuilder dsParametros1 = new StringBuilder();
			StringBuilder dsParametros2 = new StringBuilder();
			
			nomeRelatorio.append("Entradas por Ministro - Região Financeiro: ")
						.append("null".equals(request.getParameter("dsRegiaoFinanceiro"))? "Todas": request.getParameter("dsRegiaoFinanceiro"));
			
			dsParametros1.append("Depto.: ")
					.append("null".equals(request.getParameter("dsDepartamento"))? "Todos": request.getParameter("dsDepartamento"))
					.append("\tIgreja: ")
					.append("null".equals(request.getParameter("dsIgreja"))? "Todas": request.getParameter("dsIgreja"))
					.append("\tCargo: ")
					.append("null".equals(request.getParameter("dsCargo"))? "Todos": request.getParameter("dsCargo"))
					.append("\tRegião Ministro:")
					.append("null".equals(request.getParameter("dsRegiaoMinistro"))? "Todas": request.getParameter("dsRegiaoMinistro"));;
			
			dsParametros2.append("Contrib.: ")
					.append("null".equals(request.getParameter("dsContribuinte"))? "Todos": request.getParameter("dsContribuinte"))
					.append("\tCta Cont.: ")
					.append("null".equals(request.getParameter("dsConta"))? "Todos": request.getParameter("dsConta"))
					.append("\tDt Pgto.: ")
					.append("".equals(request.getParameter("dtInicio").trim())? "__/__/____": request.getParameter("dtInicio"))
					.append(" à ")
					.append("".equals(request.getParameter("dtFim").trim())? "__/__/____": request.getParameter("dtFim"));
			
			// Formatar datas
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			Date dataInicio = null;
			Date dataFim = null;
			
			if(!"".equalsIgnoreCase(request.getParameter("dtInicio"))) {
				dataInicio = sdf.parse(request.getParameter("dtInicio"));
			} 

			if(!"".equalsIgnoreCase(request.getParameter("dtFim"))) {
				dataFim= sdf.parse(request.getParameter("dtFim"));
			}
			
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
			parametros.put("sqDepartamento", "null".equals(request.getParameter("sqDepartamento"))? 0L: Long.parseLong(request.getParameter("sqDepartamento")));
			parametros.put("sqRegiaoFin", "null".equals(request.getParameter("sqRegiaoFinanceiro"))? 0L: Long.parseLong(request.getParameter("sqRegiaoFinanceiro")));
			parametros.put("sqRegiaoMin", "null".equals(request.getParameter("sqRegiaoMinistro"))? 0L: Long.parseLong(request.getParameter("sqRegiaoMinistro")));
			parametros.put("sqIgreja", "null".equals(request.getParameter("sqIgreja"))? 0L: Long.parseLong(request.getParameter("sqIgreja")));		
			parametros.put("sqCargo", "null".equals(request.getParameter("sqCargo"))? 0L: Long.parseLong(request.getParameter("sqCargo")));
			parametros.put("sqMinistro", "null".equals(request.getParameter("sqMinistro"))? 0L: Long.parseLong(request.getParameter("sqMinistro")));
			parametros.put("sqContaContabil", "null".equals(request.getParameter("sqPlanoConta"))? 0L: Long.parseLong(request.getParameter("sqPlanoConta")));
			parametros.put("dsTpLanSelecionados", sqlComplemento.toString());
			if(dataInicio != null) parametros.put("dtPagamentoInicio", new java.sql.Date(dataInicio.getTime()));
			if(dataFim != null) parametros.put("dtPagamentoFim",    new java.sql.Date(dataFim.getTime()));
			parametros.put("sqConvencao", 1L);
			parametros.put("SUBREPORT_DIR", "/relatorios/");
			parametros.put("nome-relatorio", nomeRelatorio.toString());
			parametros.put("ds-parametros1", dsParametros1.toString());
			parametros.put("ds-parametros2", dsParametros2.toString());

			ExecutorRelatorio executor = new ExecutorRelatorio("/relatorios/financeiroEntradaMinistro.jasper", response, request, parametros, "relatorioFinanceiroEntradaMinistro", request.getParameter("tpRelatorio"));

			Session session = manager.unwrap(Session.class);
			session.doWork(executor);

			if(!executor.isRelatorioGerado()) {
				response.getWriter().append("Não há dados para exibir no relatório. RELATÓRIO DE ENTRADAS POR MINISTRO \t").append(request.getContextPath());
			}

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

	}


}
