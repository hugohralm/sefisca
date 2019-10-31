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
import br.com.oversight.sefisca.controle.dto.EstabelecimentoDTO;
import br.com.oversight.sefisca.persistencia.MunicipioDao;
import br.com.oversight.sefisca.util.UtilSefisca;

@Service
public class DataSusService {

	private static final String URL_ESTABELECIMENTO_SERVICE = "https://servicos.saude.gov.br/cnes/EstabelecimentoSaudeService/v1r0";

	@Autowired
	private MunicipioDao municipioDao;

	public EstabelecimentoDTO consultarEstabelecimentoSaude(String codigoCNES)
			throws SOAPException, IOException, TransformerFactoryConfigurationError, TransformerException {
		try {
			String envelope = getEnvelope(getBodyConsultarEstabelecimentoSaude(codigoCNES));
			SOAPMessage soapResponse = soapRequest(envelope, URL_ESTABELECIMENTO_SERVICE);

			EstabelecimentoDTO estabelecimentoDTO = montarEstabelecimentoDTO(soapResponse);
			return estabelecimentoDTO;
		} catch (Exception e) {
			UtilLog.getLog().error(e.getMessage(), e);
		}
		return null;
	}

	private String getEnvelope(String body) {
		StringBuilder envelope = new StringBuilder();
		envelope.append("<soap:Envelope\r\n");
		envelope.append("    xmlns:soap=\"http://www.w3.org/2003/05/soap-envelope\"\r\n");
		envelope.append("    xmlns:est=\"http://servicos.saude.gov.br/cnes/v1r0/estabelecimentosaudeservice\"\r\n");
		envelope.append("    xmlns:cnes=\"http://servicos.saude.gov.br/cnes/v1r0/cnesservice\"\r\n");
		envelope.append(
				"    xmlns:fil=\"http://servicos.saude.gov.br/wsdl/mensageria/v1r0/filtropesquisaestabelecimentosaude\"\r\n");
		envelope.append("    xmlns:cod=\"http://servicos.saude.gov.br/schema/cnes/v1r0/codigocnes\"\r\n");
		envelope.append(
				"    xmlns:cnpj=\"http://servicos.saude.gov.br/schema/corporativo/pessoajuridica/v1r0/cnpj\">\r\n");
		envelope.append(getHeader());
		envelope.append(body);
		envelope.append("</soap:Envelope>");
		return envelope.toString();
	}

	private String getBodyConsultarEstabelecimentoSaude(String codigoCNES) {
		StringBuilder body = new StringBuilder();
		body.append("    <soap:Body>\r\n");
		body.append("        <est:requestConsultarEstabelecimentoSaude>\r\n");
		body.append("            <fil:FiltroPesquisaEstabelecimentoSaude>\r\n");
		body.append("                <cod:CodigoCNES>\r\n");
		body.append("                    <cod:codigo>").append(codigoCNES).append("</cod:codigo>\r\n");
		body.append("                </cod:CodigoCNES>\r\n");
		body.append("            </fil:FiltroPesquisaEstabelecimentoSaude>\r\n");
		body.append("        </est:requestConsultarEstabelecimentoSaude>\r\n");
		body.append("    </soap:Body>\r\n");
		return body.toString();
	}

	private String getHeader() {
		StringBuilder header = new StringBuilder();
		header.append("    <soap:Header>\r\n");
		header.append("        <wsse:Security soap:mustUnderstand=\"true\"\r\n");
		header.append(
				"            xmlns:wsse=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd\"\r\n");
		header.append(
				"            xmlns:wsu=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd\">\r\n");
		header.append("            <wsse:UsernameToken wsu:Id=\"UsernameToken-5FCA58BED9F27C406E14576381084652\">\r\n");
		header.append("                <wsse:Username>CNES.PUBLICO</wsse:Username>\r\n");
		header.append(
				"                <wsse:Password Type=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#PasswordText\">cnes#2015public</wsse:Password>\r\n");
		header.append("            </wsse:UsernameToken>\r\n");
		header.append("        </wsse:Security>\r\n");
		header.append("    </soap:Header>\r\n");
		return header.toString();
	}
	
	private static SOAPMessage soapRequest(String envelope, String url)
			throws SOAPException, IOException, TransformerFactoryConfigurationError, TransformerException {
		SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
		SOAPConnection soapConnection = soapConnectionFactory.createConnection();

		MimeHeaders headers = new MimeHeaders();
		headers.addHeader("Content-Type", "text/xml");

		MessageFactory messageFactory = MessageFactory.newInstance();

		SOAPMessage msg = messageFactory.createMessage(headers, (new ByteArrayInputStream(envelope.getBytes())));
		System.out.println("Request SOAP:");
		msg.writeTo(System.out);
		System.out.println("\n");

		return soapConnection.call(msg, url);
	}
	
	

	public EstabelecimentoDTO montarEstabelecimentoDTO(SOAPMessage soapResponse) throws SOAPException {
		SOAPBody soapBody = soapResponse.getSOAPBody();
		EstabelecimentoDTO estabelecimentoDTO = new EstabelecimentoDTO();
		estabelecimentoDTO.setCodigoCNES(UtilSefisca.getElementsByTagNameXML(soapBody, "ns2:CodigoCNES"));
		estabelecimentoDTO.setNomeEmpresarial(UtilSefisca.getElementsByTagNameXML(soapBody, "ns25:nomeEmpresarial"));
		estabelecimentoDTO.setNomeFantasia(UtilSefisca.getElementsByTagNameXML(soapBody, "ns25:nomeFantasia"));
		estabelecimentoDTO.setTipoUnidade(UtilSefisca.getElementsByTagNameXML(soapBody, "ns28:descricao"));
		estabelecimentoDTO.setLogradouro(UtilSefisca.getElementsByTagNameXML(soapBody, "ns11:nomeLogradouro"));
		estabelecimentoDTO.setNumero(UtilSefisca.getElementsByTagNameXML(soapBody, "ns11:numero"));
		estabelecimentoDTO.setComplemento(UtilSefisca.getElementsByTagNameXML(soapBody, "ns11:complemento"));
		estabelecimentoDTO.setBairro(UtilSefisca.getElementsByTagNameXML(soapBody, "ns11:Bairro"));
		estabelecimentoDTO.setCep(UtilSefisca.getElementsByTagNameXML(soapBody, "ns11:CEP"));
		//Integer codigoIBGE = Integer.parseInt(UtilSefisca.getElementsByTagNameXML(soapBody, "ns15:codigoMunicipio"));
		//estabelecimentoDTO.setMunicipio(municipioDao.municipioPorCodigoIBGE(codigoIBGE));
		estabelecimentoDTO.setUf(UtilSefisca.getElementsByTagNameXML(soapBody, "ns16:siglaUF"));
		estabelecimentoDTO.setLatitude(UtilSefisca.getElementsByTagNameXML(soapBody, "ns30:latitude"));
		estabelecimentoDTO.setLongitude(UtilSefisca.getElementsByTagNameXML(soapBody, "ns30:longitude"));
		estabelecimentoDTO.setGeoJson(UtilSefisca.getElementsByTagNameXML(soapBody, "ns30:geoJson"));
		return estabelecimentoDTO;
	}
}