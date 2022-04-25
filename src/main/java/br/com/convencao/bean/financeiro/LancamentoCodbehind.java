package br.com.convencao.bean.financeiro;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import br.com.convencao.bean.codebehind.Codebehind;
import br.com.convencao.bo.DepartamentoBO;
import br.com.convencao.bo.LancamentoResumoBO;
import br.com.convencao.bo.MinistroBO;
import br.com.convencao.bo.NegocioException;
import br.com.convencao.bo.RegLancamentoBO;
import br.com.convencao.model.Departamento;
import br.com.convencao.model.FormaRecebimento;
import br.com.convencao.model.LancamentoResumo;
import br.com.convencao.model.to.FormaRecebimentoPorReciboCpl;
import br.com.convencao.model.to.MinistroRecebimentoCpl;
import br.com.convencao.model.to.RegLancamentoCpl;
import br.com.convencao.util.jsf.FacesUtil;
import br.com.convencao.util.permissao.Permissoes;


public class LancamentoCodbehind extends Codebehind{

	private static final long serialVersionUID = 1L;

	private String param_regiaoItens_sqRegiao;
	private MinistroRecebimentoCpl ministroRecebimento;
	private String param_controle;
	private boolean flImprimirRecibo;
	private Integer cdReciboEmNome = 1; // 1 = Em nome da Igreja  -  2 = Em nome do Ministro
	private String param_igrejaItensFiltro_sqIgreja;
	private String param_departamentoItensFiltro_sqDepartamento;
	private String param_cdRg;
	private String param_nmMinistro;
	private String param_dsCpf;
	private String param_cargo_sqCargo;
	private String param_cdNsu; 
	private String param_cdNsuAtual;
	private String param_dtInicio1;
	private String param_dtFim1;
	private String param_dtInicio2; 
	private String param_dtFim2; 
	private String param_dsCidade;
	private String param_estado_sqEstado;
	private String param_cdSituacao;
	private String param_flJubilado;
	private String param_sqMinistro;
	private String param_flLancamentosEmAberto;
	private String param_flLancamentosPagos;
	private String param_controleAtualizarDados;
	private Departamento departamento;


	private List<RegLancamentoCpl> regLancamentosCpl;
	private List<FormaRecebimento> formaRecebimento;

	private LancamentoResumo lancamentoResumo;

	@Inject
	private LancamentoResumoBO lancamentoResumoBO;

	@Inject
	private RegLancamentoBO regLancamentoBO;

	@Inject
	private MinistroBO ministroBO;
	
	@Inject
	private DepartamentoBO departamentoBO;

	private BigDecimal vlTotalLancamento;
	private BigDecimal vlTotalPagamento;
	private BigDecimal vlTotalSaldoDevedor;
	private String corVlTotalSaldoDevedor = "text-align:right; color:red; font-weight: bold;";

	private BigDecimal vlTotal;
	private BigDecimal vlTotalRecebimento = BigDecimal.ZERO;

	private BigDecimal vlTroco = BigDecimal.ZERO;
	private String mensagemTroco = " - TROCO";
	private String corMensagemTroco = "color:blue; font-weight: bold; font-size: large";

	private boolean flLancamentosEmAberto = true;
	private boolean flLancamentosPagos;
	private boolean flCalculandoTotalRecebido;

	public void buscarLancamentoResumo() {
		this.lancamentoResumo = lancamentoResumoBO.findByPrimaryKey(this.lancamentoResumo.getSqResumo());
	}

	public void buscarMinistroRecebimento(Long sqMinistro) {
		this.ministroRecebimento = ministroBO.findMinistrosRecebimentoForPrimaryKey(sqMinistro);
	}

	public String getParam_regiaoItens_sqRegiao() {
		return param_regiaoItens_sqRegiao;
	}

