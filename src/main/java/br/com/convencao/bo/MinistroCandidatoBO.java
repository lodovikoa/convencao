package br.com.convencao.bo;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.PersistenceException;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import br.com.convencao.bean.cadastro.MinistroFiltro;
import br.com.convencao.dao.DepartamentoDAO;
import br.com.convencao.dao.MinistroAnexoDAO;
import br.com.convencao.dao.MinistroDAO;
import br.com.convencao.dao.MinistroParecerDAO;
import br.com.convencao.dao.ProtocoloDAO;
import br.com.convencao.dao.ProtocoloStatusDAO;
import br.com.convencao.model.Departamento;
import br.com.convencao.model.Ministro;
import br.com.convencao.model.MinistroAnexo;
import br.com.convencao.model.MinistroParecer;
import br.com.convencao.model.Protocolo;
import br.com.convencao.model.ProtocoloStatus;
import br.com.convencao.model.to.MinistroCandidatoListTO;
import br.com.convencao.util.Uteis;
import br.com.convencao.util.jpa.Transactional;

public class MinistroCandidatoBO implements Serializable {

	private static final long serialVersionUID = 1L;

	private static Log log = LogFactory.getLog(MinistroCandidatoBO.class);

	@Inject
	ProtocoloDAO protocoloDAO;

	@Inject
	ProtocoloStatusDAO protocoloStatusDAO;

	@Inject
	MinistroAnexoDAO ministroAnexoDAO;

	@Inject
	MinistroParecerDAO ministroParecerDAO;

	@Inject
	MinistroDAO ministroDAO;

	@Inject
	DepartamentoDAO departamentoDAO;

	@Inject
	AnexosBO anexosBO;

	@Inject
	MinistroParecerBO ministroParecerBO;

	@Inject
	FotoBO fotoBO;

	// Buscar Candidatos de acordo com os filtros informados
	public List<MinistroCandidatoListTO> findByParametros(MinistroFiltro filtro) {
		log.info("findByParametros(Filtro filtro)");

		List<MinistroCandidatoListTO> result = protocoloDAO.findByParametros(filtro);
		return result;
	}

