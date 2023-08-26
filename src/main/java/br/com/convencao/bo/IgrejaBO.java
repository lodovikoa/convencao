package br.com.convencao.bo;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.PersistenceException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import br.com.convencao.bean.cadastro.IgrejaFiltro;
import br.com.convencao.dao.IgrejaDAO;
import br.com.convencao.dao.MinistroDAO;
import br.com.convencao.model.Igreja;
import br.com.convencao.model.Ministro;
import br.com.convencao.model.Regiao;
import br.com.convencao.util.Uteis;
import br.com.convencao.util.jpa.Transactional;

public class IgrejaBO extends GenericoBO{

	private static final long serialVersionUID = 1L;
	private static Log log = LogFactory.getLog(IgrejaBO.class);
	
	@Inject
	IgrejaDAO dao;
	
	@Inject
	MinistroDAO ministroDAO;

	// Listar todas as igrejas
	public List<Igreja> findAll(){
		log.info("findAll()");
		List<Igreja> igrejas = dao.findAll(Igreja.class);
		
		return igrejas;
	}
	
	// Buscar pela chave primaria
	public Igreja findByPrimaryKey(Long sq){
		log.info("findByPrimaryKey(" + sq + ")");
		return dao.findByPrimaryKey(sq);
	}
	
	public Igreja find(Long sq) {
		log.info("find(" + sq + ")");
		return dao.find(Igreja.class, sq);
	}
	
	// Salvar Inclusao/Alteração de Igreja
	@Transactional
	public Igreja salvar(Igreja registro){
		try{
			log.info("salvar(" + registro.getSqIgreja() + ")");
		
			// Retirar espaços em branco de inicio/fim de campos string
			registro.setDsBairro(registro.getDsBairro() != null? registro.getDsBairro().trim(): null);
			registro.setDsCidade(registro.getDsCidade() != null? registro.getDsCidade().trim(): null);
			registro.setDsEmail(registro.getDsEmail() != null? registro.getDsEmail():  null);
			registro.setDsEndereco(registro.getDsEndereco() != null? registro.getDsEndereco().trim(): null);
			registro.setDsIgreja(registro.getDsIgreja() != null? registro.getDsIgreja().trim():null);
			
			// Validar cnpj e verificar se é único
			if(registro.getDsCnpj().length() == 14){	
				// Verificar se é único
				Igreja registroExistente = dao.findByPorAtributo(Igreja.class, "dsCnpj" , registro.getDsCnpj());
				if(registroExistente != null && !registroExistente.getSqIgreja().equals(registro.getSqIgreja())){
					throw new NegocioException("CNPJ já está cadastrado para igreja (" + registroExistente.getDsIgreja() + ")");
				}
			}

			// Setar informações para auditoria
			registro.setAuditoriaData(Uteis.DataHoje());
			registro.setAuditoriaUsuario(Uteis.UsuarioLogado().getUsuario().getDsLogin());
			
			// Verificar se houve alteração
			if(registro.getSqIgreja() != null){
				Igreja igrejaAtual = dao.findByPrimaryKey(Igreja.class, registro.getSqIgreja());
				if(igrejaAtual.equalsTO(registro)){
					throw new NegocioException("Não houve alteração!");
				}
			}
			
			registro = this.dao.salvar(registro);
					
			return registro;
			
			
		}catch (Exception e) {
			throw new NegocioException("Erro ao tentar salvar Igreja: " + e.getMessage(), e);
		}
	}
	
	// Excluir igreja
	@Transactional
	public void remover(Igreja registro) {
		try{
			log.info("remover(" + registro.getSqIgreja() + ")");
			
			// TODO Validar exclusão de igreja
			
			// Atualizar o registro para que o JPA possa exclui-lo
			registro = this.findByPrimaryKey(registro.getSqIgreja());
			dao.delete(registro, registro.getSqIgreja());
			
		}catch (PersistenceException e) {
			throw new NegocioException("Igreja não pode ser removida.", e.getCause());
		}
		
	}
	
	// Buscar ministro presidente de igrejas por região ou de todas regiões
	public List<Ministro> findPresidenteByRegiao(Long sqRegiao){	
		log.info("findPresidenteByRegiao(" + sqRegiao + ")");
		List<Ministro> result;
		if(sqRegiao == -1){
			result = ministroDAO.findPresidentes();
		}else if(sqRegiao > 0){
			result =  ministroDAO.findPresidenteByRegiao(sqRegiao);
		}else{
			result = new ArrayList<>();
		}
		
		return result;
	}
	
	// Listar Igrejas conforme parametros informados
	public List<Igreja> findByParametros(IgrejaFiltro filtro){
		log.info("findByParametros(IgrejaFiltro filtro)");
		if(filtro.getRegiaoItens() == null)
			throw new NegocioException("Regiao - Preencimento obrigatório!");
		
		List<Igreja> result = dao.findByParametros(filtro);
		
		return result;
	}
	
	// Listar Igrejas de uma região
	public List<Igreja> findAllPorRegiao(Regiao regiao){
		log.info("findAllPorRegiao(" + regiao.getSqRegiao() + ")");
		
		// Se sqRegiao == -1, buscar todas as igrejas
		if(regiao.getSqRegiao() == -1)
			return dao.findAll(regiao);
		else
			return dao.findAllPorRegiao(regiao);
	}

	public List<Igreja> findAllPorRegiaoLiberadasUsuario(List<Long> sqRegioesPermissoes) {
		log.info("findAllPorRegiaoLiberadasUsuario(" + sqRegioesPermissoes.toString() + ")");
		
		return dao.findAllPorRegiaoLiberadasUsuario(sqRegioesPermissoes);
	}
	
	// Buscar Igreja por um atributo do tipo Long
	public Igreja findByPorAtributo(String dsAtributo, Long sqMinistro) {
		Igreja igreja = dao.findByPorAtributo(Igreja.class, dsAtributo, sqMinistro);
		return igreja;
	}
}
