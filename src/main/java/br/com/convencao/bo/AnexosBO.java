package br.com.convencao.bo;

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import br.com.convencao.config.S3Config;
import br.com.convencao.dao.MinistroAnexoDAO;
import br.com.convencao.model.MinistroAnexo;
import br.com.convencao.util.Uteis;

public class AnexosBO implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private S3Config s3Config;
	
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
				
				// Gravar anexo no repositorio S3 do Amazon
				s3Config.uploadAnexo(Uteis._S3_BUCKET_confrateresanexos, ministroAnexo.getDsAnexoRenomeado(), this.diretorioRaizTemp.toString() + "/" +  nmArquivoRenomeado);

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
			// Gravar anexo no repositorio S3 do Amazon
			s3Config.uploadAnexo(Uteis._S3_BUCKET_confrateresanexos, anexoRenomeado, this.diretorioRaizTemp.toString() + "/" +  anexoRenomeado);

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

	private void deletar(Path raiz, String nome) {
		if(StringUtils.isEmpty(nome)) {
			throw new NegocioException("Não foi possível excluir anexo. Nome do arquivo não identificado.");
		}

		s3Config.deleteAnexo(Uteis._S3_BUCKET_confrateresanexos, nome);
	}

	private String renomearArquivo(String original) {
		int pos = original.lastIndexOf(".");
		String nomePrincipal;
		String nomeExtensao = "";
		if(pos > 1) {
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
