package br.com.oversight.sefisca.util;

import javax.servlet.ServletContext;

import org.springframework.web.context.support.WebApplicationContextUtils;

public class FabricaObjetoSpring {

	private static ServletContext context;
	
	public static Object criarObjeto(String nome){
		return WebApplicationContextUtils.getWebApplicationContext(context).getBean(nome);
	}

	public static ServletContext getContext() {
		return context;
	}

	public static void setContext(ServletContext context) {
		FabricaObjetoSpring.context = context;
	}
	
}
