package br.com.convencao.config;

import java.io.File;
import java.io.InputStream;
import java.io.Serializable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.S3Object;

import br.com.convencao.bo.NegocioException;

public class S3Config implements Serializable {

	private static final long serialVersionUID = 1L;
	private static Log log = LogFactory.getLog(S3Config.class);

	private AmazonS3 autenticarS3() {
		log.info("autenticarS3()");
		// Faz a conexão usando os arquivos config e credential localizados em ~/.aws/
		return AmazonS3ClientBuilder.defaultClient();
	}

	// Fazer upload de arquivo para Amazon S3
	// key_name = Nome do arquivo que será gravado no S3 
	// file_path = Caminho completo inclusive com nome do arquivo que será transferido
	public void uploadAnexo(String bucket_name, String key_name, String file_path) {
		log.info("uploadAnexo(" + key_name + ", " + file_path + ")" );
		try {
			this.autenticarS3().putObject(bucket_name, key_name, new File(file_path));
		}catch (AmazonServiceException e) {
			throw new NegocioException("Falha ao gravar anexo no repositório Amazon S3." + e.getErrorMessage());
		}
	}

	// Excluir arquivo no Amazon S3
	// key_name = Nome do arquivo que está no S3 e será excluido
	public void deleteAnexo(String bucket_name, String key_name) {
		log.info("deleteAnexo(" + key_name + ")" );
		try {
			this.autenticarS3().deleteObject(bucket_name, key_name);
		} catch (AmazonServiceException e) {
			throw new NegocioException("Falha ao tentar remover arquivo no Amazon S3 com a chave: (" + key_name + "). " + e.getErrorMessage() );
		}
	}



	// Fazer Download de arquivos no Amazon S3
	// key_name = Nome do arquivo que está gravado no S3
	public InputStream downloadAnexo(String bucket_name, String key_name) {
		log.info("downloadAnexo(" + key_name + ")" );
		try {
			S3Object obj = this.autenticarS3().getObject(bucket_name, key_name);
			InputStream stream = obj.getObjectContent();

			return stream;
		} catch (AmazonServiceException e) {
			throw new NegocioException("Falha ao tentar fazer download de arquivo no Amazon S3 com a chave: (" + key_name + "). " + e.getErrorMessage() );

		} 
	}
}
