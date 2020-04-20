package br.com.convencao.bo;

import java.io.Serializable;

import javax.inject.Inject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import br.com.convencao.dao.ProtocoloDAO;
import br.com.convencao.model.Protocolo;

public class ProtocoloBO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private static Log log = LogFactory.getLog(ProtocoloBO.class);
	
	@Inject
	ProtocoloDAO protocoloDAO;

	// Buscar Protocolo com find
	public Protocolo find(Long sq) {
		log.info("find(" + sq + ")" );
		return protocoloDAO.find(Protocolo.class, sq);
	}
}
