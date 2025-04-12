package br.com.fernando.contatos.application.token;


import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.atomic.AtomicReference;

/** COMENTÁRIOS SOBRE AS RECOMANDAÇÕES DE SEGURANÇA DA API
 * Classe responsável por armazenar o token de acesso em memória de forma segura e controlada.
 * Evita exposição acidental em logs ou uso de campos públicos.
 */

@Getter
@Setter
public class TokenStore {

    private static final AtomicReference<String> accessTokenRef = new AtomicReference<>();

    private TokenStore() {
        // Evita instância
    }

    public static void setAccessToken(String token) {
        if (token != null && !token.isBlank()) {
            accessTokenRef.set(token);
        }
    }

    public static String getAccessToken() {
        return accessTokenRef.get();
    }

    public static boolean isTokenAvailable() {
        String token = accessTokenRef.get();
        return token != null && !token.isBlank();
    }

    public static void clear() {
        accessTokenRef.set(null);
    }
}
