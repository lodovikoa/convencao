package br.com.convencao.bean.financeiro.entrada;

import java.math.BigDecimal;
import java.util.ArrayList;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.convencao.bean.cadastro.RegiaoItens;
import br.com.convencao.bean.financeiro.LancamentoCodbehind;
import br.com.convencao.bo.BoletoBO;
import br.com.convencao.bo.LancamentoBO;
import br.com.convencao.bo.NegocioException;
import br.com.convencao.bo.RegLancamentoBO;
import br.com.convencao.model.Boleto;
import br.com.convencao.model.FormaPagamento;
import br.com.convencao.model.FormaRecebimento;
import br.com.convencao.model.Ministro;
import br.com.convencao.model.Recibo;
import br.com.convencao.model.RegLancamento;
import br.com.convencao.model.Regiao;
import br.com.convencao.model.to.RegLancamentoCpl;
import br.com.convencao.util.Uteis;
import br.com.convencao.util.jsf.FacesUtil;

@Named(value = "lancamentoEntradaRecebimentosListBean")
@ViewScoped
public class LancamentoEntradaRecebimentosListBean extends LancamentoCodbehind {

	private static final long serialVersionUID = 1L;

	private RegLancamento regLancamento;

	private RegLancamentoCpl selecionado;

	@Inject
	private RegLancamentoBO regLancamentoBO;

	@Inject
	private LancamentoBO lancamentoBO;

	@Inject
	private RegiaoItens regiaoItensFiltro;

	@Inject
	private BoletoBO boletoBO;

	private Recibo recibo;
	private Boleto boleto;

	private boolean flInicializar = true;
	private boolean flControleAtualizarDados;
	private boolean flControleItemRecebimentoMarcado;
	private String stBoletoRegistrado;
	
	private boolean flExibirReciboEmNome;

	public void inicializar() {

		if(!this.flControleAtualizarDados) {
			if(flInicializar) {
				this.flInicializar = false;

				this.recuperarParametros();

				// Zerar totais
				this.setVlTotal(BigDecimal.ZERO);
				this.setVlTotalLancamento(BigDecimal.ZERO);
				this.setVlTotalPagamento(BigDecimal.ZERO);
				this.setVlTotalRecebimento(BigDecimal.ZERO);
				this.setVlTotalSaldoDevedor(BigDecimal.ZERO);
				this.setVlTroco(BigDecimal.ZERO);
				this.setFlImprimirRecibo(true);

				this.buscarMinistroRecebimento(Long.parseLong(this.getParam_sqMinistro()));
				this.inicializarLancamentosMinistro(this.getMinistroRecebimento().getSqMinistro());
				this.inicializarRegioes("PAG");

				if(this.getRegiaoItens().size() == 1) this.regiaoItensFiltro.setSqRegiao(this.getRegiaoItens().get(0).getSqRegiao());

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

				this.recibo = new Recibo();
				this.recibo.setDtRecibo(Uteis.DataHoje().toLocalDate());
			}
		}
		
		this.flExibirReciboEmNome = this.isFlImprimirRecibo();
	}

	public void inicializarIncluirEntradaMinistro() {
		// Inicializar tipos de lançamento de Cŕedito
		this.inicializarTipoLancamentoPorTipo("C");

		// Inicializar RegLancamento
		this.regLancamento = new RegLancamento();
		this.regLancamento.setDtVencimento(Uteis.DataHoje().toLocalDate());

	}

	public void inializarAlterarEntradaMinistro() {
		// Inicializar tipos de lançamento de crédito
		this.inicializarTipoLancamentoPorTipo("C");

		// Buscar registro de RegLancamento
		this.buscarRegLancamento();
	}

	public void inicializarExcluirLancamento() {
		this.buscarRegLancamento();

		this.boleto = boletoBO.findByRegLancamento(this.regLancamento.getSqRegLancamento());

		this.stBoletoRegistrado =  this.boleto != null? "Há boleto registrado, excluir assim mesmo? URL:" + this.boleto.getDsUrl():"";
	}

	public void inicializarLancamentosMinistro() {
		// Refaz as pesquisas para montar a tela

		this.flControleAtualizarDados = true;
		this.inicializarLancamentosMinistro(this.getMinistroRecebimento().getSqMinistro());
	}

	private void buscarRegLancamento() {
		this.regLancamento = regLancamentoBO.findByPrimaryKey(selecionado.getSqRegLacamento());
	}

	public RegiaoItens getRegiaoItensFiltro() {
		return regiaoItensFiltro;
	}

	public void setRegiaoItensFiltro(RegiaoItens regiaoItensFiltro) {
		this.regiaoItensFiltro = regiaoItensFiltro;
	}

	public Recibo getRecibo() {
		return recibo;
	}

	public void setRecibo(Recibo recibo) {
		this.recibo = recibo;
	}

	public Boleto getBoleto() {
		return boleto;
	}

	public void setBoleto(Boleto boleto) {
		this.boleto = boleto;
	}

	public boolean isFlControleItemRecebimentoMarcado() {
		return flControleItemRecebimentoMarcado;
	}

	public void setFlControleItemRecebimentoMarcado(boolean flControleItemRecebimentoMarcado) {
		this.flControleItemRecebimentoMarcado = flControleItemRecebimentoMarcado;
	}

