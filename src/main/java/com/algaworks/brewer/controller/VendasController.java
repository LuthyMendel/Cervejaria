package com.algaworks.brewer.controller;

import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.algaworks.brewer.controller.valida.VendaValidador;
import com.algaworks.brewer.model.Cerveja;
import com.algaworks.brewer.model.Venda;
import com.algaworks.brewer.repository.Cervejas;
import com.algaworks.brewer.security.UsuarioSistema;
import com.algaworks.brewer.service.CadastroVendaService;
import com.algaworks.brewer.session.TabelaItensSession;

@Controller 
@RequestMapping("/vendas")
public class VendasController {
	
	@Autowired
	private Cervejas cervejas;
	
	@Autowired
	private TabelaItensSession tabelaItens;
	
	@Autowired
	private CadastroVendaService cadastroVendaService;	
	
	@Autowired
	private VendaValidador vendaValidador;
	
	@InitBinder
	public void inicializarValidator(WebDataBinder binder) {
		binder.setValidator(vendaValidador);		
	}
	
	@GetMapping("/nova")
	public ModelAndView nova(Venda venda) {
		ModelAndView mv = new ModelAndView("venda/CadastroVenda");
		
		if(StringUtils.isEmpty(venda.getUuid())) {			
			venda.setUuid(UUID.randomUUID().toString());			
		}	
		
		mv.addObject("itens", venda.getItens());
		mv.addObject("valorFrete", venda.getValorFrete());
		mv.addObject("valorDesconto", venda.getValorDesconto());
		mv.addObject("valorTotalItens",tabelaItens.getValorTotal(venda.getUuid()));

		return mv;
	}

	@PostMapping(value = "/nova", params = "salvar")
	public ModelAndView salvar(Venda venda, BindingResult result, RedirectAttributes attributes, 
			@AuthenticationPrincipal UsuarioSistema usuarioSistema) {
		
		validarVenda(venda, result);		
		
		if(result.hasErrors()) {
			
			return nova(venda);
		}	
		
		
		venda.setUsuario(usuarioSistema.getUsuario());
		
		cadastroVendaService.salvar(venda);
		attributes.addFlashAttribute("mensagem", "Venda Salva com Sucesso");		
		return new ModelAndView("redirect:/vendas/nova");
	}

	
	@PostMapping(value = "/nova", params = "enviarEmail")
	public ModelAndView emitir(Venda venda, BindingResult result, RedirectAttributes attributes, 
			@AuthenticationPrincipal UsuarioSistema usuarioSistema) {
		
		validarVenda(venda, result);			
		if(result.hasErrors()) {
			
			return nova(venda);
		}	
		
		
		venda.setUsuario(usuarioSistema.getUsuario());
		
		cadastroVendaService.salvar(venda);
		attributes.addFlashAttribute("mensagem", "Venda Salva e email Enviado");		
		return new ModelAndView("redirect:/vendas/nova");
	}
	
	
	
	@PostMapping(value = "/nova", params = "emitir")
	public ModelAndView enviarEmail(Venda venda, BindingResult result, RedirectAttributes attributes, 
			@AuthenticationPrincipal UsuarioSistema usuarioSistema) {
		
		validarVenda(venda, result);		
		if(result.hasErrors()) {
			
			return nova(venda);
		}	
		
		
		venda.setUsuario(usuarioSistema.getUsuario());
		
		cadastroVendaService.emitir(venda);
		attributes.addFlashAttribute("mensagem", "Venda Emitida com Sucesso");		
		return new ModelAndView("redirect:/vendas/nova");
	}
	
	
	
	@PostMapping("/item")
	public ModelAndView  adicionarItem(Long codigoCerveja, String uuid) {
		Cerveja cerveja = cervejas.findOne(codigoCerveja);
		tabelaItens.adicionarItem(uuid, cerveja, 1);
		return mvTabelasItensVenda(uuid);	
		
	}
	
	@PutMapping("/item/{codigoCerveja}")
	public ModelAndView alterarQuantidadeCerveja(@PathVariable("codigoCerveja") Cerveja cerveja, Integer quantidade, String uuid) {
		
		tabelaItens.alterarQuantidadeItens(uuid, cerveja, quantidade);
		return mvTabelasItensVenda(uuid);
	}
	
	@DeleteMapping("/item/{uuid}/{codigoCerveja}")
	public ModelAndView excluirItem(@PathVariable("codigoCerveja") Cerveja cerveja, @PathVariable String uuid) {
		tabelaItens.excluirItem(uuid, cerveja);
		
		return mvTabelasItensVenda(uuid);
	}

	private ModelAndView mvTabelasItensVenda(String uuid) {
		ModelAndView mv = new ModelAndView("venda/tabelaItensVenda");
		mv.addObject("itens", tabelaItens.getItens(uuid));
		mv.addObject("valorTotal", tabelaItens.getValorTotal(uuid));
		return mv;
	}
	private void validarVenda(Venda venda, BindingResult result) {
		venda.adicionarItens(tabelaItens.getItens(venda.getUuid()));
		venda.calcularValorTotal();
		
		vendaValidador.validate(venda, result);
	}
}
