package br.com.convencao.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {


	@Bean
	public AppUserDetailsService userDetailsService() {
		return new AppUserDetailsService();
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}


	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		JsfLoginUrlAuthenticationEntryPoint jsfLoginEntry = new JsfLoginUrlAuthenticationEntryPoint();
		jsfLoginEntry.setLoginFormUrl("/Login.xhtml");
		jsfLoginEntry.setRedirectStrategy(new JsfRedirectStrategy());

		JsfAccessDeniedHandler jsfDeniedEntry = new JsfAccessDeniedHandler();
		jsfDeniedEntry.setLoginPath("/AcessoNegado.xhtml");
		jsfDeniedEntry.setContextRelative(true);

		http
		.csrf()
		.disable()
		.headers()
		.frameOptions()
		.sameOrigin()
		.and()
		
		.authorizeHttpRequests()
		.requestMatchers("/Login.xhtml", "/Erro.xhtml", "/javax.faces.resource/**").permitAll()
		.requestMatchers("/Home.xhtml", 
				"/AcessoNegado.xhtml", 
				"/dialogos/**", 
				"/resources/**",
				"/pages/usuario/UsuarioTrocaSenha.xhtml"
				).authenticated()

		.requestMatchers("/ministro-foto", 
				"/relatorioFichaCadastral", 
				"/relatorioMinistroGeral",
				"/relatorioMinistroPrPresidente",
				"/relatorioMinistroAniversariantes",
				"/relatorioMinistroPorIgreja",
				"/relatorioFinanceiroRecibo",
				"/relatorioFinanceiroResumo",
				"/relatorioFinanceiroEntradaPeriodo",
				"/relatorioFinanceiroEntradaMinistro",
				"/relatorioFinanceiroEntradaRecibo",
				"/relatorioFinanceiroEntradaFormaRecebimento",
				"/relatorioFinanceiroEntradaPendente",
				"/relatorioFinanceiroSaidaPeriodo",
				"/relatorioFinanceiroCartaCobranca",
				"/subRetratoPageFooter",
				"/subRetratoPageHeader")
				.hasAnyRole("guCftAdministrador", "guCftConsulta", "guCftFinanceiroLocal", "guCftFinanceiroRegiao", "guCftSecretarioConvencao", "guCftSecretarioRegiao")

		// Permissões de: 
		.requestMatchers("/pages/auditoria/**",
				"/pages/usuario/**",
				"/pages/configuracao/ConvencaoPesquisa.xhtml",
				"/pages/configuracao/ConvencaoCadastro.xhtml",
				"/pages/configuracao/PlanoContaPesquisa.xhtml",
				"/pages/configuracao/PlanoContaCadastro.xhtml",
				"/pages/configuracao/TipoLancamentoPesquisa.xhtml",
				"/pages/configuracao/TipoLancamentoCadastro.xhtml",
				"/pages/financeiro/entrada/LancamentoEntradaCreateEmLote.xhtml",
				"/pages/financeiro/entrada/LancamentoEntradaDeleteEmLote.xhtml",
				"/pages/financeiro/entrada/LancamentoEntradaRecebimentoCancelar.xhtml")
				.hasAnyRole("guCftAdministrador")

		.requestMatchers("/pages/configuracao/CargoPesquisa.xhtml",
				"/pages/configuracao/CargoCadastro.xhtml",
				"/pages/configuracao/DepartamentoPesquisa.xhtml",
				"/pages/configuracao/DepartamentoCadastro.xhtml",
				"/pages/configuracao/EscolaridadePesquisa.xhtml",
				"/pages/configuracao/EscolaridadeCadastro.xhtml",
				"/pages/configuracao/EstadoPesquisa.xhtml",
				"/pages/configuracao/EstadoCadastro.xhtml",
				"/pages/configuracao/EstadoCivelPesquisa.xhtml",
				"/pages/configuracao/EstadoCivelCadastro.xhtml",
				"/pages/configuracao/ProfissaoPesquisa.xhtml",
				"/pages/configuracao/ProfissaoCadastro.xhtml",
				"/pages/configuracao/RegiaoPesquisa.xhtml",
				"/pages/configuracao/RegiaoCadastro.xhtml")
				.hasAnyRole("guCftAdministrador", "guCftSecretarioConvencao")

		// Permissões de:
		.requestMatchers("/pages/cadastro/IgrejaPesquisa.xhtml", 
				"/pages/cadastro/IgrejaRetrive.xhtml",
				"/pages/cadastro/MinistroPesquisa.xhtml", 
				"/pages/cadastro/MinistroRetrive.xhtml", 
				"/pages/cadastro/MinistroCandidatoPesquisa.xhtml",
				"/pages/cadastro/MinistroCandidatoRetrive.xhtml")
				.hasAnyRole("guCftAdministrador", "guCftConsulta", "guCftFinanceiroLocal", "guCftFinanceiroRegiao", "guCftSecretarioConvencao", "guCftSecretarioRegiao")

		// Permissões de: 
		.requestMatchers("/pages/cadastro/MinistroCadastro.xhtml",
				"/pages/cadastro/MinistroCandidatoCadastro.xhtml")
				.hasAnyRole("guCftAdministrador", "guCftSecretarioConvencao", "guCftSecretarioRegiao")

		.requestMatchers("/pages/cadastro/IgrejaCadastro.xhtml")
				.hasAnyRole("guCftAdministrador", "guCftSecretarioConvencao")

		.requestMatchers("/pages/financeiro/entrada/LancamentoEntradaIgrejaCreate.xhtml",
				"/pages/financeiro/entrada/LancamentoEntradaIgrejaList.xhtml",
				"/pages/financeiro/entrada/LancamentoEntradaIgrejaOutrosList.xhtml",
				"/pages/financeiro/entrada/LancamentoEntradaPessoasList.xhtml",
				"/pages/financeiro/entrada/LancamentoEntradaRecebimentoRetrive.xhtml",
				"/pages/financeiro/entrada/LancamentoEntradaRecebimentosList.xhtml",
				"/pages/financeiro/saida/LancamentoSaidaList.xhtml")
				.hasAnyRole("guCftAdministrador", "guCftFinanceiroLocal", "guCftFinanceiroRegiao")

		// Permissões de: 
		.requestMatchers("/pages/financeiro/resumo/LancamentoResumoList.xhtml")
				.hasAnyRole("guCftAdministrador", "guCftFinanceiroRegiao")

		.requestMatchers("/pages/relatorio/RptFinanceiroLancamentosEntradaMinistro.xhtml",
				"/pages/relatorio/RptFinanceiroLancamentosEntradaPendente.xhtml",
				"/pages/relatorio/RptFinanceiroLancamentosEntradaPeriodo.xhtml",
				"/pages/relatorio/RptFinanceiroLancamentosEntradaRecibo.xhtml",
				"/pages/relatorio/RptFinanceiroLancamentosSaidaPeriodo.xhtml",
				"/pages/relatorio/RptFinanceiroResumo.xhtml",
				"/pages/relatorio/RptFinanceiroLancamentosEntradaFormaRecebimento.xhtml",
				"/pages/relatorio/RptFinanceiroCartaCobranca.xhtml",
				"/pages/financeiro/autoRegistro/AgoReciboList.xhtml")
				.hasAnyRole("guCftAdministrador", "guCftConsulta", "guCftFinanceiroLocal", "guCftFinanceiroRegiao")

		.requestMatchers("/pages/relatorio/MinistroRelatorioGeral.xhtml",
				"/pages/relatorio/MinistroRelatorioPorIgreja.xhtml",
				"/pages/relatorio/MinistroRelatorioPrPresidente.xhtml",
				"/pages/relatorio/MinistroRelatorioAniversariantes.xhtml")
				.hasAnyRole("guCftAdministrador", "guCftConsulta", "guCftFinanceiroLocal", "guCftFinanceiroRegiao", "guCftSecretarioConvencao", "guCftSecretarioRegiao")

		.anyRequest().denyAll()
		.and()

		.formLogin()
		.loginPage("/Login.xhtml")
		.failureUrl("/Login.xhtml?invalid=true")
		.and()

		.logout()
		.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
		.and()

		.exceptionHandling()
		.accessDeniedPage("/AcessoNegado.xhtml")
		.authenticationEntryPoint(jsfLoginEntry)
		.accessDeniedHandler(jsfDeniedEntry);	

		return http.build();
	}
	
	
	// Coloquei esse método porque está no exemplo mas não consegui entender a finalidade -> https://www.youtube.com/watch?v=7HQ-x9aoZx8
	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() throws Exception {
		return (web) -> web.ignoring().requestMatchers("/images/**", "/js/**", "webjars/**");
	}
}
