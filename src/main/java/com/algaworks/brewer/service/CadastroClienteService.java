package com.algaworks.brewer.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.algaworks.brewer.model.Cliente;
import com.algaworks.brewer.repository.Clientes;
import com.algaworks.brewer.service.exception.CpfOuCnpjClienteJaCadastradoException;

@Service
public class CadastroClienteService {
	
	@Autowired
	private Clientes clientes;
	
	public void Salvar(Cliente cliente) {
		
		Optional<Cliente> clienteExtiste = clientes.findByCpfOuCnpj(cliente.getCpfOuCnpjSemFormatacao());
		
		if(clienteExtiste.isPresent()) {
			
			throw new CpfOuCnpjClienteJaCadastradoException("CPF/CNPJ já cadastrado");
		}
		
		clientes.save(cliente);
		
	}

}
