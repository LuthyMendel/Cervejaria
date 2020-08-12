package com.algaworks.brewer.controller;


import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.algaworks.brewer.controller.page.PageWrapper;
import com.algaworks.brewer.model.Cerveja;
import com.algaworks.brewer.model.Origem;
import com.algaworks.brewer.model.Sabor;
import com.algaworks.brewer.repository.Cervejas;
import com.algaworks.brewer.repository.Estilos;
import com.algaworks.brewer.repository.filter.CervejaFilter;
import com.algaworks.brewer.service.CadastroCervejaService;


@Controller
@RequestMapping("/cervejas")
public class CervejasController {
	
	@Autowired
	private Estilos estilos;	
	
	
	@Autowired
	private Cervejas cervejas;
	
	@Autowired
	private CadastroCervejaService cadastroCervejaService;	
	
	@RequestMapping("/novo")
	public ModelAndView novo(Cerveja  cerveja) {
		
		ModelAndView mv = new ModelAndView("cervejas/cadastroCerveja");		
		mv.addObject("sabores", Sabor.values());
		mv.addObject("estilos", estilos.findAll());		
		mv.addObject("origens", Origem.values());		
			
		return mv;
	}
	

	//Recebe os dados do Formulário
	@RequestMapping(value = "/novo", method= RequestMethod.POST)
	public ModelAndView cadastrar(@Valid Cerveja cerveja, BindingResult result,
							Model model, RedirectAttributes attributes) {				
		if(result.hasErrors()) {
			return novo(cerveja);			
		}		
		 
		cadastroCervejaService.salvar(cerveja);
		attributes.addFlashAttribute("mensagem","Cerveja Salva com sucesso!");		
		return new ModelAndView("redirect:/cervejas/novo");
	}
	
	
	@RequestMapping("/cadastro")
	public String cadastro() {
		
		return "cervejas/cadastro-produto";
	}
	
	@GetMapping
	public ModelAndView pesquisar(CervejaFilter cervejaFilter, BindingResult result
			, @PageableDefault(size=2) Pageable pageable, HttpServletRequest httpServletRequest) { // o Spring crie esse objeto
		
		ModelAndView mv = new ModelAndView("cervejas/pesquisaCervejas");
		mv.addObject("estilos", estilos.findAll());
		mv.addObject("sabores", Sabor.values());
		mv.addObject("origens", Origem.values());	
		
		PageWrapper<Cerveja> paginalWrapper = new PageWrapper<>(cervejas.filtrar(cervejaFilter, pageable), httpServletRequest);
		
		mv.addObject("pagina",paginalWrapper);	
		
		return mv;
		
	}
	
}
