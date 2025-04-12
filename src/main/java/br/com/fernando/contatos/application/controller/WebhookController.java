package br.com.fernando.contatos.application.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/webhook")
public class WebhookController {

    @PostMapping("/hubspot")
    public ResponseEntity<String> handleWebhook(@RequestBody List<Map<String, Object>> payload) {
        for (Map<String, Object> event : payload) {
            String eventType = (String) event.get("subscriptionType");
            Long objectId = Long.valueOf(event.get("objectId").toString());

            if ("contact.creation".equals(eventType)) {
                System.out.println("Webhook recebido: Novo contato criado no HubSpot. ID: " + objectId);
                // Aqui você pode chamar a API do HubSpot para buscar dados detalhados do contato
            }
        }

        return ResponseEntity.ok("Webhook recebido com sucesso!");
    }


}
