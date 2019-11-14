package br.com.oversight.sefisca.util;

import java.io.InputStream;
import java.util.Scanner;

@SuppressWarnings("resource")
public class FileRequestUtils {
	private static final String ESTABELECIMENTO_XML_PATH_FILE = "./datasusXML/estabelecimento_saude_request.xml";
	private static final String CNES_XML_PATH_FILE = "./datasusXML/cnes_service_request.xml";

	private static final String ESTABELECIMENTO_MATCH = "[[FIELD_REQUEST]]";
	private static final String CNES_MATCH = "[[FIELD_CNES]]";
	private static final String CNPJ_MATCH = "[[FIELD_CNPJ]]";

	public static String getEstabelecimentoCNESXmlRequest(String cnes) {
		String request = "<cod:CodigoCNES><cod:codigo>[[FIELD_CNES]]</cod:codigo></cod:CodigoCNES>";
		return getEstabelecimentoXmlRequest(cnes, CNES_MATCH, request);
	}
	
	public static String getEstabelecimentoCNPJXmlRequest(String cnpj) {
		String request = "<cnpj:CNPJ><cnpj:numeroCNPJ>[[FIELD_CNPJ]]</cnpj:numeroCNPJ></cnpj:CNPJ>";
		return getEstabelecimentoXmlRequest(cnpj, CNPJ_MATCH, request);
	}
	
	public static String getCnesXmlRequest(String cnes) {
		String cpfXmlRequest = getXmlRequest(CNES_XML_PATH_FILE);
		return replaceMatchParam(cpfXmlRequest, CNES_MATCH, cnes);
	}
	
	private static String getEstabelecimentoXmlRequest(String field, String match, String request) {
		String cpfXmlRequest = getXmlRequest(ESTABELECIMENTO_XML_PATH_FILE);
		cpfXmlRequest= replaceMatchParam(cpfXmlRequest, ESTABELECIMENTO_MATCH, request);
		return replaceMatchParam(cpfXmlRequest, match, field);
	}
	
	private static String getXmlRequest(String pathFile) {
		ClassLoader classloader = Thread.currentThread().getContextClassLoader();
		InputStream file = classloader.getResourceAsStream(pathFile);
		Scanner s = new Scanner(file).useDelimiter("\\A");
		return s.hasNext() ? s.next() : "";
	}
	
	private static String replaceMatchParam(String text, String math, String replace) {
		return text.replace(math, replace);
	}
}
