package br.com.oversight.sefisca.entidade;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import br.com.ambientinformatica.util.AmbientValidator;
import br.com.ambientinformatica.util.UtilHash;
import br.com.ambientinformatica.util.UtilHash.Algoritimo;
import br.com.oversight.sefisca.util.UtilSefisca;
import lombok.Getter;
import lombok.Setter;

@Entity
@org.hibernate.annotations.Entity(dynamicUpdate = true)
public class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Getter
    @GeneratedValue(generator = "usuario_seq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "usuario_seq", sequenceName = "usuario_seq", allocationSize = 1, initialValue = 1)
    private Integer id;

    @Getter @Setter
    @Column(nullable = false)
    @NotBlank(message = "Informe a senha", groups = AmbientValidator.class)
    @Length(min = 0, max = 255, message = "O limite do campo senha é de 255 caracteres.", groups = AmbientValidator.class)
    private String senha;

    @Getter @Setter
    @OneToOne(cascade = CascadeType.ALL, optional = false)
    @NotNull(message = "Informe os dados de pessoa.", groups = AmbientValidator.class)
    private Pessoa pessoa = new Pessoa();
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataAlteracaoSenha = new Date();

    @Temporal(TemporalType.TIMESTAMP)
    private Date dataCriacao = new Date();

    @Temporal(TemporalType.TIMESTAMP)
    private Date dataUltimoAcesso = new Date();

    @Getter @Setter
    private boolean alterarSenha;

    @Getter @Setter
    @Column(nullable = false)
    private boolean confirmado;

    @Getter @Setter
    @Length(min = 0, max = 255, message = "O limite do campo token é de 255 caracteres.", groups = AmbientValidator.class)
    private String token;

    @Getter @Setter
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "usuario_id")
    private List<PapelUsuario> papeisUsuario = new ArrayList<>();
    
    @PrePersist
    private void atualizarData() {
        setDataCriacao(new Date());
    }

    public void gerarToken() {
        this.token = UtilSefisca.gerarCodigoAleatorio(15, this.id);
    }

    public void setSenhaNaoCriptografada(String senha) {
        this.senha = UtilHash.gerarStringHash(senha, Algoritimo.MD5);
    }

    public void addPapel(PapelUsuario papel) {
        if (!isContemPapel(papel.getPapel())) {
            this.papeisUsuario.add(papel);
        }
    }

    public void removerPapel(PapelUsuario papel) {
        this.papeisUsuario.remove(papel);
    }

    public boolean isContemPapel(EnumPapel papelUsuario) {
        for (PapelUsuario papel : this.papeisUsuario) {
            if (papel.getPapel().equals(papelUsuario)) {
                return true;
            }
        }
        return false;
    }

    public boolean isAdmin() {
        for (PapelUsuario papel : this.papeisUsuario) {
            if (papel.isAdmin()) {
                return true;
            }
        }
        return false;
    }

    public boolean isPodeConsultar() {
        for (PapelUsuario papel : this.papeisUsuario) {
            if (papel.isPodeConsultar()) {
                return true;
            }
        }
        return false;
    }

    public boolean isSenhaValida(String confirmarSenha) {
        if (!this.getSenha().equals(confirmarSenha)) {
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((pessoa == null) ? 0 : pessoa.hashCode());
        result = prime * result + ((dataCriacao == null) ? 0 : dataCriacao.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Usuario other = (Usuario) obj;
        if (pessoa == null) {
            if (other.pessoa != null)
                return false;
        } else if (!pessoa.equals(other.pessoa))
            return false;
        if (dataCriacao == null) {
            if (other.dataCriacao != null)
                return false;
        } else if (!dataCriacao.equals(other.dataCriacao))
            return false;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    public Date getDataCriacao() {
        return dataCriacao != null ? (Date) dataCriacao.clone() : null;
    }

    public void setDataCriacao(Date dataCriacao) {
        this.dataCriacao = dataCriacao != null ? (Date) dataCriacao.clone() : null;
    }    

    public Date getDataAlteracaoSenha() {
        return dataAlteracaoSenha != null ? (Date) dataAlteracaoSenha.clone() : null;
    }

    public void setDataAlteracaoSenha(Date dataAlteracaoSenha) {
        this.dataAlteracaoSenha = dataAlteracaoSenha != null ? (Date) dataAlteracaoSenha.clone() : null;
    }

    public Date getDataUltimoAcesso() {
        return dataUltimoAcesso != null ? (Date) dataUltimoAcesso.clone() : null;
    }

    public void setDataUltimoAcesso(Date dataUltimoAcesso) {
        this.dataUltimoAcesso = dataUltimoAcesso != null ? (Date) dataUltimoAcesso.clone() : null;
    }
    
    public String getDataUltimoAcessoFormatada() {
    	return UtilSefisca.getDataStringFormatadaMask(this.dataUltimoAcesso, "dd/MM/yyyy HH:mm:ss");
    }

    @Override
    public String toString() {
        return pessoa.getNome() + " (" + pessoa.getCpf() + ")";
    }
}