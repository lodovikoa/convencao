package br.com.convencao.bo;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.PersistenceException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import br.com.convencao.dao.DepartamentoDAO;
import br.com.convencao.model.Departamento;
import br.com.convencao.util.Uteis;
import br.com.convencao.util.jpa.Transactional;

public class DepartamentoBO implements Serializable {

	private static final long serialVersionUID = 1L;
	private static Log log = LogFactory.getLog(DepartamentoBO.class);

	@Inject
	private DepartamentoDAO departamentoDAO;
	
	public List<Departamento> findAll(){
		log.info("findAll()");
		List<Departamento> departamento = departamentoDAO.findAll(Departamento.class);
//		Collections.sort(departamento);
		return departamento;
	}
	
	public Departamento findByPrimaryKey(Long sqDepartamento){
		log.info("findByPrimaryKey(" + sqDepartamento + ")");
		return departamentoDAO.findByPrimaryKey(Departamento.class, sqDepartamento);
	}
	
	// Buscar registro pela Primary Key usando find.
	public Departamento find(Long sq) {
		log.info("find(" + sq + ")");
		return departamentoDAO.find(Departamento.class, sq);
	}
	
	@Transactional
	public Departamento salvar(Departamento departamento) {
		try {
			log.info("salvar(" + departamento.getSqDepartamento() + ")");
			//TODO Fazer validações

			// Retirar espaços de inicio/fim de campos string
			departamento.setDsReduzido(departamento.getDsReduzido().trim());
			departamento.setDsDepartamento(departamento.getDsDepartamento().trim());

			// Verificar se houve alteração
			if(departamento.getSqDepartamento() != null){
				Departamento departamentoAtual = departamentoDAO.findByPrimaryKey(Departamento.class, departamento.getSqDepartamento());
				if(departamentoAtual.equalsTO(departamento)){
					throw new NegocioException("Não houve alteração!");
				}
			}

			Departamento departamentoExistente = departamentoDAO.findByPorAtributo(Departamento.class, "dsReduzido", departamento.getDsReduzido());
			if(departamentoExistente != null && !departamentoExistente.getSqDepartamento().equals(departamento.getSqDepartamento())){
				throw new NegocioException("Departamento já está cadastrado para o nome (" + departamentoExistente.getDsDepartamento() + ")");
			}


			// Setar informações para auditoria
			departamento.setAuditoriaData(Uteis.DataHoje());
			departamento.setAuditoriaUsuario(Uteis.UsuarioLogado().getUsuario().getDsLogin());


			return this.departamentoDAO.salvar(departamento);
		} catch (Exception e) {
			//e.printStackTrace();
			throw new NegocioException("Erro ao tentar salvar Departamento: " + e.getMessage(), e);
		}
	}
	
	@Transactional
	public void delete(Departamento departamento) throws NegocioException {
		try{
			log.info("delete(" + departamento.getSqDepartamento() + ")");
			//TODO fazer validação para excluir
			
			departamento =  departamentoDAO.findByPrimaryKey(Departamento.class, departamento.getSqDepartamento());	
			departamentoDAO.delete(departamento, departamento.getSqDepartamento());
			
		} catch (PersistenceException e) {
			e.printStackTrace();
			throw new NegocioException("Departamento não pode ser removido.");
		}
	}
}
