package br.com.convencao.bo;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import br.com.convencao.dao.FormaRecebimentoDAO;
import br.com.convencao.model.to.FormaRecebimentoPorReciboCpl;

public class FormaRecebimentoBO implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private static Log log = LogFactory.getLog(FormaRecebimentoBO.class);
	
	@Inject
	FormaRecebimentoDAO dao;

	// Listar as formas de recebimento de um recibo por sqRecibo
	public List<FormaRecebimentoPorReciboCpl> findAllReciboCplPorSqRecibo(Long sqRecibo, boolean flSoComValores) {
		log.info("findAllReciboCplPorSqRecibo(" + sqRecibo + ")");
		
		return dao.findAllReciboCplPorSqRecibo(sqRecibo, flSoComValores);
	}

	// Listar as formas de recebimento de um recibo por cdNsu
	public List<FormaRecebimentoPorReciboCpl> findAllReciboCplPorCdNsu(Long cdNsu, boolean flSoComValores) {
		log.info("findAllReciboCplPorCdNsu(" + cdNsu + ")");
		
		return dao.findAllReciboCplPorCdNsu(cdNsu, flSoComValores);
	}


}
