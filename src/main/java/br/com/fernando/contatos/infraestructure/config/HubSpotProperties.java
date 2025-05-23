package br.com.fernando.contatos.infraestructure.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "hubspot")
public class HubSpotProperties {
    private String clientId;
    private String clientSecret;
    private String redirectUri;
    private String scope;
    private String authUrl;
    private String tokenUrl;
}
