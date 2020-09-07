package com.algaworks.brewer.controller;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.algaworks.brewer.controller.page.PageWrapper;
import com.algaworks.brewer.model.Cliente;
import com.algaworks.brewer.model.Estilo;
import com.algaworks.brewer.model.TipoPessoa;
import com.algaworks.brewer.repository.Clientes;
import com.algaworks.brewer.repository.Estados;
import com.algaworks.brewer.repository.Estilos;
import com.algaworks.brewer.repository.filter.ClienteFilter;
import com.algaworks.brewer.repository.filter.EstiloFilter;
import com.algaworks.brewer.service.CadastrarClienteService;
import com.algaworks.brewer.service.exception.CpfOuCnpjClienteJaCadastradoException;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/clientes")
public class ClientesController {
	
	@Autowired
	private Estados estados;
	
	@ Autowired
	private CadastrarClienteService cadastroClienteService;
	
	
	@Autowired
	private Estilos estilos;
	
	@Autowired
	private Clientes clientes;

	@RequestMapping("/novo")
	public ModelAndView novo(Cliente cliente) {
		
		ModelAndView mv = new ModelAndView("cliente/cadastroCliente");
		
		mv.addObject("tiposPessoa", TipoPessoa.values());
		mv.addObject("estados", estados.findAll());
		
		return  mv;
	}
	
	@PostMapping("/novo")
	public ModelAndView salvar (@Valid Cliente cliente, BindingResult result, RedirectAttributes attributes) {
		
		if(result.hasErrors()) {
			
			return novo(cliente);
		}
		try {
			
			cadastroClienteService.salvar(cliente);
			
		}catch(CpfOuCnpjClienteJaCadastradoException e) {
			
			result.rejectValue("cpfOuCnpj",e.getMessage(), e.getMessage());
			return novo(cliente);
			
		}
		
		attributes.addFlashAttribute("mensagem","Cliente Salvo com sucesso!");
		
		return new ModelAndView("redirect:/clientes/novo");
		
	}
	
	@GetMapping("/buscar")
	public ModelAndView pesquisar(ClienteFilter clienteFilter, @PageableDefault(size=2) Pageable pageable, HttpServletRequest httpServletRequest) {
		
		

		ModelAndView mv = new ModelAndView("cliente/PesquisaClientes");
		
	PageWrapper<Cliente> paginaWrapper =  new PageWrapper<>(clientes.filtrar(clienteFilter, pageable), httpServletRequest);

		mv.addObject("pagina",paginaWrapper);

			return mv;

	}
	
}
