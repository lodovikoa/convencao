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

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "tb_esc_escolaridade")
@EqualsAndHashCode
@NoArgsConstructor
@ToString
public class Escolaridade implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "esc_sq_escolaridade")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long sqEscolaridade;
	
	@Size(min = 2,  max = 40, message = "tamanho minimo de 2 e máximo de 40 caracteres")
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
}
