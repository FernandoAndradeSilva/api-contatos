package br.com.fernando.contatos.application.controller;

import br.com.fernando.contatos.domain.service.AuthenticationService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/oauth")
public class AuthenticationController {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

    private final AuthenticationService authenticationService;

    /**
     * Gera e retorna a URL de autorização do HubSpot para iniciar o fluxo OAuth.
     */
    @GetMapping("/authorize")
    public ResponseEntity<?> generateAuthorizationUrl() {


        try {
            logger.info("Requisição recebida para gerar URL de autorização do HubSpot.");
            String url = authenticationService.generateAuthorizationUrl();
            logger.debug("URL de autorização gerada com sucesso");
            return ResponseEntity.ok(url);
        } catch (Exception e) {
            logger.error("Erro ao gerar URL de autorização: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro ao gerar URL de autorização: " + e.getMessage());
        }
    }

    /**
     * Manipula o callback do OAuth e troca o código pelo access token.
     */
    @GetMapping("/callback")
    public ResponseEntity<?> handleOAuthCallback(@RequestParam String code)  {



        try {
            logger.info("Callback OAuth recebido com código com sucesso");
            authenticationService.handleOAuthCallback(code);

            // VISANDO SEGURANÇA, O TOKEN NÃO É RETORNADO NA RESPOSTA
            logger.info("Token de acesso armazenado com sucesso.");


            return ResponseEntity.ok("Autorização concluída! Token armazenado com sucesso.");
        } catch (Exception e) {
            logger.error("Erro ao processar callback OAuth: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro ao processar callback: " + e.getMessage());
        }
    }
}
