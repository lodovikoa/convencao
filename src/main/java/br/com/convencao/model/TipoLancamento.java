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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

@Entity
@Table(name = "tb_tpl_tipolancamento")
public class TipoLancamento implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "tpl_sq_tipolancamento")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long sqTipoLancamento;
	
	@NotBlank
	@Size(max = 40, message = "tamanho máximo de 40 caracteres")
	@Column(name = "tpl_ds_tipolancamento", nullable = false, length = 40) 
	private String dsTipoLancamento;
	
	@NotNull
	@Column(name="tpl_vl_tipolancamento", nullable = false, precision = 10, scale = 2)
	private BigDecimal vlTipoLancamento;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "plc_sq_plano_contas", nullable = false)
	private PlanoConta planoConta;
	
	@Column(name="auditoria_data")
	private LocalDateTime auditoriaData;
	
	@Column(name="auditoria_usuario", length=50)
	private String auditoriaUsuario;

	public Long getSqTipoLancamento() {
		return sqTipoLancamento;
	}

	public void setSqTipoLancamento(Long sqTipoLancamento) {
		this.sqTipoLancamento = sqTipoLancamento;
	}

	public String getDsTipoLancamento() {
		return dsTipoLancamento;
	}

	public void setDsTipoLancamento(String dsTipoLancamento) {
		this.dsTipoLancamento = dsTipoLancamento;
	}

	public BigDecimal getVlTipoLancamento() {
		return vlTipoLancamento;
	}

	public void setVlTipoLancamento(BigDecimal vlTipoLancamento) {
		this.vlTipoLancamento = vlTipoLancamento;
	}

	public PlanoConta getPlanoConta() {
		return planoConta;
	}

	public void setPlanoConta(PlanoConta planoConta) {
		this.planoConta = planoConta;
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
		result = prime * result + ((sqTipoLancamento == null) ? 0 : sqTipoLancamento.hashCode());
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
		TipoLancamento other = (TipoLancamento) obj;
		if (sqTipoLancamento == null) {
			if (other.sqTipoLancamento != null)
				return false;
		} else if (!sqTipoLancamento.equals(other.sqTipoLancamento))
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
		TipoLancamento other = (TipoLancamento) obj;
		if (dsTipoLancamento == null) {
			if (other.dsTipoLancamento != null)
				return false;
		} else if (!dsTipoLancamento.equals(other.dsTipoLancamento))
			return false;
		if (planoConta == null) {
			if (other.planoConta != null)
				return false;
		} else if (!planoConta.equals(other.planoConta))
			return false;
		if (vlTipoLancamento == null) {
			if (other.vlTipoLancamento != null)
				return false;
		} else if (!vlTipoLancamento.equals(other.vlTipoLancamento))
			return false;
		return true;
	}

}
