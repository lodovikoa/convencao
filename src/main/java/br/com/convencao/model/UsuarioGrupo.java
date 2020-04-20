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

@Entity
@Table(name = "tb_uug_usuario_grupo")
public class UsuarioGrupo implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "uug_sq_usuario_grupo")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long sqUsuarioGrupo;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="usu_sq_usuario", nullable=false)
	private Usuario usuario;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="ugr_sq_grupo", nullable=false)
	private UGrupo grupo ;
	
	@Column(name="auditoria_data")
	private LocalDateTime auditoriaData;
	
	@Column(name="auditoria_usuario", length=50)
	private String auditoriaUsuario;

	public Long getSqUsuarioGrupo() {
		return sqUsuarioGrupo;
	}

	public void setSqUsuarioGrupo(Long sqUsuarioGrupo) {
		this.sqUsuarioGrupo = sqUsuarioGrupo;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public UGrupo getGrupo() {
		return grupo;
	}

	public void setGrupo(UGrupo grupo) {
		this.grupo = grupo;
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
		result = prime * result + ((sqUsuarioGrupo == null) ? 0 : sqUsuarioGrupo.hashCode());
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
		UsuarioGrupo other = (UsuarioGrupo) obj;
		if (sqUsuarioGrupo == null) {
			if (other.sqUsuarioGrupo != null)
				return false;
		} else if (!sqUsuarioGrupo.equals(other.sqUsuarioGrupo))
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
		UsuarioGrupo other = (UsuarioGrupo) obj;
		if (grupo == null) {
			if (other.grupo != null)
				return false;
		} else if (!grupo.equals(other.grupo))
			return false;
		if (usuario == null) {
			if (other.usuario != null)
				return false;
		} else if (!usuario.equals(other.usuario))
			return false;
		return true;
	}
	
	

}
