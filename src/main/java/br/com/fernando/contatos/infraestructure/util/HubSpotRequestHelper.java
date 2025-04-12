package br.com.fernando.contatos.infraestructure.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

/**
 * Utilitário responsável por realizar requisições POST à API do HubSpot com política de retry.
 *
 * Esta classe implementa uma estratégia de reenvio automático em caso de erro HTTP 429 (Too Many Requests),
 * que ocorre quando o número de requisições excede o limite definido pelas políticas de *rate limiting* da API do HubSpot.
 *
 * Segundo a documentação oficial do HubSpot, ao receber esse status, o cliente deve aguardar antes de tentar novamente,
 * respeitando os intervalos recomendados para evitar bloqueios temporários.
 *
 * Essa abordagem garante maior resiliência e estabilidade nas integrações com o HubSpot,
 * tratando automaticamente picos de uso e reduzindo o risco de falhas por sobrecarga.
 *
 * Em caso de falha definitiva (após o número máximo de tentativas), uma exceção é lançada para tratamento adequado.
 *
 * Requisito solicitado explicitamente na implementação da integração.
 */


public class HubSpotRequestHelper {

    private static final Logger logger = LoggerFactory.getLogger(HubSpotRequestHelper.class);

    private static final int MAX_TENTATIVAS = 3;
    private static final long TEMPO_ESPERA_MS = 2000;

    public static void executarRequisicaoComRetry(String url, HttpEntity<?> request) {
        RestTemplate restTemplate = new RestTemplate();
        int tentativa = 0;

        while (tentativa < MAX_TENTATIVAS) {
            try {
                logger.info("Enviando requisição para HubSpot (tentativa {}/{}) - URL: {}", tentativa + 1, MAX_TENTATIVAS, url);
                restTemplate.postForEntity(url, request, String.class);
                return;

            } catch (HttpClientErrorException.TooManyRequests e) {
                tentativa++;
                logger.warn("Rate limit atingido (HTTP 429). Tentativa {}/{}. Aguardando {}ms antes de tentar novamente.",
                        tentativa, MAX_TENTATIVAS, TEMPO_ESPERA_MS);

                try {
                    Thread.sleep(TEMPO_ESPERA_MS);
                } catch (InterruptedException ex) {
                    logger.error("Thread interrompida durante espera por retry.", ex);
                    Thread.currentThread().interrupt();
                    throw new RuntimeException("Thread interrompida durante espera por retry.", ex);
                }

            } catch (HttpClientErrorException ex) {
                logger.error("Erro HTTP ao acessar HubSpot. Status: {}, Resposta: {}", ex.getStatusCode(), ex.getResponseBodyAsString());
                throw new RuntimeException("Erro HTTP ao acessar HubSpot: " + ex.getStatusCode(), ex);

            } catch (Exception ex) {
                logger.error("Erro inesperado ao enviar requisição para HubSpot: {}", ex.getMessage(), ex);
                throw new RuntimeException("Erro inesperado ao enviar requisição ao HubSpot", ex);
            }
        }

        logger.error("Número máximo de tentativas ({}) atingido. Requisição não foi bem-sucedida.", MAX_TENTATIVAS);
        throw new RuntimeException("Número máximo de tentativas atingido. Aguarde antes de tentar novamente.");
    }
}
