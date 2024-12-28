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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class KeycloakUserServiceTest {

    private KeycloakUserService keycloakUserService;
    private Keycloak mockKeycloak;
    private RealmResource mockRealmResource;
    private UsersResource mockUsersResource;
    private UserResource mockUserResource;

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

    @Test
    void testBlockUser() {
        UserRepresentation user = new UserRepresentation();
        user.setEnabled(true);

        when(mockUserResource.toRepresentation()).thenReturn(user);

        keycloakUserService.blockUser("test-user-id");

        verify(mockUserResource).update(argThat(argument -> !argument.isEnabled()));
    }

    @Test
    void testUnblockUser() {
        UserRepresentation user = new UserRepresentation();
        user.setEnabled(false);

        when(mockUserResource.toRepresentation()).thenReturn(user);

        keycloakUserService.unblockUser("test-user-id");

        verify(mockUserResource).update(argThat(argument -> argument.isEnabled()));
    }
}

