package br.com.convencao.dao;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import br.com.convencao.bo.NegocioException;
import br.com.convencao.model.Regiao;

public class RegiaoDAO extends GenericoDAO<Regiao> {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;

	// Listar todas as regiões
	public List<Regiao> findAll(){ 
		List<Regiao> result = manager.createQuery("from Regiao c left join fetch c.convencao order by c.dsRegiao", Regiao.class).getResultList();
		return result;
	}
	
	// Buscar região pela primary Key
	public Regiao findByPrimaryKey(Long sq){
		try{
			Regiao result = new Regiao();
			if(sq == -1){
				result.setSqRegiao(-1L);
			}else{
				result = manager.createQuery("from Regiao c "
						+ "left join fetch c.convencao "
						+ "where c.sqRegiao = :sq", Regiao.class)
						.setParameter("sq", sq)
						.getSingleResult();

			}
			return result;
		}catch(NoResultException e){
			throw new NegocioException("Erro ao tentar recuperar Região por primary key");
		}
	}

	public List<Regiao> findAllPermitidoPorUsuario(List<Long> sqRegioesPermissoes) {
		List<Regiao> result = manager.createQuery("from Regiao c left join fetch c.convencao where c.sqRegiao in :sqRegioesPermissoes order by c.dsRegiao", Regiao.class)
				.setParameter("sqRegioesPermissoes", sqRegioesPermissoes)
				.getResultList();
		return result;
	}

	// Buscar a quantidade de regiões registradas
	public Long findQtdeRegiao() {
		Long result = manager.createQuery("select count(c) from Regiao c", Long.class)
				.getSingleResult();
		
		return result;
	}
	
	
}
