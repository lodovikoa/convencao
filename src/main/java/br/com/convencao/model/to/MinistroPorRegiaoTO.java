package br.com.convencao.model.to;

import java.io.Serializable;

public class MinistroPorRegiaoTO implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private String dsRegiao;
	private Long qtdeMinistros;
	
	public MinistroPorRegiaoTO() {
	}

	public MinistroPorRegiaoTO(String dsRegiao, Long qtdeMinistros) {
		this.dsRegiao = dsRegiao;
		this.qtdeMinistros = qtdeMinistros;
	}

	public String getDsRegiao() {
		return dsRegiao;
	}

	public void setDsRegiao(String dsRegiao) {
		this.dsRegiao = dsRegiao;
	}

	public Long getQtdeMinistros() {
		return qtdeMinistros;
	}

	public void setQtdeMinistros(Long qtdeMinistros) {
		this.qtdeMinistros = qtdeMinistros;
	}
	
}
