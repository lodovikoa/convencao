package br.com.convencao.bo;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import br.com.convencao.dao.FormaRecebimentoDAO;
import br.com.convencao.dao.LancamentoDAO;
import br.com.convencao.dao.LancamentoResumoDAO;
import br.com.convencao.dao.NsuDAO;
import br.com.convencao.dao.ReciboDAO;
import br.com.convencao.model.FormaPagamento;
import br.com.convencao.model.FormaRecebimento;
import br.com.convencao.model.Lancamento;
import br.com.convencao.model.LancamentoResumo;
import br.com.convencao.model.Nsu;
import br.com.convencao.model.PlanoConta;
import br.com.convencao.model.Recibo;
import br.com.convencao.model.RegLancamento;
import br.com.convencao.model.to.FormaRecebimentoPorReciboCpl;
import br.com.convencao.model.to.LancamentoEntradaCpl;
import br.com.convencao.model.to.RegLancamentoCpl;
import br.com.convencao.util.Uteis;
import br.com.convencao.util.jpa.Transactional;

public class LancamentoBO  extends LancamentoGenericoBO{

	private static final long serialVersionUID = 1L;

	private static Log log = LogFactory.getLog(LancamentoBO.class);

	@Inject
	LancamentoDAO dao;


	@Inject
	NsuDAO nsuDAO;

	@Inject
	ReciboDAO reciboDAO;

	@Inject
	FormaRecebimentoDAO formaRecebimentoDAO;

	@Inject
	LancamentoResumoDAO lancamentoResumoDAO;

	public BigDecimal findValorTotalEntradasSemPeriodo(Long sqRegiao) {
		log.info("findValorTotalEntradasSemPeriodo(" + sqRegiao + ")");

		return dao.findValorTotalEntradasSemPeriodo(sqRegiao);
	}

	// @Transaction feito no método anterior, que chamou este.
	protected int updateAlterarPeriodoSqResumoParaNulo(Long sqResumo) {
		log.info("updateAlterarPeriodoSqResumoParaNulo(" + sqResumo + ")");

		return dao.updateAlterarPeriodoSqResumoParaNulo(sqResumo);
	}


	protected int updateAlterarPeriodoSqResumoNovo(LancamentoResumo selecionado, LocalDate dtPeriodoInicioTemp, LocalDate dtPeriodoFimTemp) {
		log.info("updateAlterarPeriodoSqResumoNovo(" + selecionado.getSqResumo() + "," + dtPeriodoInicioTemp + "," + dtPeriodoFimTemp + ")");

		return dao.updateAlterarPeriodoSqResumoNovo(selecionado, dtPeriodoInicioTemp, dtPeriodoFimTemp);
	}


	protected BigDecimal findValorTotalPorPeriodo(Long sqResumo) {
		log.info("findValorTotalPorPeriodo(" + sqResumo + ")");

		return dao.findValorTotalPorPeriodo(sqResumo);
	}


	// Listar todos os lancamentos de entrada de um resumo
	public List<LancamentoEntradaCpl> findAllPorResumo(Long sqResumo, StringBuilder dsCodOrigem) {
		log.info("findAllPorResumo(" + sqResumo + ")");

		return dao.findAllPorResumo(sqResumo, dsCodOrigem);
	}


	// Buscar lancamento de entrada pela primary key completo
	public Lancamento findByPrimaryKey(Long sqLancamento) {
		log.info("findByPrimaryKey(" + sqLancamento + ")");

		return dao.findByPrimaryKey(sqLancamento);
	}

	// Buscar valor total já pago de um RegLancamento
	public BigDecimal findValorTotalPorRegLancamento(Long sqRegLancamento) {
		log.info("findValorTotalPorRegLancamento(" + sqRegLancamento + ")");

		return dao.findValorTotalPorRegLancamento(sqRegLancamento);
	}

	// Listar pagamentos feitos por Igrejas, registrados na data atual e pelo usuário logado
	public List<LancamentoEntradaCpl> findAllPagamentosIgrejaUsuario() {
		log.info("findAllPagamentosIgrejaUsuario()" + Uteis.UsuarioLogado().getUsername() + " - " + Uteis.DataHoje().toLocalDate());

		return dao.findAllPagamentosIgrejaUsuario();
	}


