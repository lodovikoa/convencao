package br.com.convencao.dao;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import org.apache.commons.lang3.StringUtils;

import br.com.convencao.model.Lancamento;
import br.com.convencao.model.LancamentoResumo;
import br.com.convencao.model.to.LancamentoEntradaCpl;
import br.com.convencao.util.Uteis;

public class LancamentoDAO extends GenericoDAO<Lancamento> {

	private static final long serialVersionUID = 1L;

	@Inject
	EntityManager manager;
	
	// Buscar lançamento de entrada pela primary key completa
	public Lancamento findByPrimaryKey(Long sq) {
		try {
			Lancamento result = manager.createQuery("from Lancamento c "
					+ "left join fetch c.recibo "
					+ "left join fetch c.igreja "
					+ "left join fetch c.planoConta "
					+ "where c.sqLancamento = :sq", Lancamento.class)
					.setParameter("sq", sq)
					.getSingleResult();
			
			return result;
		} catch (NoResultException e) {
			return null;
		}
		
	}
	
	// Buscar todos os registros de Lançamentos de um RegLançamento (Ativos e cancelados)
	public List<Lancamento> findAllLancamentosPorRegLancamento(Long sqRegLancamento) {
		try {
			List<Lancamento> result = manager.createQuery("from Lancamento lan "
					+ "left join fetch lan.recibo "
					+ "where lan.regLancamento.sqRegLancamento = :sqRegLancamento ", Lancamento.class)
					.setParameter("sqRegLancamento", sqRegLancamento)
					.getResultList();
			
			return result;
			
		} catch (NoResultException e) {
			return null;
		}
	}

	public BigDecimal findValorTotalEntradasSemPeriodo(Long sqRegiao) {
		try {
			
			String sql = "select sum(l.vlPagamento) from Lancamento l "
					+ "left join l.recibo r "
					+ "where l.lancamentoResumo.sqResumo is null "
					+ "and r.dtCancelado is null "
					+ "and r.regiao.sqRegiao = :sqRegiao";
			
			BigDecimal result = manager.createQuery(sql, BigDecimal.class)
					.setParameter("sqRegiao", sqRegiao)
					.getSingleResult();
					
			return (result == null)? BigDecimal.ZERO: result;
			
		}catch(NoResultException e) {
			return null;
		}
	}
	

