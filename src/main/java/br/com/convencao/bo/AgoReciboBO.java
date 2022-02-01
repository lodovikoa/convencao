package br.com.convencao.bo;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import br.com.convencao.dao.AgoReciboDAO;
import br.com.convencao.model.AgoRecibo;

public class AgoReciboBO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private static Log log = LogFactory.getLog(AgoReciboBO.class);
	
	@Inject
	AgoReciboDAO agoReciboDAO;
	
	// Listar todos os registros de uma AGO
	public List<AgoRecibo> findAllPorAgo(Long sqAgo) {
		log.info("findAllPorAgo(" + sqAgo + ")");

		return agoReciboDAO.findAllPorAtributo(AgoRecibo.class, "ago.sqAgo", sqAgo, null);
	}

}
