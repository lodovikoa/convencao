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
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="tb_prm_permissao")
public class UPermissao implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="prm_sq_permissao")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long sqPermissao;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="usu_sq_usuario", nullable=false)
	private Usuario usuario;

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "rgn_sq_regiao", nullable=false)
	private Regiao regiao; 
	
	@Column(name = "prm_fl_secretaria")
	private boolean flSecretaria; 
	
	@Column(name = "prm_fl_financeiro")
	private boolean flFinanceiro; 
	
	@Column(name = "prm_fl_pagamento")
	private boolean flPagamento; 
	
	@Column(name="auditoria_data")
	private LocalDateTime auditoriaData;
	
	@Column(name="auditoria_usuario", length=50)
	private String auditoriaUsuario;
	
	@Transient
	private boolean permissaoAtiva;

	public Long getSqPermissao() {
		return sqPermissao;
	}

	public void setSqPermissao(Long sqPermissao) {
		this.sqPermissao = sqPermissao;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Regiao getRegiao() {
		return regiao;
	}

	public void setRegiao(Regiao regiao) {
		this.regiao = regiao;
	}

	public boolean isFlSecretaria() {
		return flSecretaria;
	}

	public void setFlSecretaria(boolean flSecretaria) {
		this.flSecretaria = flSecretaria;
	}

	public boolean isFlFinanceiro() {
		return flFinanceiro;
	}

	public void setFlFinanceiro(boolean flFinanceiro) {
		this.flFinanceiro = flFinanceiro;
	}

	public boolean isFlPagamento() {
		return flPagamento;
	}

	public void setFlPagamento(boolean flPagamento) {
		this.flPagamento = flPagamento;
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
	
	public boolean isPermissaoAtiva() {
		return permissaoAtiva;
	}
	
	public void setPermissaoAtiva(boolean permissaoAtiva) {
		this.permissaoAtiva = permissaoAtiva;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((sqPermissao == null) ? 0 : sqPermissao.hashCode());
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
		UPermissao other = (UPermissao) obj;
		if (sqPermissao == null) {
			if (other.sqPermissao != null)
				return false;
		} else if (!sqPermissao.equals(other.sqPermissao))
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
		UPermissao other = (UPermissao) obj;
		if (flFinanceiro != other.flFinanceiro)
			return false;
		if (flPagamento != other.flPagamento)
			return false;
		if (flSecretaria != other.flSecretaria)
			return false;
		if (regiao == null) {
			if (other.regiao != null)
				return false;
		} else if (!regiao.equals(other.regiao))
			return false;
		if (sqPermissao == null) {
			if (other.sqPermissao != null)
				return false;
		} else if (!sqPermissao.equals(other.sqPermissao))
			return false;
		if (usuario == null) {
			if (other.usuario != null)
				return false;
		} else if (!usuario.equals(other.usuario))
			return false;
		return true;
	}

}
