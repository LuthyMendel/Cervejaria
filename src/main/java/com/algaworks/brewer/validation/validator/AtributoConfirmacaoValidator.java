package com.algaworks.brewer.validation.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.ConstraintValidatorContext.ConstraintViolationBuilder;

import org.springframework.beans.BeanUtils;

import com.algaworks.brewer.validation.AtributoConfirmacao;

public class AtributoConfirmacaoValidator implements ConstraintValidator<AtributoConfirmacao, Object>{

	private String atributo;
	private String atributoConformacao;
	
	@Override
	public void initialize(AtributoConfirmacao constraintAnnotation) {
		this.atributo = constraintAnnotation.atributo();
		this.atributoConformacao = constraintAnnotation.atributoConfirmacao();
		
		
		
		
	}

	//este object é o usuario/objeto que estou recebendo
	@Override
	public boolean isValid(Object object, ConstraintValidatorContext context) {
		
		boolean valido = false;
		try {
			
			Object valorAtributo = org.apache.commons.beanutils.BeanUtils.getProperty(object, this.atributo);
			Object ValorAtributoConformacao = org.apache.commons.beanutils.BeanUtils.getProperty(object, this.atributoConformacao);
			
			valido = ambosSaoNull(valorAtributo, ValorAtributoConformacao) || ambosSaoIguais(valorAtributo, ValorAtributoConformacao);
			
			
		} catch(Exception e) {
			throw new RuntimeException("Erro recuperando valores dos atributos", e);
		}
		
		if(!valido) {
			context.disableDefaultConstraintViolation();
			String mensagem = context.getDefaultConstraintMessageTemplate();
			
			ConstraintViolationBuilder violationBuilder = context.buildConstraintViolationWithTemplate(mensagem);
			violationBuilder.addPropertyNode(atributoConformacao).addConstraintViolation();
		}
		
		return valido;
	}

	private boolean ambosSaoIguais(Object valorAtributo, Object valorAtributoConformacao) {


		return valorAtributo !=null && valorAtributo.equals(valorAtributoConformacao);
	}

	private boolean ambosSaoNull(Object valorAtributo, Object valorAtributoConformacao) {
		
		return valorAtributo == null && valorAtributoConformacao == null;
	}
	
	

}
