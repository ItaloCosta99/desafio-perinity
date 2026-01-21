package com.perinity.grc.application.service;

import com.perinity.grc.application.domain.model.Client;
import com.perinity.grc.application.ports.output.ClientRepositoryPort;

import jakarta.enterprise.context.ApplicationScoped;

import java.time.LocalDate;
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
}