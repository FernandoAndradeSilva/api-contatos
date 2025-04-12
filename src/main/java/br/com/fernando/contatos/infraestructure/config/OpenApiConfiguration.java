package br.com.fernando.contatos.infraestructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfiguration {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de Integração com HubSpot")
                        .version("1.0")
                        .description("Esta API realiza a integração com o HubSpot para autenticação OAuth 2.0 e criação de contatos."));
    }
}