	// Alteração de registro feito para Igreja ou Outros
	@Transactional
	public Recibo salvarIncluirIgrejaOutros(Lancamento lancamentoEntrada, List<FormaRecebimento> formaRecebimento, LancamentoResumo lancamentoResumo) {
		try {
			log.info("salvarIncluirIgrejaOutros(" + lancamentoEntrada.getSqLancamento() + ")");

			// Validações
			validarLancamentoIgrejaOutros(lancamentoEntrada);

			// 5-Apurar valor total recebido para compor o valor do recibo.
			BigDecimal vlTotalRecebido = BigDecimal.ZERO;
			List<FormaRecebimento> formaRecebimentoReal = new ArrayList<>();
			for (FormaRecebimento fr : formaRecebimento) {
				if(fr.getVlRecebido().signum() > 0) {
					if(fr.getFormaPagamento().isFlExibirComplemento()) {
						if(fr.getDsComplemento() == null || fr.getDsComplemento().trim().length() == 0) {
							throw new NegocioException("Faltou preencher o complemento do tipo de pagamento " + fr.getFormaPagamento().getDsFormaPagamento());
						} else {
							fr.setDsComplemento(fr.getDsComplemento().trim());
						}
					}

					fr.setVlContabil(fr.getVlRecebido());
					vlTotalRecebido = vlTotalRecebido.add(fr.getVlRecebido());	

					formaRecebimentoReal.add(fr);
				} else if(fr.getFormaPagamento().isFlExibirComplemento()) {
					if(fr.getDsComplemento() != null && fr.getDsComplemento().trim().length() > 0) {
						throw new NegocioException("Retire o complemento de " + fr.getFormaPagamento().getDsFormaPagamento() + ", pois está sem nenhum valor associado:  ");
					}
				}
			}

			// 6-Verificar se foi informado algum valor
			if(vlTotalRecebido.signum() <= 0)
				throw new NegocioException("Faltou informar o valor recebido");



			// Atribuir valor total recebido
			lancamentoEntrada.setVlPagamento(vlTotalRecebido);

			// Atribuir data do recibo igual à data de pagamento
			lancamentoEntrada.getRecibo().setDtRecibo(lancamentoEntrada.getDtPagamento());

			// Atribuir data atual para o recibo
			lancamentoEntrada.getRecibo().setDtRegistro(Uteis.DataHoje().toLocalDate());

			// Atribuir data atual no lancamento
			lancamentoEntrada.setDtRegistro(Uteis.DataHoje());

			// identificar o resumo de acordo com a data de pagamento
			if(lancamentoResumo == null) { 
				lancamentoResumo = lancamentoResumoDAO.findByPorDataPagamento(lancamentoEntrada.getRecibo().getRegiao().getSqRegiao(), lancamentoEntrada.getRecibo().getDtRecibo());
			} else {		
				// Atribuir regiao ao recibo
				lancamentoEntrada.getRecibo().setRegiao(lancamentoResumo.getRegiao());
			}


			//Atribuir codigo de origem = 2 para recebimento de Igrejas ou codigo de origem = 3 para recebimento de Outros
			if(lancamentoEntrada.getIgreja() != null && lancamentoEntrada.getIgreja().getSqIgreja() > 0) {
				lancamentoEntrada.setCdOrigem(2);
			} else if(lancamentoEntrada.getNmOutros() != null && lancamentoEntrada.getNmOutros().trim().length() > 0) {
				lancamentoEntrada.setCdOrigem(3);
			} else {
				throw new NegocioException("Houve algum erro na seleção da Igreja ou no preenchimento do campo Outros.");
			}


			// Pegar recibo, obter nsu e auditoria,  e gravar o Recibo
			Recibo recibo = lancamentoEntrada.getRecibo();
			recibo.setCdNsu(nsuDAO.salvar(new Nsu()).getSqNsu());
			recibo.setAuditoriaUsuario(Uteis.UsuarioLogado().getUsuario().getDsLogin());
			recibo.setAuditoriaData(Uteis.DataHoje());

			recibo = reciboDAO.salvar(recibo);

			// Ajustar e gravar formas de recebimento
			for (FormaRecebimento f : formaRecebimentoReal) {
				f.setRecibo(recibo);
				f.setAuditoriaUsuario(Uteis.UsuarioLogado().getUsuario().getDsLogin());
				f.setAuditoriaData(Uteis.DataHoje());

				formaRecebimentoDAO.salvar(f);

			}				

			// ajustar LancamentoResumo
			ajustarLancamentoResumo("Entrada", lancamentoResumo.getRegiao().getSqRegiao(),  lancamentoResumo.getSqResumo(), vlTotalRecebido, BigDecimal.ZERO, lancamentoEntrada.getDtPagamento());


			// Auditoria para o lançamento e salvar
			lancamentoEntrada.setAuditoriaUsuario(Uteis.UsuarioLogado().getUsuario().getDsLogin());
			lancamentoEntrada.setAuditoriaData(Uteis.DataHoje());
			lancamentoEntrada.setRecibo(recibo);
			lancamentoEntrada.setLancamentoResumo(lancamentoResumo);

			// Verificar registro de entrada repetido
			if(verificarRegistroRepetidoIgrejaOutros(lancamentoEntrada)) {
				throw new NegocioException("Registro repetido. Favor verificar.");
			}

			// Gravar Recebimento 
			dao.salvar(lancamentoEntrada);
			
			return recibo;

		} catch (Exception e) {
			e.printStackTrace();
			throw new NegocioException("Erro ao salvar inclusão registro de Recebimento de Igrejas ou Outros: " + e.getMessage());
		}

	}


