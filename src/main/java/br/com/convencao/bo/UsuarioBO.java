package br.com.convencao.bo;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import br.com.convencao.dao.UPermissaoDAO;
import br.com.convencao.dao.UsuarioDAO;
import br.com.convencao.model.Usuario;
import br.com.convencao.util.Uteis;
import br.com.convencao.util.jpa.Transactional;


public class UsuarioBO implements Serializable {

	private static final long serialVersionUID = 1L;

	private static Log log = LogFactory.getLog(UsuarioBO.class);

	@Inject
	private UsuarioDAO dao;
	
	@Inject
	private UPermissaoDAO uPermissaoDAO;

	// Método usado no momento de logar no sistema
	public Usuario findByLoginComGrupos(String dsLogin) {
		log.info("porLogin (" + dsLogin + ")");
		return this.dao.findByLoginComGrupos(dsLogin);
	}

	public List<Usuario> findAll() {
		log.info("UsuarioBO findAll()");
		return this.dao.findAll(Usuario.class);
	}

	public Usuario findByPrimaryKeyComGrupos(Long sq) {
		log.info("findByPrimaryKey (" + sq + ")");
		return dao.findByPrimaryKeyComGrupos(sq);
	}

	@Transactional
	public void remover(Usuario usuario) {
		log.info("remover (" + usuario.getDsLogin() + ")");

		// Verificar se usuário está associado a algum grupo
		usuario = dao.findByPrimaryKeyComGrupos(usuario.getSqUsuario());
		if(usuario.getGrupos() != null && usuario.getGrupos().size() > 0) {
			throw new NegocioException("Para remover o usuário, primeiro desassocie os grupos associados ( " + usuario.toStringGruposAssociadosAoUsuario() + " )" ); 
		}
		
		// Excluir as permissões do usuário em regiões, caso exista
		uPermissaoDAO.excluirPermissoes(usuario.getSqUsuario());

		// Excluir o usuário
		dao.delete(usuario, usuario.getSqUsuario());

	}

	@Transactional
	public Usuario salvar(Usuario usuario) {	
		try {	
			log.info("salvar (" + usuario.getDsLogin() + ")");

			// Limpar espaços
			if(StringUtils.isNotBlank(usuario.getDsEmail())) usuario.setDsEmail(usuario.getDsEmail().trim());
			if(StringUtils.isNotBlank(usuario.getDsLogin())) usuario.setDsLogin(usuario.getDsLogin().trim());
			if(StringUtils.isNotBlank(usuario.getDsNome())) usuario.setDsNome(usuario.getDsNome().trim());
			if(StringUtils.isNotBlank(usuario.getDsObs())) usuario.setDsObs(usuario.getDsObs().trim());

			//Fazer validações
			//Verificar se o login escolhido já existe
			Usuario usuarioTemp = dao.findByPorAtributo(Usuario.class, "dsLogin", usuario.getDsLogin());
			if(usuarioTemp != null && !usuarioTemp.getSqUsuario().equals(usuario.getSqUsuario())) {
				throw new NegocioException("Usuário (" + usuario.getDsLogin()  + ") já está cadastrado. Escolha outro nome de usuário");
			}

			// Tratamentos para inclusão e alteração
			if(usuario.getSqUsuario() == null) {
				// Criptografar senha
				usuario.setDsSenha(this.criptografarSenha(usuario.getDsSenha()));

				// Preencher data de cadastro
				usuario.setDtCadastro(Uteis.DataHoje().toLocalDate());
			}else {
				// Se for alteração Verificar se houve alteração
				Usuario usuarioAtual = dao.findByPrimaryKey(Usuario.class, usuario.getSqUsuario());
				if(usuarioAtual.equalsTO(usuario)){
					throw new NegocioException("Não houve alteração!");
				}
			}

			// Setar informações de auditoria
			usuario.setAuditoriaData(Uteis.DataHoje());
			usuario.setAuditoriaUsuario(Uteis.UsuarioLogado().getUsuario().getDsLogin());

			return dao.salvar(usuario);

		}  catch (Exception e) {
			e.printStackTrace();
			throw new NegocioException("Erro ao salvar usuario: " + e.getMessage());
		}
	}


	// Buscar registro usando find
	public Usuario find(Long sq) {
		log.info("find(" + sq + ")");
		return dao.find(Usuario.class, sq);
	}

