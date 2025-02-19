package de.bund.bva.isyfact.task.test.config;

import de.bund.bva.isyfact.task.test.AccessManagerDummy;
import de.bund.bva.isyfact.util.spring.MessageSourceHolder;
import de.bund.bva.isyfact.aufrufkontext.AufrufKontextFactory;
import de.bund.bva.isyfact.aufrufkontext.AufrufKontextVerwalter;
import de.bund.bva.isyfact.aufrufkontext.impl.AufrufKontextVerwalterImpl;
import de.bund.bva.isyfact.sicherheit.Sicherheit;
import de.bund.bva.isyfact.sicherheit.accessmgr.AccessManager;
import de.bund.bva.isyfact.sicherheit.config.IsySicherheitConfigurationProperties;
import de.bund.bva.isyfact.sicherheit.impl.SicherheitImpl;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;

@Configuration
@EnableAutoConfiguration
public class TestConfig {

    @Bean
    public AccessManager accessManager() {
        return new AccessManagerDummy();
    }

    @Bean
    public Sicherheit sicherheit(AufrufKontextVerwalter aufrufKontextVerwalter,
        AufrufKontextFactory aufrufKontextFactory, AccessManager accessManager,
        IsySicherheitConfigurationProperties properties) {
        return new SicherheitImpl("/sicherheit/rollenrechte.xml", aufrufKontextVerwalter,
            aufrufKontextFactory, accessManager, properties);
    }

    @Bean
    public AufrufKontextVerwalter aufrufKontextVerwalter() {
        return new AufrufKontextVerwalterImpl();
    }

    @Bean
    public MessageSourceHolder messageSourceHolder() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasenames("resources/isy-task/nachrichten/ereignisse",
                "resources/isy-task/nachrichten/hinweise");

        MessageSourceHolder messageSourceHolder = new MessageSourceHolder();
        messageSourceHolder.setMessageSource(messageSource);

        return messageSourceHolder;
    }
}
