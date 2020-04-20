package br.com.convencao.bean.cadastro;

import java.util.ArrayList;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.convencao.bo.IgrejaBO;
import br.com.convencao.bo.MinistroBO;
import br.com.convencao.model.Igreja;
import br.com.convencao.model.Ministro;
import br.com.convencao.util.jsf.FacesUtil;

@Named(value="igrejaCadastroBean")
@ViewScoped
public class IgrejaCadastroBean extends IgrejaCodbehind {

	private static final long serialVersionUID = 1L;
	
	private Igreja igreja;
	
	private List<Ministro> prMinistroIgreja;
	
	@Inject
	private IgrejaBO bo;
	
	@Inject
	private MinistroBO ministroBO;
	
	
	public void inicializar(){
		if(this.igreja == null){
			this.limpar();
		} 

		this.inicializarRegioes("SEC");
		this.inicializarEstados();
		this.inicializarMinistrosIgreja();
	}

	
	public void inicializarMinistrosIgreja() {
		if(this.igreja != null && this.igreja.getSqIgreja() != null){
			this.prMinistroIgreja = ministroBO.findMinistrosByIgrejaByDepartamento(this.igreja.getSqIgreja(), 1L);
		}else{
			this.prMinistroIgreja = new ArrayList<>();
		}
		
	}

	
	private void limpar(){
		this.igreja = new Igreja();
	}
	
	
	public void salvar(){	
		this.igreja = this.bo.salvar(this.igreja);
		
		FacesUtil.addInfoMessage("Igreja salva com sucesso!");
		
	}
	
	public Igreja getIgreja() {
		return igreja;
	}
	
	public void setIgreja(Igreja igreja) {
		this.igreja = igreja;
	}
	
	public boolean isEditando(){
		boolean retorno = false;
		if(this.igreja != null){
			retorno = this.igreja.getSqIgreja() != null;
		}
		
		return retorno;
	}

	public List<Ministro> getPrMinistroIgreja() {
		return prMinistroIgreja;
	}
	
	public void setPrMinistroIgreja(List<Ministro> prMinistroIgreja) {
		this.prMinistroIgreja = prMinistroIgreja;
	}
	
}
