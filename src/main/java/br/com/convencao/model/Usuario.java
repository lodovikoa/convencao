package br.com.convencao.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table(name = "tb_usu_usuario")
public class Usuario implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "usu_sq_usuario")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long sqUsuario;
	
	@NotBlank
	@Size(max = 50, message = "Tamanho máximo de 50 caracteres")
	@Column(name = "usu_ds_nome", nullable = false, length = 50) 
	private String dsNome;
	
	@NotBlank
	@Size(max = 15, message = "Tamanho máximo de 15 caracteres")
	@Column(name = "usu_ds_login", nullable = false, length = 15)
	private String dsLogin;
	
	@Column(name = "usu_ds_senha", nullable = false)
	private String dsSenha;
	
	@Size(max = 100, message = "Tamanho máximo de 100 caracteres")
	@Column(name = "usu_ds_email", nullable = false, length = 100)
	private String dsEmail;
	
	@Column(name = "usu_dt_cadastro")
	private LocalDate dtCadastro;
	
	@Column(name = "usu_is_situacao")
	private boolean isSituacao;
	
	@Column(name = "usu_ds_obs")
	private String dsObs;
	
	@Column(name = "usu_is_troca_senha")
	private boolean isTrocaSenha;
	
	@Column(name = "usu_nn_quantidade_erros")
	private Integer nnQuantidadeErros;
	
	@Column(name="auditoria_data")
	private LocalDateTime auditoriaData;
	
	@Column(name="auditoria_usuario", length=50)
	private String auditoriaUsuario;
	
	
	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "tb_uug_usuario_grupo", joinColumns = @JoinColumn(name = "usu_sq_usuario"), inverseJoinColumns = @JoinColumn(name = "ugr_sq_grupo"))
	private List<UGrupo> grupos = new ArrayList<>();
	
	@Transient
	private String dsSenhaConfirmarTroca;
	
	public Long getSqUsuario() {
		return sqUsuario;
	}


	public void setSqUsuario(Long sqUsuario) {
		this.sqUsuario = sqUsuario;
	}


	public String getDsNome() {
		return dsNome;
	}


	public void setDsNome(String dsNome) {
		this.dsNome = dsNome;
	}


	public String getDsLogin() {
		return dsLogin;
	}


	public void setDsLogin(String dsLogin) {
		this.dsLogin = dsLogin;
	}


	public String getDsSenha() {
		return dsSenha;
	}


	public void setDsSenha(String dsSenha) {
		this.dsSenha = dsSenha;
	}


	public String getDsEmail() {
		return dsEmail;
	}


	public void setDsEmail(String dsEmail) {
		this.dsEmail = dsEmail;
	}


	public LocalDate getDtCadastro() {
		return dtCadastro;
	}


	public void setDtCadastro(LocalDate dtCadastro) {
		this.dtCadastro = dtCadastro;
	}


	public boolean isSituacao() {
		return isSituacao;
	}


	public void setSituacao(boolean isSituacao) {
		this.isSituacao = isSituacao;
	}


	public String getDsObs() {
		return dsObs;
	}


	public void setDsObs(String dsObs) {
		this.dsObs = dsObs;
	}


	public boolean isTrocaSenha() {
		return isTrocaSenha;
	}


	public void setTrocaSenha(boolean isTrocaSenha) {
		this.isTrocaSenha = isTrocaSenha;
	}
	
	public List<UGrupo> getGrupos() {
		return grupos;
	}
	
	public void setGrupos(List<UGrupo> grupos) {
		this.grupos = grupos;
	}
	
	public LocalDateTime getAuditoriaData() {
		return auditoriaData;
	}
	
	public Integer getNnQuantidadeErros() {
		return nnQuantidadeErros;
	}
	
	public void setNnQuantidadeErros(Integer nnQuantidadeErros) {
		this.nnQuantidadeErros = nnQuantidadeErros;
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
	
	public String getDsSenhaConfirmarTroca() {
		return dsSenhaConfirmarTroca;
	}
	
	public void setDsSenhaConfirmarTroca(String dsSenhaConfirmarTroca) {
		this.dsSenhaConfirmarTroca = dsSenhaConfirmarTroca;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((sqUsuario == null) ? 0 : sqUsuario.hashCode());
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
		Usuario other = (Usuario) obj;
		if (sqUsuario == null) {
			if (other.sqUsuario != null)
				return false;
		} else if (!sqUsuario.equals(other.sqUsuario))
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
		Usuario other = (Usuario) obj;
		if (dsEmail == null) {
			if (other.dsEmail != null)
				return false;
		} else if (!dsEmail.equals(other.dsEmail))
			return false;
		if (dsLogin == null) {
			if (other.dsLogin != null)
				return false;
		} else if (!dsLogin.equals(other.dsLogin))
			return false;
		if (dsNome == null) {
			if (other.dsNome != null)
				return false;
		} else if (!dsNome.equals(other.dsNome))
			return false;
		if (dsObs == null) {
			if (other.dsObs != null)
				return false;
		} else if (!dsObs.equals(other.dsObs))
			return false;
		if (dsSenha == null) {
			if (other.dsSenha != null)
				return false;
		} else if (!dsSenha.equals(other.dsSenha))
			return false;
		if (dtCadastro == null) {
			if (other.dtCadastro != null)
				return false;
		} else if (!dtCadastro.equals(other.dtCadastro))
			return false;
		if (isSituacao != other.isSituacao)
			return false;
		if (isTrocaSenha != other.isTrocaSenha)
			return false;
		if (nnQuantidadeErros == null) {
			if (other.nnQuantidadeErros != null)
				return false;
		} else if (!nnQuantidadeErros.equals(other.nnQuantidadeErros))
			return false;
		return true;
	}

	public String toStringGruposAssociadosAoUsuario () {
		String result = "";
		if(this.grupos != null) {
			for (UGrupo g : this.grupos) {
				if(result.length() > 0) result = result + ", ";
				result = result + g.getDsNome();
			}
		}
		
		return result;
	}
}
