package br.com.convencao.bo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import br.com.convencao.dao.MinistroAnexoDAO;
import br.com.convencao.model.MinistroAnexo;
import br.com.convencao.util.Uteis;

public class AnexosBO implements Serializable {

	private static final long serialVersionUID = 1L;

	private static Log log = LogFactory.getLog(AnexosBO.class);

	@Inject
	private MinistroAnexoDAO ministroAnexoDAO;

	private Path diretorioRaiz;
	private Path diretorioRaizTemp;

	@PostConstruct
	void init() {
		diretorioRaiz = Uteis.diretorioRaizAnexos();
		diretorioRaizTemp = FileSystems.getDefault().getPath(System.getProperty("java.io.tmpdir"), "ministroanexos-temp");

		try {
			Files.createDirectories(diretorioRaiz);
			Files.createDirectories(diretorioRaizTemp);
		} catch (IOException e) {
			throw new NegocioException("Problemas ao tentar criar diretórios", e);
		}
	}

	public String salvarAnexosTemp(String nome, byte[] conteudo, Long sqMinistro) throws IOException {
		String novoNome = sqMinistro != null? sqMinistro + "_" + this.renomearArquivo(nome): this.renomearArquivo(nome);

		Path anexoTemp = diretorioRaizTemp.resolve(novoNome);

		Files.write(anexoTemp, conteudo);

		return novoNome;
	}

	public void deletar(String nome) throws IOException {
		this.deletar(diretorioRaiz, nome);
	}

	public void deletarTemp(String nome) throws IOException {
		this.deletar(diretorioRaizTemp, nome);
	}

	// Recuperar anexo da área temporaria e grava-los na área definitiva (Para uso no registro de novo candidato)
	public String recuperarAnexoTemporario(List<MinistroAnexo> anexos,Long sqMinistro, Long cdCodigo) throws Exception {
		String retorno = null;
		if(anexos.isEmpty()) {
			return retorno;
		}

		String nmArquivoRenomeado = null;
		for (MinistroAnexo ministroAnexo : anexos) {
			nmArquivoRenomeado = ministroAnexo.getDsAnexoRenomeado();
			Path anexoTemp = this.diretorioRaizTemp.resolve(nmArquivoRenomeado);
			if(Files.exists(anexoTemp)) {
				// Atualizar nome do anexo renomeado colocando no inicio o sqMinistro para facilitar a localizacao no Amazon S3
				ministroAnexo.setDsAnexoRenomeado(sqMinistro + "_" + nmArquivoRenomeado);
				ministroAnexoDAO.salvar(ministroAnexo);

				// Gravar anexo na área definitiva
				byte[] conteudo = Files.readAllBytes(anexoTemp);
				Path anexo = diretorioRaiz.resolve(ministroAnexo.getDsAnexoRenomeado());
				Files.write(anexo, conteudo);


				Files.delete(anexoTemp);
			} else {
				retorno = retorno + ministroAnexo.getDsAnexoOriginal() + " - ";
			}
		}

		return retorno;
	}

	// // Recuperar anexo da área temporaria e grava-los na área definitiva (Para uso na alteração de candidato)
	public String recuperarAnexoTemporario(String anexoRenomeado) throws IOException {

		String retorno = null;
		if(anexoRenomeado.isEmpty()) {
			return retorno;
		}

		Path anexoTemp = this.diretorioRaizTemp.resolve(anexoRenomeado);
		if(Files.exists(anexoTemp)) {
			// Gravar anexo no repositorio definitivo			
			byte[] conteudo = Files.readAllBytes(anexoTemp);
			Path anexo = diretorioRaiz.resolve(anexoRenomeado);
			Files.write(anexo, conteudo);

			Files.delete(anexoTemp);
		} else {
			retorno = "Anexo não salvo: ";
		}

		return retorno;
	}

	public byte[] recuperar(String nome) throws IOException {
		Path anexo = diretorioRaizTemp.resolve(nome);
		if(Files.exists(anexo)) {
			return Files.readAllBytes(anexo);
		}

		anexo = diretorioRaiz.resolve(nome);
		if(Files.exists(anexo)) {
			return Files.readAllBytes(anexo);
		}

		throw new RuntimeException("Não foi encontrado a foto: " + anexo);

	}


	// Download de anexos (arquivos)
	@SuppressWarnings("deprecation")
	public StreamedContent download(MinistroAnexo anexo) {
		log.info("download(SqMinistroAnexo=" + anexo.getSqMinistroAnexo() + " - DsAnexoOriginal=" + anexo.getDsAnexoOriginal() + " - DsAnexoRenomeado=" + anexo.getDsAnexoRenomeado() + ")");
		try {
			InputStream in;
			File file = new File(diretorioRaiz + "/" + anexo.getDsAnexoRenomeado());

			in = new FileInputStream(file);
			return new DefaultStreamedContent(in, anexo.getDsAnexoContentType(), anexo.getDsAnexoOriginal());
			
		} catch (FileNotFoundException e) {
			throw new NegocioException("Erro ao tentar fazer download do arquivo: ("+ anexo.getSqMinistroAnexo() + " - " + anexo.getDsAnexoOriginal() + ") "  + e.getMessage());
		}
	}

	private void deletar(Path raiz, String nome) throws IOException {
		if(StringUtils.isEmpty(nome)) {
			throw new NegocioException("Não foi possível excluir anexo. Nome do arquivo não identificado.");
		}

		Path anexo = raiz.resolve(nome);

		if(Files.exists(anexo)) {
			Files.delete(anexo);
		}
	}

	private String renomearArquivo(String original) {
		int pos = original.lastIndexOf(".");
		String nomePrincipal;
		String nomeExtensao = "";
		if(pos >= 1) {
			nomePrincipal = original.substring(0,pos);
			nomeExtensao = original.substring(pos);
		} else {
			nomePrincipal = original;
		}

		// Limitar tamanho do nome do arquivo.
		if(nomePrincipal.length() > 10) {
			nomePrincipal = nomePrincipal.substring(0, 10);
		}

		return nomePrincipal + "__" + UUID.randomUUID().toString() + nomeExtensao;
	}
}
