package be.helha.api_recettapp.services;

import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;


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

    @Value("${keycloak.resource}")
    private String clientId;

    @Value("${keycloak.credentials.secret}")
    private String clientSecret;

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
                .realm(realm)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                .build();
    }

    /**
     * Disables (blocks) a user in Keycloak.
     *
     * This method fetches the user by his email from the specified realm and disables his account.
     *
     * @param email The email of the user to block.
     */
    public void blockUser(String email) {
        Keycloak keycloak = getKeycloakInstance();
        List<UserRepresentation> users = keycloak.realm(realm).users().search(null, null, null, email, null, null);
        if (!users.isEmpty()) {
            UserRepresentation user = users.get(0); // Email is unique in Keycloak Config so no problem
            user.setEnabled(false);
            keycloak.realm(realm).users().get(user.getId()).update(user);
        } else {
            throw new RuntimeException("User not found with email: " + email);
        }
    }

    /**
     * Enables (unblocks) a user in Keycloak.
     *
     * This method fetches the user by his email from the specified realm and enables his account.
     *
     * @param email The email of the user to unblock.
     */
    public void unblockUser(String email) {
        Keycloak keycloak = getKeycloakInstance();
        List<UserRepresentation> users = keycloak.realm(realm).users().search(null, null, null, email, null, null);
        if (!users.isEmpty()) {
            UserRepresentation user = users.get(0); // Email is unique in Keycloak Config so no problem
            user.setEnabled(true);
            keycloak.realm(realm).users().get(user.getId()).update(user);
        } else {
            throw new RuntimeException("User not found with email: " + email);
        }
    }

    /**
     * Deletes a user in Keycloak.
     *
     * This method fetches the user by their email from the specified realm and deletes their account.
     *
     * @param email The email of the user to delete.
     */
    public void deleteUser(String email) {
        Keycloak keycloak = getKeycloakInstance();
        List<UserRepresentation> users = keycloak.realm(realm).users().search(null, null, null, email, null, null);
        if (!users.isEmpty()) {
            UserRepresentation user = users.get(0); // Email is unique in Keycloak Config so no problem
            keycloak.realm(realm).users().get(user.getId()).remove();
        } else {
            throw new RuntimeException("User not found with email: " + email);
        }
    }
}

