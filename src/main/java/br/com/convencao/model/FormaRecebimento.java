package br.com.convencao.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Size;

@Entity
@Table(name = "tb_frc_formarecebimento")
public class FormaRecebimento implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "frc_sq_formarecebimento")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long sqFormaRecebimento;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "rcb_sq_recibo", nullable = false)
	private Recibo recibo;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fpg_sq_formapagamento", nullable = false)
	private FormaPagamento formaPagamento;
	
	@Column(name="frc_vl_recebido", nullable = false, precision = 10, scale = 2)
	private BigDecimal vlRecebido;
	
	@Column(name="frc_vl_contabil", nullable = false, precision = 10, scale = 2)
	private BigDecimal vlContabil;
	
	@Column(name="frc_vl_troco", nullable = false, precision = 10, scale = 2)
	private BigDecimal vlTroco;
	
	@Size(max = 30, message = "tamanho máximo de 30 caracteres")
	@Column(name = "frc_ds_complemento", nullable = false, length = 50) 
	private String dsComplemento;
	
	@Column(name="auditoria_data")
	private LocalDateTime auditoriaData;
	
	@Column(name="auditoria_usuario", length=50)
	private String auditoriaUsuario;

	public Long getSqFormaRecebimento() {
		return sqFormaRecebimento;
	}

	public void setSqFormaRecebimento(Long sqFormaRecebimento) {
		this.sqFormaRecebimento = sqFormaRecebimento;
	}

	public Recibo getRecibo() {
		return recibo;
	}

	public void setRecibo(Recibo recibo) {
		this.recibo = recibo;
	}

	public FormaPagamento getFormaPagamento() {
		return formaPagamento;
	}

	public void setFormaPagamento(FormaPagamento formaPagamento) {
		this.formaPagamento = formaPagamento;
	}

	public BigDecimal getVlRecebido() {
		return vlRecebido;
	}

	public void setVlRecebido(BigDecimal vlRecebido) {
		this.vlRecebido = vlRecebido;
	}

	public BigDecimal getVlContabil() {
		return vlContabil;
	}

	public void setVlContabil(BigDecimal vlContabil) {
		this.vlContabil = vlContabil;
	}

	public BigDecimal getVlTroco() {
		return vlTroco;
	}

	public void setVlTroco(BigDecimal vlTroco) {
		this.vlTroco = vlTroco;
	}

	public String getDsComplemento() {
		return dsComplemento;
	}

	public void setDsComplemento(String dsComplemento) {
		this.dsComplemento = dsComplemento;
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
		result = prime * result + ((sqFormaRecebimento == null) ? 0 : sqFormaRecebimento.hashCode());
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
		FormaRecebimento other = (FormaRecebimento) obj;
		if (sqFormaRecebimento == null) {
			if (other.sqFormaRecebimento != null)
				return false;
		} else if (!sqFormaRecebimento.equals(other.sqFormaRecebimento))
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
		FormaRecebimento other = (FormaRecebimento) obj;
		if (dsComplemento == null) {
			if (other.dsComplemento != null)
				return false;
		} else if (!dsComplemento.equals(other.dsComplemento))
			return false;
		if (formaPagamento == null) {
			if (other.formaPagamento != null)
				return false;
		} else if (!formaPagamento.equals(other.formaPagamento))
			return false;
		if (recibo == null) {
			if (other.recibo != null)
				return false;
		} else if (!recibo.equals(other.recibo))
			return false;
		if (vlRecebido == null) {
			if (other.vlRecebido != null)
				return false;
		} else if (!vlRecebido.equals(other.vlRecebido))
			return false;
		if (vlContabil == null) {
			if (other.vlContabil != null)
				return false;
		} else if (!vlContabil.equals(other.vlContabil))
			return false;
		if (vlTroco == null) {
			if (other.vlTroco != null)
				return false;
		} else if (!vlTroco.equals(other.vlTroco))
			return false;
		return true;
	}

}
