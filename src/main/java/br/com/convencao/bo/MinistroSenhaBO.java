package br.com.convencao.bo;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import br.com.convencao.dao.MinistroSenhaDAO;
import br.com.convencao.model.MinistroSenha;
import br.com.convencao.util.Uteis;
import br.com.convencao.util.jpa.Transactional;
import sun.misc.BASE64Encoder;


@SuppressWarnings("restriction")
public class MinistroSenhaBO extends GenericoBO{

	private static final long serialVersionUID = 1L;
	private static Log log = LogFactory.getLog(MinistroSenhaBO.class);

	@Inject
	MinistroSenhaDAO dao;

	public String findByMinistroUltimoRegistro(Long sqMinistro) {
		log.info("findByMinistroUltimoRegistro(" + sqMinistro + ")");

		String dsSituacaoAtual = "";

		MinistroSenha ministroSenha = dao.findByMinistroUltimoRegistro(sqMinistro);

		if(ministroSenha == null) {
			dsSituacaoAtual = "Nenhuma senha registrada.";
		}else if(!ministroSenha.isFlAtivo()) {
			dsSituacaoAtual = "Senha bloqueada, houve " + ministroSenha.getNnQtdeErro() + " tentativas seguida com senha errada.";
		}else if(ministroSenha.isFlProvisoria()) {
			dsSituacaoAtual = "Senha provisória registrada em " + this.dataToString(ministroSenha.getDtSenha(), "dd/MM/yyyy")  + " por " + ministroSenha.getAuditoriaUsuario();
		}else if(ministroSenha.isFlAtivo()) {
			dsSituacaoAtual = "Senha ativa.";
		}else {
			dsSituacaoAtual = "Situação da senha não identificada...";
		}


		return dsSituacaoAtual;
	}

	@Transactional
	public MinistroSenha criarPasswordProvisoriaMinistro(MinistroSenha ministroSenha) {
		try {
			log.info("criarPasswordProvisoriaMinistro(" + ministroSenha.getMinistro().getSqMinistro() + ")");
			
			// Validar preenchimento das senhas
			if(StringUtils.isBlank(ministroSenha.getDsSenhaTemp1()) || StringUtils.isBlank(ministroSenha.getDsSenhaTemp2())) {
				throw new NegocioException("Senha provisória e Confirmar senha: Preechimento obrigatório");
			}else if(!ministroSenha.getDsSenhaTemp1().equals(ministroSenha.getDsSenhaTemp2())){
				throw new NegocioException("Senha provisória  diferente de Confirmar senha");
			}else if(ministroSenha.getDsSenhaTemp1().length() < 8) {
				throw new NegocioException("Senha precisa ter no mínimo 8 caracteres");
			}


			// Encriptar senha
			ministroSenha.setDsSenha(Criptografia.criptografar(ministroSenha.getDsSenhaTemp1()));
			ministroSenha.setDsSenha(this.criptografar(ministroSenha.getDsSenhaTemp1()));
			
			// Setar dados de auditoria
			ministroSenha.setAuditoriaData(Uteis.DataHoje());
			ministroSenha.setAuditoriaUsuario(Uteis.UsuarioLogado().getUsuario().getDsLogin());

			// Grava registro no banco de dados
			MinistroSenha result = dao.salvar(ministroSenha);
			
			return result;
		} catch(Exception e){
			throw new NegocioException("Erro ao tentar salvar Senha do Ministro: " + e.getMessage(), e);
		}

	}

	// Criptogragar senha
	private String criptografar (String senha) {     
		try {
			if(senha == null)
				return senha;

			MessageDigest digest = MessageDigest.getInstance("MD5");      
			digest.update(senha.getBytes());      
			BASE64Encoder encoder = new BASE64Encoder();

	
			return encoder.encode (digest.digest());      

		} catch (NoSuchAlgorithmException ns) {     
			ns.printStackTrace ();      
			return senha;      
		} 

	}
	
	

}
