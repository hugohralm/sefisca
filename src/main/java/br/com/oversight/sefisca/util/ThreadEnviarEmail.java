package br.com.oversight.sefisca.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class ThreadEnviarEmail {

    private static ExecutorService executorService = Executors.newFixedThreadPool(5);

    public static void enviarEmail(final Mensagem mensagem) {
        RunnableEnviarEmail runnableEnviarEmail;
        runnableEnviarEmail = new RunnableEnviarEmail(mensagem);
        executorService.execute(runnableEnviarEmail);
    }

}
