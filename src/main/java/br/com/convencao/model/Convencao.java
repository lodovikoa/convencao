package br.com.convencao.model;

import java.io.Serializable;
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
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;



@Entity
@Table(name="tb_con_convencao")
public class Convencao implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="con_sq_convencao")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long sqConvencao;
	
	@NotBlank
	@Size(max = 20, message = "tamanho máximo de 20 caracteres")
	@Column(name="con_ds_reduzido", nullable=false, length=20)
	private String dsReduzido;
	
	@NotBlank
	@Size(max = 100, message = "tamanho máximo de 100 caracteres")
	@Column(name="con_ds_convencao", nullable=false, length=100)
	private String dsConvencao;

	@Column(name="con_im_logo", length=100)
	private String imLogo;
	
	@Size(max = 100, message = "tamanho máximo de 100 caracteres")
	@Column(name="con_ds_endereco", length=100)
	private String dsEndereco;
	
	@Size(max = 100, message = "tamanho máximo de 100 caracteres")
	@Column(name="con_ds_bairro", length=100)
	private String dsBairro;
	
	@Size(max = 100, message = "tamanho máximo de 100 caracteres")
	@Column(name="con_ds_cidade", length=100)
	private String dsCidade;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="est_sq_estado", nullable=false)
	private Estado estado;
	
	@Size(max = 100, message = "tamanho máximo de 100 caracteres")
	@Column(name="con_ds_pais", length=50)
	private String dsPais;
	
	@Column(name="con_ds_cep", length=8)
	private String dsCep;
	
	@Column(name="con_ds_cnpj", length=14)
	private String dsCnpj;
	
	@Column(name="auditoria_data")
	private LocalDateTime auditoriaData;
	
	@Column(name="auditoria_usuario", length=50)
	private String auditoriaUsuario;

	public Long getSqConvencao() {
		return sqConvencao;
	}

	public void setSqConvencao(Long sqConvencao) {
		this.sqConvencao = sqConvencao;
	}

	public String getDsReduzido() {
		return dsReduzido;
	}

	public void setDsReduzido(String dsReduzido) {
		this.dsReduzido = dsReduzido;
	}

	public String getDsConvencao() {
		return dsConvencao;
	}

	public void setDsConvencao(String dsConvencao) {
		this.dsConvencao = dsConvencao;
	}

	public String getImLogo() {
		return imLogo;
	}

	public void setImLogo(String imLogo) {
		this.imLogo = imLogo;
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

	public Estado getEstado() {
		return estado;
	}

	public void setEstado(Estado estado) {
		this.estado = estado;
	}

	public String getDsPais() {
		return dsPais;
	}

	public void setDsPais(String dsPais) {
		this.dsPais = dsPais;
	}

	public String getDsCep() {
		return dsCep;
	}

	public void setDsCep(String dsCep) {
		this.dsCep = dsCep;
	}

	public String getDsCnpj() {
		return dsCnpj;
	}

	public void setDsCnpj(String dsCnpj) {
		this.dsCnpj = dsCnpj;
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
		result = prime * result + ((sqConvencao == null) ? 0 : sqConvencao.hashCode());
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
		Convencao other = (Convencao) obj;
		if (sqConvencao == null) {
			if (other.sqConvencao != null)
				return false;
		} else if (!sqConvencao.equals(other.sqConvencao))
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
		Convencao other = (Convencao) obj;
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
		if (dsConvencao == null) {
			if (other.dsConvencao != null)
				return false;
		} else if (!dsConvencao.equals(other.dsConvencao))
			return false;
		if (dsEndereco == null) {
			if (other.dsEndereco != null)
				return false;
		} else if (!dsEndereco.equals(other.dsEndereco))
			return false;
		if (dsPais == null) {
			if (other.dsPais != null)
				return false;
		} else if (!dsPais.equals(other.dsPais))
			return false;
		if (dsReduzido == null) {
			if (other.dsReduzido != null)
				return false;
		} else if (!dsReduzido.equals(other.dsReduzido))
			return false;
		if (estado == null) {
			if (other.estado != null)
				return false;
		} else if (!estado.equals(other.estado))
			return false;
		return true;
	}

}
