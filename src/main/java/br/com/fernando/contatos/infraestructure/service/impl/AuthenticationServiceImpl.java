package br.com.fernando.contatos.infraestructure.service.impl;

import br.com.fernando.contatos.infraestructure.config.HubSpotProperties;
import br.com.fernando.contatos.domain.model.HubSpotToken;
import br.com.fernando.contatos.domain.service.AuthenticationService;
import br.com.fernando.contatos.application.token.TokenStore;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@RequiredArgsConstructor
@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationServiceImpl.class);

    private final RestTemplate restTemplate;
    private final HubSpotProperties hubSpotProperties;

    @Override
    public String generateAuthorizationUrl() {
        return this.buildAuthorizationUrl();
    }

    @Override
    public void handleOAuthCallback(String code) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", hubSpotProperties.getClientId());
        body.add("client_secret", hubSpotProperties.getClientSecret());
        body.add("redirect_uri", hubSpotProperties.getRedirectUri());
        body.add("code", code);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(
                    hubSpotProperties.getTokenUrl(),
                    request,
                    String.class);

            HubSpotToken token = parseTokenFromJson(response.getBody());
            TokenStore.setAccessToken(token.getAccessToken());
            logger.info("Access token armazenado com sucesso");

        } catch (Exception e) {
            logger.error("Erro ao trocar o código de autorização pelo token: {}", e.getMessage(), e);
            throw new RuntimeException("Erro na autenticação com o HubSpot", e);

        }
    }

    private String buildAuthorizationUrl() {
        return UriComponentsBuilder.fromHttpUrl(hubSpotProperties.getAuthUrl())
                .queryParam("client_id", hubSpotProperties.getClientId())
                .queryParam("redirect_uri", hubSpotProperties.getRedirectUri())
                .queryParam("scope", hubSpotProperties.getScope())
                .queryParam("response_type", "code")
                .build()
                .toUriString();
    }

    private HubSpotToken parseTokenFromJson(String jsonString) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode json = mapper.readTree(jsonString);

        return new HubSpotToken(
                json.get("access_token").asText(),
                json.has("refresh_token") ? json.get("refresh_token").asText() : null,
                json.get("expires_in").asLong(),
                json.get("token_type").asText()
        );
    }

}
