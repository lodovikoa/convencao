package br.com.convencao.dao;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import br.com.convencao.model.Convencao;

public class ConvencaoDAO extends GenericoDAO<Convencao> {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private EntityManager manager;
	
	public List<Convencao> listarConvencoes(){	
		List<Convencao> result = manager.createQuery("from Convencao c left join fetch c.estado", Convencao.class).getResultList();
		return result;
	}

	// Buscar convencação pela primary key
	public Convencao findByPrimaryKey(Long sq) {
		try {
			Convencao result = manager.createQuery("from Convencao c "
					+ "left join fetch c.estado "
					+ "where c.sqConvencao = :sqConvencao", Convencao.class)
				.setParameter("sqConvencao",sq)
				.getSingleResult();
			
			return result;
		} catch (NoResultException e) {
			return null;
		}
	}

}
