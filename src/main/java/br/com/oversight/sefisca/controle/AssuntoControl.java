package br.com.oversight.sefisca.controle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import br.com.oversight.sefisca.entidade.Assunto;
import br.com.oversight.sefisca.persistencia.AssuntoDao;
import br.com.oversight.sefisca.util.UtilMessages;
import lombok.Getter;
import lombok.Setter;

@Controller("AssuntoControl")
public class AssuntoControl {

	@Autowired
	private AssuntoDao assuntoDao;

	@Getter
	@Setter
	private Assunto assunto = new Assunto();

	public void confirmar() {
		try {
		    assuntoDao.alterar(assunto);
			UtilMessages.addMessage("Sucesso!", "Registro confirmado!");
			assunto = new Assunto();

		} catch (Exception e) {
			e.printStackTrace();
			UtilMessages.addMessage(e);
		}
	}
}