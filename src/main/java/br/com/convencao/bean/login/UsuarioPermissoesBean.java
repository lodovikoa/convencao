package br.com.convencao.bean.login;

import java.util.ArrayList;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.convencao.bean.codebehind.Codebehind;
import br.com.convencao.bo.UPermissaoBO;
import br.com.convencao.model.Regiao;
import br.com.convencao.model.UPermissao;
import br.com.convencao.model.Usuario;
import br.com.convencao.util.jsf.FacesUtil;

@Named(value = "usuarioPermissoesBean")
@ViewScoped
public class UsuarioPermissoesBean extends Codebehind {

	private static final long serialVersionUID = 1L;

	@Inject
	private UPermissaoBO uPermissaoBO;
	
	private Usuario usuario;
	private List<UPermissao> listaPermissoes;
	
	private List<UPermissao> permissoesSecretariaSelecionados;
	private List<UPermissao> permissoesFinanceiroSelecionados;
	private List<UPermissao> permissoesPagamentoSelecionados;
	
	public void inicializar() {
		this.listarRegioes();
	}
	
	
	public void listarRegioes() {
		this.inicializarRegioes("TUD");
		List<UPermissao> permissoesNovas = new ArrayList<>();
		List<UPermissao> permissoesVelhas = new ArrayList<>();
		UPermissao permissaoTemp;
		Regiao regiao;

		// Buscar lista de permissões do usuário selecionado
		List<UPermissao> permissoes = uPermissaoBO.findAllPorUsuario(this.usuario.getSqUsuario());
		boolean temPermissao;
		for (int i = 0; i < this.getRegiaoItens().size(); i++) {
			temPermissao = false;
			for (int j = 0; j < permissoes.size(); j++) {
				if(permissoes.get(j).getRegiao().getSqRegiao() == getRegiaoItens().get(i).getSqRegiao()) {
					// Preparar para INSERIR permissão regiao nas peremissões de usuários
					temPermissao = true;
					permissoes.get(j).setPermissaoAtiva(true); 
					break;
				}
			}
			if(!temPermissao) {
				permissaoTemp= new UPermissao();
				regiao = new Regiao();

				regiao.setSqRegiao(this.getRegiaoItens().get(i).getSqRegiao());

				permissaoTemp.setUsuario(usuario);
				permissaoTemp.setRegiao(regiao);
				permissaoTemp.setFlFinanceiro(false);
				permissaoTemp.setFlPagamento(false);
				permissaoTemp.setFlSecretaria(false);

				permissoesNovas.add(permissaoTemp);
			}
		}

		for (int k = 0; k < permissoes.size(); k++) {
			if(!permissoes.get(k).isPermissaoAtiva()) {
				// Preparar para DELETAR permissão que não tem região correspondente
				permissaoTemp= new UPermissao();
				permissaoTemp.setSqPermissao(permissoes.get(k).getSqPermissao());

				permissoesVelhas.add(permissaoTemp);
			}

		}

		// Havendo novas regiões ou se houve exclusão de alguma região, será feito o ajuste das permissões
		if(permissoesNovas.size() > 0 || permissoesVelhas.size() > 0) {
			uPermissaoBO.ajustarPermissoes(permissoesNovas, permissoesVelhas);

			// Buscar lista atualizada de regiões que o usuário tem permissões para exibi-las na tela.
			this.listaPermissoes = uPermissaoBO.findAllPorUsuario(this.usuario.getSqUsuario());
		} else {
			// Se não houve atualização de permissões usar a mesma lista já buscada anteriormente.
			this.listaPermissoes = permissoes;
		}
	}
	
	public void salvarPermissoesUsuario() {
		uPermissaoBO.salvarPermissoesUsuario(this.permissoesSecretariaSelecionados, this.permissoesFinanceiroSelecionados, this.permissoesPagamentoSelecionados);

		// Exibir mensagem
		FacesUtil.addInfoMessage("Permissões do usuário " + this.usuario.getDsLogin() + " salvo com sucesso.");
		
		// Zerando as permissões já concedidas anteriormente
		this.permissoesSecretariaSelecionados = null;
		this.permissoesFinanceiroSelecionados = null;
		this.permissoesPagamentoSelecionados = null;

	}
	
	public List<UPermissao> getListaPermissoes() {
		return listaPermissoes;
	}
	
	public Usuario getUsuario() {
		return usuario;
	}
	
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}


	public List<UPermissao> getPermissoesSecretariaSelecionados() {
		return permissoesSecretariaSelecionados;
	}


	public void setPermissoesSecretariaSelecionados(List<UPermissao> permissoesSecretariaSelecionados) {
		this.permissoesSecretariaSelecionados = permissoesSecretariaSelecionados;
	}


	public List<UPermissao> getPermissoesFinanceiroSelecionados() {
		return permissoesFinanceiroSelecionados;
	}


	public void setPermissoesFinanceiroSelecionados(List<UPermissao> permissoesFinanceiroSelecionados) {
		this.permissoesFinanceiroSelecionados = permissoesFinanceiroSelecionados;
	}


	public List<UPermissao> getPermissoesPagamentoSelecionados() {
		return permissoesPagamentoSelecionados;
	}


	public void setPermissoesPagamentoSelecionados(List<UPermissao> permissoesPagamentoSelecionados) {
		this.permissoesPagamentoSelecionados = permissoesPagamentoSelecionados;
	}
	
}
