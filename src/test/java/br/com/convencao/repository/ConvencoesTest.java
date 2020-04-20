package br.com.convencao.repository;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.junit.Test;

import br.com.convencao.dao.ConvencaoDAO;
import br.com.convencao.model.Convencao;


public class ConvencoesTest {

	@Inject
	private ConvencaoDAO listConvencoes;
	
	
	@Test
	public void listarConvencoesTest() throws Exception{
		
		List<Convencao> convencoes = new ArrayList<>();
		
		assertEquals(convencoes, listConvencoes.listarConvencoes());
	}
	
}
