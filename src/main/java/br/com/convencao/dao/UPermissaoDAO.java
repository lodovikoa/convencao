package br.com.convencao.dao;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;

import br.com.convencao.model.UPermissao;

public class UPermissaoDAO extends GenericoDAO<UPermissao> {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;    

	public List<UPermissao> findAllPorUsuario(Long sq) {
		try {

			List<UPermissao> result = manager.createQuery("from UPermissao p left join fetch p.regiao where p.usuario.sqUsuario = :sq order by p.regiao.dsRegiao", UPermissao.class)
					.setParameter("sq", sq)
					.getResultList();

			return result;

		} catch(NoResultException e){
			return null;
		}

	}


	// tpRegiao = "SEC" para secretaria
	// tpRegiao = "FIN" para financeiro
	// tpRegiao = "PAG" para pagamento
	public List<UPermissao> findAllPorUsuarioTpRegiao(Long sq, String tpRegiao) {
		try {

			StringBuilder query =  new StringBuilder();

			query.append("from UPermissao p left join fetch p.regiao where p.usuario.sqUsuario = :sq ");

			if(tpRegiao.equals("SEC")) {
				query.append("and p.flSecretaria is true");
			} else if(tpRegiao.equals("FIN")) {
				query.append("and p.flFinanceiro is true");
			} else if(tpRegiao.equals("PAG")) {
				query.append("and p.flPagamento is true");
			}


			List<UPermissao> result = manager.createQuery(query.toString(), UPermissao.class)
					.setParameter("sq", sq)
					.getResultList();

			return result;
		}catch(NoResultException e){
			return null;
		}

	}

	// Excluir permissões de um usuário
	public void excluirPermissoes(Long sqUsuario) {
		try {
			StringBuilder query = new StringBuilder();
			query.append("delete UPermissao u where u.usuario.sqUsuario = :sqUsuario");
			
			manager.createQuery(query.toString())
				.setParameter("sqUsuario", sqUsuario)
				.executeUpdate();
			
		} catch(PersistenceException e){
			throw e;
		}
	}
}
