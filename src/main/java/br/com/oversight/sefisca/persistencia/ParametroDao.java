package br.com.oversight.sefisca.persistencia;

import java.util.Map;

import br.com.ambientinformatica.jpa.exception.PersistenciaException;
import br.com.ambientinformatica.jpa.persistencia.Persistencia;
import br.com.oversight.sefisca.entidade.Parametro;

public interface ParametroDao extends Persistencia<Parametro>{

   Map<String, Object> listarMapaParametros() throws PersistenciaException;

   String consultarValorPorChave(String chave) throws PersistenciaException;
   
   String consultarValorParametroPorChave(String chave) throws PersistenciaException;

}
