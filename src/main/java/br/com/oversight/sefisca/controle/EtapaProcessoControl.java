package br.com.oversight.sefisca.controle;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import br.com.ambientinformatica.ambientjsf.util.UtilFaces;
import br.com.oversight.sefisca.entidade.EnumEtapaProcesso;
import br.com.oversight.sefisca.entidade.EnumStatusEtapa;
import br.com.oversight.sefisca.entidade.EtapaProcesso;
import br.com.oversight.sefisca.entidade.Processo;
import br.com.oversight.sefisca.entidade.Usuario;
import br.com.oversight.sefisca.persistencia.EtapaProcessoDao;
import br.com.oversight.sefisca.persistencia.ProcessoDao;
import br.com.oversight.sefisca.persistencia.UsuarioDao;
import br.com.oversight.sefisca.util.UtilSefisca;
import lombok.Getter;
import lombok.Setter;

@Controller("EtapaProcessoControl")
@Scope("conversation")
public class EtapaProcessoControl implements Serializable {

    private static final long serialVersionUID = 1L;

    @Autowired
    private UsuarioLogadoControl usuarioLogadoControl;
    
    @Autowired
    private ProcessoDao processoDao;

    @Autowired
    private EtapaProcessoDao etapaProcessoDao;
    
    @Autowired
    private UsuarioDao usuarioDao;
    
    @Autowired
    private MenuProcessoControl menuProcessoControl;

    @Getter
    @Setter
    private EtapaProcesso etapaProcesso;

    @Getter
    private Processo processo;
    
    public String iniciar(EtapaProcesso etapaProcesso) {
        try {
            this.etapaProcesso = etapaProcessoDao.consultar(etapaProcesso.getId());
            this.processo = processoDao.consultarPorId(this.etapaProcesso.getProcesso().getId());
            return "/processo/etapaProcesso?faces-redirect=true";
        } catch (Exception e) {
            UtilFaces.addMensagemFaces(e);
        }
        return null;
    }
    
    public List<Usuario> getUsuarios() {
        return this.usuarioDao.listar();
    }
    
    public String confirmar() {
        try {
            if(!UtilSefisca.isNullOrEmpty(this.etapaProcesso.getProfissionalResponsavel())) {
                EnumStatusEtapa statusEtapa = this.etapaProcesso.getStatusEtapa();
                this.etapaProcesso.setStatusEtapa(EnumStatusEtapa.ADMITIDA);
                this.etapaProcesso.setDataFim(new Date());
                this.etapaProcesso = etapaProcessoDao.alterar(this.etapaProcesso);
                this.processo.setEtapaProcesso(EnumEtapaProcesso.FISCALIZACAO);
                this.processo.setProfissionalResponsavel(this.etapaProcesso.getProfissionalResponsavel());
                this.processo = processoDao.alterar(this.processo);
                if(statusEtapa.equals(EnumStatusEtapa.ATIVA)) {
                    etapaFiscalizacao(this.processo);
                }
                UtilFaces.addMensagemFaces(this.etapaProcesso.getEtapaProcesso().getDescricao() + " alterada com sucesso");
                return menuProcessoControl.iniciar(this.processo);
            }else {
                UtilFaces.addMensagemFaces("Informe o profissional responsável.");
            }
        } catch (Exception e) {
            UtilFaces.addMensagemFaces(e);
        }
        return null;
    }
    
    private void etapaFiscalizacao(Processo processo) {
        this.etapaProcesso = new EtapaProcesso();
        this.etapaProcesso.setDataInicio(new Date());
        this.etapaProcesso.setUsuario(usuarioLogadoControl.getUsuario());
        this.etapaProcesso.setEtapaProcesso(EnumEtapaProcesso.FISCALIZACAO);
        this.etapaProcesso.setStatusEtapa(EnumStatusEtapa.ATIVA);
        this.etapaProcesso.setProcesso(processo);
        this.etapaProcessoDao.alterar(this.etapaProcesso);
    }
}