	// Alteração de registro feito para Igreja ou Outros
	@Transactional
	public void salvarAlterarIgrejaOutros(Lancamento lancamentoEntrada,	List<FormaRecebimentoPorReciboCpl> formaRecebimentoPorReciboCpl, LancamentoResumo lancamentoResumo) {
		try {
			log.info("salvarAlterarIgrejaOutros(" + lancamentoEntrada.getSqLancamento() + ")");

			// Validações
			validarLancamentoIgrejaOutros(lancamentoEntrada);

			// Preparar alteração de FormaRecebimento
			List<FormaRecebimento> formaRecebimento = new ArrayList<>();
			FormaRecebimento formaRec; 

			// Apurar valor total recebido para compor o valor do recibo.
			BigDecimal vlTotalRecebido = BigDecimal.ZERO;
			for (FormaRecebimentoPorReciboCpl fr : formaRecebimentoPorReciboCpl) {
				if(fr.getVlRecebido().signum() > 0) {
					if(fr.isFlExibirComplemento()) {
						if(fr.getDsComplemento() == null || fr.getDsComplemento().trim().length() == 0) {
							throw new NegocioException("Faltou preencher o complemento do tipo de pagamento " + fr.getDsFormaPagamento());
						} else {
							fr.setDsComplemento(fr.getDsComplemento().trim());
						}
					}

					vlTotalRecebido = vlTotalRecebido.add(fr.getVlRecebido());

					formaRec = new FormaRecebimento();
					formaRec.setVlRecebido(fr.getVlRecebido());
					formaRec.setVlContabil(fr.getVlRecebido());
					formaRec.setDsComplemento(fr.getDsComplemento());
					formaRec.setRecibo(lancamentoEntrada.getRecibo());
					formaRec.setFormaPagamento(new FormaPagamento());
					formaRec.getFormaPagamento().setSqFormaPagamento(fr.getSqFormaPagamento());
					formaRec.setAuditoriaUsuario(Uteis.UsuarioLogado().getUsuario().getDsLogin());
					formaRec.setAuditoriaData(Uteis.DataHoje());

					formaRecebimento.add(formaRec);
				} else if(fr.isFlExibirComplemento()) {
					if(fr.getDsComplemento() != null && fr.getDsComplemento().trim().length() > 0) {
						throw new NegocioException("Retire o complemento de " + fr.getDsFormaPagamento() + ", pois está sem nenhum valor associado:  ");
					}
				}
			}

			// Verificar se foi informado algum valor
			if(vlTotalRecebido.signum() <= 0)
				throw new NegocioException("Faltou informar o valor recebido");

			// Buscar forma de recebimento anterior à alteraçãO
			List<FormaRecebimento> formaRecebimentoAtual = formaRecebimentoDAO.findAllPorReciboSimples(lancamentoEntrada.getRecibo().getSqRecibo());
			// Apurar valor total anterior à alteração
			BigDecimal vlTotalAnterior = formaRecebimentoDAO.findValorTotalPorRecibo(lancamentoEntrada.getRecibo().getCdNsu());

			// Verificar se houve alguma alteração na forma de pagamento
			boolean flAjustarFormaRecebimento = false;
			boolean flAchou = false;
			if(formaRecebimentoAtual.size() != formaRecebimento.size()) {
				flAjustarFormaRecebimento = true;
			} else {
				for (FormaRecebimento frc1 : formaRecebimentoAtual) {
					for (FormaRecebimento frc2 : formaRecebimento) {
						if(frc1.equalsTO(frc2)) {
							flAchou = true;
							break;
						}
					}
					if(!flAchou) {
						flAjustarFormaRecebimento = true;
						break;
					} else {
						flAchou = false;
					}
				}
			}

			if(flAjustarFormaRecebimento) {
				// Fazer exclusão das formas de recebimento atual
				for (FormaRecebimento frAtual : formaRecebimentoAtual) {
					frAtual = formaRecebimentoDAO.find(FormaRecebimento.class, frAtual.getSqFormaRecebimento());
					formaRecebimentoDAO.delete(frAtual, frAtual.getSqFormaRecebimento());
				}


				// Fazer inclusão das novas formas de recebimento
				for (FormaRecebimento frAlterado : formaRecebimento) {
					formaRecebimentoDAO.salvar(frAlterado);
				}

				// Atribuir valor total em Lancamento
				lancamentoEntrada.setVlPagamento(vlTotalRecebido);
			}


			// ajustar LancamentoResumo -- vlTotalRecebido = Valor total da Entrada após alteração, vlTotalAnterior = Valor total da entrada antes da alteração
			ajustarLancamentoResumo("Entrada", lancamentoResumo.getRegiao().getSqRegiao(), lancamentoResumo.getSqResumo(), vlTotalRecebido, vlTotalAnterior, lancamentoEntrada.getDtPagamento());


			// Auditoria para o lançamento e salvar
			lancamentoEntrada.setAuditoriaUsuario(Uteis.UsuarioLogado().getUsuario().getDsLogin());
			lancamentoEntrada.setAuditoriaData(Uteis.DataHoje());

			// Verificar se houve alteração no registro
			//			if(!lancamentoEntrada.equalsTO(dao.findByPrimaryKey(lancamentoEntrada.getSqLancamento()))) {
			//				throw new NegocioException("Não houve alteração no registro.");
			//			}

			// Verificar registro de entrada repetido
			if(verificarRegistroRepetidoIgrejaOutros(lancamentoEntrada)) {
				throw new NegocioException("Registro repetido. Favor verificar.");
			}

			// Gravar Recebimento 
			dao.salvar(lancamentoEntrada);
			
			lancamentoEntrada.getRecibo().setDtRecibo(lancamentoEntrada.getDtPagamento());
			lancamentoEntrada.getRecibo().setAuditoriaUsuario(Uteis.UsuarioLogado().getUsuario().getDsLogin());
			lancamentoEntrada.getRecibo().setAuditoriaData(Uteis.DataHoje());
			
			
			// Gravar Recibo
			reciboDAO.salvar(lancamentoEntrada.getRecibo());


		} catch (Exception e) {
			e.printStackTrace();
			throw new NegocioException("Erro ao salvar alteração registro de Recebimento de Igrejas ou Outros: " + e.getMessage());
		}

	}

