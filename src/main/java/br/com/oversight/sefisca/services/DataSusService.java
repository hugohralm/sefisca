package br.com.oversight.sefisca.services;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.NodeList;

import br.com.ambientinformatica.util.UtilLog;
import br.com.oversight.sefisca.controle.dto.InstituicaoDTO;
import br.com.oversight.sefisca.controle.dto.ProfissionalDTO;
import br.com.oversight.sefisca.entidade.EnumUf;
import br.com.oversight.sefisca.persistencia.MunicipioDao;
import br.com.oversight.sefisca.util.FileRequestUtils;
import br.com.oversight.sefisca.util.UtilSefisca;

@Service
public class DataSusService {

	private static final String URL_ESTABELECIMENTO_SERVICE = "https://servicos.saude.gov.br/cnes/EstabelecimentoSaudeService/v1r0";
	private static final String URL_CNES_SERVICE = "https://servicos.saude.gov.br/cnes/CnesService/v1r0";

	@Autowired
	private MunicipioDao municipioDao;

	public InstituicaoDTO consultarEstabelecimentoSaude(String cnes, String cnpj)
			throws Exception {
		try {
			String envelope;
			if (cnes != null) {
				envelope = FileRequestUtils.getEstabelecimentoCNESXmlRequest(cnes);
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
	
	public ArrayList<ProfissionalDTO> consultarProfissionaisCnes(String cnes)
			throws Exception {
		try {
			String envelope = FileRequestUtils.getCnesXmlRequest(cnes);
			if (envelope != null) {
				SOAPMessage soapResponse = soapRequest(envelope, URL_CNES_SERVICE);
				ArrayList<ProfissionalDTO> profissionaisDTO = montarProfissionalDTO(soapResponse);
				return profissionaisDTO;
			}
		} catch (Exception e) {
			UtilLog.getLog().error(e.getMessage(), e);
		}
		return null;
	}

	private static SOAPMessage soapRequest(String envelope, String url)
			throws Exception {
		SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
		SOAPConnection soapConnection = soapConnectionFactory.createConnection();

		MimeHeaders headers = new MimeHeaders();
		headers.addHeader("Content-Type", "text/xml");

		MessageFactory messageFactory = MessageFactory.newInstance();

		SOAPMessage msg = messageFactory.createMessage(headers, (new ByteArrayInputStream(envelope.getBytes())));

		return soapConnection.call(msg, url);
	}

	public InstituicaoDTO montarInstituicaoDTO(SOAPMessage soapResponse) throws Exception {
		SOAPBody soapBody = soapResponse.getSOAPBody();
		InstituicaoDTO instituicaoDTO = new InstituicaoDTO();
		instituicaoDTO.setCnes(UtilSefisca.getElementsByTagNameXML(soapBody, "ns2:CodigoCNES"));
		instituicaoDTO.setCnpj(UtilSefisca.getElementsByTagNameXML(soapBody, "ns6:CNPJ"));
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
	
	private static ArrayList<ProfissionalDTO> montarProfissionalDTO(SOAPMessage soapResponse) throws Exception {
		ArrayList<ProfissionalDTO> profissionalDTOList = new ArrayList<>();
		SOAPBody soapBody = soapResponse.getSOAPBody();
		NodeList nodeListProfissional = soapBody.getElementsByTagName("ns39:profissional");
		for (int i = 0; i < nodeListProfissional.getLength(); i++) {
			NodeList nodeListProfissonalCampos = nodeListProfissional.item(i).getChildNodes();
			ProfissionalDTO profissionalDTO = new ProfissionalDTO();
			profissionalDTO.setNome(nodeListProfissonalCampos.item(1).getTextContent());
			profissionalDTO.setCpf(nodeListProfissonalCampos.item(3).getTextContent());
			profissionalDTO.setCns(nodeListProfissonalCampos.item(2).getTextContent());
			profissionalDTO.setCbo(nodeListProfissonalCampos.item(4).getChildNodes().item(0).getTextContent());
			profissionalDTO.setDataAtualizacao(nodeListProfissonalCampos.item(0).getTextContent());
			profissionalDTOList.add(profissionalDTO);
		}
		return profissionalDTOList;
	}
}