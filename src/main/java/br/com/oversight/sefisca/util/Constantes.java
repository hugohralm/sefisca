package br.com.oversight.sefisca.util;

import br.com.ambientinformatica.ambientjsf.util.UtilFaces;

public interface Constantes {

    String AMBIENTE = System.getProperty("ambiente");
    int TAMANHO_MAXIMO_TITULO = 48;
    int TAMANHO_MAXIMO_DESCRICAO = 376;

    interface Sistema {
        String NOME = "Sefisca";
        String CONTEXTO = "sefisca";
        String URL_BASE = UtilFaces.getRequest().getScheme() + "://" + UtilFaces.getRequest().getHeader("Host");
        String URL_SISTEMA = URL_BASE + "/" + CONTEXTO;
        String URL_INICIO = "inicio.ovs";
        String AUTH_TOKEN = System.getProperty("sdl.auth-token");
    }
}
