package br.com.convencao.bean.financeiro.saida;

import java.math.BigDecimal;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.convencao.bean.financeiro.LancamentoCodbehind;
import br.com.convencao.bo.LancamentoSaidaBO;
import br.com.convencao.model.LancamentoSaida;
import br.com.convencao.model.TipoPagamento;
import br.com.convencao.util.Uteis;
import br.com.convencao.util.jsf.FacesUtil;

@Named(value = "lancamentoSaidaList")
@ViewScoped
public class LancamentoSaidaListBean extends LancamentoCodbehind {

	private static final long serialVersionUID = 1L;

	private List<LancamentoSaida> listar;
	private LancamentoSaida selecionado;
	private LancamentoSaida lancamentoSaida;
	
	private int cdFormaPagamento;
			
	
	@Inject
	private LancamentoSaidaBO lancamentoSaidaBO;

	private BigDecimal vlTotalSaidas;


	public void inicializar() {
		this.buscarListaLancamentosSaida();
	}
	
	public void inicializarIncluirSaida() {
		this.inicializarDepartamentos("cadastroSaidas");
		this.inicializarPlanoContasPorTipo("D");
		
		this.lancamentoSaida = new LancamentoSaida();
		this.lancamentoSaida.setDtSaida(Uteis.DataHoje().toLocalDate());

	}
	
	public void inicializarAlterarSaida() {
		this.inicializarDepartamentos("cadastroSaidas");
		this.inicializarPlanoContasPorTipo("D");
		
		this.buscarLancamentoSaida();
		
		// Ajustar forma de pagamento para exibição na tela
		for (TipoPagamento tp : TipoPagamento.values()) {
			if(tp.getLabel() == this.selecionado.getCdFormaPagamento()) {
				this.cdFormaPagamento = tp.getLabel();
				break;
			}
		}
	}
	
	public void inicializarConsultarSaida() {
		this.buscarLancamentoSaida();
	}
	
	public void salvarIncluirSaida() {
	
		this.lancamentoSaida.setCdFormaPagamento(this.cdFormaPagamento);
		
		lancamentoSaidaBO.salvarIncuirSaida(this.lancamentoSaida, this.getLancamentoResumo());
		
		FacesUtil.addInfoMessage("Registro salvo com sucesso!");
		
		// Fechar a tela de Dialog
		this.fecharDialogoPrimeFaces(true);
		
		this.buscarLancamentoResumo();
	}
	
	public void salvarAlterarSaida() {
		
		this.selecionado.setCdFormaPagamento(this.cdFormaPagamento);
		
		lancamentoSaidaBO.salvarAlterarSaida(this.selecionado);
		
		FacesUtil.addInfoMessage("Registro salvo com sucesso!");
		
		// Fechar a tela de Dialog
		this.fecharDialogoPrimeFaces(true);
		
		this.buscarLancamentoResumo();
		
	}
	
	public void salvarExcluir() {
		lancamentoSaidaBO.salvarExcluirSaida(this.selecionado);

		FacesUtil.addInfoMessage("Registro excluido com sucesso.");

		// Buscar lancamento de resumo
		this.buscarLancamentoResumo();
	}
		


	private void buscarLancamentoSaida() {
		this.selecionado = lancamentoSaidaBO.findByPrimaryKey(selecionado.getSqSaida());
	}
	
	private void buscarListaLancamentosSaida() {
		this.listar = this.lancamentoSaidaBO.findAllPorPeriodo(this.getLancamentoResumo().getSqResumo());
		
		this.vlTotalSaidas = BigDecimal.ZERO;
		
		for (LancamentoSaida toSaidas : listar) {
			this.vlTotalSaidas = this.vlTotalSaidas.add(toSaidas.getVlSaida());
		}
	}
	
	
	public List<LancamentoSaida> getListar() {
		return listar;
	}
	
	public void setListar(List<LancamentoSaida> listar) {
		this.listar = listar;
	}
	
	public BigDecimal getVlTotalSaidas() {
		return vlTotalSaidas;
	}
	
	public void setVlTotalSaidas(BigDecimal vlTotalSaidas) {
		this.vlTotalSaidas = vlTotalSaidas;
	}
	
	public LancamentoSaida getSelecionado() {
		return selecionado;
	}
	
	public void setSelecionado(LancamentoSaida selecionado) {
		this.selecionado = selecionado;
	}
	
	public LancamentoSaida getLancamentoSaida() {
		return lancamentoSaida;
	}
	
	public void setLancamentoSaida(LancamentoSaida lancamentoSaida) {
		this.lancamentoSaida = lancamentoSaida;
	}
	
	public int getCdFormaPagamento() {
		return cdFormaPagamento;
	}
	
	public void setCdFormaPagamento(int cdFormaPagamento) {
		this.cdFormaPagamento = cdFormaPagamento;
	}
}