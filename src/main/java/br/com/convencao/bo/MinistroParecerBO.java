package br.com.convencao.bo;

import java.io.Serializable;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import br.com.convencao.dao.MinistroParecerDAO;
import br.com.convencao.model.MinistroParecer;
import br.com.convencao.util.Uteis;
import br.com.convencao.util.jpa.Transactional;

public class MinistroParecerBO implements Serializable{

	private static final long serialVersionUID = 1L;

	private static Log log = LogFactory.getLog(MinistroParecerBO.class);

	@Inject
	MinistroParecerDAO ministroParecerDao;

	@Transactional
	public MinistroParecer salvarParecer(MinistroParecer parecer) {
		log.info("MinistroParecer(" + parecer.getSqProtocoloParecer() + ")");

		return this.salvar(parecer);
	}

	// Salvar novo e alteração de registro
	public MinistroParecer salvar(MinistroParecer parecer) {
		try {
			log.info("salvar(" + parecer.getSqProtocoloParecer() + ")");

			// Retirar espaços de inicio e fim
			parecer.setDsParecer(parecer.getDsParecer().trim());

			// Verificar se parecer foi preenchido
			if(StringUtils.isBlank(parecer.getDsParecer())) {
				throw new NegocioException("Parecer não foi preenchido.");
			}

			// Atualizar dados
			parecer.setDsResponsavel(Uteis.UsuarioLogado().getUsuario().getDsLogin());
			parecer.setAuditoriaUsuario(Uteis.UsuarioLogado().getUsuario().getDsLogin());
			parecer.setAuditoriaData(Uteis.DataHoje());
			parecer.setDtRegistro(Uteis.DataHoje());


			return this.ministroParecerDao.salvar(parecer);
		} catch (Exception e) {
			e.printStackTrace();
			throw new NegocioException("Parecer não gravado. " + e.getMessage());
		}

	}
}
