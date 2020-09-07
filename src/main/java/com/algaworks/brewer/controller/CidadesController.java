package com.algaworks.brewer.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.algaworks.brewer.model.Cidade;
import com.algaworks.brewer.repository.Cidades;
import com.algaworks.brewer.repository.Estados;
import com.algaworks.brewer.service.CadastrarCidadeService;
import com.algaworks.brewer.service.exception.NomeCidadeJaCadastradaException;

@Controller
@RequestMapping("/cidades")
public class CidadesController {
	
	@Autowired
	private Cidades cidades;
	
	@Autowired
	private CadastrarCidadeService cadastroCidadeService;
	
	
	@Autowired
	private Estados estados;

	@RequestMapping("/nova")
	@CacheEvict(value="cidades",key="#cidade.estado.codigo", condition = "#Cidade.temEstado()") //nome do cach
	public ModelAndView nova(Cidade cidade) {
		
		ModelAndView mv = new ModelAndView("cidade/cadastroCidade");
		
		mv.addObject("estados", estados.findAll());
		return mv;
	}
	
	
	@Cacheable(value ="cidades", key="#codigoEstado") //nome do cache
	@RequestMapping(consumes = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody List<Cidade> pesquisarPorCodigoEstado(
			@RequestParam(name="estado", defaultValue = "-1")Long codigoEstado){
		
		try {
			
			Thread.sleep(300);
			
		} catch (InterruptedException e) {	}
		
		return cidades.findByEstadoCodigo(codigoEstado);
	}
	
	@PostMapping("/nova")
	public ModelAndView salvar(@Valid Cidade cidade, BindingResult result, RedirectAttributes attributes) {
		
		System.out.println("-->>>"+cidade.getEstado());
		
		if(result.hasErrors()) {
			
			return nova(cidade);
		}
		
		try {
			
			cadastroCidadeService.salvar(cidade);
			
			
		} catch (NomeCidadeJaCadastradaException e) {
			
			result.rejectValue("nome", e.getMessage(),e.getMessage());
			return nova(cidade);
		}
		
		attributes.addFlashAttribute("messagem", "Cidade Salva Com Sucesso !");
		return new ModelAndView("redirect:/cidades/nova");
		
	}
	
}
