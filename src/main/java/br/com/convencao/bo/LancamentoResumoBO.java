package br.com.convencao.bo;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import br.com.convencao.dao.LancamentoResumoDAO;
import br.com.convencao.model.LancamentoResumo;
import br.com.convencao.util.Uteis;
import br.com.convencao.util.jpa.Transactional;

public class LancamentoResumoBO implements Serializable{

	private static final long serialVersionUID = 1L;

	private static Log log = LogFactory.getLog(LancamentoResumoBO.class);

	@Inject
	LancamentoResumoDAO dao;

	@Inject
	LancamentoSaidaBO saidaBO;

	@Inject
	LancamentoBO entradaBO;

	// Listar todos os registros de uma regial
	public List<LancamentoResumo> findAllPorRegiao(Long sqRegiao){
		log.info("findAllPorRegial(" + sqRegiao + ")");

		List<LancamentoResumo> result = dao.findAllPorRegiao(sqRegiao);

		return result;
	}

	// Buscar último registro de uma região
	public LancamentoResumo findUltimoRegistroPorRegiao(Long sqRegiao) {
		log.info("findUltimoRegistroPorRegiao(" + sqRegiao + ")");

		LancamentoResumo result = dao.findUltimoRegistroPorRegiao(sqRegiao);

		return result;
	}

	// Buscar lançamento pela chave primaria
	public LancamentoResumo findByPrimaryKey(Long sqResumo) {
		log.info("findByPrimaryKey(" + sqResumo + ")");

		return dao.findByPrimaryKey(LancamentoResumo.class, sqResumo);
	}
	
	// Buscar lançamento pela chave primaria com Regiao
	public LancamentoResumo findByPrimaryKeyCompleto(Long sqResumo) {
		log.info("findByPrimaryKey(" + sqResumo + ")");

		return dao.findByPrimaryKeyCompleto(sqResumo);
	}

	@Transactional
	public LancamentoResumo registrarNovoPeriodo(LancamentoResumo selecionado) {	
		try {
			log.info("registrarNovoPeriodo(sqResumo = " + (selecionado != null? selecionado.getSqResumo(): " null)"));
			// Fazer validaçoes e gravar registro no bando de dados
			
			if(selecionado == null || selecionado.getRegiao() == null || selecionado.getRegiao().getSqRegiao() < 1)
				throw new NegocioException("Faltou selecionar a região.");

			selecionado.setDtRegistro(Uteis.DataHoje().toLocalDate());
			selecionado = dao.salvar(selecionado);
			
			// Registrar sqResumo em registros que o sqResumo esteja null
			// Verificar se foi informado periodo inicio e periodo fim e ajusta-los se não informados
			LocalDate dtPeriodoInicioTemp = selecionado.getDtPeriodoInicio() != null? selecionado.getDtPeriodoInicio(): Uteis.StringParaLocalDate("1900-01-01");
			LocalDate dtPeriodoFimTemp    = selecionado.getDtPeriodoFim() != null? selecionado.getDtPeriodoFim(): Uteis.StringParaLocalDate("2999-12-31");
			entradaBO.updateAlterarPeriodoSqResumoNovo(selecionado, dtPeriodoInicioTemp, dtPeriodoFimTemp);

			return selecionado;
		} catch (Exception e) {
			throw new NegocioException("Erro ao tentar salvar novo período financeiro: " + e.getMessage(), e);
		}
	}

	@Transactional
	public LancamentoResumo updateDataFimPeriodo(LancamentoResumo selecionado) {
		try {
			log.info("updateDataFimPeriodo(sqResumo = " + selecionado.getSqResumo() + ")");

			// Buscar registro original e verificar se está aberto
			LancamentoResumo lancamentoResumoTemp = dao.findByPrimaryKey(LancamentoResumo.class, selecionado.getSqResumo());
			if(lancamentoResumoTemp.getDtFechado() != null) {
				throw new NegocioException("Alteração de Data fim não é permitido para período fechado.");
			}

			// Buscar último registro do periodo cadastrado e comparar com o registro atual
			LancamentoResumo lancamentoResumoUltimo = this.findUltimoRegistroPorRegiao(selecionado.getRegiao().getSqRegiao());
			if(lancamentoResumoUltimo.getSqResumo().compareTo(selecionado.getSqResumo()) != 0) {
				throw new NegocioException("Alteração permitida somente para o último registro do Resumo.");
			}

			// Não houve alteração na data fim periodo
			if(lancamentoResumoTemp.getDtPeriodoFim() != null && selecionado.getDtPeriodoFim() != null) {
				if(lancamentoResumoTemp.getDtPeriodoFim().isEqual(selecionado.getDtPeriodoFim()))
					throw new NegocioException("Data Fim Periodo informada já é igual a Data Fim Periodo registrada.");

			} else if(lancamentoResumoTemp.getDtPeriodoFim() == null && selecionado.getDtPeriodoFim() == null) {
				throw new NegocioException("Data Fim não informada.");
			}

			// Data do periodo fim deverá ser maior a data do periodo inicio
			if(selecionado.getDtPeriodoFim() != null && selecionado.getDtPeriodoInicio() != null) {
				if(!Uteis.primeiraDataMaiorQueSegundaData(selecionado.getDtPeriodoFim(), selecionado.getDtPeriodoInicio())){
					throw new NegocioException("Data do Periodo Final deverá ser maior que data do Periodo Inicial.");
				}
			}

			// Verificar se foi informado periodo inicio e periodo fim e ajusta-los se não informados
			LocalDate dtPeriodoInicioTemp = selecionado.getDtPeriodoInicio() != null? selecionado.getDtPeriodoInicio(): Uteis.StringParaLocalDate("1900-01-01");
			LocalDate dtPeriodoFimTemp    = selecionado.getDtPeriodoFim() != null? selecionado.getDtPeriodoFim(): Uteis.StringParaLocalDate("2999-12-31");

			// Passar sqResumo das saidas para null
			saidaBO.updateAlterarPeriodoSqResumoParaNulo(selecionado.getSqResumo());

			// Atribuir sqResumo para periodo com a nova data fim nas saidas
			saidaBO.updateAlterarPeriodoSqResumoNovo(selecionado, dtPeriodoInicioTemp, dtPeriodoFimTemp);

			// Passar sqResumo das entradas para null
			entradaBO.updateAlterarPeriodoSqResumoParaNulo(selecionado.getSqResumo());

			// Atribuir sqResumo para periodo com a nova data fim nas entradas
			entradaBO.updateAlterarPeriodoSqResumoNovo(selecionado, dtPeriodoInicioTemp, dtPeriodoFimTemp);

			//Atualizar totais de Entradas e Totais de Saida até o fim de periodo indicado
			selecionado.setVlSaidas(saidaBO.findValorTotalPorPeriodo(selecionado.getSqResumo()));
			selecionado.setVlEntradas(entradaBO.findValorTotalPorPeriodo(selecionado.getSqResumo()));

			selecionado = dao.salvar(selecionado);

			return selecionado;
		} catch (Exception e) {
			throw new NegocioException("Erro ao tentar salvar data fim do período financeiro: " + e.getMessage(), e);
		}
	}

