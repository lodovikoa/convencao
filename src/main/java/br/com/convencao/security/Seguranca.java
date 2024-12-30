package br.com.convencao.security;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import br.com.convencao.util.Uteis;
import br.com.convencao.util.jsf.FacesUtil;

@Named(value = "seguranca")
@RequestScoped
public class Seguranca {
	
	public String getNomeUsuario() {
		String dsNome = null;
		
		UsuarioSistema usuarioLogado = Uteis.UsuarioLogado();
		
		if(usuarioLogado != null) {
			dsNome = usuarioLogado.getUsuario().getDsNome();	
		}
		return dsNome;
	}
	
	// Menu que deve exibi de acordo com o usuário logado
	
	// Mnu Financeiro
	public boolean isMenuFinanceiro() {
		return FacesUtil.getExternalContext().isUserInRole("guCftAdministrador")
				|| FacesUtil.getExternalContext().isUserInRole("guCftFinanceiroLocal")
				|| FacesUtil.getExternalContext().isUserInRole("guCftFinanceiroRegiao");  
	}
	
	public boolean isSubMenuResumoFinanceiro() {
		return FacesUtil.getExternalContext().isUserInRole("guCftAdministrador")
				|| FacesUtil.getExternalContext().isUserInRole("guCftFinanceiroRegiao");
	}
	
	public boolean isSubMenuLancamentos() {
		return FacesUtil.getExternalContext().isUserInRole("guCftAdministrador")
				|| FacesUtil.getExternalContext().isUserInRole("guCftFinanceiroLocal")
				|| FacesUtil.getExternalContext().isUserInRole("guCftFinanceiroRegiao");
	}
	
	public boolean isSubMenuLancamentosCreateEmLote() {
		return FacesUtil.getExternalContext().isUserInRole("guCftAdministrador");
	}
	
	public boolean isSubMenuLancamentosDeleteEmLote() {
		return FacesUtil.getExternalContext().isUserInRole("guCftAdministrador");
	}
	
	// Menu Recebimentos Online
	public boolean isMenuRecebimentosOnline() {
		return FacesUtil.getExternalContext().isUserInRole("guCftAdministrador");
	}
	
	public boolean isSubMenuFinanceiroOnlineAgoReciboList() {
		return FacesUtil.getExternalContext().isUserInRole("guCftAdministrador");
	}
	
	// Menu Cadastro
	public boolean isMenuCadastro () {
		return FacesUtil.getExternalContext().isUserInRole("guCftAdministrador")
				|| FacesUtil.getExternalContext().isUserInRole("guCftFinanceiroLocal")
				|| FacesUtil.getExternalContext().isUserInRole("guCftConsulta")
				|| FacesUtil.getExternalContext().isUserInRole("guCftFinanceiroRegiao")
				|| FacesUtil.getExternalContext().isUserInRole("guCftSecretarioConvencao")
				|| FacesUtil.getExternalContext().isUserInRole("guCftSecretarioRegiao"); 
	}
	
	public boolean isSubMenuIgrejas() {
		return FacesUtil.getExternalContext().isUserInRole("guCftAdministrador")
				|| FacesUtil.getExternalContext().isUserInRole("guCftFinanceiroLocal")
				|| FacesUtil.getExternalContext().isUserInRole("guCftConsulta")
				|| FacesUtil.getExternalContext().isUserInRole("guCftFinanceiroRegiao")
				|| FacesUtil.getExternalContext().isUserInRole("guCftSecretarioConvencao")
				|| FacesUtil.getExternalContext().isUserInRole("guCftSecretarioRegiao");
	}
	
	public boolean isSubMenuMinistros() {
		return FacesUtil.getExternalContext().isUserInRole("guCftAdministrador")
				|| FacesUtil.getExternalContext().isUserInRole("guCftFinanceiroLocal")
				|| FacesUtil.getExternalContext().isUserInRole("guCftConsulta")
				|| FacesUtil.getExternalContext().isUserInRole("guCftFinanceiroRegiao")
				|| FacesUtil.getExternalContext().isUserInRole("guCftSecretarioConvencao")
				|| FacesUtil.getExternalContext().isUserInRole("guCftSecretarioRegiao");
	}
	
