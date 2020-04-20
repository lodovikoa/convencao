package br.com.convencao.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import br.com.convencao.util.permissao.Permissoes;

@Entity
@Table(name = "tb_min_ministro")
public class Ministro implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "min_sq_ministro")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long sqMinistro;
	
	@OneToMany(mappedBy = "ministro", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<MinistroAnexo> ministroAnexo = new ArrayList<>();
	
	@OneToMany(mappedBy = "ministro", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<MinistroParecer> ministroParecer = new ArrayList<>();

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "dpt_sq_departamento", nullable = false)
	private Departamento departamento;

	@Column(name = "min_cd_codigo")
	private Long cdCodigo;

	@Column(name = "min_nm_nome", nullable = false)
	private String nmNome;

	@Column(name = "min_fl_jubilado")
	private boolean flJubilado; 

	@Column(name = "min_dt_ingresso")
	private LocalDate dtIngresso; 

	@Column(name = "min_dt_cmvalidade")
	private LocalDate dtCmvalidade; 

	@Column(name = "min_dt_cmatualizada")
	private LocalDate dtCmatualizada; 

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cgo_sq_cargo", nullable = false)
	private Cargo cargo; 

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "esc_sq_escolaridade", nullable = false)
	private Escolaridade escolaridade; 

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "prf_sq_profissao")
	private Profissao profissao; 

	@Column(name = "min_dt_nascimento")
	private LocalDate dtNascimento; 

	@Column(name = "min_ds_local_nascimento")
	private String dsLocalNascimento; 

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = " civ_sq_estadocivel", nullable = false)
	private EstadoCivel estadoCivel; 

	@Column(name = "min_ds_identidade")
	private String dsIdentidade; 

	@Column(name = "min_ds_cpf")
	private String dsCpf; 

	@Column(name = "min_ds_endereco")
	private String dsEndereco; 

	@Column(name = "min_ds_bairro")
	private String dsBairro; 

	@Column(name = "min_ds_cep")
	private String dsCep; 

	@Column(name = "min_ds_cidade")
	private String dsCidade;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "est_sq_estado", nullable = false)
	private Estado estado;  

	@Column(name = "min_ds_telefone01")
	private String dsTelefone01;

	@Column(name = "min_ds_telefone02")
	private String dsTelefone02;

	@Column(name = "min_ds_telefone03")
	private String dsTelefone03;

	@Column(name = "min_dt_batismo")
	private LocalDate dtBatismo;

	@Column(name = "min_dt_ordenado")
	private LocalDate dtOrdenado;

	@Column(name = "min_nm_pai")
	private String nmPai;

	@Column(name = "min_nm_mae")
	private String nmMae;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "min_sq_ministro_conjuge", nullable = false)
	private Ministro conjuge;

	@Column(name = "min_dt_nascimento_esposa")
	private LocalDate dtNascimentoEsposa;

	@Column(name = "min_ds_email")
	private String dsEmail;

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "igr_sq_igreja", nullable = false)
	private Igreja igreja; 

	@Column(name = "min_fl_igrejasede")
	private boolean flIgrejasede;

	@Column(name = "min_cd_cgadb")
	private String cdCgadb;

	@Column(name = "min_ds_historico")
	private String dsHistorico;

	@Column(name = "min_dt_excluido")
	private LocalDate dtExcluido;

	@Column(name = "min_ds_foto")
	private String dsFoto;

	@Column(name = "auditoria_data")
	private LocalDateTime auditoriaData;

	@Column(name = "auditoria_usuario")
	private String auditoriaUsuario;

	@Transient
	private String dsJubilado;

	@Transient
	private boolean flInativo;

	@Transient
	private boolean flAtivo;

	@Transient
	private boolean flEditar;

	@Transient
	private boolean flExcluir;

	@Transient
	private boolean flDesativar;

	@Transient
	private boolean flReativar;

	@Transient
	private boolean flHistorico;

	@Transient
	private boolean flFichaCadastral;

	@Transient
	private boolean flSenhaProvisoria;

	@Transient
	private boolean flMinistro;
	
	@Transient
	private boolean flCandidato;

	@Transient
	private String dsHistoricoTemp;

	public Long getSqMinistro() {
		return sqMinistro;
	}

	public void setSqMinistro(Long sqMinistro) {
		this.sqMinistro = sqMinistro;
	}

	public List<MinistroAnexo> getMinistroAnexo() {
		return ministroAnexo;
	}

	public void setMinistroAnexo(List<MinistroAnexo> ministroAnexo) {
		this.ministroAnexo = ministroAnexo;
	}
	
	public List<MinistroParecer> getMinistroParecer() {
		return ministroParecer;
	}
	
	public void setMinistroParecer(List<MinistroParecer> ministroParecer) {
		this.ministroParecer = ministroParecer;
	}

	public Departamento getDepartamento() {
		return departamento;
	}

	public void setDepartamento(Departamento departamento) {
		this.departamento = departamento;
	}

	public Long getCdCodigo() {
		return cdCodigo;
	}

	public void setCdCodigo(Long cdCodigo) {
		this.cdCodigo = cdCodigo;
	}

	public String getNmNome() {
		return nmNome;
	}

	public void setNmNome(String nmNome) {
		this.nmNome = nmNome;
	}

	public boolean getFlJubilado() {
		return flJubilado;
	}

	public void setFlJubilado(boolean flJubilado) {
		this.flJubilado = flJubilado;
	}

	public LocalDate getDtIngresso() {
		return dtIngresso;
	}

	public void setDtIngresso(LocalDate dtIngresso) {
		this.dtIngresso = dtIngresso;
	}

	public LocalDate getDtCmvalidade() {
		return dtCmvalidade;
	}

	public void setDtCmvalidade(LocalDate dtCmvalidade) {
		this.dtCmvalidade = dtCmvalidade;
	}

	public LocalDate getDtCmatualizada() {
		return dtCmatualizada;
	}

	public void setDtCmatualizada(LocalDate dtCmatualizada) {
		this.dtCmatualizada = dtCmatualizada;
	}

	public Cargo getCargo() {
		return cargo;
	}

	public void setCargo(Cargo cargo) {
		this.cargo = cargo;
	}

	public Escolaridade getEscolaridade() {
		return escolaridade;
	}

	public void setEscolaridade(Escolaridade escolaridade) {
		this.escolaridade = escolaridade;
	}

	public Profissao getProfissao() {
		return profissao;
	}

	public void setProfissao(Profissao profissao) {
		this.profissao = profissao;
	}

	public LocalDate getDtNascimento() {
		return dtNascimento;
	}

	public void setDtNascimento(LocalDate dtNascimento) {
		this.dtNascimento = dtNascimento;
	}

	public String getDsLocalNascimento() {
		return dsLocalNascimento;
	}

	public void setDsLocalNascimento(String dsLocalNascimento) {
		this.dsLocalNascimento = dsLocalNascimento;
	}

	public EstadoCivel getEstadoCivel() {
		return estadoCivel;
	}

	public void setEstadoCivel(EstadoCivel estadoCivel) {
		this.estadoCivel = estadoCivel;
	}

	public String getDsIdentidade() {
		return dsIdentidade;
	}

	public void setDsIdentidade(String dsIdentidade) {
		this.dsIdentidade = dsIdentidade;
	}

	public String getDsCpf() {
		return dsCpf;
	}

	public void setDsCpf(String dsCpf) {
		this.dsCpf = dsCpf;
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

	public String getDsCep() {
		return dsCep;
	}

	public void setDsCep(String dsCep) {
		this.dsCep = dsCep;
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

	public String getDsTelefone01() {
		return dsTelefone01;
	}

	public void setDsTelefone01(String dsTelefone01) {
		this.dsTelefone01 = dsTelefone01;
	}

	public String getDsTelefone02() {
		return dsTelefone02;
	}

	public void setDsTelefone02(String dsTelefone02) {
		this.dsTelefone02 = dsTelefone02;
	}

	public String getDsTelefone03() {
		return dsTelefone03;
	}

	public void setDsTelefone03(String dsTelefone03) {
		this.dsTelefone03 = dsTelefone03;
	}

	public LocalDate getDtOrdenado() {
		return dtOrdenado;
	}

	public void setDtOrdenado(LocalDate dtOrdenado) {
		this.dtOrdenado = dtOrdenado;
	}

	public LocalDate getDtBatismo() {
		return dtBatismo;
	}

	public void setDtBatismo(LocalDate dtBatismo) {
		this.dtBatismo = dtBatismo;
	}

	public String getNmPai() {
		return nmPai;
	}

	public void setNmPai(String nmPai) {
		this.nmPai = nmPai;
	}

	public String getNmMae() {
		return nmMae;
	}

	public void setNmMae(String nmMae) {
		this.nmMae = nmMae;
	}

	public Ministro getConjuge() {
		return conjuge;
	}

	public void setConjuge(Ministro conjuge) {
		this.conjuge = conjuge;
	}

	public LocalDate getDtNascimentoEsposa() {
		return dtNascimentoEsposa;
	}

	public void setDtNascimentoEsposa(LocalDate dtNascimentoEsposa) {
		this.dtNascimentoEsposa = dtNascimentoEsposa;
	}

	public String getDsEmail() {
		return dsEmail;
	}

	public void setDsEmail(String dsEmail) {
		this.dsEmail = dsEmail;
	}

	public Igreja getIgreja() {
		return igreja;
	}

	public void setIgreja(Igreja igreja) {
		this.igreja = igreja;
	}

	public boolean getFlIgrejasede() {
		return flIgrejasede;
	}

	public void setFlIgrejasede(boolean flIgrejasede) {
		this.flIgrejasede = flIgrejasede;
	}

	public String getCdCgadb() {
		return cdCgadb;
	}

	public void setCdCgadb(String cdCgadb) {
		this.cdCgadb = cdCgadb;
	}

	public String getDsHistorico() {
		return dsHistorico;
	}

	public void setDsHistorico(String dsHistorico) {
		this.dsHistorico = dsHistorico;
	}

	public LocalDate getDtExcluido() {
		return dtExcluido;
	}

	public void setDtExcluido(LocalDate dtExcluido) {
		this.dtExcluido = dtExcluido;
	}

	public String getDsFoto() {
		return dsFoto;
	}

	public void setDsFoto(String dsFoto) {
		this.dsFoto = dsFoto;
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

	// Transient - Não corresponde a um campo de bd
	public String getDsJubilado() {
		return dsJubilado = (this.flJubilado? "Jubilado": null);
	}

	// Transient - Não corresponde a um campo de bd
	public boolean getFlInativo() {
		return flInativo = this.getFlMinistro() && this.getDtExcluido() != null;
	}

	public boolean getFlAtivo() {
		return flAtivo = this.getFlMinistro() && this.getDtExcluido() == null;
	}

	public boolean getFlEditar() {
		return flEditar = ((!this.getFlMinistro() && !this.getFlCandidato()) || (this.getFlMinistro() && this.getFlAtivo())) && Permissoes.getPermissaoInserirEditarMinistro();
	}

	public boolean getFlExcluir() {
		return flExcluir = !this.getFlMinistro() && !this.getFlCandidato() && Permissoes.getPermissaoInserirEditarMinistro();
	}

	public boolean getFlMinistro() {
		return flMinistro = this.departamento.getSqDepartamento() == 1;
	}
	
	public boolean getFlCandidato() {
		return flCandidato = this.departamento.getSqDepartamento() == 5;
	}

	public boolean getFlDesativar() {
		return flDesativar = this.getFlAtivo() && Permissoes.getPermissaoInserirEditarMinistro();
	}

	public boolean getFlReativar() {
		return flReativar = this.getFlInativo() && Permissoes.getPermissaoInserirEditarMinistro();
	}

	public boolean getFlHistorico() {
		return flHistorico = this.flMinistro && this.flAtivo  && Permissoes.getPermissaoInserirEditarMinistro();
	}


	public boolean getFlFichaCadastral() {
		return flFichaCadastral = this.flMinistro;
	}

	public boolean getFlSenhaProvisoria() {
		return flSenhaProvisoria = this.flMinistro && this.flAtivo  && Permissoes.getPermissaoInserirEditarMinistro();
	}

	public String getDsHistoricoTemp() {
		return dsHistoricoTemp;
	}

	public void setDsHistoricoTemp(String dsHistoricoTemp) {
		this.dsHistoricoTemp = dsHistoricoTemp;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((sqMinistro == null) ? 0 : sqMinistro.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		Long sqTemp = -1L;
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass()) {
			if(!((Ministro) obj).getSqMinistro().equals(this.getSqMinistro())) {
				return false;
			} else {
				sqTemp = ((Ministro) obj).getSqMinistro();
			}
		}

		Ministro other = (Ministro) obj;
		if (sqMinistro == null) {
			if (other.sqMinistro != null)
				return false;
		} else if (!sqMinistro.equals(other.sqMinistro)) {
			if(!sqMinistro.equals(sqTemp))
				return false;
		}
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
		Ministro other = (Ministro) obj;

		if (cargo == null) {
			if (other.cargo != null)
				return false;
		} else if (!cargo.equals(other.cargo))
			return false;
		if (cdCgadb == null) {
			if (other.cdCgadb != null)
				return false;
		} else if (!cdCgadb.equals(other.cdCgadb))
			return false;
		if (cdCodigo == null) {
			if (other.cdCodigo != null)
				return false;
		} else if (!cdCodigo.equals(other.cdCodigo))
			return false;
		if (conjuge == null) {
			if (other.conjuge != null)
				return false;
		} else if (!conjuge.equals(other.conjuge))
			return false;
		if (departamento == null) {
			if (other.departamento != null)
				return false;
		} else if (!departamento.equals(other.departamento))
			return false;
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
		if (dsCpf == null) {
			if (other.dsCpf != null)
				return false;
		} else if (!dsCpf.equals(other.dsCpf))
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
		if (dsHistorico == null) {
			if (other.dsHistorico != null)
				return false;
		} else if (!dsHistorico.equals(other.dsHistorico))
			return false;
		if (dsIdentidade == null) {
			if (other.dsIdentidade != null)
				return false;
		} else if (!dsIdentidade.equals(other.dsIdentidade))
			return false;
		if (dsJubilado == null) {
			if (other.dsJubilado != null)
				return false;
		} else if (!dsJubilado.equals(other.dsJubilado))
			return false;
		if (dsLocalNascimento == null) {
			if (other.dsLocalNascimento != null)
				return false;
		} else if (!dsLocalNascimento.equals(other.dsLocalNascimento))
			return false;
		if (dsTelefone01 == null) {
			if (other.dsTelefone01 != null)
				return false;
		} else if (!dsTelefone01.equals(other.dsTelefone01))
			return false;
		if (dsTelefone02 == null) {
			if (other.dsTelefone02 != null)
				return false;
		} else if (!dsTelefone02.equals(other.dsTelefone02))
			return false;
		if (dsTelefone03 == null) {
			if (other.dsTelefone03 != null)
				return false;
		} else if (!dsTelefone03.equals(other.dsTelefone03))
			return false;
		if (dtBatismo == null) {
			if (other.dtBatismo != null)
				return false;
		} else if (!dtBatismo.equals(other.dtBatismo))
			return false;
		if (dtCmatualizada == null) {
			if (other.dtCmatualizada != null)
				return false;
		} else if (!dtCmatualizada.equals(other.dtCmatualizada))
			return false;
		if (dtCmvalidade == null) {
			if (other.dtCmvalidade != null)
				return false;
		} else if (!dtCmvalidade.equals(other.dtCmvalidade))
			return false;
		if (dtExcluido == null) {
			if (other.dtExcluido != null)
				return false;
		} else if (!dtExcluido.equals(other.dtExcluido))
			return false;
		if (dtIngresso == null) {
			if (other.dtIngresso != null)
				return false;
		} else if (!dtIngresso.equals(other.dtIngresso))
			return false;
		if (dtNascimento == null) {
			if (other.dtNascimento != null)
				return false;
		} else if (!dtNascimento.equals(other.dtNascimento))
			return false;
		if (dtNascimentoEsposa == null) {
			if (other.dtNascimentoEsposa != null)
				return false;
		} else if (!dtNascimentoEsposa.equals(other.dtNascimentoEsposa))
			return false;
		if (dtOrdenado == null) {
			if (other.dtOrdenado != null)
				return false;
		} else if (!dtOrdenado.equals(other.dtOrdenado))
			return false;
		if (escolaridade == null) {
			if (other.escolaridade != null)
				return false;
		} else if (!escolaridade.equals(other.escolaridade))
			return false;
		if (estado == null) {
			if (other.estado != null)
				return false;
		} else if (!estado.equals(other.estado))
			return false;
		if (estadoCivel == null) {
			if (other.estadoCivel != null)
				return false;
		} else if (!estadoCivel.equals(other.estadoCivel))
			return false;
		if (igreja == null) {
			if (other.igreja != null)
				return false;
		} else if (!igreja.equals(other.igreja))
			return false;
		if (nmMae == null) {
			if (other.nmMae != null)
				return false;
		} else if (!nmMae.equals(other.nmMae))
			return false;
		if (nmNome == null) {
			if (other.nmNome != null)
				return false;
		} else if (!nmNome.equals(other.nmNome))
			return false;
		if (nmPai == null) {
			if (other.nmPai != null)
				return false;
		} else if (!nmPai.equals(other.nmPai))
			return false;
		if (profissao == null) {
			if (other.profissao != null)
				return false;
		} else if (!profissao.equals(other.profissao))
			return false;
		if (flIgrejasede != other.flIgrejasede)
			return false;
		if (flJubilado != other.flJubilado)
			return false;
		if (dsFoto == null) {
			if (other.dsFoto != null)
				return false;
		} else if (!dsFoto.equals(other.dsFoto))
			return false;

		return true;
	}


}
