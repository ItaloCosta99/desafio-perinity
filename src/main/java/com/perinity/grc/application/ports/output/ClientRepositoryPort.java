package com.perinity.grc.application.ports.output;

import com.perinity.grc.application.domain.model.Client;

public interface ClientRepositoryPort {
    Client save(Client client);
}