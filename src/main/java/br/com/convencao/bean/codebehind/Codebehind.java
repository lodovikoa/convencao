package br.com.convencao.bean.codebehind;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.primefaces.context.PrimeRequestContext;

import br.com.convencao.bean.cadastro.MinistroFiltro;
import br.com.convencao.bean.cadastro.MinistroListarTO;
import br.com.convencao.bean.cadastro.RegiaoItens;
import br.com.convencao.bo.CargoBO;
import br.com.convencao.bo.DepartamentoBO;
import br.com.convencao.bo.EscolaridadeBO;
import br.com.convencao.bo.EstadoBO;
import br.com.convencao.bo.EstadoCivelBO;
import br.com.convencao.bo.FormaPagamentoBO;
import br.com.convencao.bo.FormaRecebimentoBO;
import br.com.convencao.bo.IgrejaBO;
import br.com.convencao.bo.MinistroBO;
import br.com.convencao.bo.PlanoContaBO;
import br.com.convencao.bo.ProfissaoBO;
import br.com.convencao.bo.RegiaoBO;
import br.com.convencao.bo.TipoLancamentoBO;
import br.com.convencao.bo.UPermissaoBO;
import br.com.convencao.model.Cargo;
import br.com.convencao.model.Departamento;
import br.com.convencao.model.Escolaridade;
import br.com.convencao.model.Estado;
import br.com.convencao.model.EstadoCivel;
import br.com.convencao.model.FormaPagamento;
import br.com.convencao.model.Igreja;
import br.com.convencao.model.Ministro;
import br.com.convencao.model.PlanoConta;
import br.com.convencao.model.Profissao;
import br.com.convencao.model.Regiao;
import br.com.convencao.model.TipoLancamento;
import br.com.convencao.model.TipoPagamento;
import br.com.convencao.model.UPermissao;
import br.com.convencao.model.to.FormaRecebimentoPorReciboCpl;
import br.com.convencao.util.Uteis;
import br.com.convencao.util.permissao.Permissoes;

