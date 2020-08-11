package com.algaworks.brewer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.algaworks.brewer.model.Cerveja;
import com.algaworks.brewer.repository.Cervejas;
import com.algaworks.brewer.service.event.cerveja.CervejaSalvaEvent;

@Service
public class CadastroCervejaService {
	
	@Autowired
	private ApplicationEventPublisher publicher;
	
	@Autowired
	private Cervejas cervejasRepository;
	
	@Transactional
	public void salvar(Cerveja cerveja) {
		cervejasRepository.save(cerveja);
		
		publicher.publishEvent(new CervejaSalvaEvent(cerveja));
	}

}
