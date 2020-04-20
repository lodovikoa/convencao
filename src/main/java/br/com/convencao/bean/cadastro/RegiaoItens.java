package br.com.convencao.bean.cadastro;

import java.io.Serializable;

public class RegiaoItens implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long sqRegiao;
	private String dsRegiao;
	
	public Long getSqRegiao() {
		return sqRegiao;
	}
	public void setSqRegiao(Long sqRegiao) {
		this.sqRegiao = sqRegiao;
	}
	public String getDsRegiao() {
		return dsRegiao;
	}
	public void setDsRegiao(String dsRegiao) {
		this.dsRegiao = dsRegiao;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((sqRegiao == null) ? 0 : sqRegiao.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RegiaoItens other = (RegiaoItens) obj;
		if (sqRegiao == null) {
			if (other.sqRegiao != null)
				return false;
		} else if (!sqRegiao.equals(other.sqRegiao))
			return false;
		return true;
	}
	
}