	public void setParam_regiaoItens_sqRegiao(String param_regiaoItens_sqRegiao) {
		this.param_regiaoItens_sqRegiao = param_regiaoItens_sqRegiao;
	}

	public String getParam_controle() {
		return param_controle;
	}

	public void setParam_controle(String param_controle) {
		this.param_controle = param_controle;
	}

	public boolean isFlImprimirRecibo() {
		return flImprimirRecibo;
	}

	public void setFlImprimirRecibo(boolean flImprimirRecibo) {
		this.flImprimirRecibo = flImprimirRecibo;
	}
	
	public Integer getCdReciboEmNome() {
		return cdReciboEmNome;
	}
	
	public void setCdReciboEmNome(Integer cdReciboEmNome) {
		this.cdReciboEmNome = cdReciboEmNome;
	}
	
	public boolean isFlCalculandoTotalRecebido() {
		return flCalculandoTotalRecebido;
	}
	
	public void setFlCalculandoTotalRecebido(boolean flCalculandoTotalRecebido) {
		this.flCalculandoTotalRecebido = flCalculandoTotalRecebido;
	}

	public LancamentoResumo getLancamentoResumo() {
		return lancamentoResumo;
	}

	public void setLancamentoResumo(LancamentoResumo lancamentoResumo) {
		this.lancamentoResumo = lancamentoResumo;
	}


	public String getParam_igrejaItensFiltro_sqIgreja() {
		return param_igrejaItensFiltro_sqIgreja;
	}


	public void setParam_igrejaItensFiltro_sqIgreja(String param_igrejaItensFiltro_sqIgreja) {
		this.param_igrejaItensFiltro_sqIgreja = param_igrejaItensFiltro_sqIgreja;
	}


	public String getParam_departamentoItensFiltro_sqDepartamento() {
		return param_departamentoItensFiltro_sqDepartamento;
	}


	public void setParam_departamentoItensFiltro_sqDepartamento(String param_departamentoItensFiltro_sqDepartamento) {
		this.param_departamentoItensFiltro_sqDepartamento = param_departamentoItensFiltro_sqDepartamento;
	}


	public String getParam_cdRg() {
		return param_cdRg;
	}


	public void setParam_cdRg(String param_cdRg) {
		this.param_cdRg = param_cdRg;
	}


	public String getParam_nmMinistro() {
		return param_nmMinistro;
	}


	public void setParam_nmMinistro(String param_nmMinistro) {
		this.param_nmMinistro = param_nmMinistro;
	}

	public String getParam_dsCpf() {
		return param_dsCpf;
	}
	
	public void setParam_dsCpf(String param_dsCpf) {
		this.param_dsCpf = param_dsCpf;
	}

	public String getParam_cargo_sqCargo() {
		return param_cargo_sqCargo;
	}


	public void setParam_cargo_sqCargo(String param_cargo_sqCargo) {
		this.param_cargo_sqCargo = param_cargo_sqCargo;
	}


	public String getParam_cdNsu() {
		return param_cdNsu;
	}


	public void setParam_cdNsu(String param_cdNsu) {
		this.param_cdNsu = param_cdNsu;
	}

	public String getParam_cdNsuAtual() {
		return param_cdNsuAtual;
	}

	public void setParam_cdNsuAtual(String param_cdNsuAtual) {
		this.param_cdNsuAtual = param_cdNsuAtual;
	}

	public String getParam_dtInicio1() {
		return param_dtInicio1;
	}


	public void setParam_dtInicio1(String param_dtInicio1) {
		this.param_dtInicio1 = param_dtInicio1;
	}


	public String getParam_dtFim1() {
		return param_dtFim1;
	}


	public void setParam_dtFim1(String param_dtFim1) {
		this.param_dtFim1 = param_dtFim1;
	}


	public String getParam_dtInicio2() {
		return param_dtInicio2;
	}


	public void setParam_dtInicio2(String param_dtInicio2) {
		this.param_dtInicio2 = param_dtInicio2;
	}


