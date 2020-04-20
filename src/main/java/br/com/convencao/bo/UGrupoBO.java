package br.com.convencao.bo;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import br.com.convencao.dao.UGrupoDAO;
import br.com.convencao.model.UGrupo;


public class UGrupoBO implements Serializable {

	private static final long serialVersionUID = 1L;

	private static Log log = LogFactory.getLog(UGrupoBO.class);

	@Inject
	private UGrupoDAO uGrupoDAO;


	// Buscar lista de usuários
	public List<UGrupo> findAllGrupos() {
		log.info("findAllGrupos()");
		return uGrupoDAO.findAll(UGrupo.class);
	}

}
