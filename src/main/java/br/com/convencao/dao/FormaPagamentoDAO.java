package br.com.convencao.dao;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import br.com.convencao.model.FormaPagamento;

public class FormaPagamentoDAO extends GenericoDAO<FormaPagamento> {

	private static final long serialVersionUID = 1L;

	@Inject
	EntityManager manager;
	
	public List<FormaPagamento> findAllOrdenado() {
		try {
			List<FormaPagamento> result = manager.createQuery("from FormaPagamento f order by f.cdOrdem", FormaPagamento.class)
					.getResultList();
			
			return result;
			
		} catch (NoResultException e) {
			return null;
		}
	}
}
