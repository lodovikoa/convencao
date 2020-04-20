package br.com.convencao.dao;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import br.com.convencao.model.TipoLancamento;

public class TipoLancamentoDAO extends GenericoDAO<TipoLancamento> {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private EntityManager manager;
	
	public List<TipoLancamento> findAll(){
		return manager.createQuery("from TipoLancamento c "
				+ "left join fetch c.planoConta", TipoLancamento.class).getResultList();
	}
	
	public TipoLancamento findByPrimaryKey(Long sq){
		try{
			return manager.createQuery("from TipoLancamento c "
					+ "left join fetch c.planoConta "
					+ "where c.sqTipoLancamento = :sqTipoLancamento", TipoLancamento.class)
					.setParameter("sqTipoLancamento", sq)
					.getSingleResult();
		}catch (NoResultException e){
			return null;
		}
	}
	
	public List<TipoLancamento> findAllTipoLancamentoPorTipo(String tpConta) {
		try {
			StringBuilder sql = new StringBuilder();
			
			sql.append("from TipoLancamento tl ");
			sql.append("left join fetch tl.planoConta pc ");
			sql.append("where pc.tpConta like :tpConta ");
			sql.append("order by tl.dsTipoLancamento ");
			
			List<TipoLancamento> result = manager.createQuery(sql.toString(), TipoLancamento.class)
					.setParameter("tpConta", tpConta)
					.getResultList();
			
			return result;
			
		}catch (NoResultException e){
			return null;
		}
	}

}
