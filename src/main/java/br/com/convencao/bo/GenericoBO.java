package br.com.convencao.bo;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class GenericoBO implements Serializable {

	private static final long serialVersionUID = 1L;
	private static Log log = LogFactory.getLog(GenericoBO.class);

	//Validar CNPJ
	protected boolean validaCnpj( String str_cnpj ) { 
		log.info("validaCnpj(" + str_cnpj + ")");

		int soma = 0, dig;

		if ( str_cnpj.length() != 14 )  
			return false;  

		String cnpj_calc = str_cnpj.substring(0,12);  

		char[] chr_cnpj = str_cnpj.toCharArray();  

		/* Primeira parte */  
		for( int i = 0; i < 4; i++ )  
			if ( chr_cnpj[i]-48 >=0 && chr_cnpj[i]-48 <=9 )  
				soma += (chr_cnpj[i] - 48 ) * (6 - (i + 1)) ;  
		for( int i = 0; i < 8; i++ )  
			if ( chr_cnpj[i+4]-48 >=0 && chr_cnpj[i+4]-48 <=9 )  
				soma += (chr_cnpj[i+4] - 48 ) * (10 - (i + 1)) ;  
		dig = 11 - (soma % 11);  

		cnpj_calc += ( dig == 10 || dig == 11 ) ?  
				"0" : Integer.toString(dig);  

		/* Segunda parte */  
		soma = 0;  
		for ( int i = 0; i < 5; i++ )  
			if ( chr_cnpj[i]-48 >=0 && chr_cnpj[i]-48 <=9 )  
				soma += (chr_cnpj[i] - 48 ) * (7 - (i + 1)) ;  
		for ( int i = 0; i < 8; i++ )  
			if ( chr_cnpj[i+5]-48 >=0 && chr_cnpj[i+5]-48 <=9 )  
				soma += (chr_cnpj[i+5] - 48 ) * (10 - (i + 1)) ;  
		dig = 11 - (soma % 11);  
		cnpj_calc += ( dig == 10 || dig == 11 ) ?  
				"0" : Integer.toString(dig);  

		return str_cnpj.equals(cnpj_calc);  
	}

	protected boolean validaCpf(String cpfNum) {
		log.info("validaCpf(" + cpfNum + ")");
		if(cpfNum.length() != 11)
			return false;

		int[] cpf = new int[cpfNum.length()]; //define o valor com o tamanho da string  
		int resultP = 0;  
		int resultS = 0;
		int controle = 0;

		//converte a string para um array de integer  
		for (int i = 0; i < cpf.length; i++) {  
			cpf[i] = Integer.parseInt(cpfNum.substring(i, i + 1));  
		}  

		//calcula o primeiro número(DIV) do cpf 
		controle = 11;
		for (int i = 0; i < 9; i++) {  
			resultP += cpf[i] * (-- controle);  
		}  
		int divP = resultP % 11; 
		if(divP < 2) divP = 0;
		else divP = 11 - divP;

		//se o resultado for diferente ao 10º digito do cpf retorna falso  
		if (divP != cpf[9]) {  
			return false;  
		} else {  
			//calcula o segundo número(DIV) do cpf  
			controle = 12;
			for (int i = 0; i < 10; i++) {  
				resultS += cpf[i] * (-- controle);  
			}  
			int divS = resultS % 11;
			if(divS < 2) divS = 0;
			else divS = 11 - divS;

			//se o resultado for diferente ao 11º digito do cpf retorna falso  
			if (divS != cpf[10]) {  
				return false;  
			}  
		}  

		//se tudo estiver ok retorna verdadeiro  
		return true;  
	}

	//Converter uma data para uma string no formato dtFormato 
	protected String dataToString(LocalDate dtData, String dtFormato) {

		String dtConverter = null;
		if(dtData != null) {
			DateTimeFormatter formatadorBarra = DateTimeFormatter.ofPattern(dtFormato);
			dtConverter = dtData.format(formatadorBarra);
		}

		return dtConverter;
	}
}
