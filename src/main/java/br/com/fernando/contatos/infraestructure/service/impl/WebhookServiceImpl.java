package br.com.fernando.contatos.infraestructure.service.impl;

import br.com.fernando.contatos.domain.service.WebhookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class WebhookServiceImpl implements WebhookService {

    private static final Logger logger = LoggerFactory.getLogger(WebhookServiceImpl.class);

    @Override
    public void handleWebhook(List<Map<String, Object>> events) {
        logger.info("Iniciando o processamento do webhook com {} eventos.", events.size());

        for (Map<String, Object> event : events) {
            try {
                String eventType = (String) event.get("subscriptionType");
                long objectId = Long.parseLong(event.get("objectId").toString());

                if ("contact.creation".equals(eventType)) {
                    logger.info("Evento de criação de contato recebido. ID do contato: {}", objectId);
                } else {
                    logger.warn("Tipo de evento não suportado: {}", eventType);
                }
            } catch (Exception e) {
                logger.error("Erro ao processar evento: {}", event, e);
            }
        }

        logger.info("Processamento do webhook concluído.");
    }
}