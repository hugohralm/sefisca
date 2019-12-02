<!DOCTYPE html>
<%@page contentType="text/html; charset=UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml" lang="pt-br">
<head>
<title>Sefisca</title>
<link rel="shortcut icon" href="resources/imagens/favicon.ico"
	type="image/x-icon" />
<link href="resources/css/login.css" rel="stylesheet" />
<link href="resources/css/bootstrap.css" rel="stylesheet" />

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv="Cache-Control"
	content="no-cache, no-store, must-revalidate" />
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="Expires" content="0" />
<meta name="viewport" content="width=device-width, initial-scale=1">

<script src="resources/js/jquery.js" type="text/javascript"></script>
<script src="resources/js/jquery.maskedinput.js" type="text/javascript"></script>

<script type="text/javascript">
	jQuery(function($) {
		$("#usuario").mask("999.999.999-99");
	});
	function focar() {
		document.getElementById("usuario").focus();
	}
	function cadastrar() {
		location.href = "cadastrarUsuario.ovs";
	}
	function recuperarSenha() {
		location.href = "recuperarSenha.ovs";
	}
</script>

</head>
<body>
	<div class="login-form">
		<form action="j_spring_security_check" method="post">
			<div class="text-center"
				style="text-align: center; padding-bottom: 15px">
				<img src="resources/imagens/logo_sefisca.png" alt="#Sefisca"
					width="150" />
			</div>
			<div class="form-group">
				<input type="text" class="form-control" id="usuario"
					placeholder="Informe seu CPF" name="j_username" required="required">
			</div>
			<div class="form-group">
				<input type="password" class="form-control"
					placeholder="Informe sua senha" name="j_password"
					required="required">
			</div>
			<div class="form-group">
				<button type="submit" class="btn btn-primary btn-block">Entrar</button>
			</div>
			<div class="clearfix">
				<a href="#" class="pull-left" onclick="cadastrar()">Cadastrar</a> <a
					href="#" class="pull-right" onclick="recuperarSenha()">Esqueceu a senha?</a>
			</div>
		</form>
	</div>
</body>
</html>