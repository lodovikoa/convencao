package br.com.convencao.dao;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.From;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;

import br.com.convencao.bean.cadastro.MinistroFiltro;
import br.com.convencao.bo.NegocioException;
import br.com.convencao.model.Departamento;
import br.com.convencao.model.Ministro;
import br.com.convencao.model.Regiao;
import br.com.convencao.model.to.MinistroPorAnoTO;
import br.com.convencao.model.to.MinistroPorRegiaoTO;
import br.com.convencao.model.to.MinistroRecebimentoCpl;
import br.com.convencao.util.Uteis;

public class MinistroDAO extends GenericoDAO<Ministro> {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;    

	// Buscar ministro pela primary Key com Departament
	public Ministro findByPrimaryKey(Long sq){
		try {
			Ministro result = manager.createQuery("from Ministro c "
					+ "left join fetch c.departamento "
					+ "left join fetch c.cargo "
					+ "left join fetch c.conjuge "
					+ "left join fetch c.estadoCivel "
					+ "left join fetch c.escolaridade "
					+ "left join fetch c.profissao "
					+ "left join fetch c.estado "
					+ "left join fetch c.igreja "
					+ "where c.sqMinistro = :sq", Ministro.class)
					.setParameter("sq",sq)
					.getSingleResult();

			return result;
		} catch (NoResultException e) {
			return null;
		}
	}

	public Ministro findByPrimaryKeyAtivo(Long sqMinistro) {
		try {
			
			Ministro result = manager.createQuery("select m from Ministro m where m.dtExcluido is null and m.sqMinistro = :sqMinistro", Ministro.class)
					.setParameter("sqMinistro", sqMinistro)
					.getSingleResult();
			
			return result;
		}catch(NoResultException e){
			return null;
		}
		
	}

	// Buscar ministros presidentes de igrejas de uma região
	public List<Ministro> findPresidenteByRegiao(Long sqRegiao){
		try{

			List<Ministro> result = manager.createQuery("select  m from Ministro m, Igreja i where m.sqMinistro = i.ministro.sqMinistro and m.dtExcluido is null and i.regiao.sqRegiao = :sqRegiao  order by m.nmNome", Ministro.class)
					.setParameter("sqRegiao", sqRegiao)
					.getResultList();

			return result;
		}catch(NoResultException e){
			return null;
		}

	}


	// Buscar ministros presidentes de igrejas de todas as regiões
	public List<Ministro> findPresidentes(){
		try{

			List<Ministro> result = manager.createQuery("select  m from Ministro m, Igreja i where m.sqMinistro = i.ministro.sqMinistro and m.dtExcluido is null  order by m.nmNome", Ministro.class)
					.getResultList();

			return result;
		}catch(NoResultException e){
			return null;
		}
	}

	// Buscar todos os ministros de todos os departamentos de UMA IGREJA.
	public List<Ministro> findMinistrosByIgreja(Long sqIgreja){
		try{

			List<Ministro> result = manager.createQuery("from Ministro m left join fetch m.departamento left join fetch m.cargo where m.igreja.sqIgreja = :sqIgreja order by m.departamento.dsReduzido, m.nmNome", Ministro.class)
					.setParameter("sqIgreja", sqIgreja)
					.getResultList();

			return result;
		}catch(NoResultException e){
			return null;
		}
	}

	// Buscar todos os ministros de um dos departamentos de UMA IGREJA.
	public List<Ministro> findMinistrosByIgrejaByDepartamento(Long sqIgreja, Long sqDepartamento){
		try{

			List<Ministro> result = manager.createQuery("from Ministro m left join fetch m.departamento left join fetch m.cargo where m.igreja.sqIgreja = :sqIgreja and m.departamento.sqDepartamento = :sqDepartamento order by m.departamento.dsReduzido, m.nmNome", Ministro.class)
					.setParameter("sqIgreja", sqIgreja)
					.setParameter("sqDepartamento", sqDepartamento)
					.getResultList();

			return result;
		}catch(NoResultException e){
			return null;
		}
	}

	// Buscar esposas de ministros
	public List<Ministro> findEsposasMinistro(Long sqDepartamento){
		try{

			List<Ministro> result = manager.createQuery("from Ministro m where m.departamento.sqDepartamento = :sqDepartamento order by m.nmNome", Ministro.class)
					.setParameter("sqDepartamento", sqDepartamento)
					.getResultList();

			return result;
		}catch(NoResultException e){
			return null;
		}
	}

