package be.helha.api_recettapp.security;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * A custom converter to extract roles from a Keycloak JWT and convert them to Spring Security granted authorities.
 *
 * <p>This class implements the {@link Converter} interface to convert a {@link Jwt} object into a collection of
 * {@link GrantedAuthority} objects. It retrieves the roles from the "realm_access" claim in the JWT, prefixes them with "ROLE_",
 * and converts them to {@link SimpleGrantedAuthority} objects.</p>
 */
public class KeycloakRoleConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    /**
     * Converts a Keycloak JWT to a collection of Spring Security {@link GrantedAuthority} objects.
     *
     * <p>This method retrieves the "realm_access" claim from the JWT, extracts the roles, prefixes each role
     * with "ROLE_", and converts them to {@link SimpleGrantedAuthority} objects. If the "realm_access" claim
     * or the "roles" field within it is missing, an empty collection is returned.</p>
     *
     * @param jwt The {@link Jwt} object containing the user's claims.
     * @return A collection of {@link GrantedAuthority} objects representing the user's roles, or an empty list if no roles are found.
     */
    @Override
    public Collection<GrantedAuthority> convert(Jwt jwt) {
        // Extract "realm_access" claim from the JWT
        Map<String, Object> realmAccess = (Map<String, Object>) jwt.getClaims().get("realm_access");

        // Return an empty list if the "realm_access" claim or "roles" field is missing
        if (realmAccess == null || realmAccess.get("roles") == null) {
            return List.of();
        }

        // Extract roles and convert them to GrantedAuthority objects
        Collection<String> roles = (Collection<String>) realmAccess.get("roles");
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()))
                .collect(Collectors.toList());
    }
}
