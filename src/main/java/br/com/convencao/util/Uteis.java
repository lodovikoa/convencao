package br.com.convencao.util;

import java.math.BigDecimal;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Locale;

import javax.faces.context.FacesContext;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import br.com.convencao.security.UsuarioSistema;

public class Uteis {

	// Constantes
	public static final String _SECretaria = "SEC";
	public static final String _FINanceiro = "FIN";
	public static final String _PAGagamento = "PAG";
	public static final String _TUDo = "TUD";
	public static final String _FOTO_DEFAULT = "0.jpg";
//	public static final String _S3_BUCKET_confrateresanexos = "confrateresanexos";
	
	// Usado para identificar nome da tabela na exclusão de registros quando é gravado em auditoria.
	public static String obterNomeTabela(String dsClasse) {
		String result = null;
		
		switch(dsClasse) {
			case "Auditoria":
				result = "tb_aud_auditoria";
				break;
			case "Boleto":
				result = "tb_bol_boleto";
				break;
			case "Cargo":
				result = "tb_cgo_cargo";
				break;
			case "Convencao": 
				result = "tb_con_convencao";
				break;
			case "Departamento":
				result = "tb_dpt_departamento";
				break;
			case "Escolaridade":
				result = "tb_esc_escolaridade";
				break;
			case "Estado":
				result = "tb_est_estado";
				break;
			case "EstadoCivel":
				result = "tb_civ_estadocivel";
				break;
			case "FormaPagamento":
				result = "tb_fpg_formapagamento";
				break;
			case "FormaRecebimento":
				result = "tb_frc_formarecebimento";
				break;
			case "Igreja":
				result = "tb_igr_igreja";
				break;
			case "Lancamento":
				result = "tb_lan_lancamento";
				break;
			case "LancamentoResumo":
				result = "tb_lar_lancamento_resumo";
				break;
			case "LancamentoSaida":
				result = "tb_las_lancamento_saida";
				break;
			case "Ministro":
				result = "tb_min_ministro";
				break;
			case "MinistroSennha":
				result = "tb_mse_ministro_senha";
				break;
			case "Nsu":
				result = "tb_nsu_nsu";
				break;
			case "PlanoConta":
				result = "tb_plc_plano_contas";
				break;
			case "Profissao":
				result = "tb_prf_profissao";
				break;
			case "Recibo":
				result = "tb_rcb_recibo";
				break;
			case "Regiao": 
				result  = "tb_rgn_regiao";
				break;
			case "RegLancamento": 
				result  = "tb_rgl_reglancamento";
				break;
			case "TipoLancamento": 
				result  = "tb_tpl_tipolancamento";
				break;
			case "UGrupo": 
				result  = "tb_ugr_grupo";
				break;
			case "UPermissao": 
				result  = "tb_prm_permissao";
				break;
			case "Usuario": 
				result  = "tb_usu_usuario";
				break;
			case "UsuarioGrupo": 
				result  = "tb_uug_usuario_grupo";
				break;
			case "ProtocoloAnexo": 
				result  = "tb_pra_protocolo_anexo";
				break;
			case "ProtocoloParecer": 
				result  = "tb_prc_protocolo_parecer";
				break;
			case "ProtocoloStatus": 
				result  = "tb_prs_protocolo_status";
				break;
			case "Protocolo": 
				result  = "tb_prt_protocolo";
				break;				
		}
		
		return result;
	}

	// Data atual
	public static LocalDateTime DataHoje() {	
		return LocalDateTime.now(ZoneId.of("America/Sao_Paulo"));
	}

	// Adicionar dias em uma data
	public static LocalDate AdcionarDiasNaData(LocalDate data, int qtdeDias) {
		LocalDate dataNova = data.plusDays(qtdeDias);	
		return dataNova;
	}

	// Converte String no formato "yyyy-MM-dd" de LocalDate
	public static LocalDate StringParaLocalDate(String strData){
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate dtData = LocalDate.parse(strData, formatter);

		return dtData;
	}

	// Converter LocalDate em uma String formatada do tipo dd/MM/yyyy
	public static String LocalDateParaString(LocalDate dtData) {
		String dsRetorno = "";
		if(dtData != null)
			dsRetorno =	dtData.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

		return dsRetorno;
	}

