$(function(){
	
	var modal = $('#modalCadastroRapidoEstilo');
	var botaoSalvar = modal.find('.js-modal-cadastro-estilo-salvar-btn');
	var form = modal.find('form');
	form.on('submit',function(event){event.preventDefault()});
	var inputNomeEstilo = $('#nomeEstilo');
	var containeMensagemErro = $('.js-mensagem-cadastro-rapido-estilo');
	
	var url = form.attr('action');
	
	modal.on('shown.bs.modal', onModalShow);//executaFunção
	modal.on('hide.bs.modal', onModalClose); //Limpa o input
	botaoSalvar.on('click', onBotaoSalvarClick);
	
	
	
	function onModalShow(){
		inputNomeEstilo.focus();
	}
	
	function onModalClose(){
		inputNomeEstilo.val('');//Zera o input
		containeMensagemErro.addClass('hidden'); //apaga a mensagem de erro
		form.find('.form-group').removeClass('has-erro');
	}
	
	
	function onBotaoSalvarClick(){
		
		var nomeEstilo = inputNomeEstilo.val().trim();
		
		//Aqui salvo no banco de dados
		$.ajax({
			
			url: url,
			method: 'POST',
			contentType: 'application/json',
			data:JSON.stringify({nome: nomeEstilo}),
			error: onErroSalvandoEstilo,
			success: onEstiloSalvo //devolve um objeto estivo
			
		});
	}
	
	function onErroSalvandoEstilo(obj){
		var mensagemErro = obj.responseText;
		containeMensagemErro.removeClass('hidden');
		containeMensagemErro.html('<span>'+mensagemErro+'</span>');
		
	}
	
	function onEstiloSalvo(estilo){
		
		var comboEstilo = $('#estilo');
		comboEstilo.append('<option value='+estilo.codigo+'>'+estilo.nome+'</option>');
		comboEstilo.val(estilo.codigo);
		modal.modal('hide')
	}
	
	
	
});