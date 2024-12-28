package be.helha.api_recettapp.services;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


/**
 * Service class for managing Keycloak users.
 *
 * This service interacts with the Keycloak Admin API to perform administrative operations
 * such as blocking and unblocking users in a specified realm.
 * It uses {@code @Service} annotation to mark this class as a Spring service component.
 */
@Service
public class KeycloakUserService {

    @Value("${keycloak.auth-server-url}")
    private String serverUrl;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.client-id}")
    private String clientId;

    @Value("${keycloak.client-secret}")
    private String clientSecret;

    @Value("${keycloak.admin-username}")
    private String adminUsername;

    @Value("${keycloak.admin-password}")
    private String adminPassword;

    /**
     * Creates and configures a {@link Keycloak} instance for interacting with the Keycloak Admin API.
     *
     * The Keycloak instance is initialized with the admin user's credentials and connects to
     * the specified server and realm.
     *
     * @return A {@link Keycloak} instance.
     */
    public Keycloak getKeycloakInstance() {
        return KeycloakBuilder.builder()
                .serverUrl(serverUrl)
                .realm("master") // The realm where the admin is configured
                .clientId(clientId)
                .clientSecret(clientSecret)
                .username(adminUsername)
                .password(adminPassword)
                .build();
    }

    /**
     * Disables (blocks) a user in Keycloak.
     *
     * This method fetches the user by their ID from the specified realm and disables their account.
     *
     * @param userId The ID of the user to block.
     */
    public void blockUser(String userId) {
        Keycloak keycloak = getKeycloakInstance();
        UserRepresentation user = keycloak.realm(realm).users().get(userId).toRepresentation();
        user.setEnabled(false);
        keycloak.realm(realm).users().get(userId).update(user);
    }

    /**
     * Enables (unblocks) a user in Keycloak.
     *
     * This method fetches the user by their ID from the specified realm and enables their account.
     *
     * @param userId The ID of the user to unblock.
     */
    public void unblockUser(String userId) {
        Keycloak keycloak = getKeycloakInstance();
        UserRepresentation user = keycloak.realm(realm).users().get(userId).toRepresentation();
        user.setEnabled(true);
        keycloak.realm(realm).users().get(userId).update(user);
    }
}

