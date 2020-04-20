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

import br.com.convencao.bo.ReciboBO;
import br.com.convencao.model.Igreja;
import br.com.convencao.model.to.ReciboCpl;
import br.com.convencao.util.Extenso;
import br.com.convencao.util.Uteis;
import br.com.convencao.util.report.ExecutorRelatorio;

@WebServlet("/relatorioFinanceiroRecibo")
public class RelatorioFinanceiroReciboServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;

	@Inject
	private ReciboBO reciboBO;


	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Pegar a url usada para chegar aki
		String urlPrincipal = ((HttpServletRequest)request).getRequestURL().toString();

		// Pegar a url parte. Esse servlet
		String urlServlet = ((HttpServletRequest)request).getServletPath().toString();

		// URL para os parametros de url-logo
		String urlLogo = urlPrincipal.replace(urlServlet, "/javax.faces.resource/images/logo02.jpg.xhtml?ln=convencao");
		String urlAssinaturaTesoureiro = urlPrincipal.replace(urlServlet, "/javax.faces.resource/images/assinatura_tesoureiro.png.xhtml?ln=convencao");
		String param_sqRecibo = request.getParameter("sqRecibo");
		String param_tpRecibo = request.getParameter("tpRecibo") == null? "0": request.getParameter("tpRecibo");

		ReciboCpl reciboCpl = reciboBO.buscarDadosRecibo(Long.parseLong(param_sqRecibo));
		Igreja igreja = null;
		StringBuilder corpoRecibo = new StringBuilder();
		Long sqReciboLancamento = 0L;

		// Recibo para lançamento de Ministro
		if(param_tpRecibo.equals("1")) {
			sqReciboLancamento = Long.parseLong(param_sqRecibo);
			corpoRecibo.append("Recebemos da Igreja Evangélica ")
					.append(reciboCpl.getDsIgreja())
					.append(", CNPJ ")
					.append(reciboCpl.getDsCnpjFormatado())
					.append(", ")
					.append(reciboCpl.getDsTituloCargo())
					.append(" ")
					.append(reciboCpl.getDsNomeMin());
		} else	
			// Recibo para lançamento de Igreja
			if(param_tpRecibo.equals("2")) {
				igreja = reciboBO.buscarDadosIgrejaDoRecibo(Long.parseLong(param_sqRecibo));
				
				corpoRecibo.append("Recebemos da Igreja Evangélica ")
						.append(igreja.getDsIgreja())
						.append(", CNPJ ")
						.append(igreja.getDsCnpj().isEmpty()? "": Uteis.cnpjMask(igreja.getDsCnpj()));
			} else
				// Recibo para lançamento de Outros
				if(param_tpRecibo.equals("3")) {

					corpoRecibo.append("Recebemos de ")
						.append(reciboCpl.getNmOutros());
					
				}

		
		// Complementando o recibo com dados comuns aos tipos de recibo
		corpoRecibo.append(", a importância de ")
			.append(reciboCpl.getVlTotalPagamentoFormatado())
			.append(" (")
			.append(new Extenso(reciboCpl.getVlToltalPagamento()).toString())
			.append(") referente a: ")
			.append(reciboCpl.getDsHistorico().toString());

		StringBuilder nsuRecibo = new StringBuilder();
		nsuRecibo.append("Recibo N")
			.append((char) 186)
			.append(" ")
			.append(reciboCpl.getCdNsu());

		StringBuilder vlrRecibo = new StringBuilder();
		vlrRecibo.append("Valor R$ ")
		.append(reciboCpl.getVlTotalPagamentoFormatado());


		Map<String, Object> parametros = new HashMap<>();

		parametros.put("url-logo", urlLogo);
		parametros.put("url-assinatura", urlAssinaturaTesoureiro);
		parametros.put("linha-assinatura", "Tesoureiro CONFRATERES - Regiao: " + reciboCpl.getDsRegiao());
		parametros.put("corpo-recibo", corpoRecibo.toString());
		parametros.put("sqReciboLancamento", sqReciboLancamento);
		parametros.put("sqReciboFormaPagamento", Long.parseLong(param_sqRecibo));
		parametros.put("nsu-recibo", nsuRecibo.toString());
		parametros.put("vlr-recibo", vlrRecibo.toString());
		parametros.put("data-recibo", Uteis.dataExtenso(reciboCpl.getDtRecibo()));
		parametros.put("SUBREPORT_DIR", "/relatorios/");

		ExecutorRelatorio executor = new ExecutorRelatorio("/relatorios/financeiroRecibo.jasper", response, request, parametros, "financeiroRecibo", request.getParameter("tpRelatorio"));

		Session session = manager.unwrap(Session.class);
		session.doWork(executor);

		if(!executor.isRelatorioGerado()) {
			response.getWriter().append("não há dados para exibir no relatório. RECIBO \t").append(request.getContextPath());
		}
	}


}
