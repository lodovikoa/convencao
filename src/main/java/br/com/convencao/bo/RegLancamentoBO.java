package br.com.convencao.bo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import br.com.convencao.dao.BoletoDAO;
import br.com.convencao.dao.LancamentoDAO;
import br.com.convencao.dao.RegLancamentoDAO;
import br.com.convencao.model.Boleto;
import br.com.convencao.model.Departamento;
import br.com.convencao.model.Lancamento;
import br.com.convencao.model.RegLancamento;
import br.com.convencao.model.Regiao;
import br.com.convencao.model.to.RegLancamentoCpl;
import br.com.convencao.model.to.RegLancamentoListLote;
import br.com.convencao.util.Uteis;
import br.com.convencao.util.jpa.Transactional;

// Registro de lançamentos
public class RegLancamentoBO extends GenericoBO {

	private static final long serialVersionUID = 1L;
	private static Log log = LogFactory.getLog(RegLancamentoBO.class);

	@Inject
	RegLancamentoDAO dao;

	@Inject
	LancamentoBO lancamentoBO;

	@Inject
	LancamentoDAO lancamentoDAO;

	@Inject
	BoletoDAO boletoDAO;

	@Inject
	MinistroBO ministroBO;

	//Listar lançamentos de um ministro
	public List<RegLancamentoCpl> findLancamentosMinistro(Long sqMinistro, Long cdNsu, boolean flLancamentoEmAberto, boolean flLancamentoPago){
		log.info("findLancamentosMinistro(sqMinistro=" + sqMinistro + " cdNsu=" + cdNsu + " flLancamentoEmAberto=" + flLancamentoEmAberto + " flLancamentoPago=" + flLancamentoPago + ")");
		return dao.findLancamentosMinistro(sqMinistro, cdNsu, flLancamentoEmAberto, flLancamentoPago);
	}

	@Transactional
	public void excluir(RegLancamento regLancamento, Boleto boleto) {
		try {
			log.info("excluir(" + regLancamento.getSqRegLancamento() + ")");

			// Se houver registro em Lancamento deve-se  inativar, caso contrário excluir.
			List<Lancamento> lancamento = lancamentoDAO.findAllLancamentosPorRegLancamento(regLancamento.getSqRegLancamento());


			//regLancamento = dao.findByPrimaryKey(regLancamento.getSqRegLancamento());
			regLancamento = dao.find(RegLancamento.class, regLancamento.getSqRegLancamento());



			//regLancamento = dao.findByPrimaryKey(regLancamento.getSqRegLancamento());
			regLancamento = dao.find(RegLancamento.class, regLancamento.getSqRegLancamento());


			if(lancamento.size() == 0) {
				// Excluir registro de lançamento
				if(boleto != null) {
					boletoDAO.delete(boletoDAO.find(Boleto.class, boleto.getSqBoleto()), boleto.getSqBoleto());
				}
				dao.delete(regLancamento, regLancamento.getSqRegLancamento());
			} else {
				// Verificar se todos os registros de pagamentos estão cancelados
				boolean todosCancelados = true;
				for (Lancamento to : lancamento) {
					if(to.getRecibo().getDtCancelado() == null) {
						todosCancelados = false;
						break;
					}
				}

				// Inativar registro de lançamento
				if(todosCancelados) {
					regLancamento.setDtCancelado(Uteis.DataHoje());
					regLancamento.setAuditoriaData(Uteis.DataHoje());
					regLancamento.setAuditoriaUsuario(Uteis.UsuarioLogado().getUsuario().getDsLogin());

					dao.salvar(regLancamento);
				} else {
					throw new NegocioException("Não foi possível excluir registro. Foi encontrado " + lancamento.size() + " lançamentos de pagamentos.");
				}
			}

		}	catch (Exception e) {
			e.printStackTrace();
			throw new NegocioException("Erro ao excluir registro: " + e.getMessage());
		}
	}

