package com.algaworks.brewer.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.algaworks.brewer.model.Estilo;
import com.algaworks.brewer.repository.Estilos;
import com.algaworks.brewer.service.exception.NomeEstiloJaCadastradoException;

@Service
public class EstiloService {
	
	@Autowired
	private Estilos estilos;
	
	
	@Transactional
	public Estilo salvar(Estilo estilo) {
		
		Optional<Estilo> optionalEstilo = estilos.findByNomeIgnoreCase(estilo.getNome());
		
			if(optionalEstilo.isPresent()) {
				
				throw new NomeEstiloJaCadastradoException("Nome deo Estilo ja Cadastrado");
			}
			
			return estilos.saveAndFlush(estilo); 
			//saveAndFlush - Popula o Banco e devolve o Id
	}

}