	public String getParam_dtFim2() {
		return param_dtFim2;
	}


	public void setParam_dtFim2(String param_dtFim2) {
		this.param_dtFim2 = param_dtFim2;
	}


	public String getParam_dsCidade() {
		return param_dsCidade;
	}


	public void setParam_dsCidade(String param_dsCidade) {
		this.param_dsCidade = param_dsCidade;
	}


	public String getParam_estado_sqEstado() {
		return param_estado_sqEstado;
	}


	public void setParam_estado_sqEstado(String param_estado_sqEstado) {
		this.param_estado_sqEstado = param_estado_sqEstado;
	}


	public String getParam_cdSituacao() {
		return param_cdSituacao;
	}


	public void setParam_cdSituacao(String param_cdSituacao) {
		this.param_cdSituacao = param_cdSituacao;
	}


	public String getParam_flJubilado() {
		return param_flJubilado;
	}


	public void setParam_flJubilado(String param_flJubilado) {
		this.param_flJubilado = param_flJubilado;
	}

	public BigDecimal getVlTotalLancamento() {
		return vlTotalLancamento;
	}

	public void setVlTotalLancamento(BigDecimal vlTotalLancamento) {
		this.vlTotalLancamento = vlTotalLancamento;
	}

	public String getParam_flLancamentosEmAberto() {
		return param_flLancamentosEmAberto;
	}

	public void setParam_flLancamentosEmAberto(String param_flLancamentosEmAberto) {
		this.param_flLancamentosEmAberto = param_flLancamentosEmAberto;
	}

	public String getParam_flLancamentosPagos() {
		return param_flLancamentosPagos;
	}

	public void setParam_flLancamentosPagos(String param_flLancamentosPagos) {
		this.param_flLancamentosPagos = param_flLancamentosPagos;
	}

	public BigDecimal getVlTotalPagamento() {
		return vlTotalPagamento;
	}

	public void setVlTotalPagamento(BigDecimal vlTotalPagamento) {
		this.vlTotalPagamento = vlTotalPagamento;
	}

	public BigDecimal getVlTotalSaldoDevedor() {
		return vlTotalSaldoDevedor;
	}

	public void setVlTotalSaldoDevedor(BigDecimal vlTotalSaldoDevedor) {
		this.vlTotalSaldoDevedor = vlTotalSaldoDevedor;
	}

	public String getCorVlTotalSaldoDevedor() {
		return corVlTotalSaldoDevedor;
	}

	public void setCorVlTotalSaldoDevedor(String corVlTotalSaldoDevedor) {
		this.corVlTotalSaldoDevedor = corVlTotalSaldoDevedor;
	}

	public List<RegLancamentoCpl> getRegLancamentosCpl() {
		return regLancamentosCpl;
	}

	public void setRegLancamentosCpl(List<RegLancamentoCpl> regLancamentosCpl) {
		this.regLancamentosCpl = regLancamentosCpl;
	}

	public BigDecimal getVlTotal() {
		return vlTotal;
	}

	public void setVlTotal(BigDecimal vlTotal) {
		this.vlTotal = vlTotal;
	}

	public List<FormaRecebimento> getFormaRecebimento() {
		return formaRecebimento;
	}

	public void setFormaRecebimento(List<FormaRecebimento> formaRecebimento) {
		this.formaRecebimento = formaRecebimento;
	}

	public BigDecimal getVlTroco() {
		return vlTroco;
	}


	public void setVlTroco(BigDecimal vlTroco) {
		this.vlTroco = vlTroco;
	}


	public String getMensagemTroco() {
		return mensagemTroco;
	}


	public void setMensagemTroco(String mensagemTroco) {
		this.mensagemTroco = mensagemTroco;
	}


	public String getCorMensagemTroco() {
		return corMensagemTroco;
	}


	public void setCorMensagemTroco(String corMensagemTroco) {
		this.corMensagemTroco = corMensagemTroco;
	}

