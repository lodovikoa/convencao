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

import br.com.convencao.bo.IgrejaBO;
import br.com.convencao.bo.RegiaoBO;
import br.com.convencao.model.Igreja;
import br.com.convencao.model.Regiao;
import br.com.convencao.util.report.ExecutorRelatorio;

@WebServlet("/relatorioMinistroPorIgreja")
public class RelatorioMinistroPorIgrejaServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private EntityManager manager;
	
	@Inject
	private RegiaoBO regiaoBO;
	
	@Inject
	private IgrejaBO igrejaBO;
	
	private Igreja igreja;
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
		String param_sqIgreja = request.getParameter("sqIgreja");
		String param_sqMinistro = request.getParameter("sqMinistro");
		
		 
		
		Long sqConvencao = 1L;
		Long sqRegiao = StringUtils.isNotBlank(param_sqRegiao)? Long.parseLong(param_sqRegiao):  0L;
		Long sqIgreja = StringUtils.isNotBlank(param_sqIgreja)? Long.parseLong(param_sqIgreja):  0L;
		Long sqMinistro = StringUtils.isNotBlank(param_sqMinistro)? Long.parseLong(param_sqMinistro):  0L;
		
		StringBuilder sqlComplemento1 = new StringBuilder();
		StringBuilder param_relatorio = new StringBuilder();
		
		param_relatorio.append("Parametros: ");
		if(sqRegiao > 0) {
			this.regiao = this.regiaoBO.findByPrimaryKey(sqRegiao);
			sqConvencao = this.regiao.getConvencao().getSqConvencao();
			
			sqlComplemento1.append(" and igr.rgn_sq_regiao = " + sqRegiao);
			param_relatorio.append(" Região:  " + this.regiao.getDsRegiao());
		} else {
			param_relatorio.append(" Região: Todas ");
		}
		
		if(sqIgreja > 0) {
			this.igreja = igrejaBO.findByPrimaryKey(sqIgreja);
			sqlComplemento1.append(" and igr.igr_sq_igreja =  " + sqIgreja);
			param_relatorio.append(" - Igreja: " + igreja.getDsIgreja());
		} 
		
		if(sqMinistro > 0) {
			sqlComplemento1.append(" and presidente.sqPres = " + sqMinistro);
			if(sqIgreja <= 0) {
				igreja = igrejaBO.findByPorAtributo("ministro.sqMinistro"  , sqMinistro);
				param_relatorio.append(" - Igreja: " + igreja.getDsIgreja());
			}	
		} else if(sqIgreja <= 0){
			param_relatorio.append(" - Igreja: Todas");
		}
		
		Map<String, Object> parametros = new HashMap<>();
		parametros.put("urlLogo", urlLogo);
		parametros.put("sqConvencao", sqConvencao);
		parametros.put("param_sqlComplemento1", sqlComplemento1.toString());
		parametros.put("param_relatorio", param_relatorio.toString());
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
