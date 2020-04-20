package br.com.convencao.bo;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.PersistenceException;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import br.com.convencao.bean.cadastro.MinistroFiltro;
import br.com.convencao.dao.IgrejaDAO;
import br.com.convencao.dao.MinistroAnexoDAO;
import br.com.convencao.dao.MinistroDAO;
import br.com.convencao.model.Departamento;
import br.com.convencao.model.Igreja;
import br.com.convencao.model.Ministro;
import br.com.convencao.model.MinistroAnexo;
import br.com.convencao.model.Regiao;
import br.com.convencao.model.to.MinistroPorAnoTO;
import br.com.convencao.model.to.MinistroPorRegiaoTO;
import br.com.convencao.model.to.MinistroRecebimentoCpl;
import br.com.convencao.util.Uteis;
import br.com.convencao.util.jpa.Transactional;

public class MinistroBO extends GenericoBO {

	private static final long serialVersionUID = 1L;
	private static Log log = LogFactory.getLog(MinistroBO.class);

	@Inject
	private MinistroDAO dao;

	@Inject
	private IgrejaDAO igrejaDAO;
	
	@Inject
	private MinistroAnexoDAO ministroAnexoDAO;

	@Inject
	private FotoBO fotoBO;
	
	@Inject
	private AnexosBO anexosBO;
	
	
	// Buscar Ministro pela primary key
	public Ministro findByPrimaryKey(Long sqMinistro){
		log.info("findByPrimaryKey(" + sqMinistro + ")");
		return dao.findByPrimaryKey(sqMinistro);
	}

	// Buscar registro pela primary key usando find.
	public Ministro find(Long sq) {
		log.info("find(" + sq + ")");
		return dao.find(Ministro.class, sq);
	}

	// Buscar todos os ministros de todos os departamentos de uma igreja
	public List<Ministro> findMinistrosByIgreja(Long sqIgreja){
		log.info("findMinistrosByIgreja(" + sqIgreja + ")");
		return dao.findMinistrosByIgreja(sqIgreja);
	}

	// Buscar todos os ministros de um dos departamentos de UMA IGREJA.
	public List<Ministro> findMinistrosByIgrejaByDepartamento(Long sqIgreja, Long sqDepartamento){
		log.info("findMinistrosByIgrejaByDepartamento(" + sqIgreja + ", " + sqDepartamento + ")");
		return dao.findMinistrosByIgrejaByDepartamento(sqIgreja, sqDepartamento);
	}

	// Buscar ministros de acordo com filto inforado
	public List<Ministro> findByParametros(MinistroFiltro filtro) {	
		log.info("findByParametros(Filtro filtro)");
				
		List<Ministro> result = dao.findByParametros(filtro);
		return result;
	}

	// Buscar esposas de ministros
	public List<Ministro> findEsposasMinistro(){
		log.info("findEsposasMinistro(2)");
		List<Ministro> result = dao.findEsposasMinistro(2L);
		return result;
	}

