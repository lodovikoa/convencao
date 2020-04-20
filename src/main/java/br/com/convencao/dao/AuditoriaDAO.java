package br.com.convencao.dao;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;

import br.com.convencao.bean.auditoria.AuditoriaFiltro;
import br.com.convencao.model.Auditoria;

public class AuditoriaDAO extends GenericoDAO<Auditoria> {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private EntityManager manager;
	
	public List<Auditoria> findByParametros(AuditoriaFiltro filtro){
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Auditoria> criteriaQuery = builder.createQuery(Auditoria.class);
		List<Predicate> predicates = new ArrayList<>();
		
		Root<Auditoria> auditoriaRoot = criteriaQuery.from(Auditoria.class);
		
		// Data inicio >= dtDataAlteracao
		if(null != filtro.getDtInicioFiltro()){
			predicates.add(builder.greaterThanOrEqualTo(auditoriaRoot.get("dtDataAlteracao"), filtro.getDtInicioFiltro()));
		}
		
		// Data fim <= dtDataAlteracao
		if(null != filtro.getDtFimFiltro()){
			predicates.add(builder.lessThanOrEqualTo(auditoriaRoot.get("dtDataAlteracao"), filtro.getDtFimFiltro()));
		}
		
		
		if(StringUtils.isNotBlank(filtro.getDsUsuarioFiltro())){
			predicates.add(builder.equal(auditoriaRoot.get("dsUsuario"), filtro.getDsUsuarioFiltro()));
		}
		
		if(StringUtils.isNotBlank(filtro.getDsTabelaFiltro())){
			predicates.add(builder.equal(auditoriaRoot.get("dsTabela"), filtro.getDsTabelaFiltro()));
		}
		
		if(null != filtro.getCdCodigoPessoaFiltro()){
			predicates.add(builder.equal(auditoriaRoot.get("cdCodigoPessoa"), filtro.getCdCodigoPessoaFiltro()));
		}
		
		if(StringUtils.isNotBlank(filtro.getDsTipoFiltro())){
			predicates.add(builder.equal(auditoriaRoot.get("dsTipo"), filtro.getDsTipoFiltro()));
		}
		
		criteriaQuery.select(auditoriaRoot);
		criteriaQuery.where(predicates.toArray(new Predicate[0]));
		
		TypedQuery<Auditoria> query = manager.createQuery(criteriaQuery);
		
		return query.getResultList();
	}
	
	
	public List<String> findAllTabelas(){
		try{
			List<String> result = manager.createQuery("select distinct(q.dsTabela) from Auditoria q order by q.dsTabela", String.class)
					.getResultList();
			
			return result;
		}catch(NoResultException e){
			return new ArrayList<>();
		}
	}
	


	public List<String> findAllUsuarios() {
		try{
			List<String> result = manager.createQuery("select distinct(q.dsUsuario) from Auditoria q order by q.dsUsuario", String.class)
					.getResultList();
			
			return result;
		}catch(NoResultException e){
			return new ArrayList<>();
		}
	}
		
}