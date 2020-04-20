package br.com.convencao.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;

@Entity
@Table(name = "tb_fpg_formapagamento")
public class FormaPagamento implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "fpg_sq_formapagamento")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long sqFormaPagamento;
	
	@Column(name = "fpg_cd_ordem")
	private  Integer cdOrdem;
	
	@Size(max = 30, message = "tamanho máximo de 30 caracteres")
	@Column(name = "fpg_ds_formapagamento", nullable = false, length = 50) 
	private String dsFormaPagamento;
	
	@Column(name = "fpg_fl_exibircomplemento")
	private boolean flExibirComplemento; 
	
	@Column(name = "fpg_fl_permitirtroco")
	private boolean flPermitirTroco;
	
	@Column(name="auditoria_data")
	private LocalDateTime auditoriaData;
	
	@Column(name="auditoria_usuario", length=50)
	private String auditoriaUsuario;

	public Long getSqFormaPagamento() {
		return sqFormaPagamento;
	}
	
	public void setSqFormaPagamento(Long sqFormaPagamento) {
		this.sqFormaPagamento = sqFormaPagamento;
	}

	public Integer getCdOrdem() {
		return cdOrdem;
	}

	public void setCdOrdem(Integer cdOrdem) {
		this.cdOrdem = cdOrdem;
	}

	public String getDsFormaPagamento() {
		return dsFormaPagamento;
	}

	public void setDsFormaPagamento(String dsFormaPagamento) {
		this.dsFormaPagamento = dsFormaPagamento;
	}

	public boolean isFlExibirComplemento() {
		return flExibirComplemento;
	}

	public void setFlExibirComplemento(boolean flExibirComplemento) {
		this.flExibirComplemento = flExibirComplemento;
	}
	
	public boolean isFlPermitirTroco() {
		return flPermitirTroco;
	}
	
	public void setFlPermitirTroco(boolean flPermitirTroco) {
		this.flPermitirTroco = flPermitirTroco;
	}

	public LocalDateTime getAuditoriaData() {
		return auditoriaData;
	}

	public void setAuditoriaData(LocalDateTime auditoriaData) {
		this.auditoriaData = auditoriaData;
	}

	public String getAuditoriaUsuario() {
		return auditoriaUsuario;
	}

	public void setAuditoriaUsuario(String auditoriaUsuario) {
		this.auditoriaUsuario = auditoriaUsuario;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((sqFormaPagamento == null) ? 0 : sqFormaPagamento.hashCode());
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
		FormaPagamento other = (FormaPagamento) obj;
		if (sqFormaPagamento == null) {
			if (other.sqFormaPagamento != null)
				return false;
		} else if (!sqFormaPagamento.equals(other.sqFormaPagamento))
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
		FormaPagamento other = (FormaPagamento) obj;
		if (cdOrdem == null) {
			if (other.cdOrdem != null)
				return false;
		} else if (!cdOrdem.equals(other.cdOrdem))
			return false;
		if (dsFormaPagamento == null) {
			if (other.dsFormaPagamento != null)
				return false;
		} else if (!dsFormaPagamento.equals(other.dsFormaPagamento))
			return false;
		if (flExibirComplemento != other.flExibirComplemento)
			return false;
		if (flPermitirTroco != other.flPermitirTroco)
			return false;
		return true;
	}
	
	
	
}
