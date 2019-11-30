package br.com.oversight.sefisca.entidade;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;

import br.com.ambientinformatica.util.Entidade;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class Assunto extends Entidade implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Getter
    private Integer id;

    

}
