package br.com.convencao.bo;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.PersistenceException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import br.com.convencao.dao.TipoLancamentoDAO;
import br.com.convencao.model.TipoLancamento;
import br.com.convencao.util.Uteis;
import br.com.convencao.util.jpa.Transactional;

public class TipoLancamentoBO implements Serializable {

	private static final long serialVersionUID = 1L;
	private static Log log = LogFactory.getLog(TipoLancamentoBO.class);

	@Inject
	TipoLancamentoDAO dao;

	// Listar todas os tipos de lançamentos
	public List<TipoLancamento> findAll() {
		log.info("findAll()");
		
		List<TipoLancamento> registro = dao.findAll();
		
		return registro;
	}
	
	// Listar Tipos de Lancamento por Tipo Debito ou Credito
	public List<TipoLancamento> findAllTipoLancamentoPorTipo(String tpConta) {
		log.info("findAllTipoLancamentoPorTipo( " + tpConta + ")");
		
		return dao.findAllTipoLancamentoPorTipo(tpConta);
	}
	
	// Buscar Tipo de lançamento pela chave primária
	public TipoLancamento findByPrimaryKey(Long sq) {
		log.info("findByPrimaryKey(" + sq + ")");
		
		return dao.findByPrimaryKey(sq);
	}
	
	// Buscar Tipo de lançamento pela chave primária usando find
	public TipoLancamento find(Long sq) {
		log.info("find(" + sq + ")");
		return dao.find(TipoLancamento.class, sq);
	}
	
	// Buscar Tipos de Lançamentos associados a um Plano de Contas
	public List<TipoLancamento> findAllTipoLancamentByPlanoConta(Long sqPlanoConta) {
		log.info("findByPlanoConta(" + sqPlanoConta + "," +  "dsTipoLancamento)");
		return dao.findAllPorAtributo(TipoLancamento.class, "planoConta.sqPlanoConta", sqPlanoConta, "dsTipoLancamento");
	}
	
	// Salvar inclusão e alteração de Tipo de Lançamento
	@Transactional
	public TipoLancamento salvar(TipoLancamento registro){
		try {
			
			log.info("salvar(" + registro.getSqTipoLancamento() + ")");
			
			// Retirar espaços em branco de inicio/fim das strings
			registro.setDsTipoLancamento(registro.getDsTipoLancamento().trim());
	
			// Setar informações para auditoria
			registro.setAuditoriaData(Uteis.DataHoje());
			registro.setAuditoriaUsuario(Uteis.UsuarioLogado().getUsuario().getDsLogin());


			// Validar nome do tipo lançamento é único
			TipoLancamento registroExistente = dao.findByPorAtributo(TipoLancamento.class, "dsTipoLancamento", registro.getDsTipoLancamento());
			if(registroExistente != null && !registroExistente.getSqTipoLancamento().equals(registro.getSqTipoLancamento())){
				throw new NegocioException("Nome Tipo Lançamento já está cadastrado");
			}

			// Verificar se houve alteração
			if(registro.getSqTipoLancamento() != null){
				TipoLancamento registroAtual = this.findByPrimaryKey(registro.getSqTipoLancamento());
				if(registroAtual.equalsTO(registro)){
					throw new NegocioException("Não houve alteração!");
				}
			}



			return this.dao.salvar(registro);

		} catch (Exception e) {
			throw new NegocioException("Erro ao tentar salvar Tipo de Lançamento: " + e.getMessage(), e);
		}
	}


	// Excluir TipoLancamento
	@Transactional
	public void remover(TipoLancamento registro){
		try {
			log.info("remover(" + registro.getSqTipoLancamento() + ")");
			
			//Validar exclusão - Tipo de Lançamento		
			

			registro = this.findByPrimaryKey(registro.getSqTipoLancamento());	
			dao.delete(registro, registro.getSqTipoLancamento());
			
		} catch (PersistenceException e) {
			throw new NegocioException("Tipo de Lançamento não pode ser removido.", e.getCause());
		}
	}
}
