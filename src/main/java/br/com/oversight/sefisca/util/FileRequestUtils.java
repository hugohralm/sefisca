package br.com.oversight.sefisca.util;

import java.io.InputStream;
import java.util.Scanner;

@SuppressWarnings("resource")
public class FileRequestUtils {
	private static final String ESTABELECIMENTO_XML_PATH_FILE = "./datasusXML/estabelecimento_saude_request.xml";

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
	
	private static String getEstabelecimentoXmlRequest(String field, String match, String request) {
		ClassLoader classloader = Thread.currentThread().getContextClassLoader();
		InputStream file = classloader.getResourceAsStream(ESTABELECIMENTO_XML_PATH_FILE);
		Scanner s = new Scanner(file).useDelimiter("\\A");
		String cpfXmlRequest = s.hasNext() ? s.next() : "";
		cpfXmlRequest= replaceMatchParam(cpfXmlRequest, ESTABELECIMENTO_MATCH, request);
		return replaceMatchParam(cpfXmlRequest, match, field);
	}
	
	private static String replaceMatchParam(String text, String math, String replace) {
		return text.replace(math, replace);
	}
}