	//Verificar se Conjuge já está registrado como esposa de outro ministro
	public Ministro validarConjuge(Long sqMinistro, Long sqConjuge){	
		try{
			Ministro result = manager.createQuery("from Ministro m where m.conjuge.sqMinistro = :sqConjuge and m.sqMinistro <> :sqMinistro", Ministro.class)
					.setParameter("sqConjuge", sqConjuge)
					.setParameter("sqMinistro", sqMinistro == null?0:sqMinistro)
					.getSingleResult();

			return result;
		}catch(NoResultException e){
			return null;
		}
	}

	// Verificar CPF de ministro já usado para outro ministro
	public Ministro validarCpf(Long sqMinistro, String dsCpf){
		try{
			Ministro result = manager.createQuery("from Ministro m where m.dsCpf = :dsCpf and m.sqMinistro <> :sqMinistro", Ministro.class)
					.setParameter("dsCpf", dsCpf)
					.setParameter("sqMinistro", sqMinistro == null?0:sqMinistro)
					.getSingleResult();

			return result;
		}catch(NoResultException e){
			return null;
		}
	}


	public List<Ministro> findByParametros(MinistroFiltro filtro) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Ministro> criteriaQuery = builder.createQuery(Ministro.class);
		List<Predicate> predicates = new ArrayList<>();

		Root<Ministro> ministroRoot = criteriaQuery.from(Ministro.class);

		// Se necessário os joins serão inseridos aqui.
		From<?, ?> igrejaJoin = (From<?, ?>) ministroRoot.fetch("igreja", JoinType.LEFT);
		igrejaJoin.fetch("regiao", JoinType.LEFT);
		ministroRoot.fetch("departamento",JoinType.LEFT);
		ministroRoot.fetch("cargo",JoinType.LEFT);
		ministroRoot.fetch("estado", JoinType.LEFT);

		// Regiao é obrigatória. Delimitar pela região selecionada
		if(filtro.getRegiaoItensFiltro() != null && filtro.getRegiaoItensFiltro().getSqRegiao() != null && filtro.getRegiaoItensFiltro().getSqRegiao() > 0){
			predicates.add(builder.equal(igrejaJoin.get("regiao"), filtro.getRegiaoItensFiltro().getSqRegiao()));
		}else if(filtro.getRegiaoItensFiltro() == null || filtro.getRegiaoItensFiltro().getSqRegiao() == null || filtro.getRegiaoItensFiltro().getSqRegiao() != -1) {
			throw new NegocioException("Região: preenchimento obrigatório!");	
		}

		// Delimitar por um departamento
		if(filtro.getDepartamentoFiltro() != null && filtro.getDepartamentoFiltro().getSqDepartamento() != null && filtro.getDepartamentoFiltro().getSqDepartamento() > 0){
			predicates.add(builder.equal(ministroRoot.get("departamento"),  filtro.getDepartamentoFiltro().getSqDepartamento()));
		}

		// Delimitar pelo código do minisgtro na convenção
		if(filtro.getCdCodigoFiltro() != null && filtro.getCdCodigoFiltro() > 0){
			predicates.add(builder.equal(ministroRoot.get("cdCodigo"), filtro.getCdCodigoFiltro()));
		}

		// Delimitar pelo nome do ministro
		if(StringUtils.isNotBlank(filtro.getNmMinistro())){
			predicates.add(builder.like(ministroRoot.get("nmNome"), "%" + filtro.getNmMinistro() + "%"));
		}
		
		// Delimitar pelo CPF do ministro
		if(StringUtils.isNotBlank(filtro.getDsCpf())) {
			predicates.add(builder.like(ministroRoot.get("dsCpf"), Uteis.OnlyNumbers(filtro.getDsCpf())));
		}

		// Delimitar pela situação do Ministro: Ativo = 1, Inativo = 2, Todos = 3
		if(filtro.getCdSituacaoFiltro() != null){
			if(filtro.getCdSituacaoFiltro() == 1){
				predicates.add(builder.isNull(ministroRoot.get("dtExcluido")));
			}else if(filtro.getCdSituacaoFiltro() == 2){
				predicates.add(builder.isNotNull(ministroRoot.get("dtExcluido")));
			}
		}

