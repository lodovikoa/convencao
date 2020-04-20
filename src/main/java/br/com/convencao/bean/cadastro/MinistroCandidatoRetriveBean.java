package br.com.convencao.bean.cadastro;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import br.com.convencao.model.Protocolo;

@Named(value = "ministroCandidatoRetriveBean")
@ViewScoped
public class MinistroCandidatoRetriveBean extends MinistroCodbehind {

	private static final long serialVersionUID = 1L;
	
	private Protocolo protocolo;
	
	public void inicializar(String tipo) {
		// Exibir todos os lançamentos: Em aberto e pagos
		this.setFlLancamentosEmAberto(true);
		this.setFlLancamentosPagos(true);
		
		// Ordenar registros de pareceres
		 protocolo.getMinistro().getMinistroParecer().sort((a, b) -> b.getDtRegistro().compareTo(a.getDtRegistro()));
		
		this.inicializarLancamentosMinistro(protocolo.getMinistro().getSqMinistro());
	}
	
	public Protocolo getProtocolo() {
		return protocolo;
	}
	
	public void setProtocolo(Protocolo protocolo) {
		this.protocolo = protocolo;
	}
	
}