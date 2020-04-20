package br.com.convencao.bean.auditoria;

import java.io.Serializable;
import java.time.LocalDateTime;

public class AuditoriaFiltro implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private LocalDateTime dtInicioFiltro;
	private LocalDateTime dtFimFiltro;
	private String dsUsuarioFiltro;
	private String dsTabelaFiltro;
	private Integer cdCodigoPessoaFiltro;
	private String dsTipoFiltro;
	
	public LocalDateTime getDtInicioFiltro() {
		return dtInicioFiltro;
	}
	public void setDtInicioFiltro(LocalDateTime dtInicioFiltro) {
		this.dtInicioFiltro = dtInicioFiltro;
	}
	public LocalDateTime getDtFimFiltro() {
		return dtFimFiltro;
	}
	public void setDtFimFiltro(LocalDateTime dtFimFiltro) {
		this.dtFimFiltro = dtFimFiltro;
	}
	public String getDsUsuarioFiltro() {
		return dsUsuarioFiltro;
	}
	public void setDsUsuarioFiltro(String dsUsuarioFiltro) {
		this.dsUsuarioFiltro = dsUsuarioFiltro;
	}
	public String getDsTabelaFiltro() {
		return dsTabelaFiltro;
	}
	public void setDsTabelaFiltro(String dsTabelaFiltro) {
		this.dsTabelaFiltro = dsTabelaFiltro;
	}
	public Integer getCdCodigoPessoaFiltro() {
		return cdCodigoPessoaFiltro;
	}
	public void setCdCodigoPessoaFiltro(Integer cdCodigoPessoaFiltro) {
		this.cdCodigoPessoaFiltro = cdCodigoPessoaFiltro;
	}
	public String getDsTipoFiltro() {
		return dsTipoFiltro;
	}
	public void setDsTipoFiltro(String dsTipoFiltro) {
		this.dsTipoFiltro = dsTipoFiltro;
	}
	
	
	
}
