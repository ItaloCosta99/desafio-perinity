package com.perinity.grc.application.service;

import com.perinity.grc.application.domain.model.Client;
import com.perinity.grc.application.ports.output.ClientRepositoryPort;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class ClientServiceTest {

    @Mock
    private ClientRepositoryPort repositoryPort;

    @InjectMocks
    private ClientService service;

    @Test
    void shouldCreateClientSuccessfully() {
        // Arrange
        Client newClient = new Client();
        newClient.setFullName("João Mário");
        newClient.setCpf("593.978.490-93");

        // Mocking the repository behavior
        Mockito.when(repositoryPort.save(any(Client.class))).thenAnswer(invocation -> {
            return invocation.getArgument(0);
        });

        // Act
        Client savedClient = service.create(newClient);

        // Assert
        Assertions.assertNotNull(savedClient.getId(), "Client ID should be auto-generated");
        Assertions.assertNotNull(savedClient.getCreatedAt(), "Created Date should be set");
        Assertions.assertEquals("João Mário", savedClient.getFullName());

        // Verify interaction
        Mockito.verify(repositoryPort, Mockito.times(1)).save(any(Client.class));
    }

    @Test
    void shouldReturnClient_WhenIdExists() {
        // Arrange
        String validId = "any-uuid";
        Client expectedClient = new Client();
        expectedClient.setId(validId);

        Mockito.when(repositoryPort.findClientById(validId))
                .thenReturn(java.util.Optional.of(expectedClient));

        // Act
        java.util.Optional<Client> result = service.findById(validId);

        // Assert
        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(validId, result.get().getId());
    }

    @Test
    void shouldReturnEmpty_WhenIdDoesNotExist() {
        // Arrange
        String invalidId = "invalid-uuid";
        Mockito.when(repositoryPort.findClientById(invalidId))
                .thenReturn(java.util.Optional.empty());

        // Act
        java.util.Optional<Client> result = service.findById(invalidId);

        // Assert
        Assertions.assertFalse(result.isPresent());
    }

    @Test
    void shouldReturnAllClients() {
        // Arrange
        Client client1 = new Client();
        client1.setId("id-1");
        Client client2 = new Client();
        client2.setId("id-2");
        java.util.List<Client> expectedClients = java.util.Arrays.asList(client1, client2);
        Mockito.when(repositoryPort.findAllClients())
                .thenReturn(expectedClients);

        // Act
        java.util.List<Client> result = service.findAll();

        // Assert
        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals("id-1", result.get(0).getId());
        Assertions.assertEquals("id-2", result.get(1).getId());
    }

    @Test
    void shouldReturnEmpty_WhenAllClientsIsEmpty() {
        // Arrange
        Mockito.when(repositoryPort.findAllClients())
                .thenReturn(java.util.Collections.emptyList());

        // Act
        java.util.List<Client> result = service.findAll();

        // Assert
        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    void shouldDeleteClient_WhenIdExists() {
        // Arrange
        String validId = "any-uuid";
        Mockito.when(repositoryPort.deleteClient(validId)).thenReturn(true);

        // Act
        boolean deleted = service.delete(validId);

        // Assert
        Assertions.assertTrue(deleted);
        Mockito.verify(repositoryPort, Mockito.times(1)).deleteClient(validId);
    }

    @Test
    void shouldUpdateClient_WhenIdExists() {
        // Arrange
        String id = "uuid-existente";
        Client existingClient = new Client();
        existingClient.setId(id);
        existingClient.setFullName("João Mário");
        existingClient.setCreatedAt(java.time.LocalDate.now().minusDays(10));
        Client dataToUpdate = new Client();
        dataToUpdate.setFullName("João da Silva");

        Mockito.when(repositoryPort.findClientById(id)).thenReturn(java.util.Optional.of(existingClient));
        Mockito.when(repositoryPort.save(any(Client.class))).thenAnswer(i -> i.getArgument(0));

        // Act
        Client updated = service.update(id, dataToUpdate);

        // Assert
        Assertions.assertEquals("João da Silva", updated.getFullName());
        Assertions.assertEquals(id, updated.getId());
        Assertions.assertNotNull(updated.getCreatedAt());
    }
}