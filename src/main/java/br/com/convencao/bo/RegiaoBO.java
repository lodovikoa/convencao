package br.com.convencao.bo;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.PersistenceException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import br.com.convencao.dao.RegiaoDAO;
import br.com.convencao.model.Regiao;
import br.com.convencao.util.Uteis;
import br.com.convencao.util.jpa.Transactional;

public class RegiaoBO implements Serializable {

	private static final long serialVersionUID = 1L;
	private static Log log = LogFactory.getLog(RegiaoBO.class);

	@Inject
	RegiaoDAO dao; 

	public Long qtdePorConvencao(Long sq){
		log.info("qtdePorConvencao(" + sq + ")");
		return dao.qtdePorAtributo(Regiao.class, "convencao.sqConvencao", sq);
	}

	// Listar todas as regiões //
	public List<Regiao> findAll() {
		log.info("findAll");
		List<Regiao> result = dao.findAll();
	//	Collections.sort(result);
		return result;
	}

	// Buscar Região pela chave primária
	public Regiao findByPrimaryKey(Long sq) {
		log.info("findByPrimaryKey(" + sq + ")");
		return dao.findByPrimaryKey(sq);
	}
	
	// Buscar Região pela chave primária usando find.
	public Regiao find(Long sq) {
		log.info("find(" + sq + ")");
		return dao.find(Regiao.class, sq);
	}
	
	//
	public List<Regiao> findAllPermitidoPorUsuario(List<Long> sqRegioesPermissoes) {
		log.info("findAllPermitidoPorUsuario(" + sqRegioesPermissoes.toString() + ")");
		return dao.findAllPermitidoPorUsuario(sqRegioesPermissoes);
	}
	
	
	// Buscar quantidade de regiões registradas no banco de dados
	public Long findQtdeRegiao() {
		log.info("findQtdeRegiao()");
		return dao.findQtdeRegiao();
	}
	
	// Salvar inclusão e alteração de Região
	@Transactional
	public Regiao salvar(Regiao registro){
		try {
			
			log.info("salvar(" + registro.getSqRegiao() + ")");
			
			// Validar preenchimento da Convencao
			if(null == registro.getConvencao())
				throw new NegocioException("Convenção não foi selecionada.");
			
			// Retirar espaços em branco de inicio e fim de string
			registro.setDsRegiao(registro.getDsRegiao().trim());
			
			// Setar informações para auditoria
			registro.setAuditoriaData(Uteis.DataHoje());
			registro.setAuditoriaUsuario(Uteis.UsuarioLogado().getUsuario().getDsLogin());

	

			// Validar nome regiao é único
			Regiao registroExistente = dao.findByPorAtributo(Regiao.class, "dsRegiao", registro.getDsRegiao());
			if(registroExistente != null && !registroExistente.getSqRegiao().equals(registro.getSqRegiao())){
				throw new NegocioException("Região já está cadastrada");
			}

			// Verificar se houve alteração
			if(registro.getSqRegiao() != null){
				Regiao registroAtual = this.findByPrimaryKey(registro.getSqRegiao());
				if(registroAtual.equalsTO(registro)){
					throw new NegocioException("Não houve alteração!");
				}
			}

			return this.dao.salvar(registro);

		} catch (Exception e) {
			throw new NegocioException("Erro ao tentar salvar Região: " + e.getMessage(), e);
		}
	}


	// Excluir Convenção
	@Transactional
	public void remover(Regiao registro){
		try {
			log.info("remover(" + registro.getSqRegiao() + ")");
			
			//Validar exclusão - A Região está presente em alguma igreja.	
			
			
			registro = this.findByPrimaryKey(registro.getSqRegiao());	
			dao.delete(registro, registro.getSqRegiao());
			
		} catch (PersistenceException e) {
			throw new NegocioException("Região não pode ser removida.", e.getCause());
		}
	}

}
