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
import br.com.oversight.sefisca.util.UtilSefisca;

@Service
public class DataSusService {

	private static final String URL_ESTABELECIMENTO_SERVICE = "https://servicos.saude.gov.br/cnes/EstabelecimentoSaudeService/v1r0";

	@Autowired
	private MunicipioDao municipioDao;

	public InstituicaoDTO consultarEstabelecimentoSaude(String codigoCNES)
			throws SOAPException, IOException, TransformerFactoryConfigurationError, TransformerException {
		try {
			String envelope = getEnvelope(getBodyConsultarEstabelecimentoSaude(codigoCNES));
			SOAPMessage soapResponse = soapRequest(envelope, URL_ESTABELECIMENTO_SERVICE);

			InstituicaoDTO instituicaoDTO = montarInstituicaoDTO(soapResponse);
			return instituicaoDTO;
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
		envelope.append("    <soap:Header>\r\n");
		envelope.append("        <wsse:Security soap:mustUnderstand=\"true\"\r\n");
		envelope.append(
				"            xmlns:wsse=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd\"\r\n");
		envelope.append(
				"            xmlns:wsu=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd\">\r\n");
		envelope.append(
				"            <wsse:UsernameToken wsu:Id=\"UsernameToken-5FCA58BED9F27C406E14576381084652\">\r\n");
		envelope.append("                <wsse:Username>CNES.PUBLICO</wsse:Username>\r\n");
		envelope.append(
				"                <wsse:Password Type=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#PasswordText\">cnes#2015public</wsse:Password>\r\n");
		envelope.append("            </wsse:UsernameToken>\r\n");
		envelope.append("        </wsse:Security>\r\n");
		envelope.append("    </soap:Header>\r\n");
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
		//Integer codigoIBGE = Integer.parseInt(UtilSefisca.getElementsByTagNameXML(soapBody, "ns15:codigoMunicipio"));
		EnumUf uf = EnumUf.valueOf(UtilSefisca.getElementsByTagNameXML(soapBody, "ns16:siglaUF"));
		String municipio = UtilSefisca.getElementsByTagNameXML(soapBody, "ns15:nomeMunicipio");
		instituicaoDTO.setMunicipio(municipioDao.listarPorUfNome(uf, municipio).get(0));
		instituicaoDTO.setLatitude(UtilSefisca.getElementsByTagNameXML(soapBody, "ns30:latitude"));
		instituicaoDTO.setLongitude(UtilSefisca.getElementsByTagNameXML(soapBody, "ns30:longitude"));
		instituicaoDTO.setGeoJson(UtilSefisca.getElementsByTagNameXML(soapBody, "ns30:geoJson"));
		return instituicaoDTO;
	}
}