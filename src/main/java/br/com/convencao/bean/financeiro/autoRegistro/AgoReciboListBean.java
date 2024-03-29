package br.com.convencao.bean.financeiro.autoRegistro;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.convencao.bo.AgoBO;
import br.com.convencao.bo.AgoInscritoBO;
import br.com.convencao.bo.AgoReciboBO;
import br.com.convencao.bo.NegocioException;
import br.com.convencao.model.Ago;
import br.com.convencao.model.AgoInscrito;
import br.com.convencao.model.AgoRecibo;
import lombok.Getter;
import lombok.Setter;

@Named(value = "agoReciboListBean")
@ViewScoped
public class AgoReciboListBean implements Serializable {
	private static final long serialVersionUID = 1L;

	@Inject
	private AgoReciboBO agoReciboBO;

	@Inject
	private AgoBO agoBO;
	
	@Inject
	private AgoInscritoBO agoInscritoBO;

	@Getter private List<AgoRecibo> agoReciboList; 
	@Getter private List<Ago> agoList;
	@Getter @Setter private AgoRecibo agoReciboSelecionado;
	
	@Getter @Setter private Ago agoSelecionado;
	@Getter @Setter private List<AgoInscrito> agoReciboDetalheList;
	
	@Getter @Setter private BigDecimal vlAgoTotalReciboDetalhe;

	public void inicializar() {
		if(this.agoReciboList == null)
			this.agoReciboList = new ArrayList<AgoRecibo>();

		//Buscar lista de AGOs
		if(this.agoList == null)
			this.agoList = agoBO.findAllAGO();
	}
	
	public void inicializarAgoReciboDetalhes(AgoRecibo selecionado) {
		this.agoReciboSelecionado = selecionado;
		this.agoReciboDetalheList = agoInscritoBO.findAllPorAgoRecibo(selecionado.getSqRecibo());
		
		this.vlAgoTotalReciboDetalhe = BigDecimal.ZERO;
		for (AgoInscrito i : agoReciboDetalheList) {
			this.vlAgoTotalReciboDetalhe = this.vlAgoTotalReciboDetalhe.add(i.getVlInscricao());
		}
		
	}

	public void listarRecibos() {
		if(this.agoSelecionado == null || this.agoSelecionado.getSqAgo() == null) 
			throw new NegocioException("Faltou selecionar a AGO.");
		this.agoReciboList = this.agoReciboBO.findAllPorAgo(this.agoSelecionado.getSqAgo());

	}
	
	public String getMensagemRodape(){
		return "Total registros encontrado: " + this.agoReciboList.size();
	}

}