	// Retorna true se a primeira data é maior que a segunda data.
	public static boolean primeiraDataMaiorQueSegundaData(LocalDate dtMaior, LocalDate dtMenor) {
		if(dtMaior == null || dtMenor == null)
			return false;

		return dtMaior.isAfter(dtMenor);

	}

	// Retorna true se a dtInformada esta entre a dtPrimeira e dtUltima
	public static boolean primeiraDataEntreOutrasDuas(LocalDate dtInformada, LocalDate dtPrimeira, LocalDate dtUltima) {
		if(dtInformada == null || dtPrimeira == null)
			return false;

		if(dtInformada.isAfter(dtPrimeira) || dtInformada.isEqual(dtPrimeira)) {
			if(dtUltima == null || dtInformada.isBefore(dtUltima) || dtInformada.isEqual(dtUltima)) {
				return true;
			}
		}

		return false;
	}

	// Usuário logado
	public static UsuarioSistema UsuarioLogado() {

		UsuarioSistema usuario = null;

		UsernamePasswordAuthenticationToken auth = (UsernamePasswordAuthenticationToken)
				FacesContext.getCurrentInstance().getExternalContext().getUserPrincipal();

		if(auth != null && auth.getPrincipal() != null) {
			usuario = (UsuarioSistema) auth.getPrincipal();
		}

		return usuario;
	}

	// Retorna somente números de uma string
	public static String OnlyNumbers(String str) {
		if (str != null) {
			return str.replaceAll("[^0123456789]", "");
		} else {
			return null;
		}
	}

	// Colocar mascara no CNPJ
	public static String cnpjMask(String dsCnpj){
		if(dsCnpj != null){
			StringBuilder cnpj = new StringBuilder(dsCnpj);

			if(dsCnpj.length() < 14){
				int qtdeTam = dsCnpj.length();
				for (int i = qtdeTam; i <= 14; i++) {
					cnpj.insert(0, '0');
				}
			}

			cnpj.insert(cnpj.toString().length() - 12, '.');
			cnpj.insert(cnpj.toString().length() - 9, '.');
			cnpj.insert(cnpj.toString().length() - 6, '/');
			cnpj.insert(cnpj.toString().length() - 2, '-');

			return cnpj.toString();
		}else{
			return dsCnpj;
		}
	}

	// Colocar mascara no CEP
	public static String cepMask(String dsCep){

		if(dsCep != null && dsCep.length() > 5){
			StringBuilder cep = new StringBuilder(dsCep);
			cep.insert(5, '-');

			return cep.toString();
		}else{
			return dsCep;
		}
	}

	// Converter valores no formato brasileiro Ex.: R$ 1.550,85
	public static String valorFormatoBrasileiro(BigDecimal vlValor) {
		NumberFormat nf = new java.text.DecimalFormat("#,###,##0.00");
		return nf.format (vlValor);
	}

	// Converter LocalDate em uma data por extenso: Sexta, 02 de Fevereiro de 2018
	public static String dataExtenso(LocalDate data, Boolean diaSemana) {	
		StringBuilder dt = new StringBuilder();

		if(diaSemana) {
			dt.append(data.getDayOfWeek().getDisplayName(TextStyle.FULL, new Locale("pt","BR")))
			.append(", ");
		}
		
		dt.append(data.getDayOfMonth())
		.append(" de ")
		.append(data.getMonth().getDisplayName(TextStyle.FULL, new Locale("pt","BR")))
		.append(" de ")
		.append(data.getYear());

		return dt.toString();
	}
	
	// Obter o diretorio raiz onde são gravado os anexos
	public static Path diretorioRaizAnexos() {
		Path raizAplicacao = FileSystems.getDefault().getPath(System.getProperty("user.home"), ".ministrofotos");
		return raizAplicacao.resolve("anexos-ministro");
	}

	// Trocar <Enter> pela tag <br /> para que o html quebre linha
	public static String inserirQuebraLinhaHtml(String dsTexto) {
		return dsTexto.replaceAll("\r\n", "<br />");
	}
}
