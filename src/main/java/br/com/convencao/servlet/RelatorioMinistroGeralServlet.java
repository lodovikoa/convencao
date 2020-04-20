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

import br.com.convencao.bo.CargoBO;
import br.com.convencao.bo.DepartamentoBO;
import br.com.convencao.bo.IgrejaBO;
import br.com.convencao.bo.RegiaoBO;
import br.com.convencao.model.Cargo;
import br.com.convencao.model.Departamento;
import br.com.convencao.model.Igreja;
import br.com.convencao.model.Regiao;
import br.com.convencao.util.report.ExecutorRelatorio;


@WebServlet("/relatorioMinistroGeral")
public class RelatorioMinistroGeralServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;
	
	@Inject
	private RegiaoBO regiaoBO;
	
	@Inject
	private DepartamentoBO departamentoBO;
	
	@Inject
	private IgrejaBO igrejaBO;
	
	@Inject
	private CargoBO cargoBO;
	
	private Regiao regiao;
	private Departamento departamento;
	private Igreja igreja;
	private Cargo cargo;

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
		String param_sqDepartamento = request.getParameter("sqDepartamento");
		String param_sqCargo = request.getParameter("sqCargo");
		String param_cdSituacao = request.getParameter("cdSituacao");
		String param_nnOrdem = request.getParameter("nnOrdem");
		String param_situacao = "";
		String param_ordemExibicao = "";
		String param_departamento = "";
		String param_igreja = "";
		String param_cargo = "";
		String param_regiao = "";
		
		Long sqConvencao = 		0L;
		Long sqRegiao = 		StringUtils.isNotBlank(param_sqRegiao)? Long.parseLong(param_sqRegiao):  0L;
		Long sqIgreja =  		StringUtils.isNotBlank(param_sqIgreja)? Long.parseLong(param_sqIgreja):  0L;
		Long sqDepartamento = 	StringUtils.isNotBlank(param_sqDepartamento)? Long.parseLong(param_sqDepartamento): 0L;
		Long sqCargo =			StringUtils.isNotBlank(param_sqCargo)? Long.parseLong(param_sqCargo): 0L;
		Integer cdSituacao =	StringUtils.isNotBlank(param_cdSituacao)? Integer.parseInt(param_cdSituacao): 0;
		Integer nnOrdem =		StringUtils.isNotBlank(param_nnOrdem)? Integer.parseInt(param_nnOrdem): 0;
		
		StringBuilder sqlComplemento = new StringBuilder();
		boolean flWhere = false;
		
		if(sqRegiao > 0) {
			if(!flWhere) {
				sqlComplemento.append(" WHERE igr.rgn_sq_regiao = " + sqRegiao);
				flWhere = true;
			}
			this.regiao = this.regiaoBO.findByPrimaryKey(sqRegiao);
			param_regiao = "Regiao: " + this.regiao.getDsRegiao();
			sqConvencao = this.regiao.getConvencao().getSqConvencao();
		} else {
			param_regiao = "Regiao: Todas";
			sqConvencao = sqRegiao * -1;
		}
		
		if(sqDepartamento > 0) {
			if(!flWhere) {
				sqlComplemento.append(" WHERE min.dpt_sq_departamento = " + sqDepartamento);
				flWhere = true;
			}else {
				sqlComplemento.append(" AND min.dpt_sq_departamento = " + sqDepartamento);
			}
			
			this.departamento = departamentoBO.find(sqDepartamento);
			param_departamento = "Dpto: " + this.departamento.getDsReduzido();
		} else {
			param_departamento = "Dpto: Todos"; 
		}
		
		if(sqIgreja > 0) {
			if(!flWhere) {
				sqlComplemento.append(" WHERE min.igr_sq_igreja = " + sqIgreja);
				flWhere = true;
			}else {
				sqlComplemento.append(" AND min.igr_sq_igreja = " + sqIgreja);
			}
			
			this.igreja = this.igrejaBO.find(sqIgreja);
			param_igreja = "Igreja: " + this.igreja.getDsIgreja();
		} else {
			param_igreja = "Igreja: Todas";
		}
		
		if(sqCargo > 0) {
			if(!flWhere) {
				sqlComplemento.append(" WHERE min.cgo_sq_cargo = " + sqCargo);
				flWhere = true;
			}else {
				sqlComplemento.append(" AND min.cgo_sq_cargo = " + sqCargo);
			}
			
			this.cargo = this.cargoBO.find(sqCargo);
			param_cargo = "Cargo: " + this.cargo.getDsCargo();
		} else {
			param_cargo = "Cargo: Todos";
		}
		
		
		if(cdSituacao == 1) {
			if(!flWhere) {
				sqlComplemento.append(" WHERE min.min_dt_excluido is null");
				flWhere = true;
			}else {
				sqlComplemento.append(" AND min.min_dt_excluido is null");
			}			
			param_situacao = "Ministros ATIVOS";
		} else if(cdSituacao == 2) {
			if(!flWhere) {
				sqlComplemento.append(" WHERE min.min_dt_excluido is not null");
				flWhere = true;
			}else {
				sqlComplemento.append(" AND min.min_dt_excluido is not null");
			}
			param_situacao = "Ministros INATIVOS";
		}
		 
		if(nnOrdem == 1) {
			sqlComplemento.append(" ORDER BY min.min_cd_codigo");
			param_ordemExibicao = "Ordenação: Rg do Ministro";
		} else if(nnOrdem == 2) {
			sqlComplemento.append(" ORDER BY min.min_nm_nome");
			param_ordemExibicao = "Ordenação: Nome do Ministro";
		} else if(nnOrdem == 3) {
			sqlComplemento.append(" ORDER BY rgn.rgn_ds_regiao, min.min_cd_codigo");
			param_ordemExibicao = "Ordenação: Região e Rg do Ministro";
		} else if(nnOrdem == 4) {
			sqlComplemento.append(" ORDER BY rgn.rgn_ds_regiao, min.min_nm_nome");
			param_ordemExibicao = "Ordenação: Região e Nome do Ministro";
		}
		
		
		System.out.println("Sql: " + sqlComplemento.toString());
		
		Map<String, Object> parametros = new HashMap<>();
		
		parametros.put("urlLogo", urlLogo);
		parametros.put("sqConvencao", sqConvencao);
		parametros.put("param_regiao", param_regiao);
		parametros.put("param_departamento", param_departamento);		
		parametros.put("param_igreja", param_igreja);		
		parametros.put("param_cargo", param_cargo);
		parametros.put("param_situacao", param_situacao);
		parametros.put("param_ordemExibicao", param_ordemExibicao);
		parametros.put("param_sqlComplemento", sqlComplemento.toString());
		parametros.put("SUBREPORT_DIR", "/relatorios/");
		

		
		ExecutorRelatorio executor = new ExecutorRelatorio("/relatorios/ministroGeral.jasper", response, request, parametros, "MinistroGeral", request.getParameter("tpRelatorio"));
		
		Session session = manager.unwrap(Session.class);
		session.doWork(executor);
		
		if(!executor.isRelatorioGerado()) {
			response.getWriter().append("Não há dados para exibir no relatório. RELATÓRIO DE MINISTROS \t").append(request.getContextPath());
		}
		
	}

}
