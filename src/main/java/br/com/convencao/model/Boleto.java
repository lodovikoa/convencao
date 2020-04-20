package br.com.convencao.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="tb_bol_boleto")
public class Boleto implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name="bol_sq_boleto")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long sqBoleto;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "rgl_sq_reglancamento", nullable = false)
	private RegLancamento regLancamento;
	
	@Column(name = "bol_ds_url") 
	private String dsUrl;
	
	public Long getSqBoleto() {
		return sqBoleto;
	}

	public void setSqBoleto(Long sqBoleto) {
		this.sqBoleto = sqBoleto;
	}

	public RegLancamento getRegLancamento() {
		return regLancamento;
	}

	public void setRegLancamento(RegLancamento regLancamento) {
		this.regLancamento = regLancamento;
	}

	public String getDsUrl() {
		return dsUrl;
	}

	public void setDsUrl(String dsUrl) {
		this.dsUrl = dsUrl;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((sqBoleto == null) ? 0 : sqBoleto.hashCode());
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
		Boleto other = (Boleto) obj;
		if (sqBoleto == null) {
			if (other.sqBoleto != null)
				return false;
		} else if (!sqBoleto.equals(other.sqBoleto))
			return false;
		return true;
	}

	// Método equals personalizado
	public boolean equalsTO(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Boleto other = (Boleto) obj;
		if (dsUrl == null) {
			if (other.dsUrl != null)
				return false;
		} else if (!dsUrl.equals(other.dsUrl))
			return false;
		if (regLancamento == null) {
			if (other.regLancamento != null)
				return false;
		} else if (!regLancamento.equals(other.regLancamento))
			return false;
		if (sqBoleto == null) {
			if (other.sqBoleto != null)
				return false;
		} else if (!sqBoleto.equals(other.sqBoleto))
			return false;
		return true;
	}


}
