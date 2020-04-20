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

@Named(value = "lancamentoEntradaRecebimentoRetriveBean")
@ViewScoped
public class LancamentoEntradaRecebimentoRetriveBean extends LancamentoCodbehind {

	private static final long serialVersionUID = 1L;

	private List<RegLancamentoCpl> regLancamentosCplPorNSU;
	private List<FormaRecebimentoPorReciboCpl> formaRecebimentoCpl;
	private BigDecimal vlTotalRecebido = BigDecimal.ZERO;
	private String dsRegiao;
	private Recibo recibo;
	private boolean flImprimindo;

	@Inject
	private RegLancamentoBO regLancamentoBO;

	@Inject
	private FormaRecebimentoBO formaRecebimentoBO;

	@Inject
	private ReciboBO reciboBO;


	public void inicializar() {

		if(!flImprimindo) {
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
			
			this.setParam_controleAtualizarDados("1");
		}
	}
	
	public void imprimirRecibo() {
		this.imprimirReciboGenerico(recibo.getSqRecibo(), 1); //Parametro 1 indica que trata-se de recibo de Ministro
	
		this.flImprimindo = false;
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

	public boolean isFlImprimindo() {
		return flImprimindo;
	}

	public void setFlImprimindo(boolean flImprimindo) {
		this.flImprimindo = flImprimindo;
	}
}