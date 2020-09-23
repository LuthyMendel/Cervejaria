package com.algaworks.brewer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.algaworks.brewer.model.Cerveja;
import com.algaworks.brewer.repository.Cervejas;
import com.algaworks.brewer.session.TabelaItensVenda;

@Controller 
@RequestMapping("/vendas")
public class VendasController {
	
	@Autowired
	private Cervejas cervejas;
	
	@Autowired
	private TabelaItensVenda tabelaItensVenda;
	
	@GetMapping("/nova")
	public String nova() {
		
		return "venda/CadastroVenda";
	}

	@PostMapping("/item")
	public ModelAndView  adicionarItem(Long codigoCerveja) {
		Cerveja cerveja = cervejas.findOne(codigoCerveja);
		tabelaItensVenda.adicionarItem(cerveja, 1);
		return mvTabelasItensVenda();	
		
	}
	
	@PutMapping("/item/{codigoCerveja}")
	public ModelAndView alterarQuantidadeCerveja(@PathVariable Long codigoCerveja, Integer quantidade) {
		Cerveja cerveja = cervejas.findOne(codigoCerveja);
		tabelaItensVenda.alterarQuantidadeItens(cerveja, quantidade);
		return mvTabelasItensVenda();
	}
	
	@DeleteMapping("/item/{codigoCerveja}")
	public ModelAndView excluirItem(@PathVariable("codigoCerveja") Cerveja cerveja) {
		System.out.println("DELTANDO");
		tabelaItensVenda.excluirItem(cerveja);
		
		return mvTabelasItensVenda();
	}

	private ModelAndView mvTabelasItensVenda() {
		ModelAndView mv = new ModelAndView("venda/tabelaItensVenda");
		mv.addObject("itens", tabelaItensVenda.getItens());
		return mv;
	}
}
