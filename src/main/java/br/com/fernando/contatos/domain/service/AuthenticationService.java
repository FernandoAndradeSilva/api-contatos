package br.com.fernando.contatos.domain.service;

import org.springframework.stereotype.Service;

@Service
public interface AuthenticationService {

    String generateAuthorizationUrl();

    void handleOAuthCallback(String code);
}
