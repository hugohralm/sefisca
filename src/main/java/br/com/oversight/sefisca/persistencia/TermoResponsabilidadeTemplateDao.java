package br.com.oversight.sefisca.persistencia;

import br.com.ambientinformatica.jpa.exception.PersistenciaException;
import br.com.ambientinformatica.jpa.persistencia.Persistencia;
import br.com.oversight.sefisca.entidade.TermoResponsabilidadeTemplate;

public interface TermoResponsabilidadeTemplateDao extends Persistencia<TermoResponsabilidadeTemplate> {

    TermoResponsabilidadeTemplate consultarUltimo() throws PersistenciaException;

    boolean isEditavel(TermoResponsabilidadeTemplate template) throws PersistenciaException;
    
    void validarEditavel(TermoResponsabilidadeTemplate template) throws PersistenciaException;
}
