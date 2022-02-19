package br.com.convencao.bo;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.PersistenceException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import br.com.convencao.dao.EscolaridadeDAO;
import br.com.convencao.model.Escolaridade;
import br.com.convencao.util.Uteis;
import br.com.convencao.util.jpa.Transactional;

public class EscolaridadeBO implements Serializable{

	private static final long serialVersionUID = 1L;
	private static Log log = LogFactory.getLog(EscolaridadeBO.class);
	
	@Inject
	EscolaridadeDAO escolaridadeDAO;

	// Listar todos os registros
	public List<Escolaridade> findAll(){
		log.info("findAllEscolaridade()");
		
		List<Escolaridade> escolaridade = escolaridadeDAO.findAll(Escolaridade.class);
		
	//	Collections.sort(escolaridade);
		return escolaridade;
	}
	
	// Buscar um registro pela chave  primaria
	public Escolaridade findByPrimaryKey(Long sqEscolaridade){
		log.info("findByPrimaryKey(" + sqEscolaridade + ")");
		return escolaridadeDAO.findByPrimaryKey(Escolaridade.class, sqEscolaridade);
	}
	
	public Escolaridade find(Long sq) {
		log.info("find(" + sq + ")");
		return escolaridadeDAO.find(Escolaridade.class, sq);
	}

	// Salvar novo e alteração de registro
	@Transactional
	public Escolaridade salvar(Escolaridade escolaridade) {
		try {
			log.info("salvar(" + escolaridade.getSqEscolaridade() + ")");

			// Retirar espaços de inicio e fim
			escolaridade.setDsDescricao(escolaridade.getDsDescricao().trim());
			
			if(escolaridade.getDsDescricao().length() < 2)
				throw new NegocioException("Escolaridade precisa ser preenchida corretamente!");


			// Validar descrição única
			Escolaridade escolaridadeExistente = escolaridadeDAO.findByPorAtributo(Escolaridade.class, "dsDescricao", escolaridade.getDsDescricao());
			if(escolaridadeExistente != null && !escolaridadeExistente.getSqEscolaridade().equals(escolaridade.getSqEscolaridade()) ){
				throw new NegocioException("Escolaridade já está cadastrada");
			}

			// Verificar se houve alteração
			if(escolaridade.getSqEscolaridade() != null){
				Escolaridade escolaridadeAtual = escolaridadeDAO.findByPrimaryKey(Escolaridade.class, escolaridade.getSqEscolaridade());
				if(escolaridadeAtual.equals(escolaridade)){
					throw new NegocioException("Não houve alteração!");
				}
			}

			// Setar informações para auditoria
			escolaridade.setAuditoriaData(Uteis.DataHoje());
			escolaridade.setAuditoriaUsuario(Uteis.UsuarioLogado().getUsuario().getDsLogin());


			return this.escolaridadeDAO.salvar(escolaridade);
		} catch (Exception e) {
			//e.printStackTrace();
			throw new NegocioException("Erro ao salvar Escolaridade: " + e.getMessage(), e.getCause());
		}
	}
	
	// Excluir registro
	@Transactional
	public void remover(Escolaridade escolaridade) {
		try{
			log.info("remover(" + escolaridade.getSqEscolaridade() + ")");
			
			// TODO  Validar exclusão de escolaridade que está cadastrada em Ministro
			
			escolaridade = escolaridadeDAO.findByPrimaryKey(Escolaridade.class, escolaridade.getSqEscolaridade());
			escolaridadeDAO.delete(escolaridade, escolaridade.getSqEscolaridade());
			
		} catch (PersistenceException e) {
			e.printStackTrace();
			throw new NegocioException("Escolaridade não pode ser removido.");
		}

	}
}
