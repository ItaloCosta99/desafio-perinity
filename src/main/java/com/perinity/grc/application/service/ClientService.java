package com.perinity.grc.application.service;

import com.perinity.grc.application.domain.model.Client;
import com.perinity.grc.application.ports.output.ClientRepositoryPort;

import jakarta.enterprise.context.ApplicationScoped;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class ClientService {

    private final ClientRepositoryPort repository;

    public ClientService(ClientRepositoryPort repository) {
        this.repository = repository;
    }

    public Client create(Client client) {
        if (client.getId() == null || client.getId().isEmpty()) {
            client.setId(UUID.randomUUID().toString());
        }

        if (client.getCreatedAt() == null) {
            client.setCreatedAt(LocalDate.now());
        }

        return repository.save(client);
    }

    public Optional<Client> findById(String id) {
        return repository.findById(id);
    }

    public List<Client> findAll() {
        return repository.findAllClients();
    }

    public boolean delete(String id) {
        return repository.delete(id);
    }

    public Client update(String id, Client clientWithNewData) {
        return repository.findById(id)
            .map(existingClient -> {
                existingClient.setFullName(clientWithNewData.getFullName());
                existingClient.setMotherName(clientWithNewData.getMotherName());
                existingClient.setAddress(clientWithNewData.getAddress());
                existingClient.setCpf(clientWithNewData.getCpf());
                existingClient.setRg(clientWithNewData.getRg());
                existingClient.setBirthDate(clientWithNewData.getBirthDate());
                existingClient.setPhoneNumber(clientWithNewData.getPhoneNumber());
                existingClient.setEmail(clientWithNewData.getEmail());
                
                
                return repository.save(existingClient);
            })
            .orElseThrow(() -> new IllegalArgumentException("Client not found")); 
    }
}