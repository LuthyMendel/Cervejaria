package com.algaworks.brewer.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.algaworks.brewer.model.Cliente;
import com.algaworks.brewer.repository.helper.ClienteQueries;

public interface Clientes extends JpaRepository<Cliente, Long>, ClienteQueries {

	Optional<Cliente> findByCpfOuCnpj(String cpfOuCnpj);



}
