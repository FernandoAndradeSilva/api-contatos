package br.com.fernando.contatos.application.controller;

import br.com.fernando.contatos.domain.service.ContactService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/hubspot")
public class ContactController {

    private static final Logger logger = LoggerFactory.getLogger(ContactController.class);

    private final ContactService contactService;

    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    /**
     * Endpoint responsável por criar um novo contato no HubSpot.
     */
    @PostMapping("/contacts")
    public ResponseEntity<?> createContact(@RequestBody Map<String, String> contato) {
        try {
            logger.info("Recebida requisição para criação de contato.");

            if (contato == null || contato.isEmpty()) {
                logger.warn("Dados do contato são nulos ou vazios.");
                return ResponseEntity.badRequest().body("Dados do contato não podem ser nulos ou vazios.");
            }

            logger.debug("Dados recebidos para contato corretamente");

            contactService.createContact(contato);

            logger.info("Contato criado com sucesso.");
            return ResponseEntity.status(HttpStatus.CREATED).build();

        } catch (Exception e) {
            logger.error("Erro ao processar a solicitação de criação de contato: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().body("Erro ao processar a solicitação: " + e.getMessage());
        }
    }
}