	@Transactional
	public void salvar(RegLancamento regLancamento) {
		try {
			log.info("salvar()");
			// Validar registro
			if(regLancamento.getTipoLancamento() == null || regLancamento.getTipoLancamento().getSqTipoLancamento() < 1)
				throw new NegocioException("Faltou selecionar o Tipo de Lançamento.");

			if(regLancamento.getDtVencimento() == null)
				throw new NegocioException("Faltou informar a data do lançamento.");

			if(regLancamento.getVlLancamento() == null || regLancamento.getVlLancamento().compareTo(BigDecimal.ZERO) <= 0)
				throw new NegocioException("Faltou informar o valor do lançamento.");

			// Verificação para registros alterados
			if(regLancamento.getSqRegLancamento() != null && regLancamento.getSqRegLancamento() > 0) {
				log.info("Alteração - salvar(" + regLancamento.getSqRegLancamento() + ")");

				// Buscar registro original
				RegLancamento regLancamentoOriginal = dao.findByPrimaryKey(regLancamento.getSqRegLancamento());

				// Validar registro repetido
				boolean result = regLancamentoOriginal.equalsTO(regLancamento);
				if(result)
					throw new NegocioException("Não houve alteração no registro.");

				// Se o valor foi alterado para menos buscar os pagamentos realizados para verificar se pode fazer a alteração do valor
				if(regLancamentoOriginal.getVlLancamento().compareTo(regLancamento.getVlLancamento()) > 0 ) {
					BigDecimal vlTotalRegLancamento = lancamentoBO.findValorTotalPorRegLancamento(regLancamento.getSqRegLancamento());

					if(regLancamento.getVlLancamento().compareTo(vlTotalRegLancamento) < 0) {
						throw new NegocioException("Alteração de valor não permitido, o valor alterado ficará menor que o valor já recebido.");
					}

				}

			} else {
				log.info("Inclusão - salvar(regLancamento)");
				regLancamento.setCdOrigem(1);
			}

			// Buscar lancamentos pagos - a alteraçao do valor para menos não pode ficar menor do que os valores já pagos


			regLancamento.setDtRegistro(Uteis.DataHoje());
			regLancamento.setAuditoriaUsuario(Uteis.UsuarioLogado().getUsuario().getDsLogin());
			regLancamento.setAuditoriaData(Uteis.DataHoje());

			dao.salvar(regLancamento);

		} catch (Exception e) {
			e.printStackTrace();
			throw new NegocioException("Erro ao salvar Registro de Lancamento: " + e.getMessage());
		}
	}

	@Transactional
	public StringBuilder salvarEmLoate(List<Regiao> regiaoSelecionados, RegLancamento regLancamentoTemp) {
		log.info("salvarEmLoate()");
		try {

			StringBuilder stRetorno = new StringBuilder();

			// Validações dos registros entrados em tela pelo usuário
			if(regiaoSelecionados.size() == 0)
				throw new NegocioException("Por favor, selecione as regiões.");

			if(regLancamentoTemp.getTipoLancamento() == null)
				throw new NegocioException("Por favor, selecione o Tipo de Lançamento.");

			if(regLancamentoTemp.getDtVencimento() == null)
				throw new NegocioException("Por favor, informe a data do Lançamento.");

			if(regLancamentoTemp.getVlLancamento().compareTo(BigDecimal.ZERO) <= 0)
				throw new NegocioException("Por favor, informe o valor do Lançamento.");

			if(regLancamentoTemp.getMinistro().getDepartamento() == null)
				throw new NegocioException("Departamento não identificado, informe ao Administrador.");

			List<Long> sqMinistros;
			boolean flMinistroAtivo = false;
			for (Regiao regiao : regiaoSelecionados) {

				sqMinistros = ministroBO.findMinistrosAtivosPorRegiao(regiao, regLancamentoTemp.getMinistro().getDepartamento());

				if(sqMinistros != null && sqMinistros.size() > 0) {
					dao.salvarEmLote(regiao, regLancamentoTemp, sqMinistros);
					flMinistroAtivo = true;
				}

				stRetorno.append(regiao.getDsRegiao() + " atualizou " + sqMinistros.size() + " Ministros - ");
			}

			if (!flMinistroAtivo)
				throw new NegocioException("As regiões selecionadas não tem ministros ativos.");

			stRetorno.setLength(stRetorno.length() - 3);

			return stRetorno;

		} catch (Exception e) {
			e.printStackTrace();
			throw new NegocioException("Erro ao salvar lote de registros: " + e.getMessage());
		}

	}