	@Transactional
	public void salvarExcluirIgrejaOutros(LancamentoEntradaCpl selecionado, Long sqResumo) {
		try {
			log.info("salvarExcluirIgrejaOutros(" + selecionado.getSqLancamento() + ", " + sqResumo + ")");


			// ajustar LancamentoResumo -- vlTotalAnterior = Valor total da entrada que será excluido
			ajustarLancamentoResumo("Entrada", selecionado.getSqRegiao(), sqResumo, BigDecimal.ZERO, selecionado.getVlPagamento(), selecionado.getDtPagamento());


			// Buscar o recibo que será excluido (INATIVADO)
			Recibo reciboParaInativar = reciboDAO.find(Recibo.class, selecionado.getSqRecibo());

			// Inativar o recibo atualizando a auditoria
			reciboParaInativar.setDtCancelado(Uteis.DataHoje());
			reciboParaInativar.setAuditoriaUsuario(Uteis.UsuarioLogado().getUsuario().getDsLogin());
			reciboParaInativar.setAuditoriaData(Uteis.DataHoje());

			// Salvar recibo inativado
			reciboDAO.salvar(reciboParaInativar);

		} catch (Exception e) {
			e.printStackTrace();
			throw new NegocioException("Erro ao salvar EXCLUSÃO de recebimento de Igrejas ou Outros: " + e.getMessage());
		}

	}

