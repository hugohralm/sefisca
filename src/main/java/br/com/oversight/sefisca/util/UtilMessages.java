package br.com.oversight.sefisca.util;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;

public class UtilMessages {

	private static FacesContext context;

	private UtilMessages() {
	}

	private static FacesContext getContext() {
		return FacesContext.getCurrentInstance();
	}

	/**
	 * Dispara uma notificação para o client, especificando o tipo, titulo e mensagem.
	 * 
	 * @param severity : Enum do FacesMessage que especifica o tipo da notificação,
	 *                 valores aceitos: SEVERITY_INFO, SEVERITY_WARN,
	 *                 SEVERITY_ERROR, SEVERITY_FATAL.
	 * @param title    : Titulo da notificação, valores aceitos: String, null.
	 * @param message  : Mensagem da notificação, valores aceitos: String.
	 */
	public static void addMessage(Severity severity, String title, String message) {
		if (message != null)
			getContext().addMessage(null, new FacesMessage(severity, title, message));
	}

	public static void addMessage(String title, String message) {
		addMessage(FacesMessage.SEVERITY_INFO, title, message);
	}
	
	public static void addMessage( String message) {
		addMessage(FacesMessage.SEVERITY_INFO, null, message);
	}
	
	public static void addMessage(Severity severity, String message) {
		addMessage(severity, null, message);
	}
	
	public static void addMessage(Exception exception) {
		addMessage(FacesMessage.SEVERITY_WARN, "Erro!", exception.getMessage());
	}
}
