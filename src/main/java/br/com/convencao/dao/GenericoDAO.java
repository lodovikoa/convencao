package br.com.convencao.dao;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;

import br.com.convencao.model.Auditoria;
import br.com.convencao.util.Uteis;

public class GenericoDAO<T> implements Serializable  {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager; 

	// Buscar todos os registros de uma determinada tabela
	public List<T> findAll(Class<T> clazz){ 
		return  manager.createQuery("from " + clazz.getSimpleName(), clazz).getResultList();
	}

	// Buscar todos os registros de uma determinada tabela ordenado por um determinado campo
	public List<T> findAll(Class<T> clazz, String atributo){ 
		return  manager.createQuery("from " + clazz.getSimpleName() + " order by " + atributo, clazz).getResultList();
	}

	// Buscar registro pela chave primaria de uma determinada tabela
	public T findByPrimaryKey(Class<T> clazz, Long id) {
		T result = null;
		try{			
			result =  manager.createQuery("from " + clazz.getSimpleName() + " d where d.id = :id", clazz)
					.setParameter("id", id)
					.getSingleResult();
			return result;

		}catch(NoResultException e){
			return result;
		}
	}

	// Buscar registro pela chave primaria de uma determinada tabela usando find
	public T find(Class<T> clazz, Long id) {
		try {
			return manager.find(clazz, id);
		}catch(NoResultException e) {
			return null;
		}
	}

	// Buscar registro pela chave primaria de uma determinada tabela usando find com lock pessimista
	// LockModeType.PESSIMISTIC_READ
	// LockModeType.PESSIMISTIC_WRITE
	// LockModeType.PESSIMISTIC_FORCE_INCREMENT
	public T find(Class<T> clazz, Long id, LockModeType lock) {
		try {
			return manager.find(clazz, id, lock);
		}catch(NoResultException e) {
			return null;
		}
	}

	// Salvar inclusões e alterações
	public T salvar(T t) throws Exception {
		try{
			return manager.merge(t);
		}catch(Exception e){
			throw e;
		}
	}


	// Excluir registro
	public void delete(T t, Long sq)throws PersistenceException {
		try{
			manager.remove(t);
			manager.flush();

			// Gravar na auditoria o usuario que fez a exclusão do registro 
			this.gravarAuditoriaUsuarioExclusao(t, sq);

		}catch(PersistenceException e){
			throw e;
		}
	}

	// Buscar quantidade registrado de uma classe de acordo com o atributo e valor informado do tipo String
	public Long qtdePorAtributo( Class<T> clazz, String atributo, String valor){
		try{
			Long qtdeLinhas = manager.createQuery("select count(q) from "+ clazz.getSimpleName() + " q where q." + atributo + " = :atributo", Long.class)
					.setParameter("atributo", valor)
					.getSingleResult();

			return qtdeLinhas;
		}catch(NoResultException e){
			return 0L;
		}
	}

	// Buscar quantidade registrado de uma classe de acordo com o atributo e valor informado do tipo Long
	public Long qtdePorAtributo( Class<T> clazz, String atributo, Long valor){
		try{
			Long qtdeLinhas = manager.createQuery("select count(q) from "+ clazz.getSimpleName() + " q where q." + atributo + " = :atributo", Long.class)
					.setParameter("atributo", valor)
					.getSingleResult();

			return qtdeLinhas;
		}catch(NoResultException e){
			return 0L;
		}
	}

	// Buscar uma entidade de acordo com atributo informado do tipo String
	public T findByPorAtributo( Class<T> clazz, String atributo, String valor) {
		try {
			return manager.createQuery("from "+ clazz.getSimpleName() +" where " + atributo +"= :atributo", clazz)
					.setParameter("atributo",valor)
					.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	// Buscar uma entidade de acordo com atributo informado do tipo Long
	public T findByPorAtributo( Class<T> clazz, String atributo, Long valor) {
		try {
			return manager.createQuery("from "+ clazz.getSimpleName() +" where " + atributo +"= :atributo", clazz)
					.setParameter("atributo",valor)
					.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	// Buscar uma lista de registros de acordo com o atributo informado do tipo string
	public List<T> findAllPorAtributo( Class<T> clazz, String atributo, String valor) {
		try{
			return manager.createQuery("from " + clazz.getSimpleName() + " where " + atributo + "= :atributo", clazz)
					.setParameter("atributo", valor)
					.getResultList();
		}catch (NoResultException e) {
			return null;
		}
	}

	// Buscar uma lista de registros de acordo com o atributo informado do tipo Long
	public List<T> findAllPorAtributo( Class<T> clazz, String atributo, Long valor, String atributoOrdenar) {
		try{
			// Se não informar o atributoOrdenar, ordena pela primeiro atributo
			String atributoOrdenarNew = atributoOrdenar == null? "1": atributoOrdenar;
			
			return manager.createQuery("from " + clazz.getSimpleName() + " where " + atributo + "= :atributo order by " + atributoOrdenarNew, clazz)
					.setParameter("atributo", valor)
					.getResultList();
		}catch (NoResultException e) {
			return null;
		}
	}

	// Atualiza o usuário logado na auditoria quando um registro é excluído 
	private void gravarAuditoriaUsuarioExclusao( T t, Long sq) {

		String dsTabela = Uteis.obterNomeTabela(t.getClass().getSimpleName());

		// Identificar registro de auditoria que foi excluído
		Long sqAuditoriaTemp = manager.createQuery("select max(sqAuditoria) from Auditoria where sqTabela = :sq and dsTabela like :dsTabela", Long.class)
				.setParameter("sq", sq)
				.setParameter("dsTabela", dsTabela)
				.getSingleResult();
		
		// Se houver registro de auditoria faz ajuste do usuario logado que fez a exclusão
		if(sqAuditoriaTemp != null && sqAuditoriaTemp > 0) {
			// Buscar registro de auditoria que foi excluído para alteração do usuario logado
			Auditoria auditoriaTemp = manager.createQuery("from Auditoria d where d.sqAuditoria = :sq", Auditoria.class)
					.setParameter("sq", sqAuditoriaTemp)
					.getSingleResult();

			// Registrar usuario logado
			auditoriaTemp.setDsUsuario(Uteis.UsuarioLogado().getUsuario().getDsLogin());

			// Salvar auditoria alterada
			manager.merge(auditoriaTemp);
		}
	}
}
