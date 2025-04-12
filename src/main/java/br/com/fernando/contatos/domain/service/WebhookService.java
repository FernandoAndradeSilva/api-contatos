package br.com.fernando.contatos.domain.service;

import java.util.List;
import java.util.Map;

public interface WebhookService {

    void handleWebhook(List<Map<String, Object>> events);


}