	@Transactional
	public Ministro salvar(Ministro ministro) {
		log.info("salvar(" + ministro.getSqMinistro() + ")");
		try{
			//Identificar se é cadastro de novo ministro
			boolean flCadastro = false;
			Ministro ministroTemp = null;
			if(ministro.getSqMinistro() == null) { 
				flCadastro = true;
			} else {
				ministroTemp = dao.findByPrimaryKey(ministro.getSqMinistro());
				
				if(ministroTemp.getDepartamento().getSqDepartamento() == 1 && ministro.getDepartamento().getSqDepartamento() != 1) {
					throw new NegocioException("Não é permitido alterar Departamento de ministro da CONFRATERES.");
				}
			}


			// Ajustar strings
			this.ajustarStrings(ministro);

			//Buscar último código registrado para departamento que não é da confrateres para inclusões de novos "Ministros"
			if(flCadastro && ministro.getDepartamento().getSqDepartamento() != 1) {
				Long cdCodigoTemp = this.ultimoCodigoRegisgtrado(ministro);

				ministro.setCdCodigo(cdCodigoTemp);
			}

			// Validar campos obrigatórios
			this.validarPreenchimentoCamposObrigatorios(ministro);

			// Validar CPF já usado por outro ministro
			if(ministro.getDsCpf() != null && ministro.getDsCpf().trim().length() > 0){
				Ministro ministroCPF = dao.validarCpf(ministro.getSqMinistro(), ministro.getDsCpf());
				if(ministroCPF != null){
					throw new NegocioException("CPF: " + ministro.getDsCpf() + " já usado para o  ministro " + ministroCPF.getCdCodigo() + " - " + ministroCPF.getNmNome());
				}
			}

			// Validar esposa do ministro já encontra-se registrada como esposa de outro ministro.
			if(ministro.getConjuge() != null){
				Ministro validarEsposa = dao.validarConjuge(ministro.getSqMinistro(), ministro.getConjuge().getSqMinistro());
				if(validarEsposa != null){
					throw new NegocioException(ministro.getConjuge().getNmNome() + " encontra-se registrada como esposa do ministro " + validarEsposa.getCdCodigo() + " - " + validarEsposa.getNmNome());
				}
			}

			// Validar código para inclusão de ministros da confrateres, tipo 1
			if(ministro.getDepartamento().getSqDepartamento() == 1) this.validarCodigoPessoa(ministro, ministroTemp);

			//Validar Ministro é presidente de outra Igreja
			if(!flCadastro)
				this.validarPastorPresidente(ministro);

			this.incrementarHistorico(ministro);

			// Verificar se houve alteração
			if(!flCadastro){
				if(ministro.equalsTO(ministroTemp))
					throw new NegocioException("Não houve alteração!");

				if(StringUtils.isNotEmpty(ministroTemp.getDsFoto()) && !ministroTemp.getDsFoto().equals(ministro.getDsFoto()) ) {
					try {
						fotoBO.deletar(ministroTemp.getDsFoto());
					}catch (IOException e) {
						throw new NegocioException("Problema ao tentar remover foto antiga", e);
					}
				}
			}

			try {
				fotoBO.recuperarFotoTemporaria(ministro.getDsFoto(), ministro.getCdCodigo());
			} catch (IOException e) {
				throw new NegocioException("Problema ao tentar recuperar foto temporária.", e);
			}


			// Setar dados de auditoria
			ministro.setAuditoriaData(Uteis.DataHoje());
			ministro.setAuditoriaUsuario(Uteis.UsuarioLogado().getUsuario().getDsLogin());

			return dao.salvar(ministro);

		} catch(Exception e){
			throw new NegocioException("Erro ao tentar salvar Ministro: " + e.getMessage(), e);
		}
	}


	// Desativar ministro
	@Transactional
	public Ministro desativarMinistro(Ministro ministro) {
		try {
			log.info("desativarMinistro(" + ministro.getSqMinistro() + ")");

			// Validar data de desativação que não pode ser nulla
			if(ministro.getDtExcluido() == null){
				throw new NegocioException("Data de desativação - Preenchimento obrigatório.");
			}

			// Setar dados de auditoria
			ministro.setAuditoriaData(Uteis.DataHoje());
			ministro.setAuditoriaUsuario(Uteis.UsuarioLogado().getUsuario().getDsLogin());

			return dao.salvar(ministro);

		} catch(Exception e){
			throw new NegocioException("Erro ao tentar desativar Ministro: " + e.getMessage(), e);
		}

	}

	// Reativar ministro
	@Transactional
	public Ministro reativarMinistro(Ministro ministro) {
		try {
			log.info("reativarMinistro(" + ministro.getSqMinistro() + ")");

			ministro.setDtExcluido(null);

			// Setar dados de auditoria
			ministro.setAuditoriaData(Uteis.DataHoje());
			ministro.setAuditoriaUsuario(Uteis.UsuarioLogado().getUsuario().getDsLogin());

			return dao.salvar(ministro);

		} catch(Exception e){
			throw new NegocioException("Erro ao tentar reativar Ministro: " + e.getMessage(), e);
		}
	}

	// Incluir histórico do ministro
	@Transactional
	public Ministro registrarHistoricoMinistro(Ministro ministro) {
		try {
			log.info("incluirHistoricoMinistro(" + ministro.getSqMinistro() + ")");

			if(StringUtils.isBlank(ministro.getDsHistoricoTemp())){
				throw new NegocioException("Incluir histórico - Preenchimento obrigatório.");
			}

			this.incrementarHistorico(ministro);

			// Limpar historico temporario
			ministro.setDsHistoricoTemp(null);

			// Setar dados de auditoria
			ministro.setAuditoriaData(Uteis.DataHoje());
			ministro.setAuditoriaUsuario(Uteis.UsuarioLogado().getUsuario().getDsLogin());

			return dao.salvar(ministro);

		} catch(Exception e){
			throw new NegocioException("Erro ao tentar registrar histórico do Ministro: " + e.getMessage(), e);
		}
	}