	// Salvar Ministro Candidato
	@Transactional
	public Protocolo salvar(Protocolo protocolo) {
		log.info("salvar(" + protocolo.getSqProtocolo() + ")");
		try {

			// Identificar se é cadastro ou alteraçao
			boolean flEditando = (protocolo.getSqProtocolo() != null);

			// Fazer validações e ajustes nas Strings do Candidato
			this.ajustarStringsProtocolo(protocolo);
			this.validarPreenchimentoCamposObrigatorios(protocolo);

			// Na edição do candidato somente continuar se houve alteração do candidato
			if(flEditando) {
				boolean flAnexoNovo = false;
				for (MinistroAnexo anexo : protocolo.getMinistro().getMinistroAnexo()) {
					if(anexo.getSqMinistroAnexo() == null) {
						flAnexoNovo = true;
					}
				}

				if(!flAnexoNovo) {
					Ministro candidatoAnterior = ministroDAO.find(Ministro.class, protocolo.getMinistro().getSqMinistro());
					if(protocolo.getMinistro().equalsTO(candidatoAnterior)) {
						throw new NegocioException("Não houve alteração no candidato.");
					}
				}
			}

			// Validar CPF já usado por outro ministro
			if(protocolo.getMinistro().getDsCpf() != null && protocolo.getMinistro().getDsCpf().trim().length() > 0){
				Ministro ministroCPF = ministroDAO.validarCpf(protocolo.getMinistro().getSqMinistro(), protocolo.getMinistro().getDsCpf());
				if(ministroCPF != null){
					throw new NegocioException("CPF: " + protocolo.getMinistro().getDsCpf() + " já usado para o  ministro " + ministroCPF.getCdCodigo() + " - " + ministroCPF.getNmNome());
				}
			}

			// Validar esposa do ministro já encontra-se registrada como esposa de outro ministro.
			if(protocolo.getMinistro().getConjuge() != null){
				Ministro validarEsposa = ministroDAO.validarConjuge(protocolo.getMinistro().getSqMinistro(), protocolo.getMinistro().getConjuge().getSqMinistro());
				if(validarEsposa != null){
					throw new NegocioException(protocolo.getMinistro().getConjuge().getNmNome() + " encontra-se registrada como esposa do ministro " + validarEsposa.getCdCodigo() + " - " + validarEsposa.getNmNome());
				}
			}

			// Atualizar Código do Candidato a Ministro para quando está cadastrando novo candidato
			if(!flEditando) {
				List<Long> sqDepartamentos = new ArrayList<Long>();
				sqDepartamentos.add(1L); // Departamento Confrateres
				sqDepartamentos.add(5L); // Departamento Candidatos
				protocolo.getMinistro().setCdCodigo(ministroDAO.ultimoCodigoRegistrado(sqDepartamentos) + 1);
			}
			// Atualizar auditoria do Candidato a Ministro
			protocolo.getMinistro().setAuditoriaData(Uteis.DataHoje());
			protocolo.getMinistro().setAuditoriaUsuario(Uteis.UsuarioLogado().getUsuario().getDsLogin());

			// Inserir auditoria nos registros novos de anexo
			for (int i = 0; i < protocolo.getMinistro().getMinistroAnexo().size(); i++) {
				if(protocolo.getMinistro().getMinistroAnexo().get(i).getSqMinistroAnexo() == null) {
					protocolo.getMinistro().getMinistroAnexo().get(i).setDsAnexoResponsavel(Uteis.UsuarioLogado().getUsuario().getDsLogin());
					protocolo.getMinistro().getMinistroAnexo().get(i).setDtAnexoRegistro(Uteis.DataHoje().toLocalDate());

					protocolo.getMinistro().getMinistroAnexo().get(i).setAuditoriaUsuario(Uteis.UsuarioLogado().getUsuario().getDsLogin());
					protocolo.getMinistro().getMinistroAnexo().get(i).setAuditoriaData(Uteis.DataHoje());
				}
			}

			// inserir auditoria em registro de novos pareceres no cadastro de novos candidatos
			if(!flEditando) {
				for (int i = 0; i < protocolo.getMinistro().getMinistroParecer().size(); i++) {
					if(protocolo.getMinistro().getMinistroParecer().get(i).getSqProtocoloParecer() == null) {
						protocolo.getMinistro().getMinistroParecer().get(i).setDsResponsavel(Uteis.UsuarioLogado().getUsuario().getDsLogin());
						protocolo.getMinistro().getMinistroParecer().get(i).setDtRegistro(Uteis.DataHoje());
						protocolo.getMinistro().getMinistroParecer().get(i).setAuditoriaUsuario(Uteis.UsuarioLogado().getUsuario().getDsLogin());
						protocolo.getMinistro().getMinistroParecer().get(i).setAuditoriaData(Uteis.DataHoje());
					}
				}
			}

			// Salvar Candidato Ministro
			Ministro candidatoMinistro = ministroDAO.salvar(protocolo.getMinistro());

			// Atualizar Candidato em protocolo
			protocolo.setMinistro(candidatoMinistro);

			try {
				String dsRetornoAnexos = anexosBO.recuperarAnexoTemporario(protocolo.getMinistro().getMinistroAnexo(), candidatoMinistro.getSqMinistro(), candidatoMinistro.getCdCodigo());

				if(StringUtils.isNotEmpty(dsRetornoAnexos)) {
					log.info("salvar(" + protocolo.getSqProtocolo() + " ..............Anexos não salvo (" + dsRetornoAnexos +  ")");
				}

			} catch (IOException e) {
				throw new NegocioException("Problema ao tentar recuperar anexos da área temporária.", e);
			}

			// Transferir a foto do candidato que esta na área temporária para área definitiva
			try {
				// fotoBO.recuperarFotoTemporaria(protocolo.getMinistro().getDsFoto(), candidatoMinistro.getCdCodigo());
				fotoBO.recuperarFotoTemporaria(protocolo.getMinistro(),candidatoMinistro.getSqMinistro());
			} catch (IOException e) {
				throw new NegocioException("Problema ao tentar recuperar foto temporária.", e);
			}

			// Buscar número do próximo protocolo para cadastro de novos candidatos
			if(!flEditando) {
				protocolo.setCdProtocolo(protocoloDAO.findUltimoProtocoloRegistrado() + 1);	
				protocolo.setDtProtocolo(Uteis.DataHoje().toLocalDate());
			}

			// Atualizar auditoria em protocolo
			protocolo.setAuditoriaData(Uteis.DataHoje());
			protocolo.setAuditoriaUsuario(Uteis.UsuarioLogado().getUsuario().getDsLogin());

			protocolo = protocoloDAO.salvar(protocolo);

			return protocolo;

		} catch (Exception e) {
			throw new NegocioException("Erro ao tentar salvar Ministro Candidato: " + e.getMessage(), e);
		}

	}

