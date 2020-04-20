package br.com.convencao.bo;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import br.com.convencao.bean.auditoria.AuditoriaFiltro;
import br.com.convencao.dao.AuditoriaDAO;
import br.com.convencao.model.Auditoria;
import br.com.convencao.model.Ministro;
import br.com.convencao.model.to.AuditoriaCpl;

public class AuditoriaBO implements Serializable{

	private static final long serialVersionUID = 1L;

	private static Log log = LogFactory.getLog(AuditoriaBO.class);

	@Inject
	AuditoriaDAO dao;
	
	@Inject
	MinistroBO ministroBO;

	// Listar todos os registros
	public List<Auditoria> findAByParametros(AuditoriaFiltro filtro){
		log.info("findAByParametros(AuditoriaFiltro filtro)");

		if(filtro.getDtInicioFiltro() == null && filtro.getDtFimFiltro() == null)
			throw new NegocioException("Data auditoria (período de pesquisa) é de preenchimento obrigatório.");

		if(filtro.getDtInicioFiltro() != null && filtro.getDtFimFiltro() != null){
			if(filtro.getDtFimFiltro().isBefore(filtro.getDtInicioFiltro()))
				throw new NegocioException("Data final deverá ser maior que a data inicial");
		}
		List<Auditoria> result = dao.findByParametros(filtro);

	//	Collections.sort(result);
		
		return result;
	}

	// Buscar um registro pela chave  primaria
	public AuditoriaCpl findByPrimaryKey(Long sq){
		log.info("findByPrimaryKey(" + sq + ")");
		Auditoria auditoria = dao.findByPrimaryKey(Auditoria.class, sq);
		
		Ministro ministro = ministroBO.findByPrimaryKey(auditoria.getSqMinistro());
		
		AuditoriaCpl auditoriaCpl = new AuditoriaCpl();
		auditoriaCpl.setAuditoria(auditoria);
		auditoriaCpl.setMinistro(ministro);
		
		return auditoriaCpl;
	}

	// Buscar nome das tabelas registradas na auditoria
	public List<String> findAllTabelas(){
		log.info("findAllTabelas()");
		List<String> result = dao.findAllTabelas();
		return result; 
	}

	// Buscar nome dos usuários registradas na auditoria
	public List<String> findAllUsuarios(){
		log.info("findAllUsuarios()");
		List<String> result = dao.findAllUsuarios();
		return result; 
	}


}