	@Transactional
	public void remover(LancamentoResumo selecionado) {
		try {
			// Validar exclusão

			// Buscar últmo registro registrado para a regiao
			LancamentoResumo resumoUltimo = this.findUltimoRegistroPorRegiao(selecionado.getRegiao().getSqRegiao());

			// Somente pode-se excluir o último registro da região e que não esteja fechado
			if(!resumoUltimo.getSqResumo().equals(selecionado.getSqResumo()))
				throw new NegocioException("Operação não realizada, somente é permitido excluir o último registro cadastrado da Região!");
			if(resumoUltimo.getDtFechado() != null)
				throw new NegocioException("Operação não realizada, Não é permitido excluir resumo FECHADO!");

			//Limpar (passar para nulo) sqResumo em FinanceiroLancamentos e FinanceiroLancamentosSaida (Entradas e Saidas)
			saidaBO.updateAlterarPeriodoSqResumoParaNulo(selecionado.getSqResumo());
			entradaBO.updateAlterarPeriodoSqResumoParaNulo(selecionado.getSqResumo());


			//Remover registro
			dao.delete(this.findByPrimaryKey(selecionado.getSqResumo()), selecionado.getSqResumo());


		} catch (Exception e) {
			throw new NegocioException("Erro ao tentar excluir período: " + e.getMessage(), e);
		}

	}

	@Transactional
	public LancamentoResumo updateFecharPeriodo(LocalDate dtFecharPeriodo, LancamentoResumo selecionado) {
		try {

			// Buscar resumo que está sendo fechado
			LancamentoResumo resumoOriginal = this.findByPrimaryKey(selecionado.getSqResumo());

			// Validações.
			// Periodo não pode estar fechado
			if(resumoOriginal.getDtFechado() != null)
				throw new NegocioException("Periodo já está fechado!");

			// Data fim deve estar preenchida
			if(resumoOriginal.getDtPeriodoFim() == null)
				throw new NegocioException("Antes de fechar periodo, favor informar Data Fim do Periodo!");

			// Verificar se há periodos anteriores não fechados.
			Long qtdeLinhas = dao.qtdePeriodoAnteriorAberto(resumoOriginal.getRegiao().getSqRegiao(), resumoOriginal.getSqResumo());
			if(qtdeLinhas > 0)
				throw new NegocioException("Necessário antes fechar períodos anteriores, há " + qtdeLinhas + " período(s) aberto(s)!");
			
			// Gravar registro
			selecionado.setDtFechado(dtFecharPeriodo);

			return dao.salvar(selecionado);

		} catch (Exception e) {
			throw new NegocioException("Erro ao tentar fechar período: " + e.getMessage(), e);
		}		
	}

	@Transactional
	public LancamentoResumo updateReabrirPeriodo(LancamentoResumo selecionado) {
		try {
			
			// Buscar resumo que está sendo fechado
			LancamentoResumo resumoOriginal = this.findByPrimaryKey(selecionado.getSqResumo());
			
			// Verificar se o período está fechado
			if(resumoOriginal.getDtFechado() == null)
				throw new NegocioException("Período já está aberto.");
			
			
			Long qtdeLinhas = dao.qtdePeriodoPosteriorFechado(resumoOriginal.getRegiao().getSqRegiao(), resumoOriginal.getSqResumo());
			if(qtdeLinhas > 0)
				throw new NegocioException("Somente é permitido reabrir o último período fechado. Há " + qtdeLinhas + " período(s) fecahdo(s) posterior ao atual!");
			

			// Gravar registro
			selecionado.setDtFechado(null);
					
			return dao.salvar(selecionado);
			
		} catch (Exception e) {
			throw new NegocioException("Erro ao tentar reabrir período: " + e.getMessage(), e);
		}
		
		
	}
}
