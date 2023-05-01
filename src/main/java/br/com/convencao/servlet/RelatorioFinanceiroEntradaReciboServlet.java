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

@WebServlet("/relatorioFinanceiroEntradaRecibo")
public class RelatorioFinanceiroEntradaReciboServlet extends HttpServlet {
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
			
			Integer cdOrdem = Integer.parseInt(request.getParameter("cdOrdem"));
			String dsOrdenacao = "";

			switch(cdOrdem) {
				case 1: dsOrdenacao = "Recibo"; break;
				case 2: dsOrdenacao = "Data Pagamento"; break;
				case 3: dsOrdenacao = "Contribuinte"; break;
				case 4: dsOrdenacao = "Departamento"; break;
				default: dsOrdenacao = "???";
			}
			
			
			StringBuilder nomeRelatorio = new StringBuilder();
			StringBuilder dsParametros1 = new StringBuilder();
			
			nomeRelatorio.append("Entradas por Recibo: ")
						.append("null".equals(request.getParameter("dsRegiao"))? "Todas": request.getParameter("dsRegiao"));

			dsParametros1.append("Depto.: ")
			.append("null".equals(request.getParameter("dsDepartamento"))? "Todos": request.getParameter("dsDepartamento"))
			.append("\tData Pgto.: ")
			.append("".equals(request.getParameter("dtInicio").trim())? "__/__/____": request.getParameter("dtInicio"))
			.append(" à ")
			.append("".equals(request.getParameter("dtFim").trim())? "__/__/____": request.getParameter("dtFim"))
			.append("\tOrdem: ")
			.append(dsOrdenacao);
			
			// Formatar datas
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			Date dataInicio = null;
			Date dataFim = null;

			if(!"".equalsIgnoreCase(request.getParameter("dtInicio"))) {
				dataInicio = sdf.parse(request.getParameter("dtInicio"));
			} 

			if(!"".equalsIgnoreCase(request.getParameter("dtFim"))) {
				dataFim = sdf.parse(request.getParameter("dtFim"));
			} 
			
			Map<String, Object> parametros = new HashMap<>();

			parametros.put("url-logo", urlLogo);
			parametros.put("sqDepartamento", "null".equals(request.getParameter("sqDepartamento"))? null: Long.parseLong(request.getParameter("sqDepartamento")));
			parametros.put("sqRegiao", "null".equals(request.getParameter("sqRegiao"))? null: Long.parseLong(request.getParameter("sqRegiao")));
			if(dataInicio != null) parametros.put("dtInicio", new java.util.Date(dataInicio.getTime()));
			if(dataFim != null) parametros.put("dtFim", new java.util.Date(dataFim.getTime()));
			parametros.put("cdOrdem", Integer.parseInt(request.getParameter("cdOrdem")));
			parametros.put("SUBREPORT_DIR", "/relatorios/");
			parametros.put("nome-relatorio", nomeRelatorio.toString());
			parametros.put("ds-parametros1", dsParametros1.toString());
//			parametros.put(JRParameter.REPORT_LOCALE, new Locale("pt", "BR"));



			ExecutorRelatorio executor = new ExecutorRelatorio("/relatorios/financeiroEntradaRecibo.jasper", response, request, parametros, "financeiroEntradaRecibo", request.getParameter("tpRelatorio"));

			Session session = manager.unwrap(Session.class);
			session.doWork(executor);

			if(!executor.isRelatorioGerado()) {
				response.getWriter().append("não há dados para exibir no relatório. Relatório de Entradas por Recibo \t").append(request.getContextPath());
			}

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}


}