	public BigDecimal getVlTotalRecebimento() {
		return vlTotalRecebimento;
	}

	public void setVlTotalRecebimento(BigDecimal vlTotalRecebimento) {
		this.vlTotalRecebimento = vlTotalRecebimento;
	}

	public MinistroRecebimentoCpl getMinistroRecebimento() {
		return ministroRecebimento;
	}

	public void setMinistroRecebimento(MinistroRecebimentoCpl ministroRecebimento) {
		this.ministroRecebimento = ministroRecebimento;
	}

	public String getParam_sqMinistro() {
		return param_sqMinistro;
	}

	public void setParam_sqMinistro(String param_sqMinistro) {
		this.param_sqMinistro = param_sqMinistro;
	}

	public boolean isFlLancamentosEmAberto() {
		return flLancamentosEmAberto;
	}

	public void setFlLancamentosEmAberto(boolean flLancamentosEmAberto) {
		this.flLancamentosEmAberto = flLancamentosEmAberto;
	}

	public boolean isFlLancamentosPagos() {
		return flLancamentosPagos;
	}

	public void setFlLancamentosPagos(boolean flLancamentosPagos) {
		this.flLancamentosPagos = flLancamentosPagos;
	}

	public String getParam_controleAtualizarDados() {
		return param_controleAtualizarDados;
	}

	public void setParam_controleAtualizarDados(String param_controleAtualizarDados) {
		this.param_controleAtualizarDados = param_controleAtualizarDados;
	}	
	
	public Departamento getDepartamento() {
		return departamento;
	}
	
	public void setDepartamento(Departamento departamento) {
		this.departamento = departamento;
	}

	// Listar todos os lançamentos de um ministro
	public void inicializarLancamentosMinistro(Long sqMinistro) {	

		if(!this.flLancamentosEmAberto && !this.flLancamentosPagos) {
			this.regLancamentosCpl = new ArrayList<>();
		} else {
			this.regLancamentosCpl = regLancamentoBO.findLancamentosMinistro(sqMinistro, null, this.flLancamentosEmAberto, this.flLancamentosPagos);
		}

		this.vlTotalLancamento   = BigDecimal.ZERO;
		this.vlTotalPagamento    =  BigDecimal.ZERO;
		this.vlTotalSaldoDevedor = BigDecimal.ZERO;

		RegLancamentoCpl toAnterior = null;
		int qtdeRegistrosIguais = 0;
		BigDecimal vlSdDevedorLinha = BigDecimal.ZERO;
		BigDecimal vlSdDevedorLinhaAnterior = BigDecimal.ZERO;
		BigDecimal vlPagamentoParcial = BigDecimal.ZERO;

		Integer ctContador = 0;

		for (RegLancamentoCpl to : regLancamentosCpl) {
			ctContador ++;

			// Verificar se o lancamento atual é igual ao anterior (parcelamento)
			if(toAnterior != null && toAnterior.getSqRegLacamento().equals(to.getSqRegLacamento())) {
				qtdeRegistrosIguais ++;
				to.setCorLancamentoRepetido("color:Silver;");
				vlPagamentoParcial = vlPagamentoParcial.add(toAnterior.getVlPagamento());
				
				toAnterior.setFlExibirCancelarPagamento(toAnterior.getDtPagamento() != null && 
								toAnterior.getDtPeriodoFechado() == null &&
								Permissoes.getPermissaoCancelarLancamentoEntradaRecebimentosPagos());
				
				// Tratamento especial para parcelamentos no último registro da coleção
				if(ctContador == regLancamentosCpl.size()) {
					vlPagamentoParcial = prepararLancamentosRepetidos(to, vlPagamentoParcial);
				}
			} else {
				if(qtdeRegistrosIguais > 0) {
					qtdeRegistrosIguais = 0;
					vlPagamentoParcial = prepararLancamentosRepetidos(toAnterior, vlPagamentoParcial);
				}

				vlSdDevedorLinha = prepararLancamentosNormais(to);
				
				vlPagamentoParcial = BigDecimal.ZERO;
			}

			//Ajustar o registro anterior
			if(qtdeRegistrosIguais == 1) {
				toAnterior.setCorLancamentoRepetido("color:blue; font-weight:bold;");
				toAnterior.setSdDevedor(null);
				toAnterior.setVlPagamentoRecebido(null);
				toAnterior.setFlExibirAlterarLancamento(false);
				toAnterior.setFlExibirConfirmarPagamento(false);
				toAnterior.setFlExibirExcluirLancamento(toAnterior.getDtPagamento() == null);
				toAnterior.setFlExibirCancelarPagamento(toAnterior.getDtPagamento() != null &&
								toAnterior.getDtPeriodoFechado() == null &&
								Permissoes.getPermissaoCancelarLancamentoEntradaRecebimentosPagos());
				this.vlTotalSaldoDevedor = this.vlTotalSaldoDevedor.subtract(vlSdDevedorLinhaAnterior);
			}

			// Apurar valor total pago
			this.vlTotalPagamento = this.vlTotalPagamento.add(to.getVlPagamento() !=null? vlPagamentoParcial.add(to.getVlPagamento()): vlPagamentoParcial.add(new BigDecimal("0.00")));


			// Guardar o registro anterior
			
			toAnterior = to;
			vlSdDevedorLinhaAnterior = vlSdDevedorLinha;
		}

		if(this.vlTotalSaldoDevedor.compareTo(BigDecimal.ZERO) >= 0) this.corVlTotalSaldoDevedor = "text-align:right; color:blue; font-weight: bold;";

	}

