package br.com.convencao.bo;

import java.io.Serializable;

import javax.inject.Inject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import br.com.convencao.dao.MinistroAnexoDAO;
import br.com.convencao.model.MinistroAnexo;

public class MinistroAnexoBO implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private static Log log = LogFactory.getLog(MinistroAnexoBO.class);
	
	@Inject
	MinistroAnexoDAO ministroAnexoDAO;

	public MinistroAnexo find(Long sq) {
		log.info("find(" + sq + ")");
		return ministroAnexoDAO.find(MinistroAnexo.class, sq);
	}

}
