package br.com.fernando.contatos.application.controller;

import br.com.fernando.contatos.domain.service.WebhookService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/webhook")
public class WebhookController {

    private static final Logger logger = LoggerFactory.getLogger(WebhookController.class);

    private final WebhookService webhookService;

    @PostMapping("/hubspot")
    public ResponseEntity<String> handleWebhook(@RequestBody List<Map<String, Object>> payload) {
        logger.info("Recebendo webhook com payload: {}", payload);

        try {
            webhookService.handleWebhook(payload);
            logger.info("Webhook processado com sucesso.");
            return ResponseEntity.ok("Webhook recebido com sucesso!");
        } catch (Exception e) {
            logger.error("Erro ao processar webhook: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro ao processar webhook.");
        }
    }
}