	private BigDecimal prepararLancamentosRepetidos(RegLancamentoCpl toAnterior, BigDecimal vlPagamentoParcial) {
		BigDecimal vlSdDevedorLinha;
		vlPagamentoParcial = vlPagamentoParcial.add(toAnterior.getVlPagamento());
		vlSdDevedorLinha = toAnterior.getVlLancamento().subtract(vlPagamentoParcial).multiply(new BigDecimal(-1));

		toAnterior.setSdDevedor(vlSdDevedorLinha.compareTo(BigDecimal.ZERO) == 0? null: vlSdDevedorLinha);
		toAnterior.setVlPagamentoRecebido(vlSdDevedorLinha.compareTo(BigDecimal.ZERO) == 0? null: vlSdDevedorLinha.multiply(new BigDecimal(-1)));
		toAnterior.setFlExibirAlterarLancamento(toAnterior.getSdDevedor() != null && 
					toAnterior.getSdDevedor().compareTo(BigDecimal.ZERO) != 0 &&
					Permissoes.getPermissaoEditarLancamentoEntradaRecebimentos());
		toAnterior.setFlExibirConfirmarPagamento(toAnterior.getSdDevedor() != null && toAnterior.getSdDevedor().compareTo(BigDecimal.ZERO) != 0);
		toAnterior.setFlExibirExcluirLancamento(toAnterior.getDtPagamento() == null);
		toAnterior.setFlExibirCancelarPagamento(toAnterior.getDtPagamento() != null && 
						toAnterior.getDtPeriodoFechado() == null &&
						Permissoes.getPermissaoCancelarLancamentoEntradaRecebimentosPagos());

		if(vlSdDevedorLinha.compareTo(BigDecimal.ZERO) > 0) toAnterior.setCorSaldoDevedor("color:blue;");
		this.vlTotalSaldoDevedor = this.vlTotalSaldoDevedor.add(vlSdDevedorLinha);

		return vlPagamentoParcial;
	}

