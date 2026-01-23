package com.perinity.grc.application.ports.output;

import java.util.List;
import java.util.Optional;

import com.perinity.grc.application.domain.model.Client;

public interface ClientRepositoryPort {
    Client save(Client client);

    Optional<Client> findClientById(String id);

    List<Client> findAllClients();

    boolean deleteClient(String id);
}