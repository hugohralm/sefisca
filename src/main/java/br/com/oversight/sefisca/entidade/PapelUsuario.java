package br.com.oversight.sefisca.entidade;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import br.com.ambientinformatica.util.AmbientValidator;
import br.com.ambientinformatica.util.Entidade;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "usuario_id", "papel" }) })
@NoArgsConstructor
public class PapelUsuario extends Entidade implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Getter
    @GeneratedValue(generator = "papel_usuario_seq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "papel_usuario_seq", sequenceName = "papel_usuario_seq", allocationSize = 1, initialValue = 1)
    private Integer id;

    @Getter
    @Setter
    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @NotNull(message = "Informe a data de cadastro.", groups = AmbientValidator.class)
    private Date dataCadastro = new Date();

    @Getter
    @Setter
    @Column(length = 255, nullable = false)
    @Enumerated(EnumType.STRING)
    @NotNull(message = "Informe o papel do usuário.", groups = AmbientValidator.class)
    private EnumPapel papel;

    public PapelUsuario(EnumPapel papel) {
        this.papel = papel;
    }

    public boolean isAdmin() {
        return (this.papel.equals(EnumPapel.ADMIN)) ? true : false;
    }

    public boolean isPodeConsultar() {
        return this.papel.equals(EnumPapel.ADMIN) || this.papel.equals(EnumPapel.GERENTE) ? true : false;
    }
}