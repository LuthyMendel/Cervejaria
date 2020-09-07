package com.algaworks.brewer.repository.helper;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import com.algaworks.brewer.model.Cliente;
import com.algaworks.brewer.repository.filter.ClienteFilter;
import com.algaworks.brewer.repository.filter.EstiloFilter;
import com.algaworks.brewer.repository.paginacao.PaginacaoUtil;

public class ClientesImpl implements ClienteQueries {

	
	
	@PersistenceContext
	private EntityManager manager;

	
	@SuppressWarnings("unchecked")
	@Override	
	@Transactional(readOnly = true)
	public Page<Cliente> filtrar(ClienteFilter filtro, Pageable pageable) {
	
		
		Criteria criteria = manager.unwrap(Session.class).createCriteria(Cliente.class);
		
		int paginaAtual =pageable.getPageNumber();
		int totalRegistroPorPagina = pageable.getPageSize();
		int primeiroRegistro = paginaAtual * totalRegistroPorPagina;
		
		criteria.setFirstResult(primeiroRegistro);
		criteria.setMaxResults(totalRegistroPorPagina);
		
		adicionarFiltro(filtro, criteria);		
		criteria.createAlias("endereco.cidade","c", JoinType.LEFT_OUTER_JOIN); //mesmo os campos estando nullos este permite listar sem apresentar erros
		criteria.createAlias("c.estado","e", JoinType.LEFT_OUTER_JOIN);
		
		return new PageImpl<>(criteria.list(), pageable, total(filtro));
		
	}


	private Long total(ClienteFilter filtro) {
		Criteria criteria = manager.unwrap(Session.class).createCriteria(Cliente.class);
		adicionarFiltro(filtro, criteria);
		criteria.setProjection(Projections.rowCount());
		return (Long) criteria.uniqueResult();
	}
	
	private void adicionarFiltro (ClienteFilter filtro, Criteria criteria) {
		
		if (filtro != null) {

			if (!StringUtils.isEmpty(filtro.getNome())) {

				criteria.add(Restrictions.ilike("nome", filtro.getNome(), MatchMode.ANYWHERE));

			}

			if (!StringUtils.isEmpty(filtro.getCpfOuCnpj())) {

				criteria.add(Restrictions.eq("cpfOuCnpj", filtro.getCpfOuCnpjSemFormatacao()));

			}

		}

	}

}
