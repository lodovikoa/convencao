package br.com.convencao.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tb_aud_auditoria")
public class Auditoria implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "aud_sq_auditoria")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long sqAuditoria;
	
	@Column(name = "aud_sq_tabela") 
	private Long sqTabela;
	
	@Column(name = "aud_cd_codigo_pessoa")
	private Integer cdCodigoPessoa;
	
	@Column(name = "aud_sq_Ministro")
	private Long sqMinistro;
	
	@Column(name = "aud_ds_tipo")
	private String dsTipo;
	
	@Column(name = "aud_ds_tabela")
	private String dsTabela;
	
	@Column(name = "aud_ds_usuario")
	private String dsUsuario;
	

	@Column(name="aud_dt_data_alteracao")
	private LocalDateTime dtDataAlteracao;
	
	@Column(name="aud_ds_valor_anterior")
	private String dsValorAnterior;
	
	@Column(name="aud_ds_valor_atual")
	private String dsValorAtual;

	public Long getSqAuditoria() {
		return sqAuditoria;
	}

	public void setSqAuditoria(Long sqAuditoria) {
		this.sqAuditoria = sqAuditoria;
	}

	public Long getSqTabela() {
		return sqTabela;
	}
	
	public void setSqTabela(Long sqTabela) {
		this.sqTabela = sqTabela;
	}

	public Integer getCdCodigoPessoa() {
		return cdCodigoPessoa;
	}

	public void setCdCodigoPessoa(Integer cdCodigoPessoa) {
		this.cdCodigoPessoa = cdCodigoPessoa;
	}

	public Long getSqMinistro() {
		return sqMinistro;
	}

	public void setSqMinistro(Long sqMinistro) {
		this.sqMinistro = sqMinistro;
	}

	public String getDsTipo() {
		return dsTipo;
	}

	public void setDsTipo(String dsTipo) {
		this.dsTipo = dsTipo;
	}

	public String getDsTabela() {
		return dsTabela;
	}

	public void setDsTabela(String dsTabela) {
		this.dsTabela = dsTabela;
	}

	public String getDsUsuario() {
		return dsUsuario;
	}

	public void setDsUsuario(String dsUsuario) {
		this.dsUsuario = dsUsuario;
	}

	public LocalDateTime getDtDataAlteracao() {
		return dtDataAlteracao;
	}

	public void setDtDataAlteracao(LocalDateTime dtDataAlteracao) {
		this.dtDataAlteracao = dtDataAlteracao;
	}

	public String getDsValorAnterior() {
		return dsValorAnterior;
	}

	public void setDsValorAnterior(String dsValorAnterior) {
		this.dsValorAnterior = dsValorAnterior;
	}

	public String getDsValorAtual() {
		return dsValorAtual;
	}

	public void setDsValorAtual(String dsValorAtual) {
		this.dsValorAtual = dsValorAtual;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((sqAuditoria == null) ? 0 : sqAuditoria.hashCode());
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
		Auditoria other = (Auditoria) obj;
		if (sqAuditoria == null) {
			if (other.sqAuditoria != null)
				return false;
		} else if (!sqAuditoria.equals(other.sqAuditoria))
			return false;
		return true;
	}
	
}
