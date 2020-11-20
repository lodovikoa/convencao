package br.com.convencao.bo;

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import br.com.convencao.dao.MinistroDAO;
import br.com.convencao.model.Ministro;

public class FotoBO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private MinistroDAO ministroDAO;

	private Path diretorioRaiz;
	private Path diretorioRaizTemp;
	
	@PostConstruct
	void init() {
		Path raizAplicacao = FileSystems.getDefault().getPath(System.getProperty("user.home"), ".ministrofotos");
	
		diretorioRaiz = raizAplicacao.resolve("fotos-ministro");
		diretorioRaizTemp = FileSystems.getDefault().getPath(System.getProperty("java.io.tmpdir"), "ministrofotos-temp");
	
		try {
			Files.createDirectories(diretorioRaiz);
			Files.createDirectories(diretorioRaizTemp);
		} catch (IOException e) {
			throw new NegocioException("Problemas ao tentar criar diretórios", e);
		}
	}
	
	public String salvarFotoTemp(String nome, byte[] conteudo, Long sqMinistro) throws IOException {
		String novoNome = this.renomearArquivo(nome, sqMinistro);
		
		Path fotoTemp = diretorioRaizTemp.resolve(novoNome);
		
		Files.write(fotoTemp, conteudo);
		
		return novoNome;
	}
	
	public void deletar(String nome) throws IOException {
		this.deletar(diretorioRaiz, nome);
	}
	
	public void deletarTemp(String nome) throws IOException {
		this.deletar(diretorioRaizTemp, nome);
	}
	
	// Recuperar foto da área temporária e grava-la na área definitiva (Na inclusão de novo ministro)
	public void recuperarFotoTemporaria(Ministro ministro, Long sqMinistro) throws Exception {
		if(StringUtils.isEmpty(ministro.getDsFoto())) {
			return;
		}
		
		Path fotoTemp = this.diretorioRaizTemp.resolve(ministro.getDsFoto());
		if(Files.exists(fotoTemp)) {	
			
			ministro.setDsFoto(ministro.getSqMinistro() + this.obterNomeExtensaoArquivo(fotoTemp.getFileName().toString()));
			
			ministroDAO.salvar(ministro);
			
			byte[] conteudo = Files.readAllBytes(fotoTemp);
			Path foto = diretorioRaiz.resolve(ministro.getDsFoto());
			Files.write(foto, conteudo);
			
			Files.delete(fotoTemp);
			
			
		} else {
			return;
		}
	}
	
	// Recuperar foto da área temporária e grava-la na área definitiva (Na alteração de  ministro)
	public void recuperarFotoTemporaria(Ministro ministroTemp) throws IOException {
		if(StringUtils.isEmpty(ministroTemp.getDsFoto())) {
			return;
		}
		
		Path fotoTemp = this.diretorioRaizTemp.resolve(ministroTemp.getDsFoto());
		if(!Files.exists(fotoTemp)) {
			return;
		}
		
		byte[] conteudo = Files.readAllBytes(fotoTemp);
		Path foto = diretorioRaiz.resolve(ministroTemp.getDsFoto());
		Files.write(foto, conteudo);
		
		Files.delete(fotoTemp);
	}
	
	public byte[] recuperar(String nome) throws IOException {
		Path foto = diretorioRaizTemp.resolve(nome);
		if(Files.exists(foto)) {
			return Files.readAllBytes(foto);
		}
		
		foto = diretorioRaiz.resolve(nome);
		if(Files.exists(foto)) {
			return Files.readAllBytes(foto);
		}
		
		throw new RuntimeException("Não foi encontrado a foto: " + foto);
		
	}
	
	private void deletar(Path raiz, String nome) throws IOException {
		if(StringUtils.isEmpty(nome)) {
			return;
		}
		
		Path foto = raiz.resolve(nome);
		
		if(Files.exists(foto)) {
			Files.delete(foto);
		}
	}
	
	private String renomearArquivo(String original, Long sqMinistro) {
	
		String nomePrincipal = obterNomePrincipalArquivo(original);
		String nomeExtensao = obterNomeExtensaoArquivo(original);
		
		// Limitar tamanho do nome do arquivo.
		if(nomePrincipal.length() > 10) {
			nomePrincipal = nomePrincipal.substring(0, 10);
		}
		
		return sqMinistro != null? sqMinistro + nomeExtensao: nomePrincipal + "__" + UUID.randomUUID().toString() + nomeExtensao;
	}
	
	// Obter somente a extensão do arquivo quando houver e com o ponto. Ex.: foto.jpg, retorna .jpg
	private String obterNomeExtensaoArquivo(String nmArquivo) {
		int pos = nmArquivo.lastIndexOf(".");
		String nomeExtensao = "";
		if(pos >= 1) {
			nomeExtensao = nmArquivo.substring(pos);
		} 
		
		return nomeExtensao;
	}
	
	private String obterNomePrincipalArquivo(String nmArquivo) {
		int pos = nmArquivo.lastIndexOf(".");
		String nomePrincipal;
		if(pos >= 1) {
			nomePrincipal = nmArquivo.substring(0,pos);
		} else {
			nomePrincipal = nmArquivo;
		}
		
		return nomePrincipal;
	}
}
