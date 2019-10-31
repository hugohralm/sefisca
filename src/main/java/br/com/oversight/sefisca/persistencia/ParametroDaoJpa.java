package br.com.oversight.sefisca.persistencia;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import br.com.ambientinformatica.jpa.exception.PersistenciaException;
import br.com.ambientinformatica.jpa.exception.ValidacaoException;
import br.com.ambientinformatica.jpa.persistencia.PersistenciaJpa;
import br.com.ambientinformatica.util.UtilLog;
import br.com.oversight.sefisca.entidade.Parametro;

@Repository("parametroDao")
public class ParametroDaoJpa extends PersistenciaJpa<Parametro> implements ParametroDao{

	private static final long serialVersionUID = 1L;

	private static Map<String, String> parametros = new HashMap<>();

	public synchronized void carregarParametros() throws PersistenciaException {
		try {
			List<Parametro> params = listar();
			parametros = new HashMap<String, String>();
			for (Parametro p : params) {
				parametros.put(p.getChave(), p.getValor());
			}
		} catch (Exception e) {
			UtilLog.getLog().error(e.getMessage(), e);
			throw new PersistenciaException("Erro ao listar mapa de parâmetros",e);
		}
	}

	@Override
	public Map<String, Object> listarMapaParametros() throws PersistenciaException{

		Map<String, Object> parametros = new HashMap<>();
		try {
			List<Parametro> parans = super.listar();
			if (parans.isEmpty()) {
				for(Parametro pa : parans){
					parametros.put(pa.getChave(), pa.getValor());
				}
			}else{
				return new HashMap<>();
			}
			return parametros;
		} catch (Exception e) {
			UtilLog.getLog().error(e.getMessage(), e);
			throw new PersistenciaException("Erro ao listar mapa de parametros",e);
		}
	}

	@Override
	public String consultarValorPorChave(String chave) throws PersistenciaException{
		if(chave == null || chave.isEmpty()){
			throw new ValidacaoException("Parâmetro não informado");
		}

		List<String> lista = new ArrayList<>();
		try {
			TypedQuery<String> query = em.createQuery("select p.valor from Parametro p where p.chave = :chave", String.class);
			query.setParameter("chave", chave);
			lista = query.getResultList();
		} catch (Exception e) {
			UtilLog.getLog().error(e.getMessage(), e);
			throw new PersistenciaException(e.getMessage(),e);
		}

		if(lista.isEmpty()){
			throw new ValidacaoException(String.format("Paramêtro %s não encontrado", chave));
		}
		if(lista.size() > 1){
			throw new ValidacaoException(String.format("Foram encontrados mais de um parâmetro para essa chave: %s", chave));
		}
		return lista.get(0);
	}

	@Override
	public String consultarValorParametroPorChave(String chave) throws PersistenciaException{
		String valor = null;

		if(chave == null || chave.isEmpty()){
			throw new ValidacaoException("Parâmetro não informado");
		}

		try {
			if(parametros == null || parametros.isEmpty()){
				carregarParametros();
			}
			valor = parametros.get(chave);
		} catch (Exception e) {
			UtilLog.getLog().error(e.getMessage(), e);
			throw new PersistenciaException(e.getMessage(),e);
		}

		if(valor == null || valor.isEmpty()){
			throw new ValidacaoException(String.format("Paramêtro %s não encontrado", chave));
		}

		return valor;
	}

}
