package co.com.crediyaauthentication.model.auth.gateways;

import java.util.List;
import java.util.Map;

public interface TokenProviderPort {
    String generateToken(String subject, List<String> roles, Map<String, Object> extraClaims);
    String generateToken(String subject, List<String> roles);
    boolean validateToken(String token);
    String extractSubject(String token);
    List<String> extractRoles(String token);
}
