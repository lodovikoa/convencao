package br.com.convencao.bean.registro;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.convencao.bo.CargoBO;
import br.com.convencao.model.Cargo;
import br.com.convencao.util.jsf.FacesUtil;

@Named(value="cargoCadastroBean")
@ViewScoped
public class CargoCadastroBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Cargo cargo;
	
	@Inject
	private CargoBO cargoBO;
	
	public void inicializar() {
		if(this.cargo == null)
			this.limpar();
	}
	
	private void limpar(){
		this.cargo = new Cargo();
		
	}
	
	public void salvar(){
	
			this.cargo = this.cargoBO.salvar(this.cargo);
			
			FacesUtil.addInfoMessage("Cargo salvo com sucesso!");

	}
	
	public Cargo getCargo() {
		return cargo;
	}
	
	public void setCargo(Cargo cargo) {
		this.cargo = cargo;
	}
	
	public boolean isEditando(){
		boolean retorno = false;
		if(this.cargo != null)
			retorno = this.cargo.getSqCargo() != null;
		
		return retorno;
	}
	
}
