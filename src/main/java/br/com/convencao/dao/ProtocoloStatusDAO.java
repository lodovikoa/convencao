package br.com.convencao.dao;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import br.com.convencao.model.ProtocoloStatus;

public class ProtocoloStatusDAO extends GenericoDAO<ProtocoloStatus> {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager; 

	public List<ProtocoloStatus> findAllAtivos() {
		try{
			return  manager.createQuery("from ProtocoloStatus p where p.flExibir is true order by p.nnOrdem", ProtocoloStatus.class).getResultList();
		}catch(NoResultException e){
			return null;
		}
	}

}
