package be.helha.api_recettapp.services;

import be.helha.api_recettapp.services.KeycloakUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.keycloak.admin.client.Keycloak;

import org.keycloak.admin.client.resource.*;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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
    private RoleMappingResource mockRoleMappingResource;
    private RoleScopeResource mockRoleScopeResource;


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
        mockRoleMappingResource = mock(RoleMappingResource.class);
        mockRoleScopeResource = mock(RoleScopeResource.class);


        // Mock Keycloak hierarchy
        when(mockKeycloak.realm(any())).thenReturn(mockRealmResource);
        when(mockRealmResource.users()).thenReturn(mockUsersResource);
        when(mockUsersResource.get(any())).thenReturn(mockUserResource);


        // Simulate that the roles() method returns a mock RoleMappingResource
        when(mockUserResource.roles()).thenReturn(mockRoleMappingResource);

        // Simulate that realmLevel() returns a mock RoleScopeResource
        when(mockRoleMappingResource.realmLevel()).thenReturn(mockRoleScopeResource);


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

    /**
     * Test case for the {@link KeycloakUserService#listUsers()} method.
     *
     * <p>This test verifies that the {@code listUsers} method correctly retrieves a list of users from the Keycloak
     * server and assigns roles to each user. The test mocks the necessary components (e.g., users and roles) and
     * verifies the interactions and the resulting data.</p>
     *
     * <p>The following steps are performed in this test:</p>
     * <ol>
     *     <li>Mock the creation of two users, {@code user1} and {@code user2}, each with a unique ID and username.</li>
     *     <li>Mock the roles for these users, specifically an "admin" role for {@code user1} and a "user" role for {@code user2}.</li>
     *     <li>Mock the response of Keycloak's user and roles resource to return these users and their associated roles.</li>
     *     <li>Invoke the {@code listUsers} method to retrieve the list of users along with their roles.</li>
     *     <li>Verify that the expected interactions with the mock resources occurred.</li>
     *     <li>Assert that the resulting user list contains the correct number of users and that each user has the expected roles.</li>
     * </ol>
     * @see KeycloakUserService#listUsers()
     */
    @Test
    void testListUsers() {
        // Mock user data
        UserRepresentation user1 = new UserRepresentation();
        user1.setId("user1-id");
        user1.setUsername("user1");

        UserRepresentation user2 = new UserRepresentation();
        user2.setId("user2-id");
        user2.setUsername("user2");

        // Mock roles
        RoleRepresentation adminRole = new RoleRepresentation();
        adminRole.setName("admin");

        RoleRepresentation userRole = new RoleRepresentation();
        userRole.setName("user");

        // Mock users list response
        when(mockUsersResource.list())
                .thenReturn(Arrays.asList(user1, user2));

        // Mock roles for user1
        when(mockUsersResource.get(eq("user1-id")))
                .thenReturn(mockUserResource);
        when(mockRoleScopeResource.listAll())
                .thenReturn(Collections.singletonList(adminRole))
                .thenReturn(Collections.singletonList(userRole));

        // Execute method
        List<UserRepresentation> users = keycloakUserService.listUsers();

        // Verify interactions
        verify(mockUsersResource).list();
        verify(mockUsersResource).get("user1-id");
        verify(mockUsersResource).get("user2-id");
        verify(mockRoleScopeResource, times(2)).listAll();

        // Verify results
        assertEquals(2, users.size());
        assertTrue(users.get(0).getAttributes().containsKey("roles"));
        assertTrue(users.get(1).getAttributes().containsKey("roles"));
        assertEquals(Collections.singletonList("admin"), users.get(0).getAttributes().get("roles"));
        assertEquals(Collections.singletonList("user"), users.get(1).getAttributes().get("roles"));
    }

}

