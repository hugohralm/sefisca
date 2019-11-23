package br.com.oversight.sefisca.controle;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.model.SelectItem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import br.com.ambientinformatica.ambientjsf.util.UtilFaces;
import br.com.oversight.sefisca.controle.dto.InstituicaoDTO;
import br.com.oversight.sefisca.controle.dto.ViaCEPDTO;
import br.com.oversight.sefisca.entidade.EnumTipoCodigoInstituicao;
import br.com.oversight.sefisca.entidade.EnumTipoProcesso;
import br.com.oversight.sefisca.entidade.Instituicao;
import br.com.oversight.sefisca.entidade.Processo;
import br.com.oversight.sefisca.entidade.Usuario;
import br.com.oversight.sefisca.persistencia.InstituicaoDao;
import br.com.oversight.sefisca.persistencia.ProcessoDao;
import br.com.oversight.sefisca.services.CepService;
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
    private InstituicaoDao instituicaoDao;

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
    private Instituicao instituicao;

    @Autowired
    private CepService cepService;

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
                this.instituicao = atualizarMunicipio(this.instituicao);
                this.processo.setInstituicao(this.instituicao);
            } else {
                UtilFaces.addMensagemFaces(this.tipoCodigo + " não encontrado.", FacesMessage.SEVERITY_WARN);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void confirmar() {
        this.processo = processoDao.alterar(this.processo);
        novoProcesso();
        UtilFaces.addMensagemFaces("Processo salvo com sucesso");
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

    public Instituicao atualizarMunicipio(Instituicao instituicao) {
        try {
            ViaCEPDTO viaCEPDTO = cepService.consultarCep(instituicao.getEndereco().getCep());

            if (viaCEPDTO != null) {
                instituicao.getEndereco().setMunicipio(viaCEPDTO.getMunicipio());
                return instituicaoDao.alterar(instituicao);
            }
        } catch (Exception e) {
            e.printStackTrace();
            UtilFaces.addMensagemFaces(e);
        }
        return instituicao;
    }
}
