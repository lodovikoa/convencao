package br.com.convencao.bo;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.PersistenceException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import br.com.convencao.dao.ConvencaoDAO;
import br.com.convencao.model.Convencao;
import br.com.convencao.util.Uteis;
import br.com.convencao.util.jpa.Transactional;

public class ConvencaoBO implements Serializable {

	private static final long serialVersionUID = 1L;
	private static Log log = LogFactory.getLog(ConvencaoBO.class);

	@Inject
	ConvencaoDAO dao;

	@Inject
	RegiaoBO regiaoBO;

	// Listar todas as convenções
	public List<Convencao> listarConvencoes() {
		log.info("listarConvencoes())");
		List<Convencao> convencao = dao.listarConvencoes();
		//		Collections.sort(convencao);
		return convencao;
	}

	// Buscar Convenção pela chave primária
	public Convencao findByPrimaryKey(Long sq) {
		log.info("findByPrimaryKey(" + sq + ")");
		return dao.findByPrimaryKey(sq);
	}
	
	// Buscar convenção pela primary key usando find.
	public Convencao find(Long sq) {
		log.info("find(" + sq + ")");
		return dao.find(Convencao.class, sq);
	}

	// Salvar inclusão e alteração de Região
	@Transactional
	public Convencao salvar(Convencao registro){
		try {

			log.info("salvar(" + registro.getSqConvencao() + ")");

//			// Retirar caracteres não numéricos do CNPJ
//			registro.setDsCnpj(Uteis.OnlyNumbers(registro.getDsCnpj()));

//			// Retirar caracteres não numéricos do CEP
//			registro.setDsCep(Uteis.OnlyNumbers(registro.getDsCep()));

			// Setar informações para auditoria
			registro.setAuditoriaData(Uteis.DataHoje());
			registro.setAuditoriaUsuario(Uteis.UsuarioLogado().getUsuario().getDsLogin());

			// Validar CNPJ é único.
			Convencao registroExistente = dao.findByPorAtributo(Convencao.class, "dsCnpj", registro.getDsCnpj());
			if(registroExistente != null && !registroExistente.getSqConvencao().equals( registro.getSqConvencao())){
				throw new NegocioException("CNPJ já está cadastrado para convenção (" + registroExistente.getDsReduzido() + ")");
			}

			// Validar nome reduzido é único
			registroExistente = dao.findByPorAtributo(Convencao.class, "dsReduzido", registro.getDsReduzido());
			if(registroExistente != null && !registroExistente.getSqConvencao().equals(registro.getSqConvencao())){
				throw new NegocioException("Nome reduzido já está cadastrado para a convenção (" + registroExistente.getDsConvencao() + ")");
			}

			// Verificar se houve alteração
			if(registro.getSqConvencao() != null){
				Convencao convencaoAtual = dao.findByPrimaryKey(registro.getSqConvencao());
				if(convencaoAtual.equalsTO(registro)){
					throw new NegocioException("Não houve alteração!");
				}
			}



			return this.dao.salvar(registro);

		} catch (Exception e) {
			throw new NegocioException("Erro ao tentar salvar Convencao: " + e.getMessage(), e);
		}
	}


	// Excluir Convenção
	@Transactional
	public void remover(Convencao registro){
		try {
			log.info("remover(" + registro.getSqConvencao() + ")");
			//Validar exclusão - Convenção vinculada à Região não pode ser excluida.		
			Long qtdeLinhas = regiaoBO.qtdePorConvencao(registro.getSqConvencao());
			if(qtdeLinhas > 0){
				throw new NegocioException(" Convenção vinculada à Região. Não permite exclusão!" );
			}

			registro = findByPrimaryKey(registro.getSqConvencao());	
			dao.delete(registro, registro.getSqConvencao());

		} catch (PersistenceException e) {
			//			e.printStackTrace();
			throw new NegocioException("Convenção não pode ser removida.", e.getCause());
		}
	}

}
