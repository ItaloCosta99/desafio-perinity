package com.perinity.grc.infrastructure.outbound.persistence;

import com.perinity.grc.application.domain.model.Client;
import com.perinity.grc.application.ports.output.ClientRepositoryPort;
import com.perinity.grc.infrastructure.outbound.persistence.entity.ClientEntity;
import io.quarkus.mongodb.panache.PanacheMongoRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ClientRepositoryAdapter implements ClientRepositoryPort, PanacheMongoRepository<ClientEntity> {

    @Override
    public Client save(Client client) {
        ClientEntity entity = new ClientEntity();
        entity.id = client.getId();
        entity.fullName = client.getFullName();
        entity.motherName = client.getMotherName();
        entity.address = client.getAddress();
        entity.cpf = client.getCpf();
        entity.rg = client.getRg();
        entity.birthDate = client.getBirthDate();
        entity.phoneNumber = client.getPhoneNumber();
        entity.createdAt = client.getCreatedAt();
        entity.email = client.getEmail();

        persistOrUpdate(entity);

        return client;
    }
}