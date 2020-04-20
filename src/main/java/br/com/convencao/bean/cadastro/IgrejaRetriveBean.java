package br.com.convencao.bean.cadastro;

import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.convencao.bo.MinistroBO;
import br.com.convencao.model.Igreja;
import br.com.convencao.model.Ministro;

@Named(value = "igrejaRetriveBean")
@ViewScoped
public class IgrejaRetriveBean extends IgrejaCodbehind{

	private static final long serialVersionUID = 1L;

	private Igreja igreja;
	private List<MinistroListarTO> ministroListarTO;

	@Inject
	private MinistroBO ministroBO;
		
	// Buscar todos os ministros de todos os departamentos da igreja.
	public void inicializarMinistrosPorIgreja(){		
		List<Ministro> igrejaMinistros = ministroBO.findMinistrosByIgreja(this.igreja.getSqIgreja());
		this.ministroListarTO = this.getMinistroListarTOList(igrejaMinistros);

	}

	public Igreja getIgreja() {
		return igreja;
	}

	public void setIgreja(Igreja igreja) {
		this.igreja = igreja;
	}


	public String getMensagemRodape(){
		return "Total registros encontrado: " + this.ministroListarTO.size();
	}
	
	public List<MinistroListarTO> getMinistroListarTO() {
		return ministroListarTO;
	}

}