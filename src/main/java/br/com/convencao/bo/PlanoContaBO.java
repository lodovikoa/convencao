package br.com.convencao.bo;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.PersistenceException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import br.com.convencao.dao.PlanoContaDAO;
import br.com.convencao.model.PlanoConta;
import br.com.convencao.util.Uteis;
import br.com.convencao.util.jpa.Transactional;

public class PlanoContaBO implements Serializable{

	private static final long serialVersionUID = 1L;
	private static Log log = LogFactory.getLog(PlanoContaBO.class);
	
	@Inject
	PlanoContaDAO dao;

	// Listar todos os registros
	public List<PlanoConta> findAll(){
		log.info("findAll()");
		List<PlanoConta> all = dao.findAll(PlanoConta.class);
		
		return all;
	}
	
	// Buscar um registro pela chave  primaria
	public PlanoConta findByPrimaryKey(Long sq){
		log.info("findByPrimaryKey(" + sq + ")");
		return dao.findByPrimaryKey(PlanoConta.class, sq);
	}
	
	// Buscar registro pela chave  primaria usando find
	public PlanoConta find(Long sq) {
		log.info("find(" + sq + ")");
		return dao.find(PlanoConta.class, sq);
	}

	// Salvar novo e alteração de registro
	@Transactional
	public PlanoConta salvar(PlanoConta registro) {
		try {
			log.info("salvar(" + registro.getSqPlanoConta() + ")");
			
			// Retirar espaços de inicio e fim
			registro.setDsConta(registro.getDsConta().trim());
			registro.setTpConta(registro.getTpConta().trim());

			// Verificar se houve alteração
			if(registro.getSqPlanoConta() != null){
				PlanoConta registroAtual = dao.findByPrimaryKey(PlanoConta.class, registro.getSqPlanoConta());
				if(registroAtual.equalsTO(registro)){
					throw new NegocioException("Não houve alteração!");
				}
			}

			// Validar descrição única
			PlanoConta registroExistente = dao.findByPorAtributo(PlanoConta.class, "dsConta", registro.getDsConta());
			if(registroExistente != null && !registroExistente.getSqPlanoConta().equals(registro.getSqPlanoConta()) ){
				throw new NegocioException("conta (" +registroExistente.getDsConta() + ") já está cadastrada");
			}
			
			// Validar codigo da conta que deve ser única
			registroExistente = dao.findByPorAtributo(PlanoConta.class, "cdConta", registro.getCdConta());
			if(registroExistente != null && !registroExistente.getSqPlanoConta().equals(registro.getSqPlanoConta())){
				throw new NegocioException("Código da conta (" + registroExistente.getCdConta() + ") já está cadastrado.");
			}

			// Setar informações para auditoria
			registro.setAuditoriaData(Uteis.DataHoje());
			registro.setAuditoriaUsuario(Uteis.UsuarioLogado().getUsuario().getDsLogin());


			return this.dao.salvar(registro);
		} catch (Exception e) {
			//e.printStackTrace();
			throw new NegocioException("Erro ao salvar Plano de contas: " + e.getMessage(), e.getCause());
		}

	}
	
	// Excluir registro
	@Transactional
	public void remover(PlanoConta registro) throws NegocioException {
		try{
			log.info("remover(" + registro.getSqPlanoConta() + ")");
			
			// TODO  Validar exclusão de Plano de conta em Lançamentos
			
			registro = dao.findByPrimaryKey(PlanoConta.class, registro.getSqPlanoConta());
			dao.delete(registro, registro.getSqPlanoConta());
			
		} catch (PersistenceException e) {
			//e.printStackTrace();
			throw new NegocioException("Profissao não pode ser removido.");
		}

	}
	
	// Buscar próximo codigo da conta
	public Long proximoCodigoConta(){
		log.info("proximoCodigoConta()");
		return dao.proximoCodigoConta();
	}
	
	// Buscar lista de plano de contas por tipo (D)debito ou (C)credito
		public List<PlanoConta> findAllPorTipo(String tpConta) {
			log.info("findAllPorTipo(" + tpConta + ")");
			return dao.findAllPorAtributo(PlanoConta.class, "tpConta", tpConta);
		}
}
