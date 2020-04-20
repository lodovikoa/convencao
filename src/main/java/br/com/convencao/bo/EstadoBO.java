package br.com.convencao.bo;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.PersistenceException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import br.com.convencao.dao.ConvencaoDAO;
import br.com.convencao.dao.EstadoDAO;
import br.com.convencao.model.Convencao;
import br.com.convencao.model.Estado;
import br.com.convencao.util.Uteis;
import br.com.convencao.util.jpa.Transactional;

public class EstadoBO implements Serializable {

	private static final long serialVersionUID = 1L;
	private static Log log = LogFactory.getLog(EstadoBO.class);
	
	@Inject
	EstadoDAO estadoDAO;
	
	@Inject
	ConvencaoDAO convencaoDAO;

	// Listar todos os Estados
	public List<Estado> findAllEstados(){
		log.info("findAllEstados()");
		
		// Regras  de negócio - Não há
		List<Estado> estado =  estadoDAO.findAll(Estado.class);
		
//		Collections.sort(estado);
		
		return estado;
	}

	// Buscar um estado pela chave primária
	public Estado findByPrimaryKey(Long sqEstado) {
		log.info("findByPrimaryKey(" + sqEstado + ")");
		return estadoDAO.findByPrimaryKey(Estado.class, sqEstado);
	}

	public Estado find(Long sq) {
		log.info("find(" + sq + ")");
		return estadoDAO.find(Estado.class, sq);
	}
	
	@Transactional
	public void remover(Estado estado) {
		try{
			log.info("remover(" + estado.getSqEstado() + ")");
			// Validar exclusão de estado
			// Verificar estado em uso na Convencao
			Long qtdeLinhas = convencaoDAO.qtdePorAtributo(Convencao.class, "estado.sqEstado", estado.getSqEstado());
			if(qtdeLinhas > 0){
				throw new NegocioException(" Estado vinculado a Convenção. Não permite exclusão!" );
			}

			// TODO Verificar estado em uso na Igreja

			// TODO Verificar estado em uso no Ministro

			estado = estadoDAO.findByPrimaryKey(Estado.class, estado.getSqEstado());
			estadoDAO.delete(estado, estado.getSqEstado());
		} catch (PersistenceException e) {
			//e.printStackTrace();
			throw new NegocioException("Estado não pode ser removido.");
		}

	}

	// Salvar novo estado e alterações de estado
	@Transactional
	public Estado salvar(Estado estado)  {
		try {
			log.info("salvar(" + estado.getSqEstado() + ")");
			// Retirar espaços de inicio e fim
			estado.setDsNome(estado.getDsNome().trim());

			// Colocar sigla em caixa alta
			estado.setDsUf(estado.getDsUf().toUpperCase());

			// Validar UF única
			Estado estadoExistente = estadoDAO.findByPorAtributo(Estado.class, "dsUf", estado.getDsUf());
			if(estadoExistente != null && !estadoExistente.getSqEstado().equals(estado.getSqEstado()) ){
				throw new NegocioException("UF já está cadastrado para o estado (" + estadoExistente.getDsNome() + ")");
			}

			// Verificar se houve alteração
			if(estado.getSqEstado() != null){
				Estado estadoAtual = estadoDAO.findByPrimaryKey(Estado.class, estado.getSqEstado());
				if(estadoAtual.equalsTO(estado)){
					throw new NegocioException("Não houve alteração!");
				}
			}

			// Setar informações para auditoria
			estado.setAuditoriaData(Uteis.DataHoje());
			estado.setAuditoriaUsuario(Uteis.UsuarioLogado().getUsuario().getDsLogin());


			return this.estadoDAO.salvar(estado);
		} catch (Exception e) {
			//e.printStackTrace();
			throw new NegocioException("Erro ao salvar Estado: " + e.getMessage(), e.getCause());
		}
	}
}
