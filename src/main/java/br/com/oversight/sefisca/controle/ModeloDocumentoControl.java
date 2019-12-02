package br.com.oversight.sefisca.controle;

import java.util.List;

import javax.faces.model.SelectItem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import br.com.ambientinformatica.ambientjsf.util.UtilFaces;
import br.com.ambientinformatica.jpa.exception.PersistenciaException;
import br.com.oversight.sefisca.entidade.EnumTamanhoDocumento;
import br.com.oversight.sefisca.entidade.EnumTipoPessoa;
import br.com.oversight.sefisca.entidade.ModeloDocumento;
import br.com.oversight.sefisca.persistencia.ModeloDocumentoDao;
import br.com.oversight.sefisca.util.UtilMessages;
import lombok.Getter;
import lombok.Setter;

@Controller("ModeloDocumentoControl")
public class ModeloDocumentoControl {

	@Autowired
	private ModeloDocumentoDao modeloDocumentoDao;

	@Getter
	@Setter
	private ModeloDocumento modeloDocumento = new ModeloDocumento();

	public void confirmar() {
		try {
			modeloDocumentoDao.alterar(modeloDocumento);
			UtilMessages.addMessage("Sucesso!", "Registro confirmado!");
			modeloDocumento = new ModeloDocumento();

		} catch (Exception e) {
			e.printStackTrace();
			UtilMessages.addMessage(e);
		}
	}

	public List<SelectItem> getTiposPessoa() {
		return UtilFaces.getListEnum(EnumTipoPessoa.values());
	}

	public List<SelectItem> getTamanhosDocumento() {
		return UtilFaces.getListEnum(EnumTamanhoDocumento.values());
	}
}