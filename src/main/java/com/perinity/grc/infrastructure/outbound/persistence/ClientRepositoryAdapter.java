package com.perinity.grc.infrastructure.outbound.persistence;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    @Override
    public Optional<Client> findById(String id) {
        return find("id", id).firstResultOptional()
                .map(this::toDomain);
    }


    @Override
    public List<Client> findAllClients() {
        return listAll().stream() 
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public boolean delete(String id) {
        return delete("id", id) > 0;
    }

    private Client toDomain(ClientEntity entity) {
        Client client = new Client();
        client.setId(entity.id);
        client.setFullName(entity.fullName);
        client.setMotherName(entity.motherName);
        client.setAddress(entity.address);
        client.setCpf(entity.cpf);
        client.setRg(entity.rg);
        client.setBirthDate(entity.birthDate);
        client.setPhoneNumber(entity.phoneNumber);
        client.setCreatedAt(entity.createdAt);
        client.setEmail(entity.email);
        return client;
    }
}