	// Buscar usuário pelo login autenticado
	public Usuario findByLogin() {
		try {
			log.info("findByLogin()");

			return dao.findByPorAtributo(Usuario.class, "dsLogin", Uteis.UsuarioLogado().getUsuario().getDsLogin());

		}  catch (Exception e) {
			e.printStackTrace();
			throw new NegocioException("Erro ao buscar usuario pelo login: " + e.getMessage());
		}
	}

	@Transactional
	public void salvarTrocaSenha(Usuario usuario) {
		try {
			log.info("salvarTrocaSenha(" + usuario.getDsLogin()+ ")");

			// Verificar senha atual confere
			Usuario usuarioTemp = dao.find(Usuario.class, usuario.getSqUsuario());

			if(!BCrypt.checkpw(usuario.getDsSenhaConfirmarTroca(), usuarioTemp.getDsSenha())) {		
				throw new NegocioException("Senha atual não confere. Troca não realizada.");
			}

			// Verificar se a nova senha é diferente da senha atual
			if(BCrypt.checkpw(usuario.getDsSenha(), usuarioTemp.getDsSenha())) {
				throw new NegocioException("Senha nova é igual a senha atual. Alteração de senha não realizada.");
			}

			// Criptografar a nova senha
			usuario.setDsSenha(this.criptografarSenha(usuario.getDsSenha()));

			// Se a senha for provisória altera para definitiva
			if(usuario.isTrocaSenha())
				usuario.setTrocaSenha(false);

			dao.salvar(usuario);

		}  catch (Exception e) {
			e.printStackTrace();
			throw new NegocioException("Erro ao registrar troca de senha do usuário: " + e.getMessage());
		}
	}

	// Reiniciar senha do usuário
	@Transactional
	public Usuario salvarReiniciarSenha(Usuario usuario) {
		try {
			log.info("salvarReiniciarSenha(" + usuario.getDsLogin()+ ")");

			// Criptografar a nova senha
			usuario.setDsSenha(this.criptografarSenha(usuario.getDsSenha()));

			// Se a senha for definitiva altera para provisoria
			if(!usuario.isTrocaSenha())
				usuario.setTrocaSenha(true);

			return dao.salvar(usuario);

		}  catch (Exception e) {
			e.printStackTrace();
			throw new NegocioException("Erro ao reiniciar senha usuario: " + e.getMessage());
		}	
	}

	// Criptografar a senha
	private String criptografarSenha(String dsSenhaAberta) throws NegocioException {
		log.info("criptografarSenha()");

		// Se a senha digitada for menor que 8 caracteres não prosseguir
		if(dsSenhaAberta.length() < 8) {
			throw new NegocioException("Senha precisa ter no minimo 8 carateres");
		}

		return new BCryptPasswordEncoder().encode(dsSenhaAberta);
	}

	// Atribuir quantidade de login com senha errada e bloquear após QTDE_ERROS_SENHA_PERMITIDOS
	@Transactional
	public void errosSenhaUsuario(Long sqUsuario, Integer QTDE_ERROS_SENHA_PERMITIDOS) {
		try {
			log.info("errosSenhaUsuario(" + sqUsuario + " - " + QTDE_ERROS_SENHA_PERMITIDOS + ")");
			
			Usuario usuario = dao.find(Usuario.class, sqUsuario);
			if(usuario != null && usuario.isSituacao()) {
				Integer qtdeErros = usuario.getNnQuantidadeErros() == null? 0: usuario.getNnQuantidadeErros() + 1;
				
				usuario.setNnQuantidadeErros(qtdeErros);
				if(usuario.getNnQuantidadeErros() >= QTDE_ERROS_SENHA_PERMITIDOS) {
					usuario.setSituacao(false);
				}


				usuario = dao.salvar(usuario);
				
				log.info("errosSenhaUsuario(" + sqUsuario + " - quantidade registrada - " + usuario.getNnQuantidadeErros() + ")");

			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new NegocioException("Erro ao tentar registrar quantidade de erros de senha do usuario: " + e.getMessage());
		}

	}

	// Zerar quantidade de erros de senha 
	@Transactional
	public void zerarErrosSenhaUsuario(Long sqUsuario) {
		try {
			log.info("zerarErrosSenhaUsuario(" + sqUsuario + ")");
			
			Usuario usuario = dao.find(Usuario.class, sqUsuario);
			if(usuario != null) {
				usuario.setNnQuantidadeErros(0);
				
				usuario = dao.salvar(usuario);
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new NegocioException("Erro ao tentar zerar quantidade de erros de senha do usuario: " + e.getMessage());
		}
		
	}

}
