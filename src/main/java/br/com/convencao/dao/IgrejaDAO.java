package br.com.convencao.dao;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Fetch;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;

import br.com.convencao.bean.cadastro.IgrejaFiltro;
import br.com.convencao.bo.NegocioException;
import br.com.convencao.model.Estado;
import br.com.convencao.model.Igreja;
import br.com.convencao.model.Ministro;
import br.com.convencao.model.Regiao;

public class IgrejaDAO extends GenericoDAO<Igreja> {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;

	
	// Buscar Igreja pela primary Key com Regiao, Estado e Ministro
	public Igreja findByPrimaryKey(Long sq){
		try {
						
			Igreja result = manager.createQuery("from Igreja c "
					+ "left join fetch c.regiao "
					+ "left join fetch c.estado "
					+ "left join fetch c.ministro "
					+ "where c.sqIgreja = :sq", Igreja.class)
				.setParameter("sq",sq)
				.getSingleResult();
			
			return result;
		} catch (NoResultException e) {
			return null;
		}
	}
	
	// Buscar igrejas de uma determinada região
	public List<Igreja> findAllPorRegiao(Regiao regiao){
		try{
			return manager.createQuery("from Igreja c "
					+ "left join fetch c.estado "
					+ "where c.regiao = :regiao", Igreja.class)
				.setParameter("regiao", regiao)
				.getResultList();
					
		}catch (NoResultException e) {
			return null;
		}
	}
	
	// Buscar todas Igrejas de todas as regiões
	public List<Igreja> findAll(Regiao regiao){
		try {
			
			return  manager.createQuery("from Igreja c "
					+ "left join fetch c.estado "
					+ "left join fetch c.regiao "
					+ "order by c.dsIgreja "
					, Igreja.class).getResultList();
			
		}catch (NoResultException e) {
			return null;
		}
	}

	// Listar Igrejas conforme parametros definidos
	@SuppressWarnings("unused")
	public List<Igreja> findByParametros(IgrejaFiltro filtro){
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Igreja> criteriaQuery = builder.createQuery(Igreja.class);
		List<Predicate> predicates = new ArrayList<>();

		Root<Igreja> igrejaRoot = criteriaQuery.from(Igreja.class);
		
		Fetch<Igreja, Ministro> ministroJoin = igrejaRoot.fetch("ministro", JoinType.LEFT);
		
		Fetch<Igreja, Estado> estadoJoin = igrejaRoot.fetch("estado", JoinType.LEFT);

		// delimitar pela região selecionada 
		// Regiao é obrigatória
		if(filtro.getRegiaoItens() == null){
			throw new NegocioException("Regiao: preenchimento obrigatório!");
		}else if(filtro.getRegiaoItens().getSqRegiao() > 0){
			predicates.add(builder.equal(igrejaRoot.get("regiao"), filtro.getRegiaoItens().getSqRegiao()));
		}

		// Delimitar por uma igreja (parte do nome da igreja em qualquer possição)
		if(StringUtils.isNotBlank(filtro.getDsIgrejaFiltro())){
			predicates.add(builder.like(igrejaRoot.get("dsIgreja"), "%" + filtro.getDsIgrejaFiltro() + "%"));
		}

		// Delimitar por CNPJ
		if(StringUtils.isNotBlank(filtro.getDsCnpjFiltro())){
			predicates.add(builder.equal(igrejaRoot.get("dsCnpj"), filtro.getDsCnpjFiltro()));
		}

		// Delimitar por bairro (parte do bairro do endereço da igreja em qualquer possição)
		if(StringUtils.isNotBlank(filtro.getDsBairroFiltro())){
			predicates.add(builder.like(igrejaRoot.get("dsBairro"), "%" + filtro.getDsBairroFiltro() + "%"));
		}

		// Delimitar por cidade (parte da cidade do endereço da igreja em qualquer possição)
		if(StringUtils.isNotBlank(filtro.getDsCidadeFiltro())){
			predicates.add(builder.like(igrejaRoot.get("dsCidade"), "%" + filtro.getDsCidadeFiltro() + "%"));
		}
		
		// Delimitar pelo estado selecionado
		if(null != filtro.getEstado() && null != filtro.getEstado().getSqEstado() && filtro.getEstado().getSqEstado() > 0){
			predicates.add(builder.equal(igrejaRoot.get("estado"), filtro.getEstado().getSqEstado()));
		}
		
		// Delimitar pelo presidente selecionado
		if(null != filtro.getMinistro() && null != filtro.getMinistro().getSqMinistro() && filtro.getMinistro().getSqMinistro() > 0){
			predicates.add(builder.equal(igrejaRoot.get("ministro"), filtro.getMinistro().getSqMinistro()));
		}
		
		criteriaQuery.select(igrejaRoot);
		criteriaQuery.where(predicates.toArray(new Predicate[0]));

		TypedQuery<Igreja> query = manager.createQuery(criteriaQuery);

		return query.getResultList();
	}

	public Igreja findByPastorPresidente(Long sqIgreja, Ministro ministro) {
		try{
			return manager.createQuery("from Igreja c "
					+ "where c.ministro = :ministro and c.sqIgreja <> :sqIgreja", Igreja.class)
				.setParameter("ministro", ministro)
				.setParameter("sqIgreja", sqIgreja)
				.getSingleResult();
					
		}catch (NoResultException e) {
			return null;
		}
	
	}

	public List<Igreja> findAllPorRegiaoLiberadasUsuario(List<Long> sqRegioesPermissoes) {
		try {
			List<Igreja> result =  manager.createQuery("from Igreja c left join fetch c.estado where c.regiao.sqRegiao in :sqRegioesPermissoes order by c.dsIgreja", Igreja.class)
					.setParameter("sqRegioesPermissoes", sqRegioesPermissoes)
					.getResultList();
			
			return result;
		}catch (NoResultException e) {
			return null;
		}
		
	}

}
