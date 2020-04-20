package br.com.convencao.dao;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import br.com.convencao.model.UGrupo;
import br.com.convencao.model.Usuario;
import br.com.convencao.model.UsuarioGrupo;

public class UsuarioGrupoDAO extends GenericoDAO<UsuarioGrupo> {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;

	public UsuarioGrupo findUsuario(UGrupo uGrupo, Usuario usuario) {
		try {
			UsuarioGrupo result = manager.createQuery("from UsuarioGrupo u where u.usuario =:usuario and u.grupo = :grupo", UsuarioGrupo.class)
					.setParameter("usuario", usuario)
					.setParameter("grupo", uGrupo)
					.getSingleResult();

			return result;

		}catch(NoResultException e){
			return null;
		}
	}

}