	protected void validarLancamentoIgrejaOutros(Lancamento lancamentoEntrada) {
		log.info("validarLancamentoIgrejaOutros(" + lancamentoEntrada.getSqLancamento() + ")");

		// Retirar espaços em branco de inicio e fim das Strings
		if(StringUtils.isNotBlank(lancamentoEntrada.getNmOutros())) lancamentoEntrada.setNmOutros(lancamentoEntrada.getNmOutros().trim());
		if(lancamentoEntrada.getRecibo() != null && StringUtils.isNotBlank(lancamentoEntrada.getRecibo().getDsHistorico()))
			lancamentoEntrada.getRecibo().setDsHistorico(lancamentoEntrada.getRecibo().getDsHistorico().trim());
		else
			throw new NegocioException("Faltou informar o Histórico");

		// 1-Verificar campos obrigatórios preenchidos
		if(lancamentoEntrada.getIgreja() == null && StringUtils.isBlank(lancamentoEntrada.getNmOutros()))
			throw new NegocioException("Faltou preencher o campo Igreja/Outros.");

		if(lancamentoEntrada.getIgreja() != null && lancamentoEntrada.getIgreja().getSqIgreja() > 0 && (lancamentoEntrada.getNmOutros() != null && StringUtils.isNotBlank(lancamentoEntrada.getNmOutros())))
			throw new NegocioException("Selecione uma Igreja ou preencha o campo Outros. Somente um dos dois campos pode ser preenchido");

		// 2-Verificar se Conta contábil foi preenchida
		if(lancamentoEntrada.getPlanoConta() == null || lancamentoEntrada.getPlanoConta().getSqPlanoConta() < 1)
			throw new NegocioException("Faltou selecionar uma conta contábil.");

		// 3-Verificar se data de pagamento foi preenchida
		if(lancamentoEntrada.getDtPagamento() == null)
			throw new NegocioException("Faltou informar Data de Pagamento");

		// 4-Verificar se Histórico foi preenchido
		if(lancamentoEntrada.getRecibo() == null || (lancamentoEntrada.getRecibo() != null && StringUtils.isBlank(lancamentoEntrada.getRecibo().getDsHistorico())))
			throw new NegocioException("Faltou informar o Histórico");
	}


	// Verificar registro repetido
	protected boolean verificarRegistroRepetidoIgrejaOutros(Lancamento entrada) {
		log.info("verificarRegistroRepetidoIgrejaOutros(" + entrada.getSqLancamento() + ")");

		return dao.verificarRegistroRepetidoIgrejaOutros(entrada) > 0;
	}

