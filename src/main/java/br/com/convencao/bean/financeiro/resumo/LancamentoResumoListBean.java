package br.com.convencao.bean.financeiro.resumo;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.convencao.bean.cadastro.RegiaoItens;
import br.com.convencao.bean.financeiro.LancamentoCodbehind;
import br.com.convencao.bo.LancamentoBO;
import br.com.convencao.bo.LancamentoResumoBO;
import br.com.convencao.bo.LancamentoSaidaBO;
import br.com.convencao.bo.NegocioException;
import br.com.convencao.model.LancamentoResumo;
import br.com.convencao.model.Regiao;
import br.com.convencao.util.Uteis;
import br.com.convencao.util.jsf.FacesUtil;

@Named(value = "lancamentoResumoList")
@ViewScoped
public class LancamentoResumoListBean extends LancamentoCodbehind  {

	private static final long serialVersionUID = 1L;

	private List<LancamentoResumo> lista;

	private LancamentoResumo selecionado;

	@Inject
	private LancamentoResumoBO bo;

	@Inject
	private LancamentoBO lancamentoBO;

	@Inject
	private LancamentoSaidaBO lancamentoSaidaBO;

	@Inject
	private RegiaoItens regiaoItensFiltro;

	@Inject
	private RegiaoItens regiaoItensFiltroNovoPeriodo;

	private boolean flInicializarDtFecharPeriodo = true;
	private boolean flDtFimPeriodo = true; 
	private boolean flExibirFecharControle;
	private LocalDate dtFecharPeriodo;
	
	public void listar(){
		this.lista = new ArrayList<>();
		if(!this.validarPreenchimentoComboRegiao()) {
			throw new NegocioException("Região: preenchimento obrigatório!");
		}

		this.lista = bo.findAllPorRegiao(regiaoItensFiltro.getSqRegiao());

		if(this.lista.size() == 0){
			this.lista = null;
			throw new NegocioException("Nennhum registro encontrado para o filtro selecionado.");
		}

		// Preparar exibição da lista na tela
		flExibirFecharControle = false;
		int ultimoRegistroFechado = -1;
		for (int i = 0; i < this.lista.size(); i++) {

			if(lista.get(i).getDtFechado() == null) {

				if(i == this.lista.size() - 1) {
					lista.get(i).setFlExibirDataFim(true);
					lista.get(i).setFlExibirExcluir(true);
				}

				if(lista.get(i).getDtFechado() == null && !flExibirFecharControle) {
					lista.get(i).setFlExibirFecharPeriodo(true);
					flExibirFecharControle = true;
				}

			} else {
				ultimoRegistroFechado = i;
			}

		}

		// Permitir reabrir último registro fechado
		if(ultimoRegistroFechado >= 0)
			this.lista.get(ultimoRegistroFechado).setFlExibirReabrirPeriodo(true);
	}

	public void inicializar(){
		this.inicializarRegioes("FIN");
	}

	public void inicializarPeriodo() {
		this.selecionado = null;
	}


	public String getMensagemRodape(){
		return "Total registros encontrado: " + this.lista.size();
	}


	public RegiaoItens getRegiaoItensFiltro() {
		return regiaoItensFiltro;
	}

	public void setRegiaoItensFiltro(RegiaoItens regiaoItensFiltro) {
		this.regiaoItensFiltro = regiaoItensFiltro;
	}

	public RegiaoItens getRegiaoItensFiltroNovoPeriodo() {
		return regiaoItensFiltroNovoPeriodo;
	}

	public void setRegiaoItensFiltroNovoPeriodo(RegiaoItens regiaoItensFiltroNovoPeriodo) {
		this.regiaoItensFiltroNovoPeriodo = regiaoItensFiltroNovoPeriodo;
	}

	public List<LancamentoResumo> getLista() {
		return lista;
	}

	public void setLista(List<LancamentoResumo> lista) {
		this.lista = lista;
	}

	public LancamentoResumo getSelecionado() {
		return selecionado;
	}

	public void setSelecionado(LancamentoResumo selecionado) {
		this.selecionado = selecionado;
	}

	public boolean isFlDtFimPeriodo() {
		return flDtFimPeriodo;
	}

	public LocalDate getDtFecharPeriodo() {
		if(this.flInicializarDtFecharPeriodo) {
			this.flInicializarDtFecharPeriodo = false;
			this.dtFecharPeriodo = LocalDate.now();
		}
		return  dtFecharPeriodo;
	}

	public void setDtFecharPeriodo(LocalDate dtFecharPeriodo) {
		this.dtFecharPeriodo = dtFecharPeriodo;
	}

	// Validar preenchimento da combo Regiao
	public boolean validarPreenchimentoComboRegiao() {
		if(this.getRegiaoItensFiltro() == null || this.getRegiaoItensFiltro().getSqRegiao() == null || this.getRegiaoItensFiltro().getSqRegiao() < 1) {
			return false;
		}

		return true;
	}

