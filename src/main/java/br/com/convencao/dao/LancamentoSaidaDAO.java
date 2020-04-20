package br.com.convencao.dao;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import br.com.convencao.model.LancamentoResumo;
import br.com.convencao.model.LancamentoSaida;
import br.com.convencao.util.Uteis;

public class LancamentoSaidaDAO extends GenericoDAO<LancamentoSaida> {

	private static final long serialVersionUID = 1L;

	@Inject
	EntityManager manager;
	
	
	public LancamentoSaida findByPrimaryKeyCompleto(Long sqSaida) {
		try {
			LancamentoSaida result = manager.createQuery("from LancamentoSaida l "
					+ "left join fetch l.lancamentoResumo "
					+ "left join fetch l.regiao "
					+ "left join fetch l.planoConta "
					+ "left join fetch l.departamento "
					+ "where l.sqSaida = :sqSaida", LancamentoSaida.class)
					.setParameter("sqSaida", sqSaida)
					.getSingleResult();
			
			return result;
			
		}catch (NoResultException e) {
			return null;
		}
	}
	
	public BigDecimal findValorTotalSaidasSemPeriodo(Long sqRegiao) {
		try {
			
			String sql = "select sum(l.vlSaida) from LancamentoSaida l "
					+ "where l.lancamentoResumo.sqResumo is null and l.regiao.sqRegiao = :sqRegiao";
			
			BigDecimal result = manager.createQuery(sql, BigDecimal.class)
					.setParameter("sqRegiao", sqRegiao)
					.getSingleResult();
			
			return (result == null)? BigDecimal.ZERO: result;
			
		}catch(NoResultException e) {
			return null;
		}
	}

	public int updateAlterarPeriodoSqResumoParaNulo(Long sqResumo) {
		try {
			
			String sql = "update LancamentoSaida l set l.lancamentoResumo.sqResumo = null, l.auditoriaUsuario = :auditoriaUsuario, l.auditoriaData = :auditoriaData "
					+ "where l.lancamentoResumo.sqResumo = :sqResumo";
			
			return manager.createQuery(sql)
					.setParameter("sqResumo", sqResumo)
					.setParameter("auditoriaUsuario", Uteis.UsuarioLogado().getUsuario().getDsLogin())
					.setParameter("auditoriaData", Uteis.DataHoje())
					.executeUpdate();
							
		}catch(NoResultException e) {
			return -10;
		}
		
	}
	
	public int updateAlterarPeriodoSqResumoNovo(LancamentoResumo selecionado, LocalDate dtPeriodoInicioTemp,LocalDate dtPeriodoFimTemp) {
		try {
			
			String sql = "update LancamentoSaida ls set ls.lancamentoResumo.sqResumo = :sqResumo, ls.auditoriaUsuario = :auditoriaUsuario, ls.auditoriaData = :auditoriaData "
					+ "where ls.lancamentoResumo is null "
					+ "and ls.regiao.sqRegiao = :sqRegiao "
					+ "and ls.dtSaida between :dtPeriodoInicioTemp and :dtPeriodoFimTemp";
			
			return manager.createQuery(sql)
					.setParameter("sqResumo", selecionado.getSqResumo())
					.setParameter("auditoriaUsuario", Uteis.UsuarioLogado().getUsuario().getDsLogin())
					.setParameter("auditoriaData", Uteis.DataHoje())
					.setParameter("sqRegiao", selecionado.getRegiao().getSqRegiao())
					.setParameter("dtPeriodoInicioTemp", dtPeriodoInicioTemp)
					.setParameter("dtPeriodoFimTemp", dtPeriodoFimTemp)
					.executeUpdate();
			
		}catch(NoResultException e) {
			return -10;
		}
	}

	public BigDecimal findvalortotalPorPeriodo(Long sqResumo) {
		try {
			
			String sql = "select sum(l.vlSaida) from LancamentoSaida l where l.lancamentoResumo.sqResumo = :sqResumo";
			
			BigDecimal result = manager.createQuery(sql, BigDecimal.class)
					.setParameter("sqResumo", sqResumo)
					.getSingleResult();
			
			return (result == null)? BigDecimal.ZERO: result;
			
		}catch(NoResultException e) {
			return null;
		}
	}
	
	public List<LancamentoSaida> findAllPorPeriodo(Long sqResumo) {
		try {
			
			String sql = "from LancamentoSaida l "
					+ "left join fetch l.planoConta "
					+ "where l.lancamentoResumo.sqResumo = :sqResumo";
			
			return manager.createQuery(sql, LancamentoSaida.class)
					.setParameter("sqResumo", sqResumo)
					.getResultList();
		
		} catch(NoResultException e) {
			return null;
		}
	}
	
	// Verificar Registro de lanaçamento de saida repetido
	public Long verificarRegistroRepetido(LancamentoSaida saida) {
		try {
			
			StringBuilder sql = new StringBuilder();
			
			sql.append("select count(0) from LancamentoSaida lan ");
			sql.append("where lan.lancamentoResumo.sqResumo = :sqResumo ");
			sql.append("and lan.regiao.sqRegiao = :sqRegiao ");
			sql.append("and lan.planoConta.sqPlanoConta = :sqPlanoConta ");
			sql.append("and lan.departamento.sqDepartamento = :sqDepartamento ");
			sql.append("and lan.cdFormaPagamento = :cdFormaPagamento ");
			sql.append("and lan.dtRegistro = :dtRegistro ");
			sql.append("and lan.dtSaida = :dtSaida ");
			sql.append("and lan.cdDocumento = :cdDocumento ");
			sql.append("and lan.nmFaforecido = :nmFavorecido ");
			sql.append("and lan.vlSaida = :vlSaida ");
		
			// Verificar repetição para registro alterado
			if(saida.getSqSaida() != null && saida.getSqSaida() > 0) {
				sql.append("and lan.sqSaida <> ");
				sql.append(saida.getSqSaida());
			}
			
			Long result = (Long) manager.createQuery(sql.toString(), Long.class)
					.setParameter("sqResumo", saida.getLancamentoResumo().getSqResumo())
					.setParameter("sqRegiao", saida.getRegiao().getSqRegiao())
					.setParameter("sqPlanoConta", saida.getPlanoConta().getSqPlanoConta())
					.setParameter("sqDepartamento", saida.getDepartamento().getSqDepartamento())
					.setParameter("cdFormaPagamento", saida.getCdFormaPagamento())
					.setParameter("dtRegistro", saida.getDtRegistro())
					.setParameter("dtSaida", saida.getDtSaida())
					.setParameter("cdDocumento", saida.getCdDocumento())
					.setParameter("nmFavorecido", saida.getNmFaforecido())
					.setParameter("vlSaida", saida.getVlSaida())
					.getSingleResult();
			
			return result;
			
		} catch(NoResultException e) {
			return 0L;
		}
		
	}

}
