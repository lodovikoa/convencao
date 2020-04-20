package br.com.convencao.bean.registro;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.convencao.bo.DepartamentoBO;
import br.com.convencao.model.Departamento;
import br.com.convencao.util.jsf.FacesUtil;

@Named(value="departamentoCadastroBean")
@ViewScoped
public class DepartamentoCadastroBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Departamento departamento;
	
	@Inject
	private DepartamentoBO departamentoBO;
	
	public void inicializar() {
		if(this.departamento == null) {
			this.limpar();
		}
	}
	
	private void limpar(){
		this.departamento = new Departamento();
		
	}
	
	public void salvar(){
		this.departamento = this.departamentoBO.salvar(this.departamento);

		FacesUtil.addInfoMessage("Departamento salvo com sucesso!");
	}

	public Departamento getDepartamento() {
		return departamento;
	}

	public void setDepartamento(Departamento departamento) {
		this.departamento = departamento;
	}


	public boolean isEditando(){
		boolean retorno = false;
		if(this.departamento != null)
			retorno = this.departamento.getSqDepartamento()!= null;
		
		return retorno;
	}
	
}
