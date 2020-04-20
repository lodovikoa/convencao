package br.com.convencao.model;

import java.io.Serializable;
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
import javax.persistence.Transient;

import org.hibernate.validator.constraints.NotBlank;

@Entity
@Table(name = "tb_mse_ministro_senha")
public class MinistroSenha implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "mse_sq_ministro_senha")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long sqMinistroSenha;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "min_sq_ministro", nullable = false)
	private Ministro ministro;
	
	@NotBlank
	@Column(name = "mse_ds_senha", nullable = false, length = 40)
	private String dsSenha;
	
	@Column(name = "mse_fl_ativo")
	private boolean flAtivo;
	
	@Column(name = "mse_fl_provisoria")
	private boolean flProvisoria;
	
	@Column(name = "mse_dt_senha")
	private LocalDate dtSenha;
	
	@Column(name = "mse_nn_qtde_erro")
	private Integer nnQtdeErro;
	
	@Column(name = "auditoria_data")
	private LocalDateTime auditoriaData;
	
	@Column(name = "auditoria_usuario")
	private String auditoriaUsuario;
	
	@Transient
	private String dsSenhaTemp1;
	
	@Transient
	private String dsSenhaTemp2;

	public Long getSqMinistroSenha() {
		return sqMinistroSenha;
	}

	public void setSqMinistroSenha(Long sqMinistroSenha) {
		this.sqMinistroSenha = sqMinistroSenha;
	}

	public Ministro getMinistro() {
		return ministro;
	}

	public void setMinistro(Ministro ministro) {
		this.ministro = ministro;
	}

	public String getDsSenha() {
		return dsSenha;
	}

	public void setDsSenha(String dsSenha) {
		this.dsSenha = dsSenha;
	}

	public boolean isFlAtivo() {
		return flAtivo;
	}

	public void setFlAtivo(boolean flAtivo) {
		this.flAtivo = flAtivo;
	}

	public boolean isFlProvisoria() {
		return flProvisoria;
	}

	public void setFlProvisoria(boolean flProvisoria) {
		this.flProvisoria = flProvisoria;
	}

	public LocalDate getDtSenha() {
		return dtSenha;
	}

	public void setDtSenha(LocalDate dtSenha) {
		this.dtSenha = dtSenha;
	}

	public Integer getNnQtdeErro() {
		return nnQtdeErro;
	}

	public void setNnQtdeErro(Integer nnQtdeErro) {
		this.nnQtdeErro = nnQtdeErro;
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
	
	public String getDsSenhaTemp1() {
		return dsSenhaTemp1;
	}
	
	public void setDsSenhaTemp1(String dsSenhaTemp1) {
		this.dsSenhaTemp1 = dsSenhaTemp1;
	}
	
	public String getDsSenhaTemp2() {
		return dsSenhaTemp2;
	}
	
	public void setDsSenhaTemp2(String dsSenhaTemp2) {
		this.dsSenhaTemp2 = dsSenhaTemp2;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((sqMinistroSenha == null) ? 0 : sqMinistroSenha.hashCode());
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
		MinistroSenha other = (MinistroSenha) obj;
		if (sqMinistroSenha == null) {
			if (other.sqMinistroSenha != null)
				return false;
		} else if (!sqMinistroSenha.equals(other.sqMinistroSenha))
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
		MinistroSenha other = (MinistroSenha) obj;
		if (dsSenha == null) {
			if (other.dsSenha != null)
				return false;
		} else if (!dsSenha.equals(other.dsSenha))
			return false;
		if (dtSenha == null) {
			if (other.dtSenha != null)
				return false;
		} else if (!dtSenha.equals(other.dtSenha))
			return false;
		if (flAtivo != other.flAtivo)
			return false;
		if (flProvisoria != other.flProvisoria)
			return false;
		if (ministro == null) {
			if (other.ministro != null)
				return false;
		} else if (!ministro.equals(other.ministro))
			return false;
		if (nnQtdeErro == null) {
			if (other.nnQtdeErro != null)
				return false;
		} else if (!nnQtdeErro.equals(other.nnQtdeErro))
			return false;
		if (sqMinistroSenha == null) {
			if (other.sqMinistroSenha != null)
				return false;
		} else if (!sqMinistroSenha.equals(other.sqMinistroSenha))
			return false;
		return true;
	}
	
	

	
}
