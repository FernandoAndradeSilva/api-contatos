package br.com.fernando.contatos.domain.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HubSpotToken {

    // Getters e Setters
    private String accessToken;
    private String refreshToken;
    private Long expiresIn;
    private String tokenType;

    public HubSpotToken(String accessToken, String refreshToken, Long expiresIn, String tokenType) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.expiresIn = expiresIn;
        this.tokenType = tokenType;
    }

}