	// Salvar anexo do ministro(MinistroBO) e Salvar anexo do protocolo(MinisgtroCandidato) ((Atualizar para realizar esta tarefa em um único lugar))
	@Transactional
	public void salvarAnexosNaAlteracaoCandidato(Protocolo protocolo, MinistroAnexo ministroAnexo) {
		try {
			log.info("salvaAnexosNaAlteraçãoCandidato(" + protocolo.getSqProtocolo() + ") Anexo novo: " + ministroAnexo.getDsAnexoOriginal() + ")");

			String dsRetornoAnexo = anexosBO.recuperarAnexoTemporario(ministroAnexo.getDsAnexoRenomeado());

			if(StringUtils.isNotEmpty(dsRetornoAnexo)) {
				log.info("salvar(" + protocolo.getSqProtocolo() + " ..............Anexo não salvo (" + dsRetornoAnexo +  ")");
				throw new NegocioException("Anexo não salvo: " + dsRetornoAnexo);
			}

			// Inserir informações
			ministroAnexo.setDsAnexoResponsavel(Uteis.UsuarioLogado().getUsuario().getDsLogin());
			ministroAnexo.setDtAnexoRegistro(Uteis.DataHoje().toLocalDate());
			ministroAnexo.setAuditoriaUsuario(Uteis.UsuarioLogado().getUsuario().getDsLogin());
			ministroAnexo.setAuditoriaData(Uteis.DataHoje());

			// Gravar registro
			ministroAnexoDAO.salvar(ministroAnexo);

		} catch (Exception e) {
			throw new NegocioException("Problemas ao tentar salvar anexos: " + e.getMessage());
		}

	}

	@Transactional
	public void salvarFotoNaAlteracaoCandidato( Protocolo protocolo) {
		try {
			log.info("salvaFotoNaAlteracaoCandidato(" + protocolo.getSqProtocolo() + ") foto nova: (" + protocolo.getMinistro().getDsFoto() + ")");

			// Obtem o ministro atual para que outras alterações já efetuadas não sejam atualizadas junto com a foto
			Ministro ministroTemp = ministroDAO.find(Ministro.class, protocolo.getMinistro().getSqMinistro());
			String dsFotoAnterior = ministroTemp.getDsFoto();
			log.info("salvaFotoNaAlteracaoCandidato(" + protocolo.getSqProtocolo() + ") foto anterior: (" + dsFotoAnterior + ")");

			ministroTemp.setDsFoto(protocolo.getMinistro().getDsFoto());

			fotoBO.recuperarFotoTemporaria(ministroTemp);

			// Verificar se o nome da foto atual é igual ao nome da foto anterior. Se for diferente precisa gravar o novo nome e excluir a foto antiga
			if(!protocolo.getMinistro().getDsFoto().equalsIgnoreCase(dsFotoAnterior)) {

				// Salvar o Ministro com o nome da nova foto
				ministroDAO.salvar(ministroTemp);

				// Remover foto anterior (Caso a foto anterior seja a foto default, então não remover)
				if(StringUtils.isNotEmpty(dsFotoAnterior) && !dsFotoAnterior.equals(Uteis._FOTO_DEFAULT)) {
					fotoBO.deletar(dsFotoAnterior);
				}
			}

		} catch (Exception e) {
			throw new NegocioException("Problemas ao tentar salvar foto: " + e.getMessage());
		}
	}

