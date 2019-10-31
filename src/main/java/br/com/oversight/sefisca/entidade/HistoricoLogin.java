package br.com.oversight.sefisca.entidade;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Getter;
import lombok.Setter;

@Entity
public class HistoricoLogin {

    @Id
    @Getter
    @GeneratedValue(generator="historicologin_seq", strategy=GenerationType.SEQUENCE)
    @SequenceGenerator(name="historicologin_seq", sequenceName="historicologin_seq", allocationSize=1, initialValue=1)
    private Long id;

    @Getter @Setter
    @ManyToOne
    private Usuario usuario;

    @Temporal(TemporalType.TIMESTAMP)
    private Date data;

    public Date getData() {
        return data != null ? (Date) data.clone(): null;
    }

    public void setData(Date data) {
        this.data = data != null ? (Date) data.clone(): null;
    }
}
