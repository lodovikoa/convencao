package br.com.convencao.bo;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import br.com.convencao.dao.LancamentoSaidaDAO;
import br.com.convencao.dao.NsuDAO;
import br.com.convencao.model.LancamentoResumo;
import br.com.convencao.model.LancamentoSaida;
import br.com.convencao.model.Nsu;
import br.com.convencao.util.Uteis;
import br.com.convencao.util.jpa.Transactional;

public class LancamentoSaidaBO extends LancamentoGenericoBO {

	private static final long serialVersionUID = 1L;

	private static Log log = LogFactory.getLog(LancamentoSaidaBO.class);

	@Inject
	LancamentoSaidaDAO dao;

	@Inject
	NsuDAO nsuDAO;

	public LancamentoSaida findByPrimaryKey(Long sqSaida) {
		log.info("findByPrimaryKey(" + sqSaida + ")");

		return dao.findByPrimaryKeyCompleto(sqSaida);
	}

	public List<LancamentoSaida> findAllPorPeriodo(Long sqResumo) {
		log.info("findAllPorPeriodo(" + sqResumo + ")");

		return dao.findAllPorPeriodo(sqResumo);
	}

	public BigDecimal findValorTotalSaidasSemPeriodo(Long sqRegiao) {
		log.info("findValorTotalSaidasSemPeriodo(" + sqRegiao + ")");

		return dao.findValorTotalSaidasSemPeriodo(sqRegiao);
	}

	protected int updateAlterarPeriodoSqResumoParaNulo(Long sqResumo) {
		log.info("Protected updateAlterarPeriodoSqResumoParaNulo(" + sqResumo + ")");

		return dao.updateAlterarPeriodoSqResumoParaNulo(sqResumo);
	}

	protected int updateAlterarPeriodoSqResumoNovo(LancamentoResumo selecionado, LocalDate dtPeriodoInicioTemp, LocalDate dtPeriodoFimTemp) {
		log.info("Protected updateAlterarPeriodoSqResumoNovo(" + selecionado.getSqResumo() + ", " + selecionado.getRegiao().getSqRegiao() +", " + dtPeriodoInicioTemp + ", " + dtPeriodoFimTemp +")");

		return dao.updateAlterarPeriodoSqResumoNovo(selecionado, dtPeriodoInicioTemp, dtPeriodoFimTemp);

	}

	protected BigDecimal findValorTotalPorPeriodo(Long sqResumo) {
		log.info("findValorTotalPorPeriodo(" + sqResumo + ")");
		return dao.findvalortotalPorPeriodo(sqResumo);
	}

	@Transactional
	public void salvarIncuirSaida(LancamentoSaida saida, LancamentoResumo resumo) {
		try {
			log.info("salvarIncuirSaida(" + saida.getSqSaida() + ")");

			// Validações
			validarLancamentoSaida(saida);

			// ajustar LancamentoResumo
			ajustarLancamentoResumo("Saida", resumo.getRegiao().getSqRegiao(),  resumo.getSqResumo(), saida.getVlSaida(), BigDecimal.ZERO, saida.getDtSaida());

			// Preparar e gravar registro de saída
			saida.setLancamentoResumo(resumo);
			saida.setRegiao(resumo.getRegiao());
			saida.setCdNsu(nsuDAO.salvar(new Nsu()).getSqNsu());
			saida.setDtRegistro(Uteis.DataHoje().toLocalDate());
			saida.setAuditoriaUsuario(Uteis.UsuarioLogado().getUsuario().getDsLogin());
			saida.setAuditoriaData(Uteis.DataHoje());
			
			// Verificar se o lançamento foi repetido com mesmos dados.
			if(this.verificarRegistroRepetido(saida)) {
				throw new NegocioException("Registro de Saida repetido. Favor verificar");
			}

			dao.salvar(saida);

		} catch (Exception e) {
			e.printStackTrace();
			throw new NegocioException("Erro ao salvar inclusão registro de saida: " + e.getMessage());
		}
	}

