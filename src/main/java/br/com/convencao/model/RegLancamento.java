package br.com.convencao.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
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

@Entity
@Table(name="tb_rgl_reglancamento")
public class RegLancamento implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name="rgl_sq_reglancamento")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long sqRegLancamento;
	
	@Column(name = "rgl_cd_origem")
	private Integer cdOrigem;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "min_sq_ministro", nullable = false)
	private Ministro ministro;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "tpl_sq_tipolancamento", nullable = false)
	private TipoLancamento tipoLancamento;
	
	@Column(name = "rgl_dt_registro")
	private LocalDateTime dtRegistro;
	
	@Column(name = "rgl_dt_vencimento")
	private LocalDate dtVencimento;
	
	@Column(name="rgl_vl_lancamento", nullable = false, precision = 10, scale = 2)
	private BigDecimal vlLancamento;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "bol_sq_boleto", nullable = false)
	private Boleto boleto;
	
	@Column(name = "rgl_dt_cancelado")
	private LocalDateTime dtCancelado;
	
	@Column(name="auditoria_data")
	private LocalDateTime auditoriaData;
	
	@Column(name="auditoria_usuario", length=50)
	private String auditoriaUsuario;

	public Long getSqRegLancamento() {
		return sqRegLancamento;
	}

	public void setSqRegLancamento(Long sqRegLancamento) {
		this.sqRegLancamento = sqRegLancamento;
	}

	public Integer getCdOrigem() {
		return cdOrigem;
	}

	public void setCdOrigem(Integer cdOrigem) {
		this.cdOrigem = cdOrigem;
	}

	public Ministro getMinistro() {
		return ministro;
	}

	public void setMinistro(Ministro ministro) {
		this.ministro = ministro;
	}

	public TipoLancamento getTipoLancamento() {
		return tipoLancamento;
	}

	public void setTipoLancamento(TipoLancamento tipoLancamento) {
		this.tipoLancamento = tipoLancamento;
	}

	public LocalDateTime getDtRegistro() {
		return dtRegistro;
	}

	public void setDtRegistro(LocalDateTime dtRegistro) {
		this.dtRegistro = dtRegistro;
	}

	public LocalDate getDtVencimento() {
		return dtVencimento;
	}
	
	public void setDtVencimento(LocalDate dtVencimento) {
		this.dtVencimento = dtVencimento;
	}

	public BigDecimal getVlLancamento() {
		return vlLancamento;
	}

	public void setVlLancamento(BigDecimal vlLancamento) {
		this.vlLancamento = vlLancamento;
	}

	public Boleto getBoleto() {
		return boleto;
	}

	public void setBoleto(Boleto boleto) {
		this.boleto = boleto;
	}

	public LocalDateTime getDtCancelado() {
		return dtCancelado;
	}

	public void setDtCancelado(LocalDateTime dtCancelado) {
		this.dtCancelado = dtCancelado;
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
		result = prime * result + ((sqRegLancamento == null) ? 0 : sqRegLancamento.hashCode());
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
		RegLancamento other = (RegLancamento) obj;
		if (sqRegLancamento == null) {
			if (other.sqRegLancamento != null)
				return false;
		} else if (!sqRegLancamento.equals(other.sqRegLancamento))
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
		RegLancamento other = (RegLancamento) obj;
		if (boleto == null) {
			if (other.boleto != null)
				return false;
		} else if (!boleto.equals(other.boleto))
			return false;
		if (cdOrigem == null) {
			if (other.cdOrigem != null)
				return false;
		} else if (!cdOrigem.equals(other.cdOrigem))
			return false;
		if (dtCancelado == null) {
			if (other.dtCancelado != null)
				return false;
		} else if (!dtCancelado.equals(other.dtCancelado))
			return false;
		if (dtVencimento == null) {
			if (other.dtVencimento != null)
				return false;
		} else if (!dtVencimento.equals(other.dtVencimento))
			return false;
		if (dtRegistro == null) {
			if (other.dtRegistro != null)
				return false;
		} else if (!dtRegistro.equals(other.dtRegistro))
			return false;
		if (ministro == null) {
			if (other.ministro != null)
				return false;
		} else if (!ministro.equals(other.ministro))
			return false;
		if (sqRegLancamento == null) {
			if (other.sqRegLancamento != null)
				return false;
		} else if (!sqRegLancamento.equals(other.sqRegLancamento))
			return false;
		if (tipoLancamento == null) {
			if (other.tipoLancamento != null)
				return false;
		} else if (!tipoLancamento.equals(other.tipoLancamento))
			return false;
		if (vlLancamento == null) {
			if (other.vlLancamento != null)
				return false;
		} else if (!vlLancamento.equals(other.vlLancamento))
			return false;
		return true;
	}

}
