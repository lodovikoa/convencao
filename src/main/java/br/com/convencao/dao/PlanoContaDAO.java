package br.com.convencao.dao;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import br.com.convencao.model.PlanoConta;

public class PlanoContaDAO extends GenericoDAO<PlanoConta>{

	private static final long serialVersionUID = 1L;
	
	@Inject
	private EntityManager manager; 
	
	public Long proximoCodigoConta(){
		Long max = manager.createQuery("select max(q.cdConta) + 1 from PlanoConta q", Long.class).getSingleResult();
		return max;
	}
	
	
//	// Buscar lista de plano de contas por tipo (D)debito ou (C)credito
//	public List<PlanoConta> findAllPorTipo(String tpConta) {
//		try {
//			
//			List<PlanoConta> result = manager.createQuery("from PlanoConta p where p.tpConta = :tpConta order by dsConta", PlanoConta.class)
//					.setParameter("tpConta", tpConta)
//					.getResultList();
//			
//			return result;
//		} catch (NoResultException e) {
//			return null;
//		}
//	}

}
