package br.com.convencao.bo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.LockModeType;

import br.com.convencao.dao.LancamentoResumoDAO;
import br.com.convencao.model.LancamentoResumo;
import br.com.convencao.util.Uteis;

public class LancamentoGenericoBO implements Serializable {
	private static final long serialVersionUID = 1L;

	@Inject
	LancamentoResumoDAO resumoDAO;

	// Ajustar LancamentoEntrada para inclusão, alteração e exclusão de lancamentos de igrejas e outros e Ministros
	// tpLancamento (E)Entrada (S)Saida
	protected void ajustarLancamentoResumo(String tpLancamento, Long sqRegiao, Long sqResumo, BigDecimal vlLancamnento, BigDecimal vlTotalAnterior, LocalDate dtRegistro) throws Exception {
		// Verificar se o periodo informado está aberto e a data de pagamento está dentro do período
		// buscar resumo com lock na tabela

		LancamentoResumo lancamentoResumoLock = resumoDAO.find(LancamentoResumo.class, sqResumo, LockModeType.PESSIMISTIC_WRITE);
		if(lancamentoResumoLock.getDtFechado() != null)
			throw new NegocioException("Somente é permitido incluir lançamentos em períodos abertos, este período está fechado.");

		if(!Uteis.primeiraDataEntreOutrasDuas(dtRegistro, lancamentoResumoLock.getDtPeriodoInicio(), lancamentoResumoLock.getDtPeriodoFim())) 
			throw new NegocioException("A data do lançamento informada está fora do período.");


		// Incrementar entradas
		if("Entrada".equals(tpLancamento)) {
			// Incrementar entradas no resumo e salvar
			BigDecimal vlEntradas = lancamentoResumoLock.getVlEntradas();
			vlEntradas = vlEntradas.subtract(vlTotalAnterior);
			vlEntradas = vlEntradas.add(vlLancamnento);

			// Incrementar entradas no resumo e salvar
			lancamentoResumoLock.setVlEntradas(vlEntradas);



			// Havendo incremento de entradas ou saídas, atualizar o resumo no banco de dados
			resumoDAO.salvar(lancamentoResumoLock);

			// Verificar se há resumo posterior ao atual para atualização do saldo anterior.
			List<LancamentoResumo> lancamentoResumoList = resumoDAO.findAllPosteriorPorRegiao(sqRegiao, sqResumo);

			BigDecimal vlSaldoAnterior;
			for (LancamentoResumo rs : lancamentoResumoList) {
				vlSaldoAnterior = rs.getVlSaldoAnterior();
				vlSaldoAnterior = vlSaldoAnterior.subtract(vlTotalAnterior);
				vlSaldoAnterior = vlSaldoAnterior.add(vlLancamnento);

				rs.setVlSaldoAnterior(vlSaldoAnterior);
				resumoDAO.salvar(rs);
			}
		} else 
			// Incrementar saidas
			if("Saida".equals(tpLancamento)) {
				BigDecimal vlSaidas = lancamentoResumoLock.getVlSaidas();
				vlSaidas = vlSaidas.subtract(vlTotalAnterior);
				vlSaidas = vlSaidas.add(vlLancamnento);

				lancamentoResumoLock.setVlSaidas(vlSaidas);		

				// Havendo incremento de entradas ou saídas, atualizar o resumo no banco de dados
				resumoDAO.salvar(lancamentoResumoLock);

				// Verificar se há resumo posterior ao atual para atualização do saldo anterior.
				List<LancamentoResumo> lancamentoResumoList = resumoDAO.findAllPosteriorPorRegiao(sqRegiao, sqResumo);

				BigDecimal vlSaldoAnterior;
				for (LancamentoResumo rs : lancamentoResumoList) {
					vlSaldoAnterior = rs.getVlSaldoAnterior();
					vlSaldoAnterior = vlSaldoAnterior.add(vlTotalAnterior);
					vlSaldoAnterior = vlSaldoAnterior.subtract(vlLancamnento);

					rs.setVlSaldoAnterior(vlSaldoAnterior);
					resumoDAO.salvar(rs);
				}



			} else
				if("ExtornoEntrada".equals(tpLancamento)) {
					// Decrementar valor da Entrada que está estornando 
					BigDecimal vlEntradaEstorno = lancamentoResumoLock.getVlEntradas();
					vlEntradaEstorno = vlEntradaEstorno.subtract(vlLancamnento);
					
					// Atualizar valor das Entradas no Resumo
					lancamentoResumoLock.setVlEntradas(vlEntradaEstorno);
					
					// Salvar o resumo com o novo valor de Entradas
					resumoDAO.salvar(lancamentoResumoLock);
					
					// Verificar se há resumo posterior ao atual para atualização do saldo anterior.
					List<LancamentoResumo> lancamentoResumoList = resumoDAO.findAllPosteriorPorRegiao(sqRegiao, sqResumo);

					BigDecimal vlSaldoAnterior;
					for (LancamentoResumo rs : lancamentoResumoList) {
						vlSaldoAnterior = rs.getVlSaldoAnterior();
						vlSaldoAnterior = vlSaldoAnterior.subtract(vlLancamnento);

						rs.setVlSaldoAnterior(vlSaldoAnterior);
						resumoDAO.salvar(rs);
					}
					
				}

				else {
					throw new NegocioException("Erro ao atualizar o Resumo. Não foi possível reconhecer se é lançamento de Entrada ou Saida.");
				}

	}


}