	// Menu Configuração
	public boolean isMenuConfiguracao() {
		return FacesUtil.getExternalContext().isUserInRole("guCftAdministrador")
				|| FacesUtil.getExternalContext().isUserInRole("guCftSecretarioConvencao");
	}
	
	public boolean isSubMenuConvencao() {
		return FacesUtil.getExternalContext().isUserInRole("guCftAdministrador");
	}
	
	public boolean isSubMenuDepartamentos() {
		return FacesUtil.getExternalContext().isUserInRole("guCftAdministrador")
				|| FacesUtil.getExternalContext().isUserInRole("guCftSecretarioConvencao");
	}
	
	public boolean isSubMenuEstados() {
		return FacesUtil.getExternalContext().isUserInRole("guCftAdministrador")
				|| FacesUtil.getExternalContext().isUserInRole("guCftSecretarioConvencao");
	}
	
	public boolean isSubMenuEscolaridade() {
		return FacesUtil.getExternalContext().isUserInRole("guCftAdministrador")
				|| FacesUtil.getExternalContext().isUserInRole("guCftSecretarioConvencao");
	}
	
	public boolean isSubMenuCargo() {
		return FacesUtil.getExternalContext().isUserInRole("guCftAdministrador")
				|| FacesUtil.getExternalContext().isUserInRole("guCftSecretarioConvencao");
	}
	
	public boolean isSubMenuEstadoCivil() {
		return FacesUtil.getExternalContext().isUserInRole("guCftAdministrador")
				|| FacesUtil.getExternalContext().isUserInRole("guCftSecretarioConvencao");
	}
	
	public boolean isSubMenuProfissao() {
		return FacesUtil.getExternalContext().isUserInRole("guCftAdministrador")
				|| FacesUtil.getExternalContext().isUserInRole("guCftSecretarioConvencao");
	}
	
	public boolean isSubMenuPlanoContas() {
		return FacesUtil.getExternalContext().isUserInRole("guCftAdministrador");
	}
	
	public boolean isSubmenuTipoLancamento() {
		return FacesUtil.getExternalContext().isUserInRole("guCftAdministrador");
	}
	
	public boolean isSubMenuRegiao() {
		return FacesUtil.getExternalContext().isUserInRole("guCftAdministrador")
				|| FacesUtil.getExternalContext().isUserInRole("guCftSecretarioConvencao");
	}
	
	// Menu Auditoria
	public boolean isMenuAuditoria() {
		return FacesUtil.getExternalContext().isUserInRole("guCftAdministrador");
	}
	
	public boolean isSubMenuAuditoria() {
		return FacesUtil.getExternalContext().isUserInRole("guCftAdministrador");
	}
	
	// Menu Segurança
	public boolean isMenuSeguranca() {
		return FacesUtil.getExternalContext().isUserInRole("guCftAdministrador");
	}
	
	public boolean isSubMenuSeguranca() {
		return FacesUtil.getExternalContext().isUserInRole("guCftAdministrador");
	}
	
	// Menu Relatórios Secretaria
	public boolean isMenuRelatorioSecretaria() {
		return FacesUtil.getExternalContext().isUserInRole("guCftAdministrador")
				|| FacesUtil.getExternalContext().isUserInRole("guCftConsulta")
				|| FacesUtil.getExternalContext().isUserInRole("guCftSecretarioConvencao")
				|| FacesUtil.getExternalContext().isUserInRole("guCftSecretarioRegiao");
	}
	
	public boolean isSubMenuRelatorioMinistro() {
		return FacesUtil.getExternalContext().isUserInRole("guCftAdministrador")
				|| FacesUtil.getExternalContext().isUserInRole("guCftConsulta")
				|| FacesUtil.getExternalContext().isUserInRole("guCftSecretarioConvencao")
				|| FacesUtil.getExternalContext().isUserInRole("guCftSecretarioRegiao");
	}
	
	public boolean isSubMenuRelatorioMinistroPorIgreja() {
		return FacesUtil.getExternalContext().isUserInRole("guCftAdministrador")
				|| FacesUtil.getExternalContext().isUserInRole("guCftConsulta")
				|| FacesUtil.getExternalContext().isUserInRole("guCftSecretarioConvencao")
				|| FacesUtil.getExternalContext().isUserInRole("guCftSecretarioRegiao");
	}
	
