package br.com.convencao.util.permissao;

import br.com.convencao.util.jsf.FacesUtil;

public class Permissoes {

	// grupos que tem permissão para inserir/editar todos os dados de ministro
	public static boolean getPermissaoInserirEditarMinistro() {
		boolean flRetorno = FacesUtil.getExternalContext().isUserInRole("guCftAdministrador") || 
				FacesUtil.getExternalContext().isUserInRole("guCftSecretarioConvencao") ||
				FacesUtil.getExternalContext().isUserInRole("guCftSecretarioRegiao");
		return flRetorno;
	}
	
	// grupos que tem permissão para cadastrar/alterar igrejas
	public static boolean getPermissaoInserirEditarIgreja() {
		boolean flRetorno = FacesUtil.getExternalContext().isUserInRole("guCftAdministrador") || 
				FacesUtil.getExternalContext().isUserInRole("guCftSecretarioConvencao");
		
		return flRetorno;
	}
	
	//guCftAdministrador
	// grupos que tem permissão para editar lancamento de entrada de recebimentos
	public static boolean getPermissaoEditarLancamentoEntradaRecebimentos() {
		boolean flRetorno = FacesUtil.getExternalContext().isUserInRole("guCftAdministrador");
		
		return flRetorno;
	}
	
	// grupos que tem permissão para cancelar lancamento de entrada de recebimentos
	public static boolean getPermissaoCancelarLancamentoEntradaRecebimentosPagos() {
		boolean flRetorno = FacesUtil.getExternalContext().isUserInRole("guCftAdministrador");
		
		return flRetorno;
	}

	// grupos que tem permissão para selecionar departamento CONFRATERES em cadastro de ministro
	public static boolean getPermissaoDepartamentoConfrateres(String tipo) {
		boolean flRetorno = false;

		if(tipo.equalsIgnoreCase("pesquisa")) {
			flRetorno=  FacesUtil.getExternalContext().isUserInRole("guCftAdministrador") ||
					FacesUtil.getExternalContext().isUserInRole("guCftFinanceiroLocal") ||
					FacesUtil.getExternalContext().isUserInRole("guCftFinanceiroRegiao") ||
					FacesUtil.getExternalContext().isUserInRole("guCftSecretarioConvencao") ||
					FacesUtil.getExternalContext().isUserInRole("guCftSecretarioRegiao") ||
					FacesUtil.getExternalContext().isUserInRole("guCftConsulta");  
		} else if (tipo.equalsIgnoreCase("cadastro")) {
			flRetorno=  FacesUtil.getExternalContext().isUserInRole("guCftAdministrador") ||
					FacesUtil.getExternalContext().isUserInRole("guCftFinanceiroLocal") ||
					FacesUtil.getExternalContext().isUserInRole("guCftFinanceiroRegiao") ||
					FacesUtil.getExternalContext().isUserInRole("guCftSecretarioConvencao");  
		}


		return flRetorno;
	}
	
	// grupo que tem permissão para concluir o processo de Candidato, transferindo-o para Ministro ou fazendo seu cancelamento
	public static boolean getPermissaoConcluirProtocolo(Long sqProtocoloStatus) {
		boolean flRetorno = false;
		// 0 = Está cadastrando, 7 = Protocolo Cancelado, 8 = Protocolo Transferido para rol de ministros (para estes 3 status o protocolo está sendo cadastrado ou está concluído e o botão não deve ser exibido)
		if(sqProtocoloStatus != 0 && sqProtocoloStatus != 7 && sqProtocoloStatus != 8) {
			flRetorno = FacesUtil.getExternalContext().isUserInRole("guCftAdministrador") ||
					FacesUtil.getExternalContext().isUserInRole("guCftSecretarioConvencao");
		}
		
		return flRetorno;
	}
	
	// Grupos que tem permissão para Alterar protocolo de candidato a ministro
	public static boolean getPermissaoEditarProtocolo() {
		boolean flRetorno = FacesUtil.getExternalContext().isUserInRole("guCftAdministrador") || 
				FacesUtil.getExternalContext().isUserInRole("guCftSecretarioConvencao") || 
				FacesUtil.getExternalContext().isUserInRole("guCftSecretarioRegiao");
		
		return flRetorno;
	}
	
	// Grupos que tem permissão para Excluir protocolo de candidato a ministro
	public static boolean getPermissaoExcluirProtocolo() {
		boolean flRetorno = FacesUtil.getExternalContext().isUserInRole("guCftAdministrador") || 
				FacesUtil.getExternalContext().isUserInRole("guCftSecretarioConvencao") || 
				FacesUtil.getExternalContext().isUserInRole("guCftSecretarioRegiao");
		
		return flRetorno;
	}
	
	// Grupos que tem permissao para inserir novo protocolo de candidato a ministro
	public static boolean getPermissaoInserirProtocolo() {
		boolean flRetorno = FacesUtil.getExternalContext().isUserInRole("guCftAdministrador") || 
				FacesUtil.getExternalContext().isUserInRole("guCftSecretarioConvencao") || 
				FacesUtil.getExternalContext().isUserInRole("guCftSecretarioRegiao");
		
		return flRetorno;
	}

}
