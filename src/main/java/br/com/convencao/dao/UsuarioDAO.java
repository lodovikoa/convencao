package br.com.convencao.dao;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import br.com.convencao.model.Usuario;

public class UsuarioDAO extends GenericoDAO<Usuario> {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;

	// Buscar usuário com seus grupos associados pelo login
	public Usuario findByLoginComGrupos(String dsLogin) {
		try {
			Usuario result = manager.createQuery("from Usuario u left join fetch u.grupos where u.dsLogin = :dsLogin", Usuario.class)
					.setParameter("dsLogin", dsLogin)
					.getSingleResult();
			
			return result;
		}catch(NoResultException e){
			return null;
		}
	}
	
	// Buscar usuário com seus grupos associados pela primary key
	public Usuario findByPrimaryKeyComGrupos(Long sqUsuario) {
		try {
			Usuario result = manager.createQuery("from Usuario u left join fetch u.grupos where u.sqUsuario = :sqUsuario", Usuario.class)
					.setParameter("sqUsuario", sqUsuario)
					.getSingleResult();
			
			return result;
		}catch(NoResultException e){
			return null;
		}
	}

}
