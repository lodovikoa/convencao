package br.com.convencao.dao;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import br.com.convencao.model.Igreja;
import br.com.convencao.model.Recibo;
import br.com.convencao.model.to.ReciboCpl;

public class ReciboDAO extends GenericoDAO<Recibo> {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private EntityManager manager;
	
	public Recibo findByPorNsu(Long cdNsu) {
		try {
			Recibo result = manager.createQuery("from Recibo r "
					+ "left join fetch r.regiao "
					+ "where r.cdNsu = :cdNsu", Recibo.class)
					.setParameter("cdNsu", cdNsu)
					.getSingleResult();
			
			return result;
		} catch (NoResultException e) {
			return null;
		}
	}
	
	public ReciboCpl buscarDadosRecibo(Long sqRecibo) {
		try {
			StringBuilder sql = new StringBuilder();
			
			sql.append("select new br.com.convencao.model.to.ReciboCpl(sum(lan.vlPagamento), rcb.cdNsu, igr.dsIgreja, igr.dsCnpj, cgo.dsTitulo, mit.nmNome, rcb.dtRecibo, rcb.dsHistorico, rgn.dsRegiao, lan.nmOutros) ")
					.append("from Recibo rcb ")
					.append("left join Lancamento lan on lan.recibo.sqRecibo = rcb.sqRecibo ")
					.append("left join RegLancamento rgl on rgl.sqRegLancamento = lan.regLancamento.sqRegLancamento ")
					.append("left join Ministro mit on mit.sqMinistro = rgl.ministro.sqMinistro ")
					.append("left join Igreja igr on igr.sqIgreja = mit.igreja.sqIgreja ")
					.append("left join Cargo cgo on cgo.sqCargo = mit.cargo.sqCargo ")
					.append("left join Regiao rgn on rgn.sqRegiao = rcb.regiao.sqRegiao ")
					.append("where rcb.sqRecibo = :sqRecibo ")
					.append("group by rcb.cdNsu, igr.dsIgreja, igr.dsCnpj, cgo.dsTitulo, mit.nmNome, rcb.dtRecibo, lan.nmOutros");
			
			ReciboCpl reciboCpl = manager.createQuery(sql.toString(), ReciboCpl.class)
									.setParameter("sqRecibo", sqRecibo)
									.getSingleResult();
			
			return reciboCpl;
		} catch (NoResultException e) {
			return null;
		}
	}
	
	// Buscar a Igreja de um recebimento de Igrejas
	public Igreja buscarDadosIgrejaDoRecibo(Long sqRecibo  ) {
		try {
			StringBuilder sql = new StringBuilder();
			
			sql.append("select igr from Igreja igr ")
					.append("left join Lancamento lan on lan.igreja.sqIgreja = igr.sqIgreja ")
					.append("where lan.recibo.sqRecibo = :sqRecibo");
			
			Igreja result = manager.createQuery(sql.toString(), Igreja.class)
						.setParameter("sqRecibo", sqRecibo)
						.getSingleResult();
			
			return result;
			
		} catch (NoResultException e) {
			return null;
		}
	}

}
