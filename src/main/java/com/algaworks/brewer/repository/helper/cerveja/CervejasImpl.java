package com.algaworks.brewer.repository.helper.cerveja;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.algaworks.brewer.model.Cerveja;
import com.algaworks.brewer.repository.filter.CervejaFilter;

public class CervejasImpl implements CervejasQueries {
	
	@PersistenceContext
	private EntityManager manager;

	@Override
	@Transactional(readOnly = true)
	public Page<Cerveja> filtrar(CervejaFilter filtro, Pageable pageable) {
		
		Criteria criteria = manager.unwrap(Session.class).createCriteria(Cerveja.class);
		
		
		int paginaAtual = pageable.getPageNumber();		
		int totalRegistrosPorPagina = pageable.getPageSize();		
		int primeiroRegistro = paginaAtual * totalRegistrosPorPagina;
		
		criteria.setFirstResult(primeiroRegistro);
		criteria.setMaxResults(totalRegistrosPorPagina);
		
		Sort sort = pageable.getSort();
		
		if(sort !=null) {
			
			Sort.Order order = sort.iterator().next();
			String field = order.getProperty();
			criteria.addOrder(order.isAscending() ? Order.asc(field): Order.desc(field));
		}

		adicionalFiltro(filtro, criteria);
		
		return new PageImpl<Cerveja>(criteria.list(), pageable, total(filtro));
	}

	private void adicionalFiltro(CervejaFilter filtro, Criteria criteria) {
		if(filtro!=null) {
			if(!StringUtils.isEmpty(filtro.getSku())) {
				criteria.add(Restrictions.eq("sku", filtro.getSku()));
			}			
			
			if(!StringUtils.isEmpty(filtro.getNome())) {
				
				criteria.add(Restrictions.ilike("nome",filtro.getNome(), MatchMode.ANYWHERE));				
			}
			
			if(isEstiloPresente(filtro)) {
				criteria.add(Restrictions.eq("estilo", filtro.getEstilo()));
			}
			
			if(filtro.getSabor() !=null) {
				criteria.add(Restrictions.eq("sabor", filtro.getSabor()));
			}
			
			if(filtro.getOrigem() !=null) {
				criteria.add(Restrictions.eq("origem", filtro.getOrigem()));
			}
			
			if(filtro.getValorDe() !=null) {
				criteria.add(Restrictions.ge("valor", filtro.getValorDe())); // ge maior ou igual
			}
			
			if(filtro.getValorAte() !=null) {
				criteria.add(Restrictions.le("sabor", filtro.getValorAte()));//menor
			}			
		}
	}
	
	private Long total(CervejaFilter filtro) {
		Criteria criteria = manager.unwrap(Session.class).createCriteria(Cerveja.class);
		adicionalFiltro(filtro, criteria);
		criteria.setProjection(Projections.rowCount());
		return  (long) criteria.uniqueResult();
	}

	private boolean isEstiloPresente(CervejaFilter filtro) {
		return filtro.getEstilo() != null && filtro.getEstilo().getCodigo() != null;
	}	

}
