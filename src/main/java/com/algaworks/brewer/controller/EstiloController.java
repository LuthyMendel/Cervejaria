package com.algaworks.brewer.controller;


import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.algaworks.brewer.controller.page.PageWrapper;
import com.algaworks.brewer.model.Cerveja;
import com.algaworks.brewer.model.Estilo;
import com.algaworks.brewer.repository.Estilos;
import com.algaworks.brewer.repository.filter.EstiloFilter;
import com.algaworks.brewer.service.EstiloService;
import com.algaworks.brewer.service.exception.NomeEstiloJaCadastradoException;

@Controller
@RequestMapping("/estilos")
public class EstiloController {
	
	@Autowired
	private EstiloService estiloService;
	
	@Autowired
	private Estilos estilos;

	@RequestMapping("/novo")
	public ModelAndView  novo(Estilo estilo) {
		
		return new ModelAndView("estilo/cadastroEstilo");
	} 
	
	
	@RequestMapping(value = "/novo", method = RequestMethod.POST)
	public ModelAndView cadastrar(@Valid Estilo estilo, BindingResult result, RedirectAttributes attributes) {
		
		if(result.hasErrors()) {			
			
			return novo(estilo);		
			}
		
		
		try {			
			
			estiloService.salvar(estilo);
			
		} catch (NomeEstiloJaCadastradoException e) {
			
			result.rejectValue("nome",e.getMessage(), e.getMessage());
			return novo(estilo);
		}
		
		attributes.addFlashAttribute("mensagem", "Estilo Salvo com Suecesso.");
		return new ModelAndView("redirect:/estilos/novo");
		
	}
	
	//jackson biblioteca convert o JSON em um Objeto
	@RequestMapping(method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_VALUE})
	public @ResponseBody ResponseEntity<?> salvar(@RequestBody @ Valid Estilo estilo, BindingResult result) {		

		if (result.hasErrors()) {

			return ResponseEntity.badRequest().body(result.getFieldError("nome").getDefaultMessage());
		}
		
		try {
			
			estilo = estiloService.salvar(estilo);
			
		} catch (NomeEstiloJaCadastradoException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		
		}
		
		return ResponseEntity.ok(estilo);

	}
	
	
//###########################################################
//##########S	
	@GetMapping()
	public ModelAndView pesquisar (EstiloFilter estiloFilter, BindingResult result, @PageableDefault(size= 2) Pageable pageable, HttpServletRequest httpServletRequest) {
		
		ModelAndView mv = new  ModelAndView("estilo/pesquisaEstilo");
		
		
		PageWrapper<Estilo> paginaWraper = new PageWrapper<>(estilos.filtrar(estiloFilter, pageable), httpServletRequest);
		mv.addObject("pagina", paginaWraper);
		
		return mv;		
	}


	
//#################################################################
	@GetMapping("/buscar")
	public ModelAndView buscar (@RequestParam String nome, BindingResult result ) {
		

		ModelAndView mv = new  ModelAndView("estilo/pesquisaEstilo");	
		
		Optional<Estilo> estilo = estilos.findByNomeIgnoreCase(nome);
		

		if(estilo.isPresent()) {
					
			
			mv.addObject("estilos", estilo.get());
			return mv;	

		}
		
		mv.addObject("mensagem"," Estilo "+ nome+ " não enconterado");
		return mv;	
		
		
	}

}
