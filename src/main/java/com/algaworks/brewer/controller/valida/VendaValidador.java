package com.algaworks.brewer.controller.valida;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.algaworks.brewer.model.Venda;

@Component
public class VendaValidador implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		
		return Venda.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		ValidationUtils.rejectIfEmpty(errors, "cliente.codigo","","Selecione um cliente na pesquisa rápida");
		
		Venda venda = (Venda) target;		
		validarSeInformouApenasHorarioEntrega(errors, venda);			
		validarSeInformouItens(errors, venda);
		 
		valorTotalNegativo(errors, venda); 
		
	}

	private void valorTotalNegativo(Errors errors, Venda venda) {
		//Retorna 0 -1 ou 1, se o valor retornado for menor do que zero significa que o valor total e negativo.
		if(venda.getValorTotal().compareTo(BigDecimal.ZERO)<0) { 
		errors.reject("","Valro total não pode ser Negativo");
		
};
	}

	private void validarSeInformouItens(Errors errors, Venda venda) {
		if(venda.getItens().isEmpty()) {
			errors.reject("", "Adicione pelo menos uma cerveja na venda");
		}
	}

	
	private void validarSeInformouApenasHorarioEntrega(Errors errors, Venda venda) {
		if(venda.getHorarioEntrega() != null && venda.getDataEntrega() == null) {
			
			errors.rejectValue("dataEntrega","", "Informe uma data de entrega para um horário");
		}
	}

}
