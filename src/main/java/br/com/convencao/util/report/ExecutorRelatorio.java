package br.com.convencao.util.report;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.jdbc.Work;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.Exporter;
import net.sf.jasperreports.export.ExporterInput;
import net.sf.jasperreports.export.OutputStreamExporterOutput;
import net.sf.jasperreports.export.PdfExporterConfiguration;
import net.sf.jasperreports.export.PdfReportConfiguration;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.XlsxExporterConfiguration;
import net.sf.jasperreports.export.XlsxReportConfiguration;

public class ExecutorRelatorio implements Work {

	private static Log log = LogFactory.getLog(ExecutorRelatorio.class);
	
	private String caminhoRelatorio;
	private HttpServletResponse response;
	HttpServletRequest request;
	private Map<String, Object> parametros;
	private String tpRelatorio;
	private String nomeArquivoSaida;

	private boolean relatorioGerado;

	public ExecutorRelatorio(String caminhoRelatorio, HttpServletResponse response, HttpServletRequest request, Map<String, Object> parametros, String nomeArquivoSaida, String tpRelatorio) {
		this.caminhoRelatorio = caminhoRelatorio;
		this.response = response;
		this.request = request;
		this.parametros = parametros;
		this.tpRelatorio = tpRelatorio;
		this.nomeArquivoSaida = nomeArquivoSaida;

		this.parametros.put(JRParameter.REPORT_LOCALE, new Locale("pt", "BR"));
	}

	@Override
	public void execute(Connection connection) throws SQLException {
		try {
			InputStream relatorioStream = this.getClass().getResourceAsStream(this.caminhoRelatorio);

			JasperPrint print = JasperFillManager.fillReport(relatorioStream, parametros, connection);

			this.relatorioGerado = print.getPages().size() > 0;

			if(relatorioGerado) {
				if("xlsx".equalsIgnoreCase(this.tpRelatorio)) {
					
					Exporter<ExporterInput, XlsxReportConfiguration, XlsxExporterConfiguration, OutputStreamExporterOutput> exportador = new JRXlsxExporter();
					exportador.setExporterInput(new SimpleExporterInput(print));
					exportador.setExporterOutput(new SimpleOutputStreamExporterOutput(response.getOutputStream()));
					
					response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
					response.setHeader("Content-Disposition", "attachment; filename=\"" + this.nomeArquivoSaida + ".xlsx" + "\"");
					
					exportador.exportReport();
					

				} else if("pdf".equalsIgnoreCase(this.tpRelatorio)) {
					Exporter<ExporterInput, PdfReportConfiguration, PdfExporterConfiguration, OutputStreamExporterOutput> exportador = new JRPdfExporter();
					exportador.setExporterInput(new SimpleExporterInput(print));
					exportador.setExporterOutput(new SimpleOutputStreamExporterOutput(response.getOutputStream()));

					response.setContentType("application/pdf");

					exportador.exportReport();
					
				} else {
					response.getWriter().append("Relatório (" + this.caminhoRelatorio +  ") indefinido (pdf ou xlsx)? Informe ocorrência ao Analista responsável pelo sistema.\t").append(request.getContextPath());
				}

			}
		} catch (JRException e) {
			log.error("((JRExceptionErro)) - ao executar relatório " + this.caminhoRelatorio + " - " + e.getMessage());
		} catch (IOException e) {
			log.error("((IOException)) - ao executar relatório " + this.caminhoRelatorio + " - " + e.getMessage());
		//	e.printStackTrace();
		} catch (Exception e) {
			log.error("((Exception)) - ao executar relatório " + this.caminhoRelatorio + " - " + e.getMessage());
//			throw new SQLException("Erro ao executar relatório " + this.caminhoRelatorio);
		}
	}

	public boolean isRelatorioGerado() {
		return relatorioGerado;
	}

}