public class Codebehind implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private List<Estado> estados;
	private List<RegiaoItens> regiaoItens;
	private List<RegiaoItens> regiaoRecebimentoItens;
	private List<Regiao> regioes;
	private List<Departamento> departamentos;
	private List<Cargo> cargos;
	private List<Igreja> igrejas;
	private List<MinistroListarTO> ministroListarTOList;
	private List<UPermissao> permissoesRegiaoPorUsuario;
	private List<PlanoConta> planoContaList;
	private List<FormaRecebimentoPorReciboCpl> formaRecebimentoPorReciboCpl;
	private List<FormaPagamento> formaPagamento;
	private List<TipoLancamento> tipoLancamentoList;

	private List<EstadoCivel> estadoCiveis;
	private List<Escolaridade> escolaridades;
	private List<Profissao> profissoes;
	private List<Ministro> esposasMinistro;
	
	private List<TipoLancamento> tipoLancamentosList;
	private List<String> tipoLancamentosString;

	@Inject
	private RelatorioFiltro relatorioFiltro;	

	@Inject
	private EstadoBO estadoBO;

	@Inject
	private RegiaoBO regiaoBO;

	@Inject
	private DepartamentoBO departamentoBO;

	@Inject
	private CargoBO cargoBO;
	
	@Inject
	private EstadoCivelBO estadoCivelBO;

	@Inject
	private EscolaridadeBO escolaridaddeBO;

	@Inject
	private ProfissaoBO profissaoBO;

	@Inject
	private IgrejaBO igrejaBO;

	@Inject
	private UPermissaoBO permissaoBO;

	@Inject
	private PlanoContaBO planoContaBO;

	@Inject
	private TipoLancamentoBO tipoLancamentoBO;

	@Inject
	private FormaRecebimentoBO formaRecebimentoBO;

	@Inject
	private FormaPagamentoBO formaPagamentoBO;

	@Inject
	private MinistroFiltro filtro;
	
	@Inject
	private MinistroBO ministroBO;

	private String dsConvencao;

	public void inicializarEstados(){
		this.estados =  estadoBO.findAllEstados();
	}

	// tpRegiao = "SEC" para secretaria
	// tpRegiao = "FIN" para financeiro
	// tpRegiao = "PAG" para pagamento
	// tpRegiao = "TUD" Todas as regiões independente do usuário
	public int inicializarRegioes(String tpRegiao){

		this.regiaoItens = new ArrayList<>();
		RegiaoItens rgItem = new RegiaoItens();

		List<Long> sqRegioesPermissoes = this.regioesLiberadasUsuario(tpRegiao);

		if(sqRegioesPermissoes.size() > 0)
			this.regioes = regiaoBO.findAllPermitidoPorUsuario(sqRegioesPermissoes);


		if(!tpRegiao.equals(Uteis._TUDo) && this.permissoesRegiaoPorUsuario.size() == regiaoBO.findQtdeRegiao()) {
			this.dsConvencao = this.regioes.get(0).getConvencao().getDsReduzido();
			rgItem.setSqRegiao(this.regioes.get(0).getConvencao().getSqConvencao() * -1);
			rgItem.setDsRegiao(this.dsConvencao);

			this.regiaoItens.add(rgItem);
		}



		if(this.regioes !=null) {
			for (Regiao item : this.regioes) {
				rgItem = new RegiaoItens();
				rgItem.setSqRegiao(item.getSqRegiao());
				rgItem.setDsRegiao(item.getDsRegiao() + " - " + item.getConvencao().getDsReduzido());

				this.regiaoItens.add(rgItem);
			}
		}

		return this.regiaoItens.size();
	}


	// tpRegiao = "PAG" para pagamento
	// Sobrecarregar inicializarRegioes para quando a região é de pagamento
	public int inicializarRegioes(String tpRegiao, boolean flPagametno){

		this.regiaoRecebimentoItens = new ArrayList<>();
		RegiaoItens rgItem = new RegiaoItens();

		List<Long> sqRegioesPermissoes = this.regioesLiberadasUsuario(tpRegiao);

		if(sqRegioesPermissoes.size() > 0)
			this.regioes = regiaoBO.findAllPermitidoPorUsuario(sqRegioesPermissoes);


		if(!tpRegiao.equals(Uteis._TUDo) && this.permissoesRegiaoPorUsuario.size() == regiaoBO.findQtdeRegiao()) {
			this.dsConvencao = this.regioes.get(0).getConvencao().getDsReduzido();
			rgItem.setSqRegiao(this.regioes.get(0).getConvencao().getSqConvencao() * -1);
			rgItem.setDsRegiao(this.dsConvencao);

			this.regiaoRecebimentoItens.add(rgItem);
		}



		if(this.regioes !=null) {
			for (Regiao item : this.regioes) {
				rgItem = new RegiaoItens();
				rgItem.setSqRegiao(item.getSqRegiao());
				rgItem.setDsRegiao(item.getDsRegiao() + " - " + item.getConvencao().getDsReduzido());

				this.regiaoRecebimentoItens.add(rgItem);
			}
		}

		return this.regiaoRecebimentoItens.size();
	}


	// Busca as regioes liberadas para o usuário logado
	private List<Long> regioesLiberadasUsuario(String tpRegiao) {

		// Lista para montar as regiões que o usuário tem permissao
		List<Long> sqRegioesPermissoes = new ArrayList<>();

		// Buscar todas as regiões
		if(tpRegiao.equals(Uteis._TUDo)) {
			this.regioes = regiaoBO.findAll();	
		} else {
			this.permissoesRegiaoPorUsuario = permissaoBO.findAllPorUsuarioTpRegiao(Uteis.UsuarioLogado().getUsuario().getSqUsuario(), tpRegiao);

			// Montar as regioes para busca-las no bd
			for (UPermissao uPermissao : this.permissoesRegiaoPorUsuario) {
				sqRegioesPermissoes.add(uPermissao.getRegiao().getSqRegiao());
			}
		}

		return sqRegioesPermissoes;

	}


	public void inicializarDepartamentos(String tipo){
		
		// Se for cadastro de candidato busca somente o departamento "Candidatos"
		if(tipo.equalsIgnoreCase("cadastrarCandidato") || tipo.equalsIgnoreCase("alterarCandidato")) {
			this.departamentos = new ArrayList<Departamento>();
			this.departamentos.add(departamentoBO.find(5L)); // sqDepartamento 5 = CANDIDATOS
			
		// Se for cadastro de Ministro, remove os departamentos CONFRATERES e CANDIDADO
		} else if(tipo.equalsIgnoreCase("cadastro")) {
			this.departamentos = departamentoBO.findAll();
			for (int i = 0; i < this.departamentos.size(); i++) {
				if(departamentos.get(i).getSqDepartamento().equals(1L))  // sqDepartamento 1 = CONFRATERES
					this.departamentos.remove(departamentos.get(i));
				if(departamentos.get(i).getSqDepartamento().equals(5L))  // sqDepartamento 5 = CANDIDATOS
					this.departamentos.remove(departamentos.get(i));
			}
			
		// Se for cadastro de Saídas, ou consultas onde precisa exibir todos oos departamentos, exibe todos os departamentos
		} else if (tipo.equalsIgnoreCase("cadastroSaidas") || (tipo.equalsIgnoreCase("consultasInternas"))) {
			this.departamentos = departamentoBO.findAll();
			
		// Se não cair em nenhuma condição anterior, exibe todos os departamentos e 
		// verifica se o usuário logado tem permissão para visualizar o departamento CONFRATEES
		// Caso não tenha permissão faz a exclusão do departamento CONFRATERES da lista e departamentos
		} else {
			this.departamentos = departamentoBO.findAll();

			// Verificar se a pessoa está em grupo que permite mostrar o departamento Confrateres nas pesquisas (consultas)		
			if(!Permissoes.getPermissaoDepartamentoConfrateres(tipo)) {
				for (int i = 0; i < this.departamentos.size(); i++) {	
					if(departamentos.get(i).getSqDepartamento().equals(1L)) {  // sqDepartamento 1 = CONFRATERES
						this.departamentos.remove(departamentos.get(i));
						break;
					}
				}

			}
		}
	}

	public void inicializarCargos(){
		this.cargos = cargoBO.findAll();
	}
	

	public void inicializarPlanoContasPorTipo (String tpConta) {
		this.planoContaList = this.planoContaBO.findAllPorTipo(tpConta);
	}

	public void inicializarTipoLancamentoPorTipo(String tpConta) {
		this.tipoLancamentoList = this.tipoLancamentoBO.findAllTipoLancamentoPorTipo(tpConta);
	}

	public void inicializarFormaRecebimentoPorRecibo(Long sqRecibo, boolean flSoComValores) {
		this.formaRecebimentoPorReciboCpl = formaRecebimentoBO.findAllReciboCplPorSqRecibo(sqRecibo, flSoComValores);
	}

	public void inicializarFormaPagamento() {
		this.formaPagamento = formaPagamentoBO.findAllOrdenado();
	}

	public void inicializarIgrejas(Long sqRegiao){
		if(sqRegiao != null){
			Regiao regiao = new Regiao();
			regiao.setSqRegiao(sqRegiao);

			// -l será utilizado para buscar todas as regiões que o usuário logado tem permissao.
			if(regiao.getSqRegiao() == -1) {
				List<Long> sqRegioesPermissoes = this.regioesLiberadasUsuario(Uteis._SECretaria);
				this.igrejas = this.igrejaBO.findAllPorRegiaoLiberadasUsuario(sqRegioesPermissoes);
			}else {
				this.igrejas = igrejaBO.findAllPorRegiao(regiao);
			}			

//			if(flInverter){
//				for (int i = 0; i < this.igrejas.size(); i++ ) {
//					if(StringUtils.isNotBlank(this.igrejas.get(i).getDsIgreja())){
//						int nnTam = this.igrejas.get(i).getDsIgreja().toUpperCase().indexOf("ASSEMBLEIA DE DEUS");
//						if(nnTam == -1){
//							nnTam = this.igrejas.get(i).getDsIgreja().toUpperCase().indexOf("ASSEMBLÉIA DE DEUS");
//						}
//
//						if(nnTam == 0 && this.igrejas.get(i).getDsIgreja().length() > 18){
//							String dsParte1 = this.igrejas.get(i).getDsIgreja().substring(0, 18);
//							String dsParte2 = this.igrejas.get(i).getDsIgreja().substring(19);
//
//							this.igrejas.get(i).setDsIgrejaInvertida(dsParte2 + ", " + dsParte1);
//						}else{
//							this.igrejas.get(i).setDsIgrejaInvertida(this.igrejas.get(i).getDsIgreja());
//						}
//					}	
//				}
//			}
			//Collections.sort(this.igrejas);

		} else
			this.igrejas = new ArrayList<Igreja>();

	}
	
	// Inicializar EstadoCiveis
	public void inicializarEstadoCiveis(){
		this.setEstadoCiveis(estadoCivelBO.findAll());
	}

	// Inicializar Escolaridades
	public void inicializarEscolaridades(){
		this.setEscolaridades(escolaridaddeBO.findAll());
	}

	// Inicializar Profissões
	public void inicializarProfissoes(){
		this.setProfissoes(profissaoBO.findAll());
	}
	
	// Inicializar Esposas de ministros
	public void inicializarEsposasMinistro() {
		this.setEsposasMinistro(ministroBO.findEsposasMinistro());

	}

	public List<MinistroListarTO> getMinistroListarTOList(List<Ministro> ministroList){		

		this.ministroListarTOList = new ArrayList<>();

		// Variáveis locais 
		String dsRegiao;
		String dsDepartamentoReduzido;
		String dsCargo;
		String dsIgreja;
		String dsCidadeUf;
		String dsUf;

		MinistroListarTO to;

		for (Ministro ministro : ministroList) {	
			dsIgreja = Optional.ofNullable(ministro.getIgreja()).map(x->x.getDsIgreja()).orElse("");

			dsRegiao = StringUtils.isNotBlank(dsIgreja)? Optional.ofNullable(ministro.getIgreja().getRegiao()).map(x->x.getDsRegiao()).orElse(""): "";

			dsDepartamentoReduzido = Optional.ofNullable(ministro.getDepartamento()).map(x->x.getDsReduzido()).orElse("");

			dsCargo = Optional.ofNullable(ministro.getCargo()).map(x->x.getDsCargo()).orElse("");
			dsUf = Optional.ofNullable(ministro.getEstado()).map(x->x.getDsUf()).orElse("");
			dsCidadeUf = ministro.getDsCidade();

			if(StringUtils.isNotBlank(dsCidadeUf) && StringUtils.isNotBlank(dsUf))
				dsCidadeUf = dsCidadeUf + "/" + dsUf;
			else
				dsCidadeUf = (dsCidadeUf != null? dsCidadeUf: "") + dsUf;

			to = new MinistroListarTO(ministro, dsRegiao, dsDepartamentoReduzido, dsCargo, dsIgreja, dsCidadeUf);

			ministroListarTOList.add(to);
		}

		return ministroListarTOList;
	}

	public void buscarTipoLancamento() {
		this.tipoLancamentosString = new ArrayList<String>();
		if(this.getRelatorioFiltro().getPlanoConta() != null) {
			this.tipoLancamentosList = tipoLancamentoBO.findAllTipoLancamentByPlanoConta(this.getRelatorioFiltro().getPlanoConta().getSqPlanoConta());
			
			for (TipoLancamento tpLan : this.tipoLancamentosList) {
				this.tipoLancamentosString.add(tpLan.getDsTipoLancamento() + " {" + tpLan.getSqTipoLancamento() + "} " );
			}
		}
	}


	public List<Estado> getEstados() {
		return estados;
	}

	public void setEstados(List<Estado> estados) {
		this.estados = estados;
	}

	public List<Regiao> getRegioes() {
		return regioes;
	}

	public void setRegioes(List<Regiao> regioes) {
		this.regioes = regioes;
	}

	public String getDsConvencao() {
		return dsConvencao;
	}

	public void setDsConvencao(String dsConvencao) {
		this.dsConvencao = dsConvencao;
	}

	public List<RegiaoItens> getRegiaoItens() {
		return regiaoItens;
	}

	public void setRegiaoItens(List<RegiaoItens> regiaoItens) {
		this.regiaoItens = regiaoItens;
	}

	public List<RegiaoItens> getRegiaoRecebimentoItens() {
		return regiaoRecebimentoItens;
	}

	public void setRegiaoRecebimentoItens(List<RegiaoItens> regiaoRecebimentoItens) {
		this.regiaoRecebimentoItens = regiaoRecebimentoItens;
	}

	public List<Departamento> getDepartamentos() {
		return departamentos;
	}

	public void setDepartamentos(List<Departamento> departamentos) {
		this.departamentos = departamentos;
	}

	public List<Cargo> getCargos() {
		return cargos;
	}

	public void setCargos(List<Cargo> cargos) {
		this.cargos = cargos;
	}

	public List<Igreja> getIgrejas() {
		return igrejas;
	}

	public void setIgrejas(List<Igreja> igrejas) {
		this.igrejas = igrejas;
	}

	public List<PlanoConta> getPlanoContaList() {
		return planoContaList;
	}

	public void setPlanoContaList(List<PlanoConta> planoContaList) {
		this.planoContaList = planoContaList;
	}

	public List<FormaRecebimentoPorReciboCpl> getFormaRecebimentoPorReciboCpl() {
		return formaRecebimentoPorReciboCpl;
	}

	public void setFormaRecebimentoPorReciboCpl(List<FormaRecebimentoPorReciboCpl> formaRecebimentoPorReciboCpl) {
		this.formaRecebimentoPorReciboCpl = formaRecebimentoPorReciboCpl;
	}

	public List<FormaPagamento> getFormaPagamento() {
		return formaPagamento;
	}

	public List<TipoLancamento> getTipoLancamentoList() {
		return tipoLancamentoList;
	}

	public void setTipoLancamentoList(List<TipoLancamento> tipoLancamentoList) {
		this.tipoLancamentoList = tipoLancamentoList;
	}

	public TipoPagamento[] getTipoPagamentos() {
		return TipoPagamento.values();
	}

	// Fechar Dialog do PrimeFaces
	protected void fecharDialogoPrimeFaces(boolean loggedIn) {
		// RequestContext context = RequestContext.getCurrentInstance();
		// context.addCallbackParam("loggedIn", loggedIn);
		
		PrimeRequestContext.getCurrentInstance().getCallbackParams().put("loggedIn", loggedIn);
	}

	public RelatorioFiltro getRelatorioFiltro() {
		return relatorioFiltro;
	}

	public void setRelatorioFiltro(RelatorioFiltro relatorioFiltro) {
		this.relatorioFiltro = relatorioFiltro;
	}

	public MinistroFiltro getFiltro() {
		return filtro;
	}

	public void setFiltro(MinistroFiltro filtro) {
		this.filtro = filtro;
	}

	// Validar preenchimento da combo Regiao
	public boolean validarPreenchimentoComboRegiao() {
		if(this.filtro.getRegiaoItensFiltro() == null || this.filtro.getRegiaoItensFiltro().getSqRegiao() == null) {
			return false;
		}

		return true;
	}

	public List<EstadoCivel> getEstadoCiveis() {
		return estadoCiveis;
	}

	public void setEstadoCiveis(List<EstadoCivel> estadoCiveis) {
		this.estadoCiveis = estadoCiveis;
	}

	public List<Escolaridade> getEscolaridades() {
		return escolaridades;
	}

	public void setEscolaridades(List<Escolaridade> escolaridades) {
		this.escolaridades = escolaridades;
	}

	public List<Ministro> getEsposasMinistro() {
		return esposasMinistro;
	}

	public void setEsposasMinistro(List<Ministro> esposasMinistro) {
		this.esposasMinistro = esposasMinistro;
	}

	public List<Profissao> getProfissoes() {
		return profissoes;
	}

	public void setProfissoes(List<Profissao> profissoes) {
		this.profissoes = profissoes;
	}

	public List<TipoLancamento> getTipoLancamentosList() {
		return tipoLancamentosList;
	}
	
	public void setTipoLancamentosList(List<TipoLancamento> tipoLancamentosList) {
		this.tipoLancamentosList = tipoLancamentosList;
	}
	
	public List<String> getTipoLancamentosString() {
		return tipoLancamentosString;
	}
	
	public void setTipoLancamentosString(List<String> tipoLancamentosString) {
		this.tipoLancamentosString = tipoLancamentosString;
	}
}
