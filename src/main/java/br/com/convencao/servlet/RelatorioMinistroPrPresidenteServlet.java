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

import br.com.convencao.bo.RegiaoBO;
import br.com.convencao.model.Regiao;
import br.com.convencao.util.report.ExecutorRelatorio;


@WebServlet("/relatorioMinistroPrPresidente")
public class RelatorioMinistroPrPresidenteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;
	
	@Inject
	private RegiaoBO regiaoBO;
		
	private Regiao regiao;

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
		String param_cdSituacao = request.getParameter("cdSituacao");
		String param_situacao = "";
		String param_regiao = "";
		
		Long sqConvencao = 		0L;
		Long sqRegiao = 		StringUtils.isNotBlank(param_sqRegiao)? Long.parseLong(param_sqRegiao):  0L;
		Integer cdSituacao =	StringUtils.isNotBlank(param_cdSituacao)? Integer.parseInt(param_cdSituacao): 0;
		
		StringBuilder sqlComplemento1 = new StringBuilder();
		StringBuilder sqlComplemento2 = new StringBuilder();
		
		boolean flWhere = false;
		
		if(sqRegiao > 0) {
			sqlComplemento1.append(" AND igr.rgn_sq_regiao = " + sqRegiao);
			
			this.regiao = this.regiaoBO.findByPrimaryKey(sqRegiao);
			param_regiao = "Regiao: " + this.regiao.getDsRegiao();
			sqConvencao = this.regiao.getConvencao().getSqConvencao();
		} else {
			param_regiao = "Regiao: Todas";
			sqConvencao = sqRegiao * -1;
		}
		
		
		if(cdSituacao == 1) {
			if(!flWhere) {
				sqlComplemento2.append(" WHERE min.min_dt_excluido is null");
				flWhere = true;
			}else {
				sqlComplemento2.append(" AND min.min_dt_excluido is null");
			}			
			param_situacao = "Pastores Presidentes ATIVOS";
		} else if(cdSituacao == 2) {
			if(!flWhere) {
				sqlComplemento2.append(" WHERE min.min_dt_excluido is not null");
				flWhere = true;
			}else {
				sqlComplemento2.append(" AND min.min_dt_excluido is not null");
			}
			param_situacao = "Pastores Presidentes INATIVOS";
		}
		 
		sqlComplemento2.append(" ORDER BY rgn.rgn_ds_regiao, min.min_nm_nome");
		
		
		System.out.println("Sql1: " + sqlComplemento1.toString());
		System.out.println("Sql2: " + sqlComplemento2.toString());
		
		Map<String, Object> parametros = new HashMap<>();
		
		parametros.put("urlLogo", urlLogo);
		parametros.put("sqConvencao", sqConvencao);
		parametros.put("param_regiao", param_regiao);
		parametros.put("param_situacao", param_situacao);
		parametros.put("param_sqlComplemento1", sqlComplemento1.toString());
		parametros.put("param_sqlComplemento2", sqlComplemento2.toString());
		parametros.put("SUBREPORT_DIR", "/relatorios/");
		

		
		ExecutorRelatorio executor = new ExecutorRelatorio("/relatorios/ministroPrPresidente.jasper", response, request, parametros, "ministroPrPresidente", request.getParameter("tpRelatorio"));
		
		Session session = manager.unwrap(Session.class);
		session.doWork(executor);
		
		if(!executor.isRelatorioGerado()) {
			response.getWriter().append("Não há dados para exibir no relatório. RELATÓRIO DE PASTORES PRESIDENTES \t").append(request.getContextPath());
		}
		
	}

}