	// Excluir Ministro
	@Transactional
	public void remover(Ministro registro) {
		try{
			log.info("remover(" + registro.getSqMinistro() + ")");

			// TODO Validar exclusão de ministro

			// Atualizar o registro para que o JPA possa exclui-lo
			registro = this.findByPrimaryKey(registro.getSqMinistro());

			dao.delete(registro, registro.getSqMinistro());

		}catch (PersistenceException e) {
			throw new NegocioException("Ministro não pode ser removido.", e.getCause());
		}

	}

	// Buscar último codigo registrado para o ministro de acordo com seu departamento
	public Long ultimoCodigoRegisgtrado(Ministro ministro) {
		log.info("ultimoCodigoRegisgtrado(" + ministro.getSqMinistro() +  ")");
		
		return dao.ultimoCodigoRegistrado(ministro.getDepartamento()) + 1;
	}

	private void ajustarStrings(Ministro to) {
		log.info("ajustarStrings(Ministro to)");

		if(to.getDsBairro() != null) to.setDsBairro(to.getDsBairro().trim());
		if(to.getDsCidade() != null) to.setDsCidade(to.getDsCidade().trim());
		if(to.getDsEmail() != null)  to.setDsEmail(to.getDsEmail().trim());
		if(to.getDsEndereco() != null) to.setDsEndereco(to.getDsEndereco().trim());
		if(to.getDsIdentidade() != null) to.setDsIdentidade(to.getDsIdentidade().trim());
		if(to.getDsLocalNascimento() != null) to.setDsLocalNascimento(to.getDsLocalNascimento().trim());
		if(to.getDsTelefone01() != null) to.setDsTelefone01(to.getDsTelefone01().trim());
		if(to.getDsTelefone02() != null) to.setDsTelefone02(to.getDsTelefone02().trim());
		if(to.getDsTelefone03() != null) to.setDsTelefone03(to.getDsTelefone03().trim());
		if(to.getNmNome() != null) to.setNmNome(to.getNmNome().trim());
		if(to.getDsCpf() != null) to.setDsCpf(to.getDsCpf().trim());
		if(to.getNmPai() != null) to.setNmPai(to.getNmPai().trim());
		if(to.getNmMae() != null) to.setNmMae(to.getNmMae().trim());
		if(to.getDsHistorico() != null) to.setDsHistorico(to.getDsHistorico().trim());

		// Passar email para minisculo (caixa baixa)
		if(to.getDsEmail() != null) to.setDsEmail(to.getDsEmail().toLowerCase());

	}

	private void validarPreenchimentoCamposObrigatorios(Ministro to) {
		log.info("validarPreenchimentoCamposObrigatorios(Ministro to)");
		StringBuilder dsErros = new StringBuilder();

		if(to.getDepartamento() == null )
			dsErros.append("Departamento - Campo de preenchimento obrigatório!\n");   
		if(to.getCdCodigo() == null  || to.getCdCodigo() == 0)
			dsErros.append("Rg - Campo de preenchimento obrigatório!\n");
		if(to.getNmNome() == null || to.getNmNome().trim().length() == 0)
			dsErros.append("Nome Ministro - Campo de preenchimento obrigatório!\n");
		//		if(to.getIgreja() == null)
		//			dsErros.append("Igreja sede - Campo de preenchimento obrigatório!");

		if(dsErros.length() > 0)
			throw new NegocioException(dsErros.toString());
	}

	private void validarCodigoPessoa(Ministro to, Ministro ministroOriginal) {
		log.info("validarCodigoPessoa(" + to.getCdCodigo() + ")");

		Boolean codigoJaCadastrado = dao.findByPorCodigoDepartamento(to.getCdCodigo(), to.getDepartamento());

		if(to.getSqMinistro() != null) {	
			if((ministroOriginal.getCdCodigo().longValue() != to.getCdCodigo().longValue() 
					|| (ministroOriginal.getDepartamento().getSqDepartamento().longValue() != to.getDepartamento().getSqDepartamento().longValue() ))
					&& codigoJaCadastrado) {
				throw new NegocioException("Rg do Ministro já está cadastrado!");
			}
		} else if(codigoJaCadastrado){
			throw new NegocioException("Rg do Ministro já está cadastrado!");
		}  	
	}

	private void validarPastorPresidente(Ministro ministro) {
		log.info("validarPastorPresidente(" + ministro.getCdCodigo() + "- " + ministro.getNmNome() + " Ministro ministro)");

		if(ministro.getIgreja() == null)
			return;


		Igreja igrejaTemp = igrejaDAO.findByPastorPresidente(ministro.getIgreja().getSqIgreja(), ministro);

		if(igrejaTemp != null){
			throw new NegocioException("Ministro é presidente da Igreja " + igrejaTemp.getDsIgreja() + ", favor verificar! - Operação não permitida!");
		}

	}