	@Transactional
	public void salvarAlterarSaida(LancamentoSaida saida) {
		try {
			log.info("salvarAlterarSaida(" + saida.getSqSaida() + ")");

			// Validações
			this.validarLancamentoSaida(saida);

			// Verificar se o lançamento foi repetido com mesmos dados.
			if(this.verificarRegistroRepetido(saida)) {
				throw new NegocioException("Registro de Saida repetido. Favor verificar");
			}



			// Buscar valor do registro anterior
			BigDecimal vlLancamentoAnterior = dao.find(LancamentoSaida.class, saida.getSqSaida()).getVlSaida();

			// ajustar LancamentoResumo
			ajustarLancamentoResumo("Saida", saida.getLancamentoResumo().getRegiao().getSqRegiao(),  saida.getLancamentoResumo().getSqResumo(), saida.getVlSaida(), vlLancamentoAnterior, saida.getDtSaida());

			// Preparar e gravar registro de saída
			saida.setDtRegistro(Uteis.DataHoje().toLocalDate());
			saida.setAuditoriaUsuario(Uteis.UsuarioLogado().getUsuario().getDsLogin());
			saida.setAuditoriaData(Uteis.DataHoje());

			// Verificar registro de saida repetito
			if(dao.verificarRegistroRepetido(saida) > 0) {
				throw new NegocioException("Registro de saída repedito. Favor verificar.");
			}

			dao.salvar(saida);

		} catch (Exception e) {
			e.printStackTrace();
			throw new NegocioException("Erro ao salvar alteração registro de saida: " + e.getMessage());
		}

	}
	
	@Transactional
	public void salvarExcluirSaida(LancamentoSaida saida) {
		try {
			log.info("salvarExcluirSaida(" + saida.getSqSaida() + ")");
			
			// Buscar registro para exclusão
			saida = dao.findByPrimaryKeyCompleto(saida.getSqSaida());
			
			// ajustar LancamentoResumo -- vlTotalAnterior = Valor total da entrada que será excluido
			ajustarLancamentoResumo("Saida", saida.getRegiao().getSqRegiao(), saida.getLancamentoResumo().getSqResumo(), BigDecimal.ZERO, saida.getVlSaida(), saida.getDtSaida());
			
			dao.delete(saida, saida.getSqSaida());
			
		}catch (Exception e) {
			e.printStackTrace();
			throw new NegocioException("Erro ao salvar EXCLUSÃO de saidas: " + e.getMessage());
		}
		
	}

	protected void validarLancamentoSaida(LancamentoSaida saida) {
		log.info("validarLancamentoSaida(" + saida.getSqSaida() + ")");

		// Retirar possíveis espaços em branco de strings 
		if(saida.getCdDocumento() != null) saida.setCdDocumento(saida.getCdDocumento().trim());
		if(saida.getDsHistorico() != null) saida.setDsHistorico(saida.getDsHistorico().trim());
		if(saida.getNmFaforecido() != null) saida.setNmFaforecido(saida.getNmFaforecido().trim());

		// Verificar campos obrigatórios preenchidos
		if(saida.getDepartamento() == null || saida.getDepartamento().getSqDepartamento() < 1) {
			throw new NegocioException("Faltou selecionar o departamento.");
		}

		if(saida.getPlanoConta() == null || saida.getPlanoConta().getSqPlanoConta() < 1) {
			throw new NegocioException("Faltou selecionar a conta contábil.");
		}

		if(StringUtils.isBlank(saida.getNmFaforecido())) {
			throw new NegocioException("Faltou preencher o favorecido.");
		}

		if(StringUtils.isBlank(saida.getCdDocumento())) {
			throw new NegocioException("Faltou preencher o número do documento.");
		}

		if(saida.getDtSaida() == null) {
			throw new NegocioException("Faltou preencher a data do lançamento.");
		}

		// Verificar se valor foi preenchido adequadamente
		if(saida.getVlSaida() == null || saida.getVlSaida().signum() <= 0) {
			throw new NegocioException("Valor inválido.");
		}

		if(saida.getCdFormaPagamento() == null || saida.getCdFormaPagamento() < 1) {
			throw new NegocioException("Faltou preencher a forma de pagamento.");
		}

		if(StringUtils.isBlank(saida.getDsHistorico())) {
			throw new NegocioException("Faltou preencher o histórico.");
		}
	}

	// Verificar registro repetido
	protected boolean verificarRegistroRepetido(LancamentoSaida saida) {
		log.info("verificarRegistroRepetido(" + saida.getSqSaida() + ")");
		return dao.verificarRegistroRepetido(saida) > 0;
	}

}
