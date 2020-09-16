Brewer = Brewer || {};

Brewer.PesquisaRapidaCliente = (function(){

	function PesquisaRapidaCliente(){
		
		this.pesquisaRapidaClientesModal = $('#pesquisaRapidaClientes');
		this.nomeInput = $('#nomeClienteModal');
		this.pesquisaRapidaBtn = $('.js-pesquisa-rapida-clientes-btn');
		
	}
	
	PesquisaRapidaCliente.prototype.iniciar = function(){
		
		this.pesquisaRapidaBtn.on('click', onPesquisaRapidaClicado.bind(this));		
		
	}
	
	
	function onPesquisaRapidaClicado(event ){
		
		//parar o comportamento padrão do Botão
		event.preventDefault();
		
			$.ajax({
			url: this.pesquisaRapidaClientesModal.find('form').attr('action'),
			method: 'GET',
			contentType:'application/json',
			data:{
				nome:this.nomeInput.val()
			},
			
			success: onpesquisarConcluida.bind(this),
			error: onErroPesquisa
			
		});
	
		
	}
	
	function onpesquisarConcluida(resultado){
		
		console.log('resultado', resultado);
	}
	
	function  onErroPesquisa(){
		$('.js-mensagem-erro').removeClass('hidden');
	}
	
	return PesquisaRapidaCliente;
}());

$(function(){
	
	var pesquisaRapidaCliente = new Brewer.PesquisaRapidaCliente();
	pesquisaRapidaCliente.iniciar();
})