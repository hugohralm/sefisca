package br.com.oversight.sefisca.controle;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.model.SelectItem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import br.com.ambientinformatica.ambientjsf.util.UtilFaces;
import br.com.oversight.sefisca.controle.dto.InstituicaoDTO;
import br.com.oversight.sefisca.entidade.EnumEtapaProcesso;
import br.com.oversight.sefisca.entidade.EnumStatusEtapa;
import br.com.oversight.sefisca.entidade.EnumTipoCodigoInstituicao;
import br.com.oversight.sefisca.entidade.EnumTipoProcesso;
import br.com.oversight.sefisca.entidade.EtapaProcesso;
import br.com.oversight.sefisca.entidade.Instituicao;
import br.com.oversight.sefisca.entidade.Processo;
import br.com.oversight.sefisca.entidade.Usuario;
import br.com.oversight.sefisca.persistencia.EtapaProcessoDao;
import br.com.oversight.sefisca.persistencia.InstituicaoDao;
import br.com.oversight.sefisca.persistencia.ProcessoDao;
import br.com.oversight.sefisca.util.UtilSefisca;
import lombok.Getter;
import lombok.Setter;

@Scope("conversation")
@Controller("AberturaProcessoControl")
public class AberturaProcessoControl implements Serializable {

    private static final long serialVersionUID = 1L;

    @Autowired
    private UsuarioLogadoControl usuarioLogadoControl;

    @Autowired
    private ProcessoDao processoDao;

    @Autowired
    private EtapaProcessoDao etapaProcessoDao;

    @Autowired
    private InstituicaoDao instituicaoDao;

    @Autowired
    private MenuProcessoControl menuProcessoControl;

    @Getter
    @Setter
    private String cnesCnpj;

    @Getter
    @Setter
    private EnumTipoCodigoInstituicao tipoCodigo;

    @Getter
    private Usuario usuarioLogado;

    @Getter
    private InstituicaoDTO instituicaoDTO;

    @Getter
    @Setter
    private Processo processo;

    @Getter
    @Setter
    private EtapaProcesso etapaProcesso;

    @Getter
    private Instituicao instituicao;

    @PostConstruct
    public void init() {
        novoProcesso();
    }

    public void consultarEstabelecimento() {
        if (this.cnesCnpj.isEmpty()) {
            UtilFaces.addMensagemFaces("Codigo obrigatorio " + this.tipoCodigo, FacesMessage.SEVERITY_ERROR);
            return;
        }

        try {
            this.instituicao = instituicaoDao.instituicaoPorCnesCpnj(this.cnesCnpj, this.tipoCodigo);
            if (!UtilSefisca.isNullOrEmpty(this.instituicao)) {
                this.instituicao.setUsuario(usuarioLogadoControl.getUsuario());
                this.processo.setInstituicao(this.instituicao);
            } else {
                UtilFaces.addMensagemFaces(this.tipoCodigo + " não encontrado.", FacesMessage.SEVERITY_WARN);
            }
        } catch (Exception e) {
            UtilFaces.addMensagemFaces(e);
        }
    }

    public String confirmar() {
        try {
            this.processo.setEtapaProcesso(EnumEtapaProcesso.DESIGNACAO);
            this.processo = processoDao.alterar(this.processo);
            primeiraEtapa(this.processo);
            UtilFaces.addMensagemFaces("Processo salvo com sucesso");
            return menuProcessoControl.iniciar(this.processo);
        } catch (Exception e) {
            UtilFaces.addMensagemFaces(e);
        }
        return null;
    }

    private void primeiraEtapa(Processo processo) {
        this.etapaProcesso = new EtapaProcesso();
        this.etapaProcesso.setDataInicio(new Date());
        this.etapaProcesso.setUsuario(usuarioLogadoControl.getUsuario());
        this.etapaProcesso.setEtapaProcesso(EnumEtapaProcesso.DESIGNACAO);
        this.etapaProcesso.setStatusEtapa(EnumStatusEtapa.ATIVA);
        this.etapaProcesso.setProcesso(processo);
        this.etapaProcessoDao.alterar(this.etapaProcesso);
    }

    private void novoProcesso() {
        this.cnesCnpj = "";
        this.tipoCodigo = EnumTipoCodigoInstituicao.CNES;
        this.processo = new Processo(usuarioLogadoControl.getUsuario());
    }

    public boolean isCnes() {
        return this.tipoCodigo.equals(EnumTipoCodigoInstituicao.CNES);
    }

    public List<SelectItem> getTiposProcesso() {
        return UtilFaces.getListEnum(EnumTipoProcesso.values());
    }

    public List<SelectItem> getTiposCodigoInstituicao() {
        return UtilFaces.getListEnum(EnumTipoCodigoInstituicao.values());
    }

    public List<Instituicao> getInstituicoes() {
        return instituicaoDao.listar();
    }
}
