package br.com.convencao.bo;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.PersistenceException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import br.com.convencao.dao.EstadoCivelDAO;
import br.com.convencao.model.EstadoCivel;
import br.com.convencao.util.Uteis;
import br.com.convencao.util.jpa.Transactional;

public class EstadoCivelBO implements Serializable {

	private static final long serialVersionUID = 1L;
	private static Log log = LogFactory.getLog(EstadoCivelBO.class);
	
	@Inject
	EstadoCivelDAO estadoCivelDAO;

	// Listar todos os registros
	public List<EstadoCivel> findAll(){
		log.info("findAll()");
		List<EstadoCivel> estadoCivel = estadoCivelDAO.findAll(EstadoCivel.class);
		
//		Collections.sort(estadoCivel);
		return estadoCivel;
	}
	
	// Buscar um registro pela chave  primaria
	public EstadoCivel findByPrimaryKey(Long sqEstadoCivel){
		log.info("findByPrimaryKey(" + sqEstadoCivel + ")");
		return estadoCivelDAO.findByPrimaryKey(EstadoCivel.class, sqEstadoCivel);
	}
	
	// Buscar registro pela primary key usando find
	public EstadoCivel find(Long sq) {
		log.info("find(" + sq + ")");
		return estadoCivelDAO.find(EstadoCivel.class, sq);
	}

	// Salvar novo e alteração de registro
	@Transactional
	public EstadoCivel salvar(EstadoCivel estadoCivel){
		try {
			log.info("salvar(" + estadoCivel.getSqEstadoCivel() + ")");
			// Retirar espaços de inicio e fim
			estadoCivel.setDsEstadoCivel(estadoCivel.getDsEstadoCivel().trim());

			// Validar descrição única
			EstadoCivel estadoCivelExistente = estadoCivelDAO.findByPorAtributo(EstadoCivel.class, "dsEstadoCivel", estadoCivel.getDsEstadoCivel());
			if(estadoCivelExistente != null && !estadoCivelExistente.getSqEstadoCivel().equals(estadoCivel.getSqEstadoCivel()) ){
				throw new NegocioException("EstadoCivel já está cadastrada");
			}

			// Verificar se houve alteração
			if(estadoCivel.getSqEstadoCivel() != null){
				EstadoCivel estadoCivelAtual = estadoCivelDAO.findByPrimaryKey(EstadoCivel.class, estadoCivel.getSqEstadoCivel());
				if(estadoCivelAtual.equalsTO(estadoCivel)){
					throw new NegocioException("Não houve alteração!");
				}
			}

			// Setar informações para auditoria
			estadoCivel.setAuditoriaData(Uteis.DataHoje());
			estadoCivel.setAuditoriaUsuario(Uteis.UsuarioLogado().getUsuario().getDsLogin());


			return this.estadoCivelDAO.salvar(estadoCivel);
		} catch (Exception e) {
			//e.printStackTrace();
			throw new NegocioException("Erro ao salvar Estado civil: " + e.getMessage(), e.getCause());
		}
	}

	// Excluir registro
	@Transactional
	public void remover(EstadoCivel estadoCivel) {
		try{
			log.info("remover(" + estadoCivel.getSqEstadoCivel() + ")");
			// TODO  Validar exclusão de estadoCivel que está cadastrada em Ministro
			
			estadoCivel = estadoCivelDAO.findByPrimaryKey(EstadoCivel.class, estadoCivel.getSqEstadoCivel());
			estadoCivelDAO.delete(estadoCivel,estadoCivel.getSqEstadoCivel());
			
		} catch (PersistenceException e) {
			//e.printStackTrace();
			throw new NegocioException("EstadoCivel não pode ser removido.");
		}

	}

}