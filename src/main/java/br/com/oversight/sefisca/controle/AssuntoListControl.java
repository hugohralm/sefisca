package br.com.oversight.sefisca.controle;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import br.com.ambientinformatica.ambientjsf.util.UtilFaces;
import br.com.oversight.sefisca.entidade.Assunto;
import br.com.oversight.sefisca.persistencia.AssuntoDao;
import br.com.oversight.sefisca.util.UtilMessages;
import lombok.Getter;
import lombok.Setter;

@Controller("AssuntoListControl")
public class AssuntoListControl {

	@Autowired
	private AssuntoDao assuntoDao;

	@Getter
	@Setter
	private Assunto assuntoExcluir;

	@Getter
	private List<Assunto> assuntos = new ArrayList<>();

	@PostConstruct
	public void listar() {
		try {
			this.assuntos = assuntoDao.listar();
			if (this.assuntos.isEmpty())
				UtilMessages.addMessage("Não foram encontrados registros");
		} catch (Exception e) {
			e.printStackTrace();
			UtilMessages.addMessage(e);
		}
	}

	public void excluir() {
		try {
			assuntoDao.excluirPorId(this.assuntoExcluir.getId());
			listar();
			UtilFaces.addMensagemFaces("Excluído com sucesso!");
		} catch (Exception e) {
			e.printStackTrace();
			UtilMessages.addMessage(e);
		}
	}
}
