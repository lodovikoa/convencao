package br.com.convencao.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;

@Entity
@Table(name = "tb_ugr_grupo")
public class UGrupo implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "ugr_sq_grupo")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long sqGrupo;
	
	@Column(name = "ugr_is_situacao") 
	private boolean isSituacao;
	
	@Size(max = 30, message = "Tamanho máximo de 30 caracteres")
	@Column(name = "ugr_ds_nome", nullable = false, length = 30) 
	private String dsNome;

	public Long getSqGrupo() {
		return sqGrupo;
	}

	public void setSqGrupo(Long sqGrupo) {
		this.sqGrupo = sqGrupo;
	}

	public boolean isSituacao() {
		return isSituacao;
	}

	public void setSituacao(boolean isSituacao) {
		this.isSituacao = isSituacao;
	}

	public String getDsNome() {
		return dsNome;
	}

	public void setDsNome(String dsNome) {
		this.dsNome = dsNome;
	}
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((sqGrupo == null) ? 0 : sqGrupo.hashCode());
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
		UGrupo other = (UGrupo) obj;
		if (sqGrupo == null) {
			if (other.sqGrupo != null)
				return false;
		} else if (!sqGrupo.equals(other.sqGrupo))
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
		UGrupo other = (UGrupo) obj;
		if (dsNome == null) {
			if (other.dsNome != null)
				return false;
		} else if (!dsNome.equals(other.dsNome))
			return false;
		if (isSituacao != other.isSituacao)
			return false;
		return true;
	}
	
	
	

}
