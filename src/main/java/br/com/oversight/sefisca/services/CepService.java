package br.com.oversight.sefisca.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import org.primefaces.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.ambientinformatica.jpa.exception.PersistenciaException;
import br.com.ambientinformatica.jpa.exception.ValidacaoException;
import br.com.ambientinformatica.util.UtilLog;
import br.com.oversight.sefisca.controle.dto.ViaCEPDTO;
import br.com.oversight.sefisca.persistencia.MunicipioDao;

@Service
public class CepService {

    private static final String URL_VIACEP = "http://viacep.com.br/ws/";

    @Autowired
    private MunicipioDao municipioDao;

    public ViaCEPDTO consultarCep(String cepParam) throws ValidacaoException {
        try {
            String cep = cepParam.replace(".", "").replace("-", "");
            String url = String.format("%s%s/json/", URL_VIACEP, cep);
            JSONObject obj = new JSONObject(this.getCep(url));
            ViaCEPDTO viaCEPDTO = new ViaCEPDTO();
            if (!obj.has("erro")) {
                viaCEPDTO.setCep(obj.getString("cep"));
                viaCEPDTO.setLogradouro(obj.getString("logradouro"));
                viaCEPDTO.setComplemento(obj.getString("complemento"));
                viaCEPDTO.setBairro(obj.getString("bairro"));
                viaCEPDTO.setLocalidade(obj.getString("localidade"));
                viaCEPDTO.setUf(obj.getString("uf"));
                viaCEPDTO.setIbge(Integer.parseInt(obj.getString("ibge")));
                viaCEPDTO.setGia(obj.getString("gia"));
                viaCEPDTO
                        .setEnderecoCompleto(String.format("%s, %s", viaCEPDTO.getLogradouro(), viaCEPDTO.getBairro()));
                viaCEPDTO.setMunicipio(municipioDao.municipioPorCodigoIBGE(viaCEPDTO.getIbge()));

                return viaCEPDTO;
            }
        } catch (Exception e) {
            UtilLog.getLog().error(e.getMessage(), e);
        }
        return null;
    }

    private String getCep(String urlToRead) throws NumberFormatException, PersistenciaException {
        StringBuilder result = new StringBuilder();
        BufferedReader br = null;
        try {
            URL url = new URL(urlToRead);
            HttpURLConnection conn = null;

            conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("GET");
            br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            String line;
            while ((line = br.readLine()) != null) {
                result.append(line);
            }
        } catch (MalformedURLException | ProtocolException ex) {
            UtilLog.getLog().error(ex.getMessage(), ex);
        } catch (IOException ex) {
            UtilLog.getLog().error(ex.getMessage(), ex);
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    UtilLog.getLog().error(e.getMessage(), e);
                }
            }
        }
        return result.toString();
    }

}