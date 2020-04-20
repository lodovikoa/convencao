package br.com.convencao.dao;

import java.time.LocalDate;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import br.com.convencao.model.LancamentoResumo;

public class LancamentoResumoDAO extends GenericoDAO<LancamentoResumo> {

	private static final long serialVersionUID = 1L;

	@Inject
	EntityManager manager;
	
	// Buscar registro pela primary key com relacionamentos
	public LancamentoResumo findByPrimaryKeyCompleto(Long sqResumo) {
		try {
			
			LancamentoResumo result = manager.createQuery("from LancamentoResumo l "
					+ "left join fetch l.regiao "
					+ "where l.sqResumo = :sqResumo", LancamentoResumo.class)
					.setParameter("sqResumo", sqResumo)
					.getSingleResult();
			
			return result;
			
		}catch(NoResultException e) {
			return null;
		}
	}

	// Listar lançamentos de um ministro (todos exceto os cancelados)
	public List<LancamentoResumo> findAllPorRegiao(Long sq){
		try {	
			List<LancamentoResumo> result = manager.createQuery("from LancamentoResumo c "
					+ "where c.regiao.sqRegiao = :sq", LancamentoResumo.class)
					.setParameter("sq",sq)
					.getResultList();

			return result;

		}catch(NoResultException e) {
			return null;
		}
	}
	
	// Buscar último registro
	public LancamentoResumo findUltimoRegistroPorRegiao(Long sq) {
		try {
			LancamentoResumo result = manager.createQuery("from LancamentoResumo c "
					+ "where c.regiao.sqRegiao = :sq "
					+ "order by c.sqResumo desc ", LancamentoResumo.class)
					.setParameter("sq", sq)
					.setMaxResults(1)
					.getSingleResult();
			
			return result;
		}catch(NoResultException e) {
			return null;
		}
	}
	
	
	// Obter quantidade de registro anterior ao atual e em aberto
	public Long qtdePeriodoAnteriorAberto(Long sqRegiao, Long sqResumo) {
		try {
			
			Long result = (Long) manager.createQuery("select count(0) from LancamentoResumo c where c.dtFechado is null and c.regiao.sqRegiao = :sqRegiao and c.sqResumo < :sqResumo", Long.class)
					.setParameter("sqRegiao", sqRegiao)
					.setParameter("sqResumo", sqResumo)
					.getSingleResult();
			
			return result;
			
		} catch(NoResultException e) {
			return null;
		}
	}

	// Obter quantidade de registros posterior ao atual fechados
	public Long qtdePeriodoPosteriorFechado(Long sqRegiao, Long sqResumo) {
		try {
			
			Long result = (Long) manager.createQuery("select count(*) from LancamentoResumo c where c.sqResumo = "
					+ "(select max(t.sqResumo) from LancamentoResumo t where t.dtFechado is not null and t.sqResumo > :sqResumo  and t.regiao.sqRegiao = :sqRegiao)")
					.setParameter("sqRegiao", sqRegiao)
					.setParameter("sqResumo", sqResumo)
					.getSingleResult();
			
			return result;
		} catch(NoResultException e) {
			return null;
		}
	}
	
	// Buscar lista de registros de uma região e maioir que o sqResumo atual, para identificar atualização de saldo anterior em resumos alterados.
	public List<LancamentoResumo> findAllPosteriorPorRegiao(Long sqRegiao, Long sqResumo) {
		try {
			List<LancamentoResumo> result = manager.createQuery("from LancamentoResumo l where l.regiao.sqRegiao = :sqRegiao and l.sqResumo > :sqResumo", LancamentoResumo.class)
					.setParameter("sqRegiao", sqRegiao)
					.setParameter("sqResumo", sqResumo)
					.getResultList();
			
			return result;
			
		} catch(NoResultException e) {
			return null;
		}
	}

	// Buscar um lançamentoResumo de uma região e 
	public LancamentoResumo findByPorDataPagamento(Long sqRegiao, LocalDate dtRecibo) {
		try {
			StringBuilder sql = new StringBuilder();
			
			sql.append("from LancamentoResumo lar ");
			sql.append("where lar.regiao.sqRegiao = :sqRegiao ");
			sql.append("and (lar.dtPeriodoInicio <= :dtRecibo or lar.dtPeriodoInicio is null) ");
			sql.append("and (lar.dtPeriodoFim >= :dtRecibo or lar.dtPeriodoFim is null)");
			
			LancamentoResumo result = manager.createQuery(sql.toString(), LancamentoResumo.class)
						.setParameter("sqRegiao", sqRegiao)
						.setParameter("dtRecibo", dtRecibo)
						.getSingleResult();
			
			return result;
		}catch(NoResultException e) {
			return null;
		}		
	}
	
}
