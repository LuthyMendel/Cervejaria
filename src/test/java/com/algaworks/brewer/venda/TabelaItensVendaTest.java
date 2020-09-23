package com.algaworks.brewer.venda;

import static org.junit.Assert.*;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;

import com.algaworks.brewer.model.Cerveja;
import com.algaworks.brewer.session.TabelaItensVenda;

public class TabelaItensVendaTest {
	
	private TabelaItensVenda tabelaItensVenda;
	
	@Before
	public void setUp() {
		this.tabelaItensVenda = new TabelaItensVenda();
	}
	
	@Test
	public void deveCalcularValortotalSemItens() throws Exception {
		
		assertEquals(BigDecimal.ZERO, tabelaItensVenda.getValorTotal()); 
		
	}
		
	
	@Test
	public void deveCalcularValorTotalComUmItem() {
		
		Cerveja cerveja = new Cerveja();
		BigDecimal valor = new BigDecimal("20");
		cerveja.setValor(valor);
		
		tabelaItensVenda.adicionarItem(cerveja, 1);		
		assertEquals(valor, tabelaItensVenda.getValorTotal());
		
	}
	
	@Test
	public void deveCalcularValorTotalComVariosItens() throws Exception{
		
		Cerveja c1 = new Cerveja();
		c1.setCodigo(1L);
		BigDecimal v1 = new BigDecimal("8.90");
		c1.setValor(v1);
		
		
		Cerveja c2 = new Cerveja();
		c2.setCodigo(2L);
		BigDecimal v2 = new BigDecimal("4.99");
		c2.setValor(v2);
		
		tabelaItensVenda.adicionarItem(c1, 1);	
		tabelaItensVenda.adicionarItem(c2, 2);			
		
		assertEquals(new BigDecimal("18.88"), tabelaItensVenda.getValorTotal());
		
	}
	
	@Test
	public void deveAlterarQauntidadeDoItem() {
		Cerveja c1 = new Cerveja();
		c1.setCodigo(1L);
		c1.setValor(new BigDecimal("4.50"));
		tabelaItensVenda.adicionarItem(c1, 1);
		tabelaItensVenda.alterarQuantidadeItens(c1, 3);
		
		assertEquals(new BigDecimal("13.50"), tabelaItensVenda.getValorTotal());
	}
	
	@Test
	public void deveExluirItem() throws Exception{
		
		Cerveja c1 = new Cerveja();
		c1.setCodigo(1L);
		c1.setValor(new BigDecimal("5.00"));
		
		Cerveja c2 = new Cerveja();
		c2.setCodigo(2L);
		c2.setValor(new BigDecimal("5.00"));
		
		Cerveja c3 = new Cerveja();
		c3.setCodigo(3L);
		c3.setValor(new BigDecimal("5.00"));
		
		tabelaItensVenda.adicionarItem(c1, 2);
		tabelaItensVenda.adicionarItem(c2, 1);
		tabelaItensVenda.adicionarItem(c3, 1);

		
		tabelaItensVenda.excluirItem(c2);
		
		assertEquals(2, tabelaItensVenda.total());
		assertEquals(new BigDecimal("15.00"), tabelaItensVenda.getValorTotal());

		
		
		
	}
	
}
