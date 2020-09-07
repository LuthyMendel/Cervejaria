package com.algaworks.brewer.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.algaworks.brewer.model.Cidade;
import com.algaworks.brewer.repository.Cidades;
import com.algaworks.brewer.service.exception.NomeCidadeJaCadastradaException;

@Service
public class CadastrarCidadeService {
	
	@Autowired
	private Cidades cidades;
	
	@Transactional
	public void salvar(Cidade cidade) {
		Optional<Cidade> cidadeExiste = cidades.findByNomeAndEstado(cidade.getNome(), cidade.getEstado());
		if(cidadeExiste.isPresent()) {
			
			throw new NomeCidadeJaCadastradaException("Nome de Cidade já Cadastrado");
		}
		
		cidades.save(cidade);
		
		
		
	}

}
