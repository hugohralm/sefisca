package br.com.oversight.sefisca.services;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.ambientinformatica.util.UtilLog;
import br.com.oversight.sefisca.controle.dto.InstituicaoDTO;
import br.com.oversight.sefisca.entidade.EnumUf;
import br.com.oversight.sefisca.persistencia.MunicipioDao;
import br.com.oversight.sefisca.util.FileRequestUtils;
import br.com.oversight.sefisca.util.UtilSefisca;

@Service
public class DataSusService {

	private static final String URL_ESTABELECIMENTO_SERVICE = "https://servicos.saude.gov.br/cnes/EstabelecimentoSaudeService/v1r0";

	@Autowired
	private MunicipioDao municipioDao;

	public InstituicaoDTO consultarEstabelecimentoSaude(String codigoCNES, String cnpj)
			throws SOAPException, IOException, TransformerFactoryConfigurationError, TransformerException {
		try {
			String envelope;
			if (codigoCNES != null) {
				envelope = FileRequestUtils.getEstabelecimentoCNESXmlRequest(codigoCNES);
			} else {
				envelope = FileRequestUtils.getEstabelecimentoCNPJXmlRequest(cnpj);
			}
			if (envelope != null) {
				SOAPMessage soapResponse = soapRequest(envelope, URL_ESTABELECIMENTO_SERVICE);
				InstituicaoDTO instituicaoDTO = montarInstituicaoDTO(soapResponse);
				return instituicaoDTO;
			}
		} catch (Exception e) {
			UtilLog.getLog().error(e.getMessage(), e);
		}
		return null;
	}

	private static SOAPMessage soapRequest(String envelope, String url)
			throws SOAPException, IOException, TransformerFactoryConfigurationError, TransformerException {
		SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
		SOAPConnection soapConnection = soapConnectionFactory.createConnection();

		MimeHeaders headers = new MimeHeaders();
		headers.addHeader("Content-Type", "text/xml");

		MessageFactory messageFactory = MessageFactory.newInstance();

		SOAPMessage msg = messageFactory.createMessage(headers, (new ByteArrayInputStream(envelope.getBytes())));

		return soapConnection.call(msg, url);
	}

	public InstituicaoDTO montarInstituicaoDTO(SOAPMessage soapResponse) throws SOAPException {
		SOAPBody soapBody = soapResponse.getSOAPBody();
		InstituicaoDTO instituicaoDTO = new InstituicaoDTO();
		instituicaoDTO.setCodigoCNES(UtilSefisca.getElementsByTagNameXML(soapBody, "ns2:CodigoCNES"));
		instituicaoDTO.setRazaoSocial(UtilSefisca.getElementsByTagNameXML(soapBody, "ns25:nomeEmpresarial"));
		instituicaoDTO.setNomeFantasia(UtilSefisca.getElementsByTagNameXML(soapBody, "ns25:nomeFantasia"));
		instituicaoDTO.setTipoUnidade(UtilSefisca.getElementsByTagNameXML(soapBody, "ns28:descricao"));
		instituicaoDTO.setLogradouro(UtilSefisca.getElementsByTagNameXML(soapBody, "ns11:nomeLogradouro"));
		instituicaoDTO.setNumero(UtilSefisca.getElementsByTagNameXML(soapBody, "ns11:numero"));
		instituicaoDTO.setComplemento(UtilSefisca.getElementsByTagNameXML(soapBody, "ns11:complemento"));
		instituicaoDTO.setBairro(UtilSefisca.getElementsByTagNameXML(soapBody, "ns11:Bairro"));
		instituicaoDTO.setCep(UtilSefisca.getElementsByTagNameXML(soapBody, "ns11:CEP"));
		EnumUf uf = EnumUf.valueOf(UtilSefisca.getElementsByTagNameXML(soapBody, "ns16:siglaUF"));
		String municipio = UtilSefisca.getElementsByTagNameXML(soapBody, "ns15:nomeMunicipio");
		instituicaoDTO.setMunicipio(municipioDao.listarPorUfNome(uf, municipio).get(0));
		instituicaoDTO.setLatitude(UtilSefisca.getElementsByTagNameXML(soapBody, "ns30:latitude"));
		instituicaoDTO.setLongitude(UtilSefisca.getElementsByTagNameXML(soapBody, "ns30:longitude"));
		instituicaoDTO.setGeoJson(UtilSefisca.getElementsByTagNameXML(soapBody, "ns30:geoJson"));
		return instituicaoDTO;
	}
}