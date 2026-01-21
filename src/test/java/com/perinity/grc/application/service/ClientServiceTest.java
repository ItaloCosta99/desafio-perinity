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
}