	public boolean isSubMenuRelatorioMinistroPrPresidente() {
		return FacesUtil.getExternalContext().isUserInRole("guCftAdministrador")
				|| FacesUtil.getExternalContext().isUserInRole("guCftConsulta")
				|| FacesUtil.getExternalContext().isUserInRole("guCftSecretarioConvencao")
				|| FacesUtil.getExternalContext().isUserInRole("guCftSecretarioRegiao");
	}
	
	public boolean isSubMenuRelatorioMinistroAniversariantes() {
		return FacesUtil.getExternalContext().isUserInRole("guCftAdministrador")
				|| FacesUtil.getExternalContext().isUserInRole("guCftConsulta")
				|| FacesUtil.getExternalContext().isUserInRole("guCftSecretarioConvencao")
				|| FacesUtil.getExternalContext().isUserInRole("guCftSecretarioRegiao");
	}
	
	// Menu Relatórios Financeiro
	public boolean isMenuRelatorioFinanceiro() {
		return FacesUtil.getExternalContext().isUserInRole("guCftAdministrador")
				|| FacesUtil.getExternalContext().isUserInRole("guCftConsulta")
				|| FacesUtil.getExternalContext().isUserInRole("guCftFinanceiroRegiao");
	}
	
	public boolean isSubMenuRelatorioFinanceiroResumo() {
		return FacesUtil.getExternalContext().isUserInRole("guCftAdministrador")
				|| FacesUtil.getExternalContext().isUserInRole("guCftConsulta")
				|| FacesUtil.getExternalContext().isUserInRole("guCftFinanceiroRegiao");
	}
	
	public boolean isSubMenuRelatorioFinanceiroLancamentosEntradaPeriodo() {
		return FacesUtil.getExternalContext().isUserInRole("guCftAdministrador")
				|| FacesUtil.getExternalContext().isUserInRole("guCftConsulta")
				|| FacesUtil.getExternalContext().isUserInRole("guCftFinanceiroRegiao");
	}
	
	public boolean isSubMenuRelatorioFinanceiroLancamentosEntradaMinistro() {
		return FacesUtil.getExternalContext().isUserInRole("guCftAdministrador")
				|| FacesUtil.getExternalContext().isUserInRole("guCftConsulta")
				|| FacesUtil.getExternalContext().isUserInRole("guCftFinanceiroRegiao");
	}
	
	public boolean isSubMenuRelatorioFinanceiroLancamentosEntradaRecibo() {
		return FacesUtil.getExternalContext().isUserInRole("guCftAdministrador")
				|| FacesUtil.getExternalContext().isUserInRole("guCftConsulta")
				|| FacesUtil.getExternalContext().isUserInRole("guCftFinanceiroRegiao");
	}
	
	public boolean isSubMenuRelatorioFinanceiroLancamentosEntradaFormaRecebimento() {
		return FacesUtil.getExternalContext().isUserInRole("guCftAdministrador")
				|| FacesUtil.getExternalContext().isUserInRole("guCftConsulta")
				|| FacesUtil.getExternalContext().isUserInRole("guCftFinanceiroRegiao");
	}
	
	public boolean isSubMenuRelatorioFinanceiroLancamentosEmAberto() {
		return FacesUtil.getExternalContext().isUserInRole("guCftAdministrador")
				|| FacesUtil.getExternalContext().isUserInRole("guCftConsulta")
				|| FacesUtil.getExternalContext().isUserInRole("guCftFinanceiroRegiao");
	}
	
	public boolean isSubMenuRelatorioCartaCobranca() {
		return FacesUtil.getExternalContext().isUserInRole("guCftAdministrador")
				|| FacesUtil.getExternalContext().isUserInRole("guCftConsulta")
				|| FacesUtil.getExternalContext().isUserInRole("guCftFinanceiroRegiao");
	}
	
	public boolean isSubMenuRelatorioFinanceiroLancamentosSaidaPeriodo() {
		return FacesUtil.getExternalContext().isUserInRole("guCftAdministrador")
				|| FacesUtil.getExternalContext().isUserInRole("guCftConsulta")
				|| FacesUtil.getExternalContext().isUserInRole("guCftFinanceiroRegiao");
	}
}
