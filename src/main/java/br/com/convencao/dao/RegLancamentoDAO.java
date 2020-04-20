package br.com.convencao.dao;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;

import br.com.convencao.model.Ministro;
import br.com.convencao.model.RegLancamento;
import br.com.convencao.model.Regiao;
import br.com.convencao.model.to.RegLancamentoCpl;
import br.com.convencao.model.to.RegLancamentoListLote;
import br.com.convencao.util.Uteis;

public class RegLancamentoDAO extends GenericoDAO<RegLancamento> {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;

	// Listar lançamentos de um ministro (todos exceto os cancelados)
	public List<RegLancamentoCpl> findLancamentosMinistro(Long sqMinistro, Long cdNsu, boolean flLancamentoEmAberto, boolean flLancamentoPago){
		try {	

			StringBuilder sql = new StringBuilder();

			sql.append("select new br.com.convencao.model.to.RegLancamentoCpl");
			sql.append("(r.sqRegLancamento, r.dtVencimento, pc.sqPlanoConta, pc.dsConta, t.dsTipoLancamento, r.vlLancamento, r.dtRegistro, l.dtPagamento, l.vlPagamento, rc.cdNsu, rg.dsRegiao, lr.dtFechado) ");
			sql.append("from RegLancamento r ");
			sql.append("inner join r.ministro m ");
			sql.append("left join Lancamento l on l.regLancamento.sqRegLancamento = r.sqRegLancamento ");
			sql.append("and l.recibo.sqRecibo = ");
			sql.append("(select distinct(rcb2.sqRecibo) from  Recibo rcb2 where rcb2.sqRecibo = l.recibo.sqRecibo and rcb2.dtCancelado is null) ");
			sql.append("left join LancamentoResumo lr on lr.sqResumo = l.lancamentoResumo.sqResumo ");
			sql.append("and l.recibo.sqRecibo = (select distinct(rb.sqRecibo) from Recibo rb where rb.sqRecibo = l.recibo.sqRecibo and rb.dtCancelado is null) ");
			sql.append("left join Recibo rc on l.recibo.sqRecibo = rc.sqRecibo ");
			sql.append("left join TipoLancamento t on t.sqTipoLancamento =   r.tipoLancamento.sqTipoLancamento ");
			sql.append("left join PlanoConta pc on t.planoConta.sqPlanoConta = pc.sqPlanoConta ");
			sql.append("left join Regiao rg on rc.regiao.sqRegiao = rg.sqRegiao  ");

			// Somente para exibir Lancamentos em Aberto
			if(!flLancamentoPago && flLancamentoEmAberto)
				sql.append("right join RegLancamento reg on reg.sqRegLancamento = r.sqRegLancamento ");

			sql.append("where r.dtCancelado is null and m.sqMinistro = :sqMinistro ");

			// Somente se na pesquisa foi informado o codigo do recibo (NSU)
			if(cdNsu != null)
				sql.append("and rc.cdNsu = " + cdNsu + " ");

			// Somente para exibir Lancamentos em Aberto
			if(!flLancamentoPago && flLancamentoEmAberto) {
				sql.append("and (l.dtPagamento is null ");
				sql.append("or reg.vlLancamento <> ("
						+ "select sum(lan.vlPagamento) "
						+ "from Lancamento lan "
						+ "left join Recibo rec on rec.sqRecibo = lan.recibo.sqRecibo "
						+ "where rec.dtCancelado is null and lan.regLancamento.sqRegLancamento = r.sqRegLancamento)) ");
			}

			// Somente para exibir Lancamentos Pagos
			if(flLancamentoPago && !flLancamentoEmAberto)
				sql.append("and l.dtPagamento is not null ");

			sql.append("order by r.sqRegLancamento, r.dtVencimento");

			List<RegLancamentoCpl> result = manager.createQuery(sql.toString(), RegLancamentoCpl.class)
					.setParameter("sqMinistro", sqMinistro)
					.getResultList();

			return result;

		} catch(NoResultException e) {
			return null;
		}
	}


	// Buscar pela primaryKey completo
	public RegLancamento findByPrimaryKey(Long sq) {	
		try {
			StringBuilder sql = new StringBuilder();

			sql.append("from RegLancamento reg ");
			sql.append("left join fetch reg.tipoLancamento ");
			sql.append("left join fetch reg.ministro ");
			sql.append("where reg.sqRegLancamento = :sq");

			RegLancamento result = manager.createQuery(sql.toString(), RegLancamento.class)
					.setParameter("sq", sq)
					.getSingleResult();

			return result;

		}catch(NoResultException e) {
			return null;
		}
	}


