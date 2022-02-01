package br.com.convencao.bo;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import br.com.convencao.dao.AgoInscritoDAO;
import br.com.convencao.model.AgoInscrito;

public class AgoInscritoBO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private static Log log = LogFactory.getLog(AgoReciboBO.class);
	
	@Inject
	AgoInscritoDAO agoInscritoDAO;
	
	// Listar todos os registros de um AgoRecibo
	public List<AgoInscrito> findAllPorAgoRecibo(Long sqAgoRecibo) {
		log.info("findAllPorAgoRecibo(" + sqAgoRecibo + ")");

		return agoInscritoDAO.findAllPorAtributo(AgoInscrito.class, "agoRecibo.sqRecibo", sqAgoRecibo, null);
	}

}
