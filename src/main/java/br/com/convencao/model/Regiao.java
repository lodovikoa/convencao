package br.com.convencao.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotEmpty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name="tb_rgn_regiao")
@NoArgsConstructor
@ToString
public class Regiao implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="rgn_sq_regiao")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Getter @Setter  private Long sqRegiao;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="con_sq_convencao", nullable=false)
	@Getter @Setter  private Convencao convencao;

	@NotEmpty
	@Column(name="rgn_ds_regiao")
	@Getter @Setter  private String dsRegiao;

	@Column(name="auditoria_data")
	@Getter @Setter  private LocalDateTime auditoriaData;

	@Column(name="auditoria_usuario", length=50)
	@Getter @Setter  private String auditoriaUsuario;

	@Transient
	@Getter @Setter  private boolean flSelecionado;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((sqRegiao == null) ? 0 : sqRegiao.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		Long sqTemp = -1L;
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass()){
			if(!this.getSqRegiao().equals(((Regiao) obj).getSqRegiao())) {
				return false;
			} else {
				sqTemp = ((Regiao) obj).getSqRegiao();
			}
		}
		Regiao other = (Regiao) obj;
		if (sqRegiao == null) {
			if (other.sqRegiao != null)
				return false;
		} else if (!sqRegiao.equals(other.sqRegiao)) {
			if(!sqRegiao.equals(sqTemp))
				return false;
		}
		return true;
	}

	// Método equals personalizado
	public boolean equalsTO(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Regiao other = (Regiao) obj;
		if (convencao == null) {
			if (other.convencao != null)
				return false;
		} else if (!convencao.equals(other.convencao))
			return false;
		if (dsRegiao == null) {
			if (other.dsRegiao != null)
				return false;
		} else if (!dsRegiao.equals(other.dsRegiao))
			return false;
		return true;
	}

}
