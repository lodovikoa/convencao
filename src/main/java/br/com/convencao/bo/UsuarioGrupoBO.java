package br.com.convencao.bo;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import br.com.convencao.dao.UsuarioGrupoDAO;
import br.com.convencao.model.UGrupo;
import br.com.convencao.model.Usuario;
import br.com.convencao.model.UsuarioGrupo;
import br.com.convencao.util.Uteis;
import br.com.convencao.util.jpa.Transactional;


public class UsuarioGrupoBO implements Serializable {

	private static final long serialVersionUID = 1L;

	private static Log log = LogFactory.getLog(UsuarioGrupoBO.class);

	@Inject
	private UsuarioGrupoDAO dao;

	@Transactional
	public void salvarGruposAssociados(List<UGrupo> gruposDisponivelSelecionados, List<UGrupo> gruposAssociadosSelecionados, Usuario usuario) {

		log.info("salvarGruposAssociados()");

		try {
			UsuarioGrupo usuarioGrupo;
			
			// Excluir grupos que foram desassociados, caso exista
			if(gruposAssociadosSelecionados != null && gruposAssociadosSelecionados.size() > 0) {
				for (UGrupo uGrupo : gruposAssociadosSelecionados) {
					usuarioGrupo = dao.findUsuario(uGrupo, usuario);
					
					dao.delete(usuarioGrupo, usuarioGrupo.getSqUsuarioGrupo());
					
				}
			}

			// Inserir novos grupos associados ao usuário
			if(gruposDisponivelSelecionados != null && gruposDisponivelSelecionados.size() > 0) {
				for (UGrupo uGrupo : gruposDisponivelSelecionados) {
					usuarioGrupo = new UsuarioGrupo();

					usuarioGrupo.setUsuario(usuario);
					usuarioGrupo.setGrupo(uGrupo);

					usuarioGrupo.setAuditoriaData(Uteis.DataHoje());
					usuarioGrupo.setAuditoriaUsuario(Uteis.UsuarioLogado().getUsuario().getDsLogin());

					dao.salvar(usuarioGrupo);

				}

			}

		} catch (Exception e) {
			throw new NegocioException("Erro ao tentar associar grupos ao usuário: " + e.getMessage(), e);
		}

	}



}
