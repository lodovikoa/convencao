package br.com.convencao.bo;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.PersistenceException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import br.com.convencao.dao.CargoDAO;
import br.com.convencao.model.Cargo;
import br.com.convencao.util.Uteis;
import br.com.convencao.util.jpa.Transactional;

public class CargoBO implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private static Log log = LogFactory.getLog(CargoBO.class);
	
	@Inject
	CargoDAO cargoDAO;

	// Listar todos os registros
	public List<Cargo> findAll(){
		log.info("findAll()");
		List<Cargo> cargo = cargoDAO.findAll(Cargo.class);
		
	//	Collections.sort(cargo);
		return cargo;
	}
	
	// Buscar um registro pela chave  primaria
	public Cargo findByPrimaryKey(Long sqCargo){
		log.info("findByPrimaryKey(" + sqCargo + ")");
		return cargoDAO.findByPrimaryKey(Cargo.class, sqCargo);
		
	}
	
	// Buscar regisgro pel primary key usando find
	public Cargo find(Long sq) {
		log.info("find(" + sq + ")");
		return cargoDAO.find(Cargo.class, sq);
	}

	// Salvar novo e alteração de registro
	@Transactional
	public Cargo salvar(Cargo cargo) {
		try {
			log.info("salvar(" + cargo.getSqCargo() + ")");
			// Retirar espaços de inicio e fim
			cargo.setDsCargo(cargo.getDsCargo().trim());
			
			if(cargo.getDsCargo().length() < 2)
				throw new NegocioException("Cargo precisa ser preenchido corretamente!");
			
			if(cargo.getDsTitulo() != null) cargo.setDsTitulo(cargo.getDsTitulo().trim());


			// Validar descrição única
			Cargo cargoExistente = cargoDAO.findByPorAtributo(Cargo.class, "dsCargo", cargo.getDsCargo());
			if(cargoExistente != null && !cargoExistente.getSqCargo().equals(cargo.getSqCargo()) ){
				throw new NegocioException("Cargo já está cadastrada");
			}

			// Verificar se houve alteração
			if(cargo.getSqCargo() != null){
				Cargo cargoAtual = cargoDAO.findByPrimaryKey(Cargo.class, cargo.getSqCargo());
				if(cargoAtual.equals(cargo)){
					throw new NegocioException("Não houve alteração!");
				}
			}

			// Setar informações para auditoria
			cargo.setAuditoriaData(Uteis.DataHoje());
			cargo.setAuditoriaUsuario(Uteis.UsuarioLogado().getUsuario().getDsLogin());


			return this.cargoDAO.salvar(cargo);
		} catch (Exception e) {
			e.printStackTrace();
			throw new NegocioException("Erro ao salvar cargo: " + e.getMessage());
		}

	}

	// Excluir registro
	@Transactional
	public void remover(Cargo cargo) {
		try{
			log.info("remover(" + cargo.getSqCargo() + ")");
			// TODO  Validar exclusão de cargo que está cadastrada em Ministro

			cargo = cargoDAO.findByPrimaryKey(Cargo.class, cargo.getSqCargo());
			cargoDAO.delete(cargo, cargo.getSqCargo());
			
		} catch (PersistenceException e) {
			throw new NegocioException("Cargo não pode ser removido.", e);
		}

	}

}