	// Salvar novo periodo fianceiro para uma regiao
	public void salvarNovoPeriodo() {

		// Data fim de periodo deve estar preenchida.
		if(!flDtFimPeriodo) throw new NegocioException("Antes de criar novo período, preencha da Data Fim do período atual");

		// Gravar novo periodo
		this.selecionado = bo.registrarNovoPeriodo(this.selecionado);

		//Exibir mensagem
		FacesUtil.addInfoMessage("Novo período gravado com sucesso para região: " + this.selecionado.getRegiao().getDsRegiao());

		// Fechar a tela de Dialog
		this.fecharDialogoPrimeFaces(true);

		// Remontar lista para exibição
		if(this.validarPreenchimentoComboRegiao()) {
			this.listar();
		}
	}

	// Atualizar dados para registrar novo periodo para uma determinada regiao
	public void atualizarDadosNovoPeriodo() {

		this.flDtFimPeriodo = true;

		LancamentoResumo lancamentoResumoTemp;
		this.selecionado = new LancamentoResumo();
		this.selecionado.setVlSaldoAnterior(BigDecimal.ZERO);
		this.selecionado.setVlEntradas(BigDecimal.ZERO);
		this.selecionado.setVlSaidas(BigDecimal.ZERO);
		this.selecionado.setVlSaldoAtual(BigDecimal.ZERO);
		this.selecionado.setRegiao(new Regiao());

		if(this.regiaoItensFiltroNovoPeriodo != null && this.regiaoItensFiltroNovoPeriodo.getSqRegiao() > 0) {
			lancamentoResumoTemp = bo.findUltimoRegistroPorRegiao(this.regiaoItensFiltroNovoPeriodo.getSqRegiao());

			if(lancamentoResumoTemp != null) {
				if(lancamentoResumoTemp.getDtPeriodoFim() != null ) {
					this.selecionado.setDtPeriodoInicio(Uteis.AdcionarDiasNaData(lancamentoResumoTemp.getDtPeriodoFim(), 1));
				}else {
					this.flDtFimPeriodo = false;
				}
				System.out.println("Saldo anterior: " + lancamentoResumoTemp.getVlSaldoAnterior());
				System.out.println("Entradas      : " + lancamentoResumoTemp.getVlEntradas());
				System.out.println("Saidas        : " + lancamentoResumoTemp.getVlSaidas());
				System.out.println("Data Fim Perio: " + lancamentoResumoTemp.getDtPeriodoFim());

				this.selecionado.setVlSaldoAnterior(lancamentoResumoTemp.getVlSaldoAnterior().add(lancamentoResumoTemp.getVlEntradas()).subtract(lancamentoResumoTemp.getVlSaidas()));

			}

			this.selecionado.setVlEntradas(lancamentoBO.findValorTotalEntradasSemPeriodo(this.regiaoItensFiltroNovoPeriodo.getSqRegiao()));
			this.selecionado.setVlSaidas(lancamentoSaidaBO.findValorTotalSaidasSemPeriodo(this.regiaoItensFiltroNovoPeriodo.getSqRegiao()));
			this.selecionado.getRegiao().setSqRegiao(this.regiaoItensFiltroNovoPeriodo.getSqRegiao());

		} else {
			throw new NegocioException("Selecione uma região");
		}

	}

	public void fecharNovoPeriodo() {
		this.inicializarPeriodo();
		this.regiaoItensFiltroNovoPeriodo = null;
		this.flDtFimPeriodo = true;
	}

	// Salvar data fim do periodo
	public void salvarDataFimPeriodo() {

		this.selecionado = bo.updateDataFimPeriodo(this.selecionado);

		FacesUtil.addInfoMessage("Data Fim Período processada com sucesso.");

		// Fechar a tela de Dialog
		this.fecharDialogoPrimeFaces(true);

		// Remontar lista para exibição
		this.listar();
	}

	// Excluir registro de resumo
	public void excluir() {

		bo.remover(this.selecionado);

		FacesUtil.addInfoMessage("Registro excluido com sucesso.");

		// Remontar lista para exibição
		this.listar();
	}

	// Fechar periodo
	public void salvarFecharPeriodo() {

		bo.updateFecharPeriodo(this.dtFecharPeriodo, this.selecionado);

		FacesUtil.addInfoMessage("Período fechado com sucesso.");

		// Fechar a tela de Dialog
		this.fecharDialogoPrimeFaces(true);

		// Remontar lista para exibição
		this.listar();
	}

	// Reabrir periodo
	public void salvarReabrirPeriodo() {

		bo.updateReabrirPeriodo(this.selecionado);

		FacesUtil.addInfoMessage("Período reaberto com sucesso.");

		// Fechar tela de Dialog
		this.fecharDialogoPrimeFaces(true);

		// Remontar lista para exibir
		this.listar();
	}

	// Recuperar parametros para reexibir tela
	public void recuperarParametros(){
		if(this.getParam_controle() != null && this.getParam_controle().equals("1")){
			this.setParam_controle(null);

			RegiaoItens itens = null;


			if(!this.getParam_regiaoItens_sqRegiao().equals("null")){
				itens = new RegiaoItens();
				itens.setSqRegiao(Long.parseLong(this.getParam_regiaoItens_sqRegiao()));
			}

			this.setRegiaoItensFiltro(itens);

			this.listar();

		}
	}
}