		// Delimitar por ministros jubilados
		if(filtro.getFlJubiladoFiltro()){
			predicates.add(builder.isTrue(ministroRoot.get("flJubilado")));
		}

		// Delimitar pela Igreja
		if(filtro.getIgrejaFiltro() != null && filtro.getIgrejaFiltro().getSqIgreja() != null){
			predicates.add(builder.equal(ministroRoot.get("igreja"),  filtro.getIgrejaFiltro().getSqIgreja()));
		}

		// Delimitar pelas datas de ingresso: maior ou igual 
		if(filtro.getDtInicio1Filtro() != null){
			predicates.add(builder.greaterThanOrEqualTo(ministroRoot.get("dtIngresso"), filtro.getDtInicio1Filtro()));
		}

		// Delimitar pelas datas de ingresso: menor ou igual 
		if(filtro.getDtFim1Filtro() != null){
			predicates.add(builder.lessThanOrEqualTo(ministroRoot.get("dtIngresso"), filtro.getDtFim1Filtro()));
		}

		// Delimitar pelo cargo do ministro
		if(filtro.getCargoFiltro() != null && filtro.getCargoFiltro().getSqCargo() != null){
			predicates.add(builder.equal(ministroRoot.get("cargo"),  filtro.getCargoFiltro().getSqCargo()));
		}

		// Delimitar pela cidade do ministro
		if(StringUtils.isNotBlank(filtro.getDsCidadeFiltro())){
			predicates.add(builder.like(ministroRoot.get("dsCidade"), "%" + filtro.getDsCidadeFiltro() + "%"));
		}


		criteriaQuery.select(ministroRoot);
		criteriaQuery.orderBy(builder.asc(ministroRoot.get("nmNome")));
		
		criteriaQuery.where(predicates.toArray(new Predicate[0]));

		TypedQuery<Ministro> query = manager.createQuery(criteriaQuery);

