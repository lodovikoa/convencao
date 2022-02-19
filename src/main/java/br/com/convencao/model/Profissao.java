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
@Table(name = "tb_prf_profissao")
@EqualsAndHashCode
@NoArgsConstructor
@ToString
public class Profissao implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "prf_sq_profissao")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long sqProfissao;
	
	@Size(min = 2, max = 40, message = "tamanho minimo de 2 e máximo de 40 caracteres")
	@Column(name = "prf_ds_descricao", nullable = false, length = 40)
	private String dsDescricao;
	
	@Column(name="auditoria_data")
	private LocalDateTime auditoriaData;
	
	@Column(name="auditoria_usuario", length=50)
	private String auditoriaUsuario;

	public Long getSqProfissao() {
		return sqProfissao;
	}

	public void setSqProfissao(Long sqProfissao) {
		this.sqProfissao = sqProfissao;
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
