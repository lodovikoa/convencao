package br.com.convencao.bean.cadastro;

import java.util.ArrayList;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang3.StringUtils;

import br.com.convencao.bo.IgrejaBO;
import br.com.convencao.bo.NegocioException;
import br.com.convencao.model.Estado;
import br.com.convencao.model.Igreja;
import br.com.convencao.model.Ministro;
import br.com.convencao.util.Uteis;
import br.com.convencao.util.jsf.FacesUtil;
import br.com.convencao.util.permissao.Permissoes;

@Named(value = "igrejaPesquisaBean")
@ViewScoped
public class IgrejaPesquisaBean extends IgrejaCodbehind{

	private static final long serialVersionUID = 1L;

	private List<Igreja> lista;

	private List<Ministro> igrejaMinistrosPresidente;

	@Inject
	private IgrejaBO bo;

	private Igreja selecionada;

	@Inject
	private IgrejaFiltro igrejaFiltro;

	boolean exibirBotaoCadastrarIgreja;
	boolean exibirBotaoAlterarIgreja;
	boolean exibirBotaoExcluirIgreja;

	boolean flBuscarAjax = false;

	private void listarIgrejas(){
		this.lista = bo.findByParametros(this.igrejaFiltro);

		if(this.lista.size() == 0){
			this.lista = null;
			this.igrejaMinistrosPresidente = null;

			throw new NegocioException("Nennhum registro encontrado para o filtro selecionado.");
		}

		for (int i = 0; i < lista.size(); i++) {
			if(null != lista.get(i).getDsCnpj() && lista.get(i).getDsCnpj().length() > 11){
				this.lista.get(i).setDsCnpj(Uteis.cnpjMask(this.lista.get(i).getDsCnpj()));
			}
		}
	}

	public void inicializar(){

		this.exibirBotaoCadastrarIgreja = Permissoes.getPermissaoInserirEditarIgreja();
		this.exibirBotaoAlterarIgreja = Permissoes.getPermissaoInserirEditarIgreja();
		this.exibirBotaoExcluirIgreja = Permissoes.getPermissaoInserirEditarIgreja();

		if(!this.flBuscarAjax) {
			this.flBuscarAjax =  false;
			if( this.inicializarRegioes("SEC") > 0 ) {
				this.igrejaFiltro.setRegiaoItens(this.getRegiaoItens().get(0));
				this.buscarPresidentes();
			}
		} 

		if(this.lista == null)
			this.lista = new ArrayList<>();

		if(this.igrejaMinistrosPresidente == null)
			this.igrejaMinistrosPresidente = new ArrayList<>();
	}

	public void buscarPresidentes(){
		this.flBuscarAjax = true;
		if(null == this.igrejaFiltro.getRegiaoItens()){
			// Limpar presidentes
			this.igrejaMinistrosPresidente = new ArrayList<>();
		}else{
			// Buscar ministros presidentes de uma determinada região ou todos se o retorno for -1
			this.igrejaMinistrosPresidente = bo.findPresidenteByRegiao(this.igrejaFiltro.getRegiaoItens().getSqRegiao());

		}
	}

	public void excluir(){
		bo.remover(selecionada);

		// Refazer a pesquisa para lsitar as igrejas
		this.listarIgrejas();

		// Exibir mensagem
		FacesUtil.addInfoMessage("Igreja " + selecionada.getDsIgreja() + " excluida com sucesso!");
	}

	public void buscar(){
		String param = FacesUtil.obterParametro("igreja");
		this.selecionada = bo.findByPrimaryKey(Long.parseLong(param));

		// Adicionar mascara para exibição em tela
		this.selecionada.setDsCnpj(Uteis.cnpjMask(this.selecionada.getDsCnpj()));
		this.selecionada.setDsCep(Uteis.cepMask(this.selecionada.getDsCep()));
	}

	public void pesquisar(){

		// Retirar mascara do CNPJ
		if(this.igrejaFiltro.getDsCnpjFiltro() != null && this.igrejaFiltro.getDsCnpjFiltro().length() > 1){
			this.igrejaFiltro.setDsCnpjFiltro(Uteis.OnlyNumbers(this.igrejaFiltro.getDsCnpjFiltro()));
		}

		this.listarIgrejas();

	}

	public List<Igreja> getLista() {
		return lista;
	}

	public Igreja getSelecionada() {
		return selecionada;
	}

	public void setSelecionada(Igreja selecionada) {
		this.selecionada = selecionada;
	}


	public IgrejaFiltro getIgrejaFiltro() {
		return igrejaFiltro;
	}

	public void setIgrejaFiltro(IgrejaFiltro igrejaFiltro) {
		this.igrejaFiltro = igrejaFiltro;
	}

	public List<Ministro> getIgrejaMinistrosPresidente() {	
		return igrejaMinistrosPresidente;
	}

	public String getMensagemRodape(){
		return "Total registros encontrado: " + this.lista.size();
	}


	public void recuperarParametros(){
		if(StringUtils.isNotBlank(this.getParam_controle()) && this.getParam_controle().equals("1")){
			this.setParam_controle(null);

			RegiaoItens itens = null;
			Ministro min = null;
			Estado est = null;

			if(!this.getParam_regiaoItens_sqRegiao().equals("null")){
				itens = new RegiaoItens();
				itens.setSqRegiao(Long.parseLong(this.getParam_regiaoItens_sqRegiao()));
			}

			if(!this.getParam_ministro_sqMinistro().equals("null")){
				min =  new Ministro();
				min.setSqMinistro(Long.parseLong(this.getParam_ministro_sqMinistro()));
			}

			if(!this.getParam_estado_sqEstado().equals("null")){
				est = new Estado();
				est.setSqEstado(Long.parseLong(this.getParam_estado_sqEstado()));
			}


			this.igrejaFiltro.setRegiaoItens(itens);
			this.igrejaFiltro.setMinistro(min);
			this.igrejaFiltro.setEstado(est);
			this.igrejaFiltro.setDsIgrejaFiltro(this.getParam_dsIgrejaFiltro());
			this.igrejaFiltro.setDsBairroFiltro(this.getParam_dsBairroFiltro());
			this.igrejaFiltro.setDsCidadeFiltro(this.getParam_dsCidadeFiltro());
			this.igrejaFiltro.setDsCnpjFiltro(this.getParam_dsCnpjFiltro());

			this.buscarPresidentes();

			this.listarIgrejas();

		}
	}

	public boolean isExibirBotaoCadastrarIgreja() {
		return exibirBotaoCadastrarIgreja;
	}

	public void setExibirBotaoCadastrarIgreja(boolean exibirBotaoCadastrarIgreja) {
		this.exibirBotaoCadastrarIgreja = exibirBotaoCadastrarIgreja;
	}

	public boolean isExibirBotaoAlterarIgreja() {
		return exibirBotaoAlterarIgreja;
	}

	public void setExibirBotaoAlterarIgreja(boolean exibirBotaoAlterarIgreja) {
		this.exibirBotaoAlterarIgreja = exibirBotaoAlterarIgreja;
	}

	public boolean isExibirBotaoExcluirIgreja() {
		return exibirBotaoExcluirIgreja;
	}

	public void setExibirBotaoExcluirIgreja(boolean exibirBotaoExcluirIgreja) {
		this.exibirBotaoExcluirIgreja = exibirBotaoExcluirIgreja;
	}

}