	private BigDecimal prepararLancamentosNormais(RegLancamentoCpl to) {
		BigDecimal vlSaldoLinha;
		this.vlTotalLancamento =  vlTotalLancamento.add(to.getVlLancamento());
		to.setSdDevedor(to.getCdNsuRecibo() != null?  to.getVlPagamento().subtract(to.getVlLancamento()): to.getVlLancamento().multiply(new BigDecimal(-1)));
		to.setFlExibirAlterarLancamento(to.getSdDevedor().compareTo(BigDecimal.ZERO) != 0 && Permissoes.getPermissaoEditarLancamentoEntradaRecebimentos());
		to.setFlExibirConfirmarPagamento(to.getSdDevedor().compareTo(BigDecimal.ZERO) != 0);
		to.setFlExibirExcluirLancamento(to.getDtPagamento() == null);
		to.setFlExibirCancelarPagamento(to.getDtPagamento() != null && 
						to.getDtPeriodoFechado() == null &&
						Permissoes.getPermissaoCancelarLancamentoEntradaRecebimentosPagos());
		vlSaldoLinha = to.getSdDevedor();

		this.vlTotalSaldoDevedor = this.vlTotalSaldoDevedor.add(vlSaldoLinha);
		if(to.getSdDevedor().compareTo(BigDecimal.ZERO) == 0) to.setSdDevedor(null);
		if(vlSaldoLinha.compareTo(BigDecimal.ZERO) > 0) to.setCorSaldoDevedor("color:blue;");
		to.setVlPagamentoRecebido(to.getSdDevedor() != null? to.getSdDevedor().multiply(new BigDecimal(-1)): null);
		return vlSaldoLinha;
	}

	// Calcular dinamicamente o total informado na tela. I = Inclusão - A = Alteração
	public void calcularTotalRecebido(String tp) {

		this.flCalculandoTotalRecebido = true;
		
		this.vlTotal = BigDecimal.ZERO;

		if(tp.equalsIgnoreCase("I")) {
			for (FormaRecebimento frc : formaRecebimento) {
				if(frc.getVlRecebido().compareTo(BigDecimal.ZERO) < 0 )
					throw new NegocioException("Valores informados em Forma pagamento não pode ser negativo.");

				this.vlTotal = vlTotal.add(frc.getVlRecebido());
			}
		} else if(tp.equalsIgnoreCase("A")) {
			for (FormaRecebimentoPorReciboCpl frc : getFormaRecebimentoPorReciboCpl()) {
				this.vlTotal = this.vlTotal.add(frc.getVlRecebido());
			}
		}

		this.calcularTroco();


	}

	// Calcular troco
	public void calcularTroco() {

		this.vlTroco = this.getVlTotal().subtract(this.vlTotalRecebimento);

		if(this.vlTroco.compareTo(BigDecimal.ZERO) < 0) {
			this.mensagemTroco = " - FALTA";
			this.corMensagemTroco = "color:red; font-weight: bold; font-size: large;";
		} else {
			this.mensagemTroco = " - TROCO";
			this.corMensagemTroco = "color:blue; font-weight: bold; font-size: large;";
		}
	}
	
	// Buscar departamento principal
	public void inicializarDepartamentoPrincipal(Long sqDepartamento) {
		this.departamento = departamentoBO.find(sqDepartamento);
	}
	
	// Impressão de recibos
	public void imprimirReciboGenerico(Long sqRecibo, Integer tpRecibo) {
		
		// Imprimir o recibo
		StringBuilder jsFunction = new StringBuilder()
					.append("relatorioFinanceiroRecibo(")
					.append("'../../../relatorioFinanceiroRecibo'")
					.append(",'")
					.append("pdf")
					.append("',")
					.append(sqRecibo)
					.append(",")
					.append(tpRecibo)
					.append(")");
		
		// Chama função javaScript para exibir relatório
		// FacesUtil.getRequestContext().execute(jsFunction.toString());
		FacesUtil.getRequestContext().executeScript(jsFunction.toString());
		
	}

}