	public RegLancamento getRegLancamento() {
		return regLancamento;
	}

	public void setRegLancamento(RegLancamento regLancamento) {
		this.regLancamento = regLancamento;
	}

	public RegLancamentoCpl getSelecionado() {
		return selecionado;
	}

	public void setSelecionado(RegLancamentoCpl selecionado) {
		this.selecionado = selecionado;
	}

	public String getStBoletoRegistrado() {
		return stBoletoRegistrado;
	}

	public void calcularTotalRecebido() {

		this.flControleAtualizarDados = true;

		this.setVlTotalRecebimento(BigDecimal.ZERO);

		for (RegLancamentoCpl regCpl : this.getRegLancamentosCpl()) {
			if(regCpl.isFlRealizarPagamento()) {
				this.setVlTotalRecebimento(this.getVlTotalRecebimento().add(regCpl.getVlPagamentoRecebido()));
				this.flControleItemRecebimentoMarcado = true;
			}

		}

		this.calcularTroco();
	}

	public void salvarPagamentos() {
		// Validar preenchimento do recibo
		if(!this.isFlControleItemRecebimentoMarcado())
			throw new NegocioException("Nenhum item marcado para recebimento!");

		if(this.getVlTotalRecebimento().compareTo(BigDecimal.ZERO) <= 0)
			throw new NegocioException("Nenhum valor à receber!");

		if(regiaoItensFiltro == null || regiaoItensFiltro.getSqRegiao() <= 0)
			throw new NegocioException("Região é de preenchimento obrigatório!");

		if(recibo.getDtRecibo() == null)
			throw new NegocioException("Data pagamento é de preenchimento obrigatório!");

		if(this.getVlTotal().compareTo(BigDecimal.ZERO) <= 0)
			throw new NegocioException("Nenhum valor informado em Forma de Pagamento!");	

		// Iniciar e colocar a regiao no recibo
		this.recibo.setRegiao(new Regiao());
		this.recibo.getRegiao().setSqRegiao(this.regiaoItensFiltro.getSqRegiao());


		// Enviar para classe de negocio
		this.recibo = lancamentoBO.salvarIncluirPagamentosMinistros(this.getRegLancamentosCpl(), this.recibo, this.getFormaRecebimento(), this.getVlTotalRecebimento(), this.getVlTroco());

		// Ajustar controles para remontar lista de lancamentos
		this.flControleAtualizarDados = false;
		this.flInicializar = true;
		// this.inicializarLancamentosMinistro();
		
		// Imprimir o recibo
		if(this.isFlImprimirRecibo()) {
			Integer cdNomeIgrejaMinistro = 101;
			if(this.getCdReciboEmNome() == 2) cdNomeIgrejaMinistro = 102;
			this.imprimirReciboGenerico(this.recibo.getSqRecibo(), cdNomeIgrejaMinistro); //cdNomeIgrejaMinistro = 101 Recibo em nome da Igreja - cdNomeIgrejaMinistro = 102 Recibo em nome do Ministro
		}

		FacesUtil.addInfoMessage("Registro concluído com sucesso.");

	}

	public void salvarNovoLancamento() {

		this.regLancamento.setMinistro(new Ministro());
		this.regLancamento.getMinistro().setSqMinistro(this.getMinistroRecebimento().getSqMinistro());

		// Salvar registro
		regLancamentoBO.salvar(this.regLancamento);

		// Fechar a tela de Dialog
		this.fecharDialogoPrimeFaces(true);

		// Ajustar controles para remontar lista de lancamentos
		this.flControleAtualizarDados = false;
		this.inicializarLancamentosMinistro();
		
		FacesUtil.addInfoMessage("Registro incluído com sucesso.");
	}

	public void salvarAlterarLancamento() {

		// Salvar o registro
		regLancamentoBO.salvar(this.regLancamento);

		// Fechar a tela de Dialog
		this.fecharDialogoPrimeFaces(true);

		// Ajustar controles para remontar lista de lancamentos
		this.flControleAtualizarDados = false;
		this.inicializarLancamentosMinistro();

		FacesUtil.addInfoMessage("Registro alterado com sucesso.");
	}

	public void atualizarValorLancamento() {
		this.regLancamento.setVlLancamento(this.regLancamento.getTipoLancamento().getVlTipoLancamento());
	}

	public void salvarExcluirLancamento() {
		// Excluir o registro
		regLancamentoBO.excluir(this.regLancamento, this.boleto);

		this.inicializarLancamentosMinistro();
		FacesUtil.addInfoMessage("Registro excluido com sucesso.");
	}

	public void recuperarParametros() {
		if("1".equals(this.getParam_controleAtualizarDados())) {
			this.flControleAtualizarDados = true;

			this.setFlLancamentosEmAberto(Boolean.parseBoolean(this.getParam_flLancamentosEmAberto()));
			this.setFlLancamentosPagos(Boolean.parseBoolean(this.getParam_flLancamentosPagos()));
		}
	}
	
	public boolean isFlExibirReciboEmNome() {
		return flExibirReciboEmNome;
	}
	
	public void setFlExibirReciboEmNome(boolean flExibirReciboEmNome) {
		this.flExibirReciboEmNome = flExibirReciboEmNome;
	}
}