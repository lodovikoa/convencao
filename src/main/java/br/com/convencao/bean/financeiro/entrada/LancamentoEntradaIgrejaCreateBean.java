package br.com.convencao.bean.financeiro.entrada;

import java.math.BigDecimal;
import java.util.ArrayList;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.convencao.bean.cadastro.RegiaoItens;
import br.com.convencao.bean.financeiro.LancamentoCodbehind;
import br.com.convencao.bo.LancamentoBO;
import br.com.convencao.bo.NegocioException;
import br.com.convencao.model.FormaPagamento;
import br.com.convencao.model.FormaRecebimento;
import br.com.convencao.model.Igreja;
import br.com.convencao.model.Lancamento;
import br.com.convencao.model.PlanoConta;
import br.com.convencao.model.Recibo;
import br.com.convencao.model.Regiao;
import br.com.convencao.util.Uteis;
import br.com.convencao.util.jsf.FacesUtil;

@Named(value = "lancamentoEntradaIgrejaCreateBean")
@ViewScoped
public class LancamentoEntradaIgrejaCreateBean extends LancamentoCodbehind {

	private static final long serialVersionUID = 1L;

	@Inject
	private Lancamento lancamentoEntrada;

	@Inject
	private LancamentoBO lancamentoBO;

	@Inject
	private RegiaoItens regiaoItemFiltro;

	private boolean isSalvandoRegistro = false;
	private boolean isRegistroSalvo;
	private boolean flInicializar = true;

	public void inicializar() {
		if(flInicializar) {
			this.flInicializar = false;
			if(!this.isFlCalculandoTotalRecebido() && !this.isSalvandoRegistro) {
				// Inicializar combo de igrejas e Plano de Contas do tipo Crédito
				this.inicializarIgrejas(-1L, true);
				this.inicializarPlanoContasPorTipo("C");
				this.inicializarRegioes("PAG");
				if(this.getRegiaoItens().size() == 1) {
					this.regiaoItemFiltro.setSqRegiao(this.getRegiaoItens().get(0).getSqRegiao());
				}

				this.lancamentoEntrada.setRecibo(new Recibo());
				this.lancamentoEntrada.getRecibo().setRegiao(new Regiao());
				this.lancamentoEntrada.setIgreja(new Igreja());
				this.lancamentoEntrada.setPlanoConta(new PlanoConta());
				this.lancamentoEntrada.setDtPagamento(Uteis.DataHoje().toLocalDate());

				// Inicializar formas de pagamento
				this.setFormaRecebimento(new ArrayList<>());


				FormaRecebimento frec;

				this.inicializarFormaPagamento();

				for (FormaPagamento fp : this.getFormaPagamento()) {
					frec = new FormaRecebimento();
					frec.setVlRecebido(BigDecimal.ZERO);
					frec.setFormaPagamento(fp);

					this.getFormaRecebimento().add(frec);
				}



				// Zerar valor total dos recebimentos
				this.setVlTotal(BigDecimal.ZERO);

				// Selecionar flImprimirRecibo
				this.setFlImprimirRecibo(true);
			}
		}
	}


	public void salvarIncluirEntrada() {
		this.isSalvandoRegistro = true;

		// Atribuir a regiao ao Lancamento.
		if(this.regiaoItemFiltro != null)
			this.lancamentoEntrada.getRecibo().getRegiao().setSqRegiao(this.regiaoItemFiltro.getSqRegiao());
		else
			throw new NegocioException("Faltou preencher a Região");

		// Fazer validações e registro da entrada
		Recibo recibo = lancamentoBO.salvarIncluirIgrejaOutros(this.lancamentoEntrada, this.getFormaRecebimento(), null);

		this.isRegistroSalvo = true;

		// Imprimir recibo
		if(this.isFlImprimirRecibo())
			this.imprimirReciboGenerico(recibo.getSqRecibo(), this.lancamentoEntrada.getCdOrigem());

		// Mensagem de sucesso do registro de entrada
		FacesUtil.addInfoMessage("Registro incluido com sucesso.");
	}


	public Lancamento getLancamentoEntrada() {
		return lancamentoEntrada;
	}

	public void setLancamentoEntrada(Lancamento lancamentoEntrada) {
		this.lancamentoEntrada = lancamentoEntrada;
	}

	public RegiaoItens getRegiaoItemFiltro() {
		return regiaoItemFiltro;
	}

	public void setRegiaoItemFiltro(RegiaoItens regiaoItemFiltro) {
		this.regiaoItemFiltro = regiaoItemFiltro;
	}

	public boolean isSalvandoRegistro() {
		return isSalvandoRegistro;
	}

	public void setSalvandoRegistro(boolean isSalvandoRegistro) {
		this.isSalvandoRegistro = isSalvandoRegistro;
	}

	public boolean isRegistroSalvo() {
		return isRegistroSalvo;
	}

	public void setRegistroSalvo(boolean isRegistroSalvo) {
		this.isRegistroSalvo = isRegistroSalvo;
	}
}