package be.helha.api_recettapp;

import be.helha.api_recettapp.services.KeycloakUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.UserRepresentation;
import org.mockito.Mockito;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link KeycloakUserService}, focusing on the functionalities
 * of blocking and unblocking Keycloak users.
 *
 * <p>This class uses Mockito to mock Keycloak interactions and to verify the
 * behavior of the {@link KeycloakUserService#blockUser(String)} and
 * {@link KeycloakUserService#unblockUser(String)} methods.</p>
 */
class KeycloakUserServiceTest {

    private KeycloakUserService keycloakUserService;
    private Keycloak mockKeycloak;
    private RealmResource mockRealmResource;
    private UsersResource mockUsersResource;
    private UserResource mockUserResource;

    /**
     * Sets up the test environment by initializing and mocking the dependencies
     * required for testing {@link KeycloakUserService}.
     *
     * <p>This method creates mocks for {@link Keycloak} and its related resources,
     * establishes a hierarchy of mocked dependencies, and spies on the method that
     * retrieves the Keycloak instance.</p>
     */
    @BeforeEach
    void setUp() {
        // Initialize the service
        keycloakUserService = Mockito.spy(new KeycloakUserService());

        // Mock Keycloak and its related resources
        mockKeycloak = mock(Keycloak.class);
        mockRealmResource = mock(RealmResource.class);
        mockUsersResource = mock(UsersResource.class);
        mockUserResource = mock(UserResource.class);

        // Mock Keycloak hierarchy
        when(mockKeycloak.realm(any())).thenReturn(mockRealmResource);
        when(mockRealmResource.users()).thenReturn(mockUsersResource);
        when(mockUsersResource.get(any())).thenReturn(mockUserResource);

        // Spy on the method that creates Keycloak instances
        doReturn(mockKeycloak).when(keycloakUserService).getKeycloakInstance();
    }

    /**
     * Tests the {@link KeycloakUserService#blockUser(String)} method.
     *
     * <p>This test verifies that a user retrieved via Keycloak API is updated to
     * have the {@code enabled} field set to {@code false}, effectively blocking
     * the user.</p>
     */
    @Test
    void testBlockUser() {
        UserRepresentation user = new UserRepresentation();
        user.setId("test-user-id");
        user.setEnabled(true);

        when(mockUsersResource.search(isNull(), isNull(), isNull(), eq("test-email@example.com"), isNull(), isNull()))
                .thenReturn(Collections.singletonList(user));

        keycloakUserService.blockUser("test-email@example.com");

        verify(mockUsersResource).search(isNull(), isNull(), isNull(), eq("test-email@example.com"), isNull(), isNull());
        verify(mockUserResource).update(argThat(argument -> !argument.isEnabled()));
    }

    /**
     * Tests the {@link KeycloakUserService#unblockUser(String)} method.
     *
     * <p>This test verifies that a user retrieved via Keycloak API is updated to
     * have the {@code enabled} field set to {@code true}, effectively unblocking
     * the user.</p>
     */
    @Test
    void testUnblockUser() {
        UserRepresentation user = new UserRepresentation();
        user.setId("test-user-id");
        user.setEnabled(false);

        when(mockUsersResource.search(isNull(), isNull(), isNull(), eq("test-email@example.com"), isNull(), isNull()))
                .thenReturn(Collections.singletonList(user));

        keycloakUserService.unblockUser("test-email@example.com");

        verify(mockUsersResource).search(isNull(), isNull(), isNull(), eq("test-email@example.com"), isNull(), isNull());
        verify(mockUserResource).update(argThat(argument -> argument.isEnabled()));
    }

    /**
     * Tests the {@link KeycloakUserService#deleteUser(String)} method.
     *
     * <p>This test verifies that a user retrieved via Keycloak API is deleted
     * from the Keycloak server.</p>
     */
    @Test
    void testDeleteUser() {
        UserRepresentation user = new UserRepresentation();
        user.setId("test-user-id");

        when(mockUsersResource.search(isNull(), isNull(), isNull(), eq("test-email@example.com"), isNull(), isNull()))
                .thenReturn(Collections.singletonList(user));

        keycloakUserService.deleteUser("test-email@example.com");

        verify(mockUsersResource).search(isNull(), isNull(), isNull(), eq("test-email@example.com"), isNull(), isNull());
        verify(mockUserResource).remove();
    }
}

