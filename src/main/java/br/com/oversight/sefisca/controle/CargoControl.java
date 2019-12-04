package br.com.oversight.sefisca.controle;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;

import br.com.oversight.sefisca.entidade.Cargo;
import br.com.oversight.sefisca.persistencia.CargoDao;
import br.com.oversight.sefisca.util.UtilMessages;
import lombok.Getter;
import lombok.Setter;

@Scope("conversation")
@Controller("CargoControl")
public class CargoControl implements Serializable {

	private static final long serialVersionUID = 1L;

	@Autowired
	private CargoDao cargoDao;

	@Autowired
	private UsuarioLogadoControl usuarioLogadoControl;

	@Getter
	@Setter
	private Cargo cargo;

	@PostConstruct
	private void init() {
		this.cargo = new Cargo(usuarioLogadoControl.getUsuario());
	}

	public void confirmar() {
		try {

			if (StringUtils.isEmpty(this.cargo.getNome())) {
				UtilMessages.addMessage(FacesMessage.SEVERITY_INFO, "Preencha o nome!");
				return;
			}
			this.cargo = cargoDao.alterar(cargo);
			UtilMessages.addMessage("Sucesso!", "Registro confirmado!");
		} catch (Exception e) {
			e.printStackTrace();
			UtilMessages.addMessage(e);
		}
	}

}