	@Transactional
	public Recibo salvarIncluirPagamentosMinistros(List<RegLancamentoCpl> lancamentosCpl, Recibo recibo, List<FormaRecebimento> formaRecebimento, BigDecimal vlTotalRecebimento, BigDecimal vlTroco) {
		try {
			log.info("salvarIncluirPagamentosMinistros()");

			// Validar valor se valor recebido é igual ou menor que o Saldo devedor
			String dsValorMaiorSdDevedor = "";
			for (RegLancamentoCpl lanCpl : lancamentosCpl) {
				if(lanCpl.isFlRealizarPagamento()) {
					if(lanCpl.getVlPagamentoRecebido().compareTo(lanCpl.getSdDevedor().multiply(new BigDecimal(-1)) ) > 0) {
						dsValorMaiorSdDevedor = dsValorMaiorSdDevedor + "(Valor a receber de " + lanCpl.getVlPagamentoRecebido() + " é maior que o saldo devedor) ";
					}
				}
			}

			if(dsValorMaiorSdDevedor.length() > 0) {
				throw new NegocioException(dsValorMaiorSdDevedor);
			}

			// Validar forma de recebimento com complemento
			StringBuilder dsTrocoPermitido = new StringBuilder();
			for (FormaRecebimento fr : formaRecebimento) {
				if(fr.getFormaPagamento().isFlPermitirTroco()) {
					if(dsTrocoPermitido.length() > 0) dsTrocoPermitido.append(", ");
					dsTrocoPermitido.append(fr.getFormaPagamento().getDsFormaPagamento()); 
				}

				if(fr.getVlRecebido().signum() > 0) {
					if(fr.getFormaPagamento().isFlExibirComplemento()) {
						if(fr.getDsComplemento() == null || fr.getDsComplemento().trim().length() == 0) {
							throw new NegocioException("Faltou preencher o complemento do tipo de pagamento " + fr.getFormaPagamento().getDsFormaPagamento());
						} else {
							fr.setDsComplemento(fr.getDsComplemento().trim());
						}
					}

					if(fr.getFormaPagamento().isFlPermitirTroco()) {
						if(vlTroco.compareTo(BigDecimal.ZERO) > 0 ) {
							if(fr.getVlRecebido().compareTo(vlTroco) >= 0) {
								fr.setVlTroco(vlTroco);
								fr.setVlContabil(fr.getVlRecebido().subtract(fr.getVlTroco()));
								vlTroco = BigDecimal.ZERO;
							} else {
								fr.setVlTroco(fr.getVlRecebido());
								fr.setVlContabil(fr.getVlRecebido().subtract(fr.getVlTroco()));
								vlTroco = vlTroco.subtract(fr.getVlRecebido());
							}
						} else {
							fr.setVlTroco(vlTroco);
							fr.setVlContabil(fr.getVlRecebido());
							vlTroco = BigDecimal.ZERO;
						}

					} else {
						fr.setVlContabil(fr.getVlRecebido());
					}

		
				} else if(fr.getFormaPagamento().isFlExibirComplemento()) {
					if(fr.getDsComplemento() != null && fr.getDsComplemento().trim().length() > 0) {
						throw new NegocioException("Retire o complemento de " + fr.getFormaPagamento().getDsFormaPagamento() + ", pois está sem nenhum valor associado.");
					}
				}
			}

			if(vlTroco.compareTo(BigDecimal.ZERO) > 0) {
				throw new NegocioException("Troco é maior que o valor informado em " + dsTrocoPermitido.toString());
			}


			// identificar o resumo de acordo com a data de pagamento
			LancamentoResumo LancamentoResumoGravado = lancamentoResumoDAO.findByPorDataPagamento(recibo.getRegiao().getSqRegiao(), recibo.getDtRecibo());

			// Verificar se o resumo da data e região informada etá aberto.
			this.ajustarLancamentoResumo("Entrada", recibo.getRegiao().getSqRegiao(), LancamentoResumoGravado.getSqResumo(), vlTotalRecebimento, BigDecimal.ZERO, recibo.getDtRecibo());


			// Gravar recibo
			recibo.setCdNsu(nsuDAO.salvar(new Nsu()).getSqNsu());
			recibo.setAuditoriaUsuario(Uteis.UsuarioLogado().getUsuario().getDsLogin());
			recibo.setAuditoriaData(Uteis.DataHoje());
			recibo.setDtRegistro(Uteis.DataHoje().toLocalDate());

			Recibo reciboGravado = recibo = reciboDAO.salvar(recibo);

			// Gravar formas de recebimento
			for (FormaRecebimento fr : formaRecebimento) {
				if(fr.getVlContabil() != null && fr.getVlContabil().signum() > 0) {
					fr.setRecibo(reciboGravado);
					fr.setAuditoriaUsuario(Uteis.UsuarioLogado().getUsuario().getDsLogin());
					fr.setAuditoriaData(Uteis.DataHoje());

					formaRecebimentoDAO.salvar(fr);
				}

			}

			// Separar pagamentos selecionados
			Lancamento lan;
			for (RegLancamentoCpl lanTemp : lancamentosCpl) {
				if(lanTemp.isFlRealizarPagamento()) {
					lan = new Lancamento();
					lan.setRegLancamento(new RegLancamento());
					lan.setPlanoConta(new PlanoConta());

					lan.getRegLancamento().setSqRegLancamento(lanTemp.getSqRegLacamento());
					lan.setCdOrigem(1);
					lan.setLancamentoResumo(LancamentoResumoGravado);
					lan.setRecibo(reciboGravado);
					lan.getPlanoConta().setSqPlanoConta(lanTemp.getSqPlanoConta());
					lan.setDtRegistro(lanTemp.getDtRegistro());
					lan.setDtPagamento(recibo.getDtRecibo());
					lan.setVlPagamento(lanTemp.getVlPagamentoRecebido());
					lan.setAuditoriaUsuario(Uteis.UsuarioLogado().getUsuario().getDsLogin());
					lan.setAuditoriaData(Uteis.DataHoje());

					// Gravar lancamentos pagos
					dao.salvar(lan);
				}
			}
			
			return reciboGravado;

		} catch (Exception e) {
			e.printStackTrace();
			throw new NegocioException("Erro ao salvar recebimento de Ministro: " + e.getMessage());
		}
	}

}