	private void incrementarHistorico(Ministro ministro) {
		log.info("incrementarHistorico(" + ministro.getSqMinistro() + ")");
		// Incrementar histórico
		if(StringUtils.isNotBlank(ministro.getDsHistoricoTemp())){
			ministro.setDsHistorico(ministro.getDsHistoricoTemp() + "\n\n" + ministro.getDsHistorico());
		}
	}

	public List<MinistroRecebimentoCpl> findMinistrosForRecebimento(MinistroFiltro filtro) {
		log.info("findMinistrosForRecebimento()");
		
		// Validar preenchimento dos filtros
		List<MinistroRecebimentoCpl> result = dao.findMinistrosForRecebimento(filtro);
		
		return result;
	}

	public MinistroRecebimentoCpl findMinistrosRecebimentoForPrimaryKey(long sqMinistro) {
		log.info("findMinistrosRecebimentoForPrimaryKey(" + sqMinistro + ")");
		
		MinistroRecebimentoCpl result = dao.findMinistrosRecebimentoForPrimaryKey(sqMinistro);
		
		return result;
	}	
	
	public List<Long> findMinistrosAtivosPorRegiao(Regiao regiao, Departamento departamento) {
		log.info("findMinistrosAtivosPorRegiao(" + regiao.getSqRegiao() + ")");
		
		List<Long> result = dao.findMinistrosAtivosPorRegiao(regiao, departamento);
		
		return result;
	}

	public Long findCodigoDepartamento(Long sqMinistro) {
		log.info("findCodigoDepartamento(" + sqMinistro + ")");
		return dao.findCodigoDepartamento(sqMinistro);
	}

	public List<MinistroPorRegiaoTO> findMinistrosPorRegiao() {
		log.info("findMinistrosPorRegiao()");
		return dao.findMinistrosPorRegiao();
	}

	public List<MinistroPorAnoTO> findMinistroPorAnoNovos(int anoInicio, int anoAtual) {
		log.info("findMinistroPorAnoNovos(" + anoInicio + " - " + anoAtual + ")");
		return dao.findMinistrosPOrAnoNovos(anoInicio, anoAtual);
	}

	public List<MinistroPorAnoTO> findMinistroPorAnoExcluidos(int anoInicio, int anoAtual) {
		log.info("findMinistroPorAnoExcluidos(" + anoInicio + " - " + anoAtual + ")");
		return dao.findMinistrosPOrAnoExcluidos(anoInicio, anoAtual);
	}

	// Salvar anexo do ministro(MinistroBO) e Salvar anexo do protocolo(MinisgtroCandidato) ((Atualizar para realizar esta tarefa em um único lugar))
	@Transactional
	public void salvarAnexosNaAlteracao(Ministro ministro, MinistroAnexo ministroAnexo) {
		try {
			log.info("salvarAnexosNaAlteracao(" + ministro.getSqMinistro() + " - Anexo novo: " + ministroAnexo.getDsAnexoOriginal() + ")");
			
			String dsRetornoAnexo = anexosBO.recuperarAnexoTemporario(ministroAnexo.getDsAnexoRenomeado());
			
			if(StringUtils.isNotEmpty(dsRetornoAnexo)) {
				log.info("Anexo para o ministro (" + ministro.getSqMinistro() + ") não foi salvo: (" + dsRetornoAnexo + ")");
				throw new NegocioException("Anexo não foi salvo: " + dsRetornoAnexo);
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

	// Remover anexo na alteração de Ministro
	@Transactional
	public String salvarExcluirAnexoNaAlteracao(Long attribute_sqMinistroAnexo) {
		log.info("salvarExcluirAnexoNaAlteracao (" + attribute_sqMinistroAnexo + ")");

		// Buscar dados do anexo que será excluido.
		MinistroAnexo ministroAnexo = ministroAnexoDAO.find(MinistroAnexo.class, attribute_sqMinistroAnexo);

		try {
			// Excluir o registro do anexo no bd
			ministroAnexoDAO.delete(ministroAnexo, ministroAnexo.getSqMinistroAnexo());

			// Remover o anexo
			anexosBO.deletar(ministroAnexo.getDsAnexoRenomeado());

			return ministroAnexo.getDsAnexoOriginal();
		} catch (Exception e) {
			throw new NegocioException("Problemas ao tentar remover anexo (" + ministroAnexo.getDsAnexoOriginal() + "): " + e.getMessage());
		}
	}

}
