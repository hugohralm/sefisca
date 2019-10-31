package br.com.oversight.sefisca.template;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class TemplateLoader {

    private TemplateLoader() {
        throw new IllegalStateException("Utility class");
    }

    public static String getText(String template, Object parametros) throws TemplateException, IOException {
        FreeMarkerConfig freeMarkerConfig = FreeMarkerConfig.getInstace();
        Configuration configuration = freeMarkerConfig.getConfiguration();
        Template template2 = configuration.getTemplate(template.concat(".ftlh"));
        Writer out = new StringWriter();
        template2.process(parametros, out);
        return out.toString();
    }
}