		return query.getResultList();
	}


	// Retornar último codigo registrado para um determinado departamento
	public Long ultimoCodigoRegistrado(Departamento dpto) {
		try{

			Long result = null;
			if(dpto.getSqDepartamento() == 1) {
				result = manager.createQuery("select max(m.cdCodigo) FROM Ministro m where m.departamento = :dpto", Long.class)
							.setParameter("dpto", dpto)
							.getSingleResult();
			} else {
				result = manager.createQuery("select max(m.cdCodigo) FROM Ministro m where m.departamento.sqDepartamento <> 1", Long.class)
						.getSingleResult();
			}

			return result;
		}catch(NoResultException e){
			return null;
		}
	}
	
	// Retornar último codigo registrado dos departamentos listados
	public Long ultimoCodigoRegistrado(List<Long> sqDepartamentos) {
		try {
			Long result = null;
			result = manager.createQuery("select max(m.cdCodigo) from Ministro m where m.departamento.sqDepartamento in (:sqDepartamentos)", Long.class)
					.setParameter("sqDepartamentos", sqDepartamentos)
					.getSingleResult();
			
			return result;
			
		}catch(NoResultException e){
			return null;
		}
	}


	public boolean findByPorCodigoDepartamento(Long cdCodigo, Departamento departamento) {
		try {
			Ministro result = manager.createQuery("from Ministro m where m.cdCodigo = :cdCodigo and m.departamento = :departamento", Ministro.class)
					.setParameter("cdCodigo", cdCodigo)
					.setParameter("departamento", departamento)
					.getSingleResult();

			return result != null;

		}catch(NoResultException e){
			return false;
		}
	}

	public List<MinistroRecebimentoCpl> findMinistrosForRecebimento(MinistroFiltro filtro) {
		try {
			StringBuilder sql = new StringBuilder();
			boolean flWhere = false;

			sql.append("select distinct new br.com.convencao.model.to.MinistroRecebimentoCpl");
			sql.append("(m.sqMinistro, m.cdCodigo, m.nmNome, r.dsRegiao, d.dsReduzido, m.dtIngresso, c.dsCargo, i.dsIgreja, m.dsCidade, m.estado.dsUf, m.dtExcluido, m.flJubilado) ");
			sql.append("from Lancamento l ");
			sql.append("left join l.regLancamento g ");
			sql.append("right join g.ministro m ");
			sql.append("left join m.estado e");
			sql.append("left join m.igreja i ");
			sql.append("left join m.departamento d ");
			sql.append("left join m.cargo c ");
			sql.append("left join i.regiao r ");

			if(filtro.getRegiaoItensFiltro() != null && filtro.getRegiaoItensFiltro().getSqRegiao() != null && filtro.getRegiaoItensFiltro().getSqRegiao() > 0) {
				if(!flWhere) { 
					sql.append("where ");
					flWhere = true;
				}else sql.append(" and ");

				sql.append("i.regiao.sqRegiao = ");
				sql.append(filtro.getRegiaoItensFiltro().getSqRegiao());
			}

			if(filtro.getIgrejaFiltro() != null && filtro.getIgrejaFiltro().getSqIgreja() != null && filtro.getIgrejaFiltro().getSqIgreja() > 0) {
				if(!flWhere) { 
					sql.append("where ");
					flWhere = true;
				}else sql.append(" and ");

				sql.append("i.sqIgreja = ");
				sql.append(filtro.getIgrejaFiltro().getSqIgreja());
			}

			if(filtro.getDepartamentoFiltro() != null && filtro.getDepartamentoFiltro().getSqDepartamento() != null && filtro.getDepartamentoFiltro().getSqDepartamento() > 0) {
				if(!flWhere) { 
					sql.append("where ");
					flWhere = true;
				}else sql.append(" and ");

				sql.append("d.sqDepartamento = ");
				sql.append(filtro.getDepartamentoFiltro().getSqDepartamento());
			}

			if(filtro.getCdCodigoFiltro() != null && filtro.getCdCodigoFiltro() > 0) {
				if(!flWhere) { 
					sql.append("where ");
					flWhere = true;
				}else sql.append(" and ");

				sql.append("m.cdCodigo = ");
				sql.append(filtro.getCdCodigoFiltro());
			}

			if(StringUtils.isNoneBlank(filtro.getNmMinistro())) {
				if(!flWhere) { 
					sql.append("where ");
					flWhere = true;
				}else sql.append(" and ");

				sql.append("m.nmNome like ");
				sql.append("'%" + filtro.getNmMinistro() + "%'");
			}

			if(filtro.getCargoFiltro() != null && filtro.getCargoFiltro().getSqCargo() != null && filtro.getCargoFiltro().getSqCargo() > 0) {
				if(!flWhere) { 
					sql.append("where ");
					flWhere = true;
				}else sql.append(" and ");

				sql.append("c.sqCargo = ");
				sql.append(filtro.getCargoFiltro().getSqCargo());
			}

			if(filtro.getCdSituacaoFiltro() == 1) {
				if(!flWhere) { 
					sql.append("where ");
					flWhere = true;
				}else sql.append(" and ");

				sql.append("m.dtExcluido is null ");
			} else if(filtro.getCdSituacaoFiltro() == 2) {
				if(!flWhere) { 
					sql.append("where ");
					flWhere = true;
				}else sql.append(" and ");

				sql.append("m.dtExcluido is not null ");
			}

			if(filtro.getFlJubiladoFiltro()) {
				if(!flWhere) { 
					sql.append("where ");
					flWhere = true;
				}else sql.append(" and ");

				sql.append("m.flJubilado is true ");
			}

			if(StringUtils.isNotBlank(filtro.getDsCidadeFiltro())) {
				if(!flWhere) { 
					sql.append("where ");
					flWhere = true;
				}else sql.append(" and ");

				sql.append("m.dsCidade like '%");
				sql.append(filtro.getDsCidadeFiltro());
				sql.append("%' ");
			}

			if(filtro.getEstadoFiltro() != null && filtro.getEstadoFiltro().getSqEstado() != null && filtro.getEstadoFiltro().getSqEstado() > 0) {
				if(!flWhere) { 
					sql.append("where ");
					flWhere = true;
				}else sql.append(" and ");

				sql.append("m.estado.sqEstado = ");
				sql.append(filtro.getEstadoFiltro().getSqEstado());
			}

			if(filtro.getCdReciboFiltro() != null && filtro.getCdReciboFiltro() > 0) {
				if(!flWhere) { 
					sql.append("where ");
					flWhere = true;
				}else sql.append(" and ");

				sql.append("l.recibo.cdNsu = ");
				sql.append(filtro.getCdReciboFiltro());
			}

			if(filtro.getDtInicio1Filtro() != null && filtro.getDtFim1Filtro() != null) {
				if(!flWhere) { 
					sql.append("where ");
					flWhere = true;
				}else sql.append(" and ");

				sql.append("l.dtPagamento between '");
				sql.append(filtro.getDtInicio1Filtro());
				sql.append("' and '");
				sql.append(filtro.getDtFim1Filtro());
				sql.append("'");
			} else if(filtro.getDtInicio1Filtro() != null) {
				if(!flWhere) { 
					sql.append("where ");
					flWhere = true;
				}else sql.append(" and ");

				sql.append("l.dtPagamento >= '");
				sql.append(filtro.getDtInicio1Filtro());
				sql.append("'");
			} else if(filtro.getDtFim1Filtro() != null) {
				if(!flWhere) { 
					sql.append("where ");
					flWhere = true;
				}else sql.append(" and ");

				sql.append("l.dtPagamento <= '");
				sql.append(filtro.getDtFim1Filtro());
				sql.append("'");
			}

			if(filtro.getDtInicio2Filtro() != null && filtro.getDtFim2Filtro() != null) {
				if(!flWhere) { 
					sql.append("where ");
					flWhere = true;
				}else sql.append(" and ");

				sql.append("g.dtVencimento between '");
				sql.append(filtro.getDtInicio2Filtro());
				sql.append("' and '");
				sql.append(filtro.getDtFim2Filtro());
				sql.append("'");
			} else if(filtro.getDtInicio2Filtro() != null) {
				if(!flWhere) { 
					sql.append("where ");
					flWhere = true;
				}else sql.append(" and ");

				sql.append("g.dtVencimento >= '");
				sql.append(filtro.getDtInicio2Filtro());
				sql.append("'");
			} else if(filtro.getDtFim2Filtro() != null) {
				if(!flWhere) { 
					sql.append("where ");
					flWhere = true;
				}else sql.append(" and ");

				sql.append("g.dtVencimento <= '");
				sql.append(filtro.getDtFim2Filtro());
				sql.append("'");
			}

			List<MinistroRecebimentoCpl> result = manager.createQuery(sql.toString(), MinistroRecebimentoCpl.class)
					.getResultList();

			return result;

		} catch(NoResultException e){
			return null;
		}

	}

	public MinistroRecebimentoCpl findMinistrosRecebimentoForPrimaryKey(Long sqMinistro) {
		try {
			StringBuilder sql = new StringBuilder();

			sql.append("select new br.com.convencao.model.to.MinistroRecebimentoCpl");
			sql.append("(m.sqMinistro, m.cdCodigo, m.nmNome, r.dsRegiao, d.dsReduzido, m.dtIngresso, c.dsCargo, i.dsIgreja, i.dsCidade, e.dsUf, m.dtExcluido, m.flJubilado, m.dsCpf, m2.nmNome, m.dsFoto) ");
			sql.append("from Ministro m ");
			sql.append("left join m.departamento d ");
			sql.append("left join m.cargo c ");
			sql.append("left join m.igreja i ");
			sql.append("left join i.regiao r ");
			sql.append("left join i.ministro m2 ");
			sql.append("left join i.estado e ");
			sql.append("where m.sqMinistro = ");
			sql.append(sqMinistro);
			
			
			
			MinistroRecebimentoCpl result = manager.createQuery(sql.toString(), MinistroRecebimentoCpl.class)
					.getSingleResult();
			
			return result;

		} catch(NoResultException e){
			return null;
		}

	}
	
	public List<Long> findMinistrosAtivosPorRegiao(Regiao regiao, Departamento departamento) {
		try {
			StringBuilder sql =  new StringBuilder();
			
			sql.append("select ministro.sqMinistro from Ministro ministro ");
			sql.append("where ministro.flJubilado = false and ministro.dtExcluido is null and ministro.igreja.regiao = :regiao and ministro.departamento = :departamento ");
			
			
			List<Long> result = manager.createQuery(sql.toString(), Long.class)
					.setParameter("regiao", regiao)
					.setParameter("departamento", departamento)
					.getResultList();
			
			return result;
			
		} catch(NoResultException e){
			return null;
		}
	}


	public Long findCodigoDepartamento(Long sqMinistro) {
		try {
			StringBuilder sql = new StringBuilder();
			
			sql.append("select d.sqDepartamento from Ministro m ")
				.append("left join m.departamento d ")
				.append("where m.sqMinistro = :sqMinistro ");
			
			Long result = manager.createQuery(sql.toString(), Long.class)
					.setParameter("sqMinistro", sqMinistro)
					.getSingleResult();
			
			return result;
			
		} catch(NoResultException e) {
			return null;
		}
		
	}


	public List<MinistroPorRegiaoTO> findMinistrosPorRegiao() {
		try {
			StringBuilder sql = new StringBuilder();
			
			sql.append("select new br.com.convencao.model.to.MinistroPorRegiaoTO")
				.append("(r.dsRegiao, count(*)) ")
				.append("from Ministro m ")
				.append("left join Igreja i on i.sqIgreja = m.igreja.sqIgreja ")
				.append("left join Regiao r on r.sqRegiao = i.regiao.sqRegiao ")
				.append("where m.departamento.sqDepartamento = 1 and m.dtExcluido is null ")
				.append("group by r.dsRegiao ")
				.append("order by r.dsRegiao");
			
			List<MinistroPorRegiaoTO> result = manager.createQuery(sql.toString(), MinistroPorRegiaoTO.class)
					.getResultList();
			
			return result;
			
		} catch(NoResultException e) {
			return null;
		}
	}


	public List<MinistroPorAnoTO> findMinistrosPOrAnoNovos(int anoInicio, int anoAtual) {
		try {
			StringBuilder sql = new StringBuilder();
			
			sql.append("select new br.com.convencao.model.to.MinistroPorAnoTO")
				.append("(extract(year from m.dtIngresso), count(*)) ")
				.append("from Ministro m ")
				.append("where m.departamento.sqDepartamento = 1 and m.dtExcluido is null and extract(year from m.dtIngresso) between :anoInicio and :anoAtual ")
				.append("group by extract(year from m.dtIngresso) ")
				.append("order by extract(year from m.dtIngresso)");
			
			List<MinistroPorAnoTO> result = manager.createQuery(sql.toString(), MinistroPorAnoTO.class)
					.setParameter("anoInicio", anoInicio)
					.setParameter("anoAtual", anoAtual)
					.getResultList();
			
			return result;
			
//			-- Novos ministros por ano
//			select extract(year from m.min_dt_ingresso) as ano, count(*) from tb_min_ministro m
//			where m.dpt_sq_departamento = 1
//				and m.min_dt_excluido is null
//				and extract(year from m.min_dt_ingresso) between '2015' and '2019'
//			group by extract(year from m.min_dt_ingresso)
//			order by ano;
			
		} catch(NoResultException e) {
			return null;
		}
		
	}


	public List<MinistroPorAnoTO> findMinistrosPOrAnoExcluidos(int anoInicio, int anoAtual) {
		try {
			StringBuilder sql = new StringBuilder();
			
			sql.append("select new br.com.convencao.model.to.MinistroPorAnoTO")
				.append("(extract(year from m.dtExcluido), count(*)) ")
				.append("from Ministro m ")
				.append("where m.departamento.sqDepartamento = 1 and m.dtExcluido is not null and extract(year from m.dtExcluido) between :anoInicio and :anoAtual ")
				.append("group by extract(year from m.dtExcluido) ")
				.append("order by extract(year from m.dtExcluido)");
			
			List<MinistroPorAnoTO> result = manager.createQuery(sql.toString(), MinistroPorAnoTO.class)
					.setParameter("anoInicio", anoInicio)
					.setParameter("anoAtual", anoAtual)
					.getResultList();
			
			return result;
			
//			-- Ministros que saíram por ano
//			select extract(year from m.min_dt_excluido) as ano, count(*) from tb_min_ministro m
//			where m.dpt_sq_departamento = 1
//				and m.min_dt_excluido is not null
//			    and extract(year from m.min_dt_excluido) between '2015' and '2019'
//			group by extract(year from m.min_dt_excluido)
//			order by ano;
			
		} catch(NoResultException e) {
			return null;
		}
		
	}

}
