package br.com.convencao.bean.financeiro.entrada;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.convencao.bean.financeiro.LancamentoCodbehind;
import br.com.convencao.bo.LancamentoBO;
import br.com.convencao.model.FormaPagamento;
import br.com.convencao.model.FormaRecebimento;
import br.com.convencao.model.Igreja;
import br.com.convencao.model.Lancamento;
import br.com.convencao.model.PlanoConta;
import br.com.convencao.model.Recibo;
import br.com.convencao.model.to.FormaRecebimentoPorReciboCpl;
import br.com.convencao.model.to.LancamentoEntradaCpl;
import br.com.convencao.util.Uteis;
import br.com.convencao.util.jsf.FacesUtil;

@Named(value = "lancamentoEntradaIgrejaOutrosList")
@ViewScoped
public class LancamentoEntradaIgrejaOutrosListBean extends LancamentoCodbehind {

	private static final long serialVersionUID = 1L;

	private Lancamento lancamentoEntrada;
	private PlanoConta planoConta;

	private List<LancamentoEntradaCpl> listar;

	private LancamentoEntradaCpl selecionado;

	@Inject
	private LancamentoBO lancamentoBO;

	private boolean flMinistro;
	private boolean flIgreja;
	private boolean flOutros;

	private BigDecimal vlTotalEntradas;

	private boolean flControleListarLancamento;
	private boolean flControleAutalizarIgrejaOutros;

	@Inject
	private Igreja igrejaFiltro;

	public void inicializar() {

		if (!this.flControleAutalizarIgrejaOutros) {
			this.flIgreja = true;
			this.flOutros = true;
			this.flControleAutalizarIgrejaOutros = true;
		}

		this.vlTotalEntradas = BigDecimal.ZERO;
		if (!this.flControleListarLancamento) {
			this.listarLancamentosEntrada();
			this.flControleListarLancamento = true;
		}
	}

	private void listarLancamentosEntrada() {
		StringBuilder dsCodOrigem = new StringBuilder();

		// Prepara para consultar recebimentos somente de Ministros ou somente de
		// Igrejas ou Somente de Outros ou tudo.
		// dsCodOrigem = 1 (Recebimento de Ministros) - dsCodOrigem = 2 (Recebimento de
		// Igrejas) - dsCodOrigem = 3 (Recebimento de Outros)
		if (flMinistro)
			dsCodOrigem.append("1");
		if (flIgreja)
			dsCodOrigem.append("2");
		if (flOutros)
			dsCodOrigem.append("3");

		this.listar = lancamentoBO.findAllPorResumo(this.getLancamentoResumo().getSqResumo(), dsCodOrigem);

		for (LancamentoEntradaCpl entradas : listar) {
			this.vlTotalEntradas = this.vlTotalEntradas.add(entradas.getVlPagamento());
		}
	}

	// Buscar lancamento de uma entrada e as formas de pagamento, para exibir na
	// consulta de entradas
	public void buscarEntrada(boolean flSoComValores) {
		this.lancamentoEntrada = lancamentoBO.findByPrimaryKey(this.selecionado.getSqLancamento());
		this.inicializarFormaRecebimentoPorRecibo(this.lancamentoEntrada.getRecibo().getSqRecibo(), flSoComValores);

		this.setVlTotal(BigDecimal.ZERO);
		for (FormaRecebimentoPorReciboCpl frc : this.getFormaRecebimentoPorReciboCpl()) {
			this.setVlTotal(this.getVlTotal().add(frc.getVlContabil()));
		}
	}

	public void pesquisar() {
		this.flControleListarLancamento = false;
	}

