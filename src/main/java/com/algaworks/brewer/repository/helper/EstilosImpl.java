package com.algaworks.brewer.repository.helper;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.algaworks.brewer.model.Cerveja;
import com.algaworks.brewer.model.Estilo;
import com.algaworks.brewer.repository.filter.EstiloFilter;
import com.algaworks.brewer.repository.helper.estilo.EstiloQueries;
import com.algaworks.brewer.repository.paginacao.PaginacaoUtil;

public class EstilosImpl implements EstiloQueries {
	
	
	@PersistenceContext
	private EntityManager manager;
	
	@Autowired
	PaginacaoUtil paginacaoutil;

	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public Page<Estilo> filtrar(EstiloFilter filtro, Pageable pageable) {
		
		
		
		Criteria criteria = manager.unwrap(Session.class).createCriteria(Estilo.class);
		
		paginacaoutil.preparar(criteria, pageable);
				
		
		adicionarFiltro(filtro, criteria);	//adiciona o filtro na busca do sql	que no caso é apenas o nome.
		
		return new PageImpl<>(criteria.list(), pageable, total(filtro));
	}
	
	

	
	private Long total(EstiloFilter filtro) {
		
		Criteria criteria = manager.unwrap(Session.class).createCriteria(Estilo.class);
		adicionarFiltro(filtro, criteria);
		criteria.setProjection(Projections.rowCount());
		return (Long) criteria.uniqueResult();
	}
	
		


	private void adicionarFiltro(EstiloFilter filtro, Criteria criteria) {
		
		if(filtro!=null) {
			
			if(!StringUtils.isEmpty(filtro.getNome())) {
				
				criteria.add(Restrictions.ilike("nome", filtro.getNome(), MatchMode.ANYWHERE));	
			}
		}
	}

}