	// Salvar registros em lote
	public void salvarEmLote(Regiao regiao, RegLancamento regLancamentoTemp, List<Long> sqMinistros) {
		try {
			int batchSize = 30;
			int iControle = 0;

			RegLancamento regLancamento;
			Ministro ministro;
			for (Long sqMinistro : sqMinistros) {
				ministro = new Ministro();
				ministro.setSqMinistro(sqMinistro);

				regLancamento = new RegLancamento();
				regLancamento.setCdOrigem(1);
				regLancamento.setMinistro(ministro);
				regLancamento.setTipoLancamento(regLancamentoTemp.getTipoLancamento());
				regLancamento.setDtRegistro(Uteis.DataHoje());
				regLancamento.setDtVencimento(regLancamentoTemp.getDtVencimento());
				regLancamento.setVlLancamento(regLancamentoTemp.getVlLancamento());
				regLancamento.setAuditoriaData(Uteis.DataHoje());
				regLancamento.setAuditoriaUsuario(Uteis.UsuarioLogado().getUsername());

				manager.persist(regLancamento);

				if(++iControle % batchSize == 0) {
					manager.flush();
					manager.clear();
				}

			}

		} catch (Exception e) {
			throw e;
		}

	}

	// Listar registros em lote que estão em aberto.
	public List<RegLancamentoListLote> findLancamentosEmLoteEmAberto(Integer cdOrigem, Long sqDepartamento) {
		try {

			StringBuilder sql = new StringBuilder();

			sql.append("select" + 
					"        rgl.rgl_dt_vencimento as dtVencimento," + 
					"        rgn.rgn_sq_regiao as sqRegiao," + 
					"        rgn.rgn_ds_regiao as dsRegiao," + 
					"        tpl.tpl_sq_tipolancamento as sqTipoLancamento," + 
					"        tpl.tpl_ds_tipolancamento as dsTipoLancamento," + 
					"        case " + 
					"			when qtdeCancelado.qtde is null  then qtdeLancamentos.qtde else (qtdeCancelado.qtde + qtdeLancamentos.qtde)" + 
					"		end as nrQtdeLancamento" + 
					"    from tb_rgl_reglancamento rgl " + 
					"    left join tb_lan_lancamento lan on lan.rgl_sq_reglancamento = rgl.rgl_sq_reglancamento " + 
					"    left join tb_tpl_tipolancamento tpl on rgl.tpl_sq_tipolancamento = tpl.tpl_sq_tipolancamento " + 
					"    left join tb_min_ministro min on rgl.min_sq_ministro = min.min_sq_ministro " + 
					"    left join tb_igr_igreja igr on min.igr_sq_igreja = igr.igr_sq_igreja " + 
					"    left join tb_rgn_regiao rgn on igr.rgn_sq_regiao = rgn.rgn_sq_regiao " + 
					"    left join" + 
					"        (" + 
					"            select" + 
					"                count(*) as qtde," + 
					"                rgl2.rgl_dt_vencimento as dtVencimento," + 
					"                rgl2.tpl_sq_tipolancamento as sqTipoLancamento," + 
					"                igr2.rgn_sq_regiao as sqRegiao," + 
					"                min2.dpt_sq_departamento as sqDepartamento," + 
					"                rgl2.rgl_cd_origem as cdOrigem " + 
					"            from tb_rgl_reglancamento rgl2 " + 
					"            left join tb_lan_lancamento lan2 on lan2.rgl_sq_reglancamento = rgl2.rgl_sq_reglancamento " + 
					"            left join tb_min_ministro min2 on rgl2.min_sq_ministro = min2.min_sq_ministro " + 
					"            left join tb_igr_igreja igr2 on min2.igr_sq_igreja = igr2.igr_sq_igreja " + 
					"            where lan2.rgl_sq_reglancamento is null " + 
					"				and min2.min_dt_excluido is null " +
					"            group by" + 
					"                rgl2.rgl_dt_vencimento," + 
					"                rgl2.tpl_sq_tipolancamento," + 
					"                igr2.rgn_sq_regiao," + 
					"                min2.dpt_sq_departamento," + 
					"                rgl2.rgl_cd_origem " + 
					"        ) as qtdeLancamentos " + 
					"            on (" + 
					"                qtdeLancamentos.dtVencimento = rgl.rgl_dt_vencimento " + 
					"                and qtdeLancamentos.sqTipoLancamento = tpl.tpl_sq_tipolancamento " + 
					"                and qtdeLancamentos.sqRegiao = igr.rgn_sq_regiao " + 
					"                and qtdeLancamentos.sqDepartamento = min.dpt_sq_departamento " + 
					"                and qtdeLancamentos.cdOrigem = rgl.rgl_cd_origem" + 
					"            ) " + 
					"	left join (" + 
					"			select " + 
					"				rgl.rgl_dt_vencimento as dtVencimento" + 
					"				, rgn.rgn_sq_regiao as sqRegiao" + 
					"				, rgl.tpl_sq_tipolancamento as sqTipoLancamento" + 
					"				, min.dpt_sq_departamento as sqDepartamento" + 
					"				, rgl.rgl_cd_origem as cdOrigem" + 
					"				, sum( case " + 
					"					when qtdeLancamento.qtdeLancamento = qtdeLancamentoCancelado.qtdeLancamento then 1 else 0" + 
					"				  end) as qtde" + 
					"			from tb_rgl_reglancamento rgl" + 
					"				left join tb_min_ministro min on rgl.min_sq_ministro = min.min_sq_ministro" + 
					"				left join tb_igr_igreja igr on min.igr_sq_igreja = igr.igr_sq_igreja " + 
					"				left join tb_rgn_regiao rgn on igr.rgn_sq_regiao = rgn.rgn_sq_regiao" + 
					"				left join (" + 
					"					select count(lan.rgl_sq_reglancamento) as qtdeLancamento" + 
					"							, lan.rgl_sq_reglancamento as sqRegLancamento" + 
					"					from tb_lan_lancamento lan" + 
					"					left join tb_rcb_recibo rcb on rcb.rcb_sq_recibo = lan.rcb_sq_recibo and rcb.rcb_dt_cancelado is null" + 
					"					group by lan.rgl_sq_reglancamento" + 
					"				) as qtdeLancamento on qtdeLancamento.sqRegLancamento = rgl.rgl_sq_reglancamento" + 
					"				left join (" + 
					"					select count(lan.rgl_sq_reglancamento) as qtdeLancamento" + 
					"							, lan.rgl_sq_reglancamento as sqRegLancamento" + 
					"					from tb_lan_lancamento lan" + 
					"					right join tb_rcb_recibo rcb on rcb.rcb_sq_recibo = lan.rcb_sq_recibo and rcb.rcb_dt_cancelado is not null" + 
					"					group by lan.rgl_sq_reglancamento" + 
					"				) as qtdeLancamentoCancelado on qtdeLancamentoCancelado.sqRegLancamento = rgl.rgl_sq_reglancamento" + 
					"			where rgl.rgl_dt_cancelado is null " + 
					"				and qtdeLancamento.sqRegLancamento is not null" + 
					"				and min.min_dt_excluido is null" + 
					"			group by rgl.rgl_dt_vencimento " + 
					"				, rgn.rgn_sq_regiao" + 
					"				, rgl.tpl_sq_tipolancamento " + 
					"				, min.dpt_sq_departamento" + 
					"				, rgl.rgl_cd_origem" + 
					"            ) as qtdeCancelado on (" + 
					"						 qtdeCancelado.dtVencimento = rgl.rgl_dt_vencimento " + 
					"						and qtdeCancelado.sqTipoLancamento = tpl.tpl_sq_tipolancamento " + 
					"						and qtdeCancelado.sqRegiao = igr.rgn_sq_regiao " + 
					"						and qtdeCancelado.sqDepartamento = min.dpt_sq_departamento " + 
					"						and qtdeCancelado.cdOrigem = rgl.rgl_cd_origem" + 
					"					)" + 
					"    where" + 
					"        lan.rgl_sq_reglancamento is null " + 
					"        and min.min_dt_excluido is null" + 
					"        and rgl.rgl_cd_origem = :cdOrigem " + 
					"        and min.dpt_sq_departamento = :sqDepartamento " + 
					"    group by" + 
					"        igr.rgn_sq_regiao," + 
					"        rgl.rgl_dt_vencimento," + 
					"        tpl.tpl_sq_tipolancamento " + 
					"    order by" + 
					"        dtVencimento," + 
					"        dsTipoLancamento," + 
					"        dsRegiao ");

			@SuppressWarnings("unchecked")
			List<Object[]> resultObject = manager.createNativeQuery(sql.toString())
			.setParameter("cdOrigem", cdOrigem)
			.setParameter("sqDepartamento", sqDepartamento)
			.getResultList();

			// Criar lista vazia para armazenar retorno da consulta
			List<RegLancamentoListLote> result = new ArrayList<>();

			// Popular a lista que sera retornada
			Long sqControle = 0L;
			for (Object ob[] : resultObject) {
				RegLancamentoListLote reg = new RegLancamentoListLote();

				reg.setSqControle(++sqControle);
				reg.setDtVencimento(LocalDate.parse(ob[0].toString()));
				reg.setSqRegiao(Long.parseLong(ob[1].toString()));
				reg.setDsRegiao(ob[2].toString());
				reg.setSqTipoLancamento(Long.parseLong(ob[3].toString()));
				reg.setDsTipoLancamento(ob[4].toString());
				reg.setNrQtdeLancamento(Long.parseLong(ob[5].toString()));

				result.add(reg);
			}

			return result;

		} catch (NoResultException e) {
			return null;
		}
	}

