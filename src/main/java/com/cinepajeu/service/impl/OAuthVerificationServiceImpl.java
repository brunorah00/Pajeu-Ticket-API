package com.cinepajeu.service.impl;

import com.cinepajeu.entity.OAuthProvider;
import com.cinepajeu.exception.BusinessException;
import com.cinepajeu.service.OAuthUserInfo;
import com.cinepajeu.service.OAuthVerificationService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Service
@RequiredArgsConstructor
@Slf4j
public class OAuthVerificationServiceImpl implements OAuthVerificationService {

    private final ObjectMapper objectMapper;

    @Value("${app.oauth.google.client-id:}")
    private String googleClientId;

    @Value("${app.oauth.facebook.app-id:}")
    private String facebookAppId;

    @Value("${app.oauth.facebook.app-secret:}")
    private String facebookAppSecret;

    private RestClient restClient() {
        return RestClient.create();
    }

    @Override
    public OAuthUserInfo verify(OAuthProvider provider, String token) {
        return switch (provider) {
            case GOOGLE -> verifyGoogle(token);
            case FACEBOOK -> verifyFacebook(token);
        };
    }

    private OAuthUserInfo verifyGoogle(String idToken) {
        if (googleClientId == null || googleClientId.isBlank()) {
            throw new BusinessException("Login com Google não está configurado no servidor");
        }

        try {
            String body = restClient().get()
                    .uri("https://oauth2.googleapis.com/tokeninfo?id_token={token}", idToken)
                    .retrieve()
                    .body(String.class);

            JsonNode json = objectMapper.readTree(body);
            String aud = text(json, "aud");
            if (!googleClientId.equals(aud)) {
                throw new BusinessException("Token Google inválido para esta aplicação");
            }

            String subject = text(json, "sub");
            String email = text(json, "email");
            String nome = text(json, "name");
            if (subject == null || email == null) {
                throw new BusinessException("Token Google incompleto");
            }
            return new OAuthUserInfo(subject, email.toLowerCase(), nome != null ? nome : email);
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.warn("Falha ao validar token Google: {}", e.getMessage());
            throw new BusinessException("Token Google inválido ou expirado");
        }
    }

    private OAuthUserInfo verifyFacebook(String accessToken) {
        if (facebookAppId == null || facebookAppId.isBlank()
                || facebookAppSecret == null || facebookAppSecret.isBlank()) {
            throw new BusinessException("Login com Facebook não está configurado no servidor");
        }

        try {
            String appToken = facebookAppId + "|" + facebookAppSecret;
            String debugBody = restClient().get()
                    .uri("https://graph.facebook.com/debug_token?input_token={input}&access_token={app}",
                            accessToken, appToken)
                    .retrieve()
                    .body(String.class);

            JsonNode debug = objectMapper.readTree(debugBody);
            JsonNode data = debug.path("data");
            if (!data.path("is_valid").asBoolean(false)) {
                throw new BusinessException("Token Facebook inválido ou expirado");
            }
            String appIdFromToken = data.path("app_id").asText(null);
            if (!facebookAppId.equals(appIdFromToken)) {
                throw new BusinessException("Token Facebook inválido para esta aplicação");
            }

            String profileBody = restClient().get()
                    .uri("https://graph.facebook.com/me?fields=id,email,name&access_token={token}", accessToken)
                    .retrieve()
                    .body(String.class);

            JsonNode profile = objectMapper.readTree(profileBody);
            String subject = text(profile, "id");
            String email = text(profile, "email");
            String nome = text(profile, "name");
            if (subject == null || email == null) {
                throw new BusinessException("Não foi possível obter e-mail da conta Facebook. Verifique as permissões do app.");
            }
            return new OAuthUserInfo(subject, email.toLowerCase(), nome != null ? nome : email);
        } catch (BusinessException e) {
            throw e;
        } catch (RestClientException | java.io.IOException e) {
            log.warn("Falha ao validar token Facebook: {}", e.getMessage());
            throw new BusinessException("Token Facebook inválido ou expirado");
        }
    }

    private static String text(JsonNode node, String field) {
        JsonNode value = node.path(field);
        if (value.isMissingNode() || value.isNull()) {
            return null;
        }
        return value.asText();
    }
}
