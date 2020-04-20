package br.com.convencao.bean.cadastro;

import java.io.Serializable;

import br.com.convencao.model.Ministro;

public class MinistroListarTO implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Ministro ministroTO;
	private String dsRegiao;
	private String dsDepartamentoReduzido;
	private String dsCargo;
	private String dsIgreja;
	private String dsCidadeUf;
	
	public MinistroListarTO() {
	}
	
	public MinistroListarTO(Ministro ministro, String dsRegiao, String dsDepartamentoReduzido, String dsCargo, String dsIgreja, String dsCidadeUf) {
		this.ministroTO = ministro;
		this.dsRegiao = dsRegiao;
		this.dsDepartamentoReduzido = dsDepartamentoReduzido;
		this.dsCargo = dsCargo;
		this.dsIgreja = dsIgreja;
		this.dsCidadeUf = dsCidadeUf;				
	}

	public Ministro getMinistroTO() {
		return ministroTO;
	}

	public String getDsRegiao() {
		return dsRegiao;
	}

	public String getDsDepartamentoReduzido() {
		return dsDepartamentoReduzido;
	}

	public String getDsCargo() {
		return dsCargo;
	}

	public String getDsIgreja() {
		return dsIgreja;
	}

	public String getDsCidadeUf() {
		return dsCidadeUf;
	}
	
}
