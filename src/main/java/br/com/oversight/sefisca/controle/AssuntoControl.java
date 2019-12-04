package br.com.oversight.sefisca.controle;

import javax.faces.application.FacesMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;

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
			if (StringUtils.isEmpty(this.assunto.getDescricao())) {
				UtilMessages.addMessage(FacesMessage.SEVERITY_INFO, "Preencha a desciçao!");
				return;
			}
		    this.assuntoDao.alterar(this.assunto);
			UtilMessages.addMessage("Sucesso!", "Registro confirmado!");
			this.assunto = new Assunto();

		} catch (Exception e) {
			e.printStackTrace();
			UtilMessages.addMessage(e);
		}
	}
}