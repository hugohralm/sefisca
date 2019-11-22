package br.com.oversight.sefisca.entidade;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

import org.hibernate.validator.constraints.Length;

import br.com.ambientinformatica.util.AmbientValidator;
import br.com.ambientinformatica.util.Entidade;
import lombok.Getter;
import lombok.Setter;

@Entity
public class EnderecoInstituicao extends Entidade implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "endereco_instituicao_seq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "endereco_instituicao_seq", sequenceName = "endereco_instituicao_seq", allocationSize = 1, initialValue = 1)
    @Getter
    private Long id;

    @Getter
    @Setter
    @Column(nullable = true, length = 255)
    @Length(min = 0, max = 255, message = "O limite do campo endereço é de 255 caracteres.", groups = AmbientValidator.class)
    private String logradouro;

    @Getter
    @Setter
    @ManyToOne(optional = true)
    private Municipio municipio;

    @Getter
    @Setter
    @Column(nullable = true)
    private String cep;

    @Getter
    @Setter
    @Column(length = 255, nullable = true)
    @Length(min = 0, max = 255, message = "O limite do campo bairro é de 255 caracteres.", groups = AmbientValidator.class)
    private String bairro;

    @Getter
    @Setter
    @Column(length = 20, nullable = true)
    @Length(min = 0, max = 20, message = "O limite do campo número é de 20 caracteres.", groups = AmbientValidator.class)
    private String numero;

    @Getter
    @Setter
    @Column(length = 512)
    @Length(min = 0, max = 512, message = "O limite do campo complemento é de 512 caracteres.", groups = AmbientValidator.class)
    private String complemento;

    @Getter
    @Setter
    private String longitude;

    @Getter
    @Setter
    private String latitude;

    public EnderecoInstituicao() {
        super();
    }

    public EnderecoInstituicao(String logradouro, Municipio municipio, String cep, String bairro, String numero) {
        super();
        this.logradouro = logradouro;
        this.municipio = municipio;
        this.cep = cep;
        this.bairro = bairro;
        this.numero = numero;
    }

    @Override
    public String toString() {
        return String.format("%s, %s, %s, %s - %s", this.logradouro, this.numero,
                this.complemento != null && !this.complemento.isEmpty() ? this.complemento + "" : "", this.bairro,
                this.municipio);
    }
}