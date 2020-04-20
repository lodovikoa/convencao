package br.com.convencao.bean.cadastro;

import java.io.Serializable;
import java.time.LocalDate;

import javax.inject.Inject;

import br.com.convencao.model.Cargo;
import br.com.convencao.model.Departamento;
import br.com.convencao.model.Estado;
import br.com.convencao.model.Igreja;
import br.com.convencao.model.ProtocoloStatus;

public class MinistroFiltro implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private RegiaoItens regiaoItensFiltro;
	
	@Inject
	private Departamento departamentoFiltro;
	
	@Inject
	private Igreja igrejaFiltro;
	
	@Inject
	private Cargo cargoFiltro;
	
	@Inject
	private Estado estadoFiltro;
	
	@Inject
	private ProtocoloStatus protocoloStatusFiltro;
	
	private Long cdCodigoFiltro;
	private String nmMinistro;
	private String dsCpf;
	private Integer cdSituacaoFiltro = 1;
	private boolean flJubiladoFiltro;
	private LocalDate dtInicio1Filtro;
	private LocalDate dtFim1Filtro;
	private LocalDate dtInicio2Filtro;
	private LocalDate dtFim2Filtro;
	private String dsCidadeFiltro;
	private Integer nnOrdemExibicao;
	private Long cdReciboFiltro;
	private String tpRelatorio = "pdf";
	
	private Long cdProtocoloFiltro;

	
	public RegiaoItens getRegiaoItensFiltro() {
		return regiaoItensFiltro;
	}
	
	public void setRegiaoItensFiltro(RegiaoItens regiaoItensFiltro) {
		this.regiaoItensFiltro = regiaoItensFiltro;
	}
	
	public Departamento getDepartamentoFiltro() {
		return departamentoFiltro;
	}
	
	public void setDepartamentoFiltro(Departamento departamentoFiltro) {
		this.departamentoFiltro = departamentoFiltro;
	}
	
	public String getNmMinistro() {
		return nmMinistro;
	}
	
	public void setNmMinistro(String nmMinistro) {
		this.nmMinistro = nmMinistro;
	}
	
	public String getDsCpf() {
		return dsCpf;
	}
	
	public void setDsCpf(String dsCpf) {
		this.dsCpf = dsCpf;
	}
	
	public Igreja getIgrejaFiltro() {
		return igrejaFiltro;
	}
	
	public void setIgrejaFiltro(Igreja igrejaFiltro) {
		this.igrejaFiltro = igrejaFiltro;
	}
	
	public Cargo getCargoFiltro() {
		return cargoFiltro;
	}
	
	public void setCargoFiltro(Cargo cargoFiltro) {
		this.cargoFiltro = cargoFiltro;
	}
	
	public Estado getEstadoFiltro() {
		return estadoFiltro;
	}
	
	public void setEstadoFiltro(Estado estadoFiltro) {
		this.estadoFiltro = estadoFiltro;
	}
	
	public Long getCdCodigoFiltro() {
		return cdCodigoFiltro;
	}
	
	public void setCdCodigoFiltro(Long cdCodigoFiltro) {
		this.cdCodigoFiltro = cdCodigoFiltro;
	}
	
	public Integer getCdSituacaoFiltro() {
		return cdSituacaoFiltro;
	}
	
	public void setCdSituacaoFiltro(Integer cdSituacaoFiltro) {
		this.cdSituacaoFiltro = cdSituacaoFiltro;
	}
	public boolean getFlJubiladoFiltro() {
		return flJubiladoFiltro;
	}
	public void setFlJubiladoFiltro(boolean flJubiladoFiltro) {
		this.flJubiladoFiltro = flJubiladoFiltro;
	}
	
	public LocalDate getDtInicio1Filtro() {
		return dtInicio1Filtro;
	}
	
	public void setDtInicio1Filtro(LocalDate dtInicio1Filtro) {
		this.dtInicio1Filtro = dtInicio1Filtro;
	}
	
	public LocalDate getDtFim1Filtro() {
		return dtFim1Filtro;
	}
	
	public void setDtFim1Filtro(LocalDate dtFim1Filtro) {
		this.dtFim1Filtro = dtFim1Filtro;
	}
	
	public LocalDate getDtInicio2Filtro() {
		return dtInicio2Filtro;
	}
	
	public void setDtInicio2Filtro(LocalDate dtInicio2Filtro) {
		this.dtInicio2Filtro = dtInicio2Filtro;
	}
	
	public LocalDate getDtFim2Filtro() {
		return dtFim2Filtro;
	}
	
	public void setDtFim2Filtro(LocalDate dtFim2Filtro) {
		this.dtFim2Filtro = dtFim2Filtro;
	}
	
	public String getDsCidadeFiltro() {
		return dsCidadeFiltro;
	}
	
	public void setDsCidadeFiltro(String dsCidadeFiltro) {
		this.dsCidadeFiltro = dsCidadeFiltro;
	}
	
	public Integer getNnOrdemExibicao() {
		return nnOrdemExibicao;
	}
	
	public void setNnOrdemExibicao(Integer nnOrdemExibicao) {
		this.nnOrdemExibicao = nnOrdemExibicao;
	}
	
	public Long getCdReciboFiltro() {
		return cdReciboFiltro;
	}
	
	public void setCdReciboFiltro(Long cdReciboFiltro) {
		this.cdReciboFiltro = cdReciboFiltro;
	}
	
	public String getTpRelatorio() {
		if(!"xlsx".equals(this.tpRelatorio))
			this.tpRelatorio = "pdf";
		return tpRelatorio;
	}
	
	public void setTpRelatorio(String tpRelatorio) {
		this.tpRelatorio = tpRelatorio;
	}
	
	public Long getCdProtocoloFiltro() {
		return cdProtocoloFiltro;
	}
	
	public void setCdProtocoloFiltro(Long cdProtocoloFiltro) {
		this.cdProtocoloFiltro = cdProtocoloFiltro;
	}

	public ProtocoloStatus getProtocoloStatusFiltro() {
		return protocoloStatusFiltro;
	}
	
	public void setProtocoloStatusFiltro(ProtocoloStatus protocoloStatusFiltro) {
		this.protocoloStatusFiltro = protocoloStatusFiltro;
	}
}
