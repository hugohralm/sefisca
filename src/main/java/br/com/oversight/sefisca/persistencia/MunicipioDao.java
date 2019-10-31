package br.com.oversight.sefisca.persistencia;

import java.util.List;

import br.com.ambientinformatica.jpa.exception.PersistenciaException;
import br.com.ambientinformatica.jpa.persistencia.Persistencia;
import br.com.oversight.sefisca.entidade.EnumUf;
import br.com.oversight.sefisca.entidade.Municipio;


public interface MunicipioDao extends Persistencia<Municipio>{
    
    List<Municipio> listarPorUfNome(EnumUf uf, String descricao) throws PersistenciaException;
    
    Municipio municipioPorCodigoIBGE(Integer codigoIbge) throws PersistenciaException;
    
}
