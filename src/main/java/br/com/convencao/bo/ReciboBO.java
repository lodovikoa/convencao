package br.com.convencao.bo;

import java.math.BigDecimal;

import javax.inject.Inject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import br.com.convencao.dao.LancamentoDAO;
import br.com.convencao.dao.LancamentoResumoDAO;
import br.com.convencao.dao.ReciboDAO;
import br.com.convencao.model.Igreja;
import br.com.convencao.model.LancamentoResumo;
import br.com.convencao.model.Recibo;
import br.com.convencao.model.to.ReciboCpl;
import br.com.convencao.util.Uteis;
import br.com.convencao.util.jpa.Transactional;

public class ReciboBO extends LancamentoGenericoBO {

	private static final long serialVersionUID = 1L;
	
	private static Log log = LogFactory.getLog(ReciboBO.class);
	
	@Inject
	ReciboDAO reciboDAO;
	
	@Inject
	LancamentoResumoDAO lancamentoResumoDAO;
	
	@Inject
	LancamentoDAO lancamentoDAO;

	// Buscar recibo por NSU (Obs.: NSU é um código único no recibo, não pode haver NSU repetido)
	public Recibo findByPorNsu(Long cdNsu) {
		log.info("findByPorNsu(" + cdNsu + ")");
		
		return reciboDAO.findByPorNsu(cdNsu);
	}
	
	public Recibo findByPrimaryKey(Long sqRecibo) {
		log.info("findByPrimaryKey(" + sqRecibo + ")");
		
		return reciboDAO.find(Recibo.class, sqRecibo);
	}
	
	@Transactional
	public Recibo salvarEstornoEntrada(Recibo recibo, BigDecimal vlTotalRecebido) {
		try {
			log.info("salvar(NSU = " + recibo.getCdNsu() + ")");
			
			// Retirar espaços de inicio e fim
			recibo.setDsCancelado(recibo.getDsCancelado().trim());
			
			// Verificar se o recibo realmente existe e ainda não foi cancelado
			Recibo reciboTemp = reciboDAO.find(Recibo.class, recibo.getSqRecibo());
			if(reciboTemp == null) {
				throw new NegocioException("Rebido " + recibo.getCdNsu() + "não localizado. Cancelamento não realizado.");
			} else if (reciboTemp.getDtCancelado() != null) {
				throw new NegocioException("Rebido " + recibo.getCdNsu() + " já foi cancelado em " + reciboTemp.getDtCancelado() + " por " + reciboTemp.getAuditoriaUsuario() + ". Operação não realizada.");
			}
			
			// Validar preenchimento de motivo do cancelamento
			if(recibo.getDsCancelado().isEmpty())
				throw new NegocioException("Faltou preencher o motivo do cancelamento!");
			
			// identificar o resumo de acordo com a data de pagamento
			LancamentoResumo LancamentoResumoGravado = lancamentoResumoDAO.findByPorDataPagamento(recibo.getRegiao().getSqRegiao(), recibo.getDtRecibo());

			
			
			// Verificar se o resumo da data e região informada etá aberto.
			this.ajustarLancamentoResumo("ExtornoEntrada", recibo.getRegiao().getSqRegiao(), LancamentoResumoGravado.getSqResumo(), vlTotalRecebido, BigDecimal.ZERO, recibo.getDtRecibo());

			// Retirar data de Pagamento dos Lancamentos do Recibo Correspondente
			int iRetorno = lancamentoDAO.updateRetirarDataPagamento(recibo.getSqRecibo());
			if(iRetorno < 0) {
				throw new NegocioException("Erro ao tentar cancelar recibo número " + recibo.getCdNsu());
			}
			
			// Atualizar data de cancelamento e setar informações para auditoria
			recibo.setDtCancelado(Uteis.DataHoje());
			recibo.setAuditoriaData(Uteis.DataHoje());
			recibo.setAuditoriaUsuario(Uteis.UsuarioLogado().getUsuario().getDsLogin());
			
			return this.reciboDAO.salvar(recibo);
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new NegocioException("Erro ao salvar Recibo: " + e.getMessage());
		}
	}
	
	public ReciboCpl buscarDadosRecibo(Long sqRecibo) {
		log.info("buscarDadosRecibo( " + sqRecibo + ")");		
		return reciboDAO.buscarDadosRecibo(sqRecibo);
		
	}

	public Igreja buscarDadosIgrejaDoRecibo(long sqRecibo) {
		log.info("buscarDadosIgrejaDoRecibo( " + sqRecibo + ")");
		return reciboDAO.buscarDadosIgrejaDoRecibo(sqRecibo);
	}
}
