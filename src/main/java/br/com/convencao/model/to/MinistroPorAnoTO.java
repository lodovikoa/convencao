package br.com.convencao.model.to;

import java.io.Serializable;

public class MinistroPorAnoTO implements Serializable{

	private static final long serialVersionUID = 1L;

	private int nnAno;
	private Long qtdeMinistro;
	
	public MinistroPorAnoTO() {
	}

	public MinistroPorAnoTO(int nnAno, Long qtdeMinistro) {
		this.nnAno = nnAno;
		this.qtdeMinistro = qtdeMinistro;
	}

	public int getNnAno() {
		return nnAno;
	}

	public void setNnAno(int nnAno) {
		this.nnAno = nnAno;
	}

	public Long getQtdeMinistro() {
		return qtdeMinistro;
	}

	public void setQtdeMinistro(Long qtdeMinistro) {
		this.qtdeMinistro = qtdeMinistro;
	}
	
}
