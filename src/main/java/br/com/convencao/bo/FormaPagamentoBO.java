package br.com.convencao.bo;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import br.com.convencao.dao.FormaPagamentoDAO;
import br.com.convencao.model.FormaPagamento;

public class FormaPagamentoBO implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private static Log log = LogFactory.getLog(FormaPagamentoBO.class);
	
	@Inject
	FormaPagamentoDAO dao;

	// Listar lista de Formas de Pagamento ordenado
	public List<FormaPagamento> findAllOrdenado() {
		log.info("findAllOrdenado()");
		return dao.findAllOrdenado();
	}
}
