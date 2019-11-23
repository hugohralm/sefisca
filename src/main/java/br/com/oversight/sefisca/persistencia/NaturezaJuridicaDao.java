package br.com.oversight.sefisca.persistencia;

import java.util.List;

import br.com.ambientinformatica.jpa.exception.PersistenciaException;
import br.com.ambientinformatica.jpa.persistencia.Persistencia;
import br.com.oversight.sefisca.entidade.NaturezaJuridica;

public interface NaturezaJuridicaDao extends Persistencia<NaturezaJuridica> {
    List<NaturezaJuridica> naturezaJuridicaPorCodigo(String codigo) throws PersistenciaException;
}
