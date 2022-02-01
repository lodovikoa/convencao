package br.com.convencao.bo;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import br.com.convencao.dao.AgoDAO;
import br.com.convencao.model.Ago;

public class AgoBO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private static Log log = LogFactory.getLog(AgoReciboBO.class);
	
	@Inject
	AgoDAO agoDAO;
	
	// Listar todas AGOs
	public List<Ago> findAllAGO() {
		log.info("findAllAGO()");
		
		return agoDAO.findAll(Ago.class);
	}
	
	// Buscar regisgro pel primary key usando find
	public Ago find(Long sq) {
		log.info("find(" + sq + ")");
		return agoDAO.find(Ago.class, sq);
	}

}
