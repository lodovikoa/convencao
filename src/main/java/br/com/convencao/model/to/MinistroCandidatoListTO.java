package br.com.convencao.model.to;

import java.io.Serializable;

public class MinistroCandidatoListTO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Long sqProtocolo;
	private Long cdProtocolo;
	private Long sqProtocoloStatus;
	private String dsStatus;
	private Long cdCodigoCandidato;
	private String nmCandidato;
	private String dsRegiao;
	private String dsIgreja;
	private boolean flExibirBotaoEditar;
	private boolean flExibirBotaoExcluir;

	public MinistroCandidatoListTO(Long sqProtocolo, Long cdProtocolo, Long sqProtocoloStatus, String dsStatus, Long cdCodigoCandidato,
			String nmCandidato, String dsRegiao, String dsIgreja) {
		this.sqProtocolo = sqProtocolo;
		this.cdProtocolo = cdProtocolo;
		this.sqProtocoloStatus = sqProtocoloStatus;
		this.dsStatus = dsStatus;
		this.cdCodigoCandidato = cdCodigoCandidato;
		this.nmCandidato = nmCandidato;
		this.dsRegiao = dsRegiao;
		this.dsIgreja = dsIgreja;
	}

	public Long getSqProtocolo() {
		return sqProtocolo;
	}

	public void setSqProtocolo(Long sqProtocolo) {
		this.sqProtocolo = sqProtocolo;
	}

	public Long getCdProtocolo() {
		return cdProtocolo;
	}

	public void setCdProtocolo(Long cdProtocolo) {
		this.cdProtocolo = cdProtocolo;
	}

	public String getDsStatus() {
		return dsStatus;
	}

	public void setDsStatus(String dsStatus) {
		this.dsStatus = dsStatus;
	}

	public Long getCdCodigoCandidato() {
		return cdCodigoCandidato;
	}

	public void setCdCodigoCandidato(Long cdCodigoCandidato) {
		this.cdCodigoCandidato = cdCodigoCandidato;
	}

	public String getNmCandidato() {
		return nmCandidato;
	}

	public void setNmCandidato(String nmCandidato) {
		this.nmCandidato = nmCandidato;
	}

	public String getDsRegiao() {
		return dsRegiao;
	}

	public void setDsRegiao(String dsRegiao) {
		this.dsRegiao = dsRegiao;
	}

	public String getDsIgreja() {
		return dsIgreja;
	}

	public void setDsIgreja(String dsIgreja) {
		this.dsIgreja = dsIgreja;
	}
	
	public Long getSqProtocoloStatus() {
		return sqProtocoloStatus;
	}
	
	public void setSqProtocoloStatus(Long sqProtocoloStatus) {
		this.sqProtocoloStatus = sqProtocoloStatus;
	}

	public boolean isFlExibirBotaoEditar() {
		return flExibirBotaoEditar;
	}
	
	public void setFlExibirBotaoEditar(boolean flExibirBotaoEditar) {
		this.flExibirBotaoEditar = flExibirBotaoEditar;
	}
	
	public boolean isFlExibirBotaoExcluir() {
		return flExibirBotaoExcluir;
	}
	
	public void setFlExibirBotaoExcluir(boolean flExibirBotaoExcluir) {
		this.flExibirBotaoExcluir = flExibirBotaoExcluir;
	}
}
