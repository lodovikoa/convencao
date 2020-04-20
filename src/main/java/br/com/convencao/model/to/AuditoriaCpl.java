package br.com.convencao.model.to;

import java.io.Serializable;

import br.com.convencao.model.Auditoria;
import br.com.convencao.model.Ministro;

public class AuditoriaCpl implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Auditoria auditoria;
	private Ministro ministro;
	
	public Auditoria getAuditoria() {
		return auditoria;
	}
	public void setAuditoria(Auditoria auditoria) {
		this.auditoria = auditoria;
	}
	public Ministro getMinistro() {
		return ministro;
	}
	public void setMinistro(Ministro ministro) {
		this.ministro = ministro;
	}
	
	
}
