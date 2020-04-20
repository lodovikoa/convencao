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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@Entity
@Table(name = "tb_igr_igreja")
public class Igreja implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "igr_sq_igreja")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long sqIgreja;
	
	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "rgn_sq_regiao", nullable=false)
	private Regiao regiao; 
	
	//@NotBlank
	@Size(max = 100, message = "tamanho máximo de 100 caracteres")
	@Column(name = "igr_ds_igreja", nullable=false)
	private String dsIgreja;
	
	@Transient
	private String dsIgrejaInvertida;
	

	@Column(name = "igr_ds_cnpj", length = 14)
	private String dsCnpj;
	
	@Column(name = "igr_dt_aniversario") 
	private LocalDate dtAniversario;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "min_sq_ministro", nullable = false) 
	private Ministro ministro;
	
	@Size(max = 100, message = "tamanho máximo de 100 caracteres")
	@Column(name = "igr_ds_endereco") 
	private String dsEndereco;
	
	@Size(max = 100, message = "tamanho máximo de 100 caracteres")
	@Column(name = "igr_ds_bairro") 
	private String dsBairro;
	
	@Size(max = 100, message = "tamanho máximo de 100 caracteres")
	@Column(name = "igr_ds_cidade") 
	private String dsCidade;
	
	
	@Column(name = "igr_ds_cep", length = 8) 
	private String dsCep;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="est_sq_estado", nullable=false)
	private Estado estado;
	
	@Column(name = "igr_ds_email") 
	private String dsEmail;

	@Column(name="auditoria_data")
	private LocalDateTime auditoriaData;
	
	@Column(name="auditoria_usuario", length=50)
	private String auditoriaUsuario;
	
	@Transient
	private String dsCnpjComMascara;
	
	@Transient
	private String dsCepComMascara;

	public Long getSqIgreja() {
		return sqIgreja;
	}

	public void setSqIgreja(Long sqIgreja) {
		this.sqIgreja = sqIgreja;
	}

	public Regiao getRegiao() {
		return regiao;
	}

	public void setRegiao(Regiao regiao) {
		this.regiao = regiao;
	}

	public String getDsIgreja() {
		return dsIgreja;
	}

	public void setDsIgreja(String dsIgreja) {
		this.dsIgreja = dsIgreja;
	}

	public String getDsIgrejaInvertida() {
		return dsIgrejaInvertida;
	}

	public void setDsIgrejaInvertida(String dsIgrejaInvertida) {
		this.dsIgrejaInvertida = dsIgrejaInvertida;
	}

	public String getDsCnpj() {
		return dsCnpj;
	}

	public void setDsCnpj(String dsCnpj) {
		this.dsCnpj = dsCnpj;
	}

	public LocalDate getDtAniversario() {
		return dtAniversario;
	}

	public void setDtAniversario(LocalDate dtAniversario) {
		this.dtAniversario = dtAniversario;
	}

	public Ministro getMinistro() {
		return ministro;
	}

	public void setMinistro(Ministro ministro) {
		this.ministro = ministro;
	}

	public String getDsEndereco() {
		return dsEndereco;
	}

	public void setDsEndereco(String dsEndereco) {
		this.dsEndereco = dsEndereco;
	}

	public String getDsBairro() {
		return dsBairro;
	}

	public void setDsBairro(String dsBairro) {
		this.dsBairro = dsBairro;
	}

	public String getDsCidade() {
		return dsCidade;
	}

	public void setDsCidade(String dsCidade) {
		this.dsCidade = dsCidade;
	}

	public String getDsCep() {
		return dsCep;
	}

	public void setDsCep(String dsCep) {
		this.dsCep = dsCep;
	}

	public Estado getEstado() {
		return estado;
	}

	public void setEstado(Estado estado) {
		this.estado = estado;
	}

	public String getDsEmail() {
		return dsEmail;
	}

	public void setDsEmail(String dsEmail) {
		this.dsEmail = dsEmail;
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

	public String getDsCnpjComMascara() {
		return dsCnpjComMascara;
	}

	public void setDsCnpjComMascara(String dsCnpjComMascara) {
		this.dsCnpjComMascara = dsCnpjComMascara;
	}

	public String getDsCepComMascara() {
		return dsCepComMascara;
	}

	public void setDsCepComMascara(String dsCepComMascara) {
		this.dsCepComMascara = dsCepComMascara;
	}

	
	@Override
	public int hashCode() {
		return (int) ((sqIgreja == null)? 0: sqIgreja);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (obj instanceof Igreja) {
			return ((Igreja) obj).getSqIgreja().equals(this.sqIgreja);
		}
		return false;	
	}

	
// Ajuda obtida no site: http://www.guj.com.br/t/erro-de-validacao-o-valor-nao-e-valido/202076/6	
	
//	@Override
//	public int hashCode() {
//		final int prime = 31;
//		int result = 1;
//		result = prime * result + ((sqIgreja == null) ? 0 : sqIgreja.hashCode());
//		return result;
//	}
//
//	@Override
//	public boolean equals(Object obj) {
//		if (this == obj)
//			return true;
//		if (obj == null)
//			return false;
//		if (getClass() != obj.getClass())
//			return false;
//		Igreja other = (Igreja) obj;
//		if (sqIgreja == null) {
//			if (other.sqIgreja != null)
//				return false;
//		} else if (!sqIgreja.equals(other.sqIgreja))
//			return false;
//		return true;
//	}

	public boolean equalsTO(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Igreja other = (Igreja) obj;
		if (dsBairro == null) {
			if (other.dsBairro != null)
				return false;
		} else if (!dsBairro.equals(other.dsBairro))
			return false;
		if (dsCep == null) {
			if (other.dsCep != null)
				return false;
		} else if (!dsCep.equals(other.dsCep))
			return false;
		if (dsCidade == null) {
			if (other.dsCidade != null)
				return false;
		} else if (!dsCidade.equals(other.dsCidade))
			return false;
		if (dsCnpj == null) {
			if (other.dsCnpj != null)
				return false;
		} else if (!dsCnpj.equals(other.dsCnpj))
			return false;
		if (dsEmail == null) {
			if (other.dsEmail != null)
				return false;
		} else if (!dsEmail.equals(other.dsEmail))
			return false;
		if (dsEndereco == null) {
			if (other.dsEndereco != null)
				return false;
		} else if (!dsEndereco.equals(other.dsEndereco))
			return false;
		if (dsIgreja == null) {
			if (other.dsIgreja != null)
				return false;
		} else if (!dsIgreja.equals(other.dsIgreja))
			return false;
		if (dtAniversario == null) {
			if (other.dtAniversario != null)
				return false;
		} else if (!dtAniversario.equals(other.dtAniversario))
			return false;
		if (estado == null) {
			if (other.estado != null)
				return false;
		} else if (!estado.equals(other.estado))
			return false;
		if (ministro == null) {
			if (other.ministro != null)
				return false;
		} else if (!ministro.equals(other.ministro))
			return false;
		if (regiao == null) {
			if (other.regiao != null)
				return false;
		} else if (!regiao.equals(other.regiao))
			return false;
		return true;
	}

	
}
