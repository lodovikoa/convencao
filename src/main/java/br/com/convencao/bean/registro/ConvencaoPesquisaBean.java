package br.com.convencao.bean.registro;

import java.io.Serializable;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.convencao.bo.ConvencaoBO;
import br.com.convencao.model.Convencao;
import br.com.convencao.util.Uteis;
import br.com.convencao.util.jsf.FacesUtil;

@Named(value="convencaoPesquisaBean")
@ViewScoped
public class ConvencaoPesquisaBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private List<Convencao> lista;
	
	@Inject
	private ConvencaoBO convencaoBO;
	
	private Convencao convencaoSelecionada;
	
	private void listarConvencoes(){
		this.lista = convencaoBO.listarConvencoes();
	}
	
	public void excluir(){

		convencaoBO.remover(convencaoSelecionada);

		// Refazer a pesquisa para listar as convenções
		this.listarConvencoes();

		FacesUtil.addInfoMessage("Convenção " + convencaoSelecionada.getDsReduzido() + " excluida com sucesso!");

	}
	
	
	public Convencao getConvencaoSelecionada() {
		return convencaoSelecionada;
	}
	
	public void setConvencaoSelecionada(Convencao convencaoSelecionada) {
		this.convencaoSelecionada = convencaoSelecionada;
	}
	
	public List<Convencao> getLista() {
		if(this.lista == null)
			this.listarConvencoes();
		
		return lista;
	}
	
	public void buscar(){
		String param = FacesUtil.obterParametro("convencao");
		this.convencaoSelecionada = convencaoBO.findByPrimaryKey(Long.parseLong(param));

		// Adicionar mascara para exibição em tela
		this.convencaoSelecionada.setDsCnpj(Uteis.cnpjMask(this.convencaoSelecionada.getDsCnpj()));
		this.convencaoSelecionada.setDsCep(Uteis.cepMask(this.convencaoSelecionada.getDsCep()));
		
	}
	
	public String getMensagemRodape(){
		return "Total registros encontrados: " + this.lista.size();
	}
}
