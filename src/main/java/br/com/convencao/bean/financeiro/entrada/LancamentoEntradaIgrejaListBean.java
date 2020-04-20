package br.com.convencao.bean.financeiro.entrada;

import java.math.BigDecimal;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.convencao.bean.financeiro.LancamentoCodbehind;
import br.com.convencao.bo.LancamentoBO;
import br.com.convencao.model.Lancamento;
import br.com.convencao.model.to.FormaRecebimentoPorReciboCpl;
import br.com.convencao.model.to.LancamentoEntradaCpl;

@Named(value = "lancamentoEntradaIgrejaListBean")
@ViewScoped
public class LancamentoEntradaIgrejaListBean extends LancamentoCodbehind {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private LancamentoBO lancamentoBO;
	
	private List<LancamentoEntradaCpl> lancamentosEntradaIgrejaList;
	private Lancamento lancamentoEntrada;
	private LancamentoEntradaCpl selecionado;
	
	
	public void inicializar() {
		// Listar pagamentos feitos por Igrejas, registrados na data atual e pelo usuário logado
		this.lancamentosEntradaIgrejaList = lancamentoBO.findAllPagamentosIgrejaUsuario();
	}
	
	public List<LancamentoEntradaCpl> getLancamentosEntradaIgrejaList() {
		return lancamentosEntradaIgrejaList;
	}
	
	// Buscar lancamento de uma entrada e as formas de pagamento, para exibir na consulta de entradas
	public void buscarEntrada() {
		this.lancamentoEntrada = lancamentoBO.findByPrimaryKey(this.selecionado.getSqLancamento());
		this.inicializarFormaRecebimentoPorRecibo(this.lancamentoEntrada.getRecibo().getSqRecibo(), true);

		this.setVlTotal(BigDecimal.ZERO);
		for (FormaRecebimentoPorReciboCpl frc : this.getFormaRecebimentoPorReciboCpl()) {
			this.setVlTotal(this.getVlTotal().add(frc.getVlContabil()));
		}
	}
	
	public void imprimirRecibo() {
		this.imprimirReciboGenerico(this.lancamentoEntrada.getRecibo().getSqRecibo(), 2);
	}
	
	public Lancamento getLancamentoEntrada() {
		return lancamentoEntrada;
	}
	
	public void setLancamentoEntrada(Lancamento lancamentoEntrada) {
		this.lancamentoEntrada = lancamentoEntrada;
	}
	
	public LancamentoEntradaCpl getSelecionado() {
		return selecionado;
	}
	
	public void setSelecionado(LancamentoEntradaCpl selecionado) {
		this.selecionado = selecionado;
	}
}