package br.com.oversight.sefisca.controle;

import java.io.Serializable;
import java.util.Date;

import javax.annotation.PostConstruct;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import br.com.ambientinformatica.ambientjsf.util.UtilFaces;
import br.com.oversight.sefisca.persistencia.InstituicaoDao;
import br.com.oversight.sefisca.util.UtilMessages;
import br.com.oversight.sefisca.util.UtilSefisca;
import lombok.Getter;
import lombok.Setter;

@Scope("conversation")
@Controller("AtualizarInstituicaoControl")
public class AtualizarInstituicaoControl implements Serializable {

    private static final long serialVersionUID = 1L;

    @Autowired
    private InstituicaoDao instituicaoDao;

    @Autowired
    private UsuarioLogadoControl usuarioLogadoControl;

    @Getter
    @Setter
    public Date ultimaDataAtualizacao;

    @PostConstruct
    public void init() {
        getDataAtualizacao();
    }

    public void anexarArquivo(FileUploadEvent evt) {
        try {
            UploadedFile file = evt.getFile();
            if (!UtilSefisca.isNullOrEmpty(file)) {
                int instuticoesAtualizadas = instituicaoDao.atualizarInstituicaoCsv(file, this.ultimaDataAtualizacao,
                        usuarioLogadoControl.getUsuario());
                getDataAtualizacao();
                if (instuticoesAtualizadas == 0) {
                    UtilMessages.addMessage("Nenhuma instituição a ser atualizada.");
                } else {
                    UtilMessages.addMessage(instuticoesAtualizadas + " instituições foram atualizadas com sucesso.");
                }
            } else {
                UtilMessages.addMessage("Erro ao atualizar as instituições.");
            }
        } catch (Exception e) {
            UtilFaces.addMensagemFaces(e);
        }
    }

    public String getMensagemUltimaDataAtualizacao() {
        if (UtilSefisca.isNullOrEmpty(this.ultimaDataAtualizacao)) {
            return "Tabela instituição não contém registros atualizados.";
        }

        return "Última data de atualização das instituições: "
                + UtilSefisca.getDataStringFormatadaMask(this.ultimaDataAtualizacao, "dd/MM/yyyy");
    }

    private void getDataAtualizacao() {
        this.ultimaDataAtualizacao = instituicaoDao.ultimaDataAtualizacao();
    }
}
