package br.com.fernando.contatos.infraestructure.service.impl;

import br.com.fernando.contatos.domain.service.ContactService;
import br.com.fernando.contatos.application.token.TokenStore;
import br.com.fernando.contatos.infraestructure.util.HubSpotRequestHelper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class ContactServiceImpl implements ContactService {

    private static final Logger logger = LoggerFactory.getLogger(ContactServiceImpl.class);
    private static final String HUBSPOT_CONTACTS_URL = "https://api.hubapi.com/crm/v3/objects/contacts";

    @Override
    public void createContact(Map<String, String> contato) {
        logger.info("Iniciando criação de contato no HubSpot.");

        if (!TokenStore.isTokenAvailable()) {
            logger.error("Token de acesso não está disponível.");
            throw new IllegalStateException("Token de acesso não disponível.");
        }

        String accessToken = TokenStore.getAccessToken();
        logger.debug("Token de acesso recuperado com sucesso.");

        HttpEntity<Map<String, Object>> request = buildRequestEntity(contato, accessToken);
        logger.debug("Requisição para criação de contato construída: {}", request.getBody());

        HubSpotRequestHelper.executarRequisicaoComRetry(HUBSPOT_CONTACTS_URL, request);
        logger.info("Requisição enviada ao HubSpot para criação de contato.");
    }

    private HttpEntity<Map<String, Object>> buildRequestEntity(Map<String, String> contato, String token) {
        logger.debug("Montando corpo da requisição com os dados do contato...");

        Map<String, Object> properties = new HashMap<>();
        properties.put("email", contato.get("email"));
        properties.put("firstname", contato.get("firstname"));
        properties.put("lastname", contato.get("lastname"));

        Map<String, Object> payload = new HashMap<>();
        payload.put("properties", properties);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);

        return new HttpEntity<>(payload, headers);
    }
}
