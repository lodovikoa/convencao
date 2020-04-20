package br.com.convencao.dao;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import br.com.convencao.model.MinistroSenha;

public class MinistroSenhaDAO extends GenericoDAO<MinistroSenha> {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;
	
	// Buscar último registro de um ministro
	public MinistroSenha findByMinistroUltimoRegistro(Long sqMinistro) {
		try {
			List<MinistroSenha> result = manager.createQuery("from MinistroSenha ms "
					+ "left join fetch ms.ministro m "
					+ "where m.sqMinistro = :sqMinistro "
					+ "order by ms.sqMinistroSenha desc", MinistroSenha.class)
					.setParameter("sqMinistro", sqMinistro)
					.getResultList();
			
			return result.size() > 0? result.get(0): null;
			
		}catch (NoResultException e) {
			return null;
		}
	}

}
