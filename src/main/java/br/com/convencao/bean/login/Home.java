package br.com.convencao.bean.login;

import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.PieChartModel;

import br.com.convencao.bo.MinistroBO;
import br.com.convencao.bo.UsuarioBO;
import br.com.convencao.model.to.MinistroPorAnoTO;
import br.com.convencao.model.to.MinistroPorRegiaoTO;
import br.com.convencao.util.Uteis;
import br.com.convencao.util.jsf.FacesUtil;

@Named(value = "home")
@ViewScoped
public class Home implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	UsuarioBO usuarioBO;

	@Inject
	MinistroBO ministroBO;

	private PieChartModel pieModel;
	private BarChartModel barModel;

	public void inicializador() {
		try {

			// Para senha provisória redireciona para pagina de troca de senha
			if(FacesUtil.isUsuarioTrocarSenha()) {
				FacesUtil.setUsuarioTrocarSenha(false);

				FacesUtil.getExternalContext().redirect(FacesUtil.getFacesContext().getExternalContext().getRequestContextPath() + "/pages/usuario/UsuarioTrocaSenha.xhtml");
			}

			// Login com sucesso. Zerar numero de vezes que o usuário errou a senha
			if(FacesUtil.getQtdeErrosSenha() > 0) {
				usuarioBO.zerarErrosSenhaUsuario(Uteis.UsuarioLogado().getUsuario().getSqUsuario());
				FacesUtil.setQtdeErrosSenha(0);
			}

			// Grafico de Pizza
			createPieModel();
			// Grafico Barras
			createBarModel();


		} catch (IOException e) {
			e.printStackTrace();
			FacesUtil.addErrorMessage("Erro ao tentar redirecionar para troca de senha.");;
		}

	}

	public PieChartModel getPieModel() {
		return pieModel;
	}

	public BarChartModel getBarModel() {
		return barModel;
	}

	private void createPieModel() {
		this.pieModel = new PieChartModel();

		// buscar Ministros por Regiao
		List<MinistroPorRegiaoTO> ministroPorRegiao = ministroBO.findMinistrosPorRegiao();

		Long qtdeMinistrosAtivos = 0L;
		for (MinistroPorRegiaoTO to : ministroPorRegiao) {
			this.pieModel.set(to.getDsRegiao() + "(" + to.getQtdeMinistros() + ")", to.getQtdeMinistros());
			qtdeMinistrosAtivos += to.getQtdeMinistros();
		}

		this.pieModel.setTitle("Total Ministros Ativos: " + qtdeMinistrosAtivos);
		this.pieModel.setLegendPosition("w");
		this.pieModel.setShadow(false);

		this.pieModel.setShowDataLabels(true);
		this.pieModel.setDiameter(400);

	}

	private void createBarModel() {
		this.barModel = new BarChartModel();
		
		// Obter a data atual que será apresentado no eixo x
		LocalDateTime dataHoje = Uteis.DataHoje();
		int anoAtual = dataHoje.getYear();
		int anoInicio = anoAtual - 4;
		
		// Buscar quantitativo de ministros
		List<MinistroPorAnoTO> ministroPorAnoNovos = ministroBO.findMinistroPorAnoNovos(anoInicio, anoAtual);
		List<MinistroPorAnoTO> ministroPorAnoExcluidos = ministroBO.findMinistroPorAnoExcluidos(anoInicio, anoAtual);
		
		adicionarSerie(ministroPorAnoNovos, inicializarMapa(anoInicio), "Ministros novos por ano");
		
		adicionarSerie(ministroPorAnoExcluidos, inicializarMapa(anoInicio), "Ministros que saíram por ano");

		this.barModel.setTitle("Ministros movimentação por ano");
		this.barModel.setAnimate(true);
		this.barModel.setLegendPosition("ne");

		Axis xAxis = this.barModel.getAxis(AxisType.X);
		xAxis.setLabel("Ano");

		Axis yAxis = this.barModel.getAxis(AxisType.Y);
		yAxis.setLabel("Quantidade");
		yAxis.setMin(0);
		yAxis.setMax(200);
		yAxis.setTickCount(11);
	}

	
	// Inicializar Map com os cinco últimos anos
	private Map<Integer, Long> inicializarMapa(int anoInicio) {
		Map<Integer, Long> mapaInicial = new TreeMap<>();
		
		for (int i = 0; i < 5; i++) {
			mapaInicial.put(anoInicio, 0L);
			anoInicio ++;
		}
		return mapaInicial;
	}

	// Popular Map com valores obtidos do banco de dados
	private void adicionarSerie(List<MinistroPorAnoTO> ministroPorAno, Map<Integer, Long> mapaMinistros, String label) {
		
		for (MinistroPorAnoTO ministroPorAnoTO : ministroPorAno) {
			mapaMinistros.put(ministroPorAnoTO.getNnAno(), ministroPorAnoTO.getQtdeMinistro());
		}
		
		ChartSeries serieMinistros = new ChartSeries();
		serieMinistros.setLabel(label);
		for (Integer integer : mapaMinistros.keySet()) {
			serieMinistros.set(integer, mapaMinistros.get(integer));
		}
		
		this.barModel.addSeries(serieMinistros);
		
	}

}
