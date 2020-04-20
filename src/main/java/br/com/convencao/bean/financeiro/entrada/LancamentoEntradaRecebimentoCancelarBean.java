package br.com.convencao.bean.financeiro.entrada;

import java.math.BigDecimal;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.convencao.bean.financeiro.LancamentoCodbehind;
import br.com.convencao.bo.FormaRecebimentoBO;
import br.com.convencao.bo.ReciboBO;
import br.com.convencao.bo.RegLancamentoBO;
import br.com.convencao.model.Recibo;
import br.com.convencao.model.to.FormaRecebimentoPorReciboCpl;
import br.com.convencao.model.to.RegLancamentoCpl;
import br.com.convencao.util.jsf.FacesUtil;

@Named(value = "lancamentoEntradaRecebimentoCancelarBean")
@ViewScoped
public class LancamentoEntradaRecebimentoCancelarBean extends LancamentoCodbehind {

	private static final long serialVersionUID = 1L;

	private List<RegLancamentoCpl> regLancamentosCplPorNSU;
	private List<FormaRecebimentoPorReciboCpl> formaRecebimentoCpl;
	private BigDecimal vlTotalRecebido = BigDecimal.ZERO;
	private String dsRegiao;
	private Recibo recibo;
	private boolean flExibirBotaoCancelarRecibo;
	private boolean flControleInterno;

	@Inject
	private RegLancamentoBO regLancamentoBO;

	@Inject
	private FormaRecebimentoBO formaRecebimentoBO;

	@Inject
	private ReciboBO reciboBO;


	public void inicializar() {
		
		this.setParam_controleAtualizarDados("1");
		
		if(!("null".equals(this.getParam_sqMinistro()) || "null".equals(this.getParam_cdNsuAtual()))) {

		if(!this.flControleInterno) {
			this.flExibirBotaoCancelarRecibo = true;

			// Inicializar o Ministro
			this.buscarMinistroRecebimento(Long.parseLong(this.getParam_sqMinistro()));

			// Buscar Lançamentos de um recibo
			this.regLancamentosCplPorNSU = regLancamentoBO.findLancamentosMinistro(Long.parseLong(this.getParam_sqMinistro())
					, Long.parseLong(this.getParam_cdNsuAtual())
					, false
					, false);

			// Preencher a regiao
			if(this.regLancamentosCplPorNSU.size() > 0)
				this.dsRegiao = this.regLancamentosCplPorNSU.get(0).getDsRegiao();

			// Buscar formas de recebimento do recibo
			this.formaRecebimentoCpl = formaRecebimentoBO.findAllReciboCplPorCdNsu(Long.parseLong(this.getParam_cdNsuAtual()), true);

			for (FormaRecebimentoPorReciboCpl fr : formaRecebimentoCpl) {
				vlTotalRecebido = vlTotalRecebido.add(fr.getVlContabil());

			}

			this.recibo = reciboBO.findByPorNsu(Long.parseLong(this.getParam_cdNsuAtual()));
		}
		}

	}

	public void salvarCancelarPagamento() {

		this.recibo = reciboBO.salvarEstornoEntrada(recibo, vlTotalRecebido);

		if(this.recibo.getDtCancelado() != null) {
			this.flExibirBotaoCancelarRecibo = false;
			this.flControleInterno = true;
		}

		FacesUtil.addInfoMessage("Cancelamento feito com sucesso! Favor destruir o recibo número " + this.recibo.getCdNsu()); 
	}


	public List<RegLancamentoCpl> getRegLancamentosCplPorNSU() {
		return regLancamentosCplPorNSU;
	}

	public List<FormaRecebimentoPorReciboCpl> getFormaRecebimentoCpl() {
		return formaRecebimentoCpl;
	}

	public BigDecimal getVlTotalRecebido() {
		return vlTotalRecebido;
	}

	public String getDsRegiao() {
		return dsRegiao;
	}

	public Recibo getRecibo() {
		return recibo;
	}

	public void setRecibo(Recibo recibo) {
		this.recibo = recibo;
	}

	public boolean isFlExibirBotaoCancelarRecibo() {
		return flExibirBotaoCancelarRecibo;
	}

	public void setFlExibirBotaoCancelarRecibo(boolean flExibirBotaoCancelarRecibo) {
		this.flExibirBotaoCancelarRecibo = flExibirBotaoCancelarRecibo;
	}
	
}