	// Buscar lista com sqRegLancamento para exclusão em lote
	public List<Long> findLancamentosEmAbertoParaExclusao(Integer cdOrigem, LocalDate dtVencimento, Long sqTipoLancamento, Long sqRegiao, Long sqDepartamento) {
		try {

			StringBuilder sql = new StringBuilder();

			sql.append("select rgl.sqRegLancamento from RegLancamento rgl ");
			sql.append("left join Lancamento lan on lan.regLancamento.sqRegLancamento = rgl.sqRegLancamento ");	
			sql.append("left join TipoLancamento tpl on tpl.sqTipoLancamento = rgl.tipoLancamento.sqTipoLancamento ");	
			sql.append("left join Ministro mnt on mnt.sqMinistro = rgl.ministro.sqMinistro ");	
			sql.append("left join Igreja igr on igr.sqIgreja = mnt.igreja.sqIgreja ");			
			sql.append("left join Regiao rgn on rgn.sqRegiao = igr.regiao.sqRegiao ");

			sql.append("where lan.regLancamento.sqRegLancamento is null and mnt.dtExcluido is null and rgl.cdOrigem = :cdOrigem and rgl.dtVencimento = :dtVencimento and rgl.tipoLancamento.sqTipoLancamento = :sqTipoLancamento and rgn.sqRegiao = :sqRegiao and mnt.departamento.sqDepartamento = :sqDepartamento");

			List<Long> result = manager.createQuery(sql.toString(), Long.class)
					.setParameter("cdOrigem", cdOrigem)
					.setParameter("dtVencimento", dtVencimento)
					.setParameter("sqTipoLancamento", sqTipoLancamento)
					.setParameter("sqRegiao", sqRegiao)
					.setParameter("sqDepartamento", sqDepartamento)
					.getResultList();

			return result;
		} catch (NoResultException e) {
			return null;
		}
	}