	// Alterar todos os lancamentos de um determinado resumo (sqResumo) para null
	public int updateAlterarPeriodoSqResumoParaNulo(Long sqResumo) {
		try {
			
			String sql = "update Lancamento l set l.lancamentoResumo.sqResumo = null, l.auditoriaUsuario = :auditoriaUsuario, l.auditoriaData = :auditoriaData "
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
	

	// Alterar todos os lancamentos atribuindo novo sqResumo, para os lacamentos que estejam sem resumo e dentro do periodo
	public int updateAlterarPeriodoSqResumoNovo(LancamentoResumo selecionado, LocalDate dtPeriodoInicioTemp, LocalDate dtPeriodoFimTemp) {
		try {
			
			String sql = "update Lancamento l set l.lancamentoResumo.sqResumo = :sqResumo, l.auditoriaUsuario = :auditoriaUsuario, l.auditoriaData = :auditoriaData "
					+ "where l.lancamentoResumo.sqResumo is null "
					+ "and l.recibo in (select r1.sqRecibo from Recibo r1 where r1.dtCancelado is null) "
					+ "and l.dtPagamento between :dtPeriodoInicioTemp and :dtPeriodoFimTemp "
					+ "and l.recibo in (select r.sqRecibo from Recibo r where r.regiao.sqRegiao = :sqRegiao)";
			
			// update tb_lan_lancamento lan, tb_rcb_recibo rcb set lan.lar_sq_resumo = ?, lan.auditoria_usuario = ?, lan.auditoria_data = ? 
			// where lan.lar_sq_resumo is null and rcb.rcb_dt_cancelado is null and (lan.lan_dt_pagamento between ? and ? )  and (lan.rcb_sq_recibo = rcb.rcb_sq_recibo and rcb.rgn_sq_regiao = ? )
			
			return manager.createQuery(sql)
					.setParameter("sqResumo", selecionado.getSqResumo())
					.setParameter("auditoriaUsuario", Uteis.UsuarioLogado().getUsuario().getDsLogin())
					.setParameter("auditoriaData", Uteis.DataHoje())
					.setParameter("dtPeriodoInicioTemp", dtPeriodoInicioTemp)
					.setParameter("dtPeriodoFimTemp", dtPeriodoFimTemp)
					.setParameter("sqRegiao", selecionado.getRegiao().getSqRegiao())
					.executeUpdate();
			
		}catch(NoResultException e) {
			return -10;
		}
	}


	public BigDecimal findValorTotalPorPeriodo(Long sqResumo) {
		try {
			
			String sql = "select sum(l.vlPagamento) from Lancamento l where l.lancamentoResumo.sqResumo = :sqResumo";
			
			BigDecimal result = manager.createQuery(sql, BigDecimal.class)
					.setParameter("sqResumo", sqResumo)
					.getSingleResult();
			

			return (result == null)? BigDecimal.ZERO: result;
					
		}catch(NoResultException e) {
			return null;
		}
	}

	// Buscar todos os lacamentos de entrada de um resumo
	public List<LancamentoEntradaCpl> findAllPorResumo(Long sqResumo, StringBuilder dsCodOrigem) {
		try {
			StringBuilder sql = new StringBuilder();
			
			sql.append("select new br.com.convencao.model.to.LancamentoEntradaCpl"
					+ "(lan.sqLancamento, rcb.sqRecibo, lan.cdOrigem, plc.dsConta, lan.dtRegistro, lan.dtPagamento, rcb.cdNsu, lan.vlPagamento, mit.nmNome, igr.dsIgreja, lan.nmOutros, rgn.sqRegiao, lan.auditoriaUsuario) "
					+ "from Lancamento lan "
					+ "left join lan.regLancamento reg "
					+ "left join lan.planoConta plc "
					+ "left join lan.recibo rcb "
					+ "left join rcb.regiao rgn "
					+ "left join lan.igreja igr "
					+ "left join reg.ministro mit "
					+ "where rcb.dtCancelado is null and lan.lancamentoResumo.sqResumo = :sqResumo ");
			
			//Prepara para consultar recebimentos somente de Ministros ou somente de Igrejas ou Somente de Outros ou tudo.
			//cdCodTemp = 1 (Recebimento de Ministros) - cdCodTemp = 2 (Recebimento de Igrejas) - cdCodTemp = 3 (Recebimento de Outros)
			Integer cdCodTemp;
			if(dsCodOrigem.toString() != null && dsCodOrigem.toString().trim().length() > 0) {
				sql.append(" and lan.cdOrigem in(");
				for (int i = 0; i < dsCodOrigem.toString().trim().length(); i++) {
					cdCodTemp = Integer.parseInt(dsCodOrigem.toString().substring(i, i+1));
					if(i > 0)
						sql.append(" ,");
		
					sql.append(cdCodTemp);
				}
				sql.append(") ");
			}
			
			sql.append("order by lan.dtPagamento, rcb.cdNsu ");
			

			List<LancamentoEntradaCpl> result = manager.createQuery(sql.toString(), LancamentoEntradaCpl.class)
					.setParameter("sqResumo", sqResumo)
					.getResultList();

						
			return result;

		}catch(NoResultException e) {
			return null;
		}
	}
	
	// Verificar Registro de lanaçamento de entrada repetido
	public Long verificarRegistroRepetidoIgrejaOutros(Lancamento entrada) {
		try {
			
			StringBuilder sql = new StringBuilder();
			
			sql.append("select count(0) from Lancamento lan ");
			sql.append("where lan.dtPagamento = :dtPagamento ");
			sql.append("and lan.cdOrigem = :cdOrigem ");
			sql.append("and lan.planoConta.sqPlanoConta = :sqPlanoConta ");
			sql.append("and lan.vlPagamento = :vlPagamento ");
			sql.append("and lan.recibo.dsHistorico = :dsHistorico ");
			
			// Verificar repetição para Igreja ou Outros, conforme registrado.
			if(StringUtils.isNotBlank(entrada.getNmOutros())) {
				sql.append("and lan.nmOutros = ");
				sql.append("'");
				sql.append(entrada.getNmOutros());
				sql.append("'");
			} else {
				sql.append("and lan.igreja.sqIgreja = ");
				sql.append(entrada.getIgreja().getSqIgreja());
			}
			
			// Verificar repetição para registro alterado
			if(entrada.getSqLancamento() != null && entrada.getSqLancamento() > 0) {
				sql.append(" and lan.sqLancamento <> ");
				sql.append(entrada.getSqLancamento());
			}
			
			
			Long result = (Long) manager.createQuery(sql.toString(), Long.class)
					.setParameter("dtPagamento", entrada.getDtPagamento())
					.setParameter("cdOrigem", entrada.getCdOrigem())
					.setParameter("sqPlanoConta", entrada.getPlanoConta().getSqPlanoConta())
					.setParameter("vlPagamento", entrada.getVlPagamento())
					.setParameter("dsHistorico", entrada.getRecibo().getDsHistorico())
					.getSingleResult();
			
			return result;
			
		} catch(NoResultException e) {
			return 0L;
		}
		
	}
	
	// Buscar Total pago de um determinado RegLancamento
	public BigDecimal  findValorTotalPorRegLancamento(Long sqRegLancamento) {
		try {
			
			StringBuilder sql = new StringBuilder();
			
			sql.append("select sum(lan.vlPagamento) from Lancamento lan ")
				.append("right join lan.recibo rcb on rcb.dtCancelado is null ")
				.append("where lan.regLancamento.sqRegLancamento = :sqRegLancamento");
			
			BigDecimal result = manager.createQuery(sql.toString(), BigDecimal.class)
					.setParameter("sqRegLancamento", sqRegLancamento)
					.getSingleResult();
			
			return (result == null)? BigDecimal.ZERO: result;
		
			
		} catch(NoResultException e) {
			return null;
		}
	}
	
	// Retirar data de pagamento dos lançamentos que foram CANCELADOS.
	public int updateRetirarDataPagamento(Long sqRecibo) {
		try {
			
			String sql = "update Lancamento l set l.dtPagamento = null, l.auditoriaUsuario = :auditoriaUsuario, l.auditoriaData = :auditoriaData "
					+ "where l.recibo.sqRecibo = :sqRecibo ";
			
			return manager.createQuery(sql)
					.setParameter("sqRecibo", sqRecibo)
					.setParameter("auditoriaUsuario", Uteis.UsuarioLogado().getUsuario().getDsLogin())
					.setParameter("auditoriaData", Uteis.DataHoje())
					.executeUpdate();
			
		}catch(NoResultException e) {
			return -10;
		}
	}
	
	// Listar pagamentos feitos por Igrejas, registrados na data atual e pelo usuário logado
	public List<LancamentoEntradaCpl> findAllPagamentosIgrejaUsuario() {
		try {
			StringBuilder sql = new StringBuilder();
			
			sql.append("select new br.com.convencao.model.to.LancamentoEntradaCpl");
			sql.append("(lan.sqLancamento, lan.recibo.sqRecibo, lan.cdOrigem, lan.planoConta.dsConta, lan.dtRegistro, lan.dtPagamento, lan.recibo.cdNsu, lan.vlPagamento, '', lan.igreja.dsIgreja, lan.nmOutros, 0L, lan.auditoriaUsuario)");
			sql.append("from Lancamento lan ");

			sql.append("where lan.cdOrigem = 2 ");
			sql.append("and lan.recibo.dtCancelado is null ");
			sql.append("and date(lan.dtRegistro) = date(NOW()) ");
			sql.append("and lan.auditoriaUsuario like :usuarioLogado");
			
			List<LancamentoEntradaCpl> result = manager.createQuery(sql.toString(), LancamentoEntradaCpl.class)
					.setParameter("usuarioLogado", Uteis.UsuarioLogado().getUsername())
					.getResultList();
			
			return result;

		} catch (NoResultException e) {
			return null;
		}
		
	}

}
