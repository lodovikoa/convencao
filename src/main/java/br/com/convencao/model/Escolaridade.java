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

import org.hibernate.validator.constraints.NotBlank;

@Entity
@Table(name = "tb_esc_escolaridade")
public class Escolaridade implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "esc_sq_escolaridade")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long sqEscolaridade;
	
	@NotBlank
	@Size(max = 40, message = "tamanho máximo de 40 caracteres")
	@Column(name = "esc_ds_descricao", nullable = false, length = 40)
	private String dsDescricao;
	
	@Column(name="auditoria_data")
	private LocalDateTime auditoriaData;
	
	@Column(name="auditoria_usuario", length=50)
	private String auditoriaUsuario;

	public Long getSqEscolaridade() {
		return sqEscolaridade;
	}

	public void setSqEscolaridade(Long sqEscolaridade) {
		this.sqEscolaridade = sqEscolaridade;
	}

	public String getDsDescricao() {
		return dsDescricao;
	}

	public void setDsDescricao(String dsDescricao) {
		this.dsDescricao = dsDescricao;
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
		result = prime * result + ((sqEscolaridade == null) ? 0 : sqEscolaridade.hashCode());
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
		Escolaridade other = (Escolaridade) obj;
		if (sqEscolaridade == null) {
			if (other.sqEscolaridade != null)
				return false;
		} else if (!sqEscolaridade.equals(other.sqEscolaridade))
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
		Escolaridade other = (Escolaridade) obj;
		if (dsDescricao == null) {
			if (other.dsDescricao != null)
				return false;
		} else if (!dsDescricao.equals(other.dsDescricao))
			return false;
		return true;
	}


	
}