	// Buscar lista com sqRegLancamento para cancelamento em lote
	public List<Long> findLancamentosEmAbertoParaCancelar(Integer cdOrigem, LocalDate dtVencimento, Long sqTipoLancamento, Long sqRegiao, Long sqDepartamento) {
		try {

			StringBuilder sql = new StringBuilder();

			sql.append("select " + 
					"	case" + 
					"		when qtdeLancamento.qtdeLancamento = qtdeLancamentoCancelado.qtdeLancamento" + 
					"			then rgl.rgl_sq_reglancamento" + 
					"        end as sqRegLancamento" + 
					" from tb_rgl_reglancamento rgl" + 
					"	left join tb_min_ministro min on rgl.min_sq_ministro = min.min_sq_ministro" + 
					"	left join tb_igr_igreja igr on min.igr_sq_igreja = igr.igr_sq_igreja " + 
					"	left join tb_rgn_regiao rgn on igr.rgn_sq_regiao = rgn.rgn_sq_regiao" + 
					"	left join (" + 
					"		select count(lan.rgl_sq_reglancamento) as qtdeLancamento" + 
					"				, lan.rgl_sq_reglancamento as sqRegLancamento" + 
					"		from tb_lan_lancamento lan" + 
					"			left join tb_rcb_recibo rcb on rcb.rcb_sq_recibo = lan.rcb_sq_recibo and rcb.rcb_dt_cancelado is null" + 
					"		group by lan.rgl_sq_reglancamento" + 
					"		) as qtdeLancamento on qtdeLancamento.sqRegLancamento = rgl.rgl_sq_reglancamento" + 
					"	left join (" + 
					"		select count(lan.rgl_sq_reglancamento) as qtdeLancamento" + 
					"					, lan.rgl_sq_reglancamento as sqRegLancamento" + 
					"		from tb_lan_lancamento lan" + 
					"			right join tb_rcb_recibo rcb on rcb.rcb_sq_recibo = lan.rcb_sq_recibo and rcb.rcb_dt_cancelado is not null" + 
					"			group by lan.rgl_sq_reglancamento" + 
					"		) as qtdeLancamentoCancelado on qtdeLancamentoCancelado.sqRegLancamento = rgl.rgl_sq_reglancamento" + 
					" where rgl.rgl_dt_cancelado is null " + 
					"	and qtdeLancamento.sqRegLancamento is not null" + 
					"	and min.min_dt_excluido is null" + 
					"   and (qtdeLancamento.qtdeLancamento - qtdeLancamentoCancelado.qtdeLancamento) = 0" + 
					"	and rgl.rgl_dt_vencimento = :dtVencimento" + 
					"	and rgl.tpl_sq_tipolancamento = :sqTipoLancamento" + 
					"	and igr.rgn_sq_regiao = :sqRegiao" + 
					"	and min.dpt_sq_departamento = :sqDepartamento " + 
					"	and rgl.rgl_cd_origem = :cdOrigem");

			@SuppressWarnings("unchecked")
			List<Object[]> resultObject = manager.createNativeQuery(sql.toString())
			.setParameter("cdOrigem", cdOrigem)
			.setParameter("sqDepartamento", sqDepartamento)
			.setParameter("dtVencimento", dtVencimento)
			.setParameter("sqTipoLancamento", sqTipoLancamento)
			.setParameter("sqRegiao", sqRegiao)
			.getResultList();

			// Criar lista vazia para armazenar retorno da consulta
			List<Long> result = new ArrayList<>();

			// Converter de Object para Log
			for (Object ob : resultObject) {
				result.add(Long.parseLong(ob.toString()));
			}

			return result;

		} catch (NoResultException e) {
			return null;
		}
	}


