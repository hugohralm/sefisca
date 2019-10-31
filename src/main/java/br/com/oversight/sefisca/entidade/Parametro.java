package br.com.oversight.sefisca.entidade;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import org.hibernate.validator.constraints.Length;

import br.com.ambientinformatica.util.AmbientValidator;
import br.com.ambientinformatica.util.Entidade;
import lombok.Getter;
import lombok.Setter;

@Entity
public class Parametro extends Entidade implements Serializable {

   private static final long serialVersionUID = 1L;

   @Id
   @Getter @Setter
   private String chave;

   @Getter @Setter
   @Column(length=255, nullable=false)
   @Length(min=0, max=255, message="O limite do campo valor é de 255 caracteres.", groups=AmbientValidator.class)
   private String valor;

   @Override
   public Object getId() {
      return chave;
   }
}