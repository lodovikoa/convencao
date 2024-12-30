package br.com.convencao.bean.codebehind;

import java.io.Serializable;
import java.time.LocalDate;

import javax.inject.Inject;

import br.com.convencao.bean.cadastro.RegiaoItens;
import br.com.convencao.model.Cargo;
import br.com.convencao.model.Departamento;
import br.com.convencao.model.Igreja;
import br.com.convencao.model.LancamentoResumo;
import br.com.convencao.model.Ministro;
import br.com.convencao.model.PlanoConta;

public class RelatorioFiltro implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private RegiaoItens regiaoItensFiltro;
	
	@Inject
	private RegiaoItens regiaoRecebimentoItensFiltro;
	
	@Inject
	private Departamento departamento;
	
	private Igreja igreja;
	private Cargo cargo;
	private Ministro ministro;
	private PlanoConta planoConta;
	private String[] tipoLancamentosSelecionadosString; 
	private LancamentoResumo lancamentoResumoFiltro;
	private int cdSituacaoMinistro;
	private boolean flBuscarAjax;
	private int cdOrdem;
	private LocalDate dtInicio;
	private LocalDate dtFim;
	private String tpRelatorio = "pdf";
	private boolean flConsiderarAnuidadeCorrente;
	
	public RegiaoItens getRegiaoItensFiltro() {
		return regiaoItensFiltro;
	}
	
	public void setRegiaoItensFiltro(RegiaoItens regiaoItensFiltro) {
		this.regiaoItensFiltro = regiaoItensFiltro;
	}
	
	public RegiaoItens getRegiaoRecebimentoItensFiltro() {
		return regiaoRecebimentoItensFiltro;
	}
	
	public void setRegiaoRecebimentoItensFiltro(RegiaoItens regiaoRecebimentoItensFiltro) {
		this.regiaoRecebimentoItensFiltro = regiaoRecebimentoItensFiltro;
	}
	
	public Departamento getDepartamento() {
		return departamento;
	}
	
	public void setDepartamento(Departamento departamento) {
		this.departamento = departamento;
	}
	
	public Igreja getIgreja() {
		return igreja;
	}
	
	public void setIgreja(Igreja igreja) {
		this.igreja = igreja;
	}
	
	public Cargo getCargo() {
		return cargo;
	}
	
	public void setCargo(Cargo cargo) {
		this.cargo = cargo;
	}
	
	public Ministro getMinistro() {
		return ministro;
	}
	
	public void setMinistro(Ministro ministro) {
		this.ministro = ministro;
	}
	
	public PlanoConta getPlanoConta() {
		return planoConta;
	}
	
	public void setPlanoConta(PlanoConta planoConta) {
		this.planoConta = planoConta;
	}
	
	public LancamentoResumo getLancamentoResumoFiltro() {
		return lancamentoResumoFiltro;
	}
	
	public void setLancamentoResumoFiltro(LancamentoResumo lancamentoResumoFiltro) {
		this.lancamentoResumoFiltro = lancamentoResumoFiltro;
	}
	
	public int getCdSituacaoMinistro() {
		return cdSituacaoMinistro;
	}
	
	public void setCdSituacaoMinistro(int cdSituacaoMinistro) {
		this.cdSituacaoMinistro = cdSituacaoMinistro;
	}
	
	public boolean isFlBuscarAjax() {
		return flBuscarAjax;
	}
	
	public void setFlBuscarAjax(boolean flBuscarAjax) {
		this.flBuscarAjax = flBuscarAjax;
	}
	
	public boolean isFlConsiderarAnuidadeCorrente() {
		return flConsiderarAnuidadeCorrente;
	}

	public void setFlConsiderarAnuidadeCorrente(boolean flConsiderarAnuidadeCorrente) {
		this.flConsiderarAnuidadeCorrente = flConsiderarAnuidadeCorrente;
	}

	public int getCdOrdem() {
		return cdOrdem;
	}

	public void setCdOrdem(int cdOrdem) {
		this.cdOrdem = cdOrdem;
	}

	public LocalDate getDtInicio() {
		return dtInicio;
	}
	
	public void setDtInicio(LocalDate dtInicio) {
		this.dtInicio = dtInicio;
	}
	
	public LocalDate getDtFim() {
		return dtFim;
	}
	
	public void setDtFim(LocalDate dtFim) {
		this.dtFim = dtFim;
	}
	
	public String getTpRelatorio() {
		if(!"xlsx".equals(this.tpRelatorio))
			this.tpRelatorio = "pdf";
		return tpRelatorio;
	}
	
	public void setTpRelatorio(String tpRelatorio) {
		this.tpRelatorio = tpRelatorio;
	}
	
	public String[] getTipoLancamentosSelecionadosString() {
		return tipoLancamentosSelecionadosString;
	}
	
	public void setTipoLancamentosSelecionadosString(String[] tipoLancamentosSelecionadosString) {
		this.tipoLancamentosSelecionadosString = tipoLancamentosSelecionadosString;
	}

}