	public RegLancamento findByPrimaryKey(Long sqRegLancamento) {
		log.info("findByPrimaryKey(" + sqRegLancamento + ")");
		return dao.findByPrimaryKey(sqRegLancamento);
	}

	// Listar registros em lote que estão em aberto.
	public List<RegLancamentoListLote> findLancamentosEmLoteEmAberto(Integer cdOrigem, Long sqDepartamento) {
		log.info("findLancamentosEmLoteEmAberto(cdOrigem = (" + cdOrigem + ") sqDepartamento = (" + sqDepartamento +"))");

		List<RegLancamentoListLote> result = dao.findLancamentosEmLoteEmAberto(cdOrigem, sqDepartamento);

		return result;
	}

	@Transactional
	public StringBuilder salvarExcluirEmLote(Departamento departamento, List<RegLancamentoListLote> listaEmLoteSelecionada) {
		log.info("salvarExcluirEmLote()");

		List<Long> sqRegLancamentoLista = new ArrayList<>();
		List<Long> sqRegLancamentoListaCancelados = new ArrayList<>();
		List<Long> sqRegLancamentoListaTemp = null;
		List<Long> sqRegLancamentoListaCanceladosTemp = null;
		// Buscar lista de sqRegLancamento para fazer a exclusão
		for (RegLancamentoListLote listLote : listaEmLoteSelecionada) {
			log.info("Buscar lancamentos para exclusão que não há recibo cancelado Data:" + listLote.getDtVencimento() + " sqTipoLancamento: " +  listLote.getSqTipoLancamento() + " sqRegiao: " + listLote.getSqRegiao() + " sqDepartamento: " + departamento.getSqDepartamento());
			sqRegLancamentoListaTemp = dao.findLancamentosEmAbertoParaExclusao(1, listLote.getDtVencimento(), listLote.getSqTipoLancamento(), listLote.getSqRegiao(), departamento.getSqDepartamento());
			log.info("Buscar lancamentos para cancelamento que todos os recibo estão cancelados Data:" + listLote.getDtVencimento() + " sqTipoLancamento: " +  listLote.getSqTipoLancamento() + " sqRegiao: " + listLote.getSqRegiao() + " sqDepartamento: " + departamento.getSqDepartamento());
			sqRegLancamentoListaCanceladosTemp = dao.findLancamentosEmAbertoParaCancelar(1, listLote.getDtVencimento(), listLote.getSqTipoLancamento(), listLote.getSqRegiao(), departamento.getSqDepartamento());

			for (Long l : sqRegLancamentoListaTemp) {
				sqRegLancamentoLista.add(l);
			}

			for (Long l : sqRegLancamentoListaCanceladosTemp) {
				sqRegLancamentoListaCancelados.add(l);
			}

		}
		
		int qtdeExcluido = 0;
		int qtdeCancelado = 0;
		StringBuilder resultado = new StringBuilder();
		
		// Fazer a exclusão dos registros em lote
		if(sqRegLancamentoLista != null && sqRegLancamentoLista.size() > 0) {
			qtdeExcluido = dao.salvarDeleteEmLote(sqRegLancamentoLista);
			resultado.append(" Registros excluídos: " + qtdeExcluido);
		}

		// Fazer p cancelamento dos registros em lote
		if(sqRegLancamentoListaCancelados != null && sqRegLancamentoListaCancelados.size() > 0) {
			qtdeCancelado = dao.salvarUpdateEmLote(sqRegLancamentoListaCancelados);
			resultado.append(" Registros cancelados: " + qtdeCancelado);
		}
		
		return resultado;
	}

}
