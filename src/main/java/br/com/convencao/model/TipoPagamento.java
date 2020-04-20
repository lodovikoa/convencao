package br.com.convencao.model;

public enum TipoPagamento {
	DINHEIRO (1),
	CHEQUE (2),
	OUTROS (3);
	
	private int label;
	
	TipoPagamento(int label) {
		this.label = label;
	}
	
	public int getLabel() {
		return label;
	}
}