	public void iniciarIncluirEntrada() {
		// Inicializar combo de igrejas e Plano de Contas do tipo Crédito
		this.inicializarIgrejas(-1L, true);
		this.inicializarPlanoContasPorTipo("C");

		// Inicializar LancamentosEntrada
		this.lancamentoEntrada = new Lancamento();
		this.lancamentoEntrada.setIgreja(new Igreja());
		this.lancamentoEntrada.setPlanoConta(new PlanoConta());
		this.lancamentoEntrada.setRecibo(new Recibo());

		// Atribuir data atual para data de pagamento
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

	public void inicializarAlterarEntrada(boolean flSoComValores) {
		// Inicializar combo de igrejas e Plano de Contas do tipo Crédito
		this.inicializarIgrejas(-1L, true);
		this.inicializarPlanoContasPorTipo("C");

		// Buscar a entrada para alterar
		this.buscarEntrada(flSoComValores);

		// Selecionar flImprimirRecibo
		this.setFlImprimirRecibo(true);
	}

	// Incluir nova entrada de Igreja ou Outros
	public void salvarIncluirEntrada() {

		Recibo recibo = lancamentoBO.salvarIncluirIgrejaOutros(this.lancamentoEntrada, this.getFormaRecebimento(),
				this.getLancamentoResumo());

		if(this.isFlImprimirRecibo())
			this.imprimirReciboGenerico(recibo.getSqRecibo(), this.lancamentoEntrada.getCdOrigem());

		FacesUtil.addInfoMessage("Registro incluido com sucesso.");

		// Fechar a tela de Dialog
		this.fecharDialogoPrimeFaces(true);

		// Ajustar controles para remontar lista de lancamentos
		this.flControleListarLancamento = false;

		// Buscar lancamento de resumo
		this.buscarLancamentoResumo();
	}

	// Alterar registro de entrada de Igreja ou Outros
	public void salvarAlterarEntrada() {
		lancamentoBO.salvarAlterarIgrejaOutros(this.lancamentoEntrada, this.getFormaRecebimentoPorReciboCpl(),
				this.getLancamentoResumo());

		if(this.isFlImprimirRecibo())
			this.imprimirReciboGenerico(lancamentoEntrada.getRecibo().getSqRecibo(), lancamentoEntrada.getCdOrigem());

		FacesUtil.addInfoMessage("Registro alterado com sucesso.");

		// Fechar a tela de Dialog
		this.fecharDialogoPrimeFaces(true);

		// Ajustar controles para remontar lista de lancamentos
		this.flControleListarLancamento = false;

		// Buscar lancamento de resumo
		this.buscarLancamentoResumo();
	}

	public void salvarExcluir() {
		lancamentoBO.salvarExcluirIgrejaOutros(this.selecionado, this.getLancamentoResumo().getSqResumo());

		FacesUtil.addInfoMessage("Registro excluido com sucesso.");

		// Ajustar controles para remontar lista de lancamentos
		this.flControleListarLancamento = false;

		// Buscar lancamento de resumo
		this.buscarLancamentoResumo();
	}

	public void imprimirRecibo() {
		this.imprimirReciboGenerico(this.lancamentoEntrada.getRecibo().getSqRecibo(), this.lancamentoEntrada.getCdOrigem());

	}

	public List<LancamentoEntradaCpl> getListar() {
		return listar;
	}

	public void setListar(List<LancamentoEntradaCpl> listar) {
		this.listar = listar;
	}

	public LancamentoEntradaCpl getSelecionado() {
		return selecionado;
	}

	public void setSelecionado(LancamentoEntradaCpl selecionado) {
		this.selecionado = selecionado;
	}

	public Lancamento getLancamentoEntrada() {
		return lancamentoEntrada;
	}

	public void setLancamentoEntrada(Lancamento lancamentoEntrada) {
		this.lancamentoEntrada = lancamentoEntrada;
	}

	public boolean isFlMinistro() {
		return flMinistro;
	}

	public void setFlMinistro(boolean flMinistro) {
		this.flMinistro = flMinistro;
	}

	public boolean isFlIgreja() {
		return flIgreja;
	}

	public void setFlIgreja(boolean flIgreja) {
		this.flIgreja = flIgreja;
	}

	public boolean isFlOutros() {
		return flOutros;
	}

	public void setFlOutros(boolean flOutros) {
		this.flOutros = flOutros;
	}

	public BigDecimal getVlTotalEntradas() {
		return vlTotalEntradas;
	}

	public void setVlTotalEntradas(BigDecimal vlTotalEntradas) {
		this.vlTotalEntradas = vlTotalEntradas;
	}

	public Igreja getIgrejaFiltro() {
		return igrejaFiltro;
	}

	public void setIgrejaFiltro(Igreja igrejaFiltro) {
		this.igrejaFiltro = igrejaFiltro;
	}

	public PlanoConta getPlanoConta() {
		return planoConta;
	}

	public void setPlanoConta(PlanoConta planoConta) {
		this.planoConta = planoConta;
	}

	public String getMensagemRodape() {
		return "Total registros encontrado: " + this.listar.size();
	}

}