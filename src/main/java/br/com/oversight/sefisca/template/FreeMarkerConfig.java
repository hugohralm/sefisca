package br.com.oversight.sefisca.template;

import freemarker.template.Configuration;

public class FreeMarkerConfig {

    private static final FreeMarkerConfig INSTANCE = new FreeMarkerConfig();

    private Configuration configuration;

    @SuppressWarnings("deprecation")
	private FreeMarkerConfig() {
        this.configuration = new Configuration();
        this.configuration.setDefaultEncoding("UTF-8");
        this.configuration.setClassForTemplateLoading(getClass(), "/templates/");
    }

    public static FreeMarkerConfig getInstace() {
        return INSTANCE;
    }

    public Configuration getConfiguration() {
        return configuration;
    }
}
