package br.com.convencao.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tb_prs_protocolo_status")
public class ProtocoloStatus implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "prs_sq_protocolo_status")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long sqProtocoloStatus;
	
	@Column(name = "prs_nn_ordem")
	private Integer nnOrdem;
	
	@Column(name = "prs_fl_exibir")
	private boolean flExibir;
	
	@Column(name = "prs_ds_status")
	private String dsStatus;
	
	@Column(name="auditoria_data")
	private LocalDateTime auditoriaData;
	
	@Column(name="auditoria_usuario", length=50)
	private String auditoriaUsuario;

	public Long getSqProtocoloStatus() {
		return sqProtocoloStatus;
	}

	public void setSqProtocoloStatus(Long sqProtocoloStatus) {
		this.sqProtocoloStatus = sqProtocoloStatus;
	}

	public Integer getNnOrdem() {
		return nnOrdem;
	}

	public void setNnOrdem(Integer nnOrdem) {
		this.nnOrdem = nnOrdem;
	}
	
	public boolean isFlExibir() {
		return flExibir;
	}

	public void setFlExibir(boolean flExibir) {
		this.flExibir = flExibir;
	}

	public String getDsStatus() {
		return dsStatus;
	}

	public void setDsStatus(String dsStatus) {
		this.dsStatus = dsStatus;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((sqProtocoloStatus == null) ? 0 : sqProtocoloStatus.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProtocoloStatus other = (ProtocoloStatus) obj;
		if (sqProtocoloStatus == null) {
			if (other.sqProtocoloStatus != null)
				return false;
		} else if (!sqProtocoloStatus.equals(other.sqProtocoloStatus))
			return false;
		return true;
	}

	// Equals customizado
	public boolean equalsTO(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProtocoloStatus other = (ProtocoloStatus) obj;
		if (sqProtocoloStatus == null) {
			if (other.sqProtocoloStatus != null)
				return false;
		} else if (!sqProtocoloStatus.equals(other.sqProtocoloStatus))
			return false;
		return true;
	}


}
