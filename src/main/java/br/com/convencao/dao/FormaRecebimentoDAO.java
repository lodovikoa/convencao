package br.com.convencao.dao;

import java.math.BigDecimal;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import br.com.convencao.model.FormaRecebimento;
import br.com.convencao.model.to.FormaRecebimentoPorReciboCpl;

public class FormaRecebimentoDAO extends GenericoDAO<FormaRecebimento> {

	private static final long serialVersionUID = 1L;

	@Inject
	EntityManager manager;
	
	// Listar registros por recibo (sqRecibo)
	public List<FormaRecebimentoPorReciboCpl> findAllReciboCplPorSqRecibo(Long sqRecibo, boolean flSoComValores) {
		try {
			StringBuilder sql = new StringBuilder();
			
			sql.append("select new br.com.convencao.model.to.FormaRecebimentoPorReciboCpl ")
					.append("(fp.sqFormaPagamento, fp.dsFormaPagamento, fp.flExibirComplemento, fr.sqFormaRecebimento, fr.recibo.sqRecibo, fr.vlRecebido, fr.vlContabil, fr.dsComplemento) ")
					.append("from FormaRecebimento fr ")
					.append("left join fr.recibo rc ")
					.append("right join fr.formaPagamento fp with (rc.sqRecibo = :sqRecibo) ");
			
			if(flSoComValores)
				sql.append("where fr.vlContabil > 0 ");
			
			sql.append("order by fp.cdOrdem");

			List<FormaRecebimentoPorReciboCpl> result = manager.createQuery(sql.toString(), FormaRecebimentoPorReciboCpl.class)
					.setParameter("sqRecibo", sqRecibo)
					.getResultList();
			
			return result;
			
		} catch (NoResultException e) {
			return null;
		}

	}
	
	// Listar registros por recibo (cdNsu)
	public List<FormaRecebimentoPorReciboCpl> findAllReciboCplPorCdNsu(Long cdNsu, boolean flSoComValores) {
		try {
			StringBuilder sql = new StringBuilder();
			
			sql.append("select new br.com.convencao.model.to.FormaRecebimentoPorReciboCpl ");
			sql.append("(fp.sqFormaPagamento, fp.dsFormaPagamento, fp.flExibirComplemento, fr.sqFormaRecebimento, fr.recibo.sqRecibo, fr.vlRecebido, fr.vlContabil, fr.dsComplemento) ");
			sql.append("from FormaRecebimento fr ");
			sql.append("left join fr.recibo rc ");
			sql.append("right join fr.formaPagamento fp with (rc.cdNsu = :cdNsu) ");
			
			if(flSoComValores) {
				sql.append("where fr.vlContabil > 0 ");
			}
			
			sql.append("order by fp.cdOrdem");
			
			List<FormaRecebimentoPorReciboCpl> result = manager.createQuery(sql.toString(), FormaRecebimentoPorReciboCpl.class)
					.setParameter("cdNsu", cdNsu)
					.getResultList();
			
			return result;
			
		} catch (NoResultException e) {
			return null;
		}

	}
	

	// Buscar valor total de um recibo
	public BigDecimal findValorTotalPorRecibo(Long cdNsu) {
		try {
			String sql = "select sum(f.vlContabil) from FormaRecebimento f where f.recibo.cdNsu = :cdNsu";
			
			BigDecimal result = manager.createQuery(sql, BigDecimal.class)
					.setParameter("cdNsu", cdNsu)
					.getSingleResult();
			
			return (result == null)? BigDecimal.ZERO: result;
			
		} catch(NoResultException e) {
			return null;
		}
	}
	
	// Listar os recebimentos de um recibo
	public List<FormaRecebimento> findAllPorReciboSimples(Long sqRecibo) {
		try {
			String sql = "from FormaRecebimento fr "
					+ "left join fetch fr.formaPagamento fp "
					+ "where fr.recibo.sqRecibo = :sqRecibo";
			
			List<FormaRecebimento> result = manager.createQuery(sql, FormaRecebimento.class)
					.setParameter("sqRecibo", sqRecibo)
					.getResultList();
			
			return result;
			
		} catch (NoResultException e) {
			return null;
		}
	}
}
