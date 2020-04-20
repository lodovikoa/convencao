package br.com.convencao.bo;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import br.com.convencao.dao.ProtocoloStatusDAO;
import br.com.convencao.model.ProtocoloStatus;

public class ProtocoloStatusBO implements Serializable {

	private static final long serialVersionUID = 1L;
	private static Log log = LogFactory.getLog(ProtocoloStatusBO.class);
	
	@Inject
	ProtocoloStatusDAO protocoloStatusDAO;
	
	// Listar todos os registros
	public List<ProtocoloStatus> findAllAtivos() {
		log.info("findAllAtivos()");
		return protocoloStatusDAO.findAllAtivos();
	}

	// Buscar registro pla primary key usando find
	public ProtocoloStatus find(Long sq) {
		log.info("find( " + sq + " )");
		return protocoloStatusDAO.find(ProtocoloStatus.class, sq);
	}

}
