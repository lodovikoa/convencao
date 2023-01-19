package br.com.convencao.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "tb_plc_plano_contas")
public class PlanoConta implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "plc_sq_plano_contas")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long sqPlanoConta;
	
	@NotNull
	@Column(name = "plc_cd_conta", nullable = false)
	private Long cdConta;
	 
	@NotBlank
	@Size(max = 30, message = "tamanho máximo de 30 caracters")
	@Column(name = "plc_ds_conta", nullable = false, length= 30)
	private String dsConta;
	
	@NotBlank
	@Size(min = 1, max = 1, message = "deve ter 1 caracter")
	@Column(name = "plc_tp_conta", nullable = false, length = 1)
	private String tpConta;
	
	@Column(name="auditoria_data")
	private LocalDateTime auditoriaData;
	
	@Column(name="auditoria_usuario", length=50)
	private String auditoriaUsuario;
	
	public Long getSqPlanoConta() {
		return sqPlanoConta;
	}

	public void setSqPlanoConta(Long sqPlanoConta) {
		this.sqPlanoConta = sqPlanoConta;
	}

	public Long getCdConta() {
		return cdConta;
	}

	public void setCdConta(Long cdConta) {
		this.cdConta = cdConta;
	}

	public String getDsConta() {
		return dsConta;
	}

	public void setDsConta(String dsConta) {
		this.dsConta = dsConta;
	}

	public String getTpConta() {
		return tpConta;
	}

	public void setTpConta(String tpConta) {
		this.tpConta = tpConta;
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
		result = prime * result + ((sqPlanoConta == null) ? 0 : sqPlanoConta.hashCode());
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
		PlanoConta other = (PlanoConta) obj;
		if (sqPlanoConta == null) {
			if (other.sqPlanoConta != null)
				return false;
		} else if (!sqPlanoConta.equals(other.sqPlanoConta))
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
		PlanoConta other = (PlanoConta) obj;
		if (cdConta == null) {
			if (other.cdConta != null)
				return false;
		} else if (!cdConta.equals(other.cdConta))
			return false;
		if (dsConta == null) {
			if (other.dsConta != null)
				return false;
		} else if (!dsConta.equals(other.dsConta))
			return false;
		if (tpConta == null) {
			if (other.tpConta != null)
				return false;
		} else if (!tpConta.equals(other.tpConta))
			return false;
		return true;
	}

	
}