	// Remover foto na alteração de Ministro
	@Transactional
	public void salvarExcluirFotoNaAlteracao(Protocolo protocolo) {
		try {
			log.info("salvarExclusaoFotoNaAlteracao(" + protocolo.getSqProtocolo() + ") foto: (" + protocolo.getMinistro().getDsFoto() + ")");

			if(!protocolo.getMinistro().getDsFoto().equalsIgnoreCase(Uteis._FOTO_DEFAULT)) {
				// Buscar ministro para não interferir em outras possíveis alterações efetuadas
				Ministro ministroTemp = ministroDAO.find(Ministro.class, protocolo.getMinistro().getSqMinistro());

				ministroTemp.setDsFoto(Uteis._FOTO_DEFAULT);

				ministroDAO.salvar(ministroTemp);

				fotoBO.deletar(protocolo.getMinistro().getDsFoto());

			} else {
				log.info("salvarExclusaoFotoNaAlteracao(" + protocolo.getSqProtocolo() + ") Houve tentativa de excluir a foto default: (" + Uteis._FOTO_DEFAULT + ")");
				throw new NegocioException("Foto default não pode ser removida.");
			}

		} catch (Exception e) {
			throw new NegocioException("Problemas ao tentar remover foto: " + e.getMessage());
		}
	}

	// Remover anexo na alteração de Candidato
	@Transactional
	public String salvarExcluirAnexoNaAlteracao(Long attribute_sqProtocoloAnexo) {
		log.info("salvarExcluirAnexoNaAlteracao (" + attribute_sqProtocoloAnexo + ")");

		// Buscar dados do anexo que será excluido.
		MinistroAnexo protocoloAnexo = ministroAnexoDAO.find(MinistroAnexo.class, attribute_sqProtocoloAnexo);

		try {
			// Excluir o registro do anexo no bd
			ministroAnexoDAO.delete(protocoloAnexo, protocoloAnexo.getSqMinistroAnexo());

			// Remover o anexo
			anexosBO.deletar(protocoloAnexo.getDsAnexoRenomeado());

			return protocoloAnexo.getDsAnexoOriginal();
		} catch (Exception e) {
			throw new NegocioException("Problemas ao tentar remover anexo (" + protocoloAnexo.getDsAnexoOriginal() + "): " + e.getMessage());
		}
	}

	private void ajustarStringsProtocolo(Protocolo to) {
		log.info("ajustarStringsProtocolo(Protocolo to)");

		if(to.getMinistro().getDsBairro() != null) to.getMinistro().setDsBairro(to.getMinistro().getDsBairro().trim());
		if(to.getMinistro().getDsCidade() != null) to.getMinistro().setDsCidade(to.getMinistro().getDsCidade().trim());
		if(to.getMinistro().getDsEmail() != null)  to.getMinistro().setDsEmail(to.getMinistro().getDsEmail().trim());
		if(to.getMinistro().getDsEndereco() != null) to.getMinistro().setDsEndereco(to.getMinistro().getDsEndereco().trim());
		if(to.getMinistro().getDsIdentidade() != null) to.getMinistro().setDsIdentidade(to.getMinistro().getDsIdentidade().trim());
		if(to.getMinistro().getDsLocalNascimento() != null) to.getMinistro().setDsLocalNascimento(to.getMinistro().getDsLocalNascimento().trim());
		if(to.getMinistro().getDsTelefone01() != null) to.getMinistro().setDsTelefone01(to.getMinistro().getDsTelefone01().trim());
		if(to.getMinistro().getDsTelefone02() != null) to.getMinistro().setDsTelefone02(to.getMinistro().getDsTelefone02().trim());
		if(to.getMinistro().getDsTelefone03() != null) to.getMinistro().setDsTelefone03(to.getMinistro().getDsTelefone03().trim());
		if(to.getMinistro().getNmNome() != null) to.getMinistro().setNmNome(to.getMinistro().getNmNome().trim());
		if(to.getMinistro().getDsCpf() != null) to.getMinistro().setDsCpf(to.getMinistro().getDsCpf().trim());
		if(to.getMinistro().getNmPai() != null) to.getMinistro().setNmPai(to.getMinistro().getNmPai().trim());
		if(to.getMinistro().getNmMae() != null) to.getMinistro().setNmMae(to.getMinistro().getNmMae().trim());
		if(to.getMinistro().getDsHistorico() != null) to.getMinistro().setDsHistorico(to.getMinistro().getDsHistorico().trim());

		// Passar email para minisculo (caixa baixa)
		if(to.getMinistro().getDsEmail() != null) to.getMinistro().setDsEmail(to.getMinistro().getDsEmail().toLowerCase());

	}