	public int salvarDeleteEmLote(List<Long> sqRegLancamentoLista) {
		try {
			// Excluir boletos registrados
			StringBuilder sqlBoleto = new StringBuilder();
			sqlBoleto.append("delete from Boleto bol where bol.regLancamento.sqRegLancamento in( :sqRegLancamentoLista)");
			manager.createQuery(sqlBoleto.toString())
						.setParameter("sqRegLancamentoLista", sqRegLancamentoLista)
						.executeUpdate();
			
			// Excluir Registros de Lançamento
			StringBuilder sql = new StringBuilder();
			sql.append("delete from RegLancamento rgl where rgl.sqRegLancamento in( :sqRegLancamentoLista)");

			int result = manager.createQuery(sql.toString())
						.setParameter("sqRegLancamentoLista", sqRegLancamentoLista)
						.executeUpdate();
			
			// Buscar registros de auditoria para acertar nome do usuário responsável pela exclusão
			String dsTabela = Uteis.obterNomeTabela(RegLancamento.class.getSimpleName());
			List<Long> sqAuditoriaTemp = new ArrayList<Long>();
			Long sqTmp;
			for (Long sqReg : sqRegLancamentoLista) {
				sqTmp = manager.createQuery("select max(sqAuditoria) from Auditoria where sqTabela = :sq and dsTabela like :dsTabela" , Long.class)
						.setParameter("sq", sqReg)
						.setParameter("dsTabela", dsTabela)
						.getSingleResult();
				
				sqAuditoriaTemp.add(sqTmp);
			}
			
			StringBuilder sqlAuditoria = new StringBuilder();
			sqlAuditoria.append("update from Auditoria aud set aud.dsUsuario = :dsUsuario where aud.sqAuditoria in(:sqAuditoria)");
			manager.createQuery(sqlAuditoria.toString())
					.setParameter("dsUsuario", Uteis.UsuarioLogado().getUsuario().getDsLogin())
					.setParameter("sqAuditoria", sqAuditoriaTemp)
					.executeUpdate();

			return result;
		} catch(PersistenceException e){
			throw e;
		}
	}


	public int salvarUpdateEmLote(List<Long> sqRegLancamentoListaCancelados) {
		try {
			StringBuilder sql = new StringBuilder();

			sql.append("update from RegLancamento rgl set rgl.dtCancelado = :dataHoje where rgl.sqRegLancamento in( :sqRegLancamentoListaCancelados)");

			int result = manager.createQuery(sql.toString())
						.setParameter("dataHoje", Uteis.DataHoje())
						.setParameter("sqRegLancamentoListaCancelados", sqRegLancamentoListaCancelados)
						.executeUpdate();

			return result;
		} catch(Exception e){
			throw e;
		}

	}

}
