<div style='font-family: Helvetica, Arial, sans-serif; font-size: 14px; color: #000'>
  <div style='margin: auto; border-radius: 10px; padding: 20px; background-color: #f5f5f5; width: 600px'>
    <div style='width: 100%; text-align: center; margin-bottom: 20px'>
      <a href='${url_sistema}' target='_blank'>
        <img alt='${nome_sistema}' src='${logo_sistema}' style='height: 100px' />
      </a>
    </div>
    <div style='padding: 10px; background-color: #fff; border-bottom: 1px solid; border-color: #f5f5f5; border-radius: 10px; 
    font-weight: bold; position: relative; text-align: center; font-size: 1.5em'>
    	${titulo}
    </div>
    <div style='padding: 20px 10px 10px 10px; margin-top: -10px; margin-bottom: 10px; border-radius: 0px 0px 10px 10px; 
    text-align: justify; background-color: #ffffff'>
      ${ola_usuario}
      <div style='background-color: #71a5d1; border-radius: 10px; margin: 10px auto; text-align: center; padding: 10px'>
        <a href='${url_sistema}/alterarSenhaExterno.ovs?token=${token}' style='color: #000; font-weight: bold; 
        text-decoration: underline; font-size: 1.2em' target='_blank'>
        	${clique_aqui}
        </a>
        <div style='font-size: 0.8em; margin-top: 4px'>
          ${link_quebrado}
          <br />
          ${url_sistema}/alterarSenhaExterno.ovs?token=${token}
        </div>
      </div>
      ${nao_solicitou}
      <br />
      ${duvidas}
      <br />
      ${telefones}
      <br />
      <br />
      Obrigado,
      <br />
      Equipe OVERSIGHT
    </div>
    <div style='width: 100%; text-align: center'>
      <strong>${nome_sistema}</strong> - <a href='${url_sistema}'>${url_sistema}</a>
    </div>
  </div>
</div>