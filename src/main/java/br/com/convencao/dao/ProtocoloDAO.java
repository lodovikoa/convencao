package br.com.convencao.dao;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import org.apache.commons.lang3.StringUtils;

import br.com.convencao.bean.cadastro.MinistroFiltro;
import br.com.convencao.model.Protocolo;
import br.com.convencao.model.to.MinistroCandidatoListTO;

public class ProtocoloDAO extends GenericoDAO<Protocolo> {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private EntityManager manager; 
	
	public List<MinistroCandidatoListTO> findByParametros(MinistroFiltro filtro) {
		try {
			
			StringBuilder sql = new StringBuilder();
			boolean flWhere = false;
			
			sql.append("select new br.com.convencao.model.to.MinistroCandidatoListTO")
					.append("(prt.sqProtocolo, prt.cdProtocolo, prs.sqProtocoloStatus, prs.dsStatus, m.cdCodigo, m.nmNome, rgn.dsRegiao, igr.dsIgreja)")
					.append("from Protocolo prt ")
					.append("left join prt.ministro m ")
					.append("left join prt.protocoloStatus prs ")
					.append("left join m.igreja igr ")
					.append("left join igr.regiao rgn ");
			
			if(filtro.getRegiaoItensFiltro() != null && filtro.getRegiaoItensFiltro().getSqRegiao() != null & filtro.getRegiaoItensFiltro().getSqRegiao() > 0) {
				if(!flWhere) {
					sql.append("where ");
					flWhere = true;
				} else sql.append(" and ");
				
				sql.append("igr.regiao.sqRegiao = ")
					.append(filtro.getRegiaoItensFiltro().getSqRegiao());
			}
			
			if(filtro.getIgrejaFiltro() != null && filtro.getIgrejaFiltro().getSqIgreja() != null && filtro.getIgrejaFiltro().getSqIgreja() > 0) {
				if(!flWhere) { 
					sql.append("where ");
					flWhere = true;
				}else sql.append(" and ");

				sql.append("igr.sqIgreja = ");
				sql.append(filtro.getIgrejaFiltro().getSqIgreja());
			}
			
			if(filtro.getProtocoloStatusFiltro() != null && filtro.getProtocoloStatusFiltro().getSqProtocoloStatus() != null && filtro.getProtocoloStatusFiltro().getSqProtocoloStatus() > 0) {
				if(!flWhere) { 
					sql.append("where ");
					flWhere = true;
				}else sql.append(" and ");

				sql.append("prt.protocoloStatus.sqProtocoloStatus = ");
				sql.append(filtro.getProtocoloStatusFiltro().getSqProtocoloStatus());
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
			
			if(StringUtils.isNotBlank(filtro.getDsCpf())) {
				if(!flWhere) { 
					sql.append("where ");
					flWhere = true;
				}else sql.append(" and ");

				sql.append("m.dsCpf like ");
				sql.append("'" + filtro.getDsCpf() + "'");
			}
			
			if(filtro.getCdProtocoloFiltro() != null && filtro.getCdProtocoloFiltro() > 0) {
				if(!flWhere) { 
					sql.append("where ");
					flWhere = true;
				}else sql.append(" and ");

				sql.append("prt.cdProtocolo = ");
				sql.append(filtro.getCdProtocoloFiltro());
			}
			
			sql.append(" order by m.nmNome");
			
			List<MinistroCandidatoListTO> result = manager.createQuery(sql.toString(), MinistroCandidatoListTO.class)
					.getResultList();
			
			return result;
		} catch (NoResultException e) {
			return null;
		}
	}

	// Buscar útimo código de protocolo regisgtrado
	public Long findUltimoProtocoloRegistrado() {
		try{
			return manager.createQuery("select coalesce(max(prt.cdProtocolo),0) from Protocolo prt", Long.class).getSingleResult();
		}catch(NoResultException e){
			return null;
		}
		
	}

}
