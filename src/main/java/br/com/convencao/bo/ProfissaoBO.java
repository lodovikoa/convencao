package br.com.convencao.bo;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.PersistenceException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import br.com.convencao.dao.ProfissaoDAO;
import br.com.convencao.model.Profissao;
import br.com.convencao.util.Uteis;
import br.com.convencao.util.jpa.Transactional;

public class ProfissaoBO implements Serializable{

	private static final long serialVersionUID = 1L;
	private static Log log = LogFactory.getLog(ProfissaoBO.class);
	
	@Inject
	ProfissaoDAO profissaoDAO;

	// Listar todos os registros
	public List<Profissao> findAll(){
		log.info("findAll()");
		List<Profissao> profissao = profissaoDAO.findAll(Profissao.class);
		
//		Collections.sort(profissao);
		
		return profissao;
	}
	
	// Buscar um registro pela chave  primaria
	public Profissao findByPrimaryKey(Long sqProfissao){
		log.info("findByPrimaryKey(" + sqProfissao + ")");
		return profissaoDAO.findByPrimaryKey(Profissao.class, sqProfissao);
	}
	
	// Buscar registro pela chave  primaria usando find
	public Profissao find(Long sq) {
		log.info("find(" + sq + ")");
		return profissaoDAO.find(Profissao.class, sq);
	}

	// Salvar novo e alteração de registro
	@Transactional
	public Profissao salvar(Profissao profissao) {
		try {

			log.info("salvar(" + profissao.getSqProfissao() + ")");
			// Retirar espaços de inicio e fim
			profissao.setDsDescricao(profissao.getDsDescricao().trim());
			
			if(profissao.getDsDescricao().length() < 2)
				throw new NegocioException("Profissão precisa ser preenchida corretamente!");


			// Validar descrição única
			Profissao profissaoExistente = profissaoDAO.findByPorAtributo(Profissao.class, "dsDescricao", profissao.getDsDescricao());
			if(profissaoExistente != null && !profissaoExistente.getSqProfissao().equals(profissao.getSqProfissao()) ){
				throw new NegocioException("Profissao já está cadastrada");
			}

			// Verificar se houve alteração
			if(profissao.getSqProfissao() != null){
				Profissao profissaoAtual = profissaoDAO.findByPrimaryKey(Profissao.class, profissao.getSqProfissao());
				if(profissaoAtual.equals(profissao)){
					throw new NegocioException("Não houve alteração!");
				}
			}

			// Setar informações para auditoria
			profissao.setAuditoriaData(Uteis.DataHoje());
			profissao.setAuditoriaUsuario(Uteis.UsuarioLogado().getUsuario().getDsLogin());


			return this.profissaoDAO.salvar(profissao);
		} catch (Exception e) {
			//e.printStackTrace();
			throw new NegocioException("Erro ao salvar Profissão: " + e.getMessage(), e.getCause());
		}
	}
	
	// Excluir registro
	@Transactional
	public void remover(Profissao profissao) {
		try{
			log.info("remover(" + profissao.getSqProfissao() + ")");
			// TODO  Validar exclusão de profissao que está cadastrada em Ministro
			
			profissao = profissaoDAO.findByPrimaryKey(Profissao.class, profissao.getSqProfissao());
			profissaoDAO.delete(profissao, profissao.getSqProfissao());
			
		} catch (PersistenceException e) {
			e.printStackTrace();
			throw new NegocioException("Profissao não pode ser removido.");
		}

	}

}
