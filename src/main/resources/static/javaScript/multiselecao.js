Brewer = Brewer || {};

Brewer.MultiSelecao = (function(){	
	
	function MultiSelecao(){
		this.statusBtn = $('.js-status-btn');
		this.selecaoCheckbox = $('.js-selecao');	
		this.selecaoTodosCheckbox = $('.js-selecao-todos');	
	}
	
	MultiSelecao.prototype.iniciar = function(){
		this.statusBtn.on('click', onStatusBtnClicado.bind(this));	
		this.selecaoTodosCheckbox.on('click', onSelecaoTodosClicado.bind(this));	
		this.selecaoCheckbox.on('click',onSelecaoClicando.bind(this));
		
	}
	
	function onStatusBtnClicado(event){
		
		var botaoClicado = $(event.currentTarget);
		var status = botaoClicado.data('status'); //captura o botão clicado
		var url = botaoClicado.data('url');
		
		
		//Recuperar os selecionados pelo operador de sistema		
		var checkBoxSelecionados = this.selecaoCheckbox.filter(':checked');		
		var codigos = $.map(checkBoxSelecionados, function(c){			
			return $(c).data('codigo');
		});				
		
		//ENVIANDO PARA O CONTROLADOR OS DADOS DO ARRAY			
		if(codigos.length>0){
		$.ajax({			
			url: url,
			method:'PUT',
			data:{
				codigos: codigos,
				status: status
			},
			success: function(){				
				window.location.reload();
				
							}
		});
		}		
		
	}
	function onSelecaoTodosClicado(){
			var status = this.selecaoTodosCheckbox.prop('checked');
			this.selecaoCheckbox.prop('checked', status);	
			statusBotaoAcao.call(this, status);		
		}
		function onSelecaoClicando(){
			
			var selecaoCheckboxChecados = this.selecaoCheckbox.filter(':checked');
			this.selecaoTodosCheckbox.prop('checked', selecaoCheckboxChecados.length >= this.selecaoCheckbox.length);
			statusBotaoAcao.call(this,selecaoCheckboxChecados.length);
		}
		
		function statusBotaoAcao(ativar){
			
			ativar ? this.statusBtn.removeClass('disabled'): this.statusBtn.addClass('disabled');
		}
	
	return MultiSelecao;
}());

$(function(){
	
	var multiSelecao = new Brewer.MultiSelecao();
	multiSelecao.iniciar();
});