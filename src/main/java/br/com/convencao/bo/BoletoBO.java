package br.com.convencao.bo;

import java.io.Serializable;

import javax.inject.Inject;
import javax.persistence.PersistenceException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import br.com.convencao.dao.BoletoDAO;
import br.com.convencao.model.Boleto;
import br.com.convencao.util.jpa.Transactional;

public class BoletoBO implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private static Log log = LogFactory.getLog(BoletoBO.class);
	
	@Inject
	BoletoDAO boletoDAO;

	
	public Boleto findByRegLancamento(Long sqRegLancamento) {
		log.info("findByRegLancamento(" + sqRegLancamento + ")");
		return boletoDAO.findByPorAtributo(Boleto.class, "regLancamento.sqRegLancamento", sqRegLancamento);
	}
	
	
	
	// Buscar regisgro pelo primary key usando find
	public Boleto find(Long sq) {
		log.info("find(" + sq + ")");
		return boletoDAO.find(Boleto.class, sq);
	}


	// Excluir registro
	@Transactional
	public void remover(Boleto boleto) {
		try{
			log.info("remover(" + boleto.getSqBoleto() + ")");
			// TODO  Validar exclusão de Boleto

			boleto = boletoDAO.find(Boleto.class, boleto.getSqBoleto());
			boletoDAO.delete(boleto, boleto.getSqBoleto());
			
		} catch (PersistenceException e) {
			throw new NegocioException("Boleto não pode ser removido.", e);
		}

	}

}
