function filtrarSemAcento(itemLabel, filterValue) {
	itemLabel = itemLabel.normalize("NFD");
	itemLabel = itemLabel.replace(/[\u0300-\u036f]/g, "");
	itemLabel = itemLabel.toUpperCase();
	
	filterValue = filterValue.normalize("NFD");
	filterValue = filterValue.replace(/[\u0300-\u036f]/g, "");
	filterValue = filterValue.toUpperCase();
	return itemLabel.includes(filterValue);
}

function blocTextoTextArea(comp, tamanho){
	total = comp.value.length;
	if(total <= tamanho){
		resto = tamanho - total;
		document.getElementById('cont').innerHTML = resto;
		document.getElementById('contOther').innerHTML = resto;
	} else{
		comp.value = comp.value.substr(0, tamanho);
	} 
}

function limparAutocomplete(campo) {
	document.getElementById(campo).value = "";
}

function proximoCampo(atual,proximo){
	if(atual.value.length >= atual.maxLength){ document.getElementById(proximo).focus(); }
}

function aplicaMascaraCpfCnpj(campo, teclapres) {
	var tecla = teclapres.keyCode;

	if ((tecla < 48 || tecla > 57) && (tecla < 96 || tecla > 105) && tecla != 46 && tecla != 8 && tecla != 13 && tecla != 9) {
		return false;
	}

	var vr = campo.value;
	vr = vr.replace( /\//g, "" );
	vr = vr.replace( /-/g, "" );
	vr = vr.replace( /\./g, "" );
	var tam = vr.length;

	if ( tam <= 3 ) {
		campo.value = vr;
	}
	if ( (tam > 4) && (tam <= 6) ) {
		campo.value = vr.substr( 0, 3 ) + '.' + vr.substring( 3 );
	}
	if ( (tam > 7) && (tam <= 9) ) {
		campo.value = vr.substr( 0, 3 ) + '.' + vr.substr( 3, 3 ) + '.' + vr.substring( 6 );
	}
	if ( (tam > 9) && (tam <= 11) ) {
		campo.value = vr.substr( 0, 3 ) + '.' + vr.substr( 3, 3 ) + '.' + vr.substr( 6, 3 ) + '-' + vr.substring( 9 );
	}
	if ( (tam > 12) && (tam <= 14) ) {
		campo.value = vr.substr( 0, 2 ) + '.' + vr.substr( 2, 3 ) + '.' + vr.substr( 5, 3 ) + '/' + vr.substr( 8, 4 ) + '-' + vr.substring( 12 );
	}
	if(tam > 14){
		campo.value = vr.substr( 0, 2 ) + '.' + vr.substr( 2, 3 ) + '.' + vr.substr( 5, 3 ) + '/' + vr.substr( 8, 4 ) + '-' + vr.substr( 12, 2 );
	}
	return true;
} 

var compFocus;

function verificaEnterTab(evt){
	var key_code = evt.keyCode  ? evt.keyCode  :
		evt.charCode ? evt.charCode :
			evt.which    ? evt.which    : void 0;
	return key_code == 9 || key_code == 13;
}

$(function(){
	$('body').on('keypress', function(event){
		if(event.which === 13){
			event.preventDefault();
		}
	});
});

function MascaraCPF_CNPJ(field , evento){
	/*Formato  XXX.XXX.XXX-XX  ||  XX.XXX.XXX/XXXX-XX
		Testado em: IE, Firefox, Safari, Opera, Chrome
	 */
	var e;
	var cpf;
	var cnpj;

	if(evento && evento.type == "paste"){
		evento.returnValue =  false;
		return;
	}

	if(evento && evento.type == "keypress"){
		e = (evento.which) ? evento.which : evento.keyCode;
		if(!((e > 47 && e < 58) || e == 0 || e == 8)){
			if(evento.which && evento.which != 0){
				evento.preventDefault();
				return;
			}else{
				evento.keyCode = 0;
				return;
			}
		}
		if(evento.ctrlKey){
			if(evento.which && evento.which != 0){
				evento.preventDefault();
				return;
			}else{
				evento.keyCode = 0;
				return;
			}
		}
	}
	if(e != 8){
		cpf = field.value.length < 14;
		cnpj = !cpf;
		if(cpf){
			var srt = field.value;
			while(srt.indexOf('.') > -1 || srt.indexOf('-') > -1){
				srt = srt.replace('.', '').replace('-','');
			}		
			if((srt.length % 3) == 0 && srt.length > 0 ){
				if(srt.length == 9){
					field.value = field.value + '-';
				}else{
					field.value = field.value + '.';
				}
			}		
		}
		if(cnpj){
			if(field.value.length == 14){
				var srt = field.value;
				while(srt.indexOf('.') > -1 || srt.indexOf('-') > -1  || srt.indexOf('/') > -1){
					srt = srt.replace('.', '').replace('-', '').replace('/', '');
				}
				field.value = srt.substr(0, 2) + '.' + srt.substr(2, 3) + '.' + srt.substr(5, 3) + '/' + srt.substr(8, srt.length -8);
			}else{
				if(field.value.length == 15){
					field.value += '-';
				}else{
					if(field.value.length >= 18){
						field.value = field.value.substr(0, 18);
						if(evento.which && evento.which != 0){
							evento.preventDefault();
							return;
						}else{
							evento.keyCode = 0;
							return;
						}
					}			
				}		
			}	
		}
	}
}

PrimeFaces.locales['pt_BR'] = {
		closeText: 'Fechar',
		prevText: 'Anterior',
		nextText: 'Próximo',
		currentText: 'Início',
		monthNames: ['Janeiro','Fevereiro','Março','Abril','Maio','Junho','Julho','Agosto','Setembro','Outubro','Novembro','Dezembro'],
		monthNamesShort: ['Jan','Fev','Mar','Abr','Mai','Jun', 'Jul','Ago','Set','Out','Nov','Des'],
		dayNames: ['Domingo','Segunda','Terça','Quarta','Quinta','Sexta','Sabado'],
		dayNamesShort: ['Dom','Seg','Ter','Qua','Qui','Sex','Sab'],
		dayNamesMin: ['D','S','T','Q','Q','S','S'],
		weekHeader: 'Semana',
		firstDay: 1,
		isRTL: false,
		showMonthAfterYear: false,
		yearSuffix: '',
		timeOnlyTitle: 'São Horas',
		timeText: 'Tempo',
		hourText: 'Hora',
		minuteText: 'Minuto',
		secondText: 'Segundo',
		ampm: false,
		month: 'Mês',
		week: 'Semana',
		day: 'Dia',
		allDayText : 'Todo Dia'
};

function mascara(o,f){ 
	v_obj=o 
	v_fun=f 
	setTimeout("execmascara()",1) 
} 
function execmascara(){ 
	v_obj.value=v_fun(v_obj.value) 
} 

function moeda(v){ 
	v=v.replace(/\D/g,""); // permite digitar apenas numero 
	v=v.replace(/(\d{1})(\d{17})$/,"$1.$2") // coloca ponto antes dos ultimos digitos 
	v=v.replace(/(\d{1})(\d{14})$/,"$1.$2") // coloca ponto antes dos ultimos 15 digitos
	v=v.replace(/(\d{1})(\d{11})$/,"$1.$2") // coloca ponto antes dos ultimos 11 digitos 
	v=v.replace(/(\d{1})(\d{8})$/,"$1.$2") // coloca ponto antes dos ultimos 8 digitos 
	v=v.replace(/(\d{1})(\d{5})$/,"$1.$2") // coloca ponto antes dos ultimos 5 digitos 
	v=v.replace(/(\d{1})(\d{1,2})$/,"$1,$2") // coloca virgula antes dos ultimos 2 digitos 
	return v; 
}

function moedaQuatro(v){ 
	v=v.replace(/\D/g,""); // permite digitar apenas numero 
	v=v.replace(/(\d{1})(\d{19})$/,"$1.$2") // coloca ponto antes dos ultimos digitos
	v=v.replace(/(\d{1})(\d{16})$/,"$1.$2") // coloca ponto antes dos ultimos 16 digitos
	v=v.replace(/(\d{1})(\d{13})$/,"$1.$2") // coloca ponto antes dos ultimos 13 digitos 
	v=v.replace(/(\d{1})(\d{10})$/,"$1.$2") // coloca ponto antes dos ultimos 10 digitos 
	v=v.replace(/(\d{1})(\d{7})$/,"$1.$2") // coloca ponto antes dos ultimos 7 digitos 
	v=v.replace(/(\d{1})(\d{1,4})$/,"$1,$2") // coloca virgula antes dos ultimos 4 digitos 
	return v; 
}

function mascaraTelefone( campo ) {
	function trata( valor,  isOnBlur ) {
		valor = valor.replace(/\D/g,"");                      
		valor = valor.replace(/^(\d{2})(\d)/g,"($1)$2");       

		if( isOnBlur ) {
			valor = valor.replace(/(\d)(\d{4})$/,"$1-$2");   
		} else {
			valor = valor.replace(/(\d)(\d{3})$/,"$1-$2"); 
		}
		return valor;
	}

	campo.onkeypress = function (evt) {
		var code = (window.event)? window.event.keyCode : evt.which;   
		var valor = this.value

		if(code > 57 || (code < 48 && code != 8 ))  {
			return false;
		} else {
			this.value = trata(valor, false);
		}
	}

	campo.onblur = function() {
		var valor = this.value;
		if( valor.length < 13 ) {
			this.value = ""
		}else {      
			this.value = trata( this.value, true );
		}
	}
	campo.maxLength = 14;
}

var flagConcluiuRequisicao;
function ativaTimeOut(){
		flagConcluiuRequisicao = false;
  	setTimeout(function(){
			if(!flagConcluiuRequisicao){
				PF('statusDialog').show();
			}
		}, 500);
	}

	function fechaDialog(){
		PF('statusDialog').hide()
		flagConcluiuRequisicao = true;
	} 