package br.com.convencao.bo;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import br.com.convencao.dao.UPermissaoDAO;
import br.com.convencao.model.UPermissao;
import br.com.convencao.util.Uteis;
import br.com.convencao.util.jpa.Transactional;


public class UPermissaoBO implements Serializable {

	private static final long serialVersionUID = 1L;

	private static Log log = LogFactory.getLog(UPermissaoBO.class);

	@Inject
	private UPermissaoDAO dao;

	private UPermissao p;

	// Buscar todas as permissões de um usuário
	public List<UPermissao> findAllPorUsuario(Long sqUsuario) {
		log.info("findAllPorUsuario(" + sqUsuario + ")");

		return dao.findAllPorUsuario(sqUsuario);
	}

	// Buscar todas as permissoes de um usuário por tipo SECretaria, FINanceiro ou PAGamento
	public List<UPermissao> findAllPorUsuarioTpRegiao(Long sqUsuario, String tpRegiao) {
		log.info("findAllPorUsuarioTpRegiao(" + sqUsuario + ")");

		return dao.findAllPorUsuarioTpRegiao(sqUsuario, tpRegiao);
	}


	@Transactional
	public void ajustarPermissoes(List<UPermissao> permissoesNovas, List<UPermissao> permissoesVelhas) {
		try {
			log.info("ajustarPermissoes()");

			// Salvar as novas regiões em permissões para permitir conceder permissao específica de região para os usuários
			permissoesNovas.forEach(f -> {
				f.setAuditoriaData(Uteis.DataHoje());
				f.setAuditoriaUsuario(Uteis.UsuarioLogado().getUsuario().getDsLogin());
				salvar(f);
			});

			// Exluir em permissões as regiões que não existem mais
			permissoesVelhas.forEach(f -> {
				p = dao.find(UPermissao.class, f.getSqPermissao());
				dao.delete(p, p.getSqPermissao());
			});


		}catch (Exception e) {
			e.printStackTrace();
			throw new NegocioException("Erro ao tentar salvar permissões: " + e.getMessage());
		}

	}


	// Salvar as regiões para permissoes
	private void salvar(UPermissao uPermissao) {
		try {
			log.info("salvar(" + uPermissao.getSqPermissao() + ")");

			dao.salvar(uPermissao);
		} catch (Exception e) {
			throw new NegocioException("Erro ao tentar salvar permissões.");
		}
	}



	// Salvar as regiões configuradas para permissões do usuário
	@Transactional
	public void salvarPermissoesUsuario(List<UPermissao> permissoesSecretariaSelecionados,
			List<UPermissao> permissoesFinanceiroSelecionados, List<UPermissao> permissoesPagamentoSelecionados) {
		log.info("salvarPermissoesUsuario()");

		if(permissoesSecretariaSelecionados.size() == 0 && permissoesFinanceiroSelecionados.size() == 0 && permissoesPagamentoSelecionados.size() == 0) {
			throw new NegocioException("Não houve alteração nas permissões do usuário.");
		}

		// Gravar as permissões nas regiões do usuário para SECRETARIA
		permissoesSecretariaSelecionados.forEach(f -> {
			f.setAuditoriaData(Uteis.DataHoje());
			f.setAuditoriaUsuario(Uteis.UsuarioLogado().getUsuario().getDsLogin());

			f.setFlSecretaria(f.isFlSecretaria()? false: true );

			this.salvar(f);

		});

		// Gravar as permissões nas regiões do usuário para FINANCEIRO
		permissoesFinanceiroSelecionados.forEach(f -> {
			f.setAuditoriaData(Uteis.DataHoje());
			f.setAuditoriaUsuario(Uteis.UsuarioLogado().getUsuario().getDsLogin());

			f.setFlFinanceiro(f.isFlFinanceiro()? false: true);

			this.salvar(f);
		});

		// Gravar as permissões nas regiões do usuário para PAGAMENTOS
		permissoesPagamentoSelecionados.forEach(f -> {
			f.setAuditoriaData(Uteis.DataHoje());
			f.setAuditoriaUsuario(Uteis.UsuarioLogado().getUsuario().getDsLogin());

			f.setFlPagamento(f.isFlPagamento()? false: true);

			this.salvar(f);
		});	



	}

}
