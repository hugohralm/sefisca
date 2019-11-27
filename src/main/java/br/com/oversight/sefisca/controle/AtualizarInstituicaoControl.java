package br.com.oversight.sefisca.controle;

import java.io.Serializable;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import br.com.ambientinformatica.ambientjsf.util.UtilFaces;
import br.com.oversight.sefisca.entidade.Arquivo;
import br.com.oversight.sefisca.persistencia.InstituicaoDao;
import br.com.oversight.sefisca.util.UtilSefisca;
import lombok.Getter;
import lombok.Setter;

@Scope("conversation")
@Controller("AtualizarInstituicaoControl")
public class AtualizarInstituicaoControl implements Serializable {

    private static final long serialVersionUID = 1L;

    @Autowired
    private InstituicaoDao instituicaoDao;

    public void anexarArquivo(FileUploadEvent evt) {
        try {
            UploadedFile file = evt.getFile();
            if (!UtilSefisca.isNullOrEmpty(file)) {
                int instuticoesAtualizadas = instituicaoDao.atualizarInstituicaoCsv(file);
                UtilFaces.addMensagemFaces(instuticoesAtualizadas + " instituições foram atualizadas com sucesso.");
            } else {
                UtilFaces.addMensagemFaces("Erro ao atualizar as instituições.");
            }
        } catch (Exception e) {
            UtilFaces.addMensagemFaces(e);
        }
    }
}