	private void validarPreenchimentoCamposObrigatorios(Protocolo to) {
		log.info("validarPreenchimentoCamposObrigatorios(Protocolo to)");
		StringBuilder dsErros = new StringBuilder();

		if(to.getMinistro().getDepartamento() == null )
			dsErros.append("Departamento - Campo de preenchimento obrigatório!\n");   

		if(to.getMinistro().getNmNome() == null || to.getMinistro().getNmNome().trim().length() == 0)
			dsErros.append("Nome do Candidato - Campo de preenchimento obrigatório!\n");

		if(dsErros.length() > 0)
			throw new NegocioException(dsErros.toString());

	}

	@Transactional
	public void remover(MinistroCandidatoListTO registro) {
		try {
			log.info("remover(" + registro.getSqProtocolo() + ")");
			// Atualizar o protocolo para que o JPA possa exclui-lo
			Protocolo protocolo = protocoloDAO.find(Protocolo.class, registro.getSqProtocolo());

			// Atualizar o Candidato para que o JPA possa exclui-lo
			Ministro ministro = ministroDAO.find(Ministro.class, protocolo.getMinistro().getSqMinistro());


			protocoloDAO.delete(protocolo, registro.getSqProtocolo());
			ministroDAO.delete(ministro, ministro.getSqMinistro());

		} catch (PersistenceException e) {
			throw new NegocioException("Protocolo não pode ser removido. ", e.getCause());
		}

	}

	@Transactional
	public void salvarProtocoloConclusao(Protocolo protocolo, String dsParecerTemp, int cdProtocoloConclusao ) { 

		try {
			log.info("salvarProtocoloConclusao(Protocolo:" + protocolo.getSqProtocolo() + ")");

			// Buscar ministro original
			Ministro ministroAtual = ministroDAO.find(Ministro.class, protocolo.getMinistro().getSqMinistro());

			if(ministroAtual.getCargo() == null || ministroAtual.getCargo().getSqCargo().compareTo(protocolo.getMinistro().getCargo().getSqCargo()) != 0 ) {
				ministroAtual.setCargo(protocolo.getMinistro().getCargo());
			}

			if(ministroAtual.getDtOrdenado() == null || !ministroAtual.getDtOrdenado().isEqual(protocolo.getMinistro().getDtOrdenado())) {
				ministroAtual.setDtOrdenado(protocolo.getMinistro().getDtOrdenado());
			}

			// Parecer -  Trocar <Enter> por <br /> para quebrar linha na exibição na tela em html
			MinistroParecer ministroParecer = new MinistroParecer();
			ministroParecer.setDsParecer(dsParecerTemp);
			ministroParecer.setDsResponsavel(Uteis.UsuarioLogado().getUsuario().getDsLogin());
			ministroParecer.setDtRegistro(Uteis.DataHoje());
			ministroParecer.setMinistro(ministroAtual);
			ministroParecer.setAuditoriaUsuario(Uteis.UsuarioLogado().getUsuario().getDsLogin());
			ministroParecer.setAuditoriaData(Uteis.DataHoje());

			ministroAtual.getMinistroParecer().add(ministroParecer);

			// Departamento - Buscar Departamento Confrateres para ajustar o ministro que será transferido
			if(cdProtocoloConclusao == 7) {
				Departamento departamento = this.departamentoDAO.find(Departamento.class, 1L);
				ministroAtual.setDepartamento(departamento);
			} else if(cdProtocoloConclusao == 8) {
				ministroAtual.setDtExcluido(Uteis.DataHoje().toLocalDate());
			}

			// Ajustes na auditoria de Ministro
			ministroAtual.setAuditoriaUsuario(Uteis.UsuarioLogado().getUsuario().getDsLogin());
			ministroAtual.setAuditoriaData(Uteis.DataHoje());

			// Ajustes no protocolo
			protocolo.setProtocoloStatus(protocoloStatusDAO.find(ProtocoloStatus.class, Long.valueOf(cdProtocoloConclusao)));
			protocolo.setAuditoriaUsuario(Uteis.UsuarioLogado().getUsuario().getDsLogin());
			protocolo.setAuditoriaData(Uteis.DataHoje());
			protocolo.setMinistro(ministroAtual);
			protocolo.setDsResponsavel(Uteis.UsuarioLogado().getUsuario().getDsLogin());
			protocolo.setDtStatus(Uteis.DataHoje().toLocalDate());

			// Salvar Protocolo
			protocoloDAO.salvar(protocolo);

		} catch (Exception e) {
			throw new NegocioException("Erro ao tentar concluír protocolo " + protocolo.getCdProtocolo(), e.getCause());
		}
	}

}
