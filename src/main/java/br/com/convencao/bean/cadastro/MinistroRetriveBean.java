package br.com.convencao.bean.cadastro;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import br.com.convencao.model.Ministro;

@Named(value = "ministroRetriveBean")
@ViewScoped
public class MinistroRetriveBean extends MinistroCodbehind {

	private static final long serialVersionUID = 1L;

	private Ministro ministro;
	
	public void inicializar() {
		
		// Exibir todos os lançamentos: Em aberto e pagos
		this.setFlLancamentosEmAberto(true);
		this.setFlLancamentosPagos(true);
		
		this.inicializarLancamentosMinistro(ministro.getSqMinistro());
	}
	
	public Ministro getMinistro() {
		return ministro;
	}
	
	public void setMinistro(Ministro ministro) {
		this.ministro = ministro;
	}
	
	
}