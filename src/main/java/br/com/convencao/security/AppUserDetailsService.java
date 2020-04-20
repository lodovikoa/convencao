package br.com.convencao.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import br.com.convencao.bo.NegocioException;
import br.com.convencao.bo.UsuarioBO;
import br.com.convencao.model.UGrupo;
import br.com.convencao.model.Usuario;
import br.com.convencao.util.cdi.CDIServiceLocator;
import br.com.convencao.util.jsf.FacesUtil;

public class AppUserDetailsService implements UserDetailsService {

	@Override
	public UserDetails loadUserByUsername(String dsLogin) throws UsernameNotFoundException  {
		UsuarioBO usuarioBO = CDIServiceLocator.getBean(UsuarioBO.class);

		Usuario usuario = usuarioBO.findByLoginComGrupos(dsLogin);

		if(usuario == null) {
			throw new UsernameNotFoundException("Usuário não encontado.");
		}
		
		UsuarioSistema user = new UsuarioSistema(usuario, getGrupos(usuario));

		return user;
	}

	private Collection<? extends GrantedAuthority> getGrupos(Usuario usuario) {
		List<SimpleGrantedAuthority> authorities = new ArrayList<>();

		if(usuario != null) {
			// Atribui usuario para registro de quantidade de tentativas com senha errada, para caso a senha esteja errada.
			FacesUtil.setSqUsuario(usuario.getSqUsuario());
			// Atribuir quantidade de erros de senha para caso o usuário acerte a senha, possibilitar zera-la.
			FacesUtil.setQtdeErrosSenha(usuario.getNnQuantidadeErros() == null? 0: usuario.getNnQuantidadeErros());

			// Verificar se usuário está bloqueado
			if(!usuario.isSituacao()) {
				FacesUtil.setUsuarioBloqueado(true);
				throw new NegocioException("Usuário bloqueado.");
			}

			// Verificar se é para trocar senha, caso afirmativo, não atribuir nenhum grupo ao usuário
			if(usuario.isTrocaSenha()) {
				FacesUtil.setUsuarioTrocarSenha(true);
			} else {
				for (UGrupo grupo : usuario.getGrupos()) {
					if(grupo.isSituacao())
						authorities.add(new SimpleGrantedAuthority("ROLE_" + grupo.getDsNome()));
				}
			}
		}
		return authorities;